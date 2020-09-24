/*
 *   Alma on Line: 
 *   Applicazione WEB per la gestione dei progetti on line (POL)
 *   coerentemente con le linee-guida del project management,
 *   e per la visualizzazione delle schede di indagine 
 *   su popolazione dell'ateneo.
 *   
 *   Copyright (C) 2018-2020 Giovanroberto Torre<br />
 *   Alma on Line (aol), Projects on Line (pol);
 *   web applications to publish, and manage, projects
 *   according to the Project Management paradigm (PM).
 *   Copyright (C) renewed 2020 Giovanroberto Torre, 
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
import java.util.Vector;

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
    /** Persone che condividono la competenza */
    private Vector<PersonBean> personale;
    
    
    /**
     * <p>Costruttore che inizializza i campi a valori di default.</p>
     */
    public SkillBean() {
        super();
        presenza = false;
        idProgetto = -2;
        personale = null;
    }

    
    /* ********************************************************** *
     *           Metodi getter e setter per presenza              *
     * ********************************************************** */
    /**
     * Restituisce la presenza della competenza
     * @return <code>presenza</code> - presenza della competenza
     */
    public boolean isPresenza() {
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
        if (idProgetto == -2) {
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
     *          Metodi getter e setter per personale              *
     * ********************************************************** */
    /**
     * Restituisce lista delle persone che hanno/condividono la competenza.
     * @return <code>Vector&lt;PersonBean&gt;</code> - elenco delle persone a cui si riferisce la competenza 
     */
    public Vector<PersonBean> getPersone() {
        return personale;
    }

    /**
     * Imposta la lista delle persone a cui la competenza fa riferimento.
     * @param personale - elenco persone da impostare
     */
    public void setPersone(Vector<PersonBean> personale) {
        this.personale = personale;
    }
    
}
