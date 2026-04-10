package com.unipoli.agendaescolar.data

import androidx.room.*

@Entity(
    tableName = "asistencia",
    foreignKeys = [
        ForeignKey(
            entity = Clase::class,
            parentColumns = ["id"],
            childColumns = ["idClase"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value=["idClase", "fecha"], unique = true)]
)
data class Asistencia(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val idClase: Int,

    val fecha: String,

    val estado: String // "Presente", "Falta", "Retardo"
)