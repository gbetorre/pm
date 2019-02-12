/*
 *   Alma on Line: Applicazione WEB per la visualizzazione 
 *   delle schede di indagine su popolazione dell'ateneo,
 *   della gestione dei progetti on line (POL) 
 *   e della preparazione e del monitoraggio delle informazioni riguardanti 
 *   l'offerta formativa che hanno ricadute sulla valutazione della didattica 
 *   (questionari on line - QOL).
 *   
 *   Copyright (C) 2018 Giovanroberto Torre<br />
 *   Alma on Line (aol), Projects on Line (pol), Questionnaire on Line (qol);
 *   web applications to publish, and manage, students evaluation,
 *   projects, students and degrees information.
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

package it.alma.qol.command;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import it.alma.qol.DBWrapper;
import it.alma.qol.Query;
import it.alma.bean.CourseBean;
import it.alma.bean.ItemBean;
import it.alma.command.Command;
import it.alma.exception.AttributoNonValorizzatoException;
import it.alma.exception.CommandException;
import it.alma.exception.NotFoundException;
import it.alma.exception.WebStorageException;


/** 
 * <p><code>ValuationCommand.java</code><br />
 * Implementa l'algoritmo di scelta delle AD da valutare.</p>
 * 
 * <p>Classe per l'implementazione delle regole di scelta delle AD
 * da mettere in valutazione ai fini dei questionari della didattica.
 * <br /> 
 * Consente di applicare su una lista di Attivit&agrave; Didattiche 
 * le regole stabilite dal presidio della qualit&agrave;.<br /></p>
 * <p>Questa classe possiede i metodi che le consentono di capire, 
 * partendo da una lista di "AD grezze", ovvero da un "semilavorato":
 * <ul>
 * <li>quali occorrenze sono realmente doppie;</li> 
 * <li>quali sono apparentemente doppie 
 * (cio&egrave; sono uguali in tutti i dati fondamentali
 * tranne che nel periodo).</li>
 * </ul><p>
 * <p>Facciamo un esempio, molto semplificato: se avessimo il seguente insieme 
 * {3, 4a, 4a, 4b, 4b, 7, 7} come lista delle "AD semilavorate", dovremmo ottenere
 * come risultato "pulito" ("lavorato") il seguente:
 * {3, 4ab, 7}.
 * Questo risultato, secondo la logica implementata nei metodi della presente
 * classe, si otterrebbe nel seguente modo:
 * <pre>
 * {3, 4a, 4a, 4b, 4b, 7, 7} -> {3}, {4a, 4a, 4b, 4b, 7, 7};
 *    {4a, 4a, 4b, 4b, 7, 7} -> {4a, 4b, 7}
 *      {4a, 4b, 7} -> {4ab, 7}
 * {3}, {4ab, 7} -> {3, 4ab, 7}.</pre>
 * Una volta individuati i record "puliti" - risultato che si ottiene depurando
 * la lista "semilavorata" di tutti i doppi in blocco, raffinando tali doppi 
 * (prendendo solo una delle due &ndash; o pi&uacute; &ndash; occorrenze 
 * di un doppione, e unificando i periodi), e riunendo la lista di tali 
 * "occorrenze doppie lavorate" alla lista con i doppi tagliati - 
 * la classe &egrave; in grado di selezionare da questo elenco lavorato 
 * soltanto le AD che devono essere sottoposte a valutazione, 
 * attraverso l'applicazione, sulla lista "raffinata", 
 * di un algoritmo di scelta che implenenta i criteri stabiliti dal presidio.</p>
 * 
 * <p>Created on martedì 4 settembre 2018 10:28:08</p>
 * 
 * @author <a href="mailto:giovanroberto.torre@univr.it">Giovanroberto Torre</a>
 */
public class ValuationCommand extends ItemBean implements Command {

