package com.unipoli.agendaescolar.data

import androidx.room.*

@Dao
interface AsistenciaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAsistencia(asistencia: Asistencia)

    @Update
    suspend fun updateAsistencia(asistencia: Asistencia)

    @Delete
    suspend fun deleteAsistencia(asistencia: Asistencia)

    @Query("SELECT * FROM asistencia")
    suspend fun getAllAsistencias(): List<Asistencia>

    @Query("""
        SELECT * FROM asistencia
        WHERE fecha = :fecha
    """)
    suspend fun getAsistenciaPorFecha(fecha: String): List<Asistencia>

    @Query("""
        SELECT * FROM asistencia
        WHERE idClase = :idClase
    """)
    suspend fun getAsistenciaPorClase(idClase: Int): List<Asistencia>
    @Query("""
    SELECT asistencia.fecha, asistencia.estado, clases.titulo
    FROM asistencia
    INNER JOIN clases ON asistencia.idClase = clases.id
    WHERE fecha = :fecha
""")
    suspend fun getAsistenciaConClase(fecha: String): List<AsistenciaConClase>
}