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
import java.util.Vector;

import it.alma.Query;
import it.alma.exception.AttributoNonValorizzatoException;

/**
 * <p>Classe usata per rappresentare un'attivit&agrave;.</p>
 * 
 * @author <a href="mailto:andrea.tonel@studenti.univr.it">Andrea Tonel</a>
 */
public class ActivityBean extends CodeBean implements Serializable, Query {

	/**
     * La serializzazione necessita di dichiarare una costante di tipo long
     * identificativa della versione seriale. 
     * (Se questo dato non fosse inserito, verrebbe calcolato in maniera automatica
     * dalla JVM, e questo potrebbe portare a errori riguardo alla serializzazione). 
	 */
	private static final long serialVersionUID = -90515316806359446L;
	/**
     *  Nome di questa classe. 
     *  Viene utilizzato per contestualizzare i messaggi di errore.
     */
    private final String FOR_NAME = "\n" + this.getClass().getName() + ": "; //$NON-NLS-1$
    /* $NON-NLS-1$ silence a warning that Eclipse emits when it encounters string literals
        The idea is that UI messages should not be embedded as string literals, 
        but rather sourced from a resource file (so that they can be translated, proofed, etc).*/
    /* ************************************************************************ *  
     *                    Dati identificativi dell'attività                     *
     * ************************************************************************ */
    /** Attributo identificativo dell'attivit&agrave; */
    private int id;
    /** Nome dell'attivit&agrave; */
    private String nome;
    /* ************************************************************************ *  
     *                     Dati descrittivi dell'attività                       *
     * ************************************************************************ */
    /** Descrizione dell'attivit&agrave; */
    private String descrizione;
    /** Data inizio dell'attivit&agrave; */
    private Date dataInizio;
    /** Data fine dell'attivit&agrave; */
    private Date dataFine;
    /** Data inizio di attesa dell'attivit&agrave; */
    private Date dataInizioAttesa;
    /** Data fine di attesa dell'attivit&agrave; */
    private Date dataFineAttesa;
    /** Data di inizio effettiva dell'attivit&agrave; */
    private Date dataInizioEffettiva;
    /** Data fine dell'attivit&agrave; effettiva */
    private Date dataFineEffettiva;
    /** Giorni uomo previsti per l'attivit&agrave; */
    private int guPrevisti;
    /** Giorni uomo effettivi per l'attivit&agrave; */
    private int guEffettivi;
    /** Giorni uomo rimanenti per l'attivit&agrave; */
    private int guRimanenti;
    /** Note di avanzamento dell'attivit&agrave; */
    private String noteAvanzamento;
    /** Milestone attivit&agrave; */
    private boolean milestone;
    /* *********************************************** *
     *              Persone dell'attività              *
     * *********************************************** */
    /** Vector di persone che partecipano all'attivit&agrave; */
    private Vector<PersonBean> persone;
    /* *********************************************** *
     *           Riferimenti ad altre entità           *
     * *********************************************** */
    /** Riferimento alla WBS (Work Package) sotto cui viene aggregata l'attivit&agrave; */
    private int idWbs;
    /** Riferimento al livello di complessit&agrave; dell'attivit&agrave; */
    private int idComplessita;
    /** Riferimento allo stato in cui si trova l'attivit&agrave; */
    private int idStato;
    
    /**
     * <p>Costruttore: inizializza i campi a valori di default.</p>
     */
    public ActivityBean() {
    	id = -2;
    	nome = null;
    	descrizione = null;
    	dataInizio = dataFine = dataInizioAttesa = dataFineAttesa = dataInizioEffettiva = dataFineEffettiva = new Date(0);
    	guPrevisti = guEffettivi = guRimanenti = -2;
    	noteAvanzamento = null;
    	milestone = false;
    	persone = null;
    	idWbs = idComplessita = idStato = -2;
    }
    
