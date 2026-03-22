package com.unipoli.agendaescolar.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(
    entities = [Recordatorio::class],
    version = 1,
    exportSchema = false
)
abstract class RecordatorioRoomDatabase : RoomDatabase() {

    abstract fun recordatorioDao(): RecordatorioDao

    companion object {

        @Volatile
        private var INSTANCE: RecordatorioRoomDatabase? = null

        fun getDatabase(context: Context): RecordatorioRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RecordatorioRoomDatabase::class.java,
                    "recordatorios_db"
                )
                    // Opcional (solo para pruebas, NO en producción)
                    //.fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}