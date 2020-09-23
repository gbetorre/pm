/*
 *   Alma on Line: Applicazione WEB per la visualizzazione 
 *   delle schede di indagine su popolazione dell'ateneo,
 *   della gestione dei progetti on line (POL).
 *   
 *   Copyright (C) 2020 Giovanroberto Torre<br />
 *   Alma on Line (aol), Projects on Line (pol);
 *   web applications to publish, and manage, projects according to the
 *   Project Management Paradigm.
 *   Copyright (C) renewed 2020 Universita' degli Studi di Verona, 
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
import it.alma.bean.IndicatorBean;
import it.alma.bean.ItemBean;
import it.alma.bean.PersonBean;
import it.alma.bean.ProjectBean;
import it.alma.bean.WbsBean;
import it.alma.exception.AttributoNonValorizzatoException;
import it.alma.exception.CommandException;
import it.alma.exception.WebStorageException;


/** 
 * <p><code>IndicatorCommand.java</code><br />
 * Implementa la logica per la gestione degli indicatori di un progetto on line (POL).<br />
 * Gli indicatori sono importanti oggetti che permettono la valutazione della
 * realizzazione di un'<strong>azione</strong> (o progetto) relativa ad un 
 * obiettivo strategico, (nel livello di granularit&agrave; del 
 * piano delle performance corrisponde a una wbs) e in particolare 
 * consentono di misurare:<ul>
 * <li>qual &egrave; il livello attuale dell'azione (wbs) nell'indicatore</li>
 * <li>qual &egrave; il livello auspicato dell'azione (wbs) nell'indicatore</li>
 * <li>qual &egrave; il tipo di misurazione (p.es. On/Off, quantitativo, 
 * percentuale, etc.) adottato per il singolo indicatore, cio&egrave; per 
 * effettuare la specifica misurazione.</li>
 * </ul></p>
 * <p>Si ricorda che la mappatura tra i progetti di eccellenza e quelli del 
 * piano delle performance &egrave; la seguente:<hr>
 * <table>
 * <thead><tr><td><strong>Eccellenza</strong></td><td><strong>Performance</strong></td><tr></thead>
 * <tbody>
 * <tr><td>Dipartimento <small>ovvero</small> Progetto</td><td>Struttura Capofila</td>
 * <tr><td>Sottoprogetto</td><td>Obiettivo Strategico</td>
 * <tr><td>WBS</td><td>Progetto <small>ovvero</small> Azione</td>
 * <tr><td>Attivit&agrave;</td><td>Attivit&agrave;</td>
 * </table>
 * 
 * <p>Created on luned&iacute; 25 maggio 2020 17:50
 * <small>life is still beautiful</small></p>
 * 
 * @author <a href="mailto:giovanroberto.torre@univr.it">Giovanroberto Torre</a>
 */
