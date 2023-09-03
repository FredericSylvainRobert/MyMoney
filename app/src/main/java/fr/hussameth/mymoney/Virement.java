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
import android.widget.Toast;

import java.util.Calendar;

public class Virement extends AppCompatActivity {

    String nomcompte1="";
    String nomcompte2="";
    String nomcompte3="";
    int idboutton1=0;
    int idboutton4=0;
    int bouttonCreditId=0;
    int bouttonDebitId=0;

    EditText montant;
    RadioGroup groupedebit;
    RadioButton button1deb;
    RadioButton button2deb;
    RadioButton button3deb;
    RadioButton bouttontemp;
    RadioGroup groupeCredit;
    RadioButton button1cred;
    RadioButton button2cred;
    RadioButton button3cred;
    Button valide;
    Button annule;
    EditText date;
    private DatePickerDialog datePickerDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_virement);
        nomcompte1=getIntent().getStringExtra("compte1");
        nomcompte2=getIntent().getStringExtra("compte2");
        nomcompte3=getIntent().getStringExtra("compte3");
        montant=findViewById(R.id.montantvirement);
        montant.setText("0.00");
        groupedebit=findViewById(R.id.groupedeb);
        button1deb=findViewById(R.id.radioButton1);button1deb.setText(nomcompte1);
        button2deb=findViewById(R.id.radioButton2);button2deb.setText(nomcompte2);
        button3deb=findViewById(R.id.radioButton3);button3deb.setText(nomcompte3);
        idboutton1=groupedebit.getCheckedRadioButtonId();


        groupeCredit=findViewById(R.id.groupecred);
        idboutton4=groupeCredit.getCheckedRadioButtonId();
        button1cred=findViewById(R.id.radioButton4);button1cred.setText(nomcompte1);
        button2cred=findViewById(R.id.radioButton5);button2cred.setText(nomcompte2);
        button3cred=findViewById(R.id.radioButton6);button3cred.setText(nomcompte3);
        valide=findViewById(R.id.valide);
        valide.setOnClickListener(ValideBoutonListener);
        annule=findViewById(R.id.annule);
        annule.setOnClickListener(AnnuleBoutonListener);
        date=findViewById(R.id.editTextDate2);
        date.setText(getTodayDate());
        //montant.setText(String.valueOf(idboutton1)+String.valueOf(idboutton4));
        initDatePicker();

    }

    public void checkbutton(View v){
        bouttonCreditId= groupeCredit.getCheckedRadioButtonId()-idboutton4;
        bouttonDebitId= groupedebit.getCheckedRadioButtonId()-idboutton1;
        //String temp="";
          //      temp=String.valueOf(bouttonCreditId)+String.valueOf(bouttonDebitId);
        //montant.setText(temp);
        //Log.i("DEBUG","BouttonID"+bouttonRadioId);
    }

    private View.OnClickListener ValideBoutonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (bouttonCreditId == bouttonDebitId) {
                Toast.makeText(Virement.this, "Les comptes sont identiques, opération non valide ", Toast.LENGTH_LONG).show();
            }
            int id = 0;
            Intent data = new Intent();
            id = groupedebit.getCheckedRadioButtonId();
            bouttontemp = findViewById(id);
            data.putExtra("CompteDebit", bouttontemp.getText().toString());
            Log.i("DEBUG","Virement compte debité"+bouttontemp.getText());
            id = groupeCredit.getCheckedRadioButtonId();
            bouttontemp = findViewById(id);
            data.putExtra("CompteCredit", bouttontemp.getText().toString());
            Log.i("DEBUG","Virement compte credité"+bouttontemp.getText());
            data.putExtra("Date", date.getText().toString());
            data.putExtra("Montant", montant.getText().toString());
            setResult(RESULT_OK, data);
            finish();
        }
    };

    private View.OnClickListener AnnuleBoutonListener = new View.OnClickListener() {
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


    private String makeDateString(int year, int month, int day) {

        return day + "/" + month + "/" + year;
    }

}