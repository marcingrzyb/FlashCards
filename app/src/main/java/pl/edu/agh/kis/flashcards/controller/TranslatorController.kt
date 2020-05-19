package pl.edu.agh.kis.flashcards.controller

import android.util.Log
import android.widget.TextView
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

    lateinit var translatedText: TextView

    fun translate(translatedText: TextView, textToTranslate: String) {
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
            translationApi.translateWord(GOOGLE_API_KEY, "en", "pl", textToTranslate)
        call.enqueue(this)
    }

    override fun onResponse(
        call: Call<ResponseData>,
        response: Response<ResponseData>
    ) {
        if (response.isSuccessful) {
            val responseData: ResponseData? = response.body()
            translatedText.text = responseData!!.data.translations?.first()?.translatedText.orEmpty()
        } else {
            Log.e("TRANSLATION", response.errorBody().toString())
        }
    }

    override fun onFailure(
        call: Call<ResponseData>,
        t: Throwable
    ) {
        t.printStackTrace()
    }

}