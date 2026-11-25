package fr.mines.philidor.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import java.text.DateFormat;
import java.util.List;
import java.util.Locale;

import fr.mines.philidor.PartieJouee;
import fr.mines.philidor.R;
import fr.mines.philidor.databinding.ItemPartieBinding;

public class PartieAdapter extends ArrayAdapter<PartieJouee> {

    private final DateFormat formatDate = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.FRANCE);

    public PartieAdapter(Context context, List<PartieJouee> parties) {
        super(context, 0, parties);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        ItemPartieBinding b;
        if (convertView == null) {
            b = ItemPartieBinding.inflate(LayoutInflater.from(getContext()), parent, false);
            b.getRoot().setTag(b);
        } else {
            b = (ItemPartieBinding) convertView.getTag();
        }
        Context context = getContext();
        PartieJouee partie = getItem(position);
        String couleur = context.getString(partie.isJoueurBlancs() ? R.string.blancs : R.string.noirs);

        switch (partie.getResultat()) {
            case VICTOIRE:
                b.pastille.setBackgroundResource(R.drawable.pastille_victoire);
                b.pastille.setText(R.string.initiale_victoire);
                b.resultat.setText(R.string.victoire);
                break;
            case DEFAITE:
                b.pastille.setBackgroundResource(R.drawable.pastille_defaite);
                b.pastille.setText(R.string.initiale_defaite);
                b.resultat.setText(R.string.defaite);
                break;
            case NULLE:
                b.pastille.setBackgroundResource(R.drawable.pastille_nulle);
                b.pastille.setText(R.string.initiale_nulle);
                b.resultat.setText(R.string.nulle);
                break;
        }
        b.couleur.setText(context.getString(R.string.partie_couleur_jouee, couleur));
        String date = formatDate.format(partie.getDate());
        String fin = context.getString(texteFin(partie.getFin()));
        b.details.setText(context.getString(R.string.partie_details, date, partie.getNombreCoups(), fin));
        b.roi.setText(partie.isJoueurBlancs() ? R.string.roi_blanc : R.string.roi_noir);
        return b.getRoot();
    }

    private int texteFin(PartieJouee.Fin fin) {
        switch (fin) {
            case PAT:
                return R.string.fin_pat;
            case ABANDON:
                return R.string.fin_abandon;
            default:
                return R.string.fin_mat;
        }
    }
}
