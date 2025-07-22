package za.co.k0ma.benfordchecker

import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsBytes
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.jackson.JacksonConverter
import io.ktor.server.config.MapApplicationConfig
import io.ktor.server.testing.testApplication
import org.junit.Test
import za.co.k0ma.benfordchecker.config.OBJECT_MAPPER
import za.co.k0ma.main
import za.co.k0ma.module
import za.co.k0ma.za.co.k0ma.benfordchecker.api.dto.ChiSquareResponse
import za.co.k0ma.za.co.k0ma.benfordchecker.service.model.ChiSquareCheckModel
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull

class ApplicationTest {
    @Test
    fun `application should produce an expected result`() {
        testApplication {
            application {
                main()
                module()
            }
            environment {
                config = MapApplicationConfig(listOf("ktor.deployment.host" to "localhost"))
            }

            client =
                createClient {
                    install(ContentNegotiation) {
                        register(ContentType.Application.Json, JacksonConverter(OBJECT_MAPPER))
                    }
                }

            val model =
                ChiSquareCheckModel(
                    // 200 numbers. Once zeroes are discarded, the numbers are distributed according to Benford's Law.
                    payload =
                        "431000000, 06645510171571076281020000, 397816207040, 1.6140, 02.4783644190, 001.585620, " +
                            "0004322706618417528211000, 00004.711, 004980352384901822733, 00001.128128530, " +
                            "0001.51534950880000, 12011107698340539, 002.633272650, 41709167140700, " +
                            "000010576065360832003436681836675, 005.94670237960000, 3491616061871266662, 0735584, " +
                            "224006136113700, 0003.33434360622762752, 1.1796, 0000292745568316438736000, " +
                            "000051089480642807748549715527039, 38268399480868700, 00008.6533520671925000, " +
                            "0004.4301160, 08.8575332000, 001.57008600, 081046711106364477413193710, 0001.612000, " +
                            "01.76335709882790, 007.82440000, 00005.8671032275000, 0000160298000, 019619818310000, " +
                            "0020289353448264165444875000, 00005578154000, 0001.8596278033883300, 005.55759034460, " +
                            "0005.32500, 3.0000, 00546638321983192354600, 00001.757295447700, 008.624845750000, " +
                            "01.500024796258000, 00042886975682507874496, 004.32615469270000, 00004.3966890237300, " +
                            "02.800, 07.262911225918280820, 00005.32556, 0005.051655143161742308000, 00073522679000, " +
                            "001.2228828424662905, 001100, 03.46075048200, 00006525296782088822518690200, 27100, " +
                            "00722402257030262609409, 005.790000, 0001.000, 03.82052530829100, 1.865606430, 0001.318, " +
                            "9.751349278, 194168154957200, 0014091618000, 1.88349870324800, 1.9505493000, " +
                            "0002.3909062766200, 9.1500, 004.0468591100000, 01.189907633000, 01.725634456805000, " +
                            "5923209416900477943535169000, 582230172355704837772897056, 003983490000, " +
                            "0503258180598372155914166000, 0012997434788122375536200, 0550209897703630040000, " +
                            "0000698695194106884739110, 0003908795419000435284846690270, 0000162218470050000," +
                            "02.464171653984000, 0008.61495815, 02510, 00001.986570, 08.940000, 00002.2485000, " +
                            "497031838243249978378001970000, 002.429270807, 001.10, 00003.499500, 001.7539470419956500, " +
                            "000948267474433904097936961200, 2.713852527882, 1.6548339940000, 003.57800, 04.78900, " +
                            "000200, 1.30936990200, 000043140, 001446, 000033381043748211878559765323514000, " +
                            "3355349844475601362097376000, 1.5470004142, 00189761510838541260700, 1.64784786800, " +
                            "0001.39454063485, 0001.8754098892000, 000055527550854, 06153780014953632157435000, " +
                            "1637471704670956225011986900, 1.7959061256649168520000, 0003.644095878, 03.263960887474000, " +
                            "2.98612352508, 00437119004964500, 5.7003121000, 000095600000, 0000294500, " +
                            "139924031463338923190800, 001.0651131500, 00007.577690498033374900, 0000215200, 0079772090, " +
                            "0008.666000, 02.19651500, 00005.10259193351355, 001900, 0061346629643960298000, " +
                            "91063032976633713784100, 0938628156297000, 00003.14033680000, 2.6885570869400000, " +
                            "01.510000, 2.92991665000, 02.0777650882450800, 000022709809985800000, " +
                            "000346628268894681006353346050, 9472200000, 000539462408850729378662000, " +
                            "0000185736082508200213007304730040000, 2903107669542600000, 01.3440, " +
                            "03025411951719263839910000, 04017637130632071991565170000, 00017800, 006772219999005127398, " +
                            "05.0, 7.8495000, 00087659740428036541300730445549000, 00001.3488000, " +
                            "0000990016948469335547600, 00020132691067752842000, 3.8635479473500, 00004.2967887432485, " +
                            "00008.1285485992390000, 0004.2785288453329074550000, 03.72639203030, 0001216, " +
                            "0101976719980930600, 00003.04533406407248420000, 001617280456858200, 00070, " +
                            "00001.03382126900, 002842876641593535114922142262, 0000161490967250254612583540406740000, " +
                            "00001756339486914853745060763700, 0009966144573622400, 000935116293000, 78183560179720000, " +
                            "0003.1150965241320910000, 009.000, 0609470000, 1.4640, 03.3800, 026522659836500, " +
                            "0001.1440499500, 000075966749156124369370801, 00003.25564300, 001.9144346865955350000, " +
                            "007851275897457528264589936235000, 036395550289640998091284048290000, " +
                            "0382654238286197381886617400, 0068488362500, 0002218557331222387888040, " +
                            "000031264031028527297010, 1.00, 01.840, 04.8, 008.912130, 00009.67070000, 010000, " +
                            "01399530083947832260000, 002.645393260800, 007.48120699400, 00004.0719774984647943430, " +
                            "3.92852866490804715, 1.460",
                    significanceLevel = 0.05,
                )

            val response =
                client.post("/v0/chi-square") {
                    header("host", "api.localhost:8080")
                    contentType(ContentType.Application.Json)
                    setBody(model)
                }
            assertEquals(HttpStatusCode.OK, response.status)

            val body = response.bodyAsText()
            assertNotNull(body)

            val result = OBJECT_MAPPER.readValue(response.bodyAsBytes(), ChiSquareResponse::class.java)
            assertNotNull(result)
            assertEquals(200, result.sampleSize)
            assertEquals(0.664894, result.pValue, 0.000001)
            assertFalse(result.isSignificant)

            val delta = 0.00001
            assertEquals(9, result.chiSquareResult.size)

            assertEquals(1, result.chiSquareResult[0].digit)
            assertEquals(60.205999, result.chiSquareResult[0].expected, delta)
            assertEquals(63, result.chiSquareResult[0].actual)
            assertEquals(0.129662, result.chiSquareResult[0].contribution, delta)

            assertEquals(2, result.chiSquareResult[1].digit)
            assertEquals(35.218251, result.chiSquareResult[1].expected, delta)
            assertEquals(28, result.chiSquareResult[1].actual)
            assertEquals(1.479436, result.chiSquareResult[1].contribution, delta)

            assertEquals(3, result.chiSquareResult[2].digit)
            assertEquals(24.987747, result.chiSquareResult[2].expected, delta)
            assertEquals(28, result.chiSquareResult[2].actual)
            assertEquals(0.363124, result.chiSquareResult[2].contribution, delta)

            assertEquals(4, result.chiSquareResult[3].digit)
            assertEquals(19.382002, result.chiSquareResult[3].expected, delta)
            assertEquals(19, result.chiSquareResult[3].actual)
            assertEquals(0.007528, result.chiSquareResult[3].contribution, delta)

            assertEquals(5, result.chiSquareResult[4].digit)
            assertEquals(15.836249, result.chiSquareResult[4].expected, delta)
            assertEquals(19, result.chiSquareResult[4].actual)
            assertEquals(0.632051, result.chiSquareResult[4].contribution, delta)

            assertEquals(6, result.chiSquareResult[5].digit)
            assertEquals(13.389357, result.chiSquareResult[5].expected, delta)
            assertEquals(8, result.chiSquareResult[5].actual)
            assertEquals(2.169273, result.chiSquareResult[5].contribution, delta)

            assertEquals(7, result.chiSquareResult[6].digit)
            assertEquals(11.598389, result.chiSquareResult[6].expected, delta)
            assertEquals(13, result.chiSquareResult[6].actual)
            assertEquals(0.169378, result.chiSquareResult[6].contribution, delta)

            assertEquals(8, result.chiSquareResult[7].digit)
            assertEquals(10.230504, result.chiSquareResult[7].expected, delta)
            assertEquals(10, result.chiSquareResult[7].actual)
            assertEquals(0.005193, result.chiSquareResult[7].contribution, delta)

            assertEquals(9, result.chiSquareResult[8].digit)
            assertEquals(9.151498, result.chiSquareResult[8].expected, delta)
            assertEquals(12, result.chiSquareResult[8].actual)
            assertEquals(0.886626, result.chiSquareResult[8].contribution, delta)
        }
    }

    @Test
    fun `application should return an error if request payload is empty`() {
        testApplication {
            application {
                main()
                module()
            }
            environment {
                config = MapApplicationConfig(listOf("ktor.deployment.host" to "localhost"))
            }

            client =
                createClient {
                    install(ContentNegotiation) {
                        register(ContentType.Application.Json, JacksonConverter(OBJECT_MAPPER))
                    }
                }

            val model =
                ChiSquareCheckModel(
                    payload = "",
                    significanceLevel = 0.05,
                    minValueCount = 4,
                )

            val response =
                client.post("/v0/chi-square") {
                    header("host", "api.localhost:8080")
                    contentType(ContentType.Application.Json)
                    setBody(model)
                }
            assertEquals(HttpStatusCode.BadRequest, response.status)

            assertEquals("Payload is empty", response.bodyAsText())
        }
    }
}
