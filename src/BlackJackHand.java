import java.util.List;
import java.util.ArrayList;

public class BlackJackHand extends Hand {
	private boolean isAce11 = false;
	private boolean isSurrendered = false;
	private int serial; // 0 for blackjack 2, 3, 4... for # hands after split
	private double bet;
	private double odds;
	public BlackJackHand() {
		this(0);
	}

	public BlackJackHand(int number) {
		this.serial = number;
		this.bet = 1;
		this.odds = 0;
	}

	public int getValue() {
		int val = 0;      // The value computed for the hand.
		boolean isAceExist = false;
		for ( int i = 0;  i < getCardCount();  i++ ) {
			int cardValue = getCard(i).getValue();
			if(cardValue == 1)
				isAceExist = true;
			val += cardValue;    // The i-th card;
		}
		this.isAce11 = false;
		if(isAceExist) {
			if(val + 10 <= 21) {
				val += 10;
				this.isAce11 = true;
			}
		}
		return val;
	}

	@Override
	public void addCard(Card c) {
		super.addCard(c);
		getValue();
	}

	@Override
	public void removeCard(Card c) {
		super.removeCard(c);
		getValue();
		this.isSurrendered = false;
		this.bet = 1;
	}

	@Override
	public void removeCard(int position) {
		super.removeCard(position);
		getValue();
		this.isSurrendered = false;
		this.bet = 1;
	}

	public boolean isSoft() {
		return this.isAce11 ? true : false;
	}

	public boolean is2Cards() {
		return (getCardCount() == 2)? true : false;
	}

	public boolean isSplitable() {
		return (is2Cards() && getCard(0).getValue() == getCard(1).getValue())? true : false;
	}

	public boolean isBlackJack() {
		return (getValue() == 21 && is2Cards() && getSerial() == 0)? true : false;
	}

	public int getSerial() {
		return this.serial;
	}

	public void setSerial(int serial) { // split use. mean no more blackjack 1.5
		this.serial = serial;
	}

	public void setWin(double win) {
		this.odds = win;
	}

	public double getWin() {
		return this.odds;
	}

	public void setBet(double bet) {
		this.bet = bet;
	}

	public double getBet() {
		return this.bet;
	}

	public void setSurrender(boolean yesNo) {
		this.isSurrendered = yesNo;
	}

	public boolean isSurrendered() {
		return this.isSurrendered;
	}

	public boolean isBusted() {
		return (getValue() > 21) ? true : false;
	}

	public boolean isDoubled() {
		return (this.bet > 1)? true : false;
	}

}