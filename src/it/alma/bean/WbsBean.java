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

import java.io.Serializable;
import java.sql.Time;
import java.util.Date;
import java.util.Vector;

import it.alma.exception.AttributoNonValorizzatoException;

/**
 * <p>Classe usata per rappresentare una WBS.</p>
 * 
 * @author <a href="mailto:andrea.tonel@studenti.univr.it">Andrea Tonel</a>
 * @author <a href="mailto:giovanroberto.torre@univr.it">Giovanroberto Torre</a>
 */
public class WbsBean implements Serializable {

	/**
     * La serializzazione necessita di dichiarare una costante di tipo long
     * identificativa della versione seriale. 
     * (Se questo dato non fosse inserito, verrebbe calcolato in maniera automatica
     * dalla JVM, e questo potrebbe portare a errori riguardo alla serializzazione). 
	 */
	private static final long serialVersionUID = -5000665863210663709L;
	/**
     *  Nome di questa classe. 
     *  Viene utilizzato per contestualizzare i messaggi di errore.
     */
    private final String FOR_NAME = "\n" + this.getClass().getName() + ": "; //$NON-NLS-1$
    /* ************************************************************************ *  
     *                      Dati identificativi della WBS                       *
     * ************************************************************************ */
    /** Attributo identificativo della WBS */
    private int id;
    /** Nome della WBS */
    private String nome;
    /* ************************************************************************ *  
     *                       Dati descrittivi della WBS                         *
     * ************************************************************************ */
	/** Descrizione della WBS */
    private String descrizione;
    /** Work package true se WBS is work package */
    private boolean workPackage;
    /** Attributo identificativo della WBS padre della WBS corrente */
    private WbsBean wbsPadre;
    /** Attributo identificativo delle WBS figlie della WBS corrente */
    private Vector<WbsBean> wbsFiglie;
    /** Attributo identificativo delle attivit&agrave; della WBS corrente */
    private Vector<ActivityBean> attivita;
    /** Note di avanzamento della WBS */
    private String noteAvanzamento;
    /** Risultati raggiunti da questa WBS */
    private String risultatiRaggiunti;
    /** Id del progetto a cui appartiene la wbs */
    private int idProgetto;
    /* ******************************************************** *
     *          Dati descrittivi dell'ultima modifica           *
     * ******************************************************** */
    /** Data ultima modifica */
    private Date dataUltimaModifica;
    /** Ora ultima modifica */
    private Time oraUltimaModifica;
    /** Autore ultima modifica */
    private String autoreUltimaModifica;
    
	
    /**
     * <p>Costruttore: inizializza i campi a valori di default.</p>
     */
	public WbsBean() {
		id = idProgetto = -2;
		nome = null;
		descrizione = null;
		workPackage = false;
		wbsPadre = null;
		wbsFiglie = null;
		attivita = null;
		noteAvanzamento = risultatiRaggiunti = null;
		dataUltimaModifica = new Date(0);
        oraUltimaModifica = null;
        autoreUltimaModifica = null;
	}


	/* **************************************************** *
     *           Metodi getter e setter per id              *
     * **************************************************** */
    /**
	 * Restituisce l'id di una WBS
	 * @return <code>id</code> - l'id della WBS
     * @throws it.alma.exception.AttributoNonValorizzatoException  eccezione che viene sollevata se questo oggetto viene usato e l'id non &egrave; stato valorizzato (&egrave; un dato obbligatorio)
	 */
	public int getId() throws AttributoNonValorizzatoException {
		if (id == -2) {
			throw new AttributoNonValorizzatoException(FOR_NAME + "Attributo id non valorizzato!");
		}
		return id;
	}
	
