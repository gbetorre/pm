/*
 *   Alma on Line: 
 *   Applicazione WEB per la gestione dei progetti on line (POL)
 *   coerentemente con le linee-guida del project management,
 *   e per la visualizzazione delle schede di indagine 
 *   su popolazione dell'ateneo.
 *   
 *   Copyright (C) 2018-2020 Giovanroberto Torre<br />
 *   Alma on Line (aol), Projects on Line (pol);
 *   web applications to publish, and manage, projects
 *   according to the Project Management paradigm (PM).
 *   Copyright (C) renewed 2020 Giovanroberto Torre, 
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
 * <p>Classe usata per rappresentare la misurazione di un indicatore di progetto.</p>
 * 
 * @author <a href="mailto:giovanroberto.torre@univr.it">Giovanroberto Torre</a>
 */
public class MeasurementBean extends CodeBean implements Serializable {

    /**
     * La serializzazione necessita di dichiarare una costante di tipo long
     * identificativa della versione seriale. 
     * (Se questo dato non fosse inserito, verrebbe calcolato in maniera automatica
     * dalla JVM, e questo potrebbe portare a errori riguardo alla serializzazione). 
	 */
    private static final long serialVersionUID = 7375214565768143951L;
    /**
     *  Nome di questa classe. 
     *  Viene utilizzato per contestualizzare i messaggi di errore.
     */
    private final String FOR_NAME = "\n" + this.getClass().getName() + ": "; //$NON-NLS-1$
    /* ************************************************************************ *  
     *                   Dati identificativi dell'indicatore                    *
     * ************************************************************************ */
    // Inherited from his parent
    /* ************************************************************************ *  
     *                    Dati descrittivi dell'indicatore                      *
     * ************************************************************************ */
	/** Descrizione della misurazione */
    private String descrizione;
    /** Data di contestualizzazione della misurazione dell'indicatore  */
    private Date dataMisurazione;
    /** Flag di ultima misurazione: permette assunzioni conseguenti */
    private boolean ultimo;
    /** Attributo identificativo dell'indicatore misurato dalla misurazione corrente */
    private IndicatorBean indicatore;
    /** Data ultima modifica */
    private Date dataUltimaModifica;
    /** Ora ultima modifica */
    private Time oraUltimaModifica;
    /** Autore ultima modifica */
    private String autoreUltimaModifica;
    /* *******************************************************  *
     *                         Allegati                         *
     * ******************************************************** */
    /**
     * Vector di fileset, ciascuno rappresentante 
     * un riferimento logico ad un allegato fisico
     */
    private Vector<FileDocBean> allegati;
    
	
    /**
     * <p>Costruttore: inizializza i campi a valori di default.</p>
     */
	public MeasurementBean() {
	    super();
		descrizione = null;
		dataMisurazione = new Date(0);
		ultimo = false;
		indicatore = null;
        dataUltimaModifica = new Date(0);
        oraUltimaModifica = null;
        autoreUltimaModifica = null;
        allegati = null;
	}


	/* ********************************************************* *
     *         Metodi getter e setter per descrizione            *
     * ********************************************************* */
	/**
	 * Restituisce la descrizione di una misurazione  
	 * @return <code>descrizione</code> - descrizione della misurazione
	 */
	public String getDescrizione() {
		return descrizione;
	}

	/**
	 * Imposta la descrizione di una misurazione
	 * @param descrizione - descrizione da settare
	 */
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
    
    
    /* ***************************************************************** *
     *  Metodi getter e setter per data misurazione (risultato attuale)  *
     * ***************************************************************** */
    /**
     * Restituisce la data di effettuazione della misurazione
     * 
     * @return <code>Date</code> - data misurazione
     * @throws it.alma.exception.AttributoNonValorizzatoException  eccezione che viene sollevata se questo oggetto viene usato e questo attributo non &egrave; stato valorizzato (dovrebbe essere un dato obbligatorio)
     */
    public Date getDataMisurazione() throws AttributoNonValorizzatoException {
        if (new Date(0).equals(dataMisurazione)) {
            throw new AttributoNonValorizzatoException(FOR_NAME + "Attributo data non valorizzato! A quale data fa riferimento la misurazione? Specificare una data.");
        }
        return dataMisurazione;
    }

    /**
     * @param dataMisurazione data misurazione da impostare
     */
    public void setDataMisurazione(Date dataMisurazione) {
        this.dataMisurazione = dataMisurazione;
    }
    
    
    /* ********************************************************* *
     *      Metodi getter e setter per ultima misurazione        *
     * ********************************************************* */
    /**
     * Restituisce true se la misurazione &egrave; l'ultima - false (default) se non lo &egrave;
     * 
     * @return <code>ultimo</code> - true/false misurazione ultima
     */
    public boolean isUltimo() {
        return ultimo;
    }

    /**
     * Imposta ultima misurazione
     *  
     * @param ultimo - ultima misurazione true/false da impostare
     */
    public void setUltimo(boolean ultimo) {
        this.ultimo = ultimo;
    }
    
    
    /* ********************************************************************* *
     * Metodi getter e setter per indicatore misurato dalla misura corrente  *
     * ********************************************************************* */
    /**
     * Restituisce il bean rappresentante l'indicatore collegato alla misurazione corrente.
     * 
     * @return <code>IndicatorBean</code> - Indicatore misurato con la misurazione corrente
     */
    public IndicatorBean getIndicatore() {
        return indicatore;
    }

    /**
     * Imposta il bean rappresentante l'indicatore misurato.
     * 
     * @param indicatore - IndicatorBean collegato, da impostare
     */
    public void setIndicatore(IndicatorBean indicatore) {
        this.indicatore = indicatore;
    }
    

    /* *********************************************************** *
     *       Metodi getter e setter per data ultima modifica       *
     * *********************************************************** */
    /**
     * Restituisce la data dell'ultima modifica 
     * 
     * @return <code>java.util.Date</code> - data dell'ultima modifica
     */
    public Date getDataUltimaModifica() {
        return dataUltimaModifica;
    }

    /**
     * Imposta la data dell'ultima modifica
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
     * Restituisce l'ora dell'ultima modifica 
     * 
     * @return <code>java.sql.Time</code> - ora dell'ultima modifica
     */
    public Time getOraUltimaModifica() {
        return oraUltimaModifica;
    }
    
    /**
     * Imposta l'ora dell'ultima modifica 
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
     * Restituisce l'autore dell'ultima modifica 
     * 
     * @return <code>String</code> - autore ultima modifica
     */
    public String getAutoreUltimaModifica() {
        return autoreUltimaModifica;
    }

    /**
     * Imposta l'autore dell'ultima modifica 
     * 
     * @param autoreUltimaModifica autore ultima modifica da impostare
     */
    public void setAutoreUltimaModifica(String autoreUltimaModifica) {
        this.autoreUltimaModifica = autoreUltimaModifica;
    }
    
    
    /* ************************************************************** *
     *              Metodi getter e setter per allegati               *
     * ************************************************************** */
    /**
     * Restituisce l'elenco dei riferimenti logici agli allegati fisici
     * in precedenza caricati per la misurazione corrente 
     * 
     * @return <code>Vector&lt;FileDocBean&gt;</code> - elenco di riferimenti logici ad allegati fisici
     */
    public Vector<FileDocBean> getAllegati() {
        return allegati;
    }

    /**
     * Imposta l'elenco dei riferimenti logici agli allegati fisici
     * caricati per la misurazione corrente
     * 
     * @param allegati Vector di FileDocBean da impostare
     */
    public void setAllegati(Vector<FileDocBean> allegati) {
        this.allegati = allegati;
    }

}