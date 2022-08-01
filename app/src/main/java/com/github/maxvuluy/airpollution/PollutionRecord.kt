package com.github.maxvuluy.airpollution

import android.view.View

data class PollutionRecord(
	val siteId: Int,
	val siteName: String,
	val country: String,
	val pm2_5: Int,
	val status: String,
	private val substitute: String
) {

	val subStatus
		get() = if (status == "良好") substitute else status
	val arrow
		get() = if (status == "良好") View.GONE else View.VISIBLE

}
