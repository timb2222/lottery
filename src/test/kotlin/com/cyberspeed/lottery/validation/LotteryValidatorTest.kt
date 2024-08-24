package com.cyberspeed.lottery.validation

import com.cyberspeed.lottery.data.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class LotteryValidationTest {

    @Test
    fun `test valid lottery configuration`() {
        val lottery = Lottery(
            gameArea = GameArea(
                rows = 3,
                columns = 3,
                possiblePositions = listOf(
                    PositionDescription(0, 0, listOf(Characters.A)),
                    PositionDescription(0, 1, listOf(Characters.B)),
                    PositionDescription(0, 2, listOf(Characters.C)),
                    PositionDescription(1, 0, listOf(Characters.D)),
                    PositionDescription(1, 1, listOf(Characters.E)),
                    PositionDescription(1, 2, listOf(Characters.F)),
                    PositionDescription(2, 0, listOf(Characters.A)),
                    PositionDescription(2, 1, listOf(Characters.B)),
                    PositionDescription(2, 2, listOf(Characters.C))
                )
            ),
            symbols = mapOf(
                Characters.A to CharConfig(generationChance = 0.5f),
                Characters.B to CharConfig(generationChance = 0.3f),
                Characters.C to CharConfig(generationChance = 0.2f),
                Characters.D to CharConfig(generationChance = 0.4f),
                Characters.E to CharConfig(generationChance = 0.6f),
                Characters.F to CharConfig(generationChance = 0.1f)
            ),
            winCombinations = emptyMap()
        )

        assertDoesNotThrow { validate(lottery) }
    }

    @Test
    fun `test invalid rows and columns`() {
        val lottery = Lottery(
            gameArea = GameArea(
                rows = 0, // Invalid value
                columns = 3,
                possiblePositions = emptyList()
            ),
            symbols = emptyMap(),
            winCombinations = emptyMap()
        )

        val exception = assertThrows(LotteryValidationException::class.java) {
            validate(lottery)
        }
        assertEquals("Numbers of rows should not be less than 1", exception.message)
    }

    @Test
    fun `test invalid generation chance`() {
        val lottery = Lottery(
            gameArea = GameArea(
                rows = 3,
                columns = 3,
                possiblePositions = listOf(
                    PositionDescription(0, 0, listOf(Characters.A)),
                    PositionDescription(0, 1, listOf(Characters.B)),
                    PositionDescription(0, 2, listOf(Characters.C)),
                    PositionDescription(1, 0, listOf(Characters.D)),
                    PositionDescription(1, 1, listOf(Characters.E)),
                    PositionDescription(1, 2, listOf(Characters.F)),
                    PositionDescription(2, 0, listOf(Characters.A)),
                    PositionDescription(2, 1, listOf(Characters.B)),
                    PositionDescription(2, 2, listOf(Characters.C))
                )
            ),
            symbols = mapOf(
                Characters.A to CharConfig(generationChance = 1.5f), // Invalid value
                Characters.B to CharConfig(generationChance = 0.3f),
                Characters.C to CharConfig(generationChance = 0.2f),
                Characters.D to CharConfig(generationChance = 0.4f),
                Characters.E to CharConfig(generationChance = 0.6f),
                Characters.F to CharConfig(generationChance = 0.1f)
            ),
            winCombinations = emptyMap()
        )

        val exception = assertThrows(LotteryValidationException::class.java) {
            validate(lottery)
        }
        assertEquals("Generation change for symbol A should be between 0 and 1", exception.message)
    }

    @Test
    fun `test invalid positions size`() {
        val lottery = Lottery(
            gameArea = GameArea(
                rows = 3,
                columns = 3,
                possiblePositions = listOf(
                    PositionDescription(0, 0, listOf(Characters.A)),
                    PositionDescription(0, 1, listOf(Characters.B))
                    // Missing positions
                )
            ),
            symbols = mapOf(
                Characters.A to CharConfig(generationChance = 0.5f),
                Characters.B to CharConfig(generationChance = 0.3f)
            ),
            winCombinations = emptyMap()
        )

        val exception = assertThrows(LotteryValidationException::class.java) {
            validate(lottery)
        }
        assertEquals(
            "Configured positions size not equals gaming field. rows 3, columns 3, possiblePositions 2",
            exception.message
        )
    }

    @Test
    fun `test position not configured`() {
        val lottery = Lottery(
            gameArea = GameArea(
                rows = 3,
                columns = 3,
                possiblePositions = listOf(
                    PositionDescription(0, 0, listOf(Characters.A)),
                    PositionDescription(0, 1, listOf(Characters.B)),
                    PositionDescription(0, 2, listOf(Characters.C)),
                    PositionDescription(1, 0, listOf(Characters.D)),
                    PositionDescription(1, 1, listOf(Characters.E)),
                    PositionDescription(1, 2, listOf(Characters.F)),
                    PositionDescription(2, 0, listOf(Characters.A)),
                    PositionDescription(2, 1, listOf(Characters.B))
                    // Missing (2, 2) position
                )
            ),
            symbols = mapOf(
                Characters.A to CharConfig(generationChance = 0.5f),
                Characters.B to CharConfig(generationChance = 0.3f),
                Characters.C to CharConfig(generationChance = 0.2f),
                Characters.D to CharConfig(generationChance = 0.4f),
                Characters.E to CharConfig(generationChance = 0.6f),
                Characters.F to CharConfig(generationChance = 0.1f)
            ),
            winCombinations = emptyMap()
        )

        val exception = assertThrows(LotteryValidationException::class.java) {
            validate(lottery)
        }
        assertEquals("Configured positions size not equals gaming field. rows 3, columns 3, possiblePositions 8", exception.message)
    }

    @Test
    fun `test symbol not configured`() {
        val lottery = Lottery(
            gameArea = GameArea(
                rows = 3,
                columns = 3,
                possiblePositions = listOf(
                    PositionDescription(0, 0, listOf(Characters.A)),
                    PositionDescription(0, 1, listOf(Characters.B)),
                    PositionDescription(0, 2, listOf(Characters.C)),
                    PositionDescription(1, 0, listOf(Characters.D)),
                    PositionDescription(1, 1, listOf(Characters.E)),
                    PositionDescription(1, 2, listOf(Characters.F)),
                    PositionDescription(2, 0, listOf(Characters.A)),
                    PositionDescription(2, 1, listOf(Characters.B)),
                    PositionDescription(2, 2, listOf(Characters.X10)) // Invalid symbol not in configuration
                )
            ),
            symbols = mapOf(
                Characters.A to CharConfig(generationChance = 0.5f),
                Characters.B to CharConfig(generationChance = 0.3f),
                Characters.C to CharConfig(generationChance = 0.2f),
                Characters.D to CharConfig(generationChance = 0.4f),
                Characters.E to CharConfig(generationChance = 0.6f),
                Characters.F to CharConfig(generationChance = 0.1f)
            ),
            winCombinations = emptyMap()
        )

        val exception = assertThrows(LotteryValidationException::class.java) {
            validate(lottery)
        }
        assertEquals("Symbol X10 not configured", exception.message)
    }
}
