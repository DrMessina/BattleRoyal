package application;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.concurrent.TimeUnit;

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
		Runnable champion1 =new Runnable() {
			public void run() {
				try {
					ArrayList<String> toAttack = new ArrayList<>();
					for (int i=0; i<10; i++) {	if(teamB.get(i).getPv() > 0) {	toAttack.add(String.valueOf(i));	System.out.println("in :" +i);} }
					if(toAttack.size()>0) {
						if(teamA.get(0).getType()=="Prêtre" && teamAToHeal.size()>0) {
							int chooseAttOrHeal = intgenerator.nextInt(2);
							if(chooseAttOrHeal==1) {
								Collections.shuffle(toAttack);
								attack(teamA.get(0), teamB.get(Integer.parseInt(toAttack.get(0))), teamBToHeal,teamB, teamBTimers, 0);
							}else{
								heal(teamA.get(0), teamAToHeal);
							}
						}else {
							Collections.shuffle(toAttack);
							attack(teamA.get(0), teamB.get(Integer.parseInt(toAttack.get(0))), teamBToHeal,teamB, teamBTimers, 0);
						}	
					}else {
						System.out.println("no champ to attack");
					}
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		teamATimers.add(timer);
		this.teamATimers.get(0).scheduleAtFixedRate(champion1, 0, 1000/teamA.get(0).getInitiative(), TimeUnit.MILLISECONDS);
		
	}
}
