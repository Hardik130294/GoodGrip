package com.hardik.goodgrip.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.hardik.goodgrip.databinding.ItemAlbumPreviewBinding
import com.hardik.goodgrip.databinding.ItemPostPreviewBinding
import com.hardik.goodgrip.models.AlbumResponseItem
import com.hardik.goodgrip.models.PostResponseItem

class PostAdapter: RecyclerView.Adapter<PostAdapter.PostViewHolder>() {
    val TAG = PostAdapter::class.java.simpleName
    inner class PostViewHolder(val binding: ItemPostPreviewBinding): RecyclerView.ViewHolder(binding.root)

    private val differCallback = object : DiffUtil.ItemCallback<PostResponseItem>(){
        override fun areItemsTheSame(
            oldItem: PostResponseItem,
            newItem: PostResponseItem
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: PostResponseItem,
            newItem: PostResponseItem
        ): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this@PostAdapter, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        return PostViewHolder(
            ItemPostPreviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = differ.currentList[position]
        holder.itemView.alpha = 0f
        holder.itemView.animate().alpha(1f).setDuration(200).start()
        holder.itemView.apply {
            holder.binding.tvTitle.apply {
                ellipsize = android.text.TextUtils.TruncateAt.MARQUEE
                marqueeRepeatLimit = -1
                setHorizontallyScrolling(true)
                isSingleLine = true
                isSelected = true
                text = post.title
            }
            holder.binding.tvBody.apply {
                maxLines = 1000
                text = post.body
            }
            setOnClickListener {
                onItemClickListener?.let {
                    it(
                        PostResponseItem(
                            id = post.id?: 1,
                            title = post.title?: "",
                            userId = post.userId?: 1,
                            body = post.body?: ""
                        )
                    )
                }
            }
        }
    }

    private var onItemClickListener: ((PostResponseItem) -> Unit)? = null

    fun setOnItemClickListener(onItemClickListener: ((PostResponseItem) -> Unit)) {
        this.onItemClickListener = onItemClickListener
    }

}