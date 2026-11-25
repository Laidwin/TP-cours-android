package fr.mines.philidor.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

import fr.mines.philidor.R;
import fr.mines.philidor.moteur.Partie;

public class EchiquierView extends View {

    private static final String LETTRES_PIECES = "KQRBNPkqrbnp";

    private final Paint pinceauCase = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint pinceauPiece = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint pinceauCoordonnee = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint pinceauIndication = new Paint(Paint.ANTI_ALIAS_FLAG);

    private int couleurClaire;
    private int couleurFoncee;
    private int couleurDernierCoup;
    private int couleurSelection;
    private String symbolesPieces;

    private float taille;
    private float decalagePiece;

    private Partie partie = new Partie();
    private boolean blancsEnBas = true;
    private int colonneSelection = -1;
    private int ligneSelection = -1;
    private List<String> coupsSelection = new ArrayList<>();

    public EchiquierView(Context c) {
        super(c);
        preparer(c);
    }

    public EchiquierView(Context c, AttributeSet a) {
        super(c, a);
        preparer(c);
    }

    private void preparer(Context c) {
        couleurClaire = ContextCompat.getColor(c, R.color.case_claire);
        couleurFoncee = ContextCompat.getColor(c, R.color.case_foncee);
        couleurDernierCoup = ContextCompat.getColor(c, R.color.case_dernier_coup);
        couleurSelection = ContextCompat.getColor(c, R.color.case_selection);
        symbolesPieces = c.getString(R.string.symboles_pieces);

        pinceauCase.setStyle(Paint.Style.FILL);
        pinceauPiece.setColor(ContextCompat.getColor(c, R.color.black));
        pinceauPiece.setTextAlign(Paint.Align.CENTER);
        pinceauIndication.setColor(ContextCompat.getColor(c, R.color.indication_coup));
        pinceauCoordonnee.setFakeBoldText(true);
    }

    public void setPartie(Partie partie) {
        this.partie = partie;
        deselectionner();
        invalidate();
    }

    public void setBlancsEnBas(boolean blancsEnBas) {
        this.blancsEnBas = blancsEnBas;
        invalidate();
    }

    public void rafraichir() {
        deselectionner();
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int ancienW, int ancienH) {
        super.onSizeChanged(w, h, ancienW, ancienH);
        taille = Math.min(w, h) / 8f;
        pinceauPiece.setTextSize(taille * 0.8f);
        pinceauCoordonnee.setTextSize(taille * 0.2f);
        pinceauIndication.setStrokeWidth(taille * 0.08f);
        decalagePiece = (pinceauPiece.descent() + pinceauPiece.ascent()) / 2;
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        for (int colonne = 0; colonne < 8; colonne++) {
            for (int ligne = 0; ligne < 8; ligne++) {
                dessinerCase(canvas, colonne, ligne);
            }
        }
        for (String coup : coupsSelection) {
            dessinerIndication(canvas, colonneArrivee(coup), ligneArrivee(coup));
        }
    }

    private void dessinerCase(Canvas canvas, int colonne, int ligne) {
        float x = gauche(colonne);
        float y = haut(ligne);
        boolean claire = (colonne + ligne) % 2 == 1;

        if (colonne == colonneSelection && ligne == ligneSelection) {
            pinceauCase.setColor(couleurSelection);
        } else if (dansDernierCoup(colonne, ligne)) {
            pinceauCase.setColor(couleurDernierCoup);
        } else if (claire) {
            pinceauCase.setColor(couleurClaire);
        } else {
            pinceauCase.setColor(couleurFoncee);
        }
        canvas.drawRect(x, y, x + taille, y + taille, pinceauCase);

        pinceauCoordonnee.setColor(claire ? couleurFoncee : couleurClaire);
        if (colonne == (blancsEnBas ? 0 : 7)) {
            pinceauCoordonnee.setTextAlign(Paint.Align.LEFT);
            canvas.drawText(String.valueOf(ligne + 1), x + taille * 0.06f, y + taille * 0.24f, pinceauCoordonnee);
        }
        if (ligne == (blancsEnBas ? 0 : 7)) {
            pinceauCoordonnee.setTextAlign(Paint.Align.RIGHT);
            canvas.drawText(String.valueOf((char) ('a' + colonne)), x + taille * 0.94f, y + taille * 0.94f, pinceauCoordonnee);
        }

        char piece = partie.piece(colonne, ligne);
        if (piece != ' ') {
            int index = LETTRES_PIECES.indexOf(piece);
            String symbole = String.valueOf(symbolesPieces.charAt(index));
            canvas.drawText(symbole, x + taille / 2, y + taille / 2 - decalagePiece, pinceauPiece);
        }
    }

