import java.util.stream.IntStream;
import java.util.stream.DoubleStream;

public class SimPlayerHitRun {
	private int[] cards;
	private IDealerRule dealerRule;
	private PlayerRuleHandler playerRule;
	private BlackJackHand dealer;
	private enum BetEnum {SUM_4, SUM_5, SUM_6, SUM_7, SUM_8, SUM_9, SUM_10,
			SUM_11, SUM_12, SUM_13, SUM_14, SUM_15, SUM_16, SUM_17, SUM_18, SUM_19, SUM_20,
			SOFT_12, SOFT_13, SOFT_14, SOFT_15, SOFT_16, SOFT_17, SOFT_18, SOFT_19, SOFT_20, SOFT_21}
	private enum HitStateEnum {PLAYER, DEALER}
	private double[][] expected = new double[BetEnum.values().length][CardRankEnum.values().length];

	public SimPlayerHitRun(IDealerRule rule) {
		dealerRule = rule;
		playerRule = new PlayerS17HitRule();
		PlayerRuleHandler next = new PlayerS17StandRule();
		playerRule.setNext(next);
		cards = new int[] {32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32};
		dealer = new BlackJackHand();
	}

	public void simExpectedValue(BlackJackHand dealer) {
		if(dealerRule.doesDraw(dealer) == HandChoiceEnum.STAND)
			return;
		// 4 ~ 11
		BlackJackHand player = new BlackJackHand();
		player.addCard(new BlackJackCard(CardSuitEnum.CLUBS, CardRankEnum.CARD_2));
		for(int i = CardRankEnum.CARD_2.ordinal(); i <= CardRankEnum.CARD_9.ordinal(); ++i) {
			BlackJackCard c = new BlackJackCard(CardSuitEnum.CLUBS, CardRankEnum.values()[i]);
			player.addCard(c);
			for(int j = 0; j < CardRankEnum.values().length; ++j) {
//System.out.println(i + "\t" + j);
				BlackJackCard dc = new BlackJackCard(CardSuitEnum.CLUBS, CardRankEnum.values()[j]);
				dealer.addCard(dc);
				expected[i][j] = getExpectedValue(dealer, player, 1, HitStateEnum.PLAYER);
				dealer.removeCard(dc);
			}
			player.removeCard(c);
		}
		// 12 ~ 20
		player = new BlackJackHand();
		player.addCard(new BlackJackCard(CardSuitEnum.CLUBS, CardRankEnum.CARD_10));
		for(int i = CardRankEnum.CARD_2.ordinal(); i <= CardRankEnum.CARD_10.ordinal(); ++i) {
			BlackJackCard c = new BlackJackCard(CardSuitEnum.CLUBS, CardRankEnum.values()[i]);
			player.addCard(c);
			for(int j = 0; j < CardRankEnum.values().length; ++j) {
//System.out.println(i + BetEnum.SUM_12.ordinal() + "\t" + j);
				BlackJackCard dc = new BlackJackCard(CardSuitEnum.CLUBS, CardRankEnum.values()[j]);
				dealer.addCard(dc);
				expected[i + BetEnum.SUM_12.ordinal()][j] = getExpectedValue(dealer, player, 1, HitStateEnum.PLAYER);
				dealer.removeCard(dc);
			}
			player.removeCard(c);
		}
		// soft 12
		player = new BlackJackHand();
		player.addCard(new BlackJackCard(CardSuitEnum.CLUBS, CardRankEnum.ACE));
		player.addCard(new BlackJackCard(CardSuitEnum.CLUBS, CardRankEnum.ACE));
		for(int j = 0; j < CardRankEnum.values().length; ++j) {
//System.out.println(BetEnum.SOFT_12.ordinal() + "\t" + j);
			BlackJackCard dc = new BlackJackCard(CardSuitEnum.CLUBS, CardRankEnum.values()[j]);
			dealer.addCard(dc);
			expected[BetEnum.SOFT_12.ordinal()][j] = getExpectedValue(dealer, player, 1, HitStateEnum.PLAYER);
			dealer.removeCard(dc);
		}
		// soft 13 ~ soft 21
		player = new BlackJackHand();
		player.addCard(new BlackJackCard(CardSuitEnum.CLUBS, CardRankEnum.ACE));
		for(int i = CardRankEnum.CARD_2.ordinal(); i <= CardRankEnum.CARD_10.ordinal(); ++i) {
			BlackJackCard c = new BlackJackCard(CardSuitEnum.CLUBS, CardRankEnum.values()[i]);
			player.addCard(c);
			for(int j = 0; j < CardRankEnum.values().length; ++j) {
//System.out.println(i + BetEnum.SOFT_13.ordinal() + "\t" + j);
				BlackJackCard dc = new BlackJackCard(CardSuitEnum.CLUBS, CardRankEnum.values()[j]);
				dealer.addCard(dc);
				expected[i + BetEnum.SOFT_13.ordinal()][j] = getExpectedValue(dealer, player, 1, HitStateEnum.PLAYER);
				dealer.removeCard(dc);
			}
			player.removeCard(c);
		}
	}

	private double getExpectedValue(BlackJackHand dealer, BlackJackHand player, double probability, HitStateEnum state) {
		int numAllCard = IntStream.of(cards).sum();
		if(numAllCard == 0)
			return probability;
		double expectedValue = 0;
		for(int i = 0; i < cards.length; ++i) {
			if(cards[i] == 0)
				continue;
			int numValueCard = cards[i];
			double prob = probability * numValueCard / numAllCard;
			BlackJackCard c = new BlackJackCard(CardSuitEnum.CLUBS, CardRankEnum.values()[i]);
//			--cards[i];
			if(state == HitStateEnum.PLAYER) {
				player.addCard(c);
				if(player.isBusted()) {
					expectedValue -= prob * player.getBet();
//			++cards[i];
					player.removeCard(c);
					continue;
				}
				if(playerRule.takeAction(player, dealer.getValue()) == HandChoiceEnum.HIT) {
					expectedValue += getExpectedValue(dealer, player, prob, HitStateEnum.PLAYER);
//			++cards[i];
					player.removeCard(c);
					continue;
				}
			} else {
				dealer.addCard(c);
			}
			if(dealerRule.doesDraw(dealer) == HandChoiceEnum.HIT)
				expectedValue += getExpectedValue(dealer, player, prob, HitStateEnum.DEALER);
			else {
				expectedValue += prob * BlackJackRules.judge(dealer, player);
			}
//			++cards[i];
			if(state == HitStateEnum.PLAYER) {
				player.removeCard(c);
			} else {
				dealer.removeCard(c);
			}
		}
		return expectedValue;
	}

	public static void main(String[] args) {
		SimPlayerHitRun game = new SimPlayerHitRun(new DealerS17Rule());
		game.simExpectedValue(game.dealer);
		for(int i = 0; i < CardRankEnum.values().length; ++i) {
			System.out.print("\t" + CardRankEnum.values()[i].toString());
		}
		for(int i = 0; i < game.expected.length; ++i) {
			System.out.print("\n" + BetEnum.values()[i].toString());
			for(int j = 0; j < game.expected[i].length; ++j) {
				System.out.printf("\t" + game.expected[i][j]);
			}
		}
	}
}