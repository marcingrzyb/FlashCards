package pl.edu.agh.kis.flashcards.pronouncation

import android.util.Log
import org.json.JSONException
import org.json.JSONObject

class Input internal constructor(text: String) : VoiceParameter {
    private var mText: String = text
    private var mIsEnableSSML: Boolean = false

    override val jSONHeader: String
        get() = "input"

    override fun toJSONObject(): JSONObject? {
        val jsonText = JSONObject()
        try {
            jsonText.put(if (mIsEnableSSML) "ssml" else "text", mText)
            return jsonText
        } catch (e: JSONException) {
            Log.e(Input::javaClass.name, e.message!!)
        }
        return null
    }

    override fun toString(): String {
        var text = "'" + this.javaClass.simpleName.toLowerCase() + "':{"
        text += if (mIsEnableSSML) "'ssml':'$mText'}" else "'text':'$mText'}"
        return text
    }
}