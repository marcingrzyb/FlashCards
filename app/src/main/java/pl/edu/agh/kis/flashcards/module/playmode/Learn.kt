package pl.edu.agh.kis.flashcards.module.playmode

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.viewpager.widget.ViewPager
import pl.edu.agh.kis.flashcards.R
import pl.edu.agh.kis.flashcards.database.NoteListDataBase
import pl.edu.agh.kis.flashcards.database.dao.SessionDao
import pl.edu.agh.kis.flashcards.database.entity.NoteEntity
import pl.edu.agh.kis.flashcards.database.entity.Session
import pl.edu.agh.kis.flashcards.database.services.NoteRepository
import java.time.Instant
import java.util.concurrent.Executors


class Learn() : AppCompatActivity() {

    private var pager: ViewPagerDisabled? = null
    private var flashCardCollectionAdapter: FlashCardCollectionAdapter? = null
    private val repository: NoteRepository

    private lateinit var sessionDao: SessionDao
    private lateinit var notes: LiveData<List<NoteEntity>>

    init {
        val noteDao = NoteListDataBase.getDatabase(this).noteDao()
        sessionDao = NoteListDataBase.getDatabase(this).sessionDao()
        repository = NoteRepository(noteDao)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_learn)
        var intExtra = getIntent().getIntExtra("id", 0)
        notes = repository.getAllById(intExtra)
        var sessionId = 0L
        Executors.newSingleThreadExecutor().execute(Runnable { sessionId = prepareSession() })
        notes.observe(this, createFlashCardFragments(sessionId))
    }

    private fun prepareSession(): Long {
        var session = Session(null, Instant.now().epochSecond, null)
        return sessionDao.insert(session)
    }

    private fun createFlashCardFragments(id: Long): Observer<List<NoteEntity>> {
        return Observer { noteLists ->
            noteLists?.let {
                val size = notes.value!!.size
                pager = findViewById(R.id.pager)
                flashCardCollectionAdapter =
                    FlashCardCollectionAdapter(
                        supportFragmentManager,
                        size,
                        notes.value,
                        id
                    );
                pager!!.adapter = flashCardCollectionAdapter
            }
        }
    }

}
