package edu.umn.minitex.pdf.android.api

import java.io.InputStream

/**
 * The listener interface implemented by activities hosting fragments for the PDF Reader.
 */
interface PdfFragmentListenerType {

    /**
     * Called when the reader wants the file to be rendered.
     */
    fun onReaderWantsInputStream(): InputStream

    /**
     * A fragment wants the title of the document being rendered.
     */
    fun onReaderWantsTitle(): String

    /**
     * A fragment wants the page index it should display.
     */
    fun onReaderWantsCurrentPage(): Int

    /**
     * The reader published a Page Changed event for [pageIndex].
     */
    fun onReaderPageChanged(pageIndex: Int)

    /**
     * The reader wants the activity to display a Table of Contents Fragment.
     */
    fun onReaderWantsTableOfContentsFragment()

    /**
     * The reader published that is has a Table of Contents list [tableOfContentsList] available
     */
    fun onReaderLoadedTableOfContents(tableOfContentsList: ArrayList<TableOfContentsItem>)
}