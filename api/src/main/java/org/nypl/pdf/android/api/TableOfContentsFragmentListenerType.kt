package org.nypl.pdf.android.api

/**
 * The listener interface implemented by activities hosting fragments for the Table of Contents.
 */
interface TableOfContentsFragmentListenerType {

    fun onTableOfContentsWantsItems(): ArrayList<TableOfContentsItem>

    fun onTableOfContentsWantsTitle(): String

    fun onTOCItemSelected(pageSelected: Int)

    fun onTableOfContentsWantsEmptyDataText(): String
}