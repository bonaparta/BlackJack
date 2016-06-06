public class PlayerS17StandRule extends PlayerRuleHandler {
	protected HandChoiceEnum action(BlackJackHand player, int dealer) {
		return HandChoiceEnum.STAND;
	}
}