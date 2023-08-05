package fr.hussameth.mymoney;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class GestionFichier extends AppCompatActivity {
    RadioGroup groupeBoutton;
    RadioButton boutton;
    RadioButton boutton1;
    RadioButton boutton2;
    RadioButton boutton3;
    Button boutonValide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion_fichier);
        groupeBoutton=findViewById(R.id.groupeBoutton);
        boutton1=findViewById(R.id.fichier1);
        boutton2=findViewById(R.id.fichier2);
        boutton3=findViewById(R.id.fichier3);
        boutonValide=findViewById(R.id.buttonValide);
        //ecritLesNomsdeFichier();  //TODO Reset les noms de fichiers
        chargeLesNomsdeFichier();

        boutonValide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int bouttonRadioId= groupeBoutton.getCheckedRadioButtonId();
                boutton=findViewById(bouttonRadioId);
                //Log.i("DEBUG","choix:"+boutton.getText());
                Intent data = new Intent();
                data.putExtra("FICHIER", boutton.getText());
                setResult(RESULT_OK, data);

                finish();
                // il faut repasser le parametre fichier choisis
            }
        });
    }

    public void checkbutton(View v){
        int bouttonRadioId= groupeBoutton.getCheckedRadioButtonId();
        boutton=findViewById(bouttonRadioId);
        //Log.i("DEBUG","BouttonID"+bouttonRadioId);
    }

    private void ecritLesNomsdeFichier(){
        FileOutputStream fos = null;

        try {
            fos = openFileOutput("Liste.txt", MODE_PRIVATE);
                fos.write("Caisse Epargne Normandie\n".getBytes());
                fos.write("Carte Bleue\n".getBytes());
                fos.write("CB\n".getBytes());

            Toast.makeText(this, "Saved : " +"Liste.txt", Toast.LENGTH_LONG).show();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    private void chargeLesNomsdeFichier() {
        FileInputStream fis = null;
        try {
            fis = openFileInput("Liste.txt");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String ligne;
            int i=1;
            while ((ligne = br.readLine()) != null) {
                sb.append(ligne).append("\n");
                //Log.i("DEBUG","Ligne="+ligne);
                if (i==1) boutton1.setText(ligne); // doit decode la ligne complète !
                if (i==2) boutton2.setText(ligne); // doit decode la ligne complète !
                if (i==3) boutton3.setText(ligne); // doit decode la ligne complète !
                i++;

            }
        } catch (
                FileNotFoundException e) {
            throw new RuntimeException(e);

        } catch (
                IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                Log.i("DEBUG", "Gestion de fichier: Impossible de charger le fichier");
            }
        }
    }



} // fin de la classe GestionFichier

