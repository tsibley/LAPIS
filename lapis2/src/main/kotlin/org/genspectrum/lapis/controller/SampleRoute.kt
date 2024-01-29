package org.genspectrum.lapis.controller

const val AGGREGATED_ROUTE = "/aggregated"
const val DETAILS_ROUTE = "/details"
const val NUCLEOTIDE_MUTATIONS_ROUTE = "/nucleotideMutations"
const val AMINO_ACID_MUTATIONS_ROUTE = "/aminoAcidMutations"
const val NUCLEOTIDE_INSERTIONS_ROUTE = "/nucleotideInsertions"
const val AMINO_ACID_INSERTIONS_ROUTE = "/aminoAcidInsertions"
const val ALIGNED_NUCLEOTIDE_SEQUENCES_ROUTE = "/alignedNucleotideSequences"
const val ALIGNED_AMINO_ACID_SEQUENCES_ROUTE = "/alignedAminoAcidSequences"
const val UNALIGNED_NUCLEOTIDE_SEQUENCES_ROUTE = "/unalignedNucleotideSequences"

enum class SampleRoute(val pathSegment: String, val servesFasta: Boolean = false) {
    AGGREGATED(AGGREGATED_ROUTE),
    DETAILS(DETAILS_ROUTE),
    NUCLEOTIDE_MUTATIONS(NUCLEOTIDE_MUTATIONS_ROUTE),
    AMINO_ACID_MUTATIONS(AMINO_ACID_MUTATIONS_ROUTE),
    NUCLEOTIDE_INSERTIONS(NUCLEOTIDE_INSERTIONS_ROUTE),
    AMINO_ACID_INSERTIONS(AMINO_ACID_INSERTIONS_ROUTE),
    ALIGNED_NUCLEOTIDE_SEQUENCES(ALIGNED_NUCLEOTIDE_SEQUENCES_ROUTE, servesFasta = true),
    ALIGNED_AMINO_ACID_SEQUENCES(ALIGNED_AMINO_ACID_SEQUENCES_ROUTE, servesFasta = true),
    UNALIGNED_NUCLEOTIDE_SEQUENCES(UNALIGNED_NUCLEOTIDE_SEQUENCES_ROUTE, servesFasta = true),
}
