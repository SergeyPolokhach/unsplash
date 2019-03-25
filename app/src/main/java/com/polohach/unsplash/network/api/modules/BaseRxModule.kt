package com.polohach.unsplash.network.api.modules

import com.polohach.unsplash.models.converters.Converter


abstract class BaseRxModule<T, NetworkModel, M>(val api: T, val converter: Converter<NetworkModel, M>)
