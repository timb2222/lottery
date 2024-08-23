package com.cyberspeed.lottery.data

data class Lottery(
    val gameArea: GameArea,
    val symbols: Map<Characters, CharConfig>,
    val winCombinations: Map<CombinationType, RewardWrapper>,
)

data class GameArea (
    val columns: Int,
    val rows: Int,
    val possiblePositions: List<PositionDescription>
)

data class PositionDescription (
    val x: Int,
    val y: Int,
    val availableSymbols: List<Characters>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PositionDescription

        if (x != other.x) return false
        if (y != other.y) return false

        return true
    }

    override fun hashCode(): Int {
        var result = x
        result = 31 * result + y
        return result
    }
}

data class CharConfig(
    val generationChance: Float,
)

data class RewardWrapper(
    val reward: Reward
)

data class Reward(
    val action: RewardAction,
    val amount: Float
)

enum class CombinationType {
    SAME_3,
    SAME_4,
    SAME_5,
    SAME_6,
    SAME_7,
    SAME_8,
    SAME_9,
    SAME_HORIZONTAL,
    SAME_VERTICAL,
    SAME_DIAGONAL,
}

enum class Characters {
    A,
    B,
    C,
    D,
    E,
    F,
    X10,
    X5,
    P1000,
    P500,
    NONE,
}

enum class RewardAction {
    MULTIPLY,
    SUM,
}
