package org.nypl.pdf.android.pdfviewer

import org.nypl.pdf.android.api.TableOfContentsItem

/**
 * Represents a "wrapped" [TableOfContentsItem].
 * This class is designed to be used in a single-dimensional list with [indent]
 * used to denote sub-sections.
 * @property title The title that represents the text of the item. (ex. Chapter 1, Section 3)
 * @property pageNumber The number of the page of the item.
 * @property indent Indentation level used to denote sub-sections
 */
data class TableOfContentsItemWrapper(
    val title: String?,
    val pageNumber: Int,
    val indent: Int) {
    constructor(tocItem: TableOfContentsItem, indent: Int) : this(tocItem.title, tocItem.pageNumber, indent)
}