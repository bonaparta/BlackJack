public class PlayerSurrenderedRule extends PlayerRuleHandler {
	protected HandChoiceEnum action(BlackJackHand player, int dealer) {
		if(player.isSurrendered())
			return HandChoiceEnum.STAND;
		return null;
	}
}