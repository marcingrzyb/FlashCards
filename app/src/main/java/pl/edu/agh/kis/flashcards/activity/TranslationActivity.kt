package pl.edu.agh.kis.flashcards.activity

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import pl.edu.agh.kis.flashcards.R
import pl.edu.agh.kis.flashcards.controller.TranslatorController


class TranslationActivity : AppCompatActivity() {

    private val translatorController = TranslatorController()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_translator)
    }

    fun translate(view: View) {
        val textToTranslate = findViewById<EditText>(R.id.textToTranslate)
        val translatedText = findViewById<TextView>(R.id.translatedText)

        translatorController.translate(translatedText, textToTranslate.text.toString())

    }

}
