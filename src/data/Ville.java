package data;

public class Ville {
	private int numVille;
	private String nomVille;
	private int superficie;
	private int idPays;
	
	public Ville() {
		
	}
	
	public Ville (String pNomVille, int pSuperficie, int pIdPays) {
		this.nomVille=pNomVille;
		this.superficie=pSuperficie;
		this.idPays=pIdPays;
	}
	
	public Ville (int id, String pNomVille, int pSuperficie, int pIdPays) {
		this.numVille=id;
		this.nomVille=pNomVille;
		this.superficie=pSuperficie;
		this.idPays=pIdPays;
	}

	public String getNomVille() {
		return nomVille;
	}

	public void setNomVille(String nomVille) {
		this.nomVille = nomVille;
	}

	public int getSuperficie() {
		return superficie;
	}

	public void setSuperficie(int superficie) {
		this.superficie = superficie;
	}

	public int getNumVille() {
		return numVille;
	}
	
	public void setNumVille(int numVille) {
		this.numVille = numVille;
	}

	public int getIdPays() {
		return idPays;
	}

	public void setIdPays(int idPays) {
		this.idPays = idPays;
	}
	
	@Override
	public String toString() {
		return "[id=" + numVille + ", nom=" + nomVille + ", superficie=" + superficie + ", id_Pays="+idPays+"]";
	}
}
