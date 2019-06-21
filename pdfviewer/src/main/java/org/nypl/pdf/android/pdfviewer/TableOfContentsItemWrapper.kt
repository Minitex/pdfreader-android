package org.nypl.pdf.android.pdfviewer

import org.nypl.pdf.android.api.TableOfContentsItem

data class TableOfContentsItemWrapper(
    val title: String?,
    val pageNumber: Int,
    val indent: Int) {
    constructor(tocItem: TableOfContentsItem, indent: Int) : this(tocItem.title, tocItem.pageNumber, indent)
}