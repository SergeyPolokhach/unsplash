package com.polohach.unsplash.network.api.converters

import com.polohach.unsplash.models.Photo
import com.polohach.unsplash.models.PhotoModel
import com.polohach.unsplash.models.converters.BaseConverter
import com.polohach.unsplash.network.api.beans.PhotoBean

interface PhotoBeanConverter

class PhotoBeanConverterImpl : BaseConverter<PhotoBean, Photo>(), PhotoBeanConverter {

    private val linksBeanConverter by lazy { LinksBeanConverterImpl() }
    private val urlsBeanConverter by lazy { UrlsBeanConverterImpl() }

    override fun processConvertInToOut(inObject: PhotoBean) = inObject.run {
        PhotoModel(color, createdAt, description, height, id, likedByUser, likes,
                links?.let { linksBeanConverter.convertInToOut(it) }, updatedAt,
                urls?.let { urlsBeanConverter.convertInToOut(it) }, width)
    }

    override fun processConvertOutToIn(outObject: Photo) = outObject.run {
        PhotoBean(color, createdAt, description, height, id, likedByUser, likes,
                links?.let { linksBeanConverter.convertOutToIn(it) }, updatedAt,
                urls?.let { urlsBeanConverter.convertOutToIn(it) }, width)
    }
}
