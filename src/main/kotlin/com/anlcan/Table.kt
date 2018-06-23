package com.anlcan

import java.util.*

/**
 * Created on 23.06.18.
 */
class Table (val smallBlind:Int=5, val bigBlind:Int=smallBlind*2){
    /**
     * players waiting to go into game on the next round
     */
    val watchers = mutableListOf<Player>()
    /**
     * currently playing players
     */
    val players = mutableListOf<Player>()

    /**
     * players who cannot continue playing (ie run out of money
     */
    val losers = mutableListOf<Player>()

    val games = mutableListOf<Game>()

    lateinit var dealer:Player

    val seat = {
        players.addAll(watchers)
        watchers.clear()
        players.filter{ it.money < bigBlind }.forEach{losers.add(it); players.remove(it)}

        this.dealer = if (this::dealer.isInitialized)
            players.nextPlayer(this.dealer)
        else
            players[Random().nextInt(players.size)]
    }

    fun addPlayer(player:Player) {
        watchers.add(player)
    }

    fun run(){
        seat()
        if (players.size < 0)
            return

        val g = Game(players, dealer, smallBlind, bigBlind)
        val winners = g.run()
        games.add(g)
    }
}


