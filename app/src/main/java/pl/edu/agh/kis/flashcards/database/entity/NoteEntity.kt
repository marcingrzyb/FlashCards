package pl.edu.agh.kis.flashcards.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class NoteEntity (
    @PrimaryKey(autoGenerate = true) val id: Int?,
    @ColumnInfo(name="listId") val listId: Int,
    @ColumnInfo(name = "Word") var word: String,
    @ColumnInfo(name = "TranslatedWord") var translatedWord: String?,
    @ColumnInfo(name = "favourite") var favourite: Boolean?,
    @ColumnInfo(name = "remebered") var remembered: Boolean?
):Serializable
