package pl.edu.agh.kis.flashcards.module.playmode.viewmodel

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pl.edu.agh.kis.flashcards.database.NoteListDataBase
import pl.edu.agh.kis.flashcards.database.entity.NoteEntity
import pl.edu.agh.kis.flashcards.database.entity.NoteListEntity
import pl.edu.agh.kis.flashcards.database.services.NoteListRepository
import pl.edu.agh.kis.flashcards.database.services.NoteRepository
import pl.edu.agh.kis.flashcards.database.services.SessionRepository


class PlayModeViewModel(application: Application) : AndroidViewModel(application) {

    private val noteRepository: NoteRepository
    private val noteListRepository: NoteListRepository
    private val sessionRepository: SessionRepository

    lateinit var allNoteLists: List<NoteEntity>


    init {
        val database = NoteListDataBase.getDatabase(application)
        noteRepository = NoteRepository(database.noteDao())
        noteListRepository = NoteListRepository(database.noteListDAO())
        sessionRepository = SessionRepository(database.sessionDao())

    }

    fun get(id: Int) =
        viewModelScope.launch(Dispatchers.IO) {
            noteRepository.get(id)
        }

    fun update(noteEntity: NoteEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            noteRepository.update(noteEntity)
        }
//
//    fun update(noteListEntity: NoteListEntity) =
//        viewModelScope.launch(Dispatchers.IO) {
//            repository.updateNoteList(noteListEntity)
//        }
//
//    fun delete(noteListEntity: NoteListEntity) = viewModelScope.launch(Dispatchers.IO) {
//        repository.deleteNoteListSet(noteListEntity)
//    }

}