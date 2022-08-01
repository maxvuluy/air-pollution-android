package com.github.maxvuluy.airpollution

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.github.maxvuluy.airpollution.rpc.RpcHttpError
import com.github.maxvuluy.airpollution.rpc.RpcSuccess
import kotlinx.coroutines.launch
import org.json.JSONException
import java.io.InterruptedIOException
import java.net.ConnectException
import java.net.UnknownHostException

class MainViewModel(application: Application) : AndroidViewModel(application) {

	companion object {
		private const val THRESHOLD = 30
	}

	val goodRecordList = MutableLiveData<List<PollutionRecord>>()
	val badRecordList = MutableLiveData<List<PollutionRecord>>()

	init {
		viewModelScope.launch {
			handleRecords(getPollutionRecords())
		}
	}

	private suspend fun getPollutionRecords() = try {
		when (val rpcResult = RpcCall.getPollutionRecords(getApplication())) {
			is RpcSuccess -> rpcResult.result
			is RpcHttpError -> null
		}
	} catch (e: ConnectException) {
		e.printStackTrace()
		null
	} catch (e: InterruptedIOException) {
		e.printStackTrace()
		null
	} catch (e: UnknownHostException) {
		e.printStackTrace()
		null
	} catch (e: JSONException) {
		e.printStackTrace()
		null
	}

	private fun handleRecords(recordList: List<PollutionRecord>?) {
		if (recordList == null) {
			// TODO handle error
			return
		}

		getApplication<TapiaApplication>().recordList = recordList

		val goodAndBad = recordList.partition {
			it.pm2_5 <= THRESHOLD
		}

		goodRecordList.postValue(goodAndBad.first)
		badRecordList.postValue(goodAndBad.second)
	}

}