	/**
	 * Imposta l'id di una WBS
	 * @param id - l'identificativo da impostare
	 */
	public void setId(int id) {
		this.id = id;
	}
	
	
	/* ****************************************************** *
     *           Metodi getter e setter per nome              *
     * ****************************************************** */
	/**
	 * Restituisce il nome di una WBS
	 * @return <code>nome</code> - nome della WBS
	 * @throws it.alma.exception.AttributoNonValorizzatoException  eccezione che viene sollevata se questo oggetto viene usato e il nome non &egrave; stato valorizzato (&egrave; un dato obbligatorio)
	 */
	public String getNome() throws AttributoNonValorizzatoException {
		if (nome == null) {
			throw new AttributoNonValorizzatoException(FOR_NAME + "Attributo nome non valorizzato!");
		}
		return nome;
	}
	
	/**
	 * Imposta il nome di una WBS
	 * @param nome - nome da impostare
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}


	/* ********************************************************* *
     *         Metodi getter e setter per descrizione            *
     * ********************************************************* */
	/**
	 * Restituisce la descrizione di una WBS
	 * @return <code>descrizione</code> - descrizione della WBS
	 */
	public String getDescrizione() {
		return descrizione;
	}

	/**
	 * Imposta la descrizione di una WBS
	 * @param descrizione - descrizione da settare
	 */
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}


	/* ********************************************************* *
     *         Metodi getter e setter per workPackage            *
     * ********************************************************* */
	/**
	 * Restituisce true se la WBS &egrave; una work package, false altrimenti
	 * @return <code>workPackage</code> - true se WBS &egrave; work package, false altrimenti
	 */
	public boolean isWorkPackage() {
		return workPackage;
	}

	/**
	 * Imposta l'attributo workPackage della WBS
	 * @param workPackage - workPackage da impostare
	 */
	public void setWorkPackage(boolean workPackage) {
		this.workPackage = workPackage;
	}

	
    /* ********************************************************* *
     *   Metodi getter e setter per wbsPadre della WBS corrente  *
     * ********************************************************* */
	/**
	 * Restituisce il bean rappresentante la WBS padre.
     * @return <code>wbsPadre</code> - WBS padre della WBS attuale
     */
    public WbsBean getWbsPadre() {
        return wbsPadre;
    }

    /**
     * Imposta il bean rappresentante la WBS padre.
     * @param wbsPadre - WBS da impostare
     */
    public void setWbsPadre(WbsBean wbsPadre) {
        this.wbsPadre = wbsPadre;
    }
    
    
    /* ********************************************************* *
     *  Metodi getter e setter per wbsFiglie della WBS corrente  *
     * ********************************************************* */
    /**
     * Restituisce il Vector di bean rappresentante l'elenco 
     * delle WBS figlie della WBS corrente.
     * 
     * @return <code>Vector&lt;WbsBean&gt;</code> -  elenco WBS figlie da restituire
     */
    public Vector<WbsBean> getWbsFiglie() {
        return wbsFiglie;
    }

    /**
     * Imposta il Vector di bean rappresentante l'elenco 
     * delle WBS figlie della WBS corrente.
     * 
     * @param wbsFiglie - elenco WBS figlie da impostare
     */
    public void setWbsFiglie(Vector<WbsBean> wbsFiglie) {
        this.wbsFiglie = wbsFiglie;
    }
    
    
    /* ********************************************************* *
     *  Metodi getter e setter per attivita' della WBS corrente  *
     * ********************************************************* */
    /**
     * Restituisce il Vector di bean rappresentante l'elenco 
     * delle attivit&agrave; eventualmente collegate alla WBS corrente.
     * 
     * @return <code>Vector&lt;ActivityBean&gt;</code> -  elenco attivita' di questa WBS da restituire
     */
    public Vector<ActivityBean> getAttivita() {
        return attivita;
    }

    /**
     * Imposta il Vector di bean rappresentante l'elenco 
     * delle attivit&agrave; collegate alla WBS corrente.
     * 
     * @param attivita - elenco attivita' collegate da impostare
     */
    public void setAttivita(Vector<ActivityBean> attivita) {
        this.attivita = attivita;
    }
    

    /* ********************************************************* *
     *       Metodi getter e setter per noteAvanzamento          *
     * ********************************************************* */
    /**
     * Restituisce le note di avanzamento della WBS
     * 
     * @return <code>noteAvanzamento</code> - note di avanzamento della WBS
     */
    public String getNoteAvanzamento() {
        return noteAvanzamento;
    }

    /**
     * Imposta le note di avanzamento di una WBS
     * 
     * @param noteAvanzamento - note di avanzamento da impostare
     */
    public void setNoteAvanzamento(String noteAvanzamento) {
        this.noteAvanzamento = noteAvanzamento;
    }

    
    /* ********************************************************* *
     *      Metodi getter e setter per risultatiRaggiunti        *
     * ********************************************************* */
    /**
     * Restituisce i risultati raggiunti dalla WBS
     * 
     * @return <code>String</code> - risultati realizzati dalla WBS
     */
    public String getRisultatiRaggiunti() {
        return risultatiRaggiunti;
    }

    /**
     * Imposta i risultati raggiunti dalla WBS
     * 
     * @param risultatiRaggiunti i risultatiRaggiunti da impostare
     */
    public void setRisultatiRaggiunti(String risultatiRaggiunti) {
        this.risultatiRaggiunti = risultatiRaggiunti;
    }
    
    
    /* **************************************************** *
     *       Metodi getter e setter per idProgetto          *
     * **************************************************** */
    /**
     * Restituisce l'id del progetto di appartenenza.
     * @return <code>idProgetto</code> - l'id del progetto
     * @throws it.alma.exception.AttributoNonValorizzatoException  eccezione che viene sollevata se questo oggetto viene usato e l'idProgetto non &egrave; stato valorizzato (&egrave; un dato obbligatorio)
     */
    public int getIdProgetto() throws AttributoNonValorizzatoException {
        if (idProgetto == -2) {
            throw new AttributoNonValorizzatoException(FOR_NAME + "Attributo idProgetto non valorizzato!");
        }
        return idProgetto;
    }

    /**
     * Imposta l'id di un progetto.
     * @param idProgetto - l'id del progetto della wbs da impostare
     */
    public void setIdProgetto(int idProgetto) {
        this.idProgetto = idProgetto;
    }
    
    
    /* *********************************************************** *
     *       Metodi getter e setter per data ultima modifica       *
     * *********************************************************** */
    /**
     * Restituisce la data dell'ultima modifica dello status di un progetto
     * 
     * @return <code>java.util.Date</code> - data dell'ultima modifica
     */
    public Date getDataUltimaModifica() {
        return dataUltimaModifica;
    }

    /**
     * Imposta la data dell'ultima dello status di un progetto
     * 
     * @param dataUltimaModifica data ultima modifica da impostare
     */
    public void setDataUltimaModifica(Date dataUltimaModifica) {
        this.dataUltimaModifica = dataUltimaModifica;
    }

    
    /* *********************************************************** *
     *       Metodi getter e setter per ora ultima modifica        *
     * *********************************************************** */
    /**
     * Restituisce l'ora dell'ultima modifica dello status di un progetto
     * 
     * @return <code>java.sql.Time</code> - ora dell'ultima modifica
     */
    public Time getOraUltimaModifica() {
        return oraUltimaModifica;
    }
    
    /**
     * Imposta l'ora dell'ultima modifica dello status di un progetto
     * 
     * @param oraUltimaModifica ora ultima modifica da impostare
     */
    public void setOraUltimaModifica(Time oraUltimaModifica) {
        this.oraUltimaModifica = oraUltimaModifica;
    }


    /* ************************************************************** *
     *       Metodi getter e setter per autore ultima modifica        *
     * ************************************************************** */
    /**
     * Restituisce l'autore dell'ultima modifica dello status di un progetto
     * 
     * @return <code>String</code> - autore ultima modifica
     */
    public String getAutoreUltimaModifica() {
        return autoreUltimaModifica;
    }

    /**
     * Imposta l'autore dell'ultima modifica dello status di un progetto
     * 
     * @param autoreUltimaModifica autore ultima modifica da impostare
     */
    public void setAutoreUltimaModifica(String autoreUltimaModifica) {
        this.autoreUltimaModifica = autoreUltimaModifica;
    }
    
}
