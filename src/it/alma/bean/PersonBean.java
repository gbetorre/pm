/*
 *   uol: University on Line. Applicazione WEB per la visualizzazione
 *   di siti web di Facoltà
 *   Copyright (C) 2000,2001 Roberto Posenato, Mirko Manea
 *   
 *   uol: University on Line. Applicazione WEB per la visualizzazione
 *   del sito web dei dipartimenti, delle ex-facoltà, dell'ateneo, 
 *   delle scuole di dottorato.
 *   University on Line (uol), web application to publish the faculties, 
 *   departments, PhD schools and university information
 *   Copyright (C) renewed 2002 Universita' degli Studi di Verona, 
 *   all right reserved
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
 *   Giovanroberto Torre <giovanroberto.torre@univr.it>
 *   Direzione Informatica
 *   Università degli Studi di Verona
 *   Via Paradiso 6
 *   37129 Verona (Italy)
 */
package it.alma.bean;

import it.alma.exception.AttributoNonValorizzatoException;

import java.sql.Date;
import java.util.Vector;


/**
 * <p>PersonBean &egrave; l'oggetto atto a rappresentare l'elemento
 * pi&uacute; importante, ed uno dei pi&uacute; complessi, dell'applicazione
 * web <code>uol</code>.</p>
 * <p><dl>
 * <dt>Someday:</dt>
 * <dd>lun ago 27 13:14:20 CEST 2007 gtorre</dd>
 * <dd>lun ago 27 13:14:20 CEST 2007 gtorre: + 
       data di fine servizio presso le direzioni</dd>
   <dd>ven gen 12 13:37:55 CET 2007: + PEC</dd>
   <dd>gio set  8 14:32:42 CEST 2005: + flag responsabileUO</dd>
 * </dl></p>
 * 
 * @version 1.20.2.6
 * @author trrgnr59
 * @author <a href="mailto:giovanroberto.torre@univr.it">Giovanroberto Torre</a>
 */
