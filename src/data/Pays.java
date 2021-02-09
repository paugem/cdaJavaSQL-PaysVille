package data;

public class Pays {
	private int numPays;
	private String nomPays;
	private int nbHabitants;
	
	public Pays(String pNomPays, int pNbHabitants) {
		this.nomPays=pNomPays;
		this.nbHabitants=pNbHabitants;
	}
	
	public Pays(int pNumPays, String pNomPays, int pNbHabitants) {
		this.numPays=pNumPays;
		this.nomPays=pNomPays;
		this.nbHabitants=pNbHabitants;
	}
	
	public int getNumPays() {
		return numPays;
	}
	
	public String getNomPays() {
		return nomPays;
	}
	
	public void setNomPays(String nomPays) {
		this.nomPays = nomPays;
	}
	
	public int getNbHabitants() {
		return nbHabitants;
	}
	
	public void setNbHabitants(int nbHabitants) {
		this.nbHabitants = nbHabitants;
	}
}
