public class PlayerSplitedAceRule extends PlayerRuleHandler {
	protected HandChoiceEnum action(BlackJackHand player, int dealer) {
		if(player.getCard(0).getValue() == 1 && player.getSerial() > 0 && player.getCardCount() > 1)
			return HandChoiceEnum.STAND;
		return null;
	}
}