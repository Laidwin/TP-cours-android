package fr.mines.philidor.activity;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import fr.mines.philidor.PartieJouee;
import fr.mines.philidor.PartieJouee.Fin;
import fr.mines.philidor.PartieJouee.Resultat;
import fr.mines.philidor.R;
import fr.mines.philidor.adapter.PartieAdapter;
import fr.mines.philidor.databinding.ActivityHistoriqueBinding;

public class HistoriqueActivity extends AppCompatActivity {

    private ActivityHistoriqueBinding binding;
    private PartieAdapter adaptateur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityHistoriqueBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        adaptateur = new PartieAdapter(this, partiesInventees());
        binding.liste.setAdapter(adaptateur);
        binding.liste.setEmptyView(binding.vide);
        binding.liste.setOnItemLongClickListener((parent, vue, position, id) -> {
            proposerSuppression(adaptateur.getItem(position));
            return true;
        });
    }

    private void proposerSuppression(PartieJouee partie) {
        new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.suppression_titre)
                .setMessage(R.string.suppression_message)
                .setNegativeButton(R.string.annuler, null)
                .setPositiveButton(R.string.supprimer, (dialogue, quel) -> adaptateur.remove(partie))
                .show();
    }

    private List<PartieJouee> partiesInventees() {
        List<PartieJouee> parties = new ArrayList<>();
        parties.add(new PartieJouee(Resultat.VICTOIRE, true, new GregorianCalendar(2026, Calendar.NOVEMBER, 22).getTime(), 41, Fin.MAT));
        parties.add(new PartieJouee(Resultat.DEFAITE, false, new GregorianCalendar(2026, Calendar.NOVEMBER, 22).getTime(), 28, Fin.MAT));
        parties.add(new PartieJouee(Resultat.NULLE, true, new GregorianCalendar(2026, Calendar.NOVEMBER, 21).getTime(), 63, Fin.PAT));
        parties.add(new PartieJouee(Resultat.DEFAITE, true, new GregorianCalendar(2026, Calendar.NOVEMBER, 21).getTime(), 19, Fin.ABANDON));
        parties.add(new PartieJouee(Resultat.VICTOIRE, false, new GregorianCalendar(2026, Calendar.NOVEMBER, 20).getTime(), 52, Fin.MAT));
        parties.add(new PartieJouee(Resultat.DEFAITE, false, new GregorianCalendar(2026, Calendar.NOVEMBER, 20).getTime(), 35, Fin.MAT));
        parties.add(new PartieJouee(Resultat.VICTOIRE, true, new GregorianCalendar(2026, Calendar.NOVEMBER, 18).getTime(), 15, Fin.ABANDON));
        parties.add(new PartieJouee(Resultat.NULLE, false, new GregorianCalendar(2026, Calendar.NOVEMBER, 17).getTime(), 48, Fin.PAT));
        parties.add(new PartieJouee(Resultat.DEFAITE, true, new GregorianCalendar(2026, Calendar.NOVEMBER, 15).getTime(), 24, Fin.MAT));
        parties.add(new PartieJouee(Resultat.VICTOIRE, false, new GregorianCalendar(2026, Calendar.NOVEMBER, 12).getTime(), 37, Fin.MAT));
        parties.add(new PartieJouee(Resultat.DEFAITE, true, new GregorianCalendar(2026, Calendar.NOVEMBER, 9).getTime(), 31, Fin.ABANDON));
        parties.add(new PartieJouee(Resultat.DEFAITE, false, new GregorianCalendar(2026, Calendar.NOVEMBER, 4).getTime(), 44, Fin.MAT));
        return parties;
    }
}
