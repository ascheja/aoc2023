package org.github.ascheja.aoc2023

private data class Card(val id: Int, val winningNumbers: List<Int>, val numbersOnCard: List<Int>) {
    val matchingNumbersCount = winningNumbers.intersect(numbersOnCard.toSet()).size
}

private fun parseCards(lines: List<String>): Sequence<Card> = sequence {
    for (line in lines) {
        val (cardHeader, cardBody) = line.splitAndTrim(":")
        val id = cardHeader.substringAfterLast(" ").toInt()
        val (leftNumbers, rightNumbers) = cardBody.splitAndTrim("|").map { it.splitAndTrim(" ").map { it.toInt() } }
        yield(Card(id, leftNumbers, rightNumbers))
    }
}

fun main() {
    fun part1(lines: List<String>): Int {
        return parseCards(lines).sumOf { card -> card.matchingNumbersCount.takeIf { it > 0 }?.let { 1 shl (it - 1) } ?: 0 }
    }

    fun part2(lines: List<String>): Int {
        val countPerCard = parseCards(lines).fold(mapOf<Int, Int>()) { acc, card ->
            val cardCount = (acc[card.id] ?: 0) + 1
            acc + sequence {
                yield(Pair(card.id, cardCount))
                for (scratchCardId in card.id + 1..card.id + (card.matchingNumbersCount)) {
                    yield(Pair(scratchCardId, (acc[scratchCardId] ?: 0) + cardCount))
                }
            }
        }
        return countPerCard.values.sum()
    }

    println(part1(readInputFile(4, "test.txt")))

    println(part1(readInputFile(4)))

    println(part2(readInputFile(4, "test.txt")))
    
    println(part2(readInputFile(4)))
}
