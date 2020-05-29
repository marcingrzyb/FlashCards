package pl.edu.agh.kis.flashcards.module.main.viewmodel

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pl.edu.agh.kis.flashcards.R
import pl.edu.agh.kis.flashcards.database.entity.NoteEntity
import pl.edu.agh.kis.flashcards.module.main.viewmodel.NoteAdapter

class NoteHolder(
    view: View,
    private val NoteListener: NoteAdapter.OnNoteSetListener
) :
    RecyclerView.ViewHolder(view), View.OnClickListener {

    private var word: TextView? = null
    private var translatedWord: TextView? = null

    init {
        word = itemView.findViewById(R.id.list_title)
        translatedWord = itemView.findViewById(R.id.base_language)
    }

    fun bind(noteEntity: NoteEntity) {
        word?.text = noteEntity.word
        translatedWord?.text = noteEntity.translatedWord
        itemView.setOnClickListener(this)
    }

    override fun onClick(itemView: View?) {
        NoteListener.onClick(this, adapterPosition)
    }


}