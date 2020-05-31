package pl.edu.agh.kis.flashcards.module.main.view

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pl.edu.agh.kis.flashcards.R
import pl.edu.agh.kis.flashcards.database.entity.NoteEntity

class NoteHolder(
    view: View,
    private val noteListener: NoteAdapter.OnNoteSetListener
) :
    RecyclerView.ViewHolder(view), View.OnClickListener, View.OnLongClickListener {

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
        itemView.setOnLongClickListener(this)
    }

    override fun onClick(itemView: View?) {
         noteListener.onClick(this, adapterPosition)
    }

    override fun onLongClick(itemView: View?): Boolean {
        noteListener.onLongClick(this, adapterPosition)
        return true
    }
}