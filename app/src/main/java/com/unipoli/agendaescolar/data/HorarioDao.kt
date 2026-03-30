package com.unipoli.agendaescolar.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update
import androidx.room.Delete
import androidx.room.Query


@Dao
interface HorarioDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun inserthorario(horario: Horario)
    @Update
    suspend fun updatehorario(horario: Horario)
    @Delete
    suspend fun deletehorario(horario: Horario)

    @Query("""
        SELECT * FROM horario
    """)
    suspend fun getAllhorarios(): List<Horario>
    @Query("""
    SELECT * FROM horario 
    WHERE dia = :diaActual 
    ORDER BY horaInicio
""")
    suspend fun getHorariosPorDia(diaActual: Int): List<Horario>
}