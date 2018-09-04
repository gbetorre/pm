/*
 *   uol: University on Line. Applicazione WEB per la visualizzazione
 *   di siti web di Facoltà
 *   Copyright (C) 2000,2001 Roberto Posenato, Mirko Manea
 *
 *   This program is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation; either version 2 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program; if not, write to the Free Software
 *   Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 *   Roberto Posenato <posenato@sci.univr.it>
 *   Dipartimento di Informatica
 *   Università degli Studi di Verona
 *   Strada le Grazie 15
 *   37134 Verona (Italy)
 *
 */
package it.alma.bean;

import it.alma.exception.AttributoNonValorizzatoException;

import java.util.Vector;


/**
 * AttivitaFormativaBean rappresenta un'attivita formativa
 * inserita in un piano didattico.
 * Questo bean rappresenta gli attributi provenienti dalle tabelle:
 * 1. attivitaPiano
 * 2. tipoAttivita
 * @author Roberto Posenato
 */
public class AttivitaFormativaBean {
	
	private float crediti;
        private float creditiMax;
	private int id;
	private int idPiano;
	/**
	 * Gli anni di presenza di un'attività formativa sono codificati
	 * come sequenze di bit: bit 1 in posizione 1 indica presenza al primo anno,
	 * bit 1 in posizione 2 indica presenza al secondo anno, ecc.
	 */
	private int anniCodificati;
	/**
	 * insegnamenti rappresenta
	 * le occorrenze di insegnamento associate all'attività formativa
	 * Questo vector puo` avere le seguenti dimensioni:
	 *   0 => questa attivita` formativa NON è un insegnamento
	 *   1 => questa attivita formativa coincide con 1 insegnamento
	 * > 1 => questa attivita formativa è composta da insegnamenti opzionali
	 */
	private Vector insegnamenti;
	private String nome;
	private String linguaNome;
	private int numeroOrdine;
	private boolean obbligatorio;
	/**
	 * taf (Tipo Attivita Formativa) rappresenta il tipo di attività:
	 * "A" = di base,<br>
	 * "B" = caratterizzante,<br>
	 * "FN" = fondamentale, ecc.
	 */
	private String taf;
        /* il tipo di erogazione serve a discriminare le attività formative che 
         * contengono insegnamenti normali da altri tipi di erogazioni ad esempio
         * i corsi elettivi di medicina
         */
        private String tipoErogazione;
        private String nota;
	
	
	public AttivitaFormativaBean() {
		anniCodificati = id = idPiano = numeroOrdine = -2;
		crediti = creditiMax = (float) -2;
		linguaNome = taf = nome = tipoErogazione = null;
		insegnamenti = new Vector();
	}
	
	/**
	 * Gli anni di presenza di un'attività formativa sono codificati
	 * come sequenze di bit: bit 1 in posizione 1 indica presenza al primo anno,
	 * bit 1 in posizione 2 indica presenza al secondo anno, ecc.
	 *
	 * @return gli anni rappresentati come una stringa formata dagli anni separati da spazi in cui
	 * è presente l'attività.
	 */
	public String getAnni() throws AttributoNonValorizzatoException {
		if ( anniCodificati == -2)
			throw new AttributoNonValorizzatoException("AttivitaFormativaBean: attributo anni non valorizzato!");
		else return Integer.toString( anniCodificati );
	}
	
	/**
	 * Gli anni di presenza di un'attività formativa sono codificati
	 * come sequenze di bit: bit 1 in posizione 1 indica presenza al primo anno,
	 * bit 1 in posizione 2 indica presenza al secondo anno, ecc.
	 *
	 * Questo metodo non dovrebbe essere mai usato, ma è necessario per il BeanUtils.populate() per 
	 * determinare il tipo dell'attributo anniCodificati.
	 *
	 * @return gli anni rappresentati come intero.
	 */
	public int getAnniCodificati() throws AttributoNonValorizzatoException {
		if ( anniCodificati == -2)
			throw new AttributoNonValorizzatoException("AttivitaFormativaBean: attributo anniCodificati non valorizzato!");
		else return anniCodificati;
	}
	
	/**
	 * Setter for property anniCodificati.
	 * @param a New value of property anniCodificati.
	 */
	public void setAnniCodificati(int a) {
		anniCodificati = a;
	}
	
	/**
	 * @return crediti
	 */
	public float getCrediti() {
		return crediti;
	}
	
