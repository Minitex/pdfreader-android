package org.nypl.pdf.android.pdfreader

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import org.nypl.pdf.android.api.PdfFragmentListenerType
import org.nypl.pdf.android.pdfviewer.PDFViewerFragment
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.InputStream

const val KEY_PAGE_INDEX = "page_index"

class PdfReaderActivity : AppCompatActivity(), PdfFragmentListenerType {

    private lateinit var documentTitle: String
    private var documentPageIndex: Int = 0
    private lateinit var assetPath: String

    private val log: Logger = LoggerFactory.getLogger(PdfReaderActivity::class.java)

    companion object {
        private const val PARAMS_ID = "org.nypl.pdf.android.pefreader.PdfReaderActivity.params"

        fun startActivity(
            from: Activity,
            parameters: PdfReaderParameters
        ) {

            val b = Bundle()
            b.putSerializable(this.PARAMS_ID, parameters)
            val i = Intent(from, PdfReaderActivity::class.java)
            i.putExtras(b)
            from.startActivity(i)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdf_reader)

        val intentParams = intent?.getSerializableExtra(PARAMS_ID) as PdfReaderParameters

        this.documentTitle = intentParams.assestPath
        this.assetPath = intentParams.assestPath

        if (savedInstanceState != null) {
            this.documentPageIndex = savedInstanceState.getInt(KEY_PAGE_INDEX, 0)
        } else {
            this.documentPageIndex = 0
        }

        // Get the new instance of the reader you want to load here.
        var readerFragment = PDFViewerFragment.newInstance()

        this.supportFragmentManager
            .beginTransaction()
            .replace(R.id.pdf_reader_fragment_holder, readerFragment, "READER")
            .commit()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(KEY_PAGE_INDEX, documentPageIndex)
        super.onSaveInstanceState(outState)
    }

    override fun onReaderWantsInputStream(): InputStream {
        var fileStream = assets.open(this.assetPath)
        return fileStream
    }

    override fun onReaderWantsTitle(): String {
        return this.documentTitle
    }

    override fun onReaderWantsCurrentPage(): Int {
        return this.documentPageIndex
    }

    override fun onReaderPageChanged(pageIndex: Int) {
        this.documentPageIndex = pageIndex
    }
}
