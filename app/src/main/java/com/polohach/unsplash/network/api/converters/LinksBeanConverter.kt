package com.polohach.unsplash.network.api.converters

import com.polohach.unsplash.models.Links
import com.polohach.unsplash.models.LinksModel
import com.polohach.unsplash.models.converters.BaseConverter
import com.polohach.unsplash.models.converters.Converter
import com.polohach.unsplash.network.api.beans.LinksBean

interface LinksBeanConverter: Converter<LinksBean, Links>

class LinksBeanConverterImpl : BaseConverter<LinksBean, Links>(), LinksBeanConverter {

    override fun processConvertInToOut(inObject: LinksBean) = inObject.run {
        LinksModel(html, likes, photos, downloadLocation, self)
    }

    override fun processConvertOutToIn(outObject: Links) = outObject.run {
        LinksBean(html, likes, photos, downloadLocation, self)
    }
}
