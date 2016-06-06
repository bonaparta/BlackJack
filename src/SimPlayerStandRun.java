import java.util.stream.IntStream;
import java.util.stream.DoubleStream;

public class SimPlayerStandRun {
	private int[] cards;
	private IDealerRule dealerRule;
	private BlackJackHand dealer;
	private enum BetEnum {SUM0_16, SUM_17, SUM_18, SUM_19, SUM_20, SUM_21}
	private double[][] expected = new double[BetEnum.values().length][CardRankEnum.values().length];

	public SimPlayerStandRun(IDealerRule rule) {
		dealerRule = rule;
		cards = new int[] {32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32};
		dealer = new BlackJackHand();
	}

	public void simExpectedValue(BlackJackHand dealer) {
		if(dealerRule.doesDraw(dealer) == HandChoiceEnum.STAND)
			return;
		BlackJackHand player = new BlackJackHand();
		player.addCard(new BlackJackCard(CardSuitEnum.CLUBS, CardRankEnum.CARD_7));
		player.addCard(new BlackJackCard(CardSuitEnum.CLUBS, CardRankEnum.CARD_7));
		for(int i = 0; i < BetEnum.values().length; ++i) {
			BlackJackCard c = new BlackJackCard(CardSuitEnum.CLUBS, CardRankEnum.values()[i]);
			player.addCard(c);
			for(int j = 0; j < CardRankEnum.values().length; ++j) {
				BlackJackCard dc = new BlackJackCard(CardSuitEnum.CLUBS, CardRankEnum.values()[j]);
				dealer.addCard(dc);
				expected[i][j] = getExpectedValue(dealer, player, 1);
				dealer.removeCard(dc);
			}
			player.removeCard(c);
		}
	}

	private double getExpectedValue(BlackJackHand dealer, BlackJackHand player, double probability) {
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
			dealer.addCard(c);
			if(dealerRule.doesDraw(dealer) == HandChoiceEnum.HIT)
				expectedValue += getExpectedValue(dealer, player, prob);
			else {
				expectedValue += prob * BlackJackRules.judge(dealer, player);
			}
			dealer.removeCard(c);
//			++cards[i];
		}
		return expectedValue;
	}

	public static void main(String[] args) {
		SimPlayerStandRun s17run = new SimPlayerStandRun(new DealerS17Rule());
		s17run.simExpectedValue(s17run.dealer);
		for(int i = 0; i < CardRankEnum.values().length; ++i) {
			System.out.print("\t" + CardRankEnum.values()[i].toString());
		}
		for(int i = 0; i < BetEnum.values().length; ++i) {
			System.out.print("\n" + BetEnum.values()[i].toString());
			for(int j = 0; j < CardRankEnum.values().length; ++j) {
				System.out.printf("\t" + s17run.expected[i][j]);
			}
		}
	}
}