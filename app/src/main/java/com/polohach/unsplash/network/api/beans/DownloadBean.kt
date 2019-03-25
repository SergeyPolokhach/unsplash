package com.polohach.unsplash.network.api.beans

import com.fasterxml.jackson.annotation.JsonProperty


data class DownloadBean(
        @JsonProperty("url")
        var url: String?
)
