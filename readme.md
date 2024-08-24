# Lottery Generation Service

How to run
1) build  
```
./gradlew clean build
```
2) Run  
```
java -jar build/libs/Game3x3-1.0-SNAPSHOT.jar --config=config.json --betting-amount=100
```


## Overview

The Lottery Generation Service is a core component responsible for generating and validating lottery fields. This service ensures that the generated lottery fields adhere to specified configurations and that the chances of generating certain symbols are respected.

## Table of Contents

- [Overview](#overview)
- [Key Features](#key-features)
- [Components](#components)
- [Configuration](#configuration)
- [Validation](#validation)
- [Winning Calculation](#winning-calculation)
- [Testing](#testing)
- [Error Handling](#error-handling)

## Key Features

- **Dynamic Lottery Field Generation:** Generates a lottery field based on predefined rows, columns, and symbol generation chances.
- **Symbol Probability Control:** Symbols are generated with respect to their configured probability.
- **Validation:** Ensures that the generated field meets all the configuration requirements.
- **Customizable:** Easily configurable symbols, chances, and game area dimensions.

## Components

### 1. **LotteryService**
- **generateLottery():** This method generates a 2D list (grid) of symbols according to the predefined game area and symbol probabilities.
- **play():** This method simulates the gameplay by checking for winning combinations and applying bonuses.

### 2. **Validator**
- **validate(lottery: Lottery):** This function checks the validity of the lottery configuration. It ensures that:
    - The game area has a valid number of rows and columns.
    - Symbol generation chances are within the valid range (0 to 1).
    - All positions in the game area are properly configured.
    - Only configured symbols are used in the game area.

### 3. **Data Models**
- **Lottery:** Main configuration class containing the game area, symbols, and winning combinations.
- **GameArea:** Defines the rows, columns, and possible positions within the lottery grid.
- **PositionDescription:** Represents the position and the available symbols at that position.
- **Characters:** Enum defining the available symbols (A, B, C, etc., along with bonus symbols like X10, P500).
- **CharConfig:** Configuration for each symbol, including its generation chance.
- **CombinationType:** Enum representing different winning combinations (e.g., SAME_3, SAME_HORIZONTAL).
- **Reward:** Defines the reward type and amount for each combination.

## Configuration

The service relies on the `Lottery` configuration, which includes:

- **Game Area Dimensions:** The number of rows and columns.
- **Symbols and Generation Chances:** Each symbol is assigned a probability for generation.
- **Winning Combinations:** Configurable combinations of symbols that define a win (e.g., SAME_3, SAME_HORIZONTAL).

Example configuration:

```json
{
  "gameArea": {
    "columns": 3,
    "rows": 3,
    "possiblePositions": [
      {
        "x": 0,
        "y": 0,
        "availableSymbols": [
          "A",
          "B",
          "C",
          "D",
          "E",
          "F",
          "X10",
          "X5",
          "P1000",
          "P500"
        ]
      },
      {
        "x": 0,
        "y": 1,
        "availableSymbols": [
          "A",
          "B",
          "C",
          "D",
          "E",
          "F",
          "X10",
          "X5",
          "P1000",
          "P500"
        ]
      },
      {
        "x": 0,
        "y": 2,
        "availableSymbols": [
          "A",
          "B",
          "C",
          "D",
          "E",
          "F",
          "X10",
          "X5",
          "P1000",
          "P500"
        ]
      },
      {
        "x": 1,
        "y": 0,
        "availableSymbols": [
          "A",
          "B",
          "C",
          "D",
          "E",
          "F",
          "X10",
          "X5",
          "P1000",
          "P500"
        ]
      },
      {
        "x": 1,
        "y": 1,
        "availableSymbols": [
          "A",
          "B",
          "C",
          "D",
          "E",
          "F",
          "X10",
          "X5",
          "P1000",
          "P500"
        ]
      },
      {
        "x": 1,
        "y": 2,
        "availableSymbols": [
          "A",
          "B",
          "C",
          "D",
          "E",
          "F",
          "X10",
          "X5",
          "P1000",
          "P500"
        ]
      },
      {
        "x": 2,
        "y": 0,
        "availableSymbols": [
          "A",
          "B",
          "C",
          "D",
          "E",
          "F",
          "X10",
          "X5",
          "P1000",
          "P500"
        ]
      },
      {
        "x": 2,
        "y": 1,
        "availableSymbols": [
          "A",
          "B",
          "C",
          "D",
          "E",
          "F",
          "X10",
          "X5",
          "P1000",
          "P500"
        ]
      },
      {
        "x": 2,
        "y": 2,
        "availableSymbols": [
          "A",
          "B",
          "C",
          "D",
          "E",
          "F",
          "X10",
          "X5",
          "P1000",
          "P500"
        ]
      }
    ]
  },
  "symbols": {
    "A": {
      "generationChance": 0.5
    },
    "B": {
      "generationChance": 0.4
    },
    "C": {
      "generationChance": 0.3
    },
    "D": {
      "generationChance": 0.2
    },
    "E": {
      "generationChance": 0.15
    },
    "F": {
      "generationChance": 0.1
    },
    "X10": {
      "generationChance": 0.1
    },
    "X5": {
      "generationChance": 0.2
    },
    "P1000": {
      "generationChance": 0.2
    },
    "P500": {
      "generationChance": 0.3
    }
  },
  "winCombinations": {
    "SAME_3": {
      "reward": {
        "action": "MULTIPLY",
        "amount": 1
      }
    },
    "SAME_4": {
      "reward": {
        "action": "MULTIPLY",
        "amount": 2
      }
    },
    "SAME_5": {
      "reward": {
        "action": "MULTIPLY",
        "amount": 3
      }
    },
    "SAME_6": {
      "reward": {
        "action": "MULTIPLY",
        "amount": 5
      }
    },
    "SAME_7": {
      "reward": {
        "action": "MULTIPLY",
        "amount": 7
      }
    },
    "SAME_8": {
      "reward": {
        "action": "MULTIPLY",
        "amount": 10
      }
    },
    "SAME_9": {
      "reward": {
        "action": "MULTIPLY",
        "amount": 50
      }
    },
    "SAME_HORIZONTAL": {
      "reward": {
        "action": "MULTIPLY",
        "amount": 2
      }
    },
    "SAME_VERTICAL": {
      "reward": {
        "action": "MULTIPLY",
        "amount": 2
      }
    },
    "SAME_DIAGONAL": {
      "reward": {
        "action": "MULTIPLY",
        "amount": 5
      }
    }
  }
}
```

## Validation
The validate function ensures that the lottery configuration is valid:

Rows and Columns: Must be greater than 0.
Generation Chances: Must be between 0 and 1.
Positions: All positions in the game area must be configured, and each configured symbol must be defined in the symbols map.
If any validation fails, a LotteryValidationException is thrown with a descriptive error message.

## Winning Calculation
The play() method is responsible for calculating the winnings based on the generated lottery field. Here's how it works:

Initial Setup:

The player's bet is passed as an argument to the play() method.
The initial totalWin is set to the value of the bet.
Winning Combinations:

The method checks for all possible winning combinations (e.g., SAME_3, SAME_HORIZONTAL).
SAME_X Combinations: Only the highest applicable SAME_X combination is applied (e.g., if both SAME_6 and SAME_7 are found, only the reward for SAME_7 is applied).
Horizontal, Vertical, and Diagonal Wins: If these combinations are found, their rewards are added to totalWin.
### Bonus Application:

If any winning combination is detected, the method applies any relevant bonuses (e.g., X10, X5, P1000, P500).
Bonuses are applied in two categories:
Multipliers (e.g., X10, X5): The highest multiplier is applied to the totalWin.
Additions (e.g., P1000, P500): All additions are summed and added to the totalWin.
### Return Value:

The method returns the final totalWin value, which represents the player's winnings after all combinations and bonuses are applied.

## Testing
Unit tests cover the core functionalities:

generateLottery() Method: Tested for correct size, symbol generation, respect for probabilities, and uniqueness.
play() Method: Tested for correct application of winning combinations and bonuses.
validate() Method: Tested for correct validation of rows, columns, generation chances, and position configurations.

## Error Handling
The service throws LotteryValidationException for validation errors. Common scenarios include:

Invalid row or column count.
Invalid generation chance (must be between 0 and 1).
Mismatch between configured positions and game area dimensions.
Use of unconfigured symbols in the game area.
These exceptions provide detailed messages to help diagnose configuration issues quickly.

## Conclusion
The Lottery Generation Service is a flexible and robust tool for generating, validating, and simulating lottery fields. With customizable configurations, thorough validation, and precise winning calculations, it ensures a reliable and fair gameplay experience.