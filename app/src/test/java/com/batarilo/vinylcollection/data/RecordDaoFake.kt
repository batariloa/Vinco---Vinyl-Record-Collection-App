package com.batarilo.vinylcollection.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.batarilo.vinylcollection.data.model.Record
import com.batarilo.vinylcollection.data.model.RecordInList
import com.batarilo.vinylcollection.data.room.RecordDao

class RecordDaoFake (
    private val databaseFake: DatabaseFake
    ): RecordDao{

    override suspend fun addRecord(record: RecordInList) {
    databaseFake.recordsInList.add(record)
    databaseFake.recordsInList.add(record)
    }

    override suspend fun addRecords(list: List<Record>): LongArray {
        databaseFake.records.addAll(list)
        return longArrayOf(1)

    }

    override suspend fun readAllData(): List<Record> {
        return databaseFake.records
    }

    override suspend fun searchRecords(query: String): List<Record> {
        return databaseFake.records //return all records to keep it simple
    }

    override fun readWishList(): LiveData<List<RecordInList>> {
    return liveData { databaseFake.records }
     }

    override fun readCollection(): LiveData<List<RecordInList>> {
        return liveData { databaseFake.records }
    }

    override fun readHistory(): LiveData<List<RecordInList>> {
        return liveData { databaseFake.records }
    }


    override suspend fun deleteRecordInList(record: RecordInList) {
        TODO("Not yet implemented")
    }

    override suspend fun updateRecord(record: RecordInList) {
        TODO("Not yet implemented")
    }

}