package com.anlcan

/**
 * Created on 20.03.18.
 */
class Deck{

    val cards = mutableListOf<Card>()
    init {
        for (color in Color.values()){
            for (value in Value.values()){
                cards.add(Card(color, value))
            }
        }

        shuffle()
    }

    fun shuffle(){
        cards.shuffle();
    }

    fun next():Card {
        val c =  cards[0]
        cards.removeAt(0)
        return c
    }
}

class Player(val cards: MutableList<Card> = mutableListOf()) {

}

class Hand(val cards: Array<Card>) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true

        val otherHand:Hand = other as Hand

        cards.toMutableList().containsAll(otherHand.cards.toMutableList())
        return true
    }

    override fun toString(): String {
        return cards.map { it.toString() }.joinToString(" ")
    }
}
data class Card(val color: Color, val value: Value){
    override fun toString(): String {
        return "$value$color"
    }

}

/**
 * &spades;	&hearts;	&diams;	&clubs;
 * U+2664	U+2661	U+2662	U+2667
 */
enum class Color(val rep:String) {
    Spades("\u2664"),
    Hearts("\u2661"),
    Diamonds("\u2662"),
    Clubs("\u2667");

    override fun toString(): String {
        return rep;
    }
}

enum class Value (val rep:String){
    TWO("2"), THREE("3"), FOUR("4"), FIVE("5"), SIX("6"), SEVEN("7"), EIGHT("8"), NINE("9"), TEN("10"), JACK("J"), QUEEN("Q"), KING("K"), ACE("A");
    override fun toString(): String {
        return rep;
    }
}
