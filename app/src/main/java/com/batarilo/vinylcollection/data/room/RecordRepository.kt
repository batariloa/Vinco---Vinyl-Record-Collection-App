package com.batarilo.vinylcollection.data.room

import androidx.lifecycle.LiveData
import com.batarilo.vinylcollection.data.model.ListType
import com.batarilo.vinylcollection.data.model.Record
import com.batarilo.vinylcollection.data.model.RecordInList
import javax.inject.Inject

class RecordRepository  @Inject constructor(private val recordDao: RecordDao) {



    suspend fun addRecordToCollection(record: Record){

        recordDao.addRecord(RecordInList(0,record, ListType.COLLECTION))
    }

    suspend fun addRecordToWishlist(record: Record) {

        recordDao.addRecord(RecordInList(0,record, ListType.WISHLIST))
    }

    suspend fun addRecordToHistory(record: Record) {

        recordDao.addRecord(RecordInList(0,record, ListType.HISTORY))
    }


    suspend fun deleteRecordInList(record: RecordInList){
        recordDao.deleteRecordInList(record)
    }


    fun readFromWishlist(): LiveData<List<RecordInList>> {
     return recordDao.readWishList()
    }

    fun readFromCollection(): LiveData<List<RecordInList>> {
        return recordDao.readCollection()
    }

    suspend fun updateRecord(record: RecordInList){
        return recordDao.updateRecord(record)
    }

}