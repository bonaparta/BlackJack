public class PlayerS17SplitMaxRule extends PlayerS17SplitRule {
	private int splitMax = 999;

	public PlayerS17SplitMaxRule(int max) {
		this.splitMax = max;
	}

	@Override
	protected HandChoiceEnum action(BlackJackHand player, int dealer) {
		if(player.getSerial() < this.splitMax)
			return super.action(player, dealer);
		return null;
	}
}