package com.polohach.unsplash.network.api.beans

import com.fasterxml.jackson.annotation.JsonProperty


data class UrlsBean(
        @JsonProperty("full")
        var full: String?,
        @JsonProperty("raw")
        var raw: String?,
        @JsonProperty("regular")
        var regular: String?,
        @JsonProperty("small")
        var small: String?,
        @JsonProperty("thumb")
        var thumb: String?
)
