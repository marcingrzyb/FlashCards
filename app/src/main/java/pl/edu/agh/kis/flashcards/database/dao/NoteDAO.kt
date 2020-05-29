package pl.edu.agh.kis.flashcards.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import pl.edu.agh.kis.flashcards.database.entity.NoteEntity

@Dao
interface NoteDAO {

    @Query("SELECT * FROM NoteEntity WHERE listId LIKE :listId_")
    fun loadAllById(listId_: Int): LiveData<List<NoteEntity>>

    @Update
    fun update(vararg noteEntity: NoteEntity)

    @Insert
    fun insert(vararg noteEntity: NoteEntity)

    @Query("DELETE FROM NoteEntity WHERE listId LIKE :listId_")
    fun deleteAllById(listId_: Int)

    @Delete
    fun delete(noteEntity: NoteEntity)

}