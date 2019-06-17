package org.nypl.pdf.android.pdfviewer

import android.content.Context
import android.database.DataSetObserver
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import kotlinx.android.synthetic.main.fragment_table_of_contents.*
import org.nypl.pdf.android.api.TableOfContentsFragmentListenerType
import org.nypl.pdf.android.api.TableOfContentsItem
import org.slf4j.LoggerFactory


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [TableOfContentsFragmentListenerType] interface
 * to handle interaction events.
 * Use the [TableOfContentsFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
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
            // TODO: Could do any initialization we wanted here and pass in params if needed.
            return TableOfContentsFragment()
        }
    }

    private val log = LoggerFactory.getLogger(PDFViewerFragment::class.java)

    private var readerTOCLayout: View? = null

    private var inflater: LayoutInflater? = null
    private lateinit var listener: TableOfContentsFragmentListenerType
    private lateinit var titleTextView: TextView
    private var tableOfContentsList: List<TableOfContentsItem> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        this.log.debug("onCreate")
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
        // Inflate the layout for this fragment
        this.inflater = inflater
        readerTOCLayout = inflater.inflate(R.layout.fragment_table_of_contents, container, false)

        tableOfContentsList = this.listener.onTableOfContentsWantsItems()

        return readerTOCLayout
    }

    private fun wrapTableOfContentsList(tableOfContentsList: List<TableOfContentsItem>): List<TableOfContentsItemWrapper> {
        var wrappedList: MutableList<TableOfContentsItemWrapper> = mutableListOf()
        var indent = 0

        for (tocItem in tableOfContentsList) {
            // wrappedList.add(TableOfContentsItemWrapper(TableOfContentsItem, indent))
            flattenTableOfContents(wrappedList, indent, tocItem)
        }

        return wrappedList
    }

    private fun flattenTableOfContents(
        wrappedList: MutableList<TableOfContentsItemWrapper>,
        indent: Int,
        tocItem: TableOfContentsItem
    ) {
        // Add current element
        wrappedList.add(TableOfContentsItemWrapper(tocItem, indent))

        // Call children if needed
        for (child in tocItem.children)
            flattenTableOfContents(wrappedList, indent + 1, child)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        this.log.debug("onViewCreated")
        super.onViewCreated(view, savedInstanceState)

        this.titleTextView = view.findViewById(R.id.toc_title_textView)
        this.titleTextView.text = this.listener.onTableOfContentsWantsTitle()
        // TODO: Handle hud colors? this.titleTextView.setTextColor(Color.BLUE)

        // RecyclerView Setup
        val elements = wrapTableOfContentsList(tableOfContentsList)

        var recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)

        recyclerView.adapter = TableOfContentsAdapter(elements, listener)
    }
}
