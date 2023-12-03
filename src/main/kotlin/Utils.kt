package org.github.ascheja.aoc2023

import java.io.File

fun readInputFile(day: Int, filename: String = "input.txt"): List<String> {
    return File("./inputs/day${day}/${filename}").readLines().filter(String::isNotBlank)
}

fun String.splitAndTrim(delimiter: String): List<String> {
    return split(delimiter).map(String::trim).filter(String::isNotBlank)
}
