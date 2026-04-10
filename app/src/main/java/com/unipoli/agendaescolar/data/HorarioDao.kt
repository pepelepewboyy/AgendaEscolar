package com.unipoli.agendaescolar.data

import androidx.room.*

@Dao
interface HorarioDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertHorario(horario: Horario)

    @Update
    suspend fun updateHorario(horario: Horario)

    @Delete
    suspend fun deleteHorario(horario: Horario)

    @Query("SELECT * FROM horario")
    suspend fun getAllHorarios(): List<Horario>
    @Query("""
    SELECT clases.titulo, clases.profesor, horario.horaInicio, horario.horaFin
    FROM horario
    INNER JOIN clases ON horario.idClase = clases.id
    """)
    suspend fun getClasesConHorario(): List<ClaseConHorario>

    @Query("""
        SELECT * FROM horario 
        WHERE dia = :diaActual 
        ORDER BY horaInicio
    """)
    suspend fun getHorariosPorDia(diaActual: String): List<Horario>

    @Query("""
    SELECT clases.titulo, clases.profesor, horario.horaInicio, horario.horaFin
    FROM horario
    INNER JOIN clases ON horario.idClase = clases.id
    WHERE LOWER(horario.dia) = LOWER(:dia)
    ORDER BY horario.horaInicio
""")
    suspend fun getClasesPorDia(dia: String): List<ClaseConHorario>
}

