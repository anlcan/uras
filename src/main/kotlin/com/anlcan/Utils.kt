package com.anlcan

/**
 * Created on 12.05.18.
 */
fun List<Player>.nextPlayer(it:Player):Player{
    return this[(this.indexOf(it) + 1) % this.size]
}

fun List<Player>.prevPlayer(it:Player): Player {
    return this[(this.indexOf(it) -1 + this.size) % this.size]
}
