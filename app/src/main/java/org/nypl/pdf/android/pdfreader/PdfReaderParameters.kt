package org.nypl.pdf.android.pdfreader

import java.io.Serializable

/**
 * Represents the parameters our sample app wants to pass to the [PdfReaderActivity].
 * @param assetPath Path to the asset to load.
 * @param pageIndex The page to open the asset to.
 */
data class PdfReaderParameters(
    val assetPath: String,
    val pageIndex: Int
) : Serializable