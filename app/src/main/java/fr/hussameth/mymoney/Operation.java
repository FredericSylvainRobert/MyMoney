package fr.hussameth.mymoney;

import android.util.Log;
public class Operation  {
    private String nomDuCompte;
    private String beneficiaire;
    private int typeOperation;// 0=débit'-',      1= crédit'+',       2'>' virement benef => nom compte benef
    private float montant;
    private int frequence;
    private int jour=1;
    private int mois=1;
    private int annee=2000;
    private int position=1;
                                                // ici on créée le construteur
    public Operation (String nomCompte,String benef,int typeoperation ,float mont,int freq, int Jour, int Mois, int Annee,int Position){
        nomDuCompte=nomCompte;
        beneficiaire=benef;
        typeOperation=typeoperation; // 0=débit '-' ,1= crédit  '+' , 2 virement benef '>'  => nom compte=benef
        montant=mont;
        frequence=freq; // 0 pas de répétition, 10 tous les mois , 20 tous les 2 mois, 30 toutes les semaines (jour fixe)
        jour=Jour;      // 1 lundi , 2 mardi , 3 mercredi , 4 jeudi , 5 vendredi , 6 samedi , 7 dimanche
        mois=Mois;
        annee=Annee;
        position=Position;
    }
    public void decode (String ligne){
        //
        // decode et
        //
        int i = 0;
        int k = 0;
        while (ligne.charAt(i) != ',') i++; //// Lecture de la position
        setPosition(Integer.parseInt(ligne.substring(0, i)));
        i++;
        k = i;
        while (ligne.charAt(i) != ',') i++;   /// Lecture du nomducompte
        setNomDuCompte(ligne.substring(k, i));
        i++;
        k = i;
        while (ligne.charAt(i) != ',') i++;   //// Lecture du beneficiaire
        setBenef(ligne.substring(k, i));
        i++;
        k = i;
        while (ligne.charAt(i) != ',') i++;   //// lecture du type opération  // 0=débit '-' ,1= crédit  '+' , 2 virement benef '>'  => nom compte=benef
        setTypeOperation(Integer.parseInt(ligne.substring(k, i)));
        i++;
        k = i;
        while (ligne.charAt(i) != ',') i++;     //// lecture du montant en float
        setMontant(Float.parseFloat(ligne.substring(k, i)));
        i++;
        k = i;
        while (ligne.charAt(i) != ',') i++;    /// lecture de la fréquence // 0 pas de répétition, 10 tous les mois , 20 tous les 2 mois, 30 toutes les semaines (jour fixe)
        setFrequence(Integer.parseInt(ligne.substring(k, i)));  // 1 lundi , 2 mardi , 3 mercredi , 4 jeudi , 5 vendredi , 6 samedi , 7 dimanche
        i++;
        k = i;
        while (ligne.charAt(i) != ',') i++;    //// lecture du jour
        setJour(Integer.parseInt(ligne.substring(k, i)));
        i++;
        k = i;
        while (ligne.charAt(i) != ',') i++;   //// lecture du mois
        setMois(Integer.parseInt(ligne.substring(k, i)));
        i++;
        k = i;                                 /// lecture de l'annee
        setAnnee(Integer.parseInt(ligne.substring(k, i + 4)));
        i++;
        k = i;
    }



    public void afficheLog(String nomCompte,int typeOperation,String benef,float mont,/*boolean repet,*/ int freq, int jour, int Mois, int Annee){
        Log.i("DEBUG","Compte:"+ nomCompte + "Type opé="+typeOperation+" ,Beneficaire:" + benef + ", Montant :" + mont + " ,Fréquence:" + freq +" ,Jour="+ jour+"/"+Mois+"/"+Annee+"position="+position);
    }
    public void afficheLog(){
        Log.i("DEBUG","afficheLog : Compte:"+position+","+nomDuCompte + " ,Beneficaire:" + beneficiaire + ", Montant :" + montant + " ,Fréquence:" + frequence +" ,Jour="+ jour+"/"+mois+"/"+annee+"position="+position);
    }
    public String afficheOperationString(){
        String signe="";
        if (typeOperation==0) signe="-";
        if (typeOperation==1) signe="+";
        if (typeOperation==2) signe=">";
        return signe+"/"+jour+"/"+mois+"/"+annee+" , "+beneficiaire+" , " + montant + " € "+frequence+" \n" ;
    }

    public String SauveOperationString(){
            position = jour + mois * 31 + annee * 365;
        return position+","+nomDuCompte+","+beneficiaire+","+typeOperation+","+montant+","+frequence+","+jour+","+mois+","+annee+"*\n";
    }

    public int getPosition(){ return position; }
    public void setPosition(int posi){
        position=posi;
    }
    public String getDateStr(){
        return  jour+"/"+mois+"/"+annee;
    }

    public String getNomDuCompte(){
        return nomDuCompte;
    }
    public void setNomDuCompte(String ndc){
        nomDuCompte=ndc;
    }


    public String getBenef(){
        return beneficiaire;
    }
    public void setBenef(String ben){
        beneficiaire=ben;
    }

    public int getTypeOperation(){return typeOperation;}
    public void setTypeOperation(int typ){typeOperation=typ;}
    public float getMontant(){
        return montant;
    }
    public void setMontant(float mont){montant=mont;}

    public int getFrequence(){
        return frequence;
    }
    public void setFrequence(int freq){frequence=freq;}
    public int getJour(){
        return jour;
    }
    public void setJour(int j){
        jour=j;
    }


    public int getMois(){
        return mois;
    }
    public void setMois(int moi){mois=moi;}

    public int getAnnee(){
        return annee;
    }
    public void setAnnee(int an){
        annee=an;
    }

    public void ajoutemois(int nbmois){
        position+=nombrejourdansmois(mois);
        mois+=nbmois;
        if (mois>12) { mois-=12; annee++; }

    }

    private int nombrejourdansmois(int mois){

        if (mois==1) return 31;
        if (mois==3) return 31;
        if (mois==5) return 31;
        if (mois==7) return 31;
        if (mois==9) return 31;
        if (mois==11) return 31;
        if (mois==2) return 29;
        return 30;

    }

    public void setDateString(String dateString){
        int i=0;
        int j=0;
        Log.i("DEBUG","dateSring="+dateString+"!");
        for(i=0;i<dateString.length();i++) {
            Log.i("DEBUG",String.valueOf(dateString.charAt(i)));
            Log.i("DEBUG","i="+i);
        }
        i=0;
        while(dateString.charAt(i)!='/') i++;
        Log.i("DEBUG","jour="+dateString.substring(0, i)+"!i="+i);

        jour=Integer.valueOf(dateString.substring(0, i ));i++;
        j=i;
        while(dateString.charAt(i)!='/') i++;
        Log.i("DEBUG","mois="+dateString.substring(j, i)+"!");

        mois=Integer.valueOf(dateString.substring(j,i));
        annee=Integer.valueOf(dateString.substring(i+1,dateString.length()));


    }

}
