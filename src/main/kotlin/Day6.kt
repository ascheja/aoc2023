package org.github.ascheja.aoc2023

private data class Race(val time: Long, val recordDistance: Long)

private data class PossibleRace(val speed: Long, val distance: Long)

private fun parseRaces(lines: List<String>): List<Race> {
    fun parseLine(line: String): List<Long> {
        return line.replace(Regex("\\s+"), " ")
            .substringAfter(":")
            .splitAndTrim(" ")
            .map { it.toLong() }
    }
    val times = parseLine(lines[0])
    val distances = parseLine(lines[1])
    return times.zip(distances).map { Race(it.first, it.second) }
}

private fun Race.winningRaces(): Sequence<PossibleRace> {
    return (0..time).asSequence().map { holdTime ->
        val distance = holdTime * (time - holdTime)
        PossibleRace(holdTime, distance)
    }.filter {
        it.distance > recordDistance
    }
}

fun main() {
    fun part1(lines: List<String>): Int {
        val races = parseRaces(lines)
        return races.map { it.winningRaces().count() }.reduce { a, b -> a * b }
    }

    fun part2(lines: List<String>): Int {
        val race = parseRaces(lines).let { races -> Race(races.joinToString("") { it.time.toString() }.toLong(), races.joinToString("") { it.recordDistance.toString() }.toLong()) }
        return race.winningRaces().count()
    }

    println(part1(readInputFile(6, "test.txt")))

    println(part1(readInputFile(6)))
    
    println(part2(readInputFile(6, "test.txt")))
    
    println(part2(readInputFile(6)))
}
