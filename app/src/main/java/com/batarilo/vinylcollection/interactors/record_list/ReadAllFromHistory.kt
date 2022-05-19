package com.batarilo.vinylcollection.interactors.record_list

import com.batarilo.vinylcollection.data.model.RecordInList
import com.batarilo.vinylcollection.data.room.RecordDao
import com.batarilo.vinylcollection.data.room.cache.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.Exception

class ReadAllFromHistory(val recordDao: RecordDao) {

    fun execute(): Flow<DataState<List<RecordInList>>> = flow{

        try{
            emit(DataState.loading())

            val records = recordDao.readCollection()
            emit(DataState.success(records))

        }
        catch (e: Exception){
            emit(DataState.error(e.message?:"Unknown error"))
            println("Here is the errror: $e")

        }
    }
}