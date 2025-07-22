package za.co.k0ma.za.co.k0ma.benfordchecker.api.dto

import za.co.k0ma.za.co.k0ma.benfordchecker.service.model.ChiSquareResult
import java.util.UUID

data class ChiSquareResponse(
    val id: UUID,
    val sampleSize: Int,
    val chiSquareResult: List<ChiSquareData>,
    val pValue: Double,
    val isSignificant: Boolean,
) {
    constructor(result: ChiSquareResult) : this(
        id = result.id,
        sampleSize = result.sampleSize,
        chiSquareResult =
            (0..8).map { index ->
                ChiSquareData(
                    digit = index + 1,
                    expected = result.expectedDistribution[index],
                    actual = result.actualDistribution[index],
                    contribution = result.contribution[index],
                )
            },
        pValue = result.pValue,
        isSignificant = result.isSignificant,
    )
}

data class ChiSquareData(
    val digit: Int,
    val expected: Double,
    val actual: Int,
    val contribution: Double,
)
