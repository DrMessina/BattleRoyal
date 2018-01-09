package application;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.sun.media.jfxmediaimpl.platform.Platform;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class BattleRoyaleController {	
	@FXML
	private HBox HBox;
	@FXML
	private ListView<String> list = new ListView<>();
	private ListView<String> list1 = new ListView<>();
	private ListView<String> list2 = new ListView<>();
	private ListView<String> list3 = new ListView<>();


	private ArrayList<Historique> historique = new ArrayList<>();
	private ObservableList<String> data = FXCollections.observableArrayList();
	private ArrayList<Champion> teamA = new ArrayList<>();
	//Hashtable<String, Champion> teamAToHeal = new Hashtable<>();
	private ArrayList<Champion> teamAToHeal = new ArrayList<>();
	private ArrayList<Champion> teamB = new ArrayList<>();
	//Hashtable<String, Champion> teamBToHeal = new Hashtable<>();
	private ArrayList<Champion> teamBToHeal = new ArrayList<>();
	private ArrayList<Boolean> percent = new ArrayList<>();
	private ArrayList<ScheduledExecutorService> teamATimers = new ArrayList<>();
	private ArrayList<ScheduledExecutorService> teamBTimers = new ArrayList<>();
	private ScheduledExecutorService timer1;
	private Connection connection = null;
	private int teamAId;
	private int teamBId;
	private int combatId;
	
	public void init() {
		connection = connecter("battleroyale", "root", "Root");
		fight();
		Runnable historiqueLoad =new Runnable() {
			@Override
			public void run() {
				try {
					String sql = "select * from historique";
					Statement st;
					st = connection.createStatement();
					ResultSet rs = st.executeQuery(sql);
					while(rs.next()) {
						System.out.println(rs.getInt("WinnerSurvivor")+" "+ rs.getInt("LooserSurvivor")+" "+ rs.getInt("CombatId")+rs.getString("Winner")+rs.getString("Looser"));
						Historique h = new Historique(rs.getString("Winner"), rs.getString("Looser"), rs.getInt("WinnerSurvivor"), rs.getInt("LooserSurvivor"));
						BattleRoyaleController.this.historique.add(h);
					}

				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		};
		historiqueLoad.run();
	}
	//cree sql pour combat dans fight
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
		if (list != null) {
			list.setPrefWidth(newSceneWidth.intValue());
		}
		if (list3 != null) {
			list3.setPrefWidth(newSceneWidth.intValue());
		}
		if (list1 != null) {
			list1.setPrefWidth(newSceneWidth.intValue() / 2);
		}
		if (list2 != null) {
			list2.setPrefWidth(newSceneWidth.intValue() / 2);
		}
	}
	public int getRandomInt(int max) {
		Random intgenerator = new Random();
		int intgenerated = intgenerator.nextInt(max);
		return intgenerated;
	}
	public void attack(Champion attacker,Champion attacked, ArrayList<Champion> toHeal) {
		
		if(attacked.getType() == "Guerrier" || attacked.getType() == "Prêtre") {
			if(parade(attacked) == true)
			{
				try {
					data.add(attacked.getNom() + " ne reçoit pas de degats.");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else {
				if(criticalHit(attacker)==true) 
				{
					int pvOfAttacked = attacked.getPv();
					pvOfAttacked = pvOfAttacked - attacker.getAtt();
					attacked.setPv(pvOfAttacked);
					System.out.println("pv new value "+attacked.getPv());
					data.add("Coup critique! " + attacker.getNom() + "inflige des degats perçants à " + attacked.getNom() +" d'une valeur de " + attacker.getAtt()+"." );
					//add or remove in/from to heal  team table
					if(attacked.getPv() > 0) {
						int i;
						if(toHeal.size()==0) {
							toHeal.add(attacked);
						}else {
							for(i=0; i<toHeal.size() && toHeal.get(i) != attacked;i++) {}
							if (i>=toHeal.size()) {	toHeal.add(attacked);}
						}
					}
				}else{
					int pvOfAttacked = attacked.getPv();
					if(attacked.getDef() < attacker.getAtt()) 
					{
						int damage = attacker.getAtt() - attacked.getDef();
						pvOfAttacked = pvOfAttacked-damage;
						attacked.setPv(pvOfAttacked);
						System.out.println("pv new value "+attacked.getPv());
						try {
							data.add(attacker.getNom()+" attaque avec "+attacker.getAtt()+" pt dattaque, "+attacked.getNom()+" qui a "+attacked.getDef()+" defense et "+attacked.getPv()+" pv : il reçoit donc "+damage+" degat.");
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						/*try {
							if(!(toHeal.containsKey(attacked.getNom())) ) {	toHeal.put(attacked.getNom(), attacked);}
						}catch (Exception e) {toHeal.put(attacked.getNom(), attacked);}*/
						//add or remove in/from to heal  team table
						if(attacked.getPv() > 0) {
							int i;
							if(toHeal.size()==0) {
								toHeal.add(attacked);
							}else {
								for(i=0; i<toHeal.size() && toHeal.get(i) != attacked;i++) {}
								if (i>=toHeal.size()) {	toHeal.add(attacked);}
							}
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
					System.out.println("pv new value "+attacked.getPv());
					data.add("Coup critique! " + attacker.getNom() + "inflige des degats perçants à " + attacked.getNom() +" d'une valeur de " + attacker.getAtt()+"." );
					//add or remove in/from to heal  team table
					if(attacked.getPv() > 0) {
						int i;
						if(toHeal.size()==0) {
							toHeal.add(attacked);
						}else {
							for(i=0; i<toHeal.size() && toHeal.get(i) != attacked;i++) {}
							if (i>=toHeal.size()) {	toHeal.add(attacked);}
						}
					}
				}else{
					int pvOfAttacked = attacked.getPv();
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
						
						//add or remove in/from to heal  team table
						if(attacked.getPv() > 0) {
							int i;
							if(toHeal.size()==0) {
								toHeal.add(attacked);
							}else {
								for(i=0; i<toHeal.size() && toHeal.get(i) != attacked;i++) {}
								if (i>=toHeal.size()) {	toHeal.add(attacked);}
							}
						}
					}else{
						data.add(attacked.getNom() + " ne reçoit pas de degats.");
					}
				}
			}			
		}else if(attacked.getType() == "Mage") {
			if(criticalHit(attacker)==true) 
			{
				int pvOfAttacked = attacked.getPv();
				pvOfAttacked = pvOfAttacked - attacker.getAtt();
				attacked.setPv(pvOfAttacked);
				System.out.println("pv new value "+attacked.getPv());
				data.add("Coup critique! " + attacker.getNom() + "inflige des degats perçants à " + attacked.getNom() +" d'une valeur de " + attacker.getAtt()+"." );
				//add or remove in/from to heal  team table
				if(attacked.getPv() > 0) {
					int i;
					if(toHeal.size()==0) {
						toHeal.add(attacked);
					}else {
						for(i=0; i<toHeal.size() && toHeal.get(i) != attacked;i++) {}
						if (i>=toHeal.size()) {	toHeal.add(attacked);}
					}
				}
			}else{
				int pvOfAttacked = attacked.getPv();
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
					//add or remove in/from to heal  team table
					if(attacked.getPv() > 0) {
						int i;
						if(toHeal.size()==0) {
							toHeal.add(attacked);
						}else {
							for(i=0; i<toHeal.size() && toHeal.get(i) != attacked;i++) {}
							if (i>=toHeal.size()) {	toHeal.add(attacked);}
						}
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
		ArrayList<String> indexToHeal = new ArrayList<>();
		for (int i=0; i<ToHeal.size(); i++) {	if(ToHeal.get(i).getPv() > 0) {	indexToHeal.add(String.valueOf(i));	System.out.println("in :" +i);} }
		
		if (indexToHeal.size()>0) {
			Collections.shuffle(indexToHeal);
			ToHeal.get(Integer.parseInt(indexToHeal.get(0))).setPv(ToHeal.get(Integer.parseInt(indexToHeal.get(0))).getPv() + healer.getHeal());
			data.add(healer.getNom() + " ajoute " + healer.getHeal() + " pv à " + ToHeal.get(Integer.parseInt(indexToHeal.get(0))).getPv() + " pv de "
					+ ToHeal.get(Integer.parseInt(indexToHeal.get(0))).getNom() + " -> new pv value : " + ToHeal.get(Integer.parseInt(indexToHeal.get(0))).getPv());
		}
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
			System.out.println("start to stop threads");
			if(teamATimers != null && teamATimers.size() > 0 && teamBTimers != null && teamBTimers.size() > 0) {
				for(int i =0; i<teamATimers.size(); i++) {
					teamATimers.get(i).shutdown();
					teamATimers.get(i).awaitTermination(33, TimeUnit.MILLISECONDS);
				}
				for(int i =0; i<teamBTimers.size(); i++) {
					teamBTimers.get(i).shutdown();
					teamBTimers.get(i).awaitTermination(33, TimeUnit.MILLISECONDS);
				}
				timer1.shutdown();
				timer1.awaitTermination(1, TimeUnit.SECONDS);
			}
			System.out.println("threads stopped");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	@FXML
	public void fight() {
		close();
		HBox.getChildren().clear();
		list = new ListView<>();
		data.clear();
		data = FXCollections.observableArrayList();
		list.setItems(data);
		list.setPrefWidth(850);
		list.setMaxWidth(1366);
		HBox.getChildren().add(list);
		teamA.clear();
		teamA = new ArrayList<>();
		teamB.clear();
		teamB = new ArrayList<>();
		teamAToHeal.clear();
		teamAToHeal = new ArrayList<>();
		teamBToHeal.clear();
		teamBToHeal = new ArrayList<>();
		teamATimers.clear();
		teamATimers = new ArrayList<>();
		teamBTimers.clear();
		teamBTimers = new ArrayList<>();
		Random intgenerator = new Random();
		 teamAId=0;
		 teamBId=0;
		 combatId=0;
		/**
		 * enregistrement du combat
		 */
		try {
			String sql1 = "insert into combat values("+ 0 +")";
			Statement st1 = connection.createStatement();
			st1.executeUpdate(sql1);
			String sql = "select Max(CombatId) from combat";
			Statement st = connection.createStatement();
			ResultSet rs = st.executeQuery(sql);
			if (rs.next()) {
				combatId = rs.getInt("Max(CombatId)");
			}
		} catch (SQLException e1) {	e1.printStackTrace();	}
		/**	
		 * create first team
		 */
		try {
			String sql = "select Max(TeamId) from equipe";
			Statement st = connection.createStatement();
			ResultSet rs = st.executeQuery(sql);
			if (rs.next()) {
				teamAId = rs.getInt("Max(TeamId)") + 1;
				createTeam(teamA);
				String sql1 = "insert into equipe values(0,'teamA"+teamAId+"',"+ combatId +")";
				Statement st1 = connection.createStatement();
				st1.executeUpdate(sql1);
			}			
		} catch (SQLException e1) {
			e1.printStackTrace();
		}		/**
		 * create second team
		 */
		try {
			String sql = "select Max(TeamId) from equipe";
			Statement st = connection.createStatement();
			ResultSet rs = st.executeQuery(sql);
			if (rs.next()) {
				teamBId = rs.getInt("Max(TeamId)") + 1;
				createTeam(teamB);
				String sql1 = "insert into equipe values(0,'teamB"+teamBId+"',"+ combatId +")";
				Statement st1 = connection.createStatement();
				st1.executeUpdate(sql1);
			}			
		} catch (SQLException e1) {
			e1.printStackTrace();
		}	
		
		/**
		 * creation des threads
		 */
		ScheduledExecutorService timer = Executors.newSingleThreadScheduledExecutor();
			
		Runnable champion1 =new Runnable() {
			public void run() {
				try {
					if (teamA.get(0).getPv() > 0) {
						ArrayList<String> toAttack = new ArrayList<>();
						for (int i = 0; i < 10; i++) {
							if (teamB.get(i).getPv() > 0) {
								toAttack.add(String.valueOf(i));
								System.out.println("in :" + i);
							}
						}
						if (toAttack.size() > 0) {
							if (teamA.get(0).getType() == "Prêtre" && teamAToHeal.size() > 0) {
								int chooseAttOrHeal = intgenerator.nextInt(2);
								if (chooseAttOrHeal == 1) {
									Collections.shuffle(toAttack);
									attack(teamA.get(0), teamB.get(Integer.parseInt(toAttack.get(0))), teamBToHeal);
								} else {
									heal(teamA.get(0), teamAToHeal);
								}
							} else {
								Collections.shuffle(toAttack);
								attack(teamA.get(0), teamB.get(Integer.parseInt(toAttack.get(0))), teamBToHeal);
							}
						} else {
							System.out.println("no champ to attack");
						} 
					}else {
						System.out.println("cant attack! no more pv.");
					}
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		teamATimers.add(timer);
		this.teamATimers.get(0).scheduleAtFixedRate(champion1, 0, 1000/teamA.get(0).getInitiative(), TimeUnit.MILLISECONDS);
		
		Runnable champion2 =new Runnable() {
			public void run() {
				try {
					if (teamB.get(0).getPv() > 0) {
						ArrayList<String> toAttack = new ArrayList<>();
						for (int i = 0; i < 10; i++) {
							if (teamA.get(i).getPv() > 0) {
								toAttack.add(String.valueOf(i));
							}
						}
						if (toAttack.size() > 0) {
							if (teamB.get(0).getType() == "Prêtre" && teamBToHeal.size() > 0) {
								int chooseAttOrHeal = intgenerator.nextInt(2);
								if (chooseAttOrHeal == 1) {
									Collections.shuffle(toAttack);
									attack(teamB.get(0), teamA.get(Integer.parseInt(toAttack.get(0))), teamAToHeal);
								} else {
									heal(teamB.get(0), teamBToHeal);
								}
							} else {
								Collections.shuffle(toAttack);
								attack(teamB.get(0), teamA.get(Integer.parseInt(toAttack.get(0))), teamAToHeal);
							}
						} else {
							System.out.println("no champ to attack");
						} 
					}else {
						System.out.println("cant attack! no more pv.");
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		teamBTimers.add(timer);
		this.teamBTimers.get(0).scheduleAtFixedRate(champion2, 0, 1000/teamB.get(0).getInitiative(), TimeUnit.MILLISECONDS);
		
		Runnable champion3 =new Runnable() {
			public void run() {
				try {
					if (teamA.get(1).getPv() > 0) {
						ArrayList<String> toAttack = new ArrayList<>();
						for (int i = 0; i < 10; i++) {
							if (teamB.get(i).getPv() > 0) {
								toAttack.add(String.valueOf(i));
							}
						}
						if (toAttack.size() > 0) {
							if (teamA.get(1).getType() == "Prêtre" && teamAToHeal.size() > 0) {
								int chooseAttOrHeal = intgenerator.nextInt(2);
								if (chooseAttOrHeal == 1) {
									Collections.shuffle(toAttack);
									attack(teamA.get(1), teamB.get(Integer.parseInt(toAttack.get(0))), teamBToHeal);
								} else {
									heal(teamA.get(1), teamAToHeal);
								}
							} else {
								Collections.shuffle(toAttack);
								attack(teamA.get(1), teamB.get(Integer.parseInt(toAttack.get(0))), teamBToHeal);
							}
						} else {
							System.out.println("no champ to attack");
						} 
					}else {
						System.out.println("cant attack! no more pv.");
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		teamATimers.add(timer);
		this.teamATimers.get(1).scheduleAtFixedRate(champion3, 0, 1000/teamA.get(1).getInitiative(), TimeUnit.MILLISECONDS);
		Runnable champion4 =new Runnable() {
			public void run() {
				try {
					if (teamB.get(1).getPv() > 0) {
						ArrayList<String> toAttack = new ArrayList<>();
						for (int i = 0; i < 10; i++) {
							if (teamA.get(i).getPv() > 0) {
								toAttack.add(String.valueOf(i));
							}
						}
						if (toAttack.size() > 0) {
							if (teamB.get(1).getType() == "Prêtre" && teamBToHeal.size() > 0) {
								int chooseAttOrHeal = intgenerator.nextInt(2);
								if (chooseAttOrHeal == 1) {
									Collections.shuffle(toAttack);
									attack(teamB.get(1), teamA.get(Integer.parseInt(toAttack.get(0))), teamAToHeal);
								} else {
									heal(teamB.get(1), teamBToHeal);
								}
							} else {
								Collections.shuffle(toAttack);
								attack(teamB.get(1), teamA.get(Integer.parseInt(toAttack.get(0))), teamAToHeal);
							}
						} else {
							System.out.println("no champ to attack");
						} 
					}else {
						System.out.println("cant attack! no more pv.");
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		teamBTimers.add(timer);
		this.teamBTimers.get(1).scheduleAtFixedRate(champion4, 0, 1000/teamB.get(1).getInitiative(), TimeUnit.MILLISECONDS);
		//
		Runnable champion5 =new Runnable() {
			public void run() {
				try {
					if (teamA.get(2).getPv() > 0) {
						ArrayList<String> toAttack = new ArrayList<>();
						for (int i = 0; i < 10; i++) {
							if (teamB.get(i).getPv() > 0) {
								toAttack.add(String.valueOf(i));
							}
						}
						if (toAttack.size() > 0) {
							if (teamA.get(2).getType() == "Prêtre" && teamAToHeal.size() > 0) {
								int chooseAttOrHeal = intgenerator.nextInt(2);
								if (chooseAttOrHeal == 1) {
									Collections.shuffle(toAttack);
									attack(teamA.get(2), teamB.get(Integer.parseInt(toAttack.get(0))), teamBToHeal);
								} else {
									heal(teamA.get(2), teamAToHeal);
								}
							} else {
								Collections.shuffle(toAttack);
								attack(teamA.get(2), teamB.get(Integer.parseInt(toAttack.get(0))), teamBToHeal);
							}
						} else {
							System.out.println("no champ to attack");
						} 
					}else {
						System.out.println("cant attack! no more pv.");
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		teamATimers.add(timer);
		this.teamATimers.get(2).scheduleAtFixedRate(champion5, 0, 1000/teamA.get(2).getInitiative(), TimeUnit.MILLISECONDS);
		Runnable champion6 =new Runnable() {
			public void run() {
				try {
					if (teamA.get(2).getPv() > 0) {
						ArrayList<String> toAttack = new ArrayList<>();
						for (int i = 0; i < 10; i++) {
							if (teamA.get(i).getPv() > 0) {
								toAttack.add(String.valueOf(i));
							}
						}
						if (toAttack.size() > 0) {
							if (teamB.get(2).getType() == "Prêtre" && teamBToHeal.size() > 0) {
								int chooseAttOrHeal = intgenerator.nextInt(2);
								if (chooseAttOrHeal == 1) {
									Collections.shuffle(toAttack);
									attack(teamB.get(2), teamA.get(Integer.parseInt(toAttack.get(0))), teamAToHeal);
								} else {
									heal(teamB.get(2), teamBToHeal);
								}
							} else {
								Collections.shuffle(toAttack);
								attack(teamB.get(2), teamA.get(Integer.parseInt(toAttack.get(0))), teamAToHeal);
							}
						} else {
							System.out.println("no champ to attack");
						} 
					}else {
						System.out.println("cant attack! no more pv.");
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		teamBTimers.add(timer);
		this.teamBTimers.get( 2).scheduleAtFixedRate(champion6, 0, 1000/teamB.get( 2).getInitiative(), TimeUnit.MILLISECONDS);
		//
		Runnable champion7 =new Runnable() {
			public void run() {
				try {
					if (teamA.get(3).getPv() > 0) {
						ArrayList<String> toAttack = new ArrayList<>();
						for (int i = 0; i < 10; i++) {
							if (teamB.get(i).getPv() > 0) {
								toAttack.add(String.valueOf(i));
							}
						}
						if (toAttack.size() > 0) {
							if (teamA.get(3).getType() == "Prêtre" && teamAToHeal.size() > 0) {
								int chooseAttOrHeal = intgenerator.nextInt(2);
								if (chooseAttOrHeal == 1) {
									Collections.shuffle(toAttack);
									attack(teamA.get(3), teamB.get(Integer.parseInt(toAttack.get(0))), teamBToHeal);
								} else {
									heal(teamA.get(3), teamAToHeal);
								}
							} else {
								Collections.shuffle(toAttack);
								attack(teamA.get(3), teamB.get(Integer.parseInt(toAttack.get(0))), teamBToHeal);
							}
						} else {
							System.out.println("no champ to attack");
						} 
					}else {
						System.out.println("cant attack! no more pv.");
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		teamATimers.add(timer);
		this.teamATimers.get( 3).scheduleAtFixedRate(champion7, 0, 1000/teamA.get( 3).getInitiative(), TimeUnit.MILLISECONDS);
		Runnable champion8 =new Runnable() {
			public void run() {
				try {
					if (teamA.get(2).getPv() > 0) {
						ArrayList<String> toAttack = new ArrayList<>();
						for (int i = 0; i < 10; i++) {
							if (teamA.get(i).getPv() > 0) {
								toAttack.add(String.valueOf(i));
							}
						}
						if (toAttack.size() > 0) {
							if (teamB.get(3).getType() == "Prêtre" && teamBToHeal.size() > 0) {
								int chooseAttOrHeal = intgenerator.nextInt(2);
								if (chooseAttOrHeal == 1) {
									Collections.shuffle(toAttack);
									attack(teamB.get(3), teamA.get(Integer.parseInt(toAttack.get(0))), teamAToHeal);
								} else {
									heal(teamB.get(3), teamBToHeal);
								}
							} else {
								Collections.shuffle(toAttack);
								attack(teamB.get(3), teamA.get(Integer.parseInt(toAttack.get(0))), teamAToHeal);
							}
						} else {
							System.out.println("no champ to attack");
						} 
					}else {
						System.out.println("cant attack! no more pv.");
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		teamBTimers.add(timer);
		this.teamBTimers.get( 3).scheduleAtFixedRate(champion8, 0, 1000/teamB.get( 3).getInitiative(), TimeUnit.MILLISECONDS);
		//
		Runnable champion9 =new Runnable() {
			public void run() {
				try {
					if (teamA.get(2).getPv() > 0) {
						ArrayList<String> toAttack = new ArrayList<>();
						for (int i = 0; i < 10; i++) {
							if (teamB.get(i).getPv() > 0) {
								toAttack.add(String.valueOf(i));
							}
						}
						if (toAttack.size() > 0) {
							if (teamA.get(4).getType() == "Prêtre" && teamAToHeal.size() > 0) {
								int chooseAttOrHeal = intgenerator.nextInt(2);
								if (chooseAttOrHeal == 1) {
									Collections.shuffle(toAttack);
									attack(teamA.get(4), teamB.get(Integer.parseInt(toAttack.get(0))), teamBToHeal);
								} else {
									heal(teamA.get(4), teamAToHeal);
								}
							} else {
								Collections.shuffle(toAttack);
								attack(teamA.get(4), teamB.get(Integer.parseInt(toAttack.get(0))), teamBToHeal);
							}
						} else {
							System.out.println("no champ to attack");
						} 
					}else {
						System.out.println("cant attack! no more pv.");
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		teamATimers.add(timer);
		this.teamATimers.get( 4).scheduleAtFixedRate(champion9, 0, 1000/teamA.get( 4).getInitiative(), TimeUnit.MILLISECONDS);
		Runnable champion10 =new Runnable() {
			public void run() {
				try {
					if (teamA.get(2).getPv() > 0) {
						ArrayList<String> toAttack = new ArrayList<>();
						for (int i = 0; i < 10; i++) {
							if (teamA.get(i).getPv() > 0) {
								toAttack.add(String.valueOf(i));
							}
						}
						if (toAttack.size() > 0) {
							if (teamB.get(4).getType() == "Prêtre" && teamBToHeal.size() > 0) {
								int chooseAttOrHeal = intgenerator.nextInt(2);
								if (chooseAttOrHeal == 1) {
									Collections.shuffle(toAttack);
									attack(teamB.get(3), teamA.get(Integer.parseInt(toAttack.get(0))), teamAToHeal);
								} else {
									heal(teamB.get(4), teamBToHeal);
								}
							} else {
								Collections.shuffle(toAttack);
								attack(teamB.get(3), teamA.get(Integer.parseInt(toAttack.get(0))), teamAToHeal);
							}
						} else {
							System.out.println("no champ to attack");
						} 
					}else {
						System.out.println("cant attack! no more pv.");
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		teamBTimers.add(timer);
		this.teamBTimers.get( 4).scheduleAtFixedRate(champion10, 0, 1000/teamB.get( 4).getInitiative(), TimeUnit.MILLISECONDS);
		//
		Runnable champion11 =new Runnable() {
			public void run() {
				try {
					if (teamA.get(2).getPv() > 0) {
						ArrayList<String> toAttack = new ArrayList<>();
						for (int i = 0; i < 10; i++) {
							if (teamB.get(i).getPv() > 0) {
								toAttack.add(String.valueOf(i));
							}
						}
						if (toAttack.size() > 0) {
							if (teamA.get(5).getType() == "Prêtre" && teamAToHeal.size() > 0) {
								int chooseAttOrHeal = intgenerator.nextInt(2);
								if (chooseAttOrHeal == 1) {
									Collections.shuffle(toAttack);
									attack(teamA.get(5), teamB.get(Integer.parseInt(toAttack.get(0))), teamBToHeal);
								} else {
									heal(teamA.get(5), teamAToHeal);
								}
							} else {
								Collections.shuffle(toAttack);
								attack(teamA.get(5), teamB.get(Integer.parseInt(toAttack.get(0))), teamBToHeal);
							}
						} else {
							System.out.println("no champ to attack");
						} 
					}else {
						System.out.println("cant attack! no more pv.");
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		teamATimers.add(timer);
		this.teamATimers.get( 5).scheduleAtFixedRate(champion11, 0, 1000/teamA.get( 5).getInitiative(), TimeUnit.MILLISECONDS);
		Runnable champion12 =new Runnable() {
			public void run() {
				try {
					if (teamA.get(2).getPv() > 0) {
						ArrayList<String> toAttack = new ArrayList<>();
						for (int i = 0; i < 10; i++) {
							if (teamA.get(i).getPv() > 0) {
								toAttack.add(String.valueOf(i));
							}
						}
						if (toAttack.size() > 0) {
							if (teamB.get(5).getType() == "Prêtre" && teamBToHeal.size() > 0) {
								int chooseAttOrHeal = intgenerator.nextInt(2);
								if (chooseAttOrHeal == 1) {
									Collections.shuffle(toAttack);
									attack(teamB.get(5), teamA.get(Integer.parseInt(toAttack.get(0))), teamAToHeal);
								} else {

									heal(teamB.get(5), teamBToHeal);
								}
							} else {
								Collections.shuffle(toAttack);
								attack(teamB.get(5), teamA.get(Integer.parseInt(toAttack.get(0))), teamAToHeal);
							}
						}  else {
							System.out.println("no champ to attack");
						} 
					}else {
						System.out.println("cant attack! no more pv.");
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		teamBTimers.add(timer);
		this.teamBTimers.get( 5).scheduleAtFixedRate(champion12, 0, 1000/teamB.get( 5).getInitiative(), TimeUnit.MILLISECONDS);
		//
		Runnable champion13 =new Runnable() {
			public void run() {
				try {
					if (teamA.get(2).getPv() > 0) {
						ArrayList<String> toAttack = new ArrayList<>();
						for (int i = 0; i < 10; i++) {
							if (teamB.get(i).getPv() > 0) {
								toAttack.add(String.valueOf(i));
							}
						}
						if (toAttack.size() > 0) {
							if (teamA.get(6).getType() == "Prêtre" && teamAToHeal.size() > 0) {
								int chooseAttOrHeal = intgenerator.nextInt(2);
								if (chooseAttOrHeal == 1) {
									Collections.shuffle(toAttack);
									attack(teamA.get(6), teamB.get(Integer.parseInt(toAttack.get(0))), teamBToHeal);
								} else {
									heal(teamA.get(6), teamAToHeal);
								}
							} else {
								Collections.shuffle(toAttack);
								attack(teamA.get(6), teamB.get(Integer.parseInt(toAttack.get(0))), teamBToHeal);
							}
						}  else {
							System.out.println("no champ to attack");
						} 
					}else {
						System.out.println("cant attack! no more pv.");
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		teamATimers.add(timer);
		this.teamATimers.get( 6).scheduleAtFixedRate(champion13, 0, 1000/teamA.get( 6).getInitiative(), TimeUnit.MILLISECONDS);
		Runnable champion14 =new Runnable() {
			public void run() {
				try {
					if (teamA.get(2).getPv() > 0) {
						ArrayList<String> toAttack = new ArrayList<>();
						for (int i = 0; i < 10; i++) {
							if (teamA.get(i).getPv() > 0) {
								toAttack.add(String.valueOf(i));
							}
						}
						if (toAttack.size() > 0) {
							if (teamB.get(6).getType() == "Prêtre" && teamBToHeal.size() > 0) {
								int chooseAttOrHeal = intgenerator.nextInt(2);
								if (chooseAttOrHeal == 1) {
									Collections.shuffle(toAttack);
									attack(teamB.get(6), teamA.get(Integer.parseInt(toAttack.get(0))), teamAToHeal);
								} else {
									heal(teamB.get(6), teamBToHeal);
								}
							} else {
								Collections.shuffle(toAttack);
								attack(teamB.get(6), teamA.get(Integer.parseInt(toAttack.get(0))), teamAToHeal);
							}
						} else {
							System.out.println("no champ to attack");
						} 
					}else {
						System.out.println("cant attack! no more pv.");
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		teamBTimers.add(timer);
		this.teamBTimers.get( 6).scheduleAtFixedRate(champion14, 0, 1000/teamB.get( 6).getInitiative(), TimeUnit.MILLISECONDS);
		//
		Runnable champion15 =new Runnable() {
			public void run() {
				try {
					if (teamA.get(2).getPv() > 0) {
						ArrayList<String> toAttack = new ArrayList<>();
						for (int i = 0; i < 10; i++) {
							if (teamB.get(i).getPv() > 0) {
								toAttack.add(String.valueOf(i));
							}
						}
						if (toAttack.size() > 0) {
							if (teamA.get(7).getType() == "Prêtre" && teamAToHeal.size() > 0) {
								int chooseAttOrHeal = intgenerator.nextInt(2);
								if (chooseAttOrHeal == 1) {
									Collections.shuffle(toAttack);
									attack(teamA.get(7), teamB.get(Integer.parseInt(toAttack.get(0))), teamBToHeal);
								} else {
									heal(teamA.get(7), teamAToHeal);
								}
							} else {
								Collections.shuffle(toAttack);
								attack(teamA.get(7), teamB.get(Integer.parseInt(toAttack.get(0))), teamBToHeal);
							}
						} else {
							System.out.println("no champ to attack");
						} 
					}else {
						System.out.println("cant attack! no more pv.");
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		teamATimers.add(timer);
		this.teamATimers.get( 7).scheduleAtFixedRate(champion15, 0, 1000/teamA.get( 7).getInitiative(), TimeUnit.MILLISECONDS);
		Runnable champion16 =new Runnable() {
			public void run() {
				try {
					if (teamA.get(2).getPv() > 0) {
						ArrayList<String> toAttack = new ArrayList<>();
						for (int i = 0; i < 10; i++) {
							if (teamA.get(i).getPv() > 0) {
								toAttack.add(String.valueOf(i));
							}
						}
						if (toAttack.size() > 0) {
							if (teamB.get(7).getType() == "Prêtre" && teamBToHeal.size() > 0) {
								int chooseAttOrHeal = intgenerator.nextInt(2);
								if (chooseAttOrHeal == 1) {
									Collections.shuffle(toAttack);
									attack(teamB.get(7), teamA.get(Integer.parseInt(toAttack.get(0))), teamAToHeal);
								} else {
									heal(teamB.get(7), teamBToHeal);
								}
							} else {
								Collections.shuffle(toAttack);
								attack(teamB.get(7), teamA.get(Integer.parseInt(toAttack.get(0))), teamAToHeal);
							}
						}  else {
							System.out.println("no champ to attack");
						} 
					}else {
						System.out.println("cant attack! no more pv.");
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		teamBTimers.add(timer);
		this.teamBTimers.get( 7).scheduleAtFixedRate(champion16, 0, 1000/teamB.get( 7).getInitiative(), TimeUnit.MILLISECONDS);
		//
		Runnable champion17 =new Runnable() {
			public void run() {
				try {
					if (teamA.get(2).getPv() > 0) {
						ArrayList<String> toAttack = new ArrayList<>();
						for (int i = 0; i < 10; i++) {
							if (teamB.get(i).getPv() > 0) {
								toAttack.add(String.valueOf(i));
							}
						}
						if (toAttack.size() > 0) {
							if (teamA.get(8).getType() == "Prêtre" && teamAToHeal.size() > 0) {
								int chooseAttOrHeal = intgenerator.nextInt(2);
								if (chooseAttOrHeal == 1) {
									Collections.shuffle(toAttack);
									attack(teamA.get(8), teamB.get(Integer.parseInt(toAttack.get(0))), teamBToHeal);
								} else {
									heal(teamA.get(8), teamAToHeal);
								}
							} else {
								Collections.shuffle(toAttack);
								attack(teamA.get(8), teamB.get(Integer.parseInt(toAttack.get(0))), teamBToHeal);
							}
						}  else {
							System.out.println("no champ to attack");
						} 
					}else {
						System.out.println("cant attack! no more pv.");
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		teamATimers.add(timer);
		this.teamATimers.get( 8).scheduleAtFixedRate(champion17, 0, 1000/teamA.get( 8).getInitiative(), TimeUnit.MILLISECONDS);
		Runnable champion18 =new Runnable() {
			public void run() {
				try {
					if (teamA.get(2).getPv() > 0) {
						ArrayList<String> toAttack = new ArrayList<>();
						for (int i = 0; i < 10; i++) {
							if (teamA.get(i).getPv() > 0) {
								toAttack.add(String.valueOf(i));
							}
						}
						if (toAttack.size() > 0) {
							if (teamB.get(8).getType() == "Prêtre" && teamBToHeal.size() > 0) {
								int chooseAttOrHeal = intgenerator.nextInt(2);
								if (chooseAttOrHeal == 1) {
									Collections.shuffle(toAttack);
									attack(teamB.get(8), teamA.get(Integer.parseInt(toAttack.get(0))), teamAToHeal);
								} else {

									heal(teamB.get(8), teamBToHeal);
								}
							} else {
								Collections.shuffle(toAttack);
								attack(teamB.get(8), teamA.get(Integer.parseInt(toAttack.get(0))), teamAToHeal);
							}
						} else {
							System.out.println("no champ to attack");
						} 
					}else {
						System.out.println("cant attack! no more pv.");
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		teamBTimers.add(timer);
		this.teamBTimers.get( 8).scheduleAtFixedRate(champion18, 0, 1000/teamB.get( 8).getInitiative(), TimeUnit.MILLISECONDS);
		//
		Runnable champion19 =new Runnable() {
			public void run() {
				try {
					if (teamA.get(2).getPv() > 0) {
						ArrayList<String> toAttack = new ArrayList<>();
						for (int i = 0; i < 10; i++) {
							if (teamB.get(i).getPv() > 0) {
								toAttack.add(String.valueOf(i));
							}
						}
						if (toAttack.size() > 0) {
							if (teamA.get(9).getType() == "Prêtre" && teamAToHeal.size() > 0) {
								int chooseAttOrHeal = intgenerator.nextInt(2);
								if (chooseAttOrHeal == 1) {
									Collections.shuffle(toAttack);
									attack(teamA.get(9), teamB.get(Integer.parseInt(toAttack.get(0))), teamBToHeal);
								} else {
									heal(teamA.get(9), teamAToHeal);
								}
							} else {
								Collections.shuffle(toAttack);
								attack(teamA.get(9), teamB.get(Integer.parseInt(toAttack.get(0))), teamBToHeal);
							}
						}  else {
							System.out.println("no champ to attack");
						} 
					}else {
						System.out.println("cant attack! no more pv.");
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		teamATimers.add(timer);
		this.teamATimers.get(9).scheduleAtFixedRate(champion19, 0, 1000/teamA.get(9).getInitiative(), TimeUnit.MILLISECONDS);
		Runnable champion20 =new Runnable() {
			public void run() {
				try {
					if (teamA.get(2).getPv() > 0) {
						ArrayList<String> toAttack = new ArrayList<>();
						for (int i = 0; i < 10; i++) {
							if (teamA.get(i).getPv() > 0) {
								toAttack.add(String.valueOf(i));
							}
						}
						if (toAttack.size() > 0) {
							if (teamB.get(9).getType() == "Prêtre" && teamBToHeal.size() > 0) {
								int chooseAttOrHeal = intgenerator.nextInt(2);
								if (chooseAttOrHeal == 1) {
									Collections.shuffle(toAttack);
									attack(teamB.get(9), teamA.get(Integer.parseInt(toAttack.get(0))), teamAToHeal);
								} else {

									heal(teamB.get(9), teamBToHeal);
								}
							} else {
								Collections.shuffle(toAttack);
								attack(teamB.get(9), teamA.get(Integer.parseInt(toAttack.get(0))), teamAToHeal);
							}
						} else {
							System.out.println("no champ to attack");
						} 
					}else {
						System.out.println("cant attack! no more pv.");
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		teamBTimers.add(timer);
		this.teamBTimers.get(9).scheduleAtFixedRate(champion20, 0, 1000/teamB.get(9).getInitiative(), TimeUnit.MILLISECONDS);

		Runnable check =new Runnable() {
			public void run() {
				System.out.println(teamAToHeal.size() +" team a / "+ teamBToHeal.size()+" team b");
				int teamAOut=0;
				int teamBOut=0;
				for(int i=0; i<teamB.size(); i++) {
					System.out.println(teamB.get(i).getPv()+" "+teamB.get(i).getNom());
					if(teamB.get(i).getPv()<=0) {
						teamBOut++;
					}
				}
				
				for(int i=0; i<teamA.size(); i++) {
					System.out.println(teamA.get(i).getPv()+" "+teamA.get(i).getNom());
					if(teamA.get(i).getPv()<=0) {
						teamAOut++;
					}
				}
				if(teamAOut == 10 || teamBOut == 10) {
					try {
						String winnerId = new String();
						String looserId = new String();
						int winnerAlive=0;
						int looseralive=0;
						//stop all thread teamA
						for(int i =0; i<teamATimers.size(); i++) {
							teamATimers.get(i).shutdown();
							teamATimers.get(i).awaitTermination(33, TimeUnit.MILLISECONDS);
							Champion c = teamA.get(i);
							String sql = "insert into champion values(0,'"+ c.getNom()+"','"+c.getType()+"',"+c.getAtt()+","+c.getDef()+","+c.getPv()+","+c.getParade()+","+c.getEsquive()+","+c.getCrit()+","+c.getHeal()+","+ teamAId +")";
							Statement st = connection.createStatement();
							st.executeUpdate(sql);
						}
						//stop all thread teamB
						for(int i =0; i<teamBTimers.size(); i++) {
							teamBTimers.get(i).shutdown();
							teamBTimers.get(i).awaitTermination(33, TimeUnit.MILLISECONDS);
							Champion c = teamB.get(i);
							String sql = "insert into champion values(0,'"+ c.getNom()+"','"+c.getType()+"',"+c.getAtt()+","+c.getDef()+","+c.getPv()+","+c.getParade()+","+c.getEsquive()+","+c.getCrit()+","+c.getHeal()+","+ teamBId+")";
							Statement st = connection.createStatement();
							st.executeUpdate(sql);
						}
						
						if(teamAOut ==10) {
							winnerAlive = 10-teamBOut;
							looseralive = 10-teamAOut;
							winnerId = "TeamB"+teamBId;
							looserId = "TeamA"+teamAId;
						}else{
							winnerAlive = 10-teamAOut;
							looseralive = 10-teamBOut;
							winnerId = "TeamA"+teamAId;
							looserId = "TeamB"+teamBId;
						}
						int combatId=0;
						String sql = "select Max(CombatId) from combat";
						Statement st = connection.createStatement();
						ResultSet rs = st.executeQuery(sql);
						if (rs.next()) {	combatId = rs.getInt("Max(CombatId)");	}			
						Historique result= new Historique(winnerId, looserId, winnerAlive, looseralive);
						String sql1 ="insert into historique values("+ winnerAlive +","+ looseralive +","+ combatId +",'"+winnerId+"','"+looserId+"')";
						Statement st1 = connection.createStatement();
						st1.executeUpdate(sql1);
						historique.add(result);
						timer1.shutdown();
						timer1.awaitTermination(1, TimeUnit.SECONDS);
						System.out.println("end");
					} catch (InterruptedException e) {	e.printStackTrace();	}
					  catch (SQLException e) {	e.printStackTrace();	}
				}

				
			}
		};
		this.timer1 = Executors.newSingleThreadScheduledExecutor();
		this.timer1.scheduleAtFixedRate(check, 0, 1, TimeUnit.SECONDS);
	}
	@FXML
	public void quitter() {
		System.out.println("closing the app");
		close();
		System.exit(0);
	}
	@FXML
	public void equipeA() {
		if (teamA.size()>0) {
			//System.out.println(javafx.scene.text.Font.getFamilies());
			HBox.getChildren().clear();
			VBox left = new VBox();
			VBox right = new VBox();
			list1 = new ListView<>();
			ObservableList<String> data1 = FXCollections.observableArrayList();
			data1.clear();
			list1.setItems(data1);
			list1.setPrefWidth(425);
			list1.autosize();
			for (int i = 0; i < teamA.size(); i++) {
				data1.add(teamA.get(i).getNom());
			}
			Label leftLabel = new Label("liste des champions!");
			leftLabel.setTextAlignment(TextAlignment.CENTER);
			leftLabel.setFont(new Font("Blackadder ITC", 24));
			left.getChildren().add(leftLabel);
			left.getChildren().add(list1);
			left.setAlignment(Pos.CENTER);
			list2 = new ListView<>();
			ObservableList<String> data2 = FXCollections.observableArrayList();
			list2.setItems(data2);
			list2.autosize();
			list2.setPrefWidth(425);
			data2.add("selectionnez a champion!");
			Label rightLabel = new Label("caracteristique du champion!");
			rightLabel.setTextAlignment(TextAlignment.CENTER);
			rightLabel.setFont(new Font("Blackadder ITC", 24));
			right.getChildren().add(rightLabel);
			right.getChildren().add(list2);
			right.setAlignment(Pos.CENTER);
			HBox.getChildren().add(left);
			HBox.getChildren().add(right);
			list1.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					Integer champselect = null;
					for (int i = 0; i < teamA.size() && champselect == null; i++) {
						if (newValue == teamA.get(i).getNom()) {
							champselect = i;
						}
					}
					data2.clear();
					data2.add("name : " + teamA.get(champselect).getNom());
					data2.add("Attaque : " + teamA.get(champselect).getAtt());
					data2.add("defense : " + teamA.get(champselect).getDef());
					data2.add("Points de vie : " + teamA.get(champselect).getPv());
					data2.add("Pourcentage de coup critique : " + teamA.get(champselect).getCrit() + "%");
					data2.add("initiative : " + teamA.get(champselect).getInitiative());
					if (teamA.get(champselect).getType() == "Guerrier") {
						data2.add("Pourcentage de parade : " + teamA.get(champselect).getParade() + "%");
					} else if (teamA.get(champselect).getType() == "Voleur") {
						data2.add("Pourcentage d'esquive : " + teamA.get(champselect).getEsquive() + "%");
					} else if (teamA.get(champselect).getType() == "Prêtre") {
						data2.add("montant de soin : " + teamA.get(champselect).getHeal());
						data2.add("Pourcentage de parade : " + teamA.get(champselect).getParade() + "%");
					}
				}
			});
		}
	}
	@FXML
	public void equipeB() {
		if (teamB.size()>0) {
			HBox.getChildren().clear();
			VBox left = new VBox();
			VBox right = new VBox();
			list1 = new ListView<>();
			ObservableList<String> data3 = FXCollections.observableArrayList();
			list1.setItems(data3);
			list1.setPrefWidth(425);
			list1.autosize();
			for (int i = 0; i < teamB.size(); i++) {
				data3.add(teamB.get(i).getNom());
			}
			Label leftLabel = new Label("liste des champions!");
			leftLabel.setTextAlignment(TextAlignment.CENTER);
			leftLabel.setFont(new Font("Blackadder ITC", 24));
			left.getChildren().add(leftLabel);
			left.getChildren().add(list1);
			left.setAlignment(Pos.CENTER);
			list2 = new ListView<>();
			ObservableList<String> data4 = FXCollections.observableArrayList();
			data4.clear();
			list2.setItems(data4);
			list2.autosize();
			list2.setPrefWidth(425);
			data4.add("selectionnez un champion!");
			Label rightLabel = new Label("caracteristique du champion!");
			rightLabel.setTextAlignment(TextAlignment.CENTER);
			rightLabel.setFont(new Font("Blackadder ITC", 24));
			right.getChildren().add(rightLabel);
			right.getChildren().add(list2);
			right.setAlignment(Pos.CENTER);
			HBox.getChildren().add(left);
			HBox.getChildren().add(right);
			list1.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					Integer champselect = null;
					for (int i = 0; i < teamB.size() && champselect == null; i++) {
						if (newValue == teamB.get(i).getNom()) {
							champselect = i;
						}
					}
					data4.clear();
					try {
						data4.add("name : " + teamB.get(champselect).getNom());
					} catch (Exception e) {
						e.printStackTrace();
						System.out.println((champselect));
					}
					data4.add("Attaque : " + teamB.get(champselect).getAtt());
					data4.add("defense : " + teamB.get(champselect).getDef());
					data4.add("Points de vie : " + teamB.get(champselect).getPv());
					data4.add("Pourcentage de coup critique : " + teamB.get(champselect).getCrit() + "%");
					data4.add("initiative : " + teamB.get(champselect).getInitiative());
					if (teamB.get(champselect).getType() == "Guerrier") {
						data4.add("Pourcentage de parade : " + teamB.get(champselect).getParade() + "%");
					} else if (teamB.get(champselect).getType() == "Voleur") {
						data4.add("Pourcentage d'esquive : " + teamB.get(champselect).getEsquive() + "%");
					} else if (teamB.get(champselect).getType() == "Prêtre") {
						data4.add("montant de soin : " + teamB.get(champselect).getHeal());
						data4.add("Pourcentage de parade : " + teamB.get(champselect).getParade() + "%");
					}
				}
			});
		}
	}
	@FXML
	public void historique() {
		if (historique.size()>0) {
			HBox.getChildren().clear();
			VBox vbox = new VBox();
			Label label = new Label("Historique des combats!");
			label.setTextAlignment(TextAlignment.CENTER);
			label.setFont(new Font("Blackadder ITC", 30));
			vbox.getChildren().addAll(label);
			vbox.setAlignment(Pos.TOP_CENTER);
			vbox.setPadding(new Insets(0, 0, 20, 0));
			vbox.setSpacing(10);
			HBox.getChildren().add(vbox);
			HBox.setAlignment(Pos.TOP_CENTER);
			if (historique.size() <= 0) {
				return;
			} else {
				for (int i = 0; i < historique.size(); i++) {
					Label lab = new Label(historique.get(i).getWinnerId() + " \t " + historique.get(i).getWinnerAlive()
							+ " : " + historique.get(i).getLooserAlive() + " \t " + historique.get(i).getLooserId());
					lab.setTextAlignment(TextAlignment.CENTER);
					vbox.getChildren().add(lab);
				}
			} 
		}
		
	}
	@FXML
	public void fightEncours(){
		HBox.getChildren().clear();
		list = new ListView<>();
		list.setItems(data);
		list.setPrefWidth(850);
		list.setMaxWidth(1366);
		HBox.getChildren().add(list);
	}
	public Connection connecter(String bd, String user, String pass) {
		Connection connexion = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/"+bd+"?useSSL=false",user,pass);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return connexion;
	}
}
