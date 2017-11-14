package application;

import java.util.Hashtable;

public class Team {
	Hashtable<String, Champion> combattants = new Hashtable<>();
	
	public Team(Hashtable<String, Champion> combattants) {
		this.combattants = combattants;
	}
	public void delete(String nom) {
		combattants.remove(nom);
	}
	public int checkTeam() {
		return combattants.size();
	}
}
