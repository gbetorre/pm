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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.sql.DataSource;
import javax.swing.JOptionPane;

import it.alma.bean.BeanUtil;
import it.alma.bean.CourseBean;
import it.alma.bean.DepartmentBean;
import it.alma.bean.ItemBean;
import it.alma.bean.PersonBean;
import it.alma.bean.ProjectBean;
import it.alma.bean.StatoProgettoBean;
import it.alma.exception.AttributoNonValorizzatoException;
import it.alma.exception.NotFoundException;
import it.alma.exception.WebStorageException;


/**
 * <p><code>DBWrapper.java</code> &egrave; la classe che implementa 
 * l'accesso ai database utilizzati dall'applicazione nonch&eacute;
 * l'esecuzione delle query e la gestione dei risultati restituiti,
 * che impacchetta in oggetti di tipo JavaBean e restituisce al chiamante.</p>
 *  
 * @author <a href="mailto:giovanroberto.torre@univr.it">Giovanroberto Torre</a>
 */
public class DBWrapper implements Query {
    
    /**
     * <p>La serializzazione necessita di dichiarare una costante di tipo long
     * identificativa della versione seriale.<br /> 
     * (Se questo dato non fosse inserito, verrebbe calcolato in maniera a
     * utomatica dalla JVM, e questo potrebbe portare a errori 
     * riguardo alla serializzazione).</p>
     * <p><small>I moderni IDE, come Eclipse, permettono di assegnare 
     * questa costante in due modi diversi:
     *  <ul> 
     *  <li>o attraverso un valore di default assegnato a questa costante<br />
     *  (p.es. <code>private static final long serialVersionUID = 1L;</code>)
     *  </li>
     *  <li>o attraverso un valore calcolato tramite un algoritmo implementato
     *  internamente<br /> (p.es. 
     *  <code>private static final long serialVersionUID = -8762739881448133461L;</code>)
     *  </li></small></p>
     */
    private static final long serialVersionUID = -8762739881448133461L;
    /**
     * <p>Logger della classe per scrivere i messaggi di errore. 
     * All logging goes through this logger.</p>
     * <p>Non &egrave; privata ma Default (friendly) per essere visibile 
     * negli oggetti ovverride implementati da questa classe.</p>
     */
    protected static Logger LOG = Logger.getLogger(DBWrapper.class.getName());
    /**
     * <p>Nome di questa classe 
     * (viene utilizzato per contestualizzare i messaggi di errore).</p>
     * <p>Non &egrave; privata ma Default (friendly) per essere visibile 
     * negli oggetti ovverride implementati da questa classe.</p>
     */
    static final String FOR_NAME = "\n" + Logger.getLogger(new Throwable().getStackTrace()[0].getClassName()) + ": ";
    /**
     * <p>Gestore del pool di connessioni al dbms locale per almalaurea.</p>
     */
    protected static DataSource alma_manager = null;
    /**
     * <p>Connessione db oracle ESSE3.</p>
     */
    protected static DataSource pol_manager = null;

    
    /**
     * <p>Costruttore. Prepara le connessioni.</p>
     * <p>Viene usata l'interfaccia 
     * <a href="https://docs.oracle.com/javase/7/docs/api/javax/sql/DataSource.html">DataSource</a> 
     * per ottenere le connessioni.</p>
     * <p>Usa il pattern Singleton per evitare di aprire connessioni inutili
     * in caso di connessioni gi&agrave; aperte.</p>
     * 
     * @throws WebStorageException in caso di mancata connessione al database per errore password o dbms down
     * @see DataSource
     */
    public DBWrapper() throws WebStorageException {
        if (alma_manager == null) {
            try {
                alma_manager = (DataSource) ((Context) new InitialContext()).lookup("java:comp/env/jdbc/almalaurea");
                if (alma_manager == null)
                    throw new WebStorageException(FOR_NAME + "La risorsa `jdbc/almalaurea' non e\' disponibile. Verificare configurazione e collegamenti.\n");
                JOptionPane.showMessageDialog(null, "Accesso al DB per almalaurea", "Inizializzazione Completata", 0, null);
            } catch (NamingException ne) {
                throw new WebStorageException(FOR_NAME + "Problema nel recuperare la risorsa jdbc/almalaurea per problemi di naming: " + ne.getMessage());
            } catch (Exception e) {
                throw new WebStorageException(FOR_NAME + "Errore generico nel costruttore: " + e.getMessage(), e);
            }
        }
        if (pol_manager == null) {
            try {
                pol_manager = (DataSource) ((Context) new InitialContext()).lookup("java:comp/env/jdbc/pol");
                if (pol_manager == null)
                    throw new WebStorageException(FOR_NAME + "La risorsa `jdbc/pol' non e\' disponibile. Verificare configurazione e collegamenti.\n");
                JOptionPane.showMessageDialog(null, "Accesso al DB di POL", "Inizializzazione Completata", 0, null);
            } catch (NamingException ne) {
                throw new WebStorageException(FOR_NAME + "Problema nel recuperare la risorsa jdbc/pol per problemi di naming: " + ne.getMessage());
            } catch (Exception e) {
                throw new WebStorageException(FOR_NAME + "Errore generico nel costruttore: " + e.getMessage(), e);
            }
        }
    }
    
    
    /**
     * <p>Restituisce un Vector di Command.</p>
     *
     * @return <code>Vector&lt;ItemBean&gt;</code> - lista di ItemBean rappresentanti ciascuno una Command dell'applicazione
     * @throws WebStorageException se si verifica un problema nell'esecuzione della query, nell'accesso al db o in qualche tipo di puntamento
     */
    @SuppressWarnings({ "null", "static-method" })
    public Vector<ItemBean> lookupCommand()
                                   throws WebStorageException {
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement pst = null;
        ItemBean cmd = null;
        Vector<ItemBean> commands = new Vector<ItemBean>();
        try {
            con = pol_manager.getConnection();
            pst = con.prepareStatement(LOOKUP_COMMAND);
            pst.clearParameters();
            rs = pst.executeQuery();
            while (rs.next()) {
                cmd = new ItemBean();
                BeanUtil.populate(cmd, rs);
                commands.add(cmd);
            }
            return commands;
        } catch (SQLException sqle) {
            throw new WebStorageException(FOR_NAME + sqle.getMessage());
        } finally {
            try {
                con.close();
            } catch (NullPointerException npe) {
                throw new WebStorageException(FOR_NAME + npe.getMessage());
            } catch (SQLException sqle) {
                throw new WebStorageException(FOR_NAME + sqle.getMessage());
            }
        }
    }
    
    
    /**
     * <p>Restituisce un PersonBean rappresentante un utente loggato.</p>
     *
     * @return <code>PersonBean</code> - PersonBean rappresentante l'utente loggato
     * @throws WebStorageException se si verifica un problema nell'esecuzione della query, nell'accesso al db o in qualche tipo di puntamento
     */
    @SuppressWarnings({ "null", "static-method" })
    public PersonBean getUser(String username,
                              String password)
                       throws WebStorageException {
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement pst = null;
        PersonBean usr = null;
        try {
            con = pol_manager.getConnection();
            pst = con.prepareStatement(GET_USR);
            pst.clearParameters();
            pst.setString(1, username);
            pst.setString(2, password);
            rs = pst.executeQuery();
            if (rs.next()) {
                usr = new PersonBean();
                BeanUtil.populate(usr, rs);
            }
            return usr;
        } catch (SQLException sqle) {
            String msg = FOR_NAME + "Oggetto PersonBean non valorizzato; problema nella query dell\'utente.\n";
            LOG.severe(msg); 
            throw new WebStorageException(msg + sqle.getMessage(), sqle);
        } finally {
            try {
                con.close();
            } catch (NullPointerException npe) {
                String msg = FOR_NAME + "Ooops... problema nella chiusura della connessione.\n";
                LOG.severe(msg); 
                throw new WebStorageException(msg + npe.getMessage());
            } catch (SQLException sqle) {
                throw new WebStorageException(FOR_NAME + sqle.getMessage());
            }
        }
    }    
    
    
    /**
     * <p>Restituisce un Vector di CourseBean di tipo "AD Semplice".</p>
     *
     * @return <code>Vector&lt;CourseBean&gt;</code> - lista di CourseBean rappresentanti ciascuno un'AD Semplice
     * @throws WebStorageException se si verifica un problema nell'esecuzione della query, nell'accesso al db o in qualche tipo di puntamento
     */
    @SuppressWarnings({ "null", "static-method" })
    public Vector<CourseBean> getADSemplici()
                                     throws WebStorageException {
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement pst = null;
        CourseBean course = null;
        Vector<CourseBean> lista = new Vector<CourseBean>();
        try {
            con = alma_manager.getConnection();
            pst = con.prepareStatement(AD_SEMPLICI);
            pst.clearParameters();
            rs = pst.executeQuery();
            while (rs.next()) {
                course = new CourseBean();
                BeanUtil.populate(course, rs);
                lista.add(course);
            }
            return lista;
        } catch (SQLException sqle) {
            throw new WebStorageException(sqle.getMessage());
        } finally {
            try {
                con.close();
            } catch (NullPointerException npe) {
                throw new WebStorageException(npe.getMessage());
            } catch (SQLException sqle) {
                throw new WebStorageException(sqle.getMessage());
            }
        }
    }
    
    
    /**
     * <p>Restituisce un Vector di ProjectBean rappresentante i progetti dell'utente loggato .</p>
     * 
     * @param userId identificativo della persona di cui si vogliono recuperare i progetti
     * @return <code>Vector&lt;ProjectBean&gt;</code> - ProjectBean rappresentante i progetti dell'utente loggato
     * @throws WebStorageException se si verifica un problema nell'esecuzione della query, nell'accesso al db o in qualche tipo di puntamento
     */
    @SuppressWarnings({ "null", "static-method" })
    public Vector<ProjectBean> getProjects(int userId)
    							    throws WebStorageException{
    	ResultSet rs, rs2 = null;
    	Connection con = null;
        PreparedStatement pst = null;
        ProjectBean project = null;
        DepartmentBean dipart = null;
        StatoProgettoBean statoProgetto = null;
        int idDipart = -1;
        int idStatoProgetto = -1;
        Vector<ProjectBean> projects = new Vector<ProjectBean>();
        try {
            con = pol_manager.getConnection();
            pst = con.prepareStatement(GET_PROJECTS);
            pst.clearParameters();
            pst.setInt(1, userId);
            rs = pst.executeQuery();
            while (rs.next()) {
                project = new ProjectBean();
                BeanUtil.populate(project, rs);
                // Recupera dipartimento del progetto
                idDipart = project.getIdDipart();
                pst = con.prepareStatement(GET_DIPART);
                pst.clearParameters();
                pst.setInt(1, idDipart);
                rs2 = pst.executeQuery();
                if (rs2.next()) {
                    dipart = new DepartmentBean();
                    BeanUtil.populate(dipart, rs2);
                    project.setDipart(dipart);
                }
                rs2 = null;
                //Recupera statoprogetto del progetto
                idStatoProgetto = project.getIdStatoProgetto();
                pst = con.prepareStatement(GET_STATOPROGETTO);
                pst.clearParameters();
                pst.setInt(1, idStatoProgetto);
                rs2 = pst.executeQuery();
                if(rs2.next()) {
                	statoProgetto = new StatoProgettoBean();
                	BeanUtil.populate(statoProgetto, rs2);
                	project.setStatoProgetto(statoProgetto);
                }
                // Aggiunge il progetto valorizzato all'elenco
                projects.add(project);
            }
            return projects;
        } catch (AttributoNonValorizzatoException anve) {
            String msg = FOR_NAME + "Oggetto ProjectBean.idDipart non valorizzato; problema nella query dei progetti.\n";
            LOG.severe(msg); 
            throw new WebStorageException(msg + anve.getMessage(), anve);
        } catch (SQLException sqle) {
            String msg = FOR_NAME + "Oggetto ProjectBean non valorizzato; problema nella query dei progetti.\n";
            LOG.severe(msg); 
            throw new WebStorageException(msg + sqle.getMessage(), sqle);
        } finally {
            try {
                con.close();
            } catch (NullPointerException npe) {
                String msg = FOR_NAME + "Ooops... problema nella chiusura della connessione.\n";
                LOG.severe(msg); 
                throw new WebStorageException(msg + npe.getMessage());
            } catch (SQLException sqle) {
                throw new WebStorageException(FOR_NAME + sqle.getMessage());
            }
        }
    }
    
    
    /**
     * <p>Restituisce un ProjectBean rappresentante un progetto dell'utente loggato.</p>
     * 
     * @param projectId identificativo del progetto che si vuol recuperare
     * @return <code>ProjectBean</code> - ProjectBean rappresentante il progetto selezionato
     * @throws WebStorageException se si verifica un problema nell'esecuzione della query, nell'accesso al db o in qualche tipo di puntamento
     */
    @SuppressWarnings({ "null", "static-method" })
    public ProjectBean getProject(int projectId,
                                  int userId) 
                           throws WebStorageException {
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement pst = null;
        ProjectBean project = null;
        try {
            con = pol_manager.getConnection();
            pst = con.prepareStatement(GET_PROJECT);
            pst.clearParameters();
            pst.setInt(1, projectId);
            pst.setInt(2, userId);
            rs = pst.executeQuery();
            if(rs.next()) {
                project = new ProjectBean();
                BeanUtil.populate(project, rs);
            }
            return project;            
        } catch (SQLException sqle) {
            String msg = FOR_NAME + "Oggetto ProjectBean non valorizzato; problema nella query che recupera il progetto.\n";
            LOG.severe(msg); 
            throw new WebStorageException(msg + sqle.getMessage(), sqle);
        } finally {
            try {
                con.close();
            } catch (NullPointerException npe) {
                String msg = FOR_NAME + "Ooops... problema nella chiusura della connessione.\n";
                LOG.severe(msg); 
                throw new WebStorageException(msg + npe.getMessage());
            } catch (SQLException sqle) {
                throw new WebStorageException(FOR_NAME + sqle.getMessage());
            }
        }
    }
    
    
    /**
     * <p>Metodo che controlla la query da eseguire e chiama il metodo opportuno.</p>
     * 
     * @throws WebStorageException se si verifica un problema nell'esecuzione della query, nell'accesso al db o in qualche tipo di puntamento 
     */
    @SuppressWarnings("static-method")
    public void updateProjectPart(int idProj, 
                                  HashMap<String, HashMap<String, String>>params) 
                           throws WebStorageException {
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement pst = null;
        HashMap<String, String> paramsVision = params.get(PART_PROJECT_CHARTER_VISION);
        HashMap<String, String> paramsStakeholder = params.get(PART_PROJECT_CHARTER_STAKEHOLDER);
        HashMap<String, String> paramsDeliverable = params.get(PART_PROJECT_CHARTER_DELIVERABLE);
        try {
            con = pol_manager.getConnection();
            if(Utils.voidValues(paramsVision)) {
                pst = con.prepareStatement(UPDATE_VISION);
                con.setAutoCommit(false);
                pst.clearParameters();
                pst.setString(1, paramsVision.get("pcv-situazione"));
                pst.setString(2, paramsVision.get("pcv-descrizione"));
                pst.setString(3, paramsVision.get("pcv-obiettivi"));
                pst.setString(4, paramsVision.get("pcv-minacce"));
                pst.setInt(5, idProj);
                JOptionPane.showMessageDialog(null, "Chiamata arrivata a updateProjectPart dall\'applicazione!", FOR_NAME + ": esito OK", JOptionPane.INFORMATION_MESSAGE, null);
                pst.executeUpdate();
                con.commit();
            }
            pst = null;
            if(Utils.voidValues(paramsStakeholder)) {
                pst = con.prepareStatement(UPDATE_STAKEHOLDER);
                con.setAutoCommit(false);
                pst.clearParameters();
                pst.setString(1, paramsStakeholder.get("pcs-marginale"));
                pst.setString(2, paramsStakeholder.get("pcs-operativo"));
                pst.setString(3, paramsStakeholder.get("pcs-istituzionale"));
                pst.setString(4, paramsStakeholder.get("pcs-chiave"));
                pst.setInt(5, idProj);
                JOptionPane.showMessageDialog(null, "Chiamata arrivata a updateProjectPart dall\'applicazione!", FOR_NAME + ": esito OK", JOptionPane.INFORMATION_MESSAGE, null);
                pst.executeUpdate();
                con.commit();
            }
            pst = null;
            if(Utils.voidValues(paramsDeliverable)) {
                pst = con.prepareStatement(UPDATE_DELIVERABLE);
                con.setAutoCommit(false);
                pst.clearParameters();
                pst.setString(1, paramsDeliverable.get("pcd-descrizione"));
                pst.setInt(2, idProj);
                JOptionPane.showMessageDialog(null, "Chiamata arrivata a updateProjectPart dall\'applicazione!", FOR_NAME + ": esito OK", JOptionPane.INFORMATION_MESSAGE, null);
                pst.executeUpdate();
                con.commit();
            }
        } catch (SQLException sqle) {
            String msg = FOR_NAME + "Tupla non aggiornata correttamente; problema nella query che aggiorna il progetto.\n";
            LOG.severe(msg); 
            throw new WebStorageException(msg + sqle.getMessage(), sqle);
        } finally {
            try {
                con.close();
            } catch (NullPointerException npe) {
                String msg = FOR_NAME + "Ooops... problema nella chiusura della connessione.\n";
                LOG.severe(msg); 
                throw new WebStorageException(msg + npe.getMessage());
            } catch (SQLException sqle) {
                throw new WebStorageException(FOR_NAME + sqle.getMessage());
            }
        }
    }
    
}