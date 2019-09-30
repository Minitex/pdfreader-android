package edu.umn.minitex.pdf.android.api

/**
 * The listener interface implemented by activities hosting fragments for the Table of Contents.
 */
interface TableOfContentsFragmentListenerType {

    /**
     * A fragment wants the text that should be displayed at the top of the Table of Contents view.
     */
    fun onTableOfContentsWantsTitle(): String

    /**
     * A fragment wants the text that should be displayed if there are no items in the Table of Contents.
     */
    fun onTableOfContentsWantsEmptyDataText(): String

    /**
     * A fragment wants the [TableOfContentsItem]s that make up the Table of Contents.
     */
    fun onTableOfContentsWantsItems(): ArrayList<TableOfContentsItem>

    /**
     * The Table of Contents Fragment published that a [TableOfContentsItem] was selected.
     */
    fun onTableOfContentsItemSelected(pageSelected: Int)

}