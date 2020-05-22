package pl.edu.agh.kis.flashcards.database.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import pl.edu.agh.kis.flashcards.database.entities.NoteEntity

@Dao
interface NoteDAO {

    @Query("SELECT * FROM NoteEntity WHERE listId LIKE :listId_")
    fun loadAllById(listId_: Int): LiveData<List<NoteEntity>>

    @Insert
    fun insert(vararg noteEntity: NoteEntity)

    @Query("DELETE FROM NoteEntity WHERE listId LIKE :listId_")
    fun deleteAllById(listId_: Int)

    @Delete
    fun delete(noteEntity: NoteEntity)

}