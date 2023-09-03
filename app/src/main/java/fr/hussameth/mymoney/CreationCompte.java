package fr.hussameth.mymoney;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;

public class CreationCompte extends AppCompatActivity {
    // déclaration des variables
    private Button boutonAnnule;
    private Button boutonValide;
    private EditText nomDuCompte;
    private EditText date;
    private EditText soldeInitial;
    private DatePickerDialog datePickerDialog;
    RadioGroup groupeBoutton;
    RadioButton boutton;
    RadioButton boutton1;
    RadioButton boutton2;
    RadioButton boutton3;
    String nom1="";
    String nom2="";
    String nom3="";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creation_compte);
        boutonAnnule=(Button) findViewById(R.id.buttonAnnule);
        boutonAnnule.setOnClickListener(boutonAnnuleListener);
        boutonValide=(Button) findViewById(R.id.buttonValide);
        boutonValide.setOnClickListener(boutonValideListener);
        date=(EditText) findViewById(R.id.editTextDate);
        date.setText(getTodayDate());
        nomDuCompte=(EditText) findViewById(R.id.creaDuCompte);
        soldeInitial=(EditText) findViewById(R.id.lblMontant);
        soldeInitial.setText("0.00");
        groupeBoutton=findViewById(R.id.groupeBouttonChoix);
        boutton1=findViewById(R.id.choix1);
        boutton2=findViewById(R.id.choix2);
        boutton3=findViewById(R.id.choix3);

        chargeLesNomsdeFichier();
        initDatePicker();

    }


    private View.OnClickListener boutonValideListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent data = new Intent();
            data.putExtra("NouveauNomDuCompte", nomDuCompte.getText().toString());
            data.putExtra("Date",date.getText().toString());
            data.putExtra("Solde",soldeInitial.getText().toString());
            setResult(RESULT_OK, data);
            //  Ecriture du fichier Liste.txt
            FileOutputStream fos = null;

            try {
                fos = openFileOutput("Liste.txt", MODE_PRIVATE);

                nom1=boutton1.getText().toString()+"\n";
                nom2=boutton2.getText().toString()+"\n";
                nom3=boutton3.getText().toString()+"\n";
                fos.write(nom1.getBytes());
                fos.write(nom2.getBytes());
                fos.write(nom3.getBytes());
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
            finish();
        }
    };
    private View.OnClickListener boutonAnnuleListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent data = new Intent();
            data.putExtra("ANNULE", "");
            setResult(1, data); // Tout est ok, on peut envoyer
            finish();
        }
    };
    private String getTodayDate() {
        Calendar cal = Calendar.getInstance();
        int year= cal.get(Calendar.YEAR);
        int month= cal.get(Calendar.MONTH);
        month=month+1;
        int day= cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(year,month,day);
    }

    private void initDatePicker(){
        DatePickerDialog.OnDateSetListener dateSetListener=new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day)
            {
                month=month+1;
                String datepicked= makeDateString(year,month,day);
                date.setText(datepicked);
            }
        };
        Calendar cal = Calendar.getInstance();
        int year= cal.get(Calendar.YEAR);
        int month= cal.get(Calendar.MONTH);
        int day= cal.get(Calendar.DAY_OF_MONTH);
        datePickerDialog= new DatePickerDialog(this,dateSetListener,year,month,day);
    }

    public void openDateClicker(View view){
        datePickerDialog.show();
    }

    private View.OnClickListener dateButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            openDateClicker(view);
        }
    };

    private String makeDateString(int year, int month, int day) {

        return day + "/" + month + "/" + year;
    }
    public void checkbutton(View v){
        int bouttonRadioId= groupeBoutton.getCheckedRadioButtonId();
        boutton1.setText(nom1);
        boutton2.setText(nom2);
        boutton3.setText(nom3);

        boutton=findViewById(bouttonRadioId);
        boutton.setText(nomDuCompte.getText());
        //Log.i("DEBUG","BouttonID"+bouttonRadioId);
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
                if (i==1) {boutton1.setText(ligne);nom1=ligne; }// doit decode la ligne complète !
                if (i==2) {boutton2.setText(ligne);nom2=ligne; }// doit decode la ligne complète !
                if (i==3) {boutton3.setText(ligne);nom3=ligne; }// doit decode la ligne complète !
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
// fin de la déclaration de la classe CréationCompte
}