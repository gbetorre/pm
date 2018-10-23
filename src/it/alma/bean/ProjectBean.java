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
import java.util.Date;

import it.alma.Query;
import it.alma.exception.AttributoNonValorizzatoException;

/**
 * <p>Classe usata per rappresentare un progetto.</p>
 * 
 * @author <a href="mailto:andrea.tonel@studenti.univr.it">Andrea Tonel</a>
 */
public class ProjectBean implements Serializable, Query {

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
     *                    Dati identificativi del progetto                      *
     * ************************************************************************ */
    /** Attributo identificativo del progetto */
    private int id;
    /** Titolo del progetto */
    private String titolo;
    /** Descrizione del progetto */
    private String descrizione;
    /** Data di inizio del progetto */
    private Date dataInizio;
    /** Data di fine del progetto */
    private Date dataFine;
    /* ************************************************************************ *  
     *               Dati descrittivi dello stato del progetto                  *
     * ************************************************************************ */
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
    /* ************************************************************************ *  
     *                      Dati descrittivi del progetto                       *
     * ************************************************************************ */
    /** Mese riferimento del progetto */
    private Date meseRiferimento;
    /** Descrizione dello stato corrente del progetto */
    private String descrizioneStatoCorrente;
    /** Situazione attuale del progetto */
    private String situazioneAttuale;
    /** Situazione finale del progetto */
    private String situazioneFinale;
    /** Obiettivi misurabili del progetto */
    private String obiettiviMisurabili;
    /** Minacce del progetto */
    private String minacce;
    /* ************************************************************************ *  
     *             Dati descrittivi degli stakeholder del progetto              *
     * ************************************************************************ */
    /** Stakeholder marginali del progetto */
    private String stakeholderMarginali;
    /** Stakeholder operativi del progetto */
    private String stakeholderOperativi;
    /** Stakeholder istituzionali del progetto */
    private String stakeholderIstituzionali;
    /** Stakeholder chiave del progetto */
    private String stakeholderChiave;
    /* ************************************************************************ *  
     *                      Dati descrittivi del progetto                       *
     * ************************************************************************ */
    /** Deliverable del progetto */
    private String deliverable;
    /** Fornitori chiave interni del progetto */
    private String fornitoriChiaveInterni;
    /** Fornitori chiave esterni del progetto */
    private String fornitoriChiaveEsterni;
    /** Servizi ateneo del progetto */
    private String serviziAteneo;
    /** Vincoli del progetto */
    private String vincoli;
    /** Id del dipartimento del progetto*/
    private int idDipart;
    /** Dipartimento del progetto */
    private DepartmentBean dipart;
    /** Id dello stato progetto */
    private int idStatoProgetto;
    /** Stato progetto */
    private StatoProgettoBean statoProgetto;
    
    /**
     * <p>Costruttore: inizializza i campi a valori di default.</p>
     */
    public ProjectBean() {
    	id = -2;
    	titolo = null;
    	descrizione = null;
    	dataInizio = new Date(0);
    	dataFine = new Date(0);
    	statoTempi = statoCosti = statoRischi = statoRisorse = statoScope = statoComunicazione = statoQualita = statoApprovvigionamenti = statoStakeholder = null;
    	meseRiferimento = new Date(0);
    	descrizioneStatoCorrente = null;
    	situazioneAttuale = situazioneFinale = null;
    	obiettiviMisurabili = null;
    	minacce = null;
    	stakeholderMarginali = stakeholderOperativi = stakeholderIstituzionali = stakeholderChiave = null;
    	deliverable = null;
    	fornitoriChiaveInterni =  fornitoriChiaveEsterni = null;
    	serviziAteneo = null; 
    	vincoli = null;
    	idDipart = -2;
    	dipart = null;
    	idStatoProgetto = -2;
    	statoProgetto = null;    	
    }

    
    /* **************************************************** *
     *           Metodi getter e setter per id              *
     * **************************************************** */
    /**
	 * Restituisce l'id di un progetto.
	 * @return <code>id</code> - l'id del progetto
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
     *           Metodi getter e setter per titolo          *
     * **************************************************** */
	/**
	 * Restituisce il titolo di un progetto.
	 * @return <code>titolo</code> - titolo del progetto
	 * @throws it.alma.exception.AttributoNonValorizzatoException  eccezione che viene sollevata se questo oggetto viene usato e il titolo non &egrave; stato valorizzato (&egrave; un dato obbligatorio) 
	 */
	public String getTitolo() throws AttributoNonValorizzatoException {
		if (titolo == null) {
			throw new AttributoNonValorizzatoException(FOR_NAME + "Attributo titolo non valorizzato!");
		}
		return titolo;
	}

