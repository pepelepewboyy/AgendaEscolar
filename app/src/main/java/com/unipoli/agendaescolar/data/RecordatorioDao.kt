package com.unipoli.agendaescolar.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update
import androidx.room.Delete
import androidx.room.Query
import com.unipoli.agendaescolar.data.Recordatorio



@Dao
interface RecordatorioDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertRecordatorio(recordatorio: Recordatorio)
    @Update
    suspend fun updateRecordatorio(recordatorio: Recordatorio)
    @Delete
    suspend fun deleteRecordatorio(recordatorio: Recordatorio)
    @Query("SELECT * FROM recordatorios")
    suspend fun getAllRecordatorios(): List<Recordatorio>

}