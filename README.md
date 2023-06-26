# Schlawiner Engine / Kotlin

Schlawiner is a game of dice where you have to reach numbers between 1 and 100 using the basic arithmetics +, -, * and / in any order. The game is played with three dice. Each dice number can be multiplied with 10 or 100 and must be used exactly once.

| Number | Dice Number | Possible Solution | Difference |
|--------|-------------|-------------------|------------|
| 53     | 4 6 1       | 4 + 60 - 10       | 1          |
| 40     | 1 2 2       | 2 * (20 + 1)      | 2          |
| 22     | 2 1 3       | 30 + 2 - 10       | 0          |
| 96     | 5 1 5       | (500 - 10) / 5    | 2          |
| 42     | 6 6 6       | 6 * 6 + 6         | 0          |

The differences between the numbers and the calculated results are summed up. The player with the smallest difference wins.

Have fun!
