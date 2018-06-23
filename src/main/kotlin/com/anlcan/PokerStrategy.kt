package com.anlcan

import java.util.*

/**
 * Created on 23.06.18.
 */
interface PokerStrategy {
    fun action(game: Game, player: Player, actions: List<Action>): Action
//    {
//        return when (game.stage) {
//            STAGE.DEAL -> dealAction(game, player, actions)
//            STAGE.FLOP -> flopAction(game, player, actions)
//            STAGE.RIVER -> riverAction(game, player, actions)
//            STAGE.TURN -> turnAction(game, player, actions)
//        }
//    }

    fun isCheckEnough(actions: List<Action>, player: Player): Boolean {
        return raiseMoney(actions, player)?.equals(0) ?: false
    }

    fun raiseMoney(actions: List<Action>, player: Player): Int? {
        return actions
            .find { it.type == ActionType.RAISE && it.playerId != player.name }?.money
    }

//    fun dealAction(game: Game, player: Player, actions: List<Action>): Action
//    fun flopAction(game: Game, player: Player, actions: List<Action>): Action
//    fun riverAction(game: Game, player: Player, actions: List<Action>): Action
//    fun turnAction(game: Game, player: Player, actions: List<Action>): Action

    fun check(game: Game, player: Player): Action {
        return when (player) {
            game.smallBlindPlayer -> Action(player.name, ActionType.CALL, game.bigBlind - game.smallBlind)
            game.bigBlindPlayer -> Action(player.name, ActionType.CHECK)
            //Table will make sure we are only in the game if we can pay at least the bigBlind
            else -> Action(player.name, ActionType.CALL, game.bigBlind)
        }
    }
}

class Checker : PokerStrategy {
    override fun action(game: Game, player: Player, actions: List<Action>): Action {
        val type = if (isCheckEnough(actions, player))
                ActionType.CHECK
            else
                ActionType.FOLD

        return when (game.stage) {
            STAGE.DEAL -> check(game,player)
            STAGE.FLOP, STAGE.RIVER, STAGE.TURN -> player.buildAction(type)
        }
    }
}

class RandomStrategy(private val seed: Random = Random()) : PokerStrategy {
    override fun action(game: Game, player: Player, actions: List<Action>): Action {

        return if (game.stage == STAGE.DEAL) {
            Checker().check(game, player)
        }

        else if (isCheckEnough(actions, player)) {
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


