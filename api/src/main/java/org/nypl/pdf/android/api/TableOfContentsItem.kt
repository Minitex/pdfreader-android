package org.nypl.pdf.android.api

data class TableOfContentsItem(
    val title: String?,
    val pageNumber: Int,
    val children: List<TableOfContentsItem>)