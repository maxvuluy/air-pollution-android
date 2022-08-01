package com.github.maxvuluy.airpollution.http

data class FormUrlEncoded(val name: String, val value: String) {

	val form
		get() = "$name=$value"

}