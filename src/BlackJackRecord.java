import java.util.ArrayList;

public class BlackJackRecord {
	public ArrayList<BlackJackHand> player;
	public BlackJackHand dealer;

	public BlackJackRecord(BlackJackHand dealer, ArrayList<BlackJackHand> player) {
		this.dealer = dealer;
		this.player = player;
	}
}