package com.polohach.unsplash.ui.screens.main.photo.list.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.cleveroad.bootstrap.kotlin_core.ui.adapter.BaseRecyclerViewAdapter
import com.polohach.unsplash.R
import com.polohach.unsplash.extensions.find
import com.polohach.unsplash.extensions.loadImage
import com.polohach.unsplash.models.Photo
import com.polohach.unsplash.ui.screens.main.photo.list.OnPhotoClickListener
import java.lang.ref.WeakReference


class PhotoAdapter(context: Context, callback: OnPhotoClickListener) :
        BaseRecyclerViewAdapter<Photo, PhotoAdapter.PhotoHolder>(context),
        OnItemClickListener {

    private val weakRefCallback = WeakReference(callback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoHolder =
            PhotoHolder.newInstance(LayoutInflater.from(parent.context), parent, this)

    override fun onBindViewHolder(holder: PhotoHolder, position: Int) =
            holder.bind(getItem(position))

    override fun itemClick(position: Int) {
        if (position != RecyclerView.NO_POSITION && data.size >= position) {
            weakRefCallback.get()?.itemClick(getItem(position))
        }
    }

    class PhotoHolder(itemView: View, private val callback: OnItemClickListener?) :
            RecyclerView.ViewHolder(itemView) {

        companion object {
            internal fun newInstance(inflater: LayoutInflater,
                                     parent: ViewGroup?,
                                     callback: OnItemClickListener?) =
                    PhotoHolder(inflater.inflate(R.layout.item_photo, parent, false), callback)
        }

        private val ivPhoto = itemView.find<ImageView>(R.id.ivPhoto)

        fun bind(photo: Photo) {
            photo.run {
                urls?.small?.let { ivPhoto.loadImage(it, R.drawable.ic_gallery_grey_160dp) }
                ivPhoto.setOnClickListener { callback?.itemClick(adapterPosition) }
            }
        }
    }
}
