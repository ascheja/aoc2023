package org.github.ascheja.aoc2023

private val mapping = mapOf(
    "one" to "1",
    "two" to "2",
    "three" to "3",
    "four" to "4",
    "five" to "5",
    "six" to "6",
    "seven" to "7",
    "eight" to "8",
    "nine" to "9",
)

private val candidates = mapping.keys + mapping.values + "0"


fun main() {
    fun part1(lines: List<String>): Int {
        return lines.sumOf { line ->
            val numbers = line.filter { it.isDigit() }.map { it.toString().toInt() }
            numbers.first() * 10 + numbers.last()
        }
    }

    fun part2(lines: List<String>): Int {
        return lines.sumOf { line ->
            val numbers = (0..line.length).mapNotNull { index ->
                candidates.firstOrNull { candidate -> line.startsWith(candidate, startIndex = index) }
            }.map {
                (mapping[it] ?: it).toInt()
            }.toList()
            numbers.first() * 10 + numbers.last()
        }
    }

    println(part1(readInputFile(1, "test.txt")))
    println(part1(readInputFile(1)))

    println(part2(readInputFile(1, "test.txt")))
    println(part2(readInputFile(1)))
}
