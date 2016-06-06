public class SimBlackJackRun {
	public static void main(String[] args) {
		int n = (args.length <= 0) ? 5 : Integer.parseInt(args[0]);
		double win = 0;
		double bet = 0;
		for(int i = 0; i < n; ++i) {
			BlackJack game = new BlackJack(new DealerS17Rule());
			win += game.deal();

			BlackJackRecord record = game.getResult();
//			System.out.println("Player: ");
			for(int k = 0; k < record.player.size(); ++k) {
				bet += record.player.get(k).getBet();
/* 				System.out.println("Hand: " + k + " Player: " + record.player.get(k).getValue() + " Dealer: " + record.dealer.getValue() + " Bet: " + record.player.get(k).getBet() + " Win: " + record.player.get(k).getWin());
				for(int j = 0; j < record.player.get(k).getCardCount(); ++j) {
					System.out.println(record.player.get(k).getCard(j));
				} */
			}
/* 			System.out.println("Dealer: " + record.dealer.getValue());
			for(int k = 0; k < record.dealer.getCardCount(); ++k) {
				System.out.println(record.dealer.getCard(k));
			} */
		}
		System.out.printf("RTP: " + win / bet + " bet " + bet + " in " + n);
	}
}