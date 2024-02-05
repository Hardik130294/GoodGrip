package com.hardik.goodgrip.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.hardik.goodgrip.R
import com.hardik.goodgrip.databinding.ItemAlbumPreviewBinding
import com.hardik.goodgrip.databinding.ItemPostPreviewBinding
import com.hardik.goodgrip.databinding.ItemTodoPreviewBinding
import com.hardik.goodgrip.models.AlbumResponseItem
import com.hardik.goodgrip.models.PostResponseItem
import com.hardik.goodgrip.models.TodoResponseItem

class TodoAdapter: RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {
    val TAG = TodoAdapter::class.java.simpleName
    inner class TodoViewHolder(val binding: ItemTodoPreviewBinding): RecyclerView.ViewHolder(binding.root)

    private val differCallback = object : DiffUtil.ItemCallback<TodoResponseItem>(){
        override fun areItemsTheSame(
            oldItem: TodoResponseItem,
            newItem: TodoResponseItem
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: TodoResponseItem,
            newItem: TodoResponseItem
        ): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this@TodoAdapter, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        return TodoViewHolder(
            ItemTodoPreviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val todo = differ.currentList[position]
        holder.itemView.alpha = 0f
        holder.itemView.animate().alpha(1f).setDuration(200).start()
        holder.itemView.apply {
            holder.binding.tvTitle.apply {
                ellipsize = android.text.TextUtils.TruncateAt.MARQUEE
                marqueeRepeatLimit = -1
                setHorizontallyScrolling(true)
                isSingleLine = true
                isSelected = true
                text = todo.title
            }
            holder.binding.tvTaskStatus.apply {
                maxLines = 1
                isSingleLine = true
                text = ContextCompat.getString(this.context, if (todo.completed) R.string.completed else R.string.uncompleted)
                setTextColor(ContextCompat.getColorStateList(context, if (todo.completed) android.R.color.holo_green_dark else android.R.color.holo_red_dark) ?: throw IllegalArgumentException("Color resource not found") )
            }
            if (todo.completed){
                holder.binding.lavTaskStatus.visibility = View.VISIBLE
            }else{
                holder.binding.lavTaskStatus.visibility = View.GONE
            }

            setOnClickListener {
                onItemClickListener?.let {
                    it(
                        TodoResponseItem(
                            id = todo.id?: 1,
                            title = todo.title?: "",
                            userId = todo.userId?: 1,
                            completed = todo.completed?: false
                        )
                    )
                }
            }
        }
    }

    private var onItemClickListener: ((TodoResponseItem) -> Unit)? = null

    fun setOnItemClickListener(onItemClickListener: ((TodoResponseItem) -> Unit)) {
        this.onItemClickListener = onItemClickListener
    }

}