public class IndicatorCommand extends ItemBean implements Command {
    
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
     * Numero che difficilmente sar&agrave; designato come identificatore 
     * di progetto UNIVR (esistono gli identificatori negativi, e sono tanti 
     * quanti sono quelli positivi)
     */
    public static final int VERY_UNLIKELY_ID = -1000;
    /**
     * Pagina a cui la command reindirizza per mostrare la lista delle attivit&agrave; del progetto
     */
    private static final String nomeFileElenco = "/jsp/projIndicators.jsp";
    /**
     * Pagina a cui la command reindirizza per mostrare il grafico delle attivit&agrave; del progetto
     */
    //private static final String nomeFileGrafico = "/jsp/projActivitiesGrafico.jsp";
    /**
     * Pagina a cui la command fa riferimento per mostrare la lista delle 
     * attivit&agrave; del progetto nel contesto del Project Charter
     */
    //private static final String nomeFileMilestone = "/jsp/pcMilestone.jsp";
    /**
     * Pagina a cui la command fa riferimento per permettere l'aggiunta 
     * di un nuovo indicatore al progetto, o la modifica di uno esistente
     */
    private static final String nomeFileIndicator = "/jsp/pcIndicator.jsp";
    /**
     * Pagina a cui la command fa riferimento per mostrare il report
     * degli indicatori di progetto
     */
    private static final String nomeFileReport = "/jsp/projIndicatorsReport.jsp";
    /**
     * Pagina a cui la command fa riferimento per visualizzare 
     * l'elenco degli indicatori relativi ad una WBS (o azione) del progetto
     */
    private static final String nomeFileGathering = "/jsp/pcMeasurement.jsp";
    /**
     * Struttura contenente le pagina a cui la command fa riferimento per mostrare tutti gli attributi del progetto
     */    
    private static final HashMap<String, String> nomeFile = new HashMap<String, String>();
    /**
     *  Progetto di dato id
     */
    ProjectBean runtimeProject = null;
    
    
    /** 
     * Crea una nuova istanza di ActivityCommand 
     */
    public IndicatorCommand() {
        /*;*/   // It doesn't anything, yet
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
        nomeFile.put(Query.PART_ACTIVITY, nomeFileElenco);
        nomeFile.put(Query.ADD_TO_PROJECT, nomeFileIndicator);
        nomeFile.put(Query.MODIFY_PART, nomeFileIndicator);
        nomeFile.put(Query.PART_REPORT, nomeFileReport);
        nomeFile.put(Query.MONITOR_PART, nomeFileGathering);
        //nomeFile.put(Query.TRASH_PART, nomeFileElenco);
        nomeFile.put(Query.PART_PROJECT, this.getPaginaJsp());
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
        // Dichiara la pagina a cui inoltrare
        String fileJspT = null;
        // Dichiara URL a cui reindirizzare
        String redirect = null;
        // Flag per decidere se mostrare l'header
        boolean isHeader = true;
        // Flag per decidere se mostrare il footer
        boolean isFooter = true;
        // Utente loggato
        PersonBean user = null;
        // Dichiara elenco di attività
        Vector<IndicatorBean> vIndicators = new Vector<IndicatorBean>();
        // Dichiara struttura di WBS (azioni) che possono essere misurate da un indicatore
        Vector<WbsBean> actions = null;
        // Dichiara lista di tipi di indicatori
        LinkedList<CodeBean> types = null;
        // Data di oggi sotto forma di oggetto Date
        java.util.Date today = Utils.convert(Utils.getCurrentDate());
        // Indicatore da modificare, se l'utente ha scelto questa specifica funzionalità
        IndicatorBean indicator = null;
        // Wbs a cui l'indicatore è collegato
        WbsBean wbs = null;
        /* ******************************************************************** *
         *                    Recupera parametri e attributi                    *
         * ******************************************************************** */
        // Recupera o inizializza 'id progetto'
        int idPrj = parser.getIntParameter("id", Utils.DEFAULT_ID);
        // Recupera o inizializza 'id wbs (WorkPackate) delle attività' (da mostrare)
        int idWbs = parser.getIntParameter("idw", Utils.DEFAULT_ID);
        // Recupera o inizializza 'id attività' (da modificare)
        int idInd = parser.getIntParameter("idi", Utils.DEFAULT_ID);
        // Recupera o inizializza 'tipo pagina'   
        String part = parser.getStringParameter("p", Utils.DASH);
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
                // Verifica se è presente il parametro 'p'
                if (nomeFile.containsKey(part)) {
                    // Verifica se deve eseguire un'operazione di scrittura
                    if (write) {    // Ramo delle operazioni (ramo di scrittura)
                        /* **************************************************** *
                         *    UPDATE, INSERT, an Indicator or UDPATE its state  *
                         * **************************************************** */
                        // Creazione della tabella che conterrà i valori dei parametri passati dalle form
                        HashMap<String, HashMap<String, String>> params = new HashMap<String, HashMap<String, String>>();
                        // Recupera la sessione creata e valorizzata per riferimento nella req dal metodo authenticate
                        HttpSession ses = req.getSession(Query.IF_EXISTS_DONOT_CREATE_NEW);
                        // Recupera i progetti su cui l'utente ha diritti di scrittura
                        Vector<ProjectBean> writablePrj = (Vector<ProjectBean>) ses.getAttribute("writableProjects"); // I'm confident about the types...
                        // Se non ci sono progetti scrivibili e il flag "write" è true c'è qualcosa che non va...
                        if (writablePrj == null) {
                            ses.invalidate();
                            String msg = FOR_NAME + "Il flag di scrittura e\' true pero\' non sono stati trovati progetti scrivibili: problema!.\n";
                            LOG.severe(msg);
                            throw new CommandException("Attenzione: controllare di essere autenticati nell\'applicazione!\n");
                        }
                        // Recupera dalla sessione le attività su cui l'utente ha diritti di scrittura
                        LinkedHashMap<Integer, Vector<IndicatorBean>> userWritableIndicatorsByProjectId = (LinkedHashMap<Integer, Vector<IndicatorBean>>) ses.getAttribute("writableIndicators");
                        // Trasforma un Vector di progetti scrivibili dall'utente loggato in un dictionary degli stessi
                        HashMap<Integer, ProjectBean> writableProjects = ProjectCommand.decant(writablePrj);
                        // Redirect dinamico
                        StringBuffer redirectAsStringBuffer = new StringBuffer("q=" + Query.PART_INDICATOR + "&id=" + idPrj);
                        // Controlla il tipo di operazione in scrittura che deve effettuare 
                        // (inserimento, aggiornamento, sospensione, riattivazione, eliminazione)
                        if (part.equalsIgnoreCase(Query.PART_PROJECT_CHARTER_DELIVERABLE)) {
                            /* ************************************************ *
                             *            UPDATE Multiple Activity Part         *
                             * ************************************************ */
                            //loadParams(part, parser, params);
                            //Vector<ProjectBean> userWritableProjects = db.getProjects(user.getId(), Query.GET_WRITABLE_PROJECTS_ONLY);
                            //db.updateActivityPart(idPrj, writablePrj, user.getId(), writableProjects, userWritableActivitiesByProjectId, params);
                        } else if (part.equalsIgnoreCase(Query.ADD_TO_PROJECT)) {
                            /* ************************************************ *
                             *                INSERT New indicator              *
                             * ************************************************ */
                            loadParams(part, parser, params);
                            db.insertIndicator(idPrj, user, writablePrj, params.get(Query.ADD_TO_PROJECT));
                            //if (idWbs > Utils.DEFAULT_ID) {
                            //    redirectAsStringBuffer.append("&idw=" + idWbs);
                            //}
                            redirect = String.valueOf(redirectAsStringBuffer);
                        } else if (part.equalsIgnoreCase(Query.MODIFY_PART)) {
                            if (HomePageCommand.isParameter(req, "start") && user.isPmoAteneo()) {
                                /* **************************************************** *
                                 *         Set the Indicator in an active state         *
                                 * **************************************************** */
                                db.updateIndicatorState(idPrj, idInd, Query.STATI_PROGETTO_AS_LIST.getFirst());
                                redirectAsStringBuffer = new StringBuffer("q=" + Query.PART_INDICATOR + "&p=" + Query.MODIFY_PART + "&id=" + idPrj + "&idi=" + idInd);
                            } else if (HomePageCommand.isParameter(req, "end") && user.isPmoAteneo()) {
                                /* **************************************************** *
                                 *         Set the Indicator in an inactive state       *
                                 * **************************************************** */                        
                                db.updateIndicatorState(idPrj, idInd, Query.STATI_PROGETTO_AS_LIST.get(3));
                                redirectAsStringBuffer = new StringBuffer("q=" + Query.PART_INDICATOR + "&p=" + Query.MODIFY_PART + "&id=" + idPrj + "&idi=" + idInd);
                            } else {
                                /* ************************************************ *
                                 *              UPDATE Single Indicator             *
                                 * ************************************************ */
                                loadParams(part, parser, params);
                                db.updateIndicator(idPrj, user, writablePrj, userWritableIndicatorsByProjectId, params.get(Query.MODIFY_PART));
                                if (idWbs > Utils.DEFAULT_ID) {
                                    redirectAsStringBuffer.append("&idw=" + idWbs);
                                }
                            }
                            redirect = String.valueOf(redirectAsStringBuffer);
                        } else if (part.equals(Query.MONITOR_PART)) {
                            /* ************************************************ *
                             *                INSERT New Gathering              *
                             * ************************************************ */
                            loadParams(part, parser, params);
                            db.insertMeasurement(idPrj, user, writablePrj, params.get(Query.MONITOR_PART));
                            //if (idWbs > Utils.DEFAULT_ID) {
                            //    redirectAsStringBuffer.append("&idw=" + idWbs);
                            //}
                            redirect = String.valueOf(redirectAsStringBuffer);
                        }
                        /* ************************************************ *
                         *  Aggiorna gli indicatori dell'utente in sessione *
                         * ************************************************ */
                        // Rifà la query
                        Vector<IndicatorBean> userWritableIndicatorsByCurrentPrj = db.getIndicators(idPrj, user);
                        // Aggiorna la HashMap delle attività indicizzate per identificativo di progetto (wrapped)
                        userWritableIndicatorsByProjectId.put(new Integer(idPrj), userWritableIndicatorsByCurrentPrj);
                        // Aggiorna la sessione
                        ses.removeAttribute("writableIndicators");
                        ses.setAttribute("writableIndicators", userWritableIndicatorsByProjectId);
                    } else {    // Ramo delle selezioni (ramo di lettura) <==
                        /* **************************************************** *
                         *                 SELECT Indicator/s                   d*
                         * **************************************************** */
                        if (part.equals(Query.PART_PROJECT_CHARTER_DELIVERABLE)) {
                            /* ************************************************ *
                             *        Recupera gli indicatori di progetto       *
                             * ************************************************ */
                            vIndicators = db.getIndicators(idPrj, user, Utils.convert(Utils.getUnixEpoch()), Query.GET_ALL_BY_CLAUSE, Query.GET_ALL_BY_CLAUSE, !Query.GET_ALL);
                        } else if (part.equalsIgnoreCase(Query.PART_REPORT)) {
                            /* ************************************************ *
                             *   Recupera gli indicatori di wbs a fini report   *
                             * ************************************************ */
                            vIndicators = retrieveIndicators(db, idPrj, user, Utils.convert(Utils.getUnixEpoch()), Query.GET_ALL_BY_CLAUSE, Query.GET_ALL_BY_CLAUSE, Query.GET_ALL);
                        } else if (part.equals(Query.ADD_TO_PROJECT)) {
                            /* ************************************************ *
                             *        Effettua le selezioni che servono         * 
                             *      all'inserimento di un nuovo indicatore      *
                             * ************************************************ */
                            types = db.getIndicatorTypes(Query.GET_ALL_BY_CLAUSE, Query.GET_ALL_BY_CLAUSE);
                            actions = db.getWbs(runtimeProject.getId(), user, Query.WBS_GET_ALL);

                            //complexity = HomePageCommand.getComplessita();
                            //states = HomePageCommand.getStatiAttivita();
                            //d = db.getDeparts();
                        } else if (part.equals(Query.MODIFY_PART)) {
                            /* ************************************************ *
                             *        Effettua le selezioni che servono         * 
                             *      all'aggiornamento di un dato indicatore     *
                             * ************************************************ */
                            // Se siamo qui (p=mod) e l'id dell'indicatore non è significativo c'è qualcosa che non va...
                            if (idInd == Utils.DEFAULT_ID) {
                                HttpSession ses = req.getSession(Query.IF_EXISTS_DONOT_CREATE_NEW);
                                ses.invalidate();
                                String msg = FOR_NAME + "Qualcuno ha tentato di inserire un indirizzo nel browser avente un id attivita\' non valida!.\n";
                                LOG.severe(msg + "E\' presente il parametro \'p=mod\' ma non un valore \'ida\' - cioe\' id attivita\' - significativo!\n");
                                throw new CommandException("Attenzione: indirizzo richiesto non valido!\n");
                            }
                            types = db.getIndicatorTypes(Query.GET_ALL_BY_CLAUSE, Query.GET_ALL_BY_CLAUSE);
                            actions = db.getWbs(runtimeProject.getId(), user, Query.WBS_WP_ONLY);
                            indicator = db.getIndicator(idPrj, idInd, user);
                        } else if (part.equals(Query.MONITOR_PART)) {
                            /* ************************************************ *
                             *        Effettua le selezioni che servono         * 
                             *      all'aggiornamento di un dato indicatore     *
                             * ************************************************ */
                            // Se siamo qui (p=mod) e l'id dell'indicatore non è significativo c'è qualcosa che non va...
                            if (idInd == Utils.DEFAULT_ID) {
                                HttpSession ses = req.getSession(Query.IF_EXISTS_DONOT_CREATE_NEW);
                                ses.invalidate();
                                String msg = FOR_NAME + "Qualcuno ha tentato di inserire un indirizzo nel browser avente un id attivita\' non valida!.\n";
                                LOG.severe(msg + "E\' presente il parametro \'p=mod\' ma non un valore \'ida\' - cioe\' id attivita\' - significativo!\n");
                                throw new CommandException("Attenzione: indirizzo richiesto non valido!\n");
                            }
                            //types = db.getIndicatorTypes(Query.GET_ALL_BY_CLAUSE, Query.GET_ALL_BY_CLAUSE);
                            //actions = db.getWbs(runtimeProject.getId(), user, Query.WBS_WP_ONLY);
                            indicator = db.getIndicator(idPrj, idInd, user);
                        } 
                        fileJspT = nomeFile.get(part);
                    }
                } else {
                    // Se il parametro 'p' non è presente, controlla se c'è il parametro 'idw', ovvero "id wbs"
                    if (idWbs > Utils.DEFAULT_ID) {
                        // Se c'è idWBS deve recuperare gli indicatori di quella wbs
                        //vIndicators = db.getIndicatorsByWbs(idWbs, idPrj, user);
                        // Recupera anche la wbs stessa a fini etichette etc.
                        WbsBean wP = db.getWbsInstance(idPrj, idWbs, user);
                        //workPackage = new Vector<WbsBean>(1);
                        //workPackage.add(wP);
                        //fileJspT = nomeFileIndicatorByWbs;
                    } else {
                        // Se il parametro 'p' non è presente, e il parametro 'idw' nemmeno, deve solo mostrare l'elenco degli indicatori per quel progetto
                        vIndicators = db.getIndicators(idPrj, user, Utils.convert(Utils.getUnixEpoch()), Query.GET_ALL_BY_CLAUSE, Query.GET_ALL_BY_CLAUSE, !Query.GET_ALL);
                        fileJspT = nomeFileElenco;
                    }
                }
            } else {
                // Se siamo qui vuol dire che l'id del progetto non è > zero, il che è un guaio
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
            String msg = FOR_NAME + "Si e\' verificato un problema nel codice che effettua il recupero di valori dal db, a livello di SQL o nel calcolo di valori.\n";
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
        // Imposta nella request elenco attivita del progetto
        req.setAttribute("indicatori", vIndicators);
        // Imposta nella request data di oggi 
        req.setAttribute("now", today);
        // Imposta la Pagina JSP di forwarding
        req.setAttribute("fileJsp", fileJspT);
        /* ******************************************************************** *
         * Settaggi in request di valori facoltativi: attenzione, il passaggio  *
         * di questi attributi e' condizionato al fatto che siano significativi *
         * ******************************************************************** */
        if (actions != null) {
            // Imposta nella request tutte le wbs del progetto corrente
            req.setAttribute("azioni", actions);
        }
        if (types != null) {
            // Imposta nella request elenco tipi associabili
            req.setAttribute("tipi", types);
        }/*
        if (vWbsAncestors != null) {
            // Imposta nella request la gerarchia delle wbs + attività
            req.setAttribute("wbsHierarchy", vWbsAncestors);
        }
        if (complexity != null) {
            // Imposta nella request elenco wbs associabili
            req.setAttribute("complessita", complexity);
        }
        if (states != null) {
            // Imposta nella request elenco wbs associabili
            req.setAttribute("statiAttivita", states);
        }*/
        if (indicator != null) {
            // Indicatore che l'utente vul visualizzare nei dettagli, e/o modificare
            req.setAttribute("indicatore", indicator);
            /*if (wbs != null) {
                // WorkPackage o gerarchia di WBS da mostrare
                req.setAttribute("w", wbs);
            }*/
        }
        if (redirect != null) {
            req.setAttribute("redirect", redirect);
        }
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
         *          Ramo di Project Charter - Milestone         *
         * **************************************************** */
        if (part.equalsIgnoreCase(Query.PART_PROJECT_CHARTER_MILESTONE)) {
            // Recupero e caricamento parametri di project charter/milestone
            int totActivities = Integer.parseInt(parser.getStringParameter("pcm-loop-status", Utils.VOID_STRING));
            HashMap<String, String> pcm = new HashMap<String, String>();
            for (int i = 0; i <= totActivities; i++) {
                String milestone = "false";
                pcm.put("pcm-id" + String.valueOf(i), parser.getStringParameter("pcm-id" + String.valueOf(i), Utils.VOID_STRING));
                pcm.put("pcm-nome" + String.valueOf(i), parser.getStringParameter("pcm-nome" + String.valueOf(i), Utils.VOID_STRING));
                pcm.put("pcm-descrizione" + String.valueOf(i), parser.getStringParameter("pcm-descrizione" + String.valueOf(i), Utils.VOID_STRING));
                if((parser.getStringParameter("pcm-milestone" + String.valueOf(i), Utils.VOID_STRING)) != "") {
                    milestone = "true";
                }
                pcm.put("pcm-milestone" + String.valueOf(i), milestone);
            }
            formParams.put(Query.PART_PROJECT_CHARTER_MILESTONE, pcm);
        } 
        /* **************************************************** *
         *       Ramo di INSERT / UPDATE di un Indicatore       *
         * **************************************************** */
        else if (part.equalsIgnoreCase(Query.ADD_TO_PROJECT) || part.equalsIgnoreCase(Query.MODIFY_PART)) {
            GregorianCalendar date = Utils.getUnixEpoch();
            String dateAsString = Utils.format(date, Query.DATA_SQL_PATTERN);
            HashMap<String, String> ind = new HashMap<String, String>();
            ind.put("ind-id",           parser.getStringParameter("ind-id", Utils.VOID_STRING));
            ind.put("ind-nome",         parser.getStringParameter("ind-nome", Utils.VOID_STRING));
            ind.put("ind-descr",        parser.getStringParameter("ind-descr", Utils.VOID_STRING));
            ind.put("ind-baseline",     parser.getStringParameter("ind-baseline", Utils.VOID_STRING));
            ind.put("ind-database",     parser.getStringParameter("ind-database", dateAsString));
            ind.put("ind-annobase",     parser.getStringParameter("ind-annobase",  Utils.VOID_STRING));
            ind.put("ind-target",       parser.getStringParameter("ind-target", Utils.VOID_STRING));
            ind.put("ind-datatarget",   parser.getStringParameter("ind-datatarget", dateAsString));
            ind.put("ind-annotarget",   parser.getStringParameter("ind-annotarget",  Utils.VOID_STRING));
            ind.put("ind-tipo",         parser.getStringParameter("ind-tipo", Utils.VOID_STRING));
            ind.put("ind-wbs",          parser.getStringParameter("ind-wbs", Utils.VOID_STRING));
            formParams.put(Query.ADD_TO_PROJECT, ind);
            formParams.put(Query.MODIFY_PART, ind);
        }
        /* **************************************************** *
         *  Ramo di INSERT di una misurazione su un Indicatore  *
         * **************************************************** */
        else if (part.equalsIgnoreCase(Query.MONITOR_PART)) {
            GregorianCalendar date = Utils.getUnixEpoch();
            String dateAsString = Utils.format(date, Query.DATA_SQL_PATTERN);
            HashMap<String, String> ind = new HashMap<String, String>();
            ind.put("ind-id",           parser.getStringParameter("ind-id", Utils.VOID_STRING));
            ind.put("prj-id",           parser.getStringParameter("prj-id", Utils.VOID_STRING));
            ind.put("mon-nome",         parser.getStringParameter("mon-nome", Utils.VOID_STRING));
            ind.put("mon-descr",        parser.getStringParameter("mon-note", Utils.VOID_STRING));
            ind.put("mon-data",         parser.getStringParameter("mon-data", dateAsString));
            ind.put("mon-milestone",    parser.getStringParameter("mon-milestone", Utils.VOID_STRING));
            formParams.put(Query.MONITOR_PART, ind);
        }
    }


