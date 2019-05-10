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
import java.util.Vector;

import it.alma.exception.AttributoNonValorizzatoException;

/**
 * <p>Classe usata per rappresentare un campo del monitoraggio MIUR.</p>
 * 
 * @author <a href="mailto:giovanroberto.torre@univr.it">Giovanroberto Torre</a>
 */
public class MonitorBean extends CodeBean implements Serializable {

	/**
     * La serializzazione necessita di dichiarare una costante di tipo long
     * identificativa della versione seriale. 
     * (Se questo dato non fosse inserito, verrebbe calcolato in maniera automatica
     * dalla JVM, e questo potrebbe portare a errori riguardo alla serializzazione). 
	 */
    private static final long serialVersionUID = -6071966780657208115L;
    /**
     *  Nome di questa classe. 
     *  Viene utilizzato per contestualizzare i messaggi di errore.
     *  $NON-NLS-1$ silence a warning that Eclipse emits 
     *  when it encounters string literals.
     *  The idea is that UI messages should not be embedded as string literals,
     *  but rather sourced from a resource file 
     *  (so that they can be translated, proofed, etc).
     */
    private final String FOR_NAME = "\n" + this.getClass().getName() + ": "; //$NON-NLS-1$
    /** Anno del monitoraggio */
    private int anno;
    /** Identificativo del dipartimento monitorato */
    private int idDipart;
    /** Identifica se il monitoraggio &egrave; aperto o chiuso */
    private boolean open;
    /** Identificativo facoltativo per la descrizione della tipologia di quadro monitorato */
    private String descrizione;
    /** Informazioni su D4 = reclutamento */
    private String quadroD4;
    /** Informazioni su D5 = infrastrutture */
    private String quadroD5;
    /** Informazioni su D6 = premialit&agrave; */
    private String quadroD6;
    /** Informazioni su D7 = attivit&agrave; didattiche avanzate */
    private String quadroD7;
    /** Informazioni su D8 = modalit&agrave; e fasi del monitoraggio */
    private String quadroD8;
    /* *******************************************************  *
     *        Allegati che contribuiscono al monitoraggio       *
     *        Vector di fileset, ciascuno rappresentante        * 
     *        un riferimento logico ad un allegato fisico       *
     * ******************************************************** */
    /** Allegati al quadro 4 */
    private Vector<FileDocBean> allegatiD4;
    /** Allegati al quadro 5  */
    private Vector<FileDocBean> allegatiD5;
    /** Allegati al quadro 5  */
    private Vector<FileDocBean> allegatiD6;
    /** Allegati al quadro 5  */
    private Vector<FileDocBean> allegatiD7;
    /** Allegati al quadro 5  */
    private Vector<FileDocBean> allegatiD8;

    
    /**
     * <p>Costruttore: inizializza i campi a valori di default.</p>
     */
    public MonitorBean() {
    	anno = -2;
    	idDipart = -2;
    	open = false;
    	descrizione = null;
    	quadroD4 = quadroD5 = quadroD6 = quadroD7 = quadroD8 = null;
    	allegatiD4 = allegatiD5 = allegatiD6 = allegatiD7 = allegatiD8 = null;
    }
    
    
    /* **************************************************** *
     *           Metodi getter e setter per anno              *
     * **************************************************** */
    /**
	 * Restituisce l'anno di un'attivit&agrave; di monitoraggio
	 * 
	 * @return <code>id</code> - l'anno del monitoraggio
     * @throws it.alma.exception.AttributoNonValorizzatoException  eccezione che viene sollevata se questo oggetto viene usato e l'anno non &egrave; stato valorizzato (&egrave; un dato obbligatorio)
	 */
	public int getAnno() throws AttributoNonValorizzatoException {
		if (anno == -2) {
			throw new AttributoNonValorizzatoException(FOR_NAME + "Attributo anno non valorizzato!");
		}
		return anno;
	}
	
