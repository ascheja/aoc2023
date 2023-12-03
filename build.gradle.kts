import java.nio.file.Files
import kotlin.io.path.isDirectory
import kotlin.io.path.name
import kotlin.streams.asSequence

plugins {
    kotlin("jvm") version "1.9.21"
}

repositories {
    mavenCentral()
}

kotlin {
    compilerOptions {
        freeCompilerArgs.add("-Xcontext-receivers")
    }
}

tasks.register("newDay") {
    val sourceBasePath = projectDir.toPath().resolve("./src/main/kotlin")
    val inputsBasePath = projectDir.toPath().resolve("./inputs")
    val lastDay = Files.list(inputsBasePath).asSequence()
        .filter { it.isDirectory() }
        .mapNotNull { it.name.substringAfter("day", "").ifEmpty { null }?.toIntOrNull() }
        .maxOrNull() ?: 0
    doLast {
        val newDay = lastDay + 1
        Files.writeString(
            sourceBasePath.resolve("Day${newDay}.kt"),
            """
                package org.github.ascheja.aoc2023
                
                fun main() {
                    fun part1(lines: List<String>): Int {
                        TODO()
                    }
                
                    fun part2(lines: List<String>): Int {
                        TODO()
                    }
                
                    println(part1(readInputFile($newDay, "test.txt")))

                    println(part1(readInputFile(${newDay})))
                    
                    //println(part2(readInputFile($newDay, "test.txt")))
                    
                    //println(part2(readInputFile(${newDay})))
                }
            """.trimIndent()
        )
        val dayInputsDir = Files.createDirectories(inputsBasePath.resolve("day${newDay}"))
        Files.createFile(dayInputsDir.resolve("test.txt"))
        Files.createFile(dayInputsDir.resolve("input.txt"))
    }
}
