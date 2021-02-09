package exec;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.List;
import java.util.Scanner;

import data.DAO;
import data.Pays;
import data.PaysDAOImpl;
import data.Ville;
import data.VilleDAO;
import data.VilleDAOImpl;

public class Program {
	public static void main(String[] args) throws SQLException {
		DAO<Pays> PaysDAO= new PaysDAOImpl();
		VilleDAO VilleDAO= new VilleDAOImpl();
		
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
							System.out.println("Nombre d'habitants :");
							System.out.print("  < ");
							Integer nbHabitants = Integer.parseInt(sc.nextLine());
							Pays pays = PaysDAO.save(new Pays(nomPays,nbHabitants));
							if(pays!=null) {
								System.out.println("Pays créé avec succès ("+pays.getNumPays()+")");
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
//							// Vérification de la présence de la ville en BDD
//							statement = connexion.createStatement();
//							request = "SELECT * FROM Ville WHERE nom_ville = '"+nomVille+"';";
//							result = statement.executeQuery(request);
//							// Si elle n'existe pas
//							if(result.next()==false) {
//								System.out.println("Superficie :");
//								System.out.print("  < ");
//								Integer superficie = Integer.parseInt(sc.nextLine());
//								System.out.println("Nom du pays auquel appartient la ville");
//								System.out.print("  < ");
//								nomPays = sc.nextLine().toUpperCase();
//								// Vérification de l'existence du pays associé à la ville
//								statement = connexion.createStatement();
//								request = "SELECT * FROM Pays WHERE nom_pays = '"+nomPays+"';";
//								result = statement.executeQuery(request);
//								// Si le pays n'existe pas
//								if(result.next()==false) {
//									System.out.println("Le pays n'existe pas dans la base de données");
//								}
//								else {
//									int idPays = result.getInt("id");
//									request = "INSERT INTO Ville (nom_ville,superficie,id_pays) VALUES (?,?,?);";
//									PreparedStatement ps = connexion.prepareStatement(request,PreparedStatement.RETURN_GENERATED_KEYS);
//									ps.setString(1, nomVille);
//									ps.setInt(2, superficie);
//									ps.setInt(3,idPays);
//									ps.executeUpdate();
//									ResultSet resultat = ps.getGeneratedKeys();
//									if (resultat.next()) {
//										System.out.println("ID généré pour "+nomVille+" : " + resultat.getInt(1));
//									}
//								}
//								
//							}
//							// Si la ville existe déjà
//							else {
//								System.out.println("La ville "+nomVille+" existe déjà");
//							}
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
							List<Ville> allVillesByCountry = VilleDAO.getAllByCountry(nomPays);
							if(allVillesByCountry.isEmpty()) {
								System.out.println("Aucune ville dans la BDD associée à ce pays");
							}
							else {
								allVillesByCountry.stream().forEach(e->{
									System.out.println("  >  "+e);
								});
							}
							System.out.println();
							System.out.println(strMenu);
							System.out.print("  < ");
							option = sc.nextLine(); 
							break;
						case "4":
							System.out.println("Lister les pays");
							List<Pays> allPays = PaysDAO.getAll();
							if(allPays.isEmpty()) {
								System.out.println("Aucun pays dans la BDD");
							}
							else {
								allPays.stream().forEach(e->{
									System.out.println("  >  "+e);
								});
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
							PaysDAO.removeByName(nomPays);
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
							VilleDAO.removeByName(nomVille);
							System.out.println();
							System.out.println(strMenu);
							System.out.print("  < ");
							option = sc.nextLine();
							break;
						case "7":
							System.out.println("Afficher la plus grande ville et le pays le plus peuplé");
							Statement statement = connexion.createStatement();
							String request = "SELECT nom_ville, superficie, nom_pays, nbre_habitants "
									+ "FROM Ville,Pays "
									+ "WHERE Ville.superficie=(SELECT max(superficie) FROM Ville) AND nbre_habitants=(SELECT max(nbre_habitants) FROM Pays);";
							ResultSet result = statement.executeQuery(request);
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
							break;
						default:
							System.out.println("Entrez un chiffre compris entre 0 et 7 (inclus) : ");
							System.out.print("  < ");
							option = sc.nextLine();
							break;
						}
					}
					System.out.println("Fermeture de la connexion à la BDD");
					System.out.println();
					connexion.close();
					sc.close();
				} 
				catch (SQLException ignore) {
					ignore.printStackTrace();
				}
		}
	}

}
