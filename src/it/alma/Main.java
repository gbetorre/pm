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
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.alma.bean.ItemBean;
import it.alma.bean.PersonBean;
import it.alma.command.Command;
import it.alma.exception.CommandException;
import it.alma.exception.WebStorageException;


/**
 * <p>Main &egrave; la classe principale della web-application 
 * <code>Alma on Line</code>.</p>
 * 
 * @author <a href="mailto:giovanroberto.torre@univr.it">Giovanroberto Torre</a>
 */
public class Main extends HttpServlet {
    
    /**
     * La serializzazione necessita della dichiarazione 
     * di una costante di tipo long identificativa della versione seriale. 
     * (Se questo dato non fosse inserito, verrebbe calcolato in maniera automatica
     * dalla JVM, e questo potrebbe portare a errori riguardo alla serializzazione). 
     */
    private static final long serialVersionUID = 1L;
    /**
     * <p>Logger della classe per scrivere i messaggi di errore.</p> 
     * <p>All logging goes through this logger.</p>
     * <p>To avoid the 'Read access to enclosing field Main.log 
     * is emulated by a synthetic accessor method' warning, 
     * the visibility is changed to 'friendly' (id est 'default', 
     * id est 'visible from the same package').</p>
     * <p>Spiegazione, per chi non "mastica" l'inglese (o Java, o
     * <ul><li>non ha il cervello</li><li>&egrave; uno zombie</li>
     * <li>&egrave; l'Abate Far&iacute;a</li><li>&egrave; un pacco
     * raccomandato</li>):</ul>
     * non &egrave; privata ma Default (friendly) per essere visibile 
     * negli elementi ovverride implementati da questa classe.</p>
     *  
     */
    /* default */ static Logger log = Logger.getLogger(Main.class.getName());
    /**
     *  Nome di questa classe 
     *  (utilizzato per contestualizzare i messaggi di errore)
     */
    static final String FOR_NAME = "\n" + Logger.getLogger(new Throwable().getStackTrace()[0].getClassName()) + ": ";
    /**
     * <p>DataBound.</p>
     * <p>Viene definito come:<dl>
     * <dt>variabile statica</dt>
     * <dd>per poterla utilizzare nei blocchi statici
     * (p.es. blocco statico di inizializzazione)</dd>
     * <dt>variabile di classe</dt>
     * <dd>per poterla valorizzare in inizializzazione e poterla utilizzare 
     * poi in tutto il codice</dd>
     * <dt>variabile inizializzata</dt>
     * <dd>per agevolare l'applicazione del <code>pattern Singleton</code>, 
     * che deve essere utilizzato ad ogni possibile istanziazione, 
     * evitando in tal modo la generazione di istanziazioni multiple.</dd></dl>
     * <small>NOTA: Le variabili locali, sia di metodo sia di blocco, 
     * <cite id="horton">vanno sempre inizializzate, all'atto 
     * della loro definizione. Tuttavia ci&ograve; non vale per 
     * le variabili di classe e di istanza, che possono essere soltanto
     * dichiarate nel contesto della definizione della classe e di cui va
     * curata l'inizializzazione successiva.</cite> 
     * In questo caso, per prudenza, si deroga a questa regola 
     * per i motivi dianzi detti.</small> 
     * <p>La variabile non &egrave; privata ma Default per gli stessi motivi 
     * per i quali la visibilit&agrave; dell'istanza di Logger non &egrave; 
     * privata; lasciarla privata provocherebbe un:
     * <pre>Write access to enclosing field Main.db is emulated by a synthetic accessor method</pre>
     * cio&egrave; a dire che &quot;lui&quot; farebbe un metodo 
     * <code>getDb()</code> dietro le quinte per garantire l'accesso al campo
     * privato; preferibile, quindi, aprire direttamente la visibilit&agrave; 
     * a livello di package, come peraltro &quot;lui&quot; stesso suggerisce:<br />
     * <code>Quick fix:</code>
     * <pre>Change visiblility of 'db' to 'package'</pre></p>
     */
    /* default */ static DBWrapper db = null;
    /**
     * Tabella hash (dictionary) contenente le command predefinite.
     */
    private ConcurrentHashMap<String, Command> commands;
    /**
     * Nome e percorso della pagina di errore cui ridirigere in caso
     * di eccezioni rilevate.
     */
    private static String errorJsp;
    /**
     * <p>Nome del parametro di inizializzazione, valorizzato nel 
     * descrittore di deploy, che identifica la command cui la Main deve
     * girare la richiesta. </p>
     * Storicamente vale 'ent', e di esso &egrave; 
     * da ricercare il valore nella QueryString.
     */
    private static String entToken;
    /**
     * <p>Nome del parametro di inizializzazione, valorizzato nel 
     * descrittore di deploy, che identifica il nome della pagina jsp
     * che rappresenta la home page del sito locale (nel caso presente, 
     * <code>aol</code>).</p>
     * Storicamente vale 'homepage', e il valore di questo parametro &egrave;
     * a sua volta 'homepage.jsp', ma in aol questo meccanismo &egrave;
     * stato baipassato dalla definizione di un file index.jsp
     * (storicamente default.jsp), posto
     * direttamente nella root application e puntato in maniera esplicita
     * (o a cui si viene rediretti se si punta al dominio univr.it/).
     */
    private static String homePage;
    /**
     * <p>Nome del template in cui vengono assemblati i vari 'pezzi'
     * che compongono l'output html finale.</p>
     */
    private static String templateJsp;
    /**
     * <p>Fatto un nanosecondo pari a 1, la stessa quantit&agrave; di tempo
     * espressa in altre unit&agrave; pu&ograve; essere ricavata 
     * adottando opportuni divisori, come nello schema seguente:
     * <dl>
     * <dt>microsecondi</dt>
     * <dd>10<sup>-3</sup></dd>
     * <dt>millisecondi</dt>
     * <dd>10<sup>-6</sup></dd>
     * <dt>secondi</dt>
     * <dd>10<sup>-9</sup></dd>
     * <dt>minuti</dt>
     * <dd>1,67 × 10<sup>-11</sup></dd>
     * <dt>ore</dt>
     * <dd>2,78 × 10<sup>-13</sup></dd>
     * <dt>giorni</dt>
     * <dd>1,16 × 10<sup>-14</sup></dd>
     * </dl>
     * Pertanto, per convertire un tempo espresso in millisecondi, 
     * quale quello fornito ad esempio dalla classe System, 
     * basta definire numeri aventi la grandezza dei divisori, 
     * e porli al denominatore.</p>
     */
    public static final double SECOND_DIVISOR = 1E9D;
    /**
     * <p>Tempo, in millisecondi, in cui si vuole effettuare il refresh</p>
     * <p>Dalla documentazione della costante per il divisore del tempo trascorso
     * per l'esecuzione del thread, si evince che tra secondi e millisecondi
     * ci sono 3 zeri da aggiungere all'esponente, per cui i millisecondi
     * sono separati dai secondi da "soli" 3 ordini di grandezza; 
     * d'altro canto, un secondo &egrave; composto per definizione
     * da 1 x 10<sup>3</sup> ms.</p>
     * <p>Perci&ograve;, per ottenere il tempo schedulato per il refresh
     * in millisecondi, moltiplichiamo tale tempo in minuti (p.es. 60)
     * per 60 (ottenendo i secondi), ancora per 1000 
     * (ottenendo quindi i millisecondi). Aggiungendo ulteriori fattori
     * possono essere incrementati i tempi di schedulazione.</p>
     */
    static final long SCHEDULED_TIME = 1000 * 60 * 60 * 6;
    /** 
     * Timer per schedulare l'aggiornamento dello stato calcolato 
     * per le attivit&agrave;
     */
    private static Timer updateTimer = new Timer();
    /** 
     * Stringa per il puntamento al db di produzione
     */
    private static StringBuffer contextDbName = new StringBuffer("java:comp/env/jdbc/pol");
    
    
    /**
     * Blocco statico di inizializzazione per lanciare il ricalcolo e 
     * l'aggiornamento degli stati delle attivit&agrave;, calcolati all'atto
     * del salvataggio delle attivit&agrave; stesse, ma da aggiornare 
     * ogni <em>tot</em> time, perch&egrave; lo stato cambia in funzione 
     * del tempo che scorre (p.es. un'attivit&agrave; che ieri doveva essere
     * chiusa, ieri era <cite>&quot;in regola&quot;</cite> ma da oggi, 
     * se non ha ancora data fine effettiva, diventa 
     * <cite>&quot;in ritardo&quot;</cite>.
     */
    static {
        log.info(FOR_NAME + "Blocco statico di inizializzazione. ");
        // Delay: 0 millisecondi
        // Repeat: ogni 6 ore
        updateTimer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                long startTime = System.nanoTime();
                if (db == null) {
                    try {
                        log.info("DBWrapper is being created");
                        db = new DBWrapper();
                        log.info("DBWrapper newborn");
                    } catch (WebStorageException wse) {
                        log.severe(FOR_NAME + "Problema nell\'accesso al database.\n" + wse.getMessage());
                    }
                }
                try {
                    log.info("DBWrapper previously instanced");
                    log.info(FOR_NAME + "N. tuple aggiornate: " + refresh(db));
                } catch (CommandException ce) {
                    log.severe(FOR_NAME + "Problema nell\'aggiornamento degli stati attivita\'.\n" + ce.getMessage());
                }
                long elapsedTime = System.nanoTime() - startTime;
                log.config(FOR_NAME + "Stati attivita\' aggiornati in " + elapsedTime / SECOND_DIVISOR + "\"");
                log.info(FOR_NAME + "End run()");
            }
        }, 0, SCHEDULED_TIME); // Refresh in SCHEDULED_TIME milliseconds
    }

    
    /**
     * Inizializza (staticamente) le variabili globali 
     * che saranno utilizzate anche dalle altre classi:
     * <ul>
     * <li> connessione al database: <code>db</code></li>
     * <li> tabella hash di tutte le classi richiamabili dall'applicazione</li>
     * <li> pagina di errore</li>
     * etc...
     * </ul>
     * 
     * @param config la configurazione usata dal servlet container per passare informazioni alla servlet <strong>durante l'inizializzazione</strong>
     * @throws ServletException una eccezione che puo' essere sollevata quando la servlet incontra difficolta'
     */
    public void init(ServletConfig config) throws ServletException {
        /*
         *  Inizializzazione da superclasse
         */
        super.init(config);
        /* ******************************************************************** *
         *    Lettura dei parametri di configurazione dell'applicazione POL     *
         * ******************************************************************** */
        /*
         * Nome della pagina di errore
         */
        errorJsp = getServletContext().getInitParameter("errorJsp");
        if (errorJsp == null) { 
            throw new ServletException(FOR_NAME + "\n\nManca il parametro di contesto 'errorJsp'!\n\n");
        }
        /*
         * Nome del parametro che identifica la Command da interpellare
         */
        entToken = getServletContext().getInitParameter("entToken");
        if (entToken == null) {
            throw new ServletException(FOR_NAME + "\n\nManca il parametro di contesto 'entToken'!\n\n");
        }
        /*
         * Nome home page
         */
        homePage = getServletContext().getInitParameter("home");
        if (homePage == null) { 
            throw new ServletException(FOR_NAME + "\n\nManca il parametro di contesto 'nomeHomePage'!\n\n");
        }
        /*
         * Nome del template da invocare per l'assemblaggio 
         * dei vari componenti dell'output
         */
        templateJsp = getServletContext().getInitParameter("templateJsp");
        if (templateJsp == null) {
            throw new ServletException(FOR_NAME + "\n\nManca il parametro di contesto 'templateJsp'!\n\n");
        }
        /*
         * Attiva la connessione al database
         */
        // Prima deve capire su quale database deve insistere
        // Di default va in produzione, ma se siamo in locale deve andare in locale
        if (getServletContext().getRealPath("/").contains("\\Programs\\apache-tomcat-8.5.31\\webapps\\almalaurea\\")) {
            contextDbName = new StringBuffer("java:comp/env/jdbc/poldev");
        }
        try {
            db = new DBWrapper();
        }
        catch (WebStorageException wse) {
            throw new ServletException(FOR_NAME + "Non e\' possibile avere una connessione al database.\n" + wse.getMessage(), wse);
        }
        /*
         * Inizializza la tabella <code>commands</code> che deve contenere
         * tutte le classi che saranno richiamabili da questa
         * servlet. Tali classi dovrebbero essere dichiarate in un file
         * di configurazione (p.es. web.xml) o nel database.
         */
        Vector<ItemBean> classiCommand = new Vector<ItemBean>();
        try {
            classiCommand = db.lookupCommand();
        }
        catch (Exception e) {
            throw new ServletException(FOR_NAME + "Problemi nel caricare le classi Command.\n" + e.getMessage(), e);
        }
        ItemBean voceMenu = null;
        Command classCommand = null;
        commands = new ConcurrentHashMap<String, Command>();
        for (int i = 0; i < classiCommand.size(); i++) {
            voceMenu = classiCommand.get(i);
            try {
                classCommand = (Command) Class.forName("it.alma.command." + voceMenu.getNomeClasse()).newInstance();
                classCommand.init(voceMenu);
                commands.put(voceMenu.getNome(), classCommand);
            } catch (ClassNotFoundException cnfe) { // Se non riesce a istanziare la Command in generali la cerca in un sottopackage
                try {
                    classCommand = (Command) Class.forName("it.alma.qol.command." + voceMenu.getNomeClasse()).newInstance();
                    classCommand.init(voceMenu);
                    commands.put(voceMenu.getNome(), classCommand);
                }
                catch (ClassNotFoundException cnfex) {  // Se non riesce neppure nel sottopackage "si arrende"
                    String error = FOR_NAME +
                                   "La classe collegata alla voce menu '" +
                                   voceMenu.getNome() +
                                   "' &egrave; '" +
                                   voceMenu.getNomeClasse() +
                                   " e non pu&ograve; essere caricata: " + cnfex.getMessage();
                    throw new ServletException(error);
                }
                catch (Exception e) {
                    String error = FOR_NAME +
                                   "Problema generico nel caricare la classe " +
                                   voceMenu.getNomeClasse() +
                                   ". Dettaglio errore: " +
                                   e.getMessage();
                    throw new ServletException(error);
                }
            } catch (Exception e) {
                    String error = FOR_NAME +
                                   "Problema generico nel caricare la classe " +
                                   voceMenu.getNomeClasse() +
                                   ".\n Dettaglio errore: " +
                                   e.getMessage();
                    throw new ServletException(error);
            }
        }
    }
    
    
    /**
     * <p>Gestisce le richieste del client.</p>
     * <p><cite id="malacarne" data-exact-page="99">
     *  Il metodo service viene invocato dal servlet-engine come azione
     *  di risposta alla ricezione di una HttpRequest.
     *  Questo metodo, nella sua implementazione originale, funziona
     *  come dispatcher, ossia, in base al codice operazione HTTP ricevuto,
     *  attiva il metodo disponibile pi&uacute; opportuno (...)
     * </cite></p>
     * <cite id="malacarne" data-exact-page="100">
     * <p>Una sottoclasse di HttpServlet dovrebbe preferenzialmente
     *  sovrascrivere uno dei metodi precedenti<br />
     *  (n.d.r.: <code>doGet | doPost | doOption | doPut | doTrace</code>)</p>
     * <p>In taluni casi per&ograve; (...) risulta essere pi&uacute; 
     *  conveniente, <strong>ma deve essere una scelta ben ponderata</strong>, 
     *  sovrascrivere direttamente il metodo service.</p> 
     * </cite>
     * 
     * @param req la HttpServletRequest contenente la richiesta del client
     * @param res la HttpServletResponse contenente la risposta del server
     * @throws ServletException eccezione che viene sollevata se si verifica un problema nell'inoltro (forward) della richiesta/risposta
     * @throws IOException      eccezione che viene sollevata se si verifica un problema nell'inoltro (forward) della richiesta/risposta
     */
    public void doGet(HttpServletRequest req, 
                      HttpServletResponse res) 
               throws ServletException, IOException {
        /*
         * Dichiara la variabile per la pagina in cui riversare l'output prodotto
         */
        String fileJsp = null;
        /*
         * Dichiara le variabili in base a cui ricercare la Command
         */
        String q = null;
        /*
         * Cerca la command associata al parametro 'ent'
         * e, se la trova, ne invoca il metodo execute()
         */
        try {
            q = req.getParameter(entToken);
        } catch (NullPointerException npe) { // Potrebbe già uscire qui
            req.setAttribute("javax.servlet.jsp.jspException", npe);
            log(FOR_NAME + "Problema di puntamento: applicazione terminata!" + npe);
            flush(req, res, errorJsp);
        } catch (NumberFormatException nfe) { // Controllo sull'input
            req.setAttribute("javax.servlet.jsp.jspException", nfe);
            log(FOR_NAME + "Parametro in formato non valido: applicazione terminata!" + nfe);
            flush(req, res, errorJsp);
        } catch (Exception e) { // Just in case
            req.setAttribute("javax.servlet.jsp.jspException", e);
            log(FOR_NAME + "Eccezione generica: " + e);
            flush(req, res, errorJsp);
        }
        try {
            /*
             * Cerca la command associata al parametro 'ent'
             * e, se la trova, ne invoca il metodo execute()
             */
            req.setAttribute("w", false);
            Command cmd = lookupCommand(q);
            cmd.execute(req);
        } catch (CommandException ce) { // Potrebbe già uscire qui
            String msg = FOR_NAME + 
                         "L\'errore e\' stato generato dalla seguente chiamata: " + 
                         req.getQueryString() +
                         ", presente nella pagina: " + 
                         req.getHeader("Referer");
            log.log(Level.WARNING, msg, ce);
            req.setAttribute("message", ce.getMessage());
            req.setAttribute("javax.servlet.jsp.jspException", ce);
            flush(req, res, errorJsp);
        } catch (Exception e) {
            String msg = FOR_NAME +  
                         "L\'errore e\' stato generato dalla seguente chiamata: " + 
                         req.getQueryString() + 
                         ", presente nella pagina: " + 
                         req.getHeader("Referer");
            log.log(Level.SEVERE, msg, e);
            req.setAttribute("message", e.getMessage());
            req.setAttribute("javax.servlet.jsp.jspException", e);
            flush(req, res, errorJsp);
        }
        /*
         * Mantiene tutti i parametri di navigazione
         */
        req.setAttribute("queryString", req.getQueryString());
        /*
         * Controlla se deve servire un output RSS.
         * Le strade si biforcano: nel caso di RSS e' inutile fornire
         * tutta una serie di valori che non verrano mai usati
         * (liste per l'header, menu verticale, ecc.)
         */
        if (req.getAttribute("rss") != null) {
            /*
             * Se esiste il parametro 'rss' allora si deve caricare la JSP per XML 
             * memorizzata dalla Command nel parametro fileJsp, sovrascrivendo
             * il valore di fileJsp (implicitamente dereferenziando e ri-referenziando). 
             * Il file jsp per XML ignorerà la query string.
             */
            fileJsp = (String) req.getAttribute("fileJsp");
        } else {
            // Disabilita Cache
            res.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1
            res.setHeader("Pragma", "no-cache"); // HTTP 1.0
            res.setDateHeader("Expires", 0); // Proxies.
            /* 
             * Il template compone il risultato con vari pezzi 
             * (testata, aside, etc.) che decide lui se includere o meno
             */
            fileJsp = templateJsp;
            /*
             * Recupero di tutte le informazioni 'fisse' da mostrare
             * nella navigazione (info per l'header, anno corrente,
             * baseHref, etc.)
             */
            retrieveFixedInfo(req);
            /*
             * Costruisce qui il valore del <base href... /> piuttosto che nelle pagine
             */
            String baseHref = getBaseHref(req);
            /*
             *  Setta nella request il valore del <base href... />
             */
            req.setAttribute("baseHref", baseHref);
        }
        flush(req, res, fileJsp);
    }
    
    
    /**
     * <p>Gestisce le richieste del client effettuate con il metodo POST.</p>
     * 
     * @param req la HttpServletRequest contenente la richiesta del client
     * @param res la HttpServletResponse contenente la risposta del server
     * @throws ServletException eccezione che viene sollevata se si verifica un problema nell'inoltro (forward) della richiesta/risposta
     * @throws IOException      eccezione che viene sollevata se si verifica un problema nell'inoltro (forward) della richiesta/risposta
     */
    public void doPost(HttpServletRequest req, 
                       HttpServletResponse res) 
               throws ServletException, IOException {
        /*
         * Dichiara le variabili in base a cui ricercare la Command
         */
        String q = null;
        /*
         * Cerca la command associata al parametro 'ent'
         * e, se la trova, ne invoca il metodo execute()
         */
        //JOptionPane.showMessageDialog(null, "Chiamata POST arrivata dall\'applicazione!", "Main: esito OK", JOptionPane.INFORMATION_MESSAGE, null);
        try {
            q = req.getParameter(entToken);
        } catch (NullPointerException npe) { // Potrebbe già uscire qui
            req.setAttribute("javax.servlet.jsp.jspException", npe);
            req.setAttribute("message", npe.getMessage());
            log(FOR_NAME + "Problema di puntamento: applicazione terminata!" + npe);
            flush(req, res, errorJsp);
            return;
        } catch (NumberFormatException nfe) { // Controllo sull'input
            req.setAttribute("javax.servlet.jsp.jspException", nfe);
            req.setAttribute("message", nfe.getMessage());
            log(FOR_NAME + "Parametro in formato non valido: applicazione terminata!" + nfe);
            flush(req, res, errorJsp);
            return;
        } catch (Exception e) { // Just in case
            req.setAttribute("javax.servlet.jsp.jspException", e);
            req.setAttribute("message", e.getMessage());
            log(FOR_NAME + "Eccezione generica: " + e);
            flush(req, res, errorJsp);
            return;
        }
        try {
            /*
             * Cerca la command associata al parametro 'ent'
             * e, se la trova, ne invoca il metodo execute()
             */
            req.setAttribute("w", true);
            Command cmd = lookupCommand(q);
            cmd.execute(req);
        } catch (CommandException ce) { // Potrebbe già uscire qui
            req.setAttribute("javax.servlet.jsp.jspException", ce);
            req.setAttribute("message", ce.getMessage());
            log("Problema: " + ce);
            flush(req, res, errorJsp);
            return;
        } catch (Exception e) {
            req.setAttribute("javax.servlet.jsp.jspException", e);
            req.setAttribute("message", e.getMessage());
            log("Problema: " + e);
            e.printStackTrace();
            flush(req, res, errorJsp);
            return;
        }
        retrieveFixedInfo(req);
        flush(req, res, templateJsp);
    }

    
    /**
     * lookupCommand restituisce la classe Command associata parametro
     * d'input <code>cmd</code>, come specificato nella HashTable
     * <code>command</code>. Se il parametro è nullo, allora
     * restituisce la classe <code>homepage</code> che
     * punta alla home page del sito. Se la stringa non è presente
     * nella hashtable command, allora lancia l'eccezione
     * <code>CommandException</code>.
     *
     * @param cmd string che individua la classe Command da creare.
     * @return a Command class
     * @exception CommandException se il parametro <code>cmd</code> non corrisponde a nessuna classe.
     */
    private Command lookupCommand(String cmd)
                           throws CommandException {
        if (cmd == null)
            cmd = homePage;
        if (commands.containsKey(cmd))
            return commands.get(cmd);
        // Se non è zuppa è pan bagnato...
        throw new CommandException(FOR_NAME + "Classe Command non valida: " + cmd);
    }    

    
    /** 
     * <p>Esegue pezzi di codice richiamabili direttamente dalla Main,
     * che &egrave; invocata ad ogni richiesta del client e quindi gi&agrave;
     * presente in memoria.<br />
     * Pu&ograve; essere invocata, implicitamente, dal template per 
     * recuperare 
     * <ul>
     * <li>elenchi di oggetti che servono all'header 
     * per popolare le liste eventualmente in esso mostrate</li>
     * <li>il baseHref</li>
     * <li>il flagsUrl</li>
     * <br />
     * e altri valori utili.
     * </ul></p>
     * <p>L'invocazione &egrave; implicita nel senso che il presente
     * metodo non viene richiamato direttamente dal template, 
     * ma indirettamente attraverso l'invocazione della Main.
     * A ogni richiesta standard del client, infatti, ovvero
     * ogni richiesta effettuata per visualizzare un output html
     * (per output diversi quali files CSV o simili &egrave; possibile
     * invocare servlet diverse da Main), pu&ograve; implicitamente essere 
     * richiamato dal metodo service o - meglio - dalla sovrascrittura
     * dei metodi <code>doGet</code> e/o <code>doPost</code>.<br />
     * Dialoga con l'HttpServletRequest attingendo a dati eventualmente
     * valorizzati in essa e valorizzando nella stessa parametri da passare.</p> 
     * 
     * @param req  la HttpServletRequest contenente gli header HTTP e alcuni parametri
     */   
    private static void retrieveFixedInfo(HttpServletRequest req) {
        // Costruisce qui il valore del <base href... /> piuttosto che nelle pagine
        String baseHref = getBaseHref(req);
        // Setta nella request il valore del <base href... />
        req.setAttribute("baseHref", baseHref);
        // Valorizza l'anno corrente: utile al footer
        String currentYear = Utils.getCurrentYear();
        req.setAttribute("theCurrentYear", currentYear);
        // Cerca o inizializza flag di visualizzazione header
        if (req.getAttribute("header") == null) {
            req.setAttribute("header", true);
        }
        // Cerca o inizializza flag di visualizzazione footer
        if (req.getAttribute("footer") == null) {
            req.setAttribute("footer", true);
        }
    }
    
    
    /**
     * Costruisce il percorso di base dell'applicazione che si sta navigando,
     * che precede i parametri.<br />
     * Restituisce tale percorso.<br />
     * Serve a ricostruire i percorsi dei fogli di stile, dei files inclusi, ecc.
     * 
     * @param req  HttpServletRequest contenente il protocollo usato (p.es.: <code>http, https,</code> o <code>ftp</code>)
     * @return <code>String</code> - una stringa che rappresenta la root, da settare nelle jsp (p.es.: <code>&lt;base href="http://www.univr.it/"&gt;</code>)
     */
    public static String getBaseHref(HttpServletRequest req) {
        StringBuffer baseHref = new StringBuffer();
        baseHref.append(req.getScheme());
        baseHref.append("://");
        baseHref.append(req.getServerName());
        if (req.getServerPort() != 80) {
            baseHref.append(":");
            baseHref.append(req.getServerPort());
        }
        baseHref.append(req.getContextPath());
        baseHref.append('/');
        return new String(baseHref);
    }
    

    /**
     * <p>Restituisce la stringa necessaria a del database.</p>
     * <p><cite id="https://stackoverrun.com/it/q/3104484">
     * java:comp/env is the node in the JNDI tree where you can find properties 
     * for the current Java EE component (a webapp, or an EJB).<br />
     * <code>Context envContext = (Context)initContext.lookup("java:comp/env");</code>
     * allows defining a variable pointing directly to this node. It allows doing
     * <code>SomeBean s = (SomeBean) envContext.lookup("ejb/someBean");
     * DataSource ds = (DataSource) envContext.lookup("jdbc/dataSource");</code>
     * rather than
     * <code>SomeBean s = (SomeBean) initContext.lookup("java:comp/env/ejb/someBean");
     * DataSource ds = (DataSource) initContext.lookup("java:comp/env/jdbc/dataSource");</code>
     * Relative paths instead of absolute paths. That's what it's used for.<br />
     * It's an in-memory global hashtable where you can store global 
     * variables by name. 
     * The "java:" url scheme causes JNDI to look for a 
     * javaURLContextFactory class, which is usually provided by your 
     * app container, e.g. here is Tomcat's implementation javadoc.</cite></p>
     * <p>Metodo getter sulla variabile di classe.</p>
     * 
     * @return <code>String</code> - il nome usato dal DbWrapper per realizzare il puntamento jdbc
     */
    public static String getDbName() {
        return new String(contextDbName);
    }
    
    
    /**
     * <p>Inoltra la richiesta ad una pagina passata come argomento.</p>
     * 
     * @param req  HttpServletRequest contenente i parametri sulla QueryString
     * @param res  HttpServletResponse per inoltrare la chiamata
     * @param fileJspT pagina JSP a cui puntare nell'inoltro
     * @throws ServletException se si verifica un'eccezione nella redirezione
     * @throws IOException se si verifica un problema di input/output
     * @throws IllegalStateException se la Response era committata o se un URL parziale e' fornito e non puo' essere convertito in un URL valido (v. {@link HttpServletResponse#sendRedirect(String)})
     */
    private void flush(HttpServletRequest req,
                       HttpServletResponse res,
                       String fileJspT) 
                throws ServletException, 
                       IOException,
                       IllegalStateException {
        if (req.getAttribute("redirect") == null) {
            final RequestDispatcher rd = getServletContext().getRequestDispatcher(fileJspT + "?" + req.getQueryString());
            rd.forward(req, res);
            return;
        }
        res.sendRedirect(getServletContext().getInitParameter("appName") + "/?" + (String) req.getAttribute("redirect"));
    }
    
    
    /**
     * <p>Ricalcola gli stati di ogni attivit&agrave; e li aggiorna.</p>
     * <p>Lo stato di ciascuna attivit&agrave; originariamente viene valorizzato 
     * all'atto del salvataggio (inserimento o aggiornamento dell'attivit&agrave;).
     * Siccome, per&ograve;, al variare del tempo pu&ograve; cambiare anche
     * lo stato, bisogna periodicamente lanciare un ricalcolo (refresh)
     * dello stato stesso.<br /> 
     * Tale ricalcolo viene implementato dal presente metodo, che deve
     * essere richiamato da una routine schedulata, che lo invoca trascorso
     * ogni determinato periodo di tempo.</p>
     * <p>
     * Questo metodo &egrave; sincronizzato, cio&egrave; vogliamo che
     * che non sia possibile che due o pi&uacute; <code>thread</code> 
     * vi accedano contemporaneamente: la scrittura sul db, implementata
     * da questo metodo, deve essere effettuata da un thread per volta,
     * per evitare sovrapposizioni; quantunque, infatti, le transazioni
     * siano comunque gestite dal Model nel contesto degli aggiornamenti,
     * tuttavia &egrave; sempre buona norma evitare accessi concorrenti,
     * che risulterebbero quanto meno <code>idle</code>, se non vi deve essere 
     * elaborazione parallela.<br /> 
     * In generale, noi non gestiamo nell'applicazione web
     * direttamente i thread, ma questi sono gestiti a monte dal web server
     * per distribuire il carico tra tutti i client che fanno le loro varie
     * richieste.</p>
     * <p> 
     * La sincronizzazione impedisce la scrittura contemporanea
     * da parte di due o pi&uacute; thread
     * contemporaneamente; nel caso di questo metodo, 
     * se esso non fosse un metodo statico, ci&ograve; 
     * non rappresenterebbe tuttavia una garanzia, perch&eacute; 
     * la sincronizzazione 
     * impedisce l'accesso a un metodo istanza sincronizzato 
     * da parte di due o pi&uacute; thread contemporaneamente 
     * <strong>solo sulla stessa istanza</strong>, nel senso che un altro
     * thread pu&ograve; tranquillamente accedere contemporaneamente 
     * allo stesso metodo da un'altra istanza dell'oggetto: <em>visto
     * che  si tratta di un metodo istanza!</em> 
     * Solo se il metodo &egrave; statico, vi &egrave; 
     * la garanzia che uno e un solo thread per volta 
     * potr&agrave; modificare il valore del db: 
     * che, in genere, &egrave; <em>il risultato pi&uacute; desiderabile 
     * da ottenere.</em> nel momento in cui si effettua una sincronizzazione.</p>
     * 
     * @param db        istanza di Model per l'accesso al database da aggiornare
     * @return <code>int</code> - il numero di tuple aggiornate dal Model
     * @throws CommandException una it.univr.di.uol.CommandException che incapsula una RuntimeException di qualche genere, che potrebbe essere generata nell'accedere ad attributi o in qualche altro tipo di puntamento a null 
     */
    @SuppressWarnings("hiding")
    synchronized public static int refresh(DBWrapper db) 
                                    throws CommandException {
        try {
            PersonBean bot = new PersonBean();
            bot.setNome("Automatico");
            bot.setCognome("Aggiornamento");
            return db.updateActivitiesState(bot);
        } catch (WebStorageException wse) {
            throw new CommandException(FOR_NAME + "Si e\' verificato un problema nel metodo che effettua l\'aggiornamento massivo dei valori degli stati delle attivita\'.\n" + wse.getMessage(), wse);
        } catch (RuntimeException re) {
            throw new CommandException(FOR_NAME + "Si e\' verificato un problema nel refresh dei valori degli stati attivita\', probabilmente un puntamento a null.\n" + re.getMessage(), re);
        }
    }
    
}