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
import it.alma.exception.NotFoundException;
import it.alma.exception.WebStorageException;


/**
 * 
 * @author trrgnr59
 * @version 2
 */
public class DBWrapper implements Query {
    
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
     */
    public DBWrapper() throws WebStorageException {
        if (manager == null) {
            try {
                JOptionPane.showMessageDialog(null, "ok", "Messaggio: 110", 0, null);
                manager = (DataSource) ((Context) new InitialContext()).lookup("java:comp/env/jdbc/almalaurea");
                if (manager == null) 
                    throw new WebStorageException(FOR_NAME + "La risorsa `jdbc/almalaurea' non è disponibile. Verificare configurazione e collegamenti.");
            } catch (NamingException ne) {
                throw new WebStorageException(FOR_NAME + "Problema nel recuperare la risorsa jdbc/almalaurea per problemi di naming: " + ne.getMessage());
            } catch (Exception e) {
                throw new WebStorageException(FOR_NAME + "Errore generico nel costruttore: " + e.getMessage());
            }
        }
    }
    
    
    /**
     * Restituisce un vettore di CourseBean.
     * Data creazione 07/01/2016
     *
     * @param Lingue lingue
     * @return Vector DipartimentiBean
     * @throws WebStorageException
     */
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
    
    
    /* **************************************************************************
     * =========================================================================
     */
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