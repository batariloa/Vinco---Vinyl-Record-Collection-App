package com.batarilo.vinylcollection.interactors.record_list

import com.batarilo.vinylcollection.data.DatabaseFake
import com.batarilo.vinylcollection.data.RecordDaoFake
import com.batarilo.vinylcollection.data.retrofit.RecordApiService
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import okhttp3.HttpUrl
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.HttpURLConnection

class SearchRecordsTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var baseUrl: HttpUrl
    val dummyQuery = "Zeppelin"

    //system in test
    private lateinit var searchRecords:SearchRecords

    //dependencies
    private lateinit var recordService: RecordApiService
    private lateinit var databaseFake: DatabaseFake
    private lateinit var recordDaoFake: RecordDaoFake

    @BeforeEach
    fun setup(){

        mockWebServer = MockWebServer()
        mockWebServer.start()

        baseUrl = mockWebServer.url("/")

        recordService = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create(RecordApiService::class.java)

        databaseFake = DatabaseFake()
        recordDaoFake = RecordDaoFake(databaseFake)

        //ready to instantiate system
        searchRecords= SearchRecords(
            recordDaoFake,
            recordService
        )
    }

    @AfterEach
    fun tearDown(){
        mockWebServer.shutdown()
    }

    /**
     * What we are testing:
     * 1. Are records retrieved from the network? (Check if cache is empty, then check if its filled)
     * 2. Are the records inserted into the cache?
     * 3. Are the records then emitted to the UI? (as a flow)
     */
    @Test
    fun geRecordsFromNetwork_emitRecordFromCache() :Unit {
        runBlocking {
            mockWebServer.enqueue(
                MockResponse()
                    .setResponseCode(HttpURLConnection.HTTP_OK)
                    .setBody(WebMockServerResponse.recordListResponse)
            )

            //confirm that cache is empty
            assert(recordDaoFake.readAllData().isEmpty())

            val flowItems = searchRecords.execute(dummyQuery, isNetworkAvailable = true).toList()

            //confirm cache
            assert(recordDaoFake.readAllData().isNotEmpty())




        }
        }



}