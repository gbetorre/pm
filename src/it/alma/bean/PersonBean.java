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

package it.alma.bean;

import it.alma.exception.AttributoNonValorizzatoException;

import java.io.Serializable;
import java.sql.Date;
import java.util.Vector;


/**
 * <p>PersonBean &egrave; l'oggetto che rappresenta una persona fisica.</p>
 * 
 * @author <a href="mailto:giovanroberto.torre@univr.it">Giovanroberto Torre</a>
 */
public class PersonBean implements Serializable {
    /**
     * La serializzazione necessita di dichiarare una costante di tipo long
     * identificativa della versione seriale. 
     * (Se questo dato non fosse inserito, verrebbe calcolato in maniera automatica
     * dalla JVM, e questo potrebbe portare a errori riguardo alla serializzazione). 
     */
    private static final long serialVersionUID = -3415439696526030885L;
    /**
     *  Nome di questa classe. 
     *  Viene utilizzato per contestualizzare i messaggi di errore.
     */
    private final String FOR_NAME = "\n" + this.getClass().getName() + ": "; //$NON-NLS-1$
    /**
     *  Costante parlante per l'id del ruolo &quot;PMO di Ateneo&quot;
     */
    private static final int PMO_ATE = 1;
    /* ************************************************************************ *  
     *                    Dati identificativi della persona                     *
     * ************************************************************************ */
    /** Attributo identificativo della persona */
    protected int id;
    /** Specifica se la persona &egrave; obliata */
    private boolean oblio;
    /** Attributo identificativo del nome della persona */
    private String nome;    
    /** Attributo identificativo del cognome della persona */
    private String cognome;
    /** Attributo che memorizza la data di nascita della persona */
    private Date dataNascita;
    /** Sesso attuale della persona */
    private String sesso;
    /** Codice fiscale della persona */
    private String codiceFiscale;
    /** Numero telefonico dell'ufficio della persona */
    private String telefono;
    /** Cellulare (di servizio) della persona */
    private String telefonoMobile;
    /** Numero di fax a cui la persona pu&ograve; ricevere un dispaccio */
    private String fax;
    /** Flag per specificare se il numero di telefono assegnato alla persona debba essere visibile */
    private boolean mostraTelefono;
    /** Flag per specificare se la persona stessa debba essere visibile nel contesto della struttura/ufficio di appartenenza */
    private boolean mostraPersonale;
    /** E-mail della persona */
    private String email;
    /** Posta Elettronica Certificata o PEC */
    private String emailCertificata;    
    /** Attributo per memorizzare l'ID Skype ai fini della visualizzazione dello stato su web */
    private String skypeid;
    /** Ritratto della persona */
    private Vector<FileDocBean> foto;
    /** Curriculum personale */
    private Vector<FileDocBean> currFileDoc;
    /** Url della pagina personale in caso di necessit&agrave; di mostrare la propria persona in ulteriore contesto */
    private String urlPersonalPage;
    /** link alla pagina della persona (dipende dal contesto) */ 
    private String url;
    /** Flag per specificare se la persona lavora in ateneo a tempo pieno o parziale */
    private boolean tempoPieno;
    /** Note */
    private String note;
    /** Elenco delle competenze della persona */
    private Vector<SkillBean> competenze;
    /** Ruolo della persona  */
    private Vector<CodeBean> ruoli;
    /** Campo note contenente una descrizione testuale del ruolo della persona */
    private String ruolo;
    /** Flag specificante se trattasi di un PMO di Ateneo */
    private boolean pmoAteneo;
    /** Campo note contenente la presentazione della persona */
    private String presentazione;
    /** Flag specificante se si tratta di una persona non appartenente all'ateneo */
    private boolean esterno;
    /** Ateneo o struttura di provenienza (in genere in caso di relatori di seminari o visiting professor) */
    private String universita;
    /** Attributo per codice SSD del docente */
    private String codiceSettore;
    /** Attributo per descrizione SSD di appartenenza del docente */
    private String descrizioneSettore;
    /** Nome ciclo di dottorato */
    private String nomeCiclo;
    /** Nome corso di studi della persona */
    private String nomeCS;
    /** Identificativo del dipartimento cui la persona afferisce */
    protected int idDipartimento;
    /** Nome del dipartimento cui la persona afferisce */
    private String dipartimento;
    /** Indirizzo web del dipartimento cui la persona afferisce */
    protected String urlDipartimento;
    /** Nome della sezione dipartimentale in cui la persona lavora */
    private String sezione;
    /** Carica in organo */
    private String caricaInOrganoCol;
    /** String specificante l'incarico della persona */
    private String mansione;
    /** Identificativo della qualifica principale nel contesto dell'afferenza */
    private int idQualificaPrincipaleDip; // modificato per non superare i 32 caratteri
    /** Denominazione della qualifica principale nel contesto dell'afferenza */
    private String qualificaPrincipaleDip; // modificato per non superare i 32 caratteri

    
    /**
     * <p>Costruttore: inizializza i campi a valori di default.</p>
     */
    public PersonBean() {
        id = idDipartimento = idQualificaPrincipaleDip = -2;
        nome = cognome = codiceFiscale = telefono = telefonoMobile = fax = email = emailCertificata = urlPersonalPage = null;
        note = codiceSettore = descrizioneSettore = null;
        nomeCiclo = nomeCS = null;
        dipartimento = qualificaPrincipaleDip = urlDipartimento = null;
        mansione = sezione = null;
        dataNascita = new Date(0);
        esterno = mostraPersonale = mostraTelefono = tempoPieno = oblio = false;
        sesso = null;
        caricaInOrganoCol = null;
        nomeCiclo = null;
        skypeid = null;
        foto = null;
        competenze = null;
        ruoli = null;
        ruolo = null;
        pmoAteneo = false;
        universita = null;
        url = null;
    }
    

    
    /**
     * @return
     */
    public boolean isDataNascitaEmpty() {
        return (new Date(0).equals(dataNascita) || (dataNascita == null));
    }
     
   
    /**
     * @return
     */
    public boolean isDipartimentoEmpty() {
           return (dipartimento == null || dipartimento.equals(""));
    }
    
