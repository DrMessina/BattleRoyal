package application;

public class Champion {
	Guerrier guerrier= null;
	Mage mage=null;
	Pr�tre pr�tre=null;
	Voleur voleur=null;
	String type;
	
	public Champion(Guerrier Champion) {
		guerrier = Champion;
		type= Champion.getType();
	}
	public Champion(Voleur Champion) {
		voleur = Champion;
		type= Champion.getType();
	}
	public Champion(Mage Champion) {
		mage = Champion;
		type= Champion.getType();
	}
	public Champion(Pr�tre Champion) {
		pr�tre = Champion;
		type= Champion.getType();
	}
	
	public int getPv() {
		if(voleur != null) {
			return voleur.getPv();
		}else if (mage != null) {
			return mage.getPv();
		}else if (guerrier != null) {
			return guerrier.getPv();
		}else if (pr�tre != null) {
			return pr�tre.getPv();
		}
		return 0;
	}
	public void setPv(int pv) {
		if(voleur != null) {
			voleur.setPv(pv);
		}else if (mage != null) {
			mage.setPv(pv);
		}else if (guerrier != null) {
			 guerrier.setPv(pv);
		}else if (pr�tre != null) {
			 pr�tre.setPv(pv);
		}
	}
	public int getAtt() {
		if(voleur != null) {
			return voleur.getAtt();
		}else if (mage != null) {
			return mage.getAtt();
		}else if (guerrier != null) {
			return guerrier.getAtt();
		}else if (pr�tre != null) {
			return pr�tre.getAtt();
		}
		return 0;
	}
	public void setAtt(int att) {
		if(voleur != null) {
			voleur.setAtt(att);
		}else if (mage != null) {
			mage.setAtt(att);
		}else if (guerrier != null) {
			guerrier.setAtt(att);
		}else if (pr�tre != null) {
			pr�tre.setAtt(att);
		}
	}
	public int getDef() {
		if(voleur != null) {
			return voleur.getDef();
		}else if (mage != null) {
			return mage.getDef();
		}else if (guerrier != null) {
			return guerrier.getDef();
		}else if (pr�tre != null) {
			return pr�tre.getDef();
		}
		return 0;
	}
	public void setDef(int def) {
		if(voleur != null) {
			voleur.setDef(def);
		}else if (mage != null) {
			mage.setDef(def);
		}else if (guerrier != null) {
			mage.setDef(def);
		}else if (pr�tre != null) {
			pr�tre.setDef(def);
		}
	}
	public int getCrit() {
		if(voleur != null) {
			return voleur.getCrit();
		}else if (mage != null) {
			return mage.getCrit();
		}else if (guerrier != null) {
			return guerrier.getCrit();
		}else if (pr�tre != null) {
			return pr�tre.getCrit();
		}
		return 0;
	}
	public void setCrit(int crit) {
		if(voleur != null) {
			voleur.setCrit(crit);
		}else if (mage != null) {
			mage.setCrit(crit);
		}else if (guerrier != null) {
			guerrier.setCrit(crit);
		}else if (pr�tre != null) {
			pr�tre.setCrit(crit);
		}
	}
	public int getInitiative() {
		if(voleur != null) {
			return voleur.getInitiative();
		}else if (mage != null) {
			return mage.getInitiative();
		}else if (guerrier != null) {
			return guerrier.getInitiative();
		}else if (pr�tre != null) {
			return pr�tre.getInitiative();
		}
		return 0;
	}
	public void setInitiative(int initiative) {
		if(voleur != null) {
			voleur.setInitiative(initiative);
		}else if (mage != null) {
			mage.setInitiative(initiative);
		}else if (guerrier != null) {
			guerrier.setInitiative(initiative);
		}else if (pr�tre != null) {
			pr�tre.setInitiative(initiative);
		}
	}
	public int getEsquive() {
		if(voleur != null) {
			return voleur.getEsquive();
		}
		return 0;
	}
	public void setEsquive(int parade) {
		voleur.setEsquive(parade);
	}
	public int getParade() {
		if (guerrier != null) {
			return guerrier.getParade();
		}else if (pr�tre != null) {
			return pr�tre.getParade();
		}
		return 0;
	}
	public void setParade(int parade) {
		if (guerrier != null) {
			guerrier.setParade(parade);
		}else if (pr�tre != null) {
			pr�tre.setParade(parade);
		}
	}
	public int getHeal() {
		return pr�tre.getHeal();
	}
	public void setHeal(int heal) {
		pr�tre.setHeal(heal);
	}
	public String getNom() {
		if(voleur != null) {
			return voleur.getNom();
		}else if (mage != null) {
			return mage.getNom();
		}else if (guerrier != null) {
			return guerrier.getNom();
		}else if (pr�tre != null) {
			return pr�tre.getNom();
		}
		return null;
	}
	public void setNom(String nom) {
		if(voleur != null) {
			voleur.setNom(nom);
		}else if (mage != null) {
			mage.setNom(nom);
		}else if (guerrier != null) {
			guerrier.setNom(nom);
		}else if (pr�tre != null) {
			pr�tre.setNom(nom);
		}
	}
	public String getType() {
		return type;
	}
}
