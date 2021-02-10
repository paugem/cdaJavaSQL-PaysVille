package data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PaysDAOImpl implements PaysDAO {
	private static final Logger logger = LoggerFactory.getLogger("data.PaysDAOImpl");
	@Override
	public Pays save(Pays pays) {
		Connection c = MyConnection.getConnection();
		if (c != null) {
			try {
				Statement statement = c.createStatement();
				String request = "SELECT * FROM Pays WHERE nom_pays = '" + pays.getNomPays() + "';";
				ResultSet result = statement.executeQuery(request);
				if (result.next() == false) {
					PreparedStatement ps = c.prepareStatement("insert into Pays (nom_pays,nbre_habitants) values (?,?); ",
							PreparedStatement.RETURN_GENERATED_KEYS);
					ps.setString(1, pays.getNomPays());
					ps.setInt(2, pays.getNbHabitants());
					ps.executeUpdate();
					ResultSet resultat = ps.getGeneratedKeys();
					if (resultat.next()) {
						logger.info("Pays créé");
						pays.setNumPays(resultat.getInt(1));
						return pays;
					}
				} else {
					logger.warn("Pays déjà dans la BDD");
					System.out.println("Pays déjà dans la BDD");
				}
			} catch (SQLException e) {
				logger.error("erreur "+e);
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	public void removeByName(String nomPays) {
		Connection c = MyConnection.getConnection();
		if (c != null) {
			try {
				Statement statement = c.createStatement();
				String request = "SELECT * FROM Pays WHERE nom_pays = '" + nomPays + "';";
				ResultSet result = statement.executeQuery(request);
				if (result.next() == false) {
					logger.warn("Le pays " + nomPays + " n'existe pas");
					System.out.println("Le pays " + nomPays + " n'existe pas");
				} else {
					try {
						request = "DELETE FROM Pays WHERE nom_pays =?;";
						PreparedStatement ps = c.prepareStatement(request);
						ps.setString(1, nomPays);
						ps.executeUpdate();
						logger.info("Suppression de " + nomPays + " dans la table Pays");
						System.out.println("Suppression de " + nomPays + " dans la table Pays");
					}
					// Si au moins une ville est liée au pays, alors impossible de le supprimer, on
					// renverra le message suivant
					catch (SQLIntegrityConstraintViolationException sqle) {
						logger.error("erreur "+sqle);
						System.out.println(
								"Impossible de supprimer le pays, veuillez avant tout supprimer les villes qui y sont associées");
					}
				}
			} catch (SQLException e) {
				logger.error("erreur "+e);
				e.printStackTrace();
			}
		}
	}

	@Override
	public List<Pays> getAll() {
		List<Pays> pays = new ArrayList<>();
		Connection c = MyConnection.getConnection();
		if (c != null) {
			try {
				PreparedStatement ps = c.prepareStatement("SELECT * FROM Pays");
				ResultSet result = ps.executeQuery();
				logger.info("Ajout des pays à la liste");
				while (result.next()) {
					pays.add(new Pays(result.getInt("id"), result.getString("nom_pays"),
							result.getInt("nbre_habitants")));
				}
			} catch (SQLException e) {
				logger.error("erreur "+e);
				e.printStackTrace();
			}
		}
		return pays;
	}

	@Override
	public int getId(String nomPays) {
		int idPays=-1;
		Connection c = MyConnection.getConnection();
		if (c != null) {
			try {
				PreparedStatement statement = c.prepareStatement("SELECT * FROM Pays WHERE nom_pays = ?;");
				statement.setString(1, nomPays);
				ResultSet result = statement.executeQuery();
				logger.info("Récupération de l'id du pays");
				while (result.next()) {
					idPays=result.getInt(1);
				}
			} catch (SQLException e) {
				logger.error("erreur "+e);
				e.printStackTrace();
			}
		}
		return idPays;
	}

	@Override
	public Pays PaysMaxHabitants() {
		Pays pays=new Pays();
		Connection c = MyConnection.getConnection();
		if (c != null) {
			try {
				PreparedStatement statement = c.prepareStatement("SELECT * FROM Pays WHERE nbre_habitants = (SELECT max(nbre_habitants) FROM Pays);");
				ResultSet result = statement.executeQuery();
				while (result.next()) {
					pays = new Pays(result.getInt("id"), result.getString("nom_pays"),result.getInt("nbre_habitants"));
				}
			} catch (SQLException e) {
				logger.error("erreur "+e);
				e.printStackTrace();
			}
		}
		return pays;
	}

}
