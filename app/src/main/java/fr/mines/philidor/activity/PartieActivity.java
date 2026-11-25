package fr.mines.philidor.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

import fr.mines.philidor.R;
import fr.mines.philidor.databinding.ActivityPartieBinding;
import fr.mines.philidor.moteur.Partie;

public class PartieActivity extends AppCompatActivity {

    public static final String EXTRA_JOUEUR_BLANCS = "fr.mines.philidor.JOUEUR_BLANCS";
    public static final String EXTRA_CREATIVITE = "fr.mines.philidor.CREATIVITE";

    private static final double CREATIVITE_PAR_DEFAUT = 0.6;
    private static final String CLE_COUPS = "coups";

    private ActivityPartieBinding binding;
    private Partie partie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityPartieBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        boolean joueurBlancs = intent.getBooleanExtra(EXTRA_JOUEUR_BLANCS, true);
        double creativite = intent.getDoubleExtra(EXTRA_CREATIVITE, CREATIVITE_PAR_DEFAUT);

        String couleurJoueur = getString(joueurBlancs ? R.string.blancs : R.string.noirs);
        String couleurAdversaire = getString(joueurBlancs ? R.string.noirs : R.string.blancs);
        binding.detailsAdversaire.setText(getString(R.string.partie_adversaire_details, couleurAdversaire, creativite));
        binding.detailsJoueur.setText(couleurJoueur);

        if (savedInstanceState == null) {
            partie = new Partie();
        } else {
            partie = Partie.depuisCoups(savedInstanceState.getStringArrayList(CLE_COUPS));
        }

        binding.echiquier.setBlancsEnBas(joueurBlancs);
        binding.echiquier.setPartie(partie);
        binding.echiquier.setOnClickListener(v -> afficherPartie());
        binding.annuler.setOnClickListener(v -> annulerCoup());
        afficherPartie();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle etat) {
        super.onSaveInstanceState(etat);
        etat.putStringArrayList(CLE_COUPS, new ArrayList<>(partie.coups()));
    }

    private void annulerCoup() {
        partie.annuler();
        binding.echiquier.rafraichir();
        afficherPartie();
    }

    private void afficherPartie() {
        binding.statut.setText(texteStatut());
        binding.coups.setText(texteCoups());
        binding.annuler.setEnabled(!partie.coups().isEmpty());
        binding.defilementCoups.post(() -> binding.defilementCoups.fullScroll(View.FOCUS_DOWN));
    }

    private String texteStatut() {
        String trait = getString(partie.traitAuxBlancs() ? R.string.blancs : R.string.noirs);
        String autre = getString(partie.traitAuxBlancs() ? R.string.noirs : R.string.blancs);
        switch (partie.etat()) {
            case MAT:
                return getString(R.string.statut_mat, autre);
            case PAT:
                return getString(R.string.statut_pat);
            case NULLE_MATERIEL:
                return getString(R.string.statut_nulle_materiel);
            case NULLE_50_COUPS:
                return getString(R.string.statut_nulle_50_coups);
            case NULLE_REPETITION:
                return getString(R.string.statut_nulle_repetition);
            default:
                if (partie.enEchec()) {
                    return getString(R.string.statut_echec);
                }
                return getString(R.string.statut_trait, trait);
        }
    }

    private String texteCoups() {
        List<String> coups = partie.coupsSan();
        StringBuilder texte = new StringBuilder();
        for (int i = 0; i < coups.size(); i++) {
            if (i % 2 == 0) {
                texte.append(getString(R.string.numero_coup, i / 2 + 1));
            }
            texte.append(coups.get(i)).append(' ');
        }
        return texte.toString().trim();
    }
}
