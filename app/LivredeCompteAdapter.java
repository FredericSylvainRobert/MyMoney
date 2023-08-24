package fr.hussameth.mymoney;


import android.content.Context;
import android.media.VolumeShaper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class LivredeCompteAdapter extends ArrayAdapter<Operation> {


            private Context mContext;
            int mRessource;

            public LivredeCompteAdapter (Context context, int ressource, ArrayList<VolumeShaper.Operation> operation) {
                super ( context,ressource,operation);
                mContext=context;
                mRessource=ressource;
            }

            @NonNull
            @Override
    public View getView(int position, View convertview, ViewGroup parent){

                String name= getItem(position).getName();
                String birthday=getItem(position).getBirthday();
                String date=getItem(position).getDateStr();

                // create the new person Object with the information
                Person person = new Person(birthday,sexe,name);

                LayoutInflater inflater= LayoutInflater.from(mContext);
                convertview = inflater.inflate(mRessource,parent,false);

                TextView tvName= (TextView) convertview.findViewById(R.id.textView);
                TextView tvBirthday= (TextView) convertview.findViewById(R.id.textView2);
                TextView tvSexe= (TextView) convertview.findViewById(R.id.textView3);

                tvName.setText(name);
                tvSexe.setText(sexe);
                tvBirthday.setText(birthday);

                return convertview;


            }

}
