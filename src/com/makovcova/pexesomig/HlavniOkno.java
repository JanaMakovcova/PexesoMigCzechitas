package com.makovcova.pexesomig;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;
import java.util.List;
import javax.imageio.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import net.miginfocom.swing.*;
import net.sevecek.util.*;

public class HlavniOkno extends JFrame {

    private JPanel contentPane;
    private List<Karticka> vsechnyKarticky;
    private Karticka karticka1;
    private Karticka karticka2;
    private int pocetPokusu;
    private Random nahodneCislo = new Random();
    private int pocetObrazku;
    private int zmenaPocetObrazku;
    private JLabel hrej;
    private JSpinner spinnerPocetObrazku;
    private JLabel labSpinner;

    public HlavniOkno() {
        pocatecniNastaveni();
    }

    private void priOtevreniOkna(WindowEvent e) {
        nastavMig();
    }

    /**
     * počáteční nastavení okna aplikace
     */
    private void pocatecniNastaveni() {
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Pexeso Czechitas");
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                priOtevreniOkna(e);
            }
        });
    }

    /**
     * nastavení MigLayout pro rozvržení pexesa 4*4 obrázky v okně
     */
    private void nastavMig() {
        hrej = new JLabel();
        hrej.setText("HRAJ PEXESO");
        hrej.setBorder(new BevelBorder(BevelBorder.RAISED));
        hrej.setFont(hrej.getFont().deriveFont(hrej.getFont().getSize() + 5f));
        hrej.setBackground(Color.magenta);
        hrej.setOpaque(true);
        spinnerPocetObrazku = new JSpinner();
        spinnerPocetObrazku.setModel(new SpinnerNumberModel(8, 2, 8, 1));
        spinnerPocetObrazku.setFont(spinnerPocetObrazku.getFont().deriveFont(spinnerPocetObrazku.getFont().getSize() + 5f));
        labSpinner = new JLabel();
        labSpinner.setText("POČET OBRÁZKŮ");
        labSpinner.setFont(labSpinner.getFont().deriveFont(labSpinner.getFont().getSize() + 5f));
        labSpinner.setForeground(Color.magenta);
        zmenaPocetObrazku = (int) spinnerPocetObrazku.getModel().getValue();
        nastavMigPodleRadku(getPocetRadku(zmenaPocetObrazku));
        spinnerPocetObrazku.addChangeListener(e -> spinnerZmena(e));
        hrej.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                priKliknutiNaHraj(e);
            }
        });
        pack();
        setLocationRelativeTo(null);
    }

    /**
     * Podle počtu řádků se nastaví nový MigLayout
     * metoda slouží i pro počáteční nastavení okna - defaultně maximální hodnota spinneru
     * @param pocetRadku
     */
    private void nastavMigPodleRadku(int pocetRadku) {
        String migStringProKarticky = "[150, center]";
        String nastaveniLayoutu = "";
        for (int i = 0; i < pocetRadku; i++) {
            nastaveniLayoutu = nastaveniLayoutu + migStringProKarticky;
        }
        this.contentPane = (JPanel) this.getContentPane();
        this.contentPane.setBackground(this.getBackground());
        contentPane.setLayout(new MigLayout(
                "insets 0,hidemode 3",
                // columns
                "[grow]" +
                        migStringProKarticky +
                        migStringProKarticky +
                        migStringProKarticky +
                        migStringProKarticky +
                        "[grow]",
                // rows
                "[50,center]" +
                        nastaveniLayoutu +
                        "[grow]"));
        this.contentPane.add(spinnerPocetObrazku, "cell 1 0 2 1, gapx 15 15");
        this.contentPane.add(labSpinner, "cell 1 0 2 1");
        this.contentPane.add(hrej, "cell 3 0, growx, gapx 15 15");
        hrej.setHorizontalAlignment(SwingConstants.CENTER);
        pack();

    }

    /**
     * vytvoření ArrayList s Kartičkami, ArrayList obsahuje obě sady obrázků
     */
    private void ulozKartickyDoPole() throws ApplicationPublicException {
        try {
            vsechnyKarticky = new ArrayList<>();
            for (int j = 0; j < 2; j++) {
                for (int i = 1; i < pocetObrazku + 1; i++) {     //
                    Karticka karticka = new Karticka();
                    karticka.jeOtocenaNahoru = true;
                    karticka.cisloObrazku = i;
                    BufferedImage obrazekPexesa;
                    obrazekPexesa = ImageIO.read(HlavniOkno.class.getResourceAsStream("" + i + ".jpg"));
                    karticka.obrazek = new ImageIcon(obrazekPexesa);
                    karticka.setIcon(karticka.obrazek);
                    karticka.setBorder(new BevelBorder(BevelBorder.RAISED));
                    karticka.rubKarticky = new ImageIcon(ImageIO.read(HlavniOkno.class.getResourceAsStream("rub_czechitas.png")));
                    karticka.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mousePressed(MouseEvent e) {
                            priKliknutiNaLabel(e);
                        }
                    });
                    vsechnyKarticky.add(karticka);
                }
            }
        } catch (IOException e1) {
            throw new ApplicationPublicException("Nepodařilo se nahrát obrázek kartičky,", e1);
        }
    }

    /**
     * zamíchání prvků ArrayList s Kartičkami
     */
    private void zamichejKarticky() {
        for (int i = 0; i < 8; i++) {
            int cisloKarty1 = nahodneCislo.nextInt(2 * pocetObrazku);
            int cisloKarty2 = nahodneCislo.nextInt(2 * pocetObrazku);
            Karticka karta1 = vsechnyKarticky.get(cisloKarty1);
            Karticka karta2 = vsechnyKarticky.get(cisloKarty2);
            vsechnyKarticky.set(cisloKarty1, karta2);
            vsechnyKarticky.set(cisloKarty2, karta1);
        }
    }

    /**
     * Vyloží karty do okna v rozložení 4x4 v MigLayout
     */
    private void prekresliKarticky44() {
        int cisloPole = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (cisloPole < 2 * pocetObrazku) {
                    this.contentPane.add(vsechnyKarticky.get(cisloPole), "cell" + " " + (j + 1) + " " + (i + 1));
                    cisloPole++;
                }
            }
        }
        pack();
    }

    /*událost při kliknutí na kartu pexesa   - otoci se kartička za učitých podmínek, viz
    metoda otocKarticku

     */
    private void priKliknutiNaLabel(MouseEvent e) {
        Karticka karta = (Karticka) e.getSource();
        otocKarticku(karta);
    }

    /**
     *  změna čísla na spinneru způsobí změnu textu na JLabelu na HRAJ PEXESO a uložení
     *  hodnoty ze spinneru do proměnné zmenaPocetObrázků
     */
    private void spinnerZmena(ChangeEvent e) {
        JSpinner kterySpinner;
        kterySpinner = (JSpinner)e.getSource();
        zmenaPocetObrazku = (int)kterySpinner.getValue();
        hrej.setText("HRAJ PEXESO");
    }

    /**
     *  při kliknutí na Hraj Pexeso se zjistí kolik je na JSpinner zvoleno obrázků
     *  pokud je List s pexesem naplněný nebo prázdný, jsou už kartičky vyložné
     *  nebo hra dohraná a musí se smazat kartičky z hlavního okna
     *  na základě zvoleného počtu obrázků se spočítá počet řádků pro MigLayout a spustí se hra
     */
    private void priKliknutiNaHraj(MouseEvent e) {
        if ((vsechnyKarticky != null) && !(vsechnyKarticky.isEmpty())) {
            smazKartickyZOkna();
            repaint();
        }
        pocetObrazku = zmenaPocetObrazku;
        JLabel kteryLabel = (JLabel)e.getSource();
        kteryLabel.setText("HRAJEME");
        int pocetRadku = getPocetRadku(zmenaPocetObrazku);
        nastavMigPodleRadku(pocetRadku);
        hra();
    }

    /**
     * Podle počtu obrázků hry pexesa se spočítá počet řádků pro zobrazení
     * @param zmenaPocetObr
     * @return   pocetRadku
     */
    private int getPocetRadku(int zmenaPocetObr) {
        int pocetRadku = 4;
        switch (zmenaPocetObr) {
            case 2:
                pocetRadku = 1;
                break;
            case 3:
            case 4:
                pocetRadku = 2;
                break;
            case 5:
            case 6:
                pocetRadku = 3;
                break;
            case 7:
            case 8:
                pocetRadku = 4;
                break;
        }
        return pocetRadku;
    }

    /**
     * hraje se pexeso, počet pokusu se nastaví na 1, karičtičky se uloží do ArrayList,
     * zamíchají se, zobrazí a otočí rubem nahoru
     */
    private void hra() {
        pocetPokusu = 1;
        ulozKartickyDoPole();
        zamichejKarticky();
        prekresliKarticky44();
        otocVsechnyKartyRubem();
    }

    /**
     * metoda otočí Kartičku na líc, pokud není žádná otočená nebo je otočená jedna
     * pokud jsou otočeny obě, při dalším kliku se už otočené karty porovnají a případně odstraní z
     * contentPane, jsou-li stejné
     * zároveň se při každém pokusu načítá proměnná početPokusů
     *
     * @param karta    karta, na kterou se kliklo
     */
    private void otocKarticku(Karticka karta) {
        if (karticka1 == null) {
            karticka1 = karta;
            karta.otocKartuLicem();
            return;
        } else if (karticka2 == null) {
            if (!karta.equals(karticka1)) {       //takhle podminka kontroluje, jestli neni druha kliknuta karta stejna jako prvni
                karticka2 = karta;                // tj. kdyz se klikne podruhe na jiz otocenou karticku
                karta.otocKartuLicem();
            }
            return;
        }
        if (karticka1.porovnejSJinouKartou(karticka2)) {
            karticka1.setVisible(false);
            karticka2.setVisible(false);
            contentPane.remove(karticka1);
            contentPane.remove(karticka2);
            vsechnyKarticky.remove(karticka1);
            vsechnyKarticky.remove(karticka2);
            //test jestli jsou vsechny pryc  - vyskakovaci okno
            if (vsechnyKarticky.isEmpty()) {
                String vysledek = String.valueOf(pocetPokusu);
                JOptionPane.showMessageDialog(contentPane, "Konec hry, celkem jsi potřeboval " + vysledek + " tahů.");
                hrej.setText("HRAJ PEXESO");
            }
            pocetPokusu++;
        } else {
            pocetPokusu++;
        }
        otocVsechnyKartyRubem();
        karticka1 = null;
        karticka2 = null;
    }

    /**
     * otoč všechny Kartičky z ArrayList rubem nahoru
     */
    private void otocVsechnyKartyRubem() {
        for (Karticka karta : vsechnyKarticky) {
            if (karta != null) {
                karta.otocKartuRubem();
            }
        }
    }

    /**
     * smázání vyložených kartiček v okně při restartu hry
     */
    private void smazKartickyZOkna() {
        for (Karticka karta : vsechnyKarticky) {
            if (karta != null) {
                this.contentPane.remove(karta);
            }
        }
    }

}
