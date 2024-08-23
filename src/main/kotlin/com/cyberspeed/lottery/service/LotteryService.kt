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
        config.winCombinations.forEach {
            when (it.key) {
                SAME_3 -> if(ifSame(lotteryField, 4)) totalWin = calculate(SAME_3, bet)
                SAME_4 -> if(ifSame(lotteryField, 4)) totalWin = calculate(SAME_4, bet)
                SAME_5 -> if(ifSame(lotteryField, 5)) totalWin = calculate(SAME_5, bet)
                SAME_6 -> if(ifSame(lotteryField, 6)) totalWin = calculate(SAME_6, bet)
                SAME_7 -> if(ifSame(lotteryField, 7)) totalWin = calculate(SAME_7, bet)
                SAME_8 -> if(ifSame(lotteryField, 8)) totalWin = calculate(SAME_8, bet)
                SAME_9 -> if(ifSame(lotteryField, 9)) totalWin = calculate(SAME_9, bet)
                SAME_HORIZONTAL -> if(ifSameHorizontal(lotteryField)) totalWin = calculate(SAME_HORIZONTAL, bet)
                SAME_VERTICAL -> if(ifSameVertical(lotteryField)) totalWin = calculate(SAME_VERTICAL, bet)
                SAME_DIAGONAL -> if(ifSameDiagonal(lotteryField)) totalWin = calculate(SAME_DIAGONAL, bet)
            }
        }
        
        //if won
        if (totalWin > bet) {
            config.symbols.keys.forEach{
                when (it){
                    Characters.X10 -> totalWin = doMath(MULTIPLY, 10f, totalWin)
                    Characters.X5 -> totalWin = doMath(MULTIPLY, 5f, totalWin)
                    Characters.P1000 -> totalWin = doMath(SUM, 1000f, totalWin)
                    Characters.P500 -> totalWin = doMath(SUM, 500f, totalWin)
                    Characters.A -> {/*NOTHING*/}
                    Characters.B -> {/*NOTHING*/}
                    Characters.C -> {/*NOTHING*/}
                    Characters.D -> {/*NOTHING*/}
                    Characters.E -> {/*NOTHING*/}
                    Characters.F -> {/*NOTHING*/}
                    Characters.NONE -> {/*NOTHING*/}
                }
            }
        }
        
        return totalWin
    }

    private fun calculate(combination: CombinationType, bet: Int): Int {
        val reward = config.winCombinations[combination]
            ?: throw LotteryException("Win combination $combination not exist in config")
        return doMath(reward.reward.action, reward.reward.amount, bet)
    }
    
    private fun doMath(action: RewardAction, amount: Float, bet: Int): Int{
        return when (action){
            MULTIPLY -> (bet * amount).roundToInt()
            RewardAction.SUM -> (bet + amount).roundToInt()
        }
    }

    private fun ifSame(lotteryField: List<List<Characters>>, times: Int): Boolean {
        var result = false
        val symbols = lotteryField.flatten().groupingBy { it }.eachCount()
        symbols.forEach { if(it.value == times) result = true }
        return result
    }

    private fun ifSameHorizontal(lotteryField: List<List<Characters>>): Boolean {
        var result = false
        TODO()
        return result
    }

    private fun ifSameVertical(lotteryField: List<List<Characters>>): Boolean {
        var result = false
        TODO()
        return result
    }

    private fun ifSameDiagonal(lotteryField: List<List<Characters>>): Boolean {
        var result = false
        TODO()
        return result
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

class LotteryException(message: String): Exception(message)