package com.polohach.unsplash.network.api.converters

import com.polohach.unsplash.models.Photo
import com.polohach.unsplash.models.PhotoModel
import com.polohach.unsplash.models.converters.BaseConverter
import com.polohach.unsplash.models.converters.Converter
import com.polohach.unsplash.network.api.beans.PhotoBean

interface PhotoBeanConverter : Converter<PhotoBean, Photo>

class PhotoBeanConverterImpl(private val linksBeanConverter: LinksBeanConverter,
                             private val urlsBeanConverter: UrlsBeanConverter)
    : BaseConverter<PhotoBean, Photo>(), PhotoBeanConverter {

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
