public class PlayerS17SurrenderExceptARule extends PlayerRuleHandler {
	protected HandChoiceEnum action(BlackJackHand player, int dealer) {
		int handValue = player.getValue();

		if(!player.is2Cards())
			return null;
		if(!player.isSoft()) {
			if(handValue == 15 && dealer == 10)
				return HandChoiceEnum.SURRENDER;
			if(handValue == 16 && dealer >= 9 && dealer <= 10)
				return HandChoiceEnum.SURRENDER;
		}
		return null;
	}
}