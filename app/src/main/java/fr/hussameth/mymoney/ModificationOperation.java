package fr.hussameth.mymoney;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;

public class ModificationOperation extends AppCompatActivity {

    private DatePickerDialog datePickerDialog;
    private Button boutonValide;
    private Button boutonAnnule;
    private EditText date;
    private EditText beneficiaire;
    private EditText montant;
    private TextView solde;
    private TextView NomduCompte;

    private Operation op;
    private String nomducompte="";
    private String dateStr="";
    private String beneficaire="";
    private String montantStr="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modification_operation);
        initDatePicker();
        op=new Operation(getIntent().getStringExtra("nomducompte"),"E",1,0.45f,1,1,1,1,1);
        NomduCompte=(TextView) findViewById(R.id.lblNomCompteAjoute);
        date= (EditText) findViewById (R.id.editTextDate);
        montant=(EditText) findViewById(R.id.lblMontant);
        beneficiaire=(EditText) findViewById(R.id.lblBeneficiaire);
        nomducompte=getIntent().getStringExtra("nomducompte");
        dateStr=getIntent().getStringExtra("date");
        beneficaire=getIntent().getStringExtra("beneficaire");
        montantStr=getIntent().getStringExtra("montant");
        NomduCompte.setText(nomducompte);
        date.setText(dateStr);
        montant.setText(montantStr);
        beneficiaire.setText(beneficaire);

        // il faut recupérer ce qui est modifié et le renvoyer si c'est validé sinon on annule






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

    private String makeDateString(int year, int month, int day) {

        return day + "/" + month + "/" + year;
    }
}