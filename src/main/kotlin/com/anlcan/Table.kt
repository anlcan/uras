package com.anlcan

import java.util.*

/**
 * Created on 23.06.18.
 */
class Table (private val watchers: MutableList<Player> = mutableListOf(), val smallBlind:Int=5, val bigBlind:Int=smallBlind*2){
    /**
     * players waiting to go into game on the next round
     */

    /**
     * currently playing players
     */
    private val players= mutableListOf<Player>()

    fun players():List<Player>{
        return players.toList()
    }

    /**
     * players who cannot continue playing (ie run out of money
     */
    val losers = mutableListOf<Player>()

    val games = mutableListOf<Game>()

    lateinit var dealer:Player

    fun seat() {
        players.addAll(watchers)
        watchers.clear()
        players.filter{ it.money < bigBlind }.forEach{losers.add(it); players.remove(it)}
        players.forEach{it.clear()}

        this.dealer = if (this::dealer.isInitialized)
            players.nextPlayer(this.dealer)
        else
            players[Random().nextInt(players.size)]
    }

    fun addPlayer(player:Player) {
        watchers.add(player)
    }

    fun run(): List<Player> {
        assert(this::dealer.isInitialized)
        assert(this.players.size > 1)
        seat()

        val g = Game(players, dealer, smallBlind, bigBlind)
        games.add(g)
        return g.run()

    }
}


