package com.anlcan

import java.io.File

/**
 * Created on 20.03.18.
 */
fun main(args: Array<String>) {

    val players= listOf(Player("maverick"), Player("goose"))
    val game = Game()
    for(p in players)
        game.addPlayer(p)

    game.run()

    File("game.html").printWriter().use { out ->
       out.println(toHtml(game.table(), players))
    }
}



