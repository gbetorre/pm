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
/**
 * 
 */

package it.alma.bean;

import java.io.Serializable;
import java.util.Date;

import it.alma.Query;
import it.alma.exception.AttributoNonValorizzatoException;

/**
 * <p>Classe usata per rappresentare lo status di un progetto.</p>
 * 
 * @author <a href="mailto:andrea.tonel@studenti.univr.it">Andrea Tonel</a>
 */
public class StatusBean implements Serializable, Query {

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
    /* ************************************************************************ *  
     *                    Dati identificativi dello status                      *
     * ************************************************************************ */
    /** Attributo identificativo dello status */
    private int id;
    /* ************************************************************ *
     *                 Dati descrittivi dello status                *
     * ************************************************************ */
    /** Data inizio dell'attivit&agrave; */
    private Date dataInizio;
    /** Data fine dell'attivit&agrave; */
    private Date dataFine;
    /** Descrizione dell'avanzamento di un progetto */
    private String descrizioneAvanzamento;
    /** Stato tempi del progetto */
    private String statoTempi;
    /** Stato costi del progetto */
    private String statoCosti;
    /** Stato rischi del progetto */
    private String statoRischi;
    /** Stato risorse del progetto */
    private String statoRisorse;
    /** Stato scope del progetto */
    private String statoScope;
    /** Stato comunicazione del progetto */
    private String statoComunicazione;
    /** Stato qualita del progetto */
    private String statoQualita;
    /** Stato approvvigionamenti del progetto */
    private String statoApprovvigionamenti;
    /** Stato stakeholder del progetto */
    private String statoStakeholder;
    /* ******************************************************* *
     *          Dati descrittivi dell'ultima modifica          *
     * ******************************************************* */
    /** Data ultima modifica */
    private Date dataUltimaModifica;
    /** Ora ultima modifica */
    private String oraUltimaModifica;
    /** Autore ultima modifica */
    private String autoreUltimaModifica;
    

    /**
     * <p>Costruttore: inizializza i campi a valori di default.</p>
     */
    public StatusBean() {
        id = -2;
        descrizioneAvanzamento = null;
        statoTempi =statoCosti = statoRischi = statoRisorse = statoScope = statoComunicazione = statoQualita = statoApprovvigionamenti = statoStakeholder = null;
        dataInizio = dataFine = dataUltimaModifica = new Date(0);
        oraUltimaModifica = autoreUltimaModifica = null;        
    };
    
    
    /* **************************************************** *
     *           Metodi getter e setter per id              *
     * **************************************************** */
    /**
     * Restituisce l'id dello status corrente di un progetto
     * 
     * @return <code>id</code> - l'id dello status corrente di un progetto
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
     * @return <code>dataInizio</code> - data di inizio dello status corrente
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
     * @return <code>dataFine</code> - data di fine dello status corrente
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
     * @return <code>descrizioneAvanzamento</code> - descrizione avanzamento dello status corrente
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


    /* **************************************************** *
     *           Metodi getter e setter per stato tempi     *
     * **************************************************** */
    /**
     * Restituisce lo stato tempi del progetto.
     * 
     * @return <code>statoTempi</code> - stato dei tempi
     * @throws it.alma.exception.AttributoNonValorizzatoException  eccezione che viene sollevata se questo oggetto viene usato e statoTempi non &egrave; stato valorizzato (&egrave; un dato obbligatorio) 
     * oppure statoTempi non &egrave; stato valorizzato correttamente
     */
    public String getStatoTempi() throws AttributoNonValorizzatoException {
        if (statoTempi == null) {
            throw new AttributoNonValorizzatoException(FOR_NAME + "Attributo statoTempi non valorizzato!");
        }
        else if (!STATI_AS_LIST.contains(statoTempi)) {
            throw new AttributoNonValorizzatoException(FOR_NAME + "Attributo statoTempi non valorizzato correttamente!");
        }
        else {
            return statoTempi;
        }
    }

