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
 *   Copyright (C) renewed 2019 Universita' degli Studi di Verona, 
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
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.LinkedList;
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
import it.alma.bean.CodeBean;
import it.alma.bean.ItemBean;
import it.alma.bean.PersonBean;
import it.alma.bean.ProjectBean;
import it.alma.bean.StatusBean;
import it.alma.exception.AttributoNonValorizzatoException;
import it.alma.exception.CommandException;
import it.alma.exception.WebStorageException;


/** 
 * <p><code>HomePageCommand.java</code><br />
 * Gestisce la root dell'applicazione.</p>
 * 
 * <p>Created on mercoledì 11 ottobre 2018 15:13</p>
 * 
 * @author <a href="mailto:giovanroberto.torre@univr.it">Giovanroberto Torre</a>
 */
public class HomePageCommand extends ItemBean implements Command {
    
    /**
     *  Nome di questa classe 
     *  (utilizzato per contestualizzare i messaggi di errore)
     */
    static final String FOR_NAME = "\n" + Logger.getLogger(new Throwable().getStackTrace()[0].getClassName()) + ": ";
    /**
     * Log per debug in produzione
     */
    protected static Logger LOG = Logger.getLogger(Main.class.getName());
    
    private static final int SUB_MENU = 1;
    /**
     * Pagina a cui la command reindirizza per mostrare la form di login
     */
    private static final String nomeFileElenco = "/jsp/login.jsp";
    /**
     * Pagina a cui la command reindirizza per mostrare la form dell'utenza
     */
    private static final String nomeFileProfilo = "/jsp/profile.jsp";
    /**
     * DataBound.
     */
    private static DBWrapper db;
    /**
     * Lista destinata a contenere i possibili valori per esprimere 
     * la complessi&agrave; di un elemento
     */
    private static LinkedList<CodeBean> complessita = new LinkedList<CodeBean>();
    /**
     * Lista destinata a contenere i possibili valori per esprimere
     * lo stato di un'attivit&agrave; di un progetto
     */
    private static LinkedList<CodeBean> statiAttivita = new LinkedList<CodeBean>();
    /**
     * Lista destinata a contenere i possibili valori per esprimere
     * lo stato di avanzamento di un progetto
     */
    private static LinkedList<CodeBean> statiAvanzamento = new LinkedList<CodeBean>();
    /**
     * Lista destinata a contenere i possibili valori per esprimere
     * lo stato in cui si trova un progetto in generale
     */
    private static LinkedList<CodeBean> statiProgetto = new LinkedList<CodeBean>();
    
    
    /** 
     * Crea una nuova istanza di HomePageCommand 
     */
    public HomePageCommand() {
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
        try {
            // Attiva la connessione al database
            db = new DBWrapper();
            // Recupera i possibili valori degli stati
            complessita = db.getStati(Query.GET_COMPLESSITA);
            statiAttivita = db.getStati(Query.GET_STATI_ATTIVITA);
            statiAvanzamento = db.getStati(Query.GET_STATI_AVANZAMENTO);
            statiProgetto = db.getStati(Query.GET_STATI_PROGETTO);
        }
        catch (WebStorageException wse) {
            String msg = FOR_NAME + "Non e\' possibile avere una connessione al database.\n" + wse.getMessage();
            throw new CommandException(msg, wse);
        }
        catch (Exception e) {
            String msg = FOR_NAME + "Problemi nel caricare gli stati.\n" + e.getMessage();
            throw new CommandException(msg, e);
        }
    }  
  
    
    /**
     * <p>Gestisce il flusso principale.</p>
     * <p>Prepara i bean.</p>
     * <p>Passa nella Request i valori che verranno utilizzati dall'applicazione.</p>
     * 
     * @param req HttpServletRequest contenente parametri e attributi, e in cui settare attributi
     * @throws CommandException incapsula qualunque genere di eccezione che si possa verificare in qualunque punto del programma
     */
    public void execute(HttpServletRequest req) 
                 throws CommandException {
        // Parser per la gestione assistita dei parametri di input
        ParameterParser parser = new ParameterParser(req);
        // Utente loggato
        PersonBean user = null;
        // Recupera o inizializza 'tipo pagina'   
        String part = parser.getStringParameter("p", "-");
        // Flag di scrittura
        boolean write = (boolean) req.getAttribute("w");
        // Dichiara la pagina a cui reindirizzare
        String fileJspT = null;
        // Dichiara l'elenco dei progetti estratti dell'utente, partendo dal ruolo
        Vector<ItemBean> projectsByRole = null;
        // Dichiara l'elenco degli accessi degli utenti, da mostrare solo a root
        Vector<StatusBean> accessList = null;
        /* ******************************************************************** *
         *      Instanzia nuova classe WebStorage per il recupero dei dati      *
         * ******************************************************************** */
        try {
            db = new DBWrapper();
        } catch (WebStorageException wse) {
            throw new CommandException(FOR_NAME + "Non e\' disponibile un collegamento al database\n." + wse.getMessage(), wse);
        }
        // NON controlla se l'utente è già loggato perché questa command deve rispondere sempre
        /* *************************************************** *
         *                 Corpo del programma                 *
         * *************************************************** */
        // Selezione profilo utente
        try {
            if (part.equals(Query.PART_USR)) {
                // Recupera la sessione creata e valorizzata per riferimento nella req dal metodo authenticate
                HttpSession ses = req.getSession(Query.IF_EXISTS_DONOT_CREATE_NEW);
                user = (PersonBean) ses.getAttribute("usr");
                if (user == null) {
                    throw new CommandException("Attenzione: controllare di essere autenticati nell\'applicazione!\n");
                }
                if (write) {
                    /* **************************************** *
                     *          UPDATE Profile User             *
                     * **************************************** */
                    String passwd = parser.getStringParameter("txtPwd", Utils.VOID_STRING);
                    String passwdform = parser.getStringParameter("txtConfPwd", Utils.VOID_STRING);
                    if (passwd != Utils.VOID_STRING || passwdform != Utils.VOID_STRING) {
                        db.updatePassword(user.getId(), passwd, passwdform);
                    }
                }
                /* **************************************************** *
                 *                  SELECT Profile Part                 *
                 * **************************************************** */
                projectsByRole = db.getProjectsByRole(user.getId());
                accessList = db.getAccessLog(user.getId());
                fileJspT = nomeFileProfilo;
            } else {
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
        } catch (IllegalStateException ise) {
            String msg = FOR_NAME + "Impossibile redirigere l'output. Verificare se la risposta e\' stata gia\' committata.\n";
            LOG.severe(msg);
            throw new CommandException(msg + ise.getMessage(), ise);
        } catch (ClassCastException cce) {
            String msg = FOR_NAME + ": Si e\' verificato un problema in una conversione di tipo.\n";
            LOG.severe(msg);
            throw new CommandException(msg + cce.getMessage(), cce);
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
         *                          Recupera i parametri                        *
         * ******************************************************************** */
        // Recupera o inizializza 'id progetto'
        //int idPrj = parser.getIntParameter("id", -1);       
        // Imposta il testo del Titolo da visualizzare prima dell'elenco
        req.setAttribute("titoloE", "Progetti di eccellenza: login");
        // Imposta nella request i progetti dell'utente tramite il ruolo, nel caso in cui siano valorizzati
        if (projectsByRole != null) {
            req.setAttribute("projectsByRole", projectsByRole);
        }
        // Imposta nella request il log degli accessi se l'utente ne ha i privilegi
        if (accessList != null) {   // Se l'utente non ne ha il privilegio, la lista è nulla
            req.setAttribute("accesslog", accessList);
        }
        // Imposta la Pagina JSP di forwarding
        req.setAttribute("fileJsp", fileJspT);
    }
    
    
    /**
     * <p>Restituisce l'utente loggato se lo trova, altrimenti lancia 
     * un'eccezione.</p>
     * 
     * @param req HttpServletRequest contenente la sessione e i suoi attributi
     * @return <code>PersonBean</code> - l'utente loggatosi correntemente
     * @throws CommandException se si verifica un problema nel recupero della sessione o dei suoi attributi
     */
    public static PersonBean getLoggedUser(HttpServletRequest req) 
                                    throws CommandException {
        // Utente loggato
        PersonBean user = null;
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
            return user;
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
    }
    
    
    private static LinkedList<ItemBean> makeMenuOrizzontale(String appName, 
                                                            int projId)
                                                     throws CommandException {
        LinkedList<ItemBean> mO = new LinkedList<ItemBean>();
        // PROJECT CHARTER
        ItemBean vO = new ItemBean();
        vO.setId(1);
        vO.setNome("pol");
        vO.setNomeReale("pcv");
        vO.setLabelWeb("Project Charter");
        vO.setInformativa("Il Project Charter rappresenta la visione statica del progetto");
        vO.setUrl(appName + "/?q=" + vO.getNome() + "&p=" + vO.getNomeReale() + "&id=" + projId);
        vO.setIcona("pc.png");
        mO.add(vO);
        // STATUS
        vO = null;
        vO = new ItemBean();
        /*vO.setId(2);
        vO.setNome("sts");
        vO.setNomeReale("pol");
        vO.setLabelWeb("Status");
        vO.setInformativa("Lo status, o avanzamento di progetto, &egrave; il punto della situazione del progetto a un certo intervallo di tempo");
        vO.setUrl(appName + "/?q=" + vO.getNomeReale() + "&p=" + vO.getNome() + "&ids=" + Query.NOTHING + "&id=" + projId);
        vO.setIcona("sts.png");*/
        vO.setId(2);
        vO.setNome("pol");
        vO.setNomeReale("prj");
        vO.setLabelWeb("Status");
        vO.setInformativa("Lo status, o avanzamento di progetto, &egrave; il punto della situazione del progetto a un certo intervallo di tempo");
        vO.setUrl(makeUrl(appName, vO, "prj", projId));
        vO.setIcona("sts.png");
        mO.add(vO);
        // WBS
        vO = null;
        vO = new ItemBean();
        vO.setId(3);
        vO.setNome("wbs");
        vO.setLabelWeb("WBS");
        vO.setInformativa("Le WBS rappresentano unit&agrave; di suddivisione degli ambiti del progetto"); 
        vO.setUrl(makeUrl(appName, vO, null, projId));
        vO.setIcona("wbs.png");
        mO.add(vO);
        // ATTIVITA'
        vO = null;
        vO = new ItemBean();
        vO.setId(4);
        vO.setNome("act");
        vO.setLabelWeb("Attivit&agrave;");
        vO.setInformativa("Le attivit&agrave; sono le operazioni effettuate e vengono aggregate da specifiche WBS"); 
        vO.setUrl(makeUrl(appName, vO, null, projId));
        vO.setIcona("act.png");
        mO.add(vO);
        // REPORT
        vO = null;
        vO = new ItemBean();
        vO.setId(5);
        vO.setNome("pol");
        vO.setLabelWeb("Report");
        vO.setInformativa("Il report di progetto &egrave; una rappresentazione sintetica del lavoro svolto"); 
        vO.setUrl(appName + "/?q=" + vO.getNome() + "&p=rep&id=" + projId);
        vO.setIcona("rep.png");
        mO.add(vO);
        return mO;
    }
    
    /**
     *TODO commento
     * O il numero di progetti è = 1 (tipicamente ruoli TL, User - ma potrebbe essere anche un PM di un dipartimento con un solo progetto)
     * // Oppure il numero di progetti è > 1 (ruoli PM, PMOx, ma potrebbe essere anche un TL o uno User su più progetti)
     * diciamo che il numero di progetti dell'utente non permette di fare assunzioni
     * sul ruolo dell'utente stesso: un utente potrebbe essere un semplice user su parecchi progetti
     * così come un PM o un PMO di dipartimento potrebbe avere un solo progetto
     * perché il suo dipartimento ne ha prodotto uno solo!
     * Tuttavia, noi non facciamo assunzioni sul ruolo in base al numero di progetti
     * ma solo sul menu, perché è evidente che possiamo generare il menu con le
     * voci dirette alle varie pagine solo se siamo in presenza di un solo progetto;
     * se l'utente ha parecchi progetti, dobbiamo generare la lista di progetti.
     * Quanto alla presenza dei "tastini" speciali per il monitoraggio, lì è
     * effettivamente una questione di ruolo, per cui il discrimine non è più
     * il numero di progetti utente quanto il ruolo dell'utente stesso
     * 
     * @param usr
     * @param appName
     * @return
     * @throws CommandException
     */
    public static LinkedHashMap<ItemBean, ArrayList<ItemBean>> makeMegaMenu(PersonBean usr, 
                                                                            String appName) 
                                                                     throws CommandException {
        ItemBean title = null;
        ItemBean item = null;
        ArrayList<ItemBean> vV = null;
        LinkedHashMap<ItemBean, ArrayList<ItemBean>> vO = null;
        try {
            vO = new LinkedHashMap<ItemBean, ArrayList<ItemBean>>(11);
            Vector<ProjectBean> projects = db.getProjects(usr.getId(), Query.GET_ALL);
            ProjectBean project = projects.elementAt(Query.NOTHING);
            int projId = project.getId();
            LinkedList<ItemBean> titles = makeMenuOrizzontale(appName, projId);
            // Gestisce 2 casi: 
            if (projects.size() == 1) { // O il numero di progetti è = 1 
                //Collection<ProjectBean> values = projects.values();
                //int projId = (ProjectBean) projects.get(key);
                // PROJECT CHARTER
                title = titles.getFirst();
                vV = new ArrayList<ItemBean>();
                // Vision
                item = new ItemBean("pcv", "Vision", makeUrl(appName, title, "pcv", projId), SUB_MENU);
                vV.add(item);
                // Stakeholder
                item = null;
                item = new ItemBean("pcs", "Stakeholder", makeUrl(appName, title, "pcs", projId), SUB_MENU);
                vV.add(item);
                // Deliverable
                item = null;
                item = new ItemBean("pcd", "Deliverable", makeUrl(appName, title, "pcd", projId), SUB_MENU);
                vV.add(item);
                // Risorse
                item = null;
                item = new ItemBean("pcr", "Risorse", makeUrl(appName, title, "pcr", projId), SUB_MENU);
                vV.add(item);
                // Rischi
                item = null;
                item = new ItemBean();
                item.setNome("ris");
                item.setLabelWeb("Rischi");
                item.setUrl(appName + "/?q=" + item.getNome() + "&id=" + projId);
                item.setLivello(SUB_MENU);
                vV.add(item);
                // Vincoli
                item = null;
                item = new ItemBean("pcc", "Vincoli", makeUrl(appName, title, "pcc", projId), SUB_MENU);
                vV.add(item);
                // Milestone
                item = null;
                item = new ItemBean();
                item.setNome("pcm");
                item.setLabelWeb("Milestone");
                item.setUrl(appName + "/?q=act&p=" + item.getNome() + "&id=" + projId);
                item.setLivello(SUB_MENU);
                vV.add(item);
                vO.put(title, vV);
                // STATUS
                title = null;
                title = titles.get(1);
                vV = new ArrayList<ItemBean>();
                item = null;
                ArrayList<StatusBean> projectStatusList = db.getStatusList(projId);
                for (StatusBean s : projectStatusList) {
                    item = new ItemBean("sts", 
                                        "Avanzamento dal " + Utils.format(s.getDataInizio()) + " al " + Utils.format(s.getDataFine()), 
                                        appName + "/?q=" + title.getNomeReale() + "&p=" + title.getNome() + "&id=" + projId + "&ids=" + s.getId(),
                                        SUB_MENU);
                    vV.add(item);
                }
                vO.put(title, vV);
                // WBS
                title = null;
                title = titles.get(2);
                vV = new ArrayList<ItemBean>();
                // Wbs
                item = null;
                item = new ItemBean("wbs", "WBS", title.getUrl(), SUB_MENU);
                vV.add(item);
                // Grafico
                item = null;
                item = new ItemBean("gra", "Rappresentazione grafica delle WBS", makeUrl(appName, title, "gra", projId), SUB_MENU);
                vV.add(item);
                // Report di WorkPackage
                item = null;
                item = new ItemBean("rep", "Report di Work Package", makeUrl(appName, title, "rep", projId), SUB_MENU);
                vV.add(item);
                vO.put(title, vV);
                // ATTIVITA'
                title = null;
                title = titles.get(3);
                vV = new ArrayList<ItemBean>();
                // Attività
                item = null;
                item = new ItemBean("act", "Attivit&agrave;", title.getUrl(), SUB_MENU);
                vV.add(item);
                // Grafico
                item = null;
                item = new ItemBean("gra", "Rappresentazione grafica delle WBS", makeUrl(appName, title, "gra", projId), SUB_MENU);
                vV.add(item);
                // Cestino
                item = null;
                item = new ItemBean("bin", "Cestino attivit&agrave;", makeUrl(appName, title, "bin", projId), SUB_MENU);
                vV.add(item);
                vO.put(title, vV);
                // REPORT
                title = null;
                title = titles.get(4);
                vV = null;
                vO.put(title, new ArrayList<ItemBean>());
            } else if (projects.size() > 1) { // Oppure il numero di progetti è > 1 
                // PROJECT CHARTER
                title = titles.getFirst();
                vV = new ArrayList<ItemBean>();
                for (ProjectBean p : projects) {
                    item = new ItemBean("pcv", p.getTitolo(), makeUrl(appName, title, "pcv", p.getId()), SUB_MENU);
                    vV.add(item);
                }
                vO.put(title, vV);
                // STATUS
                title = null;
                title = titles.get(1);
                vV = new ArrayList<ItemBean>();
                for (ProjectBean p : projects) {
                    item = new ItemBean("pol", p.getTitolo(), makeUrl(appName, title, "prj", p.getId()), SUB_MENU);
                    vV.add(item);
                }
                vO.put(title, vV);
                // WBS
                title = null;
                title = titles.get(2);
                vV = new ArrayList<ItemBean>();
                for (ProjectBean p : projects) {
                    item = new ItemBean("wbs", p.getTitolo(), makeUrl(appName, title, null, p.getId()), SUB_MENU);
                    vV.add(item);
                }
                vO.put(title, vV);
                // Attività
                title = null;
                title = titles.get(3);
                vV = new ArrayList<ItemBean>();
                for (ProjectBean p : projects) {
                    item = new ItemBean("act", p.getTitolo(), makeUrl(appName, title, null, p.getId()), SUB_MENU);
                    vV.add(item);
                }
                vO.put(title, vV);
                // Report
                title = null;
                title = titles.get(4);
                vV = new ArrayList<ItemBean>();
                for (ProjectBean p : projects) {
                    item = new ItemBean("rep", p.getTitolo(), makeUrl(appName, title, "rep", p.getId()), SUB_MENU);
                    vV.add(item);
                }
                vO.put(title, vV);
            } else { 
                // No menu
            }
            return vO;
        } catch (AttributoNonValorizzatoException anve) {
            String msg = FOR_NAME + "Si e\' verificato un problema nell\'accesso ad un attributo obbligatorio di un bean.\n";
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
        
    }
    
    
    private static String makeUrl(String appName, 
                                  ItemBean title, 
                                  String p, 
                                  int prjId) 
                           throws CommandException {
        try {
            StringBuffer url = new StringBuffer(appName);
            url.append("/?q=").append(title.getNome());
            if (p != null) {
                url.append("&p=").append(p);
            }
            url.append("&id=").append(prjId);
            return String.valueOf(url);
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
    
    
    /* ************************************************************************ *
     *                            Metodi di utilita'                            *
     * ************************************************************************ */
    
    /**
     * <p>Restituisce i nomi e i valori degli attributi presenti in Request
     * in un dato momento e in un dato contesto, rappresentati dallo
     * stato del chiamante.</p>
     * <p>Pu&ograve; essere utilizzato per verificare rapidamente 
     * quali attributi sono presenti in Request onde evitare duplicazioni
     * o ridondanze.</p>
     * </p>
     * Ad esempio, richiamando questo metodo dal ramo "didattica" del sito web
     * di ateneo, metodo <code>requestByPage</code>
     * e.g.: <pre>req.setAttribute("reqAttr", getAttributes(req));</pre>
     * e richiamandolo dalla pagina relativa, con la semplice:
     * <pre>${reqAttr}</pre>
     * si ottiene:
     * <pre style="border:solid gray;border-width:2px;padding:8px;">
     * <strong>dipartimento</strong> = it.univr.di.uol.bean.DipartimentoBean@518dd094
     * <strong>mO</strong> = {it.univr.di.uol.bean.SegnalibroBean@1ef0921d=[it.univr.di.uol.MenuVerticale@5ab38d6b, it.univr.di.uol.MenuVerticale@42099a52], it.univr.di.uol.bean.SegnalibroBean@4408bdc9=[it.univr.di.uol.MenuVerticale@4729f5d], it.univr.di.uol.bean.SegnalibroBean@19e3fa04=[it.univr.di.uol.MenuVerticale@13c94f3], it.univr.di.uol.bean.SegnalibroBean@463329e3=[it.univr.di.uol.MenuVerticale@3056de27]}
     * <strong>lingue</strong> = it.univr.di.uol.Lingue@3578ce60
     * <strong>FirstLanguage</strong> = it
     * <strong>flagsUrl</strong> = ent=home&page=didattica
     * <strong>SecondLanguage</strong> = en
     * <strong>logoFileDoc</strong> = [[it.univr.di.uol.bean.FileDocBean@5b11bbf9]]
     * <strong>currentYear</strong> = 2015
     * </pre></p>
     * 
     * @param req HttpServletRequest contenente gli attributi che si vogliono conoscere
     * @return un unico oggetto contenente tutti i valori e i nomi degli attributi settati in request nel momento in cui lo chiede il chiamante
     */
    public static String getAttributes(HttpServletRequest req) {
        Enumeration<String> attributes = req.getAttributeNames();
        StringBuffer attributesName = new StringBuffer("<pre>");
        while (attributes.hasMoreElements()) {
            String attributeName = attributes.nextElement();
            attributesName.append("<strong><u>");
            attributesName.append(attributeName);
            attributesName.append("</u></strong>");
            attributesName.append(" = ");
            attributesName.append(req.getAttribute(attributeName));
            attributesName.append("<br />");
        }
        attributesName.append("</pre>");
        return String.valueOf(attributesName);
    }
    
    
    /**
     * <p>Restituisce i nomi e i valori dei parametri presenti in Request
     * in un dato momento e in un dato contesto, rappresentati dallo
     * stato del chiamante.</p>
     * <p>Pu&ograve; essere utilizzato per verificare rapidamente 
     * quali parametri sono presenti in Request onde evitare duplicazioni
     * e/o ridondanze.</p>
     * 
     * @param req HttpServletRequest contenente i parametri che si vogliono conoscere
     * @param mime argomento specificante il formato dell'output desiderato
     * @return un unico oggetto contenente tutti i valori e i nomi dei parametri settati in request nel momento in cui lo chiede il chiamante
     */
    public static String getParameters(HttpServletRequest req,
                                       String mime) {
        Enumeration<String> parameters = req.getParameterNames();
        StringBuffer parametersName = new StringBuffer();
        if (mime.equals(Utils.MIME_TYPE_HTML)) {
            parametersName.append("<pre>");
            while (parameters.hasMoreElements()) {
                String parameterName = parameters.nextElement();
                parametersName.append("<strong><u>");
                parametersName.append(parameterName);
                parametersName.append("</u></strong>");
                parametersName.append(" = ");
                parametersName.append(req.getParameter(parameterName));
                parametersName.append("<br />");
            }
            parametersName.append("</pre>");
        } else if (mime.equals(Utils.MIME_TYPE_TEXT)) {
            while (parameters.hasMoreElements()) {
                String parameterName = parameters.nextElement();
                parametersName.append(parameterName);
                parametersName.append(" = ");
                parametersName.append(req.getParameter(parameterName));
                parametersName.append("\n");
            }
        }
        return String.valueOf(parametersName);
    }
 
    
    /**
     * <p>Restituisce <code>true</code> se un nome di un parametro,
     * il cui valore viene passato come argomento del metodo, esiste
     * tra i parametri della HttpServletRequest; <code>false</code>
     * altrimenti.</p>
     * <p>Pu&ograve; essere utilizzato per verificare rapidamente 
     * se un dato parametro sia stato passato in Request.</p>
     * 
     * @param req HttpServletRequest contenente i parametri che si vogliono conoscere
     * @param paramName argomento specificante il nome del parametro cercato
     * @return un unico oggetto contenente tutti i valori e i nomi dei parametri settati in request nel momento in cui lo chiede il chiamante
     */
    public static boolean isParameter(HttpServletRequest req,
                                      String paramName) {
        Enumeration<String> parameters = req.getParameterNames();
        while (parameters.hasMoreElements()) {
            String parameterName = parameters.nextElement();
            if (parameterName.equalsIgnoreCase(paramName)) {
                return true;
            }
        }
        return false;
    }
    
    /* ************************************************************************ *
     *                    Getters sulle variabili di classe                     *
     * ************************************************************************ */
    
    /**
     * <p>Restituisce i possibili valori descrittivi della complessit&agrave; 
     * di un elemento.</p>
     * 
     * @return <code>LinkedList&lt;CodeBean&gt;</code> - una lista ordinata di tutti i possibili valori con cui puo' essere descritta la complessita' di un elemento
     */
    public static LinkedList<CodeBean> getComplessita() {
        return new LinkedList<CodeBean>(complessita);
    }
    
    
    /**
     * <p>Restituisce i possibili valori descrittivi della attivit&agrave; 
     * di un work package.</p>
     * 
     * @return <code>LinkedList&lt;CodeBean&gt;</code> - una lista ordinata di tutti i possibili valori con cui puo' essere descritto lo stato di un'attivita'
     */
    public static LinkedList<CodeBean> getStatiAttivita() {
        return new LinkedList<CodeBean>(statiAttivita);
    }
    
    
    /**
     * <p>Restituisce i possibili valori descrittivi dell'avanzamento 
     * di un progetto.</p>
     * 
     * @return <code>LinkedList&lt;CodeBean&gt;</code> - una lista ordinata di tutti i possibili valori con cui puo' essere descritto lo stato di avanzamento di un progetto
     */
    public static LinkedList<CodeBean> getStatiAvanzamento() {
        return new LinkedList<CodeBean>(statiAvanzamento);
    }
    
    
    /**
     * <p>Restituisce i possibili valori descrittivi dello stato 
     * di un progetto.</p>
     * 
     * @return <code>LinkedList&lt;CodeBean&gt;</code> - una lista ordinata di tutti i possibili valori con cui puo' essere descritto lo stato di un progetto
     */
    public static LinkedList<CodeBean> getStatiProgetto() {
        return new LinkedList<CodeBean>(statiProgetto);
    }
    
}