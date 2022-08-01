package com.github.maxvuluy.airpollution.rpc

data class RpcSuccess<T>(val result: T) : RpcResult<T>()