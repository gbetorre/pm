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
 * <p>Classe usata per rappresentare lo stato di un progetto.</p>
 * 
 * @author <a href="mailto:andrea.tonel@studenti.univr.it">Andrea Tonel</a>
 */
public class StatoProgettoBean implements Serializable, Query {

	/**
     * La serializzazione necessita di dichiarare una costante di tipo long
     * identificativa della versione seriale. 
     * (Se questo dato non fosse inserito, verrebbe calcolato in maniera automatica
     * dalla JVM, e questo potrebbe portare a errori riguardo alla serializzazione). 
	 */
	private static final long serialVersionUID = -7677133090505981404L;
	/**
     *  Nome di questa classe. 
     *  Viene utilizzato per contestualizzare i messaggi di errore.
     */
    private final String FOR_NAME = "\n" + this.getClass().getName() + ": "; //$NON-NLS-1$
    /* **************************************************************************** *  
     *                Dati identificativi dello stato del progetto                  *
     * **************************************************************************** */
	/** Attributo identificativo dello stato del progetto */
    private int id;
    /** Nome dello stato del progetto */
    private String nome;
    /** Valore dello stato del progetto */
    private String valore;
    
    
    public StatoProgettoBean() {
    	id = -2;
    	nome = null;
    	valore = null;
    }
    
    
    /* **************************************************** *
     *           Metodi getter e setter per id              *
     * **************************************************** */
	/**
	 * Restituisce l'identificativo dello stato del progetto
	 * @return <code>id</code> - l'identificativo dello stato del progetto
	 * @throws it.alma.exception.AttributoNonValorizzatoException  eccezione che viene sollevata se questo oggetto viene usato e l'id non &egrave; stato valorizzato (&egrave; un dato obbligatorio) 
	 */
	public int getId() throws AttributoNonValorizzatoException {
		if (id == -2) {
			throw new AttributoNonValorizzatoException(FOR_NAME + "Attributo id non valorizzato!");
		}
		return id;
	}
	
	/**
	 * Imposta l'id dello stato del progetto, passato come parametro.
	 * @param id - identificativo da impostare
	 */
	public void setId(int id) {
		this.id = id;
	}


	/* **************************************************** *
     *          Metodi getter e setter per nome             *
     * **************************************************** */
	/**
	 * Restituisce il nome dello stato del progetto
	 * @return <code>nome</code> - nome del progetto
	 * @throws it.alma.exception.AttributoNonValorizzatoException  eccezione che viene sollevata se questo oggetto viene usato e nome non &egrave; stato valorizzato (&egrave; un dato obbligatorio) 
	 * oppure nome non &egrave; stato valorizzato correttamente
	 */
	public String getNome() throws AttributoNonValorizzatoException {
		if (nome == null) {
			throw new AttributoNonValorizzatoException(FOR_NAME + "Attributo id non valorizzato!");
		}
		else if (!STATI_PROGETTO_AS_LIST.contains(nome)) {
			throw new AttributoNonValorizzatoException(FOR_NAME + "Attributo id non valorizzato correttamente!");
		}
		else {
			return nome;
		}
	}

	/**
	 * Imposta il nome dello stato del progetto
	 * @param nome - nome dello stato da impostare
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}

	
	/* **************************************************** *
     *         Metodi getter e setter per valore            *
     * **************************************************** */
	/**
	 * Restituisce il valore dello stato del progetto
	 * @return <code>valore</code> - valore del progetto
	 */
	public String getValore() {
		return valore;
	}

	/**
	 *  Imposta il valore dello stato del progetto
	 * @param valore - valore del progetto da impostare
	 */
	public void setValore(String valore) {
		this.valore = valore;
	}
	

}
