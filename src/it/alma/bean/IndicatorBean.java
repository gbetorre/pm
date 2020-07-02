/*
 *   Alma on Line: Applicazione WEB per la visualizzazione 
 *   delle schede di indagine su popolazione dell'ateneo,
 *   della gestione dei progetti on line (POL).
 *   
 *   Copyright (C) 2020 Giovanroberto Torre<br />
 *   Alma on Line (aol), Projects on Line (pol);
 *   web applications to publish, and manage, projects according to the
 *   Project Management Paradigm.
 *   Copyright (C) renewed 2020 Universita' degli Studi di Verona, 
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
 * <p>Classe usata per rappresentare un indicatore di progetto.</p>
 * 
 * @author <a href="mailto:giovanroberto.torre@univr.it">Giovanroberto Torre</a>
 */
public class IndicatorBean extends CodeBean implements Serializable {

	/**
     * La serializzazione necessita di dichiarare una costante di tipo long
     * identificativa della versione seriale. 
     * (Se questo dato non fosse inserito, verrebbe calcolato in maniera automatica
     * dalla JVM, e questo potrebbe portare a errori riguardo alla serializzazione). 
	 */
    private static final long serialVersionUID = -8193133105503064353L;
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
	/** Descrizione dell'indicatore */
    private String descrizione;
    /** Descrizione della baseline dell'indicatore (risultati attuali in questo indicatore) */
    private String baseline;
    /** Anno di contestualizzazione del valore baseline (anno in cui i risultati baseline sono attuali)  */
    private String annoBaseline;
    /** Data di contestualizzazione del valore baseline (data in cui i risultati baseline sono attuali)  */
    private Date dataBaseline;    
    /** Descrizione del target dell'indicatore (risultati attesi in questo indicatore) */
    private String target;
    /** Anno di contestualizzazione del target puntato (anno in cui i risultati target sono attesi) */
    private String annoTarget;
    /** Data di contestualizzazione del target puntato (data in cui i risultati target sono attesi) */
    private Date dataTarget;
    /** Attributo identificativo dell'azione (WBS) associata all'indicatore corrente */
    private WbsBean wbs;
    /* ******************************************************** *
     *          Dati descrittivi dell'ultima modifica           *
     * ******************************************************** */
    /** Data ultima modifica */
    private Date dataUltimaModifica;
    /** Ora ultima modifica */
    private Time oraUltimaModifica;
    /** Autore ultima modifica */
    private String autoreUltimaModifica;
    /* ******************************************************** *
     *               Attributi di tipo e di stato               *
     * ******************************************************** */
    /** Identificativo del tipo dell'indicatore */
    private int idTipo;
    /** Tipo dell'indicatore */
    private CodeBean tipo;
    /** Id dello stato indicatore */
    private int idStato;
    /** Stato indicatore */
    private CodeBean stato;
    /* ******************************************************** *
     *          Attributi dipendenti da altre entita'           *
     * ******************************************************** */ 
    /** Numero di misurazioni presenti per l'indicatore */
    private int totMisurazioni;
    /** Elenco di misurazioni presenti per l'indicatore */
    private Vector<MeasurementBean> misurazioni;
    
	
    /**
     * <p>Costruttore: inizializza i campi a valori di default.</p>
     */
	public IndicatorBean() {
	    super();
		descrizione = null;
		baseline = null;
		annoBaseline = null;
		dataBaseline = dataTarget = new Date(0);
		target = null;
		annoTarget = null;
		wbs = null;
		dataUltimaModifica = new Date(0);
        oraUltimaModifica = null;
        autoreUltimaModifica = null;
        setIdTipo(-2);
        tipo = null;
        idStato = -2;
        stato = null;
        totMisurazioni = -2;
        misurazioni = null;
	}


	/* ********************************************************* *
     *         Metodi getter e setter per descrizione            *
     * ********************************************************* */
	/**
	 * Restituisce la descrizione di un indicatore 
	 * @return <code>descrizione</code> - descrizione dell'indicatore
	 */
	public String getDescrizione() {
		return descrizione;
	}

