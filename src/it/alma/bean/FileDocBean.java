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

package it.alma.bean;

import it.alma.exception.AttributoNonValorizzatoException;

import java.io.Serializable;
import java.sql.Date;

/**
 * <p>Classe usata per rappresentare un allegato (file).</p>
 * 
 * @author <a href="mailto:giovanroberto.torre@univr.it">Giovanroberto Torre</a>
 */
public class FileDocBean implements Serializable {
    
    /**
     * La serializzazione necessita di dichiarare una costante di tipo long
     * identificativa della versione seriale. 
     * (Se questo dato non fosse inserito, verrebbe calcolato in maniera automatica
     * dalla JVM, e questo potrebbe portare a errori riguardo alla serializzazione). 
     */
    private static final long serialVersionUID = 8405391234787025432L;
    /**
     *  Nome di questa classe. 
     *  Viene utilizzato per contestualizzare i messaggi di errore.
     */
    private final String FOR_NAME = "\n" + this.getClass().getName() + ": "; //$NON-NLS-1$
    /** Identificativo nel db della rappresentazione dell'allegato. */
    private int id;
    /** Nome del file allegato. */
    private String file;
    /** Autore del documento. */
    private String autore;
    /** Lingua del documento. */
    private String lingua;
    /** Data di caricamento. */
    private Date data;
    /** Pesantezza in byte. */
    private long dimensione;
    /** Titolo logico dell'allegato. */
    private String titolo;
    /** Tipo MIME dell'allegato. */
    private String mime;
    /** Tipologia di documento. */
    private int tipologia;
    /** Id dell'utente a cui il file set è associato. */
    private int idProprietario;
    /**
     * Indica se l'accesso al file è riservato. La riservatezza &egrave; 
     * garantita offrendo il link esclusivamente a classi particolari di utenti.
     */
    private boolean riservato;

    /**
     * <p>Costruttore: inizializza i campi a valori di default.</p>
     */
    public FileDocBean() {
        id = idProprietario = -2;
        dimensione = -2;
        tipologia = -2;
        file = autore = lingua = titolo = mime =  null;
        data = new Date(0);
        riservato = false;
    }

    
    /* **************************************************** *
     *           Metodi getter e setter per id              *
     * **************************************************** */
    /**
     * @return id - identificativo della rappresentazione database dell'allegato
     * @throws AttributoNonValorizzatoException se il dato non e' valorizzato
     */
    public int getId() throws AttributoNonValorizzatoException {
        if (id == -2) {
            throw new AttributoNonValorizzatoException(FOR_NAME + "Attributo id non valorizzato!");
        }
        return this.id;
    }
    
    /**
     * @param i identificativo della rappresentazione database dell'allegato
     */
    public void setId(int i) {
        id = i;
    }
    
    
    /* **************************************************** *
     *      Metodi getter e setter per id proprietario      *
     * **************************************************** */
    /**
     * @return idProprietario - identificativo dell'utente proprietario dell'allegato
     */
    public int getIdProprietario() {
        return idProprietario;
    }

    /**
     * @param idProprietario identificativo dell'utente proprietario dell'allegato da impostare
     */
    public void setIdProprietario(int idProprietario) {
        this.idProprietario = idProprietario;
    }
    

    /* **************************************************** *
     *        Metodi getter e setter per dimensione         *
     * **************************************************** */
    /**
     * @return dimensione - pesantezza in byte del file
     */
    public long getDimensione() {
        return dimensione;
    }
    
    /**
     * @param l dimensione in byte da impostare
     */
    public void setDimensione(long l) {
        dimensione = l;
    }
    
    
    /* **************************************************** *
     *         Metodi getter e setter per tipologia         *
     * **************************************************** */
    /**
     * @return tipologia - identificativo etichetta in base a cui e' possibile effettuare raggruppamenti di allegati
     */
    public int getTipologia() {
        return tipologia;
    }
    
