package application;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

public class BattleRoyaleController2 {	
	@FXML
	Label afficheur;
	@FXML
	ListView<String> list = new ListView<>();
	private ObservableList<String> data = FXCollections.observableArrayList();
	ArrayList<Champion> teamA = new ArrayList<>();
	//Hashtable<String, Champion> teamAToHeal = new Hashtable<>();
	ArrayList<Champion> teamAToHeal = new ArrayList<>();
	ArrayList<Champion> teamB = new ArrayList<>();
	//Hashtable<String, Champion> teamBToHeal = new Hashtable<>();
	ArrayList<Champion> teamBToHeal = new ArrayList<>();
	ArrayList<Boolean> percent = new ArrayList<>();
	ArrayList<ScheduledExecutorService> teamATimers = new ArrayList<>();
	ArrayList<ScheduledExecutorService> teamBTimers = new ArrayList<>();
	int x;
	private ScheduledExecutorService timer1;

	
	public void init() {
		list.setItems(data);
		list.setPrefWidth(400);
		Random intgenerator = new Random();
		/**	
		 * create first team
		 */
		createTeam(teamA);
		for(int i=0; i<10; i++) {
			data.add(teamA.get(i).getNom() + " TeamA");
		}
		/**
		 * create second team
		 */
		createTeam(teamB);
		for(int i=0; i<10; i++) {
			data.add(teamB.get(i).getNom() + " TeamB");
		}
		 
		/**
		 * creation des threads
		 */
		ScheduledExecutorService timer = Executors.newSingleThreadScheduledExecutor();
		for(x=0; x<10;x++) {
			Runnable champion1 =new Runnable() {
				public void run() {
					try {
						System.out.println(teamA.get(x).getNom());
						if(teamA.get(x).getType()=="Prêtre" && teamAToHeal.size()>0) {
							int chooseAttOrHeal = intgenerator.nextInt(2);
							if(chooseAttOrHeal==1) {
								int selectChamp = intgenerator.nextInt(9);
								attack(teamA.get(x), teamB.get(selectChamp), teamBToHeal,teamB, teamBTimers, x);
							}else{
								heal(teamA.get(x), teamAToHeal);
							}
						}else {
							int selectChamp = intgenerator.nextInt(9);
							attack(teamA.get(x), teamB.get(selectChamp), teamBToHeal,teamB, teamBTimers, x);
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			};
			teamATimers.add(timer);
			this.teamATimers.get(x).scheduleAtFixedRate(champion1, 0, 1000/teamA.get(x).getInitiative(), TimeUnit.MILLISECONDS);
			Runnable champion2 =new Runnable() {
				public void run() {
					System.out.println(teamB.get(x).getNom());
					if(teamB.get(x).getType()=="Prêtre" && teamBToHeal.size()>0) {
						int chooseAttOrHeal = intgenerator.nextInt(2);
						if(chooseAttOrHeal==1) {
							int selectChamp = intgenerator.nextInt(9);
							attack(teamB.get(x), teamA.get(selectChamp), teamAToHeal,teamA, teamATimers, selectChamp);
						}else{
							heal(teamB.get(x), teamBToHeal);
						}
					}else {
						int selectChamp = intgenerator.nextInt(9);
						attack(teamB.get(x), teamA.get(selectChamp), teamAToHeal,teamA, teamATimers, selectChamp);
					}
				}
			};
			teamBTimers.add(timer);
			this.teamBTimers.get(x).scheduleAtFixedRate(champion2, 0, 1000/teamB.get(x).getInitiative(), TimeUnit.MILLISECONDS);
		}
		
		/*Runnable check =new Runnable() {
			public void run() {
				if(true) {
					try {
						timer.shutdown();
						timer.awaitTermination(33, TimeUnit.MILLISECONDS);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		};
		this.timer1 = Executors.newSingleThreadScheduledExecutor();
		this.timer1.scheduleAtFixedRate(check, 0, 100, TimeUnit.MILLISECONDS);
		*/
	}
	public void createTeam(	ArrayList<Champion> team) {
		ArrayList<String> t= new ArrayList<>();
		t.add("Guerrier");
		t.add("Voleur");
		t.add("Mage");
		t.add("Prêtre");
		for(int i=0; i<10;i++) {
			Collections.shuffle(t);
			if(t.get(0)=="Guerrier") {
				Guerrier g = new Guerrier("Guerrier"+i);
				Champion c = new Champion(g);
				team.add(c);
				
			}else if(t.get(0)=="Voleur") {
				Voleur v = new Voleur("voleur"+i);
				Champion c = new Champion(v);
				team.add(c);
				
			}else if(t.get(0)=="Mage") {
				Mage m = new Mage("Mage"+i);
				Champion c = new Champion(m);
				team.add(c);
				
			}else if(t.get(0)=="Prêtre") {
				Prêtre p = new Prêtre("Prêtre"+i);
				Champion c = new Champion(p);
				team.add(c);
				
			}else {
				System.out.println("erreur de creation de champion!");
			}
			
		}
	}
	public void setListViewWidth(Number newSceneWidth) {
		list.setPrefWidth(newSceneWidth.intValue());
	}
	public int getRandomInt(int max) {
		Random intgenerator = new Random();
		int intgenerated = intgenerator.nextInt(max);
		return intgenerated;
	}
	public void attack(Champion attacker,Champion attacked,/*Hashtable<String, Champion> toHeal*/ArrayList<Champion> toHeal, ArrayList<Champion> teamAttacked, ArrayList<ScheduledExecutorService> teamAttackedTimer, int champselect) {
		
		if(attacked.getType() == "Guerrier" || attacked.getType() == "Prêtre") {
			if(parade(attacked) == true)
			{
				data.add(attacked.getNom() + " ne reçoit pas de degats.");
			}else {
				if(criticalHit(attacker)==true) 
				{
					int pvOfAttacked = attacked.getPv();
					pvOfAttacked = pvOfAttacked - attacker.getAtt();
					attacked.setPv(pvOfAttacked);
					data.add("Coup critique! " + attacker.getNom() + "inflige des degats perçants à " + attacked.getNom() +" d'une valeur de " + attacker.getAtt()+"." );
				}else{
					int pvOfAttacked = attacked.getPv();
					System.out.println(pvOfAttacked+" pv "+ attacked.getNom());
					if(attacked.getDef() < attacker.getAtt()) 
					{
						int damage = attacker.getAtt() - attacked.getDef();
						pvOfAttacked = pvOfAttacked-damage;
						attacked.setPv(pvOfAttacked);
						// change le msg pour qu'il soit plus parlant
						data.add(attacker.getNom()+" attaque avec "+attacker.getAtt()+" pt dattaque, "+attacked.getNom()+" qui a "+attacked.getDef()+" defense et "+attacked.getPv()+" : il reçoit donc "+damage+" degat.");
						System.out.println("pv new value "+attacked.getPv());
						/*try {
							if(!(toHeal.containsKey(attacked.getNom())) ) {	toHeal.put(attacked.getNom(), attacked);}
						}catch (Exception e) {toHeal.put(attacked.getNom(), attacked);}*/
						int i;
						//add in to heal  team table
						if(attacked.getPv() > 0) {
							for(i=0; i<toHeal.size() && toHeal.get(i) != attacked;i++) {}
							if (i<toHeal.size()) {	toHeal.add(attacked);}
						}else {
							toHeal.remove(attacked);
							teamAttacked.remove(attacked);
							teamAttackedTimer.get(champselect).shutdown();
							try {	teamAttackedTimer.get(champselect).awaitTermination(33, TimeUnit.MILLISECONDS);} catch (InterruptedException e) {	e.printStackTrace();}
						}
					}else{
						data.add(attacked.getNom() + " ne reçoit pas de degats.");
					}
				}
			}
			
		}else if(attacked.getType() == "Voleur") {
			if(esquive(attacked) == true)
			{
				data.add(attacked.getNom() + " ne reçoit pas de degats.");
			}else {
				if(criticalHit(attacker)==true) 
				{
					int pvOfAttacked = attacked.getPv();
					pvOfAttacked = pvOfAttacked - attacker.getAtt();
					attacked.setPv(pvOfAttacked);
					data.add("Coup critique! " + attacker.getNom() + "inflige des degats perçants à " + attacked.getNom() +" d'une valeur de " + attacker.getAtt()+"." );
				}else{
					int pvOfAttacked = attacked.getPv();
					System.out.println(pvOfAttacked+" pv "+ attacked.getNom());
					if(attacked.getDef() < attacker.getAtt()) 
					{
						int damage = attacker.getAtt() - attacked.getDef();
						pvOfAttacked = pvOfAttacked-damage;
						attacked.setPv(pvOfAttacked);
						// change le msg pour qu'il soit plus parlant
						data.add(attacker.getNom()+" attaque avec "+attacker.getAtt()+" pt dattaque, "+attacked.getNom()+" qui a "+attacked.getDef()+" defense et "+attacked.getPv()+" : il reçoit donc "+damage+" degat.");
						System.out.println("pv new value "+attacked.getPv());
						/*try {
							if(!(toHeal.containsKey(attacked.getNom())) ) {	toHeal.put(attacked.getNom(), attacked);}
						}catch (Exception e) {toHeal.put(attacked.getNom(), attacked);}*/
						int i;
						//add in to heal  team table
						if(attacked.getPv() > 0) {
							for(i=0; i<toHeal.size() && toHeal.get(i) != attacked;i++) {}
							if (i<toHeal.size()) {	toHeal.add(attacked);}
						}else {
							toHeal.remove(attacked);
							teamAttacked.remove(attacked);
							teamAttackedTimer.get(champselect).shutdown();
							try {	teamAttackedTimer.get(champselect).awaitTermination(33, TimeUnit.MILLISECONDS);} catch (InterruptedException e) {	e.printStackTrace();}
						}
					}else{
						data.add(attacked.getNom() + " ne reçoit pas de degats.");
					}
				}
			}			
		}else if(attacked.getType() == "Mage") {
			//
			if(criticalHit(attacker)==true) 
			{
				int pvOfAttacked = attacked.getPv();
				pvOfAttacked = pvOfAttacked - attacker.getAtt();
				attacked.setPv(pvOfAttacked);
				data.add("Coup critique! " + attacker.getNom() + "inflige des degats perçants à " + attacked.getNom() +" d'une valeur de " + attacker.getAtt()+"." );
			}else{
				int pvOfAttacked = attacked.getPv();
				System.out.println(pvOfAttacked+" pv "+ attacked.getNom());
				if(attacked.getDef() < attacker.getAtt()) 
				{
					int damage = attacker.getAtt() - attacked.getDef();
					pvOfAttacked = pvOfAttacked-damage;
					attacked.setPv(pvOfAttacked);
					// change le msg pour qu'il soit plus parlant
					data.add(attacker.getNom()+" attaque avec "+attacker.getAtt()+" pt dattaque, "+attacked.getNom()+" qui a "+attacked.getDef()+" defense et "+attacked.getPv()+" : il reçoit donc "+damage+" degat.");
					System.out.println("pv new value "+attacked.getPv());
					/*try {
						if(!(toHeal.containsKey(attacked.getNom())) ) {	toHeal.put(attacked.getNom(), attacked);}
					}catch (Exception e) {toHeal.put(attacked.getNom(), attacked);}*/
					int i;
					//add in to heal  team table
					if(attacked.getPv() > 0) {
						for(i=0; i<toHeal.size() && toHeal.get(i) != attacked;i++) {}
						if (i<toHeal.size()) {	toHeal.add(attacked);}
					}else {
						toHeal.remove(attacked);
						teamAttacked.remove(attacked);
						teamAttackedTimer.get(champselect).shutdown();
						try {	teamAttackedTimer.get(champselect).awaitTermination(33, TimeUnit.MILLISECONDS);} catch (InterruptedException e) {	e.printStackTrace();}
					}
				}else{
					data.add(attacked.getNom() + " ne reçoit pas de degats.");
				}
			}
		}else{
			System.out.println("error in champion type");
		}
	}
	public void heal(Champion healer, ArrayList<Champion> ToHeal) {
		int x = getRandomInt(ToHeal.size());
		ToHeal.get(x).setPv(ToHeal.get(x).getPv()+healer.getHeal());

		data.add(healer.getNom()+ " ajoute "+healer.getHeal()+" pv à "+ToHeal.get(x).getPv()+" de "+ ToHeal.get(x).getNom());
		data.add(healer.getHeal()+" mont pv ajouté");
		ToHeal.get(x).setPv(ToHeal.get(x).getPv()+healer.getHeal());
		data.add("new pv value : "+ToHeal.get(x).getPv());
	}
	public boolean criticalHit(Champion tryHarder) {
		int percentToTest = tryHarder.getCrit();
		for(int i=0; i<100; i++) {
			if(i<percentToTest){
				percent.add(true);
			}else {
				percent.add(false);
			}
		}
		Collections.shuffle(percent);
		Random intgenerator = new Random();
		int toTest = intgenerator.nextInt(100);
		Boolean toReturn = percent.get(toTest);
		return toReturn;
	}
	public Boolean parade(Champion theTank) {
		int percentToTest = theTank.getParade();
		for(int i=0; i<100; i++) {
			if(i<percentToTest){
				percent.add(true);
			}else {
				percent.add(false);
			}
		}
		Collections.shuffle(percent);
		Random intgenerator = new Random();
		int toTest = intgenerator.nextInt(100);
		Boolean toReturn = percent.get(toTest);
		return toReturn;
	}
	public Boolean esquive(Champion speedy) {
		int percentToTest = speedy.getEsquive();
		for(int i=0; i<100; i++) {
			if(i<percentToTest){
				percent.add(true);
			}else {
				percent.add(false);
			}
		}
		Collections.shuffle(percent);
		Random intgenerator = new Random();
		int toTest = intgenerator.nextInt(100);
		Boolean toReturn = percent.get(toTest);
		return toReturn;
	}
	public void close() {
		try {
			if(teamATimers != null && teamATimers.size() > 0 && teamBTimers != null && teamBTimers.size() > 0) {
				for(int i =0; i<10; i++) {
					teamATimers.get(i).shutdown();
					teamATimers.get(i).awaitTermination(33, TimeUnit.MILLISECONDS);
					teamBTimers.get(i).shutdown();
					teamBTimers.get(i).awaitTermination(33, TimeUnit.MILLISECONDS);
				}
				
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	
}