	/**
	 * Imposta la descrizione di un indicatore
	 * @param descrizione - descrizione da settare
	 */
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

    
    /* ********************************************************* *
     *  Metodi getter e setter per baseline (risultati attuali)  *
     * ********************************************************* */
    /**
     * Restituisce la descrizione della baseline relativa all'anno
     * in cui viene definito l'indicatore (v. annoBaseline)
     * 
     * @return <code>baseline</code> - baseline dell'indicatore
     * @throws it.alma.exception.AttributoNonValorizzatoException  eccezione che viene sollevata se questo oggetto viene usato e questo attributo non &egrave; stato valorizzato (potrebbe essere un dato obbligatorio)
     */
    public String getBaseline() throws AttributoNonValorizzatoException {
        if (baseline == null) {
            throw new AttributoNonValorizzatoException(FOR_NAME + "Attributo baseline non valorizzato!");
        }
        return baseline;
    }

    /**
     * Imposta la baseline di un indicatore
     * 
     * @param baseline - valore da impostare
     */
    public void setBaseline(String baseline) {
        this.baseline = baseline;
    }

    
    /* ********************************************************* *
     *    Metodi getter e setter per target (risultati attesi)   *
     * ********************************************************* */
    /**
     * Restituisce i risultati attesi per l'indicatore
     * 
     * @return <code>String</code> - risultati attesi per l'indicatore
     * @throws it.alma.exception.AttributoNonValorizzatoException  eccezione che viene sollevata se questo oggetto viene usato e questo attributo non &egrave; stato valorizzato (potrebbe essere un dato obbligatorio)
     */
    public String getTarget() throws AttributoNonValorizzatoException {
        if (target == null) {
            throw new AttributoNonValorizzatoException(FOR_NAME + "Attributo target non valorizzato!");
        }
        return target;
    }

    /**
     * Imposta i risultati attesi per l'indicatore
     * 
     * @param target i risultati attesi nell'indicatore, da impostare
     */
    public void setTarget(String target) {
        this.target = target;
    }
    
    
    /* ***************************************************************** *
     *    Metodi getter e setter per data baseline (risultati attuali)   *
     * ***************************************************************** */
    /**
     * Restituisce la data a cui i risultati attuali fanno riferimento
     * 
     * @return <code>Date</code> - data Baseline
     * @throws it.alma.exception.AttributoNonValorizzatoException  eccezione che viene sollevata se questo oggetto viene usato e questo attributo non &egrave; stato valorizzato (potrebbe essere un dato obbligatorio)
     */
    public Date getDataBaseline() throws AttributoNonValorizzatoException {
        if (new Date(0).equals(dataBaseline)) {
            throw new AttributoNonValorizzatoException(FOR_NAME + "Attributo data baseline non valorizzato! A quale data fa riferimento la baseline? Specificare una data.");
        }
        return dataBaseline;
    }

    /**
     * @param dataBaseline data baseline da impostare
     */
    public void setDataBaseline(Date dataBaseline) {
        this.dataBaseline = dataBaseline;
    }

   
    /* ***************************************************************** *
     *     Metodi getter e setter per data target (risultati attesi)     *
     * ***************************************************************** */
    /**
     * Restituisce l'anno a cui i risultati attuali fanno riferimento
     * 
     * @return <code>Date</code> - data Target
     * @throws it.alma.exception.AttributoNonValorizzatoException  eccezione che viene sollevata se questo oggetto viene usato e questo attributo non &egrave; stato valorizzato (potrebbe essere un dato obbligatorio)
     */
    public Date getDataTarget() throws AttributoNonValorizzatoException {
        if (new Date(0).equals(dataTarget)) {
            throw new AttributoNonValorizzatoException(FOR_NAME + "Attributo data target non valorizzato! A quale data fa riferimento il target? Specificarne una.");
        }
        return dataTarget;
    }

    /**
     * @param dataTarget data target da impostare
     */
    public void setDataTarget(Date dataTarget) {
        this.dataTarget = dataTarget;
    }

    
    /* ***************************************************************** *
     *    Metodi getter e setter per anno baseline (risultati attuali)   *
     * ***************************************************************** */
    /**
     * Restituisce l'anno a cui i risultati attuali fanno riferimento
     * 
     * @return <code>String</code> - annoBaseline
     * @throws it.alma.exception.AttributoNonValorizzatoException  eccezione che viene sollevata se questo oggetto viene usato e questo attributo non &egrave; stato valorizzato (potrebbe essere un dato obbligatorio)
     */
    public String getAnnoBaseline() throws AttributoNonValorizzatoException {
        if (annoBaseline == null) {
            throw new AttributoNonValorizzatoException(FOR_NAME + "Attributo anno baseline non valorizzato! A quale anno fa riferimento la baseline? Specificare un valore intero.");
        }
        return annoBaseline;
    }

