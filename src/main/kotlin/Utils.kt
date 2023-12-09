package org.github.ascheja.aoc2023

import java.io.File

fun readInputFile(day: Int, filename: String = "input.txt"): List<String> {
    return File("./inputs/day${day}/${filename}").readLines().filter(String::isNotBlank)
}

fun String.splitAndTrim(delimiter: String): List<String> {
    return split(delimiter).map(String::trim).filter(String::isNotBlank)
}

fun leastCommonMultiple(a: Long, b: Long): Long {
    return a * b / greatestCommonDivisor(a, b)
}

tailrec fun greatestCommonDivisor(a: Long, b: Long): Long {
    return if (b == 0L) a else greatestCommonDivisor(b, a % b)
}
