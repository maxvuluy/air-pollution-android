package com.github.maxvuluy.airpollution

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.github.maxvuluy.airpollution.databinding.ActivitySearchBinding

class SearchActivity : AppCompatActivity(), BadRecordListAdapter.OnArrowClickListener {

	private lateinit var binding: ActivitySearchBinding

	private val adapter = BadRecordListAdapter().apply {
		onArrowClickListener = this@SearchActivity
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		val viewModel = ViewModelProvider(this).get<SearchViewModel>()
		binding = ActivitySearchBinding.inflate(layoutInflater).apply {
			this.viewModel = viewModel
			lifecycleOwner = this@SearchActivity
		}
		setContentView(binding.root)

		setSupportActionBar(binding.toolbar)
		supportActionBar?.setDisplayShowTitleEnabled(false)

		binding.apply {
			recyclerView.adapter = adapter

			toolbar.setNavigationOnClickListener {
				finish()
			}
		}

		viewModel.recordList.observe(this) {
			adapter.submitList(it)
		}
	}

	override fun onArrowClick(view: View, position: Int) {
		val pollutionRecord = adapter.currentList[position]
		Toast
			.makeText(
				this,
				getString(R.string.record_detail, pollutionRecord.siteName, pollutionRecord.status),
				Toast.LENGTH_LONG
			)
			.show()
	}

}