    /**
     * <p>Classe annidata destinata ad essere utilizzata come criterio
     * per ordinare, in base ad un codice costruito internamente alla
     * classe stessa &ndash; comunque esso sia definito &ndash; 
     * due o pi&uacute; oggetti di tipo {@link CourseBean}.</p> 
     * 
     * 
     * @author <a href="mailto:giovanroberto.torre@univr.it">trrgnr59</a>
     * @see ValuationCommand
     */
    protected class Sortbycode implements Comparator<CourseBean>
    { 
        // Used for sorting in ascending order of course key 
        @Override
        public int compare(CourseBean o1, CourseBean o2) {
            String std = null;
            if (o1 == null || o2 == null)
                return 1;
            try {
                std = o1.getCodiceFiscaleDocente() + 
                      o1.getCodiceADUGOV() + 
                      o1.getCodiceCdSUGOV() + 
                      o1.getInizioPerDid() + 
                      o1.getFinePerDid();
            } catch (NullPointerException npe) {
                // Auto-generated method stub
                return 0;
            }
            try {
                return std.compareTo(o2.getCodiceFiscaleDocente() + o2.getCodiceADUGOV() + o2.getCodiceCdSUGOV() + o2.getInizioPerDid() + o2.getFinePerDid());
            } catch (NullPointerException npe) {
                return 1;
            }
        } 
    } 
    
    
    /**
     * <p>Classe annidata destinata ad essere utilizzata come criterio
     * per ordinare, in base alla chiave &ndash; comunque essa sia definita
     * all'interno della classe {@link CourseBean} &ndash; 
     * due o pi&uacute; oggetti di tale tipo.</p> 
     *  
     * 
     * @author <a href="mailto:giovanroberto.torre@univr.it">trrgnr59</a>
     * @see ValuationCommand
     */
    protected class Sortbykey implements Comparator<CourseBean> 
    { 
        // Used for sorting in ascending order of course key 
        @Override
        public int compare(CourseBean o1, CourseBean o2) {
            String std = null;
            if (o1 == null || o2 == null)
                return 1;
            try {
                std = o1.getKey();
            } catch (AttributoNonValorizzatoException anve) {
                // Auto-generated method stub
                return 0;
            }
            try {
                return std.compareTo(o2.getKey());
            } catch (AttributoNonValorizzatoException anve) {
                return 1;
            }
        } 
    }
    
    
    /**
     * <p>Classe annidata destinata ad essere utilizzata come criterio
     * per ordinare, in base ad un codice costruito internamente alla
     * classe stessa &ndash; comunque esso sia definito &ndash; 
     * due o pi&uacute; oggetti di tipo {@link CourseBean}.</p> 
     * 
     * 
     * @author <a href="mailto:giovanroberto.torre@univr.it">trrgnr59</a>
     * @see ValuationCommand
     */
    protected class Sortbyid implements Comparator<CourseBean>
    { 
        // Used for sorting in ascending order of course key 
        @Override
        public int compare(CourseBean o1, CourseBean o2) {
            Integer std = null;
            if (o1 == null || o2 == null)
                return 1;
            try {
                std = new Integer(o1.getId());
            } catch (AttributoNonValorizzatoException anve) {
                // Auto-generated method stub
                return 0;
            } catch (NullPointerException npe) {
                // Auto-generated method stub
                return 0;
            }
            try {
                return std.compareTo(o2.getId());
            } catch (AttributoNonValorizzatoException anve) {
                return 1;
            } catch (NullPointerException npe) {
                return 1;
            }
        } 
    } 
    
    
    /**
     *  Nome di questa classe 
     *  (utilizzato per contestualizzare i messaggi di errore)
     */
    static final String FOR_NAME = "\n" + Logger.getLogger(new Throwable().getStackTrace()[0].getClassName()) + ": ";
    /**
     * Costante autoesplicativa per incapsulare 
     * il valore boolean <code>false</code>
     */
    private static final boolean READ_DUPLICATES = false;
    /**
     * Costante autoesplicativa per incapsulare 
     * il valore boolean <code>true</code>
     */
    private static final boolean WRITE_DUPLICATES = true;
    /**
     * Pagina a cui la servlet reindirizza
     */
    private static final String nomeFileElenco = "/jsp/elenco.jsp";  
    /**
     * Lista Hash contenente tutti i codici dei corsi elettivi
     */
    private LinkedList<String> corsiElettivi;
    
    
    /** 
     * Crea una nuova istanza di ValuationCommand 
     */
    public ValuationCommand() {
        ;   // It Doesn't Anything
    }
  
    
    /** 
     * <p>Raccoglie i valori dell'oggetto ItemBean
     * e li passa a questa classe command.</p>
	 * 
	 * @param voceMenu la VoceMenuBean pari alla Command presente.
	 * @throws it.alma.exception.CommandException se l'attributo paginaJsp di questa command non e' stato valorizzato.
     */
    public void init(ItemBean voceMenu) throws CommandException {
        this.setId(voceMenu.getId());
        this.setNome(voceMenu.getNome());
        this.setLabelWeb(voceMenu.getLabelWeb());
        this.setNomeClasse(voceMenu.getNomeClasse());
        this.setPaginaJsp(voceMenu.getPaginaJsp());
        this.setInformativa(voceMenu.getInformativa());
        if (this.getPaginaJsp() == null) {
          String msg = FOR_NAME + "La voce menu' " + this.getNome() + " non ha il campo paginaJsp. Impossibile visualizzare i risultati.\n";
          throw new CommandException(msg);
        }
        // Trasforma l'array dei corsi elettivi in una LinkedList per comodità di utilizzo
        corsiElettivi = new LinkedList<String>();
        for (int i = 0; i < Query.CODICI_CORSI_ELETTIVI.length; i++) {
            // Codice corrente corso elettivo
            String codiceElettivo = Query.CODICI_CORSI_ELETTIVI[i];
            corsiElettivi.add(codiceElettivo);
        }
    }  
  
    
    /**
     * <p>Gestisce il flusso principale.</p>
     * <p>Prepara i bean.</p>
     * <p>Passa nella Request i valori che verranno utilizzati dall'applicazione.</p>
     * <p>Il funzionamento dell'algoritmo deve essere il seguente:
     * <tt><ol>
     * <li>Crea 2 liste separate: lista delle AD di area medica/motorie 
     * e lista del resto delle AD (con CFU >= 4)</li>
     * <li>Su entrambe le liste applica gli stessi procedimenti, e cioè:</li>
     *   <ol>
     *      <li>a. ordina la lista per id</li>
     *      <li>b. individua le occorrenze uguali (doppi, tripli, quadrupli...)</li> 
     *      <li>c. decide quale riga tenere delle righe uguali</li>
     *      <li>d. elimina le altre e tiene solo quella</li>
     *      <li>e. controlla delle righe rimaste quali sono uguali in tutto tranne che nel periodo</li>
     *      <li>f. unisce i periodi</li>
     *      <li>g. elimina le due righe originali e tiene solo la nuova con i periodi uniti</li>
     *      <li>h. fa una lista a parte delle AD con i docenti ancora da definire</li>
     *   </ol>
     * </ol></tt></p>
     * 
     * @param req 
     * @throws CommandException 
     */
    public void execute(HttpServletRequest req) 
                 throws CommandException {
        // Databound
        DBWrapper db = null;
        // Struttura che conterrà tutte le AD Semplici dal semilavorato
        Vector<CourseBean> v = new Vector<CourseBean>();
        // Struttura che conterrà tutte le AD Semplici dal semilavorato
        Vector<CourseBean> vM = new Vector<CourseBean>();
        // Struttura che conterrà tutte le AD Semplici dal semilavorato
        Vector<CourseBean> vUL = new Vector<CourseBean>();
        // Struttura che conterrà tutte le AD Semplici dal semilavorato
        Vector<CourseBean> vSM = new Vector<CourseBean>();
        // Struttura che conterrà tutte le AD Semplici dal semilavorato
        Vector<CourseBean> vMM = new Vector<CourseBean>();
        // Struttura che conterrà tutte le AD Semplici dal semilavorato
        Vector<CourseBean> vULM = new Vector<CourseBean>();
        // Instanzia nuova classe WebStorage per il recupero dei dati
        try {
            db = new DBWrapper();
        } catch (WebStorageException wse) {
            throw new CommandException(FOR_NAME + "Non e\' disponibile un collegamento al database\n." + wse.getMessage(), wse);
        }
        /* ******************************************************************** *
         *                     1  -  A D    S E M P L I C I                     *
         * ******************************************************************** */
        // Recupera Attività Didattiche Semplici
        try {
            v = db.getAD(Query.AD_SEMPLICI);
        } catch (NotFoundException nfe) {
            throw new CommandException(FOR_NAME + ": Impossibile recuperare un attributo obbligatorio.\n" + nfe.getMessage(), nfe);
        } catch (WebStorageException wse) {
            StackTraceElement[] stackTrace = wse.getStackTrace();
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i<stackTrace.length; i++) {
                StackTraceElement ste = stackTrace[i];
                sb.append(ste.getClassName());
                sb.append(".");
                sb.append(ste.getMethodName());
                sb.append(" (");
                sb.append(ste.getClassName());
                sb.append(":");
                sb.append(ste.getLineNumber());
                sb.append(")");
                sb.append("\n\t");
            }
            throw new CommandException(FOR_NAME + "Si e\' verificato un problema nel recuperare i dati relativi all'elenco delle AD.\n" + wse.getMessage() + "\n stack: " + sb, wse);
        } catch (NullPointerException npe) {
            StackTraceElement[] stackTrace = npe.getStackTrace();
            throw new CommandException(FOR_NAME + "Si e\' verificato un puntamento a null in " + this.getClass().getName() + ": " + npe.getMessage() + "\n stack: " + stackTrace[0], npe);            
        } catch (Exception e) {
            StackTraceElement[] stackTrace = e.getStackTrace();
            throw new CommandException(FOR_NAME + "Eccezione generica da " + this.getClass().getName() + ": " + e.getMessage() + "\n stack: " + stackTrace[0], e);
        }
        /* ******************************************************************* *
         *  Individua le Attività Didattiche Semplici da porre in valutazione  *
         * ******************************************************************* *
         *       1a. - LAVORA I CORSI DI AREA MEDICA  (parte AD Semplici)      *
         * ******************************************************************* */
        // Estrapola i corsi di area medica (es. 48)
        ArrayList<CourseBean> sMedical = getMedicalCourses(v);
        // Li ordina per id
        sortById(sMedical);
        // Estrapola i corsi di area medica che sono presenti in occorrenze multiple (doppie)
        ArrayList<CourseBean> sMedicalDuplicates = handleDuplicates(sMedical, READ_DUPLICATES);
        // Ordina i duplicati trovati
        sortById(sMedicalDuplicates);
        // Rimuove (secondo un criterio logico non causale) una copia dei doppi, tenendo l'altra
        ArrayList<CourseBean> sMedicalCleaned = handleDuplicates(sMedical, WRITE_DUPLICATES);
        // Ordina le AD di area medica per codice fiscale docente
        sortByCode(sMedicalCleaned);
        /* Cerca di unire i periodi dove ci sono occorrenze doppie */
        // Ottiene la lista delle AD mediche uguali in tutto tranne che nel periodo (p. es. 6)
        List<CourseBean> sMedicalDifferentByPeriod = getCoupletByPeriod(sMedicalCleaned);
        // Riduce le righe unendo tutti i periodi, ed ottenendo così una lista di AD distinte
        List<CourseBean> sMedicalMerged = mergeDates(new ArrayList<CourseBean>(sMedicalDifferentByPeriod));
        // Fa una copia delle AD mediche con i doppioni già ripuliti
        ArrayList<CourseBean> sMedicalToEvaluate = new ArrayList<CourseBean>(sMedicalCleaned);
        // Toglie dalla lista originale delle AD mediche tutte le AD con i periodi da unire
        sMedicalToEvaluate.removeAll(sMedicalDifferentByPeriod);
        // Ci aggiunge i singoli con i periodi uniti 
        sMedicalToEvaluate.addAll(sMedicalMerged);
        /* ******************************************************************* *
         *      1b. - LAVORA I CORSI DI AREA NON MEDICA (parte AD Semplici)    *
         * ******************************************************************* */
        // Estrapola i corsi di area NON medica
        ArrayList<CourseBean> sNotMedical = discardMedicalCourses(v);
        // Estrapola i corsi di area NON medica papabili (CFU > 3)
        ArrayList<CourseBean> sNotMedicalAndHeavy = getGreatherOrEquals(sNotMedical, 4);
        // Li ordina per id
        sortById(sNotMedicalAndHeavy);
        // Estrapola i corsi di area NON medica che sono presenti in occorrenze multiple (doppie)
        ArrayList<CourseBean> sDuplicates = handleDuplicates(sNotMedicalAndHeavy, READ_DUPLICATES);
        // Ordina i duplicati trovati
        sortById(sDuplicates);
        // Rimuove (secondo un criterio logico non causale) una copia dei doppi, tenendo l'altra
        ArrayList<CourseBean> sCleaned = handleDuplicates(sNotMedicalAndHeavy, WRITE_DUPLICATES);
        // Ordina le AD di area NON medica per codice fiscale docente
        sortByCode(sCleaned);
        /* Cerca di unire i periodi dove ci sono occorrenze doppie */
        // Ottiene la lista delle AD NON mediche uguali in tutto tranne che nel periodo (p. es. 6)
        List<CourseBean> sDifferentByPeriod = getCoupletByPeriod(sCleaned);
        // Riduce le righe unendo tutti i periodi, ed ottenendo così una lista di AD distinte
        List<CourseBean> sMerged = mergeDates(new ArrayList<CourseBean>(sDifferentByPeriod));
        // Fa una copia delle AD mediche con i doppioni già ripuliti
        ArrayList<CourseBean> sToEvaluate = new ArrayList<CourseBean>(sCleaned);
        // Toglie dalla lista originale delle AD mediche tutte le AD con i periodi da unire
        sToEvaluate.removeAll(sDifferentByPeriod);
        // Ci aggiunge i singoli con i periodi uniti 
        sToEvaluate.addAll(sMerged);
        /* ******************************************************************** *
         *                          2  -  M O D U L I                           *
         * ******************************************************************** */
        // Recupera Attività Didattiche Moduli
        try {
            vM = db.getAD(Query.AD_MODULI);
        } catch (NotFoundException nfe) {
            throw new CommandException(FOR_NAME + ": Impossibile recuperare un attributo obbligatorio.\n" + nfe.getMessage(), nfe);
        } catch (WebStorageException wse) {
            StackTraceElement[] stackTrace = wse.getStackTrace();
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i<stackTrace.length; i++) {
                StackTraceElement ste = stackTrace[i];
                sb.append(ste.getClassName());
                sb.append(".");
                sb.append(ste.getMethodName());
                sb.append(" (");
                sb.append(ste.getClassName());
                sb.append(":");
                sb.append(ste.getLineNumber());
                sb.append(")");
                sb.append("\n\t");
            }
            throw new CommandException(FOR_NAME + "Si e\' verificato un problema nel recuperare i dati relativi all'elenco delle AD.\n" + wse.getMessage() + "\n stack: " + sb, wse);
        } catch (NullPointerException npe) {
            StackTraceElement[] stackTrace = npe.getStackTrace();
            throw new CommandException(FOR_NAME + "Si e\' verificato un puntamento a null in " + this.getClass().getName() + ": " + npe.getMessage() + "\n stack: " + stackTrace[0], npe);            
        } catch (Exception e) {
            StackTraceElement[] stackTrace = e.getStackTrace();
            throw new CommandException(FOR_NAME + "Eccezione generica da " + this.getClass().getName() + ": " + e.getMessage() + "\n stack: " + stackTrace[0], e);
        }
        /* ******************************************************************* *
         *   Individua le Attività Didattiche MODULI da porre in valutazione   *
         * ******************************************************************* *
         *          1a. - LAVORA I CORSI DI AREA MEDICA (parte MODULI)         *
         * ******************************************************************* */
        // Estrapola i corsi di area medica (es. 48)
        ArrayList<CourseBean> mMedical = getMedicalCourses(vM);
        // Li ordina per id
        sortById(mMedical);
        // Estrapola i corsi di area medica che sono presenti in occorrenze multiple (doppie)
        ArrayList<CourseBean> mMedicalDuplicates = handleDuplicates(mMedical, READ_DUPLICATES);
        // Ordina i duplicati trovati
        sortById(mMedicalDuplicates);
        // Rimuove (secondo un criterio logico non causale) una copia dei doppi, tenendo l'altra
        ArrayList<CourseBean> mMedicalCleaned = handleDuplicates(mMedical, WRITE_DUPLICATES);
        // Ordina le AD di area medica per codice fiscale docente
        sortByCode(mMedicalCleaned);
        /* Cerca di unire i periodi dove ci sono occorrenze doppie */
        // Ottiene la lista delle AD mediche uguali in tutto tranne che nel periodo (p. es. 6)
        List<CourseBean> mMedicalDifferentByPeriod = getCoupletByPeriod(mMedicalCleaned);
        // Riduce le righe unendo tutti i periodi, ed ottenendo così una lista di AD distinte
        List<CourseBean> mMedicalMerged = mergeDates(new ArrayList<CourseBean>(mMedicalDifferentByPeriod));
        // Fa una copia delle AD mediche con i doppioni già ripuliti
        ArrayList<CourseBean> mMedicalToEvaluate = new ArrayList<CourseBean>(mMedicalCleaned);
        // Toglie dalla lista originale delle AD mediche tutte le AD con i periodi da unire
        mMedicalToEvaluate.removeAll(mMedicalDifferentByPeriod);
        // Ci aggiunge i singoli con i periodi uniti 
        mMedicalToEvaluate.addAll(mMedicalMerged);
        /* ******************************************************************* *
         *         1b. - LAVORA I CORSI DI AREA NON MEDICA (parte MODULI)      *
         * ******************************************************************* */
        // Estrapola i corsi di area NON medica
        ArrayList<CourseBean> mNotMedical = discardMedicalCourses(v);
        // Estrapola i corsi di area NON medica papabili (CFU > 3)
        ArrayList<CourseBean> mNotMedicalAndHeavy = getGreatherOrEquals(mNotMedical, 4);
        // Li ordina per id
        sortById(mNotMedicalAndHeavy);
        // Estrapola i corsi di area NON medica che sono presenti in occorrenze multiple (doppie)
        ArrayList<CourseBean> mDuplicates = handleDuplicates(mNotMedicalAndHeavy, READ_DUPLICATES);
        // Ordina i duplicati trovati
        sortById(mDuplicates);
        // Rimuove (secondo un criterio logico non causale) una copia dei doppi, tenendo l'altra
        ArrayList<CourseBean> mCleaned = handleDuplicates(mNotMedicalAndHeavy, WRITE_DUPLICATES);
        // Ordina le AD di area NON medica per codice fiscale docente
        sortByCode(mCleaned);
        /* Cerca di unire i periodi dove ci sono occorrenze doppie */
        // Ottiene la lista delle AD NON mediche uguali in tutto tranne che nel periodo (p. es. 6)
        List<CourseBean> mDifferentByPeriod = getCoupletByPeriod(mCleaned);
        // Riduce le righe unendo tutti i periodi, ed ottenendo così una lista di AD distinte
        List<CourseBean> mMerged = mergeDates(new ArrayList<CourseBean>(mDifferentByPeriod));
        // Fa una copia delle AD mediche con i doppioni già ripuliti
        ArrayList<CourseBean> mToEvaluate = new ArrayList<CourseBean>(mCleaned);
        // Toglie dalla lista originale delle AD mediche tutte le AD con i periodi da unire
        mToEvaluate.removeAll(mDifferentByPeriod);
        // Ci aggiunge i singoli con i periodi uniti 
        mToEvaluate.addAll(mMerged);
        /* ******************************************************************** *
         *              Passaggio in Request di valori e attributi              *
         * ******************************************************************** */
        // Imposta il testo del Titolo da visualizzare prima dell'elenco
        req.setAttribute("titoloE", "AD Semplici");
        // Salva nella request: Titolo (da mostrare nell'HTML)
        req.setAttribute("tP", req.getAttribute("titoloE"));
        // Imposta la Pagina JSP di forwarding
        req.setAttribute("fileJsp", nomeFileElenco);
         
        // Salva nella request: elenco AD Semplici
        //req.setAttribute("elenco", notMedical);
        //req.setAttribute("senzaDuplicati", medicalAD);
        //req.setAttribute("lista", sda);
        //req.setAttribute("elenco", medicalAD);
        //req.setAttribute("doppioni", medicalCleaned);
        
        req.setAttribute("adFinal", mToEvaluate);
        req.setAttribute("adDaUnire", mDifferentByPeriod);
        req.setAttribute("ripuliti", mCleaned);
        req.setAttribute("duplicati", mDuplicates);
        req.setAttribute("candidati", mNotMedicalAndHeavy);
        
        req.setAttribute("adMYFinal", mMedicalToEvaluate);
        req.setAttribute("adMYDaUnire", mMedicalDifferentByPeriod);
        req.setAttribute("ripulitiMY", mMedicalCleaned);
        req.setAttribute("duplicatiMY", mMedicalDuplicates);
        req.setAttribute("adMY", mMedical);

        req.setAttribute("elenco", v);
    }
    
    
    /**
     * <p>Ordina un {@link ArrayList} secondo un criterio definito dall'utente.<br />
     * Nel caso specifico, il metodo invoca la classe annidata 
     * {@link Sortbycode} che tenta di ordinare 
     * gli elementi dell'ArrayList passato come argomento al presente metodo
     * in base a un codice definito all'interno della stessa classe annidata.</p>
     * <p>Il metodo agisce per riferimento (<code>ByRef</code>) 
     * piuttosto che per valore (<code>ByVal</code>), quindi cambia l'ordinamento
     * dell'oggetto passato come argomento; se pertanto non si desidera 
     * modificare l'ordine originale dell'oggetto, &egrave; necessario 
     * prima clonarlo e poi passare il clone come parametro.</p>
     * 
     * @param l ArrayList da ordinare secondo il criterio specificato nella classe di cui il presente metodo passera' un'instanza
     * @throws CommandException se si verifica un problema generalmente di puntamento a null
     */
    public void sortByCode(List<CourseBean> l) 
                    throws CommandException {
        Collections.sort(l, new Sortbycode());
        // return null;
    }
    
    
    /**
     * <p>Ordina un {@link ArrayList} secondo un criterio definito dall'utente.<br />
     * Nel caso specifico, il metodo invoca la classe annidata 
     * {@link Sortbykey} che tenta di ordinare 
     * gli elementi dell'ArrayList passato come argomento al presente metodo
     * in base alla chiave dell'Attivit&agrave; Didattica, 
     * come definito all'interno della stessa classe annidata.</p>
     * <p>Il metodo agisce per riferimento (<code>ByRef</code>) 
     * piuttosto che per valore (<code>ByVal</code>), quindi cambia l'ordinamento
     * dell'oggetto passato come argomento; se pertanto non si desidera 
     * modificare l'ordine originale dell'oggetto, &egrave; necessario 
     * prima clonarlo e poi passare il clone come parametro.</p>
     * 
     * @param l ArrayList da ordinare secondo il criterio specificato nella classe di cui il presente metodo passera' un'instanza
     * @throws CommandException se si verifica un problema generalmente di puntamento a null
     */
    public void sortByKey(List<CourseBean> l) 
                   throws CommandException {
        Collections.sort(l, new Sortbykey());
        // return null;
    }
    
    
    /**
     * <p>Ordina un {@link ArrayList} secondo un criterio definito dall'utente.<br />
     * Nel caso specifico, il metodo invoca la classe annidata 
     * {@link Sortbyid} che tenta di ordinare 
     * gli elementi dell'ArrayList passato come argomento al presente metodo
     * in base all'identificativo dell'Attivit&agrave; Didattica, 
     * come definito all'interno della stessa classe annidata.</p>
     * <p>Il metodo agisce per riferimento (<code>ByRef</code>) 
     * piuttosto che per valore (<code>ByVal</code>), quindi cambia l'ordinamento
     * dell'oggetto passato come argomento; se pertanto non si desidera 
     * modificare l'ordine originale dell'oggetto, &egrave; necessario 
     * prima clonarlo e poi passare il clone come parametro.</p>
     * 
     * @param l ArrayList da ordinare secondo il criterio specificato nella classe di cui il presente metodo passera' un'instanza
     * @throws CommandException se si verifica un problema generalmente di puntamento a null
     */
    public void sortById(List<CourseBean> l) 
            throws CommandException {
        Collections.sort(l, new Sortbyid());
        // return null;
    }
    
    
    /**
     * <p>Estrae dalla lista grezza delle AD semplici (semilavorato) 
     * tutte le AD di area medica da sottoporre a valutazione.</p>
     * <p>Le AD in questione:
     * <ul>
     * <li>devono essere appartenenti a un corso di medicina</li>
     * <li>devono avere &nbsp;<strong><code>crediti di lezione &gt; 0</code></strong></li>
     * <li><strong>non</strong> devono rientrare nella lista dei corsi elettivi</li>
     * </ul>
     * </p>
     * 
     * @param v e' la lista grezza delle AD in cui cercare le AD di area medica al fine di restituirle
     * @return <code>ArrayList&lt;CourseBean&gt;</code> - la lista delle AD di area medica da sottoporre a valutazione, salvo duplicazioni
     * @throws CommandException se si verifica un problema nello scorrimento di liste o in qualche altro tipo di puntamento
     */
    private  ArrayList<CourseBean> getMedicalCourses(Vector<CourseBean> v)
                                              throws CommandException {
        // Dichiara la lista delle AD di area medica
        ArrayList<CourseBean> medicalCourses = new ArrayList<CourseBean>();
        try {
            for (int i = 0; i < v.size(); i++) {
                CourseBean current = v.elementAt(i);
                // Se il codice corso studio di U-GOV inizia per 'M' sicuramente è un'AD di area medica
                // Se il codice corso studio di U-GOV inizia per 'Y' sicuramente è un'AD di scienze motorie
                if (current.getCodiceCdSUGOV().trim().startsWith("M") || current.getCodiceCdSUGOV().trim().startsWith("Y")) {
                    // Ma, nel caso di medicina, va aggiunto solo se ha crediti lezione > 0
                    if (current.getCreditiLezione() > 0) {
                        // E inoltre se è un corso elettivo non lo vogliamo
                        if (!corsiElettivi.contains(current.getCodiceADUGOV().trim())) {
                            medicalCourses.add(current);
                        }
                    }
                }
            }
        } catch (IndexOutOfBoundsException iobe) {
            String msg = FOR_NAME + "Si e\' verificato un problema nello scorrimento delle lista delle AD.\n" + iobe.getMessage();
            throw new CommandException(msg, iobe);
        } catch (ClassCastException cce) {
            String msg = FOR_NAME + "Si e\' verificato un problema in qualche conversione di tipo.\n" + cce.getMessage();
            throw new CommandException(msg, cce);
        } catch (NullPointerException npe) {
            String msg = FOR_NAME + "Si e\' verificato un problema di puntamento a null.\n" + npe.getMessage();
            throw new CommandException(msg, npe);
        } catch (Exception e) {
            String msg = FOR_NAME + "Si e\' verificato un problema.\n" + e.getMessage();
            throw new CommandException(msg, e);
        }        
        return medicalCourses;
    }
    
    
    /**
     * <p>Partendo dalla lista grezza delle AD semplici (semilavorato) 
     * restituisce tutte le AD NON di medicina.</p>
     * 
     * @param v e' la lista grezza delle AD in cui cercare le AD di area medica al fine di scartarle
     * @return <code>ArrayList&lt;CourseBean&gt;</code> - la lista delle AD semilavorate, depurate di quelle di area medica
     * @throws CommandException se si verifica un problema nello scorrimento di liste o in qualche altro tipo di puntamento
     */
    private static ArrayList<CourseBean> discardMedicalCourses(Vector<CourseBean> v)
                                                        throws CommandException {
        // Trasforma il Vector di AD semilavorate in un ArrayList
        ArrayList<CourseBean> vAsArrayList = new ArrayList<CourseBean>(v);
        // Dichiara la lista delle AD NON di area medica
        ArrayList<CourseBean> allButMedicalCourses = new ArrayList<CourseBean>();
        try {
            for (int i = 0; i < vAsArrayList.size(); i++) {
                CourseBean current = vAsArrayList.get(i);
                // Se il corso di studi dell'AD corrente NON inizia per M l'AD NON è di medicina
                if (!current.getCodiceCdSUGOV().trim().startsWith("M") && !current.getCodiceCdSUGOV().trim().startsWith("Y")) {   
                    allButMedicalCourses.add(current);
                }
            }
        } catch (IndexOutOfBoundsException iobe) {
            String msg = FOR_NAME + "Si e\' verificato un problema nello scorrimento di liste.\n" + iobe.getMessage();
            throw new CommandException(msg, iobe);
        } catch (ClassCastException cce) {
            String msg = FOR_NAME + "Si e\' verificato un problema in qualche conversione di tipo.\n" + cce.getMessage();
            throw new CommandException(msg, cce);
        } catch (NullPointerException npe) {
            String msg = FOR_NAME + "Si e\' verificato un problema di puntamento a null.\n" + npe.getMessage();
            throw new CommandException(msg, npe);
        } catch (Exception e) {
            String msg = FOR_NAME + "Si e\' verificato un problema.\n" + e.getMessage();
            throw new CommandException(msg, e);
        }        
        return allButMedicalCourses;
    }
    
    
    /**
     * <p>Partendo da una lista di AD (p.es. dalla lista delle 
     * AD semplici non di medicina) restituisce tutte le AD 
     * con un numero di crediti totali strettamente maggiori 
     * di quelli di un intero passato come argomento.</p>
     * 
     * @param l lista di AD in cui cercare le AD in base al criterio che abbiano un peso strettamente maggiore di <code>credits</code>
     * @param credits criterio di pesatura in base a cui tenere o scartare ciascuna AD passata al vaglio
     * @return <code>ArrayList&lt;CourseBean&gt;</code> - lista delle AD rispondenti al criterio
     * @throws CommandException se si verifica un problema nello scorrimento di liste o in qualche altro tipo di puntamento
     */
    private static ArrayList<CourseBean> getGreatherThan(ArrayList<CourseBean> l, 
                                                         int credits)
                                                  throws CommandException {
        ArrayList<CourseBean> greatherThan = new ArrayList<CourseBean>();
        try {
            for (int i = 0; i < l.size(); i++) {
                CourseBean current = l.get(i);
                // Se i crediti totali sono strettamente maggiori di credits...
                if (current.getCreditiTotali() > credits) {
                    // ...conserva l'AD
                    greatherThan.add(current);
                }
            }
        } catch (IndexOutOfBoundsException iobe) {
            String msg = FOR_NAME + "Si e\' verificato un problema nello scorrimento della lista delle AD grezze o di quelle duplicate.\n" + iobe.getMessage();
            throw new CommandException(msg, iobe);
        } catch (ClassCastException cce) {
            String msg = FOR_NAME + "Si e\' verificato un problema in qualche conversione di tipo.\n" + cce.getMessage();
            throw new CommandException(msg, cce);
        } catch (NullPointerException npe) {
            String msg = FOR_NAME + "Si e\' verificato un problema di puntamento a null.\n" + npe.getMessage();
            throw new CommandException(msg, npe);
        } catch (Exception e) {
            String msg = FOR_NAME + "Si e\' verificato un problema.\n" + e.getMessage();
            throw new CommandException(msg, e);
        }        
        return greatherThan;
    }   
    
    
    /**
     * <p>Partendo da una lista di AD (p.es. dalla lista delle 
     * AD semplici non di medicina) restituisce tutte le AD 
     * con un numero di crediti totali maggiori o uguali 
     * di quelli di un intero passato come argomento.</p>
     * 
     * @param l lista di AD in cui cercare le AD in base al criterio che abbiano un peso maggiore o uguale di <code>credits</code>
     * @param credits criterio di pesatura in base a cui tenere o scartare ciascuna AD passata al vaglio
     * @return <code>ArrayList&lt;CourseBean&gt;</code> - lista delle AD rispondenti al criterio
     * @throws CommandException se si verifica un problema nello scorrimento di liste o in qualche altro tipo di puntamento
     */
    private static ArrayList<CourseBean> getGreatherOrEquals(ArrayList<CourseBean> l, 
                                                             int credits)
                                                      throws CommandException {
        ArrayList<CourseBean> greatherThan = new ArrayList<CourseBean>();
        try {
            for (int i = 0; i < l.size(); i++) {
                CourseBean current = l.get(i);
                // Se i crediti totali sono maggiori di credits...
                if (current.getCreditiTotali() >= credits) {
                    // ...conserva l'AD
                    greatherThan.add(current);
                }
            }
        } catch (IndexOutOfBoundsException iobe) {
            String msg = FOR_NAME + "Si e\' verificato un problema nello scorrimento della lista delle AD grezze o di quelle duplicate.\n" + iobe.getMessage();
            throw new CommandException(msg, iobe);
        } catch (ClassCastException cce) {
            String msg = FOR_NAME + "Si e\' verificato un problema in qualche conversione di tipo.\n" + cce.getMessage();
            throw new CommandException(msg, cce);
        } catch (NullPointerException npe) {
            String msg = FOR_NAME + "Si e\' verificato un problema di puntamento a null.\n" + npe.getMessage();
            throw new CommandException(msg, npe);
        } catch (Exception e) {
            String msg = FOR_NAME + "Si e\' verificato un problema.\n" + e.getMessage();
            throw new CommandException(msg, e);
        }        
        return greatherThan;
    }
    
    
    /**
     * <p>Cerca in una lista, passata come argomento, tutti i duplicati, 
     * logici e fisici; a seconda del valore di un flag, passato come argomento, 
     * restituisce:
     * <dl>
     * <dt>(se il flag &egrave; falso)</dt>
     * <dd>la lista dei duplicati trovati</dd>
     * oppure
     * <dt>(se il flag &egrave; vero)</dt>
     * <dd>una copia della lista originale, depurata dei duplicati</dd>
     * </dl></p> 
     * 
     * @param l una lista di AD, presumibilmente contenente duplicati
     * @param removeThemAll flag specificante se i duplicati vanno restituiti oppure epurati
     * @return <code>ArrayList&lt;CourseBean&gt;</code> - lista delle AD duplicate o depurate, a seconda del valore del criterio
     * @throws CommandException se si verifica un problema nello scorrimento di liste o in qualche altro tipo di puntamento
     */
    private static ArrayList<CourseBean> handleDuplicates(ArrayList<CourseBean> l, 
                                                          boolean removeThemAll)
                                                   throws CommandException {
        // Copia l'ArrayList passato come rgomento in un ArrayList variabile locale
        // per non alterare, per riferimento, il valore del parametro passato dal chiamante
        ArrayList<CourseBean> m = new ArrayList<CourseBean>(l);
        // Dichiara la lista dei duplicati
        ArrayList<CourseBean> duplicates = new ArrayList<CourseBean>();
        // Deve ciclare la lista passata, e confrontare, riga per riga,
        // se la riga corrente è uguale - da un punto di vista logico - 
        // alla riga successiva (cioè se ne condivide la chiave)
        try {
            // Cicla due volte l'ArrayList delle AD per individuare AD con la stessa chiave
            for (int i = 0; i < m.size(); i++) {
                for (int j = 0; j < m.size(); j++) {
                    // Se i e j sono uguali sta puntando allo stesso elemento, ed è evidente che un elemento è uguale a se stesso...
                    if (i != j) {   
                        CourseBean courseRaw = m.get(i);
                        CourseBean courseOrd = m.get(j);
                        // Diamo per scontato (ragionevolmente!) che non ci siano tuple con lo stesso id
                        if (courseRaw.getKey().equals(courseOrd.getKey()) && (courseRaw.getId() != courseOrd.getId())) {
                            if (removeThemAll) {
                                // Cerca (per scartarlo) tra i 2 record uguali quello con il coordinatore = 1
                                if (courseRaw.getCoordinatore() > courseOrd.getCoordinatore()) {
                                    m.remove(courseRaw);
                                }
                                else if (courseRaw.getCoordinatore() < courseOrd.getCoordinatore()) {
                                    m.remove(courseOrd);
                                }
                                // I flag coordinatore sono uguali!
                                else {
                                    // Nel caso in cui avessero entrambi lo stesso valore, tiene quello con il numero di crediti maggiore
                                    if (courseRaw.getCrediti() > courseOrd.getCrediti()) {
                                        m.remove(courseRaw);
                                    }
                                    else {
                                        m.remove(courseOrd);
                                    }
                                }
                            }
                            else {
                                // Aggiunge ai duplicati solo se il suo id non è già presente dentro
                                boolean itIsInHere = false;
                                for (CourseBean ad : duplicates) {
                                    if (ad.getId() == courseOrd.getId()) {
                                        itIsInHere = true;
                                        break;
                                    }
                                }
                                if (!itIsInHere) {
                                    duplicates.add(courseOrd);
                                }   
                            }
                        }
                    }
                }
            }
        } catch (AttributoNonValorizzatoException anve) {
            String msg = FOR_NAME + "Si e\' verificato un problema nel recupero di un attributo del bean.\n" + anve.getMessage();
            throw new CommandException(msg, anve);            
        } catch (IndexOutOfBoundsException iobe) {
            String msg = FOR_NAME + "Si e\' verificato un probleea nello scorrimento della lista delle AD grezze o di quelle duplicate.\n" + iobe.getMessage();
            throw new CommandException(msg, iobe);
        } catch (ClassCastException cce) {
            String msg = FOR_NAME + "Si e\' verificato un problema in qualche conversione di tipo.\n" + cce.getMessage();
            throw new CommandException(msg, cce);
        } catch (NullPointerException npe) {
            String msg = FOR_NAME + "Si e\' verificato un problema di puntamento a null.\n" + npe.getMessage();
            throw new CommandException(msg, npe);
        } catch (Exception e) {
            String msg = FOR_NAME + "Si e\' verificato un problema.\n" + e.getMessage();
            throw new CommandException(msg, e);
        }
        if (removeThemAll) {
            return m;
        }
        return duplicates;
    }
    
    
    /**
     * <p>Prende in input un lista di AD che, nell'estrazione grezza, presentano
     * uno o pi&uacute; duplicati logici tra cui vi sono alcune AD aventi
     * la stessa chiave-base ma diverso periodo.<br />
     * Seleziona queste ultime, ovvero quelle con:
     * <strong> stesso CdS, Codice U-GOV, Docente </strong> 
     * ma periodo diverso (da unire successivamente) quindi in sostanza
     * l doppioni che hanno persistito nella lista dei "doppioni puliti", 
     * ottenendo cos&iacute; una lista di AD accoppiate per chiave-base
     * ma distinte per periodo, che dovranno poi essere ridotte a elementi
     * distinti con i periodi uniti.</p>
     * 
     * @param samplesDuplicates ArrayList di AD campione che sono tutte distinte, tranne alcune che hanno stessa chiave-base ma periodo diverso 
     * @return <code>List&lt;CourseBean&gt;</code> - ArrayList di AD che sono tutte uguali nella chiave-base ma diverse nel periodo  
     * @throws CommandException se si verifica un problema di puntatore fuori tabella o in qualche altro tipo di puntamento
     */
    private static List<CourseBean> getCoupletByPeriod(List<CourseBean> samplesDuplicates)
                                                throws CommandException {
        // Calcola la lunghezza della lista di campioni di duplicati logici
        int vSize = samplesDuplicates.size();
        // Dichiara la lista "grezza" di elementi che hanno la stessa chiave-base ma diverso periodo di erogazione
        List<CourseBean> baseDuplicates = new ArrayList<CourseBean>();
        // Cicla
        try {
            // Contatori
            int i = 0;
            // Cicla tutti i duplicati logici
            while (i < vSize) {
                // Punta al record corrente
                CourseBean current = samplesDuplicates.get(i);
                // Per definizione di questa List le chiavi complete non possono essere uguali, quindi recupera la chiave-base
                String smallKey = current.getSmallKey();
                // Ciclo forEach (Java 1.5+)
                for (CourseBean ad : samplesDuplicates) {
                    // Controlla che le occorrenze abbiano la stessa chiave-base ma diverso id 
                    // (AD con la stessa chiave-base e lo stesso id sono la stessa AD!)
                    if (ad.getSmallKey().equals(smallKey) && ad.getId() != current.getId()) {
                     // Nel momento in cui le chiavi-base sono uguali (e gli id diversi), tiene il record perché gli interessa
                        baseDuplicates.add(ad);
                    }
                }
                i++;
            }
        } catch (AttributoNonValorizzatoException anve) {
            throw new CommandException(FOR_NAME + "Si e\' verificato un problema nel recupero di un attributo.\n" + anve.getMessage(), anve);
        } catch (ArrayIndexOutOfBoundsException aiobe) {
            throw new CommandException(FOR_NAME + "Si e\' verificato un puntamento fuori da una lista.\n" + aiobe.getMessage(), aiobe);
        } catch (NullPointerException npe) {
            throw new CommandException(FOR_NAME + "Si e\' verificato un puntamento a un oggetto non esistente.\n" + npe.getMessage(), npe);
        } catch (Exception e) {
            throw new CommandException(FOR_NAME + "Si e\' verificato un problema" + e.getMessage(), e);
        }
        return baseDuplicates;
    }

    
    /**
     * <p>Prende in input un lista di AD aventi
     * la stessa chiave-base ma diverso periodo.<br />
     * Tenta di unire i periodi dove possibile, usando il criterio di
     * tenere le date pi&uacute; distanti possibile, quindi di calcolare
     * l'unione degli intervalli,
     * ottenendo cos&iacute; una lista di AD pulite 
     * e tutte distinte anche per chiave-base, che dovr&agrave;
     * essere poi unita (in gergo informatichese: joinata, mergiata...)
     * alla lista delle AD da cui sono stati tolti i duplicati, 
     * insieme alle basi dei duplicati veri (AD che presentano duplicati) 
     * per ottenere infine l'elenco pulito delle AD.</p>
     * <p>Questo metodo confronta la smallkey;
     * se le smallkey sono uguali, cerca la data minore, la data maggiore, 
     * e genera un unico periodo che è la fusione dei due;
     * setta il nuovo periodo in un bean avente la stessa smallkey 
     * ma ovviamente a questo punto differente key.</p>
     * TODO: gestire il caso in cui ci sono 3 o più righe con periodi uniti, non solo 2
     * 
     * @param duplicateButPeriod ArrayList contenente un elenco di AD uguali nella chiave base ma differenti solo per il periodo
     * @return <code>List&lt;CourseBean&gt;</code> - ArrayList di AD che sono tutte diverse nella chiave base e nel periodo
     * @throws CommandException se si verifica un problema nell'accesso ad un attributo o in qualche altro tipo di puntamento
     */
    private static List<CourseBean> mergeDates(List<CourseBean> duplicateButPeriod)
                                        throws CommandException {
        // Calcola la lunghezza della lista di campioni di duplicati logici
        int vSize = duplicateButPeriod.size();
        // Dichiara la lista "grezza" di elementi che hanno la stessa chiave-base ma diverso periodo di erogazione
        List<CourseBean> merged = new ArrayList<CourseBean>();
        // Cicla
        try {
            // Contatori
            int i = 0;
            int j = 0;
            // Cicla tutti i duplicati logici
            while (i < vSize) {
                // Indice del successivo
                j = i + 1;
                // Punta al record corrente
                CourseBean current = duplicateButPeriod.get(i);
                if (j < vSize) {
                    // Punta al record successivo
                    CourseBean next = duplicateButPeriod.get(j);
                    // Per definizione di questa List le chiavi complete non possono essere uguali, quindi recupera la chiave-base del corrente
                    String smallKey = current.getSmallKey();
                    // Controlla che l'occorrenza corrente e quella successiva abbiano la stessa chiave-base ma diverso id 
                    // (AD con la stessa chiave-base e lo stesso id sono la stessa AD!)
                    if ((smallKey.equals(next.getSmallKey())) && (current.getId() != next.getId())) {
                        // Se siamo qui vuol dire che le due occorrenze sono uguali nella chiave-base ma non nel periodo
                        // Recupera tutte le date, che dal bean arrivano sotto forma di String
                        String startFirstDate  = current.getInizioPerDid();
                        String endFirstDate    = current.getFinePerDid();
                        String startSecondDate = next.getInizioPerDid();
                        String endSecondDate   = next.getFinePerDid();
                        // Converte le Sting in oggetti Date
                        Date startFirstDateAsDate  = new SimpleDateFormat("yyyy-MM-dd").parse(startFirstDate);
                        Date endFirstDateAsDate    = new SimpleDateFormat("yyyy-MM-dd").parse(endFirstDate);
                        Date startSecondDateAsDate = new SimpleDateFormat("yyyy-MM-dd").parse(startSecondDate);
                        Date endSecondDateAsDate   = new SimpleDateFormat("yyyy-MM-dd").parse(endSecondDate);
                        // Carica le date in una struttura
                        ArrayList<Date> date = new ArrayList<>();
                        date.add(startFirstDateAsDate);
                        date.add(endFirstDateAsDate);
                        date.add(startSecondDateAsDate);
                        date.add(endSecondDateAsDate);
                        // Ordina le date dalla prima all'ultima
                        Collections.sort(date);
                        // Prende la prima e l'ultima (gli estremi dell'intervallo)
                        java.sql.Date firstStart = new java.sql.Date(date.get(0).getTime());
                        java.sql.Date lastEnd = new java.sql.Date(date.get(3).getTime());
                        // Costruisce un nuovo oggetto, uguale a uno dei due considerati
                        CourseBean mergedCourse = new CourseBean(current); 
                        // Setta le date trovate come estremi dell'intervallo nell'oggetto
                        mergedCourse.setInizioPerDid(firstStart.toString());
                        mergedCourse.setFinePerDid(lastEnd.toString());
                        // Aggiunge l'oggetto a una struttura da restituire come la lista delle AD con le date unite
                        merged.add(mergedCourse);
                    }
                }
                i++;
            }
        } catch (AttributoNonValorizzatoException anve) {
            throw new CommandException(FOR_NAME + "Si e\' verificato un problema nel recupero di un attributo.\n" + anve.getMessage(), anve);
        } catch (ArrayIndexOutOfBoundsException aiobe) {
            throw new CommandException(FOR_NAME + "Si e\' verificato un puntamento fuori da una lista.\n" + aiobe.getMessage(), aiobe);
        } catch (NullPointerException npe) {
            throw new CommandException(FOR_NAME + "Si e\' verificato un puntamento a un oggetto non esistente.\n" + npe.getMessage(), npe);
        } catch (Exception e) {
            throw new CommandException(FOR_NAME + "Si e\' verificato un problema" + e.getMessage(), e);
        }
        return merged;
    }
    
    
    /* ************************************************************************ *
     *                    Codice Inutile, Obsoleto o Deprecato                  *
     *                              TODO: eliminare                             *
     * ************************************************************************ */    
    
    /**
     * Estrae dalla lista grezza delle AD semplici (semilavorato) 
     * tutti i doppi indistintamente
     * 
     * 
     * @param v e' la lista grezza delle AD in cui cercare i doppi
     * @return
     * @throws CommandException
     */
    private static ArrayList<CourseBean> getDuplicates(Vector<CourseBean> v)
                                 throws CommandException {
        // Trasforma il Vector di AD semilavorate in un ArrayList
        ArrayList<CourseBean> vAsArrayList = new ArrayList<CourseBean>(v);
        // Dichiara la lista dei duplicati
        ArrayList<CourseBean> duplicates = new ArrayList<CourseBean>();
        // Deve ciclare la lista del semilavorato, e confrontare riga per riga
        // se la riga corrente è uguale - da un punto di vista logico - 
        // alla riga successiva (cioè se ne condivide la chiave)
        try {
            // Cicla due volte l'ArrayList delle AD per individuare AD con la stessa chiave
            for (int i = 0; i < vAsArrayList.size(); i++) {
                for (int j = 0; j < vAsArrayList.size(); j++) {
                    // Se i e j sono uguali sta puntando allo stesso elemento, ed è evidente che un elemento è uguale a se stesso...
                    if (i != j) {   
                        CourseBean courseRaw = vAsArrayList.get(i);
                        CourseBean courseOrd = vAsArrayList.get(j);
                        if (courseRaw.getKey().equals(courseOrd.getKey()) && (courseRaw.getId() != courseOrd.getId())) {
                            duplicates.add(courseOrd);
                        }
                    }
                }
            }
        } catch (IndexOutOfBoundsException iobe) {
            String msg = FOR_NAME + "Si e\' verificato un problema nello scorrimento della lista delle AD grezze o di quelle duplicate.\n" + iobe.getMessage();
            throw new CommandException(msg, iobe);
        } catch (ClassCastException cce) {
            String msg = FOR_NAME + "Si e\' verificato un problema in qualche conversione di tipo.\n" + cce.getMessage();
            throw new CommandException(msg, cce);
        } catch (NullPointerException npe) {
            String msg = FOR_NAME + "Si e\' verificato un problema di puntamento a null.\n" + npe.getMessage();
            throw new CommandException(msg, npe);
        } catch (Exception e) {
            String msg = FOR_NAME + "Si e\' verificato un problema.\n" + e.getMessage();
            throw new CommandException(msg, e);
        }        
        return duplicates;
    }
    
       
    /**
     * Toglie dalla lista grezza delle AD semplici (semilavorato) 
     * tutti i doppi indistintamente
     * 
     * 
     * @param v e' la lista grezza delle AD da ripulire
     * @return
     * @throws CommandException
     * @deprecated perche' questo metodo e' stato migliorato e perfezionato nel contesto della riscrittura dell'algoritmo di scelta
     */
    private static ArrayList<CourseBean> clean(Vector<CourseBean> v)
                                        throws CommandException {
        // Trasforma il Vector di AD semilavorate in un ArrayList
        ArrayList<CourseBean> vAsArrayList = new ArrayList<CourseBean>(v);
        // Deve ciclare la lista del semilavorato, e confrontare riga per riga
        // se la riga corrente è uguale - da un punto di vista logico - 
        // alla riga successiva (cioè se ne condivide la chiave)
        try {
            // Cicla due volte l'ArrayList delle AD per individuare AD con la stessa chiave
            for (int i = 0; i < vAsArrayList.size(); i++) {
                for (int j = 0; j < vAsArrayList.size(); j++) {
                    // Se i e j sono uguali sta puntando allo stesso elemento, ed è evidente che un elemento è uguale a se stesso...
                    if (i != j) {   
                        CourseBean courseRaw = vAsArrayList.get(i);
                        CourseBean courseOrd = vAsArrayList.get(j);
                        if (courseRaw.getKey().equals(courseOrd.getKey()) && (courseRaw.getId() != courseOrd.getId())) {
                            vAsArrayList.remove(j);
                        }
                    }
                }
            }
        } catch (IndexOutOfBoundsException iobe) {
            String msg = FOR_NAME + "Si e\' verificato un problema nello scorrimento della lista delle AD grezze o di quelle duplicate.\n" + iobe.getMessage();
            throw new CommandException(msg, iobe);
        } catch (ClassCastException cce) {
            String msg = FOR_NAME + "Si e\' verificato un problema in qualche conversione di tipo.\n" + cce.getMessage();
            throw new CommandException(msg, cce);
        } catch (NullPointerException npe) {
            String msg = FOR_NAME + "Si e\' verificato un problema di puntamento a null.\n" + npe.getMessage();
            throw new CommandException(msg, npe);
        } catch (Exception e) {
            String msg = FOR_NAME + "Si e\' verificato un problema.\n" + e.getMessage();
            throw new CommandException(msg, e);
        }        
        return vAsArrayList;
    }
    
    
    /**
     * <p>Ottiene i duplicati logici (righe con la stessa chiave e id diversi).</p>
     * <p>Restituisce una lista di AD che sono state individuate come doppie
     * in base alla chiave specificata nell'oggetto AD.</p>
     * <p>I doppioni si definiscono come <code>"AD aventi la stessa chiave"</code>
     * e non quindi come "tuple identiche".<br />
     * Si tratta quindi di duplicati logici, non di duplicati identici.<br /> 
     * Il presente metodo effettua un controllo per eliminare eventuali duplicati
     * identici e restituisce soltanto i duplicati logici, in tutte le occorrenze
     * (ad esempio, se lo stesso criterio si applicasse al seguente insieme:
     * <pre>{4, 4, 8, 15, 16, 23, 23, 42}</pre> il presente metodo restituirebbe 
     * il sottoinsieme <code>{4, 4, 23, 23}</code>, e non il sottoinsieme 
     * costituito dai campioni degli elementi sottoposti a duplicazione, ovvero 
     * <code>{4, 23}</code>.</p>
     * 
     * @param v un Vector di AD da setacciare alla ricerca di duplicati
     * @return <code>List&lt;CourseBean&gt;</code> - ArrayList di duplicati trovati  
     * @throws CommandException se si verifica un problema, generalmente nell'accesso a qualche valore o un puntamento fuori tabella
     */
    private List<CourseBean> getLogicalDuplicates(Vector<CourseBean> v)
                                           throws CommandException {
        List<CourseBean> genericDuplicates = null;
        List<CourseBean> logicalDuplicates = null;
        try {
            int vSize = v.size();
            genericDuplicates = new ArrayList<CourseBean>();
            // Cicla due volte il Vector delle AD per individuare AD con la stessa chiave
            for (int i = 0; i < vSize; i++) {
                for (int j = 0; j < vSize; j++) {
                    if (i != j) {   // Se i e j sono uguali sta puntando allo stesso elemento, ed è evidente che un elemento è uguale a se stesso...
                        CourseBean courseRaw = v.elementAt(i);
                        CourseBean courseOrd = v.elementAt(j);
                        if (courseRaw.getKey().equals(courseOrd.getKey()) && (courseRaw.getId() != courseOrd.getId())) {
                            genericDuplicates.add(courseOrd);
                        }
                    }
                }
            }
            // Ordina la lista ottenuta in base al criterio specificato nel metodo override compareTo del Bean
            Collections.sort(genericDuplicates);
            // Clona la lista dei duplicati
            logicalDuplicates = new ArrayList<CourseBean>(genericDuplicates);
            // Cicla la lista dei duplicati
            for (int i = 0; i < genericDuplicates.size(); i++) {
                // Costruisce l'indice del record successivo a quello recuperato
                int j = i + 1;
                // Controllo per evitare puntamento fuori tabella
                if (j < genericDuplicates.size()) {
                    // I doppioni veri hanno anche lo stesso id
                    if (genericDuplicates.get(i).getId() == genericDuplicates.get(j).getId()) {
                        // Elimina il doppione vero (vero = identico in tutto, anche nell'id)
                        logicalDuplicates.remove(j);
                    }
                }
            }
            return logicalDuplicates;
        } catch (ArrayIndexOutOfBoundsException aiobe) {
            StackTraceElement[] stackTrace = aiobe.getStackTrace();
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i<stackTrace.length; i++) {
                StackTraceElement ste = stackTrace[i];
                sb.append(ste.getClassName());
                sb.append(".");
                sb.append(ste.getMethodName());
                sb.append(" (");
                sb.append(ste.getClassName());
                sb.append(":");
                sb.append(ste.getLineNumber());
                sb.append(")");
                sb.append("\n\t");
            }
            throw new CommandException(FOR_NAME + "Puntamento fuori tabella!\n" + aiobe.getMessage() + "\n stack: " + sb, aiobe);
        } catch (NullPointerException npe) {
            StackTraceElement[] stackTrace = npe.getStackTrace();
            throw new CommandException(FOR_NAME + "Si e\' verificato un puntamento a null in " + this.getClass().getName() + ": " + npe.getMessage() + "\n stack: " + stackTrace[0], npe);            
        } catch (Exception e) {
            StackTraceElement[] stackTrace = e.getStackTrace();
            throw new CommandException(FOR_NAME + "Eccezione generica da " + this.getClass().getName() + ": " + e.getMessage() + "\n stack: " + stackTrace[0], e);
        }
    }
    
    
    /**
     * <p>Partendo da un elenco di tuple che presentano duplicazioni logiche
     * (quindi che non sono identiche in tutto (anche nell'id) ma logicamente
     * identiche &ndash; cio&egrave; uguali nella stessa chiave), passato
     * come argomento, toglie da questo elenco, 
     * in maniera relativamente arbitraria, i "doppioni doppi",
     * cio&egrave; una delle due tuple logicamente identiche, tenendo l'altra:
     * in altre parole, tiene soltanto una copia 
     * delle chiavi logiche duplicate.</p>
     * <p>Ottiene la lista dei "doppi singoli" ("doppioni puliti") 
     * ovvero di una singola copia delle righe doppie, non di entrambe
     * (e men che meno pi&uacute;, come avviene nel caso di alcuni laboratori 
     * o AD particolari come "Inglese Scientifico" che non hanno un solo
     * doppione ma quattro o cinque).</p>
     * <p>In altri termini:<br />
     * prende in input la lista dei duplicati logici, passata come
     * argomento, e suddivide tale lista delle AD duplicate approssimativamente  
     * in due, individuando le chiavi duplicate - cio&egrave; in pratica 
     * chiavi che hanno due o più occorrenze nella lista delle AD.</p>
     * <p>Restituisce una lista di elementi che hanno un valore doppio.</p>
     * <p>I duplicati, per definizione, vanno a coppie (almeno), per cui, volendo
     * eliminarli dall'elenco delle tuple "grezze", non vanno eliminati a coppie, 
     * ma solo singolarmente.<br />
     * P.es., se avessi il seguente insieme &nbsp;<code>{4, 4, 8, 15, 16}</code>
     * volendo eliminare i duplicati dovrei eliminare <strong>un solo</strong> 4, 
     * non tutti e due; ma, nel vettore dei duplicati, attualmente ci sono 
     * tutti i duplicati (tranne i duplicati identici, naturalmente, cio&egrave;
     * tuple identiche in tutto e per tutto, anche nell'id), quindi tutti i 
     * duplicati logici - ovvero le tuple aventi la stessa chiave ma diverso id -
     * e non solo l'elenco distinto degli elementi soggetti a duplicazione.
     * Per questo motivo, il presente metodo seleziona, secondo un criterio
     * ragionato, solo la met&agrave;, o poco meno, dei duplicati 
     * in modo da poterli togliere dall'elenco completo.</p> 
     * 
     * @param duplicates lista dei duplicati logici
     * @return <code>List&lt;CourseBean&gt;</code> - ArrayList di AD campione che hanno almeno un duplicato  
     * @throws CommandException se si verifica un problema in un puntamento fuori tabella o nell'accesso a qualche elemento
     * @deprecated perche' il criterio di eliminazione dei doppioni e' troppo arbitrario
     */
    private static List<CourseBean> splitDuplicates(List<CourseBean> duplicates)
                                             throws CommandException {
        // Calcola la lunghezza della lista di duplicati logici
        int vSize = duplicates.size();
        // Dichiara la lista "pulita" di elementi che hanno almeno un duplicato logico
        List<CourseBean> samplesDuplicates = new ArrayList<CourseBean>();
        try {
            // Contatori
            int i = 0;
            int j = 0;
            // Il primo va aggiunto a mano, altrimenti, dato l'algoritmo, non entrerebbe mai nei duplicati puliti...
            samplesDuplicates.add(duplicates.get(i));
            // Cicla tutti i duplicati logici
            while (i < vSize) {
                // Indice del successivo
                j = i + 1;
                // Punta al record corrente
                CourseBean current = duplicates.get(i);
                // Recupera la chiave
                String key = current.getKey();
                // Controllo per evitare puntamento fuori tabella
                if (j < vSize) {
                    // Punta al record successivo e confronta la chiave col record successivo
                    // Finché la chiave non cambia...
                    while (j < vSize && key.equals(duplicates.get(j).getKey())) {
                        // ...incrementa l'indice
                        if (j < vSize) {
                            j++;
                        }
                    }
                    // Controllo per evitare puntamento fuori tabella
                    if (j < vSize) {
                        // Nel momento in cui la chiave cambia, tiene il record
                        String candidateKey = duplicates.get(j).getKey();
                        // Flag specificante se il record corrente è già stato salvato nella lista delle AD che hanno duplicazioni
                        boolean alreadyYet = false;
                        // A meno che non sia già presente nel Vector (could be...)
                        for (int k = 0; k < samplesDuplicates.size(); k++) {
                            CourseBean controllo = samplesDuplicates.get(k);
                            // Recupera la chiave
                            String controlKey = controllo.getKey();
                            // Se le due chiavi sono uguali... 
                            if (controlKey.equals(candidateKey)) {
                                // ...vuol dire che il record era già stato salvato nella lista delle AD campione che hanno duplicazioni 
                                alreadyYet = true;
                                // Esce dal ciclo perché non c'è bisogno di sapere altro
                                break;
                            }
                        }
                        // Se non è già presente nei record salvati...
                        if (!alreadyYet) {
                            // ...lo aggiunge alla lista delle AD campione che hanno duplicazioni
                            samplesDuplicates.add(duplicates.get(j));
                        }
                    }
                } 
                i++;
            }
            return samplesDuplicates;
        } catch (ClassCastException cce) {
            throw new CommandException(FOR_NAME + cce);
        } catch (ArrayIndexOutOfBoundsException aiobe) {
            throw new CommandException(FOR_NAME + "Si e\' verificato un problema nell'indice di un puntamento in una lista di AD duplicate.\n", aiobe);
        } catch (NullPointerException npe) {
            throw new CommandException(FOR_NAME + "Si e\' verificato un problema nel puntamento a un oggetto.\n", npe);
        } catch (Exception e) {
            throw new CommandException(FOR_NAME + "Si e\' verificato un problema.\n",e);
        }
    }
    
    
    /**
     * <p>A partire da un parametro <code>p1</code> costituito da 
     * un Vector di tuple, e da un parametro <code>p2</code>, 
     * costituito da una lista di tuple da togliere da <code>p1</code>,  
     * rimuove da <code>p1</code> tutti i record presenti in <code>p2</code>.</p>
     * <p>Quindi se, per esempio, l'insieme dei dati di <code>p1</code> 
     * fosse il seguente:
     * <pre>{4, 8, 8, 15, 16, 23, 23, 42}</pre> si assume che il chiamante
     * abbia calcolato la lista dei duplicati doppi, che in questo caso sono
     * le tuple da togliere:
     * <pre>{8, 8, 23, 23}</pre> e che questa lista, passata come argomento 
     * (<code>p2</code>) debba essere tolta per cui, a seguito dell'applicazione
     * di questo metodo, dovremmo ottenere solo gli elementi che non presentano
     * duplicazioni, e cio&egrave;: 
     * <pre>{4, 15, 16, 42}</pre></p>     
     * <p>Nel caso specifico viene usato per togliere dall'elenco completo 
     * delle AD del semilavorato tutti i record soggetti a duplicazione, 
     * indistintamente.</p>
     * <p>Attenzione: <strong>notare che la cardinalit&agrave; dell'Array restituito
     * non &egrave; necessariamente uguale a:
     * <pre>p1.size() - p2.size()</pre></strong>
     * Questo avviene perché se la lista dei duplicati <code>p2</code>
     * presenta pi&uacute; occorrenze di una duplicazione, 
     * dalla lista iniziale <code>p1</code> &egrave; possibile toglierne
     * solo due e non pi&uacute; di due, a differenza di quanto l'esempio 
     * sopra riportato potrebbe suggerire. Esempio reale:
     * <pre> tupla di p1 (n. 1 occorrenze):
     *      416 MM15  Inglese scientifico 4S01572 3.0 0.0   Cafaro  Daniela CFRDNL79A60B963R    1   2018-03-05  2018-04-29
     * tupla di p2 (n. 2 occorrenze):
     *  24  416 MM15 Inglese scientifico 4S01572 Cafaro Daniela CFRDNL79A60B963R 2018-03-05 2018-04-29
     *  25  416 MM15 Inglese scientifico 4S01572 Cafaro Daniela CFRDNL79A60B963R 2018-03-05 2018-04-29</pre>
     * &Egrave; quindi possibile togliere la tupla con <code>id = 416</code>
     * da <code>p1</code> solo una volta! Se quindi la cardinalit&agrave; di 
     * <code>p1</code> fosse 8 e quella di <code>p2</code> fosse 3, 
     * l'Array finale in questo caso non avrebbe cardinalit&agrave; 8 - 3 = 5 
     * ma  8 - 2 = 6, perch&eacute; la riga con id 416 pu&ograve; 
     * essere sottratta una sola volta, non due!<br />
     * Pertanto, si pu&ograve; enunciare la formula di calcolo della
     * cardinalit&agrave; della lista al meno dei duplicati come:<br />
     * <code>c = c(p1) - c(p2) + r</code><br />
     * dove <code>c(p1)</code> &egrave; la cardinalit&agrave; dell'elenco
     * completo di elementi, cio&egrave; sia quelli che si presentano
     * una volta sola, sia quelli che si presentano pi&uacute; volte, 
     * <code>c(p2)</code> &egrave; la cardinalit&agrave; dell'elenco di elementi
     * che presentano duplicazioni logiche, e <code>r</code> &egrave; il numero 
     * di ripetizioni identiche.</p>
     * <p>Ad esempio, nel caso delle AD Semplici del II Semestre 2017, che erano
     * 1020 nell'estrazione grezza del semilavorato (<code>p1</code>) 
     * mentre i duplicati doppi erano 258, la cardinalit&agrave; delle 
     * (AD Semplici - Duplicati Doppi) non era uguale a 1020 - 258 = 762
     * bens&iacute; 1020 - 258 + 8 = 770, perch&eacute; c'erano 8 occorrenze
     * di duplicazioni identiche.<br />
     * <p><small>Erano le seguenti:<pre>
     * 24  416 MM15 Inglese scientifico 4S01572 Cafaro Daniela CFRDNL79A60B963R 2018-03-05 2018-04-29
     * 25  416 MM15 Inglese scientifico 4S01572 Cafaro Daniela CFRDNL79A60B963R 2018-03-05 2018-04-29
     * 26  417 MM15 Inglese scientifico 4S01572 Cafaro Daniela CFRDNL79A60B963R 2018-03-05 2018-04-29
     * 27  417 MM15 Inglese scientifico 4S01572 Cafaro Daniela CFRDNL79A60B963R 2018-03-05 2018-04-29
     * 29  419 MM15 Inglese scientifico 4S01572 Cafaro Daniela CFRDNL79A60B963R 2018-03-05 2018-04-29
     * 30  419 MM15 Inglese scientifico 4S01572 Cafaro Daniela CFRDNL79A60B963R 2018-03-05 2018-04-29
     * 103 913 W23  Pedagogia dell'infanzia 4S00794 Augelli Alessandra GLLLSN79M45I158D 2018-02-26 2018-04-21
     * 104 913 W23  Pedagogia dell'infanzia 4S00794 Augelli Alessandra GLLLSN79M45I158D 2018-02-26 2018-04-21
     * 105 914 W23  Pedagogia dell'infanzia 4S00794 Augelli Alessandra GLLLSN79M45I158D 2018-02-26 2018-04-21
     * 106 914 W23  Pedagogia dell'infanzia 4S00794 Augelli Alessandra GLLLSN79M45I158D 2018-02-26 2018-04-21
     * 107 915 W23  Pedagogia dell'infanzia 4S00794 Augelli Alessandra GLLLSN79M45I158D 2018-04-23 2018-06-09
     * 108 915 W23  Pedagogia dell'infanzia 4S00794 Augelli Alessandra GLLLSN79M45I158D 2018-04-23 2018-06-09
     * 109 916 W23  Pedagogia dell'infanzia 4S00794 Augelli Alessandra GLLLSN79M45I158D 2018-04-23 2018-06-09
     * 110 916 W23  Pedagogia dell'infanzia 4S00794 Augelli Alessandra GLLLSN79M45I158D 2018-04-23 2018-06-09
     * 112 918 W23  Pedagogia dell'infanzia 4S00794 Augelli Alessandra GLLLSN79M45I158D 2018-02-26 2018-04-21
     * 113 918 W23  Pedagogia dell'infanzia 4S00794 Augelli Alessandra GLLLSN79M45I158D 2018-02-26 2018-04-21
     * </pre>
     * Erano, appunto, 8 coppie, per cui:<br />
     * <code>c = c(p1) - c(p2) + r</code>  vale<br />
     * <ul>
     * <li>Elenco completo: FetchSize is: 1020</li>
     * <li>DUPLICATI DOPPI: FetchSize is: 258</li>
     * <li>Tolti duplicati: FetchSize is: 770</li>
     * </ul>
     * Infatti:<br />
     * <code>1020 - 258 + 8 =  770</code></small></p>
     * 
     * @param v la lista grezza delle AD da ripulire
     * @param duplicates la lista degli elementi che presentano duplicazioni, e le duplicazioni stesse
     * @return <code>List&lt;CourseBean&gt;</code> - ArrayList di AD che non presentano alcuna duplicazione
     * @throws CommandException se si verifica un problema nello scorrimento di liste o in qualche altro tipo di puntamento
     */
    private static List<CourseBean> removeDuplicates(Vector<CourseBean> v, 
                                                     ArrayList<CourseBean> duplicates)
                                              throws CommandException {
        List<CourseBean> vAsArrayList = null;
        try {
            vAsArrayList = new ArrayList<CourseBean>(v);
            vAsArrayList.removeAll(duplicates);
        } catch (ArrayIndexOutOfBoundsException aiobe) {
            String msg = FOR_NAME + "Si e\' verificato un problema nello scorrimento della lista delle AD grezze o di quelle duplicate" + aiobe.getMessage();
            throw new CommandException(msg, aiobe);
        } catch (ClassCastException cce) {
            String msg = FOR_NAME + "Si e\' verificato un problema in qualche conversione di tipo" + cce.getMessage();
            throw new CommandException(msg, cce);
        } catch (NullPointerException npe) {
            String msg = FOR_NAME + "Si e\' verificato un problema di puntamento a null" + npe.getMessage();
            throw new CommandException(msg, npe);
        } catch (Exception e) {
            String msg = FOR_NAME + "Si e\' verificato un problema" + e.getMessage();
            throw new CommandException(msg, e);
        }
        return vAsArrayList;
    }
    
    
    /**
     * Toglie da questo elenco tutte le AD che hanno la stessa chiave-base ma diverso periodo
     * Unisce i periodi, riducendo alla metà le righe dell'elenco dei duplicati per periodo
     * Costruisce la lista delle AD pulite unendo le righe con i periodi uniti
     * con le righe dei duplicati puliti al netto dei duplicati per periodo
     * Ottiene la lista dei "doppi singoli" ("doppioni puliti") ovvero di una singola copia delle righe doppie, non di entrambe
     * Unisce i periodi dove possibile, ovvero dove il doppione è persistito nella lista dei "doppioni puliti"
     * Toglie dalla lista grezza delle AD semplici (semilavorato) tutti i doppi indistintamente
     * Riaggiunge alla lista depurata di tutti i doppi la lista dei "doppi singoli" lavorati, ovvero con i periodi uniti
     * Questa è la lista finale pulita delle AD semplici, su cui si può implementare l'ulteriore algoritmo di scelta delle AD Semplici da valutare
     * Toglie da questo elenco, in maniera relativamente arbitraria, i "doppioni doppi",
     * cioè, in altri termini, tiene soltanto una copia delle chiavi logiche duplicate
     * rimuove dal vector i 2 oggetti con i due periodi parziali e ci aggiunge il vector con il periodo risultante dai due
     * 
     * @param v 
     * @param duplicates 
     * @return 
     * @throws CommandException 
     * @deprecated
     */
    private ArrayList<CourseBean> purge(Vector<CourseBean> v,
                                        ArrayList<CourseBean> duplicates)
                                 throws CommandException {
        ArrayList<CourseBean> distinctSingleKeys = null;
        try {
            // Ottiene una lista di solo i campioni di duplicati, cioè in pratica 
            // di chiavi che hanno due o più occorrenze nella lista delle AD
            List<CourseBean> samplesDuplicates = splitDuplicates(duplicates);
            // Ottiene una lista di tuple aventi la stessa chiave ma periodi diversi
            List<CourseBean> differentByPeriod = getCoupletByPeriod(samplesDuplicates);
            // Unisce i periodi, dimezzando le tuple
            List<CourseBean> merged = mergeDates(differentByPeriod);
            // Toglie dall'elenco completo tutti gli elementi che hanno duplicati logici
            List<CourseBean> singleKeys = removeDuplicates(v, new ArrayList<CourseBean>(duplicates));
            // Fa una copia dell'elenco delle singole istanze dei duplicati logici
            List<CourseBean> refinedDuplicates = new ArrayList<CourseBean>(samplesDuplicates);
            // In questa lista ci sono ancora le tuple distinte per periodo ma doppie per chiave
            // Bisogna quindi togliere tali elementi dalla lista delle singole istanze 
            // dei duplicati logici, prima di aggiungerci le tuple con i periodi uniti
            // Quindi fa una copia dei campioni di duplicati...
            List<CourseBean> samplesDuplicatesButDifferentByPeriod = new ArrayList<CourseBean>(refinedDuplicates);
            // ...e vi toglie tutte le tuple che verranno poi unite:
            samplesDuplicatesButDifferentByPeriod.removeAll(differentByPeriod);
            // Aggiunge alla lista dei campioni di duplicati 
            // ripulita delle tuple doppie perché dovevano essere accorpati i periodi 
            // le tuple con i periodi uniti
            samplesDuplicatesButDifferentByPeriod.addAll(merged);
            // Riaggiunge alla lista depurata di tutti i doppi la lista dei "doppi singoli" lavorati, 
            // ovvero con l'aggiunta di un singolo esemplare di doppio ma con i periodi uniti
            // Questa è la lista finale pulita delle AD semplici, su cui si può implementare l'ulteriore algoritmo di scelta delle AD Semplici da valutare
            singleKeys.addAll(samplesDuplicatesButDifferentByPeriod);
            // cardinalità: 880, infatti 1020 - (258 + 8) + (126 - 32 + 16) = 880!
            // Su questo ci sono ancora duplicazioni. Bisogna eliminare i periodi doppi residui:
            List<CourseBean> equalsButPeriod = getCoupletByPeriod(singleKeys);
            // Ordina i duplicati logici per chiave
            sortByCode(equalsButPeriod);
            // Unisce i periodi, dimezzando le tuple doppie
            List<CourseBean> mergedDates = mergeDates(equalsButPeriod);
            // Toglie i doppi per periodo
            singleKeys.removeAll(equalsButPeriod);
            // Ci aggiunge i singoli con i periodi uniti (totale: 880 - 262 + 126 = 744 - mi trovo grosso modo perché la cardinalità è 747...) 
            singleKeys.addAll(mergedDates);
            // Ho capito perché! Ci sono alcune duplicazione di righe identiche, che vanno tolte:
            // 922 W23 Psicologia transculturale   4S02388 -2.0    -2.0            De Cordova  Federica    DCRFRC67T44D969C    1   26/02/2018  09/06/2018  W234S02388DCRFRC67T44D969C
            // 924 W23 Psicologia transculturale   4S02388 -2.0    -2.0        54000.0 De Cordova  Federica    DCRFRC67T44D969C    0   26/02/2018  09/06/2018  W234S02388DCRFRC67T44D969C
            // 922 W23 Psicologia transculturale   4S02388 -2.0    -2.0            De Cordova  Federica    DCRFRC67T44D969C    1   26/02/2018  09/06/2018  W234S02388DCRFRC67T44D969C
            // 924 W23 Psicologia transculturale   4S02388 -2.0    -2.0        54000.0 De Cordova  Federica    DCRFRC67T44D969C    0   26/02/2018  21/04/2018  W234S02388DCRFRC67T44D969C
            // Ordina la lista ottenuta in base al criterio specificato nel metodo override compareTo del Bean
            Collections.sort(singleKeys);
            // Clona la lista dei duplicati
            distinctSingleKeys = new ArrayList<CourseBean>(singleKeys);
            // Cicla la lista dei duplicati
            for (int i = 0; i < distinctSingleKeys.size(); i++) {
                // Costruisce l'indice del record successivo a quello recuperato
                int j = i + 1;
                // Controllo per evitare puntamento fuori tabella
                if (j < singleKeys.size()) {
                    // I doppioni veri hanno anche lo stesso id
                    if (singleKeys.get(i).getId() == singleKeys.get(j).getId()) {
                        // Elimina il doppione vero (vero = identico in tutto, anche nell'id)
                        distinctSingleKeys.remove(j);
                    }
                }
            }
        } catch (ArrayIndexOutOfBoundsException aiobe) {
            String msg = FOR_NAME + "Si e\' verificato un problema nello scorrimento della lista delle AD grezze o di quelle duplicate" + aiobe.getMessage();
            throw new CommandException(msg, aiobe);
        } catch (ClassCastException cce) {
            String msg = FOR_NAME + "Si e\' verificato un problema in qualche conversione di tipo" + cce.getMessage();
            throw new CommandException(msg, cce);
        } catch (NullPointerException npe) {
            String msg = FOR_NAME + "Si e\' verificato un problema di puntamento a null" + npe.getMessage();
            throw new CommandException(msg, npe);
        } catch (Exception e) {
            String msg = FOR_NAME + "Si e\' verificato un problema" + e.getMessage();
            throw new CommandException(msg, e);
        }        
        return distinctSingleKeys;
    }
    
    
    /**
     * <p>Trasforma il Vector in una HashMap.</p>
     * 
     * @param v
     * @return
     * @throws CommandException
     */
    private static HashMap<String, CourseBean> transform(Vector<CourseBean> v) 
                                                  throws CommandException {
        HashMap<String, CourseBean> vH = new HashMap<String, CourseBean>();
        
        try {
            // Ciclo forEach (Java 1.5+)            
            for (CourseBean ad : v) {
                String key = ad.getKey();
                vH.put(key, ad);
            }
        } catch (AttributoNonValorizzatoException anve) {
            throw new CommandException(anve);
        } catch (ArrayIndexOutOfBoundsException aiobe) {
            throw new CommandException(aiobe);
        } catch (NullPointerException npe) {
            throw new CommandException(npe);
        } catch (Exception e) {
            throw new CommandException(e);
        }
        return vH;
    }
    
    
    private ArrayList<CourseBean> manage(Vector<CourseBean> v) {
        List<CourseBean> simpleDA = new ArrayList<CourseBean>(v);
        
        // Ripulisce la lista delle AD Semplici dai duplicati
        
        // Controlla se è un corso di area medica
        //if (ad.getCodiceCdSUGOV().startsWith("M")) {
            /* *******************************
             *    Ramo Corsi di area medica
             * *******************************/
            // Se NON è un corso elettivo
        //    if (!corsiElettivi.contains(ad.getCodiceADUGOV())) {
                // Se NON è un corso elettivo ha senso continuare...
                //
          //  }
        /*}
        
        else {
            /* *******************************
             *  Ramo Corsi di area non medica
             * *******************************/
            //
        //}
            // Se no, controlla se i CFU totali sono >= 4
                // Controlla che i CFU di tipo LEZ siano > 0
        
        // Se sì controlla che non sia un corso elettivo
            // Controlla che i CFU di tipo LEZ siano > 0
        
        // Sul risultato trovato cerca i doppioni
            // Se è un doppione cerca di accorpare i periodi
        /*
        // Controlla che i CFU totali siano pià di 3
        if (ad.getCreditiTotali() < 4) {
            // Se è un corso di area medica va considerato, altrimenti va scartato direttamente
            if (ad.getCodiceCdSUGOV())
        }
        // Controlla che ci siano ore di lezione
        //        if (ad.getOreLezione() > 0) {
            
        //        }
        }*/
        return null;
    }
    
}