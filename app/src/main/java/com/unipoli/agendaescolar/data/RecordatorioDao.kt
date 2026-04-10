package com.unipoli.agendaescolar.data

import androidx.room.*

@Dao
interface RecordatorioDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecordatorio(recordatorio: Recordatorio)

    @Query("SELECT * FROM recordatorios")
    suspend fun getAllRecordatorios(): List<Recordatorio>

    @Delete
    suspend fun deleteRecordatorio(recordatorio: Recordatorio)
}