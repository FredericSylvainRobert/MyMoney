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

public class AjouteOperation extends AppCompatActivity {

    private static final String FILE_NAME = "Default.txt";
    public static final String MESSAGE_MONTANT = "Montant";
    public static final String MESSAGE_BENEFICIAIRE = "Beneficiaire";
    public static final String MESSAGE_DATE = "Date";                              //CreationCompte
    public static final String MESSAGE_NOM_DU_COMPTE_NOUVEAU = "NouveauNomDuCompte"; //CreationCompte
    public static final String MESSAGE_SOLDE = "Solde";//CreationCompte
    public static final String MESSAGE_TYPEOP = "TypeOperation";
    public static final String MESSAGE_FREQUENCE = "frequence";
    public static final String EXTENSION = ".txt";

    private Button boutonValide;
    private Button boutonAnnule;
    private EditText date;
    private EditText beneficiaire;
    private EditText montant;
    private TextView solde;

    private String nomDuCompte="";


    private TextView labelNomDuCompte;


    private DatePickerDialog datePickerDialog;
    private Button dateButton;
    private String frequenceStr="0";
    private int typ=0; // 0=debit , 1 = credit

    RadioGroup groupeBoutton;
    RadioButton boutton;
    RadioButton boutton1;
    RadioButton boutton2;
    RadioButton boutton3;
    //
    //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajoute_operation);
        Log.i("DEBUG", "ON est dans la seconde activité");
        boutonAnnule=(Button) findViewById(R.id.buttonAnnule);
        boutonAnnule.setOnClickListener(boutonAnnuleListener);
        boutonValide=(Button) findViewById(R.id.buttonValide);
        boutonValide.setOnClickListener(boutonValideListener);
        date=(EditText) findViewById(R.id.editTextDate);
        beneficiaire=(EditText) findViewById(R.id.lblBeneficiaire);
        montant=(EditText) findViewById(R.id.lblMontant);
        solde=(TextView) findViewById(R.id.lblSolde);
        labelNomDuCompte=findViewById(R.id.lblNomCompteAjoute);
        initDatePicker();
        dateButton=findViewById(R.id.datePickerButton);
        dateButton.setOnClickListener(dateButtonListener);
        date.setText(getTodayDate());
        groupeBoutton=findViewById(R.id.groupeBouttonFreq);
        boutton1=findViewById(R.id.freq1);
        boutton2=findViewById(R.id.freq2);
        boutton3=findViewById(R.id.freq3);
        //ajouteButton=(ImageButton) findViewById(R.id.imageButton);
        //ajouteButton.setOnClickListener(ajouteBoutonListener);

        //
        // On récupère le nom du compte donné par MainActivity lors de l'appel grace à la Key "nomducompte"

        // 0=débit '-' ,1= crédit  '+' , 2 virement benef '>'  => nom compte=benef


        // on recupère le nom du compte, le type d'operation,le solde du compte
        nomDuCompte = this.getIntent().getExtras().getString("nomducompte");
        labelNomDuCompte.setText(nomDuCompte);
        typ=Integer.parseInt(this.getIntent().getExtras().getString(MESSAGE_TYPEOP));
        solde.setText(this.getIntent().getExtras().getString(MESSAGE_SOLDE));

    }

    private String getTodayDate() {
        Calendar cal = Calendar.getInstance();
        int year= cal.get(Calendar.YEAR);
        int month= cal.get(Calendar.MONTH);
        month=month+1;
        int day= cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(year,month,day);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("DEBUG","La seconde activité est detruite");
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
            Log.i("DEBUG","Bouton Valide cliqué");
            Intent data = new Intent();
            data.putExtra("nomducompte",nomDuCompte);
            data.putExtra(MESSAGE_MONTANT, montant.getText().toString());
            data.putExtra(MESSAGE_BENEFICIAIRE, beneficiaire.getText().toString());
            data.putExtra(MESSAGE_DATE,date.getText().toString());
            data.putExtra(MESSAGE_TYPEOP,String.valueOf(typ));
            data.putExtra(MESSAGE_FREQUENCE,frequenceStr);
            // 0 pas de répétition, 10 tous les mois , 20 tous les 2 mois, 30 toutes les semaines (jour fixe)
            setResult(RESULT_OK, data); // Tout est ok, on peut envoyer
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
        int year= cal.get(Calendar.YEAR);
        int month= cal.get(Calendar.MONTH);
        int day= cal.get(Calendar.DAY_OF_MONTH);

        datePickerDialog= new DatePickerDialog(this,dateSetListener,year,month,day);

    }

    private String makeDateString(int year, int month, int day) {

    return day + "/" + month + "/" + year;
    }
    public void checkbutton(View v){

        int bouttonRadioId= groupeBoutton.getCheckedRadioButtonId();
        boutton=findViewById(bouttonRadioId);
        if (boutton.getText().equals("une seul fois")) frequenceStr="0";
        if (boutton.getText().equals("Tous les mois")) frequenceStr="10";
        if (boutton.getText().equals("Tous les 2 mois")) frequenceStr="20";
        if (boutton.getText().equals("Toutes les semaines")) frequenceStr="30";

        // 0 pas de répétition, 10 tous les mois , 20 tous les 2 mois, 30 toutes les semaines (jour fixe)


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
}