    /**
     * Imposta lo stato tempi del progetto.
     * 
     * @param statoTempi lo stato tempi da impostare
     */
    public void setStatoTempi(String statoTempi) {
        this.statoTempi = statoTempi;
    }


    /* **************************************************** *
     *           Metodi getter e setter per stato costi     *
     * **************************************************** */
    /**
     * Restituisce lo stato costi del progetto.
     * 
     * @return <code>statoCosti</code> - stato dei costi
     * @throws it.alma.exception.AttributoNonValorizzatoException  eccezione che viene sollevata se questo oggetto viene usato e statoCosti non &egrave; stato valorizzato (&egrave; un dato obbligatorio) 
     * oppure statoCosti non &egrave; stato valorizzato correttamente
     */
    public String getStatoCosti() throws AttributoNonValorizzatoException {
        if (statoCosti == null) {
            throw new AttributoNonValorizzatoException(FOR_NAME + "Attributo statoCosti non valorizzato!");
        }
        else if (!STATI_AS_LIST.contains(statoCosti)) {
            throw new AttributoNonValorizzatoException(FOR_NAME + "Attributo statoCosti non valorizzato correttamente!");
        }
        else {
            return statoCosti;
        }
    }

    /**
     * Imposta lo stato costi del progetto.
     * 
     * @param statoCosti lo stato costi da impostare
     */
    public void setStatoCosti(String statoCosti) {
        this.statoCosti = statoCosti;
    }


    /* **************************************************** *
     *           Metodi getter e setter per stato rischi    *
     * **************************************************** */
    /**
     * Restituisce lo stato rischi del progetto.
     * 
     * @return <code>statoRischi</code> - stato dei rischi
     * @throws it.alma.exception.AttributoNonValorizzatoException  eccezione che viene sollevata se questo oggetto viene usato e statoRischi non &egrave; stato valorizzato (&egrave; un dato obbligatorio) 
     * oppure statoRischi non &egrave; stato valorizzato correttamente
     */
    public String getStatoRischi() throws AttributoNonValorizzatoException {
        if (statoRischi == null) {
            throw new AttributoNonValorizzatoException(FOR_NAME + "Attributo statoRischi non valorizzato!");
        }
        else if (!STATI_AS_LIST.contains(statoRischi)) {
            throw new AttributoNonValorizzatoException(FOR_NAME + "Attributo statoRischi non valorizzato correttamente!");
        }
        else {
            return statoRischi;
        }
    }

    /**
     * Imposta lo stato rischi del progetto
     * 
     * @param statoRischi lo stato rischi da impostare
     */
    public void setStatoRischi(String statoRischi) {
        this.statoRischi = statoRischi;
    }


    /* **************************************************** *
     *           Metodi getter e setter per stato risorse   *
     * **************************************************** */
    /**
     * Restituisce lo stato risorse del progetto
     * 
     * @return <code>statoRisorse</code> - stato delle risorse
     * @throws it.alma.exception.AttributoNonValorizzatoException  eccezione che viene sollevata se questo oggetto viene usato e statoRisorse non &egrave; stato valorizzato (&egrave; un dato obbligatorio) 
     * oppure statoRisorse non &egrave; stato valorizzato correttamente
     */
    public String getStatoRisorse() throws AttributoNonValorizzatoException {
        if (statoRisorse == null) {
            throw new AttributoNonValorizzatoException(FOR_NAME + "Attributo statoRisorse non valorizzato!");
        }
        else if (!STATI_AS_LIST.contains(statoRisorse)) {
            throw new AttributoNonValorizzatoException(FOR_NAME + "Attributo statoRisorse non valorizzato correttamente!");
        }
        else {
            return statoRisorse;
        }
    }

    /**
     * Imposta lo stato delle risorse del progetto
     * 
     * @param statoRisorse  lo stato delle risorse da impostare
     */
    public void setStatoRisorse(String statoRisorse) {
        this.statoRisorse = statoRisorse;
    }


