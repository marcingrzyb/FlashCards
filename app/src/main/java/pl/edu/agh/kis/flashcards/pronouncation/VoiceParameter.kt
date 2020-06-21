package pl.edu.agh.kis.flashcards.pronouncation

import org.json.JSONObject

interface VoiceParameter {
    val jSONHeader: String?
    fun toJSONObject(): JSONObject?
}