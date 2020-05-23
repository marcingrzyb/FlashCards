package pl.edu.agh.kis.flashcards.database.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import pl.edu.agh.kis.flashcards.database.entities.NoteEntity
import pl.edu.agh.kis.flashcards.database.entities.NoteListEntity
import pl.edu.agh.kis.flashcards.database.entities.NoteListWNotes

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
    abstract suspend fun delete(noteListEntity: NoteListEntity,noteEntity:List<NoteEntity>)

    @Transaction
    @Update
    abstract suspend fun update(noteListEntity: NoteListEntity,noteEntity:List<NoteEntity>)

    @Update
    abstract suspend fun update(noteListEntity: NoteListEntity)

}