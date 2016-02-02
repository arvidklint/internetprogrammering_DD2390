public class User {

	private String id;
	private int guesses;
	private int randomNumber;
	private int lower;
	private int higher;
	private boolean correct;

	public User() {
		this.guesses = 0;
		this.lower = 0;
		this.higher = 100;
		this.correct = false;
	}

	public void setCorrect(boolean _correct) {
		this.correct = true;
	}

	public boolean getCorrect() {
		return this.correct;
	}

	public void setRandomNumber (int _number) {
		this.randomNumber = _number;
	}

	public int getRandomNumber() {
		return this.randomNumber;
	}

	public void setId(String _id) {
		this.id = _id;
	}

	public String getId() {
		return this.id;
	}

	public void addGuess() {
		this.guesses++;
	}

	public int getGuesses() {
		return this.guesses;
	}

	public void setHigher(int _higher) {
		this.higher = _higher;
	}

	public int getHigher() {
		return this.higher;
	}

	public void setLower(int _lower) {
		this.lower = _lower;
	}

	public int getLower() {
		return this.lower;
	}
}