package pl.edu.agh.kis.flashcards.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import pl.edu.agh.kis.flashcards.database.entity.SessionEntity

@Dao
interface SessionDao {

    @Query("SELECT * FROM SessionEntity order by id desc")
    fun loadLast(): SessionEntity

    @Insert
    fun insert(sessionEntity: SessionEntity): Long

    @Delete
    fun update(sessionEntity: SessionEntity)

}