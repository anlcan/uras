package com.anlcan

import java.util.*

/**
 * Created on 12.05.18.
 */


class Player(val name: String, var money: Int = 0, private val strategy: PokerStrategy = Checker()) :
    PokerStrategy by strategy {

    private val cards: MutableList<Card> = mutableListOf()
    var folded: Boolean = false; private set
    var bestHand: Hand? = null

    fun cards(): List<Card> {
        return cards.toList()
    }

    fun add(card: Card) {
        assert(cards.size < 2)
        cards.add(card)
    }

    fun clear() {
        cards.clear()
        folded = false
    }

    fun blind(blind: Int): Int {
        money -= blind
        return money
    }

    fun buildAction(type: ActionType, money: Int = 0): Action {
        return Action(this.name, type, money)
    }

    fun action(game: Game, actions: List<Action>): Action {
        val action = strategy.action(game, this, actions)
        if (action.type == ActionType.FOLD)
            folded = true

        return action
    }

}