public class PersonBean {
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
    private char sesso;
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
    private Vector foto;
    /** Curriculum personale */
    private Vector currFileDoc;
    /** Url della pagina personale in caso di necessit&agrave; di mostrare la propria persona in ulteriore contesto */
    private String urlPersonalPage;
    /** link alla pagina della persona (dipende dal contesto) */ 
    private String url;
    /** Flag per specificare se la persona lavora in ateneo a tempo pieno o parziale */
    private boolean tempoPieno;
    /** Flag per specificare se la persona &egrave; ancora in servizio o meno (aspettativa, pensione...) */
    private boolean inServizio;
    /** Note in caso di aspettativa o di fine servizio */
    private String note;
    /** Campo note contenente una descrizione testuale del ruolo della persona */
    private String ruolo;
    /** Campo note contenente la presentazione della persona */
    private String presentazione;
    /** Flag specificante se si tratta di una persona non appartenente all'ateneo */
    private boolean esterno;
    /** Ateneo o struttura di provenienza (in genere in caso di relatori di seminari o visiting professor) */
    private String universita;
    /* ************************************************************************ *  
     *                  Dati strettamente per docenti ed affini                 *
     * ************************************************************************ */
    /** Attributo per codice SSD del docente */
    private String codiceSettore;
    /** Attributo per descrizione SSD di appartenenza del docente */
    private String descrizioneSettore;
    /** Nome ciclo di dottorato */
    private String nomeCiclo;
    /** Nome corso di studi della persona */
    private String nomeCS;
    /** Slot di ricevimento studenti */
    private Vector ricevimento;
    /** Descrizioni aggiuntive sul ricevimento studenti */
    private String noteRicevimento;
    /** Flag specificante se il docente &egrave; titolare dell'insegnamento */
    private boolean titolare;
    /** Flag specificante se il docente &egrave; coordinatore dei moduli */
    private boolean coordinatore;
    /** Elenco delle competenze del docente */
    private Vector argomentiComp;
    /** Rilevanza delle pubblicazioni della persona rispetto a una o pi&uacute; keyword */
    private int peso1;
    /** Rilevanza delle competenze della persona rispetto a una o pi&uacute; keyword */
    private int peso2;
    /** Rilevanza della didattica della persona rispetto a una o pi&uacute; keyword */
    private int peso3;
    /* ************************************************************************ *  
     *  Dati identificativi dell'ubicazione e dell'appartenenza della persona   *
     * ************************************************************************ */
    /** Identificativo dell'ufficio della persona */
    private int idUfficio;
    /** Nome del luogo di lavoro della persona */
    private String stanzaUfficio;
    /** Piano ove &egrave; ubicato l'ufficio della persona */
    private String pianoUfficio;
    /** Edificio ove &egrave; ubicato l'ufficio della persona */
    private String edificioUfficio;
    /** Elenco uffici di lavoro della persona */
    //private Vector<LuogoBean> uffici;
    /** Data di fine servizio presso la scuola/struttura di raccordo */
    private Date fineServizio;
    /** Data di fine servizio presso il dipartimento */
    private Date fineServizioDipart;
    /** Data di fine servizio presso le direzioni */
    private Date fineServizioDir;
    /** Data di fine servizio presso biblioteca o centro di ateneo */
    private Date fineServizioBiblioCR;
    /** Ultima data in ordine temporale di fine servizio */ 
    private Date fineServizioMax;
    /** Identificativo della scuola/struttura di raccordo della persona */
    protected int idFacolta;
    /** Nome della scuola/struttura di raccordo cui la persona appartiene */
    private String facolta;
    /** Indirizzo web della scuola/struttura di raccordo della persona */
    private String urlFacolta;
    /** Data disattivazione della scuola/struttura di raccordo della persona */
    private Date dataDisattivazioneFac;
    /** Identificativo del dipartimento cui la persona afferisce */
    protected int idDipartimento;
    /** Nome del dipartimento cui la persona afferisce */
    private String dipartimento;
    /** Indirizzo web del dipartimento cui la persona afferisce */
    protected String urlDipartimento;
    /** Data disattivazione del dipartimento cui la persona afferiva */
    private Date dataDisattivazioneDip;
    /** Identificativo del nuovo dipartimento cui la persona afferisce */
    private int idContinuazioneDip;
    /** Flag indicante se il dipartimento della persona &egrave; attivando */
    private boolean inAttivazioneDip;
    /** Identificativo della direzione di appartenenza della persona */
    protected int idDirezione;
    /** Nome della direzione cui la persona appartiene */
    private String nomeDirezione;
    /** Flag specificante se la direzione in questione &egrave; la direzione amministrativa */
    private boolean direzioneRadice;
    /** Identificativo della biblioteca centralizzata o centro di ateneo della persona */
    private int idBibliocr;
    /** Nome della biblioteca centralizzata o centro di ateneo della persona */
    private String nomeBibliocr;
    /** Indirizzo web della biblioteca centralizzata o centro di ateneo della persona */
    private String urlBibliocr;
    /** Identificativo del tipo della biblioteca centralizzata o centro di ateneo della persona */
    private int idTipoBiblioCR;
    /** Nome della sezione dipartimentale in cui la persona lavora */
    private String sezione;    
    /* ************************************************************************ *  
     *                       Qualifica / Carica / Mansione                      *
     * ************************************************************************ */
    /** Carica in organo */
    private String caricaInOrganoCol;
    /** Flag specificante se trattasi di responsabile di area */
    private boolean responsabileArea;
    /** String specificante l'incarico nel contesto dell'area */
    private String incaricoInArea;
    /** Flag specificante se trattasi di responsabile ufficio */
    private boolean responsabileUO;
    /** String specificante l'incarico della persona */
    private String mansione;
    /** Identificativo della qualifica principale nel contesto dell'appartenenza */
    private int idQualificaPrincipaleFacolta;
    /** Denominazione della qualifica principale nel contesto dell'appartenenza */
    private String qualificaPrincipaleFacolta;
    /** Identificativo della qualifica principale nel contesto dell'afferenza */
    private int idQualificaPrincipaleDip; // modificato per non superare i 32 caratteri
    /** Denominazione della qualifica principale nel contesto dell'afferenza */
    private String qualificaPrincipaleDip; // modificato per non superare i 32 caratteri
    /** Identificativo della qualifica principale nel contesto dell'appartenenza a direzione */
    private int idQualificaDirezione;
    /** Denominazione della qualifica principale nel contesto dell'appartenenza a direzione */
    private String nomeQualificaDirezione;
    /** Identificativo della qualifica principale nel contesto dell'appartenenza a biblioteca centralizzata o centro di ateneo */
    private int idQualificaBibliocr;
    /** Denominazione della qualifica principale nel contesto dell'appartenenza a biblioteca centralizzata o centro di ateneo */
    private String nomeQualificaBibliocr;  
    /** Codice specificante l'incarico della persona nel contesto della biblioteca (se coordinatore, appartenente, etc.) */
    private char ruoloBibliocr;
    /** String specificante la descrizione dell'incarico della persona nel contesto bibliotecario o di centro */
    private String incaricoBibliocr;
    /** Elenco delle afferenze secondarie */
    //private Vector<QualificaBean> qualificheAfferenzeSecondarie;
    /** Flag specificante se trattasi di un tecnico di ex-facolt&agrave; */
    private boolean isTAFacolta;
    /* ************************************************************************ *  
     *               Variabili per lingua / internazionalizzazione              *
     * ************************************************************************ */
    private String linguaNote;
    private String linguaDescrizioneSettore;
    private String linguaCaricaInOrganoCol;
    private String linguaFacolta;
    private String linguaDipartimento;
    private String linguaQualificaPrincipaleFacolta;
    private String linguaQualificaPrincipaleDip;
    private String linguaSezione;
    private String linguaNoteRicevimento;
    private String linguaNomeDirezione;
    private String linguaNomeQualificaDirezione;
    private String linguaNomeBibliocr;
    private String linguaNomeQualificaBibliocr;
    private String linguaPianoUfficio;
    private String linguaNomeCS;
    
    
    /**
     * <p>Costruttore: inizializza i campi a valori di default.</p>
     */
    public PersonBean() {
        id = idDipartimento = idFacolta = idUfficio = idQualificaPrincipaleFacolta = idQualificaPrincipaleDip = -2;
        nome = cognome = telefono = telefonoMobile = fax = email = emailCertificata = urlPersonalPage = null;
        note = linguaNote = codiceSettore = descrizioneSettore = noteRicevimento = linguaNoteRicevimento = null;
        stanzaUfficio = pianoUfficio = edificioUfficio = null;
        nomeCiclo = nomeCS = linguaNomeCS = null;
        facolta = qualificaPrincipaleFacolta = linguaQualificaPrincipaleFacolta = urlFacolta = linguaFacolta = null;
        dipartimento = qualificaPrincipaleDip = linguaQualificaPrincipaleDip = urlDipartimento = linguaDipartimento = null;
        mansione = sezione = linguaSezione = null;
        dataNascita = fineServizioDipart = fineServizio = fineServizioDir = fineServizioBiblioCR = dataDisattivazioneDip = dataDisattivazioneFac = setFineServizioMax(new Date(0));
        esterno = mostraPersonale = mostraTelefono = tempoPieno = inServizio = titolare = coordinatore = responsabileArea = responsabileUO = inAttivazioneDip = oblio = false;
        idDirezione = idBibliocr = idQualificaDirezione = idQualificaBibliocr = -2;
        linguaNomeDirezione = nomeDirezione = nomeQualificaDirezione = linguaNomeQualificaDirezione = null;
        linguaNomeBibliocr = nomeBibliocr = nomeQualificaBibliocr = linguaNomeQualificaBibliocr = urlBibliocr = incaricoBibliocr = null;
        sesso = ' ';
        ruoloBibliocr = ' ';
        caricaInOrganoCol = linguaCaricaInOrganoCol = null;
        nomeCiclo = null;
        incaricoInArea = null;
        skypeid=null;
        foto=null;
        argomentiComp = null;
        //uffici = null;
        idContinuazioneDip = -2;
        universita = null;
        direzioneRadice = false;
        ruolo = null;
        //qualificheAfferenzeSecondarie = new Vector<QualificaBean>();
        url=null;
        isTAFacolta = false;
        idTipoBiblioCR = peso1 = peso2 = -2;
    }
    

