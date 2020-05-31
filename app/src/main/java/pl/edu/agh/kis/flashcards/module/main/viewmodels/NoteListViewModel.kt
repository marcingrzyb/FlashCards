package pl.edu.agh.kis.flashcards.module.main.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import pl.edu.agh.kis.flashcards.database.NoteListDataBase
import pl.edu.agh.kis.flashcards.database.entity.NoteEntity
import pl.edu.agh.kis.flashcards.database.entity.NoteListEntity
import pl.edu.agh.kis.flashcards.database.services.NoteListRepository

public class NoteListViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: NoteListRepository

    // Using LiveData and caching what getAlphabetizedWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    val allNoteLists: LiveData<List<NoteListEntity>>

    init {
        val notelistDao = NoteListDataBase.getDatabase(application).noteListDAO()
        repository = NoteListRepository(notelistDao)
        allNoteLists = repository.allNoteLists
    }

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(noteListEntity: NoteListEntity) = viewModelScope.launch(IO) {
        repository.addNewNoteList(noteListEntity)
    }

    fun update(noteListEntity: NoteListEntity, noteEntities: List<NoteEntity>) =
        viewModelScope.launch(IO) {
            repository.updateNoteList(noteListEntity, noteEntities)
        }

    fun delete(noteListEntity: NoteListEntity) = viewModelScope.launch(IO) {
        repository.deleteNoteListSet(noteListEntity)
    }
}
