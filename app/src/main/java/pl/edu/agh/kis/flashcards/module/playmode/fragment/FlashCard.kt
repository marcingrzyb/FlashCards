package pl.edu.agh.kis.flashcards.module.playmode.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import pl.edu.agh.kis.flashcards.R
import pl.edu.agh.kis.flashcards.database.NoteListDataBase
import pl.edu.agh.kis.flashcards.database.dao.NoteDAO
import pl.edu.agh.kis.flashcards.database.entity.NoteEntity
import pl.edu.agh.kis.flashcards.module.playmode.service.EventSessionHandler
import pl.edu.agh.kis.flashcards.module.playmode.service.EventType
import java.io.Serializable

class FlashCard(private var eventSessionHandler: EventSessionHandler) : Fragment() {

    private var noteDao: NoteDAO? = null

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

        txtView.setText(value.word)
        hiddenContent.setText("click")
        processImage(value.favourite, favourite)

        hiddenContent.setOnClickListener { hiddenContent.setText(value.translatedWord) }
        favourite.setOnClickListener { favouriteListener(value, favourite) }

        return inflate
    }

    private fun favouriteListener(value: NoteEntity, favourite: ImageButton) {
        value.favourite = !value.favourite!!
        processImage(value.favourite, favourite)
        eventSessionHandler.addEvent(value.id, EventType.ADD_TO_FAVOURITE)
    }

    private fun processImage(value: Boolean?, favourite: ImageButton) {
        var heart = R.drawable.heart_disabled
        if (value?.not()!!) {
            heart = R.drawable.heart
        }
        favourite.setImageResource(heart)
    }

    companion object {
        @JvmStatic
        fun newInstance(eventSessionHandler: EventSessionHandler) =
            FlashCard(eventSessionHandler).apply {
                arguments = Bundle().apply {
                }
            }
    }

}
