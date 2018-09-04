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

import it.alma.bean.BeanUtil;
import it.alma.bean.CourseBean;
import it.alma.exception.NotFoundException;
import it.alma.exception.WebStorageException;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.sql.DataSource;
import javax.swing.JOptionPane;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;


/**
 * 
 * @author trrgnr59
 *
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
     * <p>Connessione ad Access 2016 via ODBC.</p>
     * <p>Il nome del DB cui accedere.</p>
     * <p>Prima di tutto bisogna impostare l'origine dati in windows tra le variabili ODBC.</p>
     * <tt>
     * Pannello di controllo -> strumenti di amministrazione -> origine dati (odbc) -> in DSN utente
     * fare aggiungere e in nome origine dati mettere il nome del DB, es. prova.mdb, poi cliccare su "seleziona" e inserire il path del DB.
     * </tt>
     * <p>Senza queste impostazioni non ci si può connettere al db, il programma non trova i driver e lo stesso DB.</p>
     * <small>Notare che nel caso di applicazioni web a 64bit, non sar&agrave;
     * possibile accedere ad Access 2016 via OCBC perché non esiste il driver
     * per questo DBMS a 64 bit ma solo a 32 bit!</small>
     */
    static final String URL = "jdbc:odbc:Alma2017";
    /**
     * <p>Connessione al database.</p>
     *
    Connection con;*/

    
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
        //openConnection();
    }
    
    
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
    
    
    /**
     * Restituisce un vettore di .
     * Data creazione 07/01/2016
     *
     * @param Lingue lingue
     * @return Vector DipartimentiBean
     * @throws WebStorageException
     */
    public Vector<?> getCourse()
                        throws WebStorageException {
      ResultSet rs = null;
      ResultSet rs2 = null;
      Connection con = null;
      PreparedStatement pst = null;
      CourseBean course = null;
      Vector lista = new Vector();
      int idDirettore = -2;
      int idSegreteria = -2;
      String nomeDirettore = null;
      String cognomeDirettore = null;
      //PersonBean direttore = null;
      String nomeSegreteria = null;
      //StrutturaServizioBean segreteria = null;
      try {
        con = manager.getConnection();
        pst = con.prepareStatement(AD_2016);
        pst.clearParameters();
        //pst.setString(1, lingue.getPrimaLingua());
        //pst.setString(2, lingue.getPrimaLingua());
        //pst.setString(3, lingue.getSecondaLingua());
        rs = pst.executeQuery();
        while (rs.next()) {
          course = new CourseBean();
          BeanUtil.populate(course, rs);
          lista.add(course);
        }
        return lista;
      } catch (SQLException e) {
        throw new WebStorageException(e.getMessage());
      } finally {
        try {
          con.close();
        } catch (SQLException e1) {
          throw new WebStorageException(e1.getMessage());
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
    
    
    //============================================================================
    /*
    public void update(String update){
        try{
            Statement stmt = con.createStatement();
            stmt.executeUpdate(update);
        } catch(SQLException e){
            JOptionPane.showMessageDialog(null,"Interrogazione fallita: "+e.getMessage(),"Messaggio:",0,null);
            closeConnection(new DBWrapper());
        }
    }
 
    
    /**
     * <p>Apre la connessione col DB via ODBC.</p>
     *
    public void openConnection() {
        try {
            Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
        } catch (ClassNotFoundException cnfe) {
            JOptionPane.showMessageDialog(null, "Driver per database non trovato: "+e.getMessage(),"Messaggio:",0,null);
        } 
        try {
            con = DriverManager.getConnection(url,"admin","pippo");//eventualmente user e password, se il DB è rpotetto da password, admin è di default
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,"Connessione Fallita: "+e.getMessage(),"Messaggio:",0,null);
        }
    }
    
    //chiude la connessione col DB
    public void closeConnection(){
        try{
            con.close();
            System.out.println("connessione chiusa");
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null,"Connessione Fallita: "+e.getMessage(),"Messaggio:",0,null);
        }
    }
    
    
     // @param db
    
    public void closeConnection(DBWrapper db) {
        db.closeConnection(db);
    }
     */
 
    /*
    public static void main(String[] args) {
        //esempio lettura
 
        //apro la connessione
        DBWrapper db = new DBWrapper();
 
        try{
            //eseguo la query ed ottengo un resultset
            ResultSet rslt = db.query("SELECT * FROM Utenti");
            //posso accedere ai metadati
            ResultSetMetaData rsmd=rslt.getMetaData();
 
            int i=rsmd.getColumnCount(); //trova il numero di colonne
 
            //leggo tutti i dati e li stampo a video
            while(rslt.next()){
                for(int j=1;j<=i;j++){
                    System.out.print(rslt.getString(j)+" -- ");
                }
                System.out.println("");
            }
 
            //chiudo il resultset
            rslt.close();
            //chiudo la connessione al DB
            db.closeConnection();
        }
        catch(Exception ez){
            ez.printStackTrace();
            System.err.println("errore della query");
        }
 
    }
    */
}