    /**
     * @return
     */
    public boolean isDataDisattivazioneDipEmpty() {
        return (new Date(0).equals(dataDisattivazioneDip) || (dataDisattivazioneDip == null));
    } 
    
    /**
     * @return
     */
    public boolean isDataDisattivazioneFacEmpty() {
        return (new Date(0).equals(dataDisattivazioneFac) || (dataDisattivazioneFac == null));
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
    public boolean isFineServizioEmpty() {
        return (new Date(0).equals(fineServizio) || (fineServizio == null));
    }
    
    /**
     * @return
     */
    public boolean isFineServizioDipartEmpty() {
        return (new Date(0).equals(fineServizioDipart) || (fineServizioDipart == null));
    }
    
    /**
     * @return
     */
    public boolean isFineServizioDirEmpty() {
        return (new Date(0).equals(fineServizioDir) || (fineServizioDir == null));
    }    
    
    /**
     * @return
     */
    public boolean isFineServizioBiblioCREmpty() {
        return (new Date(0).equals(fineServizioBiblioCR) || (fineServizioBiblioCR == null));
    }    
    
    /**
     * @return
     */
    public boolean isFacoltaEmpty() {
           return (facolta == null || facolta.equals(""));
    }

    /**
     * @return
     */
    public boolean isUrlFacoltaEmpty() {
           return (urlFacolta == null || urlFacolta.equals(""));
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
    public boolean isNomeDirezioneEmpty() {
           return (nomeDirezione == null || nomeDirezione.equals(""));
    }

     /**
     * @return
     */
    public boolean isNomeBibliocrEmpty() {
           return (nomeBibliocr == null || nomeBibliocr.equals(""));
    }    

    /**
     * @return
     */
    public boolean isUrlBibliocrEmpty() {
           return (urlBibliocr == null || urlBibliocr.equals(""));
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
    public boolean isInServizio() {
        return inServizio;
    }
    
    /**
     * @return
     */
    public boolean isMostraTelefono() {
        return mostraTelefono;
    }
    
    /**
     * @return
     */
    public boolean isTitolare() {
        return titolare;
    }
    
    /**
     * @return
     */
    public boolean isCoordinatore() {
        return coordinatore;
    }

    public boolean isInAttivazioneDip() {
		return inAttivazioneDip;
	}

	public void setInAttivazioneDip(boolean inAttivazioneDip) {
		this.inAttivazioneDip = inAttivazioneDip;
	}

    /**
     * @param InAttivazioneDip Valori '1' o '0'
     */
    public void setInAttivazioneDip(char inAttivazioneDip) {
        if (inAttivazioneDip == '1') {
            this.inAttivazioneDip = true;
        } else {
            this.inAttivazioneDip = false;
        }
    }
	
	
    
    /**
     * @return codiceSettore
     */
    public String getCodiceSettore() throws AttributoNonValorizzatoException {
        if (codiceSettore == null) {
            throw new AttributoNonValorizzatoException("PersonBean: attributo codiceSettore non valorizzato!");
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
    
    public Date getDataDisattivazioneDip() {
		return dataDisattivazioneDip;
	}
    
    public Date getDataDisattivazioneFac() {
        return dataDisattivazioneFac;
    }

	public void setDataDisattivazioneDip(Date dataDisattivazioneDip) {
		this.dataDisattivazioneDip = dataDisattivazioneDip;
	}
	
	public void setDataDisattivazioneFac(Date dataDisattivazioneFac) {
        this.dataDisattivazioneFac = dataDisattivazioneFac;
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
     * @return edificioUfficio
     */
    public String getEdificioUfficio() throws AttributoNonValorizzatoException {
        if (edificioUfficio == null) {
            throw new AttributoNonValorizzatoException("PersonBean: attributo edificioUfficio non valorizzato!");
        } else {
            return this.edificioUfficio;
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
     * @return fineServizio
     */
    public Date getFineServizio() throws AttributoNonValorizzatoException {
        if (new Date(0).equals(fineServizio)) {
            throw new AttributoNonValorizzatoException("PersonBean: attributo fineServizio non valorizzato!");
        } else {
            return this.fineServizio;
        }
    }
    
    /**
     * @return fineServizioDipart
     */
    public Date getFineServizioDipart() throws AttributoNonValorizzatoException {
        if (new Date(0).equals(fineServizioDipart)) {
            throw new AttributoNonValorizzatoException("PersonBean: attributo fineServizioDipart non valorizzato!");
        } else {
            return this.fineServizioDipart;
        }
    }
    
    /**
     * @return fineServizioDir
     */
    public Date getFineServizioDir() throws AttributoNonValorizzatoException {
        if (new Date(0).equals(fineServizioDir)) {
            throw new AttributoNonValorizzatoException("PersonBean: attributo fineServizioDir non valorizzato!");
        } else {
            return this.fineServizioDir;
        }
    }

    /**
     * @return fineServizioBiblioCR
     */
    public Date getFineServizioBiblioCR() throws AttributoNonValorizzatoException {
        if (new Date(0).equals(fineServizioBiblioCR)) {
            throw new AttributoNonValorizzatoException("PersonBean: attributo fineServizioBiblioCR non valorizzato!");
        } else {
            return this.fineServizioBiblioCR;
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
     * @return idFacolta
     */
    public int getIdFacolta() throws AttributoNonValorizzatoException {
        if (idFacolta == -2) {
            throw new AttributoNonValorizzatoException("PersonBean: attributo idFacolta non valorizzato!");
        } else {
            return this.idFacolta;
        }
    }
    
    /**
     * @return idUfficio
     */
    public int getIdUfficio() throws AttributoNonValorizzatoException {
        if (idUfficio == -2) {
            throw new AttributoNonValorizzatoException("PersonBean: attributo idUfficio non valorizzato!");
        } else {
            return this.idUfficio;
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
     * @return facolta
     */
    public String getFacolta() throws AttributoNonValorizzatoException {
        if (facolta == null) {
            throw new AttributoNonValorizzatoException("PersonBean: attributo facolta non valorizzato!");
        } else {
            return this.facolta;
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
     * @return pianoUfficio
     */
    public String getPianoUfficio() throws AttributoNonValorizzatoException {
        if (pianoUfficio == null) {
            throw new AttributoNonValorizzatoException("PersonBean: attributo pianoUfficio non valorizzato!");
        } else {
            return this.pianoUfficio;
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
     * @return qualificaPrincipaleFacolta
     */
    public String getQualificaPrincipaleFacolta() throws AttributoNonValorizzatoException {
        if (qualificaPrincipaleFacolta == null) {
            throw new AttributoNonValorizzatoException(
            "PersonBean: attributo qualificaPrincipaleFacolta non valorizzato!");
        } else {
            return this.qualificaPrincipaleFacolta;
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
     * @return stanzaUfficio
     */
    public String getStanzaUfficio() throws AttributoNonValorizzatoException {
        if (stanzaUfficio == null) {
            throw new AttributoNonValorizzatoException("PersonBean: attributo stanzaUfficio non valorizzato!");
        } else {
            return this.stanzaUfficio;
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
    
    public int getIdQualificaPrincipaleFacolta() throws AttributoNonValorizzatoException {
        if (idQualificaPrincipaleFacolta == -2) {
            throw new AttributoNonValorizzatoException(
            "PersonBean: attributo idQualificaPrincipaleFacolta non valorizzato!");
        } else {
            return this.idQualificaPrincipaleFacolta;
        }
    }
    
    /**
     * @return urlFacolta
     */
    public String getUrlFacolta() {
        return this.urlFacolta;
    }

    /**
     * @return urlFacolta
     */
    public String getUrlBibliocr() throws AttributoNonValorizzatoException {
        if (urlBibliocr == null) {
            throw new AttributoNonValorizzatoException("PersonBean: attributo urlBibliocr non valorizzato!");
        } else {
            return this.urlBibliocr;
        }
    }
    
    /**
     * Ritorna l'id della direzione
     *
     * @return
     *
     * @throws
     */
    public int getIdDirezione() throws AttributoNonValorizzatoException {
 /*       if ( idDirezione == -2 ) {
            String error = "PersonBean: attributo 'idDirezione' non valorizzato.";
            throw new AttributoNonValorizzatoException(error);
        } else {*/
            return idDirezione;
 //       }
    }
    
    /**
     * Ritorna l'id della biblioteca o centro di Ateneo
     *
     * @return
     *
     * @throws
     */
    public int getIdBibliocr() throws AttributoNonValorizzatoException {
/*        if ( idBibliocr == -2 ) {
            String error = "PersonBean: attributo 'idBibliocr' non valorizzato.";
            throw new AttributoNonValorizzatoException(error);
        } else {*/
            return idBibliocr;
//        }
    }
    
    
    /**
     * Ritorna il nome della direzione.
     *
     * @return
     *
     * @throws
     */
    public String getNomeDirezione() throws AttributoNonValorizzatoException {
        if ( nomeDirezione == null ) {
            String error = "PersonBean: attributo 'nomeDirezione' non valorizzato.";
            throw new AttributoNonValorizzatoException(error);
        } else {
            return nomeDirezione;
        }
    }

    /**
     * Ritorna il nome della biblioteca o centro di ateneo.
     *
     * @return
     *
     * @throws
     */
    public String getNomeBibliocr() throws AttributoNonValorizzatoException {
        if ( nomeBibliocr == null ) {
            String error = "PersonBean: attributo 'nomeBibliocr' non valorizzato.";
            throw new AttributoNonValorizzatoException(error);
        } else {
            return nomeBibliocr;
        }
    }
    
    /**
     * Ritorna l'id della qualifica nella direzione.
     *
     * @return
     *
     * @throws
     */
    public int getIdQualificaDirezione() throws AttributoNonValorizzatoException {
        if ( idQualificaDirezione == -2 ) {
            String error = "PersonBean: attributo 'idQualificaDirezione' non valorizzato.";
            throw new AttributoNonValorizzatoException(error);
        } else {
            return idQualificaDirezione;
        }
    }
    
    /**
     * Ritorna l'id della qualifica nella biblioteca o centro di ateneo.
     *
     * @return
     *
     * @throws
     */
    public int getIdQualificaBibliocr() throws AttributoNonValorizzatoException {
        if ( idQualificaBibliocr == -2 ) {
            String error = "PersonBean: attributo 'idQualificaBibliocr' non valorizzato.";
            throw new AttributoNonValorizzatoException(error);
        } else {
            return idQualificaBibliocr;
        }
    }
   
    
    /**
     * Ritorna l'id della qualifica nella direzione.
     *
     * @return
     *
     * @throws
     */
    public String getNomeQualificaDirezione() throws AttributoNonValorizzatoException {
        if ( nomeQualificaDirezione == null ) {
            String error = "PersonBean: attributo 'nomeQualificaDirezione' non valorizzato.";
            throw new AttributoNonValorizzatoException(error);
        } else {
            return nomeQualificaDirezione;
        }
    }

    /**
     * Ritorna l'id della qualifica nella biblioteca o centro di ateneo.
     *
     * @return
     *
     * @throws
     */
    public String getNomeQualificaBibliocr() throws AttributoNonValorizzatoException {
        if ( nomeQualificaBibliocr == null ) {
            String error = "PersonBean: attributo 'nomeQualificaBibliocr' non valorizzato.";
            throw new AttributoNonValorizzatoException(error);
        } else {
            return nomeQualificaBibliocr;
        }
    }

    /**
     * Restituisce il ruolo che la persona riveste nella biblioteca o centro di dipartimento.
     * Il ruolo è un attributo facoltativo.
     *
     * @author trrgnr59
     * @return <code>char ruoloBibliocr</code> - Il ruolo della persona nella biblioteca/centro.
     */
    public char getRuoloBibliocr() {
        return ruoloBibliocr;
    }
    
    /**
     * Restituisce l'incarico che la persona ricopre nella biblioteca o centro di dipartimento.
     * L'incarico è un attributo facoltativo.
     *
     * @author trrgnr59
     * @return <code>String incaricoBibliocr</code> - L'incarico della persona nella biblioteca/centro.
     */
    public String getIncaricoBibliocr() {
        return incaricoBibliocr;
    }
    
    public String getIncaricoInArea() throws AttributoNonValorizzatoException {
        if ( incaricoInArea == null ) {
            String error = "PersonBean: attributo 'incaricoInArea' non valorizzato.";
            throw new AttributoNonValorizzatoException(error);
        } else {
            return incaricoInArea;
        }        
    }
    
    public boolean isResponsabileArea() throws AttributoNonValorizzatoException {
            return responsabileArea;     
    }
    
     public boolean isResponsabileUO() throws AttributoNonValorizzatoException {
        return responsabileUO;
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
     * @param descrizioneSettore
     */
    public void setDescrizioneSettore(String string) {
        descrizioneSettore = string;
    }
    
    /**
     * @param edificioUfficio
     */
    public void setEdificioUfficio(String string) {
        edificioUfficio = string;
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
     * @param fineServizio
     */
    public void setFineServizio(Date date) {
        fineServizio = date;
    }
    
    /**
     * @param fineServizioDipart
     */
    public void setFineServizioDipart(Date date) {
        fineServizioDipart = date;
    }
    
    /**
     * @param fineServizioDir
     */
    public void setFineServizioDir(Date date) {
        fineServizioDir = date;
    }    
    
    /**
     * @param fineServizioBiblioCR
     */
    public void setFineServizioBiblioCR(Date date) {
        fineServizioBiblioCR = date;
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
     * @param idFacolta
     */
    public void setIdFacolta(int i) {
        idFacolta = i;
    }
    
    /**
     * @param idQualificaPrincipaleDip
     */
    public void setIdQualificaPrincipaleDip(int i) {
        idQualificaPrincipaleDip = i;
    }
    
    /**
     * @param idQualificaPrincipaleFacolta
     */
    public void setIdQualificaPrincipaleFacolta(int idQualifica) {
        idQualificaPrincipaleFacolta = idQualifica;
    }
    
    /**
     * @param idUfficio
     */
    public void setIdUfficio(int i) {
        idUfficio = i;
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
     * @param facolta
     */
    public void setFacolta(String string) {
        facolta = string;
    }
    
    /**
     * @param note
     */
    public void setNote(String string) {
        note = string;
    }
    
    /**
     * @param pianoUfficio
     */
    public void setPianoUfficio(String string) {
        pianoUfficio = string;
    }
    
    /**
     * @param qualificaPrincipaleDip
     */
    public void setQualificaPrincipaleDip(String string) {
        qualificaPrincipaleDip = string;
    }
    
    /**
     * @param qualificaPrincipaleFacolta
     */
    public void setQualificaPrincipaleFacolta(String string) {
        qualificaPrincipaleFacolta = string;
    }
    
    
    /**
     * @param sezione
     */
    public void setSezione(String string) {
        sezione = string;
    }
    
    /**
     * @param stanzaUfficio
     */
    public void setStanzaUfficio(String string) {
        stanzaUfficio = string;
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
     * @param inServizio Valori '1' o '0'
     */
    public void setInServizio(char v) {
        if (v == '1') {
            this.inServizio = true;
        } else {
            this.inServizio = false;
        }
    }
    
    
    /**
     * @param tempoPieno.
     */
    public void setTempoPieno(boolean v) {
        this.tempoPieno = v;
    }
    
    /**
     * @param inServizio.
     */
    public void setInServizio(boolean v) {
        this.inServizio = v;
    }
    
    /**
     * @param urlPersonalPage
     */
    public void setUrlPersonalPage(String string) {
        urlPersonalPage = string;
    }
    
    /**
     * @param urlFacolta
     *
     */
    public void setUrlFacolta(String urlFacolta) {
        this.urlFacolta = urlFacolta;
    }
    
    /**
     * @param urlBibliocr
     *
     */
    public void setUrlBibliocr(String urlBibliocr) {
        this.urlBibliocr = urlBibliocr;
    }
    
    /**
     * @param urlDipartimento
     *
     */
    public void setUrlDipartimento(java.lang.String urlDipartimento) {
        this.urlDipartimento = urlDipartimento;
    }
    
    /**
     * @return ricevimento
     */
    public Vector getRicevimento() {
        return ricevimento;
    }
    
    /**
     * @param Vector ricevimento
     */
    public void setRicevimento(Vector vector) {
        ricevimento = vector;
    }
    
    /**
     * noteRicevimento rappresenta eventuali note che un docente vuole pubblicare
     * insieme/al posto dell'orario di ricevimento
     * @return noteRicevimento
     */
    public String getNoteRicevimento() throws AttributoNonValorizzatoException {
        if (this.noteRicevimento == null) {
            throw new AttributoNonValorizzatoException("PersonBean: attributo noteRicevimento non valorizzato!");
        } else {
            return this.noteRicevimento;
        }
    }
    
    /**
     * noteRicevimento rappresenta eventuali note che un docente vuole pubblicare
     * insieme/al posto dell'orario di ricevimento
     * @return noteRicevimento
     */
    public void setNoteRicevimento(String note) {
        this.noteRicevimento = note;
    }
    
    /**
     * @param titolare
     */
    public void setTitolare(boolean b) {
        titolare = b;
    }
    
    /**
     * @param titolare Valori '1' o '0'
     */
    public void setTitolare(char v) {
        if (v == '1') {
            this.titolare = true;
        } else {
            this.titolare = false;
        }
    }

    /**
     * @param coordinatore
     */
    public void setCoordinatore(boolean b) {
        coordinatore = b;
    }
                                                                                
    /**
     * @param coordinatore Valori '1' o '0'
     */
    public void setCoordinatore(char v) {
        if (v == '1') {
            this.coordinatore = true;
        } else {
            this.coordinatore = false;
        }
    }
  
    /**
     * Imposta l'id della direzione
     *
     * @param id
     */
    public void setIdDirezione(int id) {
        idDirezione = id;
    }
    
    /**
     * Imposta l'id della biblioteca o centro di Ateneo
     *
     * @param id
     */
    public void setIdBibliocr(int id) {
        idBibliocr = id;
    }
    
    /**
     * Imposta il nome della direzione.
     *
     * @param nome
     */
    public void setNomeDirezione(String nome) {
        nomeDirezione = nome;
    }
    
    /**
     * Imposta l'id della qualifica nella direzione.
     *
     * @return
     */
    public void setIdQualificaDirezione(int id) {
        idQualificaDirezione = id;
    }
    
    /**
     * Imposta il nome della qualifica nella direzione.
     *
     * @return
     */
    public void setNomeQualificaDirezione(String nome) {
        nomeQualificaDirezione = nome;
    }
        
    /**
     * Imposta il nome della biblioteca o centro di ateneo.
     *
     * @param nome
     */
    public void setNomeBibliocr(String nome) {
        nomeBibliocr = nome;
    }
    
    /**
     * Imposta l'id della qualifica nella biblioteca o centro di ateneo.
     *
     * @return
     */
    public void setIdQualificaBibliocr(int id) {
        idQualificaBibliocr = id;
    }
    
    /**
     * Imposta il nome della qualifica nella biblioteca o centro di ateneo.
     *
     * @return
     */
    public void setNomeQualificaBibliocr(String nome) {
        nomeQualificaBibliocr = nome;
    }
    
    /**
     * Imposta il nome del ruolo che la persona riveste nella biblioteca o centro di dipartimento.
     * Il ruolo è un attributo facoltativo.
     *
     * @author trrgnr59
     * @param ruoloBibliocr il valore del ruolo rivestito dalla persona nel contesto della biblioteca/centro.
     */
    public void setRuoloBibliocr(char ruoloBibliocr) {
        this.ruoloBibliocr = ruoloBibliocr;
    }
    
    /**
     * Imposta la descrizione dell'incarico che la persona ricopre nella biblioteca o centro di dipartimento.
     * L'incarico è un attributo facoltativo.
     *
     * @author trrgnr59
     * @param incaricoBibliocr il valore dell'incarico che la persona ricopre nella biblioteca/centro.
     */
    public void setIncaricoBibliocr(String incaricoBibliocr) {
        this.incaricoBibliocr = incaricoBibliocr;
    }
    
    /**
     * Getter for property sesso.
     * @return Value of property sesso.
     */
    public char getSesso() throws AttributoNonValorizzatoException {
        if (sesso == ' ') {
            throw new AttributoNonValorizzatoException("PersonBean: attributo sesso non valorizzato!");
        } else {
            return this.sesso;
        }
    }
    
    /**
     * Setter for property sesso.
     * @param sesso New value of property sesso.
     */
    public void setSesso(char sesso) {
        // lun giu 13 15:37:39 CEST 2005
        // NO BUONO: non mettere i controlli nel bean altrimenti nel debug
        // si impazzisce (in genere non si va a pensare a inizializzazioni
        // fatte nei bean).
        // Nel caso specifico, nel database i valori NON sono 1 e 0 ma 
        // m ed f. Come mai qui e' stato messo questo controllo di 
        // inizializzazione?
        
        /* if (sesso == '1' ) this.sesso = 'M'; else this.sesso = 'F'; */
        
        if ((sesso != 'M') && (sesso != 'F') && (sesso != 'm') && (sesso != 'f'))  
            this.sesso = 'm';
        else
            this.sesso = sesso;
    }
    
    /**
     * Getter for property linguaNote.
     * @return Value of property linguaNote.
     */
    public String getLinguaNote() throws AttributoNonValorizzatoException {
        if (linguaNote == null) {
            throw new AttributoNonValorizzatoException("PersonBean: attributo linguaNote non valorizzato!");
        } else {
            return this.linguaNote;
        }
    }
    
    /**
     * Setter for property linguaNote.
     * @param linguaNote New value of property linguaNote.
     */
    public void setLinguaNote(String linguaNote) {
        this.linguaNote = linguaNote;
    }
    
    /**
     * Getter for property linguaDescrizioneSettore.
     * @return Value of property linguaDescrizioneSettore.
     */
    public String getLinguaDescrizioneSettore() throws AttributoNonValorizzatoException {
        if (linguaDescrizioneSettore == null) {
            throw new AttributoNonValorizzatoException("PersonBean: attributo linguaDescrizioneSettore non valorizzato!");
        } else {
            return this.linguaDescrizioneSettore;
        }
    }
    
    /**
     * Setter for property linguaDescrizioneSettore.
     * @param linguaDescrizioneSettore New value of property linguaDescrizioneSettore.
     */
    public void setLinguaDescrizioneSettore(String linguaDescrizioneSettore) {
        this.linguaDescrizioneSettore = linguaDescrizioneSettore;
    }
    
    
    /**
     * Setter for property linguaFacolta.
     * @param linguaFacolta New value of property linguaFacolta.
     */
    public void setLinguaFacolta(String linguaFacolta) {
        this.linguaFacolta = linguaFacolta;
    }
    
    
    
    public String getLinguaNomeCS() throws AttributoNonValorizzatoException {
        if (linguaNomeCS == null) {
            throw new AttributoNonValorizzatoException("PersonBean: attributo linguaNomeCS non valorizzato!");
        } else {
            return this.linguaNomeCS;
        }
    }
    
    public void setLinguaNomeCS(String string) {
        linguaNomeCS = string;
    }
    
    public String getLinguaFacolta() throws AttributoNonValorizzatoException {
        if (linguaFacolta == null) {
            throw new AttributoNonValorizzatoException("PersonBean: attributo linguaFacolta non valorizzato!");
        } else {
            return this.linguaFacolta;
        }
    }
    
    
    /**
     * Setter for property linguaDipartimeto.
     * @param linguaDipartimeto New value of property linguaDipartimeto.
     */
    public void setLinguaDipartimento(String lingua) {
        this.linguaDipartimento = lingua;
    }
    
    public String getLinguaDipartimento() throws AttributoNonValorizzatoException {
        if (linguaDipartimento == null) {
            throw new AttributoNonValorizzatoException("PersonBean: attributo linguaDipartimento non valorizzato!");
        } else {
            return this.linguaDipartimento;
        }
    }
    
    
    /**
     * Setter for property linguaQualificaPrincipaleFacolta.
     * @param linguaQualificaPrincipaleFacolta New value of property linguaQualificaPrincipaleFacolta.
     */
    public void setLinguaQualificaPrincipaleFacolta(String linguaQualificaPrincipaleFacolta) {
        this.linguaQualificaPrincipaleFacolta = linguaQualificaPrincipaleFacolta;
    }
    
    public String getLinguaQualificaPrincipaleFacolta() throws AttributoNonValorizzatoException {
        if (linguaQualificaPrincipaleFacolta == null) {
            throw new AttributoNonValorizzatoException(
            "PersonBean: attributo linguaQualificaPrincipaleFacolta non valorizzato!");
        } else {
            return this.linguaQualificaPrincipaleFacolta;
        }
    }
    
    
    /**
     * Setter for property linguaQualificaPrincipaleDip.
     * @param linguaQualificaPrincipaleDip New value of property linguaQualificaPrincipaleDip.
     */
    public void setLinguaQualificaPrincipaleDip(String linguaQualificaPrincipaleDip) {
        this.linguaQualificaPrincipaleDip = linguaQualificaPrincipaleDip;
    }
    
    public String getLinguaCaricaInOrganoCol() throws AttributoNonValorizzatoException {
        if (linguaCaricaInOrganoCol == null) {
            throw new AttributoNonValorizzatoException("PersonBean: attributo linguaCaricaInOrganoCol non valorizzato!");
        } else {
            return this.linguaCaricaInOrganoCol;
        }
    }
    
    /**
     * Setter for property linguaCaricaInOrganoCol.
     * @param linguaCaricaInOraganoCol New value of property linguaCaricaInOrganoCol.
     */
    public void setLinguaCaricaInOrganoCol(String lingua) {
        this.linguaCaricaInOrganoCol = lingua;
    }
    
    public String getLinguaSezione() throws AttributoNonValorizzatoException {
        if (linguaSezione == null) {
            throw new AttributoNonValorizzatoException("PersonBean: attributo linguaSezione non valorizzato!");
        } else {
            return this.linguaSezione;
        }
    }
    
    /**
     * Setter for property linguaSezione.
     * @param linguaSezione New value of property linguaSezione.
     */
    public void setLinguaSezione(String linguaSezione) {
        this.linguaSezione = linguaSezione;
    }
    
    public String getLinguaQualificaPrincipaleDip() throws AttributoNonValorizzatoException {
        if (linguaQualificaPrincipaleDip == null) {
            throw new AttributoNonValorizzatoException("PersonBean: attributo linguaQualificaPrincipaleDip non valorizzato!");
        } else {
            return this.linguaQualificaPrincipaleDip;
        }
    }
    
    public String getLinguaNoteRicevimento() throws AttributoNonValorizzatoException {
        if (this.linguaNoteRicevimento == null) {
            throw new AttributoNonValorizzatoException("PersonBean: attributo linguaNoteRicevimento non valorizzato!");
        } else {
            return this.linguaNoteRicevimento;
        }
    }
    
    /**
     * Setter for property linguaNoteRicevimento.
     * @param linguaNoteRicevimento New value of property linguaNoteRicevimento.
     */
    public void setLinguaNoteRicevimento(String linguaNoteRicevimento) {
        this.linguaNoteRicevimento = linguaNoteRicevimento;
    }
    
    public String getLinguaNomeDirezione() throws AttributoNonValorizzatoException {
        if ( linguaNomeDirezione == null ) {
            String error = "PersonBean: attributo 'linguaNomeDirezione' non valorizzato.";
            throw new AttributoNonValorizzatoException(error);
        } else {
            return linguaNomeDirezione;
        }
    }
    
    /**
     * Setter for property linguaNomeDirezione.
     * @param linguaNomeDirezione New value of property linguaNomeDirezione.
     */
    public void setLinguaNomeDirezione(String linguaNomeDirezione) {
        this.linguaNomeDirezione = linguaNomeDirezione;
    }
    
    public String getLinguaNomeBibliocr() throws AttributoNonValorizzatoException {
        if ( linguaNomeBibliocr == null ) {
            String error = "PersonBean: attributo 'linguaNomeBibliocr' non valorizzato.";
            throw new AttributoNonValorizzatoException(error);
        } else {
            return linguaNomeBibliocr;
        }
    }
    
    /**
     * Setter for property linguaNomeQualificaBibliocr.
     * @param linguaNomeQualificaBibliocr New value of property linguaNomeQualificaBibliocr.
     */
    public void setLinguaNomeQualificaBibliocr(String linguaNomeQualificaBibliocr) {
        this.linguaNomeQualificaBibliocr = linguaNomeQualificaBibliocr;
    }

    public String getLinguaNomeQualificaBibliocr() throws AttributoNonValorizzatoException {
        if ( linguaNomeQualificaBibliocr == null ) {
            String error = "PersonBean: attributo 'linguaNomeQualificaBibliocr' non valorizzato.";
            throw new AttributoNonValorizzatoException(error);
        } else {
            return linguaNomeQualificaBibliocr;
        }
    }

    /**
     * Setter for property linguaNomeBibliocr.
     * @param linguaNomeBibliocr New value of property linguaNomeBibliocr.
     */
    public void setLinguaNomeBibliocr(String linguaNomeBibliocr) {
        this.linguaNomeBibliocr = linguaNomeBibliocr;
    }
    
    public String getLinguaNomeQualificaDirezione() throws AttributoNonValorizzatoException {
        if ( linguaNomeQualificaDirezione == null ) {
            String error = "PersonBean: attributo 'linguaNomeQualificaDirezione' non valorizzato.";
            throw new AttributoNonValorizzatoException(error);
        } else {
            return linguaNomeQualificaDirezione;
        }
    }
    
    /**
     * Setter for property linguaNomeQualificaDirezione.
     * @param linguaNomeQualificaDirezione New value of property linguaNomeQualificaDirezione.
     */
    public void setLinguaNomeQualificaDirezione(String linguaNomeQualificaDirezione) {
        this.linguaNomeQualificaDirezione = linguaNomeQualificaDirezione;
    }
    
    public void setIncaricoInArea(String incaricoInArea) {
        this.incaricoInArea = incaricoInArea;
    }
    
    /**
     * Getter for property linguaPianoUfficio.
     * @return Value of property linguaPianoUfficio.
     */
    public String getLinguaPianoUfficio() throws AttributoNonValorizzatoException {
        if (linguaPianoUfficio == null) {
            throw new AttributoNonValorizzatoException("PersonBean: attributo linguaPianoUfficio non valorizzato!");
        } else {
            return this.linguaPianoUfficio;
        }
    }
    
    /**
     * Setter for property linguaPianoUfficio.
     * @param linguaDescrizioneSettore New value of property linguaPianoUfficio.
     */
    public void setLinguaPianoUfficio(String linguaPianoUfficio) {
        this.linguaPianoUfficio = linguaPianoUfficio;
    }
    
    public void setResponsabileArea(boolean responsabileArea) {
        this.responsabileArea = responsabileArea;
    }
    
     public void setResponsabileUO(boolean responsabileUO) {
        this.responsabileUO = responsabileUO;
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

    public void setArgomentiComp(Vector argomenti) {
        this.argomentiComp = argomenti;
    }
    
    public Vector getArgomentiComp() throws AttributoNonValorizzatoException {
        if (this.argomentiComp == null) {
            throw new AttributoNonValorizzatoException("PersonBean: attributo argomentiComp non valorizzato.");
        } else {
            return this.argomentiComp;
        }
    }

	/**
	 * @return the uffici
	 *
	public Vector<LuogoBean> getUffici() throws AttributoNonValorizzatoException {
		if (this.uffici == null) {
			throw new AttributoNonValorizzatoException("PersonBean: attributo uffici non valorizzato.");
		} else {
			return this.uffici;
		}
	}

	/**
	 * @param uffici the uffici to set
	 *
	public void setUffici(Vector<LuogoBean> uffici) {
		this.uffici = uffici;
	}*/

	/**
	 * @return the idContinuazioneDip
	 */
	public int getIdContinuazioneDip() throws AttributoNonValorizzatoException {
		if (this.idContinuazioneDip == -2) {
			throw new AttributoNonValorizzatoException("PersonBean: attributo idContinuazioneDip non valorizzato.");
		} else {
			return idContinuazioneDip;
		}
	}

	/**
	 * @param idContinuazioneDip the idContinuazioneDip to set
	 */
	public void setIdContinuazioneDip(int idContinuazioneDip) {
		this.idContinuazioneDip = idContinuazioneDip;
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
	 * @return the direzioneRadice
	 */
	public boolean isDirezioneRadice() {
		return direzioneRadice;
	}

	/**
	 * @param direzioneRadice the direzioneRadice to set
	 */
	public void setDirezioneRadice(boolean direzioneRadice) {
		this.direzioneRadice = direzioneRadice;
	}

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

    /**
     * @return the qualificheAfferenzeSecondarie
     *
    public Vector<QualificaBean> getQualificheAfferenzeSecondarie() {
        return qualificheAfferenzeSecondarie;
    }

    /**
     * @param qualificheAfferenzeSecondarie the dipartimentiAfferenzeSecondarie to set
     *
    public void setQualificheAfferenzeSecondarie(
            Vector<QualificaBean> dipartimentiAfferenzeSecondarie) {
        this.qualificheAfferenzeSecondarie = dipartimentiAfferenzeSecondarie;
    }*/

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
	 * @return the fineServizioMax
	 */
	public Date getFineServizioMax() {
		return fineServizioMax;
	}

	/**
	 * @param fineServizioMax the fineServizioMax to set
	 */
	public Date setFineServizioMax(Date fineServizioMax) {
		this.fineServizioMax = fineServizioMax;
		return fineServizioMax;
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
     * @return the isTAFacolta
     */
    public boolean isTAFacolta() {
        return isTAFacolta;
    }

    /**
     * @param isTAFacolta the isTAFacolta to set
     */
    public void setTAFacolta(char isTAFacolta) {
        if (isTAFacolta == '1') {
            this.isTAFacolta = true;
        } else {
            this.isTAFacolta = false;
        }
    }

    /**
     * @return the idTipoBiblioCR
     */
    public int getIdTipoBiblioCR() {
        return idTipoBiblioCR;
    }

    /**
     * @param idTipoBiblioCR the idTipoBiblioCR to set
     */
    public void setIdTipoBiblioCR(int idTipoBibliocr) {
        this.idTipoBiblioCR = idTipoBibliocr;
    }
    
    /**
     * @return the peso1
     */
    public int getPeso1() {
        return peso1;
    }

    /**
     * @param peso1 the peso1 to set
     */
    public void setPeso1(int peso1) {
        this.peso1 = peso1;
    }

    /**
     * @return the peso2
     */
    public int getPeso2() {
        return peso2;
    }

    /**
     * @param peso2 the peso2 to set
     */
    public void setPeso2(int peso2) {
        this.peso2 = peso2;
    }


    /**
     * @return the peso3
     */
    public int getPeso3() {
        return peso3;
    }


    /**
     * @param peso3 the peso3 to set
     */
    public void setPeso3(int peso3) {
        this.peso3 = peso3;
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

}
