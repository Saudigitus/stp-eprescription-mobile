package org.saudigitus.e_prescription.data.model.response


import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class TrackedEntityInstance(
    @JsonProperty("attributes")
    val attributes: List<Attribute>,
    @JsonProperty("trackedEntityInstance")
    val trackedEntityInstance: String,
    @JsonProperty("enrollments")
    val enrollments: List<Enrollment>
)