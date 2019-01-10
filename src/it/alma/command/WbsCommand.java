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

package it.alma.command;

import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Vector;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.oreilly.servlet.ParameterParser;

import it.alma.DBWrapper;
import it.alma.Main;
import it.alma.Query;
import it.alma.Utils;
import it.alma.bean.ActivityBean;
import it.alma.bean.CodeBean;
import it.alma.bean.ItemBean;
import it.alma.bean.PersonBean;
import it.alma.bean.ProjectBean;
import it.alma.bean.WbsBean;
import it.alma.exception.AttributoNonValorizzatoException;
import it.alma.exception.CommandException;
import it.alma.exception.WebStorageException;


/** 
 * <p><code>WbsCommand.java</code><br />
 * Implementa la logica per la gestione delle WBS† di un progetto on line (POL).</p>
 * 
 * <p>Created on martedÏ 27 novembre 2018 12:56</p>
 * 
 * @author <a href="mailto:giovanroberto.torre@univr.it">Giovanroberto Torre</a>
 * @author <a href="mailto:andrea.tonel@studenti.univr.it">Andrea Tonel</a>
 */
public class WbsCommand extends ItemBean implements Command {
    
    /**
     *  Nome di questa classe 
     *  (utilizzato per contestualizzare i messaggi di errore)
     */
    static final String FOR_NAME = "\n" + Logger.getLogger(new Throwable().getStackTrace()[0].getClassName()) + ": ";
    /**
     * Log per debug in produzione
     */
    protected static Logger LOG = Logger.getLogger(Main.class.getName());
    /**
     * Pagina a cui la command reindirizza per mostrare la lista delle WBS del progetto
     */
    private static final String nomeFileElenco = "/jsp/projWBS.jsp";
    /**
     * Pagina a cui la command fa riferimento per permettere l'aggiunta o la modifica
     * di una nuova wbs al progetto
     */
    private static final String nomeFileWbs = "/jsp/wbs.jsp";
    /**
     * Struttura contenente le pagina a cui la command fa riferimento per mostrare tutti gli attributi del progetto
     */    
    private static final HashMap<String, String> nomeFile = new HashMap<String, String>();
    /**
     *  Progetto di dato id
     */
    ProjectBean runtimeProject = null; 
    
    
    /** 
     * Crea una nuova istanza di WbsCommand 
     */
    public WbsCommand() {
        /*;*/   // It doesn't Anything
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
        // Carica la hashmap contenente le pagine da includere in funzione dei parametri sulla querystring
        nomeFile.put(Query.PART_WBS, nomeFileElenco);
        nomeFile.put(Query.ADD_TO_PROJECT, nomeFileWbs);
        nomeFile.put(Query.MODIFY_PART, nomeFileWbs);
    }  
  
    
    /**
     * <p>Gestisce il flusso principale.</p>
     * <p>Prepara i bean.</p>
     * <p>Passa nella Request i valori che verranno utilizzati dall'applicazione.</p>
     * 
     * @param req la HttpServletRequest contenente la richiesta del client
     * @throws CommandException se si verifica un problema, tipicamente nell'accesso a campi non accessibili o in qualche altro tipo di puntamento 
     */
    public void execute(HttpServletRequest req) 
                 throws CommandException {
        /* ******************************************************************** *
         *              Dichiara e inizializza variabili locali                 *
         * ******************************************************************** */
        // Databound
        DBWrapper db = null;
        // Parser per la gestione assistita dei parametri di input
        ParameterParser parser = new ParameterParser(req);
        // Dichiara la pagina a cui reindirizzare
        String fileJspT = null;
        // Flag per decidere se mostrare l'header
        boolean isHeader = true;
        // Flag per decidere se mostrare il footer
        boolean isFooter = true;
        // Utente loggato
        PersonBean user = null;
        // Dichiara elenco di progetti
        Vector<ProjectBean> v = new Vector<ProjectBean>();
        // Dichiara elenco di wbs
        Vector<WbsBean> vWbs = new Vector<WbsBean>();
        // Dichiara elenco di wbs non workpackage
        Vector<WbsBean> wbs = new Vector<WbsBean>();
        // Dichiara struttura di persone che possono essere aggiunte a un'attivit√†
        //Vector<PersonBean> candidates = null;
        // Dichiara struttura di Work Package cui pu√≤ essere aggiunta un'attivit√†
        //Vector<WbsBean> workPackage = null;
        // Dichiara lista di valori di complessit√†
        //LinkedList<CodeBean> complexity = null;
        // Dichiara lista di valori di stati attivit√†
        LinkedList<CodeBean> states = null;
        // Data di oggi sotto forma di oggetto String
        String today = null;
        /* ******************************************************************** *
         *                    Recupera parametri e attributi                    *
         * ******************************************************************** */
        // Recupera o inizializza 'id progetto'
        int idPrj = parser.getIntParameter("id", Utils.DEFAULT_ID);
        // Recupera o inizializza 'tipo pagina'   
        String part = parser.getStringParameter("p", "-");
        // Flag di scrittura
        boolean write = (boolean) req.getAttribute("w");
        /* ******************************************************************** *
         *      Instanzia nuova classe DBWrapper per il recupero dei dati       *
         * ******************************************************************** */
        try {
            db = new DBWrapper();
        } catch (WebStorageException wse) {
            throw new CommandException(FOR_NAME + "Non e\' disponibile un collegamento al database\n." + wse.getMessage(), wse);
        }
        /* ******************************************************************** *
         *                         Recupera la Sessione                         *
         * ******************************************************************** */
        try {
            // Recupera la sessione creata e valorizzata per riferimento nella req dal metodo authenticate
            HttpSession ses = req.getSession(Query.IF_EXISTS_DONOT_CREATE_NEW);
            if (ses == null) {
                throw new CommandException("Attenzione: controllare di essere autenticati nell\'applicazione!\n");
            }
            user = (PersonBean) ses.getAttribute("usr");
            if (user == null) {
                throw new CommandException("Attenzione: controllare di essere autenticati nell\'applicazione!\n");
            }
        } catch (IllegalStateException ise) {
            String msg = FOR_NAME + "Impossibile redirigere l'output. Verificare se la risposta e\' stata gia\' committata.\n";
            LOG.severe(msg);
            throw new CommandException(msg + ise.getMessage(), ise);
        } catch (ClassCastException cce) {
            String msg = FOR_NAME + ": Si e\' verificato un problema in una conversione di tipo.\n";
            LOG.severe(msg);
            throw new CommandException(msg + cce.getMessage(), cce);
        } catch (NullPointerException npe) {
            String msg = FOR_NAME + "Si e\' verificato un problema di puntamento a null, probabilmente nel tentativo di recuperare l\'utente.\n";
            LOG.severe(msg);
            throw new CommandException("Attenzione: controllare di essere autenticati nell\'applicazione!\n" + npe.getMessage(), npe);
        } catch (Exception e) {
            String msg = FOR_NAME + "Si e\' verificato un problema.\n";
            LOG.severe(msg);
            throw new CommandException(msg + e.getMessage(), e);
        }
        /* ******************************************************************** *
         *                          Corpo del programma                         *
         * ******************************************************************** */
        // Decide il valore della pagina
        try {
            // Controllo sull'input
            if (idPrj > Query.NOTHING) {
                // Recupera in ogni caso il progetto richiesto dalla navigazione utente
                runtimeProject = db.getProject(idPrj, user.getId());
                // Verifica se Ë presente il parametro 'p'
                if (nomeFile.containsKey(part)) {
                    // Verifica se deve eseguire un'operazione di scrittura
                    if (write) {
                        // Creazione della tabella che conterr‡† i valori dei parametri passati dalle form
                        HashMap<String, HashMap<String, String>> params = new HashMap<String, HashMap<String, String>>();
                        loadParams(part, parser, params);
                        // Recupera la sessione creata e valorizzata per riferimento nella req dal metodo authenticate
                        HttpSession ses = req.getSession(Query.IF_EXISTS_DONOT_CREATE_NEW);
                        // Recupera i progetti su cui l'utente ha diritti di scrittura
                        Vector<ProjectBean> writablePrj = (Vector<ProjectBean>) ses.getAttribute("writableProjects");
                        // Se non ci sono progetti scrivibili e il flag "write" Ë true c'È qualcosa che non va...
                        if (writablePrj == null) {
                            String msg = FOR_NAME + "Il flag di scrittura e\' true pero\' non sono stati trovati progetti scrivibili: problema!.\n";
                            LOG.severe(msg);
                            throw new CommandException("Attenzione: controllare di essere autenticati nell\'applicazione!\n");
                        }
                        // Recupera dalla sessione le attivit√† su cui l'utente ha diritti di scrittura
                        LinkedHashMap<Integer, Vector<WbsBean>> userWritableWbsByProjectId = (LinkedHashMap<Integer, Vector<WbsBean>>) ses.getAttribute("writableWbs");
                        // Trasforma un Vector di progetti scrivibili dall'utente loggato in un dictionary degli stessi
                        HashMap<Integer, ProjectBean> writableProjects = ProjectCommand.decant(writablePrj);
                        // Controlla se deve effettuare un inserimento o un aggiornamento
                        if (part.equalsIgnoreCase(Query.PART_WBS)) {
                            /* ************************************************ *
                             *                  UPDATE Wbs Part                 *
                             * ************************************************ */
                            db.updateWbsPart(idPrj, user, writableProjects, userWritableWbsByProjectId, params);
                        } else if (part.equalsIgnoreCase(Query.ADD_TO_PROJECT)) {
                            /* ************************************************ *
                             *                  INSERT Wbs Part                 *
                             * ************************************************ */
                            loadParams(part, parser, params);
                            db.insertWbs(idPrj, user, params.get(Query.ADD_TO_PROJECT));
                        }
                        // Aggiorna i progetti, le wbs† dell'utente in sessione
                        //ses.removeAttribute("writableProjects");
                        ses.removeAttribute("writableWbs");
                        //ses.setAttribute("writableProjects", userWritableProjects);
                        ses.setAttribute("writableWbs", userWritableWbsByProjectId);
                    }
                    /* **************************************************** *
                     *                   SELECT WBS Part                    *
                     * **************************************************** */
                    // Recupera eventuali campi dal Database
                    if (part.equals(Query.UPDATE_PART_PROJECT)) {
                        String idWbs = HomePageCommand.getParameters(req, Utils.MIME_TYPE_TEXT);
                        today = Utils.format(Utils.getCurrentDate());
                    } else if (part.equals(Query.ADD_TO_PROJECT)) {
                        wbs = db.getWbs(idPrj, Query.WBS_NOT_WP);
                    }
                    fileJspT = nomeFile.get(part);
                } else {
                    // Se il parametro 'p' non Ë presente, deve solo selezionare tutte le wbs
                    vWbs = db.getWbs(idPrj, Query.WBS_ALL);
                    fileJspT = nomeFileElenco;
                }
            } else {
                // Se siamo qui vuol dire che l'id del progetto non √® > zero, il che √® un guaio
                HttpSession ses = req.getSession(Query.IF_EXISTS_DONOT_CREATE_NEW);
                ses.invalidate();
                String msg = FOR_NAME + "Qualcuno ha tentato di inserire un indirizzo nel browser avente un id progetto non valido!.\n";
                LOG.severe(msg);
                throw new CommandException("Attenzione: indirizzo richiesto non valido!\n");
            }
        } catch (AttributoNonValorizzatoException anve) {
            String msg = FOR_NAME + "Si e\' verificato un problema nell\'accesso ad un attributo obbligatorio del bean, probabilmente l\'id dell\'utente..\n";
            LOG.severe(msg);
            throw new CommandException(msg + anve.getMessage(), anve);
        } catch (WebStorageException wse) {
            String msg = FOR_NAME + "Si e\' verificato un problema nel recupero di valori dal db.\n";
            LOG.severe(msg);
            throw new CommandException(msg + wse.getMessage(), wse);
        } catch (IllegalStateException ise) {
            String msg = FOR_NAME + "Impossibile redirigere l'output. Verificare se la risposta e\' stata gia\' committata.\n";
            LOG.severe(msg);
            throw new CommandException(msg + ise.getMessage(), ise);
        } catch (ClassCastException cce) {
            String msg = FOR_NAME + "Si e\' verificato un problema in una conversione di tipo.\n";
            LOG.severe(msg);
            throw new CommandException(msg + cce.getMessage(), cce);
        } catch (NullPointerException npe) {
            String msg = FOR_NAME + "Si e\' verificato un problema di puntamento a null.\n Attenzione: controllare di essere autenticati nell\'applicazione!\n";
            LOG.severe(msg);
            throw new CommandException(msg + npe.getMessage(), npe);
        } catch (Exception e) {
            String msg = FOR_NAME + "Si e\' verificato un problema.\n";
            LOG.severe(msg);
            throw new CommandException(msg + e.getMessage(), e);
        }
        /* ******************************************************************** *
         *              Settaggi in request dei valori calcolati                *
         * ******************************************************************** */
        // Importa nella request flag di visualizzazione header e footer
        req.setAttribute("header", isHeader);
        // Importa nella request flag di visualizzazione header e footer
        req.setAttribute("footer", isFooter);
        // Imposta nella request dettaglio progetto
        req.setAttribute("progetto", runtimeProject);
        // Imposta nella request elenco di tutte le wbs del progetto
        req.setAttribute("allWbs", vWbs);
        // Imposta nella request elenco di tutte le wbs non workpackage del progetto
        req.setAttribute("wbs", wbs);
        // Imposta nella request elenco persone del dipartimento
        //req.setAttribute("people", candidates);
        // Imposta nella request elenco wbs associabili
        //req.setAttribute("wbs", workPackage);
        // Imposta nella request elenco wbs associabili
        //req.setAttribute("complessita", complexity);
        // Imposta nella request elenco wbs associabili
        req.setAttribute("statiAttivita", states);
        // Imposta nella request data di oggi 
        req.setAttribute("now", today);
        // Imposta la Pagina JSP di forwarding
        req.setAttribute("fileJsp", fileJspT);
    }
    
    
    /**
     * <p>Valorizza per riferimento una mappa contenente i valori relativi  
     * ad una attivit&agrave; eventualmente da aggiornare.</p> 
     * 
     * @param part la sezione del sito che si vuole aggiornare
     * @param parser oggetto per la gestione assistita dei parametri di input, gia' pronto all'uso
     * @param formParams mappa da valorizzare per riferimento (ByRef)
     * @throws CommandException se si verifica un problema nella gestione degli oggetti data o in qualche tipo di puntamento
     */
    private static void loadParams(String part, 
                                   ParameterParser parser,
                                   HashMap<String, HashMap<String, String>> formParams)
                            throws CommandException {
        /* **************************************************** *
         *                      Ramo di Wbs                     *
         * **************************************************** */
        if (part.equalsIgnoreCase(Query.PART_WBS)) {
            // Recupero e caricamento parametri di project charter/milestone
            int totWbs = Integer.parseInt(parser.getStringParameter("wbs-loop-status", Utils.VOID_STRING));
            HashMap<String, String> wbs = new HashMap<String, String>();
            for (int i = 0; i <= totWbs; i++) {
                String workpackage = "false";
                wbs.put("wbs-id" + String.valueOf(i), parser.getStringParameter("wbs-id" + String.valueOf(i), Utils.VOID_STRING));
                wbs.put("wbs-name" + String.valueOf(i), parser.getStringParameter("wbs-name" + String.valueOf(i), Utils.VOID_STRING));
                wbs.put("wbs-descr" + String.valueOf(i), parser.getStringParameter("wbs-descr" + String.valueOf(i), Utils.VOID_STRING));
                if (parser.getStringParameter("wbs-workpackage" + String.valueOf(i), Utils.VOID_STRING) != "") {
                    workpackage = "true";
                }
                wbs.put("wbs-workpackage" + String.valueOf(i), workpackage);
            }
            formParams.put(Query.PART_WBS, wbs);
        } 
        /* **************************************************** *
         *              Ramo di INSERT di una Wbs               *
         * **************************************************** */
        else if (part.equalsIgnoreCase(Query.ADD_TO_PROJECT)) {
            GregorianCalendar date = Utils.getUnixEpoch();
            String dateAsString = Utils.format(date, Query.DATA_SQL_PATTERN);
            HashMap<String, String> wbs = new HashMap<String, String>();
            wbs.put("wbs-idpadre",      parser.getStringParameter("wbs-idpadre", Utils.VOID_STRING));
            wbs.put("wbs-name",         parser.getStringParameter("wbs-name", Utils.VOID_STRING));
            wbs.put("wbs-descr",        parser.getStringParameter("wbs-descr", dateAsString));
            wbs.put("wbs-workpackage",  parser.getStringParameter("wbs-workpackage", Utils.VOID_STRING));
            formParams.put(Query.ADD_TO_PROJECT, wbs);
        }
    }
    
}