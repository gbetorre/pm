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

package it.alma;

import java.io.Serializable;
import java.util.LinkedList;


/**
 * <p>Query &egrave; l'interfaccia contenente tutte le query della 
 * web-application &nbsp;<code>Alma on Line</code>.</p>
 * 
 * @author <a href="mailto:giovanroberto.torre@univr.it">Giovanroberto Torre</a>
 */
public interface Query extends Serializable {

    /**
     * Lista Hash contenente tutti i corsi elettivi
     */
    public LinkedList<String> CORSI_ELETTIVI = new LinkedList<String>();
    
    /**
     *  Carica la lista con i codici U-GOV dei corsi elettivi
     */
    public String[] CODICI_CORSI_ELETTIVI = {"4S000766",  
                                             "0813M",
                                             "4S004786",
                                             "4S004102",
                                             "4S006311",
                                             "0763M",
                                             "0783M",
                                             "4S003529",
                                             "4S006312",
                                             "4S00183",
                                             "0729M",
                                             "0660M",
                                             "0699M",
                                             "0656M",
                                             "0820M",
                                             "4S000825",
                                             "4S006318",
                                             "4S004785",
                                             "4S006319",
                                             "4S001136",
                                             "4S004783",
                                             "4S006320",
                                             "4S000768",
                                             "4S001459",
                                             "0828M",
                                             "4S000823",
                                             "4S000770",
                                             "0738M",
                                             "0728M",
                                             "4S001137",
                                             "0758M",
                                             "4S01927",
                                             "DF-0022",
                                             "4S001007",
                                             "0854M",
                                             "0760M",
                                             "4S004103",
                                             "4S007217",
                                             "0752M",
                                             "4S006321",
                                             "4S006322",
                                             "4S006422",
                                             "0808M",
                                             "4S000771",
                                             "0810M",
                                             "0716M",
                                             "0811M",
                                             "4S006323",
                                             "4S001139",
                                             "0812M",
                                             "4S001462",
                                             "4S006324",
                                             "4S004505",
                                             "4S001008",
                                             "0665M",
                                             "0800M",
                                             "4S003471",
                                             "4S004787",
                                             "0890M",
                                             "0858M",
                                             "4S000821",
                                             "4S000797",
                                             "4S003472",
                                             "0775M",
                                             "0794M",
                                             "4S006325",
                                             "4S004105",
                                             "0903M",
                                             "DF-0020",
                                             "0650M",
                                             "4S004106",
                                             "0671M",
                                             "4S003525",
                                             "0746M",
                                             "4S003474",
                                             "0837M",
                                             "0672M",
                                             "0889M",
                                             "0859M",
                                             "0857M",
                                             "0855M",
                                             "0856M",
                                             "0860M",
                                             "4S006326",
                                             "4S001012",
                                             "DF-0009",
                                             "4S006327",
                                             "0781M",
                                             "4S007166",
                                             "0648M",
                                             "4S001013",
                                             "4S006328",
                                             "4S006329",
                                             "4S001014",
                                             "0649M",
                                             "4S000794",
                                             "4S006330",
                                             "0902M",
                                             "0891M",
                                             "4S006331",
                                             "4S006332",
                                             "0745M",
                                             "DF-0023",
                                             "4S006333"
                                            };

    /**
     * Query per estrarre le tuple delle AD Semplici.
     */
    public static final String AD_SEMPLICI = 
            "SELECT " 
          + " \"row.names\"             AS \"id\""
          + ",\"CodiceCDSWI\"           AS \"codiceCdSWI\""
          + ",\"CodiceCDSUGOV\"         AS \"codiceCdSUGOV\""
          + ",\"NomeCDS\"               AS \"nomeCdSWI\""
          + ",\"NOMECDS2\"              AS \"nomeCdSUGOV\""
          + ",\"DipartimentoScuola\"    AS \"dipartimentoScuola\""
          + ",\"SedePrimariaCDS\"       AS \"sedePrimariaCDS\""
          + ",\"CodiceADWI\"            AS \"codiceADWI\""
          + ",\"CodiceADUGOV\"          AS \"codiceADUGOV\""
          + ",\"AD\"                    AS \"nomeInsegnamento\""
          + ",\"Modulo\"                AS \"modulo\""
          + ",\"UL\"                    AS \"ul\""
          + ",\"Discriminante\"         AS \"discriminante\""
          + ",\"CFUtotali\"             AS \"crediti\""
          + ",\"CFUtotali\"             AS \"creditiTotali\""
          + ",\"CFUlezione\"            AS \"creditiLezione\""
          + ",\"SSD\"                   AS \"SSD\""
          + ",\"CognomeDocente\"        AS \"cognomeDocente\""
          + ",\"NomeDocente\"           AS \"nomeDocente\""
          + ",\"CodiceFiscaleDocente\"  AS \"codiceFiscaleDocente\""
          + ",\"Coordinatore\"          AS \"coordinatore\""
          + ",\"Orelezione\"            AS \"oreLezione\""
          + ",\"Orelaboratorio\"        AS \"oreLaboratorio\""
          + ",\"Orealtro\"              AS \"oreAltro\""
          + ",\"Oreseminario\"          AS \"oreSeminario\""
          + ",\"Oreesercitazione\"      AS \"oreEsercitazione\""
          + ",\"Oretirocinio\"          AS \"oreTirocinio\""
          + ",\"InizioPerDid\"          AS \"inizioPerDid\""
          + ",\"FinePerDid\"            AS \"finePerDid\""
          + " FROM \"AD-Semplici-II2017\" AD" 
          + " --LIMIT 10";
    
}