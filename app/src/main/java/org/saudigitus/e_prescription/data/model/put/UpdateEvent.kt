package org.saudigitus.e_prescription.data.model.put


import com.fasterxml.jackson.annotation.JsonProperty

data class UpdateEvent(
    @JsonProperty("dataValues")
    val dataValues: List<DataValue>,
    @JsonProperty("event")
    val event: String,
    @JsonProperty("orgUnit")
    val orgUnit: String,
    @JsonProperty("program")
    val program: String,
    @JsonProperty("programStage")
    val programStage: String,
    @JsonProperty("status")
    val status: String,
    @JsonProperty("trackedEntityInstance")
    val trackedEntityInstance: String
)