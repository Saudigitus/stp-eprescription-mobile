package org.saudigitus.e_prescription.data.model.response


import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class Event(
    @JsonProperty("dataValues")
    val dataValues: List<DataValue> = emptyList(),
    @JsonProperty("event")
    val event: String = "",
    @JsonProperty("orgUnit")
    val orgUnit: String = "",
    @JsonProperty("programStage")
    val programStage: String = "",
    @JsonProperty("status")
    val status: String = ""
)