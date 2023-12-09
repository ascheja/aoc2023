package org.github.ascheja.aoc2023

import kotlin.test.assertEquals

private fun parseOASISData(lines: List<String>): List<List<Long>> {
    return lines.map { it.splitAndTrim(" ").map(String::toLong) }
}

private fun List<Long>.calculatePyramid(): List<List<Long>> {
    return sequence<List<Long>> {
        var current = this@calculatePyramid
        do {
            yield(current)
            current = current.zipWithNext().map { (left, right) -> right - left }
        } while (!current.all { it == 0L })
    }.toList().asReversed()
}

fun main() {
    fun part1(lines: List<String>): Long {
        return parseOASISData(lines).sumOf { sequence ->
            sequence.calculatePyramid().fold(0L) { previous, row ->
                row.last() + previous
            }
        }
    }

    fun part2(lines: List<String>): Long {
        return parseOASISData(lines).sumOf { sequence ->
            sequence.calculatePyramid().fold(0L) { previous, row ->
                row.first() - previous
            }
        }
    }

    println(part1(readInputFile(9, "test.txt")).also { print("Should be 114: ") })

    println(part1(readInputFile(9)).also { print("Should be 1702218515: ") })
    
    println(part2(readInputFile(9, "test.txt")).also { print("Should be 2: ") })
    
    println(part2(readInputFile(9)).also { print("Should be 925: ") })
}
