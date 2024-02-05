package com.hardik.goodgrip.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.hardik.goodgrip.R
import com.hardik.goodgrip.databinding.ItemAlbumPreviewBinding
import com.hardik.goodgrip.databinding.ItemPhotoPreviewBinding
import com.hardik.goodgrip.databinding.ItemPostPreviewBinding
import com.hardik.goodgrip.models.AlbumResponseItem
import com.hardik.goodgrip.models.PhotoResponseItem
import com.hardik.goodgrip.models.PostResponseItem

class PhotoAdapter: RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder>() {
    val TAG = PhotoAdapter::class.java.simpleName
    inner class PhotoViewHolder(val binding: ItemPhotoPreviewBinding): RecyclerView.ViewHolder(binding.root)

    private val differCallback = object : DiffUtil.ItemCallback<PhotoResponseItem>(){
        override fun areItemsTheSame(
            oldItem: PhotoResponseItem,
            newItem: PhotoResponseItem
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: PhotoResponseItem,
            newItem: PhotoResponseItem
        ): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this@PhotoAdapter, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        return PhotoViewHolder(
            ItemPhotoPreviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val photo = differ.currentList[position]
        holder.itemView.alpha = 0f
        holder.itemView.animate().alpha(1f).setDuration(200).start()
        holder.itemView.apply {
            holder.binding.tvTitle.apply {
                ellipsize = android.text.TextUtils.TruncateAt.MARQUEE
                marqueeRepeatLimit = -1
                setHorizontallyScrolling(true)
                isSingleLine = true
                isSelected = true
                text = photo.title
            }
            Glide
                .with(this)
                .load(photo.url)
//                .load("https://ozgrozer.github.io/100k-faces/0/6/006722.jpg")
//                .centerCrop()
//                .apply(RequestOptions.circleCropTransform())
//                .apply(RequestOptions().transform(RoundedCorners(20)))
                .fitCenter()
                .placeholder(ContextCompat.getDrawable(this.context, R.drawable.ic_launcher_foreground))
                .error(R.drawable.ic_launcher_background)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(holder.binding.ivUser)
            Glide
                .with(this)
                .load(photo.thumbnailUrl)
                .fitCenter()
                .placeholder(ContextCompat.getDrawable(this.context, R.drawable.ic_launcher_foreground))
                .error(R.drawable.ic_launcher_background)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(holder.binding.ivThumbnail)
            setOnClickListener {
                onItemClickListener?.let {
                    it(
                        PhotoResponseItem(
                            id = photo.id?: 1,
                            albumId = photo.albumId?: 1,
                            title = photo.title?: "",
                            url = photo.url?: "",
                            thumbnailUrl = photo.thumbnailUrl?: ""
                        )
                    )
                }
            }
        }
    }

    private var onItemClickListener: ((PhotoResponseItem) -> Unit)? = null

    fun setOnItemClickListener(onItemClickListener: ((PhotoResponseItem) -> Unit)) {
        this.onItemClickListener = onItemClickListener
    }

}