package pl.edu.agh.kis.flashcards.pronouncation

import org.json.JSONException
import org.json.JSONObject
import java.util.*

class VoiceMessage private constructor() {
    private val mInput: Input? = null
    private val mGCPVoice: GCPVoice? = null
    private val mAudioConfig: AudioConfig? = null
    private val mVoiceParameters: MutableList<VoiceParameter>

    class Builder {
        private val mVoiceMessage: VoiceMessage
        fun addParameter(voiceParameter: VoiceParameter): Builder {
            mVoiceMessage.mVoiceParameters.add(voiceParameter)
            return this
        }

        fun add(input: Input): Builder {
            mVoiceMessage.mVoiceParameters.add(input)
            return this
        }

        fun add(GCPVoice: GCPVoice): Builder {
            mVoiceMessage.mVoiceParameters.add(GCPVoice)
            return this
        }

        fun add(audioConfig: AudioConfig): Builder {
            mVoiceMessage.mVoiceParameters.add(audioConfig)
            return this
        }

        fun build(): VoiceMessage {
            return mVoiceMessage
        }

        init {
            mVoiceMessage = VoiceMessage()
        }
    }

    override fun toString(): String {
        if (mVoiceParameters.size != 0) {
            val jsonObject = JSONObject()
            try {
                for (v in mVoiceParameters) {
                    jsonObject.put(v.jSONHeader, v.toJSONObject())
                }
                return jsonObject.toString()
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
        return ""
    }

    init {
        mVoiceParameters = ArrayList()
    }
}