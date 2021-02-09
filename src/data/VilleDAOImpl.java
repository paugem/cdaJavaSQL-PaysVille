package data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class VilleDAOImpl implements VilleDAO {

	@Override
	public Ville save(Ville ville) {
		// TODO Auto-generated method stub
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
			} 
			catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public List<Ville> getAll() {
		List<Ville> ville = new ArrayList<>();
		Connection c = MyConnection.getConnection();
		if (c != null) {
			try {
				PreparedStatement statement = c.prepareStatement("SELECT * FROM Ville");
				ResultSet result = statement.executeQuery();
				while (result.next()) {
					ville.add(new Ville(result.getInt("id"), result.getString("nom_ville"),result.getInt("superficie"),result.getInt("id_pays")));
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return ville;
	}

	@Override
	public List<Ville> getAllByCountry(String nomPays) {
		List<Ville> ville = new ArrayList<>();
		Connection c = MyConnection.getConnection();
		if (c != null) {
			try {
				// Vérification de l'existence du pays
				Statement statement = c.createStatement();
				String request = "SELECT * FROM Pays WHERE nom_pays = '"+nomPays+"';";
				ResultSet result = statement.executeQuery(request);
				if(result.next()==false) {
					System.out.println("Le pays n'existe pas dans la base de données");
				}
				else {
					request = "SELECT * FROM Ville INNER JOIN Pays ON Ville.id_pays = Pays.id AND Pays.nom_pays ='"+nomPays+"';";
					result = statement.executeQuery(request);
					while (result.next()) {
						ville.add(new Ville(result.getInt("id"), result.getString("nom_ville"),result.getInt("superficie"),result.getInt("id_pays")));
					}
				}
			}catch (SQLException e) {
				e.printStackTrace();
			}
		}	
		return ville;
	}

}
