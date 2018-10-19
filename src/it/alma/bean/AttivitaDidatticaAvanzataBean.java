/*
 *   uol: University on Line. Applicazione WEB per la visualizzazione 
 *   dei siti web delle ex-facolta' e dei dipartimenti.
 *   Copyright (C) 2000,2001 Roberto Posenato.
 *   
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
 *   Direzione Servizi Informatici di Ateneo
 *   Universita' degli Studi di Verona
 *   Via S. Francesco 22
 *   37129 Verona (Italy)
 */
package it.alma.bean;

import it.alma.exception.AttributoNonValorizzatoException;

import java.util.Vector;


/**
 * <p>AttivitaDidatticaAvanzataBean &egrave; l'oggetto per la memorizzazione 
 * dei dati di un'attivit&agrave; didattica avanzata, specifica di un
 * dottorato di ricerca, di una scuola di dottorato o comune a
 * pi&uacute; scuole di dottorato.</p>
 * 
 * @author posenato
 * @author <a href="mailto:giovanroberto.torre@univr.it">Giovanroberto Torre</a>
 *
 */
public class AttivitaDidatticaAvanzataBean {
    private int id;
    private String nome;
    private String descrizione;
    private String periodo;
    private String annoErogazione;
    private String url;
    private boolean interna;
    private String nomeCiclo;
    private String linguaNome;
    private String linguaDescrizione;
    private String linguaPeriodo;
    private Vector allegati;
    private Vector docenti;
    private Vector cicli;
    private int idCs;
    private String nomeCorsoStudi;
    private String linguaNomeCorsoStudi;
    private String urlStruttura;
    private int annoCorso;
    private int idSeminario;
    private int idIniziativaConvegno;    
    private boolean attivitaComune;
    private float crediti;
    private int proponente;
		
    
    /**
     * Costruttore da ex-novo;
     * inizializza i campi a valori di default.
     */
    public AttivitaDidatticaAvanzataBean() {
        id = idCs = annoCorso = -2;
        nome = descrizione = periodo = url = nomeCiclo = urlStruttura = nomeCorsoStudi = annoErogazione = null;
        interna = attivitaComune = false; 
        linguaNome = linguaDescrizione = linguaPeriodo = linguaNomeCorsoStudi = null;
        allegati = docenti = cicli = new Vector();
        idSeminario = idIniziativaConvegno = -2;
        crediti = (float) -2;
    }
    
    
    /**
     * Restituisce true se l'attivita &egrave;
     * interna, false altrimenti.
     * 
     * @return boolean - un valore vero o falso a seconda che l'attivita' sia interna o meno
     */
    public boolean isInterna(){
        return interna;
    }

    
    /**
     * Assegna true se l'attivita &egrave;
     * interna, false altrimenti.
     * 
     * @param b il valore vero o falso da impostare
     */
    public void setInterna(boolean b){
        this.interna = b;
    }
    
    
    /**
     * 
     * @return id
     */
    public int getId() throws AttributoNonValorizzatoException {
        if (id == -2) {
            throw new AttributoNonValorizzatoException("AttivitadidatticaAvanzataBean: attributo id non valorizzato!");
        } else {
            return this.id;
        }
    }

    /**
     * @param id
     */
    public void setId(int i) {
        this.id = i;
    }
        
    /**
     * Restituisce il nome dell'insegnamento
     * avanzato
     * @return nome
     */
    public String getNome() throws AttributoNonValorizzatoException {
        if (nome == null) {
            throw new AttributoNonValorizzatoException("AttivitadidatticaAvanzataBean: attributo nome non valorizzato!");
        } else {
            return this.nome;
        }
    }
    
    /**
     * Imposta il nome dell'insegnamento
     * avanzato
     * @param nome
     */
    public void setNome(String v) {
        this.nome = v;
    }
    
    /*
     * Restituisce la descrizione dell'occorrenza
     * insegnamento avanzato
     * @return descrizione
     */
    public String getDescrizione() {
        return this.descrizione;
    }    

    /**
     * Imposta la descrizione dell'occorrenza
     * insegnamento avanzato
     */
    public void setDescrizione(String d){
        this.descrizione = d;
    }

    /*
     * Restituisce il periodo dell'occorrenza
     * insegnamento avanzato
     * @return periodo
     */
    public String getPeriodo() {
        return this.periodo;
    }    

    /**
     * Imposta il periodo dell'occorrenza
     * insegnamento avanzato
     */
    public void setPeriodo(String p){
        this.periodo = p;
    }

    /*
     * Restituisce l'url dell'occorrenza
     * insegnamento avanzato. 
     * @return url
     */
    public String getUrl() {
        return this.url;
    }    

    /**
     * Imposta l'url dell'occorrenza
     * insegnamento avanzato
     */
    public void setUrl(String u){
        this.url = u;
    }
    
    /**
     * Restituisce il nome del ciclo dell'insegnamento
     * avanzato
     * @return linguaNome
     */
    public String getNomeCiclo() {
        return this.nomeCiclo;
    }
    
    /**
     * Imposta il nome del ciclo a cui l'insegnamento
     * avanzato appartiene
     * @param linguaNome
     */
    public void setNomeCiclo(String v) {
        this.nomeCiclo = v;
    }

    /**
     * Metodo che restituisce  un vector di
     * Allegati
     *
     * @return vector di 
     */
    public Vector getAllegati(){
        return this.allegati;
    }

