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

package it.alma;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Vector;
import java.util.logging.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oreilly.servlet.ParameterParser;

import it.alma.bean.ActivityBean;
import it.alma.bean.ItemBean;
import it.alma.bean.PersonBean;
import it.alma.bean.ProjectBean;
import it.alma.bean.WbsBean;
import it.alma.command.Command;
import it.alma.command.HomePageCommand;
import it.alma.command.WbsCommand;
import it.alma.exception.AttributoNonValorizzatoException;
import it.alma.exception.CommandException;
import it.alma.exception.WebStorageException;


/**
 * <p><code>Data</code> &egrave; la servlet della web-application pol
 * che viene utilizzata per produrre output con contentType differenti da 
 * 'text/html'.</p>
 * Questa servlet fa a meno, legittimamente, del design (View),
 * in quanto l'output prodotto consiste in pure tuple prive di presentazione
 * (e.g. fileset CSV, formati XML, dati con o senza metadati, RDF 
 * <cite>and so on</cite>).<br />
 * <p>
 * Questa servlet estrae l'azione dall'URL, ne verifica la
 * correttezza, quindi in base al valore del parametro <code>'ent'</code> ricevuto
 * richiama le varie Command che devono eseguire i comandi specifici.
 * Infine, recupera l'output dai metodi delle Command stesse richiamati
 * e li restituisce a sua volta al cliente sotto forma non di outputstream
 * 'text/html' (come nel funzionamento standard delle applicazioni web), 
 * quanto sotto forma di file nel formato richiesto (.csv, .xml, ecc.).<br /> 
 * Elabora anche un nome univoco per ogni file generato, basandosi sul
 * timestamp dell'estrazione/richiesta.
 * </p>
 * <p>
 * La classe che realizza l'azione deve implementare l'interfaccia
 * Command e, dopo aver eseguito le azioni necessarie, restituire
 * un set di risultati che dovr&agrave; essere utilizzato per
 * visualizzare i dati all'interno dei files serviti, ai quali
 * sarà fatto un forward.
 * </p>
 * L'azione presente nell'URL deve avere il seguente formato:
 * <pre>q=&lt;nome&gt;</pre>
 * dove 'nome' è il valore del parametro 'q' che identifica 
 * l'azione da compiere al fine di generare i record.<br />
 * Oltre al parametro <code>'q'</code> possono essere presenti anche
 * eventuali altri parametri, ma essi non hanno interesse nel contesto
 * della presente classe, venendo incapsulati nella HttpServletRequest
 * e quindi inoltrati alla classe Command che deve fare il lavoro di
 * estrazione. Normalmente, tali altri parametri possono essere presenti
 * sotto forma di parametri sulla querystring, ma anche direttamente
 * settati nella request; ci&ograve; non interessa alcunch&eacute; ai fini
 * del funzionamento della presente classe.
 * </p>
 * <p>
 * Altre modalit&agrave; di generazione di output differenti da 'text/html'
 * (chiamate a pagine .jsp che incorporano la logica di preparazione del CSV,
 * chiamate a pagina .jsp che si occupano di presentare il metadato...)
 * non sono ammesse nell'applicazione POL 
 * <em>(data del primo rilascio: 13/02/2019)</em>
 * in favore dell'uso di questa servlet.
 * </p>
 *
 * @author <a href="mailto:giovanroberto.torre@univr.it">Giovanroberto Torre</a>
 */
public class Data extends HttpServlet {
    
