package com.anlcan

import javafx.stage.Stage

/**
 * Created on 31.03.18.
 */
data class GameState(var state:Stage) {
    //TODO(anlcan) don't pass the game but the state
}

class Game (val players:List<Player>, val dealer:Player=players[0], val smallBlind:Int=5, val bigBlind:Int=smallBlind*2){

    private val deck = Deck()
    private val table = mutableListOf<Card>()
    private val pot = mutableListOf<List<Action>>()
    private val winners:MutableList<Player> = mutableListOf()

    val smallBlindPlayer = players.prevPlayer(dealer)
    val bigBlindPlayer = players.prevPlayer(smallBlindPlayer)


    /* GAME STEPS */
    private fun round(count:Int, newStage:STAGE, burn:Boolean=true) :Boolean {
        if (burn)
            deck.next() //burn
        table.addAll(deck.next(count))
        stage = newStage
        println("----")
        println("${this.stage}: ${this.table()}")
        return actions()

    }

    val blinds:()->Boolean  = {
        smallBlindPlayer.blind(smallBlind)
        bigBlindPlayer.blind(bigBlind)

        pot.add(listOf(Action(bigBlindPlayer.name, ActionType.BLIND, bigBlind),
            Action(smallBlindPlayer.name, ActionType.BLIND, smallBlind)))
    }

    fun actions():Boolean  {
        val playersLeft = actionOrder().filter { !it.folded }
        val actions = mutableListOf<Action>()
        for (player in playersLeft){
            val action = player.nextAction(this, actions)
            isCompatible(action)
            actions.add(action)
            println(action)
            // did everybody folded except the next guy?
            if (playersLeft.count { !it.folded } == 1){
                println("everybody folded!")
                winners.add(playersLeft.first { !it.folded })
                return false
            }
        }

        return pot.add(actions)
    }

    fun run():List<Player>{

        run steps@{
            listOf<() -> Boolean>(
                {
                    blinds()
                },
                {
                    listOf(1, 2).forEach { actionOrder().forEach { it.add(deck.next()) } }
                    round(0, STAGE.DEAL, burn = false)
                },
                {
                    assert(table.size == 0)
                    round(3, STAGE.FLOP, burn = false)
                },
                {
                    assert(table.size == 3)
                    round(1, STAGE.RIVER)
                },
                {
                    assert(table.size == 4)
                    round(1, STAGE.TURN)
                },
                {
                    setWinners()
                }
            ).forEach { if (!it()) return@steps }
        }
        return winners()
    }

    var stage:STAGE = STAGE.DEAL
    private set

    fun players():List<Player>{
        return players.toList()
    }

    fun winners():List<Player>{
        return winners.toList()
    }
    fun table():List<Card> {
        return table.toList()
    }

    fun actionOrder():List<Player> {
        val index = players.indexOf(players.nextPlayer(dealer))
        return mutableListOf(
            players.subList(index, players.size),
            players.subList(0, index))
                .flatten()

    }

    fun setWinners():Boolean {
        val bestHandForPlayers = showdown()
        val result = mutableListOf(bestHandForPlayers[0])

        for(p in bestHandForPlayers.takeLast(bestHandForPlayers.size-1)){
            when (p.bestHand!!.compareTo(result[0].bestHand!!)) {
                1 -> assert(true, {"broken sort"})
                0 -> result.add(p)
            //-1 -> return result.map { it.player }
            }
        }

        winners.addAll(result.map{it})
        return true
    }

    private fun isCompatible(action: Action): Boolean {
        return true
    }

    fun potSize():Int {
        return pot.sumBy { it.sumBy {  it.money }}
    }



//    private fun showdown(listOfPlayers:List<Player>, tableCards:List<Card>): List<PlayerGameEnd> {
//        return  listOfPlayers
//            .map { PlayerGameEnd(it,  allHands(it.cards(), tableCards).sortedDescending()[0]) }
//            .sortedByDescending(PlayerGameEnd::hand)
//
//    }
    private fun showdown(): List<Player> {
        val groupBy = players
                .filter { !it.folded }
                .groupBy({ allHands(it.cards(), table).sortedDescending()[0] }, { it })
        return groupBy
                .mapValues { it.value[0] }
                .map { (k,v)-> v.bestHand = k;v }
            .sortedByDescending { it.bestHand }
    }
}

data class Action(val playerId:String, val type:ActionType, val money:Int=0){
    init {
        assert(check(), {"$type should have money value > 0: $money"})
    }

    private fun check(): Boolean {
        return when(type){
            ActionType.CALL,ActionType.RAISE, ActionType.ALL_IN, ActionType.BLIND -> money > 0
            else-> money == 0
        }
    }
}

enum class STAGE {
    DEAL, FLOP, RIVER, TURN
}
enum class ActionType {
    BLIND, FOLD, CHECK, RAISE, CALL, ALL_IN

}


fun allHands(hand: List<Card>, table: List<Card>): List<Hand> {
    //table has 5 cards
    assert(table.size == 5)
    assert(hand.size == 2)

    //OMAHA
    //val perms = permutation(table, 3)
    //perms.forEach { l -> l.addAll(hand) }

    //TEXAS
    val all = table.toMutableList()
    all.addAll(hand)
    return permutation(all, 5).map { l -> Hand(l) }.distinct()
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

