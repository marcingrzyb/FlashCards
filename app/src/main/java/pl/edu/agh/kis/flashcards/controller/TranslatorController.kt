package pl.edu.agh.kis.flashcards.controller

import android.widget.EditText
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import pl.edu.agh.kis.flashcards.api.TranslationApi
import pl.edu.agh.kis.flashcards.api.model.ResponseData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TranslatorController : Callback<ResponseData> {

    private val GOOGLE_TRANSLATE_API = "https://translation.googleapis.com/"
    private val GOOGLE_API_KEY = "AIzaSyDY37PAuUMgq21LfJ2RC_ghqq4jJzD1iV4"

    lateinit var translatedText: EditText

    fun translate(
        translatedText: EditText,
        textToTranslate: String,
        sourceLang: String,
        targetLang: String
    ) {
        this.translatedText = translatedText
        val gson: Gson = GsonBuilder()
            .setLenient()
            .create()
        val retrofit = Retrofit.Builder()
            .baseUrl(GOOGLE_TRANSLATE_API)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        val translationApi: TranslationApi =
            retrofit.create(TranslationApi::class.java)
        val call: Call<ResponseData> =
            translationApi.translateWord(GOOGLE_API_KEY, sourceLang, targetLang, textToTranslate)
        call.enqueue(this)
    }

    override fun onResponse(
        call: Call<ResponseData>,
        response: Response<ResponseData>
    ) {
        if (response.isSuccessful) {
            val responseData: ResponseData? = response.body()
            translatedText.setText(responseData!!.data.translations?.first()?.translatedText.orEmpty())
        } else {
            Toast.makeText(translatedText.context,"error during translation",Toast.LENGTH_SHORT).show()
        }
    }

    override fun onFailure(
        call: Call<ResponseData>,
        t: Throwable
    ) {
        t.printStackTrace()
    }

}