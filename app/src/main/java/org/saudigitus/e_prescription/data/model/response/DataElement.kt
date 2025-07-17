package org.saudigitus.e_prescription.data.model.response


import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class DataElement(
    @JsonProperty("displayFormName")
    val displayFormName: String,
    @JsonProperty("id")
    val id: String
)