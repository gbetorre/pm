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
 * <p>Classe usata per rappresentare un rischio.</p>
 * 
 * @author <a href="mailto:andrea.tonel@studenti.univr.it">Andrea Tonel</a>
 */
public class RiskBean implements Serializable, Query{

	/**
	 * La serializzazione necessita di dichiarare una costante di tipo long
	 * identificativa della versione seriale. 
	 * (Se questo dato non fosse inserito, verrebbe calcolato in maniera automatica
	 * dalla JVM, e questo potrebbe portare a errori riguardo alla serializzazione). 
	 */
	private static final long serialVersionUID = 5746067585544185299L;
	/**
     *  Nome di questa classe. 
     *  Viene utilizzato per contestualizzare i messaggi di errore.
     */
    private final String FOR_NAME = "\n" + this.getClass().getName() + ": "; //$NON-NLS-1$
    /* *************************************************************************** *  
     *                      Dati identificativi del rischio                        *
     * *************************************************************************** */
    /** Attributo identificativo del rischio */
    private int id;
    /** Descrizione del rischio */
    private String descrizione;
    /** Probabilit&agrave; del rischio */
    private String probabilita;
    /** Impatto del rischio */
    private String impatto;
    /** Livello del rischio */
    private String livello;
    /** Stato del rischio */
    private String stato;
    /** Identificativo del progetto a cui fa riferimento il rischio */
    private int idProgetto;
	
    /**
     * <p>Costruttore: inizializza i campi a valori di default.</p>
     */
    public RiskBean() {
    	id = -2;
    	descrizione = null;
    	probabilita = impatto = livello = null;
    	stato = null;
    	idProgetto = -2;
    }


    /* **************************************************** *
     *           Metodi getter e setter per id              *
     * **************************************************** */
    /**
	 * Restituisce l'id di un rischio
	 * @return <code>id</code> - l'id del rischio
     * @throws it.alma.exception.AttributoNonValorizzatoException  eccezione che viene sollevata se questo oggetto viene usato e l'id non &egrave; stato valorizzato (&egrave; un dato obbligatorio)
	 */
	public int getId() throws AttributoNonValorizzatoException {
		if (id == -2) {
			throw new AttributoNonValorizzatoException(FOR_NAME + "Attributo id non valorizzato!");
		}
		return id;
	}
	
	/**
	 * Imposta l'id di un rischio
	 * @param id - l'identificativo da impostare
	 */
	public void setId(int id) {
		this.id = id;
	}
	
	
	/* ********************************************************* *
     *         Metodi getter e setter per descrizione            *
     * ********************************************************* */
	/**
	 * Restituisce la descrizione di un rischio
	 * @return <code>descrizione</code> - descrizione del rischio
	 * @throws it.alma.exception.AttributoNonValorizzatoException  eccezione che viene sollevata se questo oggetto viene usato e la descrizione non &egrave; stato valorizzato (&egrave; un dato obbligatorio)
	 */
	public String getDescrizione() throws AttributoNonValorizzatoException {
		if (descrizione == null) {
			throw new AttributoNonValorizzatoException(FOR_NAME + "Attributo nome non valorizzato!");
		}
		return descrizione;
	}

