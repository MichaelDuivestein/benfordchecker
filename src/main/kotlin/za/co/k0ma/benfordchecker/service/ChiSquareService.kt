package za.co.k0ma.za.co.k0ma.benfordchecker.service

import org.apache.commons.math3.distribution.ChiSquaredDistribution
import za.co.k0ma.za.co.k0ma.benfordchecker.exception.BadDataException
import za.co.k0ma.za.co.k0ma.benfordchecker.service.model.ChiSquareCheckModel
import za.co.k0ma.za.co.k0ma.benfordchecker.service.model.ChiSquareResult
import java.util.regex.Matcher
import java.util.regex.Pattern
import kotlin.math.log10
import kotlin.math.pow

class ChiSquareService {
    // Save CPU cycles by declaring the patterns once per instance
    // This pattern looks pretty complex because it needs to deal with malformed numbers (".1.2;3.2" -> "1.2", "3.2")
    private val numericPattern: Pattern = Pattern.compile("(?<!\\d)(?:0*\\d+(?:\\.\\d+)?|(?<=\\.)\\d+)")
    private val oneToNineDigitPattern: Pattern = Pattern.compile("[1-9]")

    fun performAnalysis(model: ChiSquareCheckModel): ChiSquareResult {
        if (model.payload.isBlank()) {
            throw BadDataException("Payload is empty")
        }

        val distribution = generateBenfordDistribution(model.payload)
        val sampleSize = distribution.sum()
        if (sampleSize < model.minValueCount) {
            throw BadDataException(
                "Found $sampleSize numbers in input string, which is less than the minimum of " +
                    "${model.minValueCount}",
            )
        }

        val expectedDistribution = DoubleArray(9)
        val distributionContribution = DoubleArray(9)
        var chiSquare = 0.0
        (0..8).forEach { num ->
            expectedDistribution[num] = log10(1 + 1.0 / (num + 1)) * sampleSize
            val expected = expectedDistribution[num]

            val actual = distribution[num].toDouble()
            val contribution = if (expected == 0.0) 0.0 else (actual - expected).pow(2) / expected
            distributionContribution[num] = contribution
            chiSquare += contribution
        }

        val chiDist = ChiSquaredDistribution(8.0) // 9 digits -1 == 8
        val pValue = 1 - chiDist.cumulativeProbability(chiSquare)

        return ChiSquareResult(
            id = model.id,
            sampleSize = sampleSize,
            chiSquare = chiSquare,
            pValue = pValue,
            expectedDistribution = expectedDistribution,
            actualDistribution = distribution,
            contribution = distributionContribution,
            isSignificant = pValue < model.significanceLevel,
        )
    }

    internal fun generateBenfordDistribution(payload: String): IntArray {
        val distribution = IntArray(9) { 0 }

        // find all [int and decimal] numbers in input string
        val numericMatcher: Matcher = numericPattern.matcher(payload)
        while (numericMatcher.find()) {
            getFirstNonZeroDigit(numericMatcher.group())?.let { distribution[it - 1]++ }
        }

        return distribution
    }

    internal fun getFirstNonZeroDigit(numberString: String): Int? {
        val digitMatcher = oneToNineDigitPattern.matcher(numberString)
        if (digitMatcher.find()) {
            return digitMatcher.group().toInt()
        }
        return null
    }
}
