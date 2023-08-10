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
        return signe+"/"+jour+"/"+mois+"/"+annee+" , "+beneficiaire+" , " + montant + " € \n" ;
    }

    public String SauveOperationString(){

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

    public void ajouteunmois(){
        position+=nombrejourdansmois(mois);
        mois=mois+1;
        if (mois>12) { mois=1; annee++; mois=1;}

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
}
