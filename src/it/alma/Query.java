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


/**
 * @author trrgnr59
 * @version 2
 *
 */
public interface Query {

    
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
    
    /**
     * Estrae il profilo dei laureati dalla base dati Almalaurea
     * Access Style Query
     *
    public static final String NUM_LAUREATI = 
            "SELECT " 
          + "2016_A.CORSb            AS [Codice CdS]," 
          + "2016_A.CORSc            AS [Corso di Studio]," 
          + "Count(2016_A.MATRICOLA) AS [Numero di laureati 2016]," 
          + "Sum(2016_A.Abbin)       AS [Hanno compilato il questionario],"
          + "2016_A.CORSclasse"
          + "FROM" 
          + "[2016-A-GW] AS 2016_A"
          + "INNER JOIN [2016-GX-LT] AS 2016_B ON 2016_A.id_cv = 2016_B.id_cv" 
          + "WHERE ("
          + "("
          + "(2016_A.CORSb) = 'C75' OR (2016_A.CORSb) = 'MM10'"
          + ")" 
          + ")"
          + "GROUP BY 2016_A.CORSb, 2016_A.CORSc, 2016_A.CORSclasse"
          + "ORDER BY 2016_A.CORSb;";
    
    
    public static final String NUM_MATRICOLE_2016 = 
            "SELECT" 
//          + " count(*) "
          + " * "
          + " FROM " 
          + " \"70040-2016\" LIMIT 10";
    
    public static final String AD_2016 = 
            "SELECT " 
          + " \"MATRICOLA\"   AS \"misura\""
          + ",\"CODICIONE\"   AS \"codiceInsegnamento\""
          + ",\"CORSb\"       AS \"codiceCs\""
          + " FROM \"70040-2016\" A" 
          + " LIMIT 10";
*/
    
}