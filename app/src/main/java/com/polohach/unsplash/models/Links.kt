package com.polohach.unsplash.models

import android.os.Parcel

interface Links : Model {
    var html: String?
    var likes: String?
    var photos: String?
    var downloadLocation: String?
    var self: String?
}

data class LinksModel(
        override var html: String?,
        override var likes: String?,
        override var photos: String?,
        override var downloadLocation: String?,
        override var self: String?) : Links {

    companion object {
        @JvmField
        val CREATOR = KParcelable.generateCreator {
            LinksModel(it.read(), it.read(), it.read(), it.read(), it.read())
        }
    }

    override fun writeToParcel(dest: Parcel, flags: Int) =
            dest.write(html, likes, photos, downloadLocation, self)
}
