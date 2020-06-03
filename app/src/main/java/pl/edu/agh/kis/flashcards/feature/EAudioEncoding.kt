package pl.edu.agh.kis.flashcards.feature

enum class EAudioEncoding(var mDescription: String) {
    LINEAR16("LINEAR16");

    override fun toString(): String {
        return mDescription
    }

}