    /**
     * Definisce una versione relativa alla classe.<br /> 
     * (Se questo dato non fosse inserito, verrebbe calcolato in maniera automatica
     * dalla JVM, e questo potrebbe portare a errori riguardo alla serializzazione). 
     */
    private static final long serialVersionUID = 1L;
    /**
     * Logger della classe per scrivere i messaggi di errore  
     */
    private Logger log = Logger.getLogger(Data.class.getName());
    /**
     * Nome di questa classe (viene utilizzato per contestualizzare i messaggi di errore)
     */
    private static final String FOR_NAME = "\n" + Logger.getLogger(Data.class.getName()) + ": ";
    /**
     * Tabella Hash contenente tutte le command di interesse
     */
    private HashMap<String, Command> commands = new HashMap<String, Command>();
    /**
     * Lista Hash contenente tutti i token di interesse
     */
    private LinkedList<String> entTokens = new LinkedList<String>();
    /**
     * Command che deve fare il lavoro di estrazione
     */
    private Command cmd = null; 
    /**
     * DataBound
     */
    private DBWrapper db = null;
    /**
     * Attributo per il valore del parametro obbligatorio 'ent'
     */
    private static String entToken;
    /**
     * Costante per l'uso del separatore trattino
     */
    public static final char HYPHEN = '-';
    /**
     * Costante per l'uso del separatore virgola
     */
    public static final String COMMA = ",";
    /**
     * Costante per l'uso del separatore nel Comma Separated Values file
     */
    public static final String SEPARATOR = ";";

    
    /**
     * <p>Inizializza le variabili globali:
     * <ul>
     * <li>valore del parametro: <code>ent</code></li>
     * </ul>
     * Inoltre, effettua il caricamento in una struttura &ndash; 
     * definita come variabile di istanza &ndash; 
     * delle classi Command di <code>pol</code>
     * abilitate all'output su file.
     * </p>
     * 
     * @param config ServletConfig utilizzato per inizializzazione da superclasse
     * @throws ServletException java.lang.Throwable.Exception.ServletException che viene sollevata se manca un parametro di configurazione considerato obbligatorio o per via di qualche altro problema di puntamento
     */
    public void init(ServletConfig config) 
              throws ServletException {
        /*
         *  Inizializzazione da superclasse
         */
        super.init(config);
        /*
         * Nome del parametro che identifica la Command da interpellare
         */
        entToken = getServletContext().getInitParameter("entToken");
        if (entToken == null) {
            throw new ServletException(FOR_NAME + "\n\nManca il parametro di contesto 'entToken'!\n\n");
        }
        /*
         * Attiva la connessione al database
         */
        try {
            db = new DBWrapper();
        } catch (WebStorageException wse) {
            String error = FOR_NAME + "Non e\' possibile avere una connessione al database: " + wse.getMessage();
            log.severe("Problema grave in init!! Il server puo\' aver avuto problemi a partire!" + error);
            throw new ServletException(error, wse);
        }
        /*
         * Inizializza la tabella <code>commands</code> che deve contenere
         * tutte le classi che saranno POTENZIALMENTE richiamabili da questa
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
        /*
         *  Caricamento degli 'ent' ammessi alla generazione di output su file
         */
        entTokens.add("pol");
        entTokens.add("wbs");
        ItemBean voceMenu = null;
        Command classCommand = null;
        commands = new HashMap<String, Command>();
        for (int i = 0; i < classiCommand.size(); i++) {
            voceMenu = classiCommand.get(i);
            log.info(FOR_NAME + "Il nome della voce di menu vale: " + voceMenu.getNome());
            if (entTokens.contains(voceMenu.getNome())) {
                try {
                    log.info(FOR_NAME + "Valore di entTokens corrispondente al nome della voce di menu: " + voceMenu.getNome() + "\nTentativo di istanziare it.alma.command." + voceMenu.getNomeClasse());
                    classCommand = (Command) Class.forName("it.alma.command." + voceMenu.getNomeClasse()).newInstance();
                    log.info(FOR_NAME + "Tentativo di inizializzare it.alma.command." + voceMenu.getNomeClasse());
                    classCommand.init(voceMenu);
                    log.info(FOR_NAME + "Tentativo di aggiungere la classe di pol alle command ammesse.");
                    commands.put(voceMenu.getNome(), classCommand);
                    log.info(FOR_NAME + "La dimensione della tabella commands di Data al momento vale: " + commands.size());
                } catch (ClassNotFoundException cnfe) { // Se non riesce a istanziare la Command in generali la cerca in un sottopackage
                    try {
                        log.info(FOR_NAME + "Riprova: valore di entTokens corrispondente al nome della voce di menu: " + voceMenu.getNome() + "\nTentativo di istanziare it.alma.qol.command." + voceMenu.getNomeClasse());
                        classCommand = (Command) Class.forName("it.alma.qol.command." + voceMenu.getNomeClasse()).newInstance();
                        log.info(FOR_NAME + "Tentativo di inizializzare it.alma.qol.command." + voceMenu.getNomeClasse());
                        classCommand.init(voceMenu);
                        log.info(FOR_NAME + "Tentativo di aggiungere la classe di qol alle command ammesse.");
                        commands.put(voceMenu.getNome(), classCommand);
                        log.info(FOR_NAME + "La dimensione della tabella commands di Data al momento vale: " + commands.size());
                    } catch (ClassNotFoundException cnfex) {
                        String msg = "La classe collegata alla voce menu '" +
                                     voceMenu.getNome() +
                                     "' &egrave; '" +
                                     voceMenu.getNomeClasse() +
                                     " e non pu&ograve; essere caricata: " + cnfex.getMessage();
                        throw new ServletException(msg, cnfe);
                    } catch (Exception e) {
                        String msg = "Errore generico nel caricare la classe " +
                                     voceMenu.getNomeClasse() +
                                     ". Dettaglio errore: " +
                                     e.getMessage();
                        throw new ServletException(msg, e);
                    }
                } catch (Exception e) {
                    String msg = "Errore generico nel caricare la classe del package comune " +
                                 voceMenu.getNomeClasse() +
                                 ". Dettaglio errore: " +
                                 e.getMessage();
                    throw new ServletException(msg, e);
                }
            }
        }
    }
    
    
    /**
     * <p>Il metodo Service gestisce le richieste del client.</p>
     * <p><cite id="malacarne" data-exact-page="99">
     *  Il metodo service viene invocato dal servlet-engine come azione
     *  di risposta alla ricezione di una HttpRequest.
     *  Questo metodo, nella sua implementazione originale, funziona
     *  come dispatcher, ossia, in base al codice operazione HTTP ricevuto,
     *  attiva il metodo disponibile pi&uacute; opportuno (...)
     * </cite></p>
     * <p>Considerando che:<br />
     * <cite id="malacarne" data-exact-page="100">
     * "Una sottoclasse di HttpServlet dovrebbe preferenzialmente
     *  sovrascrivere uno dei metodi precedenti<br />
     *  (n.d.r.: <code>doGet | doPost | doOption | doPut | doTrace</code>)<br />
     *  In taluni casi per&ograve; (...) risulta essere pi&uacute; 
     *  conveniente, ma deve essere una scelta ben ponderata, sovrascrivere
     *  direttamente il metodo service"</cite><br />
     * la Servlet Data, correttamente, non implementa il metodo 
     * service, anche perch&eacute; questo si limiterebbe
     * a richiamare semplicemente la doGet(), e dunque potrebbe essere
     * fonte di perdita di prestazioni a fronte di nessun guadagno. 
     * </p>
     * Se implementare il metodo service() non avesse avuto controindicazioni, 
     * un'implementazione di tale metodo 
     * avrebbe potuto quindi assumere la seguente forma:
     * <pre>
     * @param req HttpServletRequest proveniente dal client
     * @param res HttpServletResponse che verra' utilizzata per rispedire al client il file di dati richiesto
     * @throws ServletException eccezione propagata che puo' originare dalla doGet richiamata
     * @throws IOException eccezione propagata che puo' originare dalla doGet richiamata
     *
     * public void service(HttpServletRequest req, HttpServletResponse res)
     * 	            throws ServletException, IOException {
     *      doGet(req, res);
     * }
     * </pre>
     */
    
    
    /** (non-Javadoc)
     * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
                  throws ServletException, IOException {
        // Utente loggato
        PersonBean usr = null;
        try {
            usr = HomePageCommand.getLoggedUser(req);
        } catch (CommandException ce) {
            req.setAttribute("javax.servlet.jsp.jspException", ce);
            log("Problema a livello di autenticazione: " + ce);
            throw new ServletException(ce.fillInStackTrace());
        }
        try {
            cmd = lookupCommand(req.getParameter(entToken));
        } catch (CommandException ce) {
           req.setAttribute("javax.servlet.jsp.jspException", ce);
           log("Eccezione: " + ce);
        }
        try {
            // Crea un nome univoco per il file che andrà ad essere generato
            Calendar now = Calendar.getInstance();
            String fileName = req.getParameter(entToken) + '_' + 
                              new Integer(now.get(Calendar.YEAR)).toString() + HYPHEN +
                              String.format("%02d", now.get(Calendar.MONTH) + 1) + HYPHEN +
                              String.format("%02d", now.get(Calendar.DAY_OF_MONTH)) + '_' +
                              String.format("%02d", now.get(Calendar.HOUR_OF_DAY)) + 
                              String.format("%02d", now.get(Calendar.MINUTE)) + 
                              String.format("%02d", now.get(Calendar.SECOND));
            // Configura il response per il browser
            res.setContentType("text/x-comma-separated-values");
            /* Il db di pol e' codificato in UTF-8:
             * pol  | gtorre   | UTF8     | en_US.UTF-8 | en_US.UTF-8 | =Tc/gtorre 
             * pertanto la prima idea nell'implementazione era che
             * il characterEncoding migliore da impostare fosse il medesimo:
             * //res.setCharacterEncoding("UTF-8");
             * Tuttavia, le estrazioni sono destinate ad essere visualizzate
             * attraverso fogli di calcolo che per impostazione predefinita
             * considerano il charset dei dati il latin1, non UTF-8, 
             * perlomeno per la nostra utenza e per la maggior parte
             * delle piattaforme, per cui dati, se espressi in formato UTF-8, 
             * risultano codificati in maniera imprecisa, perche', come al solito, 
             * quando un dato UTF-8 viene codificato in latin1 
             * (quest'ultimo anche identificato come: l1, csISOLatin1, iso-ir-100, 
             * IBM819, CP819, - o ultimo ma non ultimo - ISO-8859-1) 
             * i caratteri che escono al di fuori dei primi 128 caratteri,
             * che sono comuni (in quanto UTF-8 usa un solo byte per 
             * codificare i primi 128 caratteri) non vengono visualizzati
             * correttamente ma vengono espressi con caratteri che in ASCII sono 
             * non corrispondenti.                                              */
            res.setCharacterEncoding("ISO-8859-1");
            res.setHeader("Content-Disposition","attachment;filename=" + fileName + ".csv");
            fprintf(req, res, usr);
        } catch (Exception e) {
            log.severe("Problema nella doGet di Data" + e.getMessage());
        }
    }
    
    
    /**
     * <p>Genera il contenuto dello stream, che questa classe tratta 
     * sotto forma di file, che viene trasmesso sulla risposta in output,
     * a seconda del valore di <code>'ent' (q)</code> che riceve in input.</p>
     * <p>
     * Storicamente, in programmazione <code> C, C++ </code> e affini, 
     * le funzioni che scrivono sull'outputstream si chiamano tutte 
     * <code>printf</code>, precedute da vari prefissi a seconda di
     * quello che scrivono e di dove lo scrivono.<br />
     * <code>fprintf</code> &egrave; la funzione della libreria C che
     * invia output formattati allo stream, identificato con un puntatore
     * a un oggetto FILE passato come argomento 
     * (<small>per approfondire, 
     * <a href="http://www.tutorialspoint.com/c_standard_library/c_function_fprintf.htm">
     * v. p.es. qui</a></small>).<br />
     * Qui per analogia, pi&uacute; che altro nella forma di una "sfiziosa"
     * citazione (per l'ambito informatico) il metodo della Data che 
     * scrive il contenuto vero e proprio del file che viene passato
     * al client, viene chiamato allo stesso modo di questa "storica" funzione, 
     * &ndash; ma il contesto degli oggetti e degli argomenti 
     * &egrave; ovviamente completamente diverso.</p>
     * 
     * @param req la HttpServletRequest contenente il valore di 'ent' e gli altri parametri necessari a formattare opportunamente l'output
     * @param res la HttpServletResponse utilizzata per ottenere il 'Writer' su cui stampare il contenuto, cioe' il file stesso
     * @param usr PersonBean rappresentante l'utente loggato (per evitare che, conoscendo l'url, si possano scaricare report senza loggarsi!)
     * @return <code>int</code> - un valore intero restituito per motivi storici. 
     *                            Tradizionalmente, tutte le funzioni della famiglia x-printf restituiscono un intero, 
     *                            che vale il numero dei caratteri scritti - qui il numero delle righe scritte - in caso di successo 
     *                            oppure -1 (o un altro numero negativo) in caso di fallimento 
     * @throws ServletException   java.lang.Throwable.Exception.ServletException che viene sollevata se manca un parametro di configurazione considerato obbligatorio o per via di qualche altro problema di puntamento
     * @throws IOException        java.io.IOException che viene sollevata se si verifica un puntamento a null o in genere nei casi in cui nella gestione del flusso informativo di questo metodo si verifica un problema
     */
    private int fprintf(HttpServletRequest req, HttpServletResponse res, PersonBean usr)
                 throws ServletException, IOException {
        /*
         *  Genera l'oggetto per lo standard output
         */
        PrintWriter out = res.getWriter();
        /* 
         * Parser per la gestione assistita dei parametri di input
         */
        ParameterParser parser = new ParameterParser(req);
        /* 
         * Recupera o inizializza 'id progetto'
         */
        int idPrj = parser.getIntParameter("id", Utils.DEFAULT_ID);
        /*
         *  Tradizionalmente, ogni funzione della famiglia x-printf 
         *  restituisce un intero
         */
        int success = -1;
        /* **************************************************************** *
         *          Contenuto files CSV per Report di WBS          *
         * **************************************************************** */
        if (req.getParameter(entToken).equals("wbs")) { 
            try {
                WbsCommand wbsc = (WbsCommand) cmd;
                
                ProjectBean p = db.getProject(idPrj, usr.getId());
                Vector<WbsBean> listWP = WbsCommand.retrieveWorkPackages(idPrj, db);
                // Scrittura file CSV
                out.println("N." + SEPARATOR +
                            "Workpackage" + SEPARATOR + 
                            "Wbs padre" + SEPARATOR + 
                            "Attivita" + SEPARATOR + 
                            "Durata prevista" + SEPARATOR + 
                            "Durata effettiva" + SEPARATOR + 
                            "Data inizio prevista" + SEPARATOR +
                            "Data fine prevista" + SEPARATOR + 
                            "Data inizio effettiva" + SEPARATOR +
                            "Data fine effettiva" + SEPARATOR +
                            "Progetto" + SEPARATOR +
                            "Stato");
                if (listWP.size() > 0) {
                    int itCounts = 0, record = 0;
                    do {
                        WbsBean wp = listWP.elementAt(itCounts);
                        WbsBean wbsParent = db.getWbsParentByOffspring(idPrj, wp.getId());
                        StringBuffer nomePadre = new StringBuffer();
                        if (wbsParent != null) {
                            nomePadre.append(wbsParent.getNome());
                        }
                        for (ActivityBean act : wp.getAttivita()) {
                            String guPrevisti = (act.getGuPrevisti() > Query.NOTHING) ? Integer.toString(act.getGuPrevisti()) : Utils.VOID_STRING;
                            String guEffettivi = (act.getGuEffettivi() > Query.NOTHING) ? Integer.toString(act.getGuEffettivi()) : Utils.VOID_STRING;
                            String dataInizio = (act.getDataInizio() != null && act.getDataInizio().after(new Date(0))) ? act.getDataInizio().toString() : Utils.VOID_STRING;
                            String dataFine = (act.getDataFine() != null && act.getDataFine().after(new Date(0))) ? act.getDataFine().toString() : Utils.VOID_STRING;
                            String dataInizioEffettiva = (act.getDataInizioEffettiva() != null && act.getDataInizioEffettiva().after(new Date(0))) ? act.getDataInizioEffettiva().toString() : Utils.VOID_STRING;
                            String dataFineEffettiva = (act.getDataFineEffettiva() != null && act.getDataFineEffettiva().after(new Date(0))) ? act.getDataFineEffettiva().toString() : Utils.VOID_STRING;
                            String unEncodedState = act.getStato().getNome().replaceAll("&agrave;", "a\'");
                            out.println(
                                    ++record + SEPARATOR +
                                    wp.getNome().replace(';', ',') + SEPARATOR +
                                    nomePadre + SEPARATOR +
                                    act.getNome().replace(';', ',') + SEPARATOR +
                                    guPrevisti + SEPARATOR +
                                    guEffettivi + SEPARATOR +
                                    dataInizio + SEPARATOR +
                                    dataFine + SEPARATOR +
                                    dataInizioEffettiva + SEPARATOR +
                                    dataFineEffettiva + SEPARATOR +
                                    p.getTitolo() + SEPARATOR +
                                    unEncodedState.replace(';', ',')
                                   );
                        }
                        itCounts++;
                    } while (itCounts < listWP.size());
                    success = itCounts;
                }
            } catch (AttributoNonValorizzatoException anve) {
                String msg = FOR_NAME + "Attributo obbligatorio di bean non valorizzato; problema nell\'accesso a valori ai fini della generazione del file.\n";
                log.severe(msg); 
                out.println(anve.getMessage());
            } catch (CommandException ce) {
                log.severe(FOR_NAME + "Si e\' verificato un problema nel recupero di elenco work packages" + ce.getMessage());
                out.println(ce.getMessage());
            } catch (NullPointerException npe) {
                log.severe(FOR_NAME + "Si e\' verificato un problema di puntamento: applicazione terminata!" + npe.getMessage());
                out.println(npe.getMessage());
            } catch (Exception e) {
                log.severe(FOR_NAME + "Problema nella fprintf di Data" + e.getMessage());
                out.println(e.getMessage());
            }
        }
        /* **************************************************************** *
         *                Contenuto files CSV per Report di progetto             *
         * **************************************************************** */
        else if (req.getParameter(entToken).equalsIgnoreCase("gareavcp")) { /*
            try {            
                GareAVCPCommand avcp = (GareAVCPCommand) cmd;
                Vector<LottoBean> elencoGare = avcp.requestByContent(req, lingue);
                String unitaOperativa = new String("");
                String areaDiDirezione = new String("");
                String direzione = new String("");
                String biblioteca = new String("");
                String struttura = new String("");
                // Scrittura file CSV
                out.println("N." + SEPARATOR +
                            bundle.getString("CodiceCig") + SEPARATOR + 
                            bundle.getString("Oggetto") + SEPARATOR + 
                            bundle.getString("DataPubbl") + SEPARATOR + 
                            bundle.getString("TipologiaGara") + SEPARATOR + 
                            bundle.getString("SceltaContraente") + SEPARATOR +
                            bundle.getString("ImportoDiAggiudicazione") + SEPARATOR + 
                            bundle.getString("ImportoComplessivo") + SEPARATOR +
                            bundle.getString("PartecipantiGara") + SEPARATOR +
                            bundle.getString("DataInizio") + SEPARATOR +
                            bundle.getString("DataFine") + SEPARATOR +
                            bundle.getString("Anno") + SEPARATOR + 
                            bundle.getString("Direzione") + SEPARATOR +
                            bundle.getString("AreaDiDirezione") + SEPARATOR +
                            bundle.getString("UnitaOperativa").replace("&agrave;", "a\'") + SEPARATOR +
                            bundle.getString("StrutturaServizio") + " " + 
                            bundle.getString("DelDipartimento") + "/" + 
                            bundle.getString("DellaScuola") + SEPARATOR +
                            bundle.getString("Biblioteca")
                            );
                if (elencoGare.size() > 0) {
                    int itCounts = 0, record = 0;
                    do {
                        LottoBean lotto = elencoGare.elementAt(itCounts);
                        GaraBean primaGara = (GaraBean) lotto.getGara().firstElement();
                        // Calcolo dei partecipanti
                        StringBuffer partecipanti = new StringBuffer();
                        // Vector<EnteEsternoBean> partecipantiSingoli
                        Vector<?> partecipantiSingoli = lotto.getPartecipantiSingoli();
                        // Vector<GruppoOperatoriBean> partecipantiGruppi
                        Vector<?> partecipantiGruppi = lotto.getPartecipantiGruppi();
                        // Numero di partecipanti
                        int numPartecipante = 1;
                        if (!partecipantiSingoli.isEmpty()) {
                            for (int i = 0; i < partecipantiSingoli.size(); i++) {
                                EnteEsternoBean partecipante = (EnteEsternoBean) partecipantiSingoli.get(i);
                                partecipanti.append(numPartecipante);
                                partecipanti.append(". ");
                                partecipanti.append(partecipante.getRagioneSociale());
                                if (partecipante.isAggiudicatario())
                                    partecipanti.append(" (*) ");
                                numPartecipante++;
                            }
                            for (int i = 0; i < partecipantiGruppi.size(); i++) {
                                GruppoOperatoriBean gruppo = (GruppoOperatoriBean) partecipantiGruppi.get(i);
                                if (!gruppo.getOperatori().isEmpty()) {
                                    Vector<EnteEsternoBean> operatori = gruppo.getOperatori();
                                    partecipanti.append(" Gruppo: ");
                                    if (gruppo.isAggiudicatario()) {
                                        partecipanti.append("(*) ");
                                    }
                                    numPartecipante = 1;
                                    for(int j = 0; j < operatori.size(); j++) {
                                        EnteEsternoBean operatore = operatori.get(j);
                                        partecipanti.append(numPartecipante);
                                        partecipanti.append(". ");
                                        partecipanti.append(operatore.getRagioneSociale());
                                        partecipanti.append(" (");
                                        partecipanti.append(operatore.getRuoloInGruppoOE().substring(3, operatore.getRuoloInGruppoOE().length()));
                                        partecipanti.append(") ");
                                        numPartecipante++;
                                    }
                                }
                            }
                        }
                        // Controlli sull'input 
                        String dataInizio = (lotto.getDatainiziolavori() != null ? String.valueOf(lotto.getDatainiziolavori()) : bundle.getString("NonInserito").toUpperCase());
                        String dataFine = (lotto.getDatafinelavori() != null ? String.valueOf(lotto.getDatafinelavori()) : bundle.getString("NonInserito").toUpperCase());
                        String importoAggiudicazione = (lotto.getImportoaggiudicazione() > OrganizzazioneCommand.NONE ? String.format("%.2f", lotto.getImportoaggiudicazione()).replace('.', ',') : bundle.getString("NonInserito").toUpperCase());
                        String importoLiquidazione = (lotto.getImportosommeliquidate() > OrganizzazioneCommand.NONE ? String.format("%.2f", lotto.getImportosommeliquidate()).replace('.', ',') : bundle.getString("NonInserito").toUpperCase());
                        if (primaGara.getUnitaMittente() != null){
                            unitaOperativa = (primaGara.getUnitaMittente().getNome() != null ? primaGara.getUnitaMittente().getNome() : new String(""));
                        } else {
                            unitaOperativa = new String("");
                        }
                        if (primaGara.getAreaMittente() != null){
                            areaDiDirezione = (primaGara.getAreaMittente().getNome() != null ? String.valueOf(primaGara.getAreaMittente().getNome()) : new String(""));
                        } else {
                            areaDiDirezione = new String("");
                        }
                        if (primaGara.getDirMittente() != null){
                            direzione = (primaGara.getDirMittente().getNome() != null ? String.valueOf(primaGara.getDirMittente().getNome()) : new String(""));
                        } else {
                            direzione = new String("");
                        }
                        if (primaGara.getBiblioMittente() != null){
                            biblioteca = (primaGara.getBiblioMittente().getNome() != null ? String.valueOf(primaGara.getBiblioMittente().getNome()) : new String(""));
                        } else {
                            biblioteca = new String("");
                        }
                        if (primaGara.getStruttMittente() != null){
                            struttura = (primaGara.getStruttMittente().getNome() != null ? String.valueOf(primaGara.getStruttMittente().getNome()) : new String(""));
                        } else {
                            struttura = new String("");
                        }
                        // Stampa il contenuto del file
                        out.println(
                                    String.valueOf(++record) + SEPARATOR +
                                    lotto.getCodiceCig().replace(';', ',') + SEPARATOR +
                                    lotto.getNome().replace(';', ',').replace('’', '\'') + SEPARATOR +
                                    primaGara.getDataAlbo() + SEPARATOR + 
                                    primaGara.getTipo() + SEPARATOR + 
                                    lotto.getSceltacontraente() + SEPARATOR + 
                                    importoAggiudicazione + SEPARATOR +
                                    importoLiquidazione + SEPARATOR +
                                    partecipanti.toString().replace(';', ',') + SEPARATOR +
                                    dataInizio + SEPARATOR +
                                    dataFine + SEPARATOR +
                                    primaGara.getAnno() + SEPARATOR +
                                    direzione + SEPARATOR +
                                    areaDiDirezione + SEPARATOR +
                                    unitaOperativa + SEPARATOR +
                                    struttura + SEPARATOR +
                                    biblioteca                                    
                                   );
                        itCounts++;
                    } while (itCounts < elencoGare.size());
                    success = itCounts;
                }
            } catch (CommandException ce) {
                log.severe(FOR_NAME + "Si e\' verificato un problema nel recupero di elenco gare AVCP" + ce.getMessage());
                out.println(ce.getMessage());
            } catch (IndexOutOfBoundsException iobe) {
                String msg = FOR_NAME + "Si e\' verificato un problema nello scorrimento di liste.\n" + iobe.getMessage();
                log.severe(msg + "Probabile puntamento fuori tabella!");
                out.println(msg);            
            } catch (ClassCastException cce) {
                String msg = FOR_NAME + "Problema nel definire il tipo di una risorsa.\n" + cce.getMessage();
                log.severe(msg);
                out.println(msg);
            } catch (NullPointerException npe) {
                StackTraceElement[] stackTrace = npe.getStackTrace();
                StringBuffer trace = new StringBuffer("\n");
                for (StackTraceElement stack : stackTrace) 
                    trace.append(stack.toString()).append("\n");
                String msg = FOR_NAME + "Si e\' verificato un problema di puntamento a: " + npe.getMessage() + trace.toString();
                log.severe(msg);
                out.println(msg);
            } catch (RuntimeException re) {
                String msg = FOR_NAME + "Si e\' verificato un problema a tempo di esecuzione\n." + re.getMessage();
                log.severe(msg);
                out.println(msg);
            } catch (Exception e) {
                log.severe(FOR_NAME + "Problema nella fprintf di Data" + e.getMessage());
                out.println(e.getMessage());
            }*/
        }
        else {
            String msg = FOR_NAME + "La Servlet Data non accetta la stringa passata come valore di 'ent': " + req.getParameter(entToken);
            log.severe(msg + "Tentativo di indirizzare alla Servlet Data una richiesta non gestita. Hacking test?\n");
            throw new IOException(msg);
        }
        return success;
    }
    
    
    /**
     * lookupCommand restituisce la classe Command associata parametro
     * d'input <code>cmd</code>, come specificato nella HashTable
     * <code>command</code>. Se la stringa non è presente
     * nella hashtable command, allora lancia l'eccezione
     * <code>CommandExpection</code>.
     *
     * @param cmd string che individua la classe Command da creare.
     * @return <code>Command</code> una classe Command
     * @exception CommandException  se il parametro <code>cmd</code> non corrisponde a nessun caso.
     */
    private Command lookupCommand(@SuppressWarnings("hiding") String cmd)
                           throws CommandException {
        if (commands.containsKey(cmd.toLowerCase()))
            return commands.get(cmd.toLowerCase());
        throw new CommandException(FOR_NAME + "Classe Command non valida: " + cmd.toLowerCase());
    }
    
}