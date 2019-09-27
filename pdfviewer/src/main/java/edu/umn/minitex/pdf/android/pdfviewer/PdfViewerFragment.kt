package edu.umn.minitex.pdf.android.pdfviewer

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.github.barteksc.pdfviewer.PDFView
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle
import com.github.barteksc.pdfviewer.util.FitPolicy
import com.shockwave.pdfium.PdfDocument
import edu.umn.minitex.pdf.android.api.PdfFragmentListenerType
import edu.umn.minitex.pdf.android.api.TableOfContentsItem
import edu.umn.minitex.pdf.android.pdfviewer.R
import org.slf4j.LoggerFactory
import java.io.InputStream

/**
 * [Fragment] subclass for displaying PDF documents rendered with
 * the PdfViewer library.
 * Activities that contain this fragment must implement the
 * [PdfFragmentListenerType] interface
 * to handle interaction events.
 * Use the [PdfViewerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PdfViewerFragment : Fragment(), OnPageChangeListener, OnLoadCompleteListener {

    companion object {
        /**
         * Factory method to create new instance of
         * this fragment using provided params.
         *
         * @return A new instance of fragment [PdfViewerFragment].
         */
        @JvmStatic
        fun newInstance(): PdfViewerFragment {
            // Could move Fragment initialization here instead of listeners if wanted.
            return PdfViewerFragment()
        }
    }

    private val log = LoggerFactory.getLogger(PdfViewerFragment::class.java)

    private lateinit var listener: PdfFragmentListenerType
    private lateinit var titleTextView: TextView
    private lateinit var pdfView: PDFView
    private lateinit var tocImage: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        this.log.debug("onCreate")
        super.onCreate(savedInstanceState)
    }

    override fun onAttach(context: Context) {
        this.log.debug("onAttach")
        super.onAttach(context)
        if (context is PdfFragmentListenerType) {
            listener = context
        } else {
            throw RuntimeException("$context must implement PdfFragmentListenerType")
        }
    }

    override fun onResume() {
        this.log.debug("onResume")
        super.onResume()

        pdfView.jumpTo(listener.onReaderWantsCurrentPage())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        this.log.debug("onCreateView")
        return inflater.inflate(R.layout.fragment_pdf_viewer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        this.log.debug("onViewCreated")
        super.onViewCreated(view, savedInstanceState)

        this.titleTextView = view.findViewById(R.id.title_textView)
        this.titleTextView.text = this.listener.onReaderWantsTitle()
        // To customize color: this.titleTextView.setTextColor(Color.BLUE)

        this.tocImage = view.findViewById(R.id.reader_toc)
        // To customize color: this.tocImage.setColorFilter(Color.BLACK)
        this.tocImage.setOnClickListener {
            this.listener.onReaderWantsTableOfContentsFragment()
        }

        this.pdfView = view.findViewById(R.id.pdfView)
        displayFromInputStream()
    }

    /**
     * Fires when viewer has its page changed by the user
     * (or presumably code as well).
     */
    override fun onPageChanged(page: Int, pageCount: Int) {
        log.debug("onPageChanged. page: $page pageCount: $pageCount")
        this.listener.onReaderPageChanged(page)
    }

    /**
     * Fires when viewer has completed loading the document.
     */
    override fun loadComplete(pageCount: Int) {
        log.debug("PdfViewer loadComplete. pageCount: $pageCount")

        // table of contents and other file metadata not available until after load is complete
        val convertedTableOfContents = convertToStandardTableOfContents(this.pdfView.tableOfContents)
        this.listener.onReaderLoadedTableOfContents(convertedTableOfContents)
    }

    /**
     * Requests the [InputStream] from the host activity and loads it into the [PDFView].
     */
    private fun displayFromInputStream() {
        val inputStream = this.listener.onReaderWantsInputStream()

        pdfView.fromStream(inputStream)
            .defaultPage(this.listener.onReaderWantsCurrentPage())
            .enableSwipe(true)
            .swipeHorizontal(true)
            .pageSnap(true)
            .pageFling(true)
            .autoSpacing(true)
            .onPageChange(this)
            .onLoad(this)
            .enableAnnotationRendering(true)
            .scrollHandle(DefaultScrollHandle(context))
            .pageFitPolicy(FitPolicy.BOTH)
            .load()
    }

    /**
     * Converts list of [PdfDocument.Bookmark] to [ArrayList] of [TableOfContentsItem] defined by the api package.
     */
    private fun convertToStandardTableOfContents(tableOfContents: List<PdfDocument.Bookmark>): ArrayList<TableOfContentsItem> {
        val convertedTableOfContents: MutableList<TableOfContentsItem> = mutableListOf()

        for (contentItem in tableOfContents) {
            convertedTableOfContents.add(
                TableOfContentsItem(
                    contentItem.title,
                    contentItem.pageIdx.toInt(),
                    convertToStandardTableOfContents(contentItem.children)
                )
            )
        }

        return ArrayList(convertedTableOfContents.toList())
    }
}