    /**
     * Metodo per l'assegnamento di un vector di
     * allegati
     *
     * @param v vector di documenti
     */
    public void setAllegati(Vector v){
        this.allegati = v;
    }


    /**
     * Metodo che restituisce  un vector di
     * docenti
     *
     * @return vector di  docenti
     */
    public Vector getDocenti(){
        return this.docenti;
    }

    /**
     * Metodo per l'assegnamento di un vector di
     * docenti
     *
     * @param v vector di docenti
     */
    public void setDocenti(Vector v){
        this.docenti = v;
    }

    /**
     * Metodo che restituisce  un vector di
     * cicli di corso di studi
     *
     * @return vector di  cicli di corso di studi
     */
    public Vector getCicli(){
        return this.cicli;
    }

    /**
     * Metodo per l'assegnamento di un vector di
     * cicli di corso di studio
     *
     * @param v vector di cicli di corso di studio
     */
    public void setCicli(Vector v){
        this.cicli = v;
    }

//---------------------------------------------------------------- 
    /**
     * Restituisce la lingua del nome dell'insegnamento
     * avanzato
     * @return linguaNome
     */
    public String getLinguaNome() {
        return this.linguaNome;
    }
    
    /**
     * Imposta la  lingua del nome dell'insegnamento
     * avanzato
     * @param linguaNome
     */
    public void setLinguaNome(String v) {
        this.linguaNome = v;
    }
   
    /*
     * Restituisce la lingua della descrizione dell'occorrenza
     * insegnamento avanzato
     * @return linguaDescrizione
     */
    public String getLinguaDescrizione() {
        return this.linguaDescrizione;
    }    

    /**
     * Imposta la lingua della descrizione dell'occorrenza
     * insegnamento avanzato
     */
    public void setLinguaDescrizione(String d){
        this.linguaDescrizione = d;
    }

    /*
     * Restituisce la lingua del periodo dell'occorrenza
     * insegnamento avanzato
     * @return linguaPeriodo
     */
    public String getLinguaPeriodo() {
        return this.linguaPeriodo;
    }    

    /**
     * Imposta la  lingua del periodo dell'occorrenza
     * insegnamento avanzato
     */
    public void setLinguaPeriodo(String p){
        this.linguaPeriodo = p;
    }

    /**
     * 
     * @return idCs
     */
    public int getIdCs() throws AttributoNonValorizzatoException {
        if (idCs == -2) {
            throw new AttributoNonValorizzatoException("AttivitadidatticaAvanzataBean: attributo idCs non valorizzato!");
        } else {
            return this.idCs;
        }
    }

    /**
     * @param idCs
     */
    public void setIdCs(int i) {
        this.idCs = i;
    }
    
    /**
     * 
     * @return idIniziativaConvegno
     */
    public int getIdIniziativaConvegno()  {
            return this.idIniziativaConvegno;
     }

    /**
     * @param idIniziativaConvegno
     */
    public void setIdIniziativaConvegno(int i) {
        this.idIniziativaConvegno = i;
    }    
    
    /**
     * 
     * @return idSeminario
     */
    public int getIdSeminario() {
            return this.idSeminario;
     }

    /**
     * @param idSeminario
     */
    public void setIdSeminario(int i) {
        this.idSeminario = i;
    }    
    
    /**
     * 
     * @return annoCorso
     */
    public int getAnnoCorso() throws AttributoNonValorizzatoException {
        if (annoCorso == -2) {
            throw new AttributoNonValorizzatoException("AttivitadidatticaAvanzataBean: attributo annoCorso non valorizzato!");
        } else {
            return this.annoCorso;
        }
    }

    /**
     * @param annoCorso
     */
    public void setAnnoCorso(int i) {
        this.annoCorso = i;
    }
    
    public void setUrlStruttura(String v) {
        this.urlStruttura = v;
    }
    
    public String getUrlStruttura() {
        return urlStruttura;
    }
    
    public void setNomeCorsoStudi(String v) {
        nomeCorsoStudi = v;
    }
    
    public String getNomeCorsoStudi() {
        return nomeCorsoStudi;
    }
    
    public void setLinguaNomeCorsoStudi(String v) {
        linguaNomeCorsoStudi = v;
    }
    
    public String getLinguaNomeCorsoStudi() {
        return linguaNomeCorsoStudi;
    }

	/**
	 * @return the attivitaComune
	 */
	public boolean isAttivitaComune() {
		return attivitaComune;
	}

	/**
	 * @param attivitaComune the attivitaComune to set
	 */
	public void setAttivitaComune(boolean attivitaComune) {
		this.attivitaComune = attivitaComune;
	}

    /**
     * @return the crediti
     */
    public float getCrediti() {
        return crediti;
    }

    /**
     * @param crediti the crediti to set
     */
    public void setCrediti(float crediti) {
        this.crediti = crediti;
    }


    /**
     * @return the proponente
     */
    public int getProponente() {
        return proponente;
    }


    /**
     * @param proponente the proponente to set
     */
    public void setProponente(int proponente) {
        this.proponente = proponente;
    }


    /**
     * @return the annoErogazione
     */
    public String getAnnoErogazione() {
        return annoErogazione;
    }


    /**
     * @param annoErogazione the annoErogazione to set
     */
    public void setAnnoErogazione(String annoErogazione) {
        this.annoErogazione = annoErogazione;
    }
}
