package org.nypl.pdf.android.api

/**
 * The listener interface implemented by activities hosting fragments for the Table of Contents.
 */
interface TableOfContentsFragmentListenerType {

    fun onTableOfContentsWantsItems(): List<TableOfContentsItem>

    fun onTableOfContentsWantsTitle(): String

    fun onTOCItemSelected(pageSelected: Int)
}