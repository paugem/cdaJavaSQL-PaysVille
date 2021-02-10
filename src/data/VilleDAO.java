package data;

import java.util.List;

public interface VilleDAO extends DAO<Ville> {
	List<Ville> getAllByCountry(String nomPays);
	Ville villeMaxSuperficie();
}
