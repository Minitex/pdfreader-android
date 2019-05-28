package org.nypl.pdf.android.api

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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
class PdfReaderFragment : Fragment() {

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment PdfReaderFragment.
         */
        @JvmStatic
        fun newInstance() : PdfReaderFragment {
            // TODO: Could do any initialization we wanted here and pass in params if needed.
            return PdfReaderFragment()
        }
    }

    // TODO: Rename and change types of parameters
    private lateinit var listener: PdfFragmentListenerType
    private lateinit var titleTextView: TextView

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
    }

    override fun onDetach() {
        this.log.debug("onDetach")
        super.onDetach()
    }

}
