package pl.edu.agh.kis.flashcards.module.main.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pl.edu.agh.kis.flashcards.R
import pl.edu.agh.kis.flashcards.database.entity.NoteListEntity

class NoteListAdapterRecycler internal constructor(
    context: Context, private val onNoteSetListener: OnNoteSetListener
) : RecyclerView.Adapter<NoteListHolder>() {
    private val inflater: LayoutInflater = LayoutInflater.from(context)

    private var noteListEntity = emptyList<NoteListEntity>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteListHolder {
        val itemView = inflater.inflate(R.layout.list_item, parent, false)
        return NoteListHolder(
            itemView,
            onNoteSetListener
        )
    }

    override fun onBindViewHolder(holder: NoteListHolder, position: Int) {
        val current: NoteListEntity = noteListEntity[position]
        holder.bind(current)
    }

    override fun getItemCount(): Int = noteListEntity.size

    fun setList(list_: List<NoteListEntity>) {
        this.noteListEntity = list_
        notifyDataSetChanged()
    }

    interface OnNoteSetListener {
        fun onClick(noteListHolder: NoteListHolder, position: Int)
    }
}
