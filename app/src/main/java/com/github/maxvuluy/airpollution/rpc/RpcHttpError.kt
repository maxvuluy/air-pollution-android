package com.github.maxvuluy.airpollution.rpc

data class RpcHttpError<T>(val responseCode: Int) : RpcResult<T>()