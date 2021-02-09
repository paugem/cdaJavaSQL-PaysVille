package exec;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.Scanner;

public class Program {
	public static void main(String[] args) throws SQLException {
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println(e.getMessage());
		}
		String url = "jdbc:mysql://localhost:3306/cda_langues?useSSL=false&serverTimezone=UTC";
		String user = "cda1";
		String password = "cda1pwd";
		Connection connexion = null;
		try {
			connexion = DriverManager.getConnection(url, user, password);
		} catch (SQLException e) {
			System.out.println("Connexion impossible à la base de données");
			e.printStackTrace();
		} finally {
			if (connexion != null)
				try {
					Scanner sc = new Scanner(System.in);
					String strMenu="1 : Créer un pays\n"
								 + "2 : Créer une ville\n"
								 + "3 : Lister les villes d'un pays\n"
								 + "4 : Lister les pays\n"
								 + "5 : Supprimer un pays\n"
								 + "6 : Supprimer une ville\n"
								 + "7 : Afficher la plus grande ville et le pays le plus peuplé\n"
								 + "0 : Quitter";
					System.out.println(strMenu);
					System.out.print("  < ");
					String option = sc.nextLine();
					while(!option.equals("0")) {
						switch(option) {
						case "1":
							System.out.println("Créer un pays");
							System.out.println("Nom du pays :");
							System.out.print("  < ");
							String nomPays = sc.nextLine().toUpperCase();
							Statement statement = connexion.createStatement();
							String request = "SELECT * FROM Pays WHERE nom_pays = '"+nomPays+"';";
							ResultSet result = statement.executeQuery(request);
							if(result.next()==false) {
								System.out.println("Nombre d'habitants :");
								System.out.print("  < ");
								Integer nbHabitants = Integer.parseInt(sc.nextLine());
								request = "INSERT INTO Pays (nom_pays,nbre_habitants) VALUES (?,?);";
								PreparedStatement ps = connexion.prepareStatement(request,PreparedStatement.RETURN_GENERATED_KEYS);
								ps.setString(1, nomPays);
								ps.setInt(2, nbHabitants);
								ps.executeUpdate();
								ResultSet resultat = ps.getGeneratedKeys();
								if (resultat.next()) {
									System.out.println("ID généré pour "+nomPays+" : " + resultat.getInt(1));
								}
							}
							else {
								System.out.println("Le pays "+nomPays+" existe déjà");
							}
							System.out.println();
							System.out.println(strMenu);
							System.out.print("  < ");
							option = sc.nextLine();
							break;
						case "2":
							System.out.println("Créer une ville");
							System.out.println("Nom de la ville :");
							System.out.print("  < ");
							String nomVille = sc.nextLine().toUpperCase();
							statement = connexion.createStatement();
							request = "SELECT * FROM Ville WHERE nom_ville = '"+nomVille+"';";
							result = statement.executeQuery(request);
							if(result.next()==false) {
								System.out.println("Superficie :");
								System.out.print("  < ");
								Integer superficie = Integer.parseInt(sc.nextLine());
								System.out.println("Nom du pays auquel appartient la ville");
								System.out.print("  < ");
								nomPays = sc.nextLine().toUpperCase();
								statement = connexion.createStatement();
								request = "SELECT * FROM Pays WHERE nom_pays = '"+nomPays+"';";
								result = statement.executeQuery(request);
								if(result.next()==false) {
									System.out.println("Le pays n'existe pas dans la base de données");
								}
								else {
									int idPays = result.getInt("id");
									request = "INSERT INTO Ville (nom_ville,superficie,id_pays) VALUES (?,?,?);";
									PreparedStatement ps = connexion.prepareStatement(request,PreparedStatement.RETURN_GENERATED_KEYS);
									ps.setString(1, nomVille);
									ps.setInt(2, superficie);
									ps.setInt(3,idPays);
									ps.executeUpdate();
									ResultSet resultat = ps.getGeneratedKeys();
									if (resultat.next()) {
										System.out.println("ID généré pour "+nomVille+" : " + resultat.getInt(1));
									}
								}
								
							}
							else {
								System.out.println("La ville "+nomVille+" existe déjà");
							}
							System.out.println();
							System.out.println(strMenu);
							System.out.print("  < ");
							option = sc.nextLine();
							break;
						case "3":
							System.out.println("Liste des villes d'un pays");
							System.out.println("Quel pays ? : ");
							System.out.print("  < ");
							nomPays = sc.nextLine();
							statement = connexion.createStatement();
							request = "SELECT * FROM Pays WHERE nom_pays = '"+nomPays+"';";
							result = statement.executeQuery(request);
							if(result.next()==false) {
								System.out.println("Le pays n'existe pas dans la base de données");
							}
							else {
								request = "SELECT * FROM Ville INNER JOIN Pays ON Ville.id_pays = Pays.id AND Pays.nom_pays ='"+nomPays+"';";
								result = statement.executeQuery(request);
								while (result.next()) {
									int idVille = result.getInt(1);
									nomVille = result.getString(2);
									int superficie = result.getInt(3);
									System.out.println("id : "+idVille + " | " + nomVille + " | superficie : " +superficie);
								}
							}
							System.out.println();
							System.out.println(strMenu);
							System.out.print("  < ");
							option = sc.nextLine(); 
							break;
						case "4":
							System.out.println("Lister les pays");
							statement = connexion.createStatement();
							request = "SELECT * FROM Pays;";
							result = statement.executeQuery(request);
							while (result.next()) {
								int idPays = result.getInt(1);
								nomPays = result.getString(2);
								int nbHab = result.getInt(3);
								System.out.println("id : "+idPays + " | " + nomPays + " | nombre d'habitants : " +nbHab);
							}
							System.out.println();
							System.out.println(strMenu);
							System.out.print("  < ");
							option = sc.nextLine();
							break;
						case "5":
							System.out.println("Supprimer un pays");
							System.out.println("Nom du pays à supprimer:");
							System.out.print("  < ");
							nomPays = sc.nextLine().toUpperCase();
							statement = connexion.createStatement();
							request = "SELECT * FROM Pays WHERE nom_pays = '"+nomPays+"';";
							result = statement.executeQuery(request);
							if(result.next()==false) {
								System.out.println("Le pays "+nomPays+" n'existe pas");
							}
							else {
								try {
									request = "DELETE FROM Pays WHERE nom_pays =?;";
									PreparedStatement ps = connexion.prepareStatement(request);
									ps.setString(1,nomPays);
									ps.executeUpdate();
									System.out.println("Suppression de "+nomPays+" dans la table Pays");
								}
								catch(SQLIntegrityConstraintViolationException sqle) {
									System.out.println("Impossible de supprimer le pays, veuillez avant tout supprimer les villes qui y sont associées");
								}								
							}
							System.out.println();
							System.out.println(strMenu);
							System.out.print("  < ");
							option = sc.nextLine();
							break;
						case "6":
							System.out.println("Supprimer une ville");
							System.out.println("Nom de la ville à supprimer:");
							System.out.print("  < ");
							nomVille = sc.nextLine().toUpperCase();
							statement = connexion.createStatement();
							request = "SELECT * FROM Ville WHERE nom_ville = '"+nomVille+"';";
							result = statement.executeQuery(request);
							if(result.next()==false) {
								System.out.println("La ville n'existe pas");
							}
							else {
								request = "DELETE FROM Ville WHERE nom_ville =?;";
								PreparedStatement ps = connexion.prepareStatement(request);
								ps.setString(1,nomVille);
								ps.executeUpdate();
							}
							System.out.println();
							System.out.println(strMenu);
							System.out.print("  < ");
							option = sc.nextLine();
							break;
						case "7":
							System.out.println("Afficher la plus grande ville et le pays le plus peuplé");
							statement = connexion.createStatement();
							request = "SELECT nom_ville, superficie, nom_pays, nbre_habitants "
									+ "FROM Ville,Pays "
									+ "WHERE Ville.superficie=(SELECT max(superficie) FROM Ville) AND nbre_habitants=(SELECT max(nbre_habitants) FROM Pays);";
							result = statement.executeQuery(request);
							while (result.next()) {
								nomVille = result.getString(1);
								int superficie = result.getInt(2);
								nomPays = result.getString(3);
								int nbHab = result.getInt(4);
								System.out.println("Ville à la plus grande superficie : "+nomVille+" - Superficie : "+superficie+" | Pays ayant le plus grand nombre d\'habitants : "+ nomPays + " - Nombre d'habitants : " +nbHab);
							}
							System.out.println();
							System.out.println(strMenu);
							System.out.print("  < ");
							option = sc.nextLine();
							break;
						case "0":
							System.out.println("Fermeture de la connexion à la BDD");
							System.out.println();
							connexion.close();
							break;
						default:
							System.out.println("Entrez un chiffre compris entre 0 et 7 (inclus) : ");
							System.out.print("  < ");
							option = sc.nextLine();
							break;
						}
					}
					sc.close();
				} catch (SQLException ignore) {
					ignore.printStackTrace();
				}
		}
		

	}

}
