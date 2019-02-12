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

import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.oreilly.servlet.ParameterParser;

import it.alma.DBWrapper;
import it.alma.Main;
import it.alma.Query;
import it.alma.Utils;
import it.alma.bean.DepartmentBean;
import it.alma.bean.ItemBean;
import it.alma.bean.MonitorBean;
import it.alma.bean.PersonBean;
import it.alma.exception.CommandException;
import it.alma.exception.WebStorageException;


/** 
 * <p><code>MonitorCommand.java</code><br />
 * Implementa la logica per la gestione delle attivit&agrave; 
 * di monitoraggio MIUR di un dipartimento di eccellenza (POL).</p>
 * 
 * <p>Created on lunedì 11 febbraio 2019 12:48</p>
 * 
 * @author <a href="mailto:giovanroberto.torre@univr.it">Giovanroberto Torre</a>
 */
public class MonitorCommand extends ItemBean implements Command {
    
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
     * Crea una nuova istanza di ActivityCommand 
     */
    public MonitorCommand() {
        /*;*/   // It doesn't anything
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
        /* ******************************************************************** *
         *              Dichiara e inizializza variabili locali                 *
         * ******************************************************************** */
        // Databound
        DBWrapper db = null;
        // Parser per la gestione assistita dei parametri di input
        ParameterParser parser = new ParameterParser(req);
        // Dichiara la pagina a cui inoltrare
        String fileJspT = null;
        // Flag per i privilegi di scrittura a livello dipartimentale
        boolean writePriv = false;
        // Utente loggato
        PersonBean user = null;
        // Dichiara un monitor
        MonitorBean monitor = null;
        // Data di oggi sotto forma di oggetto String
        java.util.Date today = null;
        /* ******************************************************************** *
         *                    Recupera parametri e attributi                    *
         * ******************************************************************** */
        // Recupera o inizializza 'id progetto'
        int idDip = parser.getIntParameter("dip", Utils.DEFAULT_ID);
        // Recupera o inizializza 'id attività' (da modificare)
        int yMon = parser.getIntParameter("y", Utils.DEFAULT_ID);
        // Flag di scrittura
        boolean write = (boolean) req.getAttribute("w");
        /* ******************************************************************** *
         *      Instanzia nuova classe DBWrapper per il recupero dei dati       *
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
                throw new CommandException("Attenzione: controllare di essere autenticati nell\'applicazione!\n");
            }
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
        try {
            // Controllo sull'input
            if (idDip > Query.NOTHING) {
                // Verifica se è presente l'anno del monitoraggio
                if (yMon <= Query.NOTHING) {
                    // Se non è presente, gliene verrà assegnato uno d'ufficio
                    yMon = Utils.getCurrentYearAsInt();
                }
                // Recupera la sessione creata e valorizzata per riferimento nella req dal metodo authenticate
                HttpSession ses = req.getSession(Query.IF_EXISTS_DONOT_CREATE_NEW);
                // Recupera i dipartimenti in cui l'utente ha un ruolo superiore a TL
                Vector<DepartmentBean> writableDepts = (Vector<DepartmentBean>) ses.getAttribute("writableDeparments");
                // Confronta l'id del dipartimento corrente con gli id dei dipartimenti su cui si hanno privilegi
                writePriv = db.userCanMonitor(idDip, writableDepts);
                // Verifica se deve eseguire un'operazione di scrittura
                if (write && writePriv) {
                    // Creazione della tabella che conterrà i valori dei parametri passati dalle form
                    ConcurrentHashMap<String, String> params = new ConcurrentHashMap<String, String>();
                    loadParams(String.valueOf(yMon), parser, params);
                    /* **************************************************** *
                     *                  UPDATE Monitor Part                 *
                     * **************************************************** */
                    // Imposta il monitoraggio MIUR
                    db.updateMonitorPart(yMon, idDip, user, writableDepts, params);
                }
                /* **************************************************** *
                 *                  SELECT Monitor Part                 *
                 * **************************************************** */
                 // Recupera il Monitoraggio MIUR
                monitor = db.getMonitor(idDip, yMon);
                fileJspT = this.getPaginaJsp();
            } else {
                    // Se siamo qui vuol dire che l'id del dipartimento non è > zero, il che è un guaio
                    HttpSession ses = req.getSession(Query.IF_EXISTS_DONOT_CREATE_NEW);
                    ses.invalidate();
                    String msg = FOR_NAME + "Qualcuno ha tentato di inserire un indirizzo nel browser avente un id progetto non valido!.\n";
                    LOG.severe(msg);
                    throw new CommandException("Attenzione: indirizzo richiesto non valido!\n");
            }
        } catch (IllegalStateException ise) {
            String msg = FOR_NAME + "Impossibile redirigere l'output. Verificare se la risposta e\' stata gia\' committata.\n";
            LOG.severe(msg);
            throw new CommandException(msg + ise.getMessage(), ise);
        } catch (ClassCastException cce) {
            String msg = FOR_NAME + "Si e\' verificato un problema in una conversione di tipo.\n";
            LOG.severe(msg);
            throw new CommandException(msg + cce.getMessage(), cce);
        } catch (NullPointerException npe) {
            String msg = FOR_NAME + "Si e\' verificato un problema di puntamento a null.\n Attenzione: controllare di essere autenticati nell\'applicazione!\n";
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
        // Imposta nella request monitoraggio MIUR
        req.setAttribute("monitor", monitor);
        // Imposta nella request flag di autorizzazione al monitoraggio
        req.setAttribute("privileges", writePriv);
        // Imposta nella request data di oggi 
        req.setAttribute("now", today);
        // Imposta la Pagina JSP di forwarding
        req.setAttribute("fileJsp", fileJspT);
    }
    
    
    /**
     * <p>Valorizza per riferimento una mappa contenente i valori relativi  
     * ad un monitoraggio eventualmente da aggiornare.</p> 
     * 
     * @param part l'anno del monitoraggio
     * @param parser oggetto per la gestione assistita dei parametri di input, gia' pronto all'uso
     * @param mon mappa da valorizzare per riferimento (ByRef)
     * @throws CommandException se si verifica un problema nella gestione degli oggetti data o in qualche tipo di puntamento
     */
    private static void loadParams(String part, 
                                   ParameterParser parser,
                                   ConcurrentHashMap<String, String> mon)
                            throws CommandException {
        /* **************************************************** *
         *          Ramo di Monitoraggio di dato anno           *
         * **************************************************** */
        // Recupero e caricamento parametri di monitoraggio
        mon.put("mon-d4", parser.getStringParameter("mon-d4", Utils.VOID_STRING));
        mon.put("mon-d5", parser.getStringParameter("mon-d5", Utils.VOID_STRING));
        mon.put("mon-d6", parser.getStringParameter("mon-d6", Utils.VOID_STRING));
        mon.put("mon-d7", parser.getStringParameter("mon-d7", Utils.VOID_STRING));
        mon.put("mon-d8", parser.getStringParameter("mon-d8", Utils.VOID_STRING));
    }
    
}