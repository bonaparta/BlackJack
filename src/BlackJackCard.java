public class BlackJackCard extends Card {
	public BlackJackCard(CardSuitEnum suit, CardRankEnum rank) {
		super(suit, rank);
	}

	@Override
	public int getValue() {
		if(getRank() == CardRankEnum.ACE)
			return 1;
		return (getRank().compareTo(CardRankEnum.CARD_10) >= 0)? 10 : getRank().ordinal() + 2;
	}
}