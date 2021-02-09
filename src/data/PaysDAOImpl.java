package data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PaysDAOImpl implements DAO<Pays> {
	@Override
	public Pays save(Pays pays) {
		Connection c = MyConnection.getConnection();
		if (c != null) {
			try {
				Statement statement = c.createStatement();
				String request = "SELECT * FROM Pays WHERE nom_pays = '" + pays.getNomPays() + "';";
				ResultSet result = statement.executeQuery(request);
				if (result.next() == false) {
					PreparedStatement ps = c.prepareStatement("insert into Pays (nom_pays,nbre_habitants) values (?,?); ",PreparedStatement.RETURN_GENERATED_KEYS);
					ps.setString(1, pays.getNomPays());
					ps.setInt(2, pays.getNbHabitants());
					ps.executeUpdate();
					ResultSet resultat = ps.getGeneratedKeys();
					if (resultat.next()) {
						pays.setNumPays(resultat.getInt(1));
						return pays;
					}
				} else {
					System.out.println("Pays déjà dans la BDD");
				}
			} catch (SQLException e) {
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
					System.out.println("Le pays " + nomPays + " n'existe pas");
				} else {
					try {
						request = "DELETE FROM Pays WHERE nom_pays =?;";
						PreparedStatement ps = c.prepareStatement(request);
						ps.setString(1, nomPays);
						ps.executeUpdate();
						System.out.println("Suppression de " + nomPays + " dans la table Pays");
					}
					// Si au moins une ville est liée au pays, alors impossible de le supprimer, on
					// renverra le message suivant
					catch (SQLIntegrityConstraintViolationException sqle) {
						System.out.println(
								"Impossible de supprimer le pays, veuillez avant tout supprimer les villes qui y sont associées");
					}
				}
			} catch (SQLException e) {
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
				PreparedStatement statement = c.prepareStatement("SELECT * FROM Pays");
				ResultSet result = statement.executeQuery();
				while (result.next()) {
					pays.add(new Pays(result.getInt("id"), result.getString("nom_pays"),
							result.getInt("nbre_habitants")));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return pays;
	}

}