    /**
     * @param l identificativo etichetta da impostare
     */
    public void setTipologia(int l) {
        tipologia = l;
    }
    
    
    /* **************************************************** *
     *           Metodi getter e setter per file            *
     * **************************************************** */
    /**
     * @return file - il nome del file caricato nel file system del server
     * @throws AttributoNonValorizzatoException nel caso in cui il nome del file non sia valorizzato
     */
    public String getFile() throws AttributoNonValorizzatoException {
        if (file == null) {
            throw new AttributoNonValorizzatoException(FOR_NAME + "Attributo file non valorizzato!");
        }
        return this.file;
    }

    /**
     * @param string nome del file da impostare
     */
    public void setFile(String string) {
        file = string;
    }

    /**
      * @return true se il nome del file non e' valorizzato (non e' possibile usare questo ogetto se manca questo attributo)
      */
    public boolean isFileEmpty() {
        return (this.file == null);
    }
    
    
    /* **************************************************** *
     *          Metodi getter e setter per autore           *
     * **************************************************** */
    /**
     * @return autore - autore del caricamento
     * @throws AttributoNonValorizzatoException se questo oggetto viene usato e questo campo non e' valorizzato
     */
    public String getAutore() throws AttributoNonValorizzatoException {
        if (autore == null) {
            throw new AttributoNonValorizzatoException(FOR_NAME + "Attributo autore non valorizzato!");
        } 
        return this.autore;
    }
    
    /**
     * @param string nome utente da impostare come autore
     */
    public void setAutore(String string) {
        autore = string;
    }
    
    /**
     * @return true se questo oggetto ha il campo autore non valorizzato
     */
    public boolean isAutoreEmpty() {
        return (this.autore == null);
    }
    
    
    /* **************************************************** *
     *          Metodi getter e setter per lingua           *
     * **************************************************** */
    /**
     * @return lingua
     */
    public String getLingua() throws AttributoNonValorizzatoException {
        if (lingua == null) {
            throw new AttributoNonValorizzatoException(FOR_NAME + "Attributo lingua non valorizzato!");
        }
        return this.lingua;
    }

    /**
     * @param lingua
     */
    public void setLingua(String string) {
        lingua = string;
    }
   
   
    /* **************************************************** *
     *           Metodi getter e setter per titolo          *
     * **************************************************** */
    /**
     * @return titolo
     */
    public String getTitolo() throws AttributoNonValorizzatoException {
        if (titolo == null) {
            throw new AttributoNonValorizzatoException(FOR_NAME + "Attributo titolo non valorizzato!");
        }
        return this.titolo;
    }
   
    /**
     * @param titolo
     */
    public void setTitolo(String string) {
        titolo = string;
    }

    /**
     * @return
     */
    public boolean isTitoloEmpty() {
        return (this.titolo == null);
    }
    
   
    /* **************************************************** *
     *            Metodi getter e setter per MIME           *
     * **************************************************** */
    /**
     * @return mime
     */
    public String getMime() throws AttributoNonValorizzatoException {
        if (mime == null) {
            throw new AttributoNonValorizzatoException(FOR_NAME + "Attributo mime non valorizzato!");
        }
        return this.mime;
    }

    /**
     * @param mime
     */
    public void setMime(String string) {
        mime = string;
    }
   
   
    /* **************************************************** *
     *           Metodi getter e setter per data            *
     * **************************************************** */
    /**
     * @return data
     */
    public Date getData() throws AttributoNonValorizzatoException {
        if (new Date(0).equals(data)) {
            throw new AttributoNonValorizzatoException(FOR_NAME + "Attributo data non valorizzato!");
        }
        return this.data;
    }   

    /**
     * @param data
     */
    public void setData(Date date) {
        data = date;
    }
   
   
    /* **************************************************** *
     *         Metodi getter e setter per riservato         *
     * **************************************************** */
	/**
	 * @return the riservato
	 */
	public boolean isRiservato() {
		return riservato;
	}

	/**
	 * @param riservato the riservato to set
	 */
	public void setRiservato(boolean riservato) {
		this.riservato = riservato;
	}

}