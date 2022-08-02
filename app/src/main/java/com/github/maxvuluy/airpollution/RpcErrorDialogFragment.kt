package com.github.maxvuluy.airpollution

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class RpcErrorDialogFragment : DialogFragment() {

	private var onFinishListener: OnFinishListener? = null

	override fun onAttach(context: Context) {
		onFinishListener = context as? OnFinishListener
		super.onAttach(context)
	}

	override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
		return AlertDialog
			.Builder(requireContext())
			.setMessage(R.string.rpc_error)
			.setNeutralButton(R.string.ok) { dialog, which ->
				onFinishListener?.onFinish()
			}
			.create()
	}

	override fun onDetach() {
		onFinishListener = null
		super.onDetach()
	}

	override fun onCancel(dialog: DialogInterface) {
		super.onCancel(dialog)
		onFinishListener?.onFinish()
	}

	interface OnFinishListener {
		fun onFinish()
	}

}