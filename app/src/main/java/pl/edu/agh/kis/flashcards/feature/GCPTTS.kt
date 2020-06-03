package pl.edu.agh.kis.flashcards.feature

import android.media.MediaPlayer
import android.util.Log
import com.google.gson.JsonParser
import okhttp3.*
import java.io.IOException
import java.util.*


open class GCPTTS {
    private val mSpeakListeners: MutableList<ISpeakListener> =
        ArrayList()
    private var mGCPVoice: GCPVoice? = null
    private var mAudioConfig: AudioConfig? = null
    private var mMessage: String? = null
    private var mVoiceMessage: VoiceMessage? = null
    private var mMediaPlayer: MediaPlayer? = null
    private var mVoiceLength = -1

    constructor() {} // for inheritance reasons

    fun setGCPVoice(gcpVoice: GCPVoice?) {
        mGCPVoice = gcpVoice
    }

    fun setAudioConfig(audioConfig: AudioConfig?) {
        mAudioConfig = audioConfig
    }

    open fun start(text: String?) {
        if (mGCPVoice != null && mAudioConfig != null) {
            mMessage = text
            mVoiceMessage = VoiceMessage.Builder()
                .addParameter(Input(text!!))
                .addParameter(mGCPVoice!!)
                .addParameter(mAudioConfig!!)
                .build()
            Thread(runnableSend).start()
        } else {
            Log.d(TAG, "Unable to start GCPTTS")
        }
    }

    private val runnableSend = Runnable {
        Log.d(TAG, "Message: " + mVoiceMessage.toString())
        val body: RequestBody = RequestBody.create(
            MediaType.parse("application/json; charset=utf-8"),
            mVoiceMessage.toString()
        )
        val okHttpClient = OkHttpClient()

        val request: Request = Request.Builder()
            .url(Config.SYNTHESIZE_ENDPOINT)
            .addHeader(Config.API_KEY_HEADER, Config.API_KEY)
            .addHeader("Content-Type", "application/json; charset=utf-8")
            .post(body)
            .build()

        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                Log.i(TAG, "onResponse code = " + response.code())
                if (response.code() == 200) {
                    val text = response.body()!!.string()
                    val jsonElement = JsonParser().parse(text)
                    val jsonObject = jsonElement.asJsonObject
                    if (jsonObject != null) {
                        var json = jsonObject["audioContent"].toString()
                        json = json.replace("\"", "")
                        playAudio(json)
                        return
                    }
                }
                speakFail(mMessage, java.lang.NullPointerException("get response fail"))
            }

            override fun onFailure(call: Call, e: IOException) {
                speakFail(mMessage, e)
                Log.e(TAG, "onFailure error : " + e.message)
            }
        })
    }


    private fun playAudio(base64EncodedString: String) {
        try {
            val url = "data:audio/mp3;base64,$base64EncodedString"
            mMediaPlayer = MediaPlayer()
            mMediaPlayer!!.setDataSource(url)
            mMediaPlayer!!.prepare()
            mMediaPlayer!!.start()
            speakSuccess(mMessage)
        } catch (IoEx: IOException) {
            speakFail(mMessage, IoEx)
        }
    }

    open fun exit() {
        mMediaPlayer = null
    }

    private fun speakSuccess(speakMessage: String?) {
        for (speakListener in mSpeakListeners) {
            speakListener.onSuccess(speakMessage)
        }
    }

    private fun speakFail(speakMessage: String?, e: Exception) {
        for (speakListener in mSpeakListeners) {
            speakListener.onFailure(speakMessage, e)
        }
    }

    fun addSpeakListener(iSpeakListener: ISpeakListener) {
        mSpeakListeners.add(iSpeakListener)
    }

    fun removeSpeakListener(iSpeakListener: ISpeakListener) {
        mSpeakListeners.remove(iSpeakListener)
    }

    interface ISpeakListener {
        fun onSuccess(message: String?)
        fun onFailure(message: String?, e: Exception?)
    }

    companion object {
        private val TAG = GCPTTS::class.java.name
    }
}