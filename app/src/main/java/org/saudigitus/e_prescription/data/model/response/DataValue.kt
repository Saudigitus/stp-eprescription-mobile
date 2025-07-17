package org.saudigitus.e_prescription.data.model.response


import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class DataValue(
    @JsonProperty("dataElement")
    val dataElement: String = "",
    @JsonProperty("value")
    val value: String = ""
)