package fr.hussameth.mymoney;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.SimpleTimeZone;

public class LivredeCompteAdapter extends ArrayAdapter<Operation> {

    private Context mContext;
        int mRessource;


    public LivredeCompteAdapter (Context context, int resource, ArrayList<Operation> op){ // Constructeur
        super (context,resource,op);
        mContext=context;
        mRessource=resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertview, ViewGroup parent){

        String nomCompte=getItem(position).getNomDuCompte();
        String benef= getItem(position).getBenef();
        int typop=getItem(position).getTypeOperation();
        float montant=getItem(position).getMontant();
        int freq=getItem(position).getFrequence();
        int jour=getItem(position).getJour();
        int mois=getItem(position).getMois();
        int annee=getItem(position).getAnnee();
        float solde=getItem(position).getSolde();
        int couleurText=getItem(position).gettextColor();
        int couleurSolde=getItem(position).getCouleurSolde();
        String dateStr=getItem(position).getDateStr();



        Operation operation=new Operation(nomCompte,benef,typop,montant,freq,jour,mois,annee,1);
        //Log.i("DEBUG","LivreComtpeAdapter, nomducompte="+operation.getNomDuCompte());
        LayoutInflater inflater= LayoutInflater.from(mContext);
        convertview = inflater.inflate(mRessource,parent,false);

        TextView tvDate= (TextView) convertview.findViewById(R.id.listeDate);
        TextView tvBenef= (TextView) convertview.findViewById(R.id.listeBeneficiaire);
        TextView tvMontant= (TextView) convertview.findViewById(R.id.listMontant);
        TextView tvSolde=(TextView) convertview.findViewById(R.id.listSolde);

        tvDate.setText(dateStr);
        tvBenef.setText(benef);
        tvMontant.setText(String.valueOf(montant));
        tvMontant.setBackgroundColor(couleurText);
        tvSolde.setText(String.valueOf(solde));
        tvSolde.setBackgroundColor(couleurSolde);
        return convertview;
    }

}
