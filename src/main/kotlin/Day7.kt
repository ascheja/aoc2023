package org.github.ascheja.aoc2023

import kotlin.math.pow

private enum class CardKind(val id: Char) {
    A('A'),
    K('K'),
    Q('Q'),
    J('J') {
        override val partTwoOrder: Int get() = _2.ordinal + 1
    },
    T('T'),
    _9('9'),
    _8('8'),
    _7('7'),
    _6('6'),
    _5('5'),
    _4('4'),
    _3('3'),
    _2('2'),
    ;

    companion object {
        private val mapping by lazy {
            entries.associateBy(CardKind::id)
        }

        fun fromId(id: Char): CardKind = mapping[id] ?: error("Unknown id $id")
    }

    open val partTwoOrder = ordinal

    override fun toString(): String {
        return id.toString()
    }
}

private enum class HandType {
    FIVE_OF_A_KIND,
    FOUR_OF_A_KIND,
    FULL_HOUSE,
    THREE_OF_A_KIND,
    TWO_PAIR,
    ONE_PAIR,
    HIGH_CARD,
}

private data class Hand(val cards: List<CardKind>, val bid: Int) {

    val cardCounts = cards.groupingBy { it }.eachCount()
}

private fun parseHands(lines: List<String>): List<Hand> {
    return lines.map { line ->
        val (cardsString, bidString) = line.split(" ")
        Hand(cardsString.map(CardKind.Companion::fromId), bidString.toInt())
    }
}

private data class RatedHand(val hand: Hand, val handType: HandType)

private val part1Comparator = compareBy<Hand> { simpleMatcher(it.cardCounts) }
    .thenBy { it.cards[0] }
    .thenBy { it.cards[1] }
    .thenBy { it.cards[2] }
    .thenBy { it.cards[3] }
    .thenBy { it.cards[4] }

private val part2Comparator = compareBy<RatedHand> { it.handType }
    .thenBy { it.hand.cards[0].partTwoOrder }
    .thenBy { it.hand.cards[1].partTwoOrder }
    .thenBy { it.hand.cards[2].partTwoOrder }
    .thenBy { it.hand.cards[3].partTwoOrder }
    .thenBy { it.hand.cards[4].partTwoOrder }

private val simpleMatcher: (Map<CardKind, Int>) -> HandType = {
    when {
        it.size == 1 -> HandType.FIVE_OF_A_KIND
        it.any { (_, count) -> count == 4 } -> HandType.FOUR_OF_A_KIND
        it.all { (_, count) -> count == 2 || count == 3 } -> HandType.FULL_HOUSE
        it.any { (_, count) -> count == 3 } -> HandType.THREE_OF_A_KIND
        it.filter { (_, count) -> count == 2 }.size == 2 -> HandType.TWO_PAIR
        it.any { (_, count) -> count == 2 } -> HandType.ONE_PAIR
        else -> HandType.HIGH_CARD
    }
}

private val cardKindsWithoutJoker = CardKind.entries.filter { it != CardKind.J }

private val CardKind.isJoker get() = this == CardKind.J

private fun Hand.bestVersion(): RatedHand {
    val numberOfJokers = cards.count { it.isJoker }
    if (numberOfJokers == 0) return RatedHand(this, simpleMatcher(cardCounts))
    return sequence<RatedHand> {
        val possibleVersions = (CardKind.entries.size - 1).toDouble().pow(numberOfJokers).toInt()
        for (i in 0 until possibleVersions) {
            val newCards = cards.map { card ->
                if (card.isJoker) {
                    cardKindsWithoutJoker[i % cardKindsWithoutJoker.size]
                } else {
                    card
                }
            }
            yield(RatedHand(Hand(cards, bid), simpleMatcher(newCards.groupingBy { it }.eachCount())))
        }
    }.minBy { it.handType }
}

fun main() {
    fun part1(lines: List<String>): Int {
        return parseHands(lines).sortedWith(part1Comparator.reversed()).withIndex().sumOf { (index, hand) ->
            val rank = index + 1
            rank * hand.bid
        }
    }

    fun part2(lines: List<String>): Int {
        return parseHands(lines).map(Hand::bestVersion)
            .sortedWith(part2Comparator.reversed())
            .onEach { println("${it.hand.cards.joinToString("")}: ${it.handType}") }
            .withIndex()
            .sumOf { (index, hand) ->
                val rank = index + 1
                rank * hand.hand.bid
            }
    }

    println(part1(readInputFile(7, "test.txt")))

    println(part1(readInputFile(7)))
    
    println(part2(readInputFile(7, "test.txt")))

    println(part2(readInputFile(7)).also { require(it == 250665248) { "Result has be exactly 250665248" } })
}
