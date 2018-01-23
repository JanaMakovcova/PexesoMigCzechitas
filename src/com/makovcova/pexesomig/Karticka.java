package com.makovcova.pexesomig;

import javax.swing.*;

public class Karticka extends JLabel {
    /** Číslo obrázku ze sady */
    int cisloObrazku;
    /** Jestli je otočená lícem nahoru */
    boolean jeOtocenaNahoru;
    /** Obrázek kartičky */
    ImageIcon obrazek;
    ImageIcon rubKarticky;
    /**
     * Vrátí číslo obrázku ze sady  (v balíčku pexesa jsou vždy 2 obrázky se stejným číslem)
     * @return      Číslo obrázku
     */
    public int getCisloObrazku(){
        return cisloObrazku;
    }
    public void otocKartuLicem() {
        this.setIcon(obrazek);
        this.jeOtocenaNahoru = true;

    }
    public void otocKartuRubem(){
        this.setIcon(rubKarticky);
        this.jeOtocenaNahoru = false;
    }
    public void otocKartuOpacne(){
        if (this.jeOtocenaNahoru){
            this.otocKartuRubem();
        } else {
            this.otocKartuLicem();
        }
    }
    /**
     * Porovnává, jestli jsou dva obrázky stejné - podle čísla obrázku ze sady
     * (v pexesu jsou vylozene 2 stejné sady obrázků)
     * @return     true/false
     */
    public boolean porovnejSJinouKartou(Karticka karta){

        if (this.getCisloObrazku() == karta.getCisloObrazku()) {
            return true;
        } else {
            return false;
        }
    }
}


