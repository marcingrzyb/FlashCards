package pl.edu.agh.kis.flashcards.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import pl.edu.agh.kis.flashcards.database.dao.NoteDAO
import pl.edu.agh.kis.flashcards.database.dao.NoteListDAO
import pl.edu.agh.kis.flashcards.database.dao.SessionDao
import pl.edu.agh.kis.flashcards.database.entity.NoteEntity
import pl.edu.agh.kis.flashcards.database.entity.NoteListEntity
import pl.edu.agh.kis.flashcards.database.entity.Session

@Database(entities = arrayOf(NoteListEntity::class, NoteEntity::class, Session::class), version = 1, exportSchema = false)
abstract class NoteListDataBase : RoomDatabase() {

    abstract fun noteListDAO(): NoteListDAO
    abstract fun noteDao(): NoteDAO
    abstract fun sessionDao(): SessionDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: NoteListDataBase? = null

        fun getDatabase(context: Context): NoteListDataBase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NoteListDataBase::class.java,
                    "noteList_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }

}