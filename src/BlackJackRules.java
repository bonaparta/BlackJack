import java.util.ArrayList;

public class BlackJackRules {
	public static boolean doseLoseAllSplitsWhenDealerBJ = false;

	public static double judge(BlackJackHand dealer, ArrayList<BlackJackHand> player) {
		double sum = 0;
		for(int i = 0; i < player.size(); ++i) {
			sum += judge(dealer, player.get(i));
		}
		return sum;
	}

	public static double judge(BlackJackHand dealer, BlackJackHand player) {
		if(player.isBusted()) {
			player.setWin(-player.getBet());
		} else if(player.isSurrendered()) {
			player.setWin(-0.5 * player.getBet());
		} else if(dealer.isBlackJack()) {
			if(player.isBlackJack())
				player.setWin(0);
			else if(!doseLoseAllSplitsWhenDealerBJ && player.getSerial() != 0)
				player.setWin(0);
			else
				player.setWin(-player.getBet());
		} else if(player.isBlackJack()) {
			player.setWin(1.5 * player.getBet());
		} else if(dealer.isBusted() || dealer.getValue() < player.getValue()) {
			player.setWin(player.getBet());
		} else if(dealer.getValue() > player.getValue()) {
			player.setWin(-player.getBet());
		} else {
			player.setWin(0);
		}
		return player.getWin();
	}

	public static void split(ArrayList<BlackJackHand> player, int index) {
		int handNumber = player.get(index).getSerial();
		handNumber = (handNumber == 0)? 2 : ++handNumber;
		BlackJackHand hand = new BlackJackHand();
		hand.addCard(player.get(index).getCard(1));
		player.get(index).removeCard(1);
		player.add(index + 1, hand);
		for(int i = 0; i < player.size(); ++i)
			player.get(i).setSerial(handNumber);
	}
}