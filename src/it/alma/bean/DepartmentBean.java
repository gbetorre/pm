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

import it.alma.exception.AttributoNonValorizzatoException;

/**
 * <p>Classe usata per rappresentare un dipartimento.</p>
 * 
 * @author <a href="mailto:andrea.tonel@studenti.univr.it">Andrea Tonel</a>
 */
public class DepartmentBean implements Serializable {

	/**
     * La serializzazione necessita di dichiarare una costante di tipo long
     * identificativa della versione seriale. 
     * (Se questo dato non fosse inserito, verrebbe calcolato in maniera automatica
     * dalla JVM, e questo potrebbe portare a errori riguardo alla serializzazione). 
	 */
	private static final long serialVersionUID = 1L;
	/**
     *  Nome di questa classe. 
     *  Viene utilizzato per contestualizzare i messaggi di errore.
     */
    private final String FOR_NAME = "\n" + this.getClass().getName() + ": "; //$NON-NLS-1$
    /* **************************************************************************** *  
     *                    Dati identificativi del dipartimento                      *
     * **************************************************************************** */
    /** Attributo identificativo del dipartimento */
    private int id;
    /** Nome del dipartimento */
    private String nome;
    /** Prefisso del dipartimento */
    private String prefisso;
    /** Indirizzo della sede del dipartimento */
    private String indirizzoSede;
    
    /**
     * <p>Costruttore che inizializza i campi a valori di default.</p>
     */
    public DepartmentBean() {
    	id = -2;
    	nome = null;
    	prefisso = null;
    	indirizzoSede = null;
    }

    
    /* **************************************************** *
     *           Metodi getter e setter per id              *
     * **************************************************** */
    /**
	 * Restituisce l'id di un dipartimento.
	 * @return <code>id</code> - l'id del dipartimento
     * @throws it.alma.exception.AttributoNonValorizzatoException  eccezione che viene sollevata se questo oggetto viene usato e l'id non &egrave; stato valorizzato (&egrave; un dato obbligatorio)
	 */
	public int getId() throws AttributoNonValorizzatoException {
		if (id == -2) {
			throw new AttributoNonValorizzatoException(FOR_NAME + "Attributo id non valorizzato!");
		}
		return id;
	}
	
	/**
	 * Imposta l'id di un progetto.
	 * @param id - l'id del progetto da impostare
	 */
	public void setId(int id) {
		this.id = id;
	}
	
	
	/* **************************************************** *
     *           Metodi getter e setter per nome            *
     * **************************************************** */
	/**
	 * Restituisce il nome di un dipartimento.
	 * @return <code>nome</code> - nome del dipartimento
	 * @throws it.alma.exception.AttributoNonValorizzatoException  eccezione che viene sollevata se questo oggetto viene usato e il nome non &egrave; stato valorizzato (&egrave; un dato obbligatorio) 
	 */
	public String getNome() throws AttributoNonValorizzatoException {
		if (nome == null) {
			throw new AttributoNonValorizzatoException(FOR_NAME + "Attributo nome non valorizzato!");
		}
		return nome;
	}

	/**
	 * Imposta il nome di un dipartimento.
	 * @param nome - il nome del dipartimento da impostare
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}

	
	/* **************************************************** *
     *           Metodi getter e setter per prefisso        *
     * **************************************************** */
	/**
	 * Restituisce il prefisso di un dipartimento.
	 * @return <code>prefisso</code> - prefisso del dipartimento
	 */
	public String getPrefisso() {
		return prefisso;
	}

	/**
	 * Imposta il prefisso di un dipartimento.
	 * @param prefisso - il prefisso del dipartimento da impostare
	 */
	public void setPrefisso(String prefisso) {
		this.prefisso = prefisso;
	}

	

	/* **************************************************** *
     *        Metodi getter e setter per indirizzoSede      *
     * **************************************************** */
	/**
	 * Restituisce l'indirizzo della sede di un dipartimento.
	 * @return <code>indirizzoSede</code> - indirizzo della sede del dipartimento
	 */
	public String getIndirizzoSede() {
		return indirizzoSede;
	}

	/**
	 * Imposta l'indirizzo della sede di un dipartimento.
	 * @param indirizzoSede - indirizzo della sede da impostare
	 */
	public void setIndirizzoSede(String indirizzoSede) {
		this.indirizzoSede = indirizzoSede;
	}


}
