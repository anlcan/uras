package com.anlcan

import java.io.File

/**
 * Created on 20.03.18.
 */
fun main(args: Array<String>) {

    val players= listOf(Player("maverick", money = 40), Player("goose", money = 50))
    val table = Table()
    for(p in players)
        table.addPlayer(p)

    table.run()

    File("game.html").printWriter().use { out ->
       out.println(toHtml(table.games.last().table(), players))
    }
}
