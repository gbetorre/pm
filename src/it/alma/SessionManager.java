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
import it.alma.bean.PersonBean;
import it.alma.bean.ProjectBean;
import it.alma.bean.RiskBean;
import it.alma.bean.SkillBean;
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
     * Log per debug in produzione
     */
    protected static Logger LOG = Logger.getLogger(Main.class.getName());
    /**
     * <p>Nome e percorso della pagina di errore cui ridirigere in caso
     * di eccezioni rilevate.</p>
     */
    private String errorJsp;
    
    
    /** 
     * Inizializza (staticamente) alcune variabili globali 
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
        //doPost(req, res);
        try {
            // Recupera la sessione creata e valorizzata per riferimento nella req dal metodo authenticate
            HttpSession session = req.getSession(Query.IF_EXISTS_DONOT_CREATE_NEW);
            session.invalidate();
            final RequestDispatcher rd = getServletContext().getRequestDispatcher("/jsp/login.jsp");
            rd.forward(req, res);
        } catch (IllegalStateException e) {
            throw new ServletException("Impossibile redirigere l'output. Verificare se la risposta e\' stata gia\' committata.\n" + e.getMessage(), e);
        } catch (NullPointerException e) {
            throw new ServletException("Errore nell'estrazione dei dipartimenti che gestiscono il corso.\n" + e.getMessage(), e);
        } catch (Exception e) {
            ;
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
        //String fileJsp = null;      // Pagina in cui riversare l'output prodotto
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
                Vector<ProjectBean> userWritableProjects = db.getWritableProjects(username);
                LinkedHashMap<Integer, Vector<ActivityBean>> userWritableActivitiesByProject = new LinkedHashMap<Integer, Vector<ActivityBean>>();
                LinkedHashMap<Integer, Vector<SkillBean>> userWritableSkillsByProject = new LinkedHashMap<Integer, Vector<SkillBean>>();
                LinkedHashMap<Integer, Vector<RiskBean>> userWritableARisksByProject = new LinkedHashMap<Integer, Vector<RiskBean>>();
                for (ProjectBean writablePrj : userWritableProjects) {
                    int idPrj = writablePrj.getId();
                    Integer key = new Integer(idPrj);
                    Vector<ActivityBean> userWritableActivities = db.getActivities(idPrj);
                    userWritableActivitiesByProject.put(key, userWritableActivities);
                    Vector<SkillBean> userWritableSkills = db.getSkills(idPrj);
                    userWritableSkillsByProject.put(key, userWritableSkills);
                    Vector<RiskBean> userWritableRisks = db.getRisks(idPrj);
                    userWritableARisksByProject.put(key, userWritableRisks);
                }
                session.setAttribute("writableProjects", userWritableProjects);
                session.setAttribute("writableActivity", userWritableActivitiesByProject);
                session.setAttribute("writableSkills", userWritableSkillsByProject);
                session.setAttribute("writableRisks", userWritableARisksByProject);
                res.sendRedirect(res.encodeRedirectURL(getServletContext().getInitParameter("appName") + "/?q=pol"));
            }
        } catch (IllegalStateException e) {
            throw new ServletException("Impossibile redirigere l'output. Verificare se la risposta e\' stata gia\' committata.\n" + e.getMessage(), e);
        } catch (NullPointerException e) {
            throw new ServletException("Errore nell'estrazione dei dipartimenti che gestiscono il corso.\n" + e.getMessage(), e);
        } catch (Exception e) {
            //session.setAttribute("error", true);
            //session.setAttribute("msg", msg);
            //Log dell'evento
            LOG.severe("Oggetto Vector<elencoDipartCs> non valorizzato; L'username passato come parametro non ha associato alcun progetto"); 
        }
        //final RequestDispatcher rd = getServletContext().getRequestDispatcher(fileJsp + "?" + req.getQueryString());
        //rd.forward(req, res);
    }
    

    /**
     * <p>Crea la sessione utente.<br />
     * Inserisce la sessione creata nella HttpServletRequest, modificandola
     * per riferimento <code>ByRef</code>.</p>
     * 
     * @param matricola
     * @param idCorso
     * @param aa
     * @param req
     * @param db
     * @return
     * @throws CommandException
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
            }
        }
        return authenticated;
    }
    
}