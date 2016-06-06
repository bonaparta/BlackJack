public class PlayerS17DoubleRule extends PlayerRuleHandler {
	protected HandChoiceEnum action(BlackJackHand player, int dealer) {
		int handValue = player.getValue();

		if(!player.is2Cards())
			return null;
		if(player.isSoft()) {
			if(handValue < 13 || handValue > 18)
				return null;
			if((handValue == 13 || handValue == 14) && (dealer >= 5 && dealer <= 6))
				return HandChoiceEnum.DOUBLE;
			if((handValue == 15 || handValue == 16) && (dealer >= 4 && dealer <= 6))
				return HandChoiceEnum.DOUBLE;
			if((handValue == 17 || handValue == 18) && (dealer >= 3 && dealer <= 6))
				return HandChoiceEnum.DOUBLE;
		} else {
			if(handValue > 11 || handValue < 9)
				return null;
			if(handValue == 9 && dealer >= 3 && dealer <= 6)
				return HandChoiceEnum.DOUBLE;
			if(handValue == 10 && dealer >= 2 && dealer <= 9)
				return HandChoiceEnum.DOUBLE;
			if(handValue == 11 && dealer >= 2 && dealer <= 10)
				return HandChoiceEnum.DOUBLE;
		}
		return null;
	}
}