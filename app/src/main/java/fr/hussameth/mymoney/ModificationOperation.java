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
import android.widget.TextView;

import java.util.Calendar;

public class ModificationOperation extends AppCompatActivity {

    private DatePickerDialog datePickerDialog;
    private Button boutonValide;
    private Button boutonAnnule;
    private Button boutonSupprime;
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
    private String operationComplete="";
    private String position="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modification_operation);
        initDatePicker();
        op=new Operation(getIntent().getStringExtra("nomducompte"),"E",1,0.45f,1,1,1,1,1);
        boutonAnnule=(Button) findViewById(R.id.Annule);
        boutonAnnule.setOnClickListener(boutonAnnuleListener);
        boutonSupprime=(Button) findViewById(R.id.Supprime);
        boutonSupprime.setOnClickListener(boutonSupprimeListener);


        boutonValide=(Button) findViewById(R.id.Modifie);
        boutonValide.setOnClickListener(boutonValideListener);
        NomduCompte=(TextView) findViewById(R.id.lblNomCompteAjoute);
        date= (EditText) findViewById (R.id.editTextDate);
        montant=(EditText) findViewById(R.id.lblMontant);
        beneficiaire=(EditText) findViewById(R.id.lblBeneficiaire);
        operationComplete=this.getIntent().getExtras().getString("OperationComplete"); //intent.putExtra("OperationComplete",op.SauveOperationString());

        nomducompte=getIntent().getStringExtra("nomducompte");
        dateStr=getIntent().getStringExtra("date");
        beneficaire=getIntent().getStringExtra("beneficaire");
        montantStr=getIntent().getStringExtra("montant");
        position=getIntent().getStringExtra("position");
        NomduCompte.setText(nomducompte);
        date.setText(dateStr);
        montant.setText(montantStr);
        beneficiaire.setText(beneficaire);

        // il faut recupérer ce qui est modifié et le renvoyer si c'est validé sinon on annule






    }
    private View.OnClickListener boutonSupprimeListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent data = new Intent();
            data.putExtra("position",position);
            setResult(2, data); // RESULT_SUPPRIME =2
            finish();
        }
    };

    private View.OnClickListener boutonAnnuleListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent data = new Intent();
            data.putExtra("ANNULE","");
            data.putExtra("position",position);

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
            op.setMontant(Float.valueOf(montant.getText().toString()));
            op.setDateString(date.getText().toString());
            op.setBenef(beneficiaire.getText().toString());
            Log.i("DEBUG","On a valide l'operation repetée:"+op.SauveOperationString());
            data.putExtra("VALIDE",op.SauveOperationString());
            data.putExtra("position",position);
            data.putExtra("nomducompte",op.getNomDuCompte().toString());
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
        int year= cal.get(Calendar.YEAR);
        int month= cal.get(Calendar.MONTH);
        int day= cal.get(Calendar.DAY_OF_MONTH);

        datePickerDialog= new DatePickerDialog(this,dateSetListener,year,month,day);

    }

    private String makeDateString(int year, int month, int day) {

        return day + "/" + month + "/" + year;
    }

    public void openDateClicker(View view){
        datePickerDialog.show();
    }
}