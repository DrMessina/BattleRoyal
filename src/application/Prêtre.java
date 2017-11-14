package application;

import java.util.Random;

public class Prêtre {
	public static final String TYPE = "Prêtre";
	int pv,att,def, crit, initiative,parade,sucessAtt,heal;
	String nom;
	
	public Prêtre(String nom) {
		this.att = getRandom(30,60);
		this.def = getRandom(60, 80);
		this.heal = this.def/4;
		this.pv = getRandom(70, 90);
		this.crit = 0;
		this.initiative = getRandom(50, 60);
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
	public int getHeal() {
		return heal;
	}
	public void setHeal(int heal) {
		this.heal = heal;
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
