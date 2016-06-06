public class PlayerS17HitRule extends PlayerRuleHandler {
	protected HandChoiceEnum action(BlackJackHand player, int dealer) {
		int handValue = player.getValue();

		if(player.isSoft()) {
			if(handValue < 18)
				return HandChoiceEnum.HIT;
			if(handValue == 18 && dealer >= 9 && dealer <= 11)
				return HandChoiceEnum.HIT;
		}
		if(handValue < 12)
			return HandChoiceEnum.HIT;
		if(handValue == 12 && !(dealer >= 4 && dealer <= 6))
			return HandChoiceEnum.HIT;
		if(handValue >= 13 && handValue <= 16 && dealer > 6)
			return HandChoiceEnum.HIT;
		return null;
	}
}