    /**
     * @return
     */
    public boolean isUrlDipartimentoEmpty() {
           return (urlDipartimento == null || urlDipartimento.equals(""));
    }
        
    /**
     * @return
     */
    public boolean isTempoPieno() {
        return tempoPieno;
    }
    
    /**
     * @return
     */
    public boolean isMostraTelefono() {
        return mostraTelefono;
    }
    
    
    /**
     * @return codiceSettore
     */
    public String getCodiceSettore() throws AttributoNonValorizzatoException {
        if (codiceSettore == null) {
            throw new AttributoNonValorizzatoException(FOR_NAME + "Attributo codiceSettore non valorizzato!\n");
        } else {
            return this.codiceSettore;
        }
    }
    
    /**
     * @return cognome
     */
    public String getCognome() throws AttributoNonValorizzatoException {
        if (cognome == null) {
            throw new AttributoNonValorizzatoException("PersonBean: attributo cognome non valorizzato!");
        } else {
            return this.cognome;
        }
    }
    

	/**
     * @return dataNascita
     */
    public Date getDataNascita() throws AttributoNonValorizzatoException {
        if (new Date(0).equals(dataNascita)) {
            throw new AttributoNonValorizzatoException("PersonBean: attributo dataNascita non valorizzato!");
        } else {
            return this.dataNascita;
        }
    }
    
    /**
     * @return codiceFiscale
     */
    public String getCodiceFiscale() {
        return this.codiceFiscale;
    }
    
    /**
     * @return descrizioneSettore
     */
    public String getDescrizioneSettore() throws AttributoNonValorizzatoException {
        if (descrizioneSettore == null) {
            throw new AttributoNonValorizzatoException("PersonBean: attributo descrizioneSettore non valorizzato!");
        } else {
            return this.descrizioneSettore;
        }
    }
    
