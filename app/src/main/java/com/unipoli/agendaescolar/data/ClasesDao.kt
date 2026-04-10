package com.unipoli.agendaescolar.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update
import androidx.room.Delete
import androidx.room.Query


@Dao
interface ClasesDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertclase(clase: Clase)
    @Update
    suspend fun updateclase(clase: Clase)
    @Delete
    suspend fun deleteclase(clase: Clase)

    @Query("""
        SELECT * FROM clases
    """)
    suspend fun getAllClases(): List<Clase>
}