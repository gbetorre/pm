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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Vector;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import it.alma.bean.ActivityBean;
import it.alma.bean.BeanUtil;
import it.alma.bean.CodeBean;
import it.alma.bean.CourseBean;
import it.alma.bean.DepartmentBean;
import it.alma.bean.ItemBean;
import it.alma.bean.PersonBean;
import it.alma.bean.ProjectBean;
import it.alma.bean.RiskBean;
import it.alma.bean.SkillBean;
import it.alma.bean.StatusBean;
import it.alma.bean.WbsBean;
import it.alma.exception.AttributoNonValorizzatoException;
import it.alma.exception.CommandException;
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
     * <p>Connessione db postgres univr.</p>
     */
    protected static DataSource univr_manager = null;
    /**
     * 
     */
    private static final String nomeFile = "http://localhost:8080/whoami";
    /**
     * 
     *
    static String auth = null;
    static {
        try {
            auth = Utils.getContent(nomeFile);
        } catch (NotFoundException nfe) {
            String msg = FOR_NAME + "Probabile problema nel puntamento alla HashMap dei parametri.\n";
            LOG.severe(msg); 
            nfe.printStackTrace();
        } catch (CommandException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }*/
    

    
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
                //JOptionPane.showMessageDialog(null, "Accesso al DB per almalaurea", "Inizializzazione Completata", 0, null);
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
                //JOptionPane.showMessageDialog(null, "Accesso al DB di POL", "Inizializzazione Completata", 0, null);
            } catch (NamingException ne) {
                throw new WebStorageException(FOR_NAME + "Problema nel recuperare la risorsa jdbc/pol per problemi di naming: " + ne.getMessage());
            } catch (Exception e) {
                throw new WebStorageException(FOR_NAME + "Errore generico nel costruttore: " + e.getMessage(), e);
            }
        }
        if (univr_manager == null && false /*&& auth.contains("Authenticated")*/) {
            try {
                univr_manager = (DataSource) ((Context) new InitialContext()).lookup("java:comp/env/jdbc/univr");
                if (univr_manager == null)
                    throw new WebStorageException(FOR_NAME + "La risorsa `jdbc/univr' non e\' disponibile. Verificare configurazione e collegamenti.\n");
                //JOptionPane.showMessageDialog(null, "Accesso al DB di univr", "Inizializzazione Completata", 0, null);
            } catch (NamingException ne) {
                throw new WebStorageException(FOR_NAME + "Problema nel recuperare la risorsa jdbc/univr per problemi di naming: " + ne.getMessage());
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
     * <p>Restituisce 
     * <ul>
     * <li>il massimo valore del contatore identificativo di una
     * tabella il cui nome viene passato come argomento</li> 
     * <li>oppure zero se nella tabella non sono presenti record.</li>
     * </ul></p>
     * 
     * @param table nome della tabella di cui si vuol recuperare il max(id)
     * @return <code>int</code> - un intero che rappresenta il massimo valore trovato, oppure zero se non sono stati trovati valori
     * @throws WebStorageException se si verifica un problema nella query o in qualche tipo di puntamento
     */
    public int getMax (String table) 
                throws WebStorageException {
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement pst = null;
        try {
            int count = 0;
            String query = SELECT_MAX_ID + table;
            con = pol_manager.getConnection();
            pst = con.prepareStatement(query);
            pst.clearParameters();
            rs = pst.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
            return count;
        }  catch (SQLException sqle) {
            String msg = FOR_NAME + "Impossibile recuperare il max(id).\n";
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
    
    
    /* ********************************************************** *
     *                        Metodi di POL                       *
    /* ********************************************************** *
     *                     Metodi di SELEZIONE                    *
     * ********************************************************** */
    /**
     * <p>Restituisce l'username dato l'id della persona</p>
     * 
     * @param userId nome utente
     * @return <code>String</code> - stringa che definisce lo username dell'utente loggato
     * @throws WebStorageException se si verifica un problema nella query o in qualche tipo di puntamento
     */
    @SuppressWarnings({ "null", "static-method" })
    public String getLogin (int userId) 
                     throws WebStorageException {
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement pst = null;
        String username = Utils.VOID_STRING;
        try {
            con = pol_manager.getConnection();
            pst = con.prepareStatement(GET_USERNAME);
            pst.clearParameters();
            pst.setInt(1, userId);
            rs = pst.executeQuery();
            if (rs.next()) {
                username = rs.getString(1);
            }
            return username;
        } catch (SQLException sqle) {
            String msg = FOR_NAME + "Impossibile recuperare lo username.\n";
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
     * <p>Restituisce un PersonBean rappresentante un utente loggato.</p>
     * 
     * @param username - username della persona che ha eseguito il login
     * @param password - password della persona che ha eseguito il login
     * @return <code>PersonBean</code> - PersonBean rappresentante l'utente loggato
     * @throws WebStorageException se si verifica un problema nell'esecuzione della query, nell'accesso al db o in qualche tipo di puntamento
     * @throws it.alma.exception.AttributoNonValorizzatoException  eccezione che viene sollevata se questo oggetto viene usato e l'id della persona non &egrave; stato valorizzato (&egrave; un dato obbligatorio) 
     */
    @SuppressWarnings({ "null", "static-method" })
    public PersonBean getUser(String username,
                              String password)
                       throws WebStorageException, AttributoNonValorizzatoException {
        ResultSet rs, rs2 = null;
        Connection con = null;
        PreparedStatement pst = null;
        PersonBean usr = null;
        Vector<CodeBean> vRuoli = new Vector<CodeBean>();
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
            pst = con.prepareStatement(GET_RUOLIPERSONA);
            pst.clearParameters();
            pst.setInt(1, usr.getId());
            rs2 = pst.executeQuery();
            while(rs2.next()) {
                CodeBean ruolo = new CodeBean();
                BeanUtil.populate(ruolo, rs2);
                vRuoli.add(ruolo);
            }
            usr.setRuoli(vRuoli);
            return usr;
        } catch (SQLException sqle) {
            String msg = FOR_NAME + "Oggetto PersonBean non valorizzato; problema nella query dell\'utente.\n";
            LOG.severe(msg); 
            throw new WebStorageException(msg + sqle.getMessage(), sqle);
        } catch (AttributoNonValorizzatoException sqle) {
            String msg = FOR_NAME + "Oggetto id della persona non valorizzato; problema nella query dell\'utente.\n";
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
     * <p>Restituisce un Vector di PersonBean, ciascuno rappresentante 
     * un utente del dipartimento il cui identificativo viene 
     * passato come argomento.</p>
     * 
     * @param projectId - username della persona che ha eseguito il login
     * @return <code>Vector&lt;PersonBean&gt;</code> - elenco di PersonBean rappresentante le persone del dipartimento a cui il progetto e' collegato
     * @throws WebStorageException se si verifica un problema nell'esecuzione della query, nell'accesso al db o in qualche tipo di puntamento
     * @throws it.alma.exception.AttributoNonValorizzatoException  eccezione che viene sollevata se questo oggetto viene usato e l'id della persona non &egrave; stato valorizzato (&egrave; un dato obbligatorio) 
     */
    @SuppressWarnings({ "null", "static-method" })
    public Vector<PersonBean> getPeople(int projectId)
                                 throws WebStorageException, 
                                        AttributoNonValorizzatoException {
        ResultSet rs, rs1 = null;
        Connection con = null;
        PreparedStatement pst = null;
        PersonBean person = null;
        Vector<PersonBean> people = new Vector<PersonBean>();
        Vector<SkillBean> skills = null;
        try {
            con = pol_manager.getConnection();
            pst = con.prepareStatement(GET_PEOPLE_BY_DEPARTMENT);
            pst.clearParameters();
            pst.setInt(1, projectId);
            rs = pst.executeQuery();
            while (rs.next()) {
                person = new PersonBean();
                skills = new Vector<SkillBean>();
                BeanUtil.populate(person, rs);
                // Recupera competenze di ogni persona
                pst = con.prepareStatement(GET_SKILLS_BY_PERSON);
                pst.clearParameters();
                pst.setInt(1, projectId);
                pst.setInt(2, person.getId());
                rs1 = pst.executeQuery();
                while (rs1.next()) {
                    SkillBean skill = new SkillBean();
                    BeanUtil.populate(skill, rs1);
                    skills.add(skill);
                }
                person.setCompetenze(skills);
                people.add(person);
            }
            return people;
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
     * <p>Restituisce un Vector di ProjectBean rappresentante i progetti dell'utente loggato .</p>
     * 
     * @param userId identificativo della persona di cui si vogliono recuperare i progetti
     * @param getAll flag che definisce si devono recuperare tutti i progetti, oppure solo un sottoinsieme dei progetti che possono essere modificati
     * @return <code>Vector&lt;ProjectBean&gt;</code> - ProjectBean rappresentante i progetti dell'utente loggato
     * @throws WebStorageException se si verifica un problema nell'esecuzione della query, nell'accesso al db o in qualche tipo di puntamento
     */
    @SuppressWarnings({ "null", "static-method" })
    public Vector<ProjectBean> getProjects(int userId, 
                                           boolean getAll)
    							    throws WebStorageException {
    	ResultSet rs, rs2 = null;
    	Connection con = null;
        PreparedStatement pst = null;
        ProjectBean project = null;
        DepartmentBean dipart = null;
        CodeBean statoProgetto = null;
        int idDipart = -1;
        int idStatoProgetto = -1;
        Vector<ProjectBean> projects = new Vector<ProjectBean>();
        // Decide quale query deve fare
        String query = getAll ? GET_PROJECTS : GET_WRITABLE_PROJECTS_BY_USER_ID;
        try {
            con = pol_manager.getConnection();
            pst = con.prepareStatement(query);
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
                if (rs2.next()) {
                	statoProgetto = new CodeBean();
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
     * <p>Recupera i progetti su cui un utente, il cui username viene
     * passato come argomento, ha i diritti di scrittura/editing.</p>
     * 
     * @param userName login dell'utente di cui si vogliono ottenere i progetti editabili
     * @return <code>Vector&lt;ProjectBean&gt;</code> - un Vector di ProjectBean, che rappresentano i progetti su cui l'utente ha i diritti di scrittura
     * @throws WebStorageException se si verifica un problema nell'esecuzione della query, nel recupero di attributi obbligatori non valorizzati o in qualche altro tipo di puntamento
     */
    @SuppressWarnings({ "null", "static-method" })
    public Vector<ProjectBean> getWritableProjects(String userName)
                                            throws WebStorageException {
        ResultSet rs, rs2 = null;
        Connection con = null;
        PreparedStatement pst = null;
        ProjectBean project = null;
        DepartmentBean dipart = null;
        CodeBean statoProgetto = null;
        int idDipart = -1;
        int idStatoProgetto = -1;
        Vector<ProjectBean> projects = new Vector<ProjectBean>();
        try {
            con = pol_manager.getConnection();
            pst = con.prepareStatement(GET_WRITABLE_PROJECTS_BY_USER_NAME);
            pst.clearParameters();
            pst.setString(1, userName);
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
                if (rs2.next()) {
                    statoProgetto = new CodeBean();
                    BeanUtil.populate(statoProgetto, rs2);
                    project.setStatoProgetto(statoProgetto);
                }
                // Aggiunge il progetto valorizzato all'elenco
                projects.add(project);
            }
            return projects;
        } catch (AttributoNonValorizzatoException anve) {
            String msg = FOR_NAME + "Oggetto ProjectBean.idDipart non valorizzato; problema nella query dei progetti su cui l\'utente ha diritti di scrittura.\n";
            LOG.severe(msg); 
            throw new WebStorageException(msg + anve.getMessage(), anve);
        } catch (SQLException sqle) {
            String msg = FOR_NAME + "Oggetto ProjectBean non valorizzato; problema nella query dei progetti su cui l\'utente ha diritti di scrittura.\n";
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
     * @param projectId - identificativo del progetto che si vuol recuperare
     * @param userId - identificativo della persona che si vuol recuperare
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
            if (rs.next()) {
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
     * <p>Restituisce un Vector di StatusBean rappresentante tutti gli status del progetto attuale</p>
     * 
     * @param projId - id del progetto di cui estrarre gli status
     * @return <code>ArrayList&lt;StatusBean&gt;</code> - ArrayList&lt;StatusBean&gt; rappresentante gli status del progetto.
     * @throws WebStorageException se si verifica un problema nell'esecuzione delle query, nell'accesso al db o in qualche tipo di puntamento 
     */
    @SuppressWarnings({ "null", "static-method" })
    public ArrayList<StatusBean> getStatusList(int projId) 
                                        throws WebStorageException {
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement pst = null;
        StatusBean status = null;
        ArrayList<StatusBean> statusList = new ArrayList<StatusBean>();
        try {
            con = pol_manager.getConnection();
            pst = con.prepareStatement(GET_PROJECT_STATUS_LIST);
            pst.clearParameters();
            pst.setInt(1, projId);
            rs = pst.executeQuery();
            while (rs.next()) {
                status = new StatusBean();
                BeanUtil.populate(status, rs);
                statusList.add(status);
            }
            return statusList;
        } catch (SQLException sqle) {
            String msg = FOR_NAME + "Oggetto VectorStatusBean non valorizzato; problema nella query dell\'utente.\n";
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
     * <p>Restituisce un StatusBean rappresentante uno status richiesto del progetto,
     * a partire dal suo ID, passato come argomento.</p>
     * 
     * @param idStatus id dello status da restituire
     * @return <code>StatusBean</code> - StatusBean rappresentante lo status di dato identificativo passato come argomento
     * @throws WebStorageException se si verifica un problema nell'esecuzione delle query, nell'accesso al db o in qualche tipo di puntamento
     */
    @SuppressWarnings("null")
    public StatusBean getStatus(int idStatus) 
                         throws WebStorageException {
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement pst = null;
        StatusBean status = null;
        CodeBean statiTemp = new CodeBean();
        HashMap<String, CodeBean> stati = new HashMap<String, CodeBean>(15);
        int ordinal = 0;
        StringBuffer statoString = null; //new StringBuffer();
        try {
            con = pol_manager.getConnection();
            pst = con.prepareStatement(GET_STATUS);
            pst.clearParameters();
            pst.setInt(1, idStatus);
            rs = pst.executeQuery();
            if (rs.next()) {
                status = new StatusBean();
                BeanUtil.populate(status, rs);
                // Ramo di stato costi
                statiTemp = retrieve(GET_STATOCOSTI, idStatus);
                statoString = new StringBuffer("StatoCosti");
                statiTemp.setInformativa(statoString.toString());
                statiTemp.setOrdinale(ordinal++);
                stati.put(statoString.toString(), statiTemp);
                // Ramo di stato tempi
                statiTemp = retrieve(GET_STATOTEMPI, idStatus);
                statoString = null;
                statoString = new StringBuffer("StatoTempi");
                statiTemp.setInformativa(statoString.toString());
                statiTemp.setOrdinale(ordinal++);
                stati.put(statoString.toString(), statiTemp);
                // Ramo di stato rischi
                statiTemp = retrieve(GET_STATORISCHI, idStatus);
                statoString = null;
                statoString = new StringBuffer("StatoRischi");
                statiTemp.setInformativa(statoString.toString());
                statiTemp.setOrdinale(ordinal++);
                stati.put(statoString.toString(), statiTemp);
                // Ramo di stato risorse
                statiTemp = retrieve(GET_STATORISORSE, idStatus);
                statoString = null;
                statoString = new StringBuffer("StatoRisorse");
                statiTemp.setInformativa(statoString.toString());
                statiTemp.setOrdinale(ordinal++);
                stati.put(statoString.toString(), statiTemp);
                // Ramo di stato scope
                statiTemp = retrieve(GET_STATOSCOPE, idStatus);
                statoString = null;
                statoString = new StringBuffer("StatoScope");
                statiTemp.setInformativa(statoString.toString());
                statiTemp.setOrdinale(ordinal++);
                stati.put(statoString.toString(), statiTemp);
                // Ramo di stato comunicazione
                statiTemp = retrieve(GET_STATOCOMUNICAZIONE, idStatus);
                statoString = null;
                statoString = new StringBuffer("StatoComunicazione");
                statiTemp.setInformativa(statoString.toString());
                statiTemp.setOrdinale(ordinal++);
                stati.put(statoString.toString(), statiTemp);
                // Ramo di stato qualita
                statiTemp = retrieve(GET_STATOQUALITA, idStatus);
                statoString = null;
                statoString = new StringBuffer("StatoQualita");
                statiTemp.setInformativa(statoString.toString());
                statiTemp.setOrdinale(ordinal++);
                stati.put(statoString.toString(), statiTemp);
                // Ramo di stato approvvigionamenti
                statiTemp = retrieve(GET_STATOAPPROVVIGIONAMENTI, idStatus);
                statoString = null;
                statoString = new StringBuffer("StatoApprovvigionamenti");
                statiTemp.setInformativa(statoString.toString());
                statiTemp.setOrdinale(ordinal++);
                stati.put(statoString.toString(), statiTemp);
                // Ramo di stato stakeholder
                statiTemp = retrieve(GET_STATOSTAKEHOLDER, idStatus);
                statoString = null;
                statoString = new StringBuffer("StatoStakeholder");
                statiTemp.setInformativa(statoString.toString());
                statiTemp.setOrdinale(ordinal++);
                stati.put(statoString.toString(), statiTemp);
            }
            status.setStati(stati);
            return status;
        } catch (SQLException sqle) {
            String msg = FOR_NAME + "Oggetto StatusBean non valorizzato; problema nella query dello status.\n";
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
     * <p>Restituisce un StatusBean rappresentante lo status attuale del progetto,
     * ovvero l'ultimo avanzamento (status) progetto in ordine di tempo,
     * ovvero quello avente la data di inizio pi&uacute; prossima alla
     * data corrente al momento della richiesta effettuata dall'utente.</p>
     * 
     * @param idProj id del progetto del quale ricavare lo status
     * @param statusDate data di inizio dello status da ricavare
     * @return <code>StatusBean</code> - StatusBean rappresentante lo status corrente
     * @throws WebStorageException se si verifica un problema nell'esecuzione delle query, nell'accesso al db o in qualche tipo di puntamento
     */
    public StatusBean getStatus(int idProj, 
                                Date statusDate) 
                         throws WebStorageException {
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement pst = null;
        StatusBean status = null;
        CodeBean statiTemp = new CodeBean();
        HashMap<String, CodeBean> stati = new HashMap<String, CodeBean>(15);
        int ordinal = 0;
        StringBuffer statoString = null; //new StringBuffer();
        try {
            con = pol_manager.getConnection();
            pst = con.prepareStatement(GET_PROJECT_STATUS);
            pst.clearParameters();
            pst.setInt(1, idProj);
            pst.setDate(2, Utils.convert(statusDate));  // Non accetta una java.util.Date, ma una java.sql.Date
            rs = pst.executeQuery();
            if (rs.next()) {
                status = new StatusBean();
                BeanUtil.populate(status, rs);
                // Ramo di stato costi
                statiTemp = retrieve(GET_STATOCOSTI, status.getId());
                statoString = new StringBuffer("StatoCosti");
                statiTemp.setInformativa(statoString.toString());
                statiTemp.setOrdinale(ordinal++);
                stati.put(statoString.toString(), statiTemp);
                // Ramo di stato tempi
                statiTemp = retrieve(GET_STATOTEMPI, status.getId());
                statoString = null;
                statoString = new StringBuffer("StatoTempi");
                statiTemp.setInformativa(statoString.toString());
                statiTemp.setOrdinale(ordinal++);
                stati.put(statoString.toString(), statiTemp);
                // Ramo di stato rischi
                statiTemp = retrieve(GET_STATORISCHI, status.getId());
                statoString = null;
                statoString = new StringBuffer("StatoRischi");
                statiTemp.setInformativa(statoString.toString());
                statiTemp.setOrdinale(ordinal++);
                stati.put(statoString.toString(), statiTemp);
                // Ramo di stato risorse
                statiTemp = retrieve(GET_STATORISORSE, status.getId());
                statoString = null;
                statoString = new StringBuffer("StatoRisorse");
                statiTemp.setInformativa(statoString.toString());
                statiTemp.setOrdinale(ordinal++);
                stati.put(statoString.toString(), statiTemp);
                // Ramo di stato scope
                statiTemp = retrieve(GET_STATOSCOPE, status.getId());
                statoString = null;
                statoString = new StringBuffer("StatoScope");
                statiTemp.setInformativa(statoString.toString());
                statiTemp.setOrdinale(ordinal++);
                stati.put(statoString.toString(), statiTemp);
                // Ramo di stato comunicazione
                statiTemp = retrieve(GET_STATOCOMUNICAZIONE, status.getId());
                statoString = null;
                statoString = new StringBuffer("StatoComunicazione");
                statiTemp.setInformativa(statoString.toString());
                statiTemp.setOrdinale(ordinal++);
                stati.put(statoString.toString(), statiTemp);
                // Ramo di stato qualita
                statiTemp = retrieve(GET_STATOQUALITA, status.getId());
                statoString = null;
                statoString = new StringBuffer("StatoQualita");
                statiTemp.setInformativa(statoString.toString());
                statiTemp.setOrdinale(ordinal++);
                stati.put(statoString.toString(), statiTemp);
                // Ramo di stato approvvigionamenti
                statiTemp = retrieve(GET_STATOAPPROVVIGIONAMENTI, status.getId());
                statoString = null;
                statoString = new StringBuffer("StatoApprovvigionamenti");
                statiTemp.setInformativa(statoString.toString());
                statiTemp.setOrdinale(ordinal++);
                stati.put(statoString.toString(), statiTemp);
                // Ramo di stato stakeholder
                statiTemp = retrieve(GET_STATOSTAKEHOLDER, status.getId());
                statoString = null;
                statoString = new StringBuffer("StatoStakeholder");
                statiTemp.setInformativa(statoString.toString());
                statiTemp.setOrdinale(ordinal++);
                stati.put(statoString.toString(), statiTemp);
            }
            status.setStati(stati);
            return status;
        } catch (SQLException sqle) {
            String msg = FOR_NAME + "Oggetto StatusBean non valorizzato; problema nella query dello status piu\' prossimo alla data passata come argomento.\n";
            LOG.severe(msg); 
            throw new WebStorageException(msg + sqle.getMessage(), sqle);
        } catch (AttributoNonValorizzatoException anve) {
            String msg = FOR_NAME + "Oggetto StatusBean non valorizzato; problema nella query dello status piu\' prossimo alla data passata come argomento.\n";
            LOG.severe(msg); 
            throw new WebStorageException(msg + anve.getMessage(), anve);
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
     * <p>Restituisce un Vector di ActivityBean rappresentante le attivit&agrave; del progetto attuale</p>
     * 
     * @param projId - id del progetto di cui estrarre le attivit&agrave;
     * @return <code>Vector&lt;AttvitaBean&gt;</code> - ActivityBean rappresentante l'attivit&agrave; del progetto.
     * @throws WebStorageException se si verifica un problema nell'esecuzione delle query, nell'accesso al db o in qualche tipo di puntamento 
     */
    @SuppressWarnings({ "null", "static-method" })
    public Vector<ActivityBean> getActivities(int projId) 
                                       throws WebStorageException {
        ResultSet rs, rs2 = null;
        Connection con = null;
        PreparedStatement pst = null;
        ActivityBean attivita = null;
        PersonBean person = null;
        Vector<ActivityBean> activities = new Vector<ActivityBean>();
        Vector<PersonBean> people = new Vector<PersonBean>();
        try {
            con = pol_manager.getConnection();
            pst = con.prepareStatement(GET_ACTIVITIES);
            pst.clearParameters();
            pst.setInt(1, projId);
            rs = pst.executeQuery();
            while (rs.next()) {
                attivita = new ActivityBean();
                BeanUtil.populate(attivita, rs);
                pst = null;
                pst = con.prepareStatement(GET_PEOPLE_ON_ACTIVITY);
                pst.clearParameters();
                pst.setInt(1, attivita.getId());
                rs2 = pst.executeQuery();
                while (rs2.next()) {
                    person = new PersonBean();
                    BeanUtil.populate(person, rs2);
                    people.add(person);
                }
                attivita.setPersone(people);
                activities.add(attivita);
            }
            return activities;
        } catch (AttributoNonValorizzatoException anve) {
            String msg = FOR_NAME + "Oggetto PersonBean non valorizzato; problema nella query dell\'utente.\n";
            LOG.severe(msg); 
            throw new WebStorageException(msg + anve.getMessage(), anve);
        } catch (SQLException sqle) {
            String msg = FOR_NAME + "Oggetto ActivityBean non valorizzato; problema nella query dell\'utente.\n";
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
     * <p>Restituisce un Vector di SkillBean rappresentante le competenze del progetto attuale</p>
     * 
     * @param projId - id del progetto di cui estrarre le competenze
     * @return <code>Vector&lt;SkillBean&gt;</code> - SkillBean rappresentante le competenze del progetto.
     * @throws WebStorageException se si verifica un problema nell'esecuzione della query, nell'accesso al db o in qualche tipo di puntamento
     */
    @SuppressWarnings({ "null", "static-method" })
    public Vector<SkillBean> getSkills(int projId)
                                throws WebStorageException {
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement pst = null;
        SkillBean competenza = null;
        Vector<SkillBean> skills = new Vector<SkillBean>();
        try {
            con = pol_manager.getConnection();
            pst = con.prepareStatement(GET_SKILLS);
            pst.clearParameters();
            pst.setInt(1, projId);
            rs = pst.executeQuery();
            while (rs.next()) {
                competenza = new SkillBean();
                BeanUtil.populate(competenza, rs);
                skills.add(competenza);
            }
            return skills;
        } catch (SQLException sqle) {
            String msg = FOR_NAME + "Oggetto SkillBean non valorizzato; problema nella query dell\'utente.\n";
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
     * <p>Restituisce un Vector di RiskBean rappresentante i rischi del progetto attuale</p>
     * 
     * @param projId - id del progetto di cui estrarre i rischi
     * @return <code>Vector&lt;RiskBean&gt;</code> - RiskBean rappresentante i rischi del progetto.
     * @throws WebStorageException se si verifica un problema nell'esecuzione della query, nell'accesso al db o in qualche tipo di puntamento
     */
    @SuppressWarnings({ "null", "static-method" })
    public Vector<RiskBean> getRisks(int projId)
                              throws WebStorageException {
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement pst = null;
        RiskBean rischio = null;
        Vector<RiskBean> risks = new Vector<RiskBean>();
        try {
            con = pol_manager.getConnection();
            pst = con.prepareStatement(GET_RISKS);
            pst.clearParameters();
            pst.setInt(1, projId);
            rs = pst.executeQuery();
            while (rs.next()) {
                rischio = new RiskBean();
                BeanUtil.populate(rischio, rs);
                risks.add(rischio);
            }
            return risks;
        } catch (SQLException sqle) {
            String msg = FOR_NAME + "Oggetto RiskBean non valorizzato; problema nella query dell\'utente.\n";
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
     * <p>Restituisce un vector contenente tutte le Wbs di un dato progetto.</p>
     * 
     * @param idProj - id del progetto di cui caricare le wbs
     * @param getAll - flag specificante se bisogna recuperare solo i WorkPackage (false) o tutte le WBS (true)
     * @return vectorWbs - vettore contenente tutte le Wbs di un progetto
     * @throws WebStorageException se si verifica un problema nell'esecuzione della query, nell'accesso al db o in qualche tipo di puntamento
     */
    @SuppressWarnings({ "null", "static-method" })
    public Vector<WbsBean> getWbs (int idProj, 
                                   boolean getAll) 
                            throws WebStorageException {
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement pst = null;
        WbsBean wbs = null;
        Vector<WbsBean> vWbs = new Vector<WbsBean>();
        String query = getAll ? GET_WBS_BY_PROJECT : GET_WP_BY_PROJECT;
        try {
            // Ottiene il progetto precaricato quando l'utente si è loggato corrispondente al progetto sul quale aggiungere un'attività
            Integer key = new Integer(idProj);
            con = pol_manager.getConnection();
            pst = con.prepareStatement(query);
            pst.clearParameters();
            pst.setInt(1, key);
            rs = pst.executeQuery();
            while (rs.next()) {
                wbs = new WbsBean();
                BeanUtil.populate(wbs, rs);
                vWbs.add(wbs);
            }
            return vWbs;
        }  catch (SQLException sqle) {
            String msg = FOR_NAME + "Oggetto RiskBean non valorizzato; problema nella query dell\'utente.\n";
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
     * 
     * @param query 
     * @return 
     * @throws WebStorageException 
     */
    @SuppressWarnings({ "null", "static-method" })
    public LinkedList<CodeBean> getStati(String query)
                                  throws WebStorageException {
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement pst = null;
        CodeBean stato = null;
        Vector<CodeBean> stati = new Vector<CodeBean>();
        try {
            con = pol_manager.getConnection();
            pst = con.prepareStatement(query);
            pst.clearParameters();
            rs = pst.executeQuery();
            while (rs.next()) {
                stato = new CodeBean();
                BeanUtil.populate(stato, rs);
                stati.add(stato);
            }
            return new LinkedList<CodeBean>(stati);
        } catch (SQLException sqle) {
            String msg = FOR_NAME + "Oggetto non valorizzato; problema nella query.\n";
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
    
    /* ********************************************************** *
     *                        Metodi di POL                       *
    /* ********************************************************** *
     *                   Metodi di AGGIORNAMENTO                  *
     * ********************************************************** */
    /**
     * <p>Metodo che controlla la query da eseguire e chiama il metodo opportuno.</p>
     * 
     * @param idProj - id del progetto da aggiornare 
     * @param userId - id dell'utente che ha eseguito il login
     * @param projects - Map contenente la lista dei progetti su cui l'utente corrente e' abilitato alla modifica
     * @param objectsRelatedToProject - Map contenente le hashmap che contengono le attività, i rischi e le competenze su cui l'utente e' abilitato alla modifica
     * @param params - hashmap che contiene i parametri che si vogliono aggiornare del progetto
     * @throws WebStorageException se si verifica un problema nell'esecuzione della query, nell'accesso al db o in qualche tipo di puntamento 
     */
    @SuppressWarnings({ "null" })
    public void updateProjectPart(int idProj,
                                  int userId,
                                  HashMap<Integer, ProjectBean> projects, 
                                  HashMap<String, HashMap<Integer, Vector>> objectsRelatedToProject,
                                  HashMap<String, HashMap<String, String>>params) 
                           throws WebStorageException {
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement pst = null;        
        try {
            // Ottiene il progetto precaricato quando l'utente si è loggato corrispondente al progetto che vuole aggiornare
            Integer key = new Integer(idProj);
            ProjectBean project = projects.get(key);
            // Recuperare le liste di attività, skill e rischi
            HashMap<Integer, Vector> activitiesOfProject = objectsRelatedToProject.get(Query.PART_PROJECT_CHARTER_MILESTONE);
            Vector<ActivityBean> activities = (Vector<ActivityBean>) activitiesOfProject.get(key);
            // Ottiene la connessione
            con = pol_manager.getConnection();
            /* **************************************************************** *
             * Gestisce gli update delle varie funzionalita' del ProjectCharter *
             * **************************************************************** */
            if (params.containsKey(PART_PROJECT_CHARTER_VISION)) {
                HashMap<String, String> paramsVision = params.get(PART_PROJECT_CHARTER_VISION);
                if (Utils.containsValues(paramsVision)) {
                    con.setAutoCommit(false);
                    // Controllo che la vision del progetto non abbia subito modifiche non visualizzate dall'utente
                    pst = con.prepareStatement(GET_PROJECT_VISION);
                    pst.clearParameters();
                    pst.setInt(1, idProj);
                    pst.setInt(2, userId);
                    rs = pst.executeQuery();
                    con.commit();
                    if (rs.next()) {
                        if ( (rs.getString("situazioneAttuale").equals(project.getSituazioneAttuale())) &&
                             (rs.getString("descrizione").equals(project.getDescrizione())) &&
                             (rs.getString("obiettiviMisurabili").equals(project.getObiettiviMisurabili())) &&
                             (rs.getString("minacce").equals(project.getMinacce())) ) {
                            pst = null;
                            pst = con.prepareStatement(UPDATE_VISION);
                            pst.clearParameters();
                            pst.setString(1, paramsVision.get("pcv-situazione"));
                            pst.setString(2, paramsVision.get("pcv-descrizione"));
                            pst.setString(3, paramsVision.get("pcv-obiettivi"));
                            pst.setString(4, paramsVision.get("pcv-minacce"));
                            pst.setInt(5, idProj);
                            pst.executeUpdate();
                            con.commit();
                        }
                        else {
                            //JOptionPane.showMessageDialog(null, "I dati non sono stati salvati, in quanto sono stati modificati da un altro utente.", FOR_NAME + ": esito OK", JOptionPane.INFORMATION_MESSAGE, null);
                            return;
                        }
                    }
                }
            }
            if (params.containsKey(PART_PROJECT_CHARTER_STAKEHOLDER)) {
                HashMap<String, String> paramsStakeholder = params.get(PART_PROJECT_CHARTER_STAKEHOLDER);
                if (Utils.containsValues(paramsStakeholder)) {
                    con.setAutoCommit(false);
                    // Controllo che gli stakeholder del progetto non abbiano subito modifiche non visualizzate dall'utente
                    pst = con.prepareStatement(GET_PROJECT_STAKEHOLDER);
                    pst.clearParameters();
                    pst.setInt(1, idProj);
                    pst.setInt(2, userId);
                    rs = pst.executeQuery();
                    con.commit();
                    if (rs.next()) {
                        if ( (rs.getString("stakeholderMarginali").equals(project.getStakeholderMarginali())) &&
                                (rs.getString("stakeholderOperativi").equals(project.getStakeholderOperativi())) &&
                                (rs.getString("stakeholderIstituzionali").equals(project.getStakeholderIstituzionali())) &&
                                (rs.getString("stakeholderChiave").equals(project.getStakeholderChiave())) ) {
                               pst = null;
                               pst = con.prepareStatement(UPDATE_STAKEHOLDER);
                               pst.clearParameters();
                               pst.setString(1, paramsStakeholder.get("pcs-marginale"));
                               pst.setString(2, paramsStakeholder.get("pcs-operativo"));
                               pst.setString(3, paramsStakeholder.get("pcs-istituzionale"));
                               pst.setString(4, paramsStakeholder.get("pcs-chiave"));
                               pst.setInt(5, idProj);
                               pst.executeUpdate();
                               con.commit();
                        }
                        else {
                            //JOptionPane.showMessageDialog(null, "I dati non sono stati salvati, in quanto sono stati modificati da un altro utente.", FOR_NAME + ": esito OK", JOptionPane.INFORMATION_MESSAGE, null);
                            return;
                        }
                    }
                }
            }
            if (params.containsKey(PART_PROJECT_CHARTER_DELIVERABLE)) {
                HashMap<String, String> paramsDeliverable = params.get(PART_PROJECT_CHARTER_DELIVERABLE);
                if (Utils.containsValues(paramsDeliverable)) {
                    con.setAutoCommit(false);
                    // Controllo che la deliverable del progetto non abbia subito modifiche non visualizzate dall'utente
                    pst = con.prepareStatement(GET_PROJECT_DELIVERABLE);
                    pst.clearParameters();
                    pst.setInt(1, idProj);
                    pst.setInt(2, userId);
                    rs = pst.executeQuery();
                    con.commit();
                    if (rs.next()) {
                        if ( (rs.getString("deliverable").equals(project.getDeliverable())) ) {
                            pst = null;
                            pst = con.prepareStatement(UPDATE_DELIVERABLE);
                            pst.clearParameters();
                            pst.setString(1, paramsDeliverable.get("pcd-descrizione"));
                            pst.setInt(2, idProj);
                            pst.executeUpdate();
                            con.commit();
                        }
                        else {
                            //JOptionPane.showMessageDialog(null, "I dati non sono stati salvati, in quanto sono stati modificati da un altro utente.", FOR_NAME + ": esito OK", JOptionPane.INFORMATION_MESSAGE, null);
                            return;
                        }
                    }
                }
            }
            if (params.containsKey(PART_PROJECT_CHARTER_RESOURCE)) {
                HashMap<String, String> paramsResource = params.get(PART_PROJECT_CHARTER_RESOURCE);
                if (Utils.containsValues(paramsResource)) {
                    // 3 - Numero di campi fissi presenti in paramsResource (fornitoriChiaveEsterni, fornitoriChiaveInterni, ServiziAteneo)
                    // 4 - Numero di campi che contengono una competenza (id, nome, informativa, presenza)
                    for (int i = 0; i < ((paramsResource.size() - 3) / 4); i++) {
                        con.setAutoCommit(false);
                        String isPresenzaAsString = paramsResource.get("pcr-presenza" + String.valueOf(i));
                        if ( (!isPresenzaAsString.equalsIgnoreCase("true")) && (!isPresenzaAsString.equalsIgnoreCase("false"))  ) {
                            throw new ClassCastException("Attenzione: il valore del parametro \'pcr-presenza\' non e\' riconducibile a un valore boolean!\n");
                        }
                        pst = null;
                        pst = con.prepareStatement(UPDATE_SKILL_FROM_PROJECT);
                        pst.clearParameters();
                        pst.setString(1, paramsResource.get("pcr-nome" + String.valueOf(i)));
                        pst.setString(2, paramsResource.get("pcr-informativa" + String.valueOf(i)));
                        pst.setBoolean(3, Boolean.parseBoolean(paramsResource.get("pcr-presenza" + String.valueOf(i))));
                        pst.setInt(4, Integer.parseInt(paramsResource.get("pcr-id" + String.valueOf(i))));
                        pst.setInt(5, idProj);
                        pst.executeUpdate();
                        con.commit();
                    }
                    //Controllo che i campi (fornitoriChiaveEsterni, fornitoriChiaveInterni, ServiziAteneo) non siano stati modificati da altri utenti
                    /*  TODO
                     * pst = null;
                     * pst = con.prepareStatement(GET_PROJECT_RESOURCE);
                     * pst.clearParameters();
                     * pst.setInt(1, idProj);
                     * pst.setInt(2, userId);
                     * rs = pst.executeQuery();
                     * con.commit();
                     * if (rs.next()) {
                     *     if ( (rs.getString("fornitoriChiaveInterni").equals(project.getFornitoriChiaveInterni())) &&
                     *          (rs.getString("fornitoriChiaveEsterni").equals(project.getFornitoriChiaveEsterni())) &&
                     *          (rs.getString("serviziAteneo").equals(project.getServiziAteneo())) ) {
                     *         pst = null;
                     */
                    pst = con.prepareStatement(UPDATE_RESOURCE);
                    pst.clearParameters();
                    pst.setString(1, paramsResource.get("pcr-chiaveesterni"));
                    pst.setString(2, paramsResource.get("pcr-chiaveinterni"));
                    pst.setString(3, paramsResource.get("pcr-serviziateneo"));
                    pst.setInt(4, idProj);
                    //JOptionPane.showMessageDialog(null, "Chiamata arrivata a updateProjectPart dall\'applicazione!", FOR_NAME + ": esito OK", JOptionPane.INFORMATION_MESSAGE, null);
                    pst.executeUpdate();
                    con.commit();
                    /* }
                     * else {
                     *     JOptionPane.showMessageDialog(null, "I dati non sono stati salvati, in quanto sono stati modificati da un altro utente.", FOR_NAME + ": esito OK", JOptionPane.INFORMATION_MESSAGE, null);
                     *     return;
                     * }
                     * }
                     */
                } 
            }
            if (params.containsKey(PART_PROJECT_CHARTER_RISK)) {
                HashMap<String, String> paramsRisk = params.get(PART_PROJECT_CHARTER_RISK);
                if (Utils.containsValues(paramsRisk)) {
                    con.setAutoCommit(false);
                    for (int i = 0; i < ((paramsRisk.size()) / 6); i++) {
                        pst = con.prepareStatement(UPDATE_RISK);
                        pst.clearParameters();
                        pst.setString(1, paramsRisk.get("pck-nome" + String.valueOf(i)));
                        pst.setString(2, paramsRisk.get("pck-informativa" + String.valueOf(i)));
                        pst.setString(3, paramsRisk.get("pck-impatto" + String.valueOf(i)));
                        pst.setString(4, paramsRisk.get("pck-livello" + String.valueOf(i)));
                        pst.setString(5, paramsRisk.get("pck-stato" + String.valueOf(i)));
                        pst.setInt(6, Integer.parseInt(paramsRisk.get("pck-id" + String.valueOf(i))));
                        pst.setInt(7, idProj);
                        //JOptionPane.showMessageDialog(null, "Chiamata arrivata a updateProjectPart dall\'applicazione!", FOR_NAME + ": esito OK", JOptionPane.INFORMATION_MESSAGE, null);
                        pst.executeUpdate();
                        con.commit();
                    }
                }
            }
            if (params.containsKey(PART_PROJECT_CHARTER_CONSTRAINT)) {
                HashMap<String, String> paramsConstraint = params.get(PART_PROJECT_CHARTER_CONSTRAINT);
                if (Utils.containsValues(paramsConstraint)) {
                    con.setAutoCommit(false);
                    pst = con.prepareStatement(GET_PROJECT_CONSTRAINT);
                    pst.clearParameters();
                    pst.setInt(1, idProj);
                    pst.setInt(2, userId);
                    rs = pst.executeQuery();
                    con.commit();
                    if (rs.next()) {
                        if ( (rs.getString("vincoli").equals(project.getVincoli())) ) {
                            pst = null;
                            pst = con.prepareStatement(UPDATE_CONSTRAINT);
                            con.setAutoCommit(false);
                            pst.clearParameters();
                            pst.setString(1, paramsConstraint.get("pcc-descrizione"));
                            pst.setInt(2, idProj);
                            //JOptionPane.showMessageDialog(null, "Chiamata arrivata a updateProjectPart dall\'applicazione!", FOR_NAME + ": esito OK", JOptionPane.INFORMATION_MESSAGE, null);
                            pst.executeUpdate();
                            con.commit();
                        }
                        else {
                            //JOptionPane.showMessageDialog(null, "I dati non sono stati salvati, in quanto sono stati modificati da un altro utente.", FOR_NAME + ": esito OK", JOptionPane.INFORMATION_MESSAGE, null);
                            return;
                        }
                    }
                }
            }
            if (params.containsKey(PART_PROJECT_CHARTER_MILESTONE)) {
                HashMap<String, String> paramsMilestone = params.get(PART_PROJECT_CHARTER_MILESTONE);
                if (Utils.containsValues(paramsMilestone)) {
                    con.setAutoCommit(false);
                    // 4 è il numero di campi che compongono un'attività
                    for (int i = 0; i < (paramsMilestone.size() / 4); i++) {
                        String isMilestoneAsString = paramsMilestone.get("pcm-milestone" + String.valueOf(i));
                        if ( (!isMilestoneAsString.equalsIgnoreCase("true")) && (!isMilestoneAsString.equalsIgnoreCase("false"))  ) {
                            throw new ClassCastException("Attenzione: il valore del parametro \'pcm-milestone\' non e\' riconducibile a un valore boolean!\n");
                        }
                        // Controllo che le attività del progetto non abbiano subito modifiche non visualizzate dall'utente
                        /* TODO
                         * Controllo di concorrenza da implementare
                        pst = con.prepareStatement(GET_ACTIVITY);
                        pst.clearParameters();
                        pst.setInt(1, idProj);
                        pst.setInt(2, Integer.parseInt(paramsMilestone.get("pcm-id" + String.valueOf(i)))); 
                        rs = pst.executeQuery();
                        if(rs.next()) {
                            if ( (rs.getString("id").equals(activities.get(i).getId())) &&
                                 (rs.getString("nome").equals(activities.get(i).getNome())) &&
                                 (rs.getString("descrizione").equals(activities.get(i).getDescrizione())) &&
                                 (rs.getBoolean("milestone") (activities.get(i).isMilestone())) ) {
                                
                            }
                            
                        }*/
                        pst = null;
                        pst = con.prepareStatement(UPDATE_ATTIVITA_FROM_PROGETTO);
                        pst.clearParameters();
                        pst.setString(1, paramsMilestone.get("pcm-nome" + String.valueOf(i)));
                        pst.setString(2, paramsMilestone.get("pcm-descrizione" + String.valueOf(i)));
                        pst.setBoolean(3, Boolean.parseBoolean(isMilestoneAsString));
                        pst.setInt(4, Integer.parseInt(paramsMilestone.get("pcm-id" + String.valueOf(i))));
                        pst.setInt(5, idProj);
                        //JOptionPane.showMessageDialog(null, "Chiamata arrivata a updateProjectPart dall\'applicazione!", FOR_NAME + ": esito OK", JOptionPane.INFORMATION_MESSAGE, null);
                        pst.executeUpdate();
                        con.commit();
                    }
                }
            }
            if (params.containsKey(PART_STATUS)) {
                HashMap<String, String> paramsStatus = params.get(PART_STATUS);
                if (Utils.containsValues(paramsStatus) && !paramsStatus.containsValue(Utils.UNIX_EPOCH)) {
                    pst = con.prepareStatement(UPDATE_PROJECT_STATUS);
                    con.setAutoCommit(false);
                    pst.clearParameters();
                    Date dateTemp = Utils.formatDate(paramsStatus.get("sts-datainizio"), "dd/MM/yyyy", Query.DATA_SQL_PATTERN);
                    pst.setDate(1, Utils.convert(dateTemp));
                    dateTemp = null;
                    dateTemp = Utils.formatDate(paramsStatus.get("sts-datafine"), "dd/MM/yyyy", Query.DATA_SQL_PATTERN);
                    pst.setDate(2, Utils.convert(dateTemp));
                    pst.setString(3, paramsStatus.get("sts-avanzamento"));
                    pst.setInt(4, Integer.parseInt(paramsStatus.get("sts-tempi")));
                    pst.setInt(5, Integer.parseInt(paramsStatus.get("sts-costi")));
                    pst.setInt(6, Integer.parseInt(paramsStatus.get("sts-rischi")));
                    pst.setInt(7, Integer.parseInt(paramsStatus.get("sts-risorse")));
                    pst.setInt(8, Integer.parseInt(paramsStatus.get("sts-scope")));
                    pst.setInt(9, Integer.parseInt(paramsStatus.get("sts-comunicazione")));
                    pst.setInt(10, Integer.parseInt(paramsStatus.get("sts-qualita")));
                    pst.setInt(11, Integer.parseInt(paramsStatus.get("sts-approvvigionamenti")));
                    pst.setInt(12, Integer.parseInt(paramsStatus.get("sts-stakeholder")));
                    pst.setDate(13, Utils.convert(Utils.convert(Utils.getCurrentDate())));
                    pst.setDate(14, Utils.convert(Utils.convert(Utils.getCurrentDate())));
                    pst.setString(15, getLogin(userId));
                    pst.setInt(16, idProj);
                    pst.setInt(17, Integer.parseInt(paramsStatus.get("sts-id")));
                    //JOptionPane.showMessageDialog(null, "Chiamata arrivata a updateProjectPart dall\'applicazione!", FOR_NAME + ": esito OK", JOptionPane.INFORMATION_MESSAGE, null);
                    pst.executeUpdate();
                    con.commit();
                }
            }
        } catch (NotFoundException nfe) {
            String msg = FOR_NAME + "Probabile problema nel puntamento alla HashMap dei parametri.\n";
            LOG.severe(msg); 
            throw new WebStorageException(msg + nfe.getMessage(), nfe);
        } catch (SQLException sqle) {
            String msg = FOR_NAME + "Tupla non aggiornata correttamente; problema nella query che aggiorna il progetto.\n";
            LOG.severe(msg); 
            throw new WebStorageException(msg + sqle.getMessage(), sqle);
        } catch (NumberFormatException nfe) {
            String msg = FOR_NAME + "Tupla non aggiornata correttamente; problema nella query che aggiorna il progetto.\n";
            LOG.severe(msg); 
            throw new WebStorageException(msg + nfe.getMessage(), nfe);
        } catch (CommandException ce) {
            String msg = FOR_NAME + "Tupla non aggiornata correttamente; problema nella query che aggiorna il progetto.\n";
            LOG.severe(msg); 
            throw new WebStorageException(msg + ce.getMessage(), ce);
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
    
    
    /** <p>Metodo che aggiorna le attivit&agrave; di progetto..</p>
     * 
     * @param idProj - id del progetto da aggiornare 
     * @param userId - id dell'utente che ha eseguito il login
     * @param projects - Map contenente la lista dei progetti su cui l'utente corrente e' abilitato alla modifica
     * @param activitiesRelatedToProject - HashMap contenente le attività su cui l'utente e' abilitato alla modifica
     * @param params - hashmap che contiene i parametri che si vogliono aggiornare del progetto
     * @throws WebStorageException se si verifica un problema nell'esecuzione della query, nell'accesso al db o in qualche tipo di puntamento 
     */
    @SuppressWarnings({ "null", "static-method" })
    public void updateActivityPart(int idProj,
                                   int userId,
                                   HashMap<Integer, ProjectBean> projects, 
                                   HashMap<Integer, Vector<ActivityBean>> activitiesRelatedToProject,
                                   HashMap<String, HashMap<String, String>>params) 
                            throws WebStorageException {
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement pst = null;        
        try {
            // Ottiene il progetto precaricato quando l'utente si è loggato corrispondente al progetto che vuole aggiornare
            Integer key = new Integer(idProj);
            ProjectBean project = projects.get(key);
            // Recuperare le liste di attività
            Vector<ActivityBean> activities = activitiesRelatedToProject.get(key);
            // Ottiene la connessione
            con = pol_manager.getConnection();
            /* **************************************************************** *
             *   Gestisce gli update delle varie funzionalita' delle attivita'  *
             * **************************************************************** */
            if (params.containsKey(PART_PROJECT_CHARTER_MILESTONE)) {
                HashMap<String, String> paramsMilestone = params.get(PART_PROJECT_CHARTER_MILESTONE);
                if (Utils.containsValues(paramsMilestone)) {
                    con.setAutoCommit(false);
                    // 4 è il numero di campi che compongono un'attività
                    for (int i = 0; i < (paramsMilestone.size() / 4); i++) {
                        String isMilestoneAsString = paramsMilestone.get("pcm-milestone" + String.valueOf(i));
                        if ( (!isMilestoneAsString.equalsIgnoreCase("true")) && (!isMilestoneAsString.equalsIgnoreCase("false"))  ) {
                            throw new ClassCastException("Attenzione: il valore del parametro \'pcm-milestone\' non e\' riconducibile a un valore boolean!\n");
                        }
                        // Controllo che le attività del progetto non abbiano subito modifiche non visualizzate dall'utente
                        /* TODO
                         * Controllo di concorrenza da implementare
                        pst = con.prepareStatement(GET_ACTIVITY);
                        pst.clearParameters();
                        pst.setInt(1, idProj);
                        pst.setInt(2, Integer.parseInt(paramsMilestone.get("pcm-id" + String.valueOf(i)))); 
                        rs = pst.executeQuery();
                        if(rs.next()) {
                            if ( (rs.getString("id").equals(activities.get(i).getId())) &&
                                 (rs.getString("nome").equals(activities.get(i).getNome())) &&
                                 (rs.getString("descrizione").equals(activities.get(i).getDescrizione())) &&
                                 (rs.getBoolean("milestone") (activities.get(i).isMilestone())) ) {
                                
                            }
                        }*/
                        pst = null;
                        pst = con.prepareStatement(UPDATE_ATTIVITA_FROM_PROGETTO);
                        pst.clearParameters();
                        pst.setString(1, paramsMilestone.get("pcm-nome" + String.valueOf(i)));
                        pst.setString(2, paramsMilestone.get("pcm-descrizione" + String.valueOf(i)));
                        pst.setBoolean(3, Boolean.parseBoolean(isMilestoneAsString));
                        pst.setInt(4, Integer.parseInt(paramsMilestone.get("pcm-id" + String.valueOf(i))));
                        pst.setInt(5, idProj);
                        pst.executeUpdate();
                        con.commit();
                    }
                }
            }
        } catch (NotFoundException nfe) {
            String msg = FOR_NAME + "Probabile problema nel puntamento alla HashMap dei parametri.\n";
            LOG.severe(msg); 
            throw new WebStorageException(msg + nfe.getMessage(), nfe);
        } catch (SQLException sqle) {
            String msg = FOR_NAME + "Tupla non aggiornata correttamente; problema nella query che aggiorna il progetto.\n";
            LOG.severe(msg); 
            throw new WebStorageException(msg + sqle.getMessage(), sqle);
        } catch (NumberFormatException nfe) {
            String msg = FOR_NAME + "Tupla non aggiornata correttamente; problema nella query che aggiorna il progetto.\n";
            LOG.severe(msg); 
            throw new WebStorageException(msg + nfe.getMessage(), nfe);
        } catch (Exception e) {
            String msg = FOR_NAME + "Tupla non aggiornata correttamente; problema nella query che aggiorna il progetto.\n";
            LOG.severe(msg); 
            throw new WebStorageException(msg + e.getMessage(), e);
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
    
    
    /* ********************************************************** *
     *                        Metodi di POL                       *
    /* ********************************************************** *
     *                   Metodi di INSERIMENTO                    *
     * ********************************************************** */    
    /**
     * <p>Metodo per fare un inserimento di un nuovo stato progetto.</p>
     * 
     * @param user - utente loggato
     * @param idProj - id del progetto sul quale attribuire lo status
     * @param idStatus - id dello status che si va ad inserire
     * @throws WebStorageException se si verifica un problema nel cast da String a Date, nell'esecuzione della query, nell'accesso al db o in qualche tipo di puntamento
     */
    public void insertStatus (PersonBean user,
                              int idProj, 
                              int idStatus)
                       throws WebStorageException {
        Connection con = null;
        PreparedStatement pst = null;
        try {
            con = pol_manager.getConnection();
            con.setAutoCommit(false);
            pst = con.prepareStatement(INSERT_STATUS);
            pst.clearParameters();
            int nextVal = getMax("avanzamentoprogetto") + 1;
            if (nextVal == idStatus) {
                pst.setInt(1, idStatus);
                pst.setDate(2, Utils.convert(Utils.convert(Utils.getCurrentDate())));
                pst.setDate(3, Utils.convert(getDefaultEndDate(idProj)));
                pst.setDate(4, Utils.convert(Utils.convert(Utils.getCurrentDate())));
                pst.setDate(5, Utils.convert(Utils.convert(Utils.getCurrentDate().getTime())));
                pst.setString(6, user.getCognome() + " " + user.getNome());
                pst.setInt(7, idProj);
                pst.executeUpdate();
                con.commit();
            } else {
                throw new WebStorageException(FOR_NAME + "un altro utente ha inserito un nuovo avanzamento progetto.\n");
            } 
        } catch (SQLException sqle) {
            String msg = FOR_NAME + "Oggetto RiskBean non valorizzato; problema nella query dell\'utente.\n";
            LOG.severe(msg); 
            throw new WebStorageException(msg + sqle.getMessage(), sqle);
        } catch (AttributoNonValorizzatoException anve) {
            String msg = FOR_NAME + "Oggetto PersonBean non valorizzato; problema nella query dell\'utente.\n";
            LOG.severe(msg); 
            throw new WebStorageException(msg + anve.getMessage(), anve);
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
     * <p>Metodo per fare un nuovo inserimento di una nuova attivit&agrave;.</p>
     * 
     * @param idProj - identificativo del progetto, al quale l'attivit&agrave; fa riferimento
     * @param userId - identificativo dell'utente loggato
     * @param valuesActivity - Vector contenente tutti i valori che l'utente inserisce per la nuova attivit&agrave;
     * @throws WebStorageException se si verifica un problema nel cast da String a Date, nell'esecuzione della query, nell'accesso al db o in qualche tipo di puntamento
     */
    @SuppressWarnings({ "null", "static-method" })
    public void insertActivity (int idProj,
                                int userId, 
                                Vector<String> valuesActivity) 
                         throws WebStorageException {
        Connection con = null;
        PreparedStatement pst = null;
        try {
            // Ottiene il progetto precaricato quando l'utente si &egrave; loggato corrispondente al progetto sul quale aggiungere un'attività
            con = pol_manager.getConnection();
            con.setAutoCommit(false);
            pst = con.prepareStatement(INSERT_ACTIVITY);
            pst.clearParameters();
            // 6 è il numero di date che ci sono in un'attività
            // 3 è l'indice di posizione della prima data
            try {
                int nextVal = getMax("attivita") + 1;
                Date[] valuesAsDate = new Date[6];
                for ( int i = 0; i < 6; i++ ) {
                    valuesAsDate[i] = DATA_FORMAT.parse(valuesActivity.get(3 + i));
                }
                pst.setInt(1, nextVal);
                pst.setString(2, valuesActivity.get(1));
                pst.setString(3, valuesActivity.get(2));
                pst.setDate(4, Utils.convert(valuesAsDate[0])); // non accetta una data java.util.Date, ma java.sql.Date
                pst.setDate(5, Utils.convert(valuesAsDate[1])); // non accetta una data java.util.Date, ma java.sql.Date
                pst.setDate(6, Utils.convert(valuesAsDate[2])); // non accetta una data java.util.Date, ma java.sql.Date
                pst.setDate(7, Utils.convert(valuesAsDate[3])); // non accetta una data java.util.Date, ma java.sql.Date
                pst.setDate(8, Utils.convert(valuesAsDate[4])); // non accetta una data java.util.Date, ma java.sql.Date
                pst.setDate(9, Utils.convert(valuesAsDate[5])); // non accetta una data java.util.Date, ma java.sql.Date
                pst.setInt(10, Integer.parseInt(valuesActivity.get(9)));
                pst.setInt(11, Integer.parseInt(valuesActivity.get(10)));
                pst.setInt(12, Integer.parseInt(valuesActivity.get(11)));
                pst.setString(13, valuesActivity.get(12));
                pst.setBoolean(14, Boolean.parseBoolean(valuesActivity.get(13)));
                pst.setInt(15, Integer.parseInt(valuesActivity.get(14)));
                pst.setInt(16, Integer.parseInt(valuesActivity.get(15)));
                pst.setInt(17, Integer.parseInt(valuesActivity.get(16)));
                pst.setInt(18, Integer.parseInt(valuesActivity.get(17)));
                pst.setInt(19, Integer.parseInt(valuesActivity.get(18)));
                pst.setInt(20, Integer.parseInt(valuesActivity.get(19)));
                pst.executeUpdate();
                con.commit();
            } catch (ParseException pe) {
                String msg = FOR_NAME + "Si e\' verificato un problema nella conversione di date.\n" + pe.getMessage();
                LOG.severe(msg);
                throw new WebStorageException(msg, pe);
            } catch (NumberFormatException nfe) {
                String msg = FOR_NAME + "Si e\' verificato un problema nella conversione di interi.\n" + nfe.getMessage();
                LOG.severe(msg);
                throw new WebStorageException(msg, nfe);
            } catch (ClassCastException cce) {
                String msg = FOR_NAME + "Si e\' verificato un problema nella conversione di tipo.\n" + cce.getMessage();
                LOG.severe(msg);
                throw new WebStorageException(msg, cce);
            } catch (ArrayIndexOutOfBoundsException aiobe) {
                String msg = FOR_NAME + "Si e\' verificato un problema nello scorrimento di liste.\n" + aiobe.getMessage();
                LOG.severe(msg);
                throw new WebStorageException(msg, aiobe);
            } catch (NullPointerException npe) {
                String msg = FOR_NAME + "Si e\' verificato un problema in un puntamento a null.\n" + npe.getMessage();
                LOG.severe(msg);
                throw new WebStorageException(msg, npe);
            } catch (RuntimeException re) { // A scopo didattico
                String msg = FOR_NAME + "Si e\' verificato un problema a tempo di esecuzione.\n" + re.getMessage();
                LOG.severe(msg);
                throw new WebStorageException(msg, re);
            } catch (Exception e) {
                String msg = FOR_NAME + "Si e\' verificato un problema.\n" + e.getMessage();
                LOG.severe(msg);
                throw new WebStorageException(msg, e);
            } catch (Throwable t) { // A scopo didattico
                String msg = FOR_NAME + "Si e\' verificato un qualcosa.\n" + t.getMessage();
                LOG.severe(msg);
                throw new WebStorageException(msg, t);
            }
        } catch (SQLException sqle) {
            String msg = FOR_NAME + "Oggetto RiskBean non valorizzato; problema nella query dell\'utente.\n";
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
     * <p>Metodo per fare un nuovo inserimento di un nuovo rischio relativo al progetto.</p>
     * 
     * @param idProj  - identificativo del progetto, al quale l'attivit&agrave; fa riferimento
     * @param userId - identificativo dell'utente loggato
     * @param valuesRisk - vector contenente i valori inseriti dall'utente per inserimento nuovo rischio
     * @throws WebStorageException se si verifica un problema nell'esecuzione della query, nell'accesso al db o in qualche tipo di puntamento
     */
    public void insertRisk (int idProj,
                            int userId, 
                            Vector<String> valuesRisk)
                     throws WebStorageException {
        Connection con = null;
        PreparedStatement pst = null;
        try {
            // Ottiene il progetto precaricato quando l'utente si è loggato corrispondente al progetto sul quale aggiungere un'attività
            Integer key = new Integer(idProj);
            con = pol_manager.getConnection();
            con.setAutoCommit(false);
            pst = con.prepareStatement(INSERT_RISK);
            pst.clearParameters();
            pst.setInt(1, Integer.parseInt(valuesRisk.get(0)));
            pst.setString(2, valuesRisk.get(1));
            pst.setString(3, valuesRisk.get(2));
            pst.setString(4, valuesRisk.get(3));
            pst.setString(5, valuesRisk.get(4));
            pst.setString(6, valuesRisk.get(5));
            pst.setInt(7, key);
            pst.executeUpdate();
            con.commit();
        } catch (SQLException sqle) {
            String msg = FOR_NAME + "Oggetto RiskBean non valorizzato; problema nella query dell\'utente.\n";
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
     * <p>Metodo per fare un nuovo inserimento di una nuova competenza relativa al progetto.</p>
     * 
     * @param idProj  - identificativo del progetto, al quale l'attivit&agrave; fa riferimento
     * @param userId - identificativo dell'utente loggato
     * @param valuesSkill - vector contenente i valori inseriti dall'utente per inserimento nuovo rischio
     * @throws WebStorageException se si verifica un problema nell'esecuzione della query, nell'accesso al db o in qualche tipo di puntamento
     */
    public void insertSkill (int idProj,
                            int userId, 
                            Vector<String> valuesSkill)
                     throws WebStorageException {
        Connection con = null;
        PreparedStatement pst = null;
        try {
            // Ottiene il progetto precaricato quando l'utente si è loggato corrispondente al progetto sul quale aggiungere un'attività
            Integer key = new Integer(idProj);
            con = pol_manager.getConnection();
            con.setAutoCommit(false);
            pst = con.prepareStatement(INSERT_SKILL);
            pst.clearParameters();
            pst.setInt(1, Integer.parseInt(valuesSkill.get(0)));
            pst.setString(2, valuesSkill.get(1));
            pst.setString(3, valuesSkill.get(2));
            pst.setBoolean(4, Boolean.parseBoolean(valuesSkill.get(3)));
            pst.setInt(5, key);
            pst.setInt(6, Integer.parseInt(valuesSkill.get(5)));
            pst.executeUpdate();
            con.commit();
        } catch (SQLException sqle) {
            String msg = FOR_NAME + "Oggetto RiskBean non valorizzato; problema nella query dell\'utente.\n";
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
     * <p>Metodo che data in input una query e l'id di uno stato 
     * estrae i dati richiesti e popola un CodeBean, che ritorna al chiamante.</p>
     * 
     * @param query - Query che deve eseguire il metodo
     * @param idStatus - Id dello status del quale estrarre i dati
     * @return CodeBean - CodeBean contenente lo stato corrispondente alla query passata
     * @throws WebStorageException se si verifica un problema nell'esecuzione della query, nell'accesso al db o in qualche tipo di puntamento
     */
    private CodeBean retrieve (String query,
                               int idStatus) 
                        throws WebStorageException {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pst = null;
        CodeBean stato = new CodeBean();
        try {
            con = pol_manager.getConnection();
            pst = con.prepareStatement(query);
            pst.clearParameters();
            pst.setInt(1, idStatus);
            rs = pst.executeQuery();
            if (rs.next()) {
                BeanUtil.populate(stato, rs);
            }
        } catch (SQLException sqle) {
            String msg = FOR_NAME + "Oggetto CodeBean non valorizzato; problema nella query di stato.\n";
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
        return stato;
    }
    
    
    /**
     * 
     * @param idProj 
     * @return 
     * @throws WebStorageException 
     */
    private Date getDefaultEndDate (int idProj) 
                             throws WebStorageException {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        GregorianCalendar endDate = null;
        try {
            con = pol_manager.getConnection();
            pst = con.prepareStatement(GET_PROJECT_PERIOD);
            pst.clearParameters();
            pst.setInt(1, idProj);
            rs = pst.executeQuery();
            if (rs.next()) {
                if (rs.getFloat(1) == 0.25) {
                    endDate = Utils.getDate(7, 0, 0);
                } else {
                    endDate = Utils.getDate(0, (int) rs.getFloat(1), 0);
                }
            }
        } catch (SQLException sqle) {
            String msg = FOR_NAME + "Oggetto endDate non valorizzato; problema nella query di stato.\n";
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
        return Utils.convert(endDate);
    }
    /* ********************************************************** *
     *                        Metodi di QOL                       *
     *                          (ValDid)                          *
     * ********************************************************** */
    /**
     * <p>Restituisce un Vector di CourseBean di tipo "AD Semplice".</p>
     *
     * @return <code>Vector&lt;CourseBean&gt;</code> - lista di CourseBean rappresentanti ciascuno un'AD Semplice
     * @throws WebStorageException se si verifica un problema nell'esecuzione della query, nell'accesso al db o in qualche tipo di puntamento
     */
    @SuppressWarnings({ "null", "static-method" })
    public Vector<CourseBean> getAD(String query)
                             throws WebStorageException {
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement pst = null;
        CourseBean course = null;
        Vector<CourseBean> lista = new Vector<CourseBean>();
        try {
            con = alma_manager.getConnection();
            pst = con.prepareStatement(query);
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
    
}