    /**
     * @return email
     */
    public String getEmail() throws AttributoNonValorizzatoException {
        if (email == null) {
            throw new AttributoNonValorizzatoException("PersonBean: attributo email non valorizzato!");
        } else {
            return this.email;
        }
    }
    
    /**
     * @return emailCertificata
     */
    public String getEmailCertificata() throws AttributoNonValorizzatoException {
        if (emailCertificata == null) {
            throw new AttributoNonValorizzatoException("PersonBean: attributo emailCertificata non valorizzato!");
        } else {
            return this.emailCertificata;
        }
    }    
    
    /**
     * @return fax
     */
    public String getFax() throws AttributoNonValorizzatoException {
        if (fax == null) {
            throw new AttributoNonValorizzatoException("PersonBean: attributo fax non valorizzato!");
        } else {
            return this.fax;
        }
    }
    
    /**
     * @return skypeid
     */
    public String getSkypeid() {
        if (skypeid == null) {
            return new String();
        } else {
            return this.skypeid;
        }
    }

    
    /**
     * @return mansione
     */
    public String getMansione() throws AttributoNonValorizzatoException {
        if (mansione == null) {
            throw new AttributoNonValorizzatoException("PersonBean: attributo mansione non valorizzato!");
        } else {
            return this.mansione;
        }
    }
    
    /**
     * @return id
     */
    public int getId() throws AttributoNonValorizzatoException {
        if (id == -2) {
            throw new AttributoNonValorizzatoException("PersonBean: attributo id Persona non valorizzato!");
        } else {
            return this.id;
        }
    }
    
    /**
     * @return idDipartimento
     */
    public int getIdDipartimento() throws AttributoNonValorizzatoException {
        if (idDipartimento == -2) {
            throw new AttributoNonValorizzatoException("PersonBean: attributo idDipartimento non valorizzato!");
        } else {
            return this.idDipartimento;
        }
    }
    
    /**
     * @return nome
     */
    public String getNome() throws AttributoNonValorizzatoException {
        if (nome == null) {
            throw new AttributoNonValorizzatoException("PersonBean: attributo nome non valorizzato!");
        } else {
            return this.nome;
        }
    }
    
    /**
     * @return nomeCiclo
     */
    public String getNomeCiclo() throws AttributoNonValorizzatoException {
        if (nomeCiclo == null) {
            throw new AttributoNonValorizzatoException("PersonBean: attributo nomeCiclo non valorizzato!");
        } else {
            return this.nomeCiclo;
        }
    }
    
    /**
     * @return nomeCS
     */
    public String getNomeCS() throws AttributoNonValorizzatoException {
        if (nomeCS == null) {
            throw new AttributoNonValorizzatoException("PersonBean: attributo nomeCS non valorizzato!");
        } else {
            return this.nomeCS;
        }
    }
    
    /**
     * @return dipartimento
     */
    public String getDipartimento() throws AttributoNonValorizzatoException {
        if (dipartimento == null) {
            throw new AttributoNonValorizzatoException("PersonBean: attributo dipartimento non valorizzato!");
        } else {
            return this.dipartimento;
        }
    }
    
    /**
     * @return note
     */
    public String getNote() throws AttributoNonValorizzatoException {
        if (note == null) {
            throw new AttributoNonValorizzatoException("PersonBean: attributo note non valorizzato!");
        } else {
            return this.note;
        }
    }
    
    /**
     * @return qualificaPrincipaleDip
     */
    public String getQualificaPrincipaleDip() throws AttributoNonValorizzatoException {
        if (qualificaPrincipaleDip == null) {
            throw new AttributoNonValorizzatoException("PersonBean: attributo qualificaPrincipaleDip non valorizzato!");
        } else {
            return this.qualificaPrincipaleDip;
        }
    }
    
