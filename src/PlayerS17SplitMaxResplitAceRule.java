public class PlayerS17SplitMaxResplitAceRule extends PlayerRuleHandler {
	private int splitMax = 999;

	public PlayerS17SplitMaxResplitAceRule(int max) {
		this.splitMax = max;
	}

	protected HandChoiceEnum action(BlackJackHand player, int dealer) {
		int handValue = player.getValue();

		if(!player.isSplitable())
			return null;
		if(handValue == 12 && player.isSoft() && player.getSerial() < this.splitMax)
			return HandChoiceEnum.SPLIT;
		return null;
	}
}