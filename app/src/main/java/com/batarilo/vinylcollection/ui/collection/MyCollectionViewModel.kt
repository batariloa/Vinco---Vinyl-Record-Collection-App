package com.batarilo.vinylcollection.ui.collection

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.batarilo.vinylcollection.data.model.Record
import com.batarilo.vinylcollection.data.model.RecordInList
import com.batarilo.vinylcollection.data.room.RecordRepository
import com.batarilo.vinylcollection.interactors.record_list.ReadAllFromCollection
import com.batarilo.vinylcollection.interactors.record_list.SearchCollectionRecords
import com.batarilo.vinylcollection.interactors.record_list.SearchRecordsApi
import com.batarilo.vinylcollection.ui.collection.recycle.RecordAdapterCollection
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyCollectionViewModel @Inject constructor(
    val recordRepository: RecordRepository,
    val searchCollectionRecords: SearchCollectionRecords,
    val readAllFromCollection: ReadAllFromCollection
)
    : ViewModel(){
    private val loading = mutableStateOf(false)
    lateinit var recordAdapter: RecordAdapterCollection



    fun readAllFromCollection(){
        readAllFromCollection.execute().onEach { dataState ->
        loading.value= dataState.loading

        dataState.data?.let { list ->
            recordAdapter.records = list
        }
            dataState.error?.let { error->
                Log.d("MYCOLLECTIONVIEWMODEL", "Here is the error: $error")
            }

        }.launchIn(viewModelScope)
    }

    fun searchCollection(query:String){

        viewModelScope.launch(Dispatchers.IO) {
            searchCollectionRecords.execute(query, recordAdapter)
        }

    }
}