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

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Vector;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.oreilly.servlet.ParameterNotFoundException;
import com.oreilly.servlet.ParameterParser;

import it.alma.DBWrapper;
import it.alma.Main;
import it.alma.Query;
import it.alma.Utils;
import it.alma.bean.ActivityBean;
import it.alma.bean.ItemBean;
import it.alma.bean.PersonBean;
import it.alma.bean.ProjectBean;
import it.alma.bean.RiskBean;
import it.alma.bean.SkillBean;
import it.alma.bean.StatusBean;
import it.alma.bean.WbsBean;
import it.alma.exception.AttributoNonValorizzatoException;
import it.alma.exception.CommandException;
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
     * Pagina a cui la command reindirizza per mostrare 
     */
    private static final String nomeFileElenco = "/jsp/elencoProgetti.jsp";
    /**
     * Struttura contenente le pagina a cui la command fa riferimento per mostrare tutti gli attributi del progetto
     */    
    private static final HashMap<String, String> nomeFile = new HashMap<String, String>();
    /**
     *  Progetto di dato id
     */
    ProjectBean runtimeProject = null;
    
    
    /** 
     * Crea una nuova istanza di ValuationCommand 
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
        nomeFile.put(Query.PART_PROJECT_CHARTER_RISK, "/jsp/pcRischi.jsp");
        nomeFile.put(Query.PART_PROJECT_CHARTER_CONSTRAINT, "/jsp/pcVincoli.jsp");
        nomeFile.put(Query.PART_PROJECT_CHARTER_MILESTONE, "/jsp/pcMilestone.jsp");
        nomeFile.put(Query.PART_WBS, "/jsp/projWBS.jsp");
        nomeFile.put(Query.PART_ACTIVITY, "/jsp/projActivities.jsp");
        nomeFile.put(Query.PART_STATUS, "/jsp/projStatus.jsp");
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
        // Flag di scrittura
        boolean write = (boolean) req.getAttribute("w");
        // Dichiara la pagina a cui reindirizzare
        String fileJspT = null;
        // Dichiara elenco di progetti
        Vector<ProjectBean> v = new Vector<ProjectBean>();
        // Dichiara elenco di attività
        Vector<ActivityBean> vActivities = new Vector<ActivityBean>();
        // Dichiara elenco di competenze
        Vector<SkillBean> vSkills = new Vector<SkillBean>();
        // Dichiara elenco di rischi
        Vector<RiskBean> vRisks = new Vector<RiskBean>();
        // Dichiara elenco di wbs
        Vector<WbsBean> vWBS = new Vector<WbsBean>();
        // Dichiara l'elenco degli status di un progetto
        ArrayList<StatusBean> projectStatusList = new ArrayList<StatusBean>();
        // Dichiara l'avanzamento progetto più recente
        StatusBean projectStatus = null;
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
                    if (write) {
                        // Creazione della tabella valori parametri
                        HashMap<String, HashMap<String, String>> params = new HashMap<String, HashMap<String, String>>();
                        loadParams(part, parser, params);
                        /* **************************************************** *
                         *                  UPDATE Project Part                 *
                         * **************************************************** */
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
                            db.updateProjectPart(idPrj, user.getId(), writableProjects, objectsMap, params);
                            Vector<ProjectBean> userWritableProjects = db.getProjects(user.getId(), Query.GET_ONLY_WRITABLE_PROJECTS);
                            // Aggiorna i progetti, le attività dell'utente in sessione
                            ses.removeAttribute("writableProjects");
                            ses.removeAttribute("writableActivity");
                            ses.setAttribute("writableProjects", userWritableProjects);
                            ses.setAttribute("writableActivity", userWritableActivitiesByProjectId);
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
                    // Recupera tutta la lista degli Status
                    projectStatusList = db.getStatusList(idPrj);
                    // Per ottimizzare il caricamento di dati nella request separa i rami della richiesta
                    if (part.equals(Query.PART_PROJECT)) {
                        ; // Codice per mostrare dati aggregati sul progetto, o relazioni o altro
                    } else if (part.equals(Query.PART_STATUS)) {
                        // Testa se l'id dello status � significativo
                        if (idStatus > Query.NOTHING) {
                            // Recupera uno specifico status di progetto di dato id
                            projectStatus = db.getStatus(idStatus);
                        } else if (idStatus == Query.NOTHING) { // Valore fittizio (nessuno status può avere id = 0!)
                            // Recupera l'ultimo Status, cioè quello avente datainizio più prossima alla data odierna
                            Date dateProjectStatus = new Date(0);
                            if (!projectStatusList.isEmpty()) {
                                for (int i = 0; i < projectStatusList.size(); i++) {
                                    if (projectStatusList.get(i).getDataInizio().equals(Utils.convert(Utils.getCurrentDate())) ||
                                        projectStatusList.get(i).getDataInizio().before(Utils.convert(Utils.getCurrentDate())) ) {
                                        dateProjectStatus = projectStatusList.get(i).getDataInizio();
                                        break;
                                    }
                                }
                            }
                            // Recupera uno specifico stuatus di progetto a partire dalla sua data - assume UNIQUE(data, idProgetto)
                            projectStatus = db.getStatus(idPrj, dateProjectStatus);
                        }
                    } else if (part.equals(Query.PART_ACTIVITY)) {
                        vActivities = db.getActivities(idPrj);
                    } else if (part.equals(Query.PART_PROJECT_CHARTER_RESOURCE)) {
                        vSkills = db.getSkills(idPrj);
                    } else if (part.contains(Query.PART_PROJECT_CHARTER_RISK)) {
                        vRisks = db.getRisks(idPrj);
                    } else if(part.equals(Query.PART_WBS)) {
                        vWBS = db.getWbs(idPrj);
                    }
                }
                fileJspT = nomeFile.get(part);
            } else {
                v = db.getProjects(user.getId(), Query.GET_ALL);
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
        // Imposta il testo del Titolo da visualizzare prima dell'elenco
        req.setAttribute("titoloE", "Project Charter");
        // Salva nella request: Titolo pagina (da mostrare nell'HTML)
        req.setAttribute("tP", req.getAttribute("titoloE"));         
        // Imposta la Pagina JSP di forwarding
        req.setAttribute("fileJsp", fileJspT);
        // Salva nella request elenco progetti
        req.setAttribute("progetti", v);
        // Salva nella request dettaglio progetto
        req.setAttribute("progetto", runtimeProject);
        // Salva nella request elenco attivita del progetto
        req.setAttribute("attivita", vActivities);
        // Salva nella request elenco competenze del progetto
        req.setAttribute("competenze", vSkills);
        // Salva nella request elenco rischi del progetto
        req.setAttribute("rischi", vRisks);
        // Salva nella request elenco delle wbs di un progetto
        req.setAttribute("wbs", vWBS);
        // Salva nella request elenco degli status di un progetto
        req.setAttribute("listProjectStatus", projectStatusList);
        // Salva nella request l'avanzamento progetto più recente
        req.setAttribute("projectStatus", projectStatus);
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
            int totSkills = Integer.parseInt(parser.getStringParameter("pcr-loop-status", Utils.VOID_STRING));
            HashMap<String, String> pcr = new HashMap<String, String>();
            for (int i = 0; i <= totSkills; i++) {
                String presenza = "false";
                pcr.put("pcr-id" + String.valueOf(i), parser.getStringParameter("pcr-id" + String.valueOf(i), Utils.VOID_STRING));
                pcr.put("pcr-nome" + String.valueOf(i), parser.getStringParameter("pcr-nome" + String.valueOf(i), Utils.VOID_STRING));
                pcr.put("pcr-informativa" + String.valueOf(i), parser.getStringParameter("pcr-informativa" + String.valueOf(i), Utils.VOID_STRING));
                if((parser.getStringParameter("pcr-presenza" + String.valueOf(i), Utils.VOID_STRING)) != "") {
                    presenza = "true";
                }
                pcr.put("pcr-presenza" + String.valueOf(i), presenza);
            }
            pcr.put("pcr-chiaveesterni", parser.getStringParameter("pcr-chiaveesterni", Utils.VOID_STRING));
            pcr.put("pcr-chiaveinterni", parser.getStringParameter("pcr-chiaveinterni", Utils.VOID_STRING));
            pcr.put("pcr-serviziateneo", parser.getStringParameter("pcr-serviziateneo", Utils.VOID_STRING));
            params.put(Query.PART_PROJECT_CHARTER_RESOURCE, pcr);
        }
        /* **************************************************** *
         *              Ramo di Project Charter - Rischi        *
         * **************************************************** */
        else if (part.equalsIgnoreCase(Query.PART_PROJECT_CHARTER_RISK)) {
            // Recupero e caricamento parametri di project charter/risorse
            int totRisks = Integer.parseInt(parser.getStringParameter("pck-loop-status", Utils.VOID_STRING));
            HashMap<String, String> pck = new HashMap<String, String>();
            for (int i = 0; i <= totRisks; i++) {
                pck.put("pck-id" + String.valueOf(i), parser.getStringParameter("pck-id" + String.valueOf(i), Utils.VOID_STRING));
                pck.put("pck-nome" + String.valueOf(i), parser.getStringParameter("pck-nome" + String.valueOf(i), Utils.VOID_STRING));
                pck.put("pck-informativa" + String.valueOf(i), parser.getStringParameter("pck-informativa" + String.valueOf(i), Utils.VOID_STRING));
                pck.put("pck-impatto" + String.valueOf(i), parser.getStringParameter("pck-impatto" + String.valueOf(i), Utils.VOID_STRING));
                pck.put("pck-livello" + String.valueOf(i), parser.getStringParameter("pck-livello" + String.valueOf(i), Utils.VOID_STRING));
                pck.put("pck-stato" + String.valueOf(i), parser.getStringParameter("pck-stato" + String.valueOf(i), Utils.VOID_STRING));
            }
            params.put(Query.PART_PROJECT_CHARTER_RISK, pck);
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
         *          Ramo di Project Charter - Milestone         *
         * **************************************************** */
        else if (part.equalsIgnoreCase(Query.PART_PROJECT_CHARTER_MILESTONE)) {
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
            params.put(Query.PART_PROJECT_CHARTER_MILESTONE, pcm);
        }
        /* **************************************************** *
         *                Ramo di Status Progetto               *
         * **************************************************** */
        else if (part.equalsIgnoreCase(Query.PART_PROJECT)) {
            // Recupero e caricamento parametri di project status
            GregorianCalendar data = new GregorianCalendar(1970, 1, 1);
            String dataAsString = Utils.format(data, "yyyy-mm-dd");
            HashMap<String, String> statusProject = new HashMap<String, String>();
            statusProject.put("sId",parser.getStringParameter("sId", Utils.VOID_STRING));
            statusProject.put("sDataInizio", parser.getStringParameter("sDataInizio", dataAsString));
            statusProject.put("sDataFine", parser.getStringParameter("sDataFine", dataAsString));
            statusProject.put("sAvanzamento", parser.getStringParameter("sAvanzamento", Utils.VOID_STRING));
            statusProject.put("sCosti", parser.getStringParameter("sCosti", Utils.VOID_STRING));
            statusProject.put("sTempi", parser.getStringParameter("sTempi", Utils.VOID_STRING));
            statusProject.put("sRischi", parser.getStringParameter("sRischi", Utils.VOID_STRING));
            statusProject.put("sRisorse", parser.getStringParameter("sRisorse", Utils.VOID_STRING));
            statusProject.put("sScope", parser.getStringParameter("sScope", Utils.VOID_STRING));
            statusProject.put("sComunicazione", parser.getStringParameter("sComunicazione", Utils.VOID_STRING));
            statusProject.put("sQualita", parser.getStringParameter("sQualita", Utils.VOID_STRING));
            statusProject.put("sApprovvigionamenti", parser.getStringParameter("sApprovvigionamenti", Utils.VOID_STRING));
            statusProject.put("sStakeholder", parser.getStringParameter("sStakeholder", Utils.VOID_STRING));
            params.put(Query.PART_PROJECT, statusProject);
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
    private static LinkedHashMap<Integer, ProjectBean> decant(Vector<ProjectBean> projects)
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
     * @param activitiesByProject
     * @param skillsByProject
     * @param risksByProject
     * @return
     * @throws CommandException
     */
    private static LinkedHashMap<String, HashMap<Integer, Vector>> decant(LinkedHashMap<Integer, Vector> activitiesByProject,
                                                                             LinkedHashMap<Integer, Vector> skillsByProject ,
                                                                             LinkedHashMap<Integer, Vector> risksByProject)
                                                                      throws CommandException {
        LinkedHashMap<String, HashMap<Integer, Vector>> map =  new LinkedHashMap<String, HashMap<Integer, Vector>>();
        map.put(Query.PART_PROJECT_CHARTER_MILESTONE, activitiesByProject);
        map.put(Query.PART_PROJECT_CHARTER_RESOURCE, skillsByProject);
        map.put(Query.PART_PROJECT_CHARTER_RISK, risksByProject);
        return map;
        
    }
    
}