    /**
     * @return idQualificaPrincipaleDip
     */
    public int getIdQualificaPrincipaleDip() throws AttributoNonValorizzatoException {
        if (idQualificaPrincipaleDip == -2) {
            throw new AttributoNonValorizzatoException(
            "PersonBean: attributo idQualificaPrincipaleDip non valorizzato!");
        } else {
            return this.idQualificaPrincipaleDip;
        }
    }
    
    /**
     * @return sezione
     */
    public String getSezione() throws AttributoNonValorizzatoException {
        if (sezione == null) {
            throw new AttributoNonValorizzatoException("PersonBean: attributo sezione non valorizzato!");
        } else {
            return this.sezione;
        }
    }
    
    /**
     * @return telefono
     */
    public String getTelefono() throws AttributoNonValorizzatoException {
        if (telefono == null) {
            throw new AttributoNonValorizzatoException("PersonBean: attributo telefono non valorizzato!");
        } else {
            return this.telefono;
        }
    }
    
    /**
     * @return telefonoMobile
     */
    public String getTelefonoMobile() throws AttributoNonValorizzatoException {
        if (telefonoMobile == null) {
            throw new AttributoNonValorizzatoException("PersonBean: attributo telefonoMobile non valorizzato!");
        } else {
            return this.telefonoMobile;
        }
    }
    
    /**
     * @return urlPersonalPage
     */
    public String getUrlPersonalPage() throws AttributoNonValorizzatoException {
        if (urlPersonalPage == null) {
            throw new AttributoNonValorizzatoException("PersonBean: attributo urlPersonalPage non valorizzato!");
        } else {
            return this.urlPersonalPage;
        }
    }
    
    /**
     * @return urlDipartimento
     *
     */
    public String getUrlDipartimento() {
        return this.urlDipartimento;
    }
    
    /**
     * @return caricaInOrganoCol
     */
    public String getCaricaInOrganoCol() throws AttributoNonValorizzatoException {
        if (caricaInOrganoCol == null) {
            throw new AttributoNonValorizzatoException("PersonBean: attributo caricaInOrganoCol non valorizzato!");
        } else {
            return this.caricaInOrganoCol;
        }
    }
    

    /**
     * @param caricaInOrganoCol
     */
    public void setCaricaInOrganoCol(String string) {
        caricaInOrganoCol = string;
    }
    
    /**
     * @param codiceSettore
     */
    public void setCodiceSettore(String string) {
        codiceSettore = string;
    }
    
    /**
     * @param cognome
     */
    public void setCognome(String string) {
        cognome = string;
    }
    
    /**
     * @param dataNascita
     */
    public void setDataNascita(Date date) {
        dataNascita = date;
    }
    
    /**
     * @param codiceFiscale
     */
    public void setCodiceFiscale(String string) {
        codiceFiscale = string;
    }
    
    /**
     * @param descrizioneSettore
     */
    public void setDescrizioneSettore(String string) {
        descrizioneSettore = string;
    }

    
    /**
     * @param email
     */
    public void setEmail(String string) {
        email = string;
    }
    
    /**
     * @param emailCertificata
     */
    public void setEmailCertificata(String string) {
        emailCertificata = string;
    }   
    
    /**
     * @param fax
     */
    public void setFax(String string) {
        fax = string;
    }
    
    /**
     * @param skypeid
     */
    public void setSkypeid(String string) {
        skypeid = string;
    }
    
    /**
     * @param id
     */
    public void setId(int i) {
        id = i;
    }
    
    /**
     * @param idDipartimento
     */
    public void setIdDipartimento(int i) {
        idDipartimento = i;
    }
    
    /**
     * @param idQualificaPrincipaleDip
     */
    public void setIdQualificaPrincipaleDip(int i) {
        idQualificaPrincipaleDip = i;
    }
    
    /**
     * @param mansione
     */
    public void setMansione(String string) {
        mansione = string;
    }
    
    /**
     * @param mostraTelefono
     */
    public void setMostraTelefono(boolean b) {
        mostraTelefono = b;
    }
    
