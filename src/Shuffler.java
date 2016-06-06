import java.util.Random;

public class Shuffler extends Shoe {
	private Random random;

	public Shuffler() {
		this(new Random());
	}

	public <T extends Card> Shuffler(Class<T> c) {
		this(c, new Random());
	}

	public Shuffler(int decks) {
		this(new Random(), decks);
	}

	public <T extends Card> Shuffler(Class<T> c, int decks) {
		this(c, new Random(), decks);
	}

	public Shuffler(Random random) {
		this(random, 1);
	}

	public <T extends Card> Shuffler(Class<T> c, Random random) {
		this(c, random, 1);
	}

	public Shuffler(Random random, int decks) {
		this(Card.class, random, decks);
	}

	public <T extends Card> Shuffler(Class<T> c, Random random, int decks) {
		super(c, decks);
		this.random = random;
	}

	@Override
	public Card pop() {
		--count;
		return cards.remove(random.nextInt(count()));
	}
}