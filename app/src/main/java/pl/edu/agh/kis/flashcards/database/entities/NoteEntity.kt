package pl.edu.agh.kis.flashcards.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class NoteEntity(
    @PrimaryKey val listId: Int,
    @ColumnInfo(name = "Word") val word: String,
    @ColumnInfo(name = "Translated Word") val translatedWord: String?
)
