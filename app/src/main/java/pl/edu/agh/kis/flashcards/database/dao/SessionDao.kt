package pl.edu.agh.kis.flashcards.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import pl.edu.agh.kis.flashcards.database.entity.Session

@Dao
interface SessionDao {

    @Query("SELECT * FROM Session WHERE id LIKE :sessionId")
    fun load(sessionId: Int): Session

    @Insert
    fun insert(session: Session): Long

    @Delete
    fun update(session: Session)

}