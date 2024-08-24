package com.cyberspeed.lottery.validation

import com.cyberspeed.lottery.data.Lottery
import com.cyberspeed.lottery.data.PositionDescription

fun validate(lottery: Lottery) {
    //validate gaming field
    val rows = lottery.gameArea.rows
    if (rows < 1) throw LotteryValidationException("Numbers of rows should not be less than 1")
    val columns = lottery.gameArea.columns
    if (columns < 1) throw LotteryValidationException("Numbers of columns should not be less than 1")
    
    lottery.symbols.forEach { kv ->
        //validate generation chance
        if (kv.value.generationChance < 0 || kv.value.generationChance > 1)
            throw LotteryValidationException("Generation change for symbol ${kv.key} should be between 0 and 1")
    }

    //validate positions
    val positions = lottery.gameArea.possiblePositions.size
    if (rows * columns != positions)
        throw LotteryValidationException("Configured positions size not equals gaming field. rows $rows, columns $columns, possiblePositions $positions")
    repeat(rows) { x ->
        repeat(columns) { y ->
            if (!lottery.gameArea.possiblePositions.contains(PositionDescription(x, y, emptyList())))
                throw LotteryValidationException("Position $x $y not configured")
            
            lottery.gameArea.possiblePositions.flatMap { it.availableSymbols }
                .forEach { 
                    if(!lottery.symbols.keys.contains(it)) throw LotteryValidationException("Symbol $it not configured")
                }
        }
    }
}

class LotteryValidationException(message: String): Exception(message)