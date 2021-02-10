package data;

public class Pays {
	private int numPays;
	private String nomPays;
	private int nbHabitants;
	
	public Pays(String pNomPays, int pNbHabitants) {
		this.nomPays=pNomPays;
		this.nbHabitants=pNbHabitants;
	}
	
	public Pays(int id, String pNomPays, int pNbHabitants) {
		this.numPays=id;
		this.nomPays=pNomPays;
		this.nbHabitants=pNbHabitants;
	}
	
	public Pays() {

	}

	public int getNumPays() {
		return numPays;
	}
	
	public void setNumPays(int numPays) {
		this.numPays = numPays;
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
	
	@Override
	public String toString() {
		return "[id=" + numPays + ", nom=" + nomPays + ", Nombre d'habitants=" + nbHabitants + "]";
	}
}
