package org.example.com.cyberspeed.lottery

import com.cyberspeed.lottery.json.parseLotteryConfig
import com.cyberspeed.lottery.service.LotteryService
import com.cyberspeed.lottery.validation.validate
import java.util.*

class Main

fun main() {
    val s = Main::class.java.getResource("/config.json")?.readText(Charsets.UTF_8)
    val lotteryConfig = parseLotteryConfig(s)

    validate(lotteryConfig)

    val service = LotteryService(
        randomGenerator = Random(),
        config = lotteryConfig
    )

    val lotteryField = service.generateLottery()
    println()

}