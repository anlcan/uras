package com.anlcan

/**
 * Created on 12.05.18.
 */
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

    fun myAction(type:ActionType, money:Int=0):Action {
        return Action(this.name, type, money)
    }

    fun action(game: Game, actions:List<Action>): Action {
        return when(game.stage) {
            STAGE.DEAL -> {
                when {
                    this == game.smallBlindPlayer -> Action(this.name, ActionType.CALL, game.bigBlind() - game.smallBlind())
                    this == game.bigBlindPlayer -> Action(this.name, ActionType.CHECK)
                    else -> Action(this.name, ActionType.CALL, game.bigBlind())
                }
            }
            STAGE.FLOP -> myAction(ActionType.CHECK)
            STAGE.RIVER -> myAction(ActionType.CHECK)
            STAGE.TURN -> myAction(ActionType.CHECK)
        }
    }
}