    /**
     * @param mostraTelefono Valori '1' o '0'
     */
    public void setMostraTelefono(char v) {
        if (v == '1') {
            this.mostraTelefono = true;
        } else {
            this.mostraTelefono = false;
        }
    }
    
    /**
     * @param nome
     */
    public void setNome(String string) {
        nome = string;
    }
    
    /**
     * @param nomeCiclo
     */
    public void setNomeCiclo(String string) {
        nomeCiclo = string;
    }
    
    /**
     * @param nomeCS
     */
    public void setNomeCS(String string) {
        nomeCS = string;
    }
    
    /**
     * @param dipartimento
     */
    public void setDipartimento(String string) {
        dipartimento = string;
    }
    
    /**
     * @param note
     */
    public void setNote(String string) {
        note = string;
    }
    
    /**
     * @param qualificaPrincipaleDip
     */
    public void setQualificaPrincipaleDip(String string) {
        qualificaPrincipaleDip = string;
    }
   
    
    /**
     * @param sezione
     */
    public void setSezione(String string) {
        sezione = string;
    }

    
    /**
     * @param telefono
     */
    public void setTelefono(String string) {
        telefono = string;
    }
    
    /**
     * @param telefonoMobile
     */
    public void setTelefonoMobile(String string) {
        telefonoMobile = string;
    }
    
    /**
     * @param tempoPieno Valori '1' o '0'
     */
    public void setTempoPieno(char v) {
        if (v == '1') {
            this.tempoPieno = true;
        } else {
            this.tempoPieno = false;
        }
    }
    
   
    /**
     * @param tempoPieno.
     */
    public void setTempoPieno(boolean v) {
        this.tempoPieno = v;
    }
    
    
    /**
     * @param urlPersonalPage
     */
    public void setUrlPersonalPage(String string) {
        urlPersonalPage = string;
    }
    
    
    /**
     * @param urlDipartimento
     *
     */
    public void setUrlDipartimento(java.lang.String urlDipartimento) {
        this.urlDipartimento = urlDipartimento;
    }

    
    /**
     * Getter for property sesso.
     * @return Value of property sesso.
     */
    public String getSesso() throws AttributoNonValorizzatoException {
        if (sesso == null) {
            throw new AttributoNonValorizzatoException("PersonBean: attributo sesso non valorizzato!");
        } else {
            return this.sesso;
        }
    }
    
    /**
     * Setter for property sesso.
     * @param sesso New value of property sesso.
     */
    public void setSesso(String sesso) {
        // lun giu 13 15:37:39 CEST 2005
        // NO BUONO: non mettere i controlli nel bean altrimenti nel debug
        // si impazzisce (in genere non si va a pensare a inizializzazioni
        // fatte nei bean).
        // Nel caso specifico, nel database i valori NON sono 1 e 0 ma 
        // m ed f. Come mai qui e' stato messo questo controllo di 
        // inizializzazione?
        
        /* if (sesso == '1' ) this.sesso = 'M'; else this.sesso = 'F'; */
        
        if (!sesso.equals("M") && !sesso.equals("F") && !sesso.equals("m") && !sesso.equals("f"))  
            this.sesso = "m";
        else
            this.sesso = sesso;
    }
    
   
    /**
     * Holds value of property creditiInsegnati.
     */
    private String creditiInsegnati;

    /**
     * Getter for property creditiInsegnati.
     * @return Value of property creditiInsegnati.
     */
    public String getCreditiInsegnati() {

        return this.creditiInsegnati;
    }

    /**
     * Setter for property creditiInsegnati.
     * @param creditiInsegnati New value of property creditiInsegnati.
     */
    public void setCreditiInsegnati(String creditiInsegnati) {

        this.creditiInsegnati = creditiInsegnati;
    }
    
    /**
     * @return mostraPersonale
     */
    public boolean isMostraPersonale() {
        return mostraPersonale;
    }
    
