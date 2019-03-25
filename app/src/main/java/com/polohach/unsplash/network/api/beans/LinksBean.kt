package com.polohach.unsplash.network.api.beans

import com.fasterxml.jackson.annotation.JsonProperty


data class LinksBean(
        @JsonProperty("html")
        var html: String?,
        @JsonProperty("likes")
        var likes: String?,
        @JsonProperty("photos")
        var photos: String?,
        @JsonProperty("download_location")
        var downloadLocation: String?,
        @JsonProperty("self")
        var self: String?
)
