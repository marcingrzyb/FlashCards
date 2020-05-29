package pl.edu.agh.kis.flashcards.module.main.view

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pl.edu.agh.kis.flashcards.R
import pl.edu.agh.kis.flashcards.database.entity.NoteListEntity

class NoteListHolder(
    view: View,
    private val onNoteSetListener: NoteListAdapterRecycler.OnNoteSetListener
) :
    RecyclerView.ViewHolder(view), View.OnClickListener {

    private var listName: TextView? = null
    private var listBaseLang: TextView? = null
    private var listTargetLang: TextView? = null

    init {
        listName = itemView.findViewById(R.id.list_title)
        listBaseLang = itemView.findViewById(R.id.base_language)
        listTargetLang = itemView.findViewById(R.id.target_language)
    }

    fun bind(noteListEntity: NoteListEntity) {
        listName?.text = noteListEntity.listName
        listBaseLang?.text = noteListEntity.baseLanguage
        listTargetLang?.text = noteListEntity.targetLanguage
        itemView.setOnClickListener(this)
    }

    override fun onClick(itemView: View?) {
        onNoteSetListener.onClick(this, adapterPosition)
    }

}