package com.cyberspeed.lottery.service

import com.cyberspeed.lottery.data.Characters
import com.cyberspeed.lottery.data.CombinationType
import com.cyberspeed.lottery.data.CombinationType.*
import com.cyberspeed.lottery.data.Lottery
import com.cyberspeed.lottery.data.RewardAction
import com.cyberspeed.lottery.data.RewardAction.MULTIPLY
import com.cyberspeed.lottery.data.RewardAction.SUM
import java.util.*
import kotlin.math.roundToInt

class LotteryService(
    val config: Lottery,
    val randomGenerator: Random = Random(),
) {
    fun generateLottery(): List<List<Characters>> {
        val rows = config.gameArea.rows
        val columns = config.gameArea.columns
        val result: Array<Array<Characters>> = Array(rows) { Array(columns) { Characters.NONE } }

        val generationChances = config.symbols.mapValues { it.value.generationChance }
        config.gameArea
            .possiblePositions
            .forEach {
                result[it.x][it.y] = generateCell(it.availableSymbols
                    .map { s -> s to generationChances.getOrDefault(s, 0.5f) })
            }

        return result.map { it.toList() }
    }

    fun play(lotteryField: List<List<Characters>>, bet: Int): Int {
        var totalWin = bet

        var maxSameXCombination: CombinationType? = null

        if (ifSame(lotteryField, 3)) maxSameXCombination = SAME_3
        if (ifSame(lotteryField, 4)) maxSameXCombination = SAME_4
        if (ifSame(lotteryField, 5)) maxSameXCombination = SAME_5
        if (ifSame(lotteryField, 6)) maxSameXCombination = SAME_6
        if (ifSame(lotteryField, 7)) maxSameXCombination = SAME_7
        if (ifSame(lotteryField, 8)) maxSameXCombination = SAME_8
        if (ifSame(lotteryField, 9)) maxSameXCombination = SAME_9

        if (maxSameXCombination != null) {
            totalWin = calculate(maxSameXCombination, bet)
        }

        if (ifSameHorizontal(lotteryField)) totalWin = calculate(SAME_HORIZONTAL, totalWin)
        if (ifSameVertical(lotteryField)) totalWin = calculate(SAME_VERTICAL, totalWin)
        if (ifSameDiagonal(lotteryField)) totalWin = calculate(SAME_DIAGONAL, totalWin)

        var totalWithMultipliers = 1
        var totalWithAdditions = 0

        if (totalWin == bet && maxSameXCombination == null) {
            println("No winning combinations")
            return 0
        }

        lotteryField.flatten().forEach {
            when (it) {
                Characters.X5 -> {
                    println("Applying $it")
                    totalWithMultipliers = Math.max(totalWithMultipliers, 5)
                }

                Characters.X10 -> {
                    println("Applying $it")
                    totalWithMultipliers = Math.max(totalWithMultipliers, 10)
                }

                Characters.P1000 -> {
                    println("Applying $it")
                    totalWithAdditions += 1000
                }

                Characters.P500 -> {
                    println("Applying $it")
                    totalWithAdditions += 500
                }

                Characters.A -> {}
                Characters.B -> {}
                Characters.C -> {}
                Characters.D -> {}
                Characters.E -> {}
                Characters.F -> {}
                Characters.NONE -> {}
            }
        }
        
        totalWin = totalWin * totalWithMultipliers + totalWithAdditions
        return totalWin 
    }

    private fun calculate(combination: CombinationType, bet: Int): Int {
        println("Applying $combination")
        val reward = config.winCombinations[combination]
            ?: throw LotteryException("Win combination $combination not exist in config")
        return doMath(reward.reward.action, reward.reward.amount, bet)
    }

    private fun doMath(action: RewardAction, amount: Float, bet: Int): Int {
        return when (action) {
            MULTIPLY -> (bet * amount).roundToInt()
            SUM -> (bet + amount).roundToInt()
        }
    }

    private fun ifSame(lotteryField: List<List<Characters>>, times: Int): Boolean {
        var result = false
        val symbols = lotteryField.flatten().groupingBy { it }.eachCount()
        symbols.forEach { if (it.value == times && it.key.isBasicCharacters()) result = true }
        return result
    }

    private fun ifSameHorizontal(lotteryField: List<List<Characters>>): Boolean {
        repeat(config.gameArea.rows) { x ->
            val symbol = lotteryField[x][0]
            if (symbol.isBonusCharacters()) return@repeat
            var allSame = true
            repeat(config.gameArea.columns) { y ->
                if (lotteryField[x][y] != symbol) {
                    allSame = false
                    return@repeat
                }
            }
            if (allSame) return true
        }
        return false
    }

    private fun ifSameVertical(lotteryField: List<List<Characters>>): Boolean {
        repeat(config.gameArea.columns) { y ->
            val symbol = lotteryField[0][y]
            if (symbol.isBonusCharacters()) return@repeat
            var allSame = true
            repeat(config.gameArea.rows) { x ->
                if (lotteryField[x][y] != symbol) {
                    allSame = false
                    return@repeat
                }
            }
            if (allSame) return true
        }
        return false
    }

    private fun ifSameDiagonal(lotteryField: List<List<Characters>>): Boolean {
        if (lotteryField.isEmpty() || lotteryField[0].isEmpty()) return false

        val n = lotteryField.size
        val m = lotteryField[0].size

        val mainDiagonalChar = lotteryField[0][0]
        var isMainDiagonalSame = true
        repeat(minOf(n, m)) { i ->
            if (lotteryField[i][i] != mainDiagonalChar || mainDiagonalChar.isBonusCharacters()) {
                isMainDiagonalSame = false
                return@repeat
            }
        }

        val secondaryDiagonalChar = lotteryField[0][m - 1]
        var isSecondaryDiagonalSame = true
        repeat(minOf(n, m)) { i ->
            if (lotteryField[i][m - i - 1] != secondaryDiagonalChar || secondaryDiagonalChar.isBonusCharacters()) {
                isSecondaryDiagonalSame = false
                return@repeat
            }
        }

        return isMainDiagonalSame || isSecondaryDiagonalSame
    }

    private fun generateCell(charChance: List<Pair<Characters, Float>>): Characters {
        while (true) {
            val possibleChar = randomGenerator.nextInt(charChance.size)
            val chance = randomGenerator.nextFloat()
            if (chance <= 0f) throw IllegalArgumentException("Chance cannot be 0")

            if (chance > charChance[possibleChar].second)
                return charChance[possibleChar].first
        }
    }
}

class LotteryException(message: String) : Exception(message)