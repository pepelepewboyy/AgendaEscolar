package com.unipoli.agendaescolar.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "clases")
data class Clase(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "titulo")
    val titulo: String,

    @ColumnInfo(name = "profesor")
    val profesor: String
){
    override fun toString(): String {
        return titulo
    }
}