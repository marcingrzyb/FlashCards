package pl.edu.agh.kis.flashcards.database.entity

import androidx.room.Embedded
import androidx.room.Relation

data class NoteListWNotes(
    @Embedded val noteListEntity: NoteListEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "listId"
    )
    val notes: List<NoteEntity>
)