    /* **************************************************** *
     *           Metodi getter e setter per id              *
     * **************************************************** */
    /**
	 * Restituisce l'id di un'attivit&agrave;
	 * 
	 * @return <code>id</code> - l'id dell'attivit&agrave;
     * @throws it.alma.exception.AttributoNonValorizzatoException  eccezione che viene sollevata se questo oggetto viene usato e l'id non &egrave; stato valorizzato (&egrave; un dato obbligatorio)
	 */
	public int getId() throws AttributoNonValorizzatoException {
		if (id == -2) {
			throw new AttributoNonValorizzatoException(FOR_NAME + "Attributo id non valorizzato!");
		}
		return id;
	}
	
	/**
	 * Imposta l'id di un'attivit&agrave;
	 * 
	 * @param id - l'identificativo da impostare
	 */
	public void setId(int id) {
		this.id = id;
	}

	
    /* ****************************************************** *
     *           Metodi getter e setter per nome              *
     * ****************************************************** */
	/**
	 * Restituisce il nome di un'attivit&agrave;
	 * 
	 * @return <code>nome</code> - nome dell'attivit&agrave;
	 * @throws it.alma.exception.AttributoNonValorizzatoException  eccezione che viene sollevata se questo oggetto viene usato e il nome non &egrave; stato valorizzato (&egrave; un dato obbligatorio)
	 */
	public String getNome() throws AttributoNonValorizzatoException {
		if (nome == null) {
			throw new AttributoNonValorizzatoException(FOR_NAME + "Attributo nome non valorizzato!");
		}
		return nome;
	}
	
	/**
	 * Imposta il nome di un'attivit&agrave;
	 * 
	 * @param nome - nome da impostare
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}

	
	/* ************************************************************* *
     *           Metodi getter e setter per descrizione              *
     * ************************************************************* */
	/**
	 * Restituisce la descrizione di un'attivit&agrave;
	 * 
	 * @return <code>descrizione</code> - descrizione dell'attivit&agrave;
	 */
	public String getDescrizione() {
		return descrizione;
	}

	/**
	 * Imposta la descrizione di un'attivit&agrave;
	 * 
	 * @param descrizione - descrizione da settare
	 */
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	
	/* ************************************************************* *
     *           Metodi getter e setter per dataInizio               *
     * ************************************************************* */
	/**
	 * Restituisce la data inizio di un'attivit&agrave;
	 * 
	 * @return <code>dataInizio</code> - data inizio di un'attivit&agrave;
	 */
	public Date getDataInizio() {
		return dataInizio;
	}

	/**
	 * Imposta la data di inizio di un'attivit&agrave;
	 * 
	 * @param dataInizio - data di inizio da impostare
	 */
	public void setDataInizio(Date dataInizio) {
		this.dataInizio = dataInizio;
	}

	
	/* ************************************************************* *
     *            Metodi getter e setter per dataFine                *
     * ************************************************************* */
	/**
	 * Restituisce la data fine di un'attivit&agrave;
	 * 
	 * @return <code>dataFine</code> - data di fine di un'attivit&agrave;
	 * @throws it.alma.exception.AttributoNonValorizzatoException  eccezione che viene sollevata se questo oggetto viene usato e dataFine non &egrave; stato valorizzato (&egrave; un dato obbligatorio)
	 */
	public Date getDataFine() throws AttributoNonValorizzatoException {
		if (new Date(0).equals(dataFine)) {
			throw new AttributoNonValorizzatoException(FOR_NAME + "Attributo dataFine non valorizzato!");
		}
		return dataFine;
	}

	/**
	 * Imposta la data di fine di un'attivit&agrave;
	 * 
	 * @param dataFine - data di fine da impostare
	 */
	public void setDataFine(Date dataFine) {
		this.dataFine = dataFine;
	}

	
	/* ************************************************************* *
     *        Metodi getter e setter per dataInizioAttesa            *
     * ************************************************************* */
	/**
	 * Restituisce la data di inizio attesa di un'attivit&agrave;
	 * 
	 * @return <code>dataInizioAttesa</code> - data inizio di attesa di un'attivit&agrave;
	 */
	public Date getDataInizioAttesa() {
		return dataInizioAttesa;
	}

