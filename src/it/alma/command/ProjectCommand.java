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

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Vector;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.swing.JOptionPane;

import com.oreilly.servlet.ParameterParser;

import it.alma.DBWrapper;
import it.alma.Main;
import it.alma.Query;
import it.alma.Utils;
import it.alma.bean.ItemBean;
import it.alma.bean.PersonBean;
import it.alma.bean.ProjectBean;
import it.alma.exception.AttributoNonValorizzatoException;
import it.alma.exception.CommandException;
import it.alma.exception.WebStorageException;


/** 
 * <p><code>ProjectCommand.java</code><br />
 * Implementa la logica per la gestione dei progetti on line (POL).</p>
 * 
 * <p>Created on mercoledì 11 ottobre 2018 15:13</p>
 * 
 * @author <a href="mailto:giovanroberto.torre@univr.it">Giovanroberto Torre</a>
 */
public class ProjectCommand extends ItemBean implements Command {
    
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
     * Pagina a cui la command reindirizza per mostrare 
     */
    private static final String nomeFileElenco = "/jsp/elencoProgetti.jsp";
    /**
     * Pagina a cui la command fa riferimento per mostrare la Vision del progetto
     */
    //private static final String nomeFileVision = "/jsp/pcVision.jsp";
    /**
     * Pagina a cui la command fa riferimento per mostrare gli Stakeholder del progetto
     */
    //private static final String nomeFileStakeholder = "/jsp/pcStakeholder.jsp";
    