    /**
     * <p>Restituisce un Vector di tutti gli indicatori appartenenti al progetto
     * il cui identificativo viene passato come argomento; ciascuno degli
     * indicatori contiene al proprio interno la lista di misurazioni 
     * valorizzate, ciascuna a sua volta contenente i propri attributi.</p>
     * 
     * @param idPrj identificativo del progetto corrente
     * @param db    WebStorage per l'accesso ai dati
     * @param user  utente loggato; viene passato ai metodi del DBWrapper per controllare che abbia i diritti di fare quello che vuol fare
     * @param from  data baseline a partire dalla quale cercare gli indicatori. Se non interessa, passare una data molto antica (tipo UNIX_EPOCH)
     * @param typeId tipo specifico di indicatore che si vuol recuperare; per recuperare tutti i tipi, v. argomento successivo
     * @param getAll se si vogliono recuperare gli indicatori di tutti i tipi bisogna passare -1 sia sul parametro precedente che su questo; altrimenti bisogna passare l'id tipo su entrambi
     * @param measuresToo flag specificante se bisogna recuperare anche le misurazioni di ogni indicatore (true) o non interessa (false)
     * @return <code>Vector&lt;WbsBean&gt;</code> - lista di work packages recuperati 
     * @throws CommandException se si verifica un problema nell'estrazione dei dati, o in qualche tipo di puntamento
     */
    public static Vector<IndicatorBean> retrieveIndicators(DBWrapper db,
                                                           int idPrj,
                                                           PersonBean user,
                                                           Date from,
                                                           int typeId,
                                                           int getAll,
                                                           boolean measuresToo)
                                                    throws CommandException {
        Vector<IndicatorBean> vIndicators = new Vector<IndicatorBean>();
        try {
            // Fa la stessa query usata nella execute()
            vIndicators = db.getIndicators(idPrj, user, from, typeId, getAll, measuresToo);
        } catch (WebStorageException wse) {
            String msg = FOR_NAME + "Si e\' verificato un problema nel recupero di work packages.\n";
            LOG.severe(msg);
            throw new CommandException(msg + wse.getMessage(), wse);
        } catch (NullPointerException npe) {
            String msg = FOR_NAME + "Si e\' verificato un problema di puntamento a null.\n Attenzione: controllare di essere autenticati nell\'applicazione!\n";
            LOG.severe(msg);
            throw new CommandException(msg + npe.getMessage(), npe);
        } catch (Exception e) {
            String msg = FOR_NAME + "Si e\' verificato un problema.\n";
            LOG.severe(msg);
            throw new CommandException(msg + e.getMessage(), e);
        }
        return vIndicators;
    }

}