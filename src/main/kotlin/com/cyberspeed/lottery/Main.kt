package com.cyberspeed.lottery

import com.cyberspeed.lottery.json.parseLotteryConfig
import com.cyberspeed.lottery.service.LotteryService
import com.cyberspeed.lottery.validation.LotteryValidationException
import com.cyberspeed.lottery.validation.validate
import java.io.File

fun main(args: Array<String>) {
    // Step 1: Parse command-line arguments
    val configPath = args.find { it.startsWith("--config=") }?.substringAfter("=")
    val bettingAmountStr = args.find { it.startsWith("--betting-amount=") }?.substringAfter("=")

    if (configPath == null || bettingAmountStr == null) {
        println("Arguments did not passed --config=$configPath and --betting-amount=$bettingAmountStr")
        return
    }

    val bettingAmount = bettingAmountStr.toIntOrNull()
    if (bettingAmount == null || bettingAmount <= 0) {
        println("Invalid betting amount: $bettingAmountStr")
        return
    }

    // Step 2: Load configuration from JSON file
    val config = File(configPath).readText()
    val parsedConfig = parseLotteryConfig(config)

    // Step 3: Validate the configuration
    try {
        validate(parsedConfig)
    } catch (e: LotteryValidationException) {
        println("Configuration validation failed: ${e.message}")
        return
    }

    // Step 4: Create the LotteryService
    val lotteryService = LotteryService(config = parsedConfig)

    // Step 5: Generate a lottery field
    val lotteryField = lotteryService.generateLottery()

    // Step 6: Print the generated lottery field
    println("Generated Lottery Field:")
    lotteryField.forEach { row ->
        println(row.joinToString(" ") { it.name })
    }

    // Step 7: Simulate the play
    val winnings = lotteryService.play(lotteryField, bettingAmount)

    // Step 8: Print the result
    println("Betting amount: $bettingAmount")
    println("Winnings: $winnings")
}

