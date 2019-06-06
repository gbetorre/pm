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

package it.alma;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Vector;
import java.util.logging.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.oreilly.servlet.ParameterParser;

import it.alma.bean.ActivityBean;
import it.alma.bean.DepartmentBean;
import it.alma.bean.ItemBean;
import it.alma.bean.PersonBean;
import it.alma.bean.ProjectBean;
import it.alma.bean.RiskBean;
import it.alma.bean.SkillBean;
import it.alma.command.HomePageCommand;
import it.alma.exception.AttributoNonValorizzatoException;
import it.alma.exception.CommandException;
import it.alma.exception.WebStorageException;


/**
 * <p>SessionManager &egrave; la classe che si occupa dell'autenticazione
 * dell'utente, dell'impostazione dei privilegi, di eventuali cookies, 
 * del logout e di quan'altro occorra alle funzioni di gestione utenti 
 * delle web-application <code>aol e pol</code>.</p>
 * 
 * @author <a href="mailto:giovanroberto.torre@univr.it">Giovanroberto Torre</a>
 */
public class SessionManager extends HttpServlet {
    
    /**
     * La serializzazione necessita della dichiarazione 
     * di una costante di tipo long identificativa della versione seriale. 
     * (Se questo dato non fosse inserito, verrebbe calcolato in maniera automatica
     * dalla JVM, e questo potrebbe portare a errori riguardo alla serializzazione). 
     */
    private static final long serialVersionUID = 1L;
    /**
     *  Nome di questa classe 
     *  (utilizzato per contestualizzare i messaggi di errore)
     */
    static final String FOR_NAME = "\n" + Logger.getLogger(new Throwable().getStackTrace()[0].getClassName()) + ": ";
    /**
     * Logger della classe per scrivere i messaggi di errore  
     */
    private Logger log = Logger.getLogger(SessionManager.class.getName());
    /**
     * Log per debug in produzione
     */
    protected static Logger LOG = Logger.getLogger(Main.class.getName());
    /**
     * <p>Nome e percorso della pagina di errore cui ridirigere in caso
     * di eccezioni rilevate.</p>
     */
    private static String errorJsp;
    /**
     * <p>Nome del template in cui vengono assemblati i vari 'pezzi'
     * che compongono l'output html finale.</p>
     */
    private static String templateJsp;
    
    
    /** 
     * <p>Inizializza, staticamente, alcune variabili globali.</p> 
     * 
     * @param config la configurazione usata dal servlet container per passare informazioni alla servlet <strong>durante l'inizializzazione</strong>
     * @throws ServletException una eccezione che puo' essere sollevata quando la servlet incontra difficolta'
     */
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        /*
         * Nome della pagina di errore
         */
        errorJsp = getServletContext().getInitParameter("errorJsp");
        if (errorJsp == null)
            throw new ServletException(FOR_NAME + "\n\nManca il parametro di contesto 'errorJsp'!\n\n");
        /*
         * Nome del template da invocare per l'assemblaggio dei vari componenti 
         * dell'output - nel contesto della servlet di autenticazione 
         * serve piu' che altro per redirigere sulla maschera di login
         * in caso di invalidamento della sessione 
         */
        templateJsp = getServletContext().getInitParameter("templateJsp");
        if (templateJsp == null) {
            throw new ServletException(FOR_NAME + "\n\nManca il parametro di contesto 'templateJsp'!\n\n");
        }
    }
    
    
    /** 
     * <p>Gestisce le richieste del client effettuate con il metodo GET.</p>
     * 
     * @param req la HttpServletRequest contenente la richiesta del client
     * @param res la HttpServletResponse contenente la risposta del server
     * @throws ServletException eccezione che viene sollevata se si verifica un problema nell'inoltro (forward) della richiesta/risposta
     * @throws IOException      eccezione che viene sollevata se si verifica un problema nell'inoltro (forward) della richiesta/risposta
     * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doGet(HttpServletRequest req, 
                         HttpServletResponse res)
                  throws ServletException, IOException {
        try {
            // Recupera la sessione creata e valorizzata per riferimento nella req dal metodo authenticate
            HttpSession session = req.getSession(Query.IF_EXISTS_DONOT_CREATE_NEW);
            PersonBean user = (PersonBean) session.getAttribute("usr");
            String msg = "Ha effettuato il logout l\'utente: " + 
                         user.getNome() + Utils.BLANK_SPACE + user.getCognome() + 
                         " in data"  + Utils.BLANK_SPACE + Utils.format(Utils.getCurrentDate()) +
                         " alle ore" + Utils.BLANK_SPACE + Utils.getCurrentTime() +
                         ".\n";
            log.info(msg);
            session.invalidate();
            final RequestDispatcher rd = getServletContext().getRequestDispatcher(templateJsp);
            rd.forward(req, res);
        } catch (IllegalStateException ise) {
            String msg = "Impossibile redirigere l'output. Verificare se la risposta e\' stata gia\' committata.\n" + ise.getMessage();
            log.severe(msg);
            throw new ServletException(msg, ise);
        } catch (NullPointerException npe) {
            String msg = "Problemi nel recupero della sessione.\n" + npe.getMessage();
            log.severe(msg);
            throw new ServletException(msg, npe);
        } catch (Exception e) {
            String msg = "Problemi nel metodo doGet.\n" + e.getMessage();
            log.severe(msg);
            throw new ServletException(msg, e);
        }
    }
    
    
    /**
     * <p>Gestisce le richieste del client effettuate con il metodo POST.</p>
     *  
     * @param req la HttpServletRequest contenente la richiesta del client
     * @param res la HttpServletResponse contenente la risposta del server
     * @throws ServletException eccezione che viene sollevata se si verifica un problema nell'inoltro (forward) della richiesta/risposta
     * @throws IOException      eccezione che viene sollevata se si verifica un problema nell'inoltro (forward) della richiesta/risposta
     */
    @Override
    protected void doPost(HttpServletRequest req, 
                          HttpServletResponse res) 
                   throws ServletException, IOException {
        DBWrapper db = null;                                // DataBound
        ParameterParser parser = new ParameterParser(req);  // Crea il parser per i parametri
        String username = null;                             // Credenziali
        String password = null;                             // Credenziali
        StringBuffer msg = new StringBuffer();              // Messaggio
        // Effettua la connessione al databound
        try {
            db = new DBWrapper();
        } catch (WebStorageException wse) {
            throw new ServletException(FOR_NAME + "Non riesco ad instanziare databound.\n" + wse.getMessage(), wse);
        }
        // Recupera le credenziali
        username = parser.getStringParameter("usr", new String());
        password = parser.getStringParameter("pwd", new String());
        // Flag sessione
        boolean authenticated = false;
        // Crea la sessione stessa, se non c'è già, altrimenti la recupera
        try {
            authenticated = authenticate(username, password, req, db, msg);
        } catch (CommandException ce) {
            throw new ServletException(FOR_NAME + "Non riesco ad identificare l\'utente.\n" + ce.getMessage(), ce);
        }
        try {
            // Recupera la sessione creata e valorizzata per riferimento nella req dal metodo authenticate
            HttpSession session = req.getSession(Query.IF_EXISTS_DONOT_CREATE_NEW);
            if (!authenticated) {
                req.setAttribute("msg", msg);
                req.getRequestDispatcher("/jsp/login.jsp").include(req, res);
            }
            else {
                // Logga anzitutto l'accesso
                traceAccess(req, username, db);
                // Prepara gli altri attributi 
                PersonBean user = (PersonBean) session.getAttribute("usr");
                Vector<DepartmentBean> userWritableDepts = db.getWritableDeparts(username);
                Vector<ProjectBean> userWritableProjects = db.getWritableProjects(username);
                LinkedHashMap<Integer, Vector<ActivityBean>> userWritableActivitiesByProject = new LinkedHashMap<Integer, Vector<ActivityBean>>();
                LinkedHashMap<Integer, Vector<SkillBean>> userWritableSkillsByProject = new LinkedHashMap<Integer, Vector<SkillBean>>();
                LinkedHashMap<Integer, Vector<RiskBean>> userWritableARisksByProject = new LinkedHashMap<Integer, Vector<RiskBean>>();
                for (ProjectBean writablePrj : userWritableProjects) {
                    int idPrj = writablePrj.getId();
                    Integer key = new Integer(idPrj);
                    Vector<ActivityBean> userWritableActivities = db.getActivities(idPrj, user);
                    userWritableActivitiesByProject.put(key, userWritableActivities);
                    Vector<SkillBean> userWritableSkills = db.getSkills(idPrj, user);
                    userWritableSkillsByProject.put(key, userWritableSkills);
                    Vector<RiskBean> userWritableRisks = db.getRisks(idPrj, user);
                    userWritableARisksByProject.put(key, userWritableRisks);
                }
                // Prepara il menu
                LinkedHashMap<ItemBean, ArrayList<ItemBean>> vO = HomePageCommand.makeMegaMenu((PersonBean) session.getAttribute("usr"), getServletContext().getInitParameter("appName"));
                // Valorizza in sessione utente tutto ciò che ha preparato
                session.setAttribute("writableDeparments", userWritableDepts);
                session.setAttribute("writableProjects", userWritableProjects);
                session.setAttribute("writableActivity", userWritableActivitiesByProject);
                session.setAttribute("writableSkills", userWritableSkillsByProject);
                session.setAttribute("writableRisks", userWritableARisksByProject);
                session.setAttribute("menu", vO);
                res.sendRedirect(res.encodeRedirectURL(getServletContext().getInitParameter("appName") + "/?q=pol"));
            }
        } catch (IllegalStateException ise) {
            throw new ServletException("Impossibile redirigere l'output. Verificare se la risposta e\' stata gia\' committata.\n" + ise.getMessage(), ise);
        } catch (NullPointerException npe) {
            throw new ServletException("Errore nell'estrazione dei dipartimenti che gestiscono il corso.\n" + npe.getMessage(), npe);
        } catch (Exception e) {
            //session.setAttribute("error", true);
            //session.setAttribute("msg", msg);
            //Log dell'evento
            LOG.severe("Oggetto PersonBean non valorizzato; l\'username passato come parametro non ha associato alcun progetto.\n");
        }
        //final RequestDispatcher rd = getServletContext().getRequestDispatcher(fileJsp + "?" + req.getQueryString());
        //rd.forward(req, res);
    }
    

    /**
     * <p>Crea la sessione utente.<br />
     * Inserisce la sessione creata nella HttpServletRequest, modificandola
     * per riferimento <code>ByRef</code>.</p>
     * 
     * @param username nome utente inserito ai fini di login
     * @param password password inserita ai fini di login
     * @param req   HttpServletRequest per la creazione della sessione
     * @param db    DataBound per la query riguardo le credenziali
     * @param message messaggio per l'output circa l'esito della login 
     * @return <code>boolean</code> - true se l'autenticazione e' andata a buon fine, false in caso contrario
     * @throws CommandException se si verifica un problema nel recupero dell'utente in base alle credenziali fornite
     */
    public static boolean authenticate(String username, 
                                       String password, 
                                       HttpServletRequest req, 
                                       DBWrapper db, 
                                       StringBuffer message) 
                                throws CommandException {
        boolean authenticated = false;
        HttpSession session = req.getSession();
        // Se la sessione non è nuova ci sono già dentro dei valori
        if (session.getAttribute("usr") != null) {
            authenticated = true;
        }
        else {  
            // Interroga il database a proposito dell'utente
            try {
                PersonBean user = db.getUser(username, password);
                if (user != null) {                         
                    message.append("Benvenuto" + user.getNome());
                    session.setAttribute("msg", message);
                    session.setAttribute("error", false);
                    session.setAttribute("usr", user);
                    authenticated = true;
                } else {
                    message.append("Errore di autenticazione. Ricontrollare Username e Password." );
                    session.setAttribute("msg", message);
                    session.setAttribute("error", true);
                    authenticated = false;
                }
            } catch (WebStorageException wse) {    
                String msg = FOR_NAME + "Non riesco a determinare l\'utente";
                LOG.severe(msg);
                throw new CommandException(msg + wse.getMessage(), wse);
            } catch (AttributoNonValorizzatoException anve) {
                String msg = FOR_NAME + "Attributo della persona non valorizzato.\n";
                LOG.severe(msg);
                throw new CommandException(msg + anve.getMessage(), anve);
            } catch (NullPointerException npe) {
                String msg = FOR_NAME + "Oggetto persona non valorizzato.\n";
                LOG.severe(msg);
                throw new CommandException(msg + npe.getMessage(), npe);
            }
        }
        return authenticated;
    }
    
    
    /**
     * <p>Prepara le informazioni da registrare nel database per tracciare
     * l'evento di login di un determinato utente, passato come argomento.<br />
     * Chiama il metodo del model, un cui riferimento viene passato come
     * argomento, che scriver&agrave; nel database i dati  dell'accesso 
     * &ndash; o ne aggiorner&agrave; gli estremi nel caso in cui 
     * l'utente si fosse precedentemente gi&agrave; loggato.</p>
     * 
     * @param req HttpServletRequest contenente la richiesta fatta dal client, i cui estremi devono essere riportati nel database
     * @param username nome utente loggato
     * @param db riferimento al model
     * @throws WebStorageException nel caso in cui, per un qualunque motivo, l'operazione di inserimento o aggiornamento non vada a buon fine
     * @throws UnknownHostException nel caso in cui il tentativo di risolvere l'host nel contesto della creazione di un InetAddress non vada a buon fine
     * @throws RuntimeException nel caso in cui si verifichi un problema in qualche tipo di puntamento (p.es. un puntamento a null)
     */
    private static void traceAccess(HttpServletRequest req, 
                                    String username, 
                                    DBWrapper db) 
                             throws WebStorageException, 
                                    UnknownHostException, 
                                    RuntimeException {
        StringBuffer ip = new StringBuffer(req.getRemoteAddr());
        StringBuffer remoteHost = new StringBuffer(req.getRemoteHost());
        String server = req.getServerName();
        // Recupera il browser
        if (req.getHeader("user-agent") != null) {
            remoteHost = new StringBuffer(req.getHeader("user-agent"));
        }
        // Recupera il vero IPv4 dell'utente
        if (req.getHeader("x-real-ip") != null) {
            ip = new StringBuffer(req.getHeader("x-real-ip"));
        }
        // Se non riesce a recuperare il vero IP verifica se è quello del NAT
        else if (String.valueOf(ip).equalsIgnoreCase("0:0:0:0:0:0:0:1")) {
            // In tal caso tenta di recuperare l'IP dall'oggetto InetAddress
            InetAddress inetAddress = InetAddress.getLocalHost();
            remoteHost = new StringBuffer(inetAddress.getHostName());
            String ipAddress = inetAddress.getHostAddress();
            ip = new StringBuffer(ipAddress);
        }
        db.manageAccess(username, ip, remoteHost, server);
    }
    
}