public abstract class PlayerRuleHandler {
	// Surrender, Double, Split, Hit, Stand
	// Surrender Except Ace, Splited A, Double, Split Max Resplit A, Split Max, Hit, Stand MGM, Wynn
	// Surrender Except Ace, Splited A, Double, Split Max, Hit, Stand Crown, Venetian
	// Surrendered, Doubled, Surrender, Resplit A, Splited A, Double, Split, Hit, BlackJackRun
	private PlayerRuleHandler nextHandler = null;

	public void setNext(PlayerRuleHandler handler) {
		this.nextHandler = handler;
	}

	public HandChoiceEnum takeAction(BlackJackHand player, int dealer) {
		HandChoiceEnum ret = action(player, dealer);
		if(ret == null) {
			if(nextHandler != null) {
				return nextHandler.takeAction(player, dealer);
			}
		}
		return ret;
	}

	protected abstract HandChoiceEnum action(BlackJackHand player, int dealer);
}