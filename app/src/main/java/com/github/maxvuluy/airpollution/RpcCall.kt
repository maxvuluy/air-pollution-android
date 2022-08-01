package com.github.maxvuluy.airpollution

import android.content.Context
import com.github.maxvuluy.airpollution.http.FormUrlEncoded
import com.github.maxvuluy.airpollution.http.HttpConnection
import com.github.maxvuluy.airpollution.http.HttpError
import com.github.maxvuluy.airpollution.http.HttpSuccess
import com.github.maxvuluy.airpollution.rpc.RpcHttpError
import com.github.maxvuluy.airpollution.rpc.RpcResult
import com.github.maxvuluy.airpollution.rpc.RpcSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

object RpcCall {

	suspend fun getPollutionRecords(context: Context): RpcResult<List<PollutionRecord>> {
		val parameterList = listOf(
			FormUrlEncoded("limit", "1000"),
			FormUrlEncoded("api_key", "cebebe84-e17d-4022-a28f-81097fda5896"),
			FormUrlEncoded("sort", "ImportDate desc"),
			FormUrlEncoded("format", "json")
		)
		val httpResult = withContext(Dispatchers.IO) {
			HttpConnection.httpGet("data.epa.gov.tw", "api/v2/aqx_p_432", parameterList)
		}

		val result = when (httpResult) {
			is HttpSuccess -> httpResult.result
			is HttpError -> return RpcHttpError(httpResult.responseCode)
		}

		val jsonObject = JSONObject(result)
		val recordArray = jsonObject.getJSONArray("records")

		val substitute = context.getString(R.string.good_substitute)
		val recordList = buildList(recordArray.length()) {
			for (i in 0 until recordArray.length()) {
				val recordObject = recordArray.getJSONObject(i)

				with(recordObject) {
					val siteId = getString("siteid").toInt()
					val siteName = getString("sitename")
					val county = getString("county")
					val pm2_5 = getString("pm2.5").toIntOrNull() ?: 0
					val status = getString("status")

					add(PollutionRecord(siteId, siteName, county, pm2_5, status, substitute))
				}
			}
		}

		return RpcSuccess(recordList)
	}

}