package za.co.k0ma.benfordchecker.service

import org.junit.jupiter.api.assertThrows
import za.co.k0ma.benfordchecker.util.assertDoubleArrayEquals
import za.co.k0ma.za.co.k0ma.benfordchecker.exception.BadDataException
import za.co.k0ma.za.co.k0ma.benfordchecker.service.ChiSquareService
import za.co.k0ma.za.co.k0ma.benfordchecker.service.model.ChiSquareCheckModel
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class ChiSquareServiceTest {
    // ////////////// getFirstNonZeroDigit tests: ////////////////
    @Test
    fun `getFirstNonZeroDigit should get first nonZero digit from an integer string`() {
        val service = ChiSquareService()

        assertEquals(1, service.getFirstNonZeroDigit("1"))
        assertEquals(2, service.getFirstNonZeroDigit("02"))
        assertEquals(3, service.getFirstNonZeroDigit("300002"))
    }

    @Test
    fun `getFirstNonZeroDigit should get first nonZero digit from an decimal string`() {
        val service = ChiSquareService()

        assertEquals(1, service.getFirstNonZeroDigit("1.00"))
        assertEquals(2, service.getFirstNonZeroDigit("0.002"))
        assertEquals(3, service.getFirstNonZeroDigit("3.00002"))
        assertEquals(4, service.getFirstNonZeroDigit("045.000043"))
    }

    @Test
    fun `getFirstNonZeroDigit should return null if there are only zero digits in the string`() {
        val service = ChiSquareService()

        assertEquals(null, service.getFirstNonZeroDigit("0"))
        assertEquals(null, service.getFirstNonZeroDigit("00"))
        assertEquals(null, service.getFirstNonZeroDigit("00.00"))
    }

    @Test
    fun `getFirstNonZeroDigit should return the first digit even if there are non-digit characters in the string`() {
        val service = ChiSquareService()

        assertEquals(1, service.getFirstNonZeroDigit("$1"))
        assertEquals(2, service.getFirstNonZeroDigit("$02"))
        assertEquals(3, service.getFirstNonZeroDigit("$30"))

        assertEquals(4, service.getFirstNonZeroDigit("4kr"))
        assertEquals(5, service.getFirstNonZeroDigit("05kr"))
        assertEquals(6, service.getFirstNonZeroDigit("60kr"))

        assertEquals(7, service.getFirstNonZeroDigit("$0.07"))
        assertEquals(8, service.getFirstNonZeroDigit("$0.80"))
        assertEquals(9, service.getFirstNonZeroDigit("$9.00"))
        assertEquals(1, service.getFirstNonZeroDigit("$01.00"))

        assertEquals(2, service.getFirstNonZeroDigit("0.02 öre"))
        assertEquals(3, service.getFirstNonZeroDigit("0.30 öre"))
        assertEquals(4, service.getFirstNonZeroDigit("4.00kr"))
        assertEquals(5, service.getFirstNonZeroDigit("05.00kr"))

        // Edge case: this should be parsed as two numbers by `CheckService.generateDistribution(...)`.
        // These tests exist in case someone decides to use this function on its own.
        assertEquals(6, service.getFirstNonZeroDigit("06kr, 03öre"))
        assertEquals(7, service.getFirstNonZeroDigit("007;008"))
    }

    @Test
    fun `getFirstNonZeroDigit should return null if there are only zero digits and non-digit characters in the string`() {
        val service = ChiSquareService()

        assertEquals(null, service.getFirstNonZeroDigit("$0"))
        assertEquals(null, service.getFirstNonZeroDigit("$00"))
        assertEquals(null, service.getFirstNonZeroDigit("$00.00"))

        assertEquals(null, service.getFirstNonZeroDigit("0kr"))
        assertEquals(null, service.getFirstNonZeroDigit("00kr"))
        assertEquals(null, service.getFirstNonZeroDigit("00.00öre"))
    }

    @Test
    fun `getFirstNonZeroDigit should return null if there are only non-digit characters in the string`() {
        val service = ChiSquareService()

        assertEquals(null, service.getFirstNonZeroDigit("Five"))
        assertEquals(null, service.getFirstNonZeroDigit("Null"))
        assertEquals(null, service.getFirstNonZeroDigit("Something went wrong here"))
        assertEquals(null, service.getFirstNonZeroDigit(" "))
        assertEquals(null, service.getFirstNonZeroDigit("_"))
        assertEquals(null, service.getFirstNonZeroDigit("."))
        assertEquals(null, service.getFirstNonZeroDigit(","))
        assertEquals(null, service.getFirstNonZeroDigit(";"))
    }

    @Test
    fun `getFirstNonZeroDigit should return null for an empty string`() {
        val service = ChiSquareService()

        assertEquals(null, service.getFirstNonZeroDigit(""))
    }

    // ////////////// generateDistribution tests: ////////////////
    @Test
    fun `generateDistribution should generate expected distribution`() {
        val service = ChiSquareService()

        val distribution = service.generateBenfordDistribution("1, 01, 10, 0.1, .1.2;3.2, .050.")
        assertNotNull(distribution)
        assertEquals(9, distribution.size)
        assertEquals(5, distribution[0])
        assertEquals(0, distribution[1])
        assertEquals(1, distribution[2])
        assertEquals(0, distribution[3])
        assertEquals(1, distribution[4])
        assertEquals(0, distribution[5])
        assertEquals(0, distribution[6])
        assertEquals(0, distribution[7])
        assertEquals(0, distribution[8])
    }

    @Test
    fun `generateDistribution should return an array of zeroes if the payload is empty`() {
        val service = ChiSquareService()

        var distribution = service.generateBenfordDistribution("")
        assertNotNull(distribution)
        assertEquals(9, distribution.size)
        assertEquals(0, distribution[0])
        assertEquals(0, distribution[1])
        assertEquals(0, distribution[2])
        assertEquals(0, distribution[3])
        assertEquals(0, distribution[4])
        assertEquals(0, distribution[5])
        assertEquals(0, distribution[6])
        assertEquals(0, distribution[7])
        assertEquals(0, distribution[8])

        distribution = service.generateBenfordDistribution("   ")
        assertNotNull(distribution)
        assertEquals(9, distribution.size)
        assertEquals(0, distribution[0])
        assertEquals(0, distribution[1])
        assertEquals(0, distribution[2])
        assertEquals(0, distribution[3])
        assertEquals(0, distribution[4])
        assertEquals(0, distribution[5])
        assertEquals(0, distribution[6])
        assertEquals(0, distribution[7])
        assertEquals(0, distribution[8])
    }

    @Test
    fun `generateDistribution should return an array of zeroes if there are no non-zero digits`() {
        val service = ChiSquareService()

        var distribution = service.generateBenfordDistribution("0, 0.00, 00, 00.00 0;0 .0   ")
        assertNotNull(distribution)
        assertEquals(9, distribution.size)
        assertEquals(0, distribution[0])
        assertEquals(0, distribution[1])
        assertEquals(0, distribution[2])
        assertEquals(0, distribution[3])
        assertEquals(0, distribution[4])
        assertEquals(0, distribution[5])
        assertEquals(0, distribution[6])
        assertEquals(0, distribution[7])
        assertEquals(0, distribution[8])

        distribution = service.generateBenfordDistribution("aba0, 0.00kr, 00, $00.00 0;0 .0____@!@@")
        assertNotNull(distribution)
        assertEquals(9, distribution.size)
        assertEquals(0, distribution[0])
        assertEquals(0, distribution[1])
        assertEquals(0, distribution[2])
        assertEquals(0, distribution[3])
        assertEquals(0, distribution[4])
        assertEquals(0, distribution[5])
        assertEquals(0, distribution[6])
        assertEquals(0, distribution[7])
        assertEquals(0, distribution[8])
    }

    @Test
    fun `generateDistribution should return an array of zeroes if there are no digits in the input`() {
        val service = ChiSquareService()

        val distribution = service.generateBenfordDistribution("Some random string")
        assertNotNull(distribution)
        assertEquals(9, distribution.size)
        assertEquals(0, distribution[0])
        assertEquals(0, distribution[1])
        assertEquals(0, distribution[2])
        assertEquals(0, distribution[3])
        assertEquals(0, distribution[4])
        assertEquals(0, distribution[5])
        assertEquals(0, distribution[6])
        assertEquals(0, distribution[7])
        assertEquals(0, distribution[8])
    }

    @Test
    fun `generateDistribution should handle very long numbers`() {
        val service = ChiSquareService()

        val distribution =
            service.generateBenfordDistribution("99900000010002309238932490238434029213.231409903032342340983243432")
        assertNotNull(distribution)
        assertEquals(9, distribution.size)
        assertEquals(0, distribution[0])
        assertEquals(0, distribution[1])
        assertEquals(0, distribution[2])
        assertEquals(0, distribution[3])
        assertEquals(0, distribution[4])
        assertEquals(0, distribution[5])
        assertEquals(0, distribution[6])
        assertEquals(0, distribution[7])
        assertEquals(1, distribution[8])
    }

    @Test
    fun `generateDistribution should generate a statistically-significant distribution`() {
        val service = ChiSquareService()

        val distribution = service.generateBenfordDistribution("1, 01, 10, 0.1, .1.2;3.2, .050.")
        assertNotNull(distribution)
        assertEquals(9, distribution.size)
        assertEquals(5, distribution[0])
        assertEquals(0, distribution[1])
        assertEquals(1, distribution[2])
        assertEquals(0, distribution[3])
        assertEquals(1, distribution[4])
        assertEquals(0, distribution[5])
        assertEquals(0, distribution[6])
        assertEquals(0, distribution[7])
        assertEquals(0, distribution[8])
    }

    // ////////////// performAnalysis tests: ////////////////
    @Test
    fun `performAnalysis should generate expected distribution`() {
        val service = ChiSquareService()

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

        val result = service.performAnalysis(model)

        val expectedExpectedDistribution =
            doubleArrayOf(
                60.20599913279624,
                35.21825181113625,
                24.987747321659988,
                19.382002601611283,
                15.836249209524963,
                13.389357926122644,
                11.598389395537346,
                10.230504489476258,
                9.151498112135029,
            )
        val expectedActualDistribution = intArrayOf(63, 28, 28, 19, 19, 8, 13, 10, 12)
        val expectedContribution =
            doubleArrayOf(0.1296, 1.4794, 0.3631, 0.0075, 0.6320, 2.1692, 0.1693, 0.0051, 0.8866)

        assertEquals(model.id, result.id)
        assertEquals(200, result.sampleSize)
        assertEquals(5.84227488, result.chiSquare, 0.000001)
        assertEquals(0.66489421, result.pValue, 0.000001)
        assertDoubleArrayEquals(expectedExpectedDistribution, result.expectedDistribution, 0.0001)
        assertContentEquals(expectedActualDistribution, result.actualDistribution)
        assertDoubleArrayEquals(expectedContribution, result.contribution, 0.0001)
        assertFalse { result.isSignificant } // The result is statistically significant
    }

    @Test
    fun `performAnalysis should throw BadDataException if payload is empty`() {
        val service = ChiSquareService()

        assertThrows<BadDataException>(
            "Payload is empty",
            { service.performAnalysis(ChiSquareCheckModel("", 0.05)) },
        )
        assertThrows<BadDataException>(
            "Payload is empty",
            { service.performAnalysis(ChiSquareCheckModel("     ", 0.05)) },
        )
    }

    @Test
    fun `performAnalysis should throw BadDataException if payload contains no usable numbers`() {
        val service = ChiSquareService()

        assertThrows<BadDataException>(
            "Found 0 numbers, which is less than the minimum of 5",
            { service.performAnalysis(ChiSquareCheckModel("abcde", 0.05)) },
        )
        assertThrows<BadDataException>(
            "Found 0 numbers, which is less than the minimum of 5",
            { service.performAnalysis(ChiSquareCheckModel("0, 00, 0.0, 0.00", 0.05)) },
        )
    }

    @Test
    fun `performAnalysis should throw BadDataException if payload contains fewer non-zero numbers than minValueCount`() {
        val service = ChiSquareService()

        assertThrows<BadDataException>(
            "Found 4 numbers, which is less than the minimum of 5",
            { service.performAnalysis(ChiSquareCheckModel("1, 2, 3, 4.1", 0.05, 5)) },
        )
        assertThrows<BadDataException>(
            "Found 3 numbers, which is less than the minimum of 4",
            { service.performAnalysis(ChiSquareCheckModel("0, 0.0, 1.0, 2.4, 00, 00.00, 75.1111", 0.05)) },
        )
        assertThrows<BadDataException>(
            "Found 3 numbers, which is less than the minimum of 5",
            { service.performAnalysis(ChiSquareCheckModel("$75.31, $0.00, 45kr, 0.00 SEK0", 0.05)) },
        )
    }

    @Test
    fun `performAnalysis should generate a statistically-significant result given statistically-compliant numbers`() {
        val service = ChiSquareService()

        val model =
            ChiSquareCheckModel(
                // 180 numbers. Once zeroes are discarded, there are exactly 20 numbers that begin with each digit.
                payload =
                    "6491 6197.5 15 0623 5536.4 309 65 4463 2622 06503 406 00435.70 9648 321 073 95.70 48 510.0 " +
                        "518.3 98 561.60 703.3 806 382.43 18 08476 700 008228.94 0079 2275.5 07933 782.7 2532.33 72 " +
                        "962.9 616 00271 811 177 08157 067 2420.1 5013.9 0895.6 2803 046.6 95.54 299.5 31 347 78.8 " +
                        "157.4 3463.77 8729 001799 1691 03415.7 408 9518.28 296 054 5354.15 278.68 0089 967.6 53.0 " +
                        "462 740 26.3 631 49 159.36 34 705 009710 85.24 48.5 1175.9 78.6 09135.10 40 568 3651.4 25.6 " +
                        "998.96 02913 837.84 20.05 64 00138.6 810 008965 876 088 231 07021.43 431.4 9999 151 998 " +
                        "6441 0083 00524.9 5267.17 0426.70 7506 347 9047.39 03902 003866 107.4 02915.70 0096 8621 " +
                        "408 15.3 9241.18 0018 19.62 0063 0786.2 0523 754 4393.97 00174 132 32 65.41 5251 4758.55 " +
                        "50.14 73 1290 178 8648 0439.0 4795 859 0062.13 2219 09852 62 0230.72 0896.6 579 37 2729.3 " +
                        "61 20.00 354.3 0904.71 689.0 0039.7 71.76 003633 3306 3145.38 00675 502 1249.3 420.2 1424.5 " +
                        "475 3155.32 63.12 0229.6 7401.25 0654 600 59 9625.34 51.43 0761 0961 5638 52.53 9883 8003 " +
                        "4651 7302",
                significanceLevel = 0.05,
            )

        val result = service.performAnalysis(model)

        val expectedExpectedDistribution =
            doubleArrayOf(54.1854, 31.6964, 22.4890, 17.4438, 14.2526, 12.0504, 10.4386, 9.2075, 8.2363)

        val expectedActualDistribution = intArrayOf(20, 20, 20, 20, 20, 20, 20, 20, 20)

        val expectedContribution =
            doubleArrayOf(21.5674, 4.3161, 0.2754, 0.3745, 2.3176, 5.2442, 8.7580, 12.6505, 16.8015)

        assertEquals(model.id, result.id)
        assertEquals(180, result.sampleSize)
        assertEquals(72.305692, result.chiSquare, 0.000001)
        assertEquals(0.0000000000017053025, result.pValue, 0.00000000000000001)
        assertDoubleArrayEquals(expectedExpectedDistribution, result.expectedDistribution, 0.0001)
        assertContentEquals(expectedActualDistribution, result.actualDistribution)
        assertDoubleArrayEquals(expectedContribution, result.contribution, 0.0001)
        assertTrue { result.isSignificant }
    }

    @Test
    fun `performAnalysis should handle a dirty payload`() {
        val service = ChiSquareService()

        val model =
            ChiSquareCheckModel(
                payload =
                    "Some text £6491 0.00kr 15;0623 5536.4, .30.9 65 4463 06503 406 00435.70 648 321 073 5.70 Sverige" +
                        "192.168.1.1 or localhost:8080 157.4 3463.77 8729 001799 1691 03415.7 408 9518.28 296 054 " +
                        "5354.15 278.68 0089 967.6kr,53.0kr,462kr;740 26.3 631 49 159.36 34 705 SEK009710 85.24 48.5 " +
                        "1175.9 78.6 09135.10 40 568 3651.4 25.6 998.96 02913 837.84 20.05 64 00138.6 810 008965 " +
                        "876 088 231 07021.43 431.4 9999 151 998 50.14 73 1290 178 8648 0439.0 4795 859 0062.13 2219 " +
                        "09852 62 0230.72 0896.6 579 37 2729.3 61 20.00 354.3 0904.71 689.0 0039.7 71.76 003633 " +
                        "3306 3145.38 00675 502 1249.3 420.2 1424.5 475 3155.32 63.12 0229.6 7401,9625.34 51.43 0761 0",
                significanceLevel = 0.05,
            )

        val result = service.performAnalysis(model)

        val expectedExpectedDistribution =
            doubleArrayOf(31.9091, 18.6656, 13.2435, 10.2724, 8.3932, 7.0963, 6.1471, 5.4221, 4.8502)

        val expectedActualDistribution = intArrayOf(14, 12, 13, 13, 10, 13, 9, 12, 10)

        val expectedContribution =
            doubleArrayOf(10.0516, 2.3803, 0.0044, 0.7242, 0.3076, 4.9113, 1.3239, 7.9798, 5.4676)

        assertEquals(model.id, result.id)
        assertEquals(106, result.sampleSize)
        assertEquals(33.151069, result.chiSquare, 0.000001)
        assertEquals(0.0000578451652067935, result.pValue, 0.00000000000000001)
        assertDoubleArrayEquals(expectedExpectedDistribution, result.expectedDistribution, 0.0001)
        assertContentEquals(expectedActualDistribution, result.actualDistribution)
        assertDoubleArrayEquals(expectedContribution, result.contribution, 0.0001)
        assertTrue { result.isSignificant }
    }
}
