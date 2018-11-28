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

package it.alma.command;

import java.util.Enumeration;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.oreilly.servlet.ParameterParser;

import it.alma.DBWrapper;
import it.alma.Main;
import it.alma.Query;
import it.alma.bean.ItemBean;
import it.alma.bean.PersonBean;
import it.alma.exception.CommandException;
import it.alma.exception.WebStorageException;


/** 
 * <p><code>ActivityCommand.java</code><br />
 * Implementa la logica per la gestione delle attività di un progetto on line (POL).</p>
 * 
 * <p>Created on martedì 27 novembre 2018 12:56</p>
 * 
 * @author <a href="mailto:giovanroberto.torre@univr.it">Giovanroberto Torre</a>
 */
public class ActivityCommand extends ItemBean implements Command {
    
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
     * Pagina a cui la command reindirizza per mostrare la lista delle attivit&agrave; del progetto
     */
    private static final String nomeFileElenco = "/jsp/projActivities.jsp";
    /**
     * Pagina a cui la command fa riferimento per mostrare la lista delle 
     * attivit&agrave; del progetto nel contesto del Project Charter
     */
    private static final String nomeFileMilestone = "/jsp/pcMilestone.jsp";
    /**
     * Pagina a cui la command fa riferimento per permettere l'aggiunta 
     * di una nuova attivit&agrave; al progetto
     */
    private static final String nomeFileActivity = "/jsp/pcAttivita.jsp";
    
    
    /** 
     * Crea una nuova istanza di ActivityCommand 
     */
    public ActivityCommand() {
        /*;*/   // It doesn't Anything
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
    }  
  
    
    /**
     * <p>Gestisce il flusso principale.</p>
     * <p>Prepara i bean.</p>
     * <p>Passa nella Request i valori che verranno utilizzati dall'applicazione.</p>
     * 
     * @param req la HttpServletRequest contenente la richiesta del client
     * @throws CommandException se si verifica un problema, tipicamente nell'accesso a campi non accessibili o in qualche altro tipo di puntamento 
     */
    public void execute(HttpServletRequest req) 
                 throws CommandException {
        // Databound
        DBWrapper db = null;
        // Parser per la gestione assistita dei parametri di input
        ParameterParser parser = new ParameterParser(req);
        // Utente loggato
        PersonBean user = null;
        /* ******************************************************************** *
         *      Instanzia nuova classe WebStorage per il recupero dei dati      *
         * ******************************************************************** */
        try {
            db = new DBWrapper();
        } catch (WebStorageException wse) {
            throw new CommandException(FOR_NAME + "Non e\' disponibile un collegamento al database\n." + wse.getMessage(), wse);
        }
        /* ******************************************************************** *
         *                         Recupera la Sessione                         *
         * ******************************************************************** */
        try {
            // Recupera la sessione creata e valorizzata per riferimento nella req dal metodo authenticate
            HttpSession ses = req.getSession(Query.IF_EXISTS_DONOT_CREATE_NEW);
            if (ses == null) {
                return;
            } 
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
        /* ******************************************************************** *
         *                          Recupera i parametri                        *
         * ******************************************************************** */
        // Recupera o inizializza 'id progetto'
        int idPrj = parser.getIntParameter("id", -1); 
        // Recupera o inizializza 'tipo corsi di studio'
        String tcs = parser.getStringParameter("tcs", "-");
        // Recupera o inizializza 'tipo pagina'   
        String part = parser.getStringParameter("p", "-");
        // Dichiara la pagina a cui reindirizzare
        String fileJspT = null;
        // Decide il valore della pagina
        if (part.equalsIgnoreCase("pcv")) {
            fileJspT = nomeFileMilestone;
        } else if (part.equalsIgnoreCase("pcs")) {
            //fileJspT = nomeFileStakeholder;
        } else {
            fileJspT = nomeFileElenco;
        }
        // Recupera il tipo di documento
        //String tipo = parser.getStringParameter("tipo", "-");

        
        // Imposta il testo del Titolo da visualizzare prima dell'elenco
        req.setAttribute("titoloE", "Project Charter");
        // Salva nella request: Titolo pagina (da mostrare nell'HTML)
        req.setAttribute("tP", req.getAttribute("titoloE"));         
        // Imposta la Pagina JSP di forwarding
        req.setAttribute("fileJsp", nomeFileElenco);
        // Salva nella request: utente
        //req.setAttribute("user", user);
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
    
    // TODO COMMENTO
    public static String getParameters(HttpServletRequest req) {
        Enumeration<String> parameters = req.getParameterNames();
        StringBuffer parametersName = new StringBuffer("<pre>");
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
        return String.valueOf(parametersName);
    }
    
}