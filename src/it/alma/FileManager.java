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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;
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

import it.alma.bean.PersonBean;
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
     * Name of the directory where uploaded files will be saved, relative to
     * the web application directory.
     */
    private static final String SAVE_DIR = "upload";
    /**
     * Logger della classe per scrivere i messaggi di errore  
     */
    private Logger log = Logger.getLogger(FileManager.class.getName());
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
     * <p>Handles file upload.</p>
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
        //StringBuffer msg = new StringBuffer();              // Messaggio
        // Effettua la connessione al databound
        try {
            db = new DBWrapper();
        } catch (WebStorageException wse) {
            throw new ServletException(FOR_NAME + "Non riesco ad instanziare databound.\n" + wse.getMessage(), wse);
        }
        try {
            // Gets absolute path of the web application
            String appPath = req.getServletContext().getRealPath(Utils.VOID_STRING);
            // Creates the documents root directory if it does not exists
            String documentsRootName = appPath + getServletContext().getInitParameter("urlDirectoryDocumenti");
            File documentsRoot = new File(documentsRootName);
            if (!documentsRoot.exists()) {
                documentsRoot.mkdir();
            }
            // Constructs path of the directory to save uploaded file
            String savePath =  documentsRootName + File.separator + SAVE_DIR;
            // Creates the save directory if it does not exists
            File fileSaveDir = new File(savePath);
            if (!fileSaveDir.exists()) {
                fileSaveDir.mkdir();
            }
            // Recupera e gestisce titolo documento
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
                fileName = file.getName();
                // Resolves MIME type
                
                FileNameMap fileNameMap = URLConnection.getFileNameMap();
                String mimeType = fileNameMap.getContentTypeFor(file.getName());
                if (mimeType == null) {
                    Path path = new File(fileName).toPath();
                    //mimeType = Files.probeContentType(fileName);
                    mimeType = Files.probeContentType(path);
                }
                part.write(savePath + File.separator + fileName);
            }
            req.setAttribute("message", "Upload has been done successfully!");
            flush(req, res, templateJsp, "credits.jsp");
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
     * Extracts file name from HTTP header content-disposition
     */
    private String extractFileName(Part part) {
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
                       String fileJspR) 
                throws ServletException, 
                       IOException,
                       IllegalStateException {
        if (fileJspR.isEmpty()) {
            final RequestDispatcher rd = getServletContext().getRequestDispatcher(fileJspT + "?" + req.getQueryString());
            rd.forward(req, res);
            return;
        }
        res.sendRedirect(getServletContext().getInitParameter("appName") + "/jsp/" + fileJspR);
    }
    
}