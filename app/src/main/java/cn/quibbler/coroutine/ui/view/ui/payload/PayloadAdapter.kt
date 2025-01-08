package cn.quibbler.coroutine.ui.view.ui.payload

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cn.quibbler.coroutine.databinding.PayloadRecyclerItemBinding

class PayloadAdapter : RecyclerView.Adapter<PayloadAdapter.PayloadViewHolder>() {

    companion object {
        const val TAG = "TAG_PayloadAdapter"
        const val PAYLOAD = "TAG_PAYLOAD"
    }

    var isChange = false

    private val list = mutableListOf<PayloadData>().apply {
        for (i in 0 until 20) {
            add(PayloadData("PayloadData $i"))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PayloadViewHolder {
        val binding =
            PayloadRecyclerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PayloadViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: PayloadViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        Log.d(TAG, "onBindViewHolder1:  position:$position payloads:$payloads")
        if (payloads.contains(PAYLOAD)) {
            if (isChange) {
                holder.binding.text.setBackgroundColor(Color.RED)
            }else{
                holder.binding.text.setBackgroundColor(Color.BLUE)
            }
        }else{
            super.onBindViewHolder(holder, position, payloads)
        }
    }

    override fun onBindViewHolder(holder: PayloadViewHolder, position: Int) {
        Log.d(TAG, "onBindViewHolder2:  position:$position")
        if (isChange) {
            holder.binding.text.setBackgroundColor(Color.RED)
        }else{
            holder.binding.text.setBackgroundColor(Color.BLUE)
        }
        holder.binding.text.text = list[position].str
    }

    override fun getItemCount(): Int = list.size

    class PayloadViewHolder(val binding: PayloadRecyclerItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

}

data class PayloadData(val str: String) {

}