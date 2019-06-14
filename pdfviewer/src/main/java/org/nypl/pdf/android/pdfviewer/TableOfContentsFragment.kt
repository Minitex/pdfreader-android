package org.nypl.pdf.android.pdfviewer

import android.content.Context
import android.database.DataSetObserver
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
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
 * [TableOfContentsFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [TableOfContentsFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class TableOfContentsFragment : Fragment(), ListAdapter {

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
    private var readerTOCListView: ListView? = null

    private var inflater: LayoutInflater? = null
    private var adapter: ArrayAdapter<TableOfContentsItemWrapper>? = null

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
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        this.inflater = inflater
        readerTOCLayout = inflater.inflate(R.layout.fragment_table_of_contents, container, false)
        readerTOCListView = readerTOCLayout?.findViewById(R.id.reader_toc_contents_list)

        tableOfContentsList = this.listener.onTableOfContentsWantsItems()
        val elements = wrapTableOfContentsList(tableOfContentsList)

        adapter = ArrayAdapter(context!!, 0, elements)
        readerTOCListView?.adapter = this

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
        for(child in tocItem.children)
        flattenTableOfContents(wrappedList, indent + 1, child)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        this.log.debug("onViewCreated")
        super.onViewCreated(view, savedInstanceState)

        this.titleTextView = view.findViewById(R.id.toc_title_textView)
        this.titleTextView.text = this.listener.onTableOfContentsWantsTitle()
        // TODO: Handle hud colors? this.titleTextView.setTextColor(Color.BLUE)
    }

    /**
     * List View Adapter
     */
    override fun areAllItemsEnabled(): Boolean {
        return adapter!!.areAllItemsEnabled()
    }

    override fun getCount(): Int {
        return adapter!!.count
    }

    override fun getItem(position: Int): Any {
        return adapter!!.getItem(position)
    }

    override fun getItemId(position: Int): Long {
        return adapter!!.getItemId(position)
    }

    override fun getItemViewType(position: Int): Int {
        return adapter!!.getItemViewType(position)
    }

    override fun getView(position: Int, reuse: View?, parent: ViewGroup?): View {

        val itemView = if (reuse != null) {
            reuse as ViewGroup
        } else {
            inflater?.inflate(R.layout.table_of_contents_element, parent, false) as ViewGroup
        }

        val titleTextView = itemView.findViewById<TextView>(R.id.reader_toc_element_title)
        val pageNumberTextView = itemView.findViewById<TextView>(R.id.reader_toc_element_page_number)

        // Populate Title and Page Number
        val element = adapter?.getItem(position)
        titleTextView.text = element?.title ?: "Title Marker"
        pageNumberTextView.text = element?.pageNumber?.toString() ?: "Page Marker"

        val p = RelativeLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        // Set the left margin based on the desired indentation level.
        val leftIndent = if (element != null) { element.indent * 64 } else { 0 }
        p.setMargins(leftIndent, 0, 0, 0)
        titleTextView.layoutParams = p

        itemView.setOnClickListener { _ ->
            this.listener.onTOCItemSelected(element?.pageNumber ?: 0)
        }

        return itemView
    }

    override fun getViewTypeCount(): Int {
        return adapter!!.viewTypeCount
    }

    override fun hasStableIds(): Boolean {
        return adapter!!.hasStableIds()
    }

    override fun isEmpty(): Boolean {
        return adapter!!.isEmpty
    }

    override fun isEnabled(position: Int): Boolean {
        return adapter!!.isEnabled(position)
    }

    override fun registerDataSetObserver(observer: DataSetObserver?) {
        adapter!!.registerDataSetObserver(observer)
    }

    override fun unregisterDataSetObserver(observer: DataSetObserver?) {
        adapter!!.unregisterDataSetObserver(observer)
    }
}
