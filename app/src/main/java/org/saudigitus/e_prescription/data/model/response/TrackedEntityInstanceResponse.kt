package org.saudigitus.e_prescription.data.model.response


import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class TrackedEntityInstanceResponse(
    @JsonProperty("trackedEntityInstances")
    val trackedEntityInstances: List<TrackedEntityInstance> = emptyList()
)