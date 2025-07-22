package za.co.k0ma

import io.ktor.http.ContentType
import io.ktor.serialization.jackson.JacksonConverter
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import org.koin.ktor.plugin.Koin
import za.co.k0ma.benfordchecker.config.OBJECT_MAPPER
import za.co.k0ma.za.co.k0ma.benfordchecker.module.chiSquareModule

fun main() {
    embeddedServer(Netty, port = 8080) {
        main()
        module()
    }.start(wait = true)
}

fun Application.main() {
    install(Koin) {
        modules(chiSquareModule)
    }
}

fun Application.module() {
    configureRouting()
    configureContentNegotiation()
}

fun Application.configureContentNegotiation() {
    install(ContentNegotiation) {
        register(ContentType.Application.Json, JacksonConverter(OBJECT_MAPPER))
    }
}
