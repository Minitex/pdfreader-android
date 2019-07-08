package org.nypl.pdf.android.pdfreader

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

/**
 * Main Activity of the Sample app for the PdfReader library.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var googleButton: Button
    private lateinit var aliceButton: Button
    private lateinit var circuitButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Open Google Slides doc
        this.googleButton = findViewById(R.id.google_button)
        this.googleButton.setOnClickListener {
            PdfReaderActivity.startActivity(this, PdfReaderParameters("sample.pdf", 0))
        }

        // Alice in Wonderland
        this.aliceButton = findViewById(R.id.alice_button)
        this.aliceButton.setOnClickListener {
            PdfReaderActivity.startActivity(this, PdfReaderParameters("aliceInWonderland.pdf", 0))
        }

        // DC Circuits - this document has a table of contents.
        this.circuitButton = findViewById(R.id.circuit_button)
        this.circuitButton.setOnClickListener {
            PdfReaderActivity.startActivity(this, PdfReaderParameters("dcCircuits.pdf", 0))
        }
    }
}
