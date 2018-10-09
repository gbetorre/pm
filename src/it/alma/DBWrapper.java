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
import it.alma.bean.ItemBean;
import it.alma.bean.PersonBean;
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
     * <p>Gestore del pool di connessioni al dbms.</p>
     */
    protected static DataSource manager = null;

    
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
        if (manager == null) {
            try {
                manager = (DataSource) ((Context) new InitialContext()).lookup("java:comp/env/jdbc/almalaurea");
                if (manager == null)
                    throw new WebStorageException(FOR_NAME + "La risorsa `jdbc/almalaurea' non e\' disponibile. Verificare configurazione e collegamenti.\n");
                JOptionPane.showMessageDialog(null, "Accesso al DB", "Inizializzazione Completata", 0, null);
            } catch (NamingException ne) {
                throw new WebStorageException(FOR_NAME + "Problema nel recuperare la risorsa jdbc/almalaurea per problemi di naming: " + ne.getMessage());
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
            con = manager.getConnection();
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
    public PersonBean getUser()
                       throws WebStorageException {
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement pst = null;
        PersonBean usr = null;
        try {
            con = manager.getConnection();
            pst = con.prepareStatement(GET_USR);
            pst.clearParameters();
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
            con = manager.getConnection();
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
    
    
    /* ************************************************************************ *
     *                    Codice Inutile, Obsoleto o Deprecato                  *
     *                              TODO: eliminare                             *
     * ************************************************************************ */
    
    /**
     *  metodo per effettuare qualsiasi query sul DB
     * @param query
     * @return
     * @throws ServletException 
     * @throws IOException 
     * @throws WebStorageException 
     */
    public static ResultSet get(String query) 
                         throws ServletException, IOException, WebStorageException {
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement pst = null;
        try {
            con = manager.getConnection();
            pst = con.prepareStatement(query);
            pst.clearParameters();
            rs = pst.executeQuery();
            //Statement stmt = con.createStatement();
            //ResultSet rs = stmt.executeQuery(query);
            //JOptionPane.showMessageDialog(null, "ok", "Messaggio: Successo", 0, null);
            //System.out.println("ok");
            return rs;
        } catch (SQLException sqle) {
            JOptionPane.showMessageDialog(null, "Interrogazione fallita: " + sqle.getMessage(), "Messaggio:", 0, null);
            throw new NotFoundException(FOR_NAME + "DBWrapper.get: " + sqle.getMessage());
            //closeConnection();
            //return null;
        } finally {
            try {
                if (con != null) con.close();
            } catch (SQLException sqle) {
                throw new WebStorageException(FOR_NAME + "DBWrapper.esisteOrarioInPD: " + sqle.getMessage());
            }
        }
    }
    
    
    public Vector<String> getVector(ResultSet rs) {
        Vector<String> result = new Vector<String>();
        try {
            for (int i = 0; i <= rs.getFetchSize(); i++) {
            String s = rs.getString(i);
            result.add(s);
            }
        } catch (Exception ignored) {
            ; //TODO GESTISCI
        }
        return result;
    }

}