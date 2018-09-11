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

package it.alma.command;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import it.alma.DBWrapper;
import it.alma.Query;
import it.alma.bean.CourseBean;
import it.alma.bean.ItemBean;
import it.alma.exception.CommandException;
import it.alma.exception.NotFoundException;
import it.alma.exception.WebStorageException;


/** 
 * <p>ValuationCommand.java</p>
 * <p>
 * Classe per l'implementazione delle regole di scelta delle AD
 * da mettere in valutazione ai fini dei questionari della didattica.<br />
 * 
 * Consente di applicare su una lista di Attivit&agrave; Didattiche 
 * le regole stabilite dal presidio della qualit&agrave;.<br />
 * </p>
 * <p>Created on martedì 4 settembre 2018 10:28:08</p>
 * 
 * 
 * @author <a href="mailto:giovanroberto.torre@univr.it">Giovanroberto Torre</a>
 */
public class ValuationCommand extends ItemBean implements Command {
    
    /**
     *  Nome di questa classe 
     *  (utilizzato per contestualizzare i messaggi di errore)
     */
    static final String FOR_NAME = "\n" + Logger.getLogger(new Throwable().getStackTrace()[0].getClassName()) + ": ";
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
        ;   // Vuota
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
     */
    public void execute(HttpServletRequest req) throws CommandException {  
        DBWrapper db = null;               // Databound        
        Vector<CourseBean> v = new Vector<CourseBean>();
        Vector<CourseBean> adSemplici = new Vector<CourseBean>();
        Vector<CourseBean> duplicati = new Vector<CourseBean>();
        Vector<CourseBean> purged = new Vector<CourseBean>();
        HashMap<String, CourseBean> vHash = new HashMap<String, CourseBean>();
        List<CourseBean> duplicates = null;
        List<CourseBean> cleanDuplicates = null;
        //List<CourseBean> duplicati = new ArrayList<CourseBean>();
        // Instanzia nuova classe WebStorage per il recupero dei dati
        try {
            db = new DBWrapper();
        } catch (WebStorageException wse) {
            throw new CommandException(FOR_NAME + "Non e\' disponibile un collegamento al database\n." + wse.getMessage());
        }
        // Recupero Attività Didattiche Semplici
        try {
            v = db.getADSemplici();
            /* Trasforma il Vector in HashMap con chiave formata da una concatenazione univoca dei valori
            for (int i = 0; i < v.size(); i++) {
                CourseBean course = v.get(i);
                // Genera la chiave
                String key =  course.getKey();
                vHash.put(key, course);
            }*/
        } catch (NotFoundException nfe) {
            throw new CommandException(FOR_NAME + ": Impossibile recuperare un attributo obbligatorio.\n" + nfe.getMessage());
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
            throw new CommandException(FOR_NAME + "Si e\' verificato un problema nel recuperare i dati relativi all'elenco delle AD.\n" + wse.getMessage() + "\n stack: " + sb);
        } catch (NullPointerException npe) {
            StackTraceElement[] stackTrace = npe.getStackTrace();
            throw new CommandException(FOR_NAME + "Si e\' verificato un puntamento a null in " + this.getClass().getName() + ": " + npe.getMessage() + "\n stack: " + stackTrace[0]);            
        } catch (Exception e) {
            StackTraceElement[] stackTrace = e.getStackTrace();
            throw new CommandException(FOR_NAME + "Eccezione generica da " + this.getClass().getName() + ": " + e.getMessage() + "\n stack: " + stackTrace[0]);
        }
        // Individua le Attività Didattiche Semplici da porre in valutazione
        try {
            int vSize = v.size();
            purged = v;
            /*
            int j = 0;
            while (j < vSize) {
                for (int i = 0; i < vSize; i++) {
                    CourseBean first = v.elementAt(j);
                    CourseBean current = v.elementAt(i);
                    // Controllo sui duplicati
                    if (first.getKey().equals(current.getKey())) {
                        int indexOfDuplicate = v.indexOf(current);
                        duplicati.add(current);   // Elements will be removed later                   
                    }
                }
                j = j + 1;
            }
            v.removeAll(duplicati);
            */
            duplicates = new ArrayList<CourseBean>();
            int pos = 0;
            for (int i = 0; i < v.size(); i++) {
                for (int j = 0; j < v.size(); j++) {
                    if (i != j) {   // Se i e j sono uguali sta puntando allo stesso elemento, ed è evidente che un elemento è uguale a se stesso...
                        CourseBean courseRaw = v.elementAt(i);
                        CourseBean courseOrd = v.elementAt(j);
                        if (courseRaw.getKey().equals(courseOrd.getKey()) && (courseRaw.getId() != courseOrd.getId())) {
                            //duplicati.insertElementAt(courseOrd, pos);
                            //pos++;
                            duplicates.add(courseOrd);
                        }
                    }
                }
            }
            Collections.sort(duplicates);
            cleanDuplicates = new ArrayList<CourseBean>(duplicates);
            for (int i = 0; i < duplicates.size(); i++) {
                int j = i + 1;
                if (j < duplicates.size()) {
                    if (duplicates.get(i).getId() == duplicates.get(j).getId()) {
                        cleanDuplicates.remove(j);
                    }
                }
            }
            /* Toglie i doppioni puri dall'elenco completo
            for (int i = 0; i < duplicates.size(); i++) {
                CourseBean current = duplicates.get(i);
                for (int j = 0; j < v.size(); j++) {
                    CourseBean c = v.elementAt(j);
                    if (c.getId() == current.getId()) {
                        v.removeElementAt(j);
                    }
                }
            }*/
            //Collections.sort(sortedKeys);
            //return resultsVector;
                
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
//                if (ad.getOreLezione() > 0) {
                    
//                }
            }*/
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
            throw new CommandException(FOR_NAME + ": Puntamento fuori tabella!\n" + aiobe.getMessage() + "\n stack: " + sb, aiobe);
        } catch (NullPointerException npe) {
            StackTraceElement[] stackTrace = npe.getStackTrace();
            throw new CommandException(FOR_NAME + "Si e\' verificato un puntamento a null in " + this.getClass().getName() + ": " + npe.getMessage() + "\n stack: " + stackTrace[0], npe);            
        } catch (Exception e) {
            StackTraceElement[] stackTrace = e.getStackTrace();
            throw new CommandException(FOR_NAME + "Eccezione generica da " + this.getClass().getName() + ": " + e.getMessage() + "\n stack: " + stackTrace[0], e);
        }
        // Imposta il testo del Titolo da visualizzare prima dell'elenco
        req.setAttribute("titoloE", "AD Semplici");
        // Salva nella request: Titolo (da mostrare nell'HTML)
        req.setAttribute("tP", req.getAttribute("titoloE"));         
        // Imposta la Pagina JSP di forwarding
        req.setAttribute("fileJsp", nomeFileElenco);
         // Salva nella request: elenco AD Semplici
        req.setAttribute("elenco", v);
        req.setAttribute("duplicati", cleanDuplicates);
    }    
    
}