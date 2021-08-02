/*
 *   Alma on Line: 
 *   Applicazione WEB per la gestione dei progetti on line (POL)
 *   coerentemente con le linee-guida del project management,
 *   e per la visualizzazione delle schede di indagine 
 *   su popolazione dell'ateneo.
 *   
 *   Copyright (C) 2018-2020 Giovanroberto Torre<br />
 *   Alma on Line (aol), Projects on Line (pol);
 *   web applications to publish, and manage, projects
 *   according to the Project Management paradigm (PM).
 *   Copyright (C) renewed 2020 Giovanroberto Torre, 
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
import java.sql.Time;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import it.alma.bean.ActivityBean;
import it.alma.bean.BeanUtil;
import it.alma.bean.CodeBean;
import it.alma.bean.DepartmentBean;
import it.alma.bean.FileDocBean;
import it.alma.bean.IndicatorBean;
import it.alma.bean.ItemBean;
import it.alma.bean.MeasurementBean;
import it.alma.bean.MonitorBean;
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
     * <p>Connessione db postgres di pol.</p>
     */
    protected static DataSource pol_manager = null;
    /**
     * <p>Recupera da Main la stringa opportuna per il puntamento del DataSource.</p>
     */
    private static String contextDbName = Main.getDbName();
    
    
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
        if (pol_manager == null) {
            try {
                pol_manager = (DataSource) ((Context) new InitialContext()).lookup(contextDbName);
                if (pol_manager == null)
                    throw new WebStorageException(FOR_NAME + "La risorsa " + contextDbName + "non e\' disponibile. Verificare configurazione e collegamenti.\n");
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
            throw new WebStorageException(FOR_NAME + sqle.getMessage(), sqle);
        } finally {
            try {
                con.close();
            } catch (NullPointerException npe) {
                String msg = "Connessione al database in stato inconsistente!\nAttenzione: la connessione vale " + con + "\n";
                LOG.severe(msg);
                throw new WebStorageException(FOR_NAME + msg + npe.getMessage(), npe);
            } catch (SQLException sqle) {
                throw new WebStorageException(FOR_NAME + sqle.getMessage(), sqle);
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
    @SuppressWarnings({ "static-method", "null" })
    public int getMax(String table) 
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
    
    
    /**
     * <p>Restituisce 
     * <ul>
     * <li>il minimo valore del contatore identificativo di una
     * tabella il cui nome viene passato come argomento</li> 
     * <li>oppure zero se nella tabella non sono presenti record.</li>
     * </ul></p>
     * 
     * @param table nome della tabella di cui si vuol recuperare il min(id)
     * @return <code>int</code> - un intero che rappresenta il minimo valore trovato, oppure zero se non sono stati trovati valori
     * @throws WebStorageException se si verifica un problema nella query o in qualche tipo di puntamento
     */
    @SuppressWarnings({ "static-method", "null" })
    public int getMin(String table) 
               throws WebStorageException {
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement pst = null;
        try {
            int count = 0;
            String query = SELECT_MIN_ID + table;
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
    
    
    /**
     * <p>Restituisce il primo valore trovato data una query in input</p>
     * 
     * @param query da eseguire
     * @return <code>String</code> - stringa restituita
     * @throws WebStorageException se si verifica un problema nella query o in qualche tipo di puntamento
     */
    @SuppressWarnings({ "null", "static-method" })
    public String get(String query) 
               throws WebStorageException {
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement pst = null;
        String value = null;
        try {
            con = pol_manager.getConnection();
            pst = con.prepareStatement(query);
            pst.clearParameters();
            rs = pst.executeQuery();
            if (rs.next()) {
                value = rs.getString(1);
            }
            return value;
        } catch (SQLException sqle) {
            String msg = FOR_NAME + "Impossibile recuperare un valore.\n";
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
     * <p>Restituisce un CodeBean contenente la password criptata e il seme
     * per poter verificare le credenziali inserite dall'utente.</p>
     * 
     * @param username   username della persona che ha richiesto il login
     * @return <code>CodeBean</code> - CodeBean contenente la password criptata e il seme
     * @throws WebStorageException se si verifica un problema nell'esecuzione della query, nell'accesso al db o in qualche tipo di puntamento
     */
    @SuppressWarnings({ "null", "static-method" })
    public CodeBean getEncryptedPassword(String username) 
                                  throws WebStorageException {
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement pst = null;
        CodeBean password = null;
        int nextInt = 0;
        try {
            con = pol_manager.getConnection();
            pst = con.prepareStatement(GET_ENCRYPTEDPASSWORD);
            pst.clearParameters();
            pst.setString(++nextInt, username);
            rs = pst.executeQuery();
            if (rs.next()) {
                password = new CodeBean();
                BeanUtil.populate(password, rs);
            }
            return password;
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
     * <p>Restituisce un PersonBean rappresentante un utente loggato.</p>
     * 
     * @param username  username della persona che ha eseguito il login
     * @param password  password della persona che ha eseguito il login
     * @return <code>PersonBean</code> - PersonBean rappresentante l'utente loggato
     * @throws WebStorageException se si verifica un problema nell'esecuzione della query, nell'accesso al db o in qualche tipo di puntamento
     * @throws it.alma.exception.AttributoNonValorizzatoException  eccezione che viene sollevata se questo oggetto viene usato e l'id della persona non &egrave; stato valorizzato (&egrave; un dato obbligatorio) 
     */
    @SuppressWarnings({ "null", "static-method" })
    public PersonBean getUser(String username,
                              String password)
                       throws WebStorageException, AttributoNonValorizzatoException {
        ResultSet rs, rs1 = null;
        Connection con = null;
        PreparedStatement pst = null;
        PersonBean usr = null;
        int nextInt = 0;
        Vector<CodeBean> vRuoli = new Vector<CodeBean>();
        try {
            con = pol_manager.getConnection();
            pst = con.prepareStatement(GET_USR);
            pst.clearParameters();
            pst.setString(++nextInt, username);
            pst.setString(++nextInt, password);
            pst.setString(++nextInt, password);
            rs = pst.executeQuery();
            if (rs.next()) {
                usr = new PersonBean();
                BeanUtil.populate(usr, rs);
            }
            pst = con.prepareStatement(GET_RUOLIPERSONA);
            pst.clearParameters();
            pst.setInt(1, usr.getId());
            rs1 = pst.executeQuery();
            while(rs1.next()) {
                CodeBean ruolo = new CodeBean();
                BeanUtil.populate(ruolo, rs1);
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
     * <p>Restituisce un PersonBean dato l'id di un'utenza associata
     * alla persona restituita.</p>
     * <p><code><strong>Nota sull'implementazione:</strong>
     * per permettere all'applicazione la libert&agrave; di associare
     * eventualmente pi&uacute; utenze alla stessa persona, la relazione
     * &egrave; stata progettata non con una cardinalit&agrave; uno a uno 
     * ma con una cardinalit&agrave; uno a molti 
     * (pi&ugrave; utenze possono corrispondere a una data persona mentre
     * a un'utenza &egrave; associata una sola persona.
     * In altri termini, viene definita la funzione f(u) definita
     * dal dominio = {insieme delle utenze} 
     * al codominio = {insieme delle persone} &ndash; 
     * una funzione ovviamente non iniettiva, e quindi non bijettiva).<br />
     * Se la relazione tra utente e persona non fosse stata disegnata cos&iacute;,
     * se cio&egrave; fosse stata fissata in una cardinalit&agrave; uno a uno,
     * non sarebbe stato necessario avere una relazione di appoggio 
     * tra utente e persona, ma sarebbe stato sufficiente inserire 
     * l'identificativo della persona come chiave esterna della tabella
     * dell'utente (se non, addirittura, l'identificativo dell'utente come
     * attributo della persona).<br /> 
     * Tuttavia, come anzidetto, l'applicazione consente 
     * in potenza l'esistenza di pi&uacute; utenze per una sola persona; 
     * si accorda con questi gradi di libert&agrave;
     * la mancanza di vincoli sulla relazione <strong>identita</strong>. 
     * Tuttavia, se &egrave; vero che a una data persona possono corrispondere 
     * potenzialmente pi&uacute; utenze, a una sola utenza dovrebbe corrispondere 
     * una e una sola persona (altrimenti, peraltro, quella tra 
     * utenza e persona non potrebbe essere stata descritta come una funzione); 
     * di qui il senso dell'implementazione, 
     * che non effettua un ciclo sul resultset ma una sua lettura
     * puntuale.</code></p>
     * 
     * @param idUsr identificativo dell'utenza della persona
     * @return <code>PersonBean</code> - PersonBean rappresentante l'utente loggato
     * @throws WebStorageException se si verifica un problema nell'esecuzione della query, nel recupero di un attributo obbligatorio, nell'accesso al db o in qualche tipo di puntamento 
     */
    @SuppressWarnings({ "null", "static-method" })
    public PersonBean getUser(int idUsr)
                       throws WebStorageException {
        ResultSet rs, rs1 = null;
        Connection con = null;
        PreparedStatement pst = null;
        PersonBean usr = null;
        int nextInt = 0;
        Vector<CodeBean> vRuoli = new Vector<CodeBean>();
        try {
            con = pol_manager.getConnection();
            pst = con.prepareStatement(GET_PERSON_BY_USER_ID);
            pst.clearParameters();
            pst.setInt(++nextInt, idUsr);
            rs = pst.executeQuery();
            if (rs.next()) {
                usr = new PersonBean();
                BeanUtil.populate(usr, rs);
            }
            pst = con.prepareStatement(GET_RUOLIPERSONA);
            pst.clearParameters();
            pst.setInt(1, usr.getId());
            rs1 = pst.executeQuery();
            while(rs1.next()) {
                CodeBean ruolo = new CodeBean();
                BeanUtil.populate(ruolo, rs1);
                vRuoli.add(ruolo);
            }
            usr.setRuoli(vRuoli);
            return usr;
        } catch (SQLException sqle) {
            String msg = FOR_NAME + "Oggetto PersonBean non valorizzato; problema nella query della persona.\n";
            LOG.severe(msg); 
            throw new WebStorageException(msg + sqle.getMessage(), sqle);
        } catch (AttributoNonValorizzatoException sqle) {
            String msg = FOR_NAME + "Oggetto id della persona non valorizzato; problema nella query della persona.\n";
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
    * <p>Restituisce un PersonBean dato l'id della persona.</p>
    * 
    * @param id identificativo della persona
    * @return <code>PersonBean</code> - PersonBean rappresentante la persona cercata, di cui e' stato passato l'identificativo
    * @throws WebStorageException se si verifica un problema nell'esecuzione della query, nel recupero di un attributo obbligatorio, nell'accesso al db o in qualche tipo di puntamento 
    */ 
    @SuppressWarnings({ "null", "static-method" })
    public PersonBean getPerson(int id)
                         throws WebStorageException {
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement pst = null;
        PersonBean usr = null;
        int nextInt = 0;
        try {
            con = pol_manager.getConnection();
            pst = con.prepareStatement(GET_PERSON_BY_PERSON_ID);
            pst.clearParameters();
            pst.setInt(++nextInt, id);
            rs = pst.executeQuery();
            if (rs.next()) {
                usr = new PersonBean();
                BeanUtil.populate(usr, rs);
            }
            return usr;
        } catch (SQLException sqle) {
            String msg = FOR_NAME + "Oggetto PersonBean non valorizzato; problema nella query della persona.\n";
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
     * <p>Restituisce un CodeBean contenente l'id dell'utenza, 
     * dato l'id della persona a cui l'utenza stessa &egrave; associata.</p>
     * 
     * @param idPer identificativo della persona
     * @return <code>CodeBean</code> - CodeBean rappresentante l'utente cercato
     * @throws WebStorageException se si verifica un problema nell'esecuzione della query, nel recupero di un attributo obbligatorio, nell'accesso al db o in qualche tipo di puntamento 
     */ 
     @SuppressWarnings({ "null", "static-method" })
     public CodeBean getUserByPersonId(int idPer)
                                throws WebStorageException {
         ResultSet rs = null;
         Connection con = null;
         PreparedStatement pst = null;
         CodeBean userBean = null;
         int nextInt = 0;
         try {
             con = pol_manager.getConnection();
             pst = con.prepareStatement(GET_USER_BY_PERSON_ID);
             pst.clearParameters();
             pst.setInt(++nextInt, idPer);
             rs = pst.executeQuery();
             if (rs.next()) {
                 userBean = new CodeBean();
                 BeanUtil.populate(userBean, rs);
             }
             return userBean;
         } catch (SQLException sqle) {
             String msg = FOR_NAME + "Oggetto UserBean non valorizzato; problema nella query della persona.\n";
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
     * <p>Estrae le competenze assegnate a un utente di dato identificativo
     * nel contesto di un progetto di dato identificativo.</p>
     * 
     * @param projId identificativo del progetto cui l'utente e' collegato
     * @param idUsr identificativo dell'utente di cui si vogliono recuperare le competenze
     * @return <code>Vector&lt;SkillBean&gt;</code> - lista di competenze
     * @throws WebStorageException se si verifica un problema nell'esecuzione della query o in qualche puntamento 
     */
    @SuppressWarnings({ "null", "static-method" })
    public Vector<SkillBean> getSkillsByPersonAndProject(int projId, 
                                                         int idUsr)
                                                  throws WebStorageException {
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement pst = null;
        Vector<SkillBean> vSkill = new Vector<SkillBean>();
        try {
            con = pol_manager.getConnection();
            pst = con.prepareStatement(GET_SKILLS_BY_PERSON);
            pst.clearParameters();
            pst.setInt(1, projId);
            pst.setInt(2, idUsr);
            rs = pst.executeQuery();
            while(rs.next()) {
                SkillBean skill = new SkillBean();
                BeanUtil.populate(skill, rs);
                vSkill.add(skill);
            }
            return vSkill;
        } catch (SQLException sqle) {
            String msg = FOR_NAME + "Oggetto PersonBean non valorizzato; problema nella query della persona.\n";
            LOG.severe(msg); 
            throw new WebStorageException(msg + sqle.getMessage(), sqle);
        } catch (ClassCastException cce) {
            String msg = FOR_NAME + "Problema in una conversione di tipi.\n";
            LOG.severe(msg); 
            throw new WebStorageException(msg + cce.getMessage(), cce);
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
     * <p>Restituisce un ArrayList&lt;PersonBean&gt; contenente tutte
     * le persone che appartengono ad un certo gruppo.</p>
     * <p>Il gruppo si riferisce al dipartimento di appartenenza dell'utente
     * 'admin' che ha effettuato la richiesta, e che viene passato come
     * parametro.<br>
     * Se l'utente loggato &egrave; il PMO di ateneo, il metodo restituisce
     * la lista di tutte le persone inserite nel sistema.</p>
     * 
     * @param admin PersonBean della persona che ha richiesto la lista di persone
     * @return ArrayList&lt;PersonBean&gt; - ArrayList contenente la lista di persone appartenenti al gruppo di 'admin' 
     * @throws WebStorageException se si verifica un problema SQL o in qualsiasi puntamento
     */
    @SuppressWarnings({ "null", "static-method" })
    public ArrayList<PersonBean> getPersonByGrp(PersonBean admin) 
                                         throws WebStorageException{
        ResultSet rs, rs1 = null;
        Connection con = null;
        PreparedStatement pst, pst1 = null;
        int nextInt = 0;
        int nextInt1 = 0;
        PersonBean user = null;
        boolean isPmoAte = false;
        ArrayList<PersonBean> vUser = new ArrayList<PersonBean>();
        try {
            con = pol_manager.getConnection();
            pst = con.prepareStatement(GET_GROUP);
            pst.clearParameters();
            pst.setInt(++nextInt, admin.getId());
            rs = pst.executeQuery();
            while (rs.next() && !isPmoAte) {
                nextInt1 = 0;
                String depart = rs.getString("nome");
                pst1 = con.prepareStatement(GET_BELONGS_PERSON);
                pst1.clearParameters();
                pst1.setString(++nextInt1, "%" + depart.substring(depart.lastIndexOf("-"), depart.length()));
                pst1.setInt(++nextInt1, 0);
                if (admin.isPmoAteneo()) {
                    nextInt1 = 0;
                    pst1.setString(++nextInt1, Utils.VOID_STRING);
                    pst1.setInt(++nextInt1, -1);
                    isPmoAte = true;
                }
                rs1 = pst1.executeQuery();
                while (rs1.next()) {
                    user = new PersonBean();
                    BeanUtil.populate(user, rs1);
                    vUser.add(user);
                }
            }
            return vUser;
        } catch (AttributoNonValorizzatoException anve) {
            String msg = FOR_NAME + "Oggetto PersonBean non valorizzato; problema nella query di estrazione utenti appartenenti ad un gruppo.\n";
            LOG.severe(msg); 
            throw new WebStorageException(msg + anve.getMessage(), anve);
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
     * <p>Restituisce un ArrayList&lt;PersonBean&gt; contenente tutti
     * gli utenti che appartengono ad un certo gruppo.</p>
     * <p>Il gruppo si riferisce al dipartimento di appartenenza.
     * Se l'utente loggato &egrave; il PMO di ateneo, il metodo restituisce
     * la lista di tutti gli utenti inseriti nel sistema.</p>
     * 
     * @param admin PersonBean della persona che ha richiesto la lista di utenti
     * @return ArrayList&lt;PersonBean&gt; - ArrayList contenente la lista di utenti 
     * @throws WebStorageException se si verifica un problema SQL o in qualsiasi puntamento
     */
    @SuppressWarnings({ "null", "static-method" })
    public ArrayList<PersonBean> getUsrByGrp(PersonBean admin) 
                                      throws WebStorageException{
        ResultSet rs, rs1 = null;
        Connection con = null;
        PreparedStatement pst, pst1 = null;
        int nextInt = 0;
        int nextInt1 = 0;
        PersonBean user = null;
        boolean isPmoAte = false;
        ArrayList<PersonBean> vUser = new ArrayList<PersonBean>();
        try {
            con = pol_manager.getConnection();
            pst = con.prepareStatement(GET_GROUP);
            pst.clearParameters();
            pst.setInt(++nextInt, admin.getId());
            rs = pst.executeQuery();
            while (rs.next() && !isPmoAte) {
                nextInt1 = 0;
                String depart = rs.getString("nome");
                pst1 = con.prepareStatement(GET_BELONGS_USR);
                pst1.clearParameters();
                pst1.setString(++nextInt1, "%" + depart.substring(depart.lastIndexOf("-"), depart.length()));
                pst1.setInt(++nextInt1, 0);
                if (admin.isPmoAteneo()) {
                    nextInt1 = 0;
                    pst1.setString(++nextInt1, Utils.VOID_STRING);
                    pst1.setInt(++nextInt1, -1);
                    isPmoAte = true;
                }
                rs1 = pst1.executeQuery();
                while (rs1.next()) {
                    user = new PersonBean();
                    BeanUtil.populate(user, rs1);
                    vUser.add(user);
                }
            }
            return vUser;
        } catch (AttributoNonValorizzatoException anve) {
            String msg = FOR_NAME + "Oggetto PersonBean non valorizzato; problema nella query di estrazione utenti appartenenti ad un gruppo.\n";
            LOG.severe(msg); 
            throw new WebStorageException(msg + anve.getMessage(), anve);
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
     * <p>Restituisce un Vector&lt;ItemBean&gt; contenente ciascuno
     * i riferimenti al progetto, all'utente e ai suoi ruoli all'interno
     * del progetto stesso.</p>
     * 
     * @param userId id dell'utente loggato
     * @return Vector&lt;ItemBean&gt; - lista dei progetti con dipartimento e ruolo
     * @throws WebStorageException se si verifica un problema SQL o in qualsiasi puntamento
     */
    @SuppressWarnings({ "null", "static-method" })
    public Vector<ItemBean> getProjectsByRole(int userId)
                                       throws WebStorageException {
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement pst = null;
        int nextInt = 0;
        ItemBean project = null;
        Vector<ItemBean> projects = new Vector<ItemBean>();
        try {
            con = pol_manager.getConnection();
            pst = con.prepareStatement(GET_PROJECTS_BY_ROLE);
            pst.clearParameters();
            pst.setInt(++nextInt, userId);
            rs = pst.executeQuery();
            while (rs.next()) {
              project = new ItemBean();
              BeanUtil.populate(project, rs);
              projects.add(project);
            }
            return projects;
        } catch (SQLException sqle) {
            String msg = FOR_NAME + "Oggetto PersonBean non valorizzato; problema nella query.\n";
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
     * <p>Restituisce un Vector&lt;ItemBean&gt; contenente ciascuno
     * i riferimenti al progetto, all'utente e ai suoi ruoli all'interno
     * del progetto stesso.</p>
     * 
     * @param userId id dell'utente loggato
     * @return Vector&lt;ItemBean&gt; - lista dei progetti con dipartimento e ruolo
     * @throws WebStorageException se si verifica un problema SQL o in qualsiasi puntamento
     */
    @SuppressWarnings({ "null", "static-method" })
    public Vector<ItemBean> getProjectsBySkills(int userId)
                                        throws WebStorageException {
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement pst = null;
        int nextInt = 0;
        ItemBean project = null;
        Vector<ItemBean> projects = new Vector<ItemBean>();
        try {
            con = pol_manager.getConnection();
            pst = con.prepareStatement(GET_PROJECTS_BY_SKILLS);
            pst.clearParameters();
            pst.setInt(++nextInt, userId);
            rs = pst.executeQuery();
            while (rs.next()) {
              project = new ItemBean();
              BeanUtil.populate(project, rs);
              projects.add(project);
            }
            return projects;
        } catch (SQLException sqle) {
            String msg = FOR_NAME + "Oggetto PersonBean non valorizzato; problema nella query.\n";
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
     * un utente del dipartimento collegato ad un suo progetto, 
     * il cui identificativo viene passato come parametro.</p>
     * 
     * @param projectId     identificativo del progetto di cui si vogliono recuperare le persone collegate
     * @return <code>Vector&lt;PersonBean&gt;</code> - elenco di PersonBean rappresentante le persone del dipartimento a cui il progetto e' collegato
     * @throws WebStorageException se si verifica un problema nell'esecuzione della query, nell'accesso al db o in qualche tipo di puntamento
     * @throws it.alma.exception.AttributoNonValorizzatoException  eccezione che viene sollevata se questo oggetto viene usato e l'id della persona non &egrave; stato valorizzato (&egrave; un dato obbligatorio) 
     */
    @SuppressWarnings({ "null", "static-method" })
    public Vector<PersonBean> getPeopleByBelonging(int projectId)
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
     * <p>Restituisce un Vector di ItemBean, ciascuno rappresentante 
     * un utente del progetto con il ruolo rivestito all'interno del 
     * progetto stesso, il cui identificativo viene passato come parametro.</p>
     * 
     * @param projectId     identificativo del progetto di cui si vogliono recuperare le persone collegate e relativi ruoli
     * @return <code>Vector&lt;ItemBean&gt;</code> - elenco di ItemBean contenenti ciascuno una miscela di dati provenienti dalla persona e dal relativo ruolo
     * @throws WebStorageException se si verifica un problema nell'esecuzione della query, nell'accesso al db o in qualche tipo di puntamento 
     */
    @SuppressWarnings({ "null", "static-method" })
    public ArrayList<ItemBean> getPeople(int projectId)
                                  throws WebStorageException {
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement pst = null;
        ItemBean role = null;
        ArrayList<ItemBean> roles = new ArrayList<ItemBean>();
        try {
            con = pol_manager.getConnection();
            pst = con.prepareStatement(GET_PEOPLE_BY_PROJECT);
            pst.clearParameters();
            pst.setInt(1, projectId);
            rs = pst.executeQuery();
            // Recupera ogni persona con relativo ruolo nel progetto di dato id
            while (rs.next()) {
                role = new ItemBean();
                BeanUtil.populate(role, rs);
                roles.add(role);
            }
            return roles;
        } catch (SQLException sqle) {
            String msg = FOR_NAME + "Oggetto ItemBean non valorizzato; problema nella query che recupera quelli che lavorano su un progetto.\n";
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
     * @param getAll flag che definisce se si devono recuperare tutti i progetti (true), oppure solo un sottoinsieme dei progetti che possono essere modificati (false)
     * @return <code>Vector&lt;ProjectBean&gt;</code> - ProjectBean rappresentante i progetti dell'utente loggato
     * @throws WebStorageException se si verifica un problema nell'esecuzione della query, nell'accesso al db o in qualche tipo di puntamento
     */
    @SuppressWarnings({ "null", "static-method" })
    public Vector<ProjectBean> getProjects(int userId, 
                                           boolean getAll)
    							    throws WebStorageException {
    	ResultSet rs, rs1 = null;
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
                pst.setInt(2, 0);
                rs1 = pst.executeQuery();
                if (rs1.next()) {
                    dipart = new DepartmentBean();
                    BeanUtil.populate(dipart, rs1);
                    project.setDipart(dipart);
                }
                rs1 = null;
                //Recupera statoprogetto del progetto
                idStatoProgetto = project.getIdStatoProgetto();
                pst = con.prepareStatement(GET_STATOPROGETTO);
                pst.clearParameters();
                pst.setInt(1, idStatoProgetto);
                rs1 = pst.executeQuery();
                if (rs1.next()) {
                	statoProgetto = new CodeBean();
                	BeanUtil.populate(statoProgetto, rs1);
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
        ResultSet rs, rs1 = null;
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
                pst.setInt(2, 0);
                rs1 = pst.executeQuery();
                if (rs1.next()) {
                    dipart = new DepartmentBean();
                    BeanUtil.populate(dipart, rs1);
                    project.setDipart(dipart);
                }
                rs1 = null;
                //Recupera statoprogetto del progetto
                idStatoProgetto = project.getIdStatoProgetto();
                pst = con.prepareStatement(GET_STATOPROGETTO);
                pst.clearParameters();
                pst.setInt(1, idStatoProgetto);
                rs1 = pst.executeQuery();
                if (rs1.next()) {
                    statoProgetto = new CodeBean();
                    BeanUtil.populate(statoProgetto, rs1);
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
     * <p>Restituisce una mappa sincronizzata contenente 
     * i progetti visibili dall'utente loggato, indicizzati 
     * per identificativo del dipartimento.<br />
     * L'identificativo del dipartimento &egrave incapsulato 
     * in un oggetto Wrapper di tipo primitivo, a causa dell'implementazione
     * delle mappe di Java, che accettano solo oggetti come chiavi.</p>
     * 
     * @param userId identificativo della persona di cui si vogliono recuperare i progetti
     * @return <code>Vector&lt;ProjectBean&gt;</code> - ProjectBean rappresentante i progetti dell'utente loggato
     * @throws WebStorageException se si verifica un problema nell'esecuzione della query, nell'accesso al db o in qualche tipo di puntamento
     */
    @SuppressWarnings({ "null", "static-method" })
    public ConcurrentHashMap<Integer, Vector<ProjectBean>> getProjectsByDepart(int userId)
                                                                        throws WebStorageException {
        ResultSet rs, rs1, rs2 = null;
        Connection con = null;
        PreparedStatement pst = null;
        ProjectBean project = null;
        DepartmentBean dipart = null;
        CodeBean statoProgetto = null;
        int idDipart = -1;
        int idStatoProgetto = -1;
        Integer key = null;
        Vector<ProjectBean> v = null;
        ConcurrentHashMap<Integer, Vector<ProjectBean>> projects = new ConcurrentHashMap<Integer, Vector<ProjectBean>>();
        try {
            con = pol_manager.getConnection();
            pst = con.prepareStatement(GET_DIPART);
            pst.clearParameters();
            pst.setInt(1, -1);
            pst.setInt(2, -1);            
            rs = pst.executeQuery();
            while (rs.next()) {
                dipart = new DepartmentBean();
                v = new Vector<ProjectBean>();
                BeanUtil.populate(dipart, rs);
                idDipart = dipart.getId();
                key = new Integer(idDipart);
                // Recupera progetti del dipartimento
                pst = con.prepareStatement(GET_PROJECTS_BY_DEPART);
                pst.clearParameters();
                pst.setInt(1, userId);
                pst.setInt(2, idDipart);
                rs1 = pst.executeQuery();
                while (rs1.next()) {
                    project = new ProjectBean();
                    BeanUtil.populate(project, rs1);
                    // Recupera statoprogetto del progetto
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
                    v.add(project);
                }
                // Aggiunge la lista progetti del dipartimento e la chiave id dipartimento
                projects.put(key, v);
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
     * <p>Restituisce una mappa con insertion order contenente 
     * i progetti visibili dall'utente loggato, indicizzati 
     * per identificativo del dipartimento e aventi data di fine
     * compresa tra l'anno passato come parametro ed i due anni
     * successivi (per un totale di un triennio).<br />
     * Inoltre, ulteriori filtri possono essere applicati in base 
     * alla data anticipata di scadenza (campo 'meseriferimento') ed
     * allo stato del progetto (progetti in stato ELIMINATO vengono
     * scartati, progetti in stato IN PROGRESS vengono mostrati indipendentemente
     * dalle date).<br />
     * L'identificativo del dipartimento &egrave incapsulato 
     * in un oggetto Wrapper di tipo primitivo, a causa dell'implementazione
     * delle mappe di Java, che accettano solo oggetti come chiavi.</p>
     * 
     * @param userId identificativo della persona di cui si vogliono recuperare i progetti
     * @param year anno iniziale di un triennio entro il quale deve trovarsi la data di scadenza del progetto
     * @return <code>Vector&lt;ProjectBean&gt;</code> - ProjectBean rappresentante i progetti dell'utente loggato
     * @throws WebStorageException se si verifica un problema nell'esecuzione della query, nell'accesso al db o in qualche tipo di puntamento
     */
    @SuppressWarnings({ "null" })
    public LinkedHashMap<Integer, Vector<ProjectBean>> getProjectsByDepart(int userId,
                                                                           int year)
                                                                    throws WebStorageException {
        ResultSet rs, rs1, rs2 = null;
        Connection con = null;
        PreparedStatement pst = null;
        ProjectBean project = null;
        DepartmentBean dipart = null;
        CodeBean statoProgetto = null;
        int idDipart = -1;
        int idStatoProgetto = -1;
        Integer key = null;
        Vector<ProjectBean> v = null;
        LinkedHashMap<Integer, Vector<ProjectBean>> projects = new LinkedHashMap<Integer, Vector<ProjectBean>>();
        try {
            con = pol_manager.getConnection();
            pst = con.prepareStatement(GET_DIPART);
            pst.clearParameters();
            pst.setInt(1, -1);
            pst.setInt(2, -1);            
            rs = pst.executeQuery();
            while (rs.next()) {
                dipart = new DepartmentBean();
                v = new Vector<ProjectBean>();
                BeanUtil.populate(dipart, rs);
                idDipart = dipart.getId();
                key = new Integer(idDipart);
                // Recupera progetti del dipartimento
                int nextParam = 0;
                /* il seguente codice va bene se si vuol  mostrare sempre un triennio
                pst = con.prepareStatement(GET_PROJECTS_BY_DEPART_AND_YEAR);
                pst.clearParameters();
                pst.setInt(++nextParam, userId);
                pst.setInt(++nextParam, idDipart);
                pst.setDate(++nextParam, Utils.convert(Utils.convert(Utils.getLastDayOfYear(year))));
                pst.setDate(++nextParam, Utils.convert(Utils.convert(Utils.getLastDayOfYear(year + 2))));
                pst.setDate(++nextParam, Utils.convert(Utils.convert(Utils.getLastDayOfYear(year))));
                pst.setInt(++nextParam, Query.IN_PROGRESS);
                // il seguente codice invece si basa in modo stretto sulle date del progetto: */
                pst = con.prepareStatement(GET_PROJECTS_BY_DEPART_AND_DATE);
                pst.clearParameters();
                pst.setInt(++nextParam, userId);
                pst.setInt(++nextParam, idDipart);
                pst.setDate(++nextParam, Utils.convert(Utils.convert(Utils.getFirstDayOfYear(year))));
                pst.setDate(++nextParam, Utils.convert(Utils.convert(Utils.getLastDayOfYear(year))));
                pst.setDate(++nextParam, Utils.convert(Utils.convert(Utils.getLastDayOfYear(year))));
                pst.setInt(++nextParam, Query.IN_PROGRESS);
                rs1 = pst.executeQuery();
                while (rs1.next()) {
                    project = new ProjectBean();
                    BeanUtil.populate(project, rs1);
                    // Recupera statoprogetto del progetto
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
                    // Effettua un controllo per scartare dalla visualizzazione i progetti delle Performance senza WBS visualizzabili
                    if (project.getTipo() != null && project.getTipo().equals(Query.PERFORMANCE)) {
                        PersonBean user = new PersonBean();
                        user.setId(userId);
                        Vector<WbsBean> vWbsAncestors = getWbsHierarchy(project.getId(), user, year);
                        // Solo se sono state trovate WBS aggiunge il progetto alla lista
                        if (!vWbsAncestors.isEmpty()) {
                            v.add(project);
                        }
                    } else {
                        // Se il progetto non è delle Performance lo aggiunge a prescindere dalla presenza di WBS
                        v.add(project);                        
                    }
                }
                // Aggiunge la lista progetti del dipartimento e la chiave id dipartimento
                projects.put(key, v);
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
     * <p>Recupera i dipartimenti su cui un utente, il cui username viene
     * passato come argomento, ha i diritti di scrittura/editing.</p>
     * <p>Esempio:<pre>
     * id  id_ruolo  id_progetto  id_persona
     * 12    1         11           53
     * 13    2          1           47
     * 14    2          3           55
     * 15    3         11           55
     * 16    4          5           55
     * 17    5          4           55
     * </pre>
     * La persona con id 53 ('sfedeli') &egrave; PMOATE (ruolo 1) sul dipartimento
     * del progetto di id 11.<br />
     * La persona con id 47 ('amiorelli') &egrave; PMODIP (ruolo 2) sul dipartimento
     * del progetto di id 1.<br />
     * La persona con id 55 ('stroiano') &egrave; sia PMODIP, sia PM, sia TL, 
     * sia USER; se il progetto con id 4, su cui &egrave; USER o TL, appartiene 
     * allo stesso dipartimento dei progetti su cui &egrave; utente con privilegi
     * pi&uacute; alti, ci&ograve; non pregiudica la sua possibilit&agrave;
     * di editare il monitoraggio dipartimentale: &egrave; sufficiente,
     * quindi, che sullo stesso dipartimento sia presente 
     * almeno un ruolo superiore ad USER ed a TEAM LEADER.</p> 
     * 
     * @param userName login dell'utente di cui si vogliono ottenere i progetti editabili
     * @return <code>Vector&lt;DepartmentBean&gt;</code> - un Vector di dipartimenti, che rappresentano quelli su cui l'utente ha i diritti di scrittura
     * @throws WebStorageException se si verifica un problema nell'esecuzione della query, nel recupero di attributi obbligatori non valorizzati o in qualche altro tipo di puntamento
     */
    @SuppressWarnings({ "null", "static-method" })
    public Vector<DepartmentBean> getWritableDeparts(String userName)
                                              throws WebStorageException {
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement pst = null;
        DepartmentBean dipart = null;
        Vector<DepartmentBean> departs = new Vector<DepartmentBean>();
        try {
            con = pol_manager.getConnection();
            pst = con.prepareStatement(GET_WRITABLE_DEPARTMENTS_BY_USER_NAME);
            pst.clearParameters();
            pst.setString(1, userName);
            rs = pst.executeQuery();
            while (rs.next()) {
                dipart = new DepartmentBean();
                BeanUtil.populate(dipart, rs);
                // Aggiunge il dipartimento valorizzato all'elenco
                departs.add(dipart);
            }
            return departs;
        } catch (ClassCastException cce) {
            String msg = FOR_NAME + "Oggetto non valorizzato; problema nella query dei dipartimenti su cui l\'utente ha diritti di scrittura.\n";
            LOG.severe(msg); 
            throw new WebStorageException(msg + cce.getMessage(), cce);
        } catch (SQLException sqle) {
            String msg = FOR_NAME + "Oggetto non valorizzato; problema nella query dei dipartimenti su cui l\'utente ha diritti di scrittura.\n";
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
     * @param userId    identificativo della persona che deve avere un qualche ruolo sul progetto
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
     * <p>Restituisce una mappa dei dipartimenti indicizzata per identificativo
     * dipartimento.</p>
     * 
     * @return <code>HashMap&lt;Integer, DepartmentBean&gt;</code> - mappa rappresentante i dipartimenti indicizzati per id.
     * @throws WebStorageException se si verifica un problema nell'esecuzione delle query, nell'accesso al db o in qualche tipo di puntamento 
     */
    @SuppressWarnings({ "null", "static-method" })
    public LinkedHashMap<Integer, DepartmentBean> getDeparts() 
                                                      throws WebStorageException {
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement pst = null;
        DepartmentBean dipart = null;
        int idDipart = -1;
        Integer key = null;
        LinkedHashMap<Integer, DepartmentBean> departs = new LinkedHashMap<Integer, DepartmentBean>();
        try {
            con = pol_manager.getConnection();
            pst = con.prepareStatement(GET_DIPART);
            pst.clearParameters();
            pst.setInt(1, -1);
            pst.setInt(2, -1);            
            rs = pst.executeQuery();
            while (rs.next()) {
                dipart = new DepartmentBean();
                BeanUtil.populate(dipart, rs);
                idDipart = dipart.getId();
                key = new Integer(idDipart);
                departs.put(key, dipart);
            }
            return departs;
        } catch (AttributoNonValorizzatoException anve) {
            String msg = FOR_NAME + "Oggetto dipart.idDipart non valorizzato; problema nella query dei dipartimenti.\n";
            LOG.severe(msg); 
            throw new WebStorageException(msg + anve.getMessage(), anve);
        } catch (SQLException sqle) {
            String msg = FOR_NAME + "Oggetto non valorizzato; problema nella query dell\'utente.\n";
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
     * <p>Restituisce un ArrayList di StatusBean 
     * rappresentante tutti gli status del progetto attuale</p>
     * 
     * @param projId  id del progetto di cui estrarre gli status
     * @param user    utente loggato
     * @return <code>ArrayList&lt;StatusBean&gt;</code> - ArrayList&lt;StatusBean&gt; rappresentante gli status del progetto.
     * @throws WebStorageException se si verifica un problema nell'esecuzione delle query, nell'accesso al db o in qualche tipo di puntamento 
     */
    @SuppressWarnings({ "null" })
    public ArrayList<StatusBean> getStatusList(int projId,
                                               PersonBean user) 
                                        throws WebStorageException {
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement pst = null;
        StatusBean status = null;
        ArrayList<StatusBean> statusList = new ArrayList<StatusBean>();
        try {
            con = pol_manager.getConnection();
            // Per prima cosa verifica che l'utente abbia i diritti di accesso al progetto
            if (!userCanRead(projId, user.getId())) {
                String msg = FOR_NAME + "Qualcuno ha tentato di inserire un indirizzo nel browser avente un id progetto non valido!.\n";
                LOG.severe(msg + "E\' presente il parametro \"q=act\" ma non un valore \"id\" - cioe\' id progetto - significativo!\n");
                throw new WebStorageException("Attenzione: indirizzo richiesto non valido!\n");
            }
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
        } catch (AttributoNonValorizzatoException anve) {
            String msg = FOR_NAME + "Oggetto PersonBean non valorizzato; problema nella query dell\'utente.\n";
            LOG.severe(msg); 
            throw new WebStorageException(msg + anve.getMessage(), anve);
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
     * <p>Restituisce un ArrayList di StatusBean 
     * rappresentante tutti gli status del progetto attuale, 
     * contenenti ciascuno, al proprio interno, la lista delle sue 
     * attivit&agrave; correnti e delle sue attivit&agrave; future.</p>
     * 
     * @param projId  id del progetto di cui estrarre gli status
     * @param user    utente loggato
     * @return <code>ArrayList&lt;StatusBean&gt;</code> - ArrayList&lt;StatusBean&gt; rappresentante gli status del progetto, corredati di info sulle attivita'
     * @throws WebStorageException se si verifica un problema nell'esecuzione delle query, nell'accesso al db o in qualche tipo di puntamento 
     */
    @SuppressWarnings({ "null" })
    public ArrayList<StatusBean> getFullStatusList(int projId,
                                                   PersonBean user) 
                                            throws WebStorageException {
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement pst = null;
        StatusBean status, projectStatus = null;
        ArrayList<StatusBean> statusList = new ArrayList<StatusBean>();
        try {
            con = pol_manager.getConnection();
            // Per prima cosa verifica che l'utente abbia i diritti di accesso al progetto
            if (!userCanRead(projId, user.getId())) {
                String msg = FOR_NAME + "Qualcuno ha tentato di inserire un indirizzo nel browser avente un id progetto non valido!.\n";
                LOG.severe(msg + "E\' presente il parametro \"q=act\" ma non un valore \"id\" - cioe\' id progetto - significativo!\n");
                throw new WebStorageException("Attenzione: indirizzo richiesto non valido!\n");
            }
            pst = con.prepareStatement(GET_PROJECT_STATUS_LIST);
            pst.clearParameters();
            pst.setInt(1, projId);
            rs = pst.executeQuery();
            while (rs.next()) {
                // Recupera i dati di base dello status
                status = new StatusBean();
                BeanUtil.populate(status, rs);
                projectStatus = getStatus(projId, status.getDataFine(), user);
                statusList.add(status);
            }
            return statusList;
        } catch (AttributoNonValorizzatoException anve) {
            String msg = FOR_NAME + "Oggetto PersonBean non valorizzato; problema nella query dell\'utente.\n";
            LOG.severe(msg); 
            throw new WebStorageException(msg + anve.getMessage(), anve);
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
     * @param projId   id del progetto di cui estrarre lo status
     * @param idStatus id dello status da restituire
     * @param user     utente loggato
     * @return <code>StatusBean</code> - StatusBean rappresentante lo status di dato identificativo passato come argomento
     * @throws WebStorageException se si verifica un problema nell'esecuzione delle query, nell'accesso al db o in qualche tipo di puntamento
     */
    @SuppressWarnings("null")
    public StatusBean getStatus(int projId,
                                int idStatus,
                                PersonBean user) 
                         throws WebStorageException {
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement pst = null;
        StatusBean status = null;
        CodeBean statiTemp = new CodeBean();
        HashMap<String, CodeBean> stati = new HashMap<String, CodeBean>(15);
        int ordinal = 0;
        StringBuffer statoString = null;
        Vector<FileDocBean> attachments = null;
        try {
            con = pol_manager.getConnection();
            // Per prima cosa verifica che l'utente abbia i diritti di accesso al progetto
            if (!userCanRead(projId, user.getId())) {
                String msg = FOR_NAME + "Qualcuno ha tentato di inserire un indirizzo nel browser avente un id progetto non valido!.\n";
                LOG.severe(msg + "E\' presente il parametro \"q=act\" ma non un valore \"id\" - cioe\' id progetto - significativo!\n");
                throw new WebStorageException("Attenzione: indirizzo richiesto non valido!\n");
            }
            pst = con.prepareStatement(GET_STATUS);
            pst.clearParameters();
            pst.setInt(1, idStatus);
            rs = pst.executeQuery();
            if (rs.next()) {
                status = new StatusBean();
                BeanUtil.populate(status, rs);
                // Recupero di stato costi
                statiTemp = retrieve(GET_STATOCOSTI, idStatus);
                statoString = new StringBuffer("StatoCosti");
                statiTemp.setInformativa(statoString.toString());
                statiTemp.setOrdinale(ordinal++);
                stati.put(statoString.toString(), statiTemp);
                // Recupero di stato tempi
                statiTemp = retrieve(GET_STATOTEMPI, idStatus);
                statoString = null;
                statoString = new StringBuffer("StatoTempi");
                statiTemp.setInformativa(statoString.toString());
                statiTemp.setOrdinale(ordinal++);
                stati.put(statoString.toString(), statiTemp);
                // Recupero di stato rischi
                statiTemp = retrieve(GET_STATORISCHI, idStatus);
                statoString = null;
                statoString = new StringBuffer("StatoRischi");
                statiTemp.setInformativa(statoString.toString());
                statiTemp.setOrdinale(ordinal++);
                stati.put(statoString.toString(), statiTemp);
                // Recupero di stato risorse
                statiTemp = retrieve(GET_STATORISORSE, idStatus);
                statoString = null;
                statoString = new StringBuffer("StatoRisorse");
                statiTemp.setInformativa(statoString.toString());
                statiTemp.setOrdinale(ordinal++);
                stati.put(statoString.toString(), statiTemp);
                // Recupero di stato scope
                statiTemp = retrieve(GET_STATOSCOPE, idStatus);
                statoString = null;
                statoString = new StringBuffer("StatoScope");
                statiTemp.setInformativa(statoString.toString());
                statiTemp.setOrdinale(ordinal++);
                stati.put(statoString.toString(), statiTemp);
                // Recupero di stato comunicazione
                statiTemp = retrieve(GET_STATOCOMUNICAZIONE, idStatus);
                statoString = null;
                statoString = new StringBuffer("StatoComunicazione");
                statiTemp.setInformativa(statoString.toString());
                statiTemp.setOrdinale(ordinal++);
                stati.put(statoString.toString(), statiTemp);
                // Recupero di stato qualita
                statiTemp = retrieve(GET_STATOQUALITA, idStatus);
                statoString = null;
                statoString = new StringBuffer("StatoQualita");
                statiTemp.setInformativa(statoString.toString());
                statiTemp.setOrdinale(ordinal++);
                stati.put(statoString.toString(), statiTemp);
                // Recupero di stato approvvigionamenti
                statiTemp = retrieve(GET_STATOAPPROVVIGIONAMENTI, idStatus);
                statoString = null;
                statoString = new StringBuffer("StatoApprovvigionamenti");
                statiTemp.setInformativa(statoString.toString());
                statiTemp.setOrdinale(ordinal++);
                stati.put(statoString.toString(), statiTemp);
                // Recupero di stato stakeholder
                statiTemp = retrieve(GET_STATOSTAKEHOLDER, idStatus);
                statoString = null;
                statoString = new StringBuffer("StatoStakeholder");
                statiTemp.setInformativa(statoString.toString());
                statiTemp.setOrdinale(ordinal++);
                stati.put(statoString.toString(), statiTemp);
                // Settaggio dei "semafori" (aree di conoscenza)
                status.setStati(stati);
                // Recupero e settaggio degli allegati
                try {
                    attachments = getFileDoc("avanzamento", "all", status.getId(), NOTHING);
                    status.setAllegati(attachments);
                } catch (AttributoNonValorizzatoException anve) {
                    String msg = FOR_NAME + "Oggetto status.id non valorizzato; problema nella query che recupera lo status attraverso l\'id.\n";
                    LOG.severe(msg); 
                    throw new WebStorageException(msg + anve.getMessage(), anve);
                }
            }
            return status;
        } catch (AttributoNonValorizzatoException anve) {
            String msg = FOR_NAME + "Oggetto PersonBean non valorizzato; problema nella query dell\'utente.\n";
            LOG.severe(msg); 
            throw new WebStorageException(msg + anve.getMessage(), anve);
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
     * @param user utente loggato
     * @return <code>StatusBean</code> - StatusBean rappresentante lo status corrente
     * @throws WebStorageException se si verifica un problema nell'esecuzione delle query, nell'accesso al db o in qualche tipo di puntamento
     */
    @SuppressWarnings("null")
    public StatusBean getStatus(int idProj, 
                                Date statusDate,
                                PersonBean user) 
                         throws WebStorageException {
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement pst = null;
        StatusBean status = null;
        CodeBean statiTemp = new CodeBean();
        HashMap<String, CodeBean> stati = new HashMap<String, CodeBean>(15);
        int ordinal = 0;
        StringBuffer statoString = null;
        Vector<FileDocBean> attachments = null;
        try {
            con = pol_manager.getConnection();
            // Per prima cosa verifica che l'utente abbia i diritti di accesso al progetto
            if (!userCanRead(idProj, user.getId())) {
                String msg = FOR_NAME + "Qualcuno ha tentato di inserire un indirizzo nel browser avente un id progetto non valido!.\n";
                LOG.severe(msg + "E\' presente il parametro \"q=act\" ma non un valore \"id\" - cioe\' id progetto - significativo!\n");
                throw new WebStorageException("Attenzione: indirizzo richiesto non valido!\n");
            }
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
                // Settaggio degli stati ("semafori" o aree di conoscenza PMBOK) nel bean status
                status.setStati(stati);
                // Recupero e settaggio degli allegati
                try {
                    attachments = getFileDoc("avanzamento", "all", status.getId(), NOTHING);
                    status.setAllegati(attachments);
                } catch (AttributoNonValorizzatoException anve) {
                    String msg = FOR_NAME + "Oggetto status.id non valorizzato; problema nella query che recupera lo status attraverso l\'id.\n";
                    LOG.severe(msg); 
                    throw new WebStorageException(msg + anve.getMessage(), anve);
                }
            }
            return status;
        } catch (AttributoNonValorizzatoException anve) {
            String msg = FOR_NAME + "Oggetto PersonBean non valorizzato; problema nella query dell\'utente.\n";
            LOG.severe(msg); 
            throw new WebStorageException(msg + anve.getMessage(), anve);
        } catch (SQLException sqle) {
            String msg = FOR_NAME + "Oggetto StatusBean non valorizzato; problema nella query dello status piu\' prossimo alla data passata come argomento.\n";
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
     * <p>Restituisce lo status di avanzamento progetto successivo a quello dato in input, 
     * del quale viene passata la data di fine come parametro. </p>
     * 
     * @param idProj progetto richiesto dall'utente
     * @param dataInizio data dopo la quale trovare lo status successivo
     * @param user utente loggato
     * @return StatusBean - contenente lo status successivo in ordine di dataInizio
     * @throws WebStorageException se si verifica un problema nell'esecuzione delle query, nell'accesso al db o in qualche tipo di puntamento 
     */
    @SuppressWarnings({ "null" })
    public StatusBean getNextStatus(int idProj, 
                                    Date dataInizio,
                                    PersonBean user) 
                             throws WebStorageException {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        StatusBean nextStatus = null;
        int nextParam = 0;
        try {
            con = pol_manager.getConnection();
            // Per prima cosa verifica che l'utente abbia i diritti di accesso al progetto
            if (!userCanRead(idProj, user.getId())) {
                String msg = FOR_NAME + "Qualcuno ha tentato di inserire un indirizzo nel browser avente un id progetto non valido!.\n";
                LOG.severe(msg + "E\' presente il parametro \"q=act\" ma non un valore \"id\" - cioe\' id progetto - significativo!\n");
                throw new WebStorageException("Attenzione: indirizzo richiesto non valido!\n");
            }
            pst = con.prepareStatement(GET_NEXT_STATUS);
            pst.clearParameters();
            pst.setInt(++nextParam, idProj);
            pst.setDate(++nextParam, Utils.convert(dataInizio));
            rs = pst.executeQuery();
            if (rs.next()) {
                nextStatus = new StatusBean();
                BeanUtil.populate(nextStatus, rs);
            }
            return nextStatus;
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
     * <p>Restituisce un Vector di ActivityBean rappresentante tutte
     * le attivit&agrave; del progetto attuale.</p>
     * <p>Pu&ograve; essere usato &quot;a valle&quot; dell'identificazione
     * di identificativi di progetto su cui l'utente sicuramente pu&ograve; 
     * scrivere, per recuperare tutte le attivit&agrave; degli stessi,
     * senza effettuare ulteriori controlli o filtri (se un utente pu&ograve;
     * scrivere su un progetto, potr&agrave; anche editarne le attivit&agrave;,
     * dati i ruoli configurati nell'applicazione <code>pol</code>).</p>
     * <p>Lo stato calcolato potrebbe non essere necessario ai fini dell'uso
     * di questa lista; in caso contrario bisogna richiamare il metodo che
     * lo calcola passando l'attivi&agrave; corrente come parametro:
     * <pre>
     * BeanUtil.populate(attivita, rs);
     * computeActivityState(attivita, Utils.convert(Utils.getCurrentDate()));
     * </pre></p>
     * 
     * @param projId  id del progetto di cui estrarre le attivit&agrave;
     * @param user utente loggato
     * @return <code>Vector&lt;AttvitaBean&gt;</code> - ActivityBean rappresentante l'attivit&agrave; del progetto.
     * @throws WebStorageException se si verifica un problema nell'esecuzione delle query, nell'accesso al db o in qualche tipo di puntamento 
     */
    @SuppressWarnings({ "null" })
    public Vector<ActivityBean> getActivities(int projId,
                                              PersonBean user) 
                                       throws WebStorageException {
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement pst = null;
        ActivityBean attivita = null;
        //PersonBean person = null;
        //Vector<PersonBean> people = new Vector<PersonBean>();
        Vector<ActivityBean> activities = new Vector<ActivityBean>();
        try {
            con = pol_manager.getConnection();
            // Per prima cosa verifica che l'utente abbia i diritti di accesso al progetto
            if (!userCanRead(projId, user.getId())) {
                String msg = FOR_NAME + "Qualcuno ha tentato di inserire un indirizzo nel browser avente un id progetto non valido!.\n";
                LOG.severe(msg + "E\' presente il parametro \"q=act\" ma non un valore \"id\" - cioe\' id progetto - significativo!\n");
                throw new WebStorageException("Attenzione: indirizzo richiesto non valido!\n");
            }
            pst = con.prepareStatement(GET_ACTIVITIES);
            pst.clearParameters();
            pst.setInt(1, projId);
            rs = pst.executeQuery();
            while (rs.next()) {
                attivita = new ActivityBean();
                BeanUtil.populate(attivita, rs);
                /*pst = null;
                pst = con.prepareStatement(GET_PEOPLE_ON_ACTIVITY);
                pst.clearParameters();
                pst.setInt(1, attivita.getId());
                rs1 = pst.executeQuery();
                while (rs1.next()) {
                    person = new PersonBean();
                    BeanUtil.populate(person, rs1);
                    people.add(person);
                }
                attivita.setPersone(people);*/
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
     * <p>Restituisce un Vector di ActivityBean contenenti ciascuno 
     * gli estremi per il calcolo dello stato.</p>
     * <p><ul><li>Se si vuole che l'identificativo di progetto passato come 
     * argomento non abbia influenza sul risultato, perch&eacute; 
     * si desidera effettuare un'estrazione di tutte le attivit&agrave; 
     * e non solo di quelle di uno specifico progetto, allora si deve mettere 
     * un valore qualsiasi sul primo parametro (es. 0) 
     * e sul secondo si metter&agrave; -1.</li>
     *  <li>Se invece si vuole che il parametro opzionale abbia effetto, 
     *  si mette il valore corretto sul primo parametro e 0 sul secondo.</li>
     *  </ul>
     *  Infatti:
     *  <pre>
     *  SELECT count(*) FROM attivita WHERE id_progetto = 1;</pre>
     *  retituisce (p.es.) 333
     *  <pre>
     *  SELECT count(*) FROM attivita WHERE id_progetto = 1 OR -1 = -1;
     *  SELECT count(*) FROM attivita WHERE id_progetto = 0 OR -1 = -1;</pre>
     *  retituiscono sempre (p.es.) 1399<br /> 
     *  (se il secondo argomento vale -1  il primo argomento 
     *  non ha pi&uacute; alcun effetto).
     *  <pre>
     *  SELECT count(*) FROM attivita WHERE id_progetto = 1 OR -1 = 0;</pre>
     *  retituisce (p.es.) 333<br />
     *  (se il secondo argomento non vale -1, la condizione complessiva
     *  equivale a quella senza la seconda clausola, cio&egrave; il secondo
     *  argomento non ha alcun effetto).
     *  </p>
     * 
     * @param projId  id del progetto di cui estrarre le attivit&agrave; (se il secondo argomento non vale -1)
     * @param getAll  valore intero che se vale -1 genera l'estrazione di tutte le attivita' presenti in tabella indipendentemente dal valore del primo argomento
     * @return <code>Vector&lt;AttvitaBean&gt;</code> - ActivityBean rappresentante l'elenco delle attivita' del progetto - oppure di tutte le attivita'.
     * @throws WebStorageException se si verifica un problema nell'esecuzione delle query, nell'accesso al db o in qualche tipo di puntamento 
     */
    @SuppressWarnings({ "null", "static-method" })
    public Vector<ActivityBean> getActivities(int projId, 
                                              int getAll) 
                                       throws WebStorageException {
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement pst = null;
        ActivityBean attivita = null;
        Vector<ActivityBean> activities = new Vector<ActivityBean>();
        try {
            con = pol_manager.getConnection();
            pst = con.prepareStatement(GET_ACTIVITIES_WITH_DATES);
            pst.clearParameters();
            pst.setInt(1, projId);
            pst.setInt(2, getAll);
            rs = pst.executeQuery();
            while (rs.next()) {
                attivita = new ActivityBean();
                BeanUtil.populate(attivita, rs);
                activities.add(attivita);
            }
            return activities;
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
     * <p>Restituisce un Vector di ActivityBean rappresentante 
     * le attivit&agrave; del progetto attuale, il cui identificativo
     * viene passato come argomento, e permette di distinguere liste diverse
     * in funzione di un flag passato come argomento:
     * <ul>
     * <li>Se si vuole che <code>"getMilestoneOnly"</code> non abbia influenza 
     * sul risultato, perch&eacute; si desidera effettuare un'estrazione 
     * di tutti gli elementi e non solo di quelli di tipo milestone 
     * (o di tipo non-milestone), allora si deve mettere 
     * un valore qualsiasi sul primo parametro (es. false) 
     * e sul secondo si metter&agrave; true.</li>
     *  <li>Se invece si vuole che il parametro opzionale abbia effetto, 
     *  si mette il valore corretto sul primo parametro e false sul secondo.</li>
     *  </ul>
     *  In altri termini, perch&eacute; abbia effetto il primo parametro, 
     *  che discrimina tra milestone e non milestone, il secondo parametro 
     *  deve essere <code>false</code>;
     *  se il secondo parametro &egrave; <code>true</code>, il valore passato
     *  sul primo parametro... <cite>whatever</cite> (= non ha effetto).<br />
     *  Infatti:
     *  <pre>
     *  SELECT count(*) FROM attivita WHERE (milestone = false OR false);</pre>
     *  retituisce p.es. 16
     *  <pre>
     *  SELECT count(*) FROM attivita WHERE (milestone = true OR false);</pre>
     *  retituisce p.es. 4
     *  <pre>
     *  SELECT count(*) FROM attivita WHERE (milestone = false OR true);</pre>
     *  retituisce 20
     *  <pre>
     *  SELECT count(*) FROM attivita WHERE (milestone = true OR true);</pre>
     *  retituisce sempre 20
     *  </p>
     * 
     * @param projId id del progetto di cui estrarre le attivita'
     * @param user   utente loggato
     * @param date   data inizio a partire dalla quale le attivita' devono essere estratte 
     * @param getMilestoneOnly flag se 'true' specificante che devono essere estratte solo attivita' di tipo milestone, se 'false' il contrario
     * @param getAll flag se 'true' specificante che devono essere estratte tutte le attivita' indipendentemente dal flag precedente, se 'false' inattivo
     * @return <code>Vector&lt;AttvitaBean&gt;</code> - Vector di ActivityBean, ciascuna rappresentante una attivit&agrave; del progetto.
     * @throws WebStorageException se si verifica un problema nell'esecuzione delle query, nell'accesso al db o in qualche tipo di puntamento 
     */
    @SuppressWarnings({ "null" })
    public Vector<ActivityBean> getActivities(int projId,
                                              PersonBean user,
                                              Date date,
                                              boolean getMilestoneOnly,
                                              boolean getAll) 
                                       throws WebStorageException {
        ResultSet rs, rs1 = null;
        Connection con = null;
        PreparedStatement pst = null;
        ActivityBean attivita = null;
        DepartmentBean depart = null;
        //Vector<PersonBean> people = new Vector<PersonBean>();
        Vector<ActivityBean> activities = new Vector<ActivityBean>();
        int nextParam = 0;
        String query = (date.after(Utils.convert(Utils.getUnixEpoch())) ? GET_ACTIVITIES_BY_ENDDATE_AND_TYPE : GET_ACTIVITIES_BY_DATE_AND_TYPE);
        try {
            // La connessione è gestita dal finally
            con = pol_manager.getConnection();
            // Per prima cosa verifica che l'utente abbia i diritti di accesso al progetto
            if (!userCanRead(projId, user.getId())) {
                String msg = FOR_NAME + "Qualcuno ha tentato di inserire un indirizzo nel browser avente un id progetto non valido!.\n";
                LOG.severe(msg + "E\' presente il parametro \"q=act\" ma non un valore \"id\" - cioe\' id progetto - significativo!\n");
                throw new WebStorageException("Attenzione: indirizzo richiesto non valido!\n");
            }
            pst = con.prepareStatement(query);
            pst.clearParameters();
            pst.setInt(++nextParam, projId);
            pst.setDate(++nextParam, Utils.convert(date));
            pst.setBoolean(++nextParam, getMilestoneOnly);
            pst.setBoolean(++nextParam, getAll);
            rs = pst.executeQuery();
            while (rs.next()) {
                attivita = new ActivityBean();
                BeanUtil.populate(attivita, rs);
                computeActivityState(attivita, Utils.convert(Utils.getCurrentDate()));
                WbsBean wp = getWbsInstance(projId, attivita.getIdWbs(), user);
                pst = null;
                pst = con.prepareStatement(GET_DEPART_ON_ACTIVITY);
                pst.clearParameters();
                pst.setInt(1, attivita.getId());
                rs1 = pst.executeQuery();
                if (rs1.next()) {
                    depart = new DepartmentBean();
                    BeanUtil.populate(depart, rs1);
                    attivita.setDipartimento(depart);
                }
                attivita.setWbs(wp);
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
     * <p>Restituisce un Vector di ActivityBean rappresentante 
     * le attivit&agrave; in un dato stato, il cui identificativo viene
     * passato come argomento, appartenenti al progetto attuale, 
     * il cui identificativo viene passato come argomento, e permette 
     * di ottenere le attivit&agrave; in funzione di una data, 
     * passata come argomento.</p>
     * 
     * @param projId id del progetto di cui estrarre le attivita'
     * @param user   utente loggato
     * @param date   data inizio a partire dalla quale le attivita' devono essere estratte 
     * @param state  identificativo dello stato in cui devono trovarsi le attivita' selezionate
     * @return <code>Vector&lt;AttvitaBean&gt;</code> - Vector di ActivityBean, ciascuna rappresentante una attivit&agrave; del progetto in stato desiderato.
     * @throws WebStorageException se si verifica un problema nell'esecuzione delle query, nell'accesso al db o in qualche tipo di puntamento 
     */
    @SuppressWarnings({ "null" })
    public Vector<ActivityBean> getActivities(int projId,
                                              PersonBean user,
                                              Date date,
                                              int state) 
                                       throws WebStorageException {
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement pst = null;
        ActivityBean attivita = null;
        Vector<ActivityBean> activities = new Vector<ActivityBean>();
        int nextParam = 0;
        try {
            // La connessione è gestita dal finally
            con = pol_manager.getConnection();
            // Per prima cosa verifica che l'utente abbia i diritti di accesso al progetto
            if (!userCanRead(projId, user.getId())) {
                String msg = FOR_NAME + "Qualcuno ha tentato di inserire un indirizzo nel browser avente un id progetto non valido!.\n";
                LOG.severe(msg + "E\' presente il parametro \"q=act\" ma non un valore \"id\" - cioe\' id progetto - significativo!\n");
                throw new WebStorageException("Attenzione: indirizzo richiesto non valido!\n");
            }
            pst = con.prepareStatement(GET_ACTIVITIES_BY_STATE);
            pst.clearParameters();
            pst.setInt(++nextParam, projId);
            pst.setInt(++nextParam, state);
            pst.setDate(++nextParam, Utils.convert(date));
            rs = pst.executeQuery();
            while (rs.next()) {
                attivita = new ActivityBean();
                BeanUtil.populate(attivita, rs);
                computeActivityState(attivita, Utils.convert(Utils.getCurrentDate()));
                WbsBean wp = getWbsInstance(projId, attivita.getIdWbs(), user);
                attivita.setWbs(wp);
                activities.add(attivita);
            }
            return activities;
        } catch (AttributoNonValorizzatoException anve) {
            String msg = FOR_NAME + "Attributo non valorizzato; problema nella query delle attivita\'.\n";
            LOG.severe(msg); 
            throw new WebStorageException(msg + anve.getMessage(), anve);
        } catch (SQLException sqle) {
            String msg = FOR_NAME + "Oggetto ActivityBean non valorizzato; problema nella query delle attivita\'.\n";
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
    
    
    /** <p>Restituisce un oggetto Vector&lt;ActivityBean&gt; 
     * contenente tutte le attivit&agrave; che appartengono ad una WBS, 
     * identificata tramite id, passato come parametro, di un progetto, 
     * identificato tramite id, passato come parametro</p>
     * 
     * @param idWbs identificativo della WBS 
     * @param idProj identificativo del progetto corrente
     * @param user utente loggato
     * @return <code>Vector&lt;AttivitaBean&gt;</code> - Vector contenente la lista delle attivit&agrave; della WBS
     * @throws WebStorageException se si verifica un problema nell'esecuzione delle query, nell'accesso al db o in qualche tipo di puntamento 
     */
    @SuppressWarnings({ "null" })
    public Vector<ActivityBean> getActivitiesByWbs(int idWbs,
                                                   int idProj, 
                                                   PersonBean user)
                                            throws WebStorageException {
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement pst = null;
        ActivityBean activity = null;
        Vector<ActivityBean> activities = new Vector<ActivityBean>();
        int nextParam = 0;
        try {
            con = pol_manager.getConnection();
            // Per prima cosa verifica che l'utente abbia i diritti di accesso al progetto
            if (!userCanRead(idProj, user.getId())) {
                String msg = FOR_NAME + "Qualcuno ha tentato di inserire un indirizzo nel browser avente un id progetto non valido!.\n";
                LOG.severe(msg + "E\' presente il parametro \"q=act\" ma non un valore \"id\" - cioe\' id progetto - significativo!\n");
                throw new WebStorageException("Attenzione: indirizzo richiesto non valido!\n");
            }
            pst = con.prepareStatement(GET_ACTIVITIES_BY_WBS);
            pst.clearParameters();
            pst.setInt(++nextParam, idProj);
            pst.setInt(++nextParam, idWbs);
            rs = pst.executeQuery();
            while (rs.next()) {
                // Per ogni attività crea un oggetto vuoto
                activity = new ActivityBean();
                // Lo valorizza
                BeanUtil.populate(activity, rs);
                // Ne calcola lo stato
                computeActivityState(activity, Utils.convert(Utils.getCurrentDate()));
                // Ne calcola i giorni uomo
                computeDays(activity, Utils.convert(Utils.getCurrentDate()));
                // Lo aggiunge all'elenco
                activities.add(activity);
            }
            return activities;
        } catch (AttributoNonValorizzatoException anve) {
            String msg = FOR_NAME + "Attributo non valorizzato; problema nella query delle attivita\'.\n";
            LOG.severe(msg); 
            throw new WebStorageException(msg + anve.getMessage(), anve);
        } catch (SQLException sqle) {
            String msg = FOR_NAME + "Oggetto ActivityBean non valorizzato; problema nella query dell\'attivita\'.\n";
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
     * <p>Restituisce il numero di attivit&agrave; presenti all'interno di una data wbs, 
     * identificata tramite id, passato come parametro.</p>
     * 
     * @param idProj               id del progetto a cui appartengono la wbs e le attivit&agrave;
     * @param idWbs                id della wbs a cui appartengono le attivit&agrave;
     * @param user                 utente loggato
     * @return <code>int</code> - intero che rappresenta il numero di istanze di attivit&agrave; sottostanti alla wbs data
     * @throws WebStorageException se si verifica un problema nell'esecuzione delle query, nell'accesso al db o in qualche tipo di puntamento
     */
    @SuppressWarnings({ "null" })
    public int getActivitiesAmountByWbs(int idProj, 
                                        int idWbs, 
                                        PersonBean user) 
                                 throws WebStorageException {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        int amountActivities = 0;
        int nextParam = 0;
        try {
            con = pol_manager.getConnection();
            // Per prima cosa verifica che l'utente abbia i diritti di accesso al progetto
            if (!userCanRead(idProj, user.getId())) {
                String msg = FOR_NAME + "Qualcuno ha tentato di inserire un indirizzo nel browser avente un id progetto non valido!.\n";
                LOG.severe(msg + "E\' presente il parametro \"q=act\" ma non un valore \"id\" - cioe\' id progetto - significativo!\n");
                throw new WebStorageException("Attenzione: indirizzo richiesto non valido!\n");
            }
            pst = con.prepareStatement(GET_ACTIVITIES_COUNT_BY_WBS);
            pst.setInt(++nextParam, idProj);
            pst.setInt(++nextParam, idWbs);
            rs = pst.executeQuery();
            if (rs.next()) {
                amountActivities = rs.getInt(1);
            }
            return amountActivities;
        } catch (AttributoNonValorizzatoException anve) {
            String msg = FOR_NAME + "Attributo non valorizzato; problema nella query delle attivita\'.\n";
            LOG.severe(msg); 
            throw new WebStorageException(msg + anve.getMessage(), anve);
        } catch (SQLException sqle) {
            String msg = FOR_NAME + "Errore nella query che estrae il numero di attivit&agrave; presenti in una data wbs.\n";
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
     * <p>Restituisce il numero di attivit&agrave; presenti all'interno di una data wbs, 
     * identificata tramite id, passato come parametro.</p>
     * 
     * @param idProj              id del progetto a cui appartengono la wbs e le attivit&agrave;
     * @param idWbs               id della wbs a cui appartengono le attivit&agrave;
     * @param user                utente loggato
     * @param year                anno entro cui deve trovarsi la data di fine di almeno un'attività della WBS il cui id viene passato come parametro
     * @return <code>int</code> - intero che rappresenta il numero di istanze di attivit&agrave; sottostanti alla wbs data
     * @throws WebStorageException se si verifica un problema nell'esecuzione delle query, nell'accesso al db o in qualche tipo di puntamento
     */
    @SuppressWarnings({ "null" })
    public int getActivitiesAmountByWbsAndYear(int idProj, 
                                               int idWbs, 
                                               PersonBean user,
                                               int year) 
                                        throws WebStorageException {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        int amountActivities = 0;
        int nextParam = 0;
        try {
            con = pol_manager.getConnection();
            // Per prima cosa verifica che l'utente abbia i diritti di accesso al progetto
            if (!userCanRead(idProj, user.getId())) {
                String msg = FOR_NAME + "Qualcuno ha tentato di inserire un indirizzo nel browser avente un id progetto non valido!.\n";
                LOG.severe(msg + "E\' presente il parametro \"q=act\" ma non un valore \"id\" - cioe\' id progetto - significativo!\n");
                throw new WebStorageException("Attenzione: indirizzo richiesto non valido!\n");
            }
            pst = con.prepareStatement(GET_ACTIVITIES_COUNT_BY_WBS_AND_YEAR);
            pst.setInt(++nextParam, idProj);
            pst.setInt(++nextParam, idWbs);
            pst.setDate(++nextParam, Utils.convert(Utils.convert(Utils.getFirstDayOfYear(year))));
            pst.setDate(++nextParam, Utils.convert(Utils.convert(Utils.getLastDayOfYear(year))));
            pst.setInt(++nextParam, idProj);
            pst.setInt(++nextParam, idWbs);
            pst.setDate(++nextParam, Utils.convert(Utils.convert(Utils.getFirstDayOfYear(year - 1))));
            pst.setDate(++nextParam, Utils.convert(Utils.convert(Utils.getLastDayOfYear(year + 1))));
            pst.setInt(++nextParam, idProj);
            pst.setInt(++nextParam, idWbs);
            pst.setDate(++nextParam, Utils.convert(Utils.convert(Utils.getFirstDayOfYear(year))));
            pst.setDate(++nextParam, Utils.convert(Utils.convert(Utils.getLastDayOfYear(year))));
            rs = pst.executeQuery();
            while (rs.next()) {
                ++amountActivities;
            }
            return amountActivities;
        } catch (AttributoNonValorizzatoException anve) {
            String msg = FOR_NAME + "Attributo non valorizzato; problema nella query delle attivita\'.\n";
            LOG.severe(msg); 
            throw new WebStorageException(msg + anve.getMessage(), anve);
        } catch (SQLException sqle) {
            String msg = FOR_NAME + "Errore nella query che estrae il numero di attivit&agrave; presenti in una data wbs.\n";
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
     * <p>Restituisce un Vector di ActivityBean rappresentante le attivit&agrave; che 
     * a qualunque titolo intersecano un range di date passate come argomento.</p>
     * 
     * @param idProj                id del progetto a cui appartiene lo status
     * @param user                  utente loggato
     * @param dataInizio            data di inizio dello status progetto
     * @param dataFine              data di fine dello status progetto
     * @return Vector$lt;ActivityBean&gt; - contenente la lista di attivit&agrave; richiesta
     * @throws WebStorageException se si verifica un problema nell'esecuzione delle query, nell'accesso al db o in qualche tipo di puntamento
     */
    @SuppressWarnings({ "null" })
    public Vector<ActivityBean> getActivitiesByRange(int idProj,
                                                     PersonBean user,
                                                     Date dataInizio, 
                                                     Date dataFine) 
                                              throws WebStorageException {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        ActivityBean activity = null;
        Vector<ActivityBean> vectorActivity = new Vector<ActivityBean>();
        int nextParam = 0;
        try {
            con = pol_manager.getConnection();
            // Per prima cosa verifica che l'utente abbia i diritti di accesso al progetto
            if (!userCanRead(idProj, user.getId())) {
                String msg = FOR_NAME + "Qualcuno ha tentato di inserire un indirizzo nel browser avente un id progetto non valido!.\n";
                LOG.severe(msg + "E\' presente il parametro \"q=act\" ma non un valore \"id\" - cioe\' id progetto - significativo!\n");
                throw new WebStorageException("Attenzione: indirizzo richiesto non valido!\n");
            }
            pst = con.prepareStatement(GET_CURRENT_ACTIVITIES);
            pst.clearParameters();
            pst.setInt(++nextParam, idProj);
            pst.setDate(++nextParam, Utils.convert(dataInizio));
            pst.setDate(++nextParam, Utils.convert(dataInizio));
            pst.setDate(++nextParam, Utils.convert(dataInizio));
            pst.setDate(++nextParam, Utils.convert(dataFine));
            rs = pst.executeQuery();
            while (rs.next()) {
                activity = new ActivityBean();
                BeanUtil.populate(activity, rs);
                computeActivityState(activity, Utils.convert(Utils.getCurrentDate()));
                vectorActivity.add(activity);
            }
            return vectorActivity;
        } catch (AttributoNonValorizzatoException anve) {
            String msg = FOR_NAME + "Attributo non valorizzato; problema nella query delle attivita\'.\n";
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
     * <p>Restituisce un Vector di ActivityBean rappresentante le attivit&agrave; 
     * che hanno data di inizio compresa tra due date, passate come argomento.</p>
     * 
     * @param idProj                id del progetto a cui appartiene lo status
     * @param user                  utente loggato
     * @param dataInizio            data di inizio dello status progetto
     * @param dataFine              data di fine dello status progetto
     * @return Vector$lt;ActivityBean&gt; - contenente la lista di attivit&agrave; richiesta
     * @throws WebStorageException se si verifica un problema nell'esecuzione delle query, nell'accesso al db o in qualche tipo di puntamento
     */
    @SuppressWarnings({ "null" })
    public Vector<ActivityBean> getActivitiesByDate(int idProj,
                                                    PersonBean user,
                                                    Date dataInizio, 
                                                    Date dataFine) 
                                             throws WebStorageException {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs= null;
        ActivityBean activity = null;
        Vector<ActivityBean> vectorActivity = new Vector<ActivityBean>();
        int nextParam = 0;
        try {
            con = pol_manager.getConnection();
            // Per prima cosa verifica che l'utente abbia i diritti di accesso al progetto
            if (!userCanRead(idProj, user.getId())) {
                String msg = FOR_NAME + "Qualcuno ha tentato di inserire un indirizzo nel browser avente un id progetto non valido!.\n";
                LOG.severe(msg + "E\' presente il parametro \"q=act\" ma non un valore \"id\" - cioe\' id progetto - significativo!\n");
                throw new WebStorageException("Attenzione: indirizzo richiesto non valido!\n");
            }
            pst = con.prepareStatement(GET_ACTIVITIES_OF_NEXT_STATUS);
            pst.clearParameters();
            pst.setInt(++nextParam, idProj);
            pst.setDate(++nextParam, Utils.convert(dataInizio));
            pst.setDate(++nextParam, Utils.convert(dataFine));
            rs = pst.executeQuery();
            while (rs.next()) {
                activity = new ActivityBean();
                BeanUtil.populate(activity, rs);
                computeActivityState(activity, Utils.convert(Utils.getCurrentDate()));
                vectorActivity.add(activity);
            }
            return vectorActivity;
        } catch (AttributoNonValorizzatoException anve) {
            String msg = FOR_NAME + "Attributo non valorizzato; problema nella query delle attivita\'.\n";
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
     * <p>Restituisce un oggetto ActivityBean rappresentante una attivit&agrave; 
     * che deve appartenere al progetto corrente - che a sua volta deve 
     * essere leggibile dall'utente - e avente identificativo 
     * passato come argomento.</p>
     * 
     * @param projId     id del progetto di cui estrarre le attivit&agrave;
     * @param activityId id dell'attivit&agrave; che si vuol recuperare
     * @param user       utente loggato
     * @return <code>AttvitaBean</code> - ActivityBean rappresentante l\'attivit&agrave; del progetto avente l\'identificativo progetto e l\'identificativo attivita\' passati come parametri.
     * @throws WebStorageException se si verifica un problema nell'esecuzione delle query, nell'accesso al db o in qualche tipo di puntamento 
     */
    @SuppressWarnings({ "null" })
    public ActivityBean getActivity(int projId,
                                    int activityId,
                                    PersonBean user) 
                             throws WebStorageException {
        ResultSet rs, rs1, rs2 = null;
        Connection con = null;
        PreparedStatement pst = null;
        PersonBean person = null;
        ActivityBean activity = null;
        Vector<PersonBean> people = new Vector<PersonBean>();
        Vector<SkillBean> skills = null;
        try {
            con = pol_manager.getConnection();
            // Per prima cosa verifica che l'utente abbia i diritti di accesso al progetto
            if (!userCanRead(projId, user.getId())) {
                String msg = FOR_NAME + "Qualcuno ha tentato di inserire un indirizzo nel browser avente un id progetto non valido!.\n";
                LOG.severe(msg + "E\' presente il parametro \"q=act\" ma non un valore \"id\" - cioe\' id progetto - significativo!\n");
                throw new WebStorageException("Attenzione: indirizzo richiesto non valido!\n");
            }
            pst = con.prepareStatement(GET_ACTIVITY);
            pst.clearParameters();
            pst.setInt(1, projId);
            pst.setInt(2, activityId);
            rs = pst.executeQuery();
            if (rs.next()) {
                activity = new ActivityBean();
                BeanUtil.populate(activity, rs);
                computeActivityState(activity, Utils.convert(Utils.getCurrentDate()));
                pst = null;
                pst = con.prepareStatement(GET_PEOPLE_ON_ACTIVITY);
                pst.clearParameters();
                pst.setInt(1, activity.getId());
                rs1 = pst.executeQuery();
                while (rs1.next()) {
                    person = new PersonBean();
                    skills = new Vector<SkillBean>();
                    BeanUtil.populate(person, rs1);
                    pst = null;
                    // Recupera competenze di ogni persona
                    pst = con.prepareStatement(GET_SKILLS_BY_PERSON);
                    pst.clearParameters();
                    pst.setInt(1, projId);
                    pst.setInt(2, person.getId());
                    rs2 = pst.executeQuery();
                    while (rs2.next()) {
                        SkillBean skill = new SkillBean();
                        BeanUtil.populate(skill, rs2);
                        skills.add(skill);
                    }
                    person.setCompetenze(skills);
                    people.add(person);
                }
                activity.setPersone(people);
            }
            return activity;
        } catch (AttributoNonValorizzatoException anve) {
            String msg = FOR_NAME + "Oggetto PersonBean non valorizzato; problema nella query dell\'attivita\'.\n";
            LOG.severe(msg); 
            throw new WebStorageException(msg + anve.getMessage(), anve);
        } catch (SQLException sqle) {
            String msg = FOR_NAME + "Oggetto ActivityBean non valorizzato; problema nella query dell\'attivita\'.\n";
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
     * <p>Restituisce un Vector di SkillBean 
     * rappresentante le competenze del progetto corrente.</p>
     * <p>Se l'utente &egrave; un PMO di Ateneo baipassa il controllo
     * sul fatto che l'utente abbia i diritti di scrittura sul progetto
     * corrispondente all'identificativo passato. Infatti i PMOATE possono
     * assegnare competenze agli utenti in qualit&agrave; di amministratori che
     * accedenon ad una speecifica funzionalit&agrave; ammistrativa degli
     * utenti stessi, e non devono necessariamente essere inseriti sul 
     * progetto per poterne assegnare le competenze. Tutti gli altri ruoli,
     * invece, devono essere inseriti sul progetto per poterne visualizzare
     * le competenze.</p>
     * 
     * @param projId id del progetto di cui estrarre le competenze
     * @param user utente loggato
     * @return <code>Vector&lt;SkillBean&gt;</code> - SkillBean rappresentante le competenze del progetto.
     * @throws WebStorageException se si verifica un problema nell'esecuzione della query, nell'accesso al db o in qualche tipo di puntamento
     */
    @SuppressWarnings({ "null" })
    public Vector<SkillBean> getSkills(int projId,
                                       PersonBean user)
                                throws WebStorageException {
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement pst = null;
        SkillBean competenza = null;
        Vector<SkillBean> skills = new Vector<SkillBean>();
        try {
            con = pol_manager.getConnection();
            /* Per prima cosa verifica che l'utente abbia i diritti di accesso al progetto */
            boolean userCanReset = false;
            // Recupera i ruoli dell'utente
            Vector<CodeBean> ruoliUsr = user.getRuoli();
            // Se l'utente è di tipo PMO carica gli utenti su cui può agire
            for (CodeBean ruoloUsr : ruoliUsr) {
                if (ruoloUsr.getOrdinale() == 1) {
                    userCanReset = true;
                    break;
                }
            }
            if (!userCanReset) {
                if (!userCanRead(projId, user.getId())) {
                    String msg = FOR_NAME + "Qualcuno ha tentato di inserire un indirizzo nel browser avente un id progetto non valido!.\n";
                    LOG.severe(msg + "E\' presente il parametro \"q=act\" ma non un valore \"id\" - cioe\' id progetto - significativo!\n");
                    throw new WebStorageException("Attenzione: indirizzo richiesto non valido!\n");
                }
            }
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
        } catch (AttributoNonValorizzatoException anve) {
            String msg = FOR_NAME + "Oggetto PersonBean non valorizzato; problema nella query dell\'attivita\'.\n";
            LOG.severe(msg); 
            throw new WebStorageException(msg + anve.getMessage(), anve);
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
     * <p>Restituisce un Vector di RiskBean 
     * rappresentante i rischi del progetto corrente.</p>
     * 
     * @param projId id del progetto di cui estrarre i rischi
     * @param user utente loggato
     * @return <code>Vector&lt;RiskBean&gt;</code> - RiskBean rappresentante i rischi del progetto.
     * @throws WebStorageException se si verifica un problema nell'esecuzione della query, nell'accesso al db o in qualche tipo di puntamento
     */
    @SuppressWarnings({ "null" })
    public Vector<RiskBean> getRisks(int projId, 
                                     PersonBean user)
                              throws WebStorageException {
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement pst = null;
        RiskBean rischio = null;
        Vector<RiskBean> risks = new Vector<RiskBean>();
        try {
            con = pol_manager.getConnection();
            // Per prima cosa verifica che l'utente abbia i diritti di accesso al progetto
            if (!userCanRead(projId, user.getId())) {
                String msg = FOR_NAME + "Qualcuno ha tentato di inserire un indirizzo nel browser avente un id progetto non valido!.\n";
                LOG.severe(msg + "E\' presente il parametro \"q=act\" ma non un valore \"id\" - cioe\' id progetto - significativo!\n");
                throw new WebStorageException("Attenzione: indirizzo richiesto non valido!\n");
            }
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
        } catch (AttributoNonValorizzatoException anve) {
            String msg = FOR_NAME + "Oggetto PersonBean non valorizzato; problema nella query dell\'attivita\'.\n";
            LOG.severe(msg); 
            throw new WebStorageException(msg + anve.getMessage(), anve);
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
     * <p>Restituisce un Vector di IndicatorBean rappresentante tutti
     * gli indicatori del progetto attuale.</p>
     * <p>Pu&ograve; essere usato &quot;a valle&quot; dell'identificazione
     * di identificativi di progetto su cui l'utente sicuramente pu&ograve; 
     * scrivere, per recuperare tutti gli indicatori degli stessi,
     * senza effettuare ulteriori controlli o filtri (se un utente pu&ograve;
     * scrivere su un progetto, potr&agrave; anche editarne gli indicatori,
     * dati i ruoli configurati nell'applicazione <code>pol</code>).</p>
     * 
     * @param justOne id del tipo di indicatore che si vuole estrarre; se vale -1, e anche il parametro successivo vale -1, vengono estratti tutti i tipi di indicatori
     * @param getAll  intero usato come clausola in OR in funzione del cui valore vengono estratti solo i tipi di un dato id oppure tutti i tipi
     * @return <code>Vector&lt;IndicatorBean&gt;</code> - IndicatorBean rappresentante l'indicatore del progetto.
     * @throws WebStorageException se si verifica un problema nell'esecuzione delle query, nell'accesso al db o in qualche tipo di puntamento 
     */
    @SuppressWarnings({ "null", "static-method" })
    public LinkedList<CodeBean> getIndicatorTypes(int justOne, 
                                                  int getAll) 
                                                  throws WebStorageException {
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement pst = null;
        CodeBean type = null;
        LinkedList<CodeBean> types = new LinkedList<CodeBean>();
        try {
            con = pol_manager.getConnection();
            pst = con.prepareStatement(GET_INDICATOR_TYPES);
            pst.clearParameters();
            pst.setInt(1, justOne);
            pst.setInt(2, getAll); 
            rs = pst.executeQuery();
            while (rs.next()) {
                type = new CodeBean();
                BeanUtil.populate(type, rs);
                types.add(type);
            }
            return types;
        } catch (ClassCastException cce) {
            String msg = FOR_NAME + "Oggetto tipo indicatore non corrispondente; problema nella query dei tipi di indicatori.\n";
            LOG.severe(msg); 
            throw new WebStorageException(msg + cce.getMessage(), cce);
        } catch (SQLException sqle) {
            String msg = FOR_NAME + "Oggetto tipo indicatore non valorizzato; problema nella query dei tipi di indicatore.\n";
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
     * <p>Restituisce un Vector di IndicatorBean rappresentante tutti
     * gli indicatori del progetto attuale.</p>
     * <p>Pu&ograve; essere usato &quot;a valle&quot; dell'identificazione
     * di identificativi di progetto su cui l'utente sicuramente pu&ograve; 
     * scrivere, per recuperare tutti gli indicatori degli stessi,
     * senza effettuare ulteriori controlli o filtri (se un utente pu&ograve;
     * scrivere su un progetto, potr&agrave; anche editarne gli indicatori,
     * dati i ruoli configurati nell'applicazione <code>pol</code>).</p>
     * 
     * @param projId  id del progetto di cui estrarre gli indicatori
     * @param user utente loggato
     * @return <code>Vector&lt;IndicatorBean&gt;</code> - IndicatorBean rappresentante l'indicatore del progetto.
     * @throws WebStorageException se si verifica un problema nell'esecuzione delle query, nell'accesso al db o in qualche tipo di puntamento 
     */
    @SuppressWarnings({ "null" })
    public Vector<IndicatorBean> getIndicators(int projId,
                                               PersonBean user) 
                                        throws WebStorageException {
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement pst = null;
        IndicatorBean indicatore = null;
        Vector<IndicatorBean> indicatori = new Vector<IndicatorBean>();
        try {
            con = pol_manager.getConnection();
            // Per prima cosa verifica che l'utente abbia i diritti di accesso al progetto
            if (!userCanRead(projId, user.getId())) {
                String msg = FOR_NAME + "Qualcuno ha tentato di inserire un indirizzo nel browser avente un id progetto non valido!.\n";
                LOG.severe(msg + "E\' presente il parametro \"q=act\" ma non un valore \"id\" - cioe\' id progetto - significativo!\n");
                throw new WebStorageException("Attenzione: indirizzo richiesto non valido!\n");
            }
            pst = con.prepareStatement(GET_INDICATORS);
            pst.clearParameters();
            pst.setInt(1, projId);
            rs = pst.executeQuery();
            while (rs.next()) {
                indicatore = new IndicatorBean();
                BeanUtil.populate(indicatore, rs);
                indicatori.add(indicatore);
            }
            return indicatori;
        } catch (AttributoNonValorizzatoException anve) {
            String msg = FOR_NAME + "Oggetto PersonBean non valorizzato; problema nella query degli indicatori.\n";
            LOG.severe(msg); 
            throw new WebStorageException(msg + anve.getMessage(), anve);
        } catch (SQLException sqle) {
            String msg = FOR_NAME + "Oggetto ActivityBean non valorizzato; problema nella query degli indicatori.\n";
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
     * <p>Restituisce un Vector di IndicatorBean rappresentante &ndash; a 
     * seconda dei valori dei flag passati come parametri &ndash; parte o tutti
     * gli indicatori del progetto attuale, corredati o meno di tutte
     * le relative misurazioni.</p>
     * <p>Pu&ograve; essere usato &quot;a valle&quot; dell'identificazione
     * di identificativi di progetto su cui l'utente sicuramente pu&ograve; 
     * scrivere, per recuperare tutti gli indicatori degli stessi,
     * senza effettuare ulteriori controlli o filtri (se un utente pu&ograve;
     * scrivere su un progetto, potr&agrave; anche editarne gli indicatori,
     * dati i ruoli configurati nell'applicazione <code>pol</code>).</p>
     * 
     * @param projId  id del progetto di cui estrarre gli indicatori
     * @param user utente loggato
     * @param date data baseline a partire dalla quale recuperare gli indicatori; se si vogliono recuperare indicatori di qualsivoglia data passare una data antidiluviana
     * @param getSpecificType tipo specifico di indicatore che si vuol recuperare
     * @param getSpecificState String specificante eventuale stato in cui si vogliono recuperare gli indicatori (p.es. 'APERTO' o 'CONCLUSO'); puo' essere usata dalla query per recuperare il relativo id
     * @param getAllState valore intero che, se vale -1, baipassa la selezione effettuata in base al nome dello stato passato tramite la String getSpecificState
     * @param getAll se si vogliono recuperare gli indicatori di tutti i tipi bisogna passare -1 sia sul parametro precedente che su questo; altrimenti bisogna passare l'id tipo su entrambi
     * @param getMeasuresToo flag specificante se bisogna recuperare anche le misurazioni di ogni indicatore (true) o non interessa (false)
     * @return <code>Vector&lt;IndicatorBean&gt;</code> - IndicatorBean rappresentante l'indicatore del progetto.
     * @throws WebStorageException se si verifica un problema nell'esecuzione delle query, nell'accesso al db o in qualche tipo di puntamento 
     */
    @SuppressWarnings({ "null" })
    public Vector<IndicatorBean> getIndicators(int projId,
                                               PersonBean user,
                                               Date date,
                                               int getSpecificType,
                                               String getSpecificState,
                                               int getAllState,
                                               int getAll,
                                               boolean getMeasuresToo) 
                                        throws WebStorageException {
        ResultSet rs, rs1, rs2, rs3, rs4 = null;
        Connection con = null;
        PreparedStatement pst = null;
        IndicatorBean indicatore = null;
        WbsBean wbs = null;
        Vector<IndicatorBean> indicatori = new Vector<IndicatorBean>();
        int nextParam = 0;
        try {
            con = pol_manager.getConnection();
            // Per prima cosa verifica che l'utente abbia i diritti di accesso al progetto
            if (!userCanRead(projId, user.getId())) {
                String msg = FOR_NAME + "Qualcuno ha tentato di inserire un indirizzo nel browser avente un id progetto non valido!.\n";
                LOG.severe(msg + "E\' presente il parametro \"id\" - cioe\' id progetto - NON significativo!\n");
                throw new WebStorageException("Attenzione: indirizzo richiesto non valido!\n");
            }
            pst = con.prepareStatement(GET_INDICATORS_BY_DATE_AND_TYPE);
            pst.clearParameters();
            pst.setInt(++nextParam, projId);
            pst.setDate(++nextParam, Utils.convert(date));
            pst.setInt(++nextParam, getSpecificType);
            pst.setInt(++nextParam, getAll);
            pst.setString(++nextParam, getSpecificState);
            pst.setInt(++nextParam, getAllState);
            rs = pst.executeQuery();
            while (rs.next()) {
                indicatore = new IndicatorBean();
                BeanUtil.populate(indicatore, rs);
                // Recupera la wbs dell'indicatore
                pst = null;
                pst = con.prepareStatement(GET_WBS_ON_INDICATOR);
                pst.clearParameters();
                pst.setInt(1, indicatore.getId());
                rs1 = pst.executeQuery();
                if (rs1.next()) {
                    wbs = new WbsBean();
                    BeanUtil.populate(wbs, rs1);
                    indicatore.setWbs(wbs);
                }
                // Recupera il tipo dell'indicatore
                pst = null;
                pst = con.prepareStatement(GET_TYPE_ON_INDICATOR);
                pst.clearParameters();
                pst.setInt(1, indicatore.getIdTipo());
                rs2 = pst.executeQuery();
                if (rs2.next()) {
                    CodeBean tipo = new CodeBean();
                    BeanUtil.populate(tipo, rs2);
                    indicatore.setTipo(tipo);
                }
                if (getMeasuresToo) {
                    // Recupera tutte le misurazioni dell'indicatore, complete dei valori
                    Vector<MeasurementBean> misurazioni = new Vector<MeasurementBean>();
                    pst = null;
                    nextParam = 0;
                    pst = con.prepareStatement(GET_MEASURES_ON_INDICATOR);
                    pst.clearParameters();
                    pst.setInt(++nextParam, indicatore.getId());
                    pst.setInt(++nextParam, projId);
                    pst.setInt(++nextParam, projId);
                    rs3 = pst.executeQuery();
                    while (rs3.next()) {
                        MeasurementBean m = new MeasurementBean();
                        BeanUtil.populate(m, rs3);
                        misurazioni.add(m);
                    }
                    indicatore.setMisurazioni(misurazioni);
                }
                // Cerca eventuali revisioni fatte al target
                pst = null;
                pst = con.prepareStatement(GET_UPDATES_ON_INDICATOR);
                pst.clearParameters();
                pst.setInt(1, indicatore.getId());
                rs4 = pst.executeQuery();
                if (rs4.next()) {
                    MeasurementBean target = new MeasurementBean();
                    BeanUtil.populate(target, rs4);
                    indicatore.setTargetRivisto(target.getNome());
                    indicatore.setNoteRevisione(target.getDescrizione());
                    indicatore.setDataRevisione(target.getDataMisurazione());
                    indicatore.setAutoreUltimaRevisione(target.getAutoreUltimaModifica());
                }
                indicatori.add(indicatore);
                // Let's engage the Garbage Collector
                pst = null;
            }
            return indicatori;
        } catch (AttributoNonValorizzatoException anve) {
            String msg = FOR_NAME + "Oggetto PersonBean non valorizzato; problema nella query degli indicatori.\n";
            LOG.severe(msg); 
            throw new WebStorageException(msg + anve.getMessage(), anve);
        } catch (SQLException sqle) {
            String msg = FOR_NAME + "Oggetto IndicatorBean non valorizzato; problema nella query degli indicatori.\n";
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
     * <p>Restituisce un Vector di IndicatorBean rappresentante tutti
     * gli indicatori, nel contesto del progetto corrente, collegati
     * ad una wbs di dato id.</p>
     * Permette di recuperare tutti gli indicatori di una data wbs 
     * o anche di tutte le wbs, a seconda del combinato disposto dei
     * parametri passati. Se si vogliono recuperare gli indicatori 
     * di tutte le wbs bisogna passare un valore qualunque sul primo parametro 
     * e -1 sul secondo parametro; altrimenti bisogna passare l'id cercato 
     * sul primo parametro e un valore qualunque, diverso da -1 (ad esempio
     * anche lo stesso id della wbs voluta), sul secondo.</p>
     * <p>Esempio:<pre>
     * SELECT * FROM indicatore WHERE id_wbs = 1163 OR -1 = 1163;
     * 2    A + B + C + D   Descrizione di prova    4   2019    2020-06-10  8   2021/2022   2020-06-30  2020-06-11  13:39:00    Ciocchetta Giada    3   1163    40
     * 6   PerCent2         (null)                  0   (null)  2020-06-01  100 (null)      2020-06-30  2020-06-11  13:43:00    Ciocchetta Giada    3   1163    40
     * SELECT * FROM indicatore WHERE id_wbs = 1163 OR -1 = -1
     * 1   Corsi EL
     * 2   A + B + C + D
     * 3   Studenti Erasmus
     * 4   Indicatore per D.2.1
     * 5   PerCent
     * 6   PerCent2</pre></p>
     * <p>Inoltre, se &egrave; presente una revisione del target,
     * recupera anche quella, per mostrarla al posto del (o insieme al)
     * target originale.</p>
     * 
     * @param projId  id del progetto di cui estrarre gli indicatori
     * @param user utente loggato
     * @param idWbs identificativo della wbs di cui si vogliono recuperare tutti gli indicatori
     * @param getAll -1 se si vogliono ottenere tutti gli indicatori del progetto corrente, indipendentemente dalla wbs; un valore qualunque se non si vuole che il secondo parametro abbia effetto
     * @return <code>Vector&lt;IndicatorBean&gt;</code> - Vector di IndicatorBean rappresentante l'elenco di indicatori del progetto.
     * @throws WebStorageException se si verifica un problema nell'esecuzione delle query, nell'accesso al db o in qualche tipo di puntamento 
     */
    @SuppressWarnings({ "null" })
    public Vector<IndicatorBean> getIndicatorsByWbs(int projId,
                                                    PersonBean user,
                                                    //Date date,
                                                    int idWbs,
                                                    int getAll) 
                                             throws WebStorageException {
        ResultSet rs, rs1, rs2, rs3, rs4 = null;
        Connection con = null;
        PreparedStatement pst = null;
        IndicatorBean indicatore = null;
        WbsBean wbs = null;
        Vector<IndicatorBean> indicatori = new Vector<IndicatorBean>();
        Vector<MeasurementBean> misurazioni = null;
        int nextParam = 0;
        try {
            con = pol_manager.getConnection();
            // Per prima cosa verifica che l'utente abbia i diritti di accesso al progetto
            if (!userCanRead(projId, user.getId())) {
                String msg = FOR_NAME + "Qualcuno ha tentato di inserire un indirizzo nel browser avente un id progetto non valido!.\n";
                LOG.severe(msg + "E\' presente il parametro \"id\" - cioe\' id progetto - NON significativo!\n");
                throw new WebStorageException("Attenzione: indirizzo richiesto non valido!\n");
            }
            pst = con.prepareStatement(GET_INDICATORS_BY_WBS);
            pst.clearParameters();
            pst.setInt(++nextParam, projId);
            //pst.setDate(++nextParam, Utils.convert(date));
            pst.setInt(++nextParam, idWbs);
            pst.setInt(++nextParam, getAll);
            rs = pst.executeQuery();
            while (rs.next()) {
                indicatore = new IndicatorBean();
                BeanUtil.populate(indicatore, rs);
                // Recupera la wbs dell'indicatore
                pst = null;
                pst = con.prepareStatement(GET_WBS_ON_INDICATOR);
                pst.clearParameters();
                pst.setInt(1, indicatore.getId());
                rs1 = pst.executeQuery();
                if (rs1.next()) {
                    wbs = new WbsBean();
                    BeanUtil.populate(wbs, rs1);
                    indicatore.setWbs(wbs);
                }
                // Recupera il tipo dell'indicatore
                pst = null;
                pst = con.prepareStatement(GET_TYPE_ON_INDICATOR);
                pst.clearParameters();
                pst.setInt(1, indicatore.getIdTipo());
                rs2 = pst.executeQuery();
                if (rs2.next()) {
                    CodeBean tipo = new CodeBean();
                    BeanUtil.populate(tipo, rs2);
                    indicatore.setTipo(tipo);
                }
                // Recupera le misurazioni dell'indicatore
                pst = null;
                nextParam = 0;
                misurazioni = new Vector<MeasurementBean>();
                pst = con.prepareStatement(GET_MEASURES_ON_INDICATOR);
                pst.clearParameters();
                pst.setInt(++nextParam, indicatore.getId());
                pst.setInt(++nextParam, projId);
                pst.setInt(++nextParam, projId);
                rs3 = pst.executeQuery();
                while (rs3.next()) {
                    MeasurementBean m = new MeasurementBean();
                    BeanUtil.populate(m, rs3);
                    misurazioni.add(m);
                }
                indicatore.setMisurazioni(misurazioni);
                // Cerca eventuali revisioni fatte al target
                pst = null;
                pst = con.prepareStatement(GET_UPDATES_ON_INDICATOR);
                pst.clearParameters();
                pst.setInt(1, indicatore.getId());
                rs4 = pst.executeQuery();
                if (rs4.next()) {
                    MeasurementBean target = new MeasurementBean();
                    BeanUtil.populate(target, rs4);
                    indicatore.setTargetRivisto(target.getNome());
                    indicatore.setNoteRevisione(target.getDescrizione());
                    indicatore.setDataRevisione(target.getDataMisurazione());
                    indicatore.setAutoreUltimaRevisione(target.getAutoreUltimaModifica());
                }
                indicatori.add(indicatore);
                // Let's engage the Garbage Collector
                pst = null;
            }
            return indicatori;
        } catch (AttributoNonValorizzatoException anve) {
            String msg = FOR_NAME + "Oggetto PersonBean non valorizzato; problema nella query degli indicatori.\n";
            LOG.severe(msg); 
            throw new WebStorageException(msg + anve.getMessage(), anve);
        } catch (SQLException sqle) {
            String msg = FOR_NAME + "Oggetto IndicatorBean non valorizzato; problema nella query degli indicatori.\n";
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
     * <p>Restituisce un oggetto IndicatorBean rappresentante un indicatore 
     * che deve appartenere al progetto corrente - che a sua volta deve 
     * essere leggibile dall'utente - e avente identificativo 
     * passato come argomento.</p>
     * 
     * @param projId     id del progetto di cui estrarre le attivit&agrave;
     * @param indicatorId id dell'indicatore che si vuol recuperare
     * @param user       utente loggato
     * @return <code>IndicatorBean&gt;</code> - IndicatorBean rappresentante l'indicatore del progetto avente identificativo passato come parametro e avente id passato come parametro.
     * @throws WebStorageException se si verifica un problema nell'esecuzione delle query, nell'accesso al db o in qualche tipo di puntamento 
     */
    @SuppressWarnings({ "null" })
    public IndicatorBean getIndicator(int projId,
                                      int indicatorId,
                                      PersonBean user) 
                               throws WebStorageException {
        ResultSet rs, rs1, rs2, rs3 = null;
        Connection con = null;
        PreparedStatement pst = null;
        IndicatorBean indicator = null;
        try {
            con = pol_manager.getConnection();
            // Per prima cosa verifica che l'utente abbia i diritti di accesso al progetto
            if (!userCanRead(projId, user.getId())) {
                String msg = FOR_NAME + "Qualcuno ha tentato di inserire un indirizzo nel browser avente un id progetto non valido!.\n";
                LOG.severe(msg + "E\' presente il parametro \"q=act\" ma non un valore \"id\" - cioe\' id progetto - significativo!\n");
                throw new WebStorageException("Attenzione: indirizzo richiesto non valido!\n");
            }
            pst = con.prepareStatement(GET_INDICATOR);
            pst.clearParameters();
            pst.setInt(1, projId);
            pst.setInt(2, indicatorId);
            rs = pst.executeQuery();
            if (rs.next()) {
                indicator = new IndicatorBean();
                BeanUtil.populate(indicator, rs);
                int id = indicator.getId();
                int idType = indicator.getIdTipo();
                pst = null;
                pst = con.prepareStatement(GET_TYPE_ON_INDICATOR);
                pst.clearParameters();
                pst.setInt(1, idType);
                rs1 = pst.executeQuery();
                if (rs1.next()) {
                    CodeBean type = new CodeBean();
                    BeanUtil.populate(type, rs1);
                    indicator.setTipo(type);
                }
                pst = null;
                pst = con.prepareStatement(GET_WBS_ON_INDICATOR);
                pst.clearParameters();
                pst.setInt(1, id);
                rs2 = pst.executeQuery();
                if (rs2.next()) {
                    WbsBean action = new WbsBean();
                    BeanUtil.populate(action, rs2);
                    indicator.setWbs(action);
                }
                // Cerca eventuali revisioni fatte al target dell'indicatore
                pst = null;
                pst = con.prepareStatement(GET_UPDATES_ON_INDICATOR);
                pst.clearParameters();
                pst.setInt(1, indicator.getId());
                rs3 = pst.executeQuery();
                if (rs3.next()) {
                    MeasurementBean target = new MeasurementBean();
                    BeanUtil.populate(target, rs3);
                    indicator.setTargetRivisto(target.getNome());
                    indicator.setNoteRevisione(target.getDescrizione());
                    indicator.setDataRevisione(target.getDataMisurazione());
                    indicator.setAutoreUltimaRevisione(target.getAutoreUltimaModifica());
                }
            }
            return indicator;
        } catch (AttributoNonValorizzatoException anve) {
            String msg = FOR_NAME + "Oggetto Bean non valorizzato; problema nella query dell\'indicatore'.\n";
            LOG.severe(msg); 
            throw new WebStorageException(msg + anve.getMessage(), anve);
        } catch (SQLException sqle) {
            String msg = FOR_NAME + "Oggetto Bean non valorizzato; problema nella query dell\'indicatore.\n";
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
     * <p>Restituisce un Vector di MeasurementBean rappresentante &ndash; a 
     * seconda dei valori dei flag passati come parametri &ndash; parte o tutte
     * le misurazioni del progetto attuale o di tutti i progetti, corredate di 
     * tutte le relative informazioni (indicatore di appartenenza, wbs, allegati,
     * etc.).</p>
     * <p>Pu&ograve; essere usato per recuperare tutte le misurazioni di
     * tutti gli indicatori o di uno specifico indicatore, o di tutti i 
     * progetti o di uno specifico progetto, a seconda dei flag passati
     * come parametri.</p>
     * 
     * @param projId  id del progetto di cui estrarre le misurazioni
     * @param user utente loggato
     * @param date data misurazionie a partire dalla quale recuperare le misurazioni; se si vogliono recuperare le misurazioni di qualsivoglia data passare una data antidiluviana
     * @param indicatorId identificativo indicatore di cui si vogliono recuperare le misurazioni
     * @param indicatorFlag se si vogliono recuperare tutte le misurazioni di tutti gli indicatori basta passare -1 su questo parametro; altrimenti bisogna passare l'id indicatore su entrambi
     * @param projFlag se si vogliono recuperare tutte le misurazioni di tutti i progetti basta passare -1 su questo parametro, indipendentemente dal valore di projId
     * @return <code>Vector&lt;MeasurementBean&gt;</code> - Vector di MeasurementBean rappresentante le misurazioni cercate.
     * @throws WebStorageException se si verifica un problema nell'esecuzione delle query, nell'accesso al db o in qualche tipo di puntamento 
     */
    @SuppressWarnings({ "null" })
    public Vector<MeasurementBean> getMeasures(int projId,
                                               PersonBean user,
                                               Date date,
                                               int indicatorId,
                                               int indicatorFlag,
                                               int projFlag) 
                                        throws WebStorageException {
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement pst = null;
        MeasurementBean m = null;
        IndicatorBean indicator = null;
        Vector<MeasurementBean> measures = new Vector<MeasurementBean>();
        Vector<FileDocBean> attachments = null;
        int nextParam = 0;
        try {
            con = pol_manager.getConnection();
            // Per prima cosa verifica che l'utente abbia i diritti di accesso al progetto
            if (!userCanRead(projId, user.getId())) {
                String msg = FOR_NAME + "Qualcuno ha tentato di inserire un indirizzo nel browser avente un id progetto non valido!.\n";
                LOG.severe(msg + "E\' presente il parametro \"id\" - cioe\' id progetto - NON significativo!\n");
                throw new WebStorageException("Attenzione: indirizzo richiesto non valido!\n");
            }
            pst = con.prepareStatement(GET_MEASURES);
            pst.clearParameters();
            pst.setInt(++nextParam, indicatorId);
            pst.setInt(++nextParam, indicatorFlag);
            pst.setInt(++nextParam, projId);
            pst.setInt(++nextParam, projFlag);
            pst.setDate(++nextParam, Utils.convert(date));
            rs = pst.executeQuery();
            while (rs.next()) {
                m = new MeasurementBean();
                BeanUtil.populate(m, rs);
                indicator = getIndicator(projId, m.getOrdinale(), user);
                m.setIndicatore(indicator);
                // Recupera gli allegati
                try {
                    attachments = getFileDoc("indicatoregestione", "all", m.getId(), NOTHING);
                    m.setAllegati(attachments);
                } catch (AttributoNonValorizzatoException anve) {
                    String msg = FOR_NAME + "Oggetto MeasurementBean.id non valorizzato; problema nella query che recupera l\'allegato della misurazione attraverso l\'id.\n";
                    LOG.severe(msg); 
                    throw new WebStorageException(msg + anve.getMessage(), anve);
                }
                measures.add(m);
            }
            return measures;
        } catch (AttributoNonValorizzatoException anve) {
            String msg = FOR_NAME + "Oggetto non valorizzato; problema nella query delle misurazioni.\n";
            LOG.severe(msg); 
            throw new WebStorageException(msg + anve.getMessage(), anve);
        } catch (SQLException sqle) {
            String msg = FOR_NAME + "Problema SQL nella query delle misurazioni.\n";
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
     * <p>Restituisce esattamente un MeasurementBean, rappresentante 
     * una misurazione avente id passato come parametro, e corredato
     * degli eventuali allegati.</p>
     * 
     * @param projId  id cui la misurazione cercata deve afferire
     * @param measureId identificativo della misurazione cercata
     * @param user utente loggato
     * @return <code>MeasurementBean</code> - misurazione cercata.
     * @throws WebStorageException se si verifica un problema nell'esecuzione delle query, nell'accesso al db o in qualche tipo di puntamento 
     */
    @SuppressWarnings({ "null" })
    public MeasurementBean getMeasure(int projId,
                                      int measureId,
                                      PersonBean user) 
                               throws WebStorageException {
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement pst = null;
        MeasurementBean m = null;
        IndicatorBean indicator = null;
        Vector<FileDocBean> attachments = null;
        int nextParam = 0;
        try {
            con = pol_manager.getConnection();
            // Per prima cosa verifica che l'utente abbia i diritti di accesso al progetto
            if (!userCanRead(projId, user.getId())) {
                String msg = FOR_NAME + "Qualcuno ha tentato di inserire un indirizzo nel browser avente un id progetto non valido!.\n";
                LOG.severe(msg + "E\' presente il parametro \"id\" - cioe\' id progetto - NON significativo!\n");
                throw new WebStorageException("Attenzione: indirizzo richiesto non valido!\n");
            }
            pst = con.prepareStatement(GET_MEASURE);
            pst.clearParameters();
            pst.setInt(++nextParam, measureId);
            pst.setInt(++nextParam, projId);
            rs = pst.executeQuery();
            while (rs.next()) {
                m = new MeasurementBean();
                BeanUtil.populate(m, rs);
                indicator = getIndicator(projId, m.getOrdinale(), user);
                m.setIndicatore(indicator);
                // Recupero e settaggio degli allegati alla misurazione
                try {
                    attachments = getFileDoc("indicatoregestione", "all", m.getId(), NOTHING);
                    m.setAllegati(attachments);
                } catch (AttributoNonValorizzatoException anve) {
                    String msg = FOR_NAME + "Oggetto MeasurementBean.id non valorizzato; problema nella query che recupera l\'allegato della misurazione attraverso l\'id.\n";
                    LOG.severe(msg); 
                    throw new WebStorageException(msg + anve.getMessage(), anve);
                }
            }
            return m;
        } catch (AttributoNonValorizzatoException anve) {
            String msg = FOR_NAME + "Oggetto non valorizzato; problema nella query delle misurazioni.\n";
            LOG.severe(msg); 
            throw new WebStorageException(msg + anve.getMessage(), anve);
        } catch (SQLException sqle) {
            String msg = FOR_NAME + "Problema SQL nella query delle misurazioni.\n";
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
     * <p>Restituisce una specifica WBS del progetto, con identificativo progetto
     * e identificativo WBS passati come parametri.</p>
     * 
     * @param idProj   id del progetto di cui caricare le wbs
     * @param idWbs    id della wbs da estrarre
     * @param user     utente loggato
     * @return WbsBean - bean contenente la wbs richiesta
     * @throws WebStorageException se si verifica un problema nell'esecuzione della query, nell'accesso al db o in qualche tipo di puntamento
     */
    @SuppressWarnings({ "null" })
    public WbsBean getWbsInstance(int idProj, 
                                  int idWbs, 
                                  PersonBean user) 
                           throws WebStorageException {
        ResultSet rs, rs1 = null;
        Connection con = null;
        PreparedStatement pst = null;
        WbsBean wbs = null;
        WbsBean wbsPadre = null;
        try {
            // Ottiene il progetto precaricato quando l'utente si è loggato corrispondente al progetto sul quale aggiungere un'attività
            Integer keyPrj = new Integer(idProj);
            Integer keyWbs = new Integer(idWbs);
            con = pol_manager.getConnection();
            // Per prima cosa verifica che l'utente abbia i diritti di accesso al progetto
            if (!userCanRead(idProj, user.getId())) {
                String msg = FOR_NAME + "Qualcuno ha tentato di inserire un indirizzo nel browser avente un id progetto non valido!.\n";
                LOG.severe(msg + "E\' presente il parametro \"q=act\" ma non un valore \"id\" - cioe\' id progetto - significativo!\n");
                throw new WebStorageException("Attenzione: indirizzo richiesto non valido!\n");
            }
            pst = con.prepareStatement(GET_WBS);
            pst.clearParameters();
            pst.setInt(1, keyPrj);
            pst.setInt(2, keyWbs);
            rs = pst.executeQuery();
            if (rs.next()) {
                wbs = new WbsBean();
                BeanUtil.populate(wbs, rs);
                pst = null;
                pst = con.prepareStatement(GET_WBS_PADRE);
                pst.clearParameters();
                pst.setInt(1, wbs.getId());
                rs1 = pst.executeQuery();
                if (rs1.next()) {
                    wbsPadre = new WbsBean();
                    BeanUtil.populate(wbsPadre, rs1);
                    wbs.setWbsPadre(wbsPadre);
                }
            }
            return wbs;
        } catch (AttributoNonValorizzatoException anve) {
            String msg = FOR_NAME + "Oggetto WbsBean non valorizzato; problema nella query della wbs del figlio.\n";
            LOG.severe(msg); 
            throw new WebStorageException(msg + anve.getMessage(), anve);
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
     * <p>Restituisce un vector contenente tutte le Wbs di un dato progetto.</p>
     * 
     * @param idProj    id del progetto di cui caricare le wbs
     * @param user      utente loggato
     * @param getPartOfWbs flag specificante se bisogna recuperare solo i WorkPackage, solo le WBS oppure le WBS e i Workpackage
     * @return <code>Vector&lt;WbsBean&gt;</code> - vettore contenente tutte le Wbs di un progetto
     * @throws WebStorageException se si verifica un problema nell'esecuzione della query, nell'accesso al db o in qualche tipo di puntamento
     */
    @SuppressWarnings({ "null" })
    public Vector<WbsBean> getWbs(int idProj,
                                  PersonBean user,
                                  int getPartOfWbs) 
                           throws WebStorageException {
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement pst = null;
        WbsBean wbs = null;
        Vector<WbsBean> vWbs = new Vector<WbsBean>();
        String query = null;
        // Controllo qual è la query da eseguire, in base alla richiesta dell'utente
        switch (getPartOfWbs) {
            case WBS_GET_ALL: 
                query = GET_WBS_BY_PROJECT; 
                break;
            case WBS_BUT_WP:
                query = GET_WBS_NOT_WORKPACKAGE;
                break;
            case WBS_WP_ONLY:
                query = GET_WP_BY_PROJECT;
                break;
            default:
                throw new WebStorageException("Query non selezionata dall'utente.\n");
        }
        try {
            // Ottiene il progetto precaricato quando l'utente si è loggato corrispondente al progetto sul quale aggiungere un'attività
            Integer key = new Integer(idProj);
            con = pol_manager.getConnection();
            // Per prima cosa verifica che l'utente abbia i diritti di accesso al progetto
            if (!userCanRead(idProj, user.getId())) {
                String msg = FOR_NAME + "Qualcuno ha tentato di inserire un indirizzo nel browser avente un id progetto non valido!.\n";
                LOG.severe(msg + "E\' presente il parametro \"q=act\" ma non un valore \"id\" - cioe\' id progetto - significativo!\n");
                throw new WebStorageException("Attenzione: indirizzo richiesto non valido!\n");
            }
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
        } catch (AttributoNonValorizzatoException anve) {
            String msg = FOR_NAME + "Oggetto WbsBean non valorizzato; problema nella query della wbs del figlio.\n";
            LOG.severe(msg); 
            throw new WebStorageException(msg + anve.getMessage(), anve);
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
     * <p>Restituisce un vector contenente tutte le Wbs padri di un dato progetto,
     * ciascuna contenente tutte le proprie wbs figlie.</p>
     * 
     * @param idProj   id del progetto di cui caricare le wbs
     * @param user     utente loggato
     * @return vectorWbs - vettore contenente tutte la gerarchia di Wbs di un progetto
     * @throws WebStorageException se si verifica un problema nell'esecuzione della query, nell'accesso al db o in qualche tipo di puntamento
     */
    @SuppressWarnings({ "null" })
    public Vector<WbsBean> getWbsHierarchy(int idProj,
                                           PersonBean user) 
                           throws WebStorageException {
        ResultSet rs, rs1, rs2, rs3, rs4 = null;
        Connection con = null;
        PreparedStatement pst = null;
        WbsBean wbsP, wbsF, wbsN, wbsPN, wbsPPN = null;
        Vector<WbsBean> vWbsP = new Vector<WbsBean>();
        Vector<WbsBean> vWbsF = null;
        Vector<WbsBean> vWbsN = null;
        Vector<WbsBean> vWbsPN = null;
        Vector<WbsBean> vWbsPPN = null;
        try {
            con = pol_manager.getConnection();
            // Per prima cosa verifica che l'utente abbia i diritti di accesso al progetto
            if (!userCanRead(idProj, user.getId())) {
                String msg = FOR_NAME + "Qualcuno ha tentato di inserire un indirizzo nel browser avente un id progetto non valido!.\n";
                LOG.severe(msg + "E\' presente il parametro \"q=act\" ma non un valore \"id\" - cioe\' id progetto - significativo!\n");
                throw new WebStorageException("Attenzione: indirizzo richiesto non valido!\n");
            }
            pst = con.prepareStatement(GET_TOP_WBS_BY_PROJECT);
            pst.clearParameters();
            pst.setInt(1, idProj);
            rs = pst.executeQuery();
            // Valorizza lista padri
            while (rs.next()) {
                wbsP = new WbsBean();
                BeanUtil.populate(wbsP, rs);
                vWbsF = new Vector<WbsBean>();
                pst = null;
                pst = con.prepareStatement(GET_WBS_FIGLIE);
                pst.clearParameters();
                pst.setInt(1, idProj);
                pst.setInt(2, wbsP.getId());
                rs1 = pst.executeQuery();
                // Valorizza lista figli
                while (rs1.next()) {
                    wbsF = new WbsBean();
                    BeanUtil.populate(wbsF, rs1);
                    wbsF.setWbsPadre(wbsP);
                    vWbsN = new Vector<WbsBean>();
                    pst = null;
                    pst = con.prepareStatement(GET_WBS_FIGLIE);
                    pst.clearParameters();
                    pst.setInt(1, idProj);
                    pst.setInt(2, wbsF.getId());
                    rs2 = pst.executeQuery();
                    // Valorizza lista nipoti
                    while (rs2.next()) {
                        wbsN = new WbsBean();
                        BeanUtil.populate(wbsN, rs2);
                        wbsN.setWbsPadre(wbsF);
                        vWbsPN = new Vector<WbsBean>();
                        pst = null;
                        pst = con.prepareStatement(GET_WBS_FIGLIE);
                        pst.clearParameters();
                        pst.setInt(1, idProj);
                        pst.setInt(2, wbsN.getId());
                        rs3 = pst.executeQuery();
                        // Valorizza lista pronipoti
                        while (rs3.next()) {
                            wbsPN = new WbsBean();
                            BeanUtil.populate(wbsPN, rs3);
                            wbsPN.setWbsPadre(wbsN);
                            vWbsPPN = new Vector<WbsBean>();
                            pst = null;
                            pst = con.prepareStatement(GET_WBS_FIGLIE);
                            pst.clearParameters();
                            pst.setInt(1, idProj);
                            pst.setInt(2, wbsPN.getId());
                            rs4 = pst.executeQuery();
                            // Valorizza lista propronipoti
                            while (rs4.next()) {
                                wbsPPN = new WbsBean();
                                BeanUtil.populate(wbsPPN, rs4);
                                wbsPPN.setWbsPadre(wbsPN);
                                Vector<ActivityBean> wbsAct = new Vector<ActivityBean>(getActivitiesAmountByWbs(idProj, wbsPPN.getId(), user));
                                wbsPPN.setAttivita(wbsAct);
                                vWbsPPN.add(wbsPPN);
                            }
                            wbsPN.setWbsFiglie(vWbsPPN);
                            Vector<ActivityBean> wbsAct = new Vector<ActivityBean>(getActivitiesAmountByWbs(idProj, wbsPN.getId(), user));
                            wbsPN.setAttivita(wbsAct);
                            vWbsPN.add(wbsPN);
                        }
                        wbsN.setWbsFiglie(vWbsPN);
                        Vector<ActivityBean> wbsAct = new Vector<ActivityBean>(getActivitiesAmountByWbs(idProj, wbsN.getId(), user));
                        wbsN.setAttivita(wbsAct);
                        vWbsN.add(wbsN);
                    }
                    wbsF.setWbsFiglie(vWbsN);
                    Vector<ActivityBean> wbsAct = new Vector<ActivityBean>(getActivitiesAmountByWbs(idProj, wbsF.getId(), user));
                    wbsF.setAttivita(wbsAct);
                    MeasurementBean statoWbs = getWbsState(idProj, wbsF.getId());
                    wbsF.setStato(statoWbs);
                    vWbsF.add(wbsF);
                }
                wbsP.setWbsFiglie(vWbsF);
                Vector<ActivityBean> wbsAct = new Vector<ActivityBean>(getActivitiesAmountByWbs(idProj, wbsP.getId(), user));
                wbsP.setAttivita(wbsAct);
                MeasurementBean statoWbs = getWbsState(idProj, wbsP.getId());
                wbsP.setStato(statoWbs);
                vWbsP.add(wbsP);
            }
            return vWbsP;
        } catch (AttributoNonValorizzatoException anve) {
            String msg = FOR_NAME + "id WBS padre non valorizzato; problema nella query delle WBS figlie.\n";
            LOG.severe(msg); 
            throw new WebStorageException(msg + anve.getMessage(), anve);
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
     * <p>Restituisce un vector contenente tutte le Wbs padri di un dato progetto,
     * ciascuna contenente tutte le proprie wbs figlie, con la condizione che
     * ogni WBS abbia collegata almeno un'attivit&agrave; con data fine
     * (effettiva o prevista, in cascata) maggiore del 1° gennaio di un anno
     * passato come parametro.</p>
     * <p>In dettaglio: il metodo recupera le WBS top livello (root dell'albero
     * ovvero nodo 0) e le esamina subito (comportamento greedy):<ul> 
     * <li>se la WBS root &egrave; un WorkPackage allora: 
     * <ul>
     *  <li>o non ha attivit&agrave; collegate, e in tal caso va tenuta</li>
     *  <li>o ha attivit&agrave; collegate, e in tal caso va valutato se
     * almeno una di queste attivit&agrave; ha un punto di contatto con l'anno
     * passato come parametro. Un &quot;punto di contatto&quot; vuol dire
     * che l'attivit&agrave; si &egrave; svolta almeno per un giorno durante
     * l'anno desiderato.
     *  <ul>
     *      <li>se almeno un'attivit&agrave; ha un punto di contatto con l'anno
     * passato come parametro, allora la WBS va tenuta</li>
     *      <li>se neanche un'attivit&agrve; ha un punto di contatto con l'anno
     * in questione, allora la WBS va scartata</li>
     *  </ul>
     * </ul>
     * <li>se la WBS root non &egrave; un WorkPackage allora &egrave; una WBS
     * padre e va tenuta, a meno che nessuno dei suoi figli abbia almeno
     * un motivo per essere presente (v. criteri sopra).</li></ul>
     * 
     * @param idProj    id del progetto di cui caricare le wbs
     * @param user      utente loggato
     * @param year      anno entro cui deve trovarsi la data di fine di almeno un'attività della WBS affinche' la WBS stessa sia visibile
     * @return vectorWbs - vettore contenente tutte la gerarchia di Wbs di un progetto
     * @throws WebStorageException se si verifica un problema nell'esecuzione della query, nell'accesso al db o in qualche tipo di puntamento
     */
    @SuppressWarnings({ "null" })
    public Vector<WbsBean> getWbsHierarchy(int idProj,
                                           PersonBean user,
                                           int year) 
                           throws WebStorageException {
        ResultSet rs, rs1, rs2, rs3, rs4 = null;
        Connection con = null;
        PreparedStatement pst = null;
        WbsBean wbsP, wbsF, wbsN, wbsPN, wbsPPN = null;
        Vector<WbsBean> vWbsP = new Vector<WbsBean>();
        Vector<WbsBean> vWbsF = null;
        Vector<WbsBean> vWbsN = null;
        Vector<WbsBean> vWbsPN = null;
        Vector<WbsBean> vWbsPPN = null;
        try {
            con = pol_manager.getConnection();
            // Per prima cosa verifica che l'utente abbia i diritti di accesso al progetto
            if (!userCanRead(idProj, user.getId())) {
                String msg = FOR_NAME + "Qualcuno ha tentato di inserire un indirizzo nel browser avente un id progetto non valido!.\n";
                LOG.severe(msg + "E\' presente il parametro \"q=act\" ma non un valore \"id\" - cioe\' id progetto - significativo!\n");
                throw new WebStorageException("Attenzione: indirizzo richiesto non valido!\n");
            }
            pst = con.prepareStatement(GET_TOP_WBS_BY_PROJECT);
            pst.clearParameters();
            pst.setInt(1, idProj);
            rs = pst.executeQuery();
            // Valorizza lista padri
            while (rs.next()) {
                wbsP = new WbsBean();
                BeanUtil.populate(wbsP, rs);
                vWbsF = new Vector<WbsBean>();
                pst = null;
                pst = con.prepareStatement(GET_WBS_FIGLIE);
                pst.clearParameters();
                pst.setInt(1, idProj);
                pst.setInt(2, wbsP.getId());
                rs1 = pst.executeQuery();
                // Valorizza lista figli
                while (rs1.next()) {
                    wbsF = new WbsBean();
                    BeanUtil.populate(wbsF, rs1);
                    wbsF.setWbsPadre(wbsP);
                    vWbsN = new Vector<WbsBean>();
                    pst = null;
                    pst = con.prepareStatement(GET_WBS_FIGLIE);
                    pst.clearParameters();
                    pst.setInt(1, idProj);
                    pst.setInt(2, wbsF.getId());
                    rs2 = pst.executeQuery();
                    // Valorizza lista nipoti
                    while (rs2.next()) {
                        wbsN = new WbsBean();
                        BeanUtil.populate(wbsN, rs2);
                        wbsN.setWbsPadre(wbsF);
                        vWbsPN = new Vector<WbsBean>();
                        pst = null;
                        pst = con.prepareStatement(GET_WBS_FIGLIE);
                        pst.clearParameters();
                        pst.setInt(1, idProj);
                        pst.setInt(2, wbsN.getId());
                        rs3 = pst.executeQuery();
                        // Valorizza lista pronipoti
                        while (rs3.next()) {
                            wbsPN = new WbsBean();
                            BeanUtil.populate(wbsPN, rs3);
                            wbsPN.setWbsPadre(wbsN);
                            vWbsPPN = new Vector<WbsBean>();
                            pst = null;
                            pst = con.prepareStatement(GET_WBS_FIGLIE);
                            pst.clearParameters();
                            pst.setInt(1, idProj);
                            pst.setInt(2, wbsPN.getId());
                            rs4 = pst.executeQuery();
                            // Valorizza lista propronipoti
                            while (rs4.next()) {
                                wbsPPN = new WbsBean();
                                BeanUtil.populate(wbsPPN, rs4);
                                wbsPPN.setWbsPadre(wbsPN);
                                Vector<ActivityBean> wbsAct = new Vector<ActivityBean>(getActivitiesAmountByWbsAndYear(idProj, wbsPPN.getId(), user, year));
                                // Se non ci sono attività e la WBS è un WorkPackage, di che parliamo?
                                if (wbsAct.capacity() == NOTHING && wbsPPN.isWorkPackage()) {
                                    continue;
                                }
                                wbsPPN.setAttivita(wbsAct);
                                vWbsPPN.add(wbsPPN);
                            }
                            wbsPN.setWbsFiglie(vWbsPPN);
                            Vector<ActivityBean> wbsAct = new Vector<ActivityBean>(getActivitiesAmountByWbsAndYear(idProj, wbsPN.getId(), user, year));
                            if (wbsAct.capacity() == NOTHING && wbsPN.isWorkPackage()) {
                                continue;
                            }
                            wbsPN.setAttivita(wbsAct);
                            vWbsPN.add(wbsPN);
                        }
                        wbsN.setWbsFiglie(vWbsPN);
                        Vector<ActivityBean> wbsAct = new Vector<ActivityBean>(getActivitiesAmountByWbsAndYear(idProj, wbsN.getId(), user, year));
                        if (wbsAct.capacity() == NOTHING && wbsN.isWorkPackage()) {
                            continue;
                        }
                        wbsN.setAttivita(wbsAct);
                        vWbsN.add(wbsN);
                    }
                    wbsF.setWbsFiglie(vWbsN);
                    Vector<ActivityBean> wbsAct = new Vector<ActivityBean>(getActivitiesAmountByWbsAndYear(idProj, wbsF.getId(), user, year));
                    if (wbsAct.capacity() == NOTHING && wbsF.isWorkPackage()) {
                        continue;
                    }
                    wbsF.setAttivita(wbsAct);
                    MeasurementBean statoWbs = getWbsState(idProj, wbsF.getId());
                    wbsF.setStato(statoWbs);
                    vWbsF.add(wbsF);
                }
                wbsP.setWbsFiglie(vWbsF);
                Vector<ActivityBean> wbsAct = new Vector<ActivityBean>(getActivitiesAmountByWbsAndYear(idProj, wbsP.getId(), user, year));
                if (wbsAct.capacity() == NOTHING && wbsP.isWorkPackage()) {
                    continue;
                }
                wbsP.setAttivita(wbsAct);
                MeasurementBean statoWbs = getWbsState(idProj, wbsP.getId());
                wbsP.setStato(statoWbs);
                vWbsP.add(wbsP);
            }
            return vWbsP;
        } catch (AttributoNonValorizzatoException anve) {
            String msg = FOR_NAME + "id WBS padre non valorizzato; problema nella query delle WBS figlie.\n";
            LOG.severe(msg); 
            throw new WebStorageException(msg + anve.getMessage(), anve);
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
     * <p>Restituisce un WbsBean contenente tutte le proprie wbs figlie
     * in gerarchia.</p>
     * 
     * @param idProj id del progetto di cui caricare le wbs
     * @param user utente loggato
     * @param idWbs  id della wbs di cui si vuol caricare tutta la gerarchia
     * @return <code>WbsBean</code> - oggetto contenente tutte la gerarchia di Wbs di un progetto
     * @throws WebStorageException se si verifica un problema nell'esecuzione della query, nell'accesso al db o in qualche tipo di puntamento
     */
    @SuppressWarnings({ "null" })
    public WbsBean getWbsHierarchyByWbs(int idProj,
                                        PersonBean user,
                                        int idWbs)
                                 throws WebStorageException {
        ResultSet rs, rs1, rs2, rs3, rs4 = null;
        Connection con = null;
        PreparedStatement pst = null;
        WbsBean wbsPPN, wbsPN, wbsN, wbsF, wbsP = null;
        Vector<WbsBean> vWbsF = new Vector<WbsBean>();
        Vector<WbsBean> vWbsN = null;
        Vector<WbsBean> vWbsPN = null;
        Vector<WbsBean> vWbsPPN = null;
        try {
            con = pol_manager.getConnection();
            // Per prima cosa verifica che l'utente abbia i diritti di accesso al progetto
            if (!userCanRead(idProj, user.getId())) {
                String msg = FOR_NAME + "Qualcuno ha tentato di inserire un indirizzo nel browser avente un id progetto non valido!.\n";
                LOG.severe(msg + "E\' presente il parametro \"q=act\" ma non un valore \"id\" - cioe\' id progetto - significativo!\n");
                throw new WebStorageException("Attenzione: indirizzo richiesto non valido!\n");
            }
            pst = con.prepareStatement(GET_WBS);
            pst.clearParameters();
            pst.setInt(1, idProj);
            pst.setInt(2, idWbs);
            rs = pst.executeQuery();
            // Valorizza lista padri
            if (rs.next()) {
                wbsP = new WbsBean();
                BeanUtil.populate(wbsP, rs);
                vWbsF = new Vector<WbsBean>();
                pst = null;
                pst = con.prepareStatement(GET_WBS_FIGLIE);
                pst.clearParameters();
                pst.setInt(1, idProj);
                pst.setInt(2, wbsP.getId());
                rs1 = pst.executeQuery();
                // Valorizza lista figli
                while (rs1.next()) {
                    wbsF = new WbsBean();
                    BeanUtil.populate(wbsF, rs1);
                    wbsF.setWbsPadre(wbsP);
                    vWbsN = new Vector<WbsBean>();
                    pst = null;
                    pst = con.prepareStatement(GET_WBS_FIGLIE);
                    pst.clearParameters();
                    pst.setInt(1, idProj);
                    pst.setInt(2, wbsF.getId());
                    rs2 = pst.executeQuery();
                    // Valorizza lista nipoti
                    while (rs2.next()) {
                        wbsN = new WbsBean();
                        BeanUtil.populate(wbsN, rs2);
                        wbsN.setWbsPadre(wbsF);
                        vWbsPN = new Vector<WbsBean>();
                        pst = null;
                        pst = con.prepareStatement(GET_WBS_FIGLIE);
                        pst.clearParameters();
                        pst.setInt(1, idProj);
                        pst.setInt(2, wbsN.getId());
                        rs3 = pst.executeQuery();
                        // Valorizza lista pronipoti
                        while (rs3.next()) {
                            wbsPN = new WbsBean();
                            BeanUtil.populate(wbsPN, rs3);
                            wbsPN.setWbsPadre(wbsN);
                            vWbsPPN = new Vector<WbsBean>();
                            pst = null;
                            pst = con.prepareStatement(GET_WBS_FIGLIE);
                            pst.clearParameters();
                            pst.setInt(1, idProj);
                            pst.setInt(2, wbsPN.getId());
                            rs4 = pst.executeQuery();
                            // Valorizza lista pronipoti
                            while (rs4.next()) {
                                wbsPPN = new WbsBean();
                                BeanUtil.populate(wbsPPN, rs4);
                                wbsPPN.setWbsPadre(wbsPN);
                                vWbsPPN.add(wbsPPN);
                            }
                            wbsPN.setWbsFiglie(vWbsPPN);
                            vWbsPN.add(wbsPN);
                        }
                        wbsN.setWbsFiglie(vWbsPN);
                        vWbsN.add(wbsN);
                    }
                    wbsF.setWbsFiglie(vWbsN);
                    vWbsF.add(wbsF);
                }
                wbsP.setWbsFiglie(vWbsF);
            }
            return wbsP;
        } catch (AttributoNonValorizzatoException anve) {
            String msg = FOR_NAME + "id WBS padre non valorizzato; problema nella query delle WBS figlie.\n";
            LOG.severe(msg); 
            throw new WebStorageException(msg + anve.getMessage(), anve);
        }  catch (SQLException sqle) {
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
     * <p>Dato l'identificativo di un progetto e l'identificativo di una WBS,
     * restituisce tutta la gerarchia degli ascendenti della WBS di dato id.</p>
     * 
     * @param idProj identificativo del progetto a cui la WBS di cui si vuol cercare l'ascendenza deve appartenere
     * @param user utente loggato
     * @param idWbs  identificativo della WBS (offspring) di cui si vuol cercare l'ascendenza
     * @return <code>WbsBean</code> - una WBS contenente al proprio interno la gerarchia completa da scorrere per mostrare l'albero genealogico della WBS di dato id
     * @throws WebStorageException se si verifica qualche problema nell'esecuzione di query o in qualche tipo di puntamento
     */
    @SuppressWarnings("null")
    public WbsBean getWbsHierarchyByOffspring(int idProj,
                                              PersonBean user,
                                              int idWbs) 
                                       throws WebStorageException {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs, rs1, rs2, rs3 = null;
        WbsBean wbsP, wbsF, wbsN, wbsPN, wbsPPN = null;
        Vector<WbsBean> wbsFiglie = null;
        try {
            // Valorizza WBS di partenza (livello 0)
            wbsPPN = getWbsInstance(idProj, idWbs, user);
            // Ne cerca il padre
            con = pol_manager.getConnection();
            // Per prima cosa verifica che l'utente abbia i diritti di accesso al progetto
            if (!userCanRead(idProj, user.getId())) {
                String msg = FOR_NAME + "Qualcuno ha tentato di inserire un indirizzo nel browser avente un id progetto non valido!.\n";
                LOG.severe(msg + "E\' presente il parametro \"q=act\" ma non un valore \"id\" - cioe\' id progetto - significativo!\n");
                throw new WebStorageException("Attenzione: indirizzo richiesto non valido!\n");
            }
            pst = con.prepareStatement(GET_WBS_PADRE);
            pst.clearParameters();
            pst.setInt(1, wbsPPN.getId());
            rs = pst.executeQuery();
            // Valorizza padre di L0
            if (rs.next()) {
                wbsPN = new WbsBean();
                BeanUtil.populate(wbsPN, rs);
                pst = null;
                pst = con.prepareStatement(GET_WBS_PADRE);
                pst.clearParameters();
                pst.setInt(1, wbsPN.getId());
                rs1 = pst.executeQuery();
                // Valorizza nonno di L0
                if (rs1.next()) {
                    wbsN = new WbsBean();
                    BeanUtil.populate(wbsN, rs1);
                    pst = null;
                    pst = con.prepareStatement(GET_WBS_PADRE);
                    pst.clearParameters();
                    pst.setInt(1, wbsN.getId());
                    rs2 = pst.executeQuery();
                    // Valorizza bisnonno di L0
                    if (rs2.next()) {
                    // Sono stati trovati padre, nonno, bisnonno
                        wbsF = new WbsBean();
                        BeanUtil.populate(wbsF, rs2);
                        pst = null;
                        pst = con.prepareStatement(GET_WBS_PADRE);
                        pst.clearParameters();
                        pst.setInt(1, wbsF.getId());
                        rs3 = pst.executeQuery();
                        // Valorizza trisavolo di L0
                        if (rs3.next()) {
                        // Sono stati trovati padre, nonno, bisnonno e trisavolo
                            wbsP = new WbsBean();
                            BeanUtil.populate(wbsP, rs3);
                            // PPN va su PN
                            wbsFiglie = new Vector<WbsBean>();
                            wbsFiglie.add(wbsPPN);
                            wbsPN.setWbsFiglie(wbsFiglie);
                            // PN va su N
                            wbsFiglie = null;
                            wbsFiglie = new Vector<WbsBean>();
                            wbsFiglie.add(wbsPN);
                            wbsN.setWbsFiglie(wbsFiglie);
                            // N va su F
                            wbsFiglie = null;
                            wbsFiglie = new Vector<WbsBean>();
                            wbsFiglie.add(wbsN);
                            wbsF.setWbsFiglie(wbsFiglie);
                            // F va su P
                            wbsFiglie = null;
                            wbsFiglie = new Vector<WbsBean>();
                            wbsFiglie.add(wbsF);
                            wbsP.setWbsFiglie(wbsFiglie);
                            // P diventa WBS
                            return wbsP;
                        }
                        // PPN va su PN
                        wbsFiglie = new Vector<WbsBean>();
                        wbsFiglie.add(wbsPPN);
                        wbsPN.setWbsFiglie(wbsFiglie);
                        // PN va su N
                        wbsFiglie = null;
                        wbsFiglie = new Vector<WbsBean>();
                        wbsFiglie.add(wbsPN);
                        wbsN.setWbsFiglie(wbsFiglie);
                        // N va su F
                        wbsFiglie = null;
                        wbsFiglie = new Vector<WbsBean>();
                        wbsFiglie.add(wbsN);
                        wbsF.setWbsFiglie(wbsFiglie);
                        // F diventa WBS
                        return wbsF;
                    }
                    // PPN va su PN
                    wbsFiglie = new Vector<WbsBean>();
                    wbsFiglie.add(wbsPPN);
                    wbsPN.setWbsFiglie(wbsFiglie);
                    // PN va su N
                    wbsFiglie = null;
                    wbsFiglie = new Vector<WbsBean>();
                    wbsFiglie.add(wbsPN);
                    wbsN.setWbsFiglie(wbsFiglie);
                    // N diventa WBS
                    return wbsN;
                }
                // PPN va su PN
                wbsFiglie = new Vector<WbsBean>();
                wbsFiglie.add(wbsPPN);
                wbsPN.setWbsFiglie(wbsFiglie);
                // PN diventa WBS
                return wbsPN;
            }
            return wbsPPN;           
        } catch (AttributoNonValorizzatoException anve) {
            String msg = FOR_NAME + "id WBS padre non valorizzato; problema nella query delle WBS figlie.\n";
            LOG.severe(msg); 
            throw new WebStorageException(msg + anve.getMessage(), anve);
        }  catch (SQLException sqle) {
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
     * <p>Dato l'identificativo di un progetto e l'identificativo di una WBS,
     * restituisce solo il padre della WBS di dato id.</p>
     * 
     * @param idProj identificativo del progetto a cui la WBS di cui si vuol cercare l'ascendenza deve appartenere
     * @param user utente loggato
     * @param idWbs  identificativo della WBS di cui si vuol cercare l'ascendenza
     * @return <code>WbsBean</code> - una WBS contenente al proprio interno la gerarchia completa da scorrere per mostrare l'albero genealogico della WBS di dato id
     * @throws WebStorageException se si verifica qualche problema nell'esecuzione di query o in qualche tipo di puntamento
     */
    @SuppressWarnings("null")
    public WbsBean getWbsParentByOffspring(int idProj,
                                           PersonBean user,
                                           int idWbs) 
                                    throws WebStorageException {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        WbsBean wbsInstance, wbsP = null;
        try {
            wbsInstance = getWbsInstance(idProj, idWbs, user);
            // Ne cerca il padre
            con = pol_manager.getConnection();
            // Per prima cosa verifica che l'utente abbia i diritti di accesso al progetto
            if (!userCanRead(idProj, user.getId())) {
                String msg = FOR_NAME + "Qualcuno ha tentato di inserire un indirizzo nel browser avente un id progetto non valido!.\n";
                LOG.severe(msg + "E\' presente il parametro \"q=act\" ma non un valore \"id\" - cioe\' id progetto - significativo!\n");
                throw new WebStorageException("Attenzione: indirizzo richiesto non valido!\n");
            }
            pst = con.prepareStatement(GET_WBS_PADRE);
            pst.clearParameters();
            pst.setInt(1, wbsInstance.getId());
            rs = pst.executeQuery();
            // Valorizza padre di L0
            if (rs.next()) {
                wbsP = new WbsBean();
                BeanUtil.populate(wbsP, rs);
            }
            return wbsP;           
        } catch (AttributoNonValorizzatoException anve) {
            String msg = FOR_NAME + "id WBS padre non valorizzato; problema nella query delle WBS figlie.\n";
            LOG.severe(msg); 
            throw new WebStorageException(msg + anve.getMessage(), anve);
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
    * <p>Restituisce un Vector&lt;WbsBean&gt; contenente tutte le WBS figlie
    * che hanno come padre la WBS identificata tramite id, passato come parametro.</p>
    * 
    * @param idProj  id del progetto di cui caricare le wbs
    * @param user    utente loggato
    * @param idWbs id della wbs padre
    * @return Vector&lt;WbsBean&gt; - vector contenente tutte le WBS figlie
    * @throws WebStorageException se si verifica un problema nell'esecuzione della query, nell'accesso al db o in qualche tipo di puntamento 
    */
    @SuppressWarnings({ "null" })
    public Vector<WbsBean> getWbsFiglie(int idProj,
                                        PersonBean user,
                                        int idWbs) 
                                 throws WebStorageException {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pst = null;
        WbsBean wbs = null;
        Vector<WbsBean> vectorWbs = new Vector<WbsBean>();
        int nextParam = 0;
        try {
            con = pol_manager.getConnection();
            // Per prima cosa verifica che l'utente abbia i diritti di accesso al progetto
            if (!userCanRead(idProj, user.getId())) {
                String msg = FOR_NAME + "Qualcuno ha tentato di inserire un indirizzo nel browser avente un id progetto non valido!.\n";
                LOG.severe(msg + "E\' presente il parametro \"q=act\" ma non un valore \"id\" - cioe\' id progetto - significativo!\n");
                throw new WebStorageException("Attenzione: indirizzo richiesto non valido!\n");
            }
            pst = con.prepareStatement(GET_WBS_FIGLIE);
            pst.clearParameters();
            pst.setInt(++nextParam, idProj);
            pst.setInt(++nextParam, idWbs);
            rs = pst.executeQuery();
            while (rs.next()) {
                wbs = new WbsBean();
                BeanUtil.populate(wbs, rs);
                vectorWbs.add(wbs);
            }
            return vectorWbs;
        } catch (AttributoNonValorizzatoException anve) {
            String msg = FOR_NAME + "id WBS padre non valorizzato; problema nella query delle WBS figlie.\n";
            LOG.severe(msg); 
            throw new WebStorageException(msg + anve.getMessage(), anve);
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
     * <p>Restituisce un MeasurementBean rappresentante lo stato di una WBS
     * il cui identificativo viene passato come parametro, e che &egrave;
     * collegata ad un progetto, il cui identificativo viene passato come
     * parametro.</p>
     * 
     * @param projId    identificativo del progetto cui e' collegata la WBS che si vuol recuperare
     * @param wbsId     identificativo della WBS il cui stato si vuol recuperare
     * @return <code>MeasurementBean</code> - oggetto rappresentante lo stato della WBS selezionato
     * @throws WebStorageException se si verifica un problema nell'esecuzione della query, nell'accesso al db o in qualche tipo di puntamento
     */
    @SuppressWarnings({ "null", "static-method" })
    public MeasurementBean getWbsState(int projId,
                                       int wbsId) 
                                throws WebStorageException {
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement pst = null;
        MeasurementBean state = new MeasurementBean();
        try {
            con = pol_manager.getConnection();
            pst = con.prepareStatement(GET_WBS_STATE);
            pst.clearParameters();
            pst.setInt(1, projId);
            pst.setInt(2, wbsId);
            rs = pst.executeQuery();
            if (rs.next()) {
                BeanUtil.populate(state, rs);
            } else {
                /* Se non ci sono i descrittori dello stato, vuol dire che 
                 * nessun utente ha modificato lo stato di default, e quindi
                 * il software imposta lo stato di default */
                state.setId(1);
            }
            return state;
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
    
    
    /**
     * <p>Restituisce una lista formata dai possibili valori di 
     * enumerativi dinamici, interrogati in funzione di una query SQL
     * passata come argomento.</p> 
     * 
     * @param query stringa SQL contenente le indicazioni per estrarre le informazioni relative ai valori 
     * @return <code>LinkedList&lt;CodeBean&gt;</code> - lista di bean contenente i valori estratti dall'enumerativo selezionato
     * @throws WebStorageException se si verifica un problema SQL o in qualche tipo di puntamento
     */
    @SuppressWarnings({ "static-method", "null" })
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
    
    
    /**
    * <p>Restituisce un MonitorBean contenente tutti i campi del monitoraggio
    * relativo al dipartimento identificato tramite id, passato come parametro
    * e ad un anno solare passato come parametro.</p>
    * 
    * @param idDip  id del dipartimento di cui si deve recuperare il monitoraggio
    * @param anno   anno di cui si vuol recuperare il monitoraggio
    * @return MonitorBean - oggetto contenente tutti i campi del monitoraggio 
    * @throws WebStorageException se si verifica un problema nell'esecuzione della query, nell'accesso al db o in qualche tipo di puntamento 
    */
    @SuppressWarnings({ "null" })
    public MonitorBean getMonitor(int idDip, 
                                  int anno) 
                           throws WebStorageException {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pst = null;
        MonitorBean mon = null;
        Vector<FileDocBean> attachments = null;
        int nextParam = 0;
        try {
            con = pol_manager.getConnection();
            pst = con.prepareStatement(GET_MONITOR_BY_DEPART);
            pst.clearParameters();
            pst.setInt(++nextParam, anno);
            pst.setInt(++nextParam, idDip);
            rs = pst.executeQuery();
            if (rs.next()) {
                mon = new MonitorBean();
                BeanUtil.populate(mon, rs);
                // Recupero e settaggio degli allegati
                try {
                    attachments = getFileDoc("monitoraggio", "d4", mon.getId(), NOTHING);
                    mon.setAllegatiD4(attachments);
                    attachments = null;
                    attachments = getFileDoc("monitoraggio", "d5", mon.getId(), NOTHING);
                    mon.setAllegatiD5(attachments);
                    attachments = null;
                    attachments = getFileDoc("monitoraggio", "d6", mon.getId(), NOTHING);
                    mon.setAllegatiD6(attachments);
                    attachments = null;
                    attachments = getFileDoc("monitoraggio", "d7", mon.getId(), NOTHING);
                    mon.setAllegatiD7(attachments);
                    attachments = null;
                    attachments = getFileDoc("monitoraggio", "d8", mon.getId(), NOTHING);
                    mon.setAllegatiD8(attachments);
                } catch (AttributoNonValorizzatoException anve) {
                    String msg = FOR_NAME + "Oggetto status.id non valorizzato; problema nella query che recupera lo status attraverso l\'id.\n";
                    LOG.severe(msg); 
                    throw new WebStorageException(msg + anve.getMessage(), anve);
                }
            }
            return mon;
        } catch (SQLException sqle) {
            String msg = FOR_NAME + "Oggetto MonitorBean non valorizzato; problema nella query dell\'utente.\n";
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
    * <p>Restituisce un MonitorBean contenente tutti i campi del monitoraggio
    * relativo all'ateneo, identificato tramite l'anno solare di interesse, 
    * passato come parametro.</p>
    * 
    * @param anno   anno di cui si vuol recuperare il monitoraggio di ateneo
    * @return MonitorBean - oggetto contenente tutti i campi del monitoraggio 
    * @throws WebStorageException se si verifica un problema nell'esecuzione della query, nell'accesso al db o in qualche tipo di puntamento 
    */
    @SuppressWarnings({ "null" })
    public MonitorBean getMonitorAteneo(int anno) 
                           throws WebStorageException {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pst = null;
        MonitorBean mon = null;
        Vector<FileDocBean> attachments = null;
        int nextParam = 0;
        try {
            con = pol_manager.getConnection();
            pst = con.prepareStatement(GET_MONITOR_ATENEO);
            pst.clearParameters();
            pst.setInt(++nextParam, anno);
            rs = pst.executeQuery();
            if (rs.next()) {
                mon = new MonitorBean();
                BeanUtil.populate(mon, rs);
                // Recupero e settaggio degli allegati
                try {
                    attachments = getFileDoc("monitoraggioate", "all", mon.getId(), NOTHING);
                    mon.setAllegati(attachments);
                } catch (AttributoNonValorizzatoException anve) {
                    String msg = FOR_NAME + "Attributo obbligatorio di bean non valorizzato.\n";
                    LOG.severe(msg); 
                    throw new WebStorageException(msg + anve.getMessage(), anve);
                }
            }
            return mon;
        } catch (SQLException sqle) {
            String msg = FOR_NAME + "Oggetto MonitorBean non valorizzato; problema nella query dell\'utente.\n";
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
    * <p>Restituisce un Vector&lt;ActivityBean&gt; contenente tutto il log 
    * degli accessi disponibile, purch&eacute; l'utente loggato abbia 
    * privilegi di livello massimo.</p>
    * 
    * @param idUsr  id dell'utente di cui verificare il livello di autorizzazione
    * @return Vector&lt;StatusBean&gt; - vector contenente tutti gli accessi
    * @throws WebStorageException se si verifica un problema nell'esecuzione della query, nell'accesso al db o in qualche tipo di puntamento 
    */
    @SuppressWarnings({ "null", "static-method" })
    public Vector<StatusBean> getAccessLog(int idUsr) 
                                      throws WebStorageException {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pst = null;
        StatusBean accessInstance = null;
        Vector<StatusBean> accessList = new Vector<StatusBean>();
        int nextParam = 0;
        try {
            con = pol_manager.getConnection();
            pst = con.prepareStatement(GET_ACCESSLOG);
            pst.clearParameters();
            pst.setInt(++nextParam, idUsr);
            rs = pst.executeQuery();
            while (rs.next()) {
                accessInstance = new StatusBean();
                BeanUtil.populate(accessInstance, rs);
                accessList.add(accessInstance);
            }
            return accessList;
        } catch (SQLException sqle) {
            String msg = FOR_NAME + "Elenco degli accessi non valorizzato; problema nella query dell\'access log.\n";
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
     * <p>Restituisce un Vector di FileDocBean estratti da data entit&agrave;
     * e dato attributo, entrambi passati come argomenti.</p>
     * <p>In particolare, potrebbero esistere pi&uacute; tabelle di allegati
     * per ciascuna entit&agrave; referenziata (p.es. 'dipartimento_all',
     * 'dipartimento_logo' etc.), per cui il suffisso della tabella potrebbe
     * variare ed &egrave; stato parametrizzato.</p>
     *
     * @param nomeEntita nome dell'entit&agrave; da cui estrarre gli allegati
     * @param nomeAttributo suffisso del nome della tabella dei fileset referenzianti
     * @param idBelongs identificativo dell'entita' cui gli allegati fanno riferimento
     * @param getAll intero usato come clausola in OR in funzione del cui valore vengono estratti solo gli allegati di una data entita' oppure di tutte
     * @return <code>Vector&lt;FileDocBean&gt;</code> - Vector di fileset (riferimenti) agli allegati caricati
     * @throws WebStorageException se si verifica un problema SQL o in qualche puntamento
     */
    @SuppressWarnings("static-method")
    public Vector<FileDocBean> getFileDoc(String nomeEntita, 
                                          String nomeAttributo,
                                          int idBelongs,
                                          int getAll)
                                   throws WebStorageException {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        FileDocBean fileDoc = null;
        Vector<FileDocBean> vD = new Vector<FileDocBean>();
        String query =
                "SELECT  " +  nomeEntita + "_" + nomeAttributo + ".*" +
                "  FROM "   + nomeEntita + "_" + nomeAttributo + 
                " WHERE id_belongs_" + nomeEntita   + " = ? OR -1 = ? " +
                " ORDER BY data";
        try {
            con = pol_manager.getConnection();
            try {
                pst = con.prepareStatement(query);
            } catch (NullPointerException npe) {
                String msg = FOR_NAME + "Ooops... problema nel puntamento alla connessione.\n";
                LOG.severe(msg);
                throw new WebStorageException(msg + npe.getMessage());
            }
            pst = con.prepareStatement(query);
            pst.clearParameters();
            pst.setInt(1, idBelongs);
            pst.setInt(2, getAll);
            rs = pst.executeQuery();
            while (rs.next()) {
                fileDoc = new FileDocBean();
                BeanUtil.populate(fileDoc, rs);
                vD.add(fileDoc);
            }
            return vD;
        } catch (SQLException sqle) {
            String msg = FOR_NAME + "Problema nella query dei fileset.\n";
            LOG.severe(msg);
            throw new WebStorageException(msg + sqle.getMessage(), sqle);
        } finally {
            try {
                if ( con != null ) con.close();
            } catch (NullPointerException npe) {
                String msg = FOR_NAME + "Ooops... problema nella chiusura della connessione.\n";
                LOG.severe(msg);
                throw new WebStorageException(msg + npe.getMessage());                
            } catch (SQLException sqle) {
                throw new WebStorageException(sqle.getMessage());
            }
        }
    }
    
    
    /**
     * <p>Restituisce un valore boolean <code>true</code> se esiste un file
     * di dato nome, in data entit&agrave; e dato attributo, 
     * tutti passati come argomenti.</p>
     *
     * @param nomeEntita prefisso del nome dell'entit&agrave; in cui verificare l'esistenza dell'allegato
     * @param nomeAttributo suffisso del nome dell'entit&agrave; in cui verificare l'esistenza dell'allegato
     * @param nomeFile nome del file di cui verificare la presenza nell'entit&agrave; di nome ricavabile dai parametri
     * @return <code>boolean</code> - true se il nome del file &egrave; gi&agrave; presente, false altrimenti
     * @throws WebStorageException se si verifica un problema di tipo SQL o in qualche puntamento
     */
    @SuppressWarnings({ "null", "static-method" })
    public boolean existsFileName(String nomeEntita, 
                                  String nomeAttributo,
                                  String nomeFile)
                           throws WebStorageException {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        String table = nomeEntita + "_" + nomeAttributo;        
        String query =
                "SELECT " +  table + ".*" +
                "  FROM "  + table +
                "   WHERE file = ?";
        try {
            con = pol_manager.getConnection();
            try {
                pst = con.prepareStatement(query);
            } catch (NullPointerException npe) {
                String msg = FOR_NAME + "Ooops... problema nel puntamento della connessione.\n";
                LOG.severe(msg);
                throw new WebStorageException(msg + npe.getMessage());
            }
            pst.clearParameters();
            pst.setString(1, nomeFile);
            rs = pst.executeQuery();
            // Il nome esiste già
            if (rs.next()) {
                return true;
            }
            // Il nome non esiste
            return false;
        } catch (SQLException sqle) {
            String msg = FOR_NAME + "Problema nella query.\n";
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
     * <p>Restituisce il valore boolean <code>true</code> se l'utente,
     * il cui identificativo viene passato come parametro, risulta gi&agrave;
     * inserito come appartenente al gruppo principale (id_share_grp)
     * del dipartimento/struttura a cui appartiene un progetto, 
     * il cui identificativo viene passato come parametro.</p>
     * <p>In altri termini, dato un id utente e un id progetto, il metodo
     * controlla - e restituisce vero in caso trovi la corrispondenza - se 
     * tale utente &egrave; gi&agrave; inserito come appartenente al gruppo
     * principale del dipartimento che &quot;possiede&quot; il progetto.</p> 
     * 
     * @param userId identificativo utente di cui si vuol verificare l'appartenenza
     * @param projId identificativo progetto da cui si risale alla struttura che lo aggrega e di li' al suo gruppo principale
     * @return <code>boolean</code> - true se l'utente di dato id e' gia' stato inserito come appartenente al gruppo della struttura del progetto di dato id
     * @throws WebStorageException se si verifica un problema di tipo SQL o in qualche puntamento
     */
    @SuppressWarnings({ "null", "static-method" })
    public boolean existsBelongsTo(int userId, 
                                   int projId)
                            throws WebStorageException {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            con = pol_manager.getConnection();
            try {
                pst = con.prepareStatement(IS_BELONGS_PERSON);
            } catch (NullPointerException npe) {
                String msg = FOR_NAME + "Ooops... problema nel puntamento della connessione.\n";
                LOG.severe(msg);
                throw new WebStorageException(msg + npe.getMessage());
            }
            pst.clearParameters();
            pst.setInt(1, userId);
            pst.setInt(2, projId);
            rs = pst.executeQuery();
            // L'utente è stato già aggiunto al gruppo del dipartimento del progetto
            if (rs.next()) {
                return true;
            }
            // L'utente non è presente nel gruppo del dipartimento del progetto
            return false;
        } catch (SQLException sqle) {
            String msg = FOR_NAME + "Problema nella query.\n";
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
     * <p>Metodo per aggiornamento della password dell'utente.</p>
     * <p>Esegue l'update sulla password dell'utente in chiaro.</p>
     * 
     * @param userId        utente sul quale cambiare la password
     * @param userLogged    utente incaricato di cambiare la password
     * @param passwd        password inserita dall'utente
     * @throws WebStorageException  se si verifica un problema nell'esecuzione della query, nell'accesso al db o in qualche tipo di puntamento 
     */
    @SuppressWarnings({ "null", "static-method" })
    public void updatePassword (int userId,
                                PersonBean userLogged,
                                String passwd) 
                         throws WebStorageException {
        Connection con = null;
        PreparedStatement pst = null;
        int nextParam = 0;
        try {
            // Ottiene la connessione
            con = pol_manager.getConnection();
            con.setAutoCommit(false);
            pst = con.prepareStatement(UPDATE_PWD);
            pst.clearParameters();
            pst.setNull(++nextParam, Types.NULL);
            pst.setNull(++nextParam, Types.NULL);
            pst.setString(++nextParam, passwd);
            pst.setString(++nextParam,  userLogged.getCognome() + " " + userLogged.getNome());
            pst.setDate(++nextParam, Utils.convert(Utils.convert(Utils.getCurrentDate())));
            pst.setTime(++nextParam, Utils.getCurrentTime());
            pst.setInt(++nextParam, userId);
            pst.executeUpdate();
            con.commit();
        } catch (AttributoNonValorizzatoException anve) {
            String msg = FOR_NAME + "Si e\' tentato di accedere a un attributo di un bean obbligatorio ma non valorizzato.\n" + anve.getMessage();
            LOG.severe(msg + "Probabile problema nel tentativo di recuperare un parametro obbligatorio del PersonBean.\n");
            throw new WebStorageException(msg, anve);
        } catch (SQLException sqle) {
            String msg = FOR_NAME + "Tupla non aggiornata correttamente; problema nella query che aggiorna la password.\n";
            LOG.severe(msg); 
            throw new WebStorageException(msg + sqle.getMessage(), sqle);
        } catch (NullPointerException npe) {
            String msg = FOR_NAME + "Tupla non aggiornata correttamente; problema nella query che aggiorna la password.\n";
            LOG.severe(msg); 
            throw new WebStorageException(msg + npe.getMessage(), npe);
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
     * <p>Metodo per aggiornamento della password dell'utente.</p>
     * <p>Utilizza la password criptata, ovvero quella modificata dall'utente stesso.</p>
     * 
     * @param user          utente sul quale cambiare la password
     * @param passwd        password inserita dall'utente
     * @param salt          seme generato per l'utente corrente
     * @throws WebStorageException  se si verifica un problema nell'esecuzione della query, nell'accesso al db o in qualche tipo di puntamento 
     */
    @SuppressWarnings({ "null", "static-method" })
    public void updatePassword (PersonBean user,
                                String passwd,
                                String salt)
                         throws WebStorageException {
        Connection con = null;
        PreparedStatement pst = null;
        int nextParam = 0;
        try {
            // Ottiene la connessione
            con = pol_manager.getConnection();
            con.setAutoCommit(false);
            pst = con.prepareStatement(UPDATE_PWD_BY_PERSON);
            pst.clearParameters();
            pst.setString(++nextParam, passwd);
            pst.setString(++nextParam, salt);
            pst.setNull(++nextParam, Types.NULL);
            pst.setString(++nextParam,  user.getCognome() + " " + user.getNome());
            pst.setDate(++nextParam, Utils.convert(Utils.convert(Utils.getCurrentDate())));
            pst.setTime(++nextParam, Utils.getCurrentTime());
            pst.setInt(++nextParam, user.getId());
            pst.executeUpdate();
            con.commit();
        } catch (AttributoNonValorizzatoException anve) {
            String msg = FOR_NAME + "Si e\' tentato di accedere a un attributo di un bean obbligatorio ma non valorizzato.\n" + anve.getMessage();
            LOG.severe(msg + "Probabile problema nel tentativo di recuperare un parametro obbligatorio del PersonBean.\n");
            throw new WebStorageException(msg, anve);
        } catch (SQLException sqle) {
            String msg = FOR_NAME + "Tupla non aggiornata correttamente; problema nella query che aggiorna la password.\n";
            LOG.severe(msg); 
            throw new WebStorageException(msg + sqle.getMessage(), sqle);
        } catch (NullPointerException npe) {
            String msg = FOR_NAME + "Tupla non aggiornata correttamente; problema nella query che aggiorna la password.\n";
            LOG.severe(msg); 
            throw new WebStorageException(msg + npe.getMessage(), npe);
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
     * <p>Metodo che controlla la query da eseguire 
     * e chiama il metodo opportuno.</p>
     * <p>Effettua un controllo sulla concorrenza per evitare che, mentre l'utente
     * modifica i campi del project charter, un altro utente abbia modificato
     * i medesimi campi alterando la situazione di partenza.
     * Carica a tal fine i progetti scrivibili in sessione utente
     * (session-time: questa lista di elementi servir&agrave; anche per 
     * effettuare altri controlli, quali quelli sull'autenticazione 
     * per evitare situazioni di tipo <cite>garden gate</cite>); fa anche 
     * la query sul progetto visibile nel momento in cui l'utente 
     * ha aperto la pagina (request-time). A quel punto, prima di scrivere
     * (run-time) verifica che i campi del progetto a sessiontime corrispondano
     * ai campi del progetto a requesttime; se non &egrave; cos&iacute;
     * vuol dire che nel frattempo (nel tempo intercorso dopo che l'utente
     * si &egrave; loggato) &egrave; intervenuto qualcun altro, alterando
     * la premessa. Non &egrave; possibile effettuare questo controllo
     * tra il progetto caricato a requesttime e il progetto a runtime, 
     * in quanto se l'utente stesso modifica i campi, il progetto a 
     * reuqesttime non sar&agrave; mai uguale al progetto a runtime,
     * per definizione!</p>
     * 
     * @param idProj    id del progetto da aggiornare
     * @param projectsWritableByUser    Vector contenente la lista dei progetti su cui l'utente corrente e' abilitato alla modifica
     * @param user      utente che ha eseguito il login, oggetto contenente il proprio id persona 
     * @param projects  Map contenente la lista dei progetti su cui l'utente corrente e' abilitato alla modifica
     * @param objectsRelatedToProject   Map contenente le hashmap che contengono le attività, i rischi e le competenze su cui l'utente e' abilitato alla modifica
     * @param params    hashmap che contiene i parametri che si vogliono aggiornare del progetto
     * @throws WebStorageException se si verifica un problema nell'esecuzione della query, nell'accesso al db o in qualche tipo di puntamento 
     */
    @SuppressWarnings({ "null", "rawtypes" })
    public void updateProjectPart(int idProj,
                                  Vector<ProjectBean> projectsWritableByUser,
                                  PersonBean user,
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
            ProjectBean sessionPrj = projects.get(key);
            // Dichiara un oggetto dello stesso tipo che conterrà i risultati della query
            ProjectBean requestPrj = null;
            /* Recupera le liste di attività, skill e rischi
            HashMap<Integer, Vector> activitiesOfProject = objectsRelatedToProject.get(Query.PART_PROJECT_CHARTER_MILESTONE);
            Vector<ActivityBean> activities = (Vector<ActivityBean>) activitiesOfProject.get(key);*/
            // Ottiene la connessione
            con = pol_manager.getConnection();
            /* ===  Controlla anzitutto che l'id progetto sulla querystring === *
             * ===  corrisponda a un id dei progetti scrivibili dall'utente === */
            try {
                if (!userCanWrite(idProj, projectsWritableByUser)) {
                    String msg = FOR_NAME + "Attenzione: l'utente ha tentato di modificare un\'attivita\' legata ad un progetto su cui non ha i diritti di scrittura!\n";
                    LOG.severe(msg + "Problema durante il tentativo di aggiornare una parte del progetto.\n");
                    throw new WebStorageException(msg);
                }
            } catch (WebStorageException wse) {
                String msg = FOR_NAME + "Problema nel tentativo di capire se l\'utente ha o non ha i diritti di scrittura!\n";
                LOG.severe(msg + "Problema durante il tentativo di aggiornare una parte del progetto.\n");
                throw new WebStorageException(msg);
            }
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
                    pst.setInt(2, user.getId());
                    rs = pst.executeQuery();
                    if (rs.next()) {
                        requestPrj = new ProjectBean();
                        BeanUtil.populate(requestPrj, rs);
                        if ( (sessionPrj.getSituazioneAttuale().equals(requestPrj.getSituazioneAttuale())) &&
                             (sessionPrj.getDescrizione().equals(requestPrj.getDescrizione())) &&
                             (sessionPrj.getObiettiviMisurabili().equals(requestPrj.getObiettiviMisurabili())) &&
                             (sessionPrj.getMinacce().equals(requestPrj.getMinacce())) 
                           ) {
                            pst = null;
                            pst = con.prepareStatement(UPDATE_VISION);
                            pst.clearParameters();
                            pst.setString(1, paramsVision.get("pcv-situazione"));
                            pst.setString(2, paramsVision.get("pcv-descrizione"));
                            pst.setString(3, paramsVision.get("pcv-obiettivi"));
                            pst.setString(4, paramsVision.get("pcv-minacce"));
                            pst.setDate(5, Utils.convert(Utils.convert(Utils.getCurrentDate())));
                            pst.setTime(6, Utils.getCurrentTime());
                            pst.setString(7, user.getNome() + Utils.BLANK_SPACE + user.getCognome());
                            pst.setInt(8, idProj);
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
                    pst.setInt(2, user.getId());
                    rs = pst.executeQuery();
                    if (rs.next()) {
                        requestPrj = new ProjectBean();
                        BeanUtil.populate(requestPrj, rs);
                        if (    (sessionPrj.getStakeholderMarginali().equals(requestPrj.getStakeholderMarginali())) &&
                                (sessionPrj.getStakeholderOperativi().equals(requestPrj.getStakeholderOperativi())) &&
                                (sessionPrj.getStakeholderIstituzionali().equals(requestPrj.getStakeholderIstituzionali())) && 
                                (sessionPrj.getStakeholderChiave().equals(requestPrj.getStakeholderChiave())) 
                            ) {
                               pst = null;
                               pst = con.prepareStatement(UPDATE_STAKEHOLDER);
                               pst.clearParameters();
                               pst.setString(1, paramsStakeholder.get("pcs-marginale"));
                               pst.setString(2, paramsStakeholder.get("pcs-operativo"));
                               pst.setString(3, paramsStakeholder.get("pcs-istituzionale"));
                               pst.setString(4, paramsStakeholder.get("pcs-chiave"));
                               pst.setDate(5, Utils.convert(Utils.convert(Utils.getCurrentDate())));
                               pst.setTime(6, Utils.getCurrentTime());
                               pst.setString(7, user.getNome() + Utils.BLANK_SPACE + user.getCognome());
                               pst.setInt(8, idProj);
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
                    pst.setInt(2, user.getId());
                    rs = pst.executeQuery();
                    if (rs.next()) {
                        requestPrj = new ProjectBean();
                        BeanUtil.populate(requestPrj, rs);
                        if ( (sessionPrj.getDeliverable().equals(requestPrj.getDeliverable())) ) {
                            pst = null;
                            pst = con.prepareStatement(UPDATE_DELIVERABLE);
                            pst.clearParameters();
                            pst.setString(1, paramsDeliverable.get("pcd-descrizione"));
                            pst.setDate(2, Utils.convert(Utils.convert(Utils.getCurrentDate())));
                            pst.setTime(3, Utils.getCurrentTime());
                            pst.setString(4, user.getNome() + Utils.BLANK_SPACE + user.getCognome());
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
            if (params.containsKey(PART_PROJECT_CHARTER_RESOURCE)) {
                HashMap<String, String> paramsResource = params.get(PART_PROJECT_CHARTER_RESOURCE);
                if (Utils.containsValues(paramsResource)) {
                    con.setAutoCommit(false);
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
                    pst.setDate(4, Utils.convert(Utils.convert(Utils.getCurrentDate())));
                    pst.setTime(5, Utils.getCurrentTime());
                    pst.setString(6, user.getNome() + Utils.BLANK_SPACE + user.getCognome());
                    pst.setInt(7, idProj);
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
                    pst.setInt(2, user.getId());
                    rs = pst.executeQuery();
                    if (rs.next()) {
                        requestPrj = new ProjectBean();
                        BeanUtil.populate(requestPrj, rs);
                        if ( sessionPrj.getVincoli().equals(requestPrj.getVincoli())) {
                            pst = null;
                            pst = con.prepareStatement(UPDATE_CONSTRAINT);
                            con.setAutoCommit(false);
                            pst.clearParameters();
                            pst.setString(1, paramsConstraint.get("pcc-descrizione"));
                            pst.setDate(2, Utils.convert(Utils.convert(Utils.getCurrentDate())));
                            pst.setTime(3, Utils.getCurrentTime());
                            pst.setString(4, user.getNome() + Utils.BLANK_SPACE + user.getCognome());
                            pst.setInt(5, idProj);
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
                    Date dateTemp = Utils.format(paramsStatus.get("sts-datainizio"), "dd/MM/yyyy", Query.DATA_SQL_PATTERN);
                    pst.setDate(1, Utils.convert(dateTemp));
                    dateTemp = null;
                    dateTemp = Utils.format(paramsStatus.get("sts-datafine"), "dd/MM/yyyy", Query.DATA_SQL_PATTERN);
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
                    pst.setTime(14, Utils.getCurrentTime());
                    pst.setString(15, user.getNome() + Utils.BLANK_SPACE + user.getCognome());
                    pst.setInt(16, idProj);
                    pst.setInt(17, Integer.parseInt(paramsStatus.get("sts-id")));
                    //JOptionPane.showMessageDialog(null, "Chiamata arrivata a updateProjectPart dall\'applicazione!", FOR_NAME + ": esito OK", JOptionPane.INFORMATION_MESSAGE, null);
                    pst.executeUpdate();
                    con.commit();
                }
            }
        } catch (AttributoNonValorizzatoException anve) {
            String msg = FOR_NAME + "Problema nel recupero di un attributo obbligatorio di un bean.\n";
            LOG.severe(msg + "Nome, cognome o id utente non valorizzato.\n"); 
            throw new WebStorageException(msg + anve.getMessage(), anve);
        } catch (CommandException ce) {
            String msg = FOR_NAME + "Tupla non aggiornata correttamente; problema nel metodo che aggiorna il progetto.\n";
            LOG.severe(msg); 
            throw new WebStorageException(msg + ce.getMessage(), ce);
        } catch (NotFoundException nfe) {
            String msg = FOR_NAME + "Probabile problema nel puntamento alla HashMap dei parametri.\n";
            LOG.severe(msg); 
            throw new WebStorageException(msg + nfe.getMessage(), nfe);
        } catch (SQLException sqle) {
            String msg = FOR_NAME + "Tupla non aggiornata correttamente; problema nella query che aggiorna il progetto.\n";
            LOG.severe(msg); 
            throw new WebStorageException(msg + sqle.getMessage(), sqle);
        } catch (NumberFormatException nfe) {
            String msg = FOR_NAME + "Tupla non aggiornata correttamente; problema in un formato di dato.\n";
            LOG.severe(msg); 
            throw new WebStorageException(msg + nfe.getMessage(), nfe);
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
     * @param idProj    id del progetto da aggiornare 
     * @param projectsWritableByUser Vector contenente la lista dei progetti su cui l'utente corrente e' abilitato alla modifica
     * @param userId    id dell'utente che ha eseguito il login
     * @param projects  Map contenente la lista dei progetti su cui l'utente corrente e' abilitato alla modifica
     * @param activitiesRelatedToProject    HashMap contenente le attività su cui l'utente e' abilitato alla modifica
     * @param params    hashmap che contiene i parametri che si vogliono aggiornare del progetto
     * @throws WebStorageException se si verifica un problema nell'esecuzione della query, nell'accesso al db o in qualche tipo di puntamento 
     */
    @SuppressWarnings({ "null" })
    public void updateActivityPart(int idProj,
                                   Vector<ProjectBean> projectsWritableByUser,
                                   int userId,
                                   HashMap<Integer, ProjectBean> projects, 
                                   HashMap<Integer, Vector<ActivityBean>> activitiesRelatedToProject,
                                   HashMap<String, HashMap<String, String>>params) 
                            throws WebStorageException {
        //ResultSet rs = null;
        Connection con = null;
        PreparedStatement pst = null;        
        try {
            /* Ottiene il progetto precaricato quando l'utente si è loggato corrispondente al progetto che vuole aggiornare
            Integer key = new Integer(idProj);
            ProjectBean project = projects.get(key);
            // Recupera le liste di attività
            Vector<ActivityBean> activities = activitiesRelatedToProject.get(key);*/
            // Ottiene la connessione
            con = pol_manager.getConnection();
            /* ===  Controlla anzitutto che l'id progetto sulla querystring === *
             * ===  corrisponda a un id dei progetti scrivibili dall'utente === */
            try {
                if (!userCanWrite(idProj, projectsWritableByUser)) {
                    String msg = FOR_NAME + "Attenzione: l'utente ha tentato di modificare un\'attivita\' legata ad un progetto su cui non ha i diritti di scrittura!\n";
                    LOG.severe(msg + "Problema durante il tentativo di inserire una nuova attivita\'.\n");
                    throw new WebStorageException(msg);
                }
            } catch (WebStorageException wse) {
                String msg = FOR_NAME + "Problema nel tentativo di capire se l\'utente ha o non ha i diritti di scrittura!\n";
                LOG.severe(msg + "Problema durante il tentativo di inserire una nuova attivita\'.\n");
                throw new WebStorageException(msg);
            }
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
    
    
    /** <p>Aggiorna una attivit&agrave; di dato id, di un dato progetto.</p>
     * 
     * @param idProj    id del progetto da aggiornare 
     * @param user      utente che ha eseguito il login
     * @param projectsWritableByUser Vector contenente la lista dei progetti su cui l'utente corrente e' abilitato alla modifica
     * @param userWritableActivitiesByProjectId HashMap contenente le attività su cui l'utente e' abilitato alla modifica, indicizzate per Wrapper di identificativo progetto
     * @param params    hashmap che contiene i parametri che si vogliono aggiornare dell'attivita'
     * @throws WebStorageException se si verifica un problema nell'esecuzione della query, nell'accesso al db o in qualche tipo di puntamento 
     */
    @SuppressWarnings({ "null" })
    public void updateActivity(int idProj,
                               PersonBean user,
                               Vector<ProjectBean> projectsWritableByUser, 
                               HashMap<Integer, Vector<ActivityBean>> userWritableActivitiesByProjectId,
                               HashMap<String, String> params) 
                        throws WebStorageException {
        Connection con = null;
        PreparedStatement pst, pst1 = null;
        ActivityBean aw = null;
        int calculatedId = -1;
        try {
            con = pol_manager.getConnection();
            /* ===  Controlla anzitutto che l'id progetto sulla querystring === *
             * ===  corrisponda a un id dei progetti scrivibili dall'utente === */
            try {
                if (!userCanWrite(idProj, projectsWritableByUser)) {
                    String msg = FOR_NAME + "Attenzione: l'utente ha tentato di modificare un\'attivita\' legata ad un progetto su cui non ha i diritti di scrittura!\n";
                    LOG.severe(msg + "Problema durante il tentativo di inserire una nuova attivita\'.\n");
                    throw new WebStorageException(msg);
                }
            } catch (WebStorageException wse) {
                String msg = FOR_NAME + "Problema nel tentativo di capire se l\'utente ha o non ha i diritti di scrittura!\n";
                LOG.severe(msg + "Problema durante il tentativo di aggiornare un\'attivita\'.\n");
                throw new WebStorageException(msg);
            }
            // Ottiene l'id da aggiornare
            calculatedId = Integer.parseInt(params.get("act-id"));
            /* ===   Controlla che l'id attivita' passato dalla form        === * 
             * ===   corrisponda a un id di attività scrivibili dall'utente === */
            // Ottiene la lista di attività scrivibili per il progetto in esame
            Vector<ActivityBean> writableActivities = userWritableActivitiesByProjectId.get(new Integer(idProj));
            // Ottiene la cardinalità della lista delle atività precaricate 
            // quando l'utente si &egrave; loggato, una delle quali deve 
            // corrispondere all'attività da modificare
            int lastIndexOf = writableActivities.size();
            int index = 0;
            do {
                aw = writableActivities.elementAt(index);
                // Se un id di attività scrivibili è uguale all'id sulla querystring è tutto ok 
                if (aw.getId() == calculatedId) {
                    break;
                }
                index++;
            } while (index < lastIndexOf);
            // In caso contrario, ovvero se abbiamo raggiunto la fine del Vector senza trovare l'id sulla querystring, sono guai...
            if (index == lastIndexOf) {
                String msg = FOR_NAME + "Attenzione: l'utente ha tentato di modificare un\'attivita\' su cui non ha i diritti di scrittura!\n";
                LOG.severe(msg + "Problema durante il tentativo di aggiornare un\'attivita\'.\n");
                throw new WebStorageException(msg);
            }            
            /* === Se siamo qui vuol dire che l'id dell'attivita' su cui    === * 
             * === si deve aggiungere/modificare informazioni e' una        === * 
             * === attivita' scrivibile dall'utente                         === */
            // Begin: ==>
            con.setAutoCommit(false);
            pst = con.prepareStatement(UPDATE_ACTIVITY);
            pst.clearParameters();
             // Prepara i parametri per l'inserimento
            try {
                /* === Variabili locali === */
                // Definisce un contatore per l'indice del parametro
                int nextParam = 0;
                // Definisce un valore boolean in funzione del checkbox
                boolean milestone = params.get("act-milestone").equals("on") ? true : false;
                /* === Nome === */
                pst.setString(++nextParam, params.get("act-name"));
                /* === Descrizione === */
                pst.setString(++nextParam, params.get("act-descr"));
                /* === Controllo lato server sull'input delle date previste === */
                java.util.Date dataInizio = null;
                java.sql.Date dataInizioSQL = null;
                if (params.get("act-datainizio") != null) {
                    dataInizio = Utils.format(params.get("act-datainizio"), "dd/MM/yyyy", Query.DATA_SQL_PATTERN);
                    dataInizioSQL = Utils.convert(dataInizio);
                }
                java.util.Date dataFine = Utils.format(params.get("act-datafine"), "dd/MM/yyyy", Query.DATA_SQL_PATTERN);
                // Data fine non deve essere minore di Data inizio
                if (dataInizio != null && dataInizio.compareTo(dataFine) > NOTHING) {
                    throw new WebStorageException("La data di fine attivita\' e\' minore di quella di inizio attivita\'.\n");
                }
                // Data inizio (facoltativa, potrebbe valere null)
                pst.setDate(++nextParam, dataInizioSQL);
                // Data fine (obbligatoria)
                pst.setDate(++nextParam, Utils.convert(dataFine)); // non accetta una data gg/mm/aaaa, ma java.sql.Date
                // Campo residuale (Data inizio attesa)
                pst.setDate(++nextParam, null);
                // Campo residuale (Data fine attesa)
                pst.setDate(++nextParam, null);   
                /* === Gestione delle date effettive, facoltative === */
                java.util.Date dataInizioEffettiva = null;
                java.util.Date dataFineEffettiva = null;
                java.sql.Date date = null;
                if (params.get("act-datainiziovera") != null) {
                    dataInizioEffettiva = Utils.format(params.get("act-datainiziovera"), "dd/MM/yyyy", Query.DATA_SQL_PATTERN);
                    date = Utils.convert(dataInizioEffettiva);
                }
                // Data inizio effettiva
                pst.setDate(++nextParam, date); // non accetta una data italiana, ma java.sql.Date
                date = null;
                if (params.get("act-datafinevera") != null) {
                    dataFineEffettiva = Utils.format(params.get("act-datafinevera"), "dd/MM/yyyy", Query.DATA_SQL_PATTERN);
                    date = Utils.convert(dataFineEffettiva);
                }
                // Data fine effettiva
                pst.setDate(++nextParam, date); // non accetta una data italiana, ma java.sql.Date
                // Una o entrambe le date potrebbero essere null
                if (dataInizioEffettiva != null && dataFineEffettiva != null) {
                    // Data fine non deve essere minore di Data inizio
                    if (dataInizioEffettiva.compareTo(dataFineEffettiva) > NOTHING) {
                        throw new WebStorageException("La data di fine effettiva attivita\' e\' minore di quella di inizio effettiva attivita\'.\n");
                    }
                }
                /* === Gestione GU === */
                // Gestione giorni uomo previsti
                Integer gu = null;
                if (!params.get("act-guprevisti").equals(Utils.VOID_STRING)) {
                    gu = new Integer(params.get("act-guprevisti"));
                    // Giorni uomo previsti
                    pst.setInt(++nextParam, gu);
                } else {
                    // Dato facoltativo non inserito
                    pst.setNull(++nextParam, Types.NULL);
                }
                // Gestione giorni uomo effettivi
                gu = null;
                if (!params.get("act-gueffettivi").equals(Utils.VOID_STRING)) {
                    gu = new Integer(params.get("act-gueffettivi"));
                    // Giorni uomo effettivi
                    pst.setInt(++nextParam, gu);
                } else {
                    // Dato facoltativo non inserito
                    pst.setNull(++nextParam, Types.NULL);
                }
                // Gestione giorni uomo rimanenti
                gu = null;
                if (!params.get("act-gurimanenti").equals(Utils.VOID_STRING)) {
                    gu = new Integer(params.get("act-gurimanenti"));
                    // Giorni uomo rimanenti
                    pst.setInt(++nextParam, gu);
                } else {
                    // Dato facoltativo non inserito
                    pst.setNull(++nextParam, Types.NULL);
                }
                /* === Note di avanzamento === */
                pst.setString(++nextParam, params.get("act-progress"));
                /* === Risultati raggiunti === */
                pst.setString(++nextParam, params.get("act-result"));
                /* === Milestone === */
                pst.setBoolean(++nextParam, milestone);
                // E' vietato modificare l'id progetto:
                // pst.setInt(++nextParam, idProj);
                /* === WBS aggregante === */
                pst.setInt(++nextParam, Integer.parseInt(params.get("act-wbs")));
                /* === Struttura gregaria === */
                Integer idStrutturaGregaria = null;
                if (!params.get("act-grg").equals(Utils.VOID_STRING)) {
                    idStrutturaGregaria = new Integer(params.get("act-grg"));
                    pst.setInt(++nextParam, idStrutturaGregaria);
                } else {
                    // dato facoltativo non inserito
                    pst.setNull(++nextParam, Types.NULL);
                }
                 /* === Identificativo complessita' === */
                pst.setInt(++nextParam, Integer.parseInt(params.get("act-compl")));
                /* === Gestione stato automatico === */
                Date today = Utils.convert(Utils.getCurrentDate());
                // Gestione congruenza tra stato e stato
                int idStato = aw.getIdStato();
                if (idStato == SOSPESA || idStato == ELIMINATA) {
                    // Se l'attività da aggiornare è sospesa o eliminata, lascia lo stato inalterato per rispettare la scelta dell'utente
                    pst.setInt(++nextParam, idStato);
                } else {
                    // In tutti gli altri casi lo stato è da ricalcolare
                    CodeBean stato = computeActivityState(dataInizio, dataFine, dataInizioEffettiva, dataFineEffettiva, today);
                    pst.setInt(++nextParam, stato.getId());
                }
                // Campi automatici: id utente, ora ultima modifica, data ultima modifica
                pst.setDate(++nextParam, Utils.convert(Utils.convert(Utils.getCurrentDate()))); // non accetta un GregorianCalendar né una data java.util.Date, ma java.sql.Date
                pst.setTime(++nextParam, Utils.getCurrentTime());   // non accetta una Stringa, ma un oggetto java.sql.Time
                pst.setString(++nextParam, user.getCognome() + String.valueOf(Utils.BLANK_SPACE) + user.getNome());
                // Where id = ?
                pst.setInt(++nextParam, calculatedId);
                // Query contestuale di aggiornamento in attivitagestione 
                pst1 = con.prepareStatement(UPDATE_PERSON_ON_ACTIVITY);
                pst1.clearParameters();
                pst1.setInt(1, Integer.parseInt(params.get("act-people")));
                pst1.setInt(2, Integer.parseInt(params.get("act-role")));
                pst1.setInt(3, calculatedId);
                pst.executeUpdate();
                pst1.executeUpdate();
                // End: <==
                con.commit();
            } catch (WebStorageException wse) {
                String msg = FOR_NAME + "Si e\' verificato un problema nella conversione di date.\n" + wse.getMessage();
                LOG.severe(msg);
                throw new WebStorageException(msg, wse);
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
            } catch (Exception e) {
                String msg = FOR_NAME + "Si e\' verificato un problema.\n" + e.getMessage();
                LOG.severe(msg);
                throw new WebStorageException(msg, e);
            }
        } catch (AttributoNonValorizzatoException anve) {
            String msg = FOR_NAME + "Si e\' tentato di accedere a un attributo di un bean obbligatorio ma non valorizzato.\n" + anve.getMessage();
            LOG.severe(msg + "Probabile problema nel tentativo di recuperare l'id del bean di un progetto scrivibile dall\'utente.\n");
            throw new WebStorageException(msg, anve);
        } catch (NumberFormatException nfe) {
            String msg = FOR_NAME + "Si e\' verificato un problema nella conversione di interi.\n" + nfe.getMessage();
            LOG.severe(msg);
            throw new WebStorageException(msg, nfe);
        } catch (SQLException sqle) {
            String msg = FOR_NAME + "Oggetto ActivityBean non valorizzato; problema nel codice SQL.\n";
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
     * <p>Imposta un'attivit&agrave; in uno stato di sospensione logica 
     * (attivit&agrave; sospesa o eliminata) il cui identificativo 
     * viene desunto dal valore di un parametro di navigazione 
     * passato come argomento.</p>
     * <p>Gestisce anche la riattivazione di un'attivit&agrave; precedentemente
     * sospesa, ricalcolandone lo stato aggiornato e reimpostandolo.</p>
     * 
     * @param idProj    identificativo del progetto nell'ambito del quale operare 
     * @param idAct     identificativo dell'attivita' da aggiornare
     * @param part      parametro di navigazione in base a cui si identifica se bisogna aggiornare l'attivita' in uno stato sospeso o eliminato, o ricalcolarlo
     * @param user      persona corrispondente all'utente loggato
     * @param projectsWritableByUser lista di progetti su cui l'utente corrente ha il diritto di scrivere
     * @param userWritableActivitiesByProjectId    lista di attivita' su cui l'utente corrente ha il diritto di scrivere
     * @throws WebStorageException se si verifica un problema in sql, nel recupero di valori, nella conversione di tipi o in qualche puntamento
     */
    @SuppressWarnings("null")
    public void updateActivityState(int idProj,
                                    int idAct,
                                    String part,
                                    PersonBean user,
                                    Vector<ProjectBean> projectsWritableByUser, 
                                    HashMap<Integer, Vector<ActivityBean>> userWritableActivitiesByProjectId) 
                             throws WebStorageException {
        Connection con = null;
        PreparedStatement pst = null;
        int idState = NOTHING;
        try {
            // Ottiene la connessione
            con = pol_manager.getConnection();
            /* ===  Controlla anzitutto che l'id progetto sulla querystring === *
             * ===  corrisponda a un id dei progetti scrivibili dall'utente === */
            try {
                if (!userCanWrite(idProj, projectsWritableByUser)) {
                    String msg = FOR_NAME + "Attenzione: l'utente ha tentato di modificare un\'attivita\' legata ad un progetto su cui non ha i diritti di scrittura!\n";
                    LOG.severe(msg + "Problema durante il tentativo di inserire una nuova attivita\'.\n");
                    throw new WebStorageException(msg);
                }
            } catch (WebStorageException wse) {
                String msg = FOR_NAME + "Problema nel tentativo di capire se l\'utente ha o non ha i diritti di scrittura!\n";
                LOG.severe(msg + "Problema durante il tentativo di inserire una nuova attivita\'.\n");
                throw new WebStorageException(msg);
            }
            /* ===   Controlla che l'id attivita' passato dalla form        === * 
             * ===   corrisponda a un id di attività scrivibili dall'utente === */
            // Ottiene la lista di attività scrivibili per il progetto in esame
            Vector<ActivityBean> writableActivities = userWritableActivitiesByProjectId.get(new Integer(idProj));
            // Ottiene la cardinalità della lista delle atività precaricate 
            // quando l'utente si &egrave; loggato, una delle quali deve 
            // corrispondere all'attività da modificare
            int lastIndexOf = writableActivities.size();
            int index = 0;
            do {
                ActivityBean aw = writableActivities.elementAt(index);
                // Se un id di attività scrivibili è uguale all'id sulla querystring è tutto ok 
                if (aw.getId() == idAct) {
                    break;
                }
                index++;
            } while (index < lastIndexOf);
            // In caso contrario, ovvero se abbiamo raggiunto la fine del Vector senza trovare l'id sulla querystring, sono guai...
            if (index == lastIndexOf) {
                String msg = FOR_NAME + "Attenzione: l'utente ha tentato di modificare un\'attivita\' su cui non ha i diritti di scrittura!\n";
                LOG.severe(msg + "Problema durante il tentativo di inserire una nuova attivita\'.\n");
                throw new WebStorageException(msg);
            }            
            /* === Se siamo qui vuol dire che l'id dell'attivita'   === * 
             * === su cui si deve modificare informazioni e' una    === * 
             * === attivita' scrivibile dall'utente                 === */
            if (part.equals(DELETE_PART)) {
                idState = ELIMINATA;
            } else if (part.equals(SUSPEND_PART)) {
                idState = SOSPESA;
            } else if (part.equals(RESUME_PART)) {
                CodeBean stateToCompute = null;
                ActivityBean actToResume = getActivity(idProj, idAct, user);
                stateToCompute = computeActivityState(actToResume.getDataInizio(), actToResume.getDataFine(), actToResume.getDataInizioEffettiva(), actToResume.getDataFineEffettiva(), Utils.convert(Utils.getCurrentDate()));
                idState = stateToCompute.getId();
            }
            // Begin: ==>
            con = pol_manager.getConnection();
            con.setAutoCommit(false);
            pst = con.prepareStatement(UPDATE_ACTIVITY_STATE);
            pst.clearParameters();
            pst.setInt(1, idState);
            // Campi automatici: id utente, ora ultima modifica, data ultima modifica
            pst.setDate(2, Utils.convert(Utils.convert(Utils.getCurrentDate()))); // non accetta un GregorianCalendar né una data java.util.Date, ma java.sql.Date
            pst.setTime(3, Utils.getCurrentTime());   // non accetta una Stringa, ma un oggetto java.sql.Time
            pst.setString(4, user.getCognome() + String.valueOf(Utils.BLANK_SPACE) + user.getNome());
            // Identificativo attività da aggiornare
            pst.setInt(5, idAct);
            pst.executeUpdate();
            con.commit();
        } catch (AttributoNonValorizzatoException anve) {
            String msg = FOR_NAME + "Probabile problema nel recupero dei dati dell\'autore ultima modifica.\n";
            LOG.severe(msg); 
            throw new WebStorageException(msg + anve.getMessage(), anve);
        } catch (SQLException sqle) {
            String msg = FOR_NAME + "Tupla non aggiornata correttamente; problema nella query che aggiorna lo stato dell\'attivita\'.\n";
            LOG.severe(msg); 
            throw new WebStorageException(msg + sqle.getMessage(), sqle);
        } catch (NumberFormatException nfe) {
            String msg = FOR_NAME + "Tupla non aggiornata correttamente; problema nella query che aggiorna lo stato dell\'attivita\'.\n";
            LOG.severe(msg); 
            throw new WebStorageException(msg + nfe.getMessage(), nfe);
        } catch (NullPointerException npe) {
            String msg = FOR_NAME + "Tupla non aggiornata correttamente; problema nella query che aggiorna lo stato dell\'attivita\'.\n";
            LOG.severe(msg); 
            throw new WebStorageException(msg + npe.getMessage(), npe);
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
     * <p>Aggiorna lo stato di tutte le attivit&agrave; che trova, salvo
     * che lo stesso abbia valore &quot;Sospeso&quot; o &quot;Eliminato&quot;
     * &ndash; nel qual caso lo lascia invariato.</p>
     * <p>Siccome questo metodo &egrave; pensato per essere richiamato da un
     * thread che prescinde dall'autenticazione, viene passato un utente 
     * convenzionale, al solo scopo di tracciare nel database l'avvenuto
     * aggiornamento.</p>
     * 
     * @param anonymous utente di convenzione
     * @return <code>int</code> - il numero di attivita' aggiornate - oppure zero in caso non vi sia stato l'aggiornamento
     * @throws WebStorageException se si verifica un problema nel recupero di un attributo di bean, o in qualche tipo di puntamento
     */
    @SuppressWarnings("null")
    public int updateActivitiesState(PersonBean anonymous) 
                              throws WebStorageException {
        Connection con = null;
        PreparedStatement pst = null;
        int idState = NOTHING;
        int results = NOTHING; 
        try {
            // Ottiene la connessione
            con = pol_manager.getConnection();
            /*  Siamo qui perche', siccome questo metodo e' di aggiornamento
             *  massivo e destinato ad essere lanciato dall'applicazione 
             *  stessa, possiamo risparmiarci tutto il "cappello" dei
             *  controlli di autenticazione: l'applicazione pol E' autenticata! */
            Vector<ActivityBean> activities = getActivities(NOTHING, GET_ALL_BY_CLAUSE);
            for (ActivityBean actToUpdate : activities) {
                // Controlla: se l'attività è sospesa o eliminata, lascia invariato
                if (actToUpdate.getIdStato() != SOSPESA && actToUpdate.getIdStato() != ELIMINATA) {
                    CodeBean stateToCompute = null;
                    stateToCompute = computeActivityState(actToUpdate.getDataInizio(), actToUpdate.getDataFine(), actToUpdate.getDataInizioEffettiva(), actToUpdate.getDataFineEffettiva(), Utils.convert(Utils.getCurrentDate()));
                    idState = stateToCompute.getId();
                    // Se lo stato attività ha lo stesso valore dello stato calcolato, salta la riga
                    if (actToUpdate.getIdStato() != idState) {
                        // Begin: ==>
                        con.setAutoCommit(false);
                        pst = con.prepareStatement(UPDATE_ACTIVITY_STATE);
                        pst.clearParameters();
                        pst.setInt(1, idState);
                        // Campi automatici: id utente, ora ultima modifica, data ultima modifica
                        pst.setDate(2, Utils.convert(Utils.convert(Utils.getCurrentDate()))); // non accetta un GregorianCalendar né una data java.util.Date, ma java.sql.Date
                        pst.setTime(3, Utils.getCurrentTime());   // non accetta una String, ma un oggetto java.sql.Time
                        pst.setString(4, anonymous.getCognome() + String.valueOf(Utils.BLANK_SPACE) + anonymous.getNome());
                        // Identificativo attività da aggiornare
                        pst.setInt(5, actToUpdate.getId());
                        pst.executeUpdate();
                        con.commit();
                        // <== :End
                        ++results;
                    }
                }
            }
            return results;
        } catch (AttributoNonValorizzatoException anve) {
            String msg = FOR_NAME + "Probabile problema nel recupero dei dati dell\'attivita\'.\n";
            LOG.severe(msg); 
            throw new WebStorageException(msg + anve.getMessage(), anve);
        } catch (SQLException sqle) {
            String msg = FOR_NAME + "Tupla non aggiornata correttamente; problema nella query che aggiorna lo stato dell\'attivita\'.\n";
            LOG.severe(msg); 
            throw new WebStorageException(msg + sqle.getMessage(), sqle);
        } catch (NumberFormatException nfe) {
            String msg = FOR_NAME + "Tupla non aggiornata correttamente; problema nella query che aggiorna lo stato dell\'attivita\'.\n";
            LOG.severe(msg); 
            throw new WebStorageException(msg + nfe.getMessage(), nfe);
        } catch (NullPointerException npe) {
            String msg = FOR_NAME + "Tupla non aggiornata correttamente; problema nella query che aggiorna lo stato dell\'attivita\'.\n";
            LOG.severe(msg); 
            throw new WebStorageException(msg + npe.getMessage(), npe);
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
    
    
    /** <p>Aggiorna un indicatore di dato id, collegato a un progetto
     * di dato id.</p>
     * 
     * @param idProj    id del progetto a cui l'indicatore da aggiornare e' collegato 
     * @param user      utente che ha eseguito il login
     * @param projectsWritableByUser Vector contenente la lista dei progetti su cui l'utente corrente e' abilitato alla modifica
     * @param userWritableIndicatorsByProjectId HashMap contenente gli indicatori su cui l'utente e' abilitato alla modifica, indicizzate per Wrapper di identificativo progetto
     * @param params    hashmap che contiene i parametri che si vogliono aggiornare dell'indicatore
     * @throws WebStorageException se si verifica un problema nell'esecuzione della query, nell'accesso al db o in qualche tipo di puntamento
     */
    @SuppressWarnings({ "null" })
    public void updateIndicator(int idProj,
                                PersonBean user,
                                Vector<ProjectBean> projectsWritableByUser, 
                                HashMap<Integer, Vector<IndicatorBean>> userWritableIndicatorsByProjectId,
                                HashMap<String, String> params) 
                         throws WebStorageException {
        Connection con = null;
        PreparedStatement pst = null;
        IndicatorBean iw = null;
        int calculatedId = -1;
        try {
            con = pol_manager.getConnection();
            /* ===  Controlla anzitutto che l'id progetto sulla querystring === *
             * ===  corrisponda a un id dei progetti scrivibili dall'utente === */
            try {
                if (!userCanWrite(idProj, projectsWritableByUser)) {
                    String msg = FOR_NAME + "Attenzione: l'utente ha tentato di modificare un\'attivita\' legata ad un progetto su cui non ha i diritti di scrittura!\n";
                    LOG.severe(msg + "Problema durante il tentativo di inserire una nuova attivita\'.\n");
                    throw new WebStorageException(msg);
                }
            } catch (WebStorageException wse) {
                String msg = FOR_NAME + "Problema nel tentativo di capire se l\'utente ha o non ha i diritti di scrittura!\n";
                LOG.severe(msg + "Problema durante il tentativo di aggiornare un indicatore.\n");
                throw new WebStorageException(msg);
            }
            // Ottiene l'id da aggiornare
            calculatedId = Integer.parseInt(params.get("ind-id"));
            /* ===   Controlla che l'id indicatore passato dalla form        === * 
             * ===   corrisponda a un id di indicatore scrivibili dall'utente === */
            // Ottiene la lista di attività scrivibili per il progetto in esame
            Vector<IndicatorBean> writableIndicators = userWritableIndicatorsByProjectId.get(new Integer(idProj));
            // Ottiene la cardinalità della lista delle atività precaricate 
            // quando l'utente si &egrave; loggato, una delle quali deve 
            // corrispondere all'attività da modificare
            int lastIndexOf = writableIndicators.size();
            int index = 0;
            do {
                iw = writableIndicators.elementAt(index);
                // Se un id di indicatori scrivibili è uguale all'id sulla querystring è tutto ok 
                if (iw.getId() == calculatedId) {
                    break;
                }
                index++;
            } while (index < lastIndexOf);
            // In caso contrario, ovvero se abbiamo raggiunto la fine del Vector senza trovare l'id sulla querystring, sono guai...
            if (index == lastIndexOf) {
                String msg = FOR_NAME + "Attenzione: l'utente ha tentato di modificare un indicatore su cui non ha i diritti di scrittura!\n";
                LOG.severe(msg + "Problema durante il tentativo di aggiornare un indicatore\'.\n");
                throw new WebStorageException(msg);
            }            
            /* === Se siamo qui vuol dire che l'id dell'indicatore su cui   === * 
             * === si deve aggiungere/modificare informazioni e' un         === * 
             * === indicatore scrivibile dall'utente                        === */
            // Begin: ==>
            con.setAutoCommit(false);
            pst = con.prepareStatement(UPDATE_INDICATOR);
            pst.clearParameters();
             // Prepara i parametri per l'aggiornamento
            try {
                /* === Variabili locali === */
                // Definisce un contatore per l'indice del parametro
                int nextParam = 0;
                /* === Nome === */
                pst.setString(++nextParam, params.get("ind-nome"));
                /* === Descrizione === */
                pst.setString(++nextParam, params.get("ind-descr"));
                /* === Baseline === */
                pst.setString(++nextParam, params.get("ind-baseline"));
                /* === Controllo lato server sull'input delle date previste === */
                java.util.Date dataBaseline = null;
                java.sql.Date dataBaselineSQL = null;
                if (params.get("ind-database") != null) {
                    dataBaseline = Utils.format(params.get("ind-database"), "dd/MM/yyyy", Query.DATA_SQL_PATTERN);
                    dataBaselineSQL = Utils.convert(dataBaseline);
                }
                java.util.Date dataTarget = Utils.format(params.get("ind-datatarget"), "dd/MM/yyyy", Query.DATA_SQL_PATTERN);
                // Data fine non deve essere minore di Data inizio
                if (dataBaseline != null && dataBaseline.compareTo(dataTarget) > NOTHING) {
                    throw new WebStorageException("La data di target e\' minore di quella di baseline.\n");
                }
                // Data Baseline
                pst.setDate(++nextParam, dataBaselineSQL);
                // Anno Baseline
                String ab = null;
                if (!params.get("ind-annobase").equals(Utils.VOID_STRING)) {
                    ab = new String(params.get("ind-annobase"));
                    pst.setString(++nextParam, ab);
                } else {
                    // dato facoltativo non inserito
                    pst.setNull(++nextParam, Types.NULL);
                }
                /* === Target === */
                pst.setString(++nextParam, params.get("ind-target"));
                // Data Target
                pst.setDate(++nextParam, Utils.convert(dataTarget)); // non accetta una data gg/mm/aaaa, ma java.sql.Date
                // Anno Target
                String at = null;
                if (!params.get("ind-annotarget").equals(Utils.VOID_STRING)) {
                    at = new String(params.get("ind-annotarget"));
                    pst.setString(++nextParam, at);
                } else {
                    // dato facoltativo non inserito
                    pst.setNull(++nextParam, Types.NULL);
                }
                /* === Id tipo indicatore === */
                pst.setInt(++nextParam, Integer.parseInt(params.get("ind-tipo")));
                /* === Riferimento a WBS === */
                if (!params.get("ind-wbs").equals(Utils.VOID_STRING)) {
                    int idWbs = Integer.parseInt(params.get("ind-wbs"));
                    pst.setInt(++nextParam, idWbs);
                } else {
                    // dato facoltativo non inserito
                    pst.setNull(++nextParam, Types.NULL);
                }
                /* === Campi automatici: id utente, ora ultima modifica, data ultima modifica === */
                pst.setDate(++nextParam, Utils.convert(Utils.convert(Utils.getCurrentDate()))); // non accetta un GregorianCalendar né una data java.util.Date, ma java.sql.Date
                pst.setTime(++nextParam, Utils.getCurrentTime());   // non accetta una Stringa, ma un oggetto java.sql.Time
                pst.setString(++nextParam, user.getCognome() + String.valueOf(Utils.BLANK_SPACE) + user.getNome());
                // Where id = ?
                pst.setInt(++nextParam, calculatedId);
                /* Query contestuale di aggiornamento wbs in indicatoregestione
                 * NON VIENE FATTO un aggiornamento contestuale dell'
                 * indicatoregestione (misurazione o rilevazione dell'indicatore)
                 * per il seguente motivo: le misurazioni, una volta fatte,
                 * non possono essere aggiornate. Quindi l'id_wbs presente nella
                 * misurazione era relativo alla WBS nel momento in cui 
                 * l'indicatore era effettivamente collegato a quella. Se un 
                 * giorno si vede che l'id_indicatore in indicatoregestione
                 * punta a un indicatore che nella sua tabella ha l'id_wbs
                 * diverso dall'id_wbs dell'indicatoregestione corrispondente,
                 * vuol dire che l'indicatore è stato aggiornato ed è stato
                 * fatto puntare a una WBS diversa da quella originaria, o
                 * comunque presente all'atto delle misurazioni. 
                pst1 = con.prepareStatement(UPDATE_INDICATOR_MEASUREMENT);
                pst1.clearParameters();
                pst1.setInt(1, Integer.parseInt(params.get("ind-wbs")));
                pst1.setInt(2, calculatedId);*/
                pst.executeUpdate();
                //pst1.executeUpdate();
                // End: <==
                con.commit();
            } catch (WebStorageException wse) {
                String msg = FOR_NAME + "Si e\' verificato un problema nella conversione di date.\n" + wse.getMessage();
                LOG.severe(msg);
                throw new WebStorageException(msg, wse);
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
            } catch (Exception e) {
                String msg = FOR_NAME + "Si e\' verificato un problema.\n" + e.getMessage();
                LOG.severe(msg);
                throw new WebStorageException(msg, e);
            }
        } catch (AttributoNonValorizzatoException anve) {
            String msg = FOR_NAME + "Si e\' tentato di accedere a un attributo di un bean obbligatorio ma non valorizzato.\n" + anve.getMessage();
            LOG.severe(msg + "Probabile problema nel tentativo di recuperare l'id del bean di un progetto scrivibile dall\'utente.\n");
            throw new WebStorageException(msg, anve);
        } catch (NumberFormatException nfe) {
            String msg = FOR_NAME + "Si e\' verificato un problema nella conversione di interi.\n" + nfe.getMessage();
            LOG.severe(msg);
            throw new WebStorageException(msg, nfe);
        } catch (SQLException sqle) {
            String msg = FOR_NAME + "Oggetto ActivityBean non valorizzato; problema nel codice SQL.\n";
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
     * <p>Aggiorna uno stato relativo a un dato indicatore, 
     * il cui identificativo &egrave; passato come parametro, 
     * e a un relativo progetto, il cui 
     * identificativo viene passato come parametro.</p>
     * 
     * @param idPrj identificativo del progetto che aggrega l'indicatore 
     * @param idInd identificativo dell'indicatore di cui si deve aggiornare lo stato
     * @param valueState valore stringa dello stato da aggiornare (tradotto in codice identificativo da subquery)
     * @throws WebStorageException se si verifica un problema nel recupero di qualche attributo, nella query o in qualche altro tipo di puntamento
     */
    @SuppressWarnings({ "null", "static-method" })
    public void updateIndicatorState(int idPrj,
                                     int idInd,
                                     String valueState) 
                              throws WebStorageException {
        Connection con = null;
        PreparedStatement pst = null;        
        try {
            // Ottiene la connessione
            con = pol_manager.getConnection();
            con.setAutoCommit(false);
            pst = con.prepareStatement(UPDATE_INDICATOR_BY_TOGGLE);
            pst.clearParameters();
            pst.setString(1, valueState);
            pst.setInt(2, idInd);
            pst.setInt(3, idPrj);
            pst.executeUpdate();
            con.commit();
        } catch (SQLException sqle) {
            String msg = FOR_NAME + "Tupla non aggiornata correttamente; problema nella query che aggiorna lo stato dell\'indicatore.\n";
            LOG.severe(msg); 
            throw new WebStorageException(msg + sqle.getMessage(), sqle);
        } catch (NumberFormatException nfe) {
            String msg = FOR_NAME + "Tupla non aggiornata correttamente; problema nel metodo che aggiorna lo stato dell\'indicatore.\n";
            LOG.severe(msg); 
            throw new WebStorageException(msg + nfe.getMessage(), nfe);
        } catch (NullPointerException npe) {
            String msg = FOR_NAME + "Tupla non aggiornata correttamente; problema di puntamento a un valore nullo.\n";
            LOG.severe(msg); 
            throw new WebStorageException(msg + npe.getMessage(), npe);
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
     * <p>Metodo per fare l'aggiornamento di una misurazione esistente 
     * (di indicatore).</p>
     * 
     * @param idProj    identificativo del progetto, al contesto del quale la misurazione fa riferimento
     * @param user      utente loggato
     * @param projectsWritableByUser    Vector contenente tutti i progetti scrivibili dall'utente; la misurazione deve essere agganciata ad undicatore di uno di questi
     * @param idMeasure identificativo della misurazione da aggiornare
     * @param params    tabella contenente tutti i valori che l'utente inserisce per l'aggiornamento della misurazione
     * @throws WebStorageException se si verifica un problema nel cast da String a Date, nell'esecuzione della query, nell'accesso al db o in qualche puntamento
     */
    @SuppressWarnings({ "null" })
    public void updateMeasurement(int idProj,
                                  PersonBean user, 
                                  Vector<ProjectBean> projectsWritableByUser,
                                  int idMeasure,
                                  HashMap<String, String> params) 
                           throws WebStorageException {
        Connection con = null;
        PreparedStatement pst = null;
        try {
            // Effettua la connessione
            con = pol_manager.getConnection();
            /* ===  Controlla anzitutto che l'id progetto sulla querystring === *
             * ===  corrisponda all'id progetto nel campo hidden della form === */
            int idProjForm = Integer.parseInt(params.get("prj-id"));
            if (idProj != idProjForm) {
                String msg = FOR_NAME + "Attenzione: l\'identificativo di progetto sulla querystring non corrisponde a quello passato dalla form!\n";
                LOG.severe(msg + "Problema durante il tentativo di inserire una nuova misurazione.\n");
                throw new WebStorageException(msg);
            }
            /* ===  Controlla quindi che l'id progetto sulla querystring    === *
             * ===  corrisponda a un id dei progetti scrivibili dall'utente === */
            try {
                if (!userCanWrite(idProj, projectsWritableByUser)) {
                    String msg = FOR_NAME + "Attenzione: l'utente ha tentato di inserire una misurazione legata ad un progetto su cui non ha i diritti di scrittura!\n";
                    LOG.severe(msg + "Problema durante il tentativo di inserire una nuova misurazione.\n");
                    throw new WebStorageException(msg);
                }
            } catch (WebStorageException wse) {
                String msg = FOR_NAME + "Problema nel tentativo di capire se l\'utente ha o non ha i diritti di scrittura!\n";
                LOG.severe(msg + "Problema durante il tentativo di inserire una nuova misurazione.\n");
                throw new WebStorageException(msg);
            }
            /* === Se siamo qui vuol dire che l'id del progetto su cui si   === * 
             * === deve aggiungere un'attivita' e' scrivibile dall'utente   === */
            // Deve fare un inserimento
            // Begin: ==>
            con.setAutoCommit(false);
            pst = con.prepareStatement(UPDATE_MEASUREMENT);
            pst.clearParameters();
            // Definisce un contatore per l'indice del parametro
            int nextParam = 0;
             // Prepara i parametri per l'inserimento
            try {
                /* === Valore === */
                pst.setString(++nextParam, params.get("mon-nome"));
                /* === Note === */
                String descr = null;
                if (!params.get("mon-descr").equals(Utils.VOID_STRING)) {
                    descr = new String(params.get("mon-descr"));
                    pst.setString(++nextParam, descr);
                } else {
                    // La motivazione è stata annullata 
                    pst.setNull(++nextParam, Types.NULL);
                    // (un po' strano...)
                    LOG.severe("Attenzione: nell\'effettuare l\'aggiornamento della misurazione di id " + idMeasure + " il campo descrizione e\' stato sbiancato.");
                }
                // Flag di ultima misurazione
                // Definisce un valore boolean in funzione del checkbox
                boolean last = params.get("mon-milestone").equals("on") ? true : false;
                // Chiude l'indicatore misurato se la misurazione è l'ultima
                if (last) {
                    // Set the Indicator in an inactive state
                    updateIndicatorState(idProj, Integer.parseInt(params.get("ind-id")), Query.STATI_PROGETTO_AS_LIST.get(3));
                } else {
                    // Set the Indicator in the active state
                    updateIndicatorState(idProj, Integer.parseInt(params.get("ind-id")), Query.STATI_PROGETTO_AS_LIST.get(0));
                }
                pst.setBoolean(++nextParam, last);
                /* === Id misurazione aggiornata === */
                pst.setInt(++nextParam, idMeasure);
                // End: <==
                pst.executeUpdate();
                con.commit();
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
            String msg = FOR_NAME + "Oggetto Bean non valorizzato; problema nel codice SQL.\n";
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
     * <p>Aggiorna ulteriori informazioni relative ad un indicatore 
     * il cui identificativo viene desunto dal valore di un parametro 
     * di navigazione passato come argomento.</p>
     * <p>Queste ulteriori informazioni vengono aggiornate a posteriori
     * su un aggiornamento fatto al target di un indicatore, generalmente
     * consistente in un ridimensionamento del target dell'indicatore.</p>
     * 
     * @param idProj    identificativo del progetto nell'ambito del quale operare 
     * @param idInd     identificativo dell'indicatore a corredo del quale si vuole inserire un valore ulteriore del target
     * @param user      persona corrispondente all'utente loggato
     * @param projectsWritableByUser lista di progetti scrivibili dall'utente
     * @param params    lista di parametri recuperati dalla form inviata in POST
     * @throws WebStorageException se si verifica un problema in sql, nel recupero di valori, nella conversione di tipi o in qualche puntamento
     */
    @SuppressWarnings({ "null" })
    public void updateFurtherInformation(int idProj,
                                         int idInd,
                                         PersonBean user,
                                         Vector<ProjectBean> projectsWritableByUser,
                                         HashMap<String, String>params) 
                        throws WebStorageException {
        Connection con = null;
        PreparedStatement pst = null;
        try {
            // Ottiene la connessione
            con = pol_manager.getConnection();
            /* ===  Controlla anzitutto che l'id progetto sulla querystring === *
             * ===  corrisponda all'id progetto nel campo hidden della form === */
            int idProjForm = Integer.parseInt(params.get("prj-id"));
            if (idProj != idProjForm) {
                String msg = FOR_NAME + "Attenzione: l\'identificativo di progetto sulla querystring non corrisponde a quello passato dalla form!\n";
                LOG.severe(msg + "Problema durante il tentativo di inserire informazioni aggiuntive all\'indicatore.\n");
                throw new WebStorageException(msg);
            }
            /* ===  Controlla quindi che l'id progetto sulla querystring    === *
             * ===  corrisponda a un id dei progetti scrivibili dall'utente === */
            try {
                if (!userCanWrite(idProj, projectsWritableByUser)) {
                    String msg = FOR_NAME + "Attenzione: l'utente ha tentato di inserire una misurazione legata ad un progetto su cui non ha i diritti di scrittura!\n";
                    LOG.severe(msg + "Problema durante il tentativo di inserire informazioni aggiuntive all\'indicatore.\n");
                    throw new WebStorageException(msg);
                }
            } catch (WebStorageException wse) {
                String msg = FOR_NAME + "Problema nel tentativo di capire se l\'utente ha o non ha i diritti di scrittura!\n";
                LOG.severe(msg + "Problema durante il tentativo di inserire informazioni aggiuntive all\'indicatore.\n");
                throw new WebStorageException(msg);
            }
            /* ===  Controlla poi che l'id indicatore sulla querystring === *
             * ===  corrisponda all'id progetto nel campo hidden della form === */
            int idIndForm = Integer.parseInt(params.get("ind-id"));
            if (idInd != idIndForm) {
                String msg = FOR_NAME + "Attenzione: l\'identificativo di indicatore sulla querystring non corrisponde a quello passato dalla form!\n";
                LOG.severe(msg + "Problema durante il tentativo di inserire un nuovo target.\n");
                throw new WebStorageException(msg);
            }
            /* === Se siamo qui vuol dire che sono stati superati tutti i controlli   === */ 
            // Definisce un contatore per l'indice del parametro
            int nextParam = 0;
            // Begin: ==>
            con.setAutoCommit(false);
            try {
                // Ottiene l'id dell'indicatore (non conosce l'id dell'aggiornamento da aggiornare, quindi si deve basare sull'id indicatore e altri parametri)
                int ind = Integer.parseInt(params.get("ind-id"));
                // Definisce un valore boolean in funzione della checkbox
                boolean auto = params.get("modext-auto").equals("on") ? true : false;
                // In base al valore della checkbox, capisce quale query eseguire
                if (auto) {
                    pst = con.prepareStatement(UPDATE_INDICATOR_EXTRAINFO);
                    pst.clearParameters();
                     // Prepara i parametri per l'inserimento
                    /* === Motivazione === */
                    pst.setString(++nextParam, params.get("ext-note"));
                    /* === Target === */
                    pst.setString(++nextParam, params.get("ext-target"));
                    /* === Data Target === */
                    java.util.Date dataTarget = Utils.format(params.get("ext-datatarget"), "dd/MM/yyyy", Query.DATA_SQL_PATTERN);
                    pst.setDate(++nextParam, Utils.convert(dataTarget)); // non accetta una data gg/mm/aaaa, ma java.sql.Date
                    /* === Anno Target === */
                    String at = null;
                    if (!params.get("ext-annotarget").equals(Utils.VOID_STRING)) {
                        at = new String(params.get("ext-annotarget"));
                        pst.setString(++nextParam, at);
                    } else {
                        // dato facoltativo non inserito
                        pst.setNull(++nextParam, Types.NULL);
                    }
                    /* === Data Inserimento === */
                    // Data Corrente inserita automaticamente, tanto l'aggiornamento target è sempre in data corrente
                    pst.setDate(++nextParam, Utils.convert(Utils.convert(Utils.getCurrentDate())));
                    /* === Campi automatici: id utente, ora ultima modifica, data ultima modifica === */
                    pst.setDate(++nextParam, Utils.convert(Utils.convert(Utils.getCurrentDate()))); // non accetta un GregorianCalendar né una data java.util.Date, ma java.sql.Date
                    pst.setTime(++nextParam, Utils.getCurrentTime());   // non accetta una Stringa, ma un oggetto java.sql.Time
                    pst.setString(++nextParam, user.getCognome() + String.valueOf(Utils.BLANK_SPACE) + user.getNome());
                } else {
                    pst = con.prepareStatement(UPDATE_INDICATOR_EXTRAINFO_LITE);
                    pst.clearParameters();
                     // Prepara i parametri per l'inserimento
                    /* === Motivazione === */
                    pst.setString(++nextParam, params.get("ext-note"));
                    /* === Target === */
                    pst.setString(++nextParam, params.get("ext-target"));
                }
                /* === Id indicatore === */
                pst.setInt(++nextParam, ind);
                /* === Id Progetto === */
                pst.setInt(++nextParam, Integer.parseInt(params.get("prj-id")));
                /* === Id indicatore === */
                pst.setInt(++nextParam, ind);
                /* === Id Progetto === */
                pst.setInt(++nextParam, Integer.parseInt(params.get("prj-id")));
                /* === Id indicatore === */
                pst.setInt(++nextParam, ind);
                /* === Id Progetto === */
                pst.setInt(++nextParam, Integer.parseInt(params.get("prj-id")));
            // End: <==
            pst.executeUpdate();
            con.commit();
            } catch (CommandException ce) {
                String msg = FOR_NAME + "Si e\' verificato un problema nella conversione di date.\n" + ce.getMessage();
                LOG.severe(msg);
                throw new WebStorageException(msg, ce);
            } catch (AttributoNonValorizzatoException anve) {
                String msg = FOR_NAME + "Probabile problema nel recupero dei dati dell\'autore ultima modifica.\n";
                LOG.severe(msg); 
                throw new WebStorageException(msg + anve.getMessage(), anve);
            } catch (SQLException sqle) {
                String msg = FOR_NAME + "Tupla non aggiornata correttamente; problema nella query che aggiorna il nuovo target.\n";
                LOG.severe(msg); 
                throw new WebStorageException(msg + sqle.getMessage(), sqle);
            } catch (NumberFormatException nfe) {
                String msg = FOR_NAME + "Tupla non aggiornata correttamente; problema nella query che aggiorna il nuovo target.\n";
                LOG.severe(msg); 
                throw new WebStorageException(msg + nfe.getMessage(), nfe);
            } catch (NullPointerException npe) {
                String msg = FOR_NAME + "Tupla non aggiornata correttamente; problema nella query che aggiorna il nuovo target.\n";
                LOG.severe(msg); 
                throw new WebStorageException(msg + npe.getMessage(), npe);
            }
        } catch (SQLException sqle) {
            String msg = FOR_NAME + "Oggetto Bean non valorizzato; problema nel codice SQL.\n";
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
    
    
    /** <p>Metodo che aggiorna le WBS di progetto.</p>
     * 
     * @param idProj id del progetto da aggiornare 
     * @param user utente che ha eseguito il login
     * @param projects Map contenente la lista dei progetti su cui l'utente corrente e' abilitato alla modifica
     * @param wbsRelatedToProject HashMap contenente le wbs su cui l'utente e' abilitato alla modifica
     * @param projectsWritableByUser lista di progetti su cui l'utente corrente ha il diritto di scrivere
     * @param params hashmap che contiene i parametri che si vogliono aggiornare del progetto
     * @throws WebStorageException se si verifica un problema nell'esecuzione della query, nell'accesso al db o in qualche tipo di puntamento 
     */
    @SuppressWarnings({ "null" })
    public void updateWbsPart(int idProj,
                              PersonBean user,
                              HashMap<Integer, ProjectBean> projects, 
                              HashMap<Integer, Vector<WbsBean>> wbsRelatedToProject,
                              Vector<ProjectBean> projectsWritableByUser,
                              HashMap<String, HashMap<String, String>>params) 
                       throws WebStorageException {
        Connection con = null;
        PreparedStatement pst = null;
        try {
            // Ottiene la connessione
            con = pol_manager.getConnection();
            /* ===  Controlla anzitutto che l'id progetto sulla querystring === *
             * ===  corrisponda a un id dei progetti scrivibili dall'utente === */
            try {
                if (!userCanWrite(idProj, projectsWritableByUser)) {
                    String msg = FOR_NAME + "Attenzione: l'utente ha tentato di modificare un\'attivita\' legata ad un progetto su cui non ha i diritti di scrittura!\n";
                    LOG.severe(msg + "Problema durante il tentativo di inserire una nuova attivita\'.\n");
                    throw new WebStorageException(msg);
                }
            } catch (WebStorageException wse) {
                String msg = FOR_NAME + "Problema nel tentativo di capire se l\'utente ha o non ha i diritti di scrittura!\n";
                LOG.severe(msg + "Problema durante il tentativo di inserire una nuova attivita\'.\n");
                throw new WebStorageException(msg);
            }
            HashMap<String, String> paramsWbs = null;
            int nextParam = 0;
            con.setAutoCommit(false);
            // Ramo di modifica di tutti i campi di una wbs
            if (params.containsKey(MODIFY_PART)) {
                paramsWbs = params.get(MODIFY_PART);
                if (Utils.containsValues(paramsWbs)) {
                    boolean workpackage = paramsWbs.get("wbs-workpackage").equals("on") ? true : false;
                    con.setAutoCommit(false);
                    pst = con.prepareStatement(UPDATE_WBS);
                    pst.clearParameters();
                    pst.setString(++nextParam, paramsWbs.get("wbs-name"));
                    pst.setString(++nextParam, paramsWbs.get("wbs-descr"));
                    pst.setBoolean(++nextParam, workpackage);
                    // Gestione wbs padre facoltativa
                    Integer idpadre = null;
                    if (!paramsWbs.get("wbs-idpadre").equals(Utils.VOID_STRING)) {
                        idpadre = new Integer(paramsWbs.get("wbs-idpadre"));
                        pst.setInt(++nextParam, idpadre);
                    } else {
                        // dato facoltativo non inserito
                        pst.setNull(++nextParam, Types.NULL);
                    }
                    pst.setString(++nextParam, paramsWbs.get("wbs-note"));
                    pst.setString(++nextParam, paramsWbs.get("wbs-result"));
                    pst.setDate(++nextParam, Utils.convert(Utils.convert(Utils.getCurrentDate())));
                    pst.setTime(++nextParam, Utils.getCurrentTime());
                    pst.setString(++nextParam, user.getCognome() + " " + user.getNome());
                    pst.setInt(++nextParam, Integer.parseInt(paramsWbs.get("wbs-id")));
                    pst.executeUpdate();
                    con.commit();
                }
            }
            // Ramo di modifica del solo padre della wbs
            else if (params.containsKey(PART_GRAPHIC)) {
                paramsWbs = params.get(PART_GRAPHIC);
                if (Utils.containsValues(paramsWbs)) {
                    pst = con.prepareStatement(UPDATE_WBS_FATHER);
                    pst.clearParameters();
                    // Controllo che l'id del padre non sia null
                    Integer idpadre = null;
                    if (!paramsWbs.get("wbs-idpadre").equals(Utils.VOID_STRING)) {
                        idpadre = new Integer(paramsWbs.get("wbs-idpadre"));
                        pst.setInt(++nextParam, idpadre);
                    } else {
                        // dato facoltativo non inserito
                        pst.setNull(++nextParam, Types.NULL);
                    }
                    pst.setDate(++nextParam, Utils.convert(Utils.convert(Utils.getCurrentDate())));
                    pst.setTime(++nextParam, Utils.getCurrentTime());
                    pst.setString(++nextParam, user.getCognome() + " " + user.getNome());
                    pst.setInt(++nextParam, Integer.parseInt(paramsWbs.get("wbs-id")));
                    pst.executeUpdate();
                    con.commit();
                }
            }
            // Ramo di modifica del solo ordinale della wbs
            else if (params.containsKey(ORDER_BY_PART)) {
                paramsWbs = params.get(ORDER_BY_PART);
                if (Utils.containsValues(paramsWbs)) {
                    pst = con.prepareStatement(UPDATE_WBS_ORDER);
                    pst.clearParameters();
                    // Controllo che l'id del padre non sia null
                    Integer ordinale = null;
                    if (!paramsWbs.get("wbs-ordinale").equals(Utils.VOID_STRING)) {
                        ordinale = new Integer(paramsWbs.get("wbs-ordinale"));
                        pst.setInt(++nextParam, ordinale);
                    } else {
                        // dato facoltativo non inserito
                        pst.setNull(++nextParam, Types.NULL);
                    }
                    pst.setDate(++nextParam, Utils.convert(Utils.convert(Utils.getCurrentDate())));
                    pst.setTime(++nextParam, Utils.getCurrentTime());
                    pst.setString(++nextParam, user.getCognome() + " " + user.getNome());
                    pst.setInt(++nextParam, Integer.parseInt(paramsWbs.get("wbs-id")));
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
    
    
    /**
     * <p>Imposta un'attivit&agrave; in uno stato di sospensione logica 
     * (attivit&agrave; sospesa o eliminata) il cui identificativo 
     * viene desunto dal valore di un parametro di navigazione 
     * passato come argomento.</p>
     * <p>Gestisce anche la riattivazione di un'attivit&agrave; precedentemente
     * sospesa, ricalcolandone lo stato aggiornato e reimpostandolo.</p>
     * 
     * @param idProj    identificativo del progetto nell'ambito del quale operare 
     * @param idWbs     identificativo della WBS da aggiornare
     * @param part      parametro di navigazione in base a cui si identifica se bisogna aggiornare la WBS in uno stato sospeso o (ri)attivo
     * @param user      persona corrispondente all'utente loggato
     * @param params    lista di parametri recuperati dalla form inviata in POST
     * @throws WebStorageException se si verifica un problema in sql, nel recupero di valori, nella conversione di tipi o in qualche puntamento
     */
    @SuppressWarnings({ "null" })
    public void updateWbsState(int idProj,
                               int idWbs,
                               String part,
                               PersonBean user, 
                               HashMap<String, String>params) 
                        throws WebStorageException {
        Connection con = null;
        PreparedStatement pst, pst1 = null;
        int idState = NOTHING;
        try {
            // Ottiene la connessione
            con = pol_manager.getConnection();
            if (part.equals(SUSPEND_PART)) {
                idState = CHIUSA;
            } else if (part.equals(RESUME_PART)) {                
                idState = APERTA;
            }
            // Definisce un contatore per l'indice del parametro
            int nextParam = 0;
            // Begin: ==>
            con = pol_manager.getConnection();
            con.setAutoCommit(false);
            pst = con.prepareStatement(UPDATE_WBS_STATE);
            pst.clearParameters();
            // id stato wbs
            pst.setInt(++nextParam, idState);
            // Campi automatici: id utente, ora ultima modifica, data ultima modifica
            pst.setDate(++nextParam, Utils.convert(Utils.convert(Utils.getCurrentDate()))); // non accetta un GregorianCalendar né una data java.util.Date, ma java.sql.Date
            pst.setTime(++nextParam, Utils.getCurrentTime());   // non accetta una Stringa, ma un oggetto java.sql.Time
            pst.setString(++nextParam, user.getCognome() + String.valueOf(Utils.BLANK_SPACE) + user.getNome());
            // Identificativo WBS da aggiornare
            pst.setInt(++nextParam, idWbs);
            // Se stiamo facendo una sospensione, dobbiamo inserirne anche gli estremi
            if (part.equals(SUSPEND_PART)) {
                // Azzera il contatore
                nextParam = 0;
                /* === Query contestuale di inserimento in wbsgestione === */ 
                int maxWbsGestId = getMax("wbsgestione") + 1;
                pst1 = con.prepareStatement(INSERT_WBS_STATE_EXTRAINFO);
                pst1.clearParameters();
                /* === id === */
                pst1.setInt(++nextParam, maxWbsGestId);
                /* === id_wbs === */
                pst1.setInt(++nextParam, idWbs);
                /* === id_progetto === */
                pst1.setInt(++nextParam, idProj);
                /* === data sospensione === */
                java.util.Date dataSospensione = Utils.format(params.get("wbs-data"), "dd/MM/yyyy", Query.DATA_SQL_PATTERN);
                java.sql.Date  dateAsSqlDate = Utils.convert(dataSospensione);
                pst1.setDate(++nextParam, dateAsSqlDate);
                /* === motivazione sospensione === */
                pst1.setString(++nextParam, params.get("wbs-note"));
                // Campi automatici: id utente, ora ultima modifica, data ultima modifica
                pst1.setDate(++nextParam, Utils.convert(Utils.convert(Utils.getCurrentDate()))); // non accetta un GregorianCalendar né una data java.util.Date, ma java.sql.Date
                pst1.setTime(++nextParam, Utils.getCurrentTime());   // non accetta una Stringa, ma un oggetto java.sql.Time
                pst1.setString(++nextParam, user.getCognome() + String.valueOf(Utils.BLANK_SPACE) + user.getNome());
                // Ri-Azzera il contatore
                nextParam = 0;
                pst1.executeUpdate();
            }
            pst.executeUpdate();
            con.commit();
        } catch (CommandException ce) {
            String msg = FOR_NAME + "Si e\' verificato un problema nella conversione di date.\n" + ce.getMessage();
            LOG.severe(msg);
            throw new WebStorageException(msg, ce);
        } catch (AttributoNonValorizzatoException anve) {
            String msg = FOR_NAME + "Probabile problema nel recupero dei dati dell\'autore ultima modifica.\n";
            LOG.severe(msg); 
            throw new WebStorageException(msg + anve.getMessage(), anve);
        } catch (SQLException sqle) {
            String msg = FOR_NAME + "Tupla non aggiornata correttamente; problema nella query che aggiorna lo stato della WBS.\n";
            LOG.severe(msg); 
            throw new WebStorageException(msg + sqle.getMessage(), sqle);
        } catch (NumberFormatException nfe) {
            String msg = FOR_NAME + "Tupla non aggiornata correttamente; problema nella query che aggiorna lo stato della WBS.\n";
            LOG.severe(msg); 
            throw new WebStorageException(msg + nfe.getMessage(), nfe);
        } catch (NullPointerException npe) {
            String msg = FOR_NAME + "Tupla non aggiornata correttamente; problema nella query che aggiorna lo stato della WBS.\n";
            LOG.severe(msg); 
            throw new WebStorageException(msg + npe.getMessage(), npe);
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
     * <p>Aggiorna un monitoraggio relativo a un dato anno solare, 
     * passato come argomento, e a un relativo dipartimento, il cui 
     * identificativo viene passato come argomento.</p>
     * 
     * @param year  anno del monitoraggio
     * @param idDip identificativo del dipartimento oggetto del monitoraggio
     * @param user  utente che ha effettuato la modifica
     * @param deptWhereUserIsBoss   lista dei dipartimenti su cui l'utente e' autorizzato con un ruolo elevato (> TL)
     * @param params    lista dei valori provenienti dalla form compilata dall'utente
     * @throws WebStorageException se si verifica un problema nel recupero di qualche attributo, nella query o in qualche altro tipo di puntamento
     */
    @SuppressWarnings({ "null", "static-method" })
    public void updateMonitorPart(int year,
                                  int idDip,
                                  PersonBean user,
                                  Vector<DepartmentBean> deptWhereUserIsBoss,
                                  ConcurrentHashMap<String, String> params) 
                           throws WebStorageException {
        Connection con = null;
        PreparedStatement pst = null;        
        try {
            // Ottiene la connessione
            con = pol_manager.getConnection();
            con.setAutoCommit(false);
            pst = con.prepareStatement(UPDATE_MONITOR_BY_DEPART);
            pst.clearParameters();
            pst.setString(1, params.get("mon-d4"));
            pst.setString(2, params.get("mon-d5"));
            pst.setString(3, params.get("mon-d6"));
            pst.setString(4, params.get("mon-d7"));
            pst.setString(5, params.get("mon-d8"));
            // Campi automatici: id utente, ora ultima modifica, data ultima modifica
            pst.setDate(6, Utils.convert(Utils.convert(Utils.getCurrentDate()))); // non accetta un GregorianCalendar né una data java.util.Date, ma java.sql.Date
            pst.setTime(7, Utils.getCurrentTime());   // non accetta una Stringa, ma un oggetto java.sql.Time
            pst.setString(8, user.getCognome() + String.valueOf(Utils.BLANK_SPACE) + user.getNome());
            pst.setInt(9, year);
            pst.setInt(10, idDip);
            pst.executeUpdate();
            con.commit();
        } catch (AttributoNonValorizzatoException anve) {
            String msg = FOR_NAME + "Probabile problema nel recupero dei dati dell\'autore ultima modifica.\n";
            LOG.severe(msg); 
            throw new WebStorageException(msg + anve.getMessage(), anve);
        } catch (SQLException sqle) {
            String msg = FOR_NAME + "Tupla non aggiornata correttamente; problema nella query che aggiorna il monitoraggio.\n";
            LOG.severe(msg); 
            throw new WebStorageException(msg + sqle.getMessage(), sqle);
        } catch (NumberFormatException nfe) {
            String msg = FOR_NAME + "Tupla non aggiornata correttamente; problema nella query che aggiorna il monitoraggio.\n";
            LOG.severe(msg); 
            throw new WebStorageException(msg + nfe.getMessage(), nfe);
        } catch (NullPointerException npe) {
            String msg = FOR_NAME + "Tupla non aggiornata correttamente; problema nella query che aggiorna il monitoraggio.\n";
            LOG.severe(msg); 
            throw new WebStorageException(msg + npe.getMessage(), npe);
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
     * <p>Aggiorna un monitoraggio relativo a un dato anno solare, 
     * passato come argomento, e a un relativo dipartimento, il cui 
     * identificativo viene passato come argomento.</p>
     * 
     * @param year  anno del monitoraggio
     * @param idDip identificativo del dipartimento oggetto del monitoraggio
     * @param flag  valore boolean che sostituisce il valore del campo 'attivo' nel monitor individuato
     * @throws WebStorageException se si verifica un problema nel recupero di qualche attributo, nella query o in qualche altro tipo di puntamento
     */
    @SuppressWarnings({ "null", "static-method" })
    public void updateMonitor(int year,
                              int idDip,
                              boolean flag) 
                       throws WebStorageException {
        Connection con = null;
        PreparedStatement pst = null;        
        try {
            // Ottiene la connessione
            con = pol_manager.getConnection();
            con.setAutoCommit(false);
            pst = con.prepareStatement(UPDATE_MONITOR_BY_TOGGLE);
            pst.clearParameters();
            pst.setBoolean(1, flag);
            pst.setInt(2, year);
            pst.setInt(3, idDip);
            pst.executeUpdate();
            con.commit();
        } catch (SQLException sqle) {
            String msg = FOR_NAME + "Tupla non aggiornata correttamente; problema nella query che aggiorna il monitoraggio.\n";
            LOG.severe(msg); 
            throw new WebStorageException(msg + sqle.getMessage(), sqle);
        } catch (NumberFormatException nfe) {
            String msg = FOR_NAME + "Tupla non aggiornata correttamente; problema nella query che aggiorna il monitoraggio.\n";
            LOG.severe(msg); 
            throw new WebStorageException(msg + nfe.getMessage(), nfe);
        } catch (NullPointerException npe) {
            String msg = FOR_NAME + "Tupla non aggiornata correttamente; problema nella query che aggiorna il monitoraggio.\n";
            LOG.severe(msg); 
            throw new WebStorageException(msg + npe.getMessage(), npe);
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
     * <p>Aggiorna i permessi degli utenti.</p>
     * <p>Siccome c'&egrave; un vincolo di 
     * <code>UNIQUE(id_progetto, id_persona)</code>, non &egrave; necessario
     * recuperare l'id della tupla della tabella <code>ruologestione</code>
     * al fine di aggiornarla, ma basta aggiornare la tupla con 
     * <pre> {id_progetto = x ^ id_persona = y}</pre>
     * Notare che sia id_progetto sia id_persona sono NOT NULL, per cui
     * la coppia (id_progetto, id_persona) &ndash; che &egrave; quindi 
     * <code>[UNIQUE, NOT NULL;]</code>, si configura, praticamente, 
     * come una seconda chiave primaria della tabella 
     * <code>ruologestione</code>.</p>
     * <p><code><strong>Nota sull'implementazione:</strong> 
     * merita un approfondimento l'implementazione della "cancellazione" 
     * del ruolo di una persona su un progetto.<br /> 
     * (i) L'applicazione permette
     * di inserire un ruolo "NESSUNO" (NONE) per un utente su un progetto.
     * Non avrebbe avuto senso gestire questo ruolo come un vero e proprio
     * ruolo di progetto, anche perch&eacute; poi, retrospettivamente, 
     * si sarebbe dovuti andare a cambiare tutte le query che mostrano 
     * progetti controllando che il ruolo dell'utente corrente non fosse
     * corrispondente a NONE. Pi&uacute; semplice e lineare la strada 
     * scelta: associare un utente a un progetto con il ruolo "NESSUNO" 
     * (NONE, /nʌn/) comporta il fatto che l'applicazione:
     * <ol>
     * <li>non lascia il ruolo "NONE" sul progetto stesso 
     * (a differenza di come fa per gli altri ruoli:
     * se l'utente con privilegi amministrativi decide che un dato utente
     * non &egrave; TL ma USER, l'applicazione cambia il ruolo e lascia
     * tutto il resto inalterato)</li>
     * <li>non effettua una DELETE della riga della tabella 
     * <strong>ruologestione</strong> che associa l'utente al progetto con
     * il precedente ruolo, perch&eacute; questa operazione potrebbe essere
     * considerata eccessivamente distruttiva, e peraltro non lascerebbe tracce.
     * dell'accaduto. Infatti, nell'applicazione <strong>pol</strong> 
     * le uniche operazioni non tracciate sono le cancellazioni fisiche 
     * (DELETE) &ndash; che infatti
     * non vengono praticamente mai consentite da interfaccia;</li>
     * <li>ma:
     * <ol>
     * <li>ricava (SELECT) l'identificativo del progetto phantom corrispondente
     * al progetto corrente</li>
     * <li>associa (UPDATE) il ruolo "NESSUNO" (NONE) al progetto phantom
     * individuato</li>
     * </ol></ol>
     * In questo modo l'applicazione ottiene il doppio scopo di:
     * <ul>
     * <li>dereferenziare il vecchio ruolo al progetto corrente</li>
     * <li>ma senza cancellarlo fisicamente, quindi conservandolo, 
     * anche rispetto al tracciamento dell'operazione</li>
     * </ul>
     * (ii) Ulteriore chiarificazione potrebbe essere necessaria per spiegare il 
     * significato di "progetto phantom": per ogni dipartimento &egrave;
     * stato definito un suo "progetto phantom", che ha lo stesso identificativo
     * del dipartimento in modulo, ma segno diverso in Z dove Z:<pre>
     * Z = {x &isin; <strong>R</strong> : x &egrave; un numero intero relativo}</pre>
     * o, se si preferisce:<pre>
     * Z = N &cup; {0} &cup;  {x &isin; <strong>N</strong> : x = x*-1}</pre> 
     * dove N &egrave; l'insieme dei numeri naturali 
     * (<strike>escluso quindi lo zero</strike> <small><em>che sto a di'?</em>
     * Z = N &cup;  {x &isin; <strong>N</strong> : x = x*-1} perch&eacute;
     * N = {0, 1, 2, 3 ...} con un unico punto di accumulazione a +&infin;</small>).<br />
     * Quindi, per esempio, se il dipartimento di giurisprudenza ha id = 2 
     * il suo progetto phantom, chiamato "phantom giurisprudenza" avr&agrave;
     * id = -2. Se quindi l'utente amministratore (PMO) decide di staccare
     * una persona da un progetto, stabilisce tramite interfaccia che quella
     * persona ha ruolo "NESSUNO" su quel progetto e quindi, a seguito 
     * dell'UPDATE, la riga:<pre>
     *  id | id_ruolo | id_progetto | id_persona | dataultimamodifica | oraultimamodifica | autoreultimamodifica
     * 134      3           11           74             null                 null                 null</pre>
     * diventer&agrave;<pre>
     *  id | id_ruolo | id_progetto | id_persona | dataultimamodifica | oraultimamodifica | autoreultimamodifica
     * 134      6           -2           74             2019-09-02           18:02:00        Bortolazzi Chiara</pre>
     * preservando, se non il ruolo che precedentemente aveva una data persona su
     * un dato progetto, quanto meno il fatto che l'annullamento del ruolo
     * &egrave; stato fatto da un determinato utente a una determinata data.
     * Ci&ograve; consentir&agrave; anche, abbastanza agevolmente, di
     * ripristinare il vecchio ruolo dell'utente su un progetto, tramite
     * l'UPDATE<sup>-1</sup> inversa fatta da Query SQL:<pre>
     * "UPDATE ruologestione   SET  id_ruolo = 3
     *                          ,   id_progetto = 11
     *                          ,   dataultimamodifica =  CURRENT_DATE
     *                          ,   oraultimamodifica = CURRENT_TIME
     *                          ,   autoreultimamodifica = 'Query SQL'
     *      WHERE  id = 134;"</pre>
     * (iii) Altra particolarit&agrave; riguarda la gestione degli id phantom,
     * da inserire come valori della chiave esterna che punta sull'identificativo
     * di progetto nella tabella <strong>ruologestione</strong>.
     * Se gi&agrave; esiste una tupla con<br /> 
     * (id_persona = guest.getId(), id_progetto = idProj)
     * allora assegna come id_progetto fittizio l'id 
     * immediatamente successivo in Z, ammesso che esista.<br />
     * Se l'identificativo di progetto cercato non esiste, 
     * solleva, finalmente, un'eccezione.<br />
     * P.es. poniamo che il dipartimento del progetto sia
     * quello avente id = 2 e quindi il suo phantom abbia 
     * id = -2; se una persona &egrave; gi&agrave; stata
     * staccata da un progetto del dipartimento 2, allora
     * sar&agrave; gi&agrave; presente la tupla con<br /> 
     * (id_persona = x, id_progetto = -2).<br />
     * Siccome esiste un vincolo di UNIQUE sulla coppia
     * (id_progetto, id_persona), non &egrave; possibile
     * inserire una nuova tupla con gli stessi valori:
     * (id_persona = x, id_progetto = -2).
     * A questo punto l'algoritmo cerca quindi di ricavare
     * un nuovo id_progetto da assegnare, incrementando
     * il numero in valore assoluto ma procedendo in Z 
     * "verso sinistra", cio&egrave; allontanandosi dall'
     * origine. Tuttavia, anche questo espediente
     * non garantisce la mancanza di eccezioni, perch&eacute;
     * esiste un vincolo di integrit&agrave; referenziale
     * tra l'id_progetto della tabella ruologestione e 
     * l'id della tabella progetto:<pre>
     * ALTER TABLE ruologestione ADD FOREIGN KEY (id_progetto) REFERENCES progetto (id);</pre>
     * Alla fine, non &egrave; possibile garantire tutte le possibili
     * situazioni; se non esiste un progetto phantom con id necessario
     * (p.es. id = -6, id = -7, id = -8...) si pu&ograve; sempre inserire oppure,
     * in alternativa, si pu&ograve; effettuare la cancellazione da Query SQL,
     * on demand. Si potrebbe anche procedere in altro modo, per garantire
     * l'inserimento, p.es. togliendo il vincolo di UNIQUE sulla coppia<br />
     * (id_progetto, id_persona) per&ograve; visto che questa operazione
     * di "cancellazione" del ruolo non &egrave; un'attivit&agrave; di 
     * ordinaria amministrazione ma serve solo a sanare qualche ruolo
     * erroneamente attribuito, non vale la pena di complicare ulteriormente
     * l'algoritmo &ndash; che per ulteriore sicurezza viene limitato 
     * alla gestione di 2 sole dereferenziazioni consentite.</code></p>
     * <p><small>About the method name:<br /> 
     * <cite id="cambridge" rel="https://dictionary.cambridge.org/it/grammatica/grammatica-britannico/permit-or-permission">
     * The countable noun <tt>permit</tt> (pronounced /ˈpɜ:mɪt/) refers to an 
     * official document that allows you to do something or go somewhere. 
     * The uncountable noun <tt>permission</tt> refers to when someone 
     * is allowed to do something.<br /> 
     * It does not refer to a document:<br /> 
     * <em>You need to have a work permit before you can work</em>.</cite><br />
     * So, using /permission/ instead of /permit/ is here 
     * legit AND preferable.</small></p>
     * 
     * @param user      utente che vuol cambiare i permessi degli utenti (deve essere un PMO)
     * @param guest     utente di cui si vuol cambiare i permessi
     * @param idDipart  identificativo del dipartimento di cui si vuol eventualmente recuperare il progetto phantom associato
     * @param params    tabella hash contentente tante coppie chiave/valore quanti sono i progetti su cui si vuol effettuare un cambiamento di ruolo
     * @throws WebStorageException se si verifica un problema nel recupero di qualche attributo, nella query o in qualche altro tipo di puntamento
     */
    @SuppressWarnings({ "null" })
    public void updatePermissions(PersonBean user,
                                  PersonBean guest,
                                  int idDipart,
                                  HashMap<Integer, String> params) 
                           throws WebStorageException {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            // Ottiene la connessione
            con = pol_manager.getConnection();
            // Inizia la transazione
            con.setAutoCommit(false);
            // Cicla la mappa ricevuta
            Iterator<Map.Entry<Integer, String>> iterator = params.entrySet().iterator();
            while (iterator.hasNext()) {
                // Scrittura distruttiva del contatore (v. Deitel & Deitel: "C, corso completo di programmazione", pp. 28-29)
                int nextParam = 0;
                // Stringa dinamica per contenere il valore del ruolo da passare alla query
                StringBuffer roleToSetAsStringBuffer = null;
                // Query designata per l'aggiornamento
                String query = null;
                // Entry corrente per puntamento ai valori parametrici
                Map.Entry<Integer, String> entry = iterator.next();
                // Recupera identificativo di progetto passato sotto forma di chiave 
                int idProj = entry.getKey();
                // Recupera il ruolo impostato per il progetto di dato id
                String roleToSet = entry.getValue().substring(entry.getValue().lastIndexOf(Data.HYPHEN) + 1);
                // Se il mio ruolo è "NESSUNO" deve dereferenziare il progetto collegato e associarlo al progetto phantom
                if (roleToSet.equalsIgnoreCase("NESSUNO")) {
                    // Recupera l'id di progetto phantom da inserire
                    pst = con.prepareStatement(GET_PHANTOM_PROJECT_BY_DEPART);
                    pst.clearParameters();
                    pst.setInt(++nextParam, idDipart);
                    rs = pst.executeQuery();
                    // Sovrascrive identificativo del progetto corrente... 
                    if (rs.next()) {
                        // ...con identificativo del progetto phantom per quel dipartimento
                        idProj = rs.getInt("id");
                    } else {
                        // Se non c'è già un progetto phantom associato a idDipart lo crea
                        idProj = insertPhantom(user, idDipart);
                    }
                    /* v. Nota sull'implementazione (iii): controlla se
                     * la coppia (id_persona, id_phantom) esiste gia' e in tal 
                     * caso cerca di assegnare come id_progetto phantom l'id
                     * phantom immediatamente precedente */ 
                    // Pulizia delle variabili
                    pst = null;
                    rs = null;
                    nextParam = 0;
                    // Controlla se la coppia (id_persona, id_progetto_phantom) esiste già
                    pst = con.prepareStatement(IS_PROJECT_BY_ROLE);
                    pst.clearParameters();
                    // Id persona da cercare
                    pst.setInt(++nextParam, guest.getId());
                    // Id progetto phantom di cui verificare l'associazione già esistente con la persona
                    pst.setInt(++nextParam, idProj);
                    rs = pst.executeQuery();
                    // Se è presente qualcosa non c'è bisogno di approfondire... 
                    if (rs.next()) {
                        // ...quel qualcosa sarà il valore boolean 'true' e quindi basta operare di conseguenza
                        --idProj;
                    }
                    // Designazione della query
                    query = DELETE_ROLE;
                    // Pulizia delle variabili
                    pst = null;
                    nextParam = 0;
                    // If you say: "NESSUNO" then you mean: /nʌn/
                    roleToSetAsStringBuffer = new StringBuffer("NONE");
                } else if (roleToSet.equalsIgnoreCase("PMO")) {
                    // If you say "PMO" then you mean "PMODIP"
                    roleToSetAsStringBuffer = new StringBuffer("PMODIP");
                    query = UPDATE_ROLES_BY_USER;
                } else {
                    // Whatever
                    roleToSetAsStringBuffer = new StringBuffer(roleToSet);
                    query = UPDATE_ROLES_BY_USER;
                }
                // Query
                pst = con.prepareStatement(query);
                pst.clearParameters();
                // Nome nuovo ruolo
                pst.setString(++nextParam, String.valueOf(roleToSetAsStringBuffer));
                // Progetto da referenziare in caso di cancellazione logica
                if (query.equals(DELETE_ROLE)) {
                    pst.setInt(++nextParam, idProj);
                }
                // Campi automatici: id utente, ora ultima modifica, data ultima modifica
                pst.setDate(++nextParam, Utils.convert(Utils.convert(Utils.getCurrentDate()))); // non accetta un GregorianCalendar né una data java.util.Date, ma java.sql.Date
                pst.setTime(++nextParam, Utils.getCurrentTime());   // non accetta una Stringa, ma un oggetto java.sql.Time
                pst.setString(++nextParam, user.getCognome() + String.valueOf(Utils.BLANK_SPACE) + user.getNome());
                // Id progetto su cui cambiare il ruolo
                pst.setInt(++nextParam, entry.getKey());
                // Id persona a cui cambiare il ruolo
                pst.setInt(++nextParam, guest.getId());
                // Exec
                pst.executeUpdate();
            }
            // Se tutto è andato a buon fine, committa la transazione
            con.commit();
        } catch (AttributoNonValorizzatoException anve) {
            String msg = FOR_NAME + "Probabile problema nel recupero dei dati dell\'autore ultima modifica.\n";
            LOG.severe(msg); 
            throw new WebStorageException(msg + anve.getMessage(), anve);
        } catch (SQLException sqle) {
            String msg = FOR_NAME + "Tupla non aggiornata correttamente; problema nella query che aggiorna i permessi utente.\n";
            LOG.severe(msg); 
            throw new WebStorageException(msg + sqle.getMessage(), sqle);
        } catch (NumberFormatException nfe) {
            String msg = FOR_NAME + "Tupla non aggiornata correttamente; problema nella query che aggiorna i permessi utente.\n";
            LOG.severe(msg); 
            throw new WebStorageException(msg + nfe.getMessage(), nfe);
        } catch (NullPointerException npe) {
            String msg = FOR_NAME + "Tupla non aggiornata correttamente; problema nella query che aggiorna i permessi utente.\n";
            LOG.severe(msg); 
            throw new WebStorageException(msg + npe.getMessage(), npe);
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
     * <p>Verifica se per l'utente loggato esiste una tupla che indica
     * un precedente login. 
     * <ul>
     * <li>Se non esiste una tupla per l'utente loggato, la inserisce.</li>
     * <li>Se esiste una tupla per l'utente loggato, la aggiorna.</li>
     * </ul>
     * In questo modo, il metodo gestisce nella tabella degli accessi
     * sempre l'ultimo accesso e non quelli precedenti.</p>
     * 
     * @param username      login dell'utente (username usato per accedere)
     * @param ip            indirizzo IPv4 assegnato all'utente
     * @param remoteHost    nome del client da cui l'utente ha fatto il login
     * @param serverName    nome del server su cui l'utente ha fatto il login
     * @throws WebStorageException se si verifica un problema SQL o in qualche tipo di puntamento
     */
    @SuppressWarnings({ "null" })
    public void manageAccess(String username,
                             StringBuffer ip,
                             StringBuffer remoteHost,
                             String serverName)
                      throws WebStorageException {
        Connection con = null;
        PreparedStatement pst = null; 
        ResultSet rs = null;
        CodeBean accessRow = null;
        int nextParam = NOTHING;
        try {
            // Ottiene la connessione
            con = pol_manager.getConnection();
            // Verifica se la login abbia già fatto un accesso
            pst = con.prepareStatement(GET_ACCESSLOG_BY_LOGIN);
            pst.clearParameters();
            pst.setString(++nextParam, username);
            rs = pst.executeQuery();
            if (rs.next()) {    // Esiste già un accesso: lo aggiorna
                accessRow = new CodeBean();
                BeanUtil.populate(accessRow, rs);
                pst = null;
                con.setAutoCommit(false);
                pst = con.prepareStatement(UPDATE_ACCESSLOG_BY_USER);
                pst.clearParameters();
                pst.setString(nextParam, username);
                pst.setString(++nextParam, new String(ip));
                pst.setString(++nextParam, new String(remoteHost));
                pst.setString(++nextParam, serverName);
                // Campi automatici: ora ultimo accesso, data ultimo accesso
                pst.setDate(++nextParam, Utils.convert(Utils.convert(Utils.getCurrentDate()))); // non accetta un GregorianCalendar né una data java.util.Date, ma java.sql.Date
                pst.setTime(++nextParam, Utils.getCurrentTime());   // non accetta una Stringa, ma un oggetto java.sql.Time
                pst.setInt(++nextParam, accessRow.getId());
                pst.executeUpdate();
                con.commit();
            } else {            // Non esiste un accesso: ne crea uno nuovo
                // Chiude e annulla il PreparedStatement rimasto inutilizzato
                pst.close();
                pst = null;
                // BEGIN;
                con.setAutoCommit(false);
                pst = con.prepareStatement(INSERT_ACCESSLOG_BY_USER);
                pst.clearParameters();
                int nextVal = getMax("accesslog") + 1;
                pst.setInt(nextParam, nextVal);
                pst.setString(++nextParam, username);
                pst.setString(++nextParam, new String(ip));
                pst.setString(++nextParam, new String(remoteHost));
                pst.setString(++nextParam, serverName);
                pst.setDate(++nextParam, Utils.convert(Utils.convert(Utils.getCurrentDate())));
                pst.setTime(++nextParam, Utils.getCurrentTime());
                pst.executeUpdate();
                // END;
                con.commit();
            }
            String msg = "Si e\' loggato l\'utente: " + username + 
                         " dall\'IP:" + ip + 
                         " dal client:" + remoteHost +
                         " sul server:" + serverName + 
                         " in data:" + Utils.format(Utils.getCurrentDate()) +
                         " alle ore:" + Utils.getCurrentTime() +
                         ".\n";
            LOG.info(msg);
        } catch (AttributoNonValorizzatoException anve) {
            String msg = FOR_NAME + "Probabile problema nel recupero dell'id dell\'ultimo accesso\n";
            LOG.severe(msg); 
            throw new WebStorageException(msg + anve.getMessage(), anve);
        } catch (SQLException sqle) {
            String msg = FOR_NAME + "Tupla non aggiornata correttamente; problema nella query che inserisce o in quella che aggiorna ultimo accesso al sistema.\n";
            LOG.severe(msg); 
            throw new WebStorageException(msg + sqle.getMessage(), sqle);
        } catch (NumberFormatException nfe) {
            String msg = FOR_NAME + "Tupla non aggiornata correttamente; problema nella query che inserisce o in quella che aggiorna ultimo accesso al sistema.\n";
            LOG.severe(msg); 
            throw new WebStorageException(msg + nfe.getMessage(), nfe);
        } catch (NullPointerException npe) {
            String msg = FOR_NAME + "Tupla non aggiornata correttamente; problema nella query che inserisce o in quella che aggiorna ultimo accesso al sistema.\n";
            LOG.severe(msg); 
            throw new WebStorageException(msg + npe.getMessage(), npe);
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
     * <p>Aggiorna le informazioni relative alla dimensione di un file, i cui 
     * estremi vengono comunicati in un dictionary passato come argomento.</p>
     * <p>Notare che, almeno nel contesto di <code>Java 7</code>, con cui 
     * la presente applicazione &egrave; <cite>compliant</cite>, nel momento 
     * in cui un file si trova ancora in memoria volatile, 
     * cio&egrave; &ndash; in pratica &ndash; non in memoria di massa del server, 
     * Java non &egrave; in grado di calcolarne la dimensione. 
     * Per questo motivo, all'atto del caricamento
     * di un file vengono eseguite le seguenti operazioni, in transazione:
     * <ol>
     * <li>lettura del file da client e sua memorizzazione in RAM;</li>
     * <li>scrittura del file su server e salvataggio nel db della tupla che
     * rappresenta il file da un punto di vista logico;</li>
     * <li>lettura del file da disco (ovvero da file system del server); solo
     * ora &egrave; possibile calcolare la dimensione del file;</li>
     * <li>aggiornamento (postUpdate) della tupla che rappresenta il file
     * nel db, con indicazione della dimensione e di eventuali altre informazioni
     * non desumibili all'atto dell'upload.</li>
     * </ol>
     * Questo tipo di aggiornamento contestuale, anche se
     * logicamente successivo, ad un'altra operazione di scrittura 
     * prende il nome di <cite>meccanismo di post-update</cite> 
     * o <cite>postUpdate hook</cite> 
     * (nel caso specifico, si tratta di un <cite>post-insert</cite>).</p>
     *
     * @param params HashMap contenente gli estremi del file, al fine di aggiornare la tupla esatta e nel modo corretto
     * @return <code>boolean</code> - true se l'operazione di aggiornamento e' andata a buon fine
     * @throws WebStorageException se si verifica un problema SQL o in qualche tipo di puntamento
     */
    @SuppressWarnings({ "static-method", "null" })
    public boolean postUpdateFileDoc(HashMap<String, Object> params)
                              throws WebStorageException {
        Connection con = null;
        PreparedStatement pst = null;
        String table = params.get("nomeEntita") + "_" + params.get("nomeAttributo");
        assert(!table.equals("_"));
        try {
            if (!params.containsKey("file")) {
                String msg = FOR_NAME + "Problema nel recupero di attributi del file.\n";
                LOG.severe(msg); 
                throw new WebStorageException(msg);
            }
            String file = (String) params.get("file");
            long size = (Long) params.get("dimensione");
            PersonBean user = (PersonBean) params.get("usr");
            // Campi calcolati al momento
            String usr = user.getCognome() + String.valueOf(Utils.BLANK_SPACE) + user.getNome();
            java.sql.Date today = Utils.convert(Utils.convert(Utils.getCurrentDate()));
            Time now = Utils.getCurrentTime();
            // BEGIN
            con = pol_manager.getConnection();
            con.setAutoCommit(false); 
            String query =
                    "UPDATE " + table +
                    "   SET dimensione = " + size +
                    "   ,   dataultimamodifica =    '" + today + "'" +
                    "   ,   oraultimamodifica =     '"   + now + "'" +
                    "   ,   autoreultimamodifica =  '" +  usr  + "'" + 
                    "   WHERE file = '" + file + "'"
                    ;
            pst = con.prepareStatement(query);
            pst.executeUpdate();
            // END
            con.commit();
            return true;
        } catch (SQLException sqle) {
            String msg = FOR_NAME + "Problema nella query di PostUpdate allegato.\n";
            LOG.severe(msg); 
            throw new WebStorageException(msg + sqle.getMessage(), sqle);
        } catch (AttributoNonValorizzatoException anve) {
            String msg = FOR_NAME + "Problema nel PostUpdate allegato.\n";
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
    
    /* ********************************************************** *
     *                        Metodi di POL                       *
    /* ********************************************************** *
     *                    Metodi  di  INSERIMENTO                 *
     * ********************************************************** */    
    /**
     * <p>Metodo per fare un inserimento di un nuovo stato progetto.</p>
     * 
     * @param user      utente loggato
     * @param idProj    id del progetto sul quale attribuire lo status
     * @param idStatus  id dello status che si va ad inserire
     * @throws WebStorageException se si verifica un problema nel cast da String a Date, nell'esecuzione della query, nell'accesso al db o in qualche tipo di puntamento
     */
    @SuppressWarnings("null")
    public void insertStatus(PersonBean user,
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
                pst.setTime(5, Utils.getCurrentTime());
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
                LOG.severe(msg + "Probabilmente la connessione vale null.\n"); 
                throw new WebStorageException(msg + npe.getMessage());
            } catch (SQLException sqle) {
                throw new WebStorageException(FOR_NAME + sqle.getMessage());
            }
        }
    }
    
    
    /**
     * <p>Metodo per fare un nuovo inserimento di una nuova attivit&agrave;.</p>
     * 
     * @param idProj    identificativo del progetto, al quale l'attivit&agrave; fa riferimento
     * @param user      utente loggato
     * @param projectsWritableByUser    Vector contenente tutti i progetti scrivibili dall'utente; l'attivita' deve essere agganciata ad uno di questi
     * @param params    tabella contenente tutti i valori che l'utente inserisce per la nuova attivit&agrave;
     * @throws WebStorageException se si verifica un problema nel cast da String a Date, nell'esecuzione della query, nell'accesso al db o in qualche puntamento
     */
    @SuppressWarnings({ "null" })
    public void insertActivity(int idProj,
                               PersonBean user, 
                               Vector<ProjectBean> projectsWritableByUser,
                               HashMap<String, String> params) 
                        throws WebStorageException {
        Connection con = null;
        PreparedStatement pst, pst1 = null;
        try {
            // Effettua la connessione
            con = pol_manager.getConnection();
            /* ===  Controlla anzitutto che l'id progetto sulla querystring === *
             * ===  corrisponda a un id dei progetti scrivibili dall'utente === */
            try {
                if (!userCanWrite(idProj, projectsWritableByUser)) {
                    String msg = FOR_NAME + "Attenzione: l'utente ha tentato di modificare un\'attivita\' legata ad un progetto su cui non ha i diritti di scrittura!\n";
                    LOG.severe(msg + "Problema durante il tentativo di inserire una nuova attivita\'.\n");
                    throw new WebStorageException(msg);
                }
            } catch (WebStorageException wse) {
                String msg = FOR_NAME + "Problema nel tentativo di capire se l\'utente ha o non ha i diritti di scrittura!\n";
                LOG.severe(msg + "Problema durante il tentativo di inserire una nuova attivita\'.\n");
                throw new WebStorageException(msg);
            }
            /* === Se siamo qui vuol dire che l'id del progetto su cui si   === * 
             * === deve aggiungere un'attivita' e' scrivibile dall'utente   === */
            // Deve fare un inserimento
            // Begin: ==>
            con.setAutoCommit(false);
            pst = con.prepareStatement(INSERT_ACTIVITY);
            pst.clearParameters();
             // Prepara i parametri per l'inserimento
            try {
                // Definisce un contatore per l'indice del parametro
                int nextParam = 0;
                // Ottiene l'id
                int maxActId = getMax("attivita") + 1;
                // Definisce un valore boolean in funzione del checkbox
                boolean milestone = params.get("act-milestone").equals("on") ? true : false;
                /* === Id === */
                pst.setInt(++nextParam, maxActId);
                /* === Nome === */
                pst.setString(++nextParam, params.get("act-name"));
                /* === Descrizione === */
                pst.setString(++nextParam, params.get("act-descr"));
                /* === Controllo lato server sull'input delle date previste === */
                java.util.Date dataInizio = null;
                java.sql.Date dataInizioSQL = null;
                if (params.get("act-datainizio") != null) {
                    dataInizio = Utils.format(params.get("act-datainizio"), "dd/MM/yyyy", Query.DATA_SQL_PATTERN);
                    dataInizioSQL = Utils.convert(dataInizio);
                }
                java.util.Date dataFine = Utils.format(params.get("act-datafine"), "dd/MM/yyyy", Query.DATA_SQL_PATTERN);
                // Data fine non deve essere minore di Data inizio
                if (dataInizio != null && dataInizio.compareTo(dataFine) > NOTHING) {
                    throw new WebStorageException("La data di fine attivita\' e\' minore di quella di inizio attivita\'.\n");
                }
                // Data inizio (facoltativa, potrebbe valere null)
                pst.setDate(++nextParam, dataInizioSQL);
                // Data fine (obbligatoria)
                pst.setDate(++nextParam, Utils.convert(dataFine)); // non accetta una data gg/mm/aaaa, ma java.sql.Date
                // Campo residuale
                pst.setDate(++nextParam, null);
                // Campo residuale
                pst.setDate(++nextParam, null);
                /* === Gestione delle date facoltative === */
                java.util.Date dataInizioEffettiva = null;
                java.util.Date dataFineEffettiva = null;
                java.sql.Date  dateAsSqlDate = null;
                if (params.get("act-datainiziovera") != null) {
                    dataInizioEffettiva = Utils.format(params.get("act-datainiziovera"), "dd/MM/yyyy", Query.DATA_SQL_PATTERN);
                    dateAsSqlDate = Utils.convert(dataInizioEffettiva);
                }
                // Data inizio effettiva
                pst.setDate(++nextParam, dateAsSqlDate); // non accetta una data italiana, ma java.sql.Date
                dateAsSqlDate = null;
                if (params.get("act-datafinevera") != null) {
                    dataFineEffettiva = Utils.format(params.get("act-datafinevera"), "dd/MM/yyyy", Query.DATA_SQL_PATTERN);
                    dateAsSqlDate = Utils.convert(dataFineEffettiva);
                }
                // Data fine effettiva
                pst.setDate(++nextParam, dateAsSqlDate); // non accetta una data italiana, ma java.sql.Date
                // Data fine non deve essere minore di Data inizio
                if (dataInizioEffettiva != null && dataFineEffettiva != null) {
                    if (dataInizioEffettiva.compareTo(dataFineEffettiva) > NOTHING) {
                        throw new WebStorageException("La data di fine effettiva attivita\' e\' minore di quella di inizio effettiva attivita\'.\n");
                    }
                }
                /* === Gestione GU === */
                // Gestione giorni uomo previsti
                Integer gu = null;
                if (!params.get("act-guprevisti").equals(Utils.VOID_STRING)) {
                    gu = new Integer(params.get("act-guprevisti"));
                    // giorni uomo previsti
                    pst.setInt(++nextParam, gu);
                } else {
                    // dato facoltativo non inserito
                    pst.setNull(++nextParam, Types.NULL);
                }
                // Gestione giorni uomo effettivi
                gu = null;
                if (!params.get("act-gueffettivi").equals(Utils.VOID_STRING)) {
                    gu = new Integer(params.get("act-gueffettivi"));
                    // giorni uomo effettivi
                    pst.setInt(++nextParam, gu);
                } else {
                    // dato facoltativo non inserito
                    pst.setNull(++nextParam, Types.NULL);
                }
                // Gestione giorni uomo rimanenti
                gu = null;
                if (!params.get("act-gurimanenti").equals(Utils.VOID_STRING)) {
                    gu = new Integer(params.get("act-gurimanenti"));
                    // giorni uomo rimanenti
                    pst.setInt(++nextParam, gu);
                } else {
                    // dato facoltativo non inserito
                    pst.setNull(++nextParam, Types.NULL);
                }
                /* === Note di avanzamento === */
                pst.setString(++nextParam, params.get("act-progress"));
                /* === Risultati raggiunti === */
                pst.setString(++nextParam, params.get("act-result"));
                /* === Milestone === */
                pst.setBoolean(++nextParam, milestone);
                /* === Riferimento a progetto === */
                pst.setInt(++nextParam, idProj);
                /* === Riferimento a WBS === */
                pst.setInt(++nextParam, Integer.parseInt(params.get("act-wbs")));
                /* === Struttura gregaria === */
                Integer idStrutturaGregaria = null;
                if (!params.get("act-grg").equals(Utils.VOID_STRING)) {
                    idStrutturaGregaria = new Integer(params.get("act-grg"));
                    pst.setInt(++nextParam, idStrutturaGregaria);
                } else {
                    // dato facoltativo non inserito
                    pst.setNull(++nextParam, Types.NULL);
                }
                /* === Riferimento a complessita' === */
                pst.setInt(++nextParam, Integer.parseInt(params.get("act-compl")));
                /* === Stato attività calcolato dall'applicazione === */
                CodeBean stato = computeActivityState(dataInizio, dataFine, dataInizioEffettiva, dataFineEffettiva, Utils.convert(Utils.getCurrentDate()));
                pst.setInt(++nextParam, stato.getId());
                /* === Campi automatici: id utente, ora ultima modifica, data ultima modifica === */
                pst.setDate(++nextParam, Utils.convert(Utils.convert(Utils.getCurrentDate()))); // non accetta un GregorianCalendar né una data java.util.Date, ma java.sql.Date
                pst.setTime(++nextParam, Utils.getCurrentTime());   // non accetta una Stringa, ma un oggetto java.sql.Time
                pst.setString(++nextParam, user.getCognome() + String.valueOf(Utils.BLANK_SPACE) + user.getNome());
                /* === Query contestuale di inserimento in attivitagestione === */ 
                int maxActGestId = getMax("attivitagestione") + 1;
                pst1 = con.prepareStatement(INSERT_PERSON_ON_ACTIVITY);
                pst1.clearParameters();
                pst1.setInt(1, maxActGestId);
                pst1.setInt(2, maxActId);
                pst1.setInt(3, Integer.parseInt(params.get("act-people")));
                pst1.setInt(4, Integer.parseInt(params.get("act-role")));
                pst.executeUpdate();
                pst1.executeUpdate();
                // End: <==
                con.commit();
            } catch (CommandException ce) {
                String msg = FOR_NAME + "Si e\' verificato un problema nella conversione di date.\n" + ce.getMessage();
                LOG.severe(msg);
                throw new WebStorageException(msg, ce);
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
            String msg = FOR_NAME + "Oggetto ActivityBean non valorizzato; problema nel codice SQL.\n";
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
     * @param idProj    identificativo del progetto, al quale l'attivit&agrave; fa riferimento
     * @param userId    identificativo dell'utente loggato
     * @param valuesRisk    Vector contenente i valori inseriti dall'utente per inserimento nuovo rischio
     * @throws WebStorageException se si verifica un problema nell'esecuzione della query, nell'accesso al db o in qualche tipo di puntamento
     */
    @SuppressWarnings({ "static-method", "null" })
    public void insertRisk(int idProj,
                           int userId, 
                           Vector<String> valuesRisk)
                    throws WebStorageException {
        Connection con = null;
        PreparedStatement pst = null;
        try {
            // Ottiene il progetto precaricato quando l'utente si è loggato corrispondente al progetto sul quale aggiungere un'attività
            Integer key = new Integer(idProj);
            int nextParam = 0;
            con = pol_manager.getConnection();
            con.setAutoCommit(false);
            pst = con.prepareStatement(INSERT_RISK);
            pst.clearParameters();
            pst.setInt(++nextParam, Integer.parseInt(valuesRisk.get(0)));
            pst.setString(++nextParam, valuesRisk.get(1));
            pst.setString(++nextParam, valuesRisk.get(2));
            pst.setString(++nextParam, valuesRisk.get(3));
            pst.setString(++nextParam, valuesRisk.get(4));
            pst.setString(++nextParam, valuesRisk.get(5));
            pst.setInt(++nextParam, key);
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
     * @param idProj    identificativo del progetto, al quale l'attivit&agrave; fa riferimento
     * @param userId    identificativo dell'utente loggato
     * @param valuesSkill   Vector contenente i valori inseriti dall'utente per inserimento nuovo rischio
     * @throws WebStorageException se si verifica un problema nell'esecuzione della query, nell'accesso al db o in qualche tipo di puntamento
     */
    @SuppressWarnings({ "static-method", "null" })
    public void insertSkill(int idProj,
                            int userId, 
                            Vector<String> valuesSkill)
                     throws WebStorageException {
        Connection con = null;
        PreparedStatement pst = null;
        try {
            // Ottiene il progetto precaricato quando l'utente si è loggato corrispondente al progetto sul quale aggiungere un'attività
            Integer key = new Integer(idProj);
            int nextParam = 0;
            con = pol_manager.getConnection();
            con.setAutoCommit(false);
            pst = con.prepareStatement(INSERT_SKILL);
            pst.clearParameters();
            pst.setInt(++nextParam, Integer.parseInt(valuesSkill.get(0)));
            pst.setString(++nextParam, valuesSkill.get(1));
            pst.setString(++nextParam, valuesSkill.get(2));
            pst.setBoolean(++nextParam, Boolean.parseBoolean(valuesSkill.get(3)));
            pst.setInt(++nextParam, key);
            pst.setInt(++nextParam, Integer.parseInt(valuesSkill.get(5)));
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
     * <p>Il metodo controlla anche se sia stato inserito un belongs (appartenenza)
     * tra la persona e il gruppo a cui il progetto &egrave; collegato. Non avrebbe
     * senso, infatti, aggiungere una competenza in assenza del relativo belongs.
     * Se si aggiunge una competenza, vuol dire che si vuole associare una o 
     * pi&uacute; attivit&agrave; alla persona, e ci&ograve; &egrave; possibile
     * solo se esiste una riga di belongs che dice che la persona appartiene al
     * gruppo del dipartimento a cui il progetto &egrave; collegato. 
     * In assenza di belongs che associa la persona al gruppo, e quindi al 
     * dipartimento del progetto, la persona stessa non compare nella casella
     * a discesa da cui &egrave; possibile selezionare una persona da associare
     * ad una attivit&agrave;. &Egrave quindi importante accertarsi che 
     * l'associazione tra la persona e il gruppo del dipartimento che aggrega
     * il progetto sia stabilita.</p>
     * 
     * @param idProj    identificativo del progetto, al quale si fa riferimento
     * @param skillId   identificativo della competenza che si vuole associare a una persona
     * @param user      utente loggato, per eventuali controlli di autenticazione
     * @param guest     persona a cui si vuol associare la competenza selezionata
     * @throws WebStorageException se si verifica un problema nell'esecuzione della query, nell'accesso al db o in qualche tipo di puntamento
     */
    @SuppressWarnings({ "null" })
    public void insertSkillByPerson(int idProj,
                                    int skillId, 
                                    PersonBean user,
                                    PersonBean guest)
                             throws WebStorageException {
        Connection con = null;
        PreparedStatement pst, pst1 = null;
        try {
            // Crea il contatore dei parametri della query principale
            int nextParam = 0;
            // Ottiene l'id
            int maxId = getMax("competenzagestione") + 1;
            // Ottiene la connessione
            con = pol_manager.getConnection();
            // Inizia la transazione
            con.setAutoCommit(false);
            // Query di inserimento di competenzagestione
            pst = con.prepareStatement(INSERT_COMPETENZA_GESTIONE);
            pst.clearParameters();
            pst.setInt(++nextParam, maxId);
            pst.setInt(++nextParam, skillId);
            pst.setInt(++nextParam, guest.getId());
            /* ****************************************************************
            Inserisce anche il belongs, se non c'è. Prima controlla se c'è.
            A tale scopo recupera l'id dell'utente associato alla persona di cui si conosce l'id
            Questo non era evitabile perche' a monte (cioe' passsato dalla pagina) 
            ho l'informazione dell'id della persona, non dell'id dell'utente, che in
            questo caso non e' in generale l'utente loggato (a meno che qualcuno
            voglia aggiungersi competenze, ammesso di averne i diritti)         
            ******************************************************************* */
            CodeBean usr = getUserByPersonId(guest.getId());
            if (!existsBelongsTo(usr.getId(), idProj)) {
                int maxBelongsId = getMax("belongs") + 1;
                pst1 = con.prepareStatement(INSERT_BELONGS_TO);
                pst1.clearParameters();
                pst1.setInt(1, maxBelongsId);
                pst1.setInt(2, usr.getId());
                pst1.setInt(3, idProj);
                pst1.executeUpdate();
            }
            pst.executeUpdate();
            con.commit();
        } catch (AttributoNonValorizzatoException anve) {
            String msg = FOR_NAME + "Impossibile recuperare l'identificativo della persona cui attribuire competenza.\n";
            LOG.severe(msg); 
            throw new WebStorageException(msg + anve.getMessage(), anve);
        } catch (SQLException sqle) {
            String msg = FOR_NAME + "Entita\' competenza gestione non valorizzata; problema nell\'inserimento competenzagestione.\n";
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
     * <p>Metodo per fare un nuovo inserimento di una nuova wbs relativa al progetto.</p>
     * 
     * @param idProj    identificativo del progetto, al quale l'attivit&agrave; fa riferimento
     * @param user      utente loggato
     * @param projectsWritableByUser lista di progetti su cui l'utente corrente ha il diritto di scrivere
     * @param params    hashmap contenente i valori inseriti dall'utente per inserimento nuova wbs
     * @throws WebStorageException se si verifica un problema nell'esecuzione della query, nell'accesso al db o in qualche tipo di puntamento
     */
    @SuppressWarnings("null")
    public void insertWbs(int idProj,
                          PersonBean user,
                          Vector<ProjectBean> projectsWritableByUser,
                          HashMap<String, String> params)
                   throws WebStorageException {
        Connection con = null;
        /* ===  Controlla anzitutto che l'id progetto sulla querystring === *
         * ===  corrisponda a un id dei progetti scrivibili dall'utente === */
        try {
            if (!userCanWrite(idProj, projectsWritableByUser)) {
                String msg = FOR_NAME + "Attenzione: l'utente ha tentato di modificare un\'attivita\' legata ad un progetto su cui non ha i diritti di scrittura!\n";
                LOG.severe(msg + "Problema durante il tentativo di inserire una nuova attivita\'.\n");
                throw new WebStorageException(msg);
            }
        } catch (WebStorageException wse) {
            String msg = FOR_NAME + "Problema nel tentativo di capire se l\'utente ha o non ha i diritti di scrittura!\n";
            LOG.severe(msg + "Problema durante il tentativo di inserire una nuova attivita\'.\n");
            throw new WebStorageException(msg);
        }
        PreparedStatement pst = null;
        try {
            // Ottiene il progetto precaricato quando l'utente si è loggato corrispondente al progetto sul quale aggiungere un'attività
            Integer key = new Integer(idProj);
            int nextParam = 0;
            // Definisce un valore boolean in funzione del checkbox
            boolean workpackage = params.get("wbs-workpackage").equals("on") ? true : false;
            int maxWbsId = getMax("wbs") + 1;
            con = pol_manager.getConnection();
            con.setAutoCommit(false);
            pst = con.prepareStatement(INSERT_WBS);
            pst.clearParameters();
            pst.setInt(++nextParam, maxWbsId);
            // Gestione wbs padre facoltativa
            Integer idpadre = null;
            if (!params.get("wbs-idpadre").equals(Utils.VOID_STRING)) {
                idpadre = new Integer(params.get("wbs-idpadre"));
                // id wbs padre effettivo
                pst.setInt(++nextParam, idpadre);
            } else {
                // dato facoltativo non inserito
                pst.setNull(++nextParam, Types.NULL);
            }
            pst.setInt(++nextParam, key);
            pst.setString(++nextParam, params.get("wbs-name"));
            pst.setString(++nextParam, params.get("wbs-descr"));
            pst.setBoolean(++nextParam, workpackage);
            pst.setDate(++nextParam, Utils.convert(Utils.convert(Utils.getCurrentDate()))); // non accetta un GregorianCalendar né una data java.util.Date, ma java.sql.Date
            pst.setTime(++nextParam, Utils.getCurrentTime());   // non accetta una Stringa, ma un oggetto java.sql.Time
            pst.setString(++nextParam, user.getCognome() + String.valueOf(Utils.BLANK_SPACE) + user.getNome());
            pst.setString(++nextParam, params.get("wbs-note"));
            pst.setString(++nextParam, params.get("wbs-result"));
            pst.executeUpdate();
            con.commit();
        } catch (SQLException sqle) {
            String msg = FOR_NAME + "Oggetto RiskBean non valorizzato; problema nella query dell\'utente.\n";
            LOG.severe(msg); 
            throw new WebStorageException(msg + sqle.getMessage(), sqle);
        } catch (AttributoNonValorizzatoException anve) {
            String msg = FOR_NAME + "Oggetto PersonBean non valorizzato; problema nella query di inserimento WBS.\n";
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
     * <p>Metodo per fare l'inserimento di un nuovo indicatore.</p>
     * 
     * @param idProj    identificativo del progetto, al contesto del quale l'indicatore fa riferimento
     * @param user      utente loggato
     * @param projectsWritableByUser    Vector contenente tutti i progetti scrivibili dall'utente; l'indicatore deve essere agganciata ad uno di questi
     * @param params    tabella contenente tutti i valori che l'utente inserisce per il nuovo indicatore;
     * @throws WebStorageException se si verifica un problema nel cast da String a Date, nell'esecuzione della query, nell'accesso al db o in qualche puntamento
     */
    @SuppressWarnings({ "null" })
    public void insertIndicator(int idProj,
                                PersonBean user, 
                                Vector<ProjectBean> projectsWritableByUser,
                                HashMap<String, String> params) 
                         throws WebStorageException {
        Connection con = null;
        PreparedStatement pst = null;
        try {
            // Effettua la connessione
            con = pol_manager.getConnection();
            /* ===  Controlla anzitutto che l'id progetto sulla querystring === *
             * ===  corrisponda a un id dei progetti scrivibili dall'utente === */
            try {
                if (!userCanWrite(idProj, projectsWritableByUser)) {
                    String msg = FOR_NAME + "Attenzione: l'utente ha tentato di inserire un indicatore legato ad un progetto su cui non ha i diritti di scrittura!\n";
                    LOG.severe(msg + "Problema durante il tentativo di inserire un nuovo indicatore.\n");
                    throw new WebStorageException(msg);
                }
            } catch (WebStorageException wse) {
                String msg = FOR_NAME + "Problema nel tentativo di capire se l\'utente ha o non ha i diritti di scrittura!\n";
                LOG.severe(msg + "Problema durante il tentativo di inserire un nuovo indicatore.\n");
                throw new WebStorageException(msg);
            }
            /* === Se siamo qui vuol dire che l'id del progetto su cui si   === * 
             * === deve aggiungere un'attivita' e' scrivibile dall'utente   === */
            // Deve fare un inserimento
            // Begin: ==>
            con.setAutoCommit(false);
            pst = con.prepareStatement(INSERT_INDICATOR);
            pst.clearParameters();
             // Prepara i parametri per l'inserimento
            try {
                // Definisce un contatore per l'indice del parametro
                int nextParam = 0;
                // Ottiene l'id
                int maxActId = getMax("indicatore") + 1;
                /* === Id === */
                pst.setInt(++nextParam, maxActId);
                /* === Nome === */
                pst.setString(++nextParam, params.get("ind-nome"));
                /* === Descrizione === */
                String descr = null;
                if (!params.get("ind-descr").equals(Utils.VOID_STRING)) {
                    descr = new String(params.get("ind-descr"));
                    pst.setString(++nextParam, descr);
                } else {
                    // dato facoltativo non inserito
                    pst.setNull(++nextParam, Types.NULL);
                }
                /* === Baseline === */
                pst.setString(++nextParam, params.get("ind-baseline"));
                /* === Controllo lato server sull'input delle date previste === */
                java.util.Date dataBaseline = null;
                java.sql.Date dataBaselineSQL = null;
                if (params.get("ind-database") != null) {
                    dataBaseline = Utils.format(params.get("ind-database"), "dd/MM/yyyy", Query.DATA_SQL_PATTERN);
                    dataBaselineSQL = Utils.convert(dataBaseline);
                }
                java.util.Date dataTarget = Utils.format(params.get("ind-datatarget"), "dd/MM/yyyy", Query.DATA_SQL_PATTERN);
                // Data fine non deve essere minore di Data inizio
                if (dataBaseline != null && dataBaseline.compareTo(dataTarget) > NOTHING) {
                    throw new WebStorageException("La data di target e\' minore di quella di baseline.\n");
                }
                // Data Baseline
                pst.setDate(++nextParam, dataBaselineSQL);
                // Anno Baseline
                String ab = null;
                if (!params.get("ind-annobase").equals(Utils.VOID_STRING)) {
                    ab = new String(params.get("ind-annobase"));
                    pst.setString(++nextParam, ab);
                } else {
                    // dato facoltativo non inserito
                    pst.setNull(++nextParam, Types.NULL);
                }
                /* === Target === */
                pst.setString(++nextParam, params.get("ind-target"));
                // Data Target
                pst.setDate(++nextParam, Utils.convert(dataTarget)); // non accetta una data gg/mm/aaaa, ma java.sql.Date
                // Anno Target
                String at = null;
                if (!params.get("ind-annotarget").equals(Utils.VOID_STRING)) {
                    at = new String(params.get("ind-annotarget"));
                    pst.setString(++nextParam, at);
                } else {
                    // dato facoltativo non inserito
                    pst.setNull(++nextParam, Types.NULL);
                }
                /* === Campi automatici: id utente, ora ultima modifica, data ultima modifica === */
                pst.setDate(++nextParam, Utils.convert(Utils.convert(Utils.getCurrentDate()))); // non accetta un GregorianCalendar né una data java.util.Date, ma java.sql.Date
                pst.setTime(++nextParam, Utils.getCurrentTime());   // non accetta una Stringa, ma un oggetto java.sql.Time
                pst.setString(++nextParam, user.getCognome() + String.valueOf(Utils.BLANK_SPACE) + user.getNome());
                /* === Id tipo indicatore === */
                pst.setInt(++nextParam, Integer.parseInt(params.get("ind-tipo")));
                /* === Riferimento a WBS === */
                if (!params.get("ind-wbs").equals(Utils.VOID_STRING)) {
                    int idWbs = Integer.parseInt(params.get("ind-wbs"));
                    pst.setInt(++nextParam, idWbs);
                } else {
                    // dato facoltativo non inserito
                    pst.setNull(++nextParam, Types.NULL);
                }
                /* === Riferimento a progetto === */
                pst.setInt(++nextParam, idProj);
                // End: <==
                pst.executeUpdate();
                con.commit();
            } catch (CommandException ce) {
                String msg = FOR_NAME + "Si e\' verificato un problema nella conversione di date.\n" + ce.getMessage();
                LOG.severe(msg);
                throw new WebStorageException(msg, ce);
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
            String msg = FOR_NAME + "Oggetto Bean non valorizzato; problema nel codice SQL.\n";
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
     * <p>Metodo per fare l'inserimento di una nuova misurazione di indicatore.</p>
     * 
     * @param idProj    identificativo del progetto, al contesto del quale l'indicatore fa riferimento
     * @param user      utente loggato
     * @param projectsWritableByUser    Vector contenente tutti i progetti scrivibili dall'utente; l'indicatore deve essere agganciata ad uno di questi
     * @param params    tabella contenente tutti i valori che l'utente inserisce per il nuovo indicatore;
     * @throws WebStorageException se si verifica un problema nel cast da String a Date, nell'esecuzione della query, nell'accesso al db o in qualche puntamento
     */
    @SuppressWarnings({ "null" })
    public void insertMeasurement(int idProj,
                                PersonBean user, 
                                Vector<ProjectBean> projectsWritableByUser,
                                HashMap<String, String> params) 
                         throws WebStorageException {
        Connection con = null;
        PreparedStatement pst = null;
        try {
            // Effettua la connessione
            con = pol_manager.getConnection();
            /* ===  Controlla anzitutto che l'id progetto sulla querystring === *
             * ===  corrisponda all'id progetto nel campo hidden della form === */
            int idProjForm = Integer.parseInt(params.get("prj-id"));
            if (idProj != idProjForm) {
                String msg = FOR_NAME + "Attenzione: l\'identificativo di progetto sulla querystring non corrisponde a quello passato dalla form!\n";
                LOG.severe(msg + "Problema durante il tentativo di inserire una nuova misurazione.\n");
                throw new WebStorageException(msg);
            }
            /* ===  Controlla quindi che l'id progetto sulla querystring    === *
             * ===  corrisponda a un id dei progetti scrivibili dall'utente === */
            try {
                if (!userCanWrite(idProj, projectsWritableByUser)) {
                    String msg = FOR_NAME + "Attenzione: l'utente ha tentato di inserire una misurazione legata ad un progetto su cui non ha i diritti di scrittura!\n";
                    LOG.severe(msg + "Problema durante il tentativo di inserire una nuova misurazione.\n");
                    throw new WebStorageException(msg);
                }
            } catch (WebStorageException wse) {
                String msg = FOR_NAME + "Problema nel tentativo di capire se l\'utente ha o non ha i diritti di scrittura!\n";
                LOG.severe(msg + "Problema durante il tentativo di inserire una nuova misurazione.\n");
                throw new WebStorageException(msg);
            }
            /* === Se siamo qui vuol dire che l'id del progetto su cui si   === * 
             * === deve aggiungere un'attivita' e' scrivibile dall'utente   === */
            // Deve fare un inserimento
            // Begin: ==>
            con.setAutoCommit(false);
            pst = con.prepareStatement(INSERT_MEASUREMENT);
            pst.clearParameters();
             // Prepara i parametri per l'inserimento
            try {
                // Definisce un contatore per l'indice del parametro
                int nextParam = 0;
                // Ottiene l'id
                int maxActId = getMax("indicatoregestione") + 1;
                /* === Id === */
                pst.setInt(++nextParam, maxActId);
                /* === Valore === */
                pst.setString(++nextParam, params.get("mon-nome"));
                /* === Note === */
                String descr = null;
                if (!params.get("mon-descr").equals(Utils.VOID_STRING)) {
                    descr = new String(params.get("mon-descr"));
                    pst.setString(++nextParam, descr);
                } else {
                    // dato facoltativo non inserito
                    pst.setNull(++nextParam, Types.NULL);
                }
                /* === Ignora il dato passato dalla form e imposta la data corrente  === */
                // Data Corrente inserita automaticamente, tanto la misurazione è sempre in data corrente
                pst.setDate(++nextParam, Utils.convert(Utils.convert(Utils.getCurrentDate())));
                // Flag di ultima misurazione
                // Definisce un valore boolean in funzione del checkbox
                boolean last = params.get("mon-milestone").equals("on") ? true : false;
                // Chiude l'indicatore misurato se la misurazione è l'ultima
                if (last) {
                    // Set the Indicator in an inactive state
                    updateIndicatorState(idProj, Integer.parseInt(params.get("ind-id")), Query.STATI_PROGETTO_AS_LIST.get(3));
                }
                pst.setBoolean(++nextParam, last);
                /* === Campi automatici: id utente, ora ultima modifica, data ultima modifica === */
                pst.setDate(++nextParam, Utils.convert(Utils.convert(Utils.getCurrentDate()))); // non accetta un GregorianCalendar né una data java.util.Date, ma java.sql.Date
                pst.setTime(++nextParam, Utils.getCurrentTime());   // non accetta una Stringa, ma un oggetto java.sql.Time
                pst.setString(++nextParam, user.getCognome() + String.valueOf(Utils.BLANK_SPACE) + user.getNome());
                /* === Id indicatore misurato === */
                pst.setInt(++nextParam, Integer.parseInt(params.get("ind-id")));
                /* === Riferimento a WBS === */
                // Per il momento trattiamo qusto dato come dato facoltativo non inserito
                // TODO :: Dovrebbe essere un campo calcolato (fare subquery TODO)
                pst.setInt(++nextParam, Integer.parseInt(params.get("ind-id")));
                /* === Riferimento a progetto === */
                pst.setInt(++nextParam, idProj);
                // End: <==
                pst.executeUpdate();
                con.commit();
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
            String msg = FOR_NAME + "Oggetto Bean non valorizzato; problema nel codice SQL.\n";
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
     * <p>Imposta ulteriori informazioni relative ad un indicatore 
     * il cui identificativo viene desunto dal valore di un parametro 
     * di navigazione passato come argomento.</p>
     * <p>Queste ulteriori informazioni vengono inserite a posteriori
     * dall'utente e sostanzialmente consistono in un ridimensionamento
     * del target dell'indicatore.</p>
     * 
     * @param idProj    identificativo del progetto nell'ambito del quale operare 
     * @param idInd     identificativo dell'indicatore a corredo del quale si vuole inserire un valore ulteriore del target
     * @param user      persona corrispondente all'utente loggato
     * @param projectsWritableByUser lista di progetti scrivibili dall'utente
     * @param params    lista di parametri recuperati dalla form inviata in POST
     * @throws WebStorageException se si verifica un problema in sql, nel recupero di valori, nella conversione di tipi o in qualche puntamento
     */
    @SuppressWarnings({ "null" })
    public void insertFurtherInformation(int idProj,
                                         int idInd,
                                         PersonBean user,
                                         Vector<ProjectBean> projectsWritableByUser,
                                         HashMap<String, String>params) 
                        throws WebStorageException {
        Connection con = null;
        PreparedStatement pst = null;
        try {
            // Ottiene la connessione
            con = pol_manager.getConnection();
            /* ===  Controlla anzitutto che l'id progetto sulla querystring === *
             * ===  corrisponda all'id progetto nel campo hidden della form === */
            int idProjForm = Integer.parseInt(params.get("prj-id"));
            if (idProj != idProjForm) {
                String msg = FOR_NAME + "Attenzione: l\'identificativo di progetto sulla querystring non corrisponde a quello passato dalla form!\n";
                LOG.severe(msg + "Problema durante il tentativo di inserire informazioni aggiuntive all\'indicatore.\n");
                throw new WebStorageException(msg);
            }
            /* ===  Controlla quindi che l'id progetto sulla querystring    === *
             * ===  corrisponda a un id dei progetti scrivibili dall'utente === */
            try {
                if (!userCanWrite(idProj, projectsWritableByUser)) {
                    String msg = FOR_NAME + "Attenzione: l'utente ha tentato di inserire una misurazione legata ad un progetto su cui non ha i diritti di scrittura!\n";
                    LOG.severe(msg + "Problema durante il tentativo di inserire informazioni aggiuntive all\'indicatore.\n");
                    throw new WebStorageException(msg);
                }
            } catch (WebStorageException wse) {
                String msg = FOR_NAME + "Problema nel tentativo di capire se l\'utente ha o non ha i diritti di scrittura!\n";
                LOG.severe(msg + "Problema durante il tentativo di inserire informazioni aggiuntive all\'indicatore.\n");
                throw new WebStorageException(msg);
            }
            /* ===  Controlla poi che l'id indicatore sulla querystring === *
             * ===  corrisponda all'id progetto nel campo hidden della form === */
            int idIndForm = Integer.parseInt(params.get("ind-id"));
            if (idInd != idIndForm) {
                String msg = FOR_NAME + "Attenzione: l\'identificativo di indicatore sulla querystring non corrisponde a quello passato dalla form!\n";
                LOG.severe(msg + "Problema durante il tentativo di inserire un nuovo target.\n");
                throw new WebStorageException(msg);
            }
            /* === Se siamo qui vuol dire che sono stati superati tutti i controlli   === */ 
            // Deve fare un inserimento
            // Begin: ==>
            con.setAutoCommit(false);
            pst = con.prepareStatement(INSERT_INDICATOR_EXTRAINFO);
            pst.clearParameters();
             // Prepara i parametri per l'inserimento
            try {
                // Definisce un contatore per l'indice del parametro
                int nextParam = 0;
                // Ottiene l'id
                int maxExtId = getMax("indicatoreaggiornamento") + 1;
                /* === Id === */
                pst.setInt(++nextParam, maxExtId);
                /* === Id indicatore === */
                pst.setInt(++nextParam, Integer.parseInt(params.get("ind-id")));
                /* === Id Progetto === */
                pst.setInt(++nextParam, Integer.parseInt(params.get("prj-id")));
                /* === Motivazione === */
                pst.setString(++nextParam, params.get("ext-note"));
                /* === Target === */
                pst.setString(++nextParam, params.get("ext-target"));
                /* === Data Target === */
                java.util.Date dataTarget = Utils.format(params.get("ext-datatarget"), "dd/MM/yyyy", Query.DATA_SQL_PATTERN);
                pst.setDate(++nextParam, Utils.convert(dataTarget)); // non accetta una data gg/mm/aaaa, ma java.sql.Date
                /* === Anno Target === */
                String at = null;
                if (!params.get("ext-annotarget").equals(Utils.VOID_STRING)) {
                    at = new String(params.get("ext-annotarget"));
                    pst.setString(++nextParam, at);
                } else {
                    // dato facoltativo non inserito
                    pst.setNull(++nextParam, Types.NULL);
                }
                /* === Data Inserimento === */
                // === Ignora il dato passato dalla form e imposta la data corrente  === 
                // Data Corrente inserita automaticamente, tanto l'aggiornamento target è sempre in data corrente
                // Inoltre è più sicuro forzare la data corrente anziché basarsi sul form, che potrebbe essere compromesso
                pst.setDate(++nextParam, Utils.convert(Utils.convert(Utils.getCurrentDate())));
                /* === Campi automatici: id utente, ora ultima modifica, data ultima modifica === */
                pst.setDate(++nextParam, Utils.convert(Utils.convert(Utils.getCurrentDate()))); // non accetta un GregorianCalendar né una data java.util.Date, ma java.sql.Date
                pst.setTime(++nextParam, Utils.getCurrentTime());   // non accetta una Stringa, ma un oggetto java.sql.Time
                pst.setString(++nextParam, user.getCognome() + String.valueOf(Utils.BLANK_SPACE) + user.getNome());
                // End: <==
                pst.executeUpdate();
                con.commit();
            } catch (CommandException ce) {
                String msg = FOR_NAME + "Si e\' verificato un problema nella conversione di date.\n" + ce.getMessage();
                LOG.severe(msg);
                throw new WebStorageException(msg, ce);
            } catch (AttributoNonValorizzatoException anve) {
                String msg = FOR_NAME + "Probabile problema nel recupero dei dati dell\'autore ultima modifica.\n";
                LOG.severe(msg); 
                throw new WebStorageException(msg + anve.getMessage(), anve);
            } catch (SQLException sqle) {
                String msg = FOR_NAME + "Tupla non aggiornata correttamente; problema nella query che inserisce il nuovo target.\n";
                LOG.severe(msg); 
                throw new WebStorageException(msg + sqle.getMessage(), sqle);
            } catch (NumberFormatException nfe) {
                String msg = FOR_NAME + "Tupla non aggiornata correttamente; problema nella query che inserisce il nuovo target.\n";
                LOG.severe(msg); 
                throw new WebStorageException(msg + nfe.getMessage(), nfe);
            } catch (NullPointerException npe) {
                String msg = FOR_NAME + "Tupla non aggiornata correttamente; problema nella query che inserisce il nuovo target.\n";
                LOG.severe(msg); 
                throw new WebStorageException(msg + npe.getMessage(), npe);
            }
        } catch (SQLException sqle) {
            String msg = FOR_NAME + "Oggetto Bean non valorizzato; problema nel codice SQL.\n";
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
     * <p>Effettua l'inserimento di un nuovo progetto phantom per struttura di 
     * dato id, restituendo l'identificativo di tale progetto, appena creato.</p>
     * 
     * @param user      utente loggato
     * @param idDepart  identificativo della struttura relativamente alla quale deve essere creato il progetto phantom
     * @return <code>int</code> - identificativo del progetto phantom appena inserito 
     * @throws WebStorageException se si verifica un problema nel cast da String a Date, nell'esecuzione della query, nell'accesso al db o in qualche puntamento
     */
    @SuppressWarnings({ "null" })
    public int insertPhantom(PersonBean user,
                             int idDepart) 
                      throws WebStorageException {
        Connection con = null;
        PreparedStatement pst = null;
        try {
            // Effettua la connessione
            con = pol_manager.getConnection();
            // Deve fare un inserimento
            // Begin: ==>
            con.setAutoCommit(false);
            pst = con.prepareStatement(INSERT_PHANTOM_PROJECT);
            pst.clearParameters();
             // Prepara i parametri per l'inserimento
            try {
                // Definisce un contatore per l'indice del parametro
                int nextParam = 0;
                // Ottiene l'id
                int minProjId = getMin("progetto") - 1;
                /* === Id === */
                pst.setInt(++nextParam, minProjId);
                /* === Titolo === */
                pst.setInt(++nextParam, idDepart);
                /* === Data inizio === */
                // Data Corrente inserita automaticamente, tanto la misurazione è sempre in data corrente
                pst.setDate(++nextParam, Utils.convert(Utils.convert(Utils.getCurrentDate())));
                /* === Data fine === */
                pst.setDate(++nextParam, Utils.convert(Utils.convert(Utils.getCurrentDateNextYear())));
                /* === Costruzione mese riferimento === */
                GregorianCalendar calendar = new GregorianCalendar();
                int day = calendar.get(Calendar.DATE);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);
                GregorianCalendar dateConverted = new GregorianCalendar(year, month + 3, day);
                pst.setDate(++nextParam, Utils.convert(Utils.convert(dateConverted)));
                /* === Campi automatici: id utente, ora ultima modifica, data ultima modifica === */
                pst.setDate(++nextParam, Utils.convert(Utils.convert(Utils.getCurrentDate()))); // non accetta un GregorianCalendar né una data java.util.Date, ma java.sql.Date
                pst.setTime(++nextParam, Utils.getCurrentTime());   // non accetta una Stringa, ma un oggetto java.sql.Time
                pst.setString(++nextParam, user.getCognome() + String.valueOf(Utils.BLANK_SPACE) + user.getNome());
                /* === Id struttura === */
                pst.setInt(++nextParam, idDepart);
                /* === Id stato progetto === */
                pst.setInt(++nextParam, 1);
                /* === Id periodo riferimento === */
                pst.setInt(++nextParam, 2);
                // End: <==
                pst.executeUpdate();
                con.commit();
                return minProjId;
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
            String msg = FOR_NAME + "Oggetto Bean non valorizzato; problema nel codice SQL.\n";
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
     * <p>Inserisce le informazioni relative ad un file, i cui 
     * estremi vengono comunicati in un dictionary passato come argomento.</p>
     * <p>Questo inserimento, una volta andato a buon fine, rappresenter&agrave;
     * una rappresentazione logica, nel database, di un allegato fisico presente
     * nel file system (del server).</p>
     * <p>Notare che non esiste alcuna garanzia che la rappresentazione logica
     * dell'allegato corrisponda effettivamente all'allegato fisico, nel senso
     * che, una volta terminato il caricamento, un utente con accessi alla 
     * directory degli upload potrebbe eliminare l'allegato fisico, mentre
     * l'allegato logico, ovvero la tupla inserita dal presente metodo, potrebbe
     * restare. Ci&ograve; porterebbe, a valle, alla generazione di un link che
     * produce un errore di tipo 404.</p> 
     * <p><small>Per evitare questo inconveniente,
     * &egrave; possibile prevedere meccanismi di protezione delle directories
     * e/o anche meccanismi di trigger che si riverberano sul db &ndash;
     * la cui implementazione comunque non attiene alla natura applicativa
     * dei sorgenti del presente software.</small></p>
     * 
     * @param params HashMap contenente gli estremi del file, al fine di inserire la tupla
     * @return identificativo, calcolato, per la tupla che rappresentera' logicamente l'allegato fisico
     * @throws WebStorageException se si verifica un problema SQL, nel recupero di attributi di bean, o in qualche altro puntamento
     */
    @SuppressWarnings("null")
    public int setFileDoc(HashMap<String, Object> params)
                   throws WebStorageException {
        Connection con = null;
        PreparedStatement pst = null;
        String table = params.get("nomeEntita") + "_" + params.get("nomeAttributo");
        assert(!table.equals("_"));
        int nextId = Utils.DEFAULT_ID;
        try {
            // Campi passati da fuori
            if (params.containsKey("id")) {
                nextId = (Integer) params.get("id");
            } else {
                nextId = getMax(table) + 1;
            }
            String file = (String) params.get("file");
            String ext = (String) params.get("ext");
            String fileName = (String) params.get("nome");
            int idBelongs = (Integer) params.get("belongs");
            String title = (String) params.get("titolo");
            long size = (Long) params.get("dimensione");
            String mime = (String) params.get("mime");
            PersonBean user = (PersonBean) params.get("usr");
            // Campi calcolati al momento
            String usr = user.getCognome() + String.valueOf(Utils.BLANK_SPACE) + user.getNome();
            java.sql.Date today = Utils.convert(Utils.convert(Utils.getCurrentDate()));
            Time now = Utils.getCurrentTime();
            // BEGIN
            con = pol_manager.getConnection();
            con.setAutoCommit(false); 
            String query =
                    "INSERT INTO " + table +
                    "   (   id" +
                    "   ,   file" +
                    "   ,   estensione" +
                    "   ,   original" +
                    "   ,   id_belongs_" + params.get("nomeEntita") +
                    "   ,   titolo" +
                    "   ,   data" +
                    "   ,   dimensione" +
                    "   ,   mime" +
                    "   ,   dataultimamodifica" +
                    "   ,   oraultimamodifica" +
                    "   ,   autoreultimamodifica)" +
                    "   VALUES (" + String.valueOf(nextId) +// id
                    "   ,      '" + file + "'"  +           // file
                    "   ,      '" + ext  + "'"  +           // file extension
                    "   ,      '" + fileName + "'"  +       // original name
                    "   ,       " + idBelongs   +           // id belongs
                    "   ,      '" + title + "'" +           // titolo
                    "   ,      '" + today + "'" +           // CURRENT_DATE
                    "   ,       " + size        +           // dimensione
                    "   ,      '" + mime + "'"  +           // MIME
                    "   ,      '" + today + "'" +           // oggi in SQL date
                    "   ,      '" + now + "'"   +           // ora in SQL time
                    "   ,      '" + usr + "'"   +           // usr full name
                    ")" ;       
            pst = con.prepareStatement(query);
            pst.executeUpdate();
            // END
            con.commit();
            return nextId;
        } catch (SQLException sqle) {
            String msg = FOR_NAME + "Problema nella query di inserimento allegato.\n";
            LOG.severe(msg); 
            throw new WebStorageException(msg + sqle.getMessage(), sqle);
        } catch (AttributoNonValorizzatoException anve) {
            String msg = FOR_NAME + "Problema nel recupero di attributi del bean.\n";
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
    
    
    /* ********************************************************** *
     *                        Metodi di POL                       *
    /* ********************************************************** *
     *                    Metodi  di  ELIMINAZIONE                *
     * ********************************************************** */
    /** 
     * <p>Metodo per fare una dereferenziazione di una wbs 
     * relativa al progetto.</p>
     * <p>A partire dall'identificativo del dipartimento, passato come 
     * argomento, identifica un progetto "fantasma" di dipartimento
     * (avente come identificativo lo stesso identificativo del dipartimento
     * moltiplicato per -1), e vi associa la wbs che si vuol eliminare.
     * In questo modo la cancellazione della wbs non sar&agrave; fisica,
     * ma sar&agrave; logica, in quanto consister&agrave; di una 
     * de-referenziazione della wbs stessa dal progetto corrente.</p> 
     * 
     * @param user      identificativo dell'utente che ha eliminato la WBS
     * @param idWbs     identificativo della WBS da dereferenziare
     * @param idDipart  identificativo del dipartimento al quale appartiene la WBS da eliminare
     * @throws WebStorageException se si verifica un problema nell'esecuzione della query, nell'accesso al db o in qualche tipo di puntamento
     */
    @SuppressWarnings({ "static-method", "null" })
    public void deleteWbs(PersonBean user,
                          int idDipart, 
                          int idWbs)
                   throws WebStorageException {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        int nextParam = 0;
        int idProj = 0;
        try {
            con = pol_manager.getConnection();
            pst = con.prepareStatement(GET_PHANTOM_PROJECT_BY_DEPART);
            pst.clearParameters();
            pst.setInt(++nextParam, idDipart);
            rs = pst.executeQuery();
            if (rs.next()) {
                idProj = rs.getInt("id");
            }
            con.setAutoCommit(false);
            pst = null;
            nextParam = 0;
            pst = con.prepareStatement(DELETE_WBS);
            pst.clearParameters();
            pst.setInt(++nextParam, idProj);
            pst.setDate(++nextParam, Utils.convert(Utils.convert(Utils.getCurrentDate()))); // non accetta un GregorianCalendar né una data java.util.Date, ma java.sql.Date
            pst.setTime(++nextParam, Utils.getCurrentTime());   // non accetta una Stringa, ma un oggetto java.sql.Time
            pst.setString(++nextParam, user.getCognome() + String.valueOf(Utils.BLANK_SPACE) + user.getNome());
            pst.setInt(++nextParam, idWbs);
            pst.executeUpdate();
            con.commit();
        } catch (AttributoNonValorizzatoException anve) {
            String msg = FOR_NAME + "Attributi di PersonBean o CodeBean non valorizzati; problema nella query di eliminazione wbs\'.\n";
            LOG.severe(msg); 
            throw new WebStorageException(msg + anve.getMessage(), anve);
        } catch (SQLException sqle) {
            String msg = FOR_NAME + "Oggetto idProj non valorizzato; problema nella query di eliminazione wbs.\n";
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
     * <p>Effettua una cancellazione logica di una attivit&agrave; 
     * relativa a un progetto.</p>
     * <p>In un primo momento, nell'ottica di evitare la cancellazione fisica,
     * si era pensato infatti di dereferenziare un'attivit&agrave; 
     * collegandola ad un progetto fantasma; quindi, a partire 
     * dall'identificativo del dipartimento e da quello dell'attivit&agrave;, 
     * passati come argomenti, si pensava di identificare un progetto "fantasma" 
     * di dipartimento
     * (avente come identificativo lo stesso identificativo del dipartimento
     * moltiplicato per -1), una wbs parimenti "fantasma" 
     * ed associare ad entrambi l'attivit&agrave; che si vuol eliminare.
     * In questo modo la cancellazione dell'attivit&agrave; 
     * non sarebbe stata fisica, ma logica, in quanto avrebbe 
     * consistito di una de-referenziazione della wbs stessa 
     * dal progetto corrente.</p>
     * <p>Successivamente, si &egrave; deciso di gestire l'eliminazione 
     * logica in un modo diverso, ovvero valorizzando un valore di stato 
     * riservandolo per indicare un'eliminazione.
     * La prima, ora abbandonata, &egrave; la strada implementata 
     * dal presente metodo.</p>
     * 
     * @param user          utente loggato, per il tracciamento dell'operazione nel db
     * @param idActivity    identificativo dell'attivita' da eliminare logicamente
     * @param idDipart      identificativo del dipartimento al quale appartiene l'attivita' da eliminare
     * @throws WebStorageException se si verifica un problema nell'esecuzione della query, nell'accesso al db o in qualche tipo di puntamento
     */
    @SuppressWarnings({ "static-method", "null" })
    public void deleteActivity(PersonBean user,
                               int idDipart, 
                               int idActivity)
                   throws WebStorageException {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        CodeBean dipart = null;
        int nextParam = 0;
        int idProj = 0;
        try {
            con = pol_manager.getConnection();
            pst = con.prepareStatement(GET_PHANTOM_PROJECT_BY_DEPART);
            pst.clearParameters();
            pst.setInt(++nextParam, idDipart);
            rs = pst.executeQuery();
            if (rs.next()) {
                dipart = new CodeBean();
                BeanUtil.populate(dipart, rs);
                idProj = dipart.getId();
            }
            con.setAutoCommit(false);
            pst = null;
            nextParam = 0;
            pst = con.prepareStatement(DELETE_ACTIVITY);
            pst.clearParameters();
            pst.setInt(++nextParam, idProj);
            pst.setInt(++nextParam, idProj);
            pst.setDate(++nextParam, Utils.convert(Utils.convert(Utils.getCurrentDate()))); // non accetta un GregorianCalendar né una data java.util.Date, ma java.sql.Date
            pst.setTime(++nextParam, Utils.getCurrentTime());   // non accetta una Stringa, ma un oggetto java.sql.Time
            pst.setString(++nextParam, user.getCognome() + String.valueOf(Utils.BLANK_SPACE) + user.getNome());
            pst.executeUpdate();
            con.commit();
        } catch (AttributoNonValorizzatoException anve) {
            String msg = FOR_NAME + "Attributi di PersonBean o CodeBean non valorizzati; problema nella query di eliminazione attivita\'.\n";
            LOG.severe(msg); 
            throw new WebStorageException(msg + anve.getMessage(), anve);
        } catch (SQLException sqle) {
            String msg = FOR_NAME + "Problema nella query di eliminazione attivita\'.\n";
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
     *                  Metodi di utilità di POL                  *
     *                (Project management On Line)                *
     * ********************************************************** */
    /**
     * <p>Data in input una query e l'id di uno stato (avanzamento
     * di progetto), passati come argomenti, estrae i dati specificati
     * nella query e popola un CodeBean, che ritorna al chiamante.</p>
     * 
     * @param query     Query che deve essere eseguita dal metodo
     * @param idStatus  Id dello status del quale estrarre i dati
     * @return CodeBean - CodeBean contenente lo stato corrispondente alla query passata
     * @throws WebStorageException se si verifica un problema nell'esecuzione della query, nell'accesso al db o in qualche tipo di puntamento
     */
    @SuppressWarnings({ "static-method", "null" })
    private CodeBean retrieve(String query,
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
     * <p>Calcola una data di default dato in input l'id del progetto.</p> 
     * <p>Ritorna la data corrente sommata del periodo 
     * di avanzamento del progetto.</p>
     * 
     * @param idProj id del progetto dal quale ricavare il periodo di avanzamento
     * @return <code>java.util.Date</code> - un oggetto Date costruito partendo dalla data corrente e sommando il periodo di avanzamento del progetto
     * @throws WebStorageException se si verifica un problema nell'esecuzione della query, nell'accesso al db o in qualche tipo di puntamento
     */
    @SuppressWarnings({ "static-method", "null" })
    private Date getDefaultEndDate(int idProj) 
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

    
    /**
     * <p>Dato in input un identificativo di progetto e un identificativo 
     * di persona, restituisce <code>true</code> se la persona &egrave;
     * presente (a qualunque titolo) sul progetto, 
     * <code>false</code> altrimenti.</p>
     * 
     * @param idProj identificativo del progetto su cui si vuol verificare la presenza della persona
     * @param idUser identificativo della persona relativamente alla quale si vuol verificare la presenza sul progetto (di dato id)
     * @return <code>boolean</code> - valore true se la persona e' presente, in qualsivoglia ruolo, false altrimenti
     * @throws WebStorageException se si verifica un problema SQL o in qualche puntamento
     */
    @SuppressWarnings({ "static-method", "null" })
    private boolean userCanRead(int idProj,
                                int idUser) 
                         throws WebStorageException {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            con = pol_manager.getConnection();
            pst = con.prepareStatement(IS_PEOPLE_ON_PROJECT);
            pst.clearParameters();
            pst.setInt(1, idProj);
            pst.setInt(2, idUser);
            rs = pst.executeQuery();
            if (rs.next()) {
                return true;
            }
            return false;
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
    }
    
    
    /**
     * <p>Dato in input un identificativo di progetto e un Vector
     * di progetti scrivibili dall'utente, restituisce <code>true</code> 
     * se il progetto avente tale id &egrave; presente in lista progetti, 
     * <code>false</code> altrimenti.</p>
     * 
     * @param idProj identificativo del progetto di cui si vuol verificare la presenza tra quelli scrivibili dall'utente
     * @param projectsWritableByUser lista dei progetti scrivibili dall'utente
     * @return <code>boolean</code> - valore true se l'identificativo del progetto e' presente tra quelli scrivibili dall'utente, false altrimenti
     * @throws WebStorageException se si verifica un problema nel recupero di attributi o in qualche puntamento
     */
    @SuppressWarnings({ "static-method" })
    public boolean userCanWrite(int idProj,
                                Vector<ProjectBean> projectsWritableByUser) 
                         throws WebStorageException {
        try {
            /* ==       Controllo lato server sui diritti dell'utente.       == * 
             * == Controlla per prima cosa che l'id progetto sulla querystring== * 
             * == corrisponda a un id dei progetti scrivibili dall'utente    == */
            // Ottiene la cardinalità della lista dei progetti precaricati 
            // quando l'utente si &egrave; loggato, uno dei quali deve 
            // corrispondere al progetto sul quale aggiungere un'attività
            int lastIndexOf = projectsWritableByUser.size();
            int index = 0;
            // Controllo sull'input
            if (lastIndexOf == index) {
                // E' stata passata una lista di "progetti scrivibili" di cardinalità zero!
                String msg = FOR_NAME + "Si e\' tentato di utilizzare una lista di progetti scrivibili dall\'utente che non ha alcun progetto.\n";
                LOG.severe(msg + "E\' stata passata una lista di \"progetti scrivibili\" di cardinalità zero!.\n");
                throw new WebStorageException(msg);
            }
            do {
                ProjectBean wp = projectsWritableByUser.elementAt(index);
                // Se un id dei progetti scrivibili è uguale all'id sulla querystring è tutto ok 
                if (wp.getId() == idProj) {
                    return true;
                }
                index++;
            } while (index < lastIndexOf);
            // In caso contrario, ovvero se abbiamo raggiunto la fine del Vector senza trovare l'id sulla querystring, sono guai...
            return false;
        } catch (AttributoNonValorizzatoException anve) {
            String msg = FOR_NAME + "Si e\' tentato di accedere a un attributo di un bean obbligatorio ma non valorizzato.\n" + anve.getMessage();
            LOG.severe(msg + "Probabile problema nel tentativo di recuperare l'id del bean di un progetto scrivibile dall\'utente.\n");
            throw new WebStorageException(msg, anve);
        } catch (ArrayIndexOutOfBoundsException aiobe) {
            String msg = FOR_NAME + "Si e\' verificato un problema nello scorrimento di liste.\n" + aiobe.getMessage();
            LOG.severe(msg);
            throw new WebStorageException(msg, aiobe);
        } catch (NullPointerException npe) {
            String msg = FOR_NAME + "Si e\' verificato un problema in un puntamento a null.\n" + npe.getMessage();
            LOG.severe(msg);
            throw new WebStorageException(msg, npe);
        } catch (Exception e) {
            String msg = FOR_NAME + "Si e\' verificato un problema.\n" + e.getMessage();
            LOG.severe(msg);
            throw new WebStorageException(msg, e);
        }
    }
    
    
    /**
     * <p>Dato in input un identificativo di dipartimento e un Vector
     * di dipartimenti scrivibili dall'utente, restituisce <code>true</code> 
     * se il dipartimento avente tale id &egrave; presente in lista dipartimenti
     * scrivibili, <code>false</code> altrimenti.</p>
     * 
     * @param idDip identificativo del dipartimento di cui si vuol verificare la presenza tra quelli scrivibili dall'utente
     * @param departmentWritableByUser lista dei dipartimenti su cui l'utente ha i diritti di scrittura (cioe' su cui e' almeno PMODip)
     * @return <code>boolean</code> - valore true se l'identificativo del dipartimento e' presente nella lista dei dipartimenti scrivibili (quelli su cui l'utente e' PMO), false altrimenti
     * @throws WebStorageException se si verifica un problema nello scorrimento di liste o in qualche altro tipo di puntamento
     */
    @SuppressWarnings({ "static-method" })
    public boolean userCanMonitor(int idDip,
                                  Vector<DepartmentBean> departmentWritableByUser) 
                           throws WebStorageException {
        try {
            // Ottiene la cardinalità della lista dei dipartimenti precaricati 
            // quando l'utente si &egrave; loggato, uno dei quali deve 
            // corrispondere al dipartimento sul quale aggiungere un monitoraggio
            int lastIndexOf = departmentWritableByUser.size();
            int index = 0;
            // Controllo sull'input
            if (lastIndexOf == index) {
                // E' stata passata una lista di "progetti scrivibili" di cardinalità zero!
                String msg = FOR_NAME + "Si e\' tentato di utilizzare una lista di progetti scrivibili dall\'utente che non ha alcun progetto.\n";
                LOG.severe(msg + "E\' stata passata una lista di \"progetti scrivibili\" di cardinalità zero!.\n");
                throw new WebStorageException(msg);
            }
            do {
                DepartmentBean dip = departmentWritableByUser.elementAt(index);
                // Se un id dei progetti scrivibili è uguale all'id sulla querystring è tutto ok 
                if (dip.getId() == idDip) {
                    return true;
                }
                index++;
            } while (index < lastIndexOf);
            // In caso contrario, ovvero se abbiamo raggiunto la fine del Vector senza trovare l'id sulla querystring, sono guai...
            return false;
        } catch (AttributoNonValorizzatoException anve) {
            String msg = FOR_NAME + "Si e\' tentato di accedere a un attributo di un bean obbligatorio ma non valorizzato.\n" + anve.getMessage();
            LOG.severe(msg + "Probabile problema nel tentativo di recuperare l'id del bean di un progetto scrivibile dall\'utente.\n");
            throw new WebStorageException(msg, anve);
        } catch (ArrayIndexOutOfBoundsException aiobe) {
            String msg = FOR_NAME + "Si e\' verificato un problema nello scorrimento di liste.\n" + aiobe.getMessage();
            LOG.severe(msg);
            throw new WebStorageException(msg, aiobe);
        } catch (NullPointerException npe) {
            String msg = FOR_NAME + "Si e\' verificato un problema in un puntamento a null.\n" + npe.getMessage();
            LOG.severe(msg);
            throw new WebStorageException(msg, npe);
        } catch (Exception e) {
            String msg = FOR_NAME + "Si e\' verificato un problema.\n" + e.getMessage();
            LOG.severe(msg);
            throw new WebStorageException(msg, e);
        }
    }
    
    
    /**
     * <p>Dato in input una attivit&agrave; passata come argomento,
     * ne calcola per lo stato e lo restituisce sotto forma di 
     * <code>CodeBean</code>.</p>
     * <p>Implementa un algoritmo che calcola tale stato e lo setta 
     * nell'argomento, valorizzandolo per riferimento.</p>
     * 
     * @param aws       activity without state
     * @param rightNow  today
     * @return <code>CodeBean</code> - oggetto rappresentante lo stato calcolato per l'attivita' passata come argomento
     * @throws WebStorageException se si verfica un problema nel recupero di qualche attributo obbligatorio o in qualche altro tipo di puntamento
     */
    public static CodeBean computeActivityState(ActivityBean aws,
                                                Date rightNow) 
                                         throws WebStorageException {
        // Oggetto per memorizzare lo stato dell'attività passata come argomento
        CodeBean stato = new CodeBean();
        try {
            /* **************************************************************** *
             *                Controlli sugli stati a monte                     *
             *                Se un'attivita' e' in:                            *
             *      - STATO SOSPESO                                             *
             *      - STATO ELIMINATO                                           *
             *  (stati impostati dall'utente che non devono essere modificati)  *
             *  tali stati non vanno alterati in base al calcolo delle date     *
             *  perche' sono di esplicita volonta' da parte dell'utente!        *
             * **************************************************************** */
            if (aws.getIdStato() == SOSPESA || aws.getIdStato() == ELIMINATA) {
                stato.setId(aws.getIdStato());
                String nome = (aws.getIdStato() == SOSPESA) ? "Attivit&agrave; sospesa" : "Attivit&agrave; eliminata";
                stato.setNome(nome);
                String informativa = (aws.getIdStato() == SOSPESA) ? SOSPESA_HELP : ELIMINATA_HELP;
                stato.setInformativa(informativa);
                aws.setStato(stato);
                return stato;
            }
            /* ************************************************************ *
             *    Posto che l'attivita' non e' ne' SOSPESA ne' ELIMINATA    *
             *    Effettua controlli sulle premesse (che siano consistenti) *
             *   data < rightNow -> passato  |  data = rightNow -> presente *
             *                     data > rightNow -> futuro                *
             * ************************************************************ */
            // La data di fine prevista non può mai essere nulla (è un dato obbligatorio)
            if (aws.getDataFine() == null) {
                String msg = FOR_NAME + "Incongruenza grave: un dato obbligatorio (la data di fine prevista) non e\' presente!\n";
                LOG.severe(msg + Utils.BLANK_SPACE + "Tentativo di hackeraggio?");
                throw new WebStorageException(msg);
            }
            // La data di inizio effettiva, se esiste, deve essere sempre nel passato perché l'utente non è un indovino (e quindi non può prevedere il futuro)
            if (aws.getDataInizioEffettiva() != null) {
                if (aws.getDataInizioEffettiva().after(rightNow)) {
                    String msg = FOR_NAME + "Incongruenza nelle date: la data di inizio effettiva non puo\' essere maggiore della data odierna.\n";
                    LOG.severe(msg);
                    throw new WebStorageException(msg);
                }
            }
            // La data di fine effettiva, se esiste, deve essere sempre nel passato perché l'utente non è un chiromante
            if (aws.getDataFineEffettiva() != null) {
                if (aws.getDataFineEffettiva().after(rightNow)) {
                    String msg = FOR_NAME + "Incongruenza nelle date: la data di fine effettiva non puo\' essere maggiore della data odierna.\n";
                    LOG.severe(msg);
                    throw new WebStorageException(msg);
                }
            }
            // La data di inizio prevista deve essere minore della data di fine prevista 
            if (aws.getDataInizio() != null) {
                if (aws.getDataInizio().after(aws.getDataFine())) {
                    String msg = FOR_NAME + "Incongruenza nelle date: la data di inizio prevista non puo\' essere maggiore della data di fine prevista.\n";
                    LOG.severe(msg);
                    throw new WebStorageException(msg);
                }
            }
            // La data di inizio effettiva deve essere minore della data di fine effettiva
            if (aws.getDataInizioEffettiva() != null && aws.getDataFineEffettiva() != null) {
                if (aws.getDataInizioEffettiva().after(aws.getDataFineEffettiva())) {
                    String msg = FOR_NAME + "Incongruenza nelle date: la data di inizio effettiva non puo\' essere maggiore della data di fine effettiva.\n";
                    LOG.severe(msg);
                    throw new CommandException(msg);
                }
            }
            /* ************************************************************ *
             *                     Controlli sugli stati                    *
             *  - STATO CHIUSO:                                             *
             *      criterio + importante: data fine effettiva <> NULL      *
             *  - STATO APERTO                                              *
             *      criterio + importante: data inizio effettiva NULL       *
             *  - STATO IN PROGRESS                                         *
             *      criterio + importante: data inizio effettiva <> NULL    *
             * ************************************************************ */
            // APERTO
            if (aws.getDataInizioEffettiva() == null && aws.getDataFineEffettiva() == null) {
                if (aws.getDataInizio() == null) {
                    if (aws.getDataFine().after(rightNow) || aws.getDataFine().equals(rightNow)) {
                        stato.setId(APERTA);
                        stato.setNome("Aperta regolare");
                        stato.setInformativa(APERTA_REGOLARE_HELP);
                    }
                    // Intercetta attività in stato "APERTO" ma non finite e in ritardo
                    else if (aws.getDataFine().before(rightNow)) {
                        stato.setId(APERTA_IN_RITARDO);
                        stato.setNome("In ritardo sulla chiusura");
                        stato.setInformativa(APERTA_IN_RITARDO_CHIUSURA_HELP);
                    }
                }
                else {  // Data inizio prevista <> NULL
                    if (aws.getDataInizio().after(rightNow) || aws.getDataInizio().equals(rightNow)) {
                        stato.setId(APERTA);
                        stato.setNome("Attivit&agrave; pianificata regolare");
                        stato.setInformativa(APERTA_REGOLARE_PIANIFICATA_HELP);
                    }
                    else { // aws.getDataInizio().before(rightNow)
                        stato.setId(APERTA_IN_RITARDO);
                        stato.setNome("In ritardo sull\'apertura");
                        stato.setInformativa(APERTA_IN_RITARDO_APERTURA_HELP);
                    }
                }
            }
            // IN PROGRESS
            else if (aws.getDataInizioEffettiva() != null && aws.getDataFineEffettiva() == null) {
                if (aws.getDataInizio() == null) {
                    if (aws.getDataFine().before(rightNow)) {
                        stato.setId(IN_PROGRESS_IN_RITARDO);
                        stato.setNome("In progress in ritardo sulla chiusura");
                        stato.setInformativa(IN_PROGRESS_CHIUSURA_IN_RITARDO_HELP);
                    }
                    else {  // Qui data di fine prevista è >= today
                        stato.setId(IN_PROGRESS);
                        stato.setNome("In progress regolare");
                        stato.setInformativa(IN_PROGRESS_REGOLARE_HELP);
                    }
                }
                else {  // Qui la data di inizio prevista <> NULL quindi possiamo controllare il rapporto tra questa e quella effettiva!
                    if (aws.getDataInizio().before(aws.getDataInizioEffettiva())) {
                        if (aws.getDataFine().before(rightNow)) {
                            stato.setId(IN_PROGRESS_IN_RITARDO);
                            stato.setNome("In progress in ritardo sull\'inizio previsto e sulla fine prevista");
                            stato.setInformativa(IN_PROGRESS_IN_RITARDO_HELP);
                        }
                        else {
                            stato.setId(IN_PROGRESS_INIZIO_IN_RITARDO);
                            stato.setNome("In progress in ritardo sull\'inizio");
                            stato.setInformativa(IN_PROGRESS_INIZIO_IN_RITARDO_HELP);
                        }
                    }
                    else if (aws.getDataInizio().after(aws.getDataInizioEffettiva())) {
                        stato.setId(IN_PROGRESS_IN_ANTICIPO);
                        stato.setNome("In progress");
                        stato.setInformativa(IN_PROGRESS_IN_ANTICIPO_HELP);
                    }
                    else { // la data prevista e effettiva sono uguali
                        if (aws.getDataFine().after(rightNow) || aws.getDataFine().equals(rightNow)) {
                            stato.setId(IN_PROGRESS);
                            stato.setNome("In progress in regola");
                            stato.setInformativa(IN_PROGRESS_REGOLARE_HELP);
                        }
                        else {  // Caso già gestito in caso di data inizio prevista NULL
                            stato.setId(IN_PROGRESS_IN_RITARDO);
                            stato.setNome("In progress in ritardo sulla chiusura");
                            stato.setInformativa(IN_PROGRESS_CHIUSURA_IN_RITARDO_HELP);
                        }
                    }
                }
            }
            // CHIUSO
            else if (aws.getDataFineEffettiva() != null) {  // Per poter essere considerata chiusa un'attività deve avere data fine effettiva <> null
                // In questo punto la data fine effettiva sarà sempre > today, per i controlli sulle premesse
                if (aws.getDataFineEffettiva().before(aws.getDataFine())) {
                    stato.setId(CHIUSA_IN_ANTICIPO);
                    stato.setNome("Chiusa in anticipo rispetto al previsto");
                    stato.setInformativa(CHIUSA_IN_ANTICIPO_HELP);
                } else if (aws.getDataFineEffettiva().after(aws.getDataFine())) {
                        stato.setId(CHIUSA_IN_RITARDO);
                        stato.setNome("Chiusa in ritardo rispetto al previsto");
                        stato.setInformativa(CHIUSA_IN_RITARDO_HELP);
                } else {    // Se non è in anticipo e non è in ritardo, è in orario
                        stato.setId(CHIUSA);
                        stato.setNome("Chiusa regolarmente");
                        stato.setInformativa(CHIUSA_REGOLARE_HELP);
                }
            }
            // STATO INCONSISTENTE
            else {
                stato.setId(STATO_INCONSISTENTE);
                stato.setNome("Attivit&agrave; in stato inconsistente");
                stato.setInformativa(STATO_INCONSISTENTE_HELP);
                LOG.severe(stato.getInformativa() + Utils.BLANK_SPACE + "Reason: " + 
                           "Data inizio prevista  = " + aws.getDataInizio() +
                           "Data fine prevista    = " + aws.getDataFine() +
                           "Data inizio effettiva = " + aws.getDataInizioEffettiva() +
                           "Data fine effettiva   = " + aws.getDataFineEffettiva()
                          );
            }
            aws.setIdStato(stato.getId());
            aws.setStato(stato);
        } catch (AttributoNonValorizzatoException anve) {
            String msg = FOR_NAME + "Si e\' verificato un problema nell\'accesso ad un attributo obbligatorio del bean, probabilmente  un id.\n";
            LOG.severe(msg);
            throw new WebStorageException(msg + anve.getMessage(), anve);
        } catch (NullPointerException npe) {
            String msg = FOR_NAME + "Si e\' verificato un problema di puntamento a null.\n";
            LOG.severe(msg);
            throw new WebStorageException(msg + npe.getMessage(), npe);
        } catch (Exception e) {
            String msg = FOR_NAME + "Si e\' verificato un problema.\n";
            LOG.severe(msg);
            throw new WebStorageException(msg + e.getMessage(), e);
        }
        return stato;
    }
    
    
    /**
     * <p>Date in input una serie di date (relative ad una attivit&agrave;), 
     * passate come argomento, calcola uno stato esatto relativo ad una
     * ipotetica attivit&agrave; a cui le date passate facciano riferimento
     * e lo restituisce sotto forma di <code>CodeBean</code>.</p>
     * <p>Implementa un algoritmo che calcola tale stato e lo setta 
     * nell'oggetto, restituito al chiamante.</p>
     * 
     * @param dataInizioPrevista    data in cui era previsto che iniziasse la lavorazione dell'attivita'
     * @param dataFinePrevista      data entro cui era previsto che terminasse la lavorazione dell'attivita'
     * @param dataInizioEffettiva   data in cui e' effettivamente iniziata la lavorazione dell'attivita'
     * @param dataFineEffettiva     data in cui e' effettivamente finita la lavorazione dell'attivita'
     * @param rightNow  today       data odierna, per il calcolo dello stato in cui l'attivita' si trova ad oggi
     * @return <code>CodeBean</code> - oggetto rappresentante lo stato calcolato per l'attivita' a cui le date ipoteticamente fanno riferimento
     * @throws WebStorageException se si verfica un problema nel recupero di qualche attributo obbligatorio o in qualche altro tipo di puntamento
     */
    public static CodeBean computeActivityState(Date dataInizioPrevista,
                                                Date dataFinePrevista,
                                                Date dataInizioEffettiva,
                                                Date dataFineEffettiva,
                                                Date rightNow) 
                                         throws WebStorageException {
        // Oggetto per memorizzare lo stato dell'attività passata come argomento
        CodeBean stato = new CodeBean();
        try {
            /* ************************************************************ *
             *        Controlli sulle premesse (che siano consistenti)      *
             *   data < rightNow -> passato  |  data = rightNow -> presente *
             *                     data > rightNow -> futuro                *
             * ************************************************************ */
            // La data di fine prevista non può mai essere nulla (è un dato obbligatorio)
            if (dataFinePrevista == null) {
                String msg = FOR_NAME + "Incongruenza grave: un dato obbligatorio (la data di fine prevista) non e\' presente!\n";
                LOG.severe(msg + Utils.BLANK_SPACE + "Tentativo di hackeraggio?");
                throw new WebStorageException(msg);
            }
            // La data di inizio effettiva, se esiste, deve essere sempre nel passato perché l'utente non è un indovino (e quindi non può prevedere il futuro)
            if (dataInizioEffettiva != null) {
                if (dataInizioEffettiva.after(rightNow)) {
                    String msg = FOR_NAME + "Incongruenza nelle date: la data di inizio effettiva non puo\' essere maggiore della data odierna.\n";
                    LOG.severe(msg);
                    throw new WebStorageException(msg);
                }
            }
            // La data di fine effettiva, se esiste, deve essere sempre nel passato perché l'utente non è un chiromante
            if (dataFineEffettiva != null) {
                if (dataFineEffettiva.after(rightNow)) {
                    String msg = FOR_NAME + "Incongruenza nelle date: la data di fine effettiva non puo\' essere maggiore della data odierna.\n";
                    LOG.severe(msg);
                    throw new WebStorageException(msg);
                }
            }
            // La data di inizio prevista deve essere minore della data di fine prevista 
            if (dataInizioPrevista != null) {
                if (dataInizioPrevista.after(dataFinePrevista)) {
                    String msg = FOR_NAME + "Incongruenza nelle date: la data di inizio prevista non puo\' essere maggiore della data di fine prevista.\n";
                    LOG.severe(msg);
                    throw new WebStorageException(msg);
                }
            }
            // La data di inizio effettiva deve essere minore della data di fine effettiva
            if (dataInizioEffettiva != null && dataFineEffettiva != null) {
                if (dataInizioEffettiva.after(dataFineEffettiva)) {
                    String msg = FOR_NAME + "Incongruenza nelle date: la data di inizio effettiva non puo\' essere maggiore della data di fine effettiva.\n";
                    LOG.severe(msg);
                    throw new WebStorageException(msg);
                }
            }
            /* ************************************************************ *
             *                     Controlli sugli stati                    *
             *  - STATO CHIUSO:                                             *
             *      criterio + importante: data fine effettiva <> NULL      *
             *  - STATO APERTO                                              *
             *      criterio + importante: data inizio effettiva NULL       *
             *  - STATO IN PROGRESS                                         *
             *      criterio + importante: data inizio effettiva <> NULL    *
             * ************************************************************ */
            // APERTO
            if (dataInizioEffettiva == null && dataFineEffettiva == null) {
                if (dataInizioPrevista == null) {
                    if (dataFinePrevista.after(rightNow) || dataFinePrevista.equals(rightNow)) {
                        stato.setId(APERTA);
                        stato.setNome("Aperta");
                        stato.setInformativa("Aperta regolare");
                    }
                    // Intercetta attività in stato "APERTO" ma non finite e in ritardo
                    else if (dataFinePrevista.before(rightNow)) {
                        stato.setId(APERTA_IN_RITARDO);
                        stato.setNome("Aperta e in ritardo");
                        stato.setInformativa("Attivit&agrave; in ritardo sulla chiusura");
                    }
                }
                else {  // Data inizio prevista <> NULL
                    if (dataInizioPrevista.after(rightNow) || dataInizioPrevista.equals(rightNow)) {
                        stato.setId(APERTA);
                        stato.setNome("Aperta");
                        stato.setInformativa("Attivit&agrave; pianificata regolare");
                    }
                    else { // dataInizioPrevista.before(rightNow)
                        stato.setId(APERTA_IN_RITARDO);
                        stato.setNome("Aperta in ritardo");
                        stato.setInformativa("Attivit&agrave; in ritardo sull\'apertura");
                    }
                }
            }
            // IN PROGRESS
            else if (dataInizioEffettiva != null && dataFineEffettiva == null) {
                // Caso: data inizio prevista NULL
                if (dataInizioPrevista == null) {
                    // Sottocaso: data fine prevista nel passato
                    if (dataFinePrevista.before(rightNow)) {
                        stato.setId(IN_PROGRESS_IN_RITARDO);
                        stato.setNome("In progress");
                        stato.setInformativa("In progress in ritardo sulla chiusura");
                    }
                    else {  // Qui data di fine prevista è >= today
                        stato.setId(IN_PROGRESS);
                        stato.setNome("In progress");
                        stato.setInformativa("In progress regolare");
                    }
                }
                else {  // Qui la data di inizio prevista <> NULL quindi possiamo controllare il rapporto tra questa e quella effettiva!
                    if (dataInizioPrevista.before(dataInizioEffettiva)) {
                        if (dataFinePrevista.before(rightNow)) {
                            stato.setId(IN_PROGRESS_IN_RITARDO);
                            stato.setNome("In progress in ritardo su tutta la linea");
                            stato.setInformativa("In progress in ritardo sull\'inizio previsto e sulla fine prevista");
                        }
                        else {  // Qui data di fine prevista è >= today
                            stato.setId(IN_PROGRESS_INIZIO_IN_RITARDO);
                            stato.setNome("In progress in ritardo");
                            stato.setInformativa("In progress in ritardo sull\'inizio");
                        }
                    }
                    else if (dataInizioPrevista.after(dataInizioEffettiva)) {
                        stato.setId(IN_PROGRESS_IN_ANTICIPO);
                        stato.setNome("In progress");
                        stato.setInformativa("In progress in anticipo sull\'inizio");
                    }
                    else { // la data prevista e effettiva sono uguali
                        if (dataFinePrevista.after(rightNow) || dataFinePrevista.equals(rightNow)) {
                            stato.setId(IN_PROGRESS);
                            stato.setNome("In progress");
                            stato.setInformativa("In progress in regola");
                        }
                        else {  // Caso già gestito in caso di data inizio prevista NULL
                            stato.setId(IN_PROGRESS_IN_RITARDO);
                            stato.setNome("In progress");
                            stato.setInformativa("In progress in ritardo sulla chiusura");
                        }
                    }
                }
            }
            // CHIUSO
            else if (dataFineEffettiva != null) {  // Per poter essere considerata chiusa un'attività deve avere data fine effettiva <> null
                // In questo punto la data fine effettiva sarà sempre > today, per i controlli sulle premesse
                if (dataFineEffettiva.before(dataFinePrevista)) {
                    stato.setId(CHIUSA_IN_ANTICIPO);
                    stato.setNome("Chiusa in anticipo");
                    stato.setInformativa("Chiusa in anticipo rispetto al previsto");
                } else if (dataFineEffettiva.after(dataFinePrevista)) {
                        stato.setId(CHIUSA_IN_RITARDO);
                        stato.setNome("Chiusa in ritardo");
                        stato.setInformativa("Chiusa in ritardo rispetto al previsto");
                } else {    // Se non è in anticipo e non è in ritardo, è in orario
                        stato.setId(CHIUSA);
                        stato.setNome("Chiusa");
                        stato.setInformativa("Chiusa regolarmente");
                }
            }
            // STATO INCONSISTENTE
            else {
                stato.setId(STATO_INCONSISTENTE);
                stato.setNome("Stato inconsistente");
                stato.setInformativa("Attivit&agrave; in stato inconsistente");
                LOG.severe(stato.getInformativa() + Utils.BLANK_SPACE + "Reason: " + 
                           "Data inizio prevista  = " + dataInizioPrevista +
                           "Data fine prevista    = " + dataFinePrevista +
                           "Data inizio effettiva = " + dataInizioEffettiva +
                           "Data fine effettiva   = " + dataFineEffettiva
                          );
            }
        } catch (AttributoNonValorizzatoException anve) {
            String msg = FOR_NAME + "Si e\' verificato un problema nell\'accesso ad un attributo obbligatorio del bean, probabilmente  un id.\n";
            LOG.severe(msg);
            throw new WebStorageException(msg + anve.getMessage(), anve);
        } catch (NullPointerException npe) {
            String msg = FOR_NAME + "Si e\' verificato un problema di puntamento a null.\n";
            LOG.severe(msg);
            throw new WebStorageException(msg + npe.getMessage(), npe);
        } catch (Exception e) {
            String msg = FOR_NAME + "Si e\' verificato un problema.\n";
            LOG.severe(msg);
            throw new WebStorageException(msg + e.getMessage(), e);
        }
        return stato;
    }
    
    
    /**
     * <p>Data in input una attivit&agrave; passata come argomento,
     * ne calcola la durata in base alle date dell'attivit&agrave; stessa.</p>
     * <p>In particolare:<br />
     * se ci sono le date effettive considera le date effettive, cio&egrave:
     * <p><code>durata = data fine effettiva - data inizio effettiva</code></p>
     * Se non ci sono le date effettive, cerca di ricavare la durata in base
     * a quello che sa, ovvero:
     * la fine prevista è obbligatoria e dunque dev'esserci;
     * se c'è la fine effettiva considera quella e non la fine prevista
     * se non c'è la fine effettiva considera la fine prevista.</p>
     * <hr />
     * <p>Dettaglio algoritmo:
     * <ul>
     * <li><tt>1.</tt> se ci sono entrambe (le date effettive) usa quelle, quindi: 
     *      <pre>d = fine effettiva - inizio effettivo</pre>
     *      <code>(durata effettiva: guEffettivi)</code></li>
     * <li><tt>2.</tt> se non ci sono entrambe cerca la fine effettiva
     * <ul>
     * <li><tt>2.1.</tt> se la fine effettiva c'&egrave; vuole dire che non c'&egrave; 
     *      l'inizio effettivo (altrimenti saremmo nel caso precedente) quindi:
     *      <ul>
     *      <li><tt>2.1.1.</tt> cerca l'inizio previsto: c'&egrave; l'inizio previsto
     *          <pre>d = fine effettiva - inizio previsto</pre>
     *          <code>(durata effettiva, gueffettivi)</code></li>
     *      <li><tt>2.1.2.</tt> non c'&egrave; l'inizio previsto: 
     *          <pre>d = non computabile</pre>
     *          <code>(gueffettivi)</code></li>
     *      </ul>
     * <li><tt>2.2.</tt> se la fine effettiva non c'&egrave;  cerco l'inizio effettivo. 
     * A quel punto:
     *      <ul>
     *      <li><tt>2.2.1.</tt> c'&egrave; l'inizio effettivo. dunque:
     *          <pre>d = fine prevista - inizio effettivo</pre>
     *          <code>(gueffettivi)</code></li>
     *      <li><tt>2.2.2.</tt> non c'&egrave; l'inizio effettivo:
     *          <ul>
     *          <li><tt>2.2.2.1.</tt>cerco l'inizio previsto. C'&egrave; l'inizio previsto:
     *              <pre>d = fine prevista - inizio previsto</pre>
     *              <code>(durata prevista, guprevisti)</code></li>
     *          <li><tt>2.2.2.2.</tt>non c'&egrave; l'inizio previsto:
     *              <pre>d = non computabile</pre>
     *              <code>(guprevisti)</code></li>
     *          </ul>
     *      </li>
     *      </ul>
     * </li>
     * </ul>
     * </ul></p>
     * <p>Implementa un algoritmo che calcola tale dato e lo setta 
     * nell'argomento, valorizzandolo per riferimento.</p>
     * 
     * @param awl       activity without last
     * @param rightNow  today (for now, unused)
     * @return <code>CodeBean</code> - oggetto rappresentante lo stato calcolato per l'attivita' passata come argomento
     * @throws WebStorageException se si verfica un problema nel recupero di qualche attributo obbligatorio o in qualche altro tipo di puntamento
     */
    public static ActivityBean computeDays(ActivityBean awl,
                                           Date rightNow) 
                                    throws WebStorageException {
        ActivityBean act = null;
        try {
            // 1. Cerca entrambe le date effettive
            if ( (awl.getDataInizioEffettiva() != null && awl.getDataInizioEffettiva().after(new Date(0)))
                 &&
                 (awl.getDataFineEffettiva() != null && awl.getDataFineEffettiva().after(new Date(0)))
               ) {
                long endDate = awl.getDataFineEffettiva().getTime();
                long startDate = awl.getDataInizioEffettiva().getTime();
                long duration = endDate - startDate;
                long days = duration / (1000 * 60 * 60 * 24);
                awl.setGuEffettivi((int) days); 
            } else {
                // Se non ci sono entrambe le date effettive verifica se c'è quella di fine effettiva
                if ((awl.getDataFineEffettiva() != null && awl.getDataFineEffettiva().after(new Date(0)))) {
                    // Se c'è, verifica se c'è quella di inizio previsto (quella di inizio effettivo non può esserci perché altrimenti non saremmo nell'else!)
                    if ((awl.getDataInizio() != null && awl.getDataInizio().after(new Date(0)))) {
                        long endDate = awl.getDataFineEffettiva().getTime();
                        long startDate = awl.getDataInizio().getTime();
                        long duration = endDate - startDate;
                        long days = duration / (1000 * 60 * 60 * 24);
                        awl.setGuEffettivi((int) days); 
                    }
                } else if ((awl.getDataInizioEffettiva() != null && awl.getDataInizioEffettiva().after(new Date(0)))) {
                    // Se non c'è la data di fine effettiva potrebbe esserci quella di inizio effettiva
                    long endDate = awl.getDataFine().getTime();
                    long startDate = awl.getDataInizioEffettiva().getTime();
                    long duration = endDate - startDate;
                    long days = duration / (1000 * 60 * 60 * 24);
                    awl.setGuEffettivi((int) days); 
                } else if ((awl.getDataInizio() != null && awl.getDataInizio().after(new Date(0)))) {
                    // Se non c'è neppure la data di inizio effettiva potrebbe esserci quella di inizio prevista
                    long endDate = awl.getDataFine().getTime();
                    long startDate = awl.getDataInizio().getTime();
                    long duration = endDate - startDate;
                    long days = duration / (1000 * 60 * 60 * 24);
                    // In questo caso però i giorni sono previsti, non effettivi, perché computati solo in base a date previste!
                    awl.setGuPrevisti((int) days); 
                }
            }
            act = new ActivityBean(awl);
        } catch (AttributoNonValorizzatoException anve) {
            String msg = FOR_NAME + "Si e\' verificato un problema nell\'accesso ad un attributo obbligatorio del bean, probabilmente una data.\n";
            LOG.severe(msg);
            throw new WebStorageException(msg + anve.getMessage(), anve);
        } catch (NullPointerException npe) {
            String msg = FOR_NAME + "Si e\' verificato un problema di puntamento a null.\n";
            LOG.severe(msg);
            throw new WebStorageException(msg + npe.getMessage(), npe);
        } catch (Exception e) {
            String msg = FOR_NAME + "Si e\' verificato un problema.\n";
            LOG.severe(msg);
            throw new WebStorageException(msg + e.getMessage(), e);
        }
        return act;
    }
    
}