package com.codingwithjadrey.motiquotes.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.codingwithjadrey.motiquotes.data.dao.MotiveDao
import com.codingwithjadrey.motiquotes.data.entity.Motive

@Database(entities = [Motive::class], version = 2, exportSchema = false)
abstract class MotiveRoomDatabase : RoomDatabase() {
    abstract fun motiveDao(): MotiveDao

    companion object {
        @Volatile
        private var DATABASE_INSTANCE: MotiveRoomDatabase? = null
        fun getDatabase(context: Context): MotiveRoomDatabase {
            return DATABASE_INSTANCE ?: synchronized(this) {
                val databaseInstance = Room.databaseBuilder(
                    context.applicationContext,
                    MotiveRoomDatabase::class.java,
                    "motivation_quotes_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                DATABASE_INSTANCE = databaseInstance
                databaseInstance
            }
        }
    }
}