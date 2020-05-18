package pl.edu.agh.kis.flashcards.recyclerView

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pl.edu.agh.kis.flashcards.R
import pl.edu.agh.kis.flashcards.database.entities.NoteListEntity

class NoteListHolder(
    view: View,
    private val onNoteSetListener: ListAdapter.OnNoteSetListener
) :
    RecyclerView.ViewHolder(view), View.OnClickListener {

    private var listName: TextView? = null
    private var listLang: TextView? = null

    init {
        listName = itemView.findViewById(R.id.list_title)
        listLang = itemView.findViewById(R.id.list_description)
    }

    fun bind(noteListEntity: NoteListEntity) {
        listName?.text = noteListEntity.listName
        listLang?.text = noteListEntity.toLanguage
        itemView.setOnClickListener(this)
    }

    override fun onClick(itemView: View?) {
       onNoteSetListener.onClick(this, adapterPosition)
    }


}