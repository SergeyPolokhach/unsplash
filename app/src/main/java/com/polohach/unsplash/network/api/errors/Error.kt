package com.polohach.unsplash.network.api.errors

import com.fasterxml.jackson.annotation.JsonProperty

data class Error(@JsonProperty("code")
                 val code: Int? = null,
                 @JsonProperty("key")
                 var key: String? = null,
                 @JsonProperty("message")
                 var message: String? = null)


