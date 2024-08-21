package com.cyberspeed.lottery.service

import com.cyberspeed.lottery.data.CellPosition
import com.cyberspeed.lottery.data.Characters
import com.cyberspeed.lottery.data.Lottery
import java.util.*

class LotteryService(
    val randomGenerator: Random,
    val config: Lottery
) {

    fun generateLottery(): List<List<Characters>> {
        val rows = config.rows
        val columns = config.columns
        val result: Array<Array<Characters>> = Array(rows) { Array(columns) { Characters.NONE } }
        // generate symbols for each cellar

        repeat(rows) { x ->
            repeat(columns) { y ->
                val charChance = config.symbols
                    .filter { it.value.possiblePositions.contains(CellPosition(x, y)) }
                    .map { it.key to it.value.generationChance }
                val cell = generateCell(charChance)
                result[x][y] = cell
            }
        }

        return result.map { it.toList() }
    }

    private fun generateCell(charChance: List<Pair<Characters, Float>>): Characters {
        while (true) {
            val possibleChar = randomGenerator.nextInt(charChance.size)
            val chance = randomGenerator.nextFloat()

            if (chance > charChance[possibleChar].second)
                return charChance[possibleChar].first
        }
    }
}