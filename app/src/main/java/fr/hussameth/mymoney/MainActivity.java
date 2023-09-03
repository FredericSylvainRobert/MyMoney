package fr.hussameth.mymoney;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    ArrayList<Operation> livredecompte = new ArrayList<>(); //création du livre de 1 compte
    ArrayList<Operation> livrerepetition=new ArrayList<>(); //création du livre de répétition
    String listetroisfichier[]={"Default","Default","Default"};

    private static final String FILE_NAME = "Default.txt";
    public static final String MESSAGE_MONTANT = "Montant";
    public static final String MESSAGE_BENEFICIAIRE = "Beneficiaire";
    public static final String MESSAGE_DATE = "Date";                                   //CreationCompte
    public static final String MESSAGE_NOM_DU_COMPTE_NOUVEAU = "NouveauNomDuCompte";    //CreationCompte
    public static final String MESSAGE_SOLDE = "Solde";                                 //CreationCompte
    public static final String MESSAGE_TYPEOP = "TypeOperation";
    public static final String MESSAGE_FREQUENCE = "frequence";
    public static final String EXTENSION = ".txt";
    private static final int RESULT_SUPPRIME=2;
    private int Initfait=0;
    //
    //   les differents types de variable JAVA
    //
    /*public boolean bool=true; // false
    public byte bit=0; // jusqu'à 255
    public short shortvariable=-32768; //jusqu'à +32767
    public int entier=-2147483648; // jusqu'à +2147483647
    public long longentier=0; // -2^63 à 2^63
    */
    private int typeOperation; // 0 = débit ; 1 = crédit ; 2 = virement
    protected ImageButton ajouteButton;
    protected ImageButton retraitButton;
    protected ImageButton creationButton;
    protected ImageButton virementButton;
    protected Button boutonSave;
    protected Button boutonLoad;
    protected Button boutonChoixFichier;
    //private TextView historiqueCompte;
    private TextView solde;
    private TextView nomDuCompte;
    private float soldeNum = 0;
    private String nomDuFichier = "";
    ListView mlist;

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("DEBUG", "onResume Called");
        afficheHistoriqueCompte();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("DEBUG", "onRestart Called");
        init();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("DEBUG", "onPause Called");
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveLivredeCompte();
        Log.i("DEBUG", "onStop Called");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("DEBUG", "onDestroy Called");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        solde = (TextView) findViewById(R.id.montantvirement);
        nomDuCompte = (TextView) findViewById(R.id.lblNomCompte);
        ajouteButton = (ImageButton) findViewById(R.id.imageButton);
        ajouteButton.setOnClickListener(ajouteBoutonListener);
        retraitButton = (ImageButton) findViewById((R.id.imageButton2));
        retraitButton.setOnClickListener(retraitBoutonListener);
        virementButton = (ImageButton) findViewById((R.id.virementButton));
        virementButton.setOnClickListener(virementBoutonListener);
        boutonLoad = (Button) findViewById(R.id.buttonLoad);
        boutonLoad.setOnClickListener(boutonLoadListener);
        boutonSave = (Button) findViewById(R.id.buttonSave);
        boutonSave.setOnClickListener(boutonSaveListener);
        creationButton = (ImageButton) findViewById(R.id.buttonCrea);
        creationButton.setOnClickListener(creationBoutonListener);
        boutonChoixFichier = (Button) findViewById(R.id.buttonChoixFichier);
        boutonChoixFichier.setOnClickListener(choixFichierBoutonListener);
        mlist= (ListView) findViewById(R.id.listview);
        //Log.i("DEBUG","istetroifichiers"+listetroisfichier[0]+listetroisfichier[1]+listetroisfichier[2]);
        init();

    }


    //
    // Cette methode permet de creer le lien avec les autres activités
    //
    ActivityResultLauncher<Intent> activityResultLauncherAjoute =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult activityResult) {
                            int result = activityResult.getResultCode();
                            Intent data = activityResult.getData();
                            if (result == RESULT_OK) {
                                String donneeRetour;
                                String montantString;
                                String benef;
                                String frequence="0";
                                String nomducompte="";
                                int typop=0;
                                montantString = data.getStringExtra(MESSAGE_MONTANT);
                                benef = data.getStringExtra(MESSAGE_BENEFICIAIRE);
                                donneeRetour = data.getStringExtra(MESSAGE_DATE);
                                typop = Integer.parseInt(data.getStringExtra(MESSAGE_TYPEOP));
                                frequence = data.getStringExtra(MESSAGE_FREQUENCE);
                                nomducompte=data.getStringExtra("nomducompte");
                                int jour;
                                int mois;
                                int annee;
                                int i = 0;
                                while (donneeRetour.charAt(i) != '/')
                                    i++;
                                jour = Integer.parseInt(donneeRetour.substring(0, i));
                                i++;
                                int j = i;
                                while (donneeRetour.charAt(i) != '/')
                                    i++;
                                mois = Integer.parseInt(donneeRetour.substring(j, i));
                                i++;
                                annee = Integer.parseInt((donneeRetour.substring(i, donneeRetour.length())));
                                int posi = 0;
                                posi = calculpositiondate(jour, mois, annee);

                                Operation operationajout = new Operation(nomducompte, benef, typop, Float.valueOf(montantString),
                                        Integer.valueOf(frequence), jour, mois, annee, posi);
                                nomDuCompte.setText(operationajout.getNomDuCompte());
                                if(operationajout.getFrequence()==0) ajouteLigneDeCompte(operationajout.SauveOperationString());
                                //
                                // TODO IL FAUT ajouter l'operation tout de suite et ajouter la répétition
                                //
                                if(operationajout.getFrequence()!=0) ajoutAutomatiqueOperation(operationajout.SauveOperationString());
                                saveLivredeCompte();
                                afficheHistoriqueCompte();
                            } else {
                                Toast.makeText(MainActivity.this, "Opération annulée", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
            );

    ActivityResultLauncher<Intent> activityResultLauncherRepetition =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult activityResult) {
                            int result = activityResult.getResultCode();
                            Intent data = activityResult.getData();
                            //Log.i("DEBUG","Result="+result+"Result ok="+RESULT_OK);
                            if (result == RESULT_OK) {
                                String ligne;
                                ligne=data.getStringExtra("Validé");
                                int position;
                                position=Integer.valueOf(data.getStringExtra("Index"));
                                livrerepetition.remove(position);
                                ajoutAutomatiqueOperation(ligne);
                                //
                                // TODO Recuperer l'opération à repeter
                                //
                            } else {
                                Toast.makeText(MainActivity.this, "Repetition annulée", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
            );

    ActivityResultLauncher<Intent> activityResultLauncherCreation =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult activityResult) {
                            int result = activityResult.getResultCode();
                            //saveLivredeCompte(); //TODO EST IL NECESSAIRE ??
                            Intent data = activityResult.getData();
                            nomDuFichier = "";
                            Operation op;
                            String DateString = "";
                            String SoldeInitialStr = "";
                            if (result == RESULT_OK) {
                                nomDuFichier = data.getStringExtra(MESSAGE_NOM_DU_COMPTE_NOUVEAU);
                                nomDuCompte.setText(nomDuFichier);
                                DateString = data.getStringExtra(MESSAGE_DATE);
                                solde.setText(data.getStringExtra(MESSAGE_SOLDE));
                                livredecompte.clear();
                                livrerepetition.clear();
                                int jour = 0;
                                int mois = 0;
                                int annee = 0;
                                int i = 0;
                                while (DateString.charAt(i) != '/')
                                    i++;
                                jour = Integer.parseInt(DateString.substring(0, i));
                                i++;
                                int j = i;

                                while (DateString.charAt(i) != '/')
                                    i++;
                                mois = Integer.parseInt(DateString.substring(j, i));
                                i++;
                                annee = Integer.parseInt((DateString.substring(i, DateString.length())));
                                Log.i("DEBUG","nomdufichier="+ nomDuFichier);
                                op = new Operation("Livret A", "Création", 1, Float.valueOf(solde.getText().toString()), 0, jour, mois, annee, calculpositiondate(jour, mois, annee));
                                Log.i("DEBUG","Creation Compte ici");

                                Operation oprep = new Operation(nomDuFichier, "Création", 1, 1, 0, jour, mois, annee, calculpositiondate(jour, mois, annee));

                                Log.i("DEBUG","Creation Compte ici");
                                livredecompte.add(op);
                                livrerepetition.add(oprep);
                                saveLivredeCompte();
                                afficheHistoriqueCompte();
                                // SI FICHIER EXITE DEJA EFFACER ?? OU PAS ????
                            }
                        }
                    }
            );

    ActivityResultLauncher<Intent> activityResultLauncherChoixFichier =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult activityResult) {
                            int result = activityResult.getResultCode();
                            //("DEBUG","Result="+result);
                            String fichier = "";

                            Intent data = activityResult.getData();
                            if (result == RESULT_OK) {
                                fichier = data.getStringExtra("FICHIER");
                                nomDuCompte.setText(fichier);
                                //fichier = fichier+".txt";
                                //Log.i("DEBUG","Retour du choix"+fichier);
                                //nomDuCompte.setText(fichier);
                                loadLivredecompte(fichier);
                                afficheHistoriqueCompte();
                                // SI FICHIER EXITE DEJA EFFACER ?? OU PAS ????
                            }
                        }
                    }
            );

    ActivityResultLauncher<Intent> activityResultLauncherModificationOperation =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult activityResult) {
                            String ligne="";
                            int position;
                            int result = activityResult.getResultCode();
                            Operation op;
                            op=new Operation("","",1,0.0f,1,1,1,1,1);

                            Intent data = activityResult.getData();
                            if (result==RESULT_SUPPRIME) { //RESULT_SUPPRIME =2
                                position= Integer.valueOf(data.getStringExtra("position"));
                                livredecompte.remove(position);
                            }
                            if (result == RESULT_OK) {
                                ligne=data.getStringExtra("VALIDE");
                                position= Integer.valueOf(data.getStringExtra("position"));
                                livredecompte.remove(position);
                                op.decode(ligne);
                                rangeParDate(op,op.getPosition());
                            }
                        }
                    }
            );

    ActivityResultLauncher<Intent> activityResultLauncherVirement =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult activityResult) {
                            int result = activityResult.getResultCode();
                            Intent data = activityResult.getData();
                            if (result == RESULT_OK) {
                                String compteCredit="";
                                String comptedebit="";
                                String montant="";
                                String DateString="";
                                compteCredit=data.getStringExtra("CompteCredit");
                                comptedebit=data.getStringExtra("CompteDebit");
                                montant=data.getStringExtra("Montant");
                                DateString=data.getStringExtra("Date");
                                loadLivredecompte(compteCredit);
                                int jour = 0;
                                int mois = 0;
                                int annee = 0;
                                int i = 0;
                                while (DateString.charAt(i) != '/')
                                    i++;
                                jour = Integer.parseInt(DateString.substring(0, i));
                                i++;
                                int j = i;

                                while (DateString.charAt(i) != '/')
                                    i++;
                                mois = Integer.parseInt(DateString.substring(j, i));
                                i++;
                                annee = Integer.parseInt((DateString.substring(i, DateString.length())));

                                // TODO il reste à traiter les deux virements !!!
                                Operation op=new Operation(compteCredit,comptedebit,2,Float.valueOf(montant),0,jour,mois,annee,calculpositiondate(jour,mois,annee));
                                op.afficheLog();
                                rangeParDate(op,op.getPosition());
                                saveLivredeCompte();
                                loadLivredecompte(comptedebit);
                                op.setNomDuCompte(comptedebit);
                                op.setBenef(compteCredit);
                                op.setMontant(0-Float.valueOf(montant));
                                op.afficheLog();
                                rangeParDate(op,op.getPosition());
                            }
                        }
                    }
            );

    //
    //      Déclaration des boutons Listenner
    //
    private View.OnClickListener creationBoutonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(
                    MainActivity.this, //On crée le lien entre les deux activités
                    CreationCompte.class           // la deuxième activité est déclaré comme CLASS
            );
            ; // Opération de crédit
            intent.putExtra("MESSAGE_NOM_DU_COMPTE", nomDuCompte.getText().toString());
            activityResultLauncherCreation.launch(intent);
        }
    };
    private View.OnClickListener choixFichierBoutonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(
                    MainActivity.this, //On crée le lien entre les deux activités
                    GestionFichier.class           // la deuxième activité est déclaré comme CLASS
            );
            ; // Opération de crédit
            intent.putExtra("MESSAGE_NOM_DU_COMPTE", nomDuCompte.getText().toString()); // pas de message à passer logiquement
            activityResultLauncherChoixFichier.launch(intent);
        }
    };
    private View.OnClickListener ajouteBoutonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(
                    MainActivity.this, //On crée le lien entre les deux activités
                    AjouteOperation.class           // la deuxième activité est déclaré comme CLASS
            );
            typeOperation = 1; // Opération de crédit
            intent.putExtra("nomducompte", nomDuCompte.getText().toString());
            intent.putExtra(MESSAGE_SOLDE, solde.getText());
            solde.setText("1");
            //intent.putExtra("Type op", solde.getText());
            intent.putExtra(MESSAGE_TYPEOP, String.valueOf(typeOperation));
            activityResultLauncherAjoute.launch(intent);
        }
    };

    private View.OnClickListener virementBoutonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(
                    MainActivity.this, //On crée le lien entre les deux activités
                    Virement.class           // la deuxième activité est déclaré comme CLASS
            );
            typeOperation = 1; // Opération de crédit
            intent.putExtra("compte1", listetroisfichier[0]);
            intent.putExtra("compte2", listetroisfichier[1]);
            intent.putExtra("compte3", listetroisfichier[2]);
            intent.putExtra(MESSAGE_SOLDE, solde.getText());
            activityResultLauncherVirement.launch(intent);
        }
    };
    private View.OnClickListener retraitBoutonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(
                    MainActivity.this, //On crée le lien entre les deux activités
                    AjouteOperation.class           // la deuxième activité est déclaré comme CLASS
            );
            typeOperation = 0; // Opération de débit
            intent.putExtra("nomducompte", nomDuCompte.getText().toString());
            intent.putExtra(MESSAGE_SOLDE, solde.getText());
            intent.putExtra(MESSAGE_TYPEOP, String.valueOf(typeOperation));
            activityResultLauncherAjoute.launch(intent);
        }
    };
    private View.OnClickListener boutonLoadListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            loadLivredecompte(FILE_NAME);  // Revoir la video https://www.youtube.com/watch?v=EcfUkjlL9RI
        }
    };
    private View.OnClickListener boutonSaveListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            saveLivredeCompte();// Revoir la video https://www.youtube.com/watch?v=EcfUkjlL9RI
        }
    };

    private void afficheHistoriqueCompte()  // Rempli historiqueCompte par livredecompte
    {
        int i;
        livredecompte.get(0).setSolde(livredecompte.get(0).getMontant());
        if (livredecompte.size()>1)
            for(i=1;i<livredecompte.size();i++)
            {
                if (livredecompte.get(i).getTypeOperation()==1) livredecompte.get(i).setSolde(livredecompte.get(i-1).getSolde()+livredecompte.get(i).getMontant());
                else livredecompte.get(i).setSolde(livredecompte.get(i-1).getSolde()-livredecompte.get(i).getMontant());
            }
        solde.setText(String.valueOf(livredecompte.get(livredecompte.size()-1).getSolde()));
        // AFFICHAGE DE LISVIEW
        LivredeCompteAdapter ldcAdapter = new LivredeCompteAdapter (this,
                R.layout.lignelisteview,
                livredecompte);
        mlist.setAdapter(ldcAdapter);
        mlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Log.i("DEBUG",livredecompte.get(i).afficheOperationString());
                Intent intent = new Intent(
                        MainActivity.this, //On crée le lien entre les deux activités
                        ModificationOperation.class           // la deuxième activité est déclaré comme CLASS
                );
                intent.putExtra("nomducompte", livredecompte.get(i).getNomDuCompte());
                intent.putExtra("beneficaire",livredecompte.get(i).getBenef());
                intent.putExtra("montant",String.valueOf(livredecompte.get(i).getMontant()));
                intent.putExtra("date",livredecompte.get(i).getDateStr());
                intent.putExtra("OperationComplete",livredecompte.get(i).SauveOperationString());
                intent.putExtra("position",String.valueOf(i));
                activityResultLauncherModificationOperation.launch(intent);
            }
        });
    }

    private void init() {
        soldeNum = Float.valueOf("0.00");
        typeOperation = 0; // par defaut debit
        nomDuCompte.setText(R.string.NomDuCompte);
        solde.setText(soldeNum + " €");
        if (Initfait==0){
            testsifichierexiste();
            loadLivredecompte(premierfichier());
            int i;
            for(i=0;i<livrerepetition.size();i++)
                chercherepetition(livrerepetition.get(i),i);
            Initfait=1;
        }
        else
            loadLivredecompte(livredecompte.get(0).getNomDuCompte());


        //              TODO ici on bascule la répétion de frequence !!
        //              TODO fonction chercherepetition

        saveLivredeCompte();
        //saveLivreRepetition();
        afficheHistoriqueCompte();
        nomDuCompte.setText(livredecompte.get(0).getNomDuCompte());


    }

    private void ajouteLigneDeCompte(String ligne) // on ajoute la ligne de compte à ArrayList<Operation> livredecompte
    {   Log.i("DEBUG","Fonction ajouteLigneDeCompte");
        int position = 0;
        int typeOperation = 0;
        String jour = "01";
        int jourInt = Integer.parseInt(jour);
        String mois = "01";
        int moisInt = Integer.parseInt(mois);
        String annee = "2000";
        int anneeInt = Integer.parseInt(annee);
        String beneficiaire = "aucun";
        String freq = "0";
        int freqInt = Integer.parseInt(freq);
        String montant = "0.00";
        float montantFloat = Float.parseFloat(montant);
        String NomCompte = "Caisse Epargne Normandie"; /////// ICI on ne récupère pas le nom du compte(c'est le nom du fichier)
        Operation operation = new Operation(NomCompte, beneficiaire, typeOperation, montantFloat, freqInt, jourInt, moisInt, anneeInt, calculpositiondate(jourInt, moisInt, anneeInt));
        int i = 0;
        int k = 0;
        while (ligne.charAt(i) != ',') i++; //// Lecture de la position
        operation.setPosition(Integer.parseInt(ligne.substring(0, i)));
        i++;
        k = i;
        while (ligne.charAt(i) != ',') i++;   /// Lecture du nomducompte
        operation.setNomDuCompte(ligne.substring(k, i));
        Log.i("DEBUG","AjoutLignedeCompte, nomducompte="+operation.getNomDuCompte());
        i++;
        k = i;
        while (ligne.charAt(i) != ',') i++;   //// Lecture du beneficiaire
        operation.setBenef(ligne.substring(k, i));
        i++;
        k = i;
        while (ligne.charAt(i) != ',') i++;   //// lecture du type opération  // 0=débit '-' ,1= crédit  '+' , 2 virement benef '>'  => nom compte=benef
        operation.setTypeOperation(Integer.parseInt(ligne.substring(k, i)));
        i++;
        k = i;
        while (ligne.charAt(i) != ',') i++;     //// lecture du montant en float
        operation.setMontant(Float.parseFloat(ligne.substring(k, i)));
        i++;
        k = i;
        while (ligne.charAt(i) != ',') i++;    /// lecture de la fréquence // 0 pas de répétition, 10 tous les mois , 20 tous les 2 mois, 30 toutes les semaines (jour fixe)
        operation.setFrequence(Integer.parseInt(ligne.substring(k, i)));  // 1 lundi , 2 mardi , 3 mercredi , 4 jeudi , 5 vendredi , 6 samedi , 7 dimanche
        i++;
        k = i;
        while (ligne.charAt(i) != ',') i++;    //// lecture du jour
        operation.setJour(Integer.parseInt(ligne.substring(k, i)));
        i++;
        k = i;
        while (ligne.charAt(i) != ',') i++;   //// lecture du mois
        operation.setMois(Integer.parseInt(ligne.substring(k, i)));
        i++;
        k = i;                                 /// lecture de l'annee
        operation.setAnnee(Integer.parseInt(ligne.substring(k, i + 4)));
        i++;
        k = i;
        if (operation.getFrequence()==0)
                rangeParDate(operation, calculpositiondate(operation.getJour(), operation.getMois(), operation.getAnnee()));
        //else chercherepetition(operation); TODO  Verifier si il y a repetition !!! que fait on?
    }

    private void rangeParDate(Operation op, int position) { // si repetition alors dans livrepetition
        //op.afficheLog();
        if (op.getFrequence()==0) {
            int i = 0;
            int j = livredecompte.size();
            boolean ajoute = false;
            if (j != 0)
                while (i < j) {
                    if (position < livredecompte.get(i).getPosition()) {
                        livredecompte.add(i, op);
                        ajoute = true;
                        i = j - 1;
                    }
                    i++;
                }
            if (j == 0) livredecompte.add(op);
            if (ajoute == false && j != 0) livredecompte.add(op); // on ajoute à la fin
        }
        if(op.getFrequence()!=0){
            int i = 0;
            int j = livrerepetition.size();
            boolean ajoute = false;
            if (j != 0)
                while (i < j) {
                    if (position < livrerepetition.get(i).getPosition()) {
                        livrerepetition.add(i, op);
                        ajoute = true;
                        i = j - 1;
                    }
                    i++;
                }
            if (j == 0) livrerepetition.add(op);
            if (ajoute == false && j != 0) livrerepetition.add(op); // on ajoute à la fin
        }
    }

    private int calculpositiondate(int jour, int mois, int annee) {
        int position = 1;
        position = jour + mois * 31 + annee * 365;
        return position;
    }

    private void saveLivredeCompte() {
        Log.i("DEBUG","saveLivredeCompte");
        FileOutputStream fos = null;
        nomDuFichier = livredecompte.get(0).getNomDuCompte();
        Log.i("DEBUG","saveLivredeCompte"+nomDuFichier);

        try {
            fos = openFileOutput(nomDuFichier + EXTENSION, MODE_PRIVATE);
            for (Operation operation : livredecompte)
                fos.write(operation.SauveOperationString().getBytes());

            Toast.makeText(this, "Saved : " + getFilesDir() + "/" + nomDuFichier+".txt", Toast.LENGTH_LONG).show();
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
        FileOutputStream fosrep = null;
        try {
            fosrep = openFileOutput(nomDuFichier + ".rep", MODE_PRIVATE);
            for (Operation operation : livrerepetition)
                fosrep.write(operation.SauveOperationString().getBytes());

            Toast.makeText(this, "Saved : " + getFilesDir() + "/" + nomDuFichier+".rep", Toast.LENGTH_LONG).show();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (fosrep != null) {
                try {
                    fosrep.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }

    }



    private void loadLivredecompte(String fichier) {

        FileInputStream fis = null;
        try {
            fis = openFileInput(fichier + EXTENSION);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String ligne;
            livredecompte.clear();
            while ((ligne = br.readLine()) != null) {
                sb.append(ligne).append("\n");
                ajouteLigneDeCompte(ligne); // doit decode la ligne complète !
            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);

        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                Log.i("DEBUG", "Impossible de charger le fichier"+fichier+"txt");
            }
        }
        /// ON CHARGE LE FICHIER REPETITION!
        FileInputStream fichierrep = null;
        try {
            fichierrep = openFileInput(fichier + ".rep");
            InputStreamReader isr = new InputStreamReader(fichierrep);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String ligne;
            livrerepetition.clear();
            Operation operationrepet;
            while ((ligne = br.readLine()) != null) {
                sb.append(ligne).append("\n");
                operationrepet=new Operation("","",1,1,1,1,1,1,1);
                operationrepet.decode(ligne);
                livrerepetition.add(operationrepet); // doit decode la ligne complète !
            }
            if(livrerepetition.get(0).getPosition()!=1) livrerepetition.get(0).setPosition(1);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);

        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (fichierrep != null) {
                try {
                    fichierrep.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                Log.i("DEBUG", "Impossible de charger le fichier");
            }
        }
        afficheHistoriqueCompte();
    }


    private void testsifichierexiste() {
        File fichierListe = new File("data/data/fr.hussameth.mymoney/files/Liste.txt");
        if (!fichierListe.exists()) {
            try {
                //Log.i("DEBUG","dois Creer le fichier Liste.txt");
                fichierListe.createNewFile();
                FileOutputStream fos = null;
                try {
                    fos = openFileOutput("Liste.txt", MODE_PRIVATE);
                    fos.write("Default\n".getBytes());
                    fos.write("Default\n".getBytes());
                    fos.write("Default\n".getBytes());
                }
                catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
                catch (IOException e) {
                    throw new RuntimeException(e);
                }
                finally {
                    if (fos != null) {
                        try {
                            fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }

        //Creation par défaut du fichier DEFAULT.TXT

        File fichierDefault = new File("data/data/fr.hussameth.mymoney/files/Default.txt");
        if (!fichierDefault.exists()) {
            try {
                //("DEBUG","dois Creer le fichier Default.txt");
                fichierDefault.createNewFile();
                FileOutputStream fos = null;
                try {
                    fos = openFileOutput("Default.txt", MODE_PRIVATE);
                    fos.write("730032,Default,Création,1,0.0,0,1,1,2000*\n".getBytes());
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
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Creation par default du Fichier REPETITION.TXT

        File fichierRepetition = new File("data/data/fr.hussameth.mymoney/files/Default.rep");
        if (!fichierRepetition.exists()) {
            try {
                Log.i("DEBUG","Sauvegarde du fichier Default.rep");
                fichierRepetition.createNewFile();
                FileOutputStream fos = null;
                try {
                    fos = openFileOutput("Default.rep", MODE_PRIVATE);
                    fos.write("1,Default,Création,1,0.0,10,2,3,2000*\n".getBytes());
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
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }// fin fonction testsifichierexist()






    private void chercherepetition(Operation operation,int i) {

            if(getTodayDatePosition()>operation.getPosition()) {
                //Log.i("DEBUG","Postion aujourd'hui="+getTodayDatePosition()+"position de l'opération"+operation.getPosition());
                if (operation.getPosition()!=1) demandeajouteautomatique(operation,i);

            }


        // Il faut charger toutes les repetitions
        // calculer la date de répetitions.
        //comparer avec la date actuelle
        // si depassée il faut proposer de l'ajouter sinon on recalcule la prochaine.
        //  Dois rechercher si repetition à faire
    }

    private int getTodayDatePosition() {
        Calendar cal = Calendar.getInstance();
        int year= cal.get(Calendar.YEAR);
        int month= cal.get(Calendar.MONTH);
        month=month+1;
        int day= cal.get(Calendar.DAY_OF_MONTH);
        return calculpositiondate(day,month,year);
    } // fin de getTodeyDatePosition()

    private String premierfichier() {
        FileInputStream fis = null;
        String ligne1 = "";

        try {
            fis = openFileInput("Liste.txt");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String ligne;
            int i = 1;
            while ((ligne = br.readLine()) != null) {
                sb.append(ligne);//.append("\n");
                Log.i("DEBUG","Ligne="+ligne+".");
                if (i == 1) ligne1 = ligne;
                listetroisfichier[i-1]=ligne;
                Log.i("DEBUG","Ligne="+ligne+"."+listetroisfichier[i-1]);
// doit decode la ligne complète !
                i++;
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);

        } catch (IOException e) {
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
            return ligne1;
        }
    } // fin fonction premierfichier()

    void demandeajouteautomatique(Operation op,int i){
        Intent intent = new Intent(
                MainActivity.this, //On crée le lien entre les deux activités
                GestionRepetition.class           // la deuxième activité est déclaré comme CLASS
        );
        ; // Opération de crédit
        intent.putExtra("MESSAGE_NOM_DU_COMPTE", nomDuCompte.getText().toString());
        intent.putExtra(MESSAGE_MONTANT,String.valueOf(op.getMontant()));
        intent.putExtra(MESSAGE_BENEFICIAIRE,op.getBenef());
        intent.putExtra(MESSAGE_DATE,op.getDateStr());
        intent.putExtra("Index",String.valueOf(i));
        intent.putExtra("OperationComplete",op.SauveOperationString());


        Log.i("DEBUG","Demandeajoutautomoatique montant="+op.getMontant());
        activityResultLauncherRepetition.launch(intent);
        Log.i("DEBUG","il faut ajouter cette operation" + op.afficheOperationString());
    }


    void ajoutAutomatiqueOperation(String ligne){
        Operation op=new Operation("","",1,1,1,1,1,1,1);

        op.decode(ligne);
        //Log.i("DEBUG","Arrivé dans AjoutAutomatiqueOperation");
        //op.afficheLog();
        int f;
        f=op.getFrequence();
        op.setFrequence(0);
        //op.afficheLog();
        rangeParDate(op,op.getPosition());
        Operation opsuivante=new Operation(op.getNomDuCompte(),op.getBenef(),op.getTypeOperation(),op.getMontant(),f,op.getJour(),op.getMois(),op.getAnnee(),calculpositiondate(op.getJour(),op.getMois(),op.getAnnee()));
        if (f==10) opsuivante.ajoutemois(1);
        if (f==20) opsuivante.ajoutemois(2);
        rangeParDate(opsuivante,opsuivante.getPosition());
        afficheHistoriqueCompte();
    }

}// fin de la class Main Activity



