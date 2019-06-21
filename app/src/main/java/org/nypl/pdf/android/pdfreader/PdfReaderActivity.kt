package org.nypl.pdf.android.pdfreader

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import org.nypl.pdf.android.api.PdfFragmentListenerType
import org.nypl.pdf.android.api.TableOfContentsFragmentListenerType
import org.nypl.pdf.android.api.TableOfContentsItem
import org.nypl.pdf.android.pdfviewer.PDFViewerFragment
import org.nypl.pdf.android.pdfviewer.TableOfContentsFragment
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.InputStream

const val KEY_PAGE_INDEX = "page_index"
const val TABLE_OF_CONTENTS = "table_of_contents"

class PdfReaderActivity : AppCompatActivity(), PdfFragmentListenerType, TableOfContentsFragmentListenerType {
    private lateinit var documentTitle: String
    private var documentPageIndex: Int = 0
    private lateinit var assetPath: String
    private var tableOfContentsList: ArrayList<TableOfContentsItem> = arrayListOf()

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
            this.tableOfContentsList = savedInstanceState.getParcelableArrayList(TABLE_OF_CONTENTS) ?: arrayListOf()
        } else {
            this.documentPageIndex = 0

            // Get the new instance of the reader you want to load here.
            var readerFragment = PDFViewerFragment.newInstance()

            this.supportFragmentManager
                .beginTransaction()
                .replace(R.id.pdf_reader_fragment_holder, readerFragment, "READER")
                .commit()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(KEY_PAGE_INDEX, documentPageIndex)
        outState.putParcelableArrayList(TABLE_OF_CONTENTS, tableOfContentsList)
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

    override fun onReaderLoadedTableOfContents(tableOfContentsList: ArrayList<TableOfContentsItem>) {
        this.tableOfContentsList = tableOfContentsList
    }

    override fun onReaderWantsToCFragment() {
        // Get the new instance of the reader you want to load here.
        var readerFragment = TableOfContentsFragment.newInstance()

        this.supportFragmentManager
            .beginTransaction()
            .replace(R.id.pdf_reader_fragment_holder, readerFragment, "READER")
            .addToBackStack(null)
            .commit()
    }

    override fun onTableOfContentsWantsItems(): ArrayList<TableOfContentsItem> {
        return this.tableOfContentsList
    }

    override fun onTableOfContentsWantsTitle(): String {
        return getString(R.string.table_of_contents_title)
    }

    override fun onTOCItemSelected(pageSelected: Int) {
        this.documentPageIndex = pageSelected
        onBackPressed()
    }

    override fun onTableOfContentsWantsEmptyDataText(): String {
        return getString(R.string.table_of_contents_empty_message)
    }
}