    /**
     * @param annoBaseline l'anno baseline da impostare
     */
    public void setAnnoBaseline(String annoBaseline) {
        this.annoBaseline = annoBaseline;
    }

   
    /* ***************************************************************** *
     *      Metodi getter e setter per anno target (risultati attesi)    *
     * ***************************************************************** */
    /**
     * Restituisce l'anno a cui i risultati attuali fanno riferimento
     * 
     * @return <code>String</code> - annoTarget
     * @throws it.alma.exception.AttributoNonValorizzatoException  eccezione che viene sollevata se questo oggetto viene usato e questo attributo non &egrave; stato valorizzato (potrebbe essere un dato obbligatorio)
     */
    public String getAnnoTarget() throws AttributoNonValorizzatoException {
        if (annoTarget == null) {
            throw new AttributoNonValorizzatoException(FOR_NAME + "Attributo anno target non valorizzato! A quale anno fa riferimento il target? Specificarne uno.");
        }
        return annoTarget;
    }

    /**
     * @param annoTarget l'anno target da impostare
     */
    public void setAnnoTarget(String annoTarget) {
        this.annoTarget = annoTarget;
    }
    
    
    /* ********************************************************************* *
     *    Metodi getter e setter per wbs misurata dall'indicatore corrente   *
     * ********************************************************************* */
    /**
     * Restituisce il bean rappresentante la WBS collegata all'indicatore corrente.
     * 
     * @return <code>WbsBean</code> - WBS misurata con l'indicatore corrente
     */
    public WbsBean getWbs() {
        return wbs;
    }

