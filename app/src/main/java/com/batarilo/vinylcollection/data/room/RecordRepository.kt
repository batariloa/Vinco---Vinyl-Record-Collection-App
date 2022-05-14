package com.batarilo.vinylcollection.data.room

import androidx.lifecycle.LiveData
import com.batarilo.vinylcollection.data.model.Record
import com.batarilo.vinylcollection.data.model.RecordType
import javax.inject.Inject

class RecordRepository  @Inject constructor(private val recordDao: RecordDao) {

   fun readAllData(): LiveData<List<Record>>{
       return recordDao.readAllData()
   }

    suspend fun addRecordToCollection(record: Record){
        record.belongsTo = RecordType.COLLECTION
        recordDao.addRecord(record)
    }

    suspend fun addRecordToWishlist(record: Record) {
        record.belongsTo = RecordType.WISHLIST
        recordDao.addRecord(record)
    }

    suspend fun removeRecord(record:Record){
        recordDao.deleteRecord(record)
    }

    fun readFromWishlist(): LiveData<List<Record>> {
     return recordDao.readWishList()
    }

    fun readFromCollection(): LiveData<List<Record>> {
        return recordDao.readCollection()
    }
}