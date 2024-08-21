package com.cyberspeed.lottery.data

data class Lottery(
    val columns: Int,
    val rows: Int,
    val symbols: Map<Characters, CharConfig>,
    val winCombinations: Set<CombinationType>,
)

data class CharConfig(
    val reward: Reward,
    val generationChance: Float,
    val possiblePositions: List<CellPosition>,
)

data class Reward(
    val type: RewardType,
    val action: RewardAction,
    val amount: Float
)

data class CellPosition(
    val x: Int,
    val y: Int,
)

enum class CombinationType {
    SAME_4,
    SAME_5,
    SAME_6,
    SAME_7,
    SAME_8,
    SAME_9,
    SAME_HORIZONTAL,
    SAME_VERTICAL,
    SAME_DIAGONAL_LR,
    SAME_DIAGONAL_RL,
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
    SUBTRACT,
    DIVIDE,
    LOG,
    POW,
}

enum class RewardType {
    STANDARD,
    BONUS
}
