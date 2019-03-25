package com.polohach.unsplash.network.api.converters

import com.polohach.unsplash.models.Urls
import com.polohach.unsplash.models.UrlsModel
import com.polohach.unsplash.models.converters.BaseConverter
import com.polohach.unsplash.network.api.beans.UrlsBean

interface UrlsBeanConverter

class UrlsBeanConverterImpl : BaseConverter<UrlsBean, Urls>(), UrlsBeanConverter {

    override fun processConvertInToOut(inObject: UrlsBean) = inObject.run {
        UrlsModel(full, raw, regular, small, thumb)
    }

    override fun processConvertOutToIn(outObject: Urls) = outObject.run {
        UrlsBean(full, raw, regular, small, thumb)
    }
}
