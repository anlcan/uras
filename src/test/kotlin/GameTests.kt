
import com.anlcan.*
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/**
 * Created on 31.03.18.
 */
private fun randomPlayers(size: Int =3):MutableList<Player> {
    return (1..size).map { Player("Player $it", 20, strategy = RandomStrategy()) }.toMutableList()
}

class TableTests {
    @Test
    fun testTableSeat() {
        val table = Table(randomPlayers(3))
        table.seat()
        assertEquals(table.players().size, 3)
        assertNotNull(table.dealer)
    }

    @Test
    fun testButtonPlayer(){
        val table = Table( randomPlayers(3))
        table.seat()
        val lastButtonPlayer = table.dealer
        table.seat()
        assertNotEquals(table.dealer, lastButtonPlayer)

    }
}

class GameTests {


    private fun checkerPlayers(size: Int =3):List<Player> {
        return (1..size).map { Player("Player $it", 20) }
    }



    @Test
    fun testGameOrder(){
        val g = Game( randomPlayers(3))
        assertEquals(g.players().size,g.actionOrder().size)
        println("Button player is ${g.dealer.name}")

    }

    @Test
    fun testPrevNext(){
        val g = Game(randomPlayers(4))

        assertNotEquals(g.dealer, g.players().nextPlayer(g.dealer))
        assertNotEquals(g.dealer, g.players().prevPlayer(g.dealer))
    }

    @Test
    fun testBlinds(){
        val g = Game(randomPlayers(4))

        g.reset()
        g.blinds()

        assertTrue ( g.players().prevPlayer(g.dealer).money < g.dealer.money )
        assertTrue (g.players().prevPlayer(g.players().prevPlayer(g.dealer)).money < g.players().prevPlayer(g.dealer).money )
        assertEquals(g.smallBlind + g.bigBlind,  g.potSize())

    }

    @Test
    fun testRun() {
        val g = Game(randomPlayers(5))
        val winners = g.run()
        println("FLOP: ${g.table()}")
        winners.forEach{p -> println("${p.name} ${p.cards()} -> ${p.bestHand} (${p.bestHand?.rank})")}
        assertTrue(winners.size>0)
        println("----")
        g.players().filter{!winners.contains(it)}
            .forEach{println("${it.name} ${it.cards()} -> ${it.bestHand} (${it.bestHand?.rank})")}

    }

}
