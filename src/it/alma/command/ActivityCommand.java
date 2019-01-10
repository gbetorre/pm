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
 * <p><code>ActivityCommand.java</code><br />
 * Implementa la logica per la gestione delle attività di un progetto on line (POL).</p>
 * 
 * <p>Created on martedì 27 novembre 2018 12:56</p>
 * 
 * @author <a href="mailto:giovanroberto.torre@univr.it">Giovanroberto Torre</a>
 */
public class ActivityCommand extends ItemBean implements Command {
    
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
     * Pagina a cui la command reindirizza per mostrare la lista delle attivit&agrave; del progetto
     */
    private static final String nomeFileElenco = "/jsp/projActivities.jsp";
    /**
     * Pagina a cui la command fa riferimento per mostrare la lista delle 
     * attivit&agrave; del progetto nel contesto del Project Charter
     */
    private static final String nomeFileMilestone = "/jsp/pcMilestone.jsp";
    /**
     * Pagina a cui la command fa riferimento per permettere l'aggiunta 
     * di una nuova attivit&agrave; al progetto
     */
    private static final String nomeFileActivity = "/jsp/pcAttivita.jsp";
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
    public ActivityCommand() {
        /*;*/   // It doesn't anything
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
        nomeFile.put(Query.PART_PROJECT_CHARTER_MILESTONE, nomeFileMilestone);
        nomeFile.put(Query.ADD_ACTIVITY_TO_PROJECT, nomeFileActivity);
        nomeFile.put(Query.MODIFY_PART, nomeFileActivity);
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
        // Dichiara elenco di attività
        Vector<ActivityBean> vActivities = new Vector<ActivityBean>();
        // Dichiara struttura di persone che possono essere aggiunte a un'attività
        Vector<PersonBean> candidates = null;
        // Dichiara struttura di Work Package cui può essere aggiunta un'attività
        Vector<WbsBean> workPackage = null;
        // Dichiara lista di valori di complessità
        LinkedList<CodeBean> complexity = null;
        // Dichiara lista di valori di stati attività
        LinkedList<CodeBean> states = null;
        // Data di oggi sotto forma di oggetto String
        String today = null;
        // Attività da modificare, se l'utente ha scelto questa specifica funzionalità
        ActivityBean activity = null;
        /* ******************************************************************** *
         *                    Recupera parametri e attributi                    *
         * ******************************************************************** */
        // Recupera o inizializza 'id progetto'
        int idPrj = parser.getIntParameter("id", Utils.DEFAULT_ID);
        // Recupera o inizializza 'id attività' (da modificare)
        int idAct = parser.getIntParameter("ida", Utils.DEFAULT_ID);
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
                // Verifica se è presente il parametro 'p'
                if (nomeFile.containsKey(part)) {
                    // Verifica se deve eseguire un'operazione di scrittura
                    if (write) {
                        // Creazione della tabella che conterrà i valori dei parametri passati dalle form
                        HashMap<String, HashMap<String, String>> params = new HashMap<String, HashMap<String, String>>();
                        // Recupera la sessione creata e valorizzata per riferimento nella req dal metodo authenticate
                        HttpSession ses = req.getSession(Query.IF_EXISTS_DONOT_CREATE_NEW);
                        // Recupera i progetti su cui l'utente ha diritti di scrittura
                        Vector<ProjectBean> writablePrj = (Vector<ProjectBean>) ses.getAttribute("writableProjects"); // I'm confident about the types...
                        // Se non ci sono progetti scrivibili e il flag "write" è true c'è qualcosa che non va...
                        if (writablePrj == null) {
                            String msg = FOR_NAME + "Il flag di scrittura e\' true pero\' non sono stati trovati progetti scrivibili: problema!.\n";
                            LOG.severe(msg);
                            throw new CommandException("Attenzione: controllare di essere autenticati nell\'applicazione!\n");
                        }
                        // Recupera dalla sessione le attività su cui l'utente ha diritti di scrittura
                        LinkedHashMap<Integer, Vector<ActivityBean>> userWritableActivitiesByProjectId = (LinkedHashMap<Integer, Vector<ActivityBean>>) ses.getAttribute("writableActivity");
                        // Trasforma un Vector di progetti scrivibili dall'utente loggato in un dictionary degli stessi
                        HashMap<Integer, ProjectBean> writableProjects = ProjectCommand.decant(writablePrj);
                        // Controlla se deve effettuare un inserimento o un aggiornamento
                        if (part.equalsIgnoreCase(Query.PART_PROJECT_CHARTER_MILESTONE)) {
                            /* ************************************************ *
                             *                UPDATE Activity Part              *
                             * ************************************************ */
                            loadParams(part, parser, params);
                            //Vector<ProjectBean> userWritableProjects = db.getProjects(user.getId(), Query.GET_WRITABLE_PROJECTS_ONLY);
                            db.updateActivityPart(idPrj, user.getId(), writableProjects, userWritableActivitiesByProjectId, params);
                        } else if (part.equalsIgnoreCase(Query.ADD_ACTIVITY_TO_PROJECT)) {
                            /* ************************************************ *
                             *                INSERT Activity Part              *
                             * ************************************************ */
                            loadParams(part, parser, params);
                            isHeader = isFooter = false;
                            db.insertActivity(idPrj, user, writablePrj, params.get(Query.ADD_ACTIVITY_TO_PROJECT));
                        } else if (part.equalsIgnoreCase(Query.MODIFY_PART)) {
                            /* ************************************************ *
                             *             UPDATE Which One Activity            *
                             * ************************************************ */
                            loadParams(part, parser, params);
                            //Vector<ProjectBean> userWritableProjects = db.getProjects(user.getId(), Query.GET_WRITABLE_PROJECTS_ONLY);
                            db.updateActivity(idPrj, user, writableProjects, userWritableActivitiesByProjectId, params);
                        }

                        // Aggiorna i progetti, le attività dell'utente in sessione
                        //ses.removeAttribute("writableProjects");
                        ses.removeAttribute("writableActivity");
                        //ses.setAttribute("writableProjects", userWritableProjects);
                        ses.setAttribute("writableActivity", userWritableActivitiesByProjectId);
                    }
                    /* **************************************************** *
                     *                 SELECT Activity Part                 *
                     * **************************************************** */
                    if (part.equals(Query.PART_PROJECT_CHARTER_MILESTONE)) {
                        // Recupera le Milestones
                        vActivities = db.getActivities(idPrj);
                        //TODO: CAMBIARE IL METODO CON:  db.getActivities(idPrj, user, ONLY_MILESTONES);
                    } else if (part.equals(Query.ADD_ACTIVITY_TO_PROJECT)) {
                        // Effettua le selezioni che servono all'inserimento di una nuova attività
                        isHeader = isFooter = false;
                        candidates = db.getPeople(runtimeProject.getId());
                        workPackage = db.getWbs(runtimeProject.getId(), Query.GET_WORK_PACKAGES_ONLY); 
                        complexity = HomePageCommand.getComplessita();
                        states = HomePageCommand.getStatiAttivita();
                        today = Utils.format(Utils.getCurrentDate());
                    } else if (part.equals(Query.MODIFY_PART)) {
                        // Se siamo qui (p=mod) e l'id dell'attività non è significativo c'è qualcosa che non va...
                        if (idAct == Utils.DEFAULT_ID) {
                            HttpSession ses = req.getSession(Query.IF_EXISTS_DONOT_CREATE_NEW);
                            ses.invalidate();
                            String msg = FOR_NAME + "Qualcuno ha tentato di inserire un indirizzo nel browser avente un id attivita\' non valida!.\n";
                            LOG.severe(msg + "E\' presente il parametro \'p=mod\' ma non un valore \'ida\' - cioe\' id attivita\' - significativo!\n");
                            throw new CommandException("Attenzione: indirizzo richiesto non valido!\n");
                        }
                        // Effettua le selezioni che servono all'aggiornamento di una data attività
                        isHeader = isFooter = false;
                        activity = db.getActivity(idPrj, idAct, user);
                        candidates = db.getPeople(runtimeProject.getId());
                        workPackage = db.getWbs(runtimeProject.getId(), Query.GET_WORK_PACKAGES_ONLY);
                        complexity = HomePageCommand.getComplessita();
                        states = HomePageCommand.getStatiAttivita();
                        today = Utils.format(Utils.getCurrentDate());
                    }
                    fileJspT = nomeFile.get(part);
                } else {
                    // Se il parametro 'p' non è presente, deve solo mostrare l'elenco delle attività 
                    vActivities = db.getActivities(idPrj);
                    fileJspT = nomeFileElenco;
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
        // Imposta nella request elenco attivita del progetto
        req.setAttribute("attivita", vActivities);
        // Imposta nella request data di oggi 
        req.setAttribute("now", today);
        // Imposta la Pagina JSP di forwarding
        req.setAttribute("fileJsp", fileJspT);
        /* ******************************************************************** *
         * Settaggi in request di valori facoltativi: attenzione, il passaggio  *
         * di questi attributi e' condizionato al fatto che siano significativi *
         * ******************************************************************** */
        if (candidates != null) {
            // Imposta nella request elenco persone del dipartimento associabili a una attività
            req.setAttribute("people", candidates);
        }
        if (workPackage != null) {
            // Imposta nella request elenco wbs associabili
            req.setAttribute("wbs", workPackage);
        }
        if (complexity != null) {
            // Imposta nella request elenco wbs associabili
            req.setAttribute("complessita", complexity);
        }
        if (states != null) {
            // Imposta nella request elenco wbs associabili
            req.setAttribute("statiAttivita", states);
        }
        if (activity != null) {
            // Attività che l'utente vul visualizzare nei dettagli, e/o modificare
            req.setAttribute("singolaAttivita", activity);
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
         *           Ramo di INSERT di una Attivita'            *
         * **************************************************** */
        else if (part.equalsIgnoreCase(Query.ADD_ACTIVITY_TO_PROJECT)) {
            GregorianCalendar date = Utils.getUnixEpoch();
            String dateAsString = Utils.format(date, Query.DATA_SQL_PATTERN);
            HashMap<String, String> act = new HashMap<String, String>();
            act.put("act-name",         parser.getStringParameter("act-name", Utils.VOID_STRING));
            act.put("act-descr",        parser.getStringParameter("act-descr", null));
            act.put("act-datainizio",       parser.getStringParameter("act-datainizio", dateAsString));
            act.put("act-datafine",         parser.getStringParameter("act-datafine", dateAsString));
            act.put("act-datainiziovera",   parser.getStringParameter("act-datainiziovera", null));
            act.put("act-datafinevera",     parser.getStringParameter("act-datafinevera", null));
            act.put("act-guprevisti",   parser.getStringParameter("act-guprevisti", Utils.VOID_STRING));
            act.put("act-gueffettivi",  parser.getStringParameter("act-gueffettivi", Utils.VOID_STRING));
            act.put("act-gurimanenti",  parser.getStringParameter("act-gurimanenti", Utils.VOID_STRING));
            act.put("act-progress",     parser.getStringParameter("act-progress", null));
            act.put("act-people",       parser.getStringParameter("act-people", Utils.VOID_STRING));
            act.put("act-role",         parser.getStringParameter("act-role", Utils.VOID_STRING));
            act.put("act-milestone",    parser.getStringParameter("act-milestone", Utils.VOID_STRING));
            act.put("act-wbs",          parser.getStringParameter("act-wbs", Utils.VOID_STRING));
            act.put("act-compl",        parser.getStringParameter("act-compl", Utils.VOID_STRING));
            act.put("act-status",       parser.getStringParameter("act-status", Utils.VOID_STRING));
            formParams.put(Query.ADD_ACTIVITY_TO_PROJECT, act);
        }
    }
    
}