package org.saudigitus.e_prescription.data.model.put


import com.fasterxml.jackson.annotation.JsonProperty

data class DataValue(
    @JsonProperty("dataElement")
    val dataElement: String,
    @JsonProperty("providedElsewhere")
    val providedElsewhere: Boolean = false,
    @JsonProperty("value")
    val value: Int
)