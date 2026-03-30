package com.unipoli.agendaescolar.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.unipoli.agendaescolar.data.HorarioDao




@Database(
    entities = [Clase::class, Horario::class],
    version = 1,
    exportSchema = false
)
abstract class HorarioRoomDatabase : RoomDatabase() {

    abstract fun clasesDao(): ClasesDao
    abstract fun horarioDao(): HorarioDao

    companion object {

        @Volatile
        private var INSTANCE: ClasesRoomDatabase? = null

        fun getDatabase(context: Context): ClasesRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ClasesRoomDatabase::class.java,
                    "clases_db"
                ).build()

                INSTANCE = instance
                instance
            }
        }
    }
}