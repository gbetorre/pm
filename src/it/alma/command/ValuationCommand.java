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
import java.util.Comparator;
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
import it.alma.exception.AttributoNonValorizzatoException;
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
    public void execute(HttpServletRequest req) 
                 throws CommandException {  
        DBWrapper db = null;               // Databound        
        Vector<CourseBean> v = new Vector<CourseBean>();
        List<CourseBean> sda = new Vector<CourseBean>();
        List<CourseBean> duplicates = null;
        //Vector<CourseBean> purged = new Vector<CourseBean>();
        //HashMap<String, CourseBean> vHash = new HashMap<String, CourseBean>();
        // Instanzia nuova classe WebStorage per il recupero dei dati
        try {
            db = new DBWrapper();
        } catch (WebStorageException wse) {
            throw new CommandException(FOR_NAME + "Non e\' disponibile un collegamento al database\n." + wse.getMessage(), wse);
        }
        // Recupera Attività Didattiche Semplici
        try {
            v = db.getADSemplici();
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
        
        duplicates = getDuplicates(v);
        // Ordina i duplicati per chiave
        sortByCode(duplicates);
        List<CourseBean> cleanDuplicates = splitDuplicates(duplicates);
        // Individua le Attività Didattiche Semplici da porre in valutazione
        sortByCode(v);
        sda = removeDuplicates(v, new ArrayList<CourseBean>(cleanDuplicates));
        // Imposta il testo del Titolo da visualizzare prima dell'elenco
        req.setAttribute("titoloE", "AD Semplici");
        // Salva nella request: Titolo (da mostrare nell'HTML)
        req.setAttribute("tP", req.getAttribute("titoloE"));         
        // Imposta la Pagina JSP di forwarding
        req.setAttribute("fileJsp", nomeFileElenco);
         // Salva nella request: elenco AD Semplici
        req.setAttribute("elenco", sda);
        req.setAttribute("duplicati", cleanDuplicates);
    }    
    
    
    /**
     * <p>Restituisce una lista di AD che sono state individuate come doppie
     * in base alla chiave specificata nell'oggetto AD.</p>
     * 
     * @param v
     * @return
     * @throws CommandException
     */
    private List<CourseBean> getDuplicates(Vector<CourseBean> v)
                                    throws CommandException {
        List<CourseBean> duplicates = null;
        List<CourseBean> cleanDuplicates = null;
        try {
            int vSize = v.size();
            duplicates = new ArrayList<CourseBean>();
            // Cicla due volte il Vector delle AD per individuare AD con la stessa chiave
            for (int i = 0; i < vSize; i++) {
                for (int j = 0; j < vSize; j++) {
                    if (i != j) {   // Se i e j sono uguali sta puntando allo stesso elemento, ed è evidente che un elemento è uguale a se stesso...
                        CourseBean courseRaw = v.elementAt(i);
                        CourseBean courseOrd = v.elementAt(j);
                        if (courseRaw.getKey().equals(courseOrd.getKey()) && (courseRaw.getId() != courseOrd.getId())) {
                            duplicates.add(courseOrd);
                        }
                    }
                }
            }
            // Ordina la lista ottenuta in base al criterio specificato nel metodo override compareTo del Bean
            Collections.sort(duplicates);
            // Clona la lista dei duplicati
            cleanDuplicates = new ArrayList<CourseBean>(duplicates);
            // Cicla la lista dei duplicati
            for (int i = 0; i < duplicates.size(); i++) {
                // Costruisce l'indice del record successivo a quello recuperato
                int j = i + 1;
                // Controllo per evitare puntamento fuori tabella
                if (j < duplicates.size()) {
                    // I doppioni veri hanno anche lo stesso id
                    if (duplicates.get(i).getId() == duplicates.get(j).getId()) {
                        // Elimina il doppione vero
                        cleanDuplicates.remove(j);
                    }
                }
            }
            return cleanDuplicates;
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
    }
    
    
    private List<CourseBean> splitDuplicates(List<CourseBean> duplicates)
                                      throws CommandException {
        int vSize = duplicates.size();
        List<CourseBean> halfDuplicates = new ArrayList<CourseBean>();
        try {
            // Cicla la lista dei duplicati
            for (int i = 0; i < duplicates.size(); i++) {
                // Costruisce l'indice del record successivo a quello recuperato
                int j = i + 1;
                // Controllo per evitare puntamento fuori tabella
                if (j < duplicates.size()) {
                    // Recupera l'occorrenza corrente
                    CourseBean doublet = duplicates.get(i);
                    // Se i due doppioni hanno la stessa chiave ne prende il primo
                    if (doublet.getKey().equals(duplicates.get(j).getKey())) {
                        // Prende il primo
                        halfDuplicates.add(doublet);
                    }
                }
            }
            return halfDuplicates;
        } catch (AttributoNonValorizzatoException anve) {
            throw new CommandException(anve);
        } catch (ArrayIndexOutOfBoundsException aiobe) {
            throw new CommandException(aiobe);
        } catch (NullPointerException npe) {
            throw new CommandException(npe);
        } catch (Exception e) {
            throw new CommandException(e);
        }
    }
    
    
    private static Vector<CourseBean> removeDuplicates(Vector<CourseBean> v, ArrayList<CourseBean> duplicates)
            throws CommandException {
        try {
            for (int i = 0; i < duplicates.size(); i++) {
                CourseBean ad = v.elementAt(i);
                // Ciclo forEach (Java 1.5+)
                for (int j = 0; j < duplicates.size(); j++) {
                    CourseBean doublet = duplicates.get(j);
                    if (ad.getKey().equals(doublet.getKey())) {
                       v.removeElementAt(i); 
                    }
                }
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
        return v;
    }
    
    
    private ArrayList<CourseBean> clean(Vector<CourseBean> v) {
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
    
    
    private void sortByKey(List<CourseBean> l) 
            throws CommandException {
        Collections.sort(l, new Sortbykey());
        //return null;
    }
    
    
    private void sortByCode(List<CourseBean> l) 
            throws CommandException {
        Collections.sort(l, new Sortbycode());
        //return null;
    }
    
    
    protected class Sortbykey  implements Comparator<CourseBean> 
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
    
    
    protected class Sortbycode  implements Comparator<CourseBean> 
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
                return std.compareTo(o2.getCodiceFiscaleDocente() + o2.getCodiceADUGOV() + o2.getCodiceCdSUGOV() + o1.getInizioPerDid() + o1.getFinePerDid());
            } catch (NullPointerException npe) {
                return 1;
            }
        } 
    } 
    
}