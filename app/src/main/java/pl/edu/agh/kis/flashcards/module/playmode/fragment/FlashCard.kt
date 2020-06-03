package pl.edu.agh.kis.flashcards.module.playmode.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import pl.edu.agh.kis.flashcards.R
import pl.edu.agh.kis.flashcards.database.entity.NoteEntity
import pl.edu.agh.kis.flashcards.module.playmode.activity.PlayModeViewModelFactory
import pl.edu.agh.kis.flashcards.module.playmode.service.EventSessionService
import pl.edu.agh.kis.flashcards.module.playmode.service.EventType
import pl.edu.agh.kis.flashcards.module.playmode.viewmodel.PlayModeViewModel
import java.io.Serializable

class FlashCard(private var eventSessionHandler: EventSessionService) : Fragment() {

    private var favouriteChange: Boolean = false
    private var rememberChange: Boolean = false
    private lateinit var favourite: ImageButton
    private lateinit var remembered: CheckBox
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
        favourite = inflate.findViewById(R.id.favouriteButton)
        remembered = inflate.findViewById(R.id.remebered)


        txtView.setText(value.word)
        hiddenContent.setText("click")
        initFragment(value)

        hiddenContent.setOnClickListener { hiddenContent.setText(value.translatedWord) }
        remembered.setOnClickListener { rememberListener(value) }
        favourite.setOnClickListener { favouriteListener(value) }

        return inflate
    }

    private fun initFragment(value: NoteEntity) {
        var heart = if (value.favourite!!) R.drawable.heart else R.drawable.heart_disabled

        favourite.setImageResource(heart)
        remembered.isChecked = value.remembered!!
    }

    private fun rememberListener(value: NoteEntity) {
        rememberChange = !rememberChange
        value.remembered = !value.remembered!!

        initFragment(value)
        noteListViewModel.update(value)
        if (rememberChange) {
            eventSessionHandler.addEvent(value.id, EventType.REMEMBER, rememberChange)
        } else {
            eventSessionHandler.deleteEvent(value.id, EventType.REMEMBER, rememberChange)
        }

    }

    private fun favouriteListener(value: NoteEntity) {
        favouriteChange = !favouriteChange
        value.favourite = !value.favourite!!

        initFragment(value)
        noteListViewModel.update(value)

        if (favouriteChange) {
            eventSessionHandler.addEvent(value.id, EventType.FAVOURITE, favouriteChange)
        } else {
            eventSessionHandler.deleteEvent(value.id, EventType.FAVOURITE, favouriteChange)
        }

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