    /* **************************************************** *
     *           Metodi getter e setter per stato scope     *
     * **************************************************** */
    /**
     * Restituisce lo stato scope del progetto
     * 
     * @return <code>statoScope</code> -  stato scope
     * @throws it.alma.exception.AttributoNonValorizzatoException  eccezione che viene sollevata se questo oggetto viene usato e statoScope non &egrave; stato valorizzato (&egrave; un dato obbligatorio) 
     * oppure statoScope non &egrave; stato valorizzato correttamente
     */
    public String getStatoScope() throws AttributoNonValorizzatoException {
        if (statoScope == null) {
            throw new AttributoNonValorizzatoException(FOR_NAME + "Attributo statoScope non valorizzato!");
        }
        else if (!STATI_AS_LIST.contains(statoScope)) {
            throw new AttributoNonValorizzatoException(FOR_NAME + "Attributo statoScope non valorizzato correttamente!");
        }
        else {
            return statoScope;
        }
    }

    /**
     * Imposta lo stato scope del progetto
     * 
     * @param statoScope lo stato scope da impostare
     */
    public void setStatoScope(String statoScope) {
        this.statoScope = statoScope;
    }


    /* **************************************************** *
     *    Metodi getter e setter per stato comunicazione    *
     * **************************************************** */
    /**
     * Restituisce lo stato comunicazione del progetto
     * 
     * @return <code>statoComunicazione</code> - stato della comunicazione
     * @throws it.alma.exception.AttributoNonValorizzatoException  eccezione che viene sollevata se questo oggetto viene usato e statoComunicazione non &egrave; stato valorizzato (&egrave; un dato obbligatorio) 
     * oppure statoComunicazione non &egrave; stato valorizzato correttamente
     */
    public String getStatoComunicazione() throws AttributoNonValorizzatoException {
        if (statoComunicazione == null) {
            throw new AttributoNonValorizzatoException(FOR_NAME + "Attributo statoComunicazione non valorizzato!");
        }
        else if (!STATI_AS_LIST.contains(statoComunicazione)) {
            throw new AttributoNonValorizzatoException(FOR_NAME + "Attributo statoComunicazione non valorizzato correttamente!");
        }
        else {
            return statoComunicazione;
        }
    }

    /**
     * Imposta lo stato comunicazione del progetto
     * 
     * @param statoComunicazione stato della comunicazione da impostare
     */
    public void setStatoComunicazione(String statoComunicazione) {
        this.statoComunicazione = statoComunicazione;
    }


    /* **************************************************** *
     *    Metodi getter e setter per stato qualit&agrave;   *
     * **************************************************** */
    /**
     * Restituisce lo stato qualit&agrave; del progetto
     * 
     * @return <code>statoQualita</code> - stato della qualit&agrave
     * @throws it.alma.exception.AttributoNonValorizzatoException  eccezione che viene sollevata se questo oggetto viene usato e statoQualit&agrave; non &egrave; stato valorizzato (&egrave; un dato obbligatorio) 
     * oppure statoQualit&agrave; non &egrave; stato valorizzato correttamente
     */
    public String getStatoQualita() throws AttributoNonValorizzatoException {
        if (statoQualita == null) {
            throw new AttributoNonValorizzatoException(FOR_NAME + "Attributo statoQualita non valorizzato!");
        }
        else if (!STATI_AS_LIST.contains(statoQualita)) {
            throw new AttributoNonValorizzatoException(FOR_NAME + "Attributo statoQualita non valorizzato correttamente!");
        }
        else {
            return statoQualita;
        }
    }

    /**
     * Imposta lo stato qualit&agrave del progetto
     * 
     * @param statoQualita stato della qualit&agrave da impostare
     */
    public void setStatoQualita(String statoQualita) {
        this.statoQualita = statoQualita;
    }


