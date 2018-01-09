package application;

import java.util.ArrayList;

public class Historique {
	private String winnerId, looserId;
	int winnerAlive;
	int looserAlive;

	

	public Historique( String winnerId, String looserId, int winnerAlive, int looseralive) {
		super();
		this.winnerId = winnerId;
		this.looserId = looserId;
		this.winnerAlive = winnerAlive;
		this.looserAlive = looseralive;
	}
	public int getWinnerAlive() {
		return winnerAlive;
	}
	public void setWinnerAlive(int winnerAlive) {
		this.winnerAlive = winnerAlive;
	}
	public int getLooserAlive() {
		return looserAlive;
	}
	public void setLooserAlive(int looserAlive) {
		this.looserAlive = looserAlive;
	}
	public String getWinnerId() {
		return winnerId;
	}

	public void setWinnerId(String winnerId) {
		this.winnerId = winnerId;
	}

	public String getLooserId() {
		return looserId;
	}

	public void setLooserId(String looserId) {
		this.looserId = looserId;
	}
}
