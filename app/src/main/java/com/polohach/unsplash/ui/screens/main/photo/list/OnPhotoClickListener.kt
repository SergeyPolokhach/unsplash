package com.polohach.unsplash.ui.screens.main.photo.list

import com.polohach.unsplash.models.Photo


interface OnPhotoClickListener {
    fun itemClick(photo: Photo)
}
