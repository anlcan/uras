package com.anlcan

/**
 * Created on 20.03.18.
 */
class Deck {

    val cards = mutableListOf<Card>()

    init {
        for (color in Suit.values()) {
            for (value in Value.values()) {
                cards.add(Card(color, value))
            }
        }

        shuffle()
    }

    fun shuffle() {
        cards.shuffle();
    }

    fun next(): Card {
        val c = cards[0]
        cards.removeAt(0)
        return c
    }

    fun next(size: Int): List<Card> {
        val cards = mutableListOf<Card>()
        for (i in 0..size) {
            cards.add(next())
        }
        return cards
    }

}

class Player(val cards: MutableList<Card> = mutableListOf()) {

}

class Hand(listOfCards: List<Card>) : Comparable<Hand> {
    val cards: List<Card> = listOfCards.sortedByDescending { it.value }
    val rank: Rank = Rank.of(listOfCards)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true

        val otherHand: Hand = other as Hand

        cards.toMutableList().containsAll(otherHand.cards.toMutableList())
        return true
    }

    override fun toString(): String {
        return cards.map { it.toString() }.joinToString(" ")
    }

    override fun compareTo(other: Hand): Int {
        return if (other.rank == rank) {

            for (i in 0..5) {
                if (cards[i].compareTo(other.cards[i]) != 0) {
                    return cards[i].compareTo(other.cards[i])
                }
            }
            return 0
            /*
            when(rank) {
                Rank.PAIR -> {

                }

                Rank.ROYAL_FLUSH -> 0
                Rank.STRAIGHT_FLUSH -> TODO()
                Rank.FOUR_OF_A_KIND -> TODO()
                Rank.FULL_HOUSE -> TODO()
                Rank.FLUSH -> TODO()
                Rank.STRAIGHT -> TODO()
                Rank.THREE_OF_A_KIND -> TODO()
                Rank.TWO_PAIRS -> TODO()
                Rank.HIGH_CARD -> TODO()

            }
            */
        } else {
            rank.compareTo(other.rank)
        }
    }

}


enum class Rank {
    /**
     * If the high cards in two players' hands is the same,
     * the second-highest card becomes decisive.
     * If these cards are also the same, the third-highest card plays and so on. These cards are known as the kicker.
     */
    HIGH_CARD,
    PAIR,
    TWO_PAIRS,
    THREE_OF_A_KIND,
    STRAIGHT,
    FLUSH,
    FULL_HOUSE,
    FOUR_OF_A_KIND,
    STRAIGHT_FLUSH,
    ROYAL_FLUSH;

;


    companion object {
        fun of(cards: List<Card>): Rank {
            val sorted = cards.sortedByDescending { c -> c.value }

            val isOrdered = isOrdered(cards)
            val isSameSuit = isSameSuit(cards)

            return if (isOrdered) {
                if (isSameSuit) {
                    if (sorted[0].value == Value.ACE)
                        ROYAL_FLUSH
                    else
                        STRAIGHT_FLUSH
                } else {
                    STRAIGHT
                }
            } else if (isSameSuit) {
                FLUSH
            } else {

                val grouped = sorted.groupBy { it.value }.values.map { it.size }
                if (grouped.contains(4)) {
                    FOUR_OF_A_KIND
                } else if (grouped.contains(3)) {
                    if (grouped.contains(2))
                        FULL_HOUSE
                    else
                        THREE_OF_A_KIND
                } else if (grouped.contains(2)) {
                    val first = grouped.indexOf(2)
                    val second = grouped.lastIndexOf(2)
                    if (first == second)
                        PAIR
                    else
                        TWO_PAIRS

                } else {
                    HIGH_CARD
                }
            }
        }

        fun isOrdered(cards: List<Card>): Boolean {
            val sorted = cards.sortedByDescending { c -> c.value }
            val diff = sorted.take(4)
                    .map { c1 -> c1.value.ordinal - sorted[4].value.ordinal }

            return listOf(4, 3, 2, 1) == diff
        }

        // sanki groupBy daha iyi ?
        fun isSameSuit(cards: List<Card>): Boolean {
            return cards.take(4).all { c -> cards[4].suit == c.suit }
        }
    }
}

data class Card(val suit: Suit, val value: Value) : Comparable<Card> {
    override fun compareTo(other: Card): Int {
        return value.compareTo(other.value)
    }

    override fun toString(): String {
        return "$value $suit"
    }

}

/**
 * &spades;	&hearts;	&diams;	&clubs;
 * U+2664	U+2661	U+2662	U+2667
 */
enum class Suit(val rep: String) {
    Spades("\u2664"),
    Hearts("\u2661"),
    Diamonds("\u2662"),
    Clubs("\u2667");

    override fun toString(): String {
        return rep;
    }
}

enum class Value(val rep: String) {
    TWO("2"), THREE("3"), FOUR("4"), FIVE("5"), SIX("6"), SEVEN("7"), EIGHT("8"), NINE("9"), TEN("10"), JACK("J"), QUEEN("Q"), KING("K"), ACE("A");

    override fun toString(): String {
        return rep;
    }
}
