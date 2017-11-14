package application;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

public class BattleRoyaleController {
	//private Team t1 = new Team();
	//private Team t2 = new Team();
	static final ArrayList<String> championType= new ArrayList<>();
	
	@FXML
	Label afficheur;
	@FXML
	ListView<String> list = new ListView<>();
	private ObservableList<String> data = FXCollections.observableArrayList();

	private ScheduledExecutorService timer;
	private ScheduledExecutorService timer2;

	
	public void init() {
		/*afficheur.setText("hihya !!!");
		afficheur.setPrefWidth(400);
		ObservableList<String> data = FXCollections.observableArrayList();

		//data.addAll("A", "B", "C", "D", "E");
		for(int i=0; i<10; i++) {
			int intgenerated = getRandom(70, 80);
			data.add(String.valueOf(intgenerated));;
		}

		list.setItems(data);
		list.setPrefWidth(400);*/
		Guerrier g1 = new Guerrier("Landry");
		Guerrier g2 = new  Guerrier("Piib");
		System.out.println(g2.getPv());
		Prêtre p1 = new Prêtre("soraka");
		Champion c1 = new Champion(g1);
		Champion c2 = new Champion(g2);
		Champion c3 = new Champion(p1);
		
		data.addAll("Guerrier", "Voleur","Prêtre","Mage");
		championType.addAll(data);
		list.setItems(data);
		list.setPrefWidth(400);
		Runnable landry =new Runnable() {
			public void run() {
				attack(c1, c2);
			}
		};
		Runnable piib =new Runnable() {
			public void run() {
				attack(c2, c1);
			}
		};
		this.timer = Executors.newSingleThreadScheduledExecutor();
		this.timer.scheduleAtFixedRate(landry, 0, 1000/g1.getInitiative(), TimeUnit.MILLISECONDS);
		Runnable check =new Runnable() {
			public void run() {
				if(c2.getPv()<=0) {
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
		this.timer2 = Executors.newSingleThreadScheduledExecutor();
		this.timer2.scheduleAtFixedRate(check, 0, 10, TimeUnit.MILLISECONDS);
		Runnable soraka =new Runnable() {
			public void run() {
				attack(c3, c2);
			}
		};
		//attack(c1, c2);
		//heal(c3, c2);
	}
	public void setListViewWidth(Number newSceneWidth) {
		list.setPrefWidth(newSceneWidth.intValue());
	}
	public int getRandom(int min, int max) {
		Random intgenerator = new Random();
		int intgenerated = intgenerator.nextInt(max-min)+min;
		return intgenerated;
	}
	public void attack(Champion attacker,Champion attacked) {
		int pvOfAttacked = attacked.getPv();
		System.out.println(pvOfAttacked+" pv piib");
		if(attacked.getDef() < attacker.getAtt()) {
			int damage = attacker.getAtt() - attacked.getDef();
			pvOfAttacked = pvOfAttacked-damage;
			attacked.setPv(pvOfAttacked);
			data.add(attacker.getNom()+" attaque avec "+attacker.getAtt()+" pt dattaque, "+attacked.getNom()+" qui a "+attacked.getDef()+" defense et "+attacked.getPv()+" : il reçoit donc "+damage+" degat.");
			//System.out.println(attacker.getNom()+" attaque avec "+attacker.getAtt()+" pt dattaque, "+attacked.getNom()+"qui a "+attacked.getDef()+" defense et "+attacked.getPv()+" : il reçoit donc "+damage+" degat.");
			//System.out.println(pvOfAttacked);
			System.out.println("pv new value "+attacked.getPv());
		}else{
			data.add("pas de degats");
		}
	}
	public void heal(Champion healer, Champion healed) {
		data.add("pv : "+healed.getPv()+" de "+ healed.getNom());
		data.add(healer.getHeal()+" mont pv ajouté");
		healed.setPv(healed.getPv()+healer.getHeal());
		data.add("new pv value : "+healed.getPv());
	}
	public void generateTeams() {
		for(int i=0; i<20; i++) {
			
		}
	}
	
	
}
