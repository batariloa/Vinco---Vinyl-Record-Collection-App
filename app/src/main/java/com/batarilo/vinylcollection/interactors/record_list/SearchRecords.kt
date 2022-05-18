package com.batarilo.vinylcollection.interactors.record_list

import android.util.Log
import com.batarilo.vinylcollection.data.model.JsonResponse
import com.batarilo.vinylcollection.data.model.Record
import com.batarilo.vinylcollection.data.retrofit.RecordApiService
import com.batarilo.vinylcollection.data.room.RecordDao
import com.batarilo.vinylcollection.data.room.cache.DataState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SearchRecords(
    private val recordDao: RecordDao,
    private val recordApiService: RecordApiService,

){
    fun execute(
        query:String,
        isNetworkAvailable:Boolean
    ): Flow<DataState<List<Record>>> = flow{
        try {
            emit(DataState.loading())
            //just to show progress bar because api is fast
            delay(1000)

            if(query=="errorForce")
                throw Exception("Search FAILED!")

            Log.d("CACHE", "IS network available? $isNetworkAvailable")
            if(isNetworkAvailable) {
                val records = getRecordFromNetwork(query)
                //insert into cache
                Log.d("CACHE", "Adding to cache ${records.results}")

                recordDao.addRecords(records.results)
            }
            //query the cache
            val cacheResult = if(query.isBlank()){
                recordDao.readAllData()
            }
            else{
                getRecordFromCache(query)
            }
            Log.d("CACHE","CACHE RESULT $cacheResult")
            //emit list from the cache
            emit(DataState.success(cacheResult))

        }catch (e:Exception){
            emit(DataState.error(e.message?:"Unknown error"))
        }
    }
    private suspend fun getRecordFromNetwork(query:String): JsonResponse {
        return recordApiService.searchDiscogResponse(
            RecordApiService.AUTH_KEY,
            RecordApiService.AUTH_SECRET,
            query,
            RecordApiService.TYPE_RELEASE
        )
    }
    private suspend fun getRecordFromCache(query: String):List<Record>{
        return recordDao.searchRecords(query)
    }
}