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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
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
import it.alma.bean.DepartmentBean;
import it.alma.bean.ItemBean;
import it.alma.bean.PersonBean;
import it.alma.bean.ProjectBean;
import it.alma.bean.RiskBean;
import it.alma.bean.SkillBean;
import it.alma.bean.StatusBean;
import it.alma.bean.WbsBean;
import it.alma.exception.AttributoNonValorizzatoException;
import it.alma.exception.CommandException;
import it.alma.exception.NotFoundException;
import it.alma.exception.WebStorageException;


/** 
 * <p><code>ProjectCommand.java</code><br />
 * Implementa la logica per la gestione dei progetti on line (POL).</p>
 * 
 * <p>Created on mercoledì 11 ottobre 2018 15:13</p>
 * 
 * @author <a href="mailto:giovanroberto.torre@univr.it">Giovanroberto Torre</a>
 */
public class ProjectCommand extends ItemBean implements Command {
    
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
     * Pagina a cui la command reindirizza per mostrare la lista dei progetti che l'utente ha i diritti di visualizzare
     */
    private static final String nomeFileElenco = "/jsp/elencoProgetti.jsp";
    /**
     * Pagina a cui la command fa riferimento per mostrare il report
     */
    private static final String nomeFileReport = "/jsp/projReport.jsp";
    /**
     * Pagina a cui la command fa riferimento per mostrare il timelines dei workpackage
     */
    private static final String nomeFileTimelines = "/jsp/wbsTimelines.jsp";
    /**
     * Struttura contenente le pagina a cui la command fa riferimento per mostrare tutti gli attributi del progetto
     */    
    private static final HashMap<String, String> nomeFile = new HashMap<String, String>();
    /**
     *  Progetto di dato id
     */
    ProjectBean runtimeProject = null;
    
    
    /** 
     * Crea una nuova istanza di ProjectCommand 
     */
    public ProjectCommand() {
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
        // Carica la hashmap contenente le pagine da includere in funzione dei parametri sulla querystring
        nomeFile.put(Query.PART_PROJECT_CHARTER_VISION, "/jsp/pcVision.jsp");
        nomeFile.put(Query.PART_PROJECT_CHARTER_STAKEHOLDER, "/jsp/pcStakeholder.jsp");
        nomeFile.put(Query.PART_PROJECT_CHARTER_DELIVERABLE, "/jsp/pcDeliverable.jsp");
        nomeFile.put(Query.PART_PROJECT_CHARTER_RESOURCE, "/jsp/pcRisorse.jsp");
        nomeFile.put(Query.PART_PROJECT_CHARTER_CONSTRAINT, "/jsp/pcVincoli.jsp");
        nomeFile.put(Query.PART_STATUS, "/jsp/projStatus.jsp");
        nomeFile.put(Query.CREDITS, "/jsp/credits.jsp");
        nomeFile.put(Query.PART_REPORT, nomeFileReport);
        nomeFile.put(Query.PART_TIMELINES, nomeFileTimelines);
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
         *           Crea e inizializza le variabili locali comuni              *
         * ******************************************************************** */
        /*JOptionPane.showMessageDialog(null, "Chiamata execute arrivata dall\'applicazione!", "Main: esito OK", JOptionPane.INFORMATION_MESSAGE, null);*/
        // Databound
        DBWrapper db = null;
        // Parser per la gestione assistita dei parametri di input
        ParameterParser parser = new ParameterParser(req);
        // Utente loggato
        PersonBean user = null;
        // Recupera o inizializza 'id progetto'
        int idPrj = parser.getIntParameter("id", -1);
        // Recupera o inizializza 'id stato progetto'
        int idStatus = parser.getIntParameter("ids", -1);
        // Recupera o inizializza 'tipo pagina'   
        String part = parser.getStringParameter("p", "-");
        // Recupera o inizializza 'data visualizzazione report'
        String selectionDate = parser.getStringParameter("d", Utils.format(Utils.getCurrentDate()));
        // Flag di scrittura
        boolean write = (boolean) req.getAttribute("w");
        // Dichiara la pagina a cui reindirizzare
        String fileJspT = null;
        // Dichiara elenco di progetti
        ConcurrentHashMap<Integer, Vector<ProjectBean>> m = new ConcurrentHashMap<Integer, Vector<ProjectBean>>();
        // Dichiara mappa di dipartimenti indicizzata per id
        HashMap<Integer, DepartmentBean> d = null;
        // Dichiara lista di valori di stati avanzamento
        LinkedList<CodeBean> statiValues = null;
        // Dichiara elenco di competenze
        Vector<SkillBean> vSkills = new Vector<SkillBean>();
        // Dichiara elenco di rischi
        Vector<RiskBean> vRisks = new Vector<RiskBean>();
        // Dichiara l'elenco degli status di un progetto
        ArrayList<StatusBean> projectStatusList = new ArrayList<StatusBean>();
        // Dichiara l'avanzamento progetto più recente
        StatusBean projectStatus = null;
        // Dichiara l'elenco delle attività presenti in un range di date
        Vector<ActivityBean> activitiesByRange = null;
        // Dichiara l'elenco delle attività con data inizio compresa tra due date
        Vector<ActivityBean> activitiesByDate = null;
        // Dichiara l'avanzamento progetto successivo a quello di partenza
        StatusBean nextStatus = null;
        // Dichiara l'elenco di work packages del progetto 
        Vector<WbsBean> workPackagesOfProj = null;
        // Dichiara la data del report da visualizzare
        Date convertDate = null;
        // Dichiara l'id massimo della tabella avanzamentoprogetto
        int newStatusId = -1;
        // Flag specificante se l'utente vede almeno un progetto
        boolean checkThisOut = false;
        /* ******************************************************************** *
         *      Instanzia nuova classe WebStorage per il recupero dei dati      *
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
            if (nomeFile.containsKey(part)) {
                if (idPrj > 0) {
                    statiValues = HomePageCommand.getStatiAvanzamento();
                    if (write) {
                        // Creazione della tabella valori parametri
                        HashMap<String, HashMap<String, String>> params = new HashMap<String, HashMap<String, String>>();
                        loadParams(part, parser, params);
                        try {
                            // Recupera la sessione creata e valorizzata per riferimento nella req dal metodo authenticate
                            HttpSession ses = req.getSession(Query.IF_EXISTS_DONOT_CREATE_NEW);
                            Vector<ProjectBean> writablePrj = (Vector<ProjectBean>) ses.getAttribute("writableProjects");
                            if (writablePrj == null) {
                                throw new CommandException("Attenzione: controllare di essere autenticati nell\'applicazione!\n");
                            }
                            HashMap<Integer, ProjectBean> writableProjects = decant(writablePrj);
                            LinkedHashMap<Integer, Vector> userWritableActivitiesByProjectId =  (LinkedHashMap<Integer, Vector>) ses.getAttribute("writableActivity");
                            LinkedHashMap<Integer, Vector> userWritableSkillsByProjectId =  (LinkedHashMap<Integer, Vector>) ses.getAttribute("writableSkills");
                            LinkedHashMap<Integer, Vector> userWritableRisksByProjectId =  (LinkedHashMap<Integer, Vector>) ses.getAttribute("writableRisks");
                            LinkedHashMap<String, HashMap<Integer, Vector>> objectsMap = decant(userWritableActivitiesByProjectId, userWritableSkillsByProjectId, userWritableRisksByProjectId);
                            if (req.getParameterMap().containsKey("sts-add")) {
                                /* **************************************************** *
                                 *                 INSERT Project Status                *
                                 * **************************************************** */
                                db.insertStatus(user, idPrj, idStatus);
                            } else {
                                /* **************************************************** *
                                 *                  UPDATE Project Part                 *
                                 * **************************************************** */
                                db.updateProjectPart(idPrj, writablePrj, user.getId(), writableProjects, objectsMap, params);
                                Vector<ProjectBean> userWritableProjects = db.getProjects(user.getId(), Query.GET_WRITABLE_PROJECTS_ONLY);
                                // Aggiorna i progetti, le attivita dell'utente in sessione
                                ses.removeAttribute("writableProjects");
                                ses.removeAttribute("writableActivity");
                                ses.setAttribute("writableProjects", userWritableProjects);
                                ses.setAttribute("writableActivity", userWritableActivitiesByProjectId);
                            }
                        } catch (AttributoNonValorizzatoException anve) {
                            String msg = FOR_NAME + "Impossibile recuperare un attributo obbligatorio, probabilmente l\'id dell\'utente.\n";
                            LOG.severe(msg);
                            throw new CommandException(msg + anve.getMessage(), anve);
                        } catch (IllegalStateException ise) {
                            String msg = FOR_NAME + "Impossibile redirigere l'output. Verificare se la risposta e\' stata gia\' committata.\n";
                            LOG.severe(msg);
                            throw new CommandException(msg + ise.getMessage(), ise);
                        } catch (ClassCastException cce) {
                            String msg = FOR_NAME + ": Si e\' verificato un problema in una conversione di tipo.\n";
                            LOG.severe(msg);
                            throw new CommandException(msg + cce.getMessage(), cce);
                        } catch (NullPointerException npe) {
                            String msg = FOR_NAME + "Si e\' verificato un problema di puntamento a null, probabilmente nel tentativo di recuperare i progetti dell\'utente.\n";
                            LOG.severe(msg);
                            throw new CommandException("Attenzione: controllare di essere autenticati nell\'applicazione!\n" + npe.getMessage(), npe);
                        } catch (Exception e) {
                            String msg = FOR_NAME + "Si e\' verificato un problema.\n";
                            LOG.severe(msg);
                            throw new CommandException(msg + e.getMessage(), e);
                        }
                    }
                    /* **************************************************** *
                     *                  SELECT Project Part                 *
                     * **************************************************** */
                    // Recupera il progetto richiesto dalla navigazione utente
                    runtimeProject = db.getProject(idPrj, user.getId());
                    newStatusId = db.getMax("avanzamentoprogetto") + 1;
                    // Recupera tutta la lista degli Status
                    projectStatusList = db.getStatusList(idPrj, user);
                    // Per ottimizzare il caricamento di dati nella request separa i rami della richiesta
                    if (part.equals(Query.PART_PROJECT)) {
                        ; // Codice per mostrare dati aggregati sul progetto, o relazioni o altro
                    } else if (part.equals(Query.PART_STATUS)) {
                        /* ************************************************************ *
                         *      Implementazione logica per recupero degli status        *
                         *      sia nel caso l'id sia uguale a '0' sia nel caso         * 
                         *      mi venga specificato uno status tramite id              *        
                         * ************************************************************ */
                        Date rightNow = Utils.convert(Utils.getCurrentDate());
                        // Testa se l'id dello status è significativo
                        if (idStatus > Query.NOTHING) {
                            // Recupera uno specifico status di progetto di dato id
                            projectStatus = db.getStatus(idPrj, idStatus, user);
                        } else if (idStatus == Query.NOTHING) { // Valore fittizio (nessuno status può avere id = 0!)
                            // Recupera l'ultimo Status, cioè quello avente datainizio più prossima alla data odierna
                            Date dateProjectStatus = new Date(0);
                            if (!projectStatusList.isEmpty()) {
                                for (int i = 0; i < projectStatusList.size(); i++) {
                                    if (projectStatusList.get(i).getDataInizio().equals(rightNow) ||
                                        projectStatusList.get(i).getDataInizio().before(rightNow) ) {
                                        if (dateProjectStatus.before(projectStatusList.get(i).getDataInizio())) {
                                            dateProjectStatus = projectStatusList.get(i).getDataInizio();
                                        }
                                    } else {
                                        break;
                                    }
                                }
                            }
                            // Recupera uno specifico status di progetto a partire dalla sua data - assume UNIQUE(data, idProgetto)
                            projectStatus = db.getStatus(idPrj, dateProjectStatus, user);
                        }
                        if (projectStatus != null) {
                            // Recupera la lista di attività presenti in un range di date
                            activitiesByRange = db.getActivitiesByRange(idPrj, user, projectStatus.getDataInizio(), projectStatus.getDataFine());
                            nextStatus = db.getNextStatus(idPrj, projectStatus.getDataFine(), user);
                            if (nextStatus != null) {
                                // Recupera la lista di attività con data inizio compresa tra due date
                                activitiesByDate = db.getActivitiesByDate(idPrj, user, nextStatus.getDataInizio(), nextStatus.getDataFine());
                            } else {
                                // Recupera la lista di attività con data inizio nel futuro rispetto alla data di fine dello status attuale
                                activitiesByDate = db.getActivities(idPrj, user, projectStatus.getDataFine(), false, true);
                            }
                        }
                    } 
                    /* ******************************************************************************************** *
                     *      Implementazione della parte specifica per la visualizzazione del report di progetto     *
                     * ******************************************************************************************** */
                    else if (part.equals(Query.PART_REPORT)) {
                        workPackagesOfProj = WbsCommand.retrieveWorkPackages(idPrj, db, user);
                        // Chiamata al metodo per la valorizzazione dello stato dell'attività da visualizzare
                        if (selectionDate.equals(Utils.format(Utils.getCurrentDate()))) {
                            convertDate = Utils.format(Utils.format(Utils.getCurrentDate()), "dd/MM/yyyy", Query.DATA_SQL_PATTERN);
                        } else {
                            convertDate = Utils.format(selectionDate, "dd/MM/yyyy", Query.DATA_SQL_PATTERN);
                        }
                        computeActivityState(convertDate, workPackagesOfProj);
                    } 
                    // Parte specifica di risorse
                    else if (part.equals(Query.PART_PROJECT_CHARTER_RESOURCE)) {
                        vSkills = db.getSkills(idPrj, user);
                    }
                    /* ************************************************************* *
                     *         Implementazione per grafico timelines di wp           *
                     * ************************************************************* */
                    else if (part.equals(Query.PART_TIMELINES)) {
                        workPackagesOfProj = WbsCommand.retrieveWorkPackages(idPrj, db, user);
                    }
                }                
                fileJspT = nomeFile.get(part);
            } else {
                m = db.getProjectsByDepart(user.getId());
                d = db.getDeparts();
                checkThisOut = decant(m);
                fileJspT = nomeFileElenco;
            }
        } catch (AttributoNonValorizzatoException anve) {
            String msg = FOR_NAME + "Si e\' verificato un problema nell\'accesso ad un attributo obbligatorio del bean.\n";
            LOG.severe(msg);
            throw new CommandException(msg + anve.getMessage(), anve);
        } catch (WebStorageException wse) {
            String msg = FOR_NAME + "Si e\' verificato un problema nel recupero di valori dal db.\n";
            LOG.severe(msg);
            throw new CommandException(msg + wse.getMessage(), wse);
        } catch (NullPointerException npe) {
            String msg = FOR_NAME + "Si e\' verificato un problema di puntamento a null.\n";
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
        // Imposta nella request elenco dipartimenti, se presente
        if (d != null) {
            req.setAttribute("dipart", d);
        }
        // Imposta nella request il Vector di workPackages nel caso in cui si voglia visualizzare il report di status
        if (workPackagesOfProj != null) {
            req.setAttribute("workPackagesOfProj", workPackagesOfProj);
        }
        // Imposta nella request la data sulla quale visualizzare il report
        if (convertDate != new Date(0)) {
            req.setAttribute("selectionDate", convertDate);
        }
        // Imposta flag 'true' se l'utente loggato vede almeno un progetto
        req.setAttribute("checkThisOut", checkThisOut);
        // Imposta nella request elenco progetti
        req.setAttribute("progetti", m);
        // Imposta nella request dettaglio progetto
        req.setAttribute("progetto", runtimeProject);
        // Imposta nella request elenco competenze del progetto
        req.setAttribute("competenze", vSkills);
        // Imposta nella request elenco rischi del progetto
        //req.setAttribute("rischi", vRisks);
        // Imposta nella request elenco degli status di un progetto
        req.setAttribute("listProjectStatus", projectStatusList);
        // Imposta nella request l'avanzamento progetto più recente
        req.setAttribute("projectStatus", projectStatus);
        // Imposta nella request i valori degli stati di un'avanzamento progetto
        req.setAttribute("statiValues", statiValues);
        // Imposta nella request l'id massimo della tabella avanzamentoprogetto
        req.setAttribute("newStatusId", newStatusId);
        // Imposta nella request l'elenco delle attività presenti in un range di date
        req.setAttribute("activitiesByRange", activitiesByRange);
        // Imposta nella request l'elenco delle attività con data inizio compresa tra due date
        req.setAttribute("activitiesByDate", activitiesByDate);
        // Imposta la Pagina JSP di forwarding
        req.setAttribute("fileJsp", fileJspT);
    }
    
    
    /**
     * <p>Valorizza per riferimento una mappa contenente i valori relativi  
     * ad un progetto eventualmente da aggiornare.</p> 
     * 
     * @param part la sezione del sito che si vuole aggiornare
     * @param parser oggetto per la gestione assistita dei parametri di input, gia' pronto all'uso
     * @param params mappa da valorizzare per riferimento (ByRef)
     * @throws CommandException se si verifica un problema nella gestione degli oggetti data o in qualche tipo di puntamento
     */
    private static void loadParams(String part, 
                                   ParameterParser parser,
                                   HashMap<String, HashMap<String, String>> params)
                            throws CommandException {
        /* **************************************************** *
         *        Ramo di Project Charter (PC) - Vision         *
         * **************************************************** */
        //String parameters = HomePageCommand.getParameters(req);
        //System.out.println(parameters);
        if (part.equalsIgnoreCase(Query.PART_PROJECT_CHARTER_VISION)) {
            // Recupero e caricamento parametri di project charter/vision
            HashMap<String, String> pcv = new HashMap<String, String>();
            pcv.put("pcv-situazione", parser.getStringParameter("pcv-situazione", Utils.VOID_STRING));
            pcv.put("pcv-descrizione", parser.getStringParameter("pcv-descrizione", Utils.VOID_STRING));
            pcv.put("pcv-obiettivi", parser.getStringParameter("pcv-obiettivi", Utils.VOID_STRING));
            pcv.put("pcv-minacce", parser.getStringParameter("pcv-minacce", Utils.VOID_STRING));
            // Caricamento della tabella valori parametri
            params.put(Query.PART_PROJECT_CHARTER_VISION, pcv);
        }
        /* **************************************************** *
         *        Ramo di Project Charter - Stakeholder         *
         * **************************************************** */
        else if (part.equalsIgnoreCase(Query.PART_PROJECT_CHARTER_STAKEHOLDER)) {
            // Recupero e caricamento parametri di project charter/stakeholder
            HashMap<String, String> pcs = new HashMap<String, String>();
            pcs.put("pcs-chiave", parser.getStringParameter("pcs-chiave", Utils.VOID_STRING));
            pcs.put("pcs-istituzionale", parser.getStringParameter("pcs-istituzionale", Utils.VOID_STRING));
            pcs.put("pcs-marginale", parser.getStringParameter("pcs-marginale", Utils.VOID_STRING));
            pcs.put("pcs-operativo", parser.getStringParameter("pcs-operativo", Utils.VOID_STRING));
            params.put(Query.PART_PROJECT_CHARTER_STAKEHOLDER, pcs);
        }
        /* **************************************************** *
         *         Ramo di Project Charter - Deliverable        *
         * **************************************************** */
        else if (part.equalsIgnoreCase(Query.PART_PROJECT_CHARTER_DELIVERABLE)) {
            // Recupero e caricamento parametri di project charter/deliverable
            HashMap<String, String> pcd = new HashMap<String, String>();
            pcd.put("pcd-descrizione", parser.getStringParameter("pcd-descrizione", Utils.VOID_STRING));
            params.put(Query.PART_PROJECT_CHARTER_DELIVERABLE, pcd);
        }
        /* **************************************************** *
         *           Ramo di Project Charter - Risorse          *
         * **************************************************** */
        else if (part.equalsIgnoreCase(Query.PART_PROJECT_CHARTER_RESOURCE)) {
            // Recupero e caricamento parametri di project charter/risorse
            HashMap<String, String> pcr = new HashMap<String, String>();
            pcr.put("pcr-chiaveesterni", parser.getStringParameter("pcr-chiaveesterni", Utils.VOID_STRING));
            pcr.put("pcr-chiaveinterni", parser.getStringParameter("pcr-chiaveinterni", Utils.VOID_STRING));
            pcr.put("pcr-serviziateneo", parser.getStringParameter("pcr-serviziateneo", Utils.VOID_STRING));
            params.put(Query.PART_PROJECT_CHARTER_RESOURCE, pcr);
        }
        /* **************************************************** *
         *            Ramo di Project Charter - Vincoli         *
         * **************************************************** */
        else if (part.equalsIgnoreCase(Query.PART_PROJECT_CHARTER_CONSTRAINT)) {
            // Recupero e caricamento parametri di project charter/vincoli
            HashMap<String, String> pcc = new HashMap<String, String>();
            pcc.put("pcc-descrizione", parser.getStringParameter("pcc-descrizione", Utils.VOID_STRING));
            params.put(Query.PART_PROJECT_CHARTER_CONSTRAINT, pcc);
        }
        /* **************************************************** *
         *                Ramo di Status Progetto               *
         * **************************************************** */
        else if (part.equalsIgnoreCase(Query.PART_STATUS)) {
            // Recupero e caricamento parametri di project status
            GregorianCalendar date = Utils.getUnixEpoch();
            String dataAsString = Utils.format(date, Query.DATA_SQL_PATTERN);
            HashMap<String, String> statusProject = new HashMap<String, String>();
            statusProject.put("sts-id", parser.getStringParameter("sts-id", Utils.VOID_STRING));
            statusProject.put("sts-datainizio", parser.getStringParameter("sts-datainizio", dataAsString));
            statusProject.put("sts-datafine", parser.getStringParameter("sts-datafine", dataAsString));
            statusProject.put("sts-avanzamento", parser.getStringParameter("sts-avanzamento", Utils.VOID_STRING));
            statusProject.put("sts-costi", parser.getStringParameter("sts-costi", Utils.VOID_STRING));
            statusProject.put("sts-tempi", parser.getStringParameter("sts-tempi", Utils.VOID_STRING));
            statusProject.put("sts-rischi", parser.getStringParameter("sts-rischi", Utils.VOID_STRING));
            statusProject.put("sts-risorse", parser.getStringParameter("sts-risorse", Utils.VOID_STRING));
            statusProject.put("sts-scope", parser.getStringParameter("sts-scope", Utils.VOID_STRING));
            statusProject.put("sts-comunicazione", parser.getStringParameter("sts-comunicazione", Utils.VOID_STRING));
            statusProject.put("sts-qualita", parser.getStringParameter("sts-qualita", Utils.VOID_STRING));
            statusProject.put("sts-approvvigionamenti", parser.getStringParameter("sts-approvvigionamenti", Utils.VOID_STRING));
            statusProject.put("sts-stakeholder", parser.getStringParameter("sts-stakeholder", Utils.VOID_STRING));
            params.put(Query.PART_STATUS, statusProject);
        }
    }
    
    
    /**
     * <p>Travasa un Vector di ProjectBean in una corrispondente struttura di 
     * tipo Dictionary, LinkedHashMap, in cui le chiavi sono rappresentate
     * da oggetti Wrapper di tipi primitivi interi (Integer) e i valori
     * sono rappresentati dai corrispettivi elementi del Vector.</p>
     * <p>&Egrave; utile per un accesso pi&uacute; diretto agli oggetti
     * memorizzati nella struttura rispetto a quanto garantito dai Vector.</p>
     * 
     * @param projects Vector di ProjectBean da travasare in HashMap
     * @return <code>LinkedHashMap&lt;Integer&comma; ProjectBean&gt;</code> - Struttura di tipo Dictionary, o Mappa ordinata, avente per chiave un Wrapper dell'identificativo dell'oggetto, e per valore quest'ultimo
     * @throws CommandException se si verifica un problema nell'accesso all'id di un oggetto, nello scorrimento di liste o in qualche altro tipo di puntamento
     */
    public static LinkedHashMap<Integer, ProjectBean> decant(Vector<ProjectBean> projects)
                                                      throws CommandException {
        LinkedHashMap<Integer, ProjectBean> userProjects = new LinkedHashMap<Integer, ProjectBean>(7);
        for (int i = 0; i < projects.size(); i++) {
            try {
                ProjectBean project = projects.elementAt(i);
                Integer projectIdAsInteger = new Integer(project.getId());
                userProjects.put(projectIdAsInteger, project);
            } catch (AttributoNonValorizzatoException anve) {
                String msg = FOR_NAME + "Si e\' verificato un problema nel recupero di un attributo obbligatorio del bean, probabilmente l\'id.\n" + anve.getMessage();
                LOG.severe(msg);
                throw new CommandException(msg, anve);
            } catch (ArrayIndexOutOfBoundsException aiobe) {
                String msg = FOR_NAME + "Si e\' verificato un problema di puntamento fuori tabella.\n" + aiobe.getMessage();
                LOG.severe(msg);
                throw new CommandException(msg, aiobe);
            } catch (ClassCastException cce) {
                String msg = FOR_NAME + "Si e\' verificato un problema di conversione di tipo.\n" + cce.getMessage();
                LOG.severe(msg);
                throw new CommandException(msg, cce);
            } catch (NullPointerException npe) {
                String msg = FOR_NAME + "Si e\' verificato un problema di puntamento.\n" + npe.getMessage();
                LOG.severe(msg);
                throw new CommandException(msg, npe);
            } catch (Exception e) {
                String msg = FOR_NAME + "Si e\' verificato un problema nel travaso di un Vector in un Dictionary.\n" + e.getMessage();
                LOG.severe(msg);
                throw new CommandException(msg, e);
            }
        }
        return userProjects;
    }
    
    
    /**
     * <p>Partendo da una serie di strutture (mappe) passate come argomenti,
     * travasa le stesse in un'unica mappa avente come chiave un valore
     * convenzionale (Sting) corrispondente al parametro identificante
     * la sezione dell'applicazione, e un valore corrispondente alla
     * mappa ricevuta. In questo modo &quot;compatta&quot; una serie
     * di liste in una sola lista, che pu&ograve; agevolmente utilizzare
     * per passarla a sua volta e farla utilizzare da un ricevente.</p>
     * 
     * @param activitiesByProject lista di attivita' scrivibili dall'utente, indicizzate per identificativo del progetto
     * @param skillsByProject   lista di competenze scrivibili dall'utente, indicizzate per identificativo del progetto
     * @param risksByProject    lista di rischi scrivibili dall'utente, indicizzati per identificativo del progetto
     * @return <code>LinkedHashMap&lt;String, HashMap&lt;Integer, Vector&gt;&gt; - mappa contenente le mappe passate come argomento, indicizzata per stringa identificante una funzionalita' applicativa
     * @throws CommandException se si verifica un problema in qualche tipo di puntamento
     */
    private static LinkedHashMap<String, HashMap<Integer, Vector>> decant(LinkedHashMap<Integer, Vector> activitiesByProject,
                                                                          LinkedHashMap<Integer, Vector> skillsByProject ,
                                                                          LinkedHashMap<Integer, Vector> risksByProject)
                                                                   throws CommandException {
        LinkedHashMap<String, HashMap<Integer, Vector>> map =  new LinkedHashMap<String, HashMap<Integer, Vector>>();
        map.put(Query.PART_PROJECT_CHARTER_RESOURCE, skillsByProject);
        return map;
    }
    
    
    /**
     * <p>Restituisce 'true' se almeno uno dei dipartimenti il cui identificativo
     * viene usato come chiave di una mappa sincronizzata passata come 
     * argomento ha progetti associati per l'utente loggato. 'False' 
     * se neanche un dipartimento di cui sopra ha almeno 
     * un progetto associato per l'utente loggato.</p>
     * <p>Attenzione: 'false' di questo metodo non significa che nessun 
     * dipartimento ha alcun progetto, cio&egrave; che non esistono progetti,
     * ma semplicemente che l'utente che si &egrave; loggato (in base al
     * quale la mappa sincronizzata viene costruita) non ha diritto di vederne
     * alcuno.</p>
     * <p>Pi&uacute; in generale, questo metodo potrebbe essere usato per 
     * verificare se in una mappa esiste almeno un valore significativo
     * (quantunque le chiavi esistano, ed &egrave; per questo che c'&egrave;
     * bisogno di un metodo di calcolo, in quanto il test JSTL fatto nella
     * pagina JSP con ${not empty map} non funziona: perch&eacute; in effetti 
     * la map non &egrave; empty!).</p>
     * 
     * @param map una mappa sincronizzata contenente i progetti indicizzati per Wrapper di identificativo dipartimentale
     * @return <code>boolean</code> - true se e' stato estratto almeno un progetto in uno dei possibili dipartimenti, false altrimenti
     * @throws CommandException se si verifica un problema nello scorrimento di liste o in qualche tipo di puntmaneto
     */
    private static boolean decant(ConcurrentHashMap<Integer, Vector<ProjectBean>> map) 
                           throws CommandException {
        try {
            Iterator<Map.Entry<Integer, Vector<ProjectBean>>> it = map.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<Integer, Vector<ProjectBean>> entry = it.next();
                if (entry.getValue().size() > 0) {
                    return true;
                }
            }
            return false;
        } catch (ArrayIndexOutOfBoundsException aiobe) {
            String msg = FOR_NAME + "Si e\' verificato un problema di puntamento fuori tabella.\n" + aiobe.getMessage();
            LOG.severe(msg);
            throw new CommandException(msg, aiobe);
        } catch (ClassCastException cce) {
            String msg = FOR_NAME + "Si e\' verificato un problema di conversione di tipo.\n" + cce.getMessage();
            LOG.severe(msg);
            throw new CommandException(msg, cce);
        } catch (NullPointerException npe) {
            String msg = FOR_NAME + "Si e\' verificato un problema di puntamento.\n" + npe.getMessage();
            LOG.severe(msg);
            throw new CommandException(msg, npe);
        } catch (Exception e) {
            String msg = FOR_NAME + "Si e\' verificato un problema nel calcolo di un boolean da un Dictionary.\n" + e.getMessage();
            LOG.severe(msg);
            throw new CommandException(msg, e);
        }
    }
    
    
    /**
     * <p>Metodo che valorizza per riferimento lo stato dell'attivit&agrave; in relazione 
     * allo status di progetto che si vuole visualizzare. In particolare, se è presente
     * uno solo status di progetto, l'attivit&agrave; potr&agrave; riportare solo i valori:<br /> 
     *      - CHIUSA<br />
     *      - IN RITARDO<br />
     *      - IN PROGRESS<br />
     *      - APERTA<br />
     * Se invece sono presenti pi&ugrave; status, allora l'attivit&agrave; potr&agrave; riportare 
     * i seguenti valori, calcolati basandosi sulle date dell'attivit&agrave; stessa e dello status: <br />
     *      - CHIUSA<br />
     *      - APPENA CHIUSA<br />
     *      - IN RITARDO<br />
     *      - IN PROGRESS<br />
     *      - PROSSIMO PERIODO<br />
     *      - PERIODI SUCCESSIVI.</p>
     * 
     * @param referenceDate Rappresenta la data alla quale l'utente ha richiesto il report.
     * @param workPackages Vector contenente tutti i work-packages relativi al progetto corrente.
     * @throws CommandException se si verfica un problema nel recupero di qualche attributo obbligatorio o in qualche altro tipo di puntamento
     */
    private static void computeActivityState(Date referenceDate, 
                                             Vector<WbsBean> workPackages)
                                      throws CommandException {
        try {
            // Ciclo tutti i workPackages del progetto attuale, per estrarmi tutte le attività
            for (WbsBean wp: workPackages) {
                // Ciclo interno per selezionare e valutare tutte le attività
                for (ActivityBean activity: wp.getAttivita()) {
                    // Variabili identificanti le date effettive dell'attività
                    GregorianCalendar date = new GregorianCalendar();
                    date.setTime(referenceDate);
                    // Data di inizio da prendere
                    Date dataInizio = null;
                    // Eseguo tutti i controlli
                    /* ***************************************************************** *
                     *              Prendo la data di inizio più significativa           * 
                     * ***************************************************************** */
                    // Confronto con new Date(0) perchè nel bean inizializzo i valori in questo modo
                    if (activity.getDataInizioEffettiva() != null) {
                        dataInizio = activity.getDataInizioEffettiva();
                    } else if (activity.getDataInizio() != null){
                        dataInizio = activity.getDataInizio();
                    }
                    /* ***************************************************************** *
                     *  Per prima cosa controllo se l'attività ha data inizio effettiva  *
                     *  e data fine effettiva.
                     * ***************************************************************** */
                    // CASI CON DATA FINE EFFETTIVA
                    if (activity.getDataFineEffettiva() != null && 
                        activity.getDataFineEffettiva().before(referenceDate)) {
                        // caso in cui la data di fine effettiva sia precedente di oltre 30 giorni
                        if (activity.getDataFineEffettiva().before(Utils.convert(Utils.getDate(date, -30, 0, 0)))) {
                                activity.getStato().setOrdinale(Query.CHIUSA_DA_TEMPO);
                        } 
                        // caso in cui la data di fine effettiva sia precedente di massimo 30 giorni
                        else {
                            activity.getStato().setOrdinale(Query.CHIUSA_DA_POCO);
                        }
                        // settaggio date attese per visualizzazione title report progetto
                        activity.setDataInizioAttesa(dataInizio);
                        activity.setDataFineAttesa(activity.getDataFineEffettiva());
                    }
                    // CASI IN CUI LA DATA DI FINE EFFETTIVA NON SIA SIGNIFICATIVA
                    else {
                        // IN RITARDO: ho la data di fine prevista passata
                        if (activity.getDataFine().before(referenceDate)) {
                            activity.getStato().setOrdinale(Query.IN_RITARDO);
                        }
                        // data fine prevista > o = a data di riferimento
                        else {
                            if (dataInizio == null) {
                                // data di fine prevista successiva di oltre 30 giorni
                                if (activity.getDataFine().after(Utils.convert(Utils.getDate(date, 30, 0, 0)))) {
                                    activity.getStato().setOrdinale(Query.PERIODO_FUTURO_VENTURO);
                                } 
                                // data di fine prevista successiva di massimo 30 giorni 
                                else {
                                    activity.getStato().setOrdinale(Query.PERIODO_FUTURO_PROSSIMO);
                                }
                            } else {
                                if (dataInizio.before(referenceDate) || dataInizio.equals(referenceDate)) {
                                    activity.getStato().setOrdinale(Query.IN_CORSO);
                                } else {
                                    // data di inizio prevista successiva di oltre 30 giorni
                                    if (activity.getDataInizio().after(Utils.convert(Utils.getDate(date, 30, 0, 0)))) {
                                        activity.getStato().setOrdinale(Query.PERIODO_FUTURO_VENTURO);
                                    } 
                                    // data di inizio prevista successiva di massimo 30 giorni 
                                    else {
                                        activity.getStato().setOrdinale(Query.PERIODO_FUTURO_PROSSIMO);
                                    }
                                }
                            }
                        }
                        // settaggio date attese per visualizzazione title report progetto
                        activity.setDataInizioAttesa(dataInizio);
                        activity.setDataFineAttesa(activity.getDataFine());
                    }
                }
            }
        } catch (AttributoNonValorizzatoException anve) {
            String msg = FOR_NAME + "Oggetto ActivityBean non valorizzato; problema nel settaggio dell\'ordinale nello stato dell\'attivi&agrave;.\n";
            LOG.severe(msg); 
            throw new CommandException(msg + anve.getMessage(), anve);
        } catch (NotFoundException nfe) {
            String msg = FOR_NAME + "Oggetto PersonBean non valorizzato; problema nella conversione sulla somma algebrica della data di riferimento.\n";
            LOG.severe(msg); 
            throw new CommandException(msg + nfe.getMessage(), nfe);
        } catch (NullPointerException npe) {
            String msg = FOR_NAME + "Si e\' verificato un problema di puntamento a null.\n";
            LOG.severe(msg);
            throw new CommandException(msg + npe.getMessage(), npe);
        } catch (Exception e) {
            String msg = FOR_NAME + "Si e\' verificato un problema.\n";
            LOG.severe(msg);
            throw new CommandException(msg + e.getMessage(), e);
        }
    }
}