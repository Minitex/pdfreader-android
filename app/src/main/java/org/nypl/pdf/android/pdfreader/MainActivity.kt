package org.nypl.pdf.android.pdfreader

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {

    private lateinit var googleButton: Button
    private lateinit var aliceButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        this.googleButton = findViewById(R.id.google_button)
        this.googleButton.setOnClickListener {
            PdfReaderActivity.startActivity(this, PdfReaderParameters("sample", 0))
        }

        this.aliceButton = findViewById(R.id.alice_button)
        this.aliceButton.setOnClickListener {
            PdfReaderActivity.startActivity(this, PdfReaderParameters("aliceInWonderland", 0))
        }
    }
}
