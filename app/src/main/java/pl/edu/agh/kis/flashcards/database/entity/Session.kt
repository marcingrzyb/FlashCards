package pl.edu.agh.kis.flashcards.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Session(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    @ColumnInfo(name = "startTime") val startTime: Long?,
    @ColumnInfo(name = "endTime") val endTime: Long?
)