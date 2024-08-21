package com.cyberspeed.lottery.validation

import com.cyberspeed.lottery.data.Lottery

fun validate(lottery: Lottery) {
    //validate field
    if (lottery.rows < 1) throw LotteryValidationException("Numbers of rows should not be less than 1")
    if (lottery.columns < 1) throw LotteryValidationException("Numbers of columns should not be less than 1")
    lottery.symbols.forEach { kv ->
        //validate generation chance
        if (kv.value.generationChance < 0 && kv.value.generationChance > 1)
            throw LotteryValidationException("Generation change for symbol ${kv.key} should be between 0 and 1")

        //validate positions
        kv.value.possiblePositions.forEach { position ->
            run {
                if (position.x < 0 || position.x > lottery.rows) throw LotteryValidationException("Wrong position ${position} for symbol ${kv.key}")
                if (position.y < 0 || position.y > lottery.columns) throw LotteryValidationException("Wrong position ${position} for symbol ${kv.key}")
            }
        }
    }
}

class LotteryValidationException(message: String): Exception(message)