public class DealerS17Rule implements IDealerRule {
	public HandChoiceEnum doesDraw(BlackJackHand dealer) {
		if(dealer.getValue() > 16)
			return HandChoiceEnum.STAND;
		return HandChoiceEnum.HIT;
	}
}