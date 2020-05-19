package pl.edu.agh.kis.flashcards.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class NoteListEntity(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    @ColumnInfo(name="listName") val listName: String?,
    @ColumnInfo(name = "Language") val toLanguage: String?
)