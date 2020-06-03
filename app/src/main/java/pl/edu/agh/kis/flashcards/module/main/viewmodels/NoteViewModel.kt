package pl.edu.agh.kis.flashcards.module.main.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import pl.edu.agh.kis.flashcards.database.NoteListDataBase
import pl.edu.agh.kis.flashcards.database.entity.NoteEntity
import pl.edu.agh.kis.flashcards.database.services.NoteRepository

class NoteViewModel(application: Application, id: Int) : AndroidViewModel(application) {

    private val repository: NoteRepository
    // Using LiveData and caching what getAlphabetizedWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    val notes: LiveData<List<NoteEntity>>

    init {
        val noteDao = NoteListDataBase.getDatabase(application).noteDao()
        repository = NoteRepository(noteDao)
        notes = repository.getAllById(id)
    }

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(noteEntity: NoteEntity) = viewModelScope.launch(IO) {
        repository.addNote(noteEntity)
    }

    fun delete(noteEntity: NoteEntity) = viewModelScope.launch(IO) {
        repository.delete(noteEntity)
    }

    fun update(noteEntity: NoteEntity) = viewModelScope.launch(IO) {
        repository.update(noteEntity)
    }
}