/*
 *   Alma on Line: Applicazione WEB per la visualizzazione 
 *   delle schede di indagine su popolazione dell'ateneo.<br />
 *   
 *   Copyright (C) 2018 Giovanroberto Torre<br />
 *   Alma on Line (aol), web application to publish students 
 *   and degrees informations.
 *   Copyright (C) renewed 2018 Universita' degli Studi di Verona, 
 *   all right reserved
 *
 *   This program is free software; you can redistribute it and/or modify 
 *   it under the terms of the GNU General Public License as published by 
 *   the Free Software Foundation; either version 2 of the License, or 
 *   (at your option) any later version. 
 *
 *   This program is distributed in the hope that it will be useful, 
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of 
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
 *   GNU General Public License for more details. 
 *
 *   You should have received a copy of the GNU General Public License 
 *   along with this program; if not, write to the Free Software 
 *   Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA<br>
 *   
 *   Giovanroberto Torre <giovanroberto.torre@univr.it>
 *   Sistemi Informatici per il Reporting di Ateneo
 *   Universita' degli Studi di Verona
 *   Via Dell'Artigliere, 8
 *   37129 Verona (Italy)
 */

package it.alma.bean;

import it.alma.exception.AttributoNonValorizzatoException;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;
import java.util.Vector;


/**
 * <p>Classe per rappresentare occorrenze di insegnamento, anche dette,
 * in terminologia Reporting, <cite>&quot;Attivit&agrave; Didattiche&quot;</cite>.</p>
 * Le &quot;Attivit&agrave; Didattiche&quot; (o AD) possono presentarsi sotto
 * molteplici forme ed avere svariate tipologie, come per esempio:
 * <ul>
 *  <li>AD Semplici</li>
 *  <li>AD con Moduli</li>
     <ul>
 *    <li>Insegnamento Padre</li>
 *    <li>Modulo (Insegnamento Figlio)</li>
 *   </ul>
 *  <li>AD con UL (Unit&agrave; Logistiche)</li>
 *  <li>Mutuazioni</li>
 *   <ul>
 *    <li>Reale</li>
 *    <li>Fantasma</li>
 *   </ul>
 *  <li>Repliche</li> 
 * </ul>
 * Le Repliche e le Unit&agrave; Logistiche in Esse3 vanno sotto il nome di
 * <cite>Partizionamenti</cite>.
 * 
 * @author <a href="mailto:giovanroberto.torre@univr.it">Giovanroberto Torre</a>
 */
public class CourseBean implements Serializable, Comparable<CourseBean> {
    
