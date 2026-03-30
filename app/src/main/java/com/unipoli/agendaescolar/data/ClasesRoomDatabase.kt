package com.unipoli.agendaescolar.data

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Update


@Database(
    entities = [Clase::class],
    version = 1,
    exportSchema = false
)
abstract class ClasesRoomDatabase : RoomDatabase() {

    abstract fun clasesDao(): ClasesDao

    companion object {

        @Volatile
        private var INSTANCE: ClasesRoomDatabase? = null

        fun getDatabase(context: Context): ClasesRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ClasesRoomDatabase::class.java,
                    "clases_db"
                )
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}

@Dao
interface RecordatorioDao {
    @Insert(onConflict = OnConflictStrategy.Companion.IGNORE)
    suspend fun insertRecordatorio(recordatorio: Recordatorio)
    @Update
    suspend fun updateRecordatorio(recordatorio: Recordatorio)
    @Delete
    suspend fun deleteRecordatorio(recordatorio: Recordatorio)

    @Query("""
        SELECT * FROM recordatorios
        ORDER BY
            CASE prioridad
                WHEN 'Alta' THEN 1
                WHEN 'Media' THEN 2
                WHEN 'Baja' THEN 3
                ELSE 4
            END
    """)
    suspend fun getAllRecordatorios(): List<Recordatorio>
}