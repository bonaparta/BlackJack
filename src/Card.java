public class Card {
	private CardSuitEnum suit;
	private CardRankEnum rank;

	public Card(CardSuitEnum suit, CardRankEnum rank) {
		this.suit = suit;
		this.rank = rank;
	}

	public CardSuitEnum getSuit() {
		return suit;
	}

	public CardRankEnum getRank() {
		return rank;
	}

	public int getValue() {
		return rank.ordinal();
	}

	@Override
	public String toString() {
		return "Suit: " + suit.toString() + ", Rank :" + rank.toString();
	}
}