package com.polohach.unsplash.models

import android.os.Parcel
import org.joda.time.DateTime


interface Photo : Model {
    var color: String?
    var createdAt: DateTime?
    var description: String?
    var height: Int?
    var id: String?
    var likedByUser: Boolean?
    var likes: Int?
    var links: Links?
    var updatedAt: DateTime?
    var urls: Urls?
    var width: Int?
}

data class PhotoModel(override var color: String?,
                      override var createdAt: DateTime?,
                      override var description: String?,
                      override var height: Int?,
                      override var id: String?,
                      override var likedByUser: Boolean?,
                      override var likes: Int?,
                      override var links: Links?,
                      override var updatedAt: DateTime?,
                      override var urls: Urls?,
                      override var width: Int?) : Photo {

    companion object {
        @JvmField
        val CREATOR = KParcelable.generateCreator {
            PhotoModel(it.read(), it.read(), it.read(), it.read(), it.read(), it.read(),
                    it.read(), it.read(), it.read(), it.read(), it.read())
        }
    }

    override fun writeToParcel(dest: Parcel, flags: Int) =
            dest.write(color, createdAt, description, height, id, likedByUser, likes,
                    links, updatedAt, urls, width)
}
