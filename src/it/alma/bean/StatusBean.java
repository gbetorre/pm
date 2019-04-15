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
 *   Copyright (C) renewed 2019 Universita' degli Studi di Verona, 
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
/**
 * 
 */

package it.alma.bean;

import java.io.Serializable;
import java.sql.Time;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import it.alma.exception.AttributoNonValorizzatoException;

/**
 * <p>Classe usata per rappresentare lo status di un progetto.</p>
 * 
 * @author <a href="mailto:andrea.tonel@studenti.univr.it">Andrea Tonel</a>
 * @author <a href="mailto:giovanroberto.torre@univr.it">Giovanroberto Torre</a>
 */
public class StatusBean implements Serializable {

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
    /* ******************************************************** *
     *             Dati identificativi dello status             *
     * ******************************************************** */
    /** Attributo identificativo dello status */
    private int id;
    /* ******************************************************** *
     *               Dati descrittivi dello status              *
     * ******************************************************** */
    /** Data inizio dell'attivit&agrave; */
    private Date dataInizio;
    /** Data fine dell'attivit&agrave; */
    private Date dataFine;
    /** Descrizione dell'avanzamento di un progetto */
    private String descrizioneAvanzamento;
    /** Stati dell'avanzamento di un progetto */
    private HashMap<String, CodeBean> stati;
    /* ******************************************************** *
     *          Dati descrittivi dell'ultima modifica           *
     * ******************************************************** */
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
    public StatusBean() {
        id = -2;
        descrizioneAvanzamento = null;
        stati = null;
        dataInizio = dataFine = dataUltimaModifica = new Date(0);
        oraUltimaModifica = null;
        autoreUltimaModifica = null;
        allegati = null;
    }
    
    
    /* **************************************************** *
     *           Metodi getter e setter per id              *
     * **************************************************** */
    /**
     * Restituisce l'id dello status corrente di un progetto
     * 
     * @return <code>int</code> - l'id dello status corrente di un progetto
     * @throws it.alma.exception.AttributoNonValorizzatoException  eccezione che viene sollevata se questo oggetto viene usato e l'id non &egrave; stato valorizzato (&egrave; un dato obbligatorio)
     */
    public int getId() throws AttributoNonValorizzatoException {
        if (id == -2) {
            throw new AttributoNonValorizzatoException(FOR_NAME + "Attributo id non valorizzato!");
        }
        return id;
    }
    
    /**
     * Imposta l'id dello status corrente di un progetto
     * 
     * @param id l'identificativo da impostare
     */
    public void setId(int id) {
        this.id = id;
    }


    /* ************************************************************ *
     *             Metodi getter e setter per dataInizio            *
     * ************************************************************ */
    /**
     * Restituisce la data di inizio dello status corrente di un progetto
     * 
     * @return <code>java.util.Date</code> - data di inizio dello status corrente
     */
    public Date getDataInizio() {
        return dataInizio;
    }

    /**
     * Imposta la data di inizio dello status corrente di un progetto
     * 
     * @param dataInizio data di inizio da impostare
     */
    public void setDataInizio(Date dataInizio) {
        this.dataInizio = dataInizio;
    }


    /* ************************************************************ *
     *              Metodi getter e setter per dataFine             *
     * ************************************************************ */
    /**
     * Restituisce la data di fine dello status corrente di un progetto
     * 
     * @return <code>java.util.Date</code> - data di fine dello status corrente
     */
    public Date getDataFine() {
        return dataFine;
    }

    /**
     * Imposta la data di fine dello status corrente di un progetto
     * 
     * @param dataFine data di fine da impostare
     */
    public void setDataFine(Date dataFine) {
        this.dataFine = dataFine;
    }

    
    /* ****************************************************************** *
     *          Metodi getter e setter per descrizioneAvanzamento         *
     * ****************************************************************** */
    /**
     * Restituisce la descrizione dell'avanzamento del progetto
     * 
     * @return <code>String</code> - descrizione avanzamento dello status corrente
     */
    public String getDescrizioneAvanzamento() {
        return descrizioneAvanzamento;
    }

    /**
     * Imposta la descrizione dello status corrente di un progetto
     * 
     * @param descrizioneAvanzamento descrizione dell'avanzamento da impostare
     */
    public void setDescrizioneAvanzamento(String descrizioneAvanzamento) {
        this.descrizioneAvanzamento = descrizioneAvanzamento;
    }


    /* ***************************************************** *
     *          Metodi getter e setter per stati             *
     * ***************************************************** */
    /**
     * Restituisce gli stati dell'avanzamento
     * 
     * @return <code>HashMap&lt;String, CodeBean&gt;</code> - stati di un avanzamento progetto
     * @throws AttributoNonValorizzatoException eccezione che viene sollevata se questo oggetto viene usato e stati non &egrave; stato valorizzato (&egrave; un dato obbligatorio)
     */
    public HashMap<String, CodeBean> getStati() throws AttributoNonValorizzatoException {
        if (stati == null) {
            throw new AttributoNonValorizzatoException(FOR_NAME + "Attributo statoStakeholder non valorizzato!");
        }
        return stati;
    }

    /**
     * Imposta gli stati dell'avanzamento 
     * 
     * @param stati - stati da impostare
     */
    public void setStati(HashMap<String, CodeBean> stati) {
        this.stati = stati;
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

    
    /* ************************************************************** *
     *              Metodi getter e setter per allegati               *
     * ************************************************************** */
    /**
     * Restituisce l'elenco dei riferimenti logici agli allegati fisici
     * in precedenza caricati per lo status di progetto corrente 
     * 
     * @return <code>Vector&lt;FileDocBean&gt;</code> - elenco di riferimenti logici ad allegati fisici
     */
    public Vector<FileDocBean> getAllegati() {
        return allegati;
    }

    /**
     * Imposta l'elenco dei riferimenti logici agli allegati fisici
     * caricati per lo status di progetto corrente
     * 
     * @param allegati Vector di FileDocBean da impostare
     */
    public void setAllegati(Vector<FileDocBean> allegati) {
        this.allegati = allegati;
    }
    
}