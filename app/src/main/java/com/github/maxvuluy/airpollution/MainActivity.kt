package com.github.maxvuluy.airpollution

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.github.maxvuluy.airpollution.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(),
	BadRecordListAdapter.OnArrowClickListener,
	RpcErrorDialogFragment.OnFinishListener {

	private lateinit var binding: ActivityMainBinding

	private val goodAdapter = GoodRecordListAdapter()
	private val badAdapter = BadRecordListAdapter().apply {
		onArrowClickListener = this@MainActivity
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		val viewModel = ViewModelProvider(this).get<MainViewModel>()
		binding = ActivityMainBinding.inflate(layoutInflater).apply {
			this.viewModel = viewModel
			lifecycleOwner = this@MainActivity
		}
		setContentView(binding.root)

		setSupportActionBar(binding.toolbar)

		binding.apply {
			recyclerViewGood.adapter = goodAdapter
			recyclerViewBad.adapter = badAdapter
		}

		viewModel.goodRecordList.observe(this) {
			goodAdapter.submitList(it)
		}
		viewModel.badRecordList.observe(this) {
			badAdapter.submitList(it)
		}
		viewModel.loaded.observe(this) {
			if (!it) {
				RpcErrorDialogFragment().show(supportFragmentManager, null)
			}
		}
	}

	override fun onCreateOptionsMenu(menu: Menu): Boolean {
		// Inflate the menu; this adds items to the action bar if it is present.
		menuInflater.inflate(R.menu.menu_main, menu)
		return true
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		return when (item.itemId) {
			R.id.actionSearch -> {
				val intent = Intent(this, SearchActivity::class.java)
				startActivity(intent)

				true
			}
			else -> super.onOptionsItemSelected(item)
		}
	}

	override fun onArrowClick(view: View, position: Int) {
		val pollutionRecord = badAdapter.currentList[position]
		Toast
			.makeText(
				this,
				getString(R.string.record_detail, pollutionRecord.siteName, pollutionRecord.status),
				Toast.LENGTH_LONG
			)
			.show()
	}

	override fun onFinish() {
		finish()
	}

}