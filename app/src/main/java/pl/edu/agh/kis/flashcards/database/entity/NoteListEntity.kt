package pl.edu.agh.kis.flashcards.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class NoteListEntity(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    @ColumnInfo(name="listName") var listName: String?,
    @ColumnInfo(name = "baseLanguage") var baseLanguage: String?,
    @ColumnInfo(name = "targetLanguage") var targetLanguage: String?
)