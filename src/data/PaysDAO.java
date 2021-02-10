package data;

import java.util.List;

public interface PaysDAO extends DAO<Pays> {
	int getId(String nom);
	Pays PaysMaxHabitants();
	List<Pays> getAll();
}
