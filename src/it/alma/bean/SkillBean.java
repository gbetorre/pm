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

import it.alma.exception.AttributoNonValorizzatoException;

/**
 * <p>Classe usata per rappresentare una competenza.</p>
 * 
 * @author <a href="mailto:andrea.tonel@studenti.univr.it">Andrea Tonel</a>
 */

public class SkillBean extends CodeBean implements Serializable {
    
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
     *                  Dati identificativi della competenza                    *
     * ************************************************************************ */
    /** Presenza della competenza */
    private boolean presenza;
    /** Id del progetto a cui riferisce la competenza */
    private int idProgetto;
    /** Id della persona a cui riferisce la competenza */
    private int idPersona;
    
    
    /**
     * <p>Costruttore che inizializza i campi a valori di default.</p>
     */
    public SkillBean() {
        super();
        presenza = false;
        idProgetto = -2;
        idPersona = -2;
    }

    
    /* ********************************************************** *
     *           Metodi getter e setter per presenza              *
     * ********************************************************** */
    /**
     * Restituisce la presenza della competenza
     * @return <code>presenza</code> - presenza della competenza
     */
    public boolean getPresenza() {
        return presenza;
    }

    /**
     * Imposta la presenza di una competenza, passata come parametro.
     * @param presenza - presenza della competenza da impostare
     */
    public void setPresenza(boolean presenza) {
        this.presenza = presenza;
    }


    /* ********************************************************** *
     *          Metodi getter e setter per idProgetto             *
     * ********************************************************** */
    /**
     * Restituisce l'id del progetto a cui riferisce la competenza.
     * @return <code>idProgetto</code> - id del progetto a cui riferisce la competenza
     * @throws it.alma.exception.AttributoNonValorizzatoException  eccezione che viene sollevata se questo oggetto viene usato e l'idProgetto non &egrave; stato valorizzato (&egrave; un dato obbligatorio) 
     */
    public int getIdProgetto() throws AttributoNonValorizzatoException {
        if(idProgetto == -2) {
            throw new AttributoNonValorizzatoException(FOR_NAME + "Attributo idProgetto non valorizzato!");
        }
        return idProgetto;
    }

    /**
     * Imposta l'id del progetto a cui la competenza fa riferimento.
     * @param idProgetto - idProgetto da impostare
     */
    public void setIdProgetto(int idProgetto) {
        this.idProgetto = idProgetto;
    }

    
    /* ********************************************************** *
     *          Metodi getter e setter per idPersona              *
     * ********************************************************** */
    /**
     * Restituisce l'id della persona a cui riferisce la competenza.
     * @return <code>idPersona</code> - id della persona a cui riferisce la competenza
     * @throws it.alma.exception.AttributoNonValorizzatoException  eccezione che viene sollevata se questo oggetto viene usato e l'idPersona non &egrave; stato valorizzato (&egrave; un dato obbligatorio) 
     */
    public int getIdPersona() throws AttributoNonValorizzatoException {
        if(idPersona == -2) {
            throw new AttributoNonValorizzatoException(FOR_NAME + "Attributo idProgetto non valorizzato!");
        }
        return idPersona;
    }

    /**
     * Imposta l'id della persona a cui la competenza fa riferimento.
     * @param idPersona - idPersona da impostare
     */
    public void setIdPersona(int idPersona) {
        this.idPersona = idPersona;
    }
    
}
