public class PlayerS17SplitRule extends PlayerRuleHandler {
	protected HandChoiceEnum action(BlackJackHand player, int dealer) {
		int handValue = player.getValue();

		if(!player.isSplitable())
			return null;
		if((handValue == 4 || handValue == 6 || handValue == 14) && dealer >= 2 && dealer <= 7)
			return HandChoiceEnum.SPLIT;
		if(handValue == 8 && dealer >= 5 && dealer <= 6)
			return HandChoiceEnum.SPLIT;
		if(handValue == 12 && !player.isSoft() && dealer >= 2 && dealer <= 6)
			return HandChoiceEnum.SPLIT;
		if(handValue == 16 || (handValue == 12 && player.isSoft()))
			return HandChoiceEnum.SPLIT;
		if(handValue == 18 && !(dealer == 7 || dealer == 10 || dealer == 11))
			return HandChoiceEnum.SPLIT;
		return null;
	}
}