	/**
	 * Imposta la data di inizio attesa di un'attivit&agrave;
	 * 
	 * @param dataInizioAttesa - data di inizio di un'attivit&agrave;
	 */
	public void setDataInizioAttesa(Date dataInizioAttesa) {
		this.dataInizioAttesa = dataInizioAttesa;
	}

	
	/* ************************************************************* *
     *         Metodi getter e setter per dataFineAttesa             *
     * ************************************************************* */
	/**
	 * Restituisce la data di fine attesa di un'attivit&agrave;
	 * 
	 * @return <code>dataFineAttesa</code> - data fine di attesa di un'attivit&agrave;
	 */
	public Date getDataFineAttesa() {
		return dataFineAttesa;
	}

	/**
	 * Imposta la data di fine attesa di un'attivit&agrave;
	 * 
	 * @param dataFineAttesa - data di fine attesa di un'attivit&agrave;
	 */
	public void setDataFineAttesa(Date dataFineAttesa) {
		this.dataFineAttesa = dataFineAttesa;
	}


	/* ************************************************************** *
     *       Metodi getter e setter per dataInizioEffettiva           *
     * ************************************************************** */
	/**
	 * Restituisce la data inizio effettiva di un'attivit&agrave;
	 * 
	 * @return <code>dataInizioEffettiva</code> - data di inizio effettiva di un'attivit&agrave;
	 */
	public Date getDataInizioEffettiva() {
		return dataInizioEffettiva;
	}

	/**
	 * Imposta la data inizio effettiva di un'attivit&agrave;
	 * 
	 * @param dataInizioEffettiva - data di inizio effettiva da impostare
	 */
	public void setDataInizioEffettiva(Date dataInizioEffettiva) {
		this.dataInizioEffettiva = dataInizioEffettiva;
	}


	/* ************************************************************** *
     *        Metodi getter e setter per dataFineEffettiva            *
     * ************************************************************** */
	/**
	 * Restituisce la data fine effettiva di un'attivit&agrave;
	 * 
	 * @return <code>dataFineEffettiva</code> - data di fine effettiva di un'attivit&agrave;
	 */
	public Date getDataFineEffettiva() {
		return dataFineEffettiva;
	}

	/**
	 * Imposta la data fine effettiva di un'attivit&agrave;
	 * 
	 * @param dataFineEffettiva - data di fine effettiva da impostare
	 */
	public void setDataFineEffettiva(Date dataFineEffettiva) {
		this.dataFineEffettiva = dataFineEffettiva;
	}

	
	/* ********************************************************* *
     *         Metodi getter e setter per guPrevisti             *
     * ********************************************************* */
	/**
	 * Restituisce i giorni uomo previsti per l'attivit&agrave;
	 * 
	 * @return <code>guPrevisti</code> - giorni uomo previsti per l'attivit&agrave;
	 */
	public int getGuPrevisti() {
		return guPrevisti;
	}

	/**
	 * Imposta i giorni uomo previsti per l'attivit&agrave;
	 * 
	 * @param guPrevisti - giorni uomo previsti da impostare
	 */
	public void setGuPrevisti(int guPrevisti) {
		this.guPrevisti = guPrevisti;
	}

	
	/* ********************************************************* *
     *         Metodi getter e setter per guEffettivi            *
     * ********************************************************* */
	/**
	 * Restituisce i giorni uomo effettivi per l'attivit&agrave;
	 * 
	 * @return <code>guEffettivi</code> - giorni uomo effettivi per l'attivit&agrave;
	 */
	public int getGuEffettivi() {
		return guEffettivi;
	}

