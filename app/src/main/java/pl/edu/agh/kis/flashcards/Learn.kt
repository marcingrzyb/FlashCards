package pl.edu.agh.kis.flashcards

import android.app.Application
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.viewpager.widget.ViewPager
import pl.edu.agh.kis.flashcards.database.NoteListDataBase
import pl.edu.agh.kis.flashcards.database.entities.NoteEntity
import pl.edu.agh.kis.flashcards.database.services.NoteRepository

class Learn() : AppCompatActivity() {

    private var pager: ViewPager? = null
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
        notes = repository.getAllById(2)

        notes.observe(this, Observer { noteLists ->
            noteLists?.let {
                val size = notes.value!!.size
                pager = findViewById(R.id.pager)
                flashCardCollectionAdapter = FlashCardCollectionAdapter(
                    supportFragmentManager,
                    size,
                    notes.value
                );
                pager!!.adapter = flashCardCollectionAdapter }
        })

    }
}
