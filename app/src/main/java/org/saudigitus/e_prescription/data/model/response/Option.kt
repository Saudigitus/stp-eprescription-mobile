package org.saudigitus.e_prescription.data.model.response


import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class Option(
    @JsonProperty("code")
    val code: String,
    @JsonProperty("name")
    val name: String,
    @JsonProperty("optionSet")
    val optionSet: OptionSet
)