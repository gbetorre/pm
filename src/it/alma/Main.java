/*
 *   Alma on Line: Applicazione WEB per la visualizzazione 
 *   delle schede di indagine su popolazione dell'ateneo.<br />
 *   
 *   Copyright (C) 2018 Giovanroberto Torre<br />
 *   Alma on Line (aol), web application to publish students 
 *   and degrees informations.
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
import java.util.HashMap;
import java.util.Vector;
import java.util.logging.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.JOptionPane;

import it.alma.bean.ItemBean;
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
     *  Nome di questa classe 
     *  (utilizzato per contestualizzare i messaggi di errore)
     */
    static final String FOR_NAME = "\n" + Logger.getLogger(new Throwable().getStackTrace()[0].getClassName()) + ": ";
    /**
     * Suffisso per le Command
     */
    static final String COMMAND_SUFFIX = "Command";
    /**
     * DataBound.
     */
    private DBWrapper db;
    /**
     * Tabella hash (dictionary) contenente le command predefinite.
     */
    private HashMap<String, Command> commands;
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
     * Inizializza (staticamente) le variabili globali 
     * che saranno utilizzate anche dalle altre classi:
     * <ul>
     * <li> connessione al database: <code>db</code></li>
     * <li> tabella hash di tutte le classi richiamabili dall'applicazione</li>
     * <li> vettore delle voci menu principale (menu orizzontale)</li>
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
         *    Lettura dei parametri di configurazione dell'applicazione AOL     *
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
            /*
             * Prepara la Command della Valutazione
             */
            final String commandName = "Valuation" + COMMAND_SUFFIX;
            ItemBean voce = new ItemBean();
            voce.setId(voce.hashCode());
            voce.setNome("Valuation");
            voce.setNomeClasse(commandName);
            voce.setPaginaJsp(templateJsp);
            classiCommand.add(voce);
        }
        catch (Exception e) {
            throw new ServletException(FOR_NAME + "Problemi nel caricare le classi Command.\n" + e.getMessage(), e);
        }
        ItemBean voceMenu = null;
        Command classCommand = null;
        commands = new HashMap<String, Command>();
        for (int i=0; i < classiCommand.size(); i++) {
            voceMenu = classiCommand.get(i);
            try {
                classCommand = (Command) Class.forName("it.alma.command." + voceMenu.getNomeClasse()).newInstance();
                classCommand.init(voceMenu);
                commands.put(voceMenu.getNome(), classCommand);
            } catch (ClassNotFoundException cnfe) {
                try {
                    classCommand = (Command) Class.forName("it.alma.command." + voceMenu.getNomeClasse()).newInstance();
                    classCommand.init(voceMenu);
                    commands.put(voceMenu.getNome(), classCommand);
                }
                catch (ClassNotFoundException cnfex) {
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
     * <pre>
     * OUTPUT:
     *  nome variabile  | scope    | descrizione
     * -----------------------------------------------------------------------
     *  menuPrincipale   request    nome del main menu di aol 
     *  queryString      request    tutta la QueryString in input 
     *  lingue           request    oggetto costruito in base alla lingua ev.
     *                               presente nei parametri di navigazione
     * </pre>
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
        JOptionPane.showMessageDialog(null, "Chiamata arrivata dall\'applicazione!", "Esito: OK", JOptionPane.INFORMATION_MESSAGE, null);
        try {
            q = req.getParameter(entToken);
        } catch (NullPointerException npe) { // Potrebbe già uscire qui
            req.setAttribute("javax.servlet.jsp.jspException", npe);
            fileJsp = errorJsp;
            log(FOR_NAME + "Problema di puntamento: applicazione terminata!" + npe);
            final RequestDispatcher rd = getServletContext().getRequestDispatcher(fileJsp + "?" + req.getQueryString());
            rd.forward(req, res);
            return;
        } catch (NumberFormatException nfe) { // Controllo sull'input
            req.setAttribute("javax.servlet.jsp.jspException", nfe);
            fileJsp = errorJsp;
            log(FOR_NAME + "Parametro in formato non valido: applicazione terminata!" + nfe);
            final RequestDispatcher rd = getServletContext().getRequestDispatcher(fileJsp + "?" + req.getQueryString());
            rd.forward(req, res);
            return;
        } catch (Exception e) { // Just in case
            req.setAttribute("javax.servlet.jsp.jspException", e);
            fileJsp = errorJsp;
            log(FOR_NAME + "Eccezione generica: " + e);
            final RequestDispatcher rd = getServletContext().getRequestDispatcher(fileJsp + "?" + req.getQueryString());
            rd.forward(req, res);
            return;
        }
        try {
            /*
             * Cerca la command associata al parametro 'ent'
             * e, se la trova, ne invoca il metodo execute()
             */
            Command cmd = lookupCommand(q);
            cmd.execute(req);
        } catch (CommandException e) { // Potrebbe già uscire qui
            req.setAttribute("javax.servlet.jsp.jspException", e);
            fileJsp = errorJsp;
            log("Errore: " + e);
            final RequestDispatcher rd = getServletContext().getRequestDispatcher(fileJsp + "?" + req.getQueryString());
            rd.forward(req, res);
            return;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
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
            /* 
             * Il template compone il risultato con vari pezzi 
             * (testata, aside, etc.)
             */
            fileJsp = templateJsp;
            /*
             * Recupero di tutte le informazioni 'fisse' da mostrare
             * nella navigazione (liste di strutture per l'header, anno corrente,
             * baseHref, etc.)
             */
            retrieveFixedInfo(req);
        }
        final RequestDispatcher rd = getServletContext().getRequestDispatcher(fileJsp + "?" + req.getQueryString());
        rd.forward(req, res);
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
     * @exception CommandException  se il parametro <code>cmd</code> non
     *      corrisponde a nessuna classe.
     */
    private Command lookupCommand(String cmd)
                           throws CommandException {
        if (cmd == null)
            cmd = homePage;
        if (commands.containsKey(cmd))
            return (Command) commands.get(cmd);
        else
            throw new CommandException(FOR_NAME + "Classe Command non valida: " + cmd);
    }    

    
    /** 
     * Esegue pezzi di codice della OrganizzazioneCommand senza crearne 
     * una nuova istanza, ma richiamandoli direttamente dalla Main,
     * che &egrave; invocata ad ogni richiesta del client e quindi gi&agrave;
     * presente in memoria.<br />
     * &Egrave; invocata, implicitamente, dal template per 
     * recuperare 
     * <ul>
     * <li>le lingue</li> 
     * <li>gli elenchi di oggetti che servono all'header 
     * per popolare le liste in esso mostrate 
     * (aree, dipartimenti, scuole di dottorato, ecc.)</li>
     * <li>il baseHref</li>
     * <li>il flagsUrl</li>
     * <br />
     * e altri valori utili.
     * </ul><br />
     * <p>L'invocazione &egrave; implicita nel senso che il presente
     * metodo non viene richiamato direttamente dal template, 
     * ma indirettamente attraverso l'invocazione della Main.
     * A ogni richiesta standard del client, infatti, ovvero
     * ogni richiesta effettuata per visualizzare un output html
     * (per output diversi quali files CSV o simili &egrave; possibile
     * invocare servlet diverse da Main), viene implicitamente 
     * richiamato il metodo service, e di conseguenza il presente
     * metodo, la cui invocazione &egrave; presente in service.</p>
     * <p>Questo meccanismo di valorizzazione dell'header tramite
     * valori peraltro &ndash; nella maggior parte dei casi &ndash;  
     * gi&agrave; presenti in memoria perch&eacute;
     * statici (vengono valorizzati dalla init() della OrganizzazioneCommand
     * quando "sale" il server), vale solo per tutte le pagine di aol gestite
     * tramite template, quindi NON per l'homepage.<br />
     * Quest'ultima istanzia una propria copia della OrganizzazioneCommand
     * direttamente, copia che fa poi sostanzialmente le stesse
     * operazioni implementate nel corrente metodo. 
     * Il motivo per il quale questo codice, nel caso della home page 
     * del sito di ateneo, non viene recuperato da Main ma attraverso
     * un costruttore di OrganizzazioneCommand &egrave; chiaro:
     * nell'homepage non esiste ancora quasi nulla dell'applicazione web 
     * chiamata AOL!
     * Non c'&egrave; la Main, 
     * non si pu&ograve; far riferimento all'oggetto Lingue, 
     * non si pu&ograve; far riferimento al DataUrl... 
     * Quindi, se si vuol cambiare qualcosa nel comportamento del presente
     * metodo, bisogna ricordarsi di gestire le stesse modidfiche
     * anche nel costruttore dell'OrganizzazioneCommand 
     * invocato dalla homepage!!</p> 
     * Dialoga con l'HttpServletRequest attingendo a dati eventualmente
     * valorizzati in essa e valorizzando nella stessa parametri da passare.<br />
     * In particolare:
     * <pre>
     * INPUT: 
     * nome variabile       | scope    | descrizione
     * --------------------------------------------------------------------------
     *   lang                 param      lingua in cui l'utente sta visualizzando
     *   queryString          request    tutta la QueryString in input
     *  
     * OUTPUT:
     * nome variabile       | scope    | descrizione
     * -------------------------------------------------------------------------
     *   lang                 request    valore del parametro 'lang'
     *   dateFormatSymbols    request    formato di date e ore 
     *   queryString          request    tutta la QueryString in input
     *   baseHref             request    base href della pagina
     *   flagsUrl             request    formattatore lingua contenuti
     *   theCurrentYear       request    anno corrente per il &copy; del footer
     *   dipartimenti         request    elenco dipartimenti (menu header)
     *   scuoledottorato      request    elenco scuole dottorato (menu header)
     *   scuoledottoratoesau  request    elenco scuole dottorato ad esaurimento (menu header)
     *   biblioteche          request    elenco biblioteche centralizzate (menu header)
     *   biblioPoli           request    elenco poli di biblioteca dipartimentale (menu header)
     *   biblioCentri         request    elenco biblioteche dei centri (menu header)
     *   biblioAltre          request    elenco altre biblioteche (menu header)
     *   centriAteneo         request    elenco centri di servizio di ateneo (menu header)
     *   centriRicerca        request    elenco centri di ricerca di ateneo (menu header)
     *   poli                 request    elenco sedi didattiche compr. polo VI (menu header)
     *   scuole               request    elenco scuole/strutture di raccordo (menu header)
     *  </pre>
     * 
     * @param req  la HttpServletRequest contenente gli header HTTP e alcuni parametri
     */   
    public void retrieveFixedInfo(HttpServletRequest req) {
        // Costruisce qui il valore del <base href... /> piuttosto che nelle pagine
        String baseHref = getBaseHref(req);
        // Setta nella request il valore del <base href... />
        req.setAttribute("baseHref", baseHref);
        // Costruisce qui il valore del <flagsUrl> per cambiare lingua ai contenuti
        String flagsUrl = (req.getAttribute("queryString") != null) ? ((String) req.getAttribute("queryString") ): req.getQueryString();
        if (flagsUrl == null) 
            flagsUrl = new String();                        // In error.jsp QueryString è nulla
        flagsUrl = flagsUrl.replaceAll("&?lang=[^&]*", ""); // Toglie eventuale "lang=xx"
        flagsUrl = flagsUrl.replaceAll("&", "&amp;");       // Sostituisce tutti gli & con &amp;
        flagsUrl = (flagsUrl.length()>0) ? "main?" + flagsUrl + "&amp;lang=" : "?lang="; // Su fol il valore dell'ultima assegnazione sarebbe : "main?lang="
        // Setta nella request il valore del <flagsUrl>
        req.setAttribute("flagsUrl", flagsUrl);
        // Valorizza l'anno corrente: utile al footer
        String currentYear = Utils.getCurrentYear();
        req.setAttribute("theCurrentYear", currentYear);
    }
    
    
    /**
     * Costruisce il percorso di base dell'applicazione che si sta navigando,
     * che precede i parametri.<br />
     * Restituisce tale percorso.<br />
     * Serve a ricostruire i percorsi dei fogli di stile, dei files inclusi, ecc.
     * 
     * @param req  la HttpServletRequest contenente il protocollo usato (p.es.: <code>http, https,</code> o <code>ftp</code>)
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
    
    
}