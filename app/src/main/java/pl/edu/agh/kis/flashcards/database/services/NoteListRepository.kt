package pl.edu.agh.kis.flashcards.database.services

import androidx.lifecycle.LiveData
import pl.edu.agh.kis.flashcards.database.daos.NoteListDAO
import pl.edu.agh.kis.flashcards.database.entities.NoteListEntity

class NoteListRepository(private val noteListDAO: NoteListDAO) {
    val allNoteLists:LiveData<List<NoteListEntity>> = noteListDAO.getAll()

    suspend fun addNewNoteList(noteListEntity: NoteListEntity){
        noteListDAO.insert(noteListEntity)
    }
}