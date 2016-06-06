import java.util.stream.IntStream;
import java.util.stream.DoubleStream;

public class SimDealerRun {
	private int[] cards;
	private IDealerRule dealerRule;
	private enum BetEnum {BUST, SUM_17, SUM_18, SUM_19, SUM_20, SUM_21, BLACK_JACK}
	private double[] probability = new double[BetEnum.values().length];
	private double[][] probabilityInit = new double[CardRankEnum.values().length][BetEnum.values().length];

	public SimDealerRun(IDealerRule rule) {
		dealerRule = rule;
		cards = new int[] {32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32};
	}

	public void setDecks(int[] decks) {
		cards = decks;
	}

	public void setDecks(int decks) {
		for(int i = 0; i < cards.length; ++i)
			cards[i] = 4 * decks;
	}

	public void simDistribution(BlackJackHand dealer, double probability) {
		if(dealerRule.doesDraw(dealer) == HandChoiceEnum.STAND)
			return;
		int numAllCard = IntStream.of(cards).sum();
		for(int i = 0; i < cards.length; ++i) {
			int numValueCard = cards[i];
			double prob = probability * numValueCard / numAllCard;
			--cards[i];
			BlackJackCard c = new BlackJackCard(CardSuitEnum.CLUBS, CardRankEnum.values()[i]);
			dealer.addCard(c);
			if(dealerRule.doesDraw(dealer) == HandChoiceEnum.HIT)
				simDistribution(dealer, prob);
			else {
				if(dealer.getValue() <= 21) {
					if(dealer.isBlackJack()) {
						this.probability[BetEnum.BLACK_JACK.ordinal()] += prob;
						this.probabilityInit[dealer.getCard(0).getRank().ordinal()][BetEnum.BLACK_JACK.ordinal()] += prob;
					} else {
						this.probability[BetEnum.SUM_17.ordinal() + (dealer.getValue() - 17)] += prob;
						this.probabilityInit[dealer.getCard(0).getRank().ordinal()][BetEnum.SUM_17.ordinal() + (dealer.getValue() - 17)] += prob;
					}
				} else {
					this.probability[BetEnum.BUST.ordinal()] += prob;
					this.probabilityInit[dealer.getCard(0).getRank().ordinal()][BetEnum.BUST.ordinal()] += prob;
				}
			}
			dealer.removeCard(c);
			++cards[i];
		}
	}

	public static void main(String[] args) {
		SimDealerRun game = new SimDealerRun(new DealerS17Rule());
		game.simDistribution(new BlackJackHand(), 1);
		for(int i = 0; i < CardRankEnum.values().length; ++i) {
			System.out.print("\t" + CardRankEnum.values()[i].toString());
		}
		System.out.println("\tTotal");
		for(int i = 0; i < BetEnum.values().length; ++i) {
			System.out.print(BetEnum.values()[i].toString());
			for(int j = 0; j < CardRankEnum.values().length; ++j) {
				System.out.printf("\t%.4f", game.probabilityInit[j][i] / DoubleStream.of(game.probabilityInit[i]).sum());
			}
			System.out.printf("\t%.4f\n", game.probability[i]);
		}
	}
}