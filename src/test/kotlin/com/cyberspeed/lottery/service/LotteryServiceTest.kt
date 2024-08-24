package com.cyberspeed.lottery.service
import com.cyberspeed.lottery.data.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class LotteryServiceTest {

    private lateinit var lotteryService: LotteryService

    @BeforeEach
    fun setUp() {
        // Инициализация символов с их шансами генерации
        val symbols = mapOf(
            Characters.A to CharConfig(generationChance = 0.5f),
            Characters.B to CharConfig(generationChance = 0.4f),
            Characters.C to CharConfig(generationChance = 0.3f),
            Characters.D to CharConfig(generationChance = 0.2f),
            Characters.E to CharConfig(generationChance = 0.15f),
            Characters.F to CharConfig(generationChance = 0.1f),
            Characters.X10 to CharConfig(generationChance = 0.1f),
            Characters.X5 to CharConfig(generationChance = 0.2f),
            Characters.P1000 to CharConfig(generationChance = 0.2f),
            Characters.P500 to CharConfig(generationChance = 0.3f)
        )

        // Инициализация выигрышных комбинаций с их наградами
        val winCombinations = mapOf(
            CombinationType.SAME_3 to RewardWrapper(Reward(RewardAction.MULTIPLY, 1f)),
            CombinationType.SAME_4 to RewardWrapper(Reward(RewardAction.MULTIPLY, 2f)),
            CombinationType.SAME_5 to RewardWrapper(Reward(RewardAction.MULTIPLY, 3f)),
            CombinationType.SAME_6 to RewardWrapper(Reward(RewardAction.MULTIPLY, 5f)),
            CombinationType.SAME_7 to RewardWrapper(Reward(RewardAction.MULTIPLY, 7f)),
            CombinationType.SAME_8 to RewardWrapper(Reward(RewardAction.MULTIPLY, 10f)),
            CombinationType.SAME_9 to RewardWrapper(Reward(RewardAction.MULTIPLY, 50f)),
            CombinationType.SAME_HORIZONTAL to RewardWrapper(Reward(RewardAction.MULTIPLY, 2f)),
            CombinationType.SAME_VERTICAL to RewardWrapper(Reward(RewardAction.MULTIPLY, 2f)),
            CombinationType.SAME_DIAGONAL to RewardWrapper(Reward(RewardAction.MULTIPLY, 5f))
        )

        // Инициализация игровой области с возможными позициями символов
        val possiblePositions = listOf(
            PositionDescription(0, 0, listOf(Characters.A, Characters.B, Characters.C, Characters.D, Characters.E, Characters.F, Characters.X10, Characters.X5, Characters.P1000, Characters.P500)),
            PositionDescription(0, 1, listOf(Characters.A, Characters.B, Characters.C, Characters.D, Characters.E, Characters.F, Characters.X10, Characters.X5, Characters.P1000, Characters.P500)),
            PositionDescription(0, 2, listOf(Characters.A, Characters.B, Characters.C, Characters.D, Characters.E, Characters.F, Characters.X10, Characters.X5, Characters.P1000, Characters.P500)),
            PositionDescription(1, 0, listOf(Characters.A, Characters.B, Characters.C, Characters.D, Characters.E, Characters.F, Characters.X10, Characters.X5, Characters.P1000, Characters.P500)),
            PositionDescription(1, 1, listOf(Characters.A, Characters.B, Characters.C, Characters.D, Characters.E, Characters.F, Characters.X10, Characters.X5, Characters.P1000, Characters.P500)),
            PositionDescription(1, 2, listOf(Characters.A, Characters.B, Characters.C, Characters.D, Characters.E, Characters.F, Characters.X10, Characters.X5, Characters.P1000, Characters.P500)),
            PositionDescription(2, 0, listOf(Characters.A, Characters.B, Characters.C, Characters.D, Characters.E, Characters.F, Characters.X10, Characters.X5, Characters.P1000, Characters.P500)),
            PositionDescription(2, 1, listOf(Characters.A, Characters.B, Characters.C, Characters.D, Characters.E, Characters.F, Characters.X10, Characters.X5, Characters.P1000, Characters.P500)),
            PositionDescription(2, 2, listOf(Characters.A, Characters.B, Characters.C, Characters.D, Characters.E, Characters.F, Characters.X10, Characters.X5, Characters.P1000, Characters.P500))
        )

        val gameArea = GameArea(
            columns = 3,
            rows = 3,
            possiblePositions = possiblePositions
        )

        // Инициализация лотереи
        val lottery = Lottery(
            gameArea = gameArea,
            symbols = symbols,
            winCombinations = winCombinations
        )

        // Инициализация сервиса лотереи
        lotteryService = LotteryService(lottery)
    }
    @Test
    fun `test play with no winning combinations and no bonuses`() {
        val lotteryField = listOf(
            listOf(Characters.A, Characters.B, Characters.C),
            listOf(Characters.D, Characters.E, Characters.F),
            listOf(Characters.A, Characters.B, Characters.C)
        )

        val bet = 100
        // No win expected, result should be equal to bet
        val expectedWin = 0

        val actualWin = lotteryService.play(lotteryField, bet)

        assertEquals(expectedWin, actualWin)
    }

    @Test
    fun `test play with SAME_3 combination only `() {
        val lotteryField = listOf(
            listOf(Characters.A, Characters.A, Characters.C), // SAME_3
            listOf(Characters.B, Characters.A, Characters.D),
            listOf(Characters.E, Characters.F, Characters.NONE)
        )

        val bet = 100
        val same3Win = bet * 1 // MULTIPLY for SAME_3 = 1

        val expectedWin = same3Win

        val actualWin = lotteryService.play(lotteryField, bet)

        assertEquals(expectedWin, actualWin)
    }

    @Test
    fun `test play with SAME_6 combination and no bonuses`() {
        val lotteryField = listOf(
            listOf(Characters.A, Characters.A, Characters.A), //HORIZONTAL
            listOf(Characters.A, Characters.A, Characters.A), // SAME_6
            listOf(Characters.B, Characters.C, Characters.D)
        )

        val bet = 100
        val same6Win = bet * 5 // MULTIPLY for SAME_6 = 5
        val horizontalWin = same6Win * 2 

        val expectedWin = horizontalWin

        val actualWin = lotteryService.play(lotteryField, bet)

        assertEquals(expectedWin, actualWin)
    }

    @Test
    fun `test play with horizontal combination only`() {
        val lotteryField = listOf(
            listOf(Characters.A, Characters.A, Characters.A), // Horizontal win
            listOf(Characters.B, Characters.C, Characters.D),
            listOf(Characters.E, Characters.F, Characters.NONE)
        )

        val bet = 100
        val horizontalWin = bet * 2 // MULTIPLY for SAME_HORIZONTAL = 2

        val expectedWin = horizontalWin

        val actualWin = lotteryService.play(lotteryField, bet)

        assertEquals(expectedWin, actualWin)
    }

    @Test
    fun `test play with vertical combination only`() {
        val lotteryField = listOf(
            listOf(Characters.A, Characters.B, Characters.C),
            listOf(Characters.A, Characters.E, Characters.F),
            listOf(Characters.A, Characters.E, Characters.F) // Vertical win
        )

        val bet = 100
        val verticalWin = bet * 2 // MULTIPLY for SAME_VERTICAL = 2

        val expectedWin = verticalWin

        val actualWin = lotteryService.play(lotteryField, bet)

        assertEquals(expectedWin, actualWin)
    }

    @Test
    fun `test play with diagonal combination only`() {
        val lotteryField = listOf(
            listOf(Characters.A, Characters.B, Characters.C),
            listOf(Characters.D, Characters.A, Characters.F),
            listOf(Characters.B, Characters.C, Characters.A) // Diagonal win
        )

        val bet = 100
        val diagonalWin = bet * 5 // MULTIPLY for SAME_DIAGONAL = 5 (new bonus)

        val expectedWin = diagonalWin

        val actualWin = lotteryService.play(lotteryField, bet)

        assertEquals(expectedWin, actualWin)
    }

    @Test
    fun `test play with multiple SAME_9 combinations and no bonuses`() {
        val lotteryField = listOf(
            listOf(Characters.A, Characters.A, Characters.A), // DIAGONAL
            listOf(Characters.A, Characters.A, Characters.A), // VERTICAL
            listOf(Characters.A, Characters.A, Characters.A)  // HORIZONTAL AND SAME_9
        )

        val bet = 100
        val same9Win = bet * 50 // MULTIPLY for SAME_9 = 50
        val horizontalWin = same9Win * 2 // MULTIPLY for SAME_HORIZONTAL = 2
        val verticalWin = horizontalWin * 2 // MULTIPLY for SAME_HORIZONTAL = 2
        val diagonalWin = verticalWin * 5 // MULTIPLY for SAME_DIAGONAL = 5

        val expectedWin = diagonalWin

        val actualWin = lotteryService.play(lotteryField, bet)

        assertEquals(expectedWin, actualWin)
    }

    @Test
    fun `test play with multiple SAME_X combinations and bonuses`() {
        val lotteryField = listOf(
            listOf(Characters.A, Characters.A, Characters.A), // SAME_6
            listOf(Characters.X10, Characters.X5, Characters.P1000),
            listOf(Characters.A, Characters.A, Characters.A) // SAME_HORIZONTAL
        )

        val bet = 100
        val same6Win = bet * 5 // MULTIPLY for SAME_6 = 5
        val horizontalWin = same6Win * 2 // MULTIPLY for SAME_HORIZONTAL = 2

        var expectedWin = horizontalWin

        // Applying bonuses
        val multiplierBonus = 10 // X10 bonus
        val sumBonus = 1000 // P1000 bonus
        expectedWin = expectedWin * multiplierBonus + sumBonus

        val actualWin = lotteryService.play(lotteryField, bet)

        assertEquals(expectedWin, actualWin)
    }

    @Test
    fun `test play with multiple bonuses but no winning combinations`() {
        val lotteryField = listOf(
            listOf(Characters.X10, Characters.X5, Characters.P500),
            listOf(Characters.P1000, Characters.B, Characters.C),
            listOf(Characters.D, Characters.E, Characters.F)
        )

        val bet = 100
        // No winning combination, so the win should be equal to the bet, and bonuses do not apply
        val expectedWin = 0

        val actualWin = lotteryService.play(lotteryField, bet)

        assertEquals(expectedWin, actualWin)
    }

    @Test
    fun `test play with a draw scenario`() {
        val lotteryField = listOf(
            listOf(Characters.A, Characters.B, Characters.C),
            listOf(Characters.D, Characters.E, Characters.F),
            listOf(Characters.A, Characters.B, Characters.NONE)
        )

        val bet = 100
        // No winning combination or bonuses, result should be equal to bet
        val expectedWin = 0

        val actualWin = lotteryService.play(lotteryField, bet)

        assertEquals(expectedWin, actualWin)
    }

    @Test
    fun `test play with SAME_7 and all possible bonuses`() {
        val lotteryField = listOf(
            listOf(Characters.A, Characters.A, Characters.A), // SAME_7 AND HORIZONTAL
            listOf(Characters.A, Characters.X10, Characters.A), // VERTICAL
            listOf(Characters.A, Characters.P1000, Characters.A) // All bonuses
        )

        val bet = 100
        val same7Win = bet * 7 // MULTIPLY for SAME_9 = 50
        val horizontalWin = same7Win * 2 // MULTIPLY for SAME_9 = 50
        val verticalWin = horizontalWin * 2 // MULTIPLY for SAME_9 = 50

        var expectedWin = verticalWin

        // Applying all possible bonuses
        val multiplierBonus = 10 // X10 bonus (highest)
        val sumBonus = 1000 // P1000 bonus
        expectedWin = expectedWin * multiplierBonus + sumBonus

        val actualWin = lotteryService.play(lotteryField, bet)

        assertEquals(expectedWin, actualWin)
    }


}
