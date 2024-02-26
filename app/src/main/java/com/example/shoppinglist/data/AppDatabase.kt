package com.example.shoppinglist.data

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [ShopItemDbModel::class], version = 1, exportSchema = false)
abstract class AppDatabase() : RoomDatabase() {
    abstract fun shopListDao(): ShopListDao

    companion object {
        private val LOCK = Any()
        private var INSTANCE: AppDatabase? = null
        private val NAME_OF_DATABASE = "shop_items.db"

        fun createInstance(app: Application): AppDatabase {
            INSTANCE?.let { return it }
            synchronized(LOCK) {
                INSTANCE?.let { return it }

                val db =
                    Room.databaseBuilder(app, AppDatabase::class.java, NAME_OF_DATABASE)
                        .allowMainThreadQueries()
                        .build()
                INSTANCE = db
                return db
            }
        }
    }
}