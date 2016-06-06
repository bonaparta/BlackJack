import java.util.ArrayList;
import java.util.stream.IntStream;
import java.util.stream.DoubleStream;

public class SimPlayerSplitRun {
	private int[] cards;
	private IDealerRule dealerRule;
	private PlayerRuleHandler playerRule;
	private BlackJackHand dealer;
	private enum BetEnum {S22, S33, S44, S55, S66, S77, S88, S99, STT, SAA}
	private double[][] expected = new double[BetEnum.values().length][CardRankEnum.values().length];

	// assist tool
	private enum DealerEnum {BUST, SUM_17, SUM_18, SUM_19, SUM_20, SUM_21, BLACK_JACK}
	private double[][] dealerDistribution = new double[CardRankEnum.values().length][DealerEnum.values().length];
	private enum PlayerEnum {BUST, SUM4_16, SUM_17, SUM_18, SUM_19, SUM_20, SUM_21, BLACK_JACK}
	// 16 vs 2 or 20 vs 6 [dealer][player]
	private double[][] dealerExpectedDistribution = new double[CardRankEnum.values().length][PlayerEnum.values().length];
	// hand no more split 2 vs 3 or T vs A [player][dealer]
	private double[][] playerNoSplitExpectedDistribution = new double[CardRankEnum.values().length][CardRankEnum.values().length];

	public SimPlayerSplitRun(IDealerRule rule) {
		dealerRule = rule;
		// Doubled, Splitted A, Sim Split Must Split Max, Double, Hit, Stand
		playerRule = new PlayerDoubledRule();
		PlayerRuleHandler next = new PlayerSplitedAceRule();
		playerRule.setNext(next);
		PlayerRuleHandler nextNew = new SimPlayerSplitSplitMustSplitMaxRule(3);
		next.setNext(nextNew);
		next = nextNew;
		nextNew = new PlayerS17DoubleRule();
		next.setNext(nextNew);
		next = nextNew;
		nextNew = new PlayerS17HitRule();
		next.setNext(nextNew);
		nextNew.setNext(new PlayerS17StandRule());
		cards = new int[] {32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32};
		dealer = new BlackJackHand();

		// Tool 1: don't trace into dealer
		simDistributionFillDealerDistribution(this.dealerDistribution, new BlackJackHand(), 1);
		normalizeByRank(this.dealerDistribution);
		transform2Expected(this.dealerExpectedDistribution, this.dealerDistribution);

		// Tool 2: don't trace into player who can't split any more
		simExpectedValueFillPlayerNoSplitExpectedDistribution(this.playerNoSplitExpectedDistribution);
	}

	public void simExpectedValue(BlackJackHand dealer) {
		if(dealerRule.doesDraw(dealer) == HandChoiceEnum.STAND)
			return;
		// 2 ~ 10
		for(int i = 0; i <= CardRankEnum.CARD_10.ordinal(); ++i) {
			ArrayList<BlackJackHand> player = new ArrayList<BlackJackHand>();
			BlackJackHand hand = new BlackJackHand();
			hand.addCard(new BlackJackCard(CardSuitEnum.CLUBS, CardRankEnum.values()[i]));
			hand.addCard(new BlackJackCard(CardSuitEnum.CLUBS, CardRankEnum.values()[i]));
			player.add(hand);
			int index = 0;
			BlackJackRules.split(player, index);
System.out.println("****Player**** " + i);
			for(int j = 0; j < CardRankEnum.values().length; ++j) {
System.out.println("****Dealer**** " + j);
				BlackJackCard c = new BlackJackCard(CardSuitEnum.CLUBS, CardRankEnum.values()[j]);
				dealer.addCard(c);
				expected[i][j] = getExpectedValue(dealer, player, index, 1);
				dealer.removeCard(c);
			}
		}
		// A
		ArrayList<BlackJackHand> player = new ArrayList<BlackJackHand>();
		BlackJackHand hand = new BlackJackHand();
		hand.addCard(new BlackJackCard(CardSuitEnum.CLUBS, CardRankEnum.ACE));
		hand.addCard(new BlackJackCard(CardSuitEnum.CLUBS, CardRankEnum.ACE));
		player.add(hand);
		int index = 0;
		BlackJackRules.split(player, index);
		for(int j = 0; j < CardRankEnum.values().length; ++j) {
			BlackJackCard c = new BlackJackCard(CardSuitEnum.CLUBS, CardRankEnum.values()[j]);
			dealer.addCard(c);
			expected[BetEnum.SAA.ordinal()][j] = getExpectedValue(dealer, player, index, 1);
			dealer.removeCard(c);
		}
	}

