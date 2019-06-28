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
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.oreilly.servlet.ParameterParser;

import it.alma.DBWrapper;
import it.alma.Main;
import it.alma.PasswordGenerator;
import it.alma.Query;
import it.alma.SessionManager;
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
    /* friendly */ 
    static final String FOR_NAME = "\n" + Logger.getLogger(new Throwable().getStackTrace()[0].getClassName()) + ": "; //$NON-NLS-1$
    /* $NON-NLS-1$ silence a warning that Eclipse emits when it encounters 
     * string literals.
     * The idea is that UI messages should not be embedded as string literals,
     * but rather sourced from a resource file 
     * (so that they can be translated, proofed, etc).*/
    /**
     * Log per debug in produzione
     */
    protected static Logger LOG = Logger.getLogger(Main.class.getName());
    /**
     * Costante parlante per impostare il livello di voci di menu
     * che non hanno un livello superiore (sono padri di sottomenu)
     */
    public static final int MAIN_MENU = 0;
    /**
     * Costante parlante per impostare il livello di voci di sottomenu
     * che hanno un solo livello superiore (padre di livello 0)
     */
    public static final int SUB_MENU = 1;
    /**
     * Pagina a cui la command reindirizza per mostrare la form di login
     */
    private static final String nomeFileElenco = "/jsp/login.jsp";
    /**
     * Pagina a cui la command reindirizza per mostrare la form dell'utenza
     */
    private static final String nomeFileProfilo = "/jsp/profile.jsp";
    /**
     * Pagina a cui la command reindirizza per mostrare la password resettata
     */
    private static final String nomeFileReset = "/jsp/profileReset.jsp";
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
        // Recupera o inizializza 'tipo pagina'   
        String whichPw = parser.getStringParameter("pwd", "-");
        // Recupera o inizializza 'tipo pagina'   
        boolean userCanReset = false;
        ArrayList<PersonBean> userList = null;
        String password = null;
        // Variabile contenente l'indirizzo alla pagina di reindirizzamento
        String redirect = null;
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
                Vector<CodeBean> ruoliUsr = user.getRuoli();
                if (write) {
                    /* **************************************** *
                     *          UPDATE Profile User             *
                     * **************************************** */
                    if (whichPw.equals("-")) {
                        String passwd = parser.getStringParameter("txtPwd", Utils.VOID_STRING);
                        String passwdConf = parser.getStringParameter("txtConfPwd", Utils.VOID_STRING);
                        if (passwd != Utils.VOID_STRING && passwdConf != Utils.VOID_STRING && passwd.equals(passwdConf)) {
                            String salt = SessionManager.generateSalt(SessionManager.SALT_LENGTH);
                            String encryptedPassword = SessionManager.hashPassword(passwd, salt);
                            db.updatePassword(user, encryptedPassword, salt);
                        }
                    }
                    /* **************************************** *
                     *          RESET password user             *
                     * **************************************** */
                    else {
                        for (CodeBean ruoloUsr: ruoliUsr) {
                            if (ruoloUsr.getOrdinale() == 1 || ruoloUsr.getOrdinale() == 2) {
                                PasswordGenerator passwordGenerator = new PasswordGenerator.PasswordGeneratorBuilder()
                                                                                           .useDigits(true)
                                                                                           .useLower(true)
                                                                                           .useUpper(true)
                                                                                           .usePunctuation(true)
                                                                                           .build();
                                password = passwordGenerator.generate(8);
                                int userModified = parser.getIntParameter("pwd-usr");
                                db.updatePassword(userModified, user, password);
                            }
                        }
                    }
                }
                /* **************************************************** *
                 *                  SELECT Profile Part                 *
                 * **************************************************** */
                if (whichPw.equals("-")) {
                    projectsByRole = db.getProjectsByRole(user.getId());
                    accessList = db.getAccessLog(user.getId());
                    for (CodeBean ruoloUsr: ruoliUsr) {
                        if (ruoloUsr.getOrdinale() == 1) {
                            userCanReset = true;
                            userList = db.getUsrByGrp(user);
                            break;
                        } else if (ruoloUsr.getOrdinale() == 2) {
                            userCanReset = true;
                            userList = db.getUsrByGrp(user);
                        }
                    }
                    fileJspT = nomeFileProfilo;
                } else {
                    fileJspT = nomeFileReset;
                }
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
        // Imposta il testo del Titolo da visualizzare prima dell'elenco
        req.setAttribute("titoloE", "Progetti di eccellenza: login");
        // Imposta nella request il permesso di reset password dell'utente loggato
        req.setAttribute("resetPwd", userCanReset);
        // Imposta nella request la password modificata in chiaro
        if (password != null) {
            req.setAttribute("password", password);
        }
        // Imposta nella request la lista di utenti per ogni PMO, nel caso in cui siano valorizzati
        if (userList != null) {
            req.setAttribute("userList", userList);
        }
        // Imposta nella request i progetti dell'utente tramite il ruolo, nel caso in cui siano valorizzati
        if (projectsByRole != null) {
            req.setAttribute("projectsByRole", projectsByRole);
        }
        // Imposta nella request il log degli accessi se l'utente ne ha i privilegi
        if (accessList != null) {   // Se l'utente non ne ha il privilegio, la lista è nulla
            req.setAttribute("accesslog", accessList);
        }
        if (redirect != null) {
            req.setAttribute("redirect", redirect);
        }
        // Imposta la Pagina JSP di forwarding
        req.setAttribute("fileJsp", fileJspT);
    }
    
    
    /**
     * <p>Restituisce l'utente loggato, se lo trova nella sessione utente, 
     * altrimenti lancia un'eccezione.</p>
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
    
    
    /**
     * <p>Restituisce una struttura vettoriale <cite>(by the way: 
     * with insertion order)</cite> contenente le voci principali
     * del (mega)menu orizzontale del sito di gestione dei progetti 
     * on-line <em>(pol)</em>.
     *  
     * @param appName nome della web application, seguente la root
     * @param projId identificativo del progetto corrente oppure del progetto di default in caso di accesso utente a piu' di un progetto 
     * @return <code>LinkedList&lt;ItemBean&gt;</code> - struttura vettoriale, rispettante l'ordine di inserimento, che contiene le voci di primo livello
     * @throws CommandException nel caso in cui si verifichi un problema nel recupero di un attributo obbligatorio, o in qualche altro tipo di puntamento
     */
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
        vO.setInformativa("Il Project Charter rappresenta la &quot;carta d\'identut&agrave;&quot; del progetto");
        vO.setUrl(appName + "/?q=" + vO.getNome() + "&p=" + vO.getNomeReale() + "&id=" + projId);
        vO.setIcona("pc.png");
        vO.setLivello(MAIN_MENU);
        mO.add(vO);
        // STATUS
        vO = null;
        vO = new ItemBean();
        vO.setId(2);
        vO.setNome("pol");
        vO.setNomeReale("prj");
        vO.setLabelWeb("Status");
        vO.setInformativa("Lo status, o avanzamento di progetto, &egrave; il punto della situazione del progetto a un dato intervallo di tempo");
        vO.setUrl(makeUrl(appName, vO, "prj", projId));
        vO.setIcona("sts.png");
        vO.setLivello(MAIN_MENU);
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
        vO.setLivello(MAIN_MENU);
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
        vO.setLivello(MAIN_MENU);
        mO.add(vO);
        // REPORT
        vO = null;
        vO = new ItemBean();
        vO.setId(5);
        vO.setNome("pol");
        vO.setLabelWeb("Report");
        vO.setInformativa("Il report di progetto &egrave; una rappresentazione sintetica del lavoro svolto a una certa data"); 
        vO.setUrl(appName + "/?q=" + vO.getNome() + "&p=rep&id=" + projId);
        vO.setIcona("rep.png");
        vO.setLivello(MAIN_MENU);
        mO.add(vO);
        return mO;
    }
    
    
    /**
     * <p>Restituisce una <code>tabella hash</code> contenente i sottomenu 
     * di voci di livello superiore, le quali svolgono anche la funzione 
     * di chiavi della tabella stessa.</p>
     * <p>La <code>tabella hash</code> ha come chiave un oggetto 
     * di tipo <code>ItemBean</code> che rappresenta la voce principale, e come valore una struttura vettoriale
     * ordinata, contenente le voci del sottomenu che alla chiave 
     * fa riferimento.<br />
     * Naturalmente, l'oggetto che fa da chiave implementa <code>l'Override</code>
     * dei metodi necessari all'impiego come chiave, appunto, di tabella hash: 
     * <pre>equals(), hashCode()</pre> &ndash;
     * oltre, a fare l'Override di altri metodi, utili per gli ordinamenti:
     * <pre>compareTo(), toString()</pre></p>
     * <p>La decisione circa il contenuto del menu da generare &egrave; presa
     * in base al seguente ragionamento:
     * <dl>
     * <dt>O il numero di progetti &egrave; = 1</dt>
     * <dd>(tipicamente ruoli TL, User - ma potrebbe essere anche il caso di 
     * un PM di un dipartimento con un solo progetto) &ndash;
     * in questo caso genera le voci di menu come link alle sottosezioni
     * delle varie sezioni (quindi sottosezioni del project charter, delle wbs,
     * etc.)
     * <dt>Oppure il numero di progetti è > 1</dt>
     * <dd>(tipicamente ruoli PM, PMOx, ma potrebbe essere anche il caso di 
     * un TL o uno User su pi&uacute; progetti) &ndash;
     * e in tal caso genera come label delle voci il nome di ogni progetto
     * e come url il link alla prima pagina (landing di default) della
     * sezione a cui la voce chiave fa riferimento (quindi, p.es., per 
     * il project charter la sezione della vision, per le wbs la pagina
     * di elenco delle wbs, e così via).</dd>
     * </dl>
     * Diciamo che il numero di progetti dell'utente non permette 
     * di fare assunzioni sul ruolo dell'utente stesso: un utente 
     * potrebbe essere un semplice User su parecchi progetti
     * così come un PM o un PMO di dipartimento potrebbe avere un solo progetto
     * semplicemente perch&eacute; il suo dipartimento ne ha prodotto uno solo!<br />
     * Tuttavia, noi non facciamo assunzioni sul ruolo in base al numero di progetti
     * ma solo sul menu, perch&eacute; &egrave; evidente che possiamo 
     * generare il menu con le voci dirette alle varie pagine solo se 
     * siamo in presenza di un solo progetto; se l'utente ha parecchi 
     * progetti, dobbiamo generare la lista di progetti.<br />
     * Quanto alla presenza dei "tastini" speciali per il monitoraggio, l&iacute;
     * &egrave; effettivamente una questione di ruolo, per cui il discrimine 
     * non &egrave; pi&uacute; il numero di progetti utente quanto il ruolo 
     * dell'utente stesso; tuttavia potrebbe non aver senso generare una 
     * voce di menu principale per le funzioni di monitoraggio, che sono 
     * piuttosto &quot;di nicchia&quot; in quanto destinate ad utenti molto
     * particolari, quali i PMO.</p>
     * 
     * @param usr utente loggato, in funzione del quale bisogna generare il menu
     * @param appName nome della web application, seguente la root, per la corretta generazione dei link delle voci
     * @return <code>LinkedHashMap&lt;ItemBean, ArrayList&lt;ItemBean&gt;&gt; - tabella hash contenente il menu completo, costituita da una chiave che contiene i dati della voce principale ed un valore che contiene la lista delle sue voci
     * @throws CommandException se si verifica un problema 
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
            ProjectBean project = null;
            int projId = Utils.DEFAULT_ID;
            LinkedList<ItemBean> titles = null;
            Vector<ProjectBean> projects = db.getProjects(usr.getId(), Query.GET_ALL);
            if (!projects.isEmpty()) {
                project = projects.elementAt(Query.NOTHING);
                projId = project.getId();
                titles = makeMenuOrizzontale(appName, projId);
            }
            // Gestisce tutti casi: 
            if (projects.size() == 1) { // O il numero di progetti è = 1 
                // PROJECT CHARTER
                title = titles.getFirst();
                vV = new ArrayList<ItemBean>();
                // Vision
                item = new ItemBean("pcv", "Vision", makeUrl(appName, title, "pcv", projId), Utils.VOID_STRING, "fas fa-file-invoice", SUB_MENU);
                vV.add(item);
                // Stakeholder
                item = null;
                item = new ItemBean("pcs", "Stakeholder", makeUrl(appName, title, "pcs", projId), Utils.VOID_STRING, "far fa-handshake", SUB_MENU);
                vV.add(item);
                // Deliverable
                item = null;
                item = new ItemBean("pcd", "Deliverable", makeUrl(appName, title, "pcd", projId), Utils.VOID_STRING, "fas fa-shipping-fast", SUB_MENU);
                vV.add(item);
                // Risorse
                item = null;
                item = new ItemBean("pcr", "Risorse", makeUrl(appName, title, "pcr", projId), Utils.VOID_STRING, "fas fa-user-friends", SUB_MENU);
                vV.add(item);
                // Rischi
                item = null;
                item = new ItemBean();
                item.setNome("ris");
                item.setLabelWeb("Rischi");
                item.setUrl(appName + "/?q=" + item.getNome() + "&id=" + projId);
                item.setIcona("fas fa-radiation-alt");
                item.setLivello(SUB_MENU);
                vV.add(item);
                // Vincoli
                item = null;
                item = new ItemBean("pcc", "Vincoli", makeUrl(appName, title, "pcc", projId), Utils.VOID_STRING, "fas fa-link", SUB_MENU);
                vV.add(item);
                // Milestone
                item = null;
                item = new ItemBean();
                item.setNome("pcm");
                item.setLabelWeb("Milestone");
                item.setUrl(appName + "/?q=act&p=" + item.getNome() + "&id=" + projId);
                item.setLivello(SUB_MENU);
                item.setIcona("far fa-gem");
                vV.add(item);
                vO.put(title, vV);
                // STATUS
                title = null;
                title = titles.get(1);
                vV = new ArrayList<ItemBean>();
                item = null;
                ArrayList<StatusBean> projectStatusList = db.getStatusList(projId, usr);
                for (StatusBean s : projectStatusList) {
                    item = new ItemBean("sts", 
                                        "Avanzamento dal " + Utils.format(s.getDataInizio()) + " al " + Utils.format(s.getDataFine()), 
                                        appName + "/?q=" + title.getNome() + "&p=" + "sts" + "&id=" + projId + "&ids=" + s.getId(),
                                        Utils.VOID_STRING,
                                        "far fa-clock",
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
                item = new ItemBean("wbs", "WBS", title.getUrl(), Utils.VOID_STRING, "fas fa-sitemap", SUB_MENU);
                vV.add(item);
                // Grafico
                item = null;
                item = new ItemBean("gra", "Rappresentazione grafica delle WBS", makeUrl(appName, title, "gra", projId), Utils.VOID_STRING, "fas fa-network-wired", SUB_MENU);
                vV.add(item);
                // Report di WorkPackage
                item = null;
                item = new ItemBean("rep", "Report di Work Package", makeUrl(appName, title, "rep", projId), Utils.VOID_STRING, "fas fa-chart-line", SUB_MENU);
                vV.add(item);
                vO.put(title, vV);
                // ATTIVITA'
                title = null;
                title = titles.get(3);
                vV = new ArrayList<ItemBean>();
                // Attività
                item = null;
                item = new ItemBean("act", "Attivit&agrave;", title.getUrl(), Utils.VOID_STRING, "fas fa-bars", SUB_MENU);
                vV.add(item);
                // Grafico
                item = null;
                item = new ItemBean("gra", "Rappresentazione grafica delle attivit&agrave;", makeUrl(appName, title, "gra", projId), Utils.VOID_STRING, "fas fa-code-branch", SUB_MENU);
                vV.add(item);
                // Cestino
                item = null;
                item = new ItemBean("bin", "Cestino attivit&agrave;", makeUrl(appName, title, "bin", projId), Utils.VOID_STRING, "far fa-trash-alt", SUB_MENU);
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
                /*;*/ // Il caso in cui non ci siano progetti viene gestito passando il menù vuoto
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
    
    
    /**
     * <p>Restituisce una String che rappresenta un url da impostare in una
     * voce di menu, il cui padre viene passato come argomento, come 
     * analogamente la web applicazion seguente la root ed un eventuale
     * parametro aggiuntivo.</p>
     * 
     * @param appName nome della web application, seguente la root, per la corretta generazione dell'url
     * @param title voce di livello immediatamente superiore alla voce per la quale si vuol generare l'url
     * @param p eventuale valore del parametro 'p' della Querystring
     * @param prjId identificativo del progetto entro il cui contesto si vuol generare l'url
     * @return <code>String</code> - url ben formato e valido, da applicare a una voce di menu
     * @throws CommandException se si verifica un problema nell'accesso a qualche parametro o in qualche altro puntamento
     */
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