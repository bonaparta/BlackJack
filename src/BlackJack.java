import java.util.ArrayList;

public class BlackJack {
	private Shoe shuffler;
	private IDealerRule dealerRule;
	private PlayerRuleHandler playerRule;
	private BlackJackRecord record;

	public BlackJack(IDealerRule rule) {
		shuffler = new Shuffler(BlackJackCard.class);
		dealerRule = rule;

	// Surrendered, Doubled, Surrender, Resplit A, Splited A, Double, Split, Hit, BlackJackRun
		PlayerRuleHandler next = new PlayerSurrenderedRule();
		playerRule = next;
		PlayerRuleHandler nextNew = new PlayerDoubledRule();
		next.setNext(nextNew);
		next = nextNew;
		nextNew = new PlayerS17SurrenderRule();
		next.setNext(nextNew);
		next = nextNew;
		nextNew = new PlayerResplitAceRule();
		next.setNext(nextNew);
		next = nextNew;
		nextNew = new PlayerSplitedAceRule();
		next.setNext(nextNew);
		next = nextNew;
		nextNew = new PlayerS17DoubleRule();
		next.setNext(nextNew);
		next = nextNew;
		nextNew = new PlayerS17SplitRule();
		next.setNext(nextNew);
		next = nextNew;
		nextNew = new PlayerS17HitRule();
		next.setNext(nextNew);
		nextNew.setNext(new PlayerS17StandRule());
	}

	public double deal() {
		ArrayList<BlackJackHand> player = new ArrayList<BlackJackHand>();
		BlackJackHand dealer = new BlackJackHand();
		player.add(new BlackJackHand());
		return deal(shuffler, dealer, player);
	}

	public double deal(Shoe shuffler, BlackJackHand dealer,
			ArrayList<BlackJackHand> player) {
		record = new BlackJackRecord(dealer, player);
		player.get(0).addCard(shuffler.pop());
		dealer.addCard(shuffler.pop());
		player.get(0).addCard(shuffler.pop());
next_hand:
		for(int i = 0; i < player.size(); ++i) {
			while(player.get(i).getCardCount() < 2)
				player.get(i).addCard(shuffler.pop());
			HandChoiceEnum action;
			while((action = playerRule.takeAction(player.get(i), dealer.getValue())) != HandChoiceEnum.STAND) {
				switch(action) {
				case SURRENDER:
					player.get(i).setSurrender(true);
					break;
				case DOUBLE:
					player.get(i).addCard(shuffler.pop());
					player.get(i).setBet(2);
					break;
				case SPLIT:
					BlackJackRules.split(player, i);
					--i;
					continue next_hand;
				default: // HIT
					player.get(i).addCard(shuffler.pop());
					break;
				}
			}
		}
		for(BlackJackHand anyHand : player) {
			if(!anyHand.isSurrendered() && !anyHand.isBusted()) {
				dealerDraw(shuffler, dealer);
				break;
			}
		}
		return BlackJackRules.judge(dealer, player);
	}

	public BlackJackRecord getResult() {
		return record;
	}

	private void dealerDraw(Shoe shuffler, BlackJackHand dealer) {
		while(dealerRule.doesDraw(dealer) == HandChoiceEnum.HIT) {
			dealer.addCard(shuffler.pop());
		}
	}
} // end class BlackJack