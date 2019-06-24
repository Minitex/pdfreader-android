package org.nypl.pdf.android.api

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Represents an item in a Table of Contents.
 * [Parcelable] so we can save an [ArrayList] of [TableOfContentsItem] on layout changes.
 * @property title The title that represents the text of the item. (ex. Chapter 1, Section 3)
 * @property pageNumber The number of the page of the item.
 * @property children List of child items. (ex. Section 3.1, Section 3.2)
 */
@Parcelize
data class TableOfContentsItem(
    val title: String? = null,
    val pageNumber: Int,
    val children: ArrayList<TableOfContentsItem>) : Parcelable