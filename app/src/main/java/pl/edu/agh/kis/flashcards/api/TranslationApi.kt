package pl.edu.agh.kis.flashcards.api

import pl.edu.agh.kis.flashcards.api.model.ResponseData
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface TranslationApi {

    @GET("language/translate/v2")
    fun translateWord(
        @Query("key") key: String,
        @Query("source") source: String,
        @Query("target") target: String,
        @Query("q") word: String
    ): Call<ResponseData>

}