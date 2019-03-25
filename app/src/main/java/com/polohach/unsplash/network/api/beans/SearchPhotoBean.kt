package com.polohach.unsplash.network.api.beans

import com.fasterxml.jackson.annotation.JsonProperty


data class SearchPhotoBean(
        @JsonProperty("results")
        var results: List<PhotoBean>?,
        @JsonProperty("total")
        var total: Int?,
        @JsonProperty("total_pages")
        var totalPages: Int?
)
