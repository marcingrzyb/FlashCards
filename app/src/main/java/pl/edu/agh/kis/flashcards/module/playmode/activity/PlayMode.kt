package pl.edu.agh.kis.flashcards.module.playmode.activity

import android.annotation.SuppressLint
import android.app.Application
import android.os.AsyncTask
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*
import androidx.viewpager2.widget.ViewPager2
import pl.edu.agh.kis.flashcards.R
import kotlinx.coroutines.launch
import pl.edu.agh.kis.flashcards.database.NoteListDataBase
import pl.edu.agh.kis.flashcards.database.entity.NoteEntity
import pl.edu.agh.kis.flashcards.database.services.NoteRepository
import pl.edu.agh.kis.flashcards.module.playmode.service.EventSessionService
import pl.edu.agh.kis.flashcards.module.playmode.view.FlashCardPlayModeAdapter
import pl.edu.agh.kis.flashcards.module.playmode.viewmodel.PlayModeViewModel
import java.util.*
import kotlin.properties.Delegates


class PlayMode() : AppCompatActivity() {

    private var pager: ViewPager2? = null
    private var flashCardCollectionAdapter: FlashCardPlayModeAdapter? = null
    private val repository: NoteRepository
    private lateinit var noteListViewModel: PlayModeViewModel

    private lateinit var notes: LiveData<List<Int>>

    init {
        val noteDao = NoteListDataBase.getDatabase(this).noteDao()
        repository = NoteRepository(noteDao)
    }

    @SuppressLint("StaticFieldLeak")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_learn)
        var intExtra = getIntent().getIntExtra("id", 0)
        notes = repository.loadIds(intExtra)

        PlayModeViewModelFactory.setApplication(application)
        noteListViewModel = ViewModelProvider(
            this,
            PlayModeViewModelFactory
        ).get(PlayModeViewModel::class.java)

        object : AsyncTask<Any?, Any?, Any?>() {

            var allById = Collections.emptyList<NoteEntity>()

            override fun onPostExecute(result: Any?) {
                var eventSessionHandler = EventSessionService(allById.size)
                pager = findViewById(R.id.pager)
                flashCardCollectionAdapter =
                    FlashCardPlayModeAdapter(
                        supportFragmentManager,
                        getLifecycle(),
                        allById,
                        eventSessionHandler
                    )
                pager!!.adapter = flashCardCollectionAdapter
            }

            override fun doInBackground(vararg params: Any?) {
                allById = repository.loadList()
            }

        }.execute()

    }


}

object PlayModeViewModelFactory : ViewModelProvider.Factory {
    lateinit var app: Application
    var id by Delegates.notNull<Int>()
    fun setApplication(application: Application) {
        app = application
    }

    fun setIdF(id_: Int) {
        id = id_
    }

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return PlayModeViewModel(
            app
        ) as T
    }
}
