/*
 *   University on Line (uol), web application to publish the faculties, 
 *   departments and university informations.
 *   Copyright © 2002-2016 by Università degli Studi di Verona. Verona (I).
 *   All rights reserved.
 *
 *   This program is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation; either version 2 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program; if not, write to the Free Software
 *   Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *   or see <http://www.gnu.org/licenses/>
 *
 *   Giovanroberto Torre <giovanroberto.torre@univr.it>
 *   Direzione Servizi Informatici di Ateneo
 *   Universita' degli Studi di Verona
 *   Via Paradiso 6
 *   37129 Verona (Italy)
 */
package it.alma;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Vector;
import java.util.logging.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.alma.bean.ItemBean;
import it.alma.command.Command;
import it.alma.exception.CommandException;
import it.alma.exception.WebStorageException;


/**
 * <p><code>Data</code> &egrave; la servlet della web-application aol
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
 * <pre>ent=&lt;nome&gt;</pre>
 * dove 'nome' è il valore del parametro 'ent' che identifica 
 * l'azione da compiere al fine di generare i record.<br />
 * Oltre al parametro <code>'ent'</code> possono essere presenti anche
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
 * dovrebbero essere progressivamente abbandonate a partire da AOL 3.0 
 * <em>(data di rilascio: 27/06/2013)</em>
 * in favore dell'uso di questa servlet.
 * </p>
 *
 * @version 1.7
 * @author <a href="mailto:giovanroberto.torre@univr.it">Giovanroberto Torre</a>
 * 
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
     * delle classi Command di <code>aol</code>
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
         * Attiva la connessione al database
         */
        try {
            db = new DBWrapper();
        }
        catch (WebStorageException wse) {
            String error = FOR_NAME + "Non e\' possibile avere una connessione al database: " + wse.getMessage();
            log.severe(error);
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
        entTokens.add("incarichi");
        entTokens.add("gareavcp");    
        ItemBean voceMenu = null;
        Command classCommand = null;
        for (int i = 0; i < classiCommand.size(); i++) {
            voceMenu = classiCommand.get(i);
            log.info(FOR_NAME + "Il nome della voce di menu vale: " + voceMenu.getNome());
            if (entTokens.contains(voceMenu.getNome())) {
                try {
                    log.info(FOR_NAME + "Valore di entTokens corrispondente al nome della voce di menu: " + voceMenu.getNome() + "\nTentativo di istanziare it.univr.di.uol.command." + voceMenu.getNomeClasse());
                    classCommand = (Command) Class.forName("it.univr.di.uol.command." + voceMenu.getNomeClasse()).newInstance();
                    log.info(FOR_NAME + "Tentativo di inizializzare it.univr.di.uol.command." + voceMenu.getNomeClasse());
                    classCommand.init(voceMenu);
                    log.info(FOR_NAME + "Tentativo di aggiungere la classe comune alle command ammesse.");
                    commands.put(voceMenu.getNome(), classCommand);
                    log.info(FOR_NAME + "La dimensione della tabella commands di Data al momento vale: " + commands.size());
                } catch (ClassNotFoundException cnfe) {
                    try {
                        log.info(FOR_NAME + "Riprova: valore di entTokens corrispondente al nome della voce di menu: " + voceMenu.getNome() + "\nTentativo di istanziare it.univr.di.uol.aol.command." + voceMenu.getNomeClasse());
                        classCommand = (Command) Class.forName("it.univr.di.uol.aol.command." + voceMenu.getNomeClasse()).newInstance();
                        log.info(FOR_NAME + "Tentativo di inizializzare it.univr.di.uol.aol.command." + voceMenu.getNomeClasse());
                        classCommand.init(voceMenu);
                        log.info(FOR_NAME + "Tentativo di aggiungere la classe di aol alle command ammesse.");
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
        try {
            cmd = lookupCommand(req.getParameter(entToken));
        } catch (CommandException ce) {
           req.setAttribute("javax.servlet.jsp.jspException", ce);
           log("Errore: " + ce);
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
            /* Il db di WebIntegrato e' codificato in UTF-8; 
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
            fprintf(req, res);
        } catch (Exception e) {
            log.severe("Problema nella doGet di Data" + e.getMessage());
        }
    }
    
    
    /**
     * <p>Genera il contenuto dello stream, che questa classe tratta 
     * sotto forma di file, che viene trasmesso sulla risposta in output,
     * a seconda del valore di <code>'ent'</code> che riceve in input.</p>
     * <p>
     * Storicamente, in programmazione <code> C, C++ </code> e affini, 
     * le funzioni che scrivono sull'outputstream si chiamano tutte 
     * <code>printf</code>, precedute da vari prefissi a seconda di
     * quello che scrivono e dove lo scrivono.<br />
     * <code>fprintf</code> &egrave; la funzione della libreria C che
     * invia output formattati allo stream, identificato con un puntatore
     * a un oggetto FILE passato come argomento 
     * (<small>per approfondire, 
     * <a href="http://www.tutorialspoint.com/c_standard_library/c_function_fprintf.htm">
     * v. p.es. qui</a></small>).<br />
     * Qui per analogia, pi&uacute; che altro nella forma di una "dotta"
     * citazione (per l'ambito informatico) il metodo della Data che 
     * scrive il contenuto vero e proprio del file che viene passato
     * al client, viene chiamato allo stesso modo di questa "storica" funzione, 
     * ma il contesto degli oggetti e degli argomenti 
     * &egrave; ovviamente completamente diverso.</p>
     * 
     * @param req la HttpServletRequest contenente il valore di 'ent' e gli altri parametri necessari a formattare opportunamente l'output
     * @param res la HttpServletResponse utilizzata per ottenere il 'Writer' su cui stampare il contenuto, cioe' il file stesso
     * @return <code>int</code> - un valore intero restituito per motivi storici. 
     *                            Tradizionalmente, tutte le funzioni della famiglia x-printf restituiscono un intero, 
     *                            che vale il numero dei caratteri scritti - qui il numero delle righe scritte - in caso di successo 
     *                            oppure -1 (o un altro numero negativo) in caso di fallimento 
     * @throws ServletException   java.lang.Throwable.Exception.ServletException che viene sollevata se manca un parametro di configurazione considerato obbligatorio o per via di qualche altro problema di puntamento
     * @throws IOException        java.io.IOException che viene sollevata se si verifica un puntamento a null o in genere nei casi in cui nella gestione del flusso informativo di questo metodo si verifica un problema
     */
    private int fprintf(HttpServletRequest req, HttpServletResponse res)
                 throws ServletException, IOException {
        /*
         *  Genera l'oggetto per lo standard output
         */
        PrintWriter out = res.getWriter();
        /*
         *  Per ottimizzare
         */
        ServletContext servletContext = getServletContext();
        /*
         *  Tradizionalmente, ogni funzione della famiglia x-printf 
         *  restituisce un intero
         */
        int success = -1;
        /* **************************************************************** *
         *          Contenuto files CSV per Report di WBS          *
         * **************************************************************** */
        if (req.getParameter(entToken).equals("incarichi")) { /*
            try {            
                IncarichiPersonaleCommand ip = (IncarichiPersonaleCommand) cmd;
                Vector<IncaricoBean> elencoIncarichi = ip.requestByContent(req);
                HashMap<Integer, String> elencoTipiContratto = IncarichiPersonaleCommand.NOMI_TIPO_INCARICO;
                // Scrittura file CSV
                out.println("N." + SEPARATOR +
                            "Persona" + SEPARATOR + 
                            "Tipo Incarico" + SEPARATOR + 
                            "Tipo Contratto" + SEPARATOR + 
                            "Oggetto" + SEPARATOR + 
                            "Estremi del provvedimento" + SEPARATOR +
                            "Data provvedimento" + SEPARATOR + 
                            "Data inizio" + SEPARATOR +
                            "Data fine" + SEPARATOR +
                            "Compenso" + SEPARATOR +
                            "Struttura conferente");
                if (elencoIncarichi.size() > 0) {
                    int itCounts = 0, record = 0;
                    do {
                        IncaricoBean incarico = elencoIncarichi.elementAt(itCounts);
                        out.println(
                                    ++record + SEPARATOR +
                                    incarico.getCognomePersona().replace(';', ',') + " " +
                                    incarico.getNomePersona().replace(';', ',') + SEPARATOR +
                                    elencoTipiContratto.get(incarico.getTipoIncarico()).replace(';', ',') + SEPARATOR +
                                    incarico.getTipoContratto() + SEPARATOR +
                                    incarico.getOggetto().replace(';', ',') + SEPARATOR +
                                    incarico.getProvvedimento().replace(';', ',') + SEPARATOR +
                                    incarico.getDataProvvedimento() + SEPARATOR +
                                    incarico.getDataInizio() + SEPARATOR +
                                    incarico.getDataFine() + SEPARATOR +
                                    Float.toString(incarico.getCompensoPrevisto()).replace('.', ',') + SEPARATOR +
                                    incarico.getNomeStruttura().replace(';', ',')
                                   );
                        itCounts++;
                    } while (itCounts < elencoIncarichi.size());
                    success = itCounts;
                }
            } catch (CommandException ce) {
                log.severe(FOR_NAME + "Si e\' verificato un problema nel recupero di elenco incarichi" + ce.getMessage());
                out.println(ce.getMessage());
            } catch (Exception e) {
                log.severe(FOR_NAME + "Problema nella fprintf di Data" + e.getMessage());
                out.println(e.getMessage());
            }*/
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