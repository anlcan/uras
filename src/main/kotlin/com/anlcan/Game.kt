package com.anlcan

import java.util.*

/**
 * Created on 31.03.18.
 */
class Game (private val smallBlind:Int=5, private val bigBlind:Int=smallBlind*2){

    private val players = mutableListOf<Player>()
    private val watchers = mutableListOf<Player>()
    private val deck = Deck()
    private val table = mutableListOf<Card>()
    private val pot = mutableListOf<List<Action>>()

    var stage:STAGE = STAGE.DEAL
    private set

    /**
     * initialized at {@link Game::seat} method
     */
    lateinit var dealer: Player
    var smallBlindPlayer: Player? = null
    var bigBlindPlayer: Player? = null

    fun smallBlind(): Int {
        return smallBlind
    }

    fun bigBlind(): Int {
        return bigBlind
    }

    fun addPlayer(player:Player) {
        watchers.add(player)
    }

    fun players():List<Player>{
        return players.toList()
    }

    fun table():List<Card> {
        return table.toList()
    }

    fun actionOrder():List<Player> {
        val index = players.indexOf(nextPlayer(dealer))
        return mutableListOf(
            players.subList(index, players.size),
            players.subList(0, index))
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
        actions()

        val hand = Hand(table)
        println("FLOP: $hand")

        val winners = showdown()
        winners.forEach{p -> println("${p.name} ${p.cards()} -> ${p.bestHand}")}

        return winners
    }

    fun actions() {
        val playersLeft = actionOrder().filter { !it.folded }
        val actions = mutableListOf<Action>()
        for (player in playersLeft){
            val action = player.action(this, actions)
            isCompatible(action)
            actions.add(action)
            println(action)

        }
        pot.add(actions)
    }

    private fun isCompatible(action: Action): Boolean {
        return true
    }

    fun blinds() {
        smallBlindPlayer = prevPlayer(dealer)
        smallBlindPlayer?.blind(smallBlind)

        bigBlindPlayer = prevPlayer(smallBlindPlayer!!)
        bigBlindPlayer?.blind(bigBlind)

        pot.add(listOf(Action(bigBlindPlayer!!.name, ActionType.BLIND, bigBlind),
                        Action(smallBlindPlayer!!.name, ActionType.BLIND, smallBlind)))
    }

    fun potSize():Int {
        return pot.sumBy { it.sumBy {  it.money }}
    }

    fun seat() {
        players.addAll(watchers)
        watchers.clear()
        players.filter{ it.money < bigBlind }.forEach{players.remove(it)}

        this.dealer = if (this::dealer.isInitialized)
            nextPlayer(this.dealer)
        else
            players[Random().nextInt(players.size)]
    }

     fun nextPlayer(it:Player): Player {
        return players[(players.indexOf(it) + 1) % players.size]
    }

     fun prevPlayer(it:Player): Player {
        return players[(players.indexOf(it) -1 + players.size) % players.size]
    }

    private fun showdown(): List<Player> {
        val groupBy = players
                .filter { !it.folded }
                .groupBy({ allHands(it.cards(), table).sortedDescending()[0] }, { it })
        return groupBy
                .mapValues { it.value[0] }
                .map { (k,v)-> v.bestHand = k;v }

    }


    fun turn() {
        assert(table.size ==4)
        deck.next() //burn
        table.add(deck.next()) // turn
        stage = STAGE.TURN
    }

    fun river() {
        assert(table.size ==3)
        deck.next() //burn
        table.add(deck.next()) // river
        stage = STAGE.RIVER
    }

    fun flop() {
        assert(table.size ==0)
        deck.next() //burn
        table.addAll(deck.next(3))
        stage = STAGE.FLOP
    }

    fun deal(){
        for (i in 1..2) {
            for (p: Player in actionOrder()) {
                p.add(deck.next())
            }
        }
    }


    fun reset(){
        deck.shuffle()
        table.clear()
        pot.clear()
        for(player in players){
            player.clear()
        }
    }


}
data class Action(val playerId:String, val type:ActionType, val money:Int=0)
enum class STAGE {
    DEAL, FLOP, RIVER, TURN
}
enum class ActionType {
    BLIND, FOLD, CHECK, RAISE, CALL

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

