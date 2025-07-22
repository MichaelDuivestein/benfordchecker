package za.co.k0ma.za.co.k0ma.benfordchecker.service.model

import java.util.UUID

data class ChiSquareResult(
    val id: UUID,
    val sampleSize: Int,
    val chiSquare: Double,
    val pValue: Double,
    val expectedDistribution: DoubleArray,
    val actualDistribution: IntArray,
    val contribution: DoubleArray,
    val isSignificant: Boolean,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ChiSquareResult

        if (sampleSize != other.sampleSize) return false
        if (chiSquare != other.chiSquare) return false
        if (pValue != other.pValue) return false
        if (isSignificant != other.isSignificant) return false
        if (id != other.id) return false
        if (!expectedDistribution.contentEquals(other.expectedDistribution)) return false
        if (!actualDistribution.contentEquals(other.actualDistribution)) return false
        if (!contribution.contentEquals(other.contribution)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = sampleSize
        result = 31 * result + chiSquare.hashCode()
        result = 31 * result + pValue.hashCode()
        result = 31 * result + isSignificant.hashCode()
        result = 31 * result + id.hashCode()
        result = 31 * result + expectedDistribution.contentHashCode()
        result = 31 * result + actualDistribution.contentHashCode()
        result = 31 * result + contribution.contentHashCode()
        return result
    }
}
