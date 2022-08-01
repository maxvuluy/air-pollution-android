package com.github.maxvuluy.airpollution

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.maxvuluy.airpollution.databinding.ActivitySearchBinding

class SearchActivity : AppCompatActivity() {

	private lateinit var binding: ActivitySearchBinding

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		binding = ActivitySearchBinding.inflate(layoutInflater)
		setContentView(binding.root)

		setSupportActionBar(binding.toolbar)
		supportActionBar?.setDisplayShowTitleEnabled(false)
		binding.toolbar.setNavigationOnClickListener {
			finish()
		}
	}

}