package org.nypl.pdf.android.api

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TableOfContentsItem(
    val title: String? = null,
    val pageNumber: Int,
    val children: ArrayList<TableOfContentsItem>) : Parcelable