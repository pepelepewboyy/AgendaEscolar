package com.unipoli.agendaescolar

@Entity(tableName = "recordatorios")
data class Recordatorio(
    @PrimaryKey(autoGenerate = true)
    val id: Int=0,
    val titulo: String,
    val fecha: String,
    val hora: String


)