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

package it.alma;

import java.io.Serializable;
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
     * Costante parlante per flag di recupero sessione utente
     */
    public static final boolean IF_EXISTS_DONOT_CREATE_NEW = false;
    /**
     * Costanti corrispondenti ai parametri ammessi sulla querystring
     */
    public static final String PART_PROJECT_CHARTER_VISION      = "pcv";
    public static final String PART_PROJECT_CHARTER_STAKEHOLDER = "pcs";
    public static final String PART_PROJECT_CHARTER_DELIVERABLE = "pcd";
    public static final String PART_PROJECT_CHARTER_RESOURCE    = "pcr";
    public static final String PART_PROJECT_CHARTER_RISK        = "pck";
    public static final String PART_PROJECT_CHARTER_CONSTRAINT  = "pcc";
    public static final String PART_PROJECT_CHARTER_MILESTONE   = "pcm";
    public static final String PART_WBS                         = "wbs";
    public static final String PART_ACTIVITY                    = "act";
    public static final String PART_REPORT                      = "rep";
    public static final String PART_PROJECT                     = "prj";
    /**
     * Valori possibili degli attributi di stato di un progetto
     */
    static final String[] STATI = {"OK", "POSSIBILI PROBLEMI", "CRITICO"};
    /**
     * Valori possibili dell'attributo nome di statoprogetto
     */
    static final String[] STATI_PROGETTO = {"APERTO", "IN PROGRESS", "SOSPESO", "CONCLUSO", "ELIMINATO"};
    /**
     * Valori possibili degli attributi probabilita, impatto, livello di rischio
     */
    static final String[] LIVELLI_RISCHIO = {"ALTO", "MEDIO", "BASSO"};
    /**
     * Valori possibili dell'attributo stato di rischio
     */
    static final String[] STATO_RISCHIO = {"APERTO", "CHIUSO", "IN RISOLUZIONE"};
    /**
     * Lista contenente i possibili valori dei campi di stato del progetto
     */    
    public static final LinkedList<String> STATI_AS_LIST = new LinkedList<String>(Arrays.asList(STATI));
    /**
     * Lista contenente i possibili valori del campo nome della classe StatoProgettoBean
     */    
    public static final LinkedList<String> STATI_PROGETTO_AS_LIST = new LinkedList<String>(Arrays.asList(STATI_PROGETTO));
    /**
     * Lista contenente i possibili valori degli attributi probabilita, impatto, livello della classe RischioBean
     */    
    public static final LinkedList<String> LIVELLI_RISCHIO_AS_LIST = new LinkedList<String>(Arrays.asList(LIVELLI_RISCHIO));
    /**
     * Lista contenente i possibili valori dell'attributo stato della classe RischioBean
     */    
    public static final LinkedList<String> STATO_RISCHIO_AS_LIST = new LinkedList<String>(Arrays.asList(STATO_RISCHIO));
    
    /**
     * Estrae le classi command previste per l'applicazione
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
    
    /**
     * Estrae l'utente con username e password passati come parametri
     */
    public static final String GET_USR =
            "SELECT " +
            "       U.id            " +
            "   ,   P.id            AS \"id\"" +
            "   ,   P.nome          AS \"nome\"" +
            "   ,   P.cognome       AS \"cognome\"" +
            "   ,   P.sesso         AS \"sesso\"" +
            "   ,   P.datanascita   AS \"dataNascita\"" +
            "   ,   P.codicefiscale AS \"codiceFiscale\"" +
            //"   ,   informativa AS \"informativa\"" + 
            "   FROM usr U" +
            "       INNER JOIN identita I ON U.id = I.id0_usr" + 
            "       INNER JOIN persona P ON P.id = I.id1_persona" + 
            "   WHERE   login = ?" +
            "       AND passwd = ?"
            ;
    
    /**
     * Estrae i progetti dell'utente passato come parametro
     */
    public static final String GET_PROJECTS = 
    		"SELECT " +
    		"		PJ.id			" + 
    		"	, 	PJ.id_dipart				AS \"idDipart\"" + 
    		"	,	PJ.titolo					AS \"titolo\"" + 
    		"	,	PJ.descrizione				AS \"descrizione\"" + 
    		"	,	PJ.datainizio				AS \"dataInizio\"" + 
    		"	,	PJ.datafine					AS \"dataFine\"" + 
    		"	,	PJ.id_statoprogetto			AS \"idStatoProgetto\"" + 
    		"	,	PJ.statotempi				AS \"statoTempi\"" + 
    		"	,	PJ.statocosti				AS \"statoCosti\"" + 
    		"	,	PJ.statorischi				AS \"statoRischi\"" + 
    		"	,	PJ.statorisorse				AS \"statoRisorse\"" + 
    		"	,	PJ.statoscope				AS \"statoScope\"" + 
    		"	,	PJ.statocomunicazione		AS \"statoComunicazione\"" + 
    		"	,	PJ.statoqualita				AS \"statoQualita\"" + 
    		"	,	PJ.statoapprovvigionamenti	AS \"statoApprovvigionamenti\"" + 
    		"	,	PJ.statostakeholder			AS \"statoStakeholder\"" + 
    		"	,	PJ.meseriferimento			AS \"meseRiferimento\"" + 
    		"	,	PJ.descrizionestatocorrente	AS \"descrizioneStatoCorrente\"" + 
    		"	,	PJ.situazioneattuale		AS \"situazioneAttuale\"" + 
    		"	,	PJ.situazionefinale			AS \"situazioneFinale\"" + 
    		"	,	PJ.obiettivimisurabili		AS \"obiettiviMisurabili\"" + 
    		"	,	PJ.minacce					AS \"minacce\"" + 
    		"	,	PJ.stakeholdermarginali		AS \"stakeholderMarginali\"" + 
    		"	,	PJ.stakeholderoperativi		AS \"stakeholderOperativi\"" + 
    		"	,	PJ.stakeholderistituzionali	AS \"stakeholderIstituzionali\"" + 
    		"	,	PJ.stakeholderchiave		AS \"stakeholderChiave\"" + 
    		"	,	PJ.deliverable				AS \"deliverable\"" + 
    		"	,	PJ.fornitorichiaveinterni	AS \"fornitoriChiaveInterni\"" + 
    		"	,	PJ.fornitorichiaveesterni	AS \"fornitoriChiaveEsterni\"" + 
    		"	,	PJ.serviziateneo			AS \"serviziAteneo\"" + 
    		"	,	PJ.vincoli					AS \"vincoli\"" + 
    		"	FROM progetto PJ" + 
    		"		INNER JOIN ruologestione RG ON PJ.id = RG.id_progetto" + 
    		"		INNER JOIN persona P ON RG.id_persona = P.id" + 
    		"		INNER JOIN identita I ON P.id = I.id1_persona" + 
    		"		INNER JOIN usr U ON I.id0_usr = U.id" + 
    		"	WHERE 	P.id = ?";
    
    /**
     * Estrae un progetto di dato id, passato come parametro
     */
    public static final String GET_PROJECT = 
            "SELECT " +
            "       PJ.id           " + 
            "   ,   PJ.id_dipart                AS \"idDipart\"" + 
            "   ,   PJ.titolo                   AS \"titolo\"" + 
            "   ,   PJ.descrizione              AS \"descrizione\"" + 
            "   ,   PJ.datainizio               AS \"dataInizio\"" + 
            "   ,   PJ.datafine                 AS \"dataFine\"" + 
            "   ,   PJ.id_statoprogetto         AS \"idStatoProgetto\"" + 
            "   ,   PJ.statotempi               AS \"statoTempi\"" + 
            "   ,   PJ.statocosti               AS \"statoCosti\"" + 
            "   ,   PJ.statorischi              AS \"statoRischi\"" + 
            "   ,   PJ.statorisorse             AS \"statoRisorse\"" + 
            "   ,   PJ.statoscope               AS \"statoScope\"" + 
            "   ,   PJ.statocomunicazione       AS \"statoComunicazione\"" + 
            "   ,   PJ.statoqualita             AS \"statoQualita\"" + 
            "   ,   PJ.statoapprovvigionamenti  AS \"statoApprovvigionamenti\"" + 
            "   ,   PJ.statostakeholder         AS \"statoStakeholder\"" + 
            "   ,   PJ.meseriferimento          AS \"meseRiferimento\"" + 
            "   ,   PJ.descrizionestatocorrente AS \"descrizioneStatoCorrente\"" + 
            "   ,   PJ.situazioneattuale        AS \"situazioneAttuale\"" + 
            "   ,   PJ.situazionefinale         AS \"situazioneFinale\"" + 
            "   ,   PJ.obiettivimisurabili      AS \"obiettiviMisurabili\"" + 
            "   ,   PJ.minacce                  AS \"minacce\"" + 
            "   ,   PJ.stakeholdermarginali     AS \"stakeholderMarginali\"" + 
            "   ,   PJ.stakeholderoperativi     AS \"stakeholderOperativi\"" + 
            "   ,   PJ.stakeholderistituzionali AS \"stakeholderIstituzionali\"" + 
            "   ,   PJ.stakeholderchiave        AS \"stakeholderChiave\"" + 
            "   ,   PJ.deliverable              AS \"deliverable\"" + 
            "   ,   PJ.fornitorichiaveinterni   AS \"fornitoriChiaveInterni\"" + 
            "   ,   PJ.fornitorichiaveesterni   AS \"fornitoriChiaveEsterni\"" + 
            "   ,   PJ.serviziateneo            AS \"serviziAteneo\"" + 
            "   ,   PJ.vincoli                  AS \"vincoli\"" + 
            "   FROM progetto PJ" + 
            "       INNER JOIN ruologestione RG ON PJ.id = RG.id_progetto" + 
            "       INNER JOIN persona P ON RG.id_persona = P.id" + 
            "   WHERE   PJ.id = ?" +
            "       AND P.id= ?";

    /**
     * Estrae un dipartimento dato il suo id, passato come parametro
     */
    public static final String GET_DIPART = 
            "SELECT " +
            "       D.id           " + 
            "   ,   D.nome                      AS \"nome\"" + 
            "   ,   D.prefisso                  AS \"prefisso\"" + 
            "   ,   D.indirizzosede             AS \"indirizzoSede\"" + 
            "   FROM dipartimento D" + 
            "   WHERE   D.id = ?";
    
    /** 
     * Estrae lo stato di un progetto dato il suo id, passato come parametro
     */
    public static final String GET_STATOPROGETTO = 
    		"SELECT " +
    		"		SP.id		AS \"id\"" +
    		"	, 	SP.nome		AS \"nome\"" +
    		"	, 	SP.valore	AS \"valore\"" + 
    		"	FROM statoprogetto SP" + 
    		"	WHERE SP.id = ?";
    
    /**
     * <p>Modifica la tupla della tabella attivit&agrave; identificata dall'id, che &egrave; passato come parametro.</p>
     * <p>I ? servono per prelevare i dati modificati dalle form, e settarli quindi nel db 
     * (tranne nella clausola WHERE dove sta ad indicare l'id dell'attività da modificare).</p>
     * <p>Ho considerato che nella tabella attivita i seguenti attributi non possono essere modificati: <br />
     *      - id<br />
     *      - id_progetto<br />
     *      - id_wbs<br />
     *      - id_complessita<br />
     *      - id_stato<br />
     *      - id_ruolo<br />
     *      - id_persona<br />
     * se si possono modificare, basta aggiungere la stringa da settare.</p>
     */
    public static final String UPDATE_ATTIVITA = 
            "UPDATE attivita" +
            "   SET     nome = ?" +
            "   ,       descrizione = ?" + 
            "   ,       datainizio = ?" + 
            "   ,       datafine = ?" + 
            "   ,       datainizioattesa = ?" + 
            "   ,       datafineattesa = ?" + 
            "   ,       datainizioeffettiva = ?" + 
            "   ,       datafineeffettiva = ?" + 
            "   ,       guprevisti = ?" + 
            "   ,       gueffettivi = ?" + 
            "   ,       gurimanenti = ?" + 
            "   ,       noteavanzamento = ?" + 
            "   ,       milestone = ?" + 
            "   WHERE id = ?";

    /**
     * <p>Modifica i campi della vision di un progetto, identificato tramite l'id , passato come parametro.</p>
     */
    public static final String UPDATE_VISION = 
            "UPDATE progetto" + 
            "   SET     situazioneattuale = ?" +
            "   ,       descrizione = ?" +
            "   ,       obiettivimisurabili = ?" +
            "   ,       minacce = ? " + 
            "   WHERE id = ?";
    /**
     * <p>Modifica i campi degli stakeholder di un progetto, identificato tramite l'id, passato come parametro.</p>
     */
    public static final String UPDATE_STAKEHOLDER = 
            "UPDATE progetto" + 
            "   SET     stakeholdermarginali=?" +
            "   ,       stakeholderoperativi=? " +
            "   ,       stakeholderistituzionali=?" +
            "   ,       stakeholderchiave=?" + 
            "   WHERE id=?";
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
          //+ " FROM \"AD-Semplici-II2017\" AD"
          + " FROM \"ADSemplici-2018-I\" AD"
          + " ORDER BY \"codiceFiscaleDocente\", \"codiceADUGOV\", \"codiceCdSUGOV\", \"inizioPerDid\", \"finePerDid\""
          + " --ORDER BY \"id\" "
          + " --LIMIT 30";
    
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