    /**
     * @return
     */
    public boolean getMostraPersonale() {
        return mostraPersonale;
    }
  
    /**
     * @param mostraPersonale
     */
    public void setMostraPersonale(boolean b) {
        mostraPersonale = b;
    }
    
    /**
     * @return esterno
     */
    public boolean isEsterno() {
        return esterno;
    }
    
    /**
     * @param esterno
     */
    public void setEsterno(boolean b) {
        esterno = b;
    }
    
    public void setFoto(Vector fotoFileDoc) {
        foto = fotoFileDoc;
    }
    
    public Vector getFoto() {
        return foto;
    }


	/**
	 * @return the universita
	 */
	public String getUniversita() throws AttributoNonValorizzatoException {
		if (this.universita == null) {
			throw new AttributoNonValorizzatoException("PersonBean: attributo universita non valorizzato.");
		} else {
			return universita;
		}
	}

	/**
	 * @param universita the universita to set
	 */
	public void setUniversita(String universita) {
		this.universita = universita;
	}

    /**
     * @param url the url to set
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * @return the url
     */
    public String getUrl() {
        return this.url;
    }

	/**
	 * @return the currFileDoc
	 */
	public Vector getCurrFileDoc() {
		return currFileDoc;
	}

	/**
	 * @param currFileDoc the currFileDoc to set
	 */
	public void setCurrFileDoc(Vector currFileDoc) {
		this.currFileDoc = currFileDoc;
	}

	/**
	 * @return the oblio
	 */
	public boolean isOblio() {
		return oblio;
	}

	/**
	 * @param oblio the oblio to set
	 */
	public void setOblio(boolean oblio) {
		this.oblio = oblio;
	}

    /**
     * @return the presentazione
     */
    public String getPresentazione() {
        return presentazione;
    }


    /**
     * @param presentazione the presentazione to set
     */
    public void setPresentazione(String presentazione) {
        this.presentazione = presentazione;
    }

    
    /* ******************************************************* *
     *         Metodi getter e setter per competenze           *
     * ******************************************************* */
    public void setCompetenze(Vector<SkillBean> argomenti) {
        this.competenze = argomenti;
    }
    
    public Vector<SkillBean> getCompetenze() {
        return this.competenze;
    }
    
    
    /* ******************************************************* *
     *           Metodi getter e setter per ruoli              *
     * ******************************************************* */
    /**
     * Restituisce un vector contenente i ruoli della persona
     * @return <code>ruoli</code> - ruoli della persona nei vari progetti
     */
    public Vector<CodeBean> getRuoli() {
        return ruoli;
    }

    /**
     * Imposta i ruoli che una persona ha nei progetti
     * @param ruoli - ruoli da impostare
     */
    public void setRuoli(Vector<CodeBean> ruoli) {
        this.ruoli = ruoli;
    }

    
    /* ******************************************************* *
     *           Metodi getter e setter per ruolo              *
     * ******************************************************* */
    /**
     * @return the ruolo
     */
    public String getRuolo() throws AttributoNonValorizzatoException {
        if (this.ruolo == null) {
            throw new AttributoNonValorizzatoException("PersonBean: attributo ruolo non valorizzato.");
        } else {
            return ruolo;
        }
    }

    /**
     * @param ruolo the ruolo to set
     */
    public void setRuolo(String ruolo) {
        this.ruolo = ruolo;
    }

    /* ******************************************************* *
     *      Metodi getter e setter per flag PMO di Ateneo      *
     * ******************************************************* */
    public boolean isPmoAteneo() {
        // Se PMOAte è false verifica se è giusto o meno
        if (!this.pmoAteneo) {
            for (CodeBean ruolo : ruoli) {
                if (ruolo.getOrdinale() == PMO_ATE) {
                    return true;
                }
            }
        }
        return pmoAteneo;
    }

    public void setPMOAteneo(boolean pmoAte) {
        pmoAteneo = pmoAte;
    }
    
}