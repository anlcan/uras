package com.anlcan

/**
 * Created on 12.05.18.
 */
interface PokerStrategy {
    fun action(game: Game, player:Player, actions:List<Action>): Action
}

class Checker :PokerStrategy {
    override fun action(game: Game, player: Player, actions:List<Action>): Action {
        return when(game.stage) {
            STAGE.DEAL -> {
                when (player) {
                    game.smallBlindPlayer -> Action(player.name, ActionType.CALL, game.bigBlind - game.smallBlind)
                    game.bigBlindPlayer -> Action(player.name, ActionType.CHECK)
                    else -> Action(player.name, ActionType.CALL, game.bigBlind)
                }
            }
            STAGE.FLOP -> player.buildAction(ActionType.CHECK)
            STAGE.RIVER -> player.buildAction(ActionType.CHECK)
            STAGE.TURN -> player.buildAction(ActionType.CHECK)
        }
    }
}

class Player(val name:String, var money:Int =0, private val strategy: PokerStrategy = Checker()) : PokerStrategy by strategy {

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

    fun buildAction(type:ActionType, money:Int=0):Action {
        return Action(this.name, type, money)
    }

    fun action(game:Game, actions: List<Action>): Action {
        return strategy.action(game, this, actions)
    }

}
