package application;

import java.util.Random;

public class Voleur {
	public static final String TYPE = "Voleur";
	int pv,att,def, crit, initiative,esquive;
	String nom;
	
	public Voleur(String nom) {
		this.att = getRandom(40,60);
		this.def = getRandom(30, 50);
		this.pv = getRandom(70, 80);
		this.crit = getRandom(15, 20);
		this.esquive = getRandom(40, 70);
		this.initiative = getRandom(75, 90);
		this.nom = nom;
	}	
	public int getRandom(int min, int max) {
		Random intgenerator = new Random();
		int intgenerated = intgenerator.nextInt(max-min)+min;
		return intgenerated;
	}
	public int getPv() {
		return pv;
	}
	public void setPv(int pv) {
		this.pv = pv;
	}
	public int getAtt() {
		return att;
	}
	public void setAtt(int att) {
		this.att = att;
	}
	public int getDef() {
		return def;
	}
	public void setDef(int def) {
		this.def = def;
	}
	public int getCrit() {
		return crit;
	}
	public void setCrit(int crit) {
		this.crit = crit;
	}
	public int getInitiative() {
		return initiative;
	}
	public void setInitiative(int initiative) {
		this.initiative = initiative;
	}
	public int getEsquive() {
		return esquive;
	}
	public void setEsquive(int parade) {
		this.esquive = parade;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public String getType() {
		return TYPE;
	}
}