	/**
	 * @param crediti
	 */
	public void setCrediti(float v) {
		this.crediti = v;
	}
	
	/**
	 * @return id
	 */
	public int getId() throws AttributoNonValorizzatoException {
		if (id == -2)
			throw new AttributoNonValorizzatoException("AttivitaFormativaBean: attributo id non valorizzato!");
		else return this.id;
	}
	
	/**
	 * @param id
	 */
	public void setId(int v) {
		this.id = v;
	}
	
	/**
	 * @return idPiano
	 */
	public int getIdPiano() throws AttributoNonValorizzatoException {
		if (idPiano == -2) {
			throw new AttributoNonValorizzatoException("AttivitaFormativaBean: attributo idPiano non valorizzato!");
		} else {
			return this.idPiano;
		}
	}
	
	/**
	 * @param idPiano
	 */
	public void setIdPiano(int v) {
		this.idPiano = v;
	}
	
	/**
	 * @return numeroOrdine
	 */
	public int getNumeroOrdine() throws AttributoNonValorizzatoException {
		if (numeroOrdine == -2) {
			throw new AttributoNonValorizzatoException("AttivitaFormativaBean: attributo numeroOrdine non valorizzato!");
		} else {
			return this.numeroOrdine;
		}
	}
	
	/**
	 * @param numeroOrdine
	 */
	public void setNumeroOrdine(int v) {
		this.numeroOrdine = v;
	}
	
	/**
	 * @return taf
	 */
	public String getTaf() throws AttributoNonValorizzatoException {
		if (taf == null) {
			throw new AttributoNonValorizzatoException("AttivitaFormativaBean: attributo taf non valorizzato!");
		} else {
			return this.taf;
		}
	}
	
	/**
	 * @param taf
	 */
	public void setTaf(String v) {
		this.taf = v;
	}
	
	/**
	 * @return nome
	 */
	public String getNome() throws AttributoNonValorizzatoException {
		if (nome == null) {
			throw new AttributoNonValorizzatoException("AttivitaFormativaBean: attributo nome non valorizzato!");
		} else {
			return this.nome;
		}
	}
	
	/**
	 * @param nome
	 */
	public void setNome(String v) {
		this.nome = v;
	}
	
	/**
	 * @return insegnamenti
	 */
	public Vector getInsegnamenti() {
		return insegnamenti;
	}
	
	/**
	 * @param insegnamenti
	 */
	public void setInsegnamenti(Vector v) {
		this.insegnamenti = (Vector) v.clone();
	}
	
	/**
	 * @return obbligatorio
	 *
	 */
	public boolean isObbligatorio() {
		return this.obbligatorio;
	}
	
	/**
	 * @param obbligatorio
	 *
	 */
	public void setObbligatorio(boolean obbligatorio) {
		this.obbligatorio = obbligatorio;
	}
	
	/**
	 * Getter for property linguaNome.
	 * @return Value of property linguaNome.
	 */
	public String getLinguaNome() throws AttributoNonValorizzatoException {
		if (linguaNome == null) {
			throw new AttributoNonValorizzatoException("AttivitaFormativaBean: attributo linguaNome non valorizzato!");
		} else {
			return this.linguaNome;
		}
	}
	
	/**
	 * Setter for property linguaNome.
	 * @param linguaNome New value of property linguaNome.
	 */
	public void setLinguaNome(String linguaNome) {
		this.linguaNome = linguaNome;
	}
        
	/**
	 * @param tipoErogazione
	 */
	public void setTipoErogazione(String v) {
		this.tipoErogazione = v;
	}        
	
	/**
	 * @return tipoErogazione
	 */
	public String getTipoErogazione() {
		return tipoErogazione;
	} 
        
        /**
	 * @return nota
	 */
	public String getNota() throws AttributoNonValorizzatoException {
		if (nota == null) {
			throw new AttributoNonValorizzatoException("AttivitaFormativaBean: attributo nota non valorizzato!");
		} else {
			return this.nota;
		}
	}
	
	/**
	 * @param nota
	 */
	public void setNota(String v) {
		this.nota = v;
	}
	
        /**
	 * @return crediti
	 */
	public float getCreditiMax() {
		return creditiMax;
	}
	
	/**
	 * @param crediti
	 */
	public void setCreditiMax(float v) {
		this.creditiMax = v;
	}
}
