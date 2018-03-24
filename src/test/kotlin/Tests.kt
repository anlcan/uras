import com.anlcan.Deck
import com.anlcan.permutation
import org.junit.Assert
import org.junit.Test
/**
 * Created on 20.03.18.
 */

class Test {

    val deck = Deck()

    @Test
    fun testPerms(){
        Assert.assertEquals(permutation(mutableListOf(deck.next()), 0).size,1)
        Assert.assertEquals(permutation(mutableListOf(deck.next(), deck.next()), 1).size, 2)
        Assert.assertEquals(permutation(mutableListOf(deck.next(), deck.next(), deck.next()), 1).size , 3)

        Assert.assertEquals(6, permutation(mutableListOf(deck.next(), deck.next(), deck.next()), 2).size)


        val k =permutation(mutableListOf(deck.next(), deck.next(), deck.next(), deck.next(), deck.next()), 3)
        Assert.assertEquals(60, k.size)

    }
}