    /**
     * La serializzazione necessita di dichiarare una costante di tipo long
     * identificativa della versione seriale. 
     * (Se questo dato non fosse inserito, verrebbe calcolato in maniera automatica
     * dalla JVM, e questo potrebbe portare a errori riguardo alla serializzazione). 
     */
    private static final long serialVersionUID = 1L;
    /**
     *  Nome di questa classe. 
     *  Viene utilizzato per contestualizzare i messaggi di errore.
     */
    private final String FOR_NAME = "\n" + this.getClass().getName() + ": "; //$NON-NLS-1$
    /* ************************************************************************ *  
     *               Attributi da tracciato record estrazione WI                *
     * ************************************************************************ */
    /** Contatore (row.names) */
    private int id; 
    /** Codice Corso di Studi di WebIntegrato (WI) */
    private String codiceCdSWI;
    /** Codice Corso di Studi di UGOV */
    private String codiceCdSUGOV;
    /** Nome Corso di Studi di WI */
    private String nomeCdSWI;
    /** Nome Corso di Studi di UGOV */
    private String nomeCdSUGOV;
    /** Nome struttura didattica associata */
    private String dipartimentoScuola;
    /** Sede di WebIntegrato */
    private String sedePrimariaCDS;
    /** Codice Attivit&agrave; Didattica di WebIntegrato */
    private int codiceADWI;
    /** Codice Attivit&agrave; Didattica di UGOV */
    private String codiceADUGOV;
    /** Nome Attività Didattica (AD) */
    private String nomeInsegnamento;
    /** Settore Scientifico Disciplinare (SSD) */
    private String SSD;
    /** Cognome Docente Attivit&agrave; Didattica */
    private String cognomeDocente;
    /** Nome Docente Attivit&agrave; Didattica */
    private String nomeDocente;
    /** CF Docente Attivit&agrave; Didattica */
    private String codiceFiscaleDocente;
    /** Flag Docente coordinatore Attivit&agrave; Didattica */
    private int coordinatore;
    /** Docente Attivit&agrave; Didattica */
    private PersonBean docente;
     /** Flag Modulo */
    private int modulo;
    /** Flag Unit&agrave; Logistica */
    private boolean ul;
    /** Eventuale discriminante modulo/partizionamento */
    private String discriminante;
    /** Crediti totali (CFU) */
    private float creditiTotali;
    /** Crediti di lezione (LEZ) */
    private float creditiLezione;
    /** Crediti di laboratorio (LAB) */
    private float creditiLaboratorio; 
    /** Crediti di altro tipo (ALT) */
    private float creditiAltro;
    /** Ore totali */
    private String ore;
    /** Frazione ore di lezione (LEZ) */
    private String oreLezione;
    /** Frazione ore di laboratorio (LAB) */
    private String oreLaboratorio;
    /** Frazione ore di altro (ALT) */
    private String oreAltro;
    /** Frazione ore di seminario (SEM) */
    private String oreSeminario;
    /** Frazione ore di esercitazione (ESE) */
    private String oreEsercitazione;
    /** Frazione ore di tirocinio (TIR) */
    private String oreTirocinio;
    /** Data inizio periodo didattico */
    private String inizioPerDid;
    /** Data fine periodo didattico */
    private String finePerDid;
    /* ************************************************************************ *  
     *                      Attributi utili da codice legacy                    *
     * ************************************************************************ */
    /** Anno Accademico in esame */
    private String annoAccademico;
    /** Flag identificante la modulazione dell'insegnamento cio&egrave;
     *  identificante il fatto che l'insegnamento corrente &egrave; padre */
    private boolean divisoInModuli;
    /** Nome modulo in caso l'AD corrente sia un Modulo di un Padre*/
    private String nomeModulo;
    /** Crediti del Modulo */
    private float crediti;
    /** Crediti del Padre */
    private float creditiPadre;
    /** Discriminante per crediti diversi */
    private String discrPerCreditiDiversi;
    /** Discriminante Modulo in caso di Modulo */
    private String discriminanteModulo;
    /** Flag identificante il fatto che l'AD &egrave; suddivisa in Unit&agrave; Logistiche */
    private boolean divisoInUnita;
    /** Nome unit&agrave; logistica in caso l'AD corrente si un'Unit&agrave; Logistica di un Padre */
    private String nomeUL;
    /** Identificativo del Padre nel caso in cui l'AD corrente si un'Unit&agrave; Logistica di un Padre */
    private int idPadreUL;
    /** Reale dell'insegnamento, in caso l'AD corrente sia un Fantasma */
    private CourseBean mutuante;
    /** ID del reale dell'insegnamento, in caso il corrente sia un fantasma */
    private int idMutuante;
    /** Flag identificante il fatto che l'AD corrente sia un Reale di un Fantasma */
    private boolean mutuato;
    /** Flag replica */
    private char replica;
    /** Eventuale URL a una pagina pubblica dell'insegnamento */
    private String url;
    /** Flag identificante il fatto che l'AD corrente ha ore di lezione (LEZ) */
    private boolean conLezioni;
    /** Elenco di periodi in cui l'insegnamento &egrave; erogato */
    private Vector<CodeBean> periodi;
    /** Inizio del periodo di lezioni */
    private Date inizioPeriodo;
    /** Fine del periodo di lezioni */
    private Date finePeriodo;
    /** Lingua in cui l'ìnsegnamento &egrave; erogato */
    private String linguaErog;
    /** Lista di docenti collegati all'Attivit&agrave; Didattica */
    private Vector<PersonBean> docenti;
    /** Misura in cui &egrave; espresso il peso dell'insegnamento (p.es. 'CFU' o 'Ore') */ 
    private String misura;
    /** Vettore di corsi di studio a cui l'insegnamento &egrave; collegato */
    private Vector<DegreeBean> corsiStudi;
    /** Sede didattica ove &egrave; erogato l'insegnamento */
    private String sede;
    /** Provincia sede didattica */
    private String provinciaSede;
    /** Obiettivi Formativi / Sbocchi professionali */
    private String obiettiviFormativi;
    /** Lingua in cui sono inserite le spiegazioni inerenti gli obiettivi formativi */
    private String linguaObiettiviFormativi;
    /** Programma */
    private String programma;
    /** Lingua in cui sono inserite le informazioni inerenti al programma d'esame */
    private String linguaProgramma;
    /** Modalit&agrave; di esame */
    private String modalitaEsame;
    /** Lingua in cui sono inserite le spiegazioni inerenti le modalit&agrave: di esame */
    private String linguaModalitaEsame;
    /** Flag identificante il Percorso Comune ai vari curricula */
    private boolean comuneIndirizzi;

    
    /**
     * Costruttore.
     * Inizializza i campi a valori convenzionali di default.
     */
    public CourseBean() {
        annoAccademico = nomeInsegnamento = url = null;
        codiceCdSWI = codiceCdSUGOV = nomeCdSWI = nomeCdSUGOV = dipartimentoScuola = sedePrimariaCDS = null;
        codiceADWI = -2;
        codiceADUGOV = nomeInsegnamento = SSD = null;
        cognomeDocente = nomeDocente = codiceFiscaleDocente = null;
        coordinatore = -2;
        docente = null;
        docenti = null;
        modulo = -2;
        ul = false;
        id = modulo = idMutuante = idPadreUL = -2;
        divisoInModuli = divisoInUnita = mutuato = conLezioni = comuneIndirizzi = false;
        nomeModulo = discriminanteModulo = discrPerCreditiDiversi = discriminante = nomeUL = null;
        mutuante = null;
        misura = null;        
        replica = ' ';        
        crediti = creditiTotali = creditiLezione = creditiLaboratorio = creditiAltro = -2;
        creditiPadre = -2;
        ore = oreLezione = oreLaboratorio = oreAltro = oreSeminario = oreEsercitazione = oreTirocinio = null;
        periodi = null;
        corsiStudi = null;
        obiettiviFormativi = linguaObiettiviFormativi = null;
        programma = linguaProgramma = null;
        modalitaEsame = linguaModalitaEsame = null;
        inizioPeriodo = finePeriodo = new Date(0);
        sede = provinciaSede = null;
        linguaErog = null;
    }

    
    /**
     * Restituisce una chiave univoca per un'Attivit&agrave; Didattica.
     * Due Attivit&agrave; Didattiche sono uguali quando hanno la stessa chiave univoca.
     * 
     * @return
     * @throws AttributoNonValorizzatoException
     */
    public String getKey()
                  throws AttributoNonValorizzatoException {
        return this.getCodiceCdSUGOV().trim() + 
               this.getCodiceADUGOV().trim()  + 
               this.getCodiceFiscaleDocente().trim() + 
               this.getInizioPerDid().trim() + 
               this.getFinePerDid().trim();
    }
    
    
    /**
     * Restituisce una chiave univoca per un'Attivit&agrave; Didattica.
     * Due Attivit&agrave; Didattiche sono uguali quando hanno la stessa chiave univoca.
     * 
     * @return
     * @throws AttributoNonValorizzatoException
     */
    public String getSmallKey()
                  throws AttributoNonValorizzatoException {
        return this.getCodiceCdSUGOV().trim() + 
               this.getCodiceADUGOV().trim()  + 
               this.getCodiceFiscaleDocente().trim();
    }
    
    
    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof CourseBean) {
            return this.id == ((CourseBean) obj).id 
                && this.codiceCdSUGOV.trim().equals(((CourseBean) obj).codiceCdSUGOV.trim())
                && this.codiceADUGOV.trim().equals(((CourseBean) obj).codiceADUGOV.trim())
                && this.codiceFiscaleDocente.trim().equals(((CourseBean) obj).codiceFiscaleDocente.trim())
                && this.inizioPerDid.trim().equals(((CourseBean) obj).inizioPerDid.trim())
                && this.finePerDid.trim().equals(((CourseBean) obj).inizioPerDid.trim());
        }
        return super.equals(obj);
    }
    

    /**
     * Restituisce un intero:
     * <ul>
     * <li>&lt; 0</li>
     * <li>&equiv; 0</li>
     * <li>&gt; 0</li>
     * </ul> 
     * a seconda che l'id del dipartimento
     * corrente sia minore, uguale, maggiore
     * dell'id del dipartimento <code>o</code> passato come argomento.
     * 
     */
    @Override
    public int compareTo(CourseBean o) {
        int std = -1;
        if (o == null)
            return 1;
        try {
            std = this.getId();
        } catch (AttributoNonValorizzatoException anve) {
            // Auto-generated method stub
            return 0;
        }
        try {
            return new Integer(std).compareTo(new Integer(o.getId())); 
        } catch (AttributoNonValorizzatoException anve) {
            return 1;
        }
    }
        
    
    /* **************************************************** *
     *           Metodi getter e setter per id              *
     * **************************************************** */
    /**
     * @return id
     */
    public int getId() 
              throws AttributoNonValorizzatoException {
        if (id == -2) {
            throw new AttributoNonValorizzatoException(FOR_NAME + "Attributo id non valorizzato!");
        } else {
            return this.id;
        }
    }
    
    /**
     * @param id
     */
    public void setId(int i) {
        this.id = i;
    }

    
    /* **************************************************** *
     *           Metodi getter e setter per AA              *
     * **************************************************** */
    /**
     * @return annoAccademico
     */
    public String getAnnoAccademico() 
                             throws AttributoNonValorizzatoException {
        if (annoAccademico == null) {
            throw new AttributoNonValorizzatoException(FOR_NAME + "Attributo annoAccademico non valorizzato!");
        } else {
            return this.annoAccademico;
        }
    }

    /**
     * @param annoAccademico
     */
    public void setAnnoAccademico(String string) {
        this.annoAccademico = string;
    }

    
    /* **************************************************** *
     *           Metodi getter e setter per nome AD         *
     * **************************************************** */
    /**
     * @return nomeInsegnamento
     */
    public String getNomeInsegnamento() throws AttributoNonValorizzatoException {
        if (nomeInsegnamento == null) {
            throw new AttributoNonValorizzatoException(
                    "CourseBean: attributo nomeInsegnamento non valorizzato!");
        } else {
            return this.nomeInsegnamento;
        }
    }

    /**
     * @param nomeInsegnamento
     */
    public void setNomeInsegnamento(String v) {
        this.nomeInsegnamento = v;
    }

    
    /* **************************************************** *
     *                        Replica                       *
     * **************************************************** */
    /**
     * @return replica
     */
    public char getReplica() {
        return replica;
    }

    /**
     * @param replica
     */
    public void setReplica(char v) {
        this.replica = v;
    }

    
    /* **************************************************** *
     *                    Discriminante                     *
     * **************************************************** */
    /**
     * @return discriminante
     */
    public String getDiscriminante() {
        return this.discriminante;
    }

    /**
     * @param discriminante
     */
    public void setDiscriminante(String v) {
        this.discriminante = v;
    }

    
    /* **************************************************** *
     *                 discrPerCreditiDiversi               *
     * **************************************************** */
    /**
     * @return discrPerCreditiDiversi
     * @throws AttributoNonValorizzatoException
     */
    public String getDiscrPerCreditiDiversi() throws AttributoNonValorizzatoException {
        if (discrPerCreditiDiversi == null) {
            throw new AttributoNonValorizzatoException(
                    "CourseBean: attributo discrPerCreditiDiversi non valorizzato!");
        } else {
            return this.discrPerCreditiDiversi;
        }
    }

    /**
     * @param discrPerCreditiDiversi
     */
    public void setDiscrPerCreditiDiversi(String v) {
        this.discrPerCreditiDiversi = v;
    }

    
    /* **************************************************** *
     *                        Nome Modulo                   * 
     * **************************************************** */
    /**
     * @return nomeModulo
     */
    public String getNomeModulo() {
        return this.nomeModulo;
    }

    /**
     * @param nomeModulo
     */
    public void setNomeModulo(String v) {
        this.nomeModulo = v;
    }

    
    /* **************************************************** *
     *                         Crediti                      *
     * **************************************************** */
    /**
     * @return crediti
     */
    public float getCrediti() throws AttributoNonValorizzatoException {
        if (crediti == -2) {
            throw new AttributoNonValorizzatoException("CourseBean: attributo crediti non valorizzato!");
        }
        return this.crediti;
    }

    /**
     * @param crediti
     */
    public void setCrediti(float crediti) {
        this.crediti = crediti;
    }

    
    /* **************************************************** *
     *                      Crediti Padre                   *
     * **************************************************** */
    /**
     * @return creditiPadre
     */
    public float getCreditiPadre() throws AttributoNonValorizzatoException {
        if (creditiPadre == -2) {
            throw new AttributoNonValorizzatoException(
                    "CourseBean: attributo creditiPadre non valorizzato!");
        }
        return this.creditiPadre;
    }

    /**
     * @param creditiPadre
     */
    public void setCreditiPadre(float creditiPadre) {
        this.creditiPadre = creditiPadre;
    }

    
    /* **************************************************** *
     *                           URL                        *
     * **************************************************** */
    /**
     * @return url
     */
    public String getUrl() throws AttributoNonValorizzatoException {
        if (url == null) {
            throw new AttributoNonValorizzatoException("CourseBean: attributo url non valorizzato!");
        }
        return this.url;
    }

    /**
     * @param url
     */
    public void setUrl(String v) {
        this.url = v;
    }
    

    /* **************************************************** *
     *                         Docenti                      *
     * **************************************************** */
    /**
     * Metodo per assegnare i docenti che tenfono l'insegnamento
     **/
    public void setDocenti(Vector<PersonBean> d) {
        docenti = d;
    }

    /**
     * Metodo che restituisce i docenti dell'insegnamento
     */
    public Vector<PersonBean> getDocenti() {
        return docenti;
    }

    
    /* **************************************************** *
     *                          Misura                      *
     * **************************************************** */
    /**
     * Metodo per assegnare la misura dell'insegnamento (crediti/unita didattiche..)
     */
    public void setMisura(String m) {
        misura = m;
    }

    /**
     * Metodo che restituisce la misura dell'insegnamento (crediti/unita didattiche..)
     */
    public String getMisura() {
        return misura;
    }

    
    /* **************************************************** *
     *       Corsi di Studio in cui l'AD e' presente        *
     * **************************************************** */
    /**
     * Metodo per assegnare un vector di CorsoStudi a cui l'occorrenza appartiene
     */
    public void setCorsiStudi(Vector c) {
        corsiStudi = c;
    }

    /**
     * Metodo che restituisce un vector di CorsoStudi a cui l'occorrenza apprtiene
     */
    public Vector getCorsiStudi() {
        return corsiStudi;
    }

    
    /* **************************************************** *
     *  ID del Reale (nel caso AD corrente sia un Fantasma) *
     * **************************************************** */
    /**
     * Metodo per assegnare l'id del mutuante
     */
    public void setIdMutuante(int c) {
        idMutuante = c;
    }

    /**
     * Metodo che restituisce l'id del mutuante
     */
    public int getIdMutuante() {
        return idMutuante;
    }
    
    
    /* **************************************************** *
     *     Reale  (nel caso AD corrente sia un Fantasma)    *
     * **************************************************** */
    /**
     * Metodo per assegnare il mutuante
     */
    public void setMutuante(CourseBean c) {
        mutuante = c;
    }

    /**
     * Metodo che restituisce il mutuante
     */
    public CourseBean getMutuante() {
        return mutuante;
    }

    
    /* **************************************************** *
     *                     Flag Mutuato                     * 
     *  (identifica il fatto che l'AD corrente e' il Reale  *
     *                    di un Fantasma)                   *
     * **************************************************** */    
    /**
     * Metodo che ritorna se l'occorrenza insegnamento &egrave; mutuata
     */
    public boolean isMutuato() {
        return mutuato;
    }

    /**
     * Metodo per settare se l'occorrenza insegnamento &egrave; mutuata
     */
    public void setMutuato(boolean b) {
        mutuato = b;
    }
    
    
    /* **************************************************** *
     *     Flag per indicare che AD corrente e' modulato    *
     * **************************************************** */
    /**
     * Metodo che ritorna se l'occorrenza insegnamento ha moduli
     */
    public boolean isDivisoInModuli() {
        return divisoInModuli;
    }

    /**
     * Metodo per settare se l'occorrenza insegnamento ha moduli
     */
    public void setDivisoInModuli(boolean b) {
        divisoInModuli = b;
    }
    
    
    /* **************************************************** *
     *                     Discriminante                    *
     * **************************************************** */
    /**
     * @return discriminanteModulo
     */
    public String getDiscriminanteModulo() {
        return this.discriminanteModulo;
    }

    /**
     * @param discriminanteModulo
     */
    public void setDiscriminanteModulo(String v) {
        this.discriminanteModulo = v;
    }

    
    /* **************************************************** *
     *                       Flag Modulo                    *
     * **************************************************** */
    /**
     * Metodo per assegnare il modulo
     */
    public void setModulo(int c) {
        modulo = c;
    }

    /**
     * Metodo che restituisce il modulo
     */
    public int getModulo() {
        return modulo;
    }

    
    /* **************************************************** *
     *                       Gestione UL                    *
     * **************************************************** */
    /**
     * Metodo che ritorna se l'occorrenza insegnamento ha UL
     */
    public boolean isDivisoInUnita() {
        return divisoInUnita;
    }

    /**
     * Metodo per settare se l'occorrenza insegnamento ha UL
     */
    public void setDivisoInUnita(boolean b) {
        divisoInUnita = b;
    }
    
    /**
     * @return nomeUL
     */
    public String getNomeUL() {
        return this.nomeUL;
    }

    /**
     * @param nomeUL
     */
    public void setNomeUL(String v) {
        this.nomeUL = v;
    }

    /**
     * @return Value of idPadreUL
     */
    public int getIdPadreUL() { 
        return this.idPadreUL;
    }

    /**
     * @param idPadreUL
     */
    public void setIdPadreUL(int v) {
        this.idPadreUL = v;
    }
    
    
    /* **************************************************** *
     *         Inizio e Fine Periodo; lista Periodi         *
     * **************************************************** */
    /**
     * @return inizio
     */
    public Date getInizioPeriodo() throws AttributoNonValorizzatoException {
        if (new Date(0).equals(inizioPeriodo)) {
            throw new AttributoNonValorizzatoException(FOR_NAME + "Attributo inizio periodo non valorizzato!");
        } 
        return this.inizioPeriodo;
    }

    /**
     * @param inizio
     */
    public void setInizioPeriodo(Date date) {
        inizioPeriodo = date;
    }

    /**
     * @return fine
     */
    public Date getFinePeriodo() throws AttributoNonValorizzatoException {
        if (new Date(0).equals(finePeriodo)) {
            throw new AttributoNonValorizzatoException(FOR_NAME + "Attributo fine non valorizzato!");
        }
        return this.finePeriodo;
    }

    /**
     * @param fine
     */
    public void setFinePeriodo(Date date) {
        finePeriodo = date;
    }

    /**
     * Getter for property Periodi.
     * 
     * @return Value of property Periodi.
     */
    public Vector getPeriodi() throws AttributoNonValorizzatoException {
        if (periodi == null) {
            throw new AttributoNonValorizzatoException("CourseBean: attributo Periodi non valorizzato!");
        } else {
            return this.periodi;
        }
    }

    /**
     * Setter for property Periodi.
     * 
     * @param linguaProgramma New value of property Periodi.
     */
    public void setPeriodi(Vector periodi) {
        this.periodi = periodi;
    }
    
    
    /* **************************************************** *
     *                    AD di tipo LEZ                    * 
     *    (o che tra i suoi carichi ha Ore di tipo LEZ)     *
     * **************************************************** */
    /**
     * Metodo che ritorna se l'occorrenza insegnamento ha lezioni
     */
    public boolean isConLezioni() {
        return conLezioni;
    }

    /**
     * Metodo per settare se l'occorrenza insegnamento prevede lezioni
     */
    public void setConLezioni(boolean b) {
        conLezioni = b;
    }


    /* **************************************************** *
     *   Obiettivi formativi e lingua in cui sono espressi  *
     * **************************************************** */
    /**
     * Getter for property obiettiviFormativi.
     * 
     * @return Value of property obiettiviFormativi.
     */
    public String getObiettiviFormativi() throws AttributoNonValorizzatoException {
        if (obiettiviFormativi == null) {
            throw new AttributoNonValorizzatoException(
                    "CourseBean: attributo obiettiviFormativi non valorizzato!");
        } else {
            return this.obiettiviFormativi;
        }
    }

    /**
     * Setter for property obiettiviFormativi.
     * 
     * @param obiettiviFormativi New value of property obiettiviFormativi.
     */
    public void setObiettiviFormativi(String obiettiviFormativi) {

        this.obiettiviFormativi = obiettiviFormativi;
    }

    /**
     * Getter for property LinguaObiettiviFormativi.
     * 
     * @return Value of property LinguaObiettiviFormativi.
     */
    public String getLinguaObiettiviFormativi() throws AttributoNonValorizzatoException {
        if (linguaObiettiviFormativi == null) {
            throw new AttributoNonValorizzatoException(
                    "CourseBean: attributo LinguaobiettiviFormativi non valorizzato!");
        } else {
            return this.linguaObiettiviFormativi;
        }
    }

    /**
     * Setter for property LinguaobiettiviFormativi.
     * 
     * @param obiettiviFormativi New value of property obiettiviFormativi.
     */
    public void setLinguaObiettiviFormativi(String linguaObiettiviFormativi) {

        this.linguaObiettiviFormativi = linguaObiettiviFormativi;
    }

    
    /* **************************************************** *
     *  Programma insegnamento e lingua in cui e' espresso  *
     * **************************************************** */
    /**
     * Getter for property programma
     * 
     * @return Value of property programma.
     */
    public String getProgramma() throws AttributoNonValorizzatoException {
        if (programma == null) {
            throw new AttributoNonValorizzatoException(
                    "CourseBean: attributo programma non valorizzato!");
        } else {
            return this.programma;
        }
    }

    /**
     * Setter for property programma.
     * 
     * @param programma New value of property programma.
     */
    public void setProgramma(String programma) {

        this.programma = programma;
    }

    /**
     * Getter for property LinguaProgramma.
     * 
     * @return Value of property LinguaProgramma.
     */
    public String getLinguaProgramma() throws AttributoNonValorizzatoException {
        if (linguaProgramma == null) {
            throw new AttributoNonValorizzatoException(
                    "CourseBean: attributo LinguaProgramma non valorizzato!");
        } else {
            return this.linguaProgramma;
        }
    }

    /**
     * Setter for property LinguaProgramma.
     * 
     * @param linguaProgramma New value of property linguaProgramma.
     */
    public void setLinguaProgramma(String linguaProgramma) {

        this.linguaProgramma = linguaProgramma;
    }

    
    /* **************************************************** *
     *    Modalita' di esame e lingua in cui sono espresse  *
     * **************************************************** */
    /**
     * Getter for property modalitaEsame.
     * 
     * @return Value of property modalitaEsame.
     */
    public String getModalitaEsame() throws AttributoNonValorizzatoException {
        if (modalitaEsame == null) {
            throw new AttributoNonValorizzatoException(
                    "CourseBean: attributo modalità esame non valorizzato!");
        } else {

            return this.modalitaEsame;
        }
    }

    /**
     * Setter for property modalitaEsame.
     * 
     * @param modalitaEsame New value of property modalitaEsame.
     */
    public void setModalitaEsame(String modalitaEsame) {

        this.modalitaEsame = modalitaEsame;
    }

    /**
     * Getter for property linguaModalitaEsame.
     * 
     * @return Value of property modalitaEsame.
     */
    public String getLinguaModalitaEsame() throws AttributoNonValorizzatoException {
        if (linguaModalitaEsame == null) {
            throw new AttributoNonValorizzatoException(
                    "CourseBean: attributo lingua modalità esame non valorizzato!");
        } else {

            return this.linguaModalitaEsame;
        }
    }

    /**
     * Setter for property modalitaEsame.
     * 
     * @param modalitaEsame New value of property modalitaEsame.
     */
    public void setLinguaModalitaEsame(String linguaModalitaEsame) {

        this.linguaModalitaEsame = linguaModalitaEsame;
    }

    
    /* **************************************************** *
     *             Sede didattica e sua Provincia           *
     * **************************************************** */
    /**
     * Getter for property Sede.
     * 
     * @return Value of property Sede.
     */
    public String getSede() throws AttributoNonValorizzatoException {
        if (sede == null) {
            throw new AttributoNonValorizzatoException("CourseBean: attributo Sede non valorizzato!");
        } else {
            return this.sede;
        }
    }

    /**
     * Setter for property Sede.
     * 
     * @param linguaProgramma New value of property Sede.
     */
    public void setSede(String sede) {
        this.sede = sede;
    }

    /**
     * Getter for property ProvinciaSede.
     * 
     * @return Value of property ProvinciaSede.
     */
    public String getProvinciaSede() throws AttributoNonValorizzatoException {
        if (provinciaSede == null) {
            throw new AttributoNonValorizzatoException(
                    "CourseBean: attributo ProvinciaSede non valorizzato!");
        } else {
            return this.provinciaSede;
        }
    }

    /**
     * Setter for property ProvinciaSede.
     * 
     * @param linguaProgramma New value of property ProvinciaSede.
     */
    public void setProvinciaSede(String provincia) {
        this.provinciaSede = provincia;
    }

    
    /* **************************************************** *
     *     Flag indicante se l'AD e' comune ai curricula    *
     * **************************************************** */
    /**
     * Metodo che ritorna se l'occorrenza insegnamento è comune agli indirizzi
     */
    public boolean isComuneIndirizzi() {
        return comuneIndirizzi;
    }

    /**
     * Metodo per settare se l'occorrenza insegnamento è comune agli indirizzi
     */
    public void setComuneIndirizzi(boolean b) {
        comuneIndirizzi = b;
    }

    
    /* **************************************************** *
     *             Lingua in cui l'AD e' erogata            *
     * **************************************************** */
        /**
     * @return linguaErog
     */
    public String getLinguaErog() {
        return this.linguaErog;
    }

    /**
     * @param linguaErog
     */
    public void setLinguaErog(String l) {
        this.linguaErog = l;
    }


    /**
     * @return the codiceCdSWI
     */
    public String getCodiceCdSWI() {
        return codiceCdSWI;
    }


    /**
     * @param codiceCdSWI the codiceCdSWI to set
     */
    public void setCodiceCdSWI(String codiceCdSWI) {
        this.codiceCdSWI = codiceCdSWI;
    }


    /**
     * @return the codiceCdSUGOV
     */
    public String getCodiceCdSUGOV() {
        return codiceCdSUGOV;
    }


    /**
     * @param codiceCdSUGOV the codiceCdSUGOV to set
     */
    public void setCodiceCdSUGOV(String codiceCdSUGOV) {
        this.codiceCdSUGOV = codiceCdSUGOV;
    }


    /**
     * @return the nomeCdSWI
     */
    public String getNomeCdSWI() {
        return nomeCdSWI;
    }


    /**
     * @param nomeCdSWI the nomeCdSWI to set
     */
    public void setNomeCdSWI(String nomeCdSWI) {
        this.nomeCdSWI = nomeCdSWI;
    }


    /**
     * @return the nomeCdSUGOV
     */
    public String getNomeCdSUGOV() {
        return nomeCdSUGOV;
    }


    /**
     * @param nomeCdSUGOV the nomeCdSUGOV to set
     */
    public void setNomeCdSUGOV(String nomeCdSUGOV) {
        this.nomeCdSUGOV = nomeCdSUGOV;
    }


    /**
     * @return the dipartimentoScuola
     */
    public String getDipartimentoScuola() {
        return dipartimentoScuola;
    }


    /**
     * @param dipartimentoScuola the dipartimentoScuola to set
     */
    public void setDipartimentoScuola(String dipartimentoScuola) {
        dipartimentoScuola = dipartimentoScuola;
    }


    /**
     * @return the sedePrimariaCDS
     */
    public String getSedePrimariaCDS() {
        return sedePrimariaCDS;
    }


    /**
     * @param sedePrimariaCDS the sedePrimariaCDS to set
     */
    public void setSedePrimariaCDS(String sedePrimariaCDS) {
        sedePrimariaCDS = sedePrimariaCDS;
    }


    /**
     * @return the codiceADWI
     */
    public int getCodiceADWI() {
        return codiceADWI;
    }


    /**
     * @param codiceADWI the codiceADWI to set
     */
    public void setCodiceADWI(int codiceADWI) {
        this.codiceADWI = codiceADWI;
    }


    /**
     * @return the codiceADUGOV
     */
    public String getCodiceADUGOV() {
        return codiceADUGOV;
    }


    /**
     * @param codiceADUGOV the codiceADUGOV to set
     */
    public void setCodiceADUGOV(String codiceADUGOV) {
        this.codiceADUGOV = codiceADUGOV;
    }


    /**
     * @return the sSD
     */
    public String getSSD() {
        return SSD;
    }


    /**
     * @param sSD the sSD to set
     */
    public void setSSD(String sSD) {
        SSD = sSD;
    }


    /**
     * @return the docente
     */
    public PersonBean getDocente() {
        return docente;
    }


    /**
     * @param docente the docente to set
     */
    public void setDocente(PersonBean docente) {
        this.docente = docente;
    }


    /**
     * @return the ul
     */
    public boolean isUl() {
        return ul;
    }


    /**
     * @param ul the ul to set
     */
    public void setUl(boolean ul) {
        this.ul = ul;
    }


    /**
     * @return the creditiTotali
     */
    public float getCreditiTotali() {
        return creditiTotali;
    }


    /**
     * @param creditiTotali the creditiTotali to set
     */
    public void setCreditiTotali(float creditiTotali) {
        this.creditiTotali = creditiTotali;
    }


    /**
     * @return the creditiLezione
     */
    public float getCreditiLezione() {
        return creditiLezione;
    }


    /**
     * @param creditiLezione the creditiLezione to set
     */
    public void setCreditiLezione(float creditiLezione) {
        this.creditiLezione = creditiLezione;
    }


    /**
     * @return the creditiLaboratorio
     */
    public float getCreditiLaboratorio() {
        return creditiLaboratorio;
    }


    /**
     * @param creditiLaboratorio the creditiLaboratorio to set
     */
    public void setCreditiLaboratorio(float creditiLaboratorio) {
        this.creditiLaboratorio = creditiLaboratorio;
    }


    /**
     * @return the creditiAltro
     */
    public float getCreditiAltro() {
        return creditiAltro;
    }


    /**
     * @param creditiAltro the creditiAltro to set
     */
    public void setCreditiAltro(float creditiAltro) {
        this.creditiAltro = creditiAltro;
    }


    /**
     * @return the ore
     */
    public String getOre() {
        return ore;
    }


    /**
     * @param ore the ore to set
     */
    public void setOre(String ore) {
        this.ore = ore;
    }


    /**
     * @return the oreLezione
     */
    public String getOreLezione() {
        return oreLezione;
    }


    /**
     * @param oreLezione the oreLezione to set
     */
    public void setOreLezione(String oreLezione) {
        this.oreLezione = oreLezione;
    }


    /**
     * @return the oreLaboratorio
     */
    public String getOreLaboratorio() {
        return oreLaboratorio;
    }


    /**
     * @param oreLaboratorio the oreLaboratorio to set
     */
    public void setOreLaboratorio(String oreLaboratorio) {
        this.oreLaboratorio = oreLaboratorio;
    }


    /**
     * @return the oreAltro
     */
    public String getOreAltro() {
        return oreAltro;
    }


    /**
     * @param oreAltro the oreAltro to set
     */
    public void setOreAltro(String oreAltro) {
        this.oreAltro = oreAltro;
    }


    /**
     * @return the oreSeminario
     */
    public String getOreSeminario() {
        return oreSeminario;
    }


    /**
     * @param oreSeminario the oreSeminario to set
     */
    public void setOreSeminario(String oreSeminario) {
        this.oreSeminario = oreSeminario;
    }


    /**
     * @return the oreEsercitazione
     */
    public String getOreEsercitazione() {
        return oreEsercitazione;
    }


    /**
     * @param oreEsercitazione the oreEsercitazione to set
     */
    public void setOreEsercitazione(String oreEsercitazione) {
        this.oreEsercitazione = oreEsercitazione;
    }


    /**
     * @return the oreTirocinio
     */
    public String getOreTirocinio() {
        return oreTirocinio;
    }


    /**
     * @param oreTirocinio the oreTirocinio to set
     */
    public void setOreTirocinio(String oreTirocinio) {
        this.oreTirocinio = oreTirocinio;
    }


    /**
     * @return the inizioPerDid
     */
    public String getInizioPerDid() {
        return inizioPerDid;
    }


    /**
     * @param inizioPerDid the inizioPerDid to set
     */
    public void setInizioPerDid(String inizioPerDid) {
        this.inizioPerDid = inizioPerDid;
    }


    /**
     * @return the finePerDid
     */
    public String getFinePerDid() {
        return finePerDid;
    }


    /**
     * @param finePerDid the finePerDid to set
     */
    public void setFinePerDid(String finePerDid) {
        this.finePerDid = finePerDid;
    }

    /* Attributi nominali del docente.
     * In realtà esiste un bean di tipo PersonBean, e un Vector<PersonBean> in caso
     * di codocenze, proprio per rappresentare il / i docenti dell'AD, perché 
     * è più pulito rappresentare l'informazione in quel modo.
     * Tuttavia, siccome le tuple devono essere elaborate da un algoritmo 
     * più che presentate in una webapplication, sono stati aggiunti anche
     * i campi nominali de-normalizzati, e dunque queste due tipologie di
     * attributi (String e PersonBean) in questo caso coesistono.
     * (And don't be a pine in my ass, please, I don't give a shit!...).
     */
    /**
     * @return the cognomeDocente
     */
    public String getCognomeDocente() {
        return cognomeDocente;
    }


    /**
     * @param cognomeDocente the cognomeDocente to set
     */
    public void setCognomeDocente(String cognomeDocente) {
        this.cognomeDocente = cognomeDocente;
    }


    /**
     * @return the nomeDocente
     */
    public String getNomeDocente() {
        return nomeDocente;
    }


    /**
     * @param nomeDocente the nomeDocente to set
     */
    public void setNomeDocente(String nomeDocente) {
        this.nomeDocente = nomeDocente;
    }


    /**
     * @return the codiceFiscaleDocente
     */
    public String getCodiceFiscaleDocente() {
        return codiceFiscaleDocente;
    }


    /**
     * @param codiceFiscaleDocente the codiceFiscaleDocente to set
     */
    public void setCodiceFiscaleDocente(String codiceFiscaleDocente) {
        this.codiceFiscaleDocente = codiceFiscaleDocente;
    }


    /**
     * @return the coordinatore
     */
    public int getCoordinatore() {
        return coordinatore;
    }


    /**
     * @param coordinatore the coordinatore to set
     */
    public void setCoordinatore(int coordinatore) {
        this.coordinatore = coordinatore;
    }


}
