package data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VilleDAOImpl implements VilleDAO {
	private static final Logger logger = LoggerFactory.getLogger("data.VilleDAOImpl");
	@Override
	public Ville save(Ville ville) {
		Connection c = MyConnection.getConnection();
		if (c != null) {
			try {
				Statement statement = c.createStatement();
				String request = "SELECT * FROM Ville WHERE nom_ville = '" + ville.getNomVille() + "';";
				ResultSet result = statement.executeQuery(request);
				if (result.next() == false) {
					PreparedStatement ps = c.prepareStatement("INSERT INTO Ville (nom_ville,superficie,id_pays) values (?,?,?); ",PreparedStatement.RETURN_GENERATED_KEYS);
					ps.setString(1, ville.getNomVille());
					ps.setInt(2, ville.getSuperficie());
					ps.setInt(3, ville.getIdPays());
					ps.executeUpdate();
					ResultSet resultat = ps.getGeneratedKeys();
					if (resultat.next()) {
						ville.setNumVille(resultat.getInt(1));
						return ville;
					}
				} else {
					System.out.println("La ville existe déjà dans la BDD");
				}
			} catch (SQLException e) {
				logger.error("erreur "+e);
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	public void removeByName(String nomVille) {
		Connection c = MyConnection.getConnection();
		if (c != null) {
			try {
				Statement statement = c.createStatement();
				String request = "SELECT * FROM Ville WHERE nom_ville = '" + nomVille + "';";
				ResultSet result = statement.executeQuery(request);
				if (result.next() == false) {
					System.out.println("La ville " + nomVille + " n'existe pas");
				} else {
					request = "DELETE FROM Ville WHERE nom_ville =?;";
					PreparedStatement ps = c.prepareStatement(request);
					ps.setString(1, nomVille);
					ps.executeUpdate();
					System.out.println("Suppression de " + nomVille + " dans la table Ville");
				}
			} catch (SQLException e) {
				logger.error("erreur "+e);
				e.printStackTrace();
			}
		}
	}

	@Override
	public List<Ville> getAllByCountry(String nomPays) {
		List<Ville> ville = new ArrayList<>();
		Connection c = MyConnection.getConnection();
		if (c != null) {
			try {
				// Vérification de l'existence du pays
				Statement statement = c.createStatement();
				String request = "SELECT * FROM Pays WHERE nom_pays = '" + nomPays + "';";
				ResultSet result = statement.executeQuery(request);
				if (result.next() == false) {
					System.out.println("Le pays n'existe pas dans la base de données");
				} else {
					request = "SELECT * FROM Ville INNER JOIN Pays ON Ville.id_pays = Pays.id AND Pays.nom_pays ='"
							+ nomPays + "';";
					result = statement.executeQuery(request);
					while (result.next()) {
						ville.add(new Ville(result.getInt("id"), result.getString("nom_ville"),
								result.getInt("superficie"), result.getInt("id_pays")));
					}
				}
			} catch (SQLException e) {
				logger.error("erreur "+e);
				e.printStackTrace();
			}
		}
		return ville;
	}

	@Override
	public Ville villeMaxSuperficie() {
		Ville ville=new Ville();
		Connection c = MyConnection.getConnection();
		if (c != null) {
			try {
				PreparedStatement statement = c.prepareStatement("SELECT * FROM Ville WHERE superficie = (SELECT max(superficie) FROM Ville);");
				ResultSet result = statement.executeQuery();
				while (result.next()) {
					ville = new Ville(result.getInt("id"), result.getString("nom_ville"),
							result.getInt("superficie"), result.getInt("id_pays"));
				}
			} catch (SQLException e) {
				logger.error("erreur "+e);
				e.printStackTrace();
			}
		}
		return ville;
	}

}
