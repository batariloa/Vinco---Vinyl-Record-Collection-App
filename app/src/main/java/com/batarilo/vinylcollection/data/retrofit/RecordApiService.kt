package com.batarilo.vinylcollection.data.retrofit

import com.batarilo.vinylcollection.data.model.JsonResponse
import retrofit2.Response
import retrofit2.http.*

interface RecordApiService {

    @GET("database/search")
    @Headers(
        USER_AGENT_HEADER,
        CONTENT_TYPE_HEADER
    )
   suspend fun homeSearch(
        @Query("key") auth_key:String,
        @Query ("secret") auth_secret:String,
        @Query("release_title") searchTerm:String
    ): Response<JsonResponse>


    companion object{
         const val USER_AGENT_HEADER = "User-Agent: FooBarApp/3.0"
         const val CONTENT_TYPE_HEADER = "Accept: application/json"
        const val AUTH_KEY = "aDHPMLIyOyNPkvTkCyPY"
         const val AUTH_SECRET = "RoSLDJLGyNMZXazEsuIskUmVeALHLlZE"
        const val BASE_URL = "https://api.discogs.com/"
    }

}