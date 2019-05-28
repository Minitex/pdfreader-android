package org.nypl.pdf.android.pdfreader

import java.io.Serializable

data class PdfReaderParameters(
    val assestPath: String,
    val pageIndex: Int
): Serializable
