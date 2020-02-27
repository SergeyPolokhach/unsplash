package com.polohach.unsplash.providers.base

import com.polohach.unsplash.models.Model


abstract class BaseOnlineProvider<M : Model, NetworkModule> : Provider<M>