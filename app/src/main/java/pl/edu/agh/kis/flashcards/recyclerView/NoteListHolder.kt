package pl.edu.agh.kis.flashcards.recyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pl.edu.agh.kis.flashcards.R
import pl.edu.agh.kis.flashcards.database.entities.NoteListEntity

class NoteListHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.list_item, parent, false)) {


        private var listName: TextView? = null
        private var listLang: TextView? = null


        init {
            listName = itemView.findViewById(R.id.list_title)
            listLang = itemView.findViewById(R.id.list_description)
        }

        fun bind(noteListEntity: NoteListEntity) {
            listName?.text = noteListEntity.listName
            listLang?.text = noteListEntity.toLanguage
        }


}