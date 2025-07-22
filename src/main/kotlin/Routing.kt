package za.co.k0ma

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import org.koin.ktor.ext.inject
import za.co.k0ma.za.co.k0ma.benfordchecker.api.dto.ChiSquareRequest
import za.co.k0ma.za.co.k0ma.benfordchecker.api.dto.ChiSquareResponse
import za.co.k0ma.za.co.k0ma.benfordchecker.exception.BadDataException
import za.co.k0ma.za.co.k0ma.benfordchecker.service.ChiSquareService

fun Application.configureRouting() {
    val chiSquareService by inject<ChiSquareService>()

    routing {
        post("/v0/chi-square") {
            val request = call.receive<ChiSquareRequest>()

            try {
                val result = chiSquareService.performAnalysis(request.toModel())
                call.respond(HttpStatusCode.OK, ChiSquareResponse(result))
            } catch (exception: BadDataException) {
                call.respond(HttpStatusCode.BadRequest, exception.message!!)
            }
        }
    }
}
