package pl.edu.agh.kis.flashcards.pronouncation

interface ISpeech {
    fun exit()
    fun addSpeechListener(speechListener: ISpeechListener?)
    fun removeSpeechListener(speechListener: ISpeechListener?)
    interface ISpeechListener {
        fun onSuccess(message: String?)
        fun onFailure(message: String?, e: Exception?)
    }
}
