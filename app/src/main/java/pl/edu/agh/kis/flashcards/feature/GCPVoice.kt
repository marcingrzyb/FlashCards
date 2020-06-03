package pl.edu.agh.kis.flashcards.feature

import android.util.Log
import org.json.JSONException
import org.json.JSONObject

class GCPVoice(languageCode: String, name: String) : VoiceParameter {
    var languageCode: String = languageCode
        private set
    var name: String = name
        private set
    var eSSMLlGender: ESSMLlVoiceGender
        private set
    var naturalSampleRateHertz: Int
        private set

    init {
        eSSMLlGender = ESSMLlVoiceGender.NONE
        naturalSampleRateHertz = 0
    }

    override val jSONHeader: String
        get() = "voice"

    override fun toJSONObject(): JSONObject? {
        val jsonObject = JSONObject()
        try {
            jsonObject.put("languageCode", languageCode)
            jsonObject.put("name", name)
            if (eSSMLlGender !== ESSMLlVoiceGender.NONE) {
                jsonObject.put("name", eSSMLlGender.toString())
            }
            if (naturalSampleRateHertz != 0) {
                jsonObject.put("naturalSampleRateHertz", naturalSampleRateHertz.toString())
            }
            return jsonObject
        } catch (e: JSONException) {
            Log.e(GCPVoice::javaClass.name, e.message!!)
        }
        return null
    }

    override fun toString(): String {
        var text = "'voice':{"
        text += "'languageCode':'" + languageCode + "',"
        text += "'name':'" + name + "'"
        text += if (eSSMLlGender === ESSMLlVoiceGender.NONE) "" else ",'ssmlGender':'$eSSMLlGender'"
        text += if (naturalSampleRateHertz == 0) "" else ",'naturalSampleRateHertz':'$naturalSampleRateHertz'"
        text += "}"
        return text
    }

}