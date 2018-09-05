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

package it.alma.command;

import java.text.SimpleDateFormat;
import java.util.Vector;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import it.alma.DBWrapper;
import it.alma.bean.CourseBean;
import it.alma.bean.ItemBean;
import it.alma.exception.CommandException;
import it.alma.exception.NotFoundException;
import it.alma.exception.WebStorageException;


/** 
 * <p>ValuationCommand.java</p>
 * <p>
 * Classe per l'implementazione delle regole di scelta delle AD
 * da mettere in valutazione ai fini dei questionari della didattica.<br />
 * 
 * Consente di applicare su una lista di Attivit&agrave; Didattiche 
 * le regole stabilite dal presidio della qualit&agrave;.<br />
 * </p>
 * <p>Created on martedì 4 settembre 2018 10:28:08</p>
 * 
 * @version 2
 * @author <a href="mailto:giovanroberto.torre@univr.it">Giovanroberto Torre</a>
 */
public class ValuationCommand extends ItemBean implements Command {
    
    /**
     *  Nome di questa classe 
     *  (utilizzato per contestualizzare i messaggi di errore)
     */
    static final String FOR_NAME = "\n" + Logger.getLogger(new Throwable().getStackTrace()[0].getClassName()) + ": ";
    /**
     * Pagina a cui la servlet reindirizza
     */
    private static final String nomeFileElenco = "/jsp/elenco.jsp";  
  
    
    /** 
     * Crea una nuova istanza di ValuationCommand 
     */
    public ValuationCommand() {
        ;   // Vuota
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
        if (this.getPaginaJsp()==null) {
          String error = "La voce menu' " + this.getNome() + " non ha il campo paginaJsp. Impossibile visualizzare i risultati.";
          throw new CommandException(error);
        }
    }  
  
    
    /**
     * <p>Gestisce il flusso principale.</p>
     * <p>Prepara i bean.</p>
     */
    public void execute(HttpServletRequest req) throws CommandException {  
        DBWrapper db = null;               // Databound        
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Vector<CourseBean> v = new Vector<CourseBean>();
        Vector<CourseBean> list = new Vector<CourseBean>();
        // Instanzia nuova classe WebStorage per il recupero dei dati
        try {
            db = new DBWrapper();
        } catch (WebStorageException wse) {
            throw new CommandException(FOR_NAME + "Non e\' disponibile un collegamento al database\n." + wse.getMessage());
        }
        // Recupero Attività Didattiche Semplici
        try {
            v = db.getADSemplici();
        } catch (NotFoundException nfe) {
            throw new CommandException(FOR_NAME + ": Impossibile recuperare un attributo obbligatorio.\n" + nfe.getMessage());
        } catch (WebStorageException wse) {
            StackTraceElement[] stackTrace = wse.getStackTrace();
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i<stackTrace.length; i++) {
                StackTraceElement ste = stackTrace[i];
                sb.append(ste.getClassName());
                sb.append(".");
                sb.append(ste.getMethodName());
                sb.append(" (");
                sb.append(ste.getClassName());
                sb.append(":");
                sb.append(ste.getLineNumber());
                sb.append(")");
                sb.append("\n\t");
            }
            throw new CommandException(FOR_NAME + "Si e\' verificato un problema nel recuperare i dati relativi all'elenco delle AD.\n" + wse.getMessage() + "\n stack: " + sb);
        } catch (NullPointerException npe) {
            StackTraceElement[] stackTrace = npe.getStackTrace();
            throw new CommandException(FOR_NAME + "Si e\' verificato un puntamento a null in " + this.getClass().getName() + ": " + npe.getMessage() + "\n stack: " + stackTrace[0]);            
        } catch (Exception e) {
            StackTraceElement[] stackTrace = e.getStackTrace();
            throw new CommandException(FOR_NAME + "Eccezione generica da " + this.getClass().getName() + ": " + e.getMessage() + "\n stack: " + stackTrace[0]);
        }
        // Individua le Attività Didattiche Semplici da porre in valutazione
        try {
            for (int i = 0; i < v.size(); i++) {
                CourseBean ad = v.get(i);
                // Controlla che ci siano ore di lezione
//                if (ad.getOreLezione() > 0) {
                    
//                }
            }
//        } catch (NotFoundException nfe) {
//            throw new CommandException(FOR_NAME + ": Impossibile recuperare un attributo obbligatorio.\n" + nfe.getMessage());
        } catch (NullPointerException npe) {
            StackTraceElement[] stackTrace = npe.getStackTrace();
            throw new CommandException(FOR_NAME + "Si e\' verificato un puntamento a null in " + this.getClass().getName() + ": " + npe.getMessage() + "\n stack: " + stackTrace[0]);            
        } catch (Exception e) {
            StackTraceElement[] stackTrace = e.getStackTrace();
            throw new CommandException(FOR_NAME + "Eccezione generica da " + this.getClass().getName() + ": " + e.getMessage() + "\n stack: " + stackTrace[0]);
        }
        // Imposta il testo del Titolo da visualizzare prima dell'elenco
        req.setAttribute("titoloE", "AD Semplici");
        // Salva nella request: Titolo (da mostrare nell'HTML)
        req.setAttribute("tP", req.getAttribute("titoloE"));         
        // Imposta la Pagina JSP di forwarding
        req.setAttribute("fileJsp", nomeFileElenco);
         // Salva nella request: elenco AD Semplici
        req.setAttribute("elenco", v);
    }
    
    
    
    
}