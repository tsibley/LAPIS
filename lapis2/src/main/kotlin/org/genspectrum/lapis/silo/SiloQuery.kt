package org.genspectrum.lapis.silo

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.core.type.TypeReference
import org.genspectrum.lapis.response.AggregatedResponse
import org.genspectrum.lapis.response.MutationProportion
import java.time.LocalDate

data class SiloQuery<ResponseType>(val action: SiloAction<ResponseType>, val filterExpression: SiloFilterExpression)

sealed class SiloAction<ResponseType>(@JsonIgnore val typeReference: TypeReference<SiloQueryResponse<ResponseType>>) {
    companion object {
        fun aggregated(): SiloAction<AggregatedResponse> = AggregatedAction("Aggregated")

        fun mutations(minProportion: Double? = null): SiloAction<List<MutationProportion>> =
            MutationsAction("Mutations", minProportion)
    }

    private data class AggregatedAction(val type: String) :
        SiloAction<AggregatedResponse>(object : TypeReference<SiloQueryResponse<AggregatedResponse>>() {})

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private data class MutationsAction(val type: String, val minProportion: Double?) :
        SiloAction<List<MutationProportion>>(object : TypeReference<SiloQueryResponse<List<MutationProportion>>>() {})
}

sealed class SiloFilterExpression(val type: String)

data class StringEquals(val column: String, val value: String) : SiloFilterExpression("StringEquals")

data class PangoLineageEquals(val value: String, val includeSublineages: Boolean) :
    SiloFilterExpression("PangoLineage")

data class NucleotideSymbolEquals(val position: Int, val symbol: String) : SiloFilterExpression("NucleotideEquals")

data class DateBetween(val from: LocalDate?, val to: LocalDate?) : SiloFilterExpression("DateBetween")

object True : SiloFilterExpression("True")

data class And(val children: List<SiloFilterExpression>) : SiloFilterExpression("And")
