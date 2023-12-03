package org.github.ascheja.aoc2023

private data class Grid2D(private val data: List<String>): Iterable<Grid2D.Point2D> {
    data class Point2D(val char: Char, val x: Int, val y: Int) {
        fun asCoordinate2D() = Coordinate2D(x, y)
    }
    data class Coordinate2D(val x: Int, val y: Int) {
        operator fun plus(other: Coordinate2D): Coordinate2D {
            return Coordinate2D(x + other.x, y + other.y)
        }
    }

    operator fun get(coordinate2D: Coordinate2D): Point2D {
        return Point2D(data[coordinate2D.y][coordinate2D.x], coordinate2D.x, coordinate2D.y)
    }

    fun Coordinate2D.findAdjacent(range: Int = 1): List<Coordinate2D> {
        return ((-range)..range).flatMap { y ->
            ((-range)..range).map { x ->
                Coordinate2D(x, y)
            }
        }.filterNot { it.x == 0 && it.y == 0 }.map { offset -> this + offset }.filter { contains(it) }
    }

    override operator fun iterator(): Iterator<Point2D> = iterator {
        data.forEachIndexed { y, line ->
            line.forEachIndexed { x, char ->
                yield(Point2D(char, x, y))
            }
        }
    }

    operator fun contains(coordinate2D: Coordinate2D): Boolean {
        return coordinate2D.y in data.indices && coordinate2D.x in data[coordinate2D.y].indices
    }
}

private fun Grid2D.findNumbers(): Sequence<List<Grid2D.Point2D>> = sequence {
    val iter = iterator()
    while (iter.hasNext()) {
        var currentY: Int? = null
        val nextNumber = iter.asSequence()
            .dropWhile { !it.char.isDigit() }
            .takeWhile { it.char.isDigit() && (currentY == null || currentY == it.y) }
            .onEach { currentY = it.y }
            .toList()
        if (nextNumber.isEmpty()) {
            continue
        }
        yield(nextNumber)
    }
}

context(Grid2D)
private fun Grid2D.Point2D.isAdjacentToAny(check: (Grid2D.Point2D) -> Boolean): Boolean {
    val pointCoordinates = asCoordinate2D()
    return pointCoordinates.findAdjacent().any { check(get(it)) }
}

private fun List<Grid2D.Point2D>.toNumber(): Int {
    return String(map { it.char }.toCharArray()).toInt()
}

private fun Grid2D.findPartNumbers(): Sequence<List<Grid2D.Point2D>> {
    return findNumbers().filter { number ->
        number.any { point ->
            point.isAdjacentToAny { point -> !point.char.isDigit() && point.char != '.' }
        }
    }
}

private fun Grid2D.findGearRatios(): Sequence<Int> {
    val partNumbers = findPartNumbers()
        .map { Pair(it.map { it.asCoordinate2D() }, it.toNumber()) }
    return asSequence().filter { it.char == '*' }.map { gearSymbol ->
        val adjacentCoordinates = gearSymbol.asCoordinate2D().findAdjacent()
        partNumbers.filter { it.first.any { it in adjacentCoordinates } }.map { it.second }.toList()
    }.filter { it.size == 2 }.map { it[0] * it[1] }
}

fun main() {
    fun part1(lines: List<String>): Int {
        return Grid2D(lines).findPartNumbers().map { it.toNumber() }.sum()
    }

    fun part2(lines: List<String>): Int {
        return Grid2D(lines).findGearRatios().sum()
    }

    println(part1(readInputFile(3, "test.txt")) == 4361)

    println(part1(readInputFile(3)))

    println(part2(readInputFile(3, "test.txt")) == 467835)

    println(part2(readInputFile(3)))
}
