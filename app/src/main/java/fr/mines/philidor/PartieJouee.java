package fr.mines.philidor;

import java.util.Date;

public class PartieJouee {

    public enum Resultat {VICTOIRE, DEFAITE, NULLE}

    public enum Fin {MAT, PAT, ABANDON}

    private final Resultat resultat;
    private final boolean joueurBlancs;
    private final Date date;
    private final int nombreCoups;
    private final Fin fin;

    public PartieJouee(Resultat resultat, boolean joueurBlancs, Date date, int nombreCoups, Fin fin) {
        this.resultat = resultat;
        this.joueurBlancs = joueurBlancs;
        this.date = date;
        this.nombreCoups = nombreCoups;
        this.fin = fin;
    }

    public Resultat getResultat() {
        return resultat;
    }

    public boolean isJoueurBlancs() {
        return joueurBlancs;
    }

    public Date getDate() {
        return date;
    }

    public int getNombreCoups() {
        return nombreCoups;
    }

    public Fin getFin() {
        return fin;
    }
}
