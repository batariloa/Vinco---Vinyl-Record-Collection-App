package com.batarilo.vinylcollection.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.batarilo.vinylcollection.data.model.Record
import com.batarilo.vinylcollection.data.model.RecordData
import com.batarilo.vinylcollection.data.model.RecordInList

@Database(entities = [Record::class, RecordData::class], version = 38, exportSchema = false)
@TypeConverters(RecordConverters::class)
abstract class RecordDatabase
    : RoomDatabase(){

    abstract fun recordDao():RecordDao

    companion object{
        @Volatile
        private var INSTANCE: RecordDatabase? = null

        fun getDatabase(context: Context): RecordDatabase{
            val tempInstance = INSTANCE
            if(tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RecordDatabase::class.java,
                    "record_database")
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }

}