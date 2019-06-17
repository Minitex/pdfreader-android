package org.nypl.pdf.android.pdfviewer

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.github.barteksc.pdfviewer.PDFView
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle
import com.github.barteksc.pdfviewer.util.FitPolicy
import com.shockwave.pdfium.PdfDocument
import org.nypl.pdf.android.api.PdfFragmentListenerType
import org.nypl.pdf.android.api.TableOfContentsItem
import org.slf4j.LoggerFactory

class PDFViewerFragment : Fragment(), OnPageChangeListener, OnLoadCompleteListener {


    companion object {
        /**
         * Factory method to create new instance of
         * this fragment using provided params.
         *
         * @return A new instance of fragment PDFViewerFragment.
         */
        @JvmStatic
        fun newInstance(): PDFViewerFragment {
            // TODO: Could do any initialization we wanted here and pass in params if needed.
            return PDFViewerFragment()
        }
    }

    private val log = LoggerFactory.getLogger(PDFViewerFragment::class.java)

    private lateinit var listener: PdfFragmentListenerType
    private lateinit var titleTextView: TextView
    private lateinit var pdfView: PDFView
    private lateinit var tocImage: ImageView


    /**
     * Fires when viewer has its page changed by the user
     * (or presumably code as well).
     */
    override fun onPageChanged(page: Int, pageCount: Int) {
        this.listener.onReaderPageChanged(page)
    }

    /**
     * Fires when viewer has completed loading the document.
     */
    override fun loadComplete(nbPages: Int) {
        // table of contents and other file metadata not available until after load is complete
        var convertedTableOfContents = convertToStandardTableOfContents(this.pdfView.tableOfContents)
        this.listener.onReaderLoadedTableOfContents(convertedTableOfContents)
    }

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
            throw RuntimeException(context.toString() + " must implement PdfFragmentListnerType")
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
        // TODO: Handle hud colors? this.titleTextView.setTextColor(Color.BLUE)

        this.tocImage = view.findViewById(R.id.reader_toc)
        // TODO: Handle hud colors? this.tocImage.setColorFilter(Color.BLACK)
        this.tocImage.setOnClickListener {
            this.listener.onReaderWantsToCFragment()
        }

        this.pdfView = view.findViewById(R.id.pdfView)
        displayFromInputStream()
    }

    private fun displayFromInputStream() {
        var inputStream = this.listener.onReaderWantsInputStream()

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

    private fun convertToStandardTableOfContents(tableOfContents: List<PdfDocument.Bookmark>): List<TableOfContentsItem> {
        var convertedTableOfContents: MutableList<TableOfContentsItem> = mutableListOf()

        for (contentItem in tableOfContents) {
            convertedTableOfContents.add(
                TableOfContentsItem(
                    contentItem.title,
                    contentItem.pageIdx.toInt(),
                    convertToStandardTableOfContents(contentItem.children)
                )
            )
        }

        return convertedTableOfContents.toList()
    }
}