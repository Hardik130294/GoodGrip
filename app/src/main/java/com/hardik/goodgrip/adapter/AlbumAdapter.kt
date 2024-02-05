package com.hardik.goodgrip.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.hardik.goodgrip.databinding.ItemAlbumPreviewBinding
import com.hardik.goodgrip.models.AlbumResponseItem

class AlbumAdapter: RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder>() {
    val TAG = AlbumAdapter::class.java.simpleName
    inner class AlbumViewHolder(val binding: ItemAlbumPreviewBinding): RecyclerView.ViewHolder(binding.root)

    private val differCallback = object : DiffUtil.ItemCallback<AlbumResponseItem>(){
        override fun areItemsTheSame(
            oldItem: AlbumResponseItem,
            newItem: AlbumResponseItem
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: AlbumResponseItem,
            newItem: AlbumResponseItem
        ): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this@AlbumAdapter, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        return AlbumViewHolder(
            ItemAlbumPreviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        val album = differ.currentList[position]
        holder.itemView.alpha = 0f
        holder.itemView.animate().alpha(1f).setDuration(200).start()
        holder.itemView.apply {
            holder.binding.tvTitle.apply {
                ellipsize = android.text.TextUtils.TruncateAt.MARQUEE
                marqueeRepeatLimit = -1
                setHorizontallyScrolling(true)
                isSingleLine = true
                isSelected = true
                text = album.title
            }
            setOnClickListener {
                onItemClickListener?.let {
                    it(
                        AlbumResponseItem(
                            id = album.id?: 1,
                            title = album.title?: "",
                            userId = album.userId?: 1
                        )
                    )
                }
            }
        }
    }

    private var onItemClickListener: ((AlbumResponseItem) -> Unit)? = null

    fun setOnItemClickListener(onItemClickListener: ((AlbumResponseItem) -> Unit)) {
        this.onItemClickListener = onItemClickListener
    }

}