import java.util.ArrayList;
import java.util.List;

public class Shoe {
	protected List<Card> cards;
	protected int count;

	public Shoe() {
		this(1);
	}

	public <T extends Card> Shoe(Class<T> c) {
		this(c, 1);
	}

	public Shoe(int decks) {
		this(Card.class, decks);
	}

	public <T extends Card> Shoe(Class<T> c, int decks) {
		createDeck(c, decks);
	}

	private <T extends Card> void createDeck(Class<T> c, int decks) {
		cards = new ArrayList<Card>();
		Card cardObj = null;
		for(int i = 0; i < decks; ++i) {
			for (CardSuitEnum suit : CardSuitEnum.values()) {
				for (CardRankEnum rank : CardRankEnum.values()) {
					try {
						cardObj = (Card)Class.forName(c.getName()).getDeclaredConstructor(CardSuitEnum.class, CardRankEnum.class).newInstance(suit, rank);
					} catch(Exception e) {
						e.printStackTrace();
					}
					cards.add(cardObj);//new Card(suit, rank));
				}
			}
		}
		count = decks * CardSuitEnum.values().length * CardRankEnum.values().length;
	}

	public int count() {
		return count;
	}

	public Card pop() {
		--count;
		return cards.remove(0);
	}
}