package com.aflabs.coffeebliss.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Member::class, Transaction::class, RedeemHistory::class],
    version = 1,
    exportSchema = false
)
abstract class CoffeeBlissDatabase : RoomDatabase() {
    abstract val dao: CoffeeBlissDao

    companion object {
        @Volatile
        private var INSTANCE: CoffeeBlissDatabase? = null

        fun getDatabase(context: Context): CoffeeBlissDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CoffeeBlissDatabase::class.java,
                    "coffeebliss_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
