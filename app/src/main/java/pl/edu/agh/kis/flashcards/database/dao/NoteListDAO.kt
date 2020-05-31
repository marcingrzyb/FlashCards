package pl.edu.agh.kis.flashcards.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import pl.edu.agh.kis.flashcards.database.entity.NoteEntity
import pl.edu.agh.kis.flashcards.database.entity.NoteListEntity
import pl.edu.agh.kis.flashcards.database.entity.NoteListWNotes

@Dao
abstract class NoteListDAO {

    @Query("SELECT * FROM NoteListEntity")
    abstract fun getAll():LiveData<List<NoteListEntity>>

    @Transaction
    @Query("SELECT * FROM NoteListEntity")
    abstract suspend fun getAllWNotes(): List<NoteListWNotes>

    @Transaction
    @Query("SELECT * FROM NoteListEntity WHERE listName LIKE :listName_ LIMIT 1")
    abstract suspend fun findById(listName_: String): NoteListWNotes

    @Insert
    abstract suspend fun insert(vararg noteListEntity: NoteListEntity)

    @Transaction
    @Delete
    abstract suspend fun delete(noteListEntity: NoteListEntity)

    @Transaction
    @Update
    abstract suspend fun update(noteListEntity: NoteListEntity,noteEntity:List<NoteEntity>)

    @Update
    abstract suspend fun update(noteListEntity: NoteListEntity)

}