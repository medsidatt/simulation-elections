import dao.*;
import model.Candidate;
import model.Electeur;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Election {
    private final GenericDAO<Candidate, Integer> candidateDAO;
    private final ElecteurDAO electeurDAO;
    private final VoteDAO voteDAO;

    public Election(GenericDAO<Candidate, Integer> candidateDAO, ElecteurDAO electeurDAO, VoteDAO voteDAO) {
        this.candidateDAO = candidateDAO;
        this.electeurDAO = electeurDAO;
        this.voteDAO = voteDAO;
    }

    public void addCandidate(Candidate candidate) {
        candidateDAO.save(candidate);
    }

    public void removeCandidate(int id) {
        candidateDAO.delete(id);
    }

    public void enregistrerElecteur(Electeur electeur) {
        electeurDAO.save(electeur);
    }

    public void vote(String candidateName, String electeurNumeroId) {
        Electeur electeur = electeurDAO.findByNumeroIdentification(electeurNumeroId);
        if (electeur == null) {
            System.out.println("Électeur non trouvé.");
            return;
        }
        if (electeurDAO.aVote(electeurNumeroId)) {
            System.out.println("Cet électeur a déjà voté.");
            return;
        }

        List<Candidate> candidates = candidateDAO.findAll();
        Candidate selectedCandidate = null;
        for (Candidate c : candidates) {
            if (c.getName().equalsIgnoreCase(candidateName)) {
                selectedCandidate = c;
                break;
            }
        }

        if (selectedCandidate == null) {
            System.out.println("Candidat non trouvé.");
            return;
        }

        voteDAO.saveVote(selectedCandidate.getId(), electeur.getId());
        electeurDAO.marquerCommeAyantVote(electeurNumeroId);
        System.out.println("Vote enregistré avec succès.");
    }

    public List<Candidate> getCandidates() {
        return candidateDAO.findAll();
    }

    public List<Electeur> getElecteurs() {
        return electeurDAO.findAll();
    }

    public void displayResults() {
        List<Candidate> candidates = getCandidates();
        int totalVotes = getTotalVotes();
        System.out.println("\nRésultats globaux :");
        for (Candidate c : candidates) {
            double percentage = totalVotes > 0 ? (c.getVotes() * 100.0 / totalVotes) : 0;
            System.out.printf("%s (%s) : %d votes (%.2f%%)%n", c.getName(), c.getParty(), c.getVotes(), percentage);
        }
        determineWinner();
    }

    public void determineWinner() {
        List<Candidate> candidates = getCandidates();
        int totalVotes = getTotalVotes();
        Candidate winner = null;
        Candidate second = null;

        for (Candidate c : candidates) {
            if (winner == null || c.getVotes() > winner.getVotes()) {
                second = winner;
                winner = c;
            } else if (second == null || c.getVotes() > second.getVotes()) {
                second = c;
            }
        }

        if (totalVotes > 0 && winner != null) {
            double winnerPercentage = winner.getVotes() * 100.0 / totalVotes;
            if (winnerPercentage > 50) {
                System.out.println("Gagnant : " + winner.getName() + " avec " + winnerPercentage + "% des voix.");
            } else {
                System.out.println("Aucun candidat n'a obtenu plus de 50%. Deuxième tour requis.");
                System.out.println("Candidats admis au deuxième tour : " + winner.getName() + " et " + second.getName());
            }
        }
    }

    public void displayResultsByCity() {
        List<Candidate> candidates = getCandidates();
        List<Electeur> electeurs = getElecteurs();
        Map<String, Map<Integer, Integer>> votesByCity = new HashMap<>();

        for (Electeur e : electeurs) {
            votesByCity.putIfAbsent(e.getVille(), new HashMap<>());
        }

        // À compléter avec une requête SQL pour récupérer les votes par ville
        System.out.println("\nRésultats par ville : (À implémenter avec une requête SQL)");
    }

    public int getTotalVotes() {
        int total = 0;
        for (Candidate c : getCandidates()) {
            total += c.getVotes();
        }
        return total;
    }
}