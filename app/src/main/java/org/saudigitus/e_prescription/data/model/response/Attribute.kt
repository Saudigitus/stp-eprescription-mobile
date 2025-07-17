package org.saudigitus.e_prescription.data.model.response


import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class Attribute(
    @JsonProperty("attribute")
    val attribute: String,
    @JsonProperty("displayName")
    val displayName: String,
    @JsonProperty("value")
    val value: String
)