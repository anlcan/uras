package com.anlcan

import java.util.*

/**
 * Created on 31.03.18.
 */
class Game {

    private val players = mutableListOf<Player>()
    private val watchers = mutableListOf<Player>()
    private val deck = Deck()
    val table = mutableListOf<Card>()

    var buttonPlayer: Player? = null

    fun addPlayer(player:Player) {
        watchers.add(player)
    }

    fun players():List<Player>{
        return players.toList()
    }

    fun gameOrder():List<Player> {
        val index = players.indexOf(buttonPlayer)
       return mutableListOf(players.subList(index, players.size), players.subList(0, index))
                .flatten()

    }

    fun run():List<Player>{

        reset()
        seat()
        blinds()
        deal()
        actions()
        flop()
        actions()
        river()
        actions()
        turn()
        val hand = Hand(table)
        println("FLOP: $hand")

        val winners = showdown()
        winners.forEach{p -> println("${p.name} ${p.cards()} -> ${p.bestHand}")}

        return winners
    }

    private fun actions() {
        for (player in players.filter { !it.folded }){
            val action = player.action()

        }

    }

    private fun blinds() {

    }

    fun seat() {
        players.addAll(watchers)
        watchers.clear()
        this.buttonPlayer = this.buttonPlayer?.let {
            players[(players.indexOf(it) + 1) % players.size]
        } ?: let{
            players[Random().nextInt(players.size)]
        }
    }

    private fun showdown(): List<Player> {
        val groupBy = players
                .filter { !it.folded }
                .groupBy({ allHands(it.cards(), table).sortedDescending()[0] }, { it })
        return groupBy
                .mapValues { it.value[0] }
                .map { (k,v)-> v.bestHand = k;v }

    }


    private fun turn() {
        assert(table.size ==4)
        deck.next() //burn
        table.add(deck.next()) // turn
    }

    private fun river() {
        assert(table.size ==3)
        deck.next() //burn
        table.add(deck.next()) // river
    }

    private fun flop() {
        assert(table.size ==0)
        deck.next() //burn
        table.addAll(deck.next(3))
    }

    private fun deal(){
        for (i in 1..2) {
            for (p: Player in players) {
                p.add(deck.next())
            }
        }
    }


    fun reset(){
        deck.shuffle()
        table.clear()
        for(player in players){
            player.clear()
        }
    }


}

enum class ACTION {
    FOLD, CHECK, RAISE
}

class Player(val name:String, var money:Int =0) {

    private val _cards:MutableList<Card> = mutableListOf()
    var folded:Boolean = false; private set
    var bestHand:Hand? = null

    fun cards():List<Card>{
        return _cards.toList()
    }

    fun add(card:Card) {
        assert(_cards.size<2)
        _cards.add(card)
    }

    fun clear() {
        _cards.clear()
        folded = false
    }


    fun blind(blind:Int):Int {
        money -= blind
        return money
    }

    fun action(): ACTION {
        return ACTION.FOLD
    }


}

fun allHands(hand: List<Card>, table: List<Card>): List<Hand> {
    //table has 5 cards
    assert(table.size == 5)
    assert(hand.size == 2)

    val perms = permutation(table, 3)
    perms.forEach{ l -> l.addAll(hand) }
    return perms.map { l -> Hand(l) }.distinct()
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
//                for (mutableList in subResult) {
//                    println("adding $card to $mutableList")
//                }
                subResult.forEach({ it.add(card) })
                //subResult.distinct()
                collector.addAll(subResult)
            }
            collector

        }
    }


}