	/**
	 * Imposta l'anno di un'attivit&agrave; di monitoraggio
	 * 
	 * @param anno - l'anno da impostare
	 */
	public void setAnno(int anno) {
		this.anno = anno;
	}

	
    /* **************************************************** *
     *      Metodi getter e setter per id dipartimento      *
     * **************************************************** */
    /**
     * Restituisce l'identificativo di un dipartimento oggetto del monitoraggio
     * 
     * @return <code>id</code> - l'identificativo del dipartimento monitorato
     * @throws it.alma.exception.AttributoNonValorizzatoException  eccezione che viene sollevata se questo oggetto viene usato e l'anno non &egrave; stato valorizzato (&egrave; un dato obbligatorio)
     */
    public int getIdDipart() throws AttributoNonValorizzatoException {
        if (idDipart == -2) {
            throw new AttributoNonValorizzatoException(FOR_NAME + "Attributo identificativo dipartimento non valorizzato!");
        }
        return idDipart;
    }
    
    /**
     * Imposta l'identificativo di un dipartimento monitorato
     * 
     * @param idDipart - l'identificativo del dipartimento da impostare
     */
    public void setIdDipart(int idDipart) {
        this.idDipart = idDipart;
    }
    

    /* ******************************************************** *
     *  Metodi getter e setter per flag di monitoraggio aperto  *
     * ******************************************************** */
	/**
	 * Restituisce il flag boolean che specifica se il monitoraggio &egrave; editabile o meno
	 * 
     * @return <code>booolean</code> - flag che vale 'true' se il monitoraggio e' editabile, 'false' altrimenti
     */
    public boolean isOpen() {
        return open;
    }

    /**
     * Imposta il flag boolean che specifica se il monitoraggio &egrave; editabile o meno
     * 
     * @param open flag specificante se il monitoraggio deve essere editabile (true) o non modificabile (false)
     */
    public void setOpen(boolean open) {
        this.open = open;
    }
    

    /* ************************************************************* *
     *           Metodi getter e setter per descrizione              *
     * ************************************************************* */
	/**
	 * Restituisce la descrizione di un'attivit&agrave; di monitoraggio
	 * 
	 * @return <code>descrizione</code> - descrizione dell'attivit&agrave; di monitoraggio
	 */
	public String getDescrizione() {
		return descrizione;
	}

	/**
	 * Imposta la descrizione di un'attivit&agrave; di monitoraggio
	 * 
	 * @param descrizione - descrizione da settare
	 */
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
	
	
    /* ************************************************************* *
     *             Metodi getter e setter per quadro D4              *
     * ************************************************************* */
    /**
     * Restituisce la descrizione del quadro D.4 (reclutamento del personale)
     * 
     * @return <code>quadro D.4</code> - descrizione del quadro 4
     */
    public String getQuadroD4() {
        return quadroD4;
    }

    /**
     * Imposta la descrizione del quadro D.4 (reclutamento del personale)
     * 
     * @param quadroD4 - descrizione da settare
     */
    public void setQuadroD4(String quadroD4) {
        this.quadroD4 = quadroD4;
    }

	
    /* ************************************************************* *
     *             Metodi getter e setter per quadro D5              *
     * ************************************************************* */
    /**
     * Restituisce la descrizione del quadro D.5 (infrastrutture)
     * 
     * @return <code>quadro D.5</code> - descrizione del quadro 5
     */
    public String getQuadroD5() {
        return quadroD5;
    }

    /**
     * Imposta la descrizione del quadro D.5 (infrastrutture)
     * 
     * @param quadroD5 - descrizione da settare
     */
    public void setQuadroD5(String quadroD5) {
        this.quadroD5 = quadroD5;
    }

    
    /* ************************************************************* *
     *             Metodi getter e setter per quadro D6              *
     * ************************************************************* */
    /**
     * Restituisce la descrizione del quadro D.6 (premialit&agrave;)
     * 
     * @return <code>quadro D.6</code> - descrizione del quadro 6
     */
    public String getQuadroD6() {
        return quadroD6;
    }

    /**
     * Imposta la descrizione del quadro D.4 (reclutamento del personale)
     * 
     * @param quadroD6 - descrizione da settare
     */
    public void setQuadroD6(String quadroD6) {
        this.quadroD6 = quadroD6;
    }

    
    /* ************************************************************* *
     *             Metodi getter e setter per quadro D7              *
     * ************************************************************* */
    /**
     * Restituisce la descrizione del quadro D.7 (attivit&agrave; didattiche ad elevata qualificazione)
     * 
     * @return <code>quadro D.7</code> - descrizione del quadro 7
     */
    public String getQuadroD7() {
        return quadroD7;
    }

