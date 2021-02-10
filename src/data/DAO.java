package data;

public interface DAO<T> {
	T save(T o);
	void removeByName(String nomObjet);
}
