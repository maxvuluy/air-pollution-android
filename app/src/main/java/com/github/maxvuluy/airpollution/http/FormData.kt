package com.github.maxvuluy.airpollution.http

import java.io.ByteArrayOutputStream
import java.io.File

@Suppress("unused")
data class FormData(
	val name: String,
	val filename: String?,
	val contentType: String?,
	val value: ByteArray
) {

	constructor(name: String, value: String) : this(name, null, null, value.toByteArray())

	constructor(name: String, contentType: String, file: File) : this(
		name,
		file.name,
		contentType,
		ByteArray(file.length().toInt())
	) {
		file.inputStream().use {
			it.read(value)
		}
	}

	val form: ByteArray
		get() = ByteArrayOutputStream().use {
			it.write(
				"Content-Disposition: ${
					buildString {
						append("form-data; name=\"$name\"")
						if (filename != null) {
							append("; filename=\"$filename\"")
						}
					}
				}".toByteArray()
			)
			it.write(HttpConnection.CRLF)
			if (contentType != null) {
				it.write("Content-Type: $contentType".toByteArray())
				it.write(HttpConnection.CRLF)
			}
			it.write(HttpConnection.CRLF)
			it.write(value)
			it.toByteArray()
		}

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (javaClass != other?.javaClass) return false

		other as FormData

		if (name != other.name) return false
		if (filename != other.filename) return false
		if (contentType != other.contentType) return false
		if (!value.contentEquals(other.value)) return false

		return true
	}

	override fun hashCode(): Int {
		var result = name.hashCode()
		result = 31 * result + (filename?.hashCode() ?: 0)
		result = 31 * result + (contentType?.hashCode() ?: 0)
		result = 31 * result + value.contentHashCode()
		return result
	}

}