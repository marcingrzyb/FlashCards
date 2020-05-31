package pl.edu.agh.kis.flashcards.module.main.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pl.edu.agh.kis.flashcards.R
import pl.edu.agh.kis.flashcards.database.entity.NoteEntity

class NoteAdapter internal constructor(
    context: Context, private val onNoteSetListener: OnNoteSetListener
) : RecyclerView.Adapter<NoteHolder>() {
    private val inflater: LayoutInflater = LayoutInflater.from(context)

    private var noteEnitites = emptyList<NoteEntity>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteHolder {
        val itemView = inflater.inflate(R.layout.list_item, parent, false)
        return NoteHolder(
            itemView,
            onNoteSetListener
        )
    }

    override fun onBindViewHolder(holder: NoteHolder, position: Int) {
        val current: NoteEntity = noteEnitites[position]
        holder.bind(current)
    }

    override fun getItemCount(): Int = noteEnitites.size

    fun setList(list_: List<NoteEntity>) {
        this.noteEnitites = list_
        notifyDataSetChanged()
    }

    interface OnNoteSetListener {
        fun onClick(noteHolder: NoteHolder, position: Int)
        fun onLongClick(noteListHolder: NoteHolder, position: Int)

    }
}