    private void dessinerIndication(Canvas canvas, int colonne, int ligne) {
        float cx = gauche(colonne) + taille / 2;
        float cy = haut(ligne) + taille / 2;
        if (partie.piece(colonne, ligne) == ' ') {
            pinceauIndication.setStyle(Paint.Style.FILL);
            canvas.drawCircle(cx, cy, taille * 0.15f, pinceauIndication);
        } else {
            pinceauIndication.setStyle(Paint.Style.STROKE);
            canvas.drawCircle(cx, cy, taille * 0.45f, pinceauIndication);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        switch (e.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                return true;
            case MotionEvent.ACTION_UP:
                int i = Math.min(7, (int) (e.getX() / taille));
                int j = Math.min(7, (int) (e.getY() / taille));
                int colonne = blancsEnBas ? i : 7 - i;
                int ligne = blancsEnBas ? 7 - j : j;
                toucherCase(colonne, ligne);
                performClick();
                return true;
        }
        return super.onTouchEvent(e);
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    private void toucherCase(int colonne, int ligne) {
        if (partie.etat() != Partie.Etat.EN_COURS) {
            return;
        }
        String coup = coupVers(colonne, ligne);
        if (coup != null) {
            partie.jouer(coup);
            deselectionner();
        } else if (pieceQuiALeTrait(colonne, ligne)) {
            colonneSelection = colonne;
            ligneSelection = ligne;
            coupsSelection = partie.coupsDepuis(colonne, ligne);
        } else {
            deselectionner();
        }
        invalidate();
    }

    private String coupVers(int colonne, int ligne) {
        for (String coup : coupsSelection) {
            boolean bonneCase = colonneArrivee(coup) == colonne && ligneArrivee(coup) == ligne;
            boolean promotionEnDame = coup.length() == 4 || coup.charAt(4) == 'q';
            if (bonneCase && promotionEnDame) {
                return coup;
            }
        }
        return null;
    }

    private boolean pieceQuiALeTrait(int colonne, int ligne) {
        char piece = partie.piece(colonne, ligne);
        if (piece == ' ') {
            return false;
        }
        return Character.isUpperCase(piece) == partie.traitAuxBlancs();
    }

    private boolean dansDernierCoup(int colonne, int ligne) {
        String coup = partie.dernierCoup();
        if (coup == null) {
            return false;
        }
        boolean depart = colonneDepart(coup) == colonne && ligneDepart(coup) == ligne;
        boolean arrivee = colonneArrivee(coup) == colonne && ligneArrivee(coup) == ligne;
        return depart || arrivee;
    }

    private void deselectionner() {
        colonneSelection = -1;
        ligneSelection = -1;
        coupsSelection = new ArrayList<>();
    }

    private float gauche(int colonne) {
        return (blancsEnBas ? colonne : 7 - colonne) * taille;
    }

    private float haut(int ligne) {
        return (blancsEnBas ? 7 - ligne : ligne) * taille;
    }

    private int colonneDepart(String coup) {
        return coup.charAt(0) - 'a';
    }

    private int ligneDepart(String coup) {
        return coup.charAt(1) - '1';
    }

    private int colonneArrivee(String coup) {
        return coup.charAt(2) - 'a';
    }

    private int ligneArrivee(String coup) {
        return coup.charAt(3) - '1';
    }
}
