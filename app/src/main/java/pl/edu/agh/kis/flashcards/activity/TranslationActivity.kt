package pl.edu.agh.kis.flashcards.activity

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.fasterxml.jackson.databind.ObjectMapper
import pl.edu.agh.kis.flashcards.R
import pl.edu.agh.kis.flashcards.api.RequestHelper
import pl.edu.agh.kis.flashcards.api.model.ResponseData
import java.io.IOException

import com.fasterxml.jackson.module.kotlin.KotlinModule

class TranslationActivity : AppCompatActivity() {

    private val GOOGLE_TRANSLATE_API = "https://translation.googleapis.com/language/translate/v2"
    private val OBJECT_MAPPER = ObjectMapper().registerModule(KotlinModule())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_translatior)
    }

    fun translate(view: View) {
        val textToTranslate = findViewById<EditText>(R.id.textToTranslate)
        val translatedText = findViewById<TextView>(R.id.translatedText)
        val translateButton = findViewById<Button>(R.id.translate)

        @SuppressLint("StaticFieldLeak")
        val execute = object : AsyncTask<String?, Void?, Void?>() {
            var result = ""

            fun generateRequestParams(parameterName: String): String {
                return "?key=AIzaSyDY37PAuUMgq21LfJ2RC_ghqq4jJzD1iV4" +
                        "&q=" + parameterName +
                        "&source=en" +
                        "&target=pl"
            }

            override fun onPostExecute(aVoid: Void?) {
                super.onPostExecute(aVoid)
                var readValue = OBJECT_MAPPER.readValue<ResponseData>(result, ResponseData::class.java)

                translatedText.text = readValue.data.translations?.first()?.translatedText.orEmpty()
            }

            override fun doInBackground(vararg params: String?): Void? {
                try {
                    result = RequestHelper.sendPost(GOOGLE_TRANSLATE_API, generateRequestParams(params[0].orEmpty())).toString()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                return null
            }
        }.execute(textToTranslate.text.toString())

    }

}
