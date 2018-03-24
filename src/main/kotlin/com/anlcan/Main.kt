package com.anlcan

/**
 * Created on 20.03.18.
 */

fun main(args: Array<String>) {

    val deck = Deck()
    assert(deck.cards.size == 52)

    val players = mutableListOf(Player(), Player())

    for (i in 1..2) {
        for (p: Player in players) {
            p.cards.add(deck.next())
        }
    }

    val table = mutableListOf(deck.next(), deck.next(), deck.next()) // flop
    table.add(deck.next()) // river
    table.add(deck.next()) // turn

    // showdown
    for (player in players) {
        val possibleHands = allHands(player.cards, table)
        for (possibleHand in possibleHands) {
            println(possibleHand)
        }
    }
}



fun allHands(hand: List<Card>, table: List<Card>): List<Hand> {
    //table has 5 cards
    assert(table.size == 5)
    assert(hand.size == 2)

    val perms = permutation(table, 3)
    perms.forEach{ l -> l.addAll(hand) }
    return perms.map { l -> Hand(l.toTypedArray()) }.distinct()
}

fun permutation(list: List<Card>, size: Int): List<MutableList<Card>> {

    assert(list.size >= size)
    return when (size) {
        0 -> mutableListOf(list.toMutableList())
        1 -> list.chunked(1).map { it.toMutableList() }
        else -> {
            val collector = mutableListOf<MutableList<Card>>()
            list.forEach { card ->
                val mutable = list.toMutableList()
                mutable.remove(card)
                val subResult =  permutation(mutable, size-1)
                for (mutableList in subResult) {
                    println("adding $card to $mutableList")
                }
                subResult.forEach({ it.add(card) })
                //subResult.distinct()
                collector.addAll(subResult)
            }
            collector

        }
    }


}
