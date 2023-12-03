package org.github.ascheja.aoc2023

private enum class Color {
    RED,
    GREEN,
    BLUE,
    ;
}

private data class Game(val id: Int, val rounds: List<Map<Color, Int>>) {
    val red = rounds.maxOf { it[Color.RED] ?: 0 }
    val green = rounds.maxOf { it[Color.GREEN] ?: 0 }
    val blue = rounds.maxOf { it[Color.BLUE] ?: 0 }
}

private fun List<String>.parseGames(): List<Game> {
    return filter(String::isNotBlank).map { line ->
        val (gameName, roundsString) = line.splitAndTrim(":")
        Game(
            gameName.removePrefix("Game ").toInt(),
            roundsString.splitAndTrim(";").map { round ->
                round.splitAndTrim(",").associate { pairString ->
                    val (countString, colorString) = pairString.splitAndTrim(" ")
                    Pair(Color.valueOf(colorString.uppercase()), countString.toInt())
                }
            }
        )
    }
}

private fun List<Game>.filterPossible(): List<Game> {
    return filter { game ->
        game.red <= 12 && game.green <= 13 && game.blue <= 14
    }
}

private val Game.power get() = red * green * blue

fun main() {
    fun part1(lines: List<String>): Int {
        return lines.parseGames().filterPossible().sumOf { it.id }
    }

    fun part2(lines: List<String>): Int {
        return lines.parseGames().sumOf(Game::power)
    }

    println(part1(readInputFile(2, "test.txt")))

    println(part1(readInputFile(2)))

    println(part2(readInputFile(2, "test.txt")))

    println(part2(readInputFile(2)))
}