    /* ***************************************************** *
     *  Metodi getter e setter per stato approvvigionamenti  *
     * ***************************************************** */
    /**
     * Restituisce lo stato approvvigionamenti del progetto
     * 
     * @return <code>statoApprovvigionamenti</code> - stato approvvigionamenti
     * @throws it.alma.exception.AttributoNonValorizzatoException  eccezione che viene sollevata se questo oggetto viene usato e statoApprovvigionamenti non &egrave; stato valorizzato (&egrave; un dato obbligatorio) 
     * oppure statoApprovvigionamenti; non &egrave; stato valorizzato correttamente
     */
    public String getStatoApprovvigionamenti() throws AttributoNonValorizzatoException {
        if (statoApprovvigionamenti == null) {
            throw new AttributoNonValorizzatoException(FOR_NAME + "Attributo statoApprovvigionamenti non valorizzato!");
        }
        else if (!STATI_AS_LIST.contains(statoApprovvigionamenti)) {
            throw new AttributoNonValorizzatoException(FOR_NAME + "Attributo statoApprovvigionamenti non valorizzato correttamente!");
        }
        else {
            return statoApprovvigionamenti;
        }
    }

    /**
     * Imposta lo stato approvvigionamenti del progetto
     * @param statoApprovvigionamenti stato approvvigionamenti da impostare
     */
    public void setStatoApprovvigionamenti(String statoApprovvigionamenti) {
        this.statoApprovvigionamenti = statoApprovvigionamenti;
    }


    /* ***************************************************** *
     *     Metodi getter e setter per stato stakeholder      *
     * ***************************************************** */
    /**
     * Restituisce lo stato degli stakeholder del progetto
     * 
     * @return <code>statoStakeholder</code> - stato degli stakeholder
     * @throws it.alma.exception.AttributoNonValorizzatoException  eccezione che viene sollevata se questo oggetto viene usato e statoStakeholder non &egrave; stato valorizzato (&egrave; un dato obbligatorio) 
     * oppure statoStakeholder; non &egrave; stato valorizzato correttamente
     */
    public String getStatoStakeholder() throws AttributoNonValorizzatoException {
        if (statoStakeholder == null) {
            throw new AttributoNonValorizzatoException(FOR_NAME + "Attributo statoStakeholder non valorizzato!");
        }
        else if (!STATI_AS_LIST.contains(statoStakeholder)) {
            throw new AttributoNonValorizzatoException(FOR_NAME + "Attributo statoStakeholder non valorizzato correttamente!");
        }
        else {
            return statoStakeholder;
        }
    }

    /**
     * Imposta lo stato degli stakeholder del progetto
     * 
     * @param statoStakeholder stato degli stakeholder da impostare
     */
    public void setStatoStakeholder(String statoStakeholder) {
        this.statoStakeholder = statoStakeholder;
    }


    /* *********************************************************** *
     *       Metodi getter e setter per data ultima modifica       *
     * *********************************************************** */
    /**
     * Restituisce la data dell'ultima modifica dello status di un progetto
     * 
     * @return <code>dataUltimaModifica</code> - data dell'ultima modifica
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
     * @return <code>oraUltimaModifica</code> - ora dell'ultima modifica
     */
    public String getOraUltimaModifica() {
        return oraUltimaModifica;
    }
    

    /**
     * Imposta l'ora dell'ultima modifica dello status di un progetto
     * 
     * @param oraUltimaModifica ora ultima modifica da impostare
     */
    public void setOraUltimaModifica(String oraUltimaModifica) {
        this.oraUltimaModifica = oraUltimaModifica;
    }


    /* ************************************************************** *
     *       Metodi getter e setter per autore ultima modifica        *
     * ************************************************************** */
    /**
     * Restituisce l'autore dell'ultima modifica dello status di un progetto
     * 
     * @return <code>autoreUltimaModifica</code> - autore ultima modifica
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
