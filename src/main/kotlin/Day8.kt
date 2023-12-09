package org.github.ascheja.aoc2023

private enum class Direction(private val value: Char) {
    LEFT('L'),
    RIGHT('R'),
    ;

    companion object {

        private val mapping by lazy {
            Direction.entries.associateBy(Direction::value)
        }

        fun ofValue(char: Char) = mapping[char] ?: error("Unexpected character $char")
    }
}

private data class GeoMap(val directions: List<Direction>, val nodes: Map<String, Pair<String, String>>) :
    Iterable<Direction> {
    override fun iterator(): Iterator<Direction> = iterator {
        while (true) {
            yieldAll(directions)
        }
    }

    fun countFromStartUntilMatcher(start: String, matcher: (String) -> Boolean): Int {
        return asSequence().runningFold(start) { name, direction ->
            when (direction) {
                Direction.LEFT -> nodes[name]!!.first
                Direction.RIGHT -> nodes[name]!!.second
            }
        }.takeWhile(matcher).count()
    }
}

private fun parseMap(lines: List<String>): GeoMap {
    val directions = lines.first().map(Direction::ofValue)
    val nodes = lines.drop(1).map { line ->
        val (name, left, right) = line
            .replace(Regex("[() ]"), "")
            .split('=', ',')
        Pair(name, Pair(left, right))
    }.toMap()
    return GeoMap(directions, nodes)
}

fun main() {

    fun part1(lines: List<String>): Int {
        val map = parseMap(lines)
        return map.countFromStartUntilMatcher("AAA") { it != "ZZZ"}
    }

    fun part2(lines: List<String>): Long {
        val map = parseMap(lines)
        val startNames = map.nodes.keys.filter { it.last() == 'A' }
        val endingPaths = startNames.map { startName ->
            map.countFromStartUntilMatcher(startName) { it.last() != 'Z' }.toLong()
        }
        return endingPaths.reduce(::leastCommonMultiple)
    }

    println(part1(readInputFile(8, "test.txt")).also { require(it == 2) { "$it should be 2" } })

    println(part1(readInputFile(8)))

    part2(readInputFile(8, "test2.txt")).also { println(it); require(it == 6L) { "$it should be 6" } }

    println(part2(readInputFile(8)))
}
