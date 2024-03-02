package io.ssafy.mogeun.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "keyValue")
data class Key (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo(name = "user_key")
    val userKey: Int
)
