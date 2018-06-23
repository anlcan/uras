package com.anlcan

import java.util.*

/**
 * Created on 23.06.18.
 */
interface PokerStrategy {
    fun action(game: Game, player: Player, actions: List<Action>): Action

    fun isCheckEnough(actions: List<Action>, player: Player): Boolean {
        return raiseMoney(actions, player)?.equals(0)?:false
    }

    fun raiseMoney(actions: List<Action>, player: Player): Int? {
        return actions
            .find { it.type == ActionType.RAISE && it.playerId != player.name }?.money
    }
}

class Checker : PokerStrategy {
    override fun action(game: Game, player: Player, actions: List<Action>): Action {
        return when (game.stage) {
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

class RandomStrategy(private val seed: Random = Random()) : PokerStrategy {
    override fun action(game: Game, player: Player, actions: List<Action>): Action {
        return if (isCheckEnough(actions, player)) {
            if (seed.nextBoolean()) {
                player.buildAction(ActionType.CHECK)
            } else {
                player.buildAction(ActionType.RAISE, 1)
            }
        } else {
            if (seed.nextBoolean()) {
                player.buildAction(ActionType.FOLD)
            } else {
                player.buildAction(ActionType.CALL, 1)
            }
        }

    }
}
