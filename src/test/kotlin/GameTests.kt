
import com.anlcan.Game
import com.anlcan.Player
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/**
 * Created on 31.03.18.
 */
class GameTests {

    private fun randomPlayers(size: Int =3):List<Player> {
        return (1..size).map { Player("Player $it", 20) }
    }

    @Test
    fun testButtonPlayer(){
        val g = Game()
        val players = randomPlayers(3)

        players.forEach{g.addPlayer(it)}
        g.reset()
        g.seat()
        assertEquals(g.players().size, players.size)

        assertNotNull(g.dealer)
        val lastButtonPlayer = g.dealer
        g.seat()
        assertNotEquals(g.dealer, lastButtonPlayer)

    }

    @Test
    fun testGameOrder(){
        val g = Game()
        val players = randomPlayers(5)
        players.forEach{g.addPlayer(it)}
        for (i in 1..players.size*2) {
            g.reset()
            g.seat()
            assertEquals(g.players().size,g.actionOrder().size)
            println("Button player is ${g.dealer?.name}")
        }
    }

    @Test
    fun testPrevNext(){
        val g = Game()
        randomPlayers(4).forEach{g.addPlayer(it)}
        for (i in 1..g.players().size*2) {
            g.reset()
            g.seat()
            assertNotEquals(g.dealer, g.nextPlayer(g.dealer))
            assertNotEquals(g.dealer, g.prevPlayer(g.dealer))
        }
    }

    @Test
    fun testBlinds(){
        val g = Game()
        randomPlayers(4).forEach{g.addPlayer(it)}
        g.reset()
        g.seat()
        g.blinds()
        assertTrue {  g.prevPlayer(g.dealer).money < g.dealer.money }
        assertTrue {  g.prevPlayer(g.prevPlayer(g.dealer)).money < g.prevPlayer(g.dealer).money }

    }
}
