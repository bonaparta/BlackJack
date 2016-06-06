public class SimPlayerSplitSplitMustSplitMaxRule extends PlayerRuleHandler {
	private int splitMax = 999;

	public SimPlayerSplitSplitMustSplitMaxRule(int max) {
		this.splitMax = max;
	}

	protected HandChoiceEnum action(BlackJackHand player, int dealer) {
		int handValue = player.getValue();

		if(!player.isSplitable())
			return null;
		if(player.getSerial() < this.splitMax)
			return HandChoiceEnum.SPLIT;
		return null;
	}
}