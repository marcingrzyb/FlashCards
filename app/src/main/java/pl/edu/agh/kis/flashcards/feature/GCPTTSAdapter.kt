package pl.edu.agh.kis.flashcards.feature

import java.util.*

class GCPTTSAdapter : GCPTTS(), ISpeech, GCPTTS.ISpeakListener {
    private val mSpeechListeners: MutableList<ISpeech.ISpeechListener>

    override fun exit() {
        super.exit()
        removeSpeakListener(this)
        mSpeechListeners.clear()
    }

    override fun addSpeechListener(speechListener: ISpeech.ISpeechListener?) {
        if (speechListener != null) {
            mSpeechListeners.add(speechListener)
        }
    }

    override fun removeSpeechListener(speechListener: ISpeech.ISpeechListener?) {
        mSpeechListeners.remove(speechListener)
    }

    override fun onSuccess(message: String?) {
        for (speechListener in mSpeechListeners) {
            speechListener.onSuccess(message)
        }
    }

    override fun onFailure(message: String?, e: Exception?) {
        for (speechListener in mSpeechListeners) {
            speechListener.onFailure(message, e)
        }
    }

    init {
        mSpeechListeners = ArrayList<ISpeech.ISpeechListener>()
        addSpeakListener(this)
    }
}