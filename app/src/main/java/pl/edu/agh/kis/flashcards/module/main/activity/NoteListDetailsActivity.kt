package pl.edu.agh.kis.flashcards.module.main.activity

import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import pl.edu.agh.kis.flashcards.module.main.fragment.NotesSetFragment

class NoteListDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            finish()
            return
        }

        if (savedInstanceState == null) {
            // During initial setup, plug in the details fragment.
            val details = NotesSetFragment()
                .apply {
                arguments = intent.extras
            }
            supportFragmentManager.beginTransaction()
                .add(android.R.id.content, details)
                .commit()
        }
    }
}
