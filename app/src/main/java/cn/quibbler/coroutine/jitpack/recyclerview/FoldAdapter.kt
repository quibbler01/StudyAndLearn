package cn.quibbler.coroutine.jitpack.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cn.quibbler.coroutine.databinding.RecyclerViewItemBinding
import cn.quibbler.coroutine.databinding.RecyclerViewItemHeaderBinding
import kotlin.math.min

class FoldAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val TAG = "TAG_FoldAdapter"

        const val COLLAPSE_NUM = 3

        const val TYPE_HEADER = 0
        const val TYPE_ITEM = 1
    }

    private val realDataList: MutableList<MockData> = MockData.mockListData(5)

    private val displayDataList: MutableList<MockData> = ArrayList<MockData>().apply {
        addAll(realDataList.take(min(realDataList.size, COLLAPSE_NUM)))
    }

    private var isExpand = false

    fun expandList() {
        isExpand = !isExpand
        displayDataList.clear()
        if (isExpand) {
            displayDataList.addAll(realDataList)
            notifyItemRangeInserted(COLLAPSE_NUM, realDataList.size - COLLAPSE_NUM)
        } else {
            displayDataList.addAll(realDataList.take(min(realDataList.size, COLLAPSE_NUM)))
            notifyItemRangeRemoved(COLLAPSE_NUM, realDataList.size - COLLAPSE_NUM)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return if (viewType == TYPE_HEADER) {
            HeaderViewHodler(
                RecyclerViewItemHeaderBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            ).apply {
                binding.root.setOnClickListener {
                    expandList()
                }
            }
        } else {
            ItemViewHodler(
                RecyclerViewItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        val data = displayDataList[position]
        if (holder is HeaderViewHodler) {
            holder.bind(data)
        } else if (holder is ItemViewHodler) {
            holder.bind(data)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) TYPE_HEADER else TYPE_ITEM
    }

    override fun getItemCount(): Int = displayDataList.size

    class HeaderViewHodler(val binding: RecyclerViewItemHeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: MockData) {

        }
    }

    class ItemViewHodler(val binding: RecyclerViewItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: MockData) {

        }
    }

}