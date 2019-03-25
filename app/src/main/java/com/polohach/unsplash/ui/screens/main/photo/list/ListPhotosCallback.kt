package com.polohach.unsplash.ui.screens.main.photo.list

interface ListPhotosCallback {
    fun openDetailPhoto(id: String, downloadUrl: String, photoUrl: String)
}
