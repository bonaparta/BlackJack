public class PlayerDoubledRule extends PlayerRuleHandler {
	protected HandChoiceEnum action(BlackJackHand player, int dealer) {
		if(player.isDoubled() && player.getCardCount() > 2)
			return HandChoiceEnum.STAND;
		return null;
	}
}