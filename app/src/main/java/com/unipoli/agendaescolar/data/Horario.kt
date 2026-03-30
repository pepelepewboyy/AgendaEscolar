package com.unipoli.agendaescolar.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import androidx.room.Index


@Entity(tableName = "horario",
    foreignKeys =[
        ForeignKey(
            entity = Clase::class,
            parentColumns = ["id"],
            childColumns = ["idClase"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("idClase")]
)
data class Horario(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "idClase")
    val idClase: Int,
    @ColumnInfo(name = "dia")
    val dia: String, // "Lunes", "Martes", etc.
    @ColumnInfo(name = "horaInicio")
    val horaInicio: String,
    @ColumnInfo(name = "horaFin")
    val horaFin: String
)