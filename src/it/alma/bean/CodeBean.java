/*
 *   Alma on Line: Applicazione WEB per la visualizzazione 
 *   delle schede di indagine su popolazione dell'ateneo.<br />
 *   
 *   Copyright (C) 2018 Giovanroberto Torre<br />
 *   Alma on Line (aol), web application to publish students 
 *   and degrees informations.
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

import it.alma.exception.AttributoNonValorizzatoException;


/** 
 * <p>Classe per rappresentare oggetti generici o raggruppamenti di elementi.</p>
 * In virt&uacute; della sua struttura generica, pu&ograve; essere utilizzato 
 * per veicolare valori di molteplici entit&agrave;, come per esempio:
 * <ul>
 * <li>gruppo di altre entit&agrave;</li>
 * <li>strutture didattiche</li>
 * <li>classificazioni</li>
 * <li>etc...</li>
 * </ul>
 * ecc.  
 * 
 * @version 1.2
 * @author <a href="mailto:giovanroberto.torre@univr.it">Giovanroberto Torre</a>
 */
public class CodeBean implements Serializable {
    
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
    /**
     * Identificativo del gruppo di elementi
     */
    private int id;
    /**
     * Nome del gruppo di elementi
     */
    private String nome;
    /**
     * Descrizione del gruppo di elementi
     */
    private String informativa;
    /**
     * Numero d'ordine del gruppo di elementi rispetto agli altri
     */
    private int ordinale;
    
    
    /**
     * Costruttore di CodeBean.
     * Inizializza i campi a valori convenzionali di default.
     */
    public CodeBean() {
        id = ordinale = -2;
        nome = informativa = null;
    }
    
    
    /* **************************************************** *
     *           Metodi getter e setter per id              *
     * **************************************************** */
    /**
     * Restituisce l'identificativo di un raggruppamento.
     * @return <code>id</code> - l'id del gruppo 
     * @throws it.alma.exception.AttributoNonValorizzatoException  eccezione che viene sollevata se questo oggetto viene usato e l'id non &egrave; stato valorizzato (&egrave; un dato obbligatorio)
     */
    public int getId() throws AttributoNonValorizzatoException {
        if (id == -2) {
            throw new AttributoNonValorizzatoException(FOR_NAME + "Attributo id non valorizzato!");
        }
        return this.id;
    }
    
    /**
     * Imposta  l'id di un raggruppamento.
     * @param i l'id del gruppo da impostare
     */
    public void setId(int i) {
        id = i;
    }
    
    
    /* **************************************************** *
     *           Metodi getter e setter per nome            *
     * **************************************************** */
    /**
     * Restituisce il nome di un raggruppamento.
     * @return <code>nome</code> - la denominazione del gruppo 
     * @throws it.alma.exception.AttributoNonValorizzatoException  eccezione che viene sollevata se questo oggetto viene usato e questo attributo non &egrave; stato valorizzato (potrebbe essere un dato obbligatorio)
     */
    public String getNome() throws AttributoNonValorizzatoException {
        if (nome == null) {
            throw new AttributoNonValorizzatoException(FOR_NAME + "Attributo nome non valorizzato!");
        }
        return this.nome;
    }
    
    /**
     * Imposta la denominazione di un raggruppamento.
     * @param string il nome da impostare per il gruppo
     */
    public void setNome(String string) {
        nome = string;
    }    
        
    
    /* **************************************************** *
     *           Metodi getter e setter per informativa     *
     * **************************************************** */
    /**
     * Restituisce la descrizione di un raggruppamento.
     * @return <code>informativa</code> - la descrizione del gruppo 
     * @throws it.alma.exception.AttributoNonValorizzatoException  eccezione che viene sollevata se questo oggetto viene usato e questo attributo non &egrave; stato valorizzato (potrebbe essere un dato obbligatorio)
     */
    public String getInformativa() throws AttributoNonValorizzatoException {
        if (informativa == null) {
            throw new AttributoNonValorizzatoException(FOR_NAME + "Attributo informativa non valorizzato!");
        }
        return this.informativa;
    }
    
    /**
     * Imposta la descrizione di un raggruppamento.
     * @param string il nome da impostare per la descrizione del gruppo
     */
    public void setInformativa(String string) {
        informativa = string;
    }    
    
    
    /* **************************************************** *
     *           Metodi getter e setter per ordinale        *
     * **************************************************** */
    /**
     * Restituisce l'ordinale di un raggruppamento rispetto agli altri.
     * @return <code>ordinale</code> - il valore del numero d'ordine del gruppo 
     * @throws it.alma.exception.AttributoNonValorizzatoException  eccezione che viene sollevata se questo oggetto viene usato e questo attributo non &egrave; stato valorizzato (potrebbe essere un dato obbligatorio)
     */
    public int getOrdinale() throws AttributoNonValorizzatoException {
        if (ordinale == -2) {
            throw new AttributoNonValorizzatoException(FOR_NAME + "Attributo ordinale non valorizzato!");
        }
        return this.ordinale;
    }
    
    /**
     * Imposta  il numero d'ordine di un raggruppamento.
     * @param i l'ordinale del gruppo da impostare
     */
    public void setOrdinale(int i) {
        ordinale = i;
    }

}
