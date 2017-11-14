package application;

import java.util.Random;

public class Guerrier {
	public static final String TYPE = "Guerrier";
	int pv,att,def, crit, initiative,parade,sucessAtt;
	String nom;
	
	public Guerrier(String nom) {
		this.pv = getRandom(120, 150);
		this.att = getRandom(70,90);
		this.def = getRandom(70, 90);
		this.crit = 0;
		this.initiative = getRandom(40, 60);
		this.parade = 0;
		this.sucessAtt = 0;
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
	public int getParade() {
		return parade;
	}
	public void setParade(int parade) {
		this.parade = parade;
	}
	public int getSucessAtt() {
		return sucessAtt;
	}
	public void setSucessAtt(int sucessAtt) {
		this.sucessAtt = sucessAtt;
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
