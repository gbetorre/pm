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

import java.io.File;
import java.io.IOException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Vector;
import java.util.logging.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import com.oreilly.servlet.ParameterParser;

import it.alma.bean.DepartmentBean;
import it.alma.bean.PersonBean;
import it.alma.bean.ProjectBean;
import it.alma.command.Command;
import it.alma.exception.CommandException;
import it.alma.exception.WebStorageException;


/**
 * <p>FileManager &egrave; la classe che si occupa della gestione dei files
 * caricati dall'utente, della generazione di link per il download, 
 * l'impostazione dei privilegi, secondo eventuali policy, 
 * dell'aggiornamento e di quan'altro occorra alle funzioni di gestione allegati 
 * della web-application <code>pol</code>.</p>
 * 
 * @author <a href="mailto:giovanroberto.torre@univr.it">Giovanroberto Torre</a>
 */
@MultipartConfig(fileSizeThreshold=1024*1024*2, // 2MB
                 maxFileSize=1024*1024*10,      // 10MB
                 maxRequestSize=1024*1024*50)   // 50MB

public class FileManager extends HttpServlet {
    /**
     * Talking const for the preferred, standard separator
     * between entity names and attribute names.
     */
    public static final char STD_SPACER = '_';
    /**
     * La serializzazione necessita della dichiarazione 
     * di una costante di tipo long identificativa della versione seriale. 
     * (Se questo dato non fosse inserito, verrebbe calcolato in maniera automatica
     * dalla JVM, e questo potrebbe portare a errori riguardo alla serializzazione). 
     */
    private static final long serialVersionUID = 1L;
    /**
     *  The name of the present class (useful for talking error messages).
     */
    static final String FOR_NAME = "\n" + Logger.getLogger(new Throwable().getStackTrace()[0].getClassName()) + ": ";
     /**
     * Logger della classe per scrivere i messaggi di errore.
     */
    private Logger log = Logger.getLogger(FileManager.class.getName());
    /**
     * Name of the directory where uploaded files will be saved, relative to
     * the web application directory.
     */
    private static final String SAVE_DIR = "upload";
    /**
     * Suffix for the directory where uploaded files will be saved, relative to
     * the web application specific functionality.
     */
    private static final String STANDARD_DIR_SUFFIX = "_all";
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
     * Attributo per il valore del parametro obbligatorio 'q'
     */
    private static String entToken;
    /**
     * Command che gestisce la pagina da cui &egrave; partita la richiesta
     */
    private Command cmd = null; 
    /**
     * Lista contenente tutti i token di interesse, ovvero ammesi a generare files
     */
    private LinkedList<String> entTokens = new LinkedList<String>();
    /**
     * Lista contenente tutti i token di interesse, ovvero ammesi a generare files
     */
    private static LinkedList<String> forbiddenExt = null;
    /**
     *  Carica la lista con le estensioni vietate
     */
    public static final String[] FORBIDDEN_EXTENSIONS = {"apk",
                                                         "bash","bat",
                                                         "cmd","com","cpl",
                                                         "dll","docm","dotm",
                                                         "exe",
                                                         "gadget",
                                                         "hta","htm","html",
                                                         "inf","info",
                                                         "jar","js","jse",
                                                         "lnk",
                                                         "msc","msh","msh1","msh2","mshxml","msh1xml","msh2xml","msi","msp",
                                                         "nfo",
                                                         "potm","ppam","ppsm","pptm","ps1","ps1xml","ps2","ps2xml","psc1","psc2","py",
                                                         "reg",
                                                         "scf","scr","scf","sfx","sldm","sh",
                                                         "tmp",
                                                         "vb","vbe","vbs",
                                                         "ws","wsc","wsf","wsh",
                                                         "xlam","xlsm","xltm"};    
    
    
    /** 
     * <p>Inizializza, staticamente, alcune variabili globali.</p> 
     * 
     * @param config la configurazione usata dal servlet container per passare informazioni alla servlet <strong>durante l'inizializzazione</strong>
     * @throws ServletException una eccezione che puo' essere sollevata quando la servlet incontra difficolta'
     */
    @Override
    public void init(ServletConfig config) throws ServletException {
        /*
         * Inizializzazione da superclasse
         */
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
        /*
         *  Caricamento degli 'ent' ammessi alla generazione di upload di file
         */
        entTokens.add(Query.PROJECT_PART);
        entTokens.add(Query.MONITOR_PART);
        /**
         * Lista contenente tutti i token di interesse, ovvero ammesi a generare files
         */
        //String[] extensions = {"apk","bash","bat","cmd","com","cpl","dll","docm","dotm","exe","gadget","hta","htm","html","inf","info","jar","js","jse","lnk","msc","msh","msh1","msh2","mshxml","msh1xml","msh2xml","msi","msp","potm","ppam","ppsm","pptm","ps1","ps1xml","ps2","ps2xml","psc1","psc2","py","reg","scf","scr","scf","sfx","sldm","sh","tmp","vb","vbe","vbs","ws","wsc","wsf","wsh","xlam","xlsm","xltm"};
        // Arrays.asList(String[]) returns a List<String>!
        forbiddenExt = new LinkedList<String>(Arrays.asList(FORBIDDEN_EXTENSIONS));
        
        //ArrayList<String> extensionsList = Arrays.asList(extensions);
        //extAllowed = Arrays.asList(extensions);
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
        } catch (Exception e) {
            String msg = "Problemi nel metodo doGet.\n" + e.getMessage();
            log.severe(msg);
            throw new ServletException(msg, e);
        }
    }
    
    
    /**
     * <p>Handles file upload.</p>
     * <p>Utilizza un approccio di programmazione difensiva per garantire
     * la correttezza dell'input.</p>
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
        HttpSession ses = null;                             // Sessione
        ParameterParser parser = new ParameterParser(req);  // Crea il parser per i parametri
        PersonBean user = null;                             // Utente loggato
        /* ******************************************************************** *
         *              Variabili per gestione upload da pagina                 *
         * ******************************************************************** */
        // Recupera o inizializza 'q' 
        entToken = parser.getStringParameter("q", Utils.VOID_STRING);
        // Recupera o inizializza 'id progetto' (upload da Status)
        int idPrj = parser.getIntParameter("prj-id", Utils.DEFAULT_ID);
        // Recupera o inizializza 'id dipartimento'     (upload da Monitor)
        int idDip = parser.getIntParameter("dip-id", Utils.DEFAULT_ID);
        /* ******************************************************************** *
         *           I - Control :: the user Session MUST BE valid              *
         *              to avoid the "garden gate situation"                    *
         * ******************************************************************** */
        try {
            // Recupera la sessione creata e valorizzata per riferimento nella req dal metodo authenticate
            ses = req.getSession(Query.IF_EXISTS_DONOT_CREATE_NEW);
            if (ses == null) {
                throw new CommandException("Attenzione: controllare di essere autenticati nell\'applicazione!\n");
            }
            user = (PersonBean) ses.getAttribute("usr");
            if (user == null) {
                log.severe("Utente punta a null!\n");
                throw new ServletException("Attenzione: controllare di essere autenticati nell\'applicazione!\n");
            }
        } catch (IllegalStateException ise) {
            String msg = FOR_NAME + "Impossibile redirigere l'output. Verificare se la risposta e\' stata gia\' committata.\n";
            log.severe(msg);
            throw new ServletException(msg + ise.getMessage(), ise);
        } catch (NullPointerException npe) {
            String msg = FOR_NAME + "Si e\' verificato un problema di puntamento a null, probabilmente nel tentativo di recuperare l\'utente.\n";
            log.severe(msg);
            throw new ServletException("Attenzione: controllare di essere autenticati nell\'applicazione!\n" + npe.getMessage(), npe);
        } catch (Exception e) {
            String msg = FOR_NAME + "Si e\' verificato un problema.\n";
            log.severe(msg);
            throw new ServletException(msg + e.getMessage(), e);
        }
        /* ******************************************************************** *
         *  II - Control :: user MUST HAVE writable project(s) or writable dept *
         * ******************************************************************** */
        // Recupera i progetti su cui l'utente ha diritti di scrittura
        Vector<ProjectBean> writablePrj = (Vector<ProjectBean>) ses.getAttribute("writableProjects"); // I'm confident about the types...
        // Recupera i dipartimenti in cui l'utente ha un ruolo superiore a TL
        Vector<DepartmentBean> writableDepts = (Vector<DepartmentBean>) ses.getAttribute("writableDeparments");
        // Se non ci sono progetti scrivibili ed è stata inviata una form post multipart c'è qualcosa che non va!!!
        if (writablePrj == null) {
            ses.invalidate();
            String msg = FOR_NAME + "L\'utente ha tentato di caricare un file pero\' non sono stati trovati progetti scrivibili: problema!.\n";
            log.severe(msg);
            throw new ServletException("Attenzione: controllare di essere autenticati nell\'applicazione!\n");
        }
        // Se è stato passato un id dipart ma l'utente non ha dipartimenti scrivibili, c'è qualcosa che non va!!
        if (idDip > Utils.DEFAULT_ID && writableDepts == null) {
            ses.invalidate();
            String msg = FOR_NAME + "L\'utente ha tentato di caricare un file pero\' non sono stati trovati dipartimenti monitorabili: problema!.\n";
            log.severe(msg);
            throw new ServletException("Attenzione: controllare di essere autenticati nell\'applicazione!\n");
        }
        /* ******************************************************************** *
         *         III - Control :: token MUST BE among allowed tokens          *
         * ******************************************************************** */
        // Controlla che il token corrente sia abilitato all'upload
        if (!entTokens.contains(entToken)) {
            String msg = FOR_NAME + "Hai chiamato il FileManager ma non sei in un token ammesso: allora: che vuoi??\n";
            log.severe(msg + "Non sei piu\' rock? Non sei piu\' metal?\n");
            throw new ServletException(msg);
        }
        /* ******************************************************************** *
         *       III - Control :: there's an id and MUST BE a valid id          *
         * ******************************************************************** */
        if (idPrj > Utils.DEFAULT_ID) {
            uploadStatus(req, res, parser, user, writablePrj);
        } else if (idDip > Utils.DEFAULT_ID) {
            uploadMonitor(req, res, parser, user, writableDepts);
        } else {    // Se non riesce a recuperare né 'id progetto' né 'id dipartimento' qualcosa non va, ed esce
            String msg = "Problemi nel recupero di un identificativo necessario.\n";
            log.severe(msg);
            throw new ServletException(msg);
        }

    }

    
    private void uploadStatus(HttpServletRequest req, 
                              HttpServletResponse res, 
                              ParameterParser parser,
                              PersonBean user,
                              Vector<ProjectBean> writablePrj)
                       throws ServletException, IOException {
        DBWrapper db = null;                                // DataBound
        String entityName = null;
        String attributeName = null;
        // Struttura contenente gli estremi di ogni allegato
        HashMap<String, Object> params = new HashMap<String, Object>(13);
        // Recupera o inizializza 'q' 
        entToken = parser.getStringParameter("q", Utils.VOID_STRING);
        /* ******************************************************************** *
         *              Variabili per gestione upload da pagina                 *
         * ******************************************************************** */
        // Recupera o inizializza 'id progetto' (upload da Status)
        int idPrj = parser.getIntParameter("prj-id", Utils.DEFAULT_ID);
        // Recupera o inizializza "id status"   (upload da Status)
        int idSts = parser.getIntParameter("sts-id", Utils.DEFAULT_ID);
        /* ******************************************************************** *
         *                      Effettua la connessione                         *
         * ******************************************************************** */
        // Effettua la connessione al databound
        try {
            db = new DBWrapper();
        } catch (WebStorageException wse) {
            throw new ServletException(FOR_NAME + "Non riesco ad instanziare databound.\n" + wse.getMessage(), wse);
        }
        /* ******************************************************************** *
         *                           IV - Control                               * 
         *      if the upload is about a project, the id of current project     * 
         *                MUST BE among those writable by user                  *
         * ******************************************************************** */
        // Controllo per upload da status
        try {
            // Se non riesce a trovare 'id progetto' tra quelli scrivibili, anche qui c'è qualcosa che non va, ed esce
            if (!db.userCanWrite(idPrj, writablePrj)) {
                String msg = FOR_NAME + "Si e\' verificato un problema l\'id del progetto corrente non e\' tra quelli scrivibili dall\'utente.\n";
                log.severe(msg);
                throw new ServletException(msg);
            }
        } catch (WebStorageException wse) {
            String msg = FOR_NAME + "Si e\' verificato un problema nel controllo che l\'id del progetto corrente sia tra quelli scrivibili dall\'utente.\n" + wse.getMessage();
            log.severe(msg);
            throw new ServletException(msg, wse);
        }
        /* ******************************************************************** *
         *              ONLY IF previous controls are ok, go on                 * 
         *        (Se i controlli vanno a buon fine gestisce l'upload)          *
         * ******************************************************************** */
        // Nome della sottodirectory che conterrà i files del sottoprogetto
        final String PRJ_DIR = String.valueOf(idPrj);
        // Nome della sottodirectory che conterrà gli allegati specifici
        String ALL_DIR = null;
        // Controlla che il token corrente sia abilitato all'upload
        if (entTokens.contains(entToken)) {
            if (entToken.equals(Query.PROJECT_PART)) {
                entityName = "avanzamento";
                attributeName = "all";
                ALL_DIR = entityName + STANDARD_DIR_SUFFIX;
            }
        } else {
            String msg = FOR_NAME + "Hai chiamato il FileManager per gestire l'upload dallo status ma non sei nel token ammesso: allora: che vuoi??\n";
            log.severe(msg + "Non sei piu\' rock? Non sei piu\' metal?\n");
            throw new ServletException(msg);
        }
        try {
            /* **************************************************************** *
             *     Checks if the directories do exist; if not, it makes them    *
             * **************************************************************** */
            // Gets absolute path of the web application (/)
            String appPath = req.getServletContext().getRealPath(Utils.VOID_STRING);
            log.info(FOR_NAME + "appPath vale: " + appPath);
            // Creates the documents root directory (/documents) if it does not exists
            String documentsRootName = appPath + getServletContext().getInitParameter("urlDirectoryDocumenti");
            File documentsRoot = new File(documentsRootName);
            if (!documentsRoot.exists()) {
                documentsRoot.mkdir();
                log.info(FOR_NAME + documentsRootName + " created.\n");
            }
            // Constructs path of the directory to save uploaded file 
            String uploadPath =  documentsRootName + File.separator + SAVE_DIR;
            // Creates the save directory (/documents/upload) if it does not exists
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdir();
                log.info(FOR_NAME + uploadPath + " created.\n");
            }
            // Constructs path of the directory to save attached file(s) 
            String attachPath =  uploadPath + File.separator + ALL_DIR;
            // Creates the save directory (/documents/upload/status_attachs) if it does not exists
            File attachDir = new File(attachPath);
            if (!attachDir.exists()) {
                attachDir.mkdir();
                log.info(FOR_NAME + attachPath + " created.\n");
            }
            // Constructs path of the directory to save specific uploaded file 
            String projPath =  attachPath + File.separator + PRJ_DIR;
            // Creates the save directory if it does not exists
            File projDir = new File(projPath);
            if (!projDir.exists()) {
                projDir.mkdir();
                log.info(FOR_NAME + projPath + " created.\n");
            }
            /* **************************************************************** *
             *              Finally it may manage streams as files!             *
             * **************************************************************** */
            // Retrieve and manage the document title
            String title = parser.getStringParameter("doc-name", Utils.VOID_STRING);
            // Recupera e scrive tutti gli allegati
            for (Part part : req.getParts()) {
                String fileName = extractFileName(part);
                if (fileName.equals(Utils.VOID_STRING)) {
                    continue;
                }
                // Creates the file object
                File file = new File(fileName);
                // Refines the fileName in case it is an absolute path
                fileName = null;
                fileName = file.getName();
                /* ************************************************************ *
                 *                     Resolves MIME type(s)                    *
                 * ************************************************************ */
                // Get the default library (JRE_HOME/lib/content-types.properties)               
                FileNameMap fileNameMap = URLConnection.getFileNameMap();
                // Tries to get the MIME type from default library
                String mimeType = fileNameMap.getContentTypeFor(file.getName());
                // If not, tries to get the MIME type guessing that from a 
                // service-provider loading facility - see {@link Files.probeContentType}
                if (mimeType == null) {
                    Path path = new File(fileName).toPath();
                    mimeType = Files.probeContentType(path);
                }
                /* ************************************************************ *
                 *                Retrieve and manage file extension            *
                 * ************************************************************ */
                String extension = fileName.substring(fileName.lastIndexOf("."));
                if (forbiddenExt.contains(extension)) {
                    String msg = FOR_NAME + "Si e\' verificato un problema di estensione non ammessa; l\'utente ha tentato di caricare un file non consentito.\n";
                    log.severe(msg);
                    throw new ServletException("Attenzione: il formato del file caricato non e\' consentito.\n");
                }
                /* ************************************************************ *
                 *                          Given name                          *
                 * ************************************************************ */
                String givenName = this.makeName(entityName, attributeName, db);
                log.info(FOR_NAME + "Nome autogenerato per il file: " + givenName);
                /* ************************************************************ *
                 *      Prepares the table hash with parameters to insert       *
                 * ************************************************************ */
                log.info(FOR_NAME + "Salvataggio previsto in tabella relativa all\'entita\': " + givenName);
                // Entity name
                params.put("nomeEntita", entityName);
                // Attribute name
                params.put("nomeAttributo", attributeName);
                // Real name of the uploaded file in the file system
                params.put("file", givenName);
                // Extension of the original file
                params.put("ext", extension);
                // Name of the original file chosen by user (named user file)
                params.put("nome", fileName);
                // Which one entity instance
                params.put("belongs", idSts);
                // The title chosen by user for the attachment
                params.put("titolo", checkPrime(title));              
                // Calculated size for now is zero
                params.put("dimensione", getFileSizeBytes(file));
                // Calculated mime
                params.put("mime", mimeType);
                // Logged user
                params.put("usr", user);
                // Write to the database
                db.setFileDoc(params);
                // Write to the filesystem
                part.write(projPath + File.separator + givenName + extension);
                log.info(FOR_NAME + "Salvataggio in file system effettuato.\n");
                // Read the file just written
                File fileVolume = new File(projPath + File.separator + givenName + extension);
                // Calculated size from file system
                params.put("dimensione", getFileSizeBytes(fileVolume));
                // Post Update FileDoc
                db.postUpdateFileDoc(params);
            }
            req.setAttribute("message", "Upload has been done successfully!");
            flush(req, res, templateJsp, "?q=pol&p=sts&id=" + idPrj + "&ids=" + idSts);
        } catch (IllegalStateException e) {
            throw new ServletException("Impossibile redirigere l'output. Verificare se la risposta e\' stata gia\' committata.\n" + e.getMessage(), e);
        } catch (NullPointerException e) {
            throw new ServletException("Errore nell'estrazione dei dipartimenti che gestiscono il corso.\n" + e.getMessage(), e);
        } catch (Exception e) {
            req.setAttribute("javax.servlet.jsp.jspException", e);
            req.setAttribute("message", e.getMessage());
            // Log dell'evento
            log.severe("Problema: " + e);
            e.printStackTrace();
            // Flusso
            flush(req, res, errorJsp, Utils.VOID_STRING);
            return;
        }
    }
    

    
    private void uploadMonitor(HttpServletRequest req, 
                               HttpServletResponse res, 
                               ParameterParser parser,
                               PersonBean user,
                               Vector<DepartmentBean> writableDepts)
                        throws ServletException, IOException {
        DBWrapper db = null;                                // DataBound
        String entityName = null;
        String attributeName = null;
        // Struttura contenente gli estremi di ogni allegato
        HashMap<String, Object> params = new HashMap<String, Object>(13);
        // Recupera o inizializza 'q' 
        entToken = parser.getStringParameter("q", Utils.VOID_STRING);
        /* ******************************************************************** *
         *              Variabili per gestione upload da pagina                 *
         * ******************************************************************** */
        // Recupera o inizializza 'id dipartimento'     (upload da Monitor)
        int idDip = parser.getIntParameter("dip-id", Utils.DEFAULT_ID);
        // Recupera o inizializza 'id monitoraggio'     (upload da Monitor)
        int idMon = parser.getIntParameter("mon-id", Utils.DEFAULT_ID);
        // Recupera o inizializza 'anno monitoragigo'
        int year = parser.getIntParameter("y", Utils.DEFAULT_ID);
        // Recupera o inizializza "quadro monitoraggio" (upload da Monitor)
        String qM = parser.getStringParameter("qd-id", Utils.VOID_STRING);
        /* ******************************************************************** *
         *                      Effettua la connessione                         *
         * ******************************************************************** */
        // Effettua la connessione al databound
        try {
            db = new DBWrapper();
        } catch (WebStorageException wse) {
            throw new ServletException(FOR_NAME + "Non riesco ad instanziare databound.\n" + wse.getMessage(), wse);
        }
        /* ******************************************************************** *
         *                           IV - Control                               * 
         *      if the upload is about a dept, the id of current department     * 
         *             ad it MUST BE among those writable by user               *
         * ******************************************************************** */
        // Controllo per upload da monitor
        try {
            // Se non riesce a trovare 'id dipartimento' tra quelli scrivibili, c'è qualcosa che non va, ed esce
            if (!db.userCanMonitor(idDip, writableDepts)) {
                String msg = FOR_NAME + "Si e\' verificato un problema l\'id del dipartimento corrente non e\' tra quelli scrivibili dall\'utente.\n";
                log.severe(msg);
                throw new ServletException(msg);
            }
        } catch (WebStorageException wse) {
            String msg = FOR_NAME + "Si e\' verificato un problema nel controllo che l\'id del dipartimento corrente sia tra quelli scrivibili dall\'utente.\n" + wse.getMessage();
            log.severe(msg);
            throw new ServletException(msg, wse);
        }
        /* ******************************************************************** *
         *              ONLY IF previous controls are ok, go on                 * 
         *        (Se i controlli vanno a buon fine gestisce l'upload)          *
         * ******************************************************************** */
        // Nome della sottodirectory che conterrà i files del sottoprogetto
        final String DEP_DIR = String.valueOf(idDip);
        // Nome della sottodirectory che conterrà gli allegati specifici
        String ALL_DIR = null;
        // Controlla che il token corrente sia abilitato all'upload
        if (entTokens.contains(entToken)) {
           if (entToken.equals(Query.MONITOR_PART)) {
               entityName = "monitoraggio";
               attributeName = qM;
               String peculiarDirSuffix = String.valueOf(STD_SPACER) + qM;
               ALL_DIR = entityName + peculiarDirSuffix;
            }
        } else {
            String msg = FOR_NAME + "Hai chiamato il FileManager per gestire l'upload da monitor ma non sei nel token ammesso: allora: che vuoi??\n";
            log.severe(msg + "Non sei piu\' rock? Non sei piu\' metal?\n");
            throw new ServletException(msg);
        }
        try {
            /* **************************************************************** *
             *     Checks if the directories do exist; if not, it makes them    *
             * **************************************************************** */
            // Gets absolute path of the web application (/)
            String appPath = req.getServletContext().getRealPath(Utils.VOID_STRING);
            log.info(FOR_NAME + "appPath vale: " + appPath);
            // Creates the documents root directory (/documents) if it does not exists
            String documentsRootName = appPath + getServletContext().getInitParameter("urlDirectoryDocumenti");
            File documentsRoot = new File(documentsRootName);
            if (!documentsRoot.exists()) {
                documentsRoot.mkdir();
                log.info(FOR_NAME + documentsRootName + " created.\n");
            }
            // Constructs path of the directory to save uploaded file 
            String uploadPath =  documentsRootName + File.separator + SAVE_DIR;
            // Creates the save directory (/documents/upload) if it does not exists
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdir();
                log.info(FOR_NAME + uploadPath + " created.\n");
            }
            // Constructs path of the directory to save attached file(s) 
            String attachPath =  uploadPath + File.separator + ALL_DIR;
            // Creates the save directory (/documents/upload/status_attachs) if it does not exists
            File attachDir = new File(attachPath);
            if (!attachDir.exists()) {
                attachDir.mkdir();
                log.info(FOR_NAME + attachPath + " created.\n");
            }
            // Constructs path of the directory to save specific uploaded file 
            String deptPath =  attachPath + File.separator + DEP_DIR;
            // Creates the save directory if it does not exists
            File deptDir = new File(deptPath);
            if (!deptDir.exists()) {
                deptDir.mkdir();
                log.info(FOR_NAME + deptDir + " created.\n");
            }
            /* **************************************************************** *
             *              Finally it may manage streams as files!             *
             * **************************************************************** */
            // Retrieve and manage the document title
            String title = parser.getStringParameter("doc-name", Utils.VOID_STRING);
            // Recupera e scrive tutti gli allegati
            for (Part part : req.getParts()) {
                String fileName = extractFileName(part);
                if (fileName.equals(Utils.VOID_STRING)) {
                    continue;
                }
                // Creates the file object
                File file = new File(fileName);
                // Refines the fileName in case it is an absolute path
                fileName = null;
                fileName = file.getName();
                /* ************************************************************ *
                 *                     Resolves MIME type(s)                    *
                 * ************************************************************ */
                // Get the default library (JRE_HOME/lib/content-types.properties)               
                FileNameMap fileNameMap = URLConnection.getFileNameMap();
                // Tries to get the MIME type from default library
                String mimeType = fileNameMap.getContentTypeFor(file.getName());
                // If not, tries to get the MIME type guessing that from a 
                // service-provider loading facility - see {@link Files.probeContentType}
                if (mimeType == null) {
                    Path path = new File(fileName).toPath();
                    mimeType = Files.probeContentType(path);
                }
                /* ************************************************************ *
                 *                Retrieve and manage file extension            *
                 * ************************************************************ */
                String extension = fileName.substring(fileName.lastIndexOf("."));
                if (forbiddenExt.contains(extension)) {
                    String msg = FOR_NAME + "Si e\' verificato un problema di estensione non ammessa; l\'utente ha tentato di caricare un file non consentito.\n";
                    log.severe(msg);
                    throw new ServletException("Attenzione: il formato del file caricato non e\' consentito.\n");
                }
                /* ************************************************************ *
                 *                          Given name                          *
                 * ************************************************************ */
                String givenName = this.makeName(entityName, attributeName, db);
                log.info(FOR_NAME + "Nome autogenerato per il file: " + givenName);
                /* ************************************************************ *
                 *      Prepares the table hash with parameters to insert       *
                 * ************************************************************ */
                log.info(FOR_NAME + "Salvataggio previsto in tabella relativa all\'entita\': " + givenName);
                // Entity name
                params.put("nomeEntita", entityName);
                // Attribute name
                params.put("nomeAttributo", attributeName);
                // Real name of the uploaded file in the file system
                params.put("file", givenName);
                // Extension of the original file
                params.put("ext", extension);
                // Name of the original file chosen by user (named user file)
                params.put("nome", fileName);
                // Which one entity instance
                params.put("belongs", idMon);
                // The title chosen by user for the attachment
                params.put("titolo", checkPrime(title));              
                // Calculated size for now is zero
                params.put("dimensione", getFileSizeBytes(file));
                // Calculated mime
                params.put("mime", mimeType);
                // Logged user
                params.put("usr", user);
                // Write to the database
                db.setFileDoc(params);
                // Write to the filesystem
                part.write(deptPath + File.separator + givenName + extension);
                log.info(FOR_NAME + "Salvataggio in file system effettuato.\n");
                // Read the file just written
                File fileVolume = new File(deptPath + File.separator + givenName + extension);
                // Calculated size from file system
                params.put("dimensione", getFileSizeBytes(fileVolume));
                // Post Update FileDoc
                db.postUpdateFileDoc(params);
            }
            req.setAttribute("message", "Upload has been done successfully!");
            flush(req, res, templateJsp, "?q=mon&y=" + year + "&dip=" + idDip);
        } catch (IllegalStateException e) {
            throw new ServletException("Impossibile redirigere l'output. Verificare se la risposta e\' stata gia\' committata.\n" + e.getMessage(), e);
        } catch (NullPointerException e) {
            throw new ServletException("Errore nell'estrazione dei dipartimenti che gestiscono il corso.\n" + e.getMessage(), e);
        } catch (Exception e) {
            req.setAttribute("javax.servlet.jsp.jspException", e);
            req.setAttribute("message", e.getMessage());
            // Log dell'evento
            log.severe("Problema: " + e);
            e.printStackTrace();
            // Flusso
            flush(req, res, errorJsp, Utils.VOID_STRING);
            return;
        }
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
                       String fileJspT,
                       String q) 
                throws ServletException, 
                       IOException,
                       IllegalStateException {
        if (q == null || q.isEmpty()) {
            final RequestDispatcher rd = getServletContext().getRequestDispatcher(fileJspT + "?" + req.getQueryString());
            rd.forward(req, res);
            return;
        }
        res.sendRedirect(getServletContext().getInitParameter("appName") + q);
    }
    

    /**
     * <p>Extracts file name from HTTP header content-disposition.</p>
     * 
     * @param part the {@link javax.mail.Part Part} of the {@link javax.servlet.httpHttpServletRequest Request}
     * @return <code>String</code> - the name of the file which might be in Request
     */
    private static String extractFileName(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        String[] items = contentDisp.split(";");
        for (String s : items) {
            if (s.trim().startsWith("filename")) {
                return s.substring(s.indexOf("=") + 2, s.length()-1);
            }
        }
        return Utils.VOID_STRING;
    }
    
    
    /**
     * 
     * 
     * @param tableName
     * @param attributeName
     * @param db
     * @return
     * @throws ServletException
     * @throws IOException
     * @throws IllegalStateException
     */
    private String makeName(String tableName, 
                            String attributeName, 
                            DBWrapper db) 
                     throws ServletException, 
                            IOException,
                            IllegalStateException {
        GregorianCalendar calendar = Utils.getCurrentDate();
        // Gets current year in two-digits format
        int shortYear = calendar.get(Calendar.YEAR) - 2000;
        assert(shortYear > Query.NOTHING);
        // Gets current month in started-from-one format
        int month = calendar.get(Calendar.MONTH) + 1;
        StringBuffer monthAsStringBuffer = new StringBuffer(String.valueOf(month));
        if (month < 10) {
            monthAsStringBuffer.insert(0, '0');
        }
        // Gets current day in two-digits format
        int day = calendar.get(Calendar.DATE);
        StringBuffer dayAsStringBuffer = new StringBuffer(String.valueOf(day));
        if (day < 10) {
            dayAsStringBuffer.insert(0, '0');
        }
        // Gets current hour in two-digits format
        String hour = Utils.getCurrentHour();
        // Gets current minutes in two-digits format
        String minutes = Utils.getCurrentMinutes();
        // Values about the desired range
        int max = 99, min = 01;
        // Makes a two-digits pseudo-random numeric suffix
        int progressive = (int) Math.round(Math.random() * ((max - min) + 1) + min);
        // Makes the given name
        StringBuffer givenName = new StringBuffer(String.valueOf(shortYear).concat(String.valueOf(monthAsStringBuffer).concat(String.valueOf(dayAsStringBuffer).concat(hour).concat(minutes).concat(String.valueOf(progressive)))));
        // Check if the calculated name already exists
        try {
            if (db.existsFileName(tableName, attributeName, String.valueOf(givenName))) {
                // If the name already exists, it changes that 
                progressive = (int) Math.round(Math.random() * ((max - min) + 1) + min);
                givenName.append(progressive);
            }
        } catch (WebStorageException wse) {
            String msg = FOR_NAME + "Si e\' verificato un problema nel codice che effettua il recupero di valori dal db, a livello di SQL o nel calcolo di valori.\n";
            log.severe(msg);
            throw new ServletException(msg + wse.getMessage(), wse);
        } catch (IllegalStateException ise) {
            String msg = FOR_NAME + "Impossibile redirigere l'output. Verificare se la risposta e\' stata gia\' committata.\n";
            log.severe(msg);
            throw new IllegalStateException(msg + ise.getMessage(), ise);
        } catch (NullPointerException npe) {
            String msg = FOR_NAME + "Si e\' verificato un problema di puntamento a null.\n";
            log.severe(msg);
            throw new ServletException(msg + npe.getMessage(), npe);
        } catch (Exception e) {
            String msg = FOR_NAME + "Si e\' verificato un problema.\n";
            log.severe(msg);
            throw new ServletException(msg + e.getMessage(), e);
        }
        return givenName.toString();
    }
    
    
    private static String checkPrime(String toCheck) {
        String pattern = toCheck.replaceAll("(?<!')'(?!')", "''");
        return pattern;
    }
    
    
    /**
     * <p>Restituisce il peso in bytes di un file 
     * che accetta come argomento.</p>
     * 
     * @param file File di cui bisogna restituire il peso in B
     * @return <code>long</code> - il peso del file in B
     */
    private static long getFileSizeBytes(File file) {
        return file.length();
    }    
    
    
    /**
     * <p>Restituisce il peso in KiloBytes di un file 
     * che accetta come argomento.</p>
     * 
     * @param file File di cui bisogna restituire il peso in KB
     * @return <code>double</code> - il peso del file in Kappa
     */
    private static double getFileSizeKiloBytes(File file) {
        return (double) file.length() / 1024;
    }    
    
    
    /**
     * <p>Restituisce il peso in MegaBytes di un file 
     * che accetta come argomento.</p>
     * 
     * @param file File di cui bisogna restituire il peso in MB
     * @return <code>double</code> - il peso del file in Mega
     */
    private static double getFileSizeMegaBytes(File file) {
        return (double) file.length() / (1024 * 1024);
    }
    
}