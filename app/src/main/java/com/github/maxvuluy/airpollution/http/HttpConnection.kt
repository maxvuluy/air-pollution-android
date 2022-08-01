package com.github.maxvuluy.airpollution.http

import android.util.Base64
import android.util.Log
import java.net.HttpURLConnection
import java.net.URI
import java.net.URL
import java.util.*

@Suppress("unused")
object HttpConnection {

	private const val TAG = "HttpConnection"

	internal val CRLF = byteArrayOf('\r'.code.toByte(), '\n'.code.toByte())
	private val DASH_DASH = ByteArray(2) {
		'-'.code.toByte()
	}

	fun httpGet(host: String, file: String?, parameterList: List<FormUrlEncoded>?): HttpResult {
		val path = file?.let {
			"/$it"
		}
		val query = parameterList?.joinToString("&") {
			it.form
		}
		val httpURLConnection = URI("https", host, path, query, null).toURL().also {
			Log.d(TAG, "url: $it")
		}.openConnection() as HttpURLConnection

		return connect(httpURLConnection.apply {
			connectTimeout = 10000
			readTimeout = 10000
		})
	}

	fun httpGet(url: String, username: String?, password: String?): HttpResult {
		val httpURLConnection = URL(url).also {
			Log.d(TAG, "url: $it")
		}.openConnection() as HttpURLConnection

		return connect(httpURLConnection.apply {
			if (!username.isNullOrEmpty() && !password.isNullOrEmpty()) {
				setRequestProperty(
					"Authorization",
					"Basic ${
						Base64.encodeToString("$username:$password".toByteArray(), Base64.NO_WRAP)
					}"
				)
			}
			connectTimeout = 10000
			readTimeout = 10000
		})
	}

	fun httpPost(host: String, file: String?, parameterList: List<FormData>?): HttpResult {
		val httpURLConnection = URL("https", host, file).also {
			Log.d(TAG, "url: $it")
		}.openConnection() as HttpURLConnection

		return connect(httpURLConnection.apply {
			connectTimeout = 10000
			readTimeout = 10000
			doOutput = true
			requestMethod = "POST"

			val boundary = UUID.randomUUID().toString().also {
				setRequestProperty("Content-Type", "multipart/form-data; boundary=$it")
			}
			outputStream.use {
				parameterList?.forEach { parameter ->
					it.write(DASH_DASH)
					it.write(boundary.toByteArray())
					it.write(CRLF)

					it.write(parameter.form)
					it.write(CRLF)
				}

				it.write(DASH_DASH)
				it.write(boundary.toByteArray())
				it.write(DASH_DASH)
			}
		})
	}

	fun httpPost(host: String, file: String?, contentType: String, body: ByteArray?): HttpResult {
		val httpURLConnection = URL("https", host, file).also {
			Log.d(TAG, "url: $it")
		}.openConnection() as HttpURLConnection

		return connect(httpURLConnection.apply {
			connectTimeout = 10000
			readTimeout = 10000
			doOutput = true
			requestMethod = "POST"

			setRequestProperty("Content-Type", contentType)
			outputStream.use {
				it.write(body)
			}
		})
	}

	private fun connect(httpURLConnection: HttpURLConnection): HttpResult {
		val responseCode = httpURLConnection.responseCode.also {
			Log.d(TAG, "responseCode: $it")
		}
		return if (responseCode == HttpURLConnection.HTTP_OK) {
			httpURLConnection.inputStream.reader().use {
				HttpSuccess(it.readText())
			}
		} else {
			HttpError(responseCode)
		}
	}

}