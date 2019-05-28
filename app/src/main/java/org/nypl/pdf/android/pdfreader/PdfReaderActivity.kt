package org.nypl.pdf.android.pdfreader

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;

import kotlinx.android.synthetic.main.activity_pdf_reader.*
import org.nypl.pdf.android.api.PdfFragmentListenerType
import org.nypl.pdf.android.api.PdfReaderFragment
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.InputStream

class PdfReaderActivity : AppCompatActivity(), PdfFragmentListenerType {

    private lateinit var readerFragment: PdfReaderFragment
    private lateinit var documentTitle: String

    private val log: Logger = LoggerFactory.getLogger(PdfReaderActivity::class.java)


    companion object {
        private const val PARAMS_ID = "org.nypl.pdf.android.pefreader.PdfReaderActivity.params"

        fun startActivity(
            from: Activity,
            parameters: PdfReaderParameters) {

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

        this.documentTitle = intentParams.assestPath;

        this.readerFragment = PdfReaderFragment.newInstance()

        this.supportFragmentManager
            .beginTransaction()
            .replace(R.id.pdf_reader_fragment_holder, this.readerFragment, "READER")
            .commit()
    }

    override fun onReaderWantsInputStream(): InputStream {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onReaderWantsTitle(): String {
        return this.documentTitle
    }

    override fun onReaderWantsCurrentPage(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onReaderPageChanged(pageIndex: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
