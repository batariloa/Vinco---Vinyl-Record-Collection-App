package com.batarilo.vinylcollection.data.room.cache

sealed class BaseResult<out T:Any, out U:Any>{
    data class Success<T:Any>(val data:T) : BaseResult<T,Nothing>()
    data class Error<U:Any>(val error : U): BaseResult<Nothing, U>()
}
