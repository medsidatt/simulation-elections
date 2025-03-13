package model;

public class Electeur {
    private int id;
    private String numeroIdentification;
    private String nom;
    private String ville;
    private boolean aVote;

    public Electeur(String numeroIdentification, String nom, String ville) {
        this.numeroIdentification = numeroIdentification;
        this.nom = nom;
        this.ville = ville;
        this.aVote = false;
    }

    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNumeroIdentification() { return numeroIdentification; }
    public void setNumeroIdentification(String numeroIdentification) { this.numeroIdentification = numeroIdentification; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getVille() { return ville; }
    public void setVille(String ville) { this.ville = ville; }
    public boolean isAVote() { return aVote; }
    public void setAVote(boolean aVote) { this.aVote = aVote; }

    @Override
    public String toString() {
        return "Electeur{id=" + id + ", numeroIdentification='" + numeroIdentification + "', nom='" + nom + "', ville='" + ville + "', aVote=" + aVote + "}";
    }
}