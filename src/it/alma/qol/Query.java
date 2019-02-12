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

package it.alma.qol;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.LinkedList;


/**
 * <p>Query &egrave; l'interfaccia contenente tutte le query della 
 * web-application &nbsp;<code>Alma on Line</code>.</p>
 * <p>Definisce inoltre alcune costanti di utilit&agrave; e 
 * insiemi di valori ammessi per parametri applicativi.</p> 
 * 
 * @author <a href="mailto:giovanroberto.torre@univr.it">Giovanroberto Torre</a>
 */
public interface Query extends Serializable {

    /**
     * <p>Costante parlante per i test che controllano che interi abbiano un valore significativo.</p>
     */
    public static final byte NOTHING = 0;
    /* ************************************************************************ *
     *                  Costanti parlanti per valori boolean                    *
     * ************************************************************************ */
    /**
     * <p>Costante parlante per flag di recupero sessione utente.</p>
     */
    public static final boolean IF_EXISTS_DONOT_CREATE_NEW = false;
    /**
     * <p>Costante parlante per flag di recupero progetti utente 
     * indipendentemente dal livello di autorizzazione 
     * che l'utente ha sui progetti stessi.</p>
     */
    public static final boolean GET_ALL = true;
    /**
     * <p>Pattern che deve avere una data (oggetto java.util.Date o GregorianCalendar) per essere conforme al fornato SQL.</p>
     */
    public static final String DATA_SQL_PATTERN = "yyyy-MM-dd";
    /**
     * <p>Contiene la formattazione che deve avere una data all'interno dell'applicazione.</p>
     */
    public static final SimpleDateFormat DATA_FORMAT = new SimpleDateFormat(DATA_SQL_PATTERN);
    
    /* ************************************************************************ *
     *                   Query comuni a tutte le applicazioni                   *
     * ************************************************************************ */
    /**
     * <p>Estrae le classi Command previste per la/le applicazione/i.</p>
     */
    public static final String LOOKUP_COMMAND =
            "SELECT " +
            "       id          AS \"id\"" +
            "   ,   nome        AS \"nomeReale\"" +
            "   ,   nome        AS \"nomeClasse\"" +
            "   ,   token       AS \"nome\"" +
            "   ,   labelweb    AS \"labelWeb\"" +
            "   ,   jsp         AS \"paginaJsp\"" +
            "   ,   informativa AS \"informativa\"" + 
            "  FROM command";
    
    
    /* ************************************************************************ *
     *                                   QOL                                    *
    /* ************************************************************************ *
     *                  Costanti per applicazione QOL (ValDid)                  *
     * ************************************************************************ */
    /**
     *  Carica la lista con i codici U-GOV dei corsi elettivi.
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
    
    /* ************************************************************************ *
     *                              QUERY DI QOL                                *
     * ************************************************************************ *
     *                       4.  Query di eliminazione                          *
     * ************************************************************************ */
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
          //+ " FROM \"AD-Semplici-II2017\" AD"
          + " FROM \"ADSemplici-2018-I\" AD"
          + " ORDER BY \"codiceFiscaleDocente\", \"codiceADUGOV\", \"codiceCdSUGOV\", \"inizioPerDid\", \"finePerDid\""
          + " --ORDER BY \"id\" "
          + " --LIMIT 30";
    
    /**
     * Query per estrarre le tuple delle AD Moduli.
     */
    public static final String AD_MODULI = 
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
          + ",\"Modulo\"                AS \"nomeModulo\""
          + ",\"MODULO2\"               AS \"motivo\""
          + ",\"UL\"                    AS \"nomeUl\""
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
          + " FROM \"ADModuli-2018-I\" MOD"
          + " ORDER BY \"codiceFiscaleDocente\", \"codiceADUGOV\", \"codiceCdSUGOV\", \"inizioPerDid\", \"finePerDid\"";
    
    /**
     * Query per estrarre le tuple delle Unit&agrave; Logistiche.
     */
    public static final String UL = 
            "SELECT " 
          + " \"row.names\"             AS \"id\""
          + ",\"CodiceCDSWI\"           AS \"codiceCdSWI\""
          + ",\"CodiceCDSUGOV\"         AS \"codiceCdSUGOV\""
          + ",\"NomeCDS\"               AS \"nomeCdSWI\""
          //+ ",\"NOMECDS2\"              AS \"nomeCdSUGOV\""
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
          //+ ",\"CFUlezione\"            AS \"creditiLezione\""
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
          + " FROM \"UL-2018-I\" UL"
          + " ORDER BY \"codiceFiscaleDocente\", \"codiceADUGOV\", \"codiceCdSUGOV\", \"inizioPerDid\", \"finePerDid\""
          + " --ORDER BY \"id\" "
          + " --LIMIT 30";
    
}