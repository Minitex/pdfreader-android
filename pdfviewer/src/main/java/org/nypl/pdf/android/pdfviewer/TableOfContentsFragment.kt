package org.nypl.pdf.android.pdfviewer

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import org.nypl.pdf.android.api.TableOfContentsFragmentListenerType
import org.nypl.pdf.android.api.TableOfContentsItem
import org.slf4j.LoggerFactory

/**
 * [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [TableOfContentsFragmentListenerType] interface
 * to handle interaction events.
 * Use the [TableOfContentsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TableOfContentsFragment : Fragment() {

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment TableOfContentsFragment.
         */
        @JvmStatic
        fun newInstance(): TableOfContentsFragment {
            // Could move Fragment initialization here instead of listeners if wanted.
            return TableOfContentsFragment()
        }
    }

    private val log = LoggerFactory.getLogger(PdfViewerFragment::class.java)

    private lateinit var listener: TableOfContentsFragmentListenerType
    private lateinit var titleTextView: TextView

    private var tableOfContentsLayout: View? = null
    private var inflater: LayoutInflater? = null
    private var tableOfContentsList: List<TableOfContentsItem> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        log.debug("onCreate")
        super.onCreate(savedInstanceState)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is TableOfContentsFragmentListenerType) {
            listener = context
        } else {
            throw RuntimeException("$context must implement TableOfContentsFragmentListenerType")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        log.debug("onCreateView")
        // Inflate the layout for this fragment
        this.inflater = inflater
        tableOfContentsLayout = inflater.inflate(R.layout.fragment_table_of_contents, container, false)

        tableOfContentsList = listener.onTableOfContentsWantsItems()

        return tableOfContentsLayout
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        log.debug("onViewCreated")
        super.onViewCreated(view, savedInstanceState)

        titleTextView = view.findViewById(R.id.toc_title_textView)
        titleTextView.text = listener.onTableOfContentsWantsTitle()
        // To customize color: this.titleTextView.setTextColor(Color.BLUE)

        // RecyclerView Setup
        val elements = wrapTableOfContentsList(tableOfContentsList)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        val noDataView = view.findViewById<TextView>(R.id.table_of_contents_no_data)

        if (elements.isNotEmpty()) {
            noDataView.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE

            recyclerView.layoutManager = LinearLayoutManager(context)
            recyclerView.adapter = TableOfContentsAdapter(elements, listener)
        } else {
            recyclerView.visibility = View.GONE
            noDataView.visibility = View.VISIBLE

            noDataView.text = listener.onTableOfContentsWantsEmptyDataText()
        }
    }

    /**
     * Converts list of [TableOfContentsItem] to [TableOfContentsItemWrapper] for display in a RecyclerView
     */
    private fun wrapTableOfContentsList(tableOfContentsList: List<TableOfContentsItem>): List<TableOfContentsItemWrapper> {
        val wrappedList: MutableList<TableOfContentsItemWrapper> = mutableListOf()
        val indent = 0

        for (tocItem in tableOfContentsList) {
            flattenTableOfContents(wrappedList, indent, tocItem)
        }

        return wrappedList
    }

    /**
     * Flattens list with children to a single-dimensional list and indent value.
     */
    private fun flattenTableOfContents(
        wrappedList: MutableList<TableOfContentsItemWrapper>,
        indent: Int,
        tocItem: TableOfContentsItem
    ) {
        // Add current element
        wrappedList.add(TableOfContentsItemWrapper(tocItem, indent))

        // Recursively call with an incremented indent value
        for (child in tocItem.children) {
            flattenTableOfContents(wrappedList, indent + 1, child)
        }
    }
}
