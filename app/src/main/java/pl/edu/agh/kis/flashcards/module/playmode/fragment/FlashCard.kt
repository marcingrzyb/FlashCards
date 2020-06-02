package pl.edu.agh.kis.flashcards.module.playmode.fragment

import android.app.Application
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import pl.edu.agh.kis.flashcards.R
import pl.edu.agh.kis.flashcards.database.entity.NoteEntity
import pl.edu.agh.kis.flashcards.module.main.fragment.ViewModelFactory
import pl.edu.agh.kis.flashcards.module.main.viewmodels.NoteViewModel
import pl.edu.agh.kis.flashcards.module.playmode.service.EventSessionService
import pl.edu.agh.kis.flashcards.module.playmode.service.EventType
import pl.edu.agh.kis.flashcards.module.playmode.viewmodel.PlayModeViewModel
import java.io.Serializable
import kotlin.properties.Delegates

class FlashCard(private var eventSessionHandler: EventSessionService) : Fragment() {

    private var favouriteChange: Boolean = false
    private var rememberChange: Boolean = false
    private lateinit var noteListViewModel: PlayModeViewModel

    val entity: Serializable by lazy {
        arguments?.getSerializable("note")!!
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PlayModeViewModelFactory.setApplication(
            this.activity!!.application
        )
        noteListViewModel = ViewModelProvider(
            this,
            PlayModeViewModelFactory
        ).get(PlayModeViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val inflate = inflater.inflate(R.layout.fragment_flash_card, container, false)
        var value: NoteEntity = entity as NoteEntity;
        val txtView: TextView = inflate.findViewById(R.id.word)
        val hiddenContent: TextView = inflate.findViewById(R.id.word1)
        val favourite: ImageButton = inflate.findViewById(R.id.favouriteButton)
        val remembered: CheckBox = inflate.findViewById(R.id.remebered)



        txtView.setText(value.word)
        hiddenContent.setText("click")
        processImage(value.favourite, favourite)

        hiddenContent.setOnClickListener { hiddenContent.setText(value.translatedWord) }
        remembered.setOnClickListener { rememberListener(value, remembered) }
        favourite.setOnClickListener { favouriteListener(value, favourite) }

        return inflate
    }

    private fun rememberListener(value: NoteEntity, remembered: CheckBox) {
        rememberChange = !rememberChange
        value.remembered = !value.remembered!!

        noteListViewModel.update(value)
        if (rememberChange) {
            eventSessionHandler.addEvent(value.id, EventType.REMEMBER, rememberChange)
        } else {
            eventSessionHandler.deleteEvent(value.id, EventType.REMEMBER, rememberChange)
        }

    }

    private fun favouriteListener(value: NoteEntity, favourite: ImageButton) {
        favouriteChange = !favouriteChange
        processImage(favouriteChange, favourite)

        value.favourite = !value.favourite!!
        noteListViewModel.update(value)

        if (favouriteChange) {
            eventSessionHandler.addEvent(value.id, EventType.FAVOURITE, favouriteChange)
        } else {
            eventSessionHandler.deleteEvent(value.id, EventType.FAVOURITE, favouriteChange)
        }

    }

    private fun processImage(value: Boolean?, favourite: ImageButton) {
        var heart = R.drawable.heart_disabled
        if (!value?.not()!!) {
            heart = R.drawable.heart
        }
        favourite.setImageResource(heart)
    }

    companion object {
        @JvmStatic
        fun newInstance(eventSessionHandler: EventSessionService) =
            FlashCard(eventSessionHandler).apply {
                arguments = Bundle().apply {
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

    fun setIdF(id_: Int) {
        id = id_
    }

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return PlayModeViewModel(
            app
        ) as T
    }
}
