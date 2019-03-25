package com.polohach.unsplash.network.api.beans

import com.fasterxml.jackson.annotation.JsonProperty
import org.joda.time.DateTime


data class PhotoBean(
        @JsonProperty("color")
        var color: String?,
        @JsonProperty("created_at")
        var createdAt: DateTime?,
        @JsonProperty("description")
        var description: String?,
        @JsonProperty("height")
        var height: Int?,
        @JsonProperty("id")
        var id: String?,
        @JsonProperty("liked_by_user")
        var likedByUser: Boolean?,
        @JsonProperty("likes")
        var likes: Int?,
        @JsonProperty("links")
        var links: LinksBean?,
        @JsonProperty("updated_at")
        var updatedAt: DateTime?,
        @JsonProperty("urls")
        var urls: UrlsBean?,
        @JsonProperty("width")
        var width: Int?
)
