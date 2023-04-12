package org.genspectrum.lapis.config

typealias FieldName = String

data class SequenceFilterFields(val fields: Map<FieldName, SequenceFilterFieldType>) {
    companion object {
        private val nucleotideMutationsField = Pair("nucleotideMutations", SequenceFilterFieldType.MutationsList)

        fun fromDatabaseConfig(databaseConfig: DatabaseConfig): SequenceFilterFields {
            val metadataFields = databaseConfig.schema.metadata
                .map(::mapToSequenceFilterFields)
                .flatten()
                .toMap()
            val staticFields = listOf(nucleotideMutationsField)

            val featuresFields = if (databaseConfig.schema.features.isNullOrEmpty()) {
                emptyMap<FieldName, SequenceFilterFieldType>()
            } else {
                databaseConfig.schema.features
                    .map(::mapToSequenceFilterFieldsFromFeatures)
                    .flatten()
                    .toMap()
            }

            return SequenceFilterFields(fields = metadataFields + staticFields + featuresFields)
        }
    }
}

private fun mapToSequenceFilterFields(databaseMetadata: DatabaseMetadata) = when (databaseMetadata.type) {
    "string" -> listOf(databaseMetadata.name to SequenceFilterFieldType.String)
    "pango_lineage" -> listOf(databaseMetadata.name to SequenceFilterFieldType.PangoLineage)
    "date" -> listOf(
        databaseMetadata.name to SequenceFilterFieldType.Date,
        "${databaseMetadata.name}From" to SequenceFilterFieldType.DateFrom(databaseMetadata.name),
        "${databaseMetadata.name}To" to SequenceFilterFieldType.DateTo(databaseMetadata.name),
    )

    else -> throw IllegalArgumentException(
        "Unknown field type '${databaseMetadata.type}' for field '${databaseMetadata.name}'",
    )
}

private fun mapToSequenceFilterFieldsFromFeatures(databaseFeature: DatabaseFeature) = when (databaseFeature.name) {
    "sarsCoV2VariantQuery" -> listOf("variantQuery" to SequenceFilterFieldType.VariantQuery)
    else -> throw IllegalArgumentException(
        "Unknown feature '${databaseFeature.name}'",
    )
}

sealed class SequenceFilterFieldType(val openApiType: kotlin.String) {
    object String : SequenceFilterFieldType("string")
    object PangoLineage : SequenceFilterFieldType("string")
    object Date : SequenceFilterFieldType("string")
    object MutationsList : SequenceFilterFieldType("string")
    object VariantQuery : SequenceFilterFieldType("string")
    data class DateFrom(val associatedField: kotlin.String) : SequenceFilterFieldType("string")
    data class DateTo(val associatedField: kotlin.String) : SequenceFilterFieldType("string")
}
