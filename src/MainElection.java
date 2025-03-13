import dao.*;
import model.Candidate;
import model.Electeur;

import java.sql.Connection;
import java.util.Scanner;

public class MainElection {
    public static void main(String[] args) {
        DatabaseConnection dbConnection = DatabaseConnection.getInstance();
        Connection conn = dbConnection.getConnection();

        if (conn == null) {
            System.out.println("Échec de la connexion à MySQL. Programme terminé.");
            return;
        }

        CandidateDAO candidateDAO = new CandidateDAO(conn);
        ElecteurDAO electeurDAO = new ElecteurDAO(conn);
        VoteDAO voteDAO = new VoteDAO(conn);
        Election election = new Election(candidateDAO, electeurDAO, voteDAO);

        Scanner scanner = new Scanner(System.in);
        try {
            while (true) {
                displayMenu();
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consomme le retour à la ligne

                switch (choice) {
                    case 1:
                        System.out.println("Liste des candidats :");
                        election.getCandidates().forEach(System.out::println);
                        break;
                    case 2:
                        System.out.print("Nom du candidat : ");
                        String candidateName = scanner.nextLine();
                        System.out.print("ID de l'électeur : ");
                        String electeurId = scanner.nextLine();
                        election.vote(candidateName, electeurId);
                        break;
                    case 3:
                        System.out.print("ID du candidat à supprimer : ");
                        int idToDelete = scanner.nextInt();
                        election.removeCandidate(idToDelete);
                        break;
                    case 4:
                        election.displayResults();
                        break;
                    case 5:
                        System.out.print("Nom du candidat : ");
                        String name = scanner.nextLine();
                        System.out.print("Parti : ");
                        String party = scanner.nextLine();
                        election.addCandidate(new Candidate(name, party));
                        break;
                    case 6:
                        System.out.print("Numéro d'identification : ");
                        String numeroId = scanner.nextLine();
                        System.out.print("Nom : ");
                        String nom = scanner.nextLine();
                        System.out.print("Ville : ");
                        String ville = scanner.nextLine();
                        election.enregistrerElecteur(new Electeur(numeroId, nom, ville));
                        break;
                    case 7:
                        System.out.println("Liste des électeurs :");
                        election.getElecteurs().forEach(System.out::println);
                        break;
                    case 8:
                        election.displayResultsByCity();
                        break;
                    case 9:
                        System.out.println("Au revoir !");
                        dbConnection.closeConnection();
                        return;
                    default:
                        System.out.println("Choix invalide.");
                }
            }
        } finally {
            scanner.close();
            dbConnection.closeConnection();
        }
    }

    private static void displayMenu() {
        System.out.println("\nMenu :");
        System.out.println("1. Afficher la liste des candidats");
        System.out.println("2. Voter pour un candidat");
        System.out.println("3. Supprimer un candidat");
        System.out.println("4. Afficher les résultats");
        System.out.println("5. Ajouter un candidat");
        System.out.println("6. Enregistrer un électeur");
        System.out.println("7. Afficher la liste des électeurs");
        System.out.println("8. Afficher les résultats par ville");
        System.out.println("9. Quitter");
        System.out.print("Choix : ");
    }
}