    private static final HashMap<String, String> nomeFile = new HashMap<String, String>();
    
    
    /** 
     * Crea una nuova istanza di ValuationCommand 
     */
    public ProjectCommand() {
        ;   // It Doesn't Anything
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
        // Carica la hashmap contenente le pagine da includere in funzione dei parametri sulla querystring
        nomeFile.put(Query.PART_PROJECT_CHARTER_VISION, "/jsp/pcVision.jsp");
        nomeFile.put(Query.PART_PROJECT_CHARTER_STAKEHOLDER, "/jsp/pcStakeholder.jsp");
        nomeFile.put(Query.PART_PROJECT_CHARTER_DELIVERABLE, "/jsp/pcDeliverable.jsp");
        nomeFile.put(Query.PART_PROJECT_CHARTER_RESOURCE, "");
        nomeFile.put(Query.PART_PROJECT_CHARTER_RISK, "");
        nomeFile.put(Query.PART_PROJECT_CHARTER_CONSTRAINT, "");
        nomeFile.put(Query.PART_PROJECT_CHARTER_MILESTONE, "");
        nomeFile.put(Query.PART_PROJECT, this.getPaginaJsp());
    }  
  
    
    /**
     * <p>Gestisce il flusso principale.</p>
     * <p>Prepara i bean.</p>
     * <p>Passa nella Request i valori che verranno utilizzati dall'applicazione.</p>
     */
    public void execute(HttpServletRequest req) 
                 throws CommandException {
        /* ******************************************************************** *
         *              Crea e inizializza le variabili locali                  *
         * ******************************************************************** */
        
        JOptionPane.showMessageDialog(null, "Chiamata execute arrivata dall\'applicazione!", "Main: esito OK", JOptionPane.INFORMATION_MESSAGE, null);
        // Databound
        DBWrapper db = null;
        // Parser per la gestione assistita dei parametri di input
        ParameterParser parser = new ParameterParser(req);
        // Utente loggato
        PersonBean user = null;
        // Progetto di dato id
        ProjectBean project = null;
        // Recupera o inizializza 'id progetto'
        int idPrj = parser.getIntParameter("id", -1); 
        // Recupera o inizializza 'tipo pagina'   
        String part = parser.getStringParameter("p", "-");
        // Flag di scrittura
        //boolean write = parser.getBooleanParameter("w");
        boolean write = (boolean) req.getAttribute("w");
        // Creazione della tabella valori parametri
        HashMap<String, HashMap<String, String>> params = new HashMap<String, HashMap<String, String>>();
        // Recupero e caricamento parametri di project charter/vision
        HashMap<String, String> pcv = new HashMap<String, String>();
        pcv.put("pcv-situazione", parser.getStringParameter("pcv-situazione", Utils.VOID_STRING));
        pcv.put("pcv-descrizione", parser.getStringParameter("pcv-descrizione", Utils.VOID_STRING));
        pcv.put("pcv-obiettivi", parser.getStringParameter("pcv-obiettivi", Utils.VOID_STRING));
        pcv.put("pcv-minacce", parser.getStringParameter("pcv-minacce", Utils.VOID_STRING));
        // Caricamento della tabella valori parametri
        params.put(Query.PART_PROJECT_CHARTER_VISION, pcv);
        // Recupero e caricamento parametri di project charter/stakeholder
        HashMap<String, String> pcs = new HashMap<String, String>();
        pcs.put("pcs-chiave", parser.getStringParameter("pcs-chiave", Utils.VOID_STRING));
        pcs.put("pcs-istituzionale", parser.getStringParameter("pcs-istituzionale", Utils.VOID_STRING));
        pcs.put("pcs-marginale", parser.getStringParameter("pcs-marginale", Utils.VOID_STRING));
        pcs.put("pcs-operativo", parser.getStringParameter("pcs-operativo", Utils.VOID_STRING));
        params.put(Query.PART_PROJECT_CHARTER_STAKEHOLDER, pcs);
        // Recupero e caricamento parametri di project charter/deliverable
        HashMap<String, String> pcd = new HashMap<String, String>();
        pcd.put("pcd-descrizione", parser.getStringParameter("pcd-descrizione", Utils.VOID_STRING));
        params.put(Query.PART_PROJECT_CHARTER_DELIVERABLE, pcd);
        // Recupero e caricamento parametri di project charter/risorse
        // TODO ...
        // Recupero e caricamento parametri di project charter/rischi
        // TODO ...
        // Recupero e caricamento parametri di project charter/vincoli
        // TODO ...
        // Recupero e caricamento parametri di project charter/milestone
        // TODO ...
        // Recupero e caricamento parametri di project status
        GregorianCalendar data = new GregorianCalendar(1970, 1, 1);
        String dataAsString = Utils.format(data, "yyyy-mm-dd");
        HashMap<String, String> statusProject = new HashMap<String, String>();
        statusProject.put("sMese", parser.getStringParameter("sMese", dataAsString));
        statusProject.put("sAttuale", parser.getStringParameter("sAttuale", Utils.VOID_STRING));
        statusProject.put("sCosti", parser.getStringParameter("sCosti", Utils.VOID_STRING));
        statusProject.put("sTempi", parser.getStringParameter("sTempi", Utils.VOID_STRING));
        statusProject.put("sRischi", parser.getStringParameter("sRischi", Utils.VOID_STRING));
        statusProject.put("sRisorse", parser.getStringParameter("sRisorse", Utils.VOID_STRING));
        statusProject.put("sScope", parser.getStringParameter("sScope", Utils.VOID_STRING));
        statusProject.put("sComunicazione", parser.getStringParameter("sComunicazione", Utils.VOID_STRING));
        statusProject.put("sQualita", parser.getStringParameter("sQualita", Utils.VOID_STRING));
        statusProject.put("sApprovvigionamenti", parser.getStringParameter("sApprovvigionamenti", Utils.VOID_STRING));
        statusProject.put("sStakeholder", parser.getStringParameter("sStakeholder", Utils.VOID_STRING));
        params.put(Query.PART_PROJECT, statusProject);
        // Valorizzazione tabella valori
        //params.put(key, value);

        // Dichiara la pagina a cui reindirizzare
        String fileJspT = null;
        // Dichiara elenco di progetti
        Vector<ProjectBean> v = new Vector<ProjectBean>();
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
            user = (PersonBean) ses.getAttribute("usr");
            if (user == null) {
                throw new CommandException("Attenzione: controllare di essere autenticati nell\'applicazione!\n");
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
         *                          Corpo del programma                         *
         * ******************************************************************** */
        // Decide il valore della pagina
        try {
            if (nomeFile.containsKey(part)) {
                if (idPrj > 0) {
                    if (write) {
                        //JOptionPane.showMessageDialog(null, "Chiamata arrivata dall\'applicazione!", FOR_NAME + ": esito OK", JOptionPane.INFORMATION_MESSAGE, null);
                        db.updateProjectPart(idPrj, params);
                    }
                    project = db.getProject(idPrj, user.getId());
                }
                fileJspT = nomeFile.get(part);
            } else {
                v = db.getProjects(user.getId());
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
         *              Settaggi in request dei valori calcolati                *
         * ******************************************************************** */
        // Imposta il testo del Titolo da visualizzare prima dell'elenco
        req.setAttribute("titoloE", "Project Charter");
        // Salva nella request: Titolo pagina (da mostrare nell'HTML)
        req.setAttribute("tP", req.getAttribute("titoloE"));         
        // Imposta la Pagina JSP di forwarding
        req.setAttribute("fileJsp", fileJspT);
        // Salva nella request elenco progetti
        req.setAttribute("progetti", v);
        // Salva nella request dettaglio progetto
        req.setAttribute("progetto", project);
    }
    
}