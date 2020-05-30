package pl.edu.agh.kis.flashcards.module.playmode.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import pl.edu.agh.kis.flashcards.R
import pl.edu.agh.kis.flashcards.database.NoteListDataBase
import pl.edu.agh.kis.flashcards.database.entity.NoteEntity
import pl.edu.agh.kis.flashcards.database.services.NoteRepository
import pl.edu.agh.kis.flashcards.module.playmode.service.EventSessionService
import pl.edu.agh.kis.flashcards.module.playmode.view.FlashCardCollectionAdapter
import pl.edu.agh.kis.flashcards.module.playmode.view.ViewPagerDisabled


class PlayMode() : AppCompatActivity() {

    private var pager: ViewPagerDisabled? = null
    private var flashCardCollectionAdapter: FlashCardCollectionAdapter? = null
    private val repository: NoteRepository

    private lateinit var notes: LiveData<List<NoteEntity>>

    init {
        val noteDao = NoteListDataBase.getDatabase(this).noteDao()
        repository = NoteRepository(noteDao)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_learn)
        var intExtra = getIntent().getIntExtra("id", 0)
        notes = repository.getAllById(intExtra)

        notes.observe(this, createFlashCardFragments())
    }

    private fun createFlashCardFragments(): Observer<List<NoteEntity>> {
        return Observer { noteLists ->
            noteLists?.let {
                val size = notes.value!!.size
                var eventSessionHandler = EventSessionService(size)
                pager = findViewById(R.id.pager)
                flashCardCollectionAdapter =
                    FlashCardCollectionAdapter(
                        supportFragmentManager,
                        size,
                        notes.value,
                        eventSessionHandler
                    );
                pager!!.adapter = flashCardCollectionAdapter
            }
        }
    }

}
