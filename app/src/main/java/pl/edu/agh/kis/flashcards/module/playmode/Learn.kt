package pl.edu.agh.kis.flashcards.module.playmode

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import pl.edu.agh.kis.flashcards.R
import pl.edu.agh.kis.flashcards.database.NoteListDataBase
import pl.edu.agh.kis.flashcards.database.dao.SessionDao
import pl.edu.agh.kis.flashcards.database.entity.NoteEntity
import pl.edu.agh.kis.flashcards.database.entity.Session
import pl.edu.agh.kis.flashcards.database.services.NoteRepository
import pl.edu.agh.kis.flashcards.module.playmode.service.EventSessionHandler
import pl.edu.agh.kis.flashcards.module.playmode.view.FlashCardCollectionAdapter
import pl.edu.agh.kis.flashcards.module.playmode.view.ViewPagerDisabled
import java.time.Instant
import java.util.concurrent.Executors


class Learn() : AppCompatActivity() {

    private var pager: ViewPagerDisabled? = null
    private var flashCardCollectionAdapter: FlashCardCollectionAdapter? = null
    private val repository: NoteRepository

    private lateinit var notes: LiveData<List<NoteEntity>>

    init {
        val noteDao = NoteListDataBase.getDatabase(this).noteDao()
        repository = NoteRepository(noteDao)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        var eventSessionHandler = EventSessionHandler()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_learn)
        var intExtra = getIntent().getIntExtra("id", 0)
        notes = repository.getAllById(intExtra)
        notes.observe(this, createFlashCardFragments(eventSessionHandler))
    }

    private fun createFlashCardFragments(eventSessionHandler: EventSessionHandler): Observer<List<NoteEntity>> {
        return Observer { noteLists ->
            noteLists?.let {
                val size = notes.value!!.size
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