	private double getExpectedValue(BlackJackHand dealer, ArrayList<BlackJackHand> player, int index, double probability) {
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
			player.get(index).addCard(c);
			HandChoiceEnum action = playerRule.takeAction(player.get(index), dealer.getValue());
			switch(action) {
			case DOUBLE:
				player.get(index).setBet(2);
				expectedValue += getExpectedValue(dealer, player, index, prob);
				break;
			case HIT:
				expectedValue += getExpectedValue(dealer, player, index, prob);
				break;
			case SPLIT:
				expectedValue += (player.size() - index + 1) * prob * judgeNoSplitExpectedValue(this.playerNoSplitExpectedDistribution, player.get(index).getCard(0).getValue(), dealer.getValue());
				break;
			default: // STAND
				expectedValue += prob * judgeExpectedValue(this.dealerExpectedDistribution, player.get(index), dealer.getValue());
				if(index + 1 < player.size()) {
					if(index == 2)
						expectedValue += prob * judgeNoSplitExpectedValue(this.playerNoSplitExpectedDistribution, player.get(index).getCard(0).getValue(), dealer.getValue());
					else
						expectedValue += getExpectedValue(dealer, player, index + 1, prob);
				}
			}
			player.get(index).removeCard(c);
//			++cards[i];
		}
		return expectedValue;
	}

	private double judgeExpectedValue(double[][] expectedTable, BlackJackHand completeHand, int dealer) {
		int player = completeHand.getValue();
		int dealerIndex = (dealer == 11)? CardRankEnum.values().length - 1 : dealer - 2;
		int playerIndex = -1;
		if(player > 21)
			playerIndex = 0;
		else if(player < 17)
			playerIndex = 1;
		else
			playerIndex = player - 15;
		return expectedTable[dealerIndex][playerIndex];
	}

	private double judgeNoSplitExpectedValue(double[][] expectedTable, int playerCardValue, int dealer) {
		int playerIndex = (playerCardValue == 1)? CardRankEnum.values().length - 1 : playerCardValue - 2;
		int dealerIndex = (dealer == 11)? CardRankEnum.values().length - 1 : dealer - 2;
		return expectedTable[playerIndex][dealerIndex];
	}

	private void simDistributionFillDealerDistribution(double[][] distribution, BlackJackHand dealer, double probability) {
		if(dealerRule.doesDraw(dealer) == HandChoiceEnum.STAND)
			return;
		int numAllCard = IntStream.of(cards).sum();
		for(int i = 0; i < cards.length; ++i) {
			int numValueCard = cards[i];
			double prob = probability * numValueCard / numAllCard;
//			--cards[i];
			BlackJackCard c = new BlackJackCard(CardSuitEnum.CLUBS, CardRankEnum.values()[i]);
			dealer.addCard(c);
			if(dealerRule.doesDraw(dealer) == HandChoiceEnum.HIT)
				simDistributionFillDealerDistribution(distribution, dealer, prob);
			else {
				if(dealer.getValue() <= 21) {
					if(dealer.isBlackJack()) {
						distribution[dealer.getCard(0).getRank().ordinal()][DealerEnum.BLACK_JACK.ordinal()] += prob;
					} else {
						distribution[dealer.getCard(0).getRank().ordinal()][DealerEnum.SUM_17.ordinal() + (dealer.getValue() - 17)] += prob;
					}
				} else {
					distribution[dealer.getCard(0).getRank().ordinal()][DealerEnum.BUST.ordinal()] += prob;
				}
			}
			dealer.removeCard(c);
//			++cards[i];
		}
	}

	private void normalizeByRank(double[][] distribution) {
		double[] sum = new double[CardRankEnum.values().length];
		for(int i = 0; i < distribution.length; ++i)
			sum[i] = DoubleStream.of(distribution[i]).sum();
		for(int i = 0; i < distribution.length; ++i)
			for(int j = 0; j < distribution[i].length; ++j)
				distribution[i][j] = distribution[i][j] / sum[i];
	}

	private void transform2Expected(double[][] expectedTable, double[][] distribution) {
		for(int i = 0; i < expectedTable.length; ++i) { // [2 - A][Bust 0-16 17 ... 21 BJ]
			for(int j = 0; j < expectedTable[i].length; ++j) { // [Bust 0-16 17 ... 21 BJ]
				double winProb = 0;
				if(j < 2) {
					for(int k = 0; k < j; ++k)
						winProb += distribution[i][k];
				} else {
					for(int k = 0; k < j - 1; ++k)
						winProb += distribution[i][k];
				}
				double loseProb = 0;
				for(int k = j; k < distribution[i].length; ++k)
					loseProb += distribution[i][k];
				expectedTable[i][j] = winProb - loseProb;
			}
		}
	}

	private PlayerRuleHandler playerNoSplitRule;
	private void simExpectedValueFillPlayerNoSplitExpectedDistribution(double[][] expectedTable) {
		playerNoSplitRule = new PlayerDoubledRule();
		PlayerRuleHandler next = new PlayerSplitedAceRule();
		playerNoSplitRule.setNext(next);
		PlayerRuleHandler nextNew = new PlayerS17DoubleRule();
		next.setNext(nextNew);
		next = nextNew;
		nextNew = new PlayerS17HitRule();
		next.setNext(nextNew);
		nextNew.setNext(new PlayerS17StandRule());

		BlackJackHand player = new BlackJackHand();
		player.setSerial(2);
		for(int i = 0; i < CardRankEnum.values().length; ++i) {
			BlackJackCard c = new BlackJackCard(CardSuitEnum.CLUBS, CardRankEnum.values()[i]);
			player.addCard(c);
			for(int j = 0; j < CardRankEnum.values().length; ++j) {
				BlackJackHand dealer = new BlackJackHand();
				dealer.addCard(new BlackJackCard(CardSuitEnum.CLUBS, CardRankEnum.values()[j]));
				expectedTable[i][j] = getPlayerNoSplitExpectedValue(dealer, player, 1);
			}
			player.removeCard(c);
		}
	}

	private double getPlayerNoSplitExpectedValue(BlackJackHand dealer, BlackJackHand player, double probability) {
		int numAllCard = IntStream.of(cards).sum();
		double expectedValue = 0;
		for(int i = 0; i < cards.length; ++i) {
			int numValueCard = cards[i];
			double prob = probability * numValueCard / numAllCard;
			BlackJackCard c = new BlackJackCard(CardSuitEnum.CLUBS, CardRankEnum.values()[i]);
//			--cards[i];
			player.addCard(c);
			HandChoiceEnum action = playerNoSplitRule.takeAction(player, dealer.getValue());
			switch(action) {
			case DOUBLE:
				player.setBet(2);
				expectedValue += getPlayerNoSplitExpectedValue(dealer, player, prob);
				break;
			case HIT:
				expectedValue += getPlayerNoSplitExpectedValue(dealer, player, prob);
				break;
			default: // STAND
				expectedValue += prob * judgeExpectedValue(this.dealerExpectedDistribution, player, dealer.getValue());
			}
			player.removeCard(c);
//			++cards[i];
		}
		return expectedValue;
	}

	public static void main(String[] args) {
		SimPlayerSplitRun game = new SimPlayerSplitRun(new DealerS17Rule());
		game.simExpectedValue(game.dealer);
		for(int i = 0; i < CardRankEnum.values().length; ++i) {
			System.out.print("\t" + CardRankEnum.values()[i].toString());
		}
		for(int i = 0; i < game.expected.length; ++i) {
			System.out.print("\n" + BetEnum.values()[i].toString());
			for(int j = 0; j < game.expected[i].length; ++j) {
				System.out.printf("\t%.4f", game.expected[i][j]);
			}
		}
	}
}