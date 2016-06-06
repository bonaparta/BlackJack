public class DealerH17Rule implements IDealerRule {
	public HandChoiceEnum doesDraw(BlackJackHand dealer) {
		int handValue = dealer.getValue();
		if(handValue > 17) {
			return HandChoiceEnum.STAND;
		} else if(handValue == 17) {
			if(!dealer.isSoft())
				return HandChoiceEnum.STAND;
		}
		return HandChoiceEnum.HIT;
	}
}