	/**
	 * Imposta i giorni uomo effettivi per l'attivit&agrave;
	 * 
	 * @param guEffettivi - giorni uomo effettivi da impostare
	 */
	public void setGuEffettivi(int guEffettivi) {
		this.guEffettivi = guEffettivi;
	}


	/* ********************************************************* *
     *         Metodi getter e setter per guRimanenti            *
     * ********************************************************* */
	/**
	 * Restituisce i giorni uomo rimanenti per l'attivit&agrave;
	 * 
	 * @return <code>guRimanenti</code> - giorni uomo rimanenti per l'attivit&agrave;
	 */
	public int getGuRimanenti() {
		return guRimanenti;
	}

	/**
	 * Imposta i giorni uomo rimanenti per l'attivit&agrave;
	 * 
	 * @param guRimanenti - giorni uomo rimanenti da impostare
	 */
	public void setGuRimanenti(int guRimanenti) {
		this.guRimanenti = guRimanenti;
	}


	/* ********************************************************* *
     *       Metodi getter e setter per noteAvanzamento          *
     * ********************************************************* */
	/**
	 * Restituisce le note di avanzamento dell'attivit&agrave;
	 * 
	 * @return <code>noteAvanzamento</code> - note di avanzamento dell'attvit&agrave;
	 */
	public String getNoteAvanzamento() {
		return noteAvanzamento;
	}

	/**
	 * Imposta le note di avanzamento di un'attivit&agrave;
	 * 
	 * @param noteAvanzamento - note di avanzamento da impostare
	 */
	public void setNoteAvanzamento(String noteAvanzamento) {
		this.noteAvanzamento = noteAvanzamento;
	}

	
	/* ********************************************************* *
     *          Metodi getter e setter per milestone             *
     * ********************************************************* */
	/**
	 * Restituisce true se l'attivit&agrave; &egrave; milestone - false (default) se non lo &egrave;
	 * 
	 * @return <code>milestone</code> - true/false attivit&agrave; milestone
	 */
	public boolean isMilestone() {
		return milestone;
	}

	/**
	 * Imposta milestone l'attivit&agrave;
	 *  
	 * @param milestone - milestone true/false da impostare
	 */
	public void setMilestone(boolean milestone) {
		this.milestone = milestone;
	}

	
	/* ********************************************************* *
     *           Metodi getter e setter per persone              *
     * ********************************************************* */
    /**
     * Restituisce il vector contenente le persone partecipanti all'attivit&agrave;
     * 
     * @return <code>persone</code> - Vector contenente le persone che partecipano all'attivit&agrave;
     */
    public Vector<PersonBean> getPersone() {
        return persone;
    }

    /**
     * Imposta le persone che partecipano all'attivit&agrave;
     * 
     * @param persone - Vector da impostare
     */
    public void setPersone(Vector<PersonBean> persone) {
        this.persone = persone;
    }

    
    /* ******************************************************** *
     *   Metodi getter e setter per id della WBS dell'attivita' *
     * ******************************************************** */
    /**
     * Restituisce l'id di una WBS sotto cui l'attivit&agrave;
     * &egrave; aggregata. La WBS deve essere di tipo 'Work Package'. 
     * 
     * @return <code>idWbs</code> - l'id dela WBS sotto cui l'attivit&agrave; si trova
     * @throws it.alma.exception.AttributoNonValorizzatoException  eccezione che viene sollevata se questo oggetto viene usato e l'id WBS non &egrave; stato valorizzato (&egrave; un dato obbligatorio)
     */
    public int getIdWbs() throws AttributoNonValorizzatoException {
        if (idWbs == -2) {
            throw new AttributoNonValorizzatoException(FOR_NAME + "Attributo id WBS non valorizzato!");
        }
        return idWbs;
    }

