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

import it.alma.Query;
import it.alma.exception.AttributoNonValorizzatoException;

/**
 * <p>Classe usata per rappresentare una WBS.</p>
 * 
 * @author <a href="mailto:andrea.tonel@studenti.univr.it">Andrea Tonel</a>
 */
public class WbsBean implements Serializable, Query {

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
    /** Attributo identificativo della WBS padre */
    private WbsBean wbsPadre;
    
	
    /**
     * <p>Costruttore: inizializza i campi a valori di default.</p>
     */
	public WbsBean() {
		id = -2;
		nome = null;
		descrizione = null;
		workPackage = false;
		wbsPadre = null;
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
     *         Metodi getter e setter per workPackage            *
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

}