	/**
	 * Imposta il titolo di un progetto.
	 * @param titolo - il titolo del progetto da impostare
	 */
	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}

	
	 /* **************************************************** *
     *           Metodi getter e setter per descrizione     *
     * **************************************************** */
	/**
	 * Restituisce la descrizione di un progetto. 
	 * @return <code>descrizione</code> - la descrizione del progetto
	 */
	public String getDescrizione() {
		return descrizione;
	}

	/**
	 * Imposta la descrizione di un progetto.
	 * @param descrizione - la descrizione da impostare
	 */
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}


	 /* **************************************************** *
     *           Metodi getter e setter per data inizio     *
     * **************************************************** */
	/**
	 * Restituisce la data di inizio di un progetto
	 * @return <code>dataInizio</code> - la data di inizio del progetto
	 * @throws it.alma.exception.AttributoNonValorizzatoException  eccezione che viene sollevata se questo oggetto viene usato e dataInizio non &egrave; stato valorizzato (&egrave; un dato obbligatorio) 
	 */
	public Date getDataInizio() throws AttributoNonValorizzatoException  {
		if(new Date(0).equals(dataInizio)) {
			throw new AttributoNonValorizzatoException(FOR_NAME + "Attributo data inizio non valorizzato!");
		}
		return dataInizio;
	}

	/**
	 * Imposta la data di inizio di un progetto.
	 * @param dataInizio  - la data di inizio da impostare
	 */
	public void setDataInizio(Date dataInizio) {
		this.dataInizio = dataInizio;
	}


	/* **************************************************** *
     *           Metodi getter e setter per data fine       *
     * **************************************************** */
	/**
	 * Restituisce la data di fine di un progetto
	 * @return <code>dataFine</code> - la data di fine del progetto
	 * @throws it.alma.exception.AttributoNonValorizzatoException  eccezione che viene sollevata se questo oggetto viene usato e dataFine non &egrave; stato valorizzato (&egrave; un dato obbligatorio) 
	 */
	public Date getDataFine() throws AttributoNonValorizzatoException {
		if(new Date(0).equals(dataFine)) {
			throw new AttributoNonValorizzatoException(FOR_NAME + "Attributo data fine non valorizzato!");
		}
		return dataFine;
	}

	/**
	 * Imposta la data di fine di un progetto.
	 * @param dataFine - la data di fine da impostare
	 */
	public void setDataFine(Date dataFine) {
		this.dataFine = dataFine;
	}


	/* **************************************************** *
     *           Metodi getter e setter per stato tempi     *
     * **************************************************** */
	/**
	 * Restituisce lo stato tempi del progetto.
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
	 * @param statoTempi - lo stato tempi da impostare
	 */
	public void setStatoTempi(String statoTempi) {
		this.statoTempi = statoTempi;
	}


	/* **************************************************** *
     *           Metodi getter e setter per stato costi     *
     * **************************************************** */
	/**
	 * Restituisce lo stato costi del progetto.
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
	 * @param statoCosti - lo stato costi da impostare
	 */
	public void setStatoCosti(String statoCosti) {
		this.statoCosti = statoCosti;
	}


	/* **************************************************** *
     *           Metodi getter e setter per stato rischi    *
     * **************************************************** */
	/**
	 * Restituisce lo stato rischi del progetto.
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
	 * @param statoRischi  - lo stato rischi da impostare
	 */
	public void setStatoRischi(String statoRischi) {
		this.statoRischi = statoRischi;
	}


	/* **************************************************** *
     *           Metodi getter e setter per stato risorse   *
     * **************************************************** */
	/**
	 * Restituisce lo stato risorse del progetto
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
	 * @param statoRisorse  - lo stato delle risorse da impostare
	 */
	public void setStatoRisorse(String statoRisorse) {
		this.statoRisorse = statoRisorse;
	}


	/* **************************************************** *
     *           Metodi getter e setter per stato scope     *
     * **************************************************** */
	/**
	 * Restituisce lo stato scope del progetto
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
	 * @param statoScope - lo stato scope da impostare
	 */
	public void setStatoScope(String statoScope) {
		this.statoScope = statoScope;
	}


	/* **************************************************** *
     *    Metodi getter e setter per stato comunicazione    *
     * **************************************************** */
	/**
	 * Restituisce lo stato comunicazione del progetto
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
	 * @param statoComunicazione - stato della comunicazione da impostare
	 */
	public void setStatoComunicazione(String statoComunicazione) {
		this.statoComunicazione = statoComunicazione;
	}


	/* **************************************************** *
     *    Metodi getter e setter per stato qualit&agrave;   *
     * **************************************************** */
	/**
	 * Restituisce lo stato qualit&agrave; del progetto
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
	 * @param statoQualita - stato della qualit&agrave da impostare
	 */
	public void setStatoQualita(String statoQualita) {
		this.statoQualita = statoQualita;
	}


	/* ***************************************************** *
     *  Metodi getter e setter per stato approvvigionamenti  *
     * ***************************************************** */
	/**
	 * Restituisce lo stato approvvigionamenti del progetto
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
	 * @param statoApprovvigionamenti - stato approvvigionamenti da impostare
	 */
	public void setStatoApprovvigionamenti(String statoApprovvigionamenti) {
		this.statoApprovvigionamenti = statoApprovvigionamenti;
	}


	/* ***************************************************** *
     *     Metodi getter e setter per stato stakeholder      *
     * ***************************************************** */
	/**
	 * Restituisce lo stato degli stakeholder del progetto
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
	 * @param statoStakeholder - stato degli stakeholder da impostare
	 */
	public void setStatoStakeholder(String statoStakeholder) {
		this.statoStakeholder = statoStakeholder;
	}


	/* ***************************************************** *
     *     Metodi getter e setter per meseRiferimento        *
     * ***************************************************** */
	/**
	 * Restituisce il mese di riferimento del progetto
	 * @return <code>meseRiferimento</code> - mese di riferimento 
	 * @throws it.alma.exception.AttributoNonValorizzatoException  eccezione che viene sollevata se questo oggetto viene usato e meseRiferimento non &egrave; stato valorizzato (&egrave; un dato obbligatorio) 
	 */
	public Date getMeseRiferimento() throws AttributoNonValorizzatoException {
		if(new Date(0).equals(meseRiferimento)) {
			throw new AttributoNonValorizzatoException(FOR_NAME + "Attributo meseRiferimento non valorizzato!");
		}
		return meseRiferimento;
	}

	/**
	 * Imposta il mese di riferimento del progetto
	 * @param meseRiferimento - mese di riferimento da impostare
	 */
	public void setMeseRiferimento(Date meseRiferimento) {
		this.meseRiferimento = meseRiferimento;
	}


	/* ******************************************************* *
     *   Metodi getter e setter per descrizioneStatoCorrente   *
     * ******************************************************* */
	/**
	 * Restituisce la descrizione dello stato corrente del progetto
	 * @return <code>descrizioneStatoCorrente</code> - descrizione stato corrente
	 */
	public String getDescrizioneStatoCorrente() {
		return descrizioneStatoCorrente;
	}

	/**
	 * Imposta la descrizione dello stato corrente
	 * @param descrizioneStatoCorrente - descrizione dello stato corrente del progetto da impostare
	 */
	public void setDescrizioneStatoCorrente(String descrizioneStatoCorrente) {
		this.descrizioneStatoCorrente = descrizioneStatoCorrente;
	}


	/* **************************************************** *
     *     Metodi getter e setter per situazioneAttuale     *
     * **************************************************** */
	/**
	 * Restituisce la situazione attuale del progetto
	 * @return <code>situazioneAttuale</code> - situazione attuale
	 */
	public String getSituazioneAttuale() {
		return situazioneAttuale;
	}

	/**
	 * Imposta la situazione attuale del progetto
	 * @param situazioneAttuale - situazione attuale del progetto da impostare
	 */
	public void setSituazioneAttuale(String situazioneAttuale) {
		this.situazioneAttuale = situazioneAttuale;
	}
	

	/* *************************************************** *
     *     Metodi getter e setter per situazioneFinale     *
     * *************************************************** */
	/**
	 * Restituisce la situazione finale del progetto
	 * @return <code>situazioneFinale</code> - situazione finale
	 */
	public String getSituazioneFinale() {
		return situazioneFinale;
	}

	/**
	 * Imposta la situazione finale del progetto
	 * @param situazioneFinale - situazione finale da impostare
	 */
	public void setSituazioneFinale(String situazioneFinale) {
		this.situazioneFinale = situazioneFinale;
	}


	/* ****************************************************** *
     *     Metodi getter e setter per obiettiviMisurabili     *
     * ****************************************************** */
	/**
	 * Restituisce gli obiettivi misurabili del progetto
	 * @return <code>obiettiviMisurabili</code> - obiettivi misurabili
	 */
	public String getObiettiviMisurabili() {
		return obiettiviMisurabili;
	}

	/**
	 * Imposta gli obiettivi misurabili
	 * @param obiettiviMisurabili - obiettivi misurabili da impostare
	 */
	public void setObiettiviMisurabili(String obiettiviMisurabili) {
		this.obiettiviMisurabili = obiettiviMisurabili;
	}


	/* ************************************************ *
     *        Metodi getter e setter per minacce        *
     * ************************************************ */
	/**
	 * Restituisce le minacce di un progetto
	 * @return <code>minacce</code> - minacce del progetto attuale
	 */
	public String getMinacce() {
		return minacce;
	}

	/**
	 * Imposta le minacce del progetto
	 * @param minacce - minacce da impostare
	 */
	public void setMinacce(String minacce) {
		this.minacce = minacce;
	}


	/* ****************************************************** *
     *     Metodi getter e setter per stakeholderMarginali    *
     * ****************************************************** */
	/**
	 * Restituisce gli stakeholder marginali del progetto
	 * @return <code>stakeholderMarginali</code> - stakeholder marginali del progetto attuale
	 */
	public String getStakeholderMarginali() {
		return stakeholderMarginali;
	}

	/**
	 * Imposta gli stakeholder marginali del progetto
	 * @param stakeholderMarginali - stakeholder marginali da impostare
	 */
	public void setStakeholderMarginali(String stakeholderMarginali) {
		this.stakeholderMarginali = stakeholderMarginali;
	}


	/* ****************************************************** *
     *     Metodi getter e setter per stakeholderOperativi    *
     * ****************************************************** */
	/**
	 * Restituisce gli stakeholder operativi del progetto
	 * @return <code>stakeholderOperativi</code> - stakeholder operativi del progetto
	 */
	public String getStakeholderOperativi() {
		return stakeholderOperativi;
	}

	/**
	 * Imposta gli stakeholder operativi del progetto
	 * @param stakeholderOperativi - stakeholder operativi da impostare
	 */
	public void setStakeholderOperativi(String stakeholderOperativi) {
		this.stakeholderOperativi = stakeholderOperativi;
	}

	/* ********************************************************** *
     *     Metodi getter e setter per stakeholderIstituzionali    *
     * ********************************************************** */
	/**
	 * Restituisce gli stakeholder istituzionali del progetto
	 * @return <code>stakeholderIstituzionali</code> - stakeholder istituzionali del progetto
	 */
	public String getStakeholderIstituzionali() {
		return stakeholderIstituzionali;
	}

	/**
	 * Imposta gli stakeholder istituzionali del progetto
	 * @param stakeholderIstituzionali - stakeholder istituzionali da impostare
	 */
	public void setStakeholderIstituzionali(String stakeholderIstituzionali) {
		this.stakeholderIstituzionali = stakeholderIstituzionali;
	}

	
	/* ***************************************************** *
     *      Metodi getter e setter per stakeholderChiave     *
     * ***************************************************** */
	/**
	 * Restituisce gli stakeholder chiave del progetto
	 * @return <code>stakeholderChiave</code> - stakeholder chiave del progetto
	 */
	public String getStakeholderChiave() {
		return stakeholderChiave;
	}

	/**
	 * Imposta gli stakeholder chive del progetto
	 * @param stakeholderChiave - stakeholder chiave da impostare
	 */
	public void setStakeholderChiave(String stakeholderChiave) {
		this.stakeholderChiave = stakeholderChiave;
	}


	/* ***************************************************** *
     *         Metodi getter e setter per deliverable        *
     * ***************************************************** */
	/**
	 * Restituisce la derivable del progetto
	 * @return <code>deliverable</code> - deliverable del progetto attuale
	 */
	public String getDeliverable() {
		return deliverable;
	}

	/**
	 * Imposta la derivable del progetto
	 * @param deliverable - deriverable da impostare
	 */
	public void setDeliverable(String deliverable) {
		this.deliverable = deliverable;
	}

	/* **************************************************************** *
     *         Metodi getter e setter per fornitoriChiaveInterni        *
     * **************************************************************** */
	/**
	 * Restituisce i fornitori chiave interni del progetto attuale
	 * @return <code>fornitoriChiaveInterni</code> - fornitori chiavi interni del progetto
	 */
	public String getFornitoriChiaveInterni() {
		return fornitoriChiaveInterni;
	}

	/**
	 * Imposta i fornitori chiave interni del progetto
	 * @param fornitoriChiaveInterni - fornitori chiavi interni da impostare
	 */
	public void setFornitoriChiaveInterni(String fornitoriChiaveInterni) {
		this.fornitoriChiaveInterni = fornitoriChiaveInterni;
	}


	/* **************************************************************** *
     *         Metodi getter e setter per fornitoriChiaveEsterni        *
     * **************************************************************** */
	/**
	 * Restituisce i fornitori chiave esterni del progetto attuale
	 * @return <code>fornitoriChiaveEsterni</code> - fornitori chiavi esterni del progetto
	 */
	public String getFornitoriChiaveEsterni() {
		return fornitoriChiaveEsterni;
	}

	/**
	 * Imposta i fornitori chiave esterni del progetto
	 * @param fornitoriChiaveEsterni - fornitori chiavi esterni da impostare
	 */
	public void setFornitoriChiaveEsterni(String fornitoriChiaveEsterni) {
		this.fornitoriChiaveEsterni = fornitoriChiaveEsterni;
	}


	/* ******************************************************* *
     *         Metodi getter e setter per serviziAteneo        *
     * ******************************************************* */
	/**
	 * Restituisce i servizi di ateneo del progetto
	 * @return <code>serviziAteneo</code> - servizi di ateneo del progetto
	 */
	public String getServiziAteneo() {
		return serviziAteneo;
	}

	/**
	 * Imposta i servizi di ateneo del progetto
	 * @param serviziAteneo - servizi di ateneo da impostare
	 */
	public void setServiziAteneo(String serviziAteneo) {
		this.serviziAteneo = serviziAteneo;
	}


	/* ************************************************* *
     *         Metodi getter e setter per vincoli        *
     * ************************************************* */
	/**
	 * Restituisce i vincoli del progetto
	 * @return <code>vincoli</code> - vincoli del progetto
	 */
	public String getVincoli() {
		return vincoli;
	}

	/**
	 * Imposta i vincoli del progetto
	 * @param vincoli - vincoli del progetto da impostare
	 */
	public void setVincoli(String vincoli) {
		this.vincoli = vincoli;
	}

	
	/* ************************************************** *
     *         Metodi getter e setter per idDipart        *
     * ************************************************** */
	/**
	 * Restituisce l'id del dipartimento del progetto
	 * @return <code>idDipart</code> - id del dipartimento del progetto
	 * @throws it.alma.exception.AttributoNonValorizzatoException  eccezione che viene sollevata se questo oggetto viene usato e idDipart non 		&egrave; stato valorizzato (&egrave; un dato obbligatorio) 
	 */
	public int getIdDipart() throws AttributoNonValorizzatoException {
		if (idDipart == -2) {
			throw new AttributoNonValorizzatoException(FOR_NAME + "Attributo idDipart non valorizzato!");
		}
		return idDipart;
	}

	/**
	 * Imposta l'id del dipartimento del progetto
	 * @param idDipart - id del dipartimento del progetto
	 */
	public void setIdDipart(int idDipart) {
		this.idDipart = idDipart;
	}


	/* *************************************************** *
     *          Metodi getter e setter per dipart          *
     * *************************************************** */
	/**
	 * Restituisce un oggetto DepartmentBean che rappresenta il dipartimento del progetto
	 * @return <code>dipart</code> - dipartimento del progetto
	 * @throws it.alma.exception.AttributoNonValorizzatoException  eccezione che viene sollevata se questo oggetto viene usato e dipart non 		&egrave; stato valorizzato (&egrave; un dato obbligatorio)
	 */
	public DepartmentBean getDipart() throws AttributoNonValorizzatoException {
		if (dipart == null) {
			throw new AttributoNonValorizzatoException(FOR_NAME + "Attributo dipart non valorizzato!");
		}
		return dipart;
	}

	/**
	 * Imposta dipart con l'oggetto DepartmentBean passato
	 * @param dipart - dipartimento del progetto da impostare
	 */
	public void setDipart(DepartmentBean dipart) {
		this.dipart = dipart;
	}

	
	/* ***************************************************** *
     *       Metodi getter e setter per idStatoProgetto      *
     * ***************************************************** */
	/**
	 * Restituisce l'id dello stato del progetto
	 * @return <code>idStatoProgetto</code> - id dello stato del progetto
	 * @throws it.alma.exception.AttributoNonValorizzatoException  eccezione che viene sollevata se questo oggetto viene usato e idStatoProgetto non 		&egrave; stato valorizzato (&egrave; un dato obbligatorio) 
	 */
	public int getIdStatoProgetto() throws AttributoNonValorizzatoException {
		if(idStatoProgetto == -2) {
			throw new AttributoNonValorizzatoException(FOR_NAME + "Attributo idStatoProgetto non valorizzato!");
		}
		return idStatoProgetto;
	}

	/**
	 * Imposta l'id dello stato del progetto
	 * @param idStatoProgetto - id dello stato del progetto
	 */
	public void setIdStatoProgetto(int idStatoProgetto) {
		this.idStatoProgetto = idStatoProgetto;
	}


	/* *************************************************** *
     *       Metodi getter e setter per statoProgetto      *
     * *************************************************** */
	/**
	 * Restituisce un oggetto StatoProgettoBean che rappresenta lo stato del progetto
	 * @return <code>statoProgetto</code> - stato del progetto
	 * @throws it.alma.exception.AttributoNonValorizzatoException  eccezione che viene sollevata se questo oggetto viene usato e statoProgetto non 		&egrave; stato valorizzato (&egrave; un dato obbligatorio) 
	 */
	public StatoProgettoBean getStatoProgetto() throws AttributoNonValorizzatoException {
		if(statoProgetto == null) {
			throw new AttributoNonValorizzatoException(FOR_NAME + "Attributo statoProgetto non valorizzato!");
		}
		return statoProgetto;
	}

	/**
	 * Imposta lo stato di un progetto.
	 * @param statoProgetto - stato progetto da impostare
	 */
	public void setStatoProgetto(StatoProgettoBean statoProgetto) {
		this.statoProgetto = statoProgetto;
	}
	
    
}
