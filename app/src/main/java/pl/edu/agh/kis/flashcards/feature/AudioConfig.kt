package pl.edu.agh.kis.flashcards.feature

import org.json.JSONException
import org.json.JSONObject
import java.util.*

open class AudioConfig : VoiceParameter {
    var mEAudioEncoding: EAudioEncoding? = null
    var mSpeakingRate = 0f
    var mPitch = 0f
    var mVolumeGainDb = 0
    var mSampleRateHertz = 0

    constructor() {
        mEAudioEncoding = EAudioEncoding.LINEAR16
        mSpeakingRate = 1.0f
        mPitch = 0.0f
        mVolumeGainDb = 0
        mSampleRateHertz = 0
    }

    override val jSONHeader: String?
        get() {
            return "audioConfig"
        }

    override fun toJSONObject(): JSONObject? {
        val jsonObject = JSONObject()
        try {
            jsonObject.put("audioEncoding", mEAudioEncoding.toString())
            jsonObject.put("speakingRate", mSpeakingRate.toString())
            jsonObject.put("pitch", getPitch())
            return jsonObject
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return null
    }


    open fun getPitch(): String? {
        val pitchList: MutableList<String?> =
            ArrayList()
        pitchList.add(mPitch.toString())
        if (mVolumeGainDb != 0) {
            pitchList.add(mVolumeGainDb.toString())
        }
        if (mSampleRateHertz != 0) {
            pitchList.add(mSampleRateHertz.toString())
        }
        return pitchList.toString().replace("[", "").replace("]", "")
    }
}
