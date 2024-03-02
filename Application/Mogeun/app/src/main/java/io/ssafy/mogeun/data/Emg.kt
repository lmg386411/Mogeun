package io.ssafy.mogeun.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "emgValue")
data class Emg (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo(name = "device_id")
    val deviceId: Int,
    @ColumnInfo(name = "sensing_part")
    val sensingPart: String,
    val value: Int,
    val time: Long
)