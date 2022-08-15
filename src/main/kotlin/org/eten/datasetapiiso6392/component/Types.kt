package org.eten.datasetapiiso6392.component

import kotlinx.serialization.Serializable
import org.eten.datasetapiiso6392.common.ErrorType

@Serializable
enum class Iso_639_2_entry_type {
  B,
  T,
}
@Serializable
data class Iso6392Entry(
    val id: Long,
    val iso_639_2: String,
    val entry_type: Iso_639_2_entry_type? = null,
    val iso_639_1: String? = null,
    val english_name: String? = null,
    val french_name: String? = null,
    val german_name: String? = null,
)

@Serializable
data class ListResponse(
    val error: ErrorType,
    val items: List<Iso6392Entry>? = null,
)