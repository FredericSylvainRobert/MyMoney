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
import android.widget.Button;
import android.widget.ImageButton;
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

public class MainActivity extends AppCompatActivity {

    ArrayList<Operation> livredecompte = new ArrayList<>();  //création du livre de 1 compte
    //private int nombredeligne; // nombre de ligne dans historique de compte
    //public Operation operation=new Operation("Caisse Epargne Normandie","Initialisation",1000, LocalDate.of(2000,01,01);
    private static final String FILE_NAME = "Default.txt";
    private static final String FILE_NAME2 = "CaisseEpargne.txt";
    public static final String MESSAGE_MONTANT = "Montant";
    public static final String MESSAGE_BENEFICIAIRE = "Beneficiaire";
    public static final String MESSAGE_DATE = "Date";                              //CreationCompte
    public static final String MESSAGE_NOM_DU_COMPTE_NOUVEAU="NouveauNomDuCompte"; //CreationCompte
    public static final String MESSAGE_SOLDE="Solde";//CreationCompte
    public static final String EXTENSION=".txt";

    private int typeOperation; // 0 = débit ; 1 = crédit ; 2 = virement
    protected ImageButton ajouteButton;
    protected ImageButton retraitButton;
    protected ImageButton creationButton;

    protected Button boutonSave;
    protected Button boutonLoad;
    protected Button boutonChoixFichier;
    private TextView historiqueCompte;

    private TextView solde;
    private TextView nomDuCompte;
    private float soldeNum = 0;
    private String nomDuFichier="";


    @Override protected void onResume() {
        super.onResume();
        Log.i("DEBUG", "onResume Called");
        for (Operation operation : livredecompte)
        {operation.afficheLog();Log.i("DEBUG","NomduCompte="+operation.getNomDuCompte());}
        //historiqueCompte.setText("onResume");
        historiqueCompte.setText("");
        for (Operation operation : livredecompte)
            historiqueCompte.append(operation.afficheOperationString());
    }

    @Override protected void onRestart() {
        super.onRestart();
        Log.i("DEBUG", "onRestart Called");
    }

    @Override protected void onPause() {
        super.onPause();
        Log.i("DEBUG", "onPause Called");
    }

    @Override protected void onStop() {
        super.onStop();
        Log.i("DEBUG", "onStop Called");
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        Log.i("DEBUG", "onDestroy Called");
    }


        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        historiqueCompte = (TextView) findViewById(R.id.HistoriqueCompte);
        solde = (TextView) findViewById(R.id.lblSolde);
        nomDuCompte = (TextView) findViewById(R.id.lblNomCompte);
        ajouteButton = (ImageButton) findViewById(R.id.imageButton);
        ajouteButton.setOnClickListener(ajouteBoutonListener);
        retraitButton = (ImageButton) findViewById((R.id.imageButton2));
        retraitButton.setOnClickListener(retraitBoutonListener);
        boutonLoad = (Button) findViewById(R.id.buttonLoad);
        boutonLoad.setOnClickListener(boutonLoadListener);
        boutonSave = (Button) findViewById(R.id.buttonSave);
        boutonSave.setOnClickListener(boutonSaveListener);
        creationButton = (ImageButton) findViewById(R.id.buttonCrea);
        creationButton.setOnClickListener(creationBoutonListener);
        boutonChoixFichier=(Button) findViewById(R.id.buttonChoixFichier);
        boutonChoixFichier.setOnClickListener(choixFichierBoutonListener);
        init();// appelle la fonction Init
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
                                String donneeRetour = "";
                                String montantString = "";
                                String benef = "";
                                String frequence="";
                                int typop = 0;
                                montantString = data.getStringExtra(MESSAGE_MONTANT);
                                benef = data.getStringExtra(MESSAGE_BENEFICIAIRE);
                                donneeRetour = data.getStringExtra(MESSAGE_DATE);
                                typop = Integer.parseInt(data.getStringExtra("TYPE OPERATION"));
                                frequence=data.getStringExtra("frequence");
                                //("DEBUG","Retour frequence"+frequence);

                                int jour = 0;
                                int mois = 0;
                                int annee = 0;
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
                                donneeRetour = data.getStringExtra("TYPE OPERATION");
                                int posi = 0;
                                posi = calculpositiondate(jour, mois, annee);
                                if (frequence.equals("Tous les mois")) frequence="10";
                                if (frequence.equals("Tous les 2 mois")) frequence="20";
                                if (frequence.equals("une seul fois")) frequence="0";
                                Log.i("DEBUG","frequence dans mainactivity"+frequence);
                                    Operation operationajout = new Operation(nomDuCompte.getText().toString(), benef, typop, Float.valueOf(montantString),
                                        Integer.valueOf(frequence), jour, mois, annee, posi);
                                nomDuCompte.setText(operationajout.getNomDuCompte());
                                //operationajout.afficheLog();
                                ajouteLigneDeCompte(operationajout.SauveOperationString());
                                historiqueCompte.append("On est passé là");
                                //Log.i("DEBUG","activity result: nombre de ligne="+livredecompte.size());
                                saveLivredeCompte();
                                afficheHistoriqueCompte();


