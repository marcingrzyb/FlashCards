package pl.edu.agh.kis.flashcards.module.playmode.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import pl.edu.agh.kis.flashcards.R
import pl.edu.agh.kis.flashcards.database.NoteListDataBase
import pl.edu.agh.kis.flashcards.database.dao.NoteDAO
import pl.edu.agh.kis.flashcards.database.entity.NoteEntity
import pl.edu.agh.kis.flashcards.module.playmode.service.EventSessionService
import pl.edu.agh.kis.flashcards.module.playmode.service.EventType
import java.io.Serializable

class FlashCard(private var eventSessionHandler: EventSessionService) : Fragment() {

    private var noteDao: NoteDAO? = null
    private var favouriteChange: Boolean = false
    private var rememberChange: Boolean = false

    val entity: Serializable by lazy {
        arguments?.getSerializable("note")!!
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        noteDao = NoteListDataBase.getDatabase(this.activity!!.application).noteDao()
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

        if (rememberChange) {
            eventSessionHandler.addEvent(value.id, EventType.REMEMBER, rememberChange)
        } else {
            eventSessionHandler.deleteEvent(value.id, EventType.REMEMBER, rememberChange)
        }

    }

    private fun favouriteListener(value: NoteEntity, favourite: ImageButton) {
        favouriteChange = !favouriteChange
        processImage(favouriteChange, favourite)

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
