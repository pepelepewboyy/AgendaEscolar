package com.unipoli.agendaescolar.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo


@Entity(tableName = "recordatorios")
data class Recordatorio(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "titulo")
    val titulo: String,

    @ColumnInfo(name = "fecha")
    val fecha: String,

    @ColumnInfo(name = "hora")
    val hora: String
)