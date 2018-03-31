import com.anlcan.*
import org.junit.Assert
import org.junit.Test
/**
 * Created on 20.03.18.
 */

class Test {

    private val deck = Deck()

    @Test
    fun testPerms(){
        Assert.assertEquals(permutation(mutableListOf(deck.next()), 0).size,1)
        Assert.assertEquals(permutation(mutableListOf(deck.next(), deck.next()), 1).size, 2)
        Assert.assertEquals(permutation(mutableListOf(deck.next(), deck.next(), deck.next()), 1).size , 3)

        Assert.assertEquals(6, permutation(mutableListOf(deck.next(), deck.next(), deck.next()), 2).size)


        val k =permutation(mutableListOf(deck.next(), deck.next(), deck.next(), deck.next(), deck.next()), 3)
        Assert.assertEquals(60, k.size)

    }

    @Test
    fun testHands() {

        val h1 = Hand(deck.next(5))
        val h2 = Hand(deck.next(5))
        Assert.assertTrue(h1 != h2)

    }

    @Test
    fun testRanks(){
        val royal_flush = Hand(
                mutableListOf(Card(Suit.Spades, Value.ACE),
                        Card(Suit.Spades, Value.KING),
                        Card(Suit.Spades, Value.QUEEN),
                        Card(Suit.Spades, Value.JACK),
                        Card(Suit.Spades, Value.TEN)).shuffled())

        Assert.assertTrue(Rank.isOrdered(royal_flush.cards))
        Assert.assertTrue(Rank.isSameSuit(royal_flush.cards))
        Assert.assertEquals(Rank.ROYAL_FLUSH, royal_flush.rank)


        val straight_flush = Hand(
                mutableListOf(Card(Suit.Spades, Value.NINE),
                        Card(Suit.Spades, Value.KING),
                        Card(Suit.Spades, Value.QUEEN),
                        Card(Suit.Spades, Value.JACK),
                        Card(Suit.Spades, Value.TEN)).shuffled())

        Assert.assertTrue(Rank.isOrdered(straight_flush.cards))
        Assert.assertTrue(Rank.isSameSuit(straight_flush.cards))
        Assert.assertEquals(Rank.STRAIGHT_FLUSH, straight_flush.rank)

        val fourOfaKind = Hand(
                mutableListOf(Card(Suit.Spades, Value.ACE),
                        Card(Suit.Hearts, Value.ACE),
                        Card(Suit.Clubs, Value.ACE),
                        Card(Suit.Diamonds, Value.ACE),
                        Card(Suit.Spades, Value.TEN)).shuffled())

        Assert.assertEquals(Rank.FOUR_OF_A_KIND, fourOfaKind.rank)

        val fullHouse = Hand(
                mutableListOf(Card(Suit.Spades, Value.NINE),
                        Card(Suit.Clubs, Value.NINE),
                        Card(Suit.Diamonds, Value.NINE),
                        Card(Suit.Spades, Value.JACK),
                        Card(Suit.Diamonds, Value.JACK)).shuffled())

        Assert.assertEquals(Rank.FULL_HOUSE, fullHouse.rank)

        val flush = Hand(
                mutableListOf(Card(Suit.Spades, Value.NINE),
                        Card(Suit.Spades, Value.KING),
                        Card(Suit.Spades, Value.SEVEN),
                        Card(Suit.Spades, Value.JACK),
                        Card(Suit.Spades, Value.FOUR)).shuffled())

        Assert.assertEquals(Rank.FLUSH, flush.rank)

        val straight = Hand(
                mutableListOf(Card(Suit.Spades, Value.NINE),
                        Card(Suit.Hearts, Value.KING),
                        Card(Suit.Spades, Value.QUEEN),
                        Card(Suit.Diamonds, Value.JACK),
                        Card(Suit.Spades, Value.TEN)).shuffled())

        Assert.assertEquals(Rank.STRAIGHT, straight.rank)


        val threeOfaKind = Hand(
                mutableListOf(Card(Suit.Spades, Value.ACE),
                        Card(Suit.Hearts, Value.ACE),
                        Card(Suit.Clubs, Value.ACE),
                        Card(Suit.Diamonds, Value.QUEEN),
                        Card(Suit.Spades, Value.TEN)).shuffled())

        Assert.assertEquals(Rank.THREE_OF_A_KIND, threeOfaKind.rank)

        val twoParis = Hand(
                mutableListOf(Card(Suit.Spades, Value.ACE),
                        Card(Suit.Hearts, Value.ACE),
                        Card(Suit.Clubs, Value.TEN),
                        Card(Suit.Diamonds, Value.QUEEN),
                        Card(Suit.Spades, Value.QUEEN)).shuffled())

        Assert.assertEquals(Rank.TWO_PAIRS, twoParis.rank)

        val paris = Hand(
                mutableListOf(Card(Suit.Spades, Value.ACE),
                        Card(Suit.Hearts, Value.ACE),
                        Card(Suit.Clubs, Value.TEN),
                        Card(Suit.Diamonds, Value.SEVEN),
                        Card(Suit.Spades, Value.QUEEN)).shuffled())

        Assert.assertEquals(Rank.PAIR, paris.rank)

        val highCard = Hand(
                mutableListOf(Card(Suit.Spades, Value.ACE),
                        Card(Suit.Hearts, Value.FOUR),
                        Card(Suit.Clubs, Value.TWO),
                        Card(Suit.Diamonds, Value.SEVEN),
                        Card(Suit.Spades, Value.QUEEN)).shuffled())

        Assert.assertEquals(Rank.HIGH_CARD, highCard.rank)

        // what are the odds???
        val random = Hand(deck.next(5))
        Assert.assertFalse(Rank.isOrdered(random.cards))
        Assert.assertFalse(Rank.isSameSuit(random.cards))

        val order = listOf(royal_flush, straight_flush, fourOfaKind,
                        fullHouse, flush,  straight, threeOfaKind,
                        twoParis, paris,highCard)
        order.all { handHigh ->
            order.reversed().take(order.indexOf(handHigh)).all { handLow-> handHigh > handLow } }

    }



}
