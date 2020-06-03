package pl.edu.agh.kis.flashcards.database.services

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import pl.edu.agh.kis.flashcards.database.dao.NoteDAO
import pl.edu.agh.kis.flashcards.database.entity.NoteEntity
import pl.edu.agh.kis.flashcards.database.entity.NoteListEntity

class NoteRepository(private val noteDAO: NoteDAO) {

    fun getAllById(id:Int): LiveData<List<NoteEntity>> {
        return noteDAO.loadAllById(id)
    }

    fun loadList(id:Int): List<NoteEntity> {
        return noteDAO.loadList(id)
    }

    fun addNote(noteEntity: NoteEntity){
        noteDAO.insert(noteEntity)
    }

    fun delete(noteEntity: NoteEntity){
        noteDAO.delete(noteEntity)
    }

    fun update(noteEntity: NoteEntity){
        noteDAO.update(noteEntity)
    }

}