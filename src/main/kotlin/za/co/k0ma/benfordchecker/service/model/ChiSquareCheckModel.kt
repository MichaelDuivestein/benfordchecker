package za.co.k0ma.za.co.k0ma.benfordchecker.service.model

import java.util.UUID

data class ChiSquareCheckModel(
    val id: UUID,
    val payload: String,
    // Due to concerns about accuracy, I'd normally use BigDecimal for numbers, however the Apache library wants a double.
    val significanceLevel: Double,
    var minValueCount: Int,
) {
    constructor(payload: String, significanceLevel: Double, minValueCount: Int) : this(
        id = UUID.randomUUID(),
        payload = payload,
        significanceLevel = significanceLevel,
        minValueCount = minValueCount,
    )

    constructor(payload: String, significanceLevel: Double) : this(
        id = UUID.randomUUID(),
        payload = payload,
        significanceLevel = significanceLevel,
        minValueCount = 5,
    )
}
