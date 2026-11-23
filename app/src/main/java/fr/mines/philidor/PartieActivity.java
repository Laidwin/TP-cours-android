package fr.mines.philidor;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import fr.mines.philidor.databinding.ActivityPartieBinding;

public class PartieActivity extends AppCompatActivity {

    public static final String EXTRA_JOUEUR_BLANCS = "fr.mines.philidor.JOUEUR_BLANCS";
    public static final String EXTRA_CREATIVITE = "fr.mines.philidor.CREATIVITE";

    private static final double CREATIVITE_PAR_DEFAUT = 0.6;

    private ActivityPartieBinding binding;

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

        String couleur = getString(joueurBlancs ? R.string.blancs : R.string.noirs);
        binding.couleurJoueur.setText(getString(R.string.partie_couleur, couleur));
        binding.creativite.setText(getString(R.string.partie_creativite, creativite));
    }
}
