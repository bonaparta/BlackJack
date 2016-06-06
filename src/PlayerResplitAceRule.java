public class PlayerResplitAceRule extends PlayerRuleHandler {
	protected HandChoiceEnum action(BlackJackHand player, int dealer) {
		if(player.getSerial() > 0 && player.getCardCount() > 1)
			if(player.getCard(0).getValue() == 1 && player.getCard(1).getValue() == 1)
				return HandChoiceEnum.SPLIT;
		return null;
	}
}