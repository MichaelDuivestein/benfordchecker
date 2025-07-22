package za.co.k0ma.za.co.k0ma.benfordchecker.api.dto

import za.co.k0ma.za.co.k0ma.benfordchecker.service.model.ChiSquareCheckModel
import java.util.UUID

data class ChiSquareRequest(
    val id: UUID = UUID.randomUUID(),
    val payload: String,
    val significanceLevel: Double,
    // The minimum number of values to calculate with. If there are fewer values than this number, an error will be
    // returned. Default 5. (Note: This would normally be part of the OpenAPI documentation)
    val minValueCount: Int?,
) {
    fun toModel() =
        ChiSquareCheckModel(
            id = id,
            payload = payload,
            significanceLevel = significanceLevel,
            minValueCount = minValueCount ?: 5,
        )
}