                                //Toast.makeText(MainActivity.this, "Solde mis à jour", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(MainActivity.this, "Opération annulée", Toast.LENGTH_LONG).show();
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
                            //Log.i("DEBUG","Result="+result);
                            saveLivredeCompte();
                            Intent data = activityResult.getData();
                            String Nomdufichier="";
                            String DateString="";
                            String SoldeInitialStr="";
                            //Log.i("DEBUG","Result="+result);

                            if (result == RESULT_OK) {
                                Nomdufichier = data.getStringExtra(MESSAGE_NOM_DU_COMPTE_NOUVEAU);
                                nomDuCompte.setText(Nomdufichier);
                                //Log.i("DEBUG","Creation du fichier:"+Nomdufichier);
                                DateString = data.getStringExtra(MESSAGE_DATE);
                                SoldeInitialStr = data.getStringExtra(MESSAGE_SOLDE);
                                solde.setText(SoldeInitialStr);
                                livredecompte.clear();
                                //String ligne = "";
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
                                Operation op = new Operation(Nomdufichier, "Création", 1, Float.parseFloat(SoldeInitialStr), 0, jour, mois, annee, calculpositiondate(jour, mois, annee));
                                livredecompte.add(op);
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
                            String fichier="";

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
            ; // Opération de crédit
            intent.putExtra("nomducompte", nomDuCompte.getText().toString());
            intent.putExtra("Solde", solde.getText());
            solde.setText("1");
            intent.putExtra("Type op", solde.getText());
            intent.putExtra("TYPE OPERATION", "1");
            activityResultLauncherAjoute.launch(intent);
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
            intent.putExtra("Solde", solde.getText());
            solde.setText("0");
            intent.putExtra("Type op", solde.getText());
            activityResultLauncherAjoute.launch(intent);
        }
    };
    private View.OnClickListener boutonLoadListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            loadLivredecompte("Default.txt");  // Revoir la video https://www.youtube.com/watch?v=EcfUkjlL9RI
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
        String longTexte="";
        int i=0;
        //Log.i("DEBUG","afficheHistoriqueCompte:livredecompte size="+livredecompte.size());
        //for(i=0;i<livredecompte.size();i++)
        //    longTexte=longTexte+livredecompte.get(i).afficheOperationString();
        //Log.i("DEBUG","Affiche historqiue compte long texte="+longTexte);
        historiqueCompte.setText("");

        //historiqueCompte.setText("Efface tout!");
        for (Operation operation : livredecompte)
            historiqueCompte.append(operation.afficheOperationString());
        int k=0;
        soldeNum=0;
        for(k=0;k<livredecompte.size();k++)
        {
            livredecompte.get(k).afficheLog();
            if (livredecompte.get(k).getTypeOperation()==0) soldeNum-=livredecompte.get(k).getMontant();
            if (livredecompte.get(k).getTypeOperation()==1) soldeNum+=livredecompte.get(k).getMontant();
            if (livredecompte.get(k).getTypeOperation()==2) soldeNum-=livredecompte.get(k).getMontant();
            soldeNum=Math.round(soldeNum*100);
            //Log.i("DEBUG","SoldeNum*100="+soldeNum);
            soldeNum=soldeNum/100;
            //Log.i("DEBUG","SoldeNum="+soldeNum);
        }
        solde.setText(String.valueOf(soldeNum));
    }
    private void init() {
        // On charge le fichier Historique.TXT
        soldeNum = Float.valueOf("0.00");
        typeOperation = 0; // par defaut debit
        nomDuCompte.setText(R.string.NomDuCompte);
        solde.setText(soldeNum + " €");
        historiqueCompte.setText("");
        testsifichierexiste();
        loadLivredecompte(premierfichier());
        afficheHistoriqueCompte();
        chercherepetition();
        nomDuCompte.setText(livredecompte.get(0).getNomDuCompte());
        //Log.i("DEBUG","Passage par la fonction Init()");

    }
    public void ajouteLigneDeCompte(String ligne) // on ajoute la ligne de compte à ArrayList<Operation> livredecompte
    {
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
        while (ligne.charAt(i) != ',') i++;
        operation.setPosition(Integer.parseInt(ligne.substring(0, i)));
        i++;
        k = i;
        while (ligne.charAt(i) != ',') i++;
        operation.setNomDuCompte(ligne.substring(k, i));
        i++;
        k = i;
        while (ligne.charAt(i) != ',') i++;
        operation.setBenef(ligne.substring(k, i));
        i++;
        k = i;
        while (ligne.charAt(i) != ',') i++;
        operation.setTypeOperation(Integer.parseInt(ligne.substring(k, i)));
        i++;
        k = i;
        while (ligne.charAt(i) != ',') i++;
        operation.setMontant(Float.parseFloat(ligne.substring(k, i)));
        i++;
        k = i;
        /*if (operation.getTypeOperation() == 0) soldeNum = soldeNum - operation.getMontant();
        if (operation.getTypeOperation() == 1) soldeNum = soldeNum + operation.getMontant();
        if (operation.getTypeOperation() == 2) soldeNum = soldeNum - operation.getMontant();
        solde.setText(String.valueOf(soldeNum));*/
        //Log.i("DEBUG","AjouteLigneDeCompte : Solde : "+solde.getText().toString()+"Operation n°:"+livredecompte.size());
        while (ligne.charAt(i) != ',') i++;
        operation.setFrequence(Integer.parseInt(ligne.substring(k, i)));
        i++;
        k = i;
        while (ligne.charAt(i) != ',') i++;
        operation.setJour(Integer.parseInt(ligne.substring(k, i)));
        i++;
        k = i;
        while (ligne.charAt(i) != ',') i++;
        operation.setMois(Integer.parseInt(ligne.substring(k, i)));
        i++;
        k = i;
        operation.setAnnee(Integer.parseInt(ligne.substring(k, i + 4)));
        i++;
        k = i;
        rangeParDate(operation, calculpositiondate(operation.getJour(), operation.getMois(), operation.getAnnee()));
    }
    private void rangeParDate(Operation op, int position) {
        int i=0;
        int j=livredecompte.size();
        boolean ajoute=false;
        if (j != 0)
            while (i < j) {
                //("DEBUG","i="+i+"j="+j+"position cherchée=" + position+"/livredecompteposition="+livredecompte.get(i).getPosition());
                if (position < livredecompte.get(i).getPosition())
                {
                    //Log.i("DEBUG","Trouvé i="+i);
                    livredecompte.add(i, op);ajoute=true;
                    i = j - 1;
                }
                i++;
            }
        if (j == 0) livredecompte.add(op);
        if (ajoute==false&& j!=0) livredecompte.add(op); // on ajoute à la fin
        //Log.i("DEBUG","Ajout à la fin");
        //afficheHistoriqueCompte();


    }
    private int calculpositiondate(int jour, int mois, int annee) {
        int position = 1;
        position = jour + mois * 31 + annee * 365;
        return position;

    }
    private void saveLivredeCompte() {
        //String historique = historiqueCompte.getText().toString();
        FileOutputStream fos = null;
        String nomDuFichier=livredecompte.get(0).getNomDuCompte();
        //Log.i("DEBUG","Nom du ficheir a écrire:"+nomDuFichier);
        try {
            fos = openFileOutput(nomDuFichier+EXTENSION, MODE_PRIVATE);
            for (Operation operation : livredecompte)
                fos.write(operation.SauveOperationString().getBytes());
            //historiqueCompte.setText("");
            Toast.makeText(this, "Saved : " + getFilesDir() + "/" + livredecompte.get(0).getNomDuCompte(), Toast.LENGTH_LONG).show();
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
    private void loadLivredecompte(String fichier) {
        //("DEBUG","loadLivredecompte:"+fichier);
        FileInputStream fis = null;
        try {
            fis = openFileInput(fichier+EXTENSION);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String ligne;
            livredecompte.clear();
            while ((ligne = br.readLine()) != null) {
                sb.append(ligne).append("\n");
                ajouteLigneDeCompte(ligne); // doit decode la ligne complète !
            }
            afficheHistoriqueCompte();
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
                Log.i("DEBUG","Impossible de charger le fichier");
            }
        }



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
        //("DEBUG","jusque là Ok");
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
    }

    private void chercherepetition(){
        //
        // Il faut charger toutes les repetitions
        // calculer la date de répetitions.
        //comparer avec la date actuelle
        // si depassée il faut proposer de l'ajouter sinon on recalcule la prochaine.
        // TODO Dois rechercher si repetition à faire
    }
    private String premierfichier(){
        FileInputStream fis = null;
        String ligne1="";

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
                if (i==1) ligne1=ligne; // doit decode la ligne complète !
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
        return ligne1;
    }

}

