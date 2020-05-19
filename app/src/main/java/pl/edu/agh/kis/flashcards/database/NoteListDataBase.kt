package pl.edu.agh.kis.flashcards.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import pl.edu.agh.kis.flashcards.database.daos.NoteDAO
import pl.edu.agh.kis.flashcards.database.daos.NoteListDAO
import pl.edu.agh.kis.flashcards.database.entities.NoteEntity
import pl.edu.agh.kis.flashcards.database.entities.NoteListEntity

@Database(entities = arrayOf(NoteListEntity::class,NoteEntity::class), version = 2, exportSchema = false)
abstract class NoteListDataBase : RoomDatabase() {
    abstract fun noteListDAO(): NoteListDAO
    abstract fun noteDao():NoteDAO


    private class NoteListDataBaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    var noteListDAO = database.noteListDAO()
                    noteListDAO.insert(NoteListEntity(null, listName = "1", toLanguage = "eng"))

                    noteListDAO.insert(NoteListEntity(null,listName = "2",toLanguage = "eng"))

                }
            }
        }
    }


    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: NoteListDataBase? = null

        fun getDatabase(context: Context,scope: CoroutineScope): NoteListDataBase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NoteListDataBase::class.java,
                    "noteList_database"
                ).addCallback(NoteListDataBaseCallback(scope)).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}