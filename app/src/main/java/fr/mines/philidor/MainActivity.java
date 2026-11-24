package fr.mines.philidor;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Random;

import fr.mines.philidor.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private final Random hasard = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        afficherCreativite(binding.creativite.getValue());
        binding.creativite.addOnChangeListener(
                (slider, valeur, parUtilisateur) -> afficherCreativite(valeur));

        binding.nouvellePartie.setOnClickListener(v -> lancerPartie());
        binding.historique.setOnClickListener(v ->
                startActivity(new Intent(this, HistoriqueActivity.class)));
    }

    private void afficherCreativite(float valeur) {
        binding.valeurCreativite.setText(getString(R.string.creativite_valeur, valeur));
    }

    private void lancerPartie() {
        Intent intent = new Intent(this, PartieActivity.class);
        intent.putExtra(PartieActivity.EXTRA_JOUEUR_BLANCS, joueurAvecLesBlancs());
        intent.putExtra(PartieActivity.EXTRA_CREATIVITE, creativite());
        startActivity(intent);
    }

    private boolean joueurAvecLesBlancs() {
        int id = binding.couleur.getCheckedButtonId();
        if (id == R.id.noirs) {
            return false;
        }
        if (id == R.id.hasard) {
            return hasard.nextBoolean();
        }
        return true;
    }

    private float creativite() {
        return binding.creativite.getValue();
    }
}
