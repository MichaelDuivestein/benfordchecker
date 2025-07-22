package za.co.k0ma.benfordchecker.util

import kotlin.test.assertEquals

fun assertDoubleArrayEquals(
    expectedDistribution: DoubleArray,
    actualDistribution: DoubleArray,
    delta: Double,
) {
    assertEquals(expectedDistribution.size, actualDistribution.size)

    for (index in 0 until expectedDistribution.size) {
        assertEquals(expectedDistribution[index], actualDistribution[index], delta)
    }
}

fun assertDoubleArrayEquals(
    expectedDistribution: DoubleArray,
    actualDistribution: DoubleArray,
) {
    assertEquals(expectedDistribution.size, actualDistribution.size)

    for (index in 0 until expectedDistribution.size) {
        assertEquals(expectedDistribution[index], actualDistribution[index], 0.0001)
    }
}
