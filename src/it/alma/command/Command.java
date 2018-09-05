/*
 *   uol: University on Line. Applicazione WEB per la visualizzazione
 *	  del sito web della Facoltà
 *   Copyright (C) 2000,2001 Roberto Posenato, Mirko Manea
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
 *   Mirko Manea <mami@mami.net>
 */

package it.alma.command;


import it.alma.exception.CommandException;
import it.alma.bean.ItemBean;
import javax.servlet.http.HttpServletRequest;

/**
 * Command è una semplice interfaccia per realizzare un'applicazione
 * web servlet-centrica.
 *
 * Nell'approccio servlet-centrico esiste una servlet centrale che
 * gestisce la logica dell'applicazione. Ogni azione del web è
 * realizzata da una classe specifica e l'interazione tra la
 * servlet-centrale e le classi avviene tramite questa
 * interfaccia. Questo approccio è anche detto 'command pattern'.
 *
 * @author Roberto Posenato
 * @version 2
 */
public interface Command {
    
    /**
     * Esegue tutte le operazioni necessarie per recuperare le informazioni
     * per la visualizzazione. Tutte le informazioni devono essere inserite
     * nella sessione o request
     *
     * @param req HttpServletRequest
     * @throws CommandException 
     */
    public void execute(HttpServletRequest req)
    throws CommandException;
    
    /**
     * init supplies a constructor that can't be used
     * directly. Remember that almost of the implementing classes are
     * instancied by the Class.newInstance().
     *
     * @param voce a ItemBean containing al the useful information
     * for instantiation.
     * @throws CommandException 
     */
    public void init(ItemBean voce)
    throws CommandException;
}
