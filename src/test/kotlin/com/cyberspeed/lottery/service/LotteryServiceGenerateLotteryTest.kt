package com.cyberspeed.lottery.service
import com.cyberspeed.lottery.data.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import java.util.*

class LotteryServiceGenerateLotteryTest {

    private lateinit var lotteryService: LotteryService
    private lateinit var mockedLotteryService: LotteryService
    private lateinit var mockRandomGenerator: Random
    

    @BeforeEach
    fun setUp() {
        mockRandomGenerator = mock(Random::class.java)
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
        
        mockedLotteryService = LotteryService(lottery, mockRandomGenerator)
    }

    @Test
    fun `test generateLottery generates correct size`() {
        val generatedField = lotteryService.generateLottery()

        // Check that the field has the correct number of rows and columns
        assertEquals(3, generatedField.size) // Assuming 3 rows
        generatedField.forEach { row ->
            assertEquals(3, row.size) // Assuming 3 columns
        }
    }

    @Test
    fun `test generateLottery fills all positions`() {
        val generatedField = lotteryService.generateLottery()

        // Check that all positions are filled with valid symbols
        generatedField.forEach { row ->
            row.forEach { cell ->
                assertTrue(cell != Characters.NONE)
            }
        }
    }

    @Test
    fun `test generateLottery generates only allowed symbols`() {
        val generatedField = lotteryService.generateLottery()

        // Assuming allowed symbols are A, B, C, D, E, F, X10, X5, P1000, P500
        val allowedSymbols = setOf(
            Characters.A, Characters.B, Characters.C, Characters.D,
            Characters.E, Characters.F, Characters.X10, Characters.X5,
            Characters.P1000, Characters.P500
        )

        // Check that each cell contains a symbol from the allowed set
        generatedField.forEach { row ->
            row.forEach { cell ->
                assertTrue(allowedSymbols.contains(cell))
            }
        }
    }

    @Test
    fun `test generateLottery generates unique fields`() {
        val generatedFields = mutableSetOf<List<List<Characters>>>()

        // Generate lottery multiple times and check for uniqueness
        repeat(100) {
            val generatedField = lotteryService.generateLottery()
            assertTrue(generatedFields.add(generatedField))
        }
    }

    @Test
    fun `test generateLottery never generates NONE`() {
        val generatedField = lotteryService.generateLottery()

        // Check that NONE is never generated
        generatedField.forEach { row ->
            row.forEach { cell ->
                assertTrue(cell != Characters.NONE)
            }
        }
    }

    @Test
    fun `test generateLottery respects generation chances`() {
        // Set up the mock behavior for random number generation
        `when`(mockRandomGenerator.nextInt(anyInt()))
            .thenReturn(0) 
            .thenReturn(1) 
            .thenReturn(0) 
            .thenReturn(2) 
            .thenReturn(1) 
            .thenReturn(1) 
            .thenReturn(1) 
            .thenReturn(1) 
            .thenReturn(1) 

        `when`(mockRandomGenerator.nextFloat()).thenReturn(1f) 

        val generatedField = mockedLotteryService.generateLottery()

        assertEquals(Characters.A, generatedField[0][0])
        assertEquals(Characters.B, generatedField[0][1])
        assertEquals(Characters.A, generatedField[0][2])
        assertEquals(Characters.C, generatedField[1][0])
        assertEquals(Characters.B, generatedField[1][1])
        assertEquals(Characters.B, generatedField[1][2])
        assertEquals(Characters.B, generatedField[2][0])
        assertEquals(Characters.B, generatedField[2][1])
        assertEquals(Characters.B, generatedField[2][2])

        // Verify that the mock was called the expected number of times
        verify(mockRandomGenerator, times(9)).nextInt(anyInt())
        verify(mockRandomGenerator, times(9)).nextFloat()
    }
}
