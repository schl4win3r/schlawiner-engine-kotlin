# Puzzle and Dice Game

Schlawiner is a game of dice where you must reach numbers between 1 and 100 using the basic arithmetics +, −, ×, and ÷ in any order. The game is played with three dice. Each dice number can be multiplied by 10 or 100 and used precisely once.

| Number | Dice Number                                                                                                                                                                                                                                                                          | Possible Solution | Difference |
|--------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|-------------------|------------|
| 53     | <img src="https://schl4win3r.github.io/4.svg" width="24" height="24" alt="4" title="4"/>&nbsp;<img src="https://schl4win3r.github.io/6.svg" width="24" height="24" alt="6" title="6"/>&nbsp;<img src="https://schl4win3r.github.io/1.svg" width="24" height="24" alt="1" title="1"/> | 4 + 60 − 10       | 1          |
| 40     | <img src="https://schl4win3r.github.io/2.svg" width="24" height="24" alt="2" title="2"/>&nbsp;<img src="https://schl4win3r.github.io/2.svg" width="24" height="24" alt="2" title="2"/>&nbsp;<img src="https://schl4win3r.github.io/1.svg" width="24" height="24" alt="1" title="1"/> | 2 × (20 + 1)      | 2          |
| 22     | <img src="https://schl4win3r.github.io/3.svg" width="24" height="24" alt="3" title="3"/>&nbsp;<img src="https://schl4win3r.github.io/2.svg" width="24" height="24" alt="2" title="2"/>&nbsp;<img src="https://schl4win3r.github.io/1.svg" width="24" height="24" alt="1" title="1"/> | 30 + 2 − 10       | 0          |
| 96     | <img src="https://schl4win3r.github.io/5.svg" width="24" height="24" alt="5" title="5"/>&nbsp;<img src="https://schl4win3r.github.io/1.svg" width="24" height="24" alt="1" title="1"/>&nbsp;<img src="https://schl4win3r.github.io/5.svg" width="24" height="24" alt="5" title="5"/> | (500 − 10) ÷ 5    | 2          |
| 42     | <img src="https://schl4win3r.github.io/6.svg" width="24" height="24" alt="6" title="6"/>&nbsp;<img src="https://schl4win3r.github.io/6.svg" width="24" height="24" alt="6" title="6"/>&nbsp;<img src="https://schl4win3r.github.io/6.svg" width="24" height="24" alt="6" title="6"/> | 6 × 6 + 6         | 0          |

The differences between the numbers and the calculated results are summed up. The player with the most minor difference wins.

Have fun!

# Play

If you want to play Schlawiner in the terminal, use the provided [KScript](https://github.com/kscripting/kscript) script (make sure you've executed `./gradlew publishToMavenLocal`, before running it).

```shell
kscript play.kts
```
