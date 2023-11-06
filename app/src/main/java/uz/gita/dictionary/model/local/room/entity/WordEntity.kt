package uz.gita.dictionary.model.local.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dictionary")
data class WordEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val english: String?,
    val type: String?,
    val transcript: String?,
    val uzbek: String?,
    val countable: String?,
    @ColumnInfo("is_favourite")
    val isFavourite: Int?
)