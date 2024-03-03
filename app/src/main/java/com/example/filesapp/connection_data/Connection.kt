package com.example.filesapp.connection_data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

data class Connection(
    val key: String? = null,
    val ip: String,
    val port: Int
)