package pl.edu.agh.kis.flashcards.database.entities

import androidx.room.DatabaseView
import androidx.room.Embedded
import androidx.room.Relation
import kotlin.reflect.KClass


data class NoteListWNotes (
    @Embedded val noteListEntity: NoteListEntity,
    @Relation(parentColumn = "id" ,
    entityColumn = "listId")
    val notes:List<NoteEntity>
    )