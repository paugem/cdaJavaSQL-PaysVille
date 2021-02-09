package data;


import java.util.List;

public interface DAO<T> {
	T save(T o);
	void removeByName(String nomObjet);
	List<T> getAll();
}
