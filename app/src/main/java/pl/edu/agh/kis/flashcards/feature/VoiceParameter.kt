package pl.edu.agh.kis.flashcards.feature

import org.json.JSONObject

interface VoiceParameter {
    val jSONHeader: String?
    fun toJSONObject(): JSONObject?
}