    /**
     * Imposta la descrizione del quadro D.7 (attivit&agrave; didattiche ad elevata qualificazione)
     * 
     * @param quadroD7 - descrizione da settare
     */
    public void setQuadroD7(String quadroD7) {
        this.quadroD7 = quadroD7;
    }

    
    /* ************************************************************* *
     *             Metodi getter e setter per quadro D8              *
     * ************************************************************* */
    /**
     * Restituisce la descrizione del quadro D.8 (modalit&agrave; e fasi del monitoraggio)
     * 
     * @return <code>quadro D.8</code> - descrizione del qaudro 8
     */
    public String getQuadroD8() {
        return quadroD8;
    }

    /**
     * Imposta la descrizione del quadro D.4 (modalit&agrave; e fasi del monitoraggio))
     * 
     * @param quadroD8 - descrizione da settare
     */
    public void setQuadroD8(String quadroD8) {
        this.quadroD8 = quadroD8;
    }

    
    /* ************************************************************* *
     *             Metodi getter e setter per allegati               *
     * ************************************************************* */
    /**
     * Restituisce gli allegati di un'attivit&agrave; di monitoraggio
     * 
     * @return <code>Vector&lt;FileDocBean&gt;</code> - ciascuno rappresentante un riferimento logico ad un allegato fisico all'attivita' di monitoraggio
     */
    public Vector<FileDocBean> getAllegatiD4() {
        return allegatiD4;
    }

    /**
     * Imposta gli allegati di un'attivit&agrave; di monitoraggio
     * 
     * @param allegati - allegati da settare
     */
    public void setAllegatiD4(Vector<FileDocBean> allegati) {
        this.allegatiD4 = allegati;
    }
    
    
    /**
     * Restituisce gli allegati di un'attivit&agrave; di monitoraggio
     * 
     * @return <code>Vector&lt;FileDocBean&gt;</code> - ciascuno rappresentante un riferimento logico ad un allegato fisico all'attivita' di monitoraggio
     */
    public Vector<FileDocBean> getAllegatiD5() {
        return allegatiD5;
    }

    /**
     * Imposta gli allegati di un'attivit&agrave; di monitoraggio
     * 
     * @param allegati - allegati da settare
     */
    public void setAllegatiD5(Vector<FileDocBean> allegati) {
        this.allegatiD5 = allegati;
    }
    
    
    /**
     * Restituisce gli allegati di un'attivit&agrave; di monitoraggio
     * 
     * @return <code>Vector&lt;FileDocBean&gt;</code> - ciascuno rappresentante un riferimento logico ad un allegato fisico all'attivita' di monitoraggio
     */
    public Vector<FileDocBean> getAllegatiD6() {
        return allegatiD6;
    }

    /**
     * Imposta gli allegati di un'attivit&agrave; di monitoraggio
     * 
     * @param allegati - allegati da settare
     */
    public void setAllegatiD6(Vector<FileDocBean> allegati) {
        this.allegatiD6 = allegati;
    }
    
    
    /**
     * Restituisce gli allegati di un'attivit&agrave; di monitoraggio
     * 
     * @return <code>Vector&lt;FileDocBean&gt;</code> - ciascuno rappresentante un riferimento logico ad un allegato fisico all'attivita' di monitoraggio
     */
    public Vector<FileDocBean> getAllegatiD7() {
        return allegatiD7;
    }

    /**
     * Imposta gli allegati di un'attivit&agrave; di monitoraggio
     * 
     * @param allegati - allegati da settare
     */
    public void setAllegatiD7(Vector<FileDocBean> allegati) {
        this.allegatiD7 = allegati;
    }
    
    
    /**
     * Restituisce gli allegati di un'attivit&agrave; di monitoraggio
     * 
     * @return <code>Vector&lt;FileDocBean&gt;</code> - ciascuno rappresentante un riferimento logico ad un allegato fisico all'attivita' di monitoraggio
     */
    public Vector<FileDocBean> getAllegatiD8() {
        return allegatiD8;
    }

    /**
     * Imposta gli allegati di un'attivit&agrave; di monitoraggio
     * 
     * @param allegati - allegati da settare
     */
    public void setAllegatiD8(Vector<FileDocBean> allegati) {
        this.allegatiD8 = allegati;
    }
	
}