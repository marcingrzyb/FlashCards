package pl.edu.agh.kis.flashcards.module.playmode.viewmodel

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pl.edu.agh.kis.flashcards.database.NoteListDataBase
import pl.edu.agh.kis.flashcards.database.entity.NoteEntity
import pl.edu.agh.kis.flashcards.database.services.NoteListRepository
import pl.edu.agh.kis.flashcards.database.services.NoteRepository


class PlayModeViewModel(application: Application) : AndroidViewModel(application) {

    private val noteRepository: NoteRepository
    private val noteListRepository: NoteListRepository

    init {
        val database = NoteListDataBase.getDatabase(application)
        noteRepository = NoteRepository(database.noteDao())
        noteListRepository = NoteListRepository(database.noteListDAO())
    }

    fun update(noteEntity: NoteEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            noteRepository.update(noteEntity)
        }

}