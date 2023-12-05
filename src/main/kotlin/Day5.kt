@file:Suppress("EmptyRange")

package org.github.ascheja.aoc2023

private data class Almanach(
    val seeds: List<Long>,
    val maps: List<List<MappingRange>>,
) {
    fun map(seed: Long): Long {
        fun List<MappingRange>.convert(source: Long) = firstOrNull { source in it }?.map(source) ?: source
        return maps.fold(seed) { source, map ->
            map.convert(source)
        }
    }
}

private data class MappingRange(val destinationRangeStart: Long, val sourceRangeStart: Long, val rangeLength: Long) {

    private val sourceRange = sourceRangeStart..<sourceRangeStart + rangeLength

    operator fun contains(source: Long): Boolean = source in sourceRange

    fun map(source: Long): Long = (source - sourceRangeStart) + destinationRangeStart

    fun map(sourceRange: LongRange): LongRange = map(sourceRange.first)..map(sourceRange.last)
}

private fun parseAlmanach(lines: List<String>): Almanach {
    var seeds = emptyList<Long>()
    var currentMap: String? = null
    val maps = mutableMapOf<String, List<MappingRange>>()
    for ((index, line) in lines.withIndex()) {
        if ("seeds:" in line) {
            seeds = line.splitAndTrim(" ").drop(1).map { it.toLong() }
        } else if (":" in line) {
            currentMap = line.substringBefore(" map:")
        } else {
            val forMap = currentMap ?: error("Unknown section in line ${index + 1}")
            maps.compute(forMap) { _, existing ->
                val (destinationRangeStart, sourceRangeStart, rangeLength) = line.splitAndTrim(" ").map { it.toLong() }
                (existing ?: emptyList()) + MappingRange(destinationRangeStart, sourceRangeStart, rangeLength)
            }
        }
    }
    fun findMap(name: String) = maps[name]?.sortedBy { it.sourceRangeStart } ?: error("$name not found")
    return Almanach(
        seeds,
        listOf(
            findMap("seed-to-soil"),
            findMap("soil-to-fertilizer"),
            findMap("fertilizer-to-water"),
            findMap("water-to-light"),
            findMap("light-to-temperature"),
            findMap("temperature-to-humidity"),
            findMap("humidity-to-location"),
        ),
    )
}

fun main() {
    fun part1(lines: List<String>): Long {
        val almanach = parseAlmanach(lines)
        return almanach.seeds.minOf { seed ->
            almanach.map(seed)
        }
    }

    fun part2(lines: List<String>): Long {
        val almanach = parseAlmanach(lines)
        val seedRanges = almanach.seeds.chunked(2).map { (seedStart, seedLength) ->
            seedStart..<(seedStart + seedLength)
        }.sortedBy { it.first }
        return almanach.maps.fold(seedRanges) { sources, map ->
            buildList {
                sources.forEach forEachSource@ { source ->
                    var remainingRange = source
                    map.forEach forEachMap@ { mappingRange ->
                        if (remainingRange.first !in mappingRange) {
                            // Not part at all
                            return@forEachMap
                        }

                        if (remainingRange.last in mappingRange) {
                            // remainingRange is a subrange of mappingRange
                            add(mappingRange.map(remainingRange))
                            return@forEachSource
                        }

                        // start is in range, but end isn't -> split
                        val newFirst = mappingRange.sourceRangeStart + mappingRange.rangeLength
                        add(mappingRange.map(remainingRange.first..<newFirst))
                        remainingRange = newFirst..remainingRange.last
                    }
                    if (!remainingRange.isEmpty()) {
                        add(remainingRange)
                    }
                }
            }
        }.minOf { it.first }
    }

    println(part1(readInputFile(5, "test.txt")))

    println(part1(readInputFile(5)))

    println(part2(readInputFile(5, "test.txt")))

    println(part2(readInputFile(5)).let { "$it == 15880236: ${it == 15880236L}" })
}
