
import com.anlcan.Game
import com.anlcan.Player
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertNotNull

/**
 * Created on 31.03.18.
 */
class GameTests {

    private fun randomPlayers(size: Int =3):List<Player> {
        return (1..size).map { Player("Player $it") }
    }

    @Test
    fun testButtonPlayer(){
        val g = Game()
        val players = randomPlayers(3)

        players.forEach{g.addPlayer(it)}
        g.reset()
        g.seat()
        assertEquals(g.players().size, players.size)

        assertNotNull(g.buttonPlayer)
        val lastButtonPlayer = g.buttonPlayer
        g.seat()
        assertNotEquals(g.buttonPlayer, lastButtonPlayer)

    }

    @Test
    fun testGameOrder(){
        val g = Game()
        val players = randomPlayers(5)
        players.forEach{g.addPlayer(it)}
        for (i in 1..players.size*2) {
            g.reset()
            g.seat()
            assertEquals(g.players().size,g.gameOrder().size)
            println("Button player is ${g.buttonPlayer?.name}")
        }

    }
}
