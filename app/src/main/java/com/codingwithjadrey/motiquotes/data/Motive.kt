package com.codingwithjadrey.motiquotes.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "motive_item")
data class Motive(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "creationDate") val createdOn: String,
    @ColumnInfo(name = "motiveQuote") val motiveQuote: String
)
