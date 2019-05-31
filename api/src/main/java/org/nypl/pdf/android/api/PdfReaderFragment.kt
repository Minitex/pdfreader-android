package org.nypl.pdf.android.api

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.github.barteksc.pdfviewer.PDFView
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle
import com.github.barteksc.pdfviewer.util.FitPolicy
import org.slf4j.LoggerFactory

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [PdfReaderFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [PdfReaderFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class PdfReaderFragment : Fragment(), OnPageChangeListener {
    override fun onPageChanged(page: Int, pageCount: Int) {
        this.listener.onReaderPageChanged(page)
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment PdfReaderFragment.
         */
        @JvmStatic
        fun newInstance(): PdfReaderFragment {
            // TODO: Could do any initialization we wanted here and pass in params if needed.
            return PdfReaderFragment()
        }
    }

    private lateinit var listener: PdfFragmentListenerType
    private lateinit var titleTextView: TextView
    private lateinit var pdfView: PDFView
    private lateinit var tocImage: ImageView

    private val log = LoggerFactory.getLogger(PdfReaderFragment::class.java)

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        this.log.debug("onCreateView")
        return inflater.inflate(R.layout.fragment_pdf_reader, container, false)
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
            Toast.makeText(context, "Implement Table of Contents", Toast.LENGTH_SHORT).show()
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
            .enableAnnotationRendering(true)
            .scrollHandle(DefaultScrollHandle(context))
//            .spacing(10) // in dp
            .pageFitPolicy(FitPolicy.BOTH)
            .load()
    }

    override fun onDetach() {
        this.log.debug("onDetach")
        super.onDetach()
    }
}