    /**
     * Imposta la WBS che aggrega l'attivit&agrave;
     * 
     * @param idWbs id della Wbs da settare
     */
    public void setIdWbs(int idWbs) {
        this.idWbs = idWbs;
    }

    
    /* ******************************************************************** *
     *  Metodi getter e setter per il grado di complessita' dell'attivita'  *
     * ******************************************************************** */
    /**
     * Restituisce l'id del grado di complessit&agrave; di una attivit&agrave; 
     * 
     * @return <code>idComplessita</code> - l'id della complessit&agrave; che descrive l'attivit&agrave;
     * @throws it.alma.exception.AttributoNonValorizzatoException  eccezione che viene sollevata se questo oggetto viene usato e questo attributo non &egrave; stato valorizzato (&egrave; un dato obbligatorio)
     */
    public int getIdComplessita() throws AttributoNonValorizzatoException {
        if (idComplessita == -2) {
            throw new AttributoNonValorizzatoException(FOR_NAME + "Attributo id complessita\' non valorizzato!");
        }
        return idComplessita;
    }

    /**
     * <p>Imposta l'identificativo della complessit&agrave; che descrive, appunto,
     * il grado di complessit&agrave; dell'attivit&agrave;</p>
     * <p>Restituisce l'id del grado di complessit&agrave; dell'attivit&agrave;.
     * Quantunque generalmente i metodi setter non restituiscano valori,
     * nulla vieta di permettergli di restituire il valore oggetto del settaggio
     * (valore del parametro o dell'argomento).
     * Tuttavia ci&ograve; comporterebbe un problema nella reflection:
     * <pre>
     * java.util.logging.Logger@4893ccd0: Oggetto ActivityBean non valorizzato; problema nella query dell'attivita'.
     * Problemi nel settare l'attributo 'idComplessita' nel bean di tipo 'class it.alma.bean.ActivityBean': Cannot set idComplessita
     * Problemi nel settare l'attributo 'idComplessita' nel bean di tipo 'class it.alma.bean.ActivityBean': Cannot set idComplessita
     * it.alma.bean.BeanUtil.populate(BeanUtil.java:185)
     * </pre>
     * Per cui nel costruttore NON facciamo:
     * <pre>setIdWbs(setIdStato(setIdComplessita(-2)));</pre>
     * ma usiamo il solito sistema di inizializzare tutto a un default negativo.</p>
     * 
     * @param idComplessita idComplessita da impostare
     */
    public void setIdComplessita(int idComplessita) {
        this.idComplessita = idComplessita;
    }
    
    
    /* ******************************************************************** *
     *    Metodi getter e setter per lo stato in cui si trova l'attivita'   *
     * ******************************************************************** */
    /**
     * Restituisce l'id dello stato di avanzamento di una attivit&agrave; 
     * 
     * @return <code>idStato</code> - l'id dello stato che descrive l'avanzamento in cui l'attivit&agrave; si trova
     * @throws it.alma.exception.AttributoNonValorizzatoException  eccezione che viene sollevata se questo oggetto viene usato e questo attributo non &egrave; stato valorizzato (&egrave; un dato obbligatorio)
     */
    public int getIdStato() throws AttributoNonValorizzatoException {
        if (idStato == -2) {
            throw new AttributoNonValorizzatoException(FOR_NAME + "Attributo id stato non valorizzato!");
        }
        return idStato;
    }

    /**
     * <p>Imposta l'identificativo dello stato che descrive, appunto,
     * il grado di avanzamento dell'attivit&agrave;</p>
     * <p>Restituisce l'id del grado di avanzamento dell'attivit&agrave; (stato).
     * Quantunque generalmente i metodi setter non restituiscano valori,
     * Eclipse IDE propone questo pattern nel momento in cui i getter e 
     * setter vengono generati automaticamente. Tuttavia questo modello
     * collide con la reflection implementata dal BeanUtil, per cui
     * lasciamo <code>void</code>.</p>
     * 
     * @param idStato identificativo dello stato di avanzamento da impostare
     */
    public void setIdStato(int idStato) {
        this.idStato = idStato;
    }
	
}