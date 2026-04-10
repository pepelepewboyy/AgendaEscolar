package com.unipoli.agendaescolar.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        Clase::class,
        Horario::class,
        Asistencia::class,
        Recordatorio::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun clasesDao(): ClasesDao
    abstract fun horarioDao(): HorarioDao
    abstract fun asistenciaDao(): AsistenciaDao
    abstract fun recordatorioDao(): RecordatorioDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "agenda_db"
                )
                    .fallbackToDestructiveMigration() // 🔥 importante en desarrollo
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}