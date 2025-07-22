package za.co.k0ma.za.co.k0ma.benfordchecker.module

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import za.co.k0ma.za.co.k0ma.benfordchecker.service.ChiSquareService

val chiSquareModule =
    module {
        singleOf(::ChiSquareService)
    }
