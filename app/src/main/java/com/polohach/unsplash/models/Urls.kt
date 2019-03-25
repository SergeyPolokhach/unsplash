package com.polohach.unsplash.models

import android.os.Parcel


interface Urls : Model {
    var full: String?
    var raw: String?
    var regular: String?
    var small: String?
    var thumb: String?
}

data class UrlsModel(override var full: String?,
                     override var raw: String?,
                     override var regular: String?,
                     override var small: String?,
                     override var thumb: String?) : Urls {

    companion object {
        @JvmField
        val CREATOR = KParcelable.generateCreator {
            UrlsModel(it.read(), it.read(), it.read(), it.read(), it.read())
        }
    }

    override fun writeToParcel(dest: Parcel, flags: Int) =
            dest.write(full, raw, regular, small, thumb)
}