	/**
	 * Imposta la descrizione di un rischio
	 * @param descrizione - descrizione da settare
	 */
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}


	/* ********************************************************* *
     *         Metodi getter e setter per probabilità            *
     * ********************************************************* */
	/**
	 * Restituisce la probabilit&agrave; del rischio
	 * @return <code>probabilita</code> - probabilita del rischio
	 * @throws it.alma.exception.AttributoNonValorizzatoException  eccezione che viene sollevata se questo oggetto viene usato e probabilita non &egrave; stato valorizzato (&egrave; un dato obbligatorio) 
	 * oppure probabilita non &egrave; stato valorizzato correttamente
	 */
	public String getProbabilita() throws AttributoNonValorizzatoException {
		if (probabilita == null) {
			throw new AttributoNonValorizzatoException(FOR_NAME + "Attributo probabilita non valorizzato!");
		}
		else if (!LIVELLI_RISCHIO_AS_LIST.contains(probabilita)) {
			throw new AttributoNonValorizzatoException(FOR_NAME + "Attributo probabilita non valorizzato correttamente!");
		}
		else {
			return probabilita;
		}
	}

	/**
	 * Imposta la probabilit&agrave; di rischio
	 * @param probabilita - probabilita del rischio
	 */
	public void setProbabilita(String probabilita) {
		this.probabilita = probabilita;
	}
	
	
	/* ********************************************************* *
     *           Metodi getter e setter per impatto              *
     * ********************************************************* */
	/**
	 * Restituisce l'impatto del rischio
	 * @return <code>impatto</code> - impatto del rischio
	 * @throws it.alma.exception.AttributoNonValorizzatoException  eccezione che viene sollevata se questo oggetto viene usato e impatto non &egrave; stato valorizzato (&egrave; un dato obbligatorio) 
	 * oppure impatto non &egrave; stato valorizzato correttamente
	 */
	public String getImpatto() throws AttributoNonValorizzatoException {
		if (impatto == null) {
			throw new AttributoNonValorizzatoException(FOR_NAME + "Attributo impatto non valorizzato!");
		}
		else if (!LIVELLI_RISCHIO_AS_LIST.contains(impatto)) {
			throw new AttributoNonValorizzatoException(FOR_NAME + "Attributo impatto non valorizzato correttamente!");
		}
		else {
			return impatto;
		}
	}

	/**
	 * Imposta l'impatto di rischio
	 * @param impatto - impatto del rischio
	 */
	public void setImpatto(String impatto) {
		this.impatto = impatto;
	}
	
	
	/* ********************************************************* *
     *           Metodi getter e setter per livello              *
     * ********************************************************* */
	/**
	 * Restituisce il livello del rischio
	 * @return <code>livello</code> - livello del rischio
	 * @throws it.alma.exception.AttributoNonValorizzatoException  eccezione che viene sollevata se questo oggetto viene usato e livello non &egrave; stato valorizzato (&egrave; un dato obbligatorio) 
	 * oppure livello non &egrave; stato valorizzato correttamente
	 */
	public String getLivello() throws AttributoNonValorizzatoException {
		if (livello == null) {
			throw new AttributoNonValorizzatoException(FOR_NAME + "Attributo livello non valorizzato!");
		}
		else if (!LIVELLI_RISCHIO_AS_LIST.contains(livello)) {
			throw new AttributoNonValorizzatoException(FOR_NAME + "Attributo livello non valorizzato correttamente!");
		}
		else {
			return livello;
		}
	}

	/**
	 * Imposta il livello di rischio
	 * @param livello - livello del rischio
	 */
	public void setLivello(String livello) {
		this.livello = livello;
	}


	/* ********************************************************* *
     *            Metodi getter e setter per stato               *
     * ********************************************************* */
	/**
	 * Restituisce lo stato del rischio
	 * @return <code>stato</code> - stato del rischio
	 * @throws it.alma.exception.AttributoNonValorizzatoException  eccezione che viene sollevata se questo oggetto viene usato e stato non &egrave; stato valorizzato (&egrave; un dato obbligatorio) 
	 * oppure stato non &egrave; stato valorizzato correttamente
	 */
	public String getStato() throws AttributoNonValorizzatoException {
		if (stato == null) {
			throw new AttributoNonValorizzatoException(FOR_NAME + "Attributo stato non valorizzato!");
		}
		else if (!STATO_RISCHIO_AS_LIST.contains(stato)) {
			throw new AttributoNonValorizzatoException(FOR_NAME + "Attributo stato non valorizzato correttamente!");
		}
		else {
			return stato;
		}
	}

	/**
	 * Imposta il stato di rischio
	 * @param stato - stato del rischio
	 */
	public void setStato(String stato) {
		this.stato = stato;
	}


	/* ********************************************************* *
     *          Metodi getter e setter per idProgetto            *
     * ********************************************************* */
    /**
     * Restituisce l'id del progetto a cui il rischio fa riferimento
     * @return <code>idProgetto</code> identificativo del progetto a cui il rischio fa riferimento
     * @throws it.alma.exception.AttributoNonValorizzatoException  eccezione che viene sollevata se questo oggetto viene usato e idProgetto non &egrave; stato valorizzato (&egrave; un dato obbligatorio)
     */
    public int getIdProgetto() throws AttributoNonValorizzatoException {
        if (stato == null) {
            throw new AttributoNonValorizzatoException(FOR_NAME + "Attributo stato non valorizzato!");
        }
        return idProgetto;
    }


    /**
     * Imposta l'id del progetto a cui il rischio fa riferimento
     * @param idProgetto - id del progetto da impostare
     */
    public void setIdProgetto(int idProgetto) {
        this.idProgetto = idProgetto;
    }
}
