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
import android.widget.TextView;

import java.util.Calendar;



public class GestionRepetition extends AppCompatActivity {

    private static final String FILE_NAME = "Default.txt";
    public static final String MESSAGE_MONTANT = "Montant";
    public static final String MESSAGE_BENEFICIAIRE = "Beneficiaire";
    public static final String MESSAGE_DATE = "Date";                                   //CreationCompte
    public static final String MESSAGE_NOM_DU_COMPTE_NOUVEAU = "NouveauNomDuCompte";    //CreationCompte
    public static final String MESSAGE_SOLDE = "Solde";                                 //CreationCompte
    public static final String MESSAGE_TYPEOP = "TypeOperation";
    public static final String MESSAGE_FREQUENCE = "frequence";
    public static final String EXTENSION = ".txt";

    private Button boutonValide;
    private Button boutonAnnule;
    private String nomDuCompte="";
    private TextView labelNomDuCompte;
    private TextView afficheMontant;
    private TextView beneficiaire;
    private TextView date;
    private DatePickerDialog datePickerDialog;
    private String operationComplete;
    private Operation op=new Operation("","",1,1,1,1,1,1,1);
    private int position;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion_repetition);
        boutonAnnule=(Button) findViewById(R.id.buttonAnnule);
        boutonAnnule.setOnClickListener(boutonAnnuleListener);
        boutonValide=(Button) findViewById(R.id.buttonValide);
        boutonValide.setOnClickListener(boutonValideListener);
        labelNomDuCompte=findViewById(R.id.lblNomCompteAjoute);
        afficheMontant=findViewById((R.id.lblMontant));
        beneficiaire=findViewById(R.id.lblBeneficiaire);
        date=findViewById(R.id.editTextDate);


        op.setNomDuCompte(this.getIntent().getExtras().getString("MESSAGE_NOM_DU_COMPTE"));
        op.setMontant(Float.valueOf(this.getIntent().getExtras().getString(MESSAGE_MONTANT)));
        op.setBenef(this.getIntent().getExtras().getString(MESSAGE_BENEFICIAIRE));
        date.setText(this.getIntent().getExtras().getString(MESSAGE_DATE));
        position=Integer.parseInt(this.getIntent().getExtras().getString("Index"));
        op.setDateString(date.getText().toString());

        operationComplete=this.getIntent().getExtras().getString("OperationComplete"); //intent.putExtra("OperationComplete",op.SauveOperationString());
        labelNomDuCompte.setText(op.getNomDuCompte());
        beneficiaire.setText(op.getBenef());
        afficheMontant.setText(String.valueOf(op.getMontant()));

        labelNomDuCompte.setText((nomDuCompte));
        initDatePicker();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("DEBUG","La activité repetition est detruite");
    }

    private View.OnClickListener boutonAnnuleListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent data = new Intent();
            data.putExtra("ANNULE","");
            setResult(1, data);
            finish();
        }
    };
    private View.OnClickListener boutonValideListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            Intent data = new Intent();

            Operation op=new Operation("","",1,1,1,1,1,1,1);
            op.decode(operationComplete);
            op.setMontant(Float.valueOf(afficheMontant.getText().toString()));
            op.setDateString(date.getText().toString());
            Log.i("DEBUG","On a valide l'operation repetée:"+op.SauveOperationString());
            data.putExtra("Validé",op.SauveOperationString());
            data.putExtra("Index",String.valueOf(position));// position dans livrerepetition
            setResult(RESULT_OK, data);
            finish();
        }
    };

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
        //int year= cal.get(Calendar.YEAR);
        //int month= cal.get(Calendar.MONTH);
        //int day= cal.get(Calendar.DAY_OF_MONTH);
        int year=op.getAnnee();
        int month=op.getMois()-1;
        int day=op.getJour();
        datePickerDialog= new DatePickerDialog(this,dateSetListener,year,month,day);

    }

    private String makeDateString(int year, int month, int day) {

        return day + "/" + month + "/" + year;
    }

    public void openDateClicker(View view){
        datePickerDialog.show();
    }


    // fin de la class GestionRepetition
}