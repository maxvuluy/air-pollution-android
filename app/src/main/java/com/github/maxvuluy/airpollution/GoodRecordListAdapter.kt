package com.github.maxvuluy.airpollution

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.maxvuluy.airpollution.databinding.AdapterGoodRecordListBinding

class GoodRecordListAdapter : ListAdapter<PollutionRecord, GoodRecordListAdapter.ViewHolder>(
	DIFF_CALLBACK
) {

	companion object {
		private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<PollutionRecord>() {
			override fun areItemsTheSame(oldItem: PollutionRecord, newItem: PollutionRecord) =
				newItem.siteId == oldItem.siteId

			override fun areContentsTheSame(oldItem: PollutionRecord, newItem: PollutionRecord) =
				newItem == oldItem
		}
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
		AdapterGoodRecordListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
	)

	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		with(holder) {
			with(getItem(position)) {
				binding.record = this
			}
		}
	}

	class ViewHolder(val binding: AdapterGoodRecordListBinding) : RecyclerView.ViewHolder(binding.root)

}