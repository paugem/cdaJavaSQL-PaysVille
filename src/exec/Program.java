package exec;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import data.Pays;
import data.PaysDAO;
import data.PaysDAOImpl;
import data.Ville;
import data.VilleDAO;
import data.VilleDAOImpl;

public class Program {
	private static final Logger logger = LoggerFactory.getLogger("exec.Program");
	public static void main(String[] args) throws SQLException {
		PaysDAO paysDAO = new PaysDAOImpl();
		VilleDAO villeDAO = new VilleDAOImpl();

		Scanner sc = new Scanner(System.in);
		String strMenu = "1 : Créer un pays\n" 
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
		while (!option.equals("0")) {
			switch (option) {
			case "1":
				System.out.println("Créer un pays");
				System.out.println("Nom du pays :");
				System.out.print("  < ");
				String nomPays = sc.nextLine().toUpperCase();
				System.out.println("Nombre d'habitants :");
				System.out.print("  < ");
				String test = sc.nextLine();
				if(!test.matches("[0-9]+")) {
					System.out.println("Veuillez saisir un nombre");
					logger.warn("Ce n'est pas un nombre qui a été saisie, retour au menu");	
				}
				else {
					Integer nbHabitants = Integer.parseInt(test);
					logger.info("Données rentrées: nomPays : "+nomPays+" | nbHabitants : "+nbHabitants);				
					Pays pays = paysDAO.save(new Pays(nomPays, nbHabitants));
					if (pays != null) {
						System.out.println("Pays créé avec succès (" + pays.getNumPays() + ")");
					}					
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
				System.out.println("Superficie :");
				System.out.print("  < ");
				Integer superficie = Integer.parseInt(sc.nextLine());
				System.out.println("Nom du pays auquel appartient la ville");
				System.out.print("  < ");
				nomPays = sc.nextLine().toUpperCase();
				Integer idPays = paysDAO.getId(nomPays);
				Ville ville = villeDAO.save(new Ville(nomVille, superficie, idPays));
				if (ville != null) {
					System.out.println("Ville créée avec succès (" + ville.getNumVille() + ")");
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
				List<Ville> allVillesByCountry = villeDAO.getAllByCountry(nomPays);
				if (allVillesByCountry.isEmpty()) {
					System.out.println("Aucune ville dans la BDD associée à ce pays");
				} else {
					allVillesByCountry.stream().forEach(e -> {
						System.out.println("  >  " + e);
					});
				}
				System.out.println();
				System.out.println(strMenu);
				System.out.print("  < ");
				option = sc.nextLine();
				break;
			case "4":
				System.out.println("Lister les pays");
				List<Pays> allPays = paysDAO.getAll();
				if (allPays.isEmpty()) {
					System.out.println("Aucun pays dans la BDD");
				} else {
					allPays.stream().forEach(e -> {
						System.out.println("  >  " + e);
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
				paysDAO.removeByName(nomPays);
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
				villeDAO.removeByName(nomVille);
				System.out.println();
				System.out.println(strMenu);
				System.out.print("  < ");
				option = sc.nextLine();
				break;
			case "7":
				System.out.println("Afficher la plus grande ville et le pays le plus peuplé");
				Pays pays = paysDAO.PaysMaxHabitants();
				ville = villeDAO.villeMaxSuperficie();
				System.out.println("Ville à la plus grande superficie : " + ville.getNomVille() + " - Superficie : "
						+ ville.getSuperficie() + " | Pays ayant le plus grand nombre d\'habitants : "
						+ pays.getNomPays() + " - Nombre d'habitants : " + pays.getNbHabitants());
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
		System.out.println();
		System.out.println("Fermeture de la connexion à la BDD : OK");
		sc.close();
		System.out.println("Fermeture de l'application : OK");

	}

}
