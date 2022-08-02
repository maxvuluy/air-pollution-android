package com.github.maxvuluy.airpollution

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations

class SearchViewModel(application: Application) : AndroidViewModel(application) {

	val siteName = MutableLiveData("")
	val recordList = Transformations.map(siteName) {
		if (it.isNotEmpty()) {
			getApplication<TapiaApplication>().recordList.filter { record ->
				record.siteName.contains(it) || record.county.contains(it)
			}
		} else {
			emptyList()
		}
	}
	val emptyText = Transformations.map(siteName) {
		application.run {
			if (it.isEmpty()) {
				getString(R.string.input_search)
			} else {
				getString(R.string.search_empty, it)
			}
		}
	}

}