public class User {
	private int guesses;
	private String id;
	private int higher;
	private int lower;
	private int randomNumber;
	private boolean winner;

	User(String _id, int _rn) {
		this.id = _id;
		this.guesses = 0;
		this.lower = 0;
		this.higher = 100;
		this.randomNumber = _rn;
		this.winner = false;
	}

	public int getRandomNumber() {
		return randomNumber;
	}

	public void addGuess() {
		this.guesses++;
	}

	public int getGuesses() {
		return this.guesses;
	}

	public String getID() {
		return this.id;
	}

	public void setLower(int _n) {
		this.lower = _n;
	}

	public int getLower() {
		return this.lower;
	}

	public void setHigher(int _n) {
		this.higher = _n;
	}

	public int getHigher() {
		return this.higher;
	}

	public void setWinner(boolean _b) {
		this.winner = _b;
	}

	public boolean getWinner() {
		return this.winner;
	}
}