package pl.edu.agh.kis.flashcards.module.playmode.activity

import android.annotation.SuppressLint
import android.app.Application
import android.content.pm.ActivityInfo
import android.os.AsyncTask
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*
import androidx.viewpager2.widget.ViewPager2
import pl.edu.agh.kis.flashcards.R
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

    init {
        val noteDao = NoteListDataBase.getDatabase(this).noteDao()
        repository = NoteRepository(noteDao)
    }

    @SuppressLint("StaticFieldLeak", "SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContentView(R.layout.activity_learn)
        val intExtra = getIntent().getIntExtra("id", 0)
        val sourceLang = intent.getStringExtra("sourceLang")
        val targetLang = intent.getStringExtra("targetLang")
        PlayModeViewModelFactory.setApplication(application)
        noteListViewModel = ViewModelProvider(
            this,
            PlayModeViewModelFactory
        ).get(PlayModeViewModel::class.java)

        object : AsyncTask<Any?, Any?, Any?>() {

            var allById = Collections.emptyList<NoteEntity>()

            override fun onPostExecute(result: Any?) {
                val eventSessionHandler = EventSessionService(allById.size)
                pager = findViewById(R.id.pager)
                flashCardCollectionAdapter =
                    FlashCardPlayModeAdapter(
                        supportFragmentManager,
                        getLifecycle(),
                        allById,
                        eventSessionHandler
                    )
                flashCardCollectionAdapter!!.setSourceLang(sourceLang)
                flashCardCollectionAdapter!!.setTargetLang(targetLang)
                pager!!.adapter = flashCardCollectionAdapter

                val doppelgangerPageChangeCallback = onPageChangeCallback(allById)
                pager!!.registerOnPageChangeCallback(doppelgangerPageChangeCallback)
            }

            override fun doInBackground(vararg params: Any?) {
                allById = repository.loadList(intExtra)
            }

        }.execute()

    }

    private fun onPageChangeCallback(allById: List<NoteEntity>): ViewPager2.OnPageChangeCallback {
        return object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                if (position == allById.size) {
                    flashCardCollectionAdapter!!.processData()
                    pager!!.setUserInputEnabled(false);
                }
            }
        }
    }

}

object PlayModeViewModelFactory : ViewModelProvider.Factory {
    lateinit var app: Application
    var id by Delegates.notNull<Int>()
    fun setApplication(application: Application) {
        app = application
    }

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return PlayModeViewModel(
            app
        ) as T
    }
}
