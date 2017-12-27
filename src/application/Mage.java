package application;

import java.util.Random;

public class Mage {
	public static final String TYPE = "Mage";
	int pv,att,def, crit, initiative;
	String nom;
	
	public Mage(String nom) {
		this.att = getRandom(100,150);
		this.def = getRandom(20, 40);
		this.pv = getRandom(60, 70);
		this.crit = getRandom(5, 7);
		this.initiative = getRandom(60, 70);
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
