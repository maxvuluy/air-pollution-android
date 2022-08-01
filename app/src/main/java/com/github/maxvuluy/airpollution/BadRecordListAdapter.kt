package com.github.maxvuluy.airpollution

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.maxvuluy.airpollution.databinding.AdapterBadRecordListBinding

class BadRecordListAdapter : ListAdapter<PollutionRecord, BadRecordListAdapter.ViewHolder>(
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

	var onArrowClickListener: OnArrowClickListener? = null

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
		AdapterBadRecordListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
	)

	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		with(holder) {
			binding.record = getItem(position)
			binding.imageButtonArrow.setOnClickListener {
				onArrowClickListener?.onArrowClick(it, position)
			}
		}
	}

	interface OnArrowClickListener {
		fun onArrowClick(view: View, position: Int)
	}

	class ViewHolder(val binding: AdapterBadRecordListBinding) : RecyclerView.ViewHolder(binding.root)

}