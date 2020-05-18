package pl.edu.agh.kis.flashcards

import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import pl.edu.agh.kis.flashcards.fragments.NotesSetFragment

class NoteListDetailsActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            finish()
            return
        }

        if (savedInstanceState == null) {
            // During initial setup, plug in the details fragment.
            val details = NotesSetFragment().apply {
                arguments = intent.extras
            }
            supportFragmentManager.beginTransaction()
                .add(android.R.id.content, details)
                .commit()
        }
    }
}