    /**
     * Imposta il bean rappresentante la WBS misurata dall'indicatore corrente.
     * 
     * @param wbs - WBS collegata, cioe' sulla quale l'indicatore corrente e' stato usato, da impostare
     */
    public void setWbs(WbsBean wbs) {
        this.wbs = wbs;
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
     *    Metodi getter e setter per identificativo tipo indicatore   *
     * ************************************************************** */
    /**
     * Restituisce l'identificativo del tipo dell'indicatore, valore che 
     * permette la decodifica del tipo nominale ("On/Off", "Quantitativo",
     * "Percentuale", etc.) o solleva un'
     * eccezione se questo attibuto non viene riscontrato avere un valore
     * significativo
     * 
     * @return <code>int</code> - identificativo numerico del tipo indicatore
     * @throws it.alma.exception.AttributoNonValorizzatoException  eccezione che viene sollevata se questo oggetto viene usato e questo attributo non &egrave; stato valorizzato (potrebbe essere un dato obbligatorio)
     */
    public int getIdTipo() throws AttributoNonValorizzatoException {
        if (idTipo == -2) {
            throw new AttributoNonValorizzatoException(FOR_NAME + "Attributo id tipo indicatore non valorizzato! A quale id tipo fa riferimento l\'indicatore? Specificarne uno.");
        }
        return idTipo;
    }

    /**
     * Imposta l'identificativo del tipo dell'indicatore
     * 
     * @param idTipo il tipo indicatore da impostare
     */
    public void setIdTipo(int idTipo) {
        this.idTipo = idTipo;
    }
    
    
    /* ************************************************************** *
     *           Metodi getter e setter per tipo indicatore           *
     * ************************************************************** */
    /**
     * Restituisce il tipo dell'indicatore
     * 
     * @return <code>CodeBean</code> - oggetto per il tipo indicatore
     * @throws it.alma.exception.AttributoNonValorizzatoException  eccezione che viene sollevata se questo oggetto viene usato e questo attributo non &egrave; stato valorizzato (potrebbe essere un dato obbligatorio)
     */
    public CodeBean getTipo() throws AttributoNonValorizzatoException {
        if (tipo == null) {
            throw new AttributoNonValorizzatoException(FOR_NAME + "Attributo tipo indicatore non valorizzato! Qual e\' il tipo dell\'indicatore? Specificarne uno.");
        }
        return tipo;
    }

    /**
     * Imposta il tipo dell'indicatore
     * 
     * @param tipo il tipo indicatore da impostare
     */
    public void setTipo(CodeBean tipo) {
        this.tipo = tipo;
    }
    
    
    /* ************************************************************** *
     *         Metodi getter e setter per identificativo stato        *
     * ************************************************************** */
    /**
     * Restituisce l'identificativo dello stato dell'indicatore, valore che 
     * permette la decodifica dello stato nominale ("Aperto", "In Progress",
     * "Concluso", etc.) o solleva un'eccezione 
     * se questo attibuto non viene riscontrato avere un valore significativo
     * 
     * @return <code>int</code> - identificativo numerico dell'id dello stato indicatore
     * @throws it.alma.exception.AttributoNonValorizzatoException  eccezione che viene sollevata se questo oggetto viene usato e questo attributo non &egrave; stato valorizzato (potrebbe essere un dato obbligatorio)
     */
    public int getIdStato() throws AttributoNonValorizzatoException {
        if (idStato == -2) {
            throw new AttributoNonValorizzatoException(FOR_NAME + "Attributo id stato indicatore non valorizzato! A quale id stato fa riferimento l\'indicatore? Specificarne uno.");
        }
        return idStato;
    }

    /**
     * Imposta l'identificativo dello stato dell'indicatore
     * 
     * @param idStato id dello stato indicatore da impostare
     */
    public void setIdStato(int idStato) {
        this.idStato = idStato;
    }
    
    
    /* ************************************************************** *
     *           Metodi getter e setter per stato indicatore          *
     * ************************************************************** */
    /**
     * Restituisce lo stato dell'indicatore
     * 
     * @return <code>CodeBean</code> - oggetto per lo stato dell'indicatore
     * @throws it.alma.exception.AttributoNonValorizzatoException  eccezione che viene sollevata se questo oggetto viene usato e questo attributo non &egrave; stato valorizzato (potrebbe essere un dato obbligatorio)
     */
    public CodeBean getStato() throws AttributoNonValorizzatoException {
        if (stato == null) {
            throw new AttributoNonValorizzatoException(FOR_NAME + "Attributo stato indicatore non valorizzato! Qual e\' lo stato dell\'indicatore? Specificarne uno!");
        }
        return stato;
    }

    /**
     * Imposta lo stato dell'indicatore
     * 
     * @param stato lo stato indicatore da impostare
     */
    public void setStato(CodeBean stato) {
        this.stato = stato;
    }
    
    
    /* ************************************************************************* *
     * Metodi getter e setter per numero di misurazioni presenti sull'indicatore *
     * ************************************************************************* */
    /**
     * Restituisce il numero di misurazioni eventualmente presenti 
     * relative all'indicatore, o il valore di default altrimenti.
     * L'attributo &egrave; obbligatorio non perch&eacute; sia obbligatorio
     * che per un dato indicatore siano presenti misurazioni, ma perch&eacute;
     * deve essere sempre possibile stabilire, quanto meno, se per quell'
     * indicatore sono o meno presenti misurazioni "at all".
     * 
     * @return <code>int</code> - numero di misurazioni trovate sull'indicatore
     * @throws it.alma.exception.AttributoNonValorizzatoException  eccezione che viene sollevata se questo oggetto viene usato e questo attributo non &egrave; stato valorizzato (potrebbe essere un dato obbligatorio)
     */
    public int getTotMisurazioni() throws AttributoNonValorizzatoException {
        if (totMisurazioni == -2) {
            throw new AttributoNonValorizzatoException(FOR_NAME + "Attributo numero misurazioni indicatore non valorizzato! Ci sono o non ci sono misurazioni su questo indicatore? Questo deve essere un attributo sempre decidibile.");
        }
        return totMisurazioni;
    }

    /**
     * Imposta il numero di misurazioni trovate per l'indicatore
     * 
     * @param totMisurazioni il numero di misurazioni da impostare
     */
    public void setTotMisurazioni(int totMisurazioni) {
        this.totMisurazioni = totMisurazioni;
    }

    
    /* ********************************************************** *
     *          Metodi getter e setter per misurazioni            *
     * ********************************************************** */
    /**
     * Restituisce lista delle misurazioni che hanno rilevato i valori assunti da questo indicatore.
     * @return <code>Vector&lt;MeasurementBean&gt;</code> - elenco delle misurazioni che si riferiscono all'indicatore 
     */
    public Vector<MeasurementBean> getMisurazioni() {
        return misurazioni;
    }

    /**
     * Imposta la lista delle misurazioni che fanno riferimento all'indicatore.
     * @param misurazioni - elenco misurazioni da impostare
     */
    public void setMisurazioni(Vector<MeasurementBean> misurazioni) {
        this.misurazioni = misurazioni;
    }

}