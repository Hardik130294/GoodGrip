package com.hardik.goodgrip.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.hardik.goodgrip.databinding.ItemAlbumPreviewBinding
import com.hardik.goodgrip.databinding.ItemCommentPreviewBinding
import com.hardik.goodgrip.databinding.ItemPostPreviewBinding
import com.hardik.goodgrip.models.AlbumResponseItem
import com.hardik.goodgrip.models.CommentResponseItem
import com.hardik.goodgrip.models.PostResponseItem

class CommentAdapter: RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {
    val TAG = CommentAdapter::class.java.simpleName
    inner class CommentViewHolder(val binding: ItemCommentPreviewBinding): RecyclerView.ViewHolder(binding.root)

    private val differCallback = object : DiffUtil.ItemCallback<CommentResponseItem>(){
        override fun areItemsTheSame(
            oldItem: CommentResponseItem,
            newItem: CommentResponseItem
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: CommentResponseItem,
            newItem: CommentResponseItem
        ): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this@CommentAdapter, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        return CommentViewHolder(
            ItemCommentPreviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val comment = differ.currentList[position]
        holder.itemView.alpha = 0f
        holder.itemView.animate().alpha(1f).setDuration(200).start()
        holder.itemView.apply {
            holder.binding.tvTitle.apply {
                ellipsize = android.text.TextUtils.TruncateAt.MARQUEE
                marqueeRepeatLimit = -1
                setHorizontallyScrolling(true)
                isSingleLine = true
                isSelected = true
                text = comment.name
            }
            holder.binding.tvEmail.apply {
                ellipsize = android.text.TextUtils.TruncateAt.MARQUEE
                marqueeRepeatLimit = -1
                setHorizontallyScrolling(true)
                isSingleLine = true
                isSelected = true
                text = comment.email
            }
            holder.binding.tvBody.apply {
                maxLines = 1000
                text = comment.body
            }
            setOnClickListener {
                onItemClickListener?.let {
                    it(
                        CommentResponseItem(
                            id = comment.id?: 1,
                            postId = comment.postId?: 1,
                            name = comment.name?: "",
                            body = comment.body?: "",
                            email = comment.email?: ""
                        )
                    )
                }
            }
        }
    }

    private var onItemClickListener: ((CommentResponseItem) -> Unit)? = null

    fun setOnItemClickListener(onItemClickListener: ((CommentResponseItem) -> Unit)) {
        this.onItemClickListener = onItemClickListener
    }

}