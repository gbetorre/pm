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
     * <p>Costante parlante per flag di recupero progetti utente che lo stesso pu&ograve; editaere.</p>
     */
    public static final boolean GET_WRITABLE_PROJECTS_ONLY = false;
    /**
     * <p>Costante parlante per flag di recupero WBS eclusivamente di tipo Work Package.</p>
     */
    public static final boolean GET_WORK_PACKAGES_ONLY = false;
    /**
     * <p>Costante parlante per flag di recupero attivit&agrave; eclusivamente di tipo milestone.</p>
     */
    public static final boolean GET_MILESTONES_ONLY = true;
    /**
     * <p>Costante parlante per flag di recupero progetti utente 
     * indipendentemente dal livello di autorizzazione 
     * che l'utente ha sui progetti stessi.</p>
     */
    public static final boolean GET_ALL = true;
    /* ************************************************************************ *
     *      Costanti corrispondenti ai parametri ammessi sulla querystring      *
     * ************************************************************************ */
    /**
     * <p>Costante per il parametro identificante la pagina dei credits dell'applicazione.</p>
     */
    public static final String CREDITS      = "credits";
    /**
     * <p>Costante per il parametro identificante la pagina della Vision di un progetto.</p>
     */
    public static final String PART_PROJECT_CHARTER_VISION      = "pcv";
    /**
     * <p>Costante per il parametro identificante la pagina degli Stakeholder di un progetto.</p>
     */
    public static final String PART_PROJECT_CHARTER_STAKEHOLDER = "pcs";
    /**
     * <p>Costante per il parametro identificante la pagina dei Deliverable di un progetto.</p>
     */
    public static final String PART_PROJECT_CHARTER_DELIVERABLE = "pcd";
    /**
     * <p>Costante per il parametro identificante la pagina delle Risorse di un progetto.</p>
     */
    public static final String PART_PROJECT_CHARTER_RESOURCE    = "pcr";
    /**
     * <p>Costante per il parametro identificante la pagina dei Rischi di un progetto.</p>
     */
    public static final String PART_PROJECT_CHARTER_RISK        = "pck";
    /**
     * <p>Costante per il parametro identificante la pagina dei Vincoli di un progetto.</p>
     */
    public static final String PART_PROJECT_CHARTER_CONSTRAINT  = "pcc";
    /**
     * <p>Costante per il parametro identificante la pagina delle Milestone di un progetto.</p>
     */
    public static final String PART_PROJECT_CHARTER_MILESTONE   = "pcm";
    /**
     * <p>Costante per il parametro identificante parte applicazione legata a un progetto.</p>
     */
    public static final String PART_PROJECT                     = "prj";
    /**
     * <p>Costante per il parametro identificante la pagina dello Status di un progetto.</p>
     */
    public static final String PART_STATUS                      = "sts";
    /**
     * <p>Costante per il parametro identificante la pagina delle WBS di un progetto.</p>
     */
    public static final String PART_WBS                         = "wbs";
    /**
     * <p>Costante per il parametro identificante la pagina delle Attivit&agrave; di un progetto.</p>
     */
    public static final String PART_ACTIVITY                    = "act";
    /**
     * <p>Costante per il parametro identificante la pagina del Report di un progetto.</p>
     */
    public static final String PART_REPORT                      = "rep";
    /**
     * <p>Costante per il parametro identificante la pagina di inserimento di uno status di un progetto.</p>
     */
    public static final String ADD_STATUS_TO_PROJECT            = "addSts";
    /**
     * <p>Costante per il parametro identificante la pagina di inserimento di una entit&agrave; nel progetto.</p>
     */
    public static final String ADD_TO_PROJECT                   = "add";
    /**
     * <p>Costante per il parametro identificante la pagina di inserimento di un'attivit&agrave; di un progetto.</p>
     */
    public static final String ADD_ACTIVITY_TO_PROJECT          = "add";
    /**
     * <p>Costante per il parametro identificante la pagina di modifica di una entit&agrave;.</p>
     */
    public static final String MODIFY_PART                      = "mod";
    /**
     * <p>Costante per il parametro identificante la parte di eliminazione di una entit&agrave;.</p>
     */
    public static final String DELETE_PART                      = "del";
    /**
     * <p>Costante per il parametro identificante la pagina di inserimento di un rischio di un progetto.</p>
     */
    public static final String ADD_RISK_TO_PROJECT              = "addRisk";
    /**
     * <p>Costante per il parametro identificante la pagina di inserimento di una competenza di un progetto.</p>
     */
    public static final String ADD_SKILL_TO_PROJECT             = "addSkill";
    /**
     * <p>Costante identificante la query che estrae tutte le WBS compresi i Workpackage</p>
     */
    public static final int WBS_ALL = 1;
    /**
     * <p>Costante identificante la query che estrae tutte le WBS esclusi i Workpackage</p>
     */
    public static final int WBS_NOT_WP = 2;
    /**
     * <p>Costante identificante la query che estrae tutte le WBS che sono Workpackage</p>
     */
    public static final int WBS_ONLY_WP = 3;
    /**
     * <p>Costante identificante la query che estrae la WBS padre della WBS data</p>
     */
    public static final int WBS_PARENT_ONLY = 4;
    /**
     * <p>Costante identificante la query che estrae le WBS figlie della WBS data</p>
     */
    public static final int WBS_CHILDREN_ONLY = 5;
    /**
     * <p>Costante identificante la query che estrae le attivit&agrave; correnti nel periodo corrente di avanzamento progetto.</p>
     */
    public static final int ACT_GET_CURRENT_STATUS = 6;
    /**
     * <p>Costante identificante la query che estrae le attivit&agrave; correnti nel periodo prossimo di avanzamento progetto.</p>
     */
    public static final int ACT_GET_NEXT_STATUS = 7;
    /**
     * <p>Costante identificante la query che estrae le attivit&agrave; future rispetto al periodo corrente di avanzamento progetto.</p>
     */
    public static final int ACT_GET_FUTURE_ACTIVITIES = 8;
    /* ************************************************************************ *
     *   Enumerativi statici per incapsulare i valori di enumerativi dinamici   *
     * ************************************************************************ */
    /**
     * <p>Valori possibili degli attributi di stato di un progetto.</p>
     */
    static final String[] STATI = {"OK", "POSSIBILI PROBLEMI", "CRITICO"};
    /**
     * <p>Valori possibili dell'attributo nome di statoprogetto.</p>
     */
    static final String[] STATI_PROGETTO = {"APERTO", "IN PROGRESS", "SOSPESO", "CONCLUSO", "ELIMINATO"};
    /**
     * <p>Valori possibili degli attributi probabilita, impatto, livello di rischio.</p>
     */
    static final String[] LIVELLI_RISCHIO = {"ALTO", "MEDIO", "BASSO"};
    /**
     * <p>Valori possibili dell'attributo stato di rischio.</p>
     */
    static final String[] STATO_RISCHIO = {"APERTO", "CHIUSO", "IN RISOLUZIONE"};
    /**
     * <p>Lista contenente i possibili valori dei campi di stato del progetto.</p>
     */    
    public static final LinkedList<String> STATI_AS_LIST = new LinkedList<String>(Arrays.asList(STATI));
    /**
     * <p>Lista contenente i possibili valori del campo nome della classe StatoProgettoBean.</p>
     */    
    public static final LinkedList<String> STATI_PROGETTO_AS_LIST = new LinkedList<String>(Arrays.asList(STATI_PROGETTO));
    /**
     * <p>Lista contenente i possibili valori degli attributi probabilita, impatto, livello della classe RiskBean.</p>
     */    
    public static final LinkedList<String> LIVELLI_RISCHIO_AS_LIST = new LinkedList<String>(Arrays.asList(LIVELLI_RISCHIO));
    /**
     * <p>Lista contenente i possibili valori dell'attributo stato della classe RiskBean.</p>
     */    
    public static final LinkedList<String> STATO_RISCHIO_AS_LIST = new LinkedList<String>(Arrays.asList(STATO_RISCHIO));
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
     *                               QUERY DI POL                               *
     * ************************************************************************ *
     *                          1. Query di selezione                           *
     * ************************************************************************ */
    /**
     * <p>Seleziona tutti i valori della tabella Complessita</p>
     */
    public static final String GET_COMPLESSITA = 
            "SELECT " +
            "       C.id        AS \"id\"" +
            "   ,   C.nome      AS \"nome\"" +
            "   FROM complessita C" + 
            "   ORDER BY C.id";
    
    /**
     * <p>Seleziona tutti i valori della tabella StatoAttivita</p>
     */
    public static final String GET_STATI_ATTIVITA = 
            "SELECT " +
            "       SA.id        AS \"id\"" +
            "   ,   SA.nome      AS \"nome\"" +
            "   FROM statoattivita SA" + 
            "   ORDER BY SA.id";
    
    /**
     * <p>Seleziona tutti i valori della tabella StatoAvanzamento</p>
     */
    public static final String GET_STATI_AVANZAMENTO = 
            "SELECT " +
            "       SAV.id        AS \"id\"" +
            "   ,   SAV.nome      AS \"nome\"" +
            "   FROM statoavanzamento SAV" + 
            "   ORDER BY SAV.id";
    
    /**
     * <p>Seleziona tutti i valori della tabella StatoProgetto</p>
     */
    public static final String GET_STATI_PROGETTO = 
            "SELECT " +
            "       SP.id        AS \"id\"" +
            "   ,   SP.nome      AS \"nome\"" +
            "   FROM statoprogetto SP" + 
            "   ORDER BY SP.id";
    
    /**
     * <p>Estrae l'utente con username e password passati come parametri.</p>
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
     * <p>Estrae l'username dell'utente tramite l'id, passato come parametro.</p>
     */
    public static final String GET_USERNAME = 
            "SELECT " +
            "       U.login      AS \"login\"" +
            "   FROM usr U" + 
            "   WHERE U.id = ?";
        
    /**
     * <p>Estrae i ruoli di una persona  
     * avente identificativo passato come parametro.</p>
     */
    public static final String GET_RUOLIPERSONA = 
            "SELECT " +
            "       RG.id_progetto  AS \"id\"" +
            "   ,   RG.id_ruolo     AS \"ordinale\"" +
            "   ,   R.nome          AS \"nome\"" +
            "   FROM ruologestione RG " +
            "       INNER JOIN ruolo R on RG.id_ruolo = R.id " +
            "   WHERE RG.id_persona = ?";
        
    /**
     * <p>Estrae i progetti dell'utente 
     * avente identificativo passato come parametro.</p>
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
    		"	WHERE 	P.id = ?" +
    		"   ORDER BY PJ.titolo ASC";
    
    /**
     * <p>Estrae i progetti dell'utente avente <code>username</code> 
     * passato come parametro e nel contesto dei quali lo stesso risulta essere 
     * un utente che pu&ograve; modificare i dati del progetto.</p>
     */
    public static final String GET_WRITABLE_PROJECTS_BY_USER_NAME = 
            "SELECT " +
            "       PJ.id           " + 
            "   ,   PJ.id_dipart                AS \"idDipart\"" + 
            "   ,   PJ.titolo                   AS \"titolo\"" + 
            "   ,   PJ.descrizione              AS \"descrizione\"" + 
            "   ,   PJ.datainizio               AS \"dataInizio\"" + 
            "   ,   PJ.datafine                 AS \"dataFine\"" + 
            "   ,   PJ.id_statoprogetto         AS \"idStatoProgetto\"" + 
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
            "       INNER JOIN ruolo R ON RG.id_ruolo = R.id" +
            "       INNER JOIN persona P ON RG.id_persona = P.id" + 
            "       INNER JOIN identita I ON P.id = I.id1_persona" + 
            "       INNER JOIN usr U ON I.id0_usr = U.id" + 
            "   WHERE   U.login = ?" + 
            "       AND R.nome IN ('PM', 'TL', 'PMOATE', 'PMODIP')";
    
    /**
     * <p>Estrae i progetti dell'utente avente <code>user id</code> 
     * passato come parametro e nel contesto dei quali lo stesso risulta essere 
     * un utente che pu&ograve; modificare i dati del progetto.</p>
     */
    public static final String GET_WRITABLE_PROJECTS_BY_USER_ID = 
            "SELECT " +
            "       PJ.id           " + 
            "   ,   PJ.id_dipart                AS \"idDipart\"" + 
            "   ,   PJ.titolo                   AS \"titolo\"" + 
            "   ,   PJ.descrizione              AS \"descrizione\"" + 
            "   ,   PJ.datainizio               AS \"dataInizio\"" + 
            "   ,   PJ.datafine                 AS \"dataFine\"" + 
            "   ,   PJ.id_statoprogetto         AS \"idStatoProgetto\"" + 
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
            "       INNER JOIN ruolo R ON RG.id_ruolo = R.id" +
            "       INNER JOIN persona P ON RG.id_persona = P.id" + 
            "       INNER JOIN identita I ON P.id = I.id1_persona" + 
            "       INNER JOIN usr U ON I.id0_usr = U.id" + 
            "   WHERE   U.id = ?" + 
            "       AND R.nome IN ('PM', 'TL', 'PMOATE', 'PMODIP')";
    
    /**
     * <p>Estrae un progetto di dato id, passato come parametro.</p>
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
     * <p>Estrae la wbs (o il workpackage) selezionata,
     * identificata tramite l'id, passato come parametro</p>
     */
    public static final String GET_WBS =
            "SELECT " +
            "       W.id            AS \"id\"" + 
            "   ,   W.nome          AS \"nome\"" + 
            "   ,   W.descrizione   AS \"descrizione\"" + 
            "   ,   W.workpackage   AS \"workPackage\"" + 
            "   FROM wbs W" +
            "       INNER JOIN progetto PJ ON PJ.id = W.id_progetto" +
            "   WHERE PJ.id = ?" +
            "       AND W.id = ?";
    
    /**
     * <p>Estrae la wbs padre data una wbs figlia, 
     * identificata tramite l'id, passato come parametro.</p>
     */
    public static final String GET_WBS_PADRE = 
            "SELECT " +
            "       W.id            AS \"id\"" +
            "   ,   W.nome          AS \"nome\"" +
            "   ,   W.descrizione   AS \"descrizione\"" +
            "   ,   W.workpackage   AS \"workPackage\"" + 
            "   FROM wbs W" + 
            "   WHERE W.id = (SELECT " +
            "                        WF.id_wbs" + 
            "                 FROM wbs WF" + 
            "                 WHERE WF.id = ?)" + 
            "   ORDER BY W.dataultimamodifica ASC";
    
    /**
     * <p>Estrae le wbs figlie data una wbs padre, 
     * identificata tramite l'id, passato come parametro.</p>
     */
    public static final String GET_WBS_FIGLIE = 
            "SELECT " +
            "       W.id            AS \"id\"" +
            "   ,   W.nome          AS \"nome\"" +
            "   ,   W.descrizione   AS \"descrizione\"" +
            "   ,   W.workpackage   AS \"workPackage\"" + 
            "   FROM wbs W" + 
            "   WHERE W.id_progetto = ?" +
            "       AND W.id_wbs = ?" + 
            "   ORDER BY W.dataultimamodifica ASC"; 
    
    /**
     * <p>Estrae le wbs che non sono workpackage di un progetto,
     * identificato tramite l'id, passato come parametro</p>
     */
    public static final String GET_WBS_NOT_WORKPACKAGE =
            "SELECT " +
            "       W.id            AS \"id\"" + 
            "   ,   W.nome          AS \"nome\"" + 
            "   ,   W.descrizione   AS \"descrizione\"" + 
            "   FROM wbs W" +
            "   WHERE W.id_progetto = ?" +
            "     AND W.workpackage = false" + 
            "   ORDER BY W.dataultimamodifica ASC";
    
    /**
     * <p>Estrae le wbs relative ad un progetto, identificato tramite id, passato come parametro</p>
     */
    public static final String GET_WBS_BY_PROJECT =
            "SELECT " +
            "       W.id            AS \"id\"" + 
            "   ,   W.nome          AS \"nome\"" + 
            "   ,   W.descrizione   AS \"descrizione\"" + 
            "   ,   W.workpackage   AS \"workPackage\"" + 
            "   FROM wbs W" +
            "   WHERE W.id_progetto = ?" + 
            "   ORDER BY W.dataultimamodifica ASC";
    
    /**
     * <p>Estrae i workpackage relative ad un progetto, identificato tramite id, passato come parametro</p>
     */
    public static final String GET_WP_BY_PROJECT =
            "SELECT " +
            "       W.id            AS \"id\"" + 
            "   ,   W.nome          AS \"nome\"" + 
            "   ,   W.descrizione   AS \"descrizione\"" + 
            "   ,   W.workpackage   AS \"workPackage\"" + 
            "   FROM wbs W" +
            "   WHERE id_progetto = ?" +
            "     AND W.workpackage = true" +
            "   ORDER BY W.dataultimamodifica ASC";
    
    /**
     * <p>Estrae tutte le persone che hanno un (qualsivoglia) ruolo 
     * in un determinato progetto, identificato tramite id, 
     * passato come parametro.</p>
     * <p>In pratica, se la query restituisce almeno una riga vuol dire che sul
     * progetto, il cui identificativo viene passato come parametro,
     * c'&egrave; almeno una persona.</p>
     */
    public static final String GET_PEOPLE_ON_PROJECT = 
            "SELECT " + 
            "       P.id            AS \"id\"" +
            "   ,   P.nome          AS \"nome\"" +
            "   ,   P.cognome       AS \"cognome\"" +
            "   FROM ruologestione  RG" +
            "       INNER JOIN persona P ON RG.id_persona = P.id" +
            "   WHERE RG.id_progetto = ?";

    /**
     * <p>Verifica se una persona, il cui identificativo viene
     * passato come parametro, sia presente, a qualunque titolo, 
     * cio&egrave; con qualunque ruolo, in un determinato progetto, 
     * identificato tramite id, passato come parametro.</p>
     * <p>In pratica, se la query restituisce una riga vuol dire che sul
     * progetto, il cui identificativo viene passato come parametro,
     * c'&egrave; la persona, il cui identificativo 
     * viene passato come parametro.<br />
     * Se la query non restituisce nulla vuol dire che la persona,
     * il cui identificativo viene passato come parametro, non &egrave;
     * presente sul progetto a nessun titolo, cio&egrave; in alcun ruolo.</p>
     */
    public static final String IS_PEOPLE_ON_PROJECT = 
            "SELECT " +
            "           persona.id " +
            "   FROM    persona "   +
            "   WHERE   persona.id " +
            "   IN (" +
            "       SELECT " + 
            "               P.id            AS \"id\"" +
            "           FROM ruologestione  RG" +
            "               INNER JOIN persona P ON RG.id_persona = P.id" +
            "           WHERE RG.id_progetto = ?" +
            "      )" +
            "     AND   persona.id = ?";
    
    /**
     * <p>Estrae tutte le persone che appartengono al dipartimento 
     * a cui appartiene un determinato progetto,
     * identificato tramite id, passato come parametro.</p>
     */
    public static final String GET_PEOPLE_BY_DEPARTMENT = 
            "SELECT " + 
            "       P.id            AS \"id\"" +
            "   ,   P.nome          AS \"nome\"" +
            "   ,   P.cognome       AS \"cognome\"" +
            "   FROM progetto PJ    " +
            "       INNER JOIN dipartimento D ON D.id = PJ.id_dipart" +
            "       INNER JOIN grp G ON D.id_share_grp = G.id" +
            "       INNER JOIN belongs B ON G.id = B.id1_grp" +
            "       INNER JOIN usr U on B.id0_usr = U.id" +
            "       INNER JOIN identita I ON U.id = I.id0_usr" +
            "       INNER JOIN persona P ON I.id1_persona = P.id" +
            "   WHERE PJ.id = ?" + 
            "   ORDER BY P.cognome, P.nome";
    
    /**
     * <p>Estrae i valori dei campi relativi alla vision di un progetto, 
     * identificato tramite l'id, passato come parametro.</p>
     */
    public static final String GET_PROJECT_VISION = 
            "SELECT " +
            "       PJ.id           " + 
            "   ,   PJ.descrizione              AS \"descrizione\"" + 
            "   ,   PJ.situazioneattuale        AS \"situazioneAttuale\"" + 
            "   ,   PJ.obiettivimisurabili      AS \"obiettiviMisurabili\"" + 
            "   ,   PJ.minacce                  AS \"minacce\"" + 
            "   FROM progetto PJ" + 
            "       INNER JOIN ruologestione RG ON PJ.id = RG.id_progetto" + 
            "       INNER JOIN persona P ON RG.id_persona = P.id" + 
            "   WHERE   PJ.id = ?" +
            "       AND P.id = ?";
    
    /**
     * <p>Estrae i valori dei campi relativi agli stakeholder di un progetto, 
     * identificato tramite l'id, passato come parametro.</p>
     */
    public static final String GET_PROJECT_STAKEHOLDER = 
            "SELECT " +
            "       PJ.id           " +
            "   ,   PJ.stakeholdermarginali     AS \"stakeholderMarginali\"" + 
            "   ,   PJ.stakeholderoperativi     AS \"stakeholderOperativi\"" + 
            "   ,   PJ.stakeholderistituzionali AS \"stakeholderIstituzionali\"" + 
            "   ,   PJ.stakeholderchiave        AS \"stakeholderChiave\"" +
            "   FROM progetto PJ" + 
            "       INNER JOIN ruologestione RG ON PJ.id = RG.id_progetto" + 
            "       INNER JOIN persona P ON RG.id_persona = P.id" + 
            "   WHERE   PJ.id = ?" +
            "       AND P.id= ?";
    
    /**
     * <p>Estrae i valori dei campi relativi alla deliverable di un progetto, 
     * identificato tramite l'id, passato come parametro.</p>
     */
    public static final String GET_PROJECT_DELIVERABLE = 
            "SELECT " +
            "       PJ.id           " + 
            "   ,   PJ.deliverable              AS \"deliverable\"" + 
            "   FROM progetto PJ" + 
            "       INNER JOIN ruologestione RG ON PJ.id = RG.id_progetto" + 
            "       INNER JOIN persona P ON RG.id_persona = P.id" + 
            "   WHERE   PJ.id = ?" +
            "       AND P.id= ?";
    
    /**
     * <p>Estrae i valori dei campi relativi alle risorse di un progetto, 
     * identificato tramite l'id, passato come parametro.</p>
     */
    public static final String GET_PROJECT_RESOURCE = 
            "SELECT " +
            "       PJ.id           " + 
            "   ,   PJ.fornitorichiaveinterni   AS \"fornitoriChiaveInterni\"" + 
            "   ,   PJ.fornitorichiaveesterni   AS \"fornitoriChiaveEsterni\"" + 
            "   ,   PJ.serviziateneo            AS \"serviziAteneo\"" + 
            "   FROM progetto PJ" + 
            "       INNER JOIN ruologestione RG ON PJ.id = RG.id_progetto" + 
            "       INNER JOIN persona P ON RG.id_persona = P.id" + 
            "   WHERE   PJ.id = ?" +
            "       AND P.id= ?";
    
    /**
     * <p>Estrae i valori dei campi relativi ai vincoli di un progetto, 
     * identificato tramite l'id, passato come parametro.</p>
     */
    public static final String GET_PROJECT_CONSTRAINT = 
            "SELECT " +
            "       PJ.id           " + 
            "   ,   PJ.vincoli   AS \"vincoli\"" +
            "   FROM progetto PJ" + 
            "       INNER JOIN ruologestione RG ON PJ.id = RG.id_progetto" + 
            "       INNER JOIN persona P ON RG.id_persona = P.id" + 
            "   WHERE   PJ.id = ?" +
            "       AND P.id = ?";
    
    /**
     * Estrae un dipartimento dato il suo id, passato come parametro
     */
    public static final String GET_DIPART = 
            "SELECT " +
            "       D.id              AS \"id\"" + 
            "   ,   D.nome            AS \"nome\"" + 
            "   ,   D.prefisso        AS \"prefisso\"" + 
            "   ,   D.indirizzosede   AS \"indirizzoSede\"" + 
            "   FROM dipartimento D" + 
            "   WHERE   D.id = ?";
    
    /** 
     * Estrae lo stato di un progetto dato il suo id, passato come parametro
     */
    public static final String GET_STATOPROGETTO = 
    		"SELECT " +
    		"		SP.id		AS \"id\"" +
    		"	, 	SP.nome		AS \"nome\"" +
    		"	, 	SP.valore	AS \"informativa\"" + 
    		"	FROM statoprogetto SP" + 
    		"	WHERE SP.id = ?";
    
    /**
     * <p>Estrae le attivit&agrave; relative ad un progetto, identificato 
     * tramite l'id, passato come parametro.</p>
     */
    public static final String  GET_ACTIVITIES = 
            "SELECT " +
            "       A.id                    AS  \"id\"" +
            "   ,   A.nome                  AS  \"nome\"" +
            "   ,   A.descrizione           AS  \"descrizione\"" +
            "   ,   A.datainizio            AS  \"dataInizio\"" +
            "   ,   A.datafine              AS  \"dataFine\"" +
            "   ,   A.datainizioattesa      AS  \"dataInizioAttesa\"" +
            "   ,   A.datafineattesa        AS  \"dataFineAttesa\"" +
            "   ,   A.datainizioeffettiva   AS  \"dataInizioEffettiva\"" +
            "   ,   A.datafineeffettiva     AS  \"dataFineEffettiva\"" +
            "   ,   A.guprevisti            AS  \"guPrevisti\"" +
            "   ,   A.gueffettivi           AS  \"guEffettivi\"" +
            "   ,   A.gurimanenti           AS  \"guRimanenti\"" +
            "   ,   A.noteavanzamento       AS  \"noteAvanzamento\"" +
            "   ,   A.milestone             AS  \"milestone\"" +
            "   FROM attivita A" + 
            "   WHERE id_progetto = ?" +
            "   ORDER BY A.dataultimamodifica ASC";
    
    /**
     * <p>Estrae le attivit&agrave; relative ad un progetto, identificato 
     * tramite l'id, passato come parametro.</p>
     */
    public static final String  GET_ACTIVITIES_BY_DATE_AND_TYPE = 
            "SELECT " +
            "       A.id                    AS  \"id\"" +
            "   ,   A.nome                  AS  \"nome\"" +
            "   ,   A.descrizione           AS  \"descrizione\"" +
            "   ,   A.datainizio            AS  \"dataInizio\"" +
            "   ,   A.datafine              AS  \"dataFine\"" +
            "   ,   A.datainizioattesa      AS  \"dataInizioAttesa\"" +
            "   ,   A.datafineattesa        AS  \"dataFineAttesa\"" +
            "   ,   A.datainizioeffettiva   AS  \"dataInizioEffettiva\"" +
            "   ,   A.datafineeffettiva     AS  \"dataFineEffettiva\"" +
            "   ,   A.guprevisti            AS  \"guPrevisti\"" +
            "   ,   A.gueffettivi           AS  \"guEffettivi\"" +
            "   ,   A.gurimanenti           AS  \"guRimanenti\"" +
            "   ,   A.noteavanzamento       AS  \"noteAvanzamento\"" +
            "   ,   A.milestone             AS  \"milestone\"" +
            "   FROM attivita A" + 
            "   WHERE id_progetto = ?" +
            "       AND A.datainizio >= ?" +
            "       AND (A.milestone = ? OR ?)" +
            "   ORDER BY A.dataultimamodifica ASC";
    
    /**
     * <p>Estrae le attivit&agrave; di una specifica WBS,
     * identificata tramite id, passato come parametro, relativa ad 
     * un progetto, identificato tramite id, passato come parametro.</p>
     */
    public static final String GET_ACTIVITIES_OF_WBS =
            "SELECT " + 
            "       A.id                    AS  \"id\"" + 
            "   ,   A.nome                  AS  \"nome\"" + 
            "   ,   A.descrizione           AS  \"descrizione\"" + 
            "   ,   A.datainizio            AS  \"dataInizio\"" + 
            "   ,   A.datafine              AS  \"dataFine\"" + 
            "   ,   A.datainizioattesa      AS  \"dataInizioAttesa\"" + 
            "   ,   A.datafineattesa        AS  \"dataFineAttesa\"" + 
            "   ,   A.datainizioeffettiva   AS  \"dataInizioEffettiva\"" + 
            "   ,   A.datafineeffettiva     AS  \"dataFineEffettiva\"" + 
            "   ,   A.guprevisti            AS  \"guPrevisti\"" + 
            "   ,   A.gueffettivi           AS  \"guEffettivi\"" + 
            "   ,   A.gurimanenti           AS  \"guRimanenti\"" + 
            "   ,   A.noteavanzamento       AS  \"noteAvanzamento\"" + 
            "   ,   A.milestone             AS  \"milestone\"" + 
            "   ,   A.id_wbs                AS  \"idWbs\"" + 
            "   ,   A.id_stato              AS  \"idStato\"" + 
            "   ,   A.id_complessita        AS  \"idComplessita\"" + 
            "   FROM attivita A" + 
            "   WHERE id_progetto = ?" + 
            "     AND id_wbs = ?" +
            "   ORDER BY A.dataultimamodifica ASC";
    
    /**
     * <p>Estrae l'attivit&agrave; specificata tramite id 
     * passato come parametro, 
     * relativa ad un progetto, identificato tramite id, 
     * passato come parametro.</p>
     */
    public static final String GET_ACTIVITY = 
            "SELECT " +
            "       A.id                    AS  \"id\"" +
            "   ,   A.nome                  AS  \"nome\"" +
            "   ,   A.descrizione           AS  \"descrizione\"" +
            "   ,   A.datainizio            AS  \"dataInizio\"" +
            "   ,   A.datafine              AS  \"dataFine\"" +
            "   ,   A.datainizioattesa      AS  \"dataInizioAttesa\"" +
            "   ,   A.datafineattesa        AS  \"dataFineAttesa\"" +
            "   ,   A.datainizioeffettiva   AS  \"dataInizioEffettiva\"" +
            "   ,   A.datafineeffettiva     AS  \"dataFineEffettiva\"" +
            "   ,   A.guprevisti            AS  \"guPrevisti\"" +
            "   ,   A.gueffettivi           AS  \"guEffettivi\"" +
            "   ,   A.gurimanenti           AS  \"guRimanenti\"" +
            "   ,   A.noteavanzamento       AS  \"noteAvanzamento\"" +
            "   ,   A.milestone             AS  \"milestone\"" +
            "   ,   A.id_wbs                AS  \"idWbs\"" +
            "   ,   A.id_stato              AS  \"idStato\"" +
            "   ,   A.id_complessita        AS  \"idComplessita\"" +
            "   FROM attivita A" + 
            "   WHERE id_progetto = ?" +
            "     AND id = ?";
    
    /**
     * <p>Estrae le persone che sono collegate ad una attivit&agrave;</p>
     */
    public static final String GET_PEOPLE_ON_ACTIVITY = 
            "SELECT " +
            "       P.id                AS \"id\"" +
            "   ,   P.nome              AS \"nome\"" +
            "   ,   P.cognome           AS \"cognome\"" +
            "   ,   C.descrizione       AS \"mansione\"" +
            "   ,   AG.id_competenza    AS \"idQualificaPrincipaleDip\"" +
            "   FROM attivitagestione   AG" +
            "       INNER JOIN persona P ON AG.id_persona = P.id" +
            "       INNER JOIN competenza C ON AG.id_competenza = C.id" + 
            "   WHERE AG.id_attivita = ?";
    
    /**
     * <p>Estrae le competenze relative ad un progetto, identificato 
     * tramite l'id, passato come parametro.</p>
     */
    public static final String GET_SKILLS = 
            "SELECT " +
            "       C.id            AS \"id\"" +
            "   ,   C.descrizione   AS \"nome\"" +
            "   ,   C.informativa   AS \"informativa\"" + 
            "   ,   C.presenza      AS \"presenza\"" +
            //"   ,   C.id_persona    AS \"idPersona\"" +
            "   FROM competenza C" +
            "   WHERE C.id_progetto = ?";
    
    /**
     * <p>Estrae le persone collegate a una determinata competenza
     * (a sua volta relativa ad un progetto), identificata 
     * tramite il suo id, passato come parametro.</p>
     */
    public static final String GET_PEOPLE_BY_SKILL = 
            "SELECT " +
            "       P.id            AS \"id\"" +
            "   ,   P.nome          AS \"nome\"" +
            "   ,   P.cognome       AS \"cognome\"" +
            "   FROM persona P    " +
            "       INNER JOIN competenzagestione CG ON CG.id_persona = P.id" +
            "       INNER JOIN competenza C ON CG.id_competenza = C.id" +
            "   WHERE C.id = ?" + 
            "   ORDER BY P.cognome, P.nome";
    
    /**
     * <p>Estrae le competenze relative ad una persona 
     * &ndash; il cui identificativo viene passato come parametro &ndash; 
     * collegata ad un dato progetto 
     * &ndash; il cui identificativo viene passato come parametro.</p>
     */
    public static final String GET_SKILLS_BY_PERSON = 
            "SELECT " +
            "       C.id            AS \"id\"" +
            "   ,   C.descrizione   AS \"nome\"" +
            "   ,   C.informativa   AS \"informativa\"" + 
            "   ,   C.presenza      AS \"presenza\"" +
            "   FROM competenza C" +
            "       INNER JOIN competenzagestione CG ON CG.id_competenza = C.id" +
            "   WHERE C.id_progetto = ?"
            + "     AND CG.id_persona = ?";
    
    /**
     * <p>Estrae i rischi relativi ad un progetto, identificato 
     * tramite l'id, passato come parametro.</p>
     */
    public static final String GET_RISKS = 
            "SELECT " +
            "       R.id            AS \"id\"" +
            "   ,   R.descrizione   AS \"nome\"" +
            "   ,   R.probabilita   AS \"informativa\"" + 
            "   ,   R.impatto       AS \"impatto\"" + 
            "   ,   R.livello       AS \"livello\"" +
            "   ,   R.stato         AS \"stato\"" +
            "   FROM rischio R" +
            "   WHERE id_progetto = ?" +
            "   ORDER BY R.descrizione ASC";
    
    
    /**
     * <p>Estrae lo stato costi dell'avanzamento di un progetto, identificato tramite id, passato
     * come parametro.</p>
     */
    public static final String GET_STATOCOSTI =  
            "SELECT "  +
            "       AP.id_statocosti                AS \"id\"" +
            "   ,   SAV1.nome                       AS \"nome\"" +
            "   FROM avanzamentoprogetto AP" +
            "       INNER JOIN statoavanzamento SAV1 ON AP.id_statocosti = SAV1.id" +
            "   WHERE AP.id = ?";
    

    /**
     * <p>Estrae lo stato tempi dell'avanzamento di un progetto, identificato tramite id, passato
     * come parametro.</p>
     */
    public static final String GET_STATOTEMPI =  
            "SELECT "  +
            "       AP.id_statotempi                AS \"id\"" +
            "   ,   SAV1.nome                       AS \"nome\"" +
            "   FROM avanzamentoprogetto AP" +
            "       INNER JOIN statoavanzamento SAV1 ON AP.id_statotempi = SAV1.id" +
            "   WHERE AP.id = ?";
    
    
    /**
     * <p>Estrae lo stato rischi dell'avanzamento di un progetto, identificato tramite id, passato
     * come parametro.</p>
     */
    public static final String GET_STATORISCHI =  
            "SELECT "  +
            "       AP.id_statorischi               AS \"id\"" +
            "   ,   SAV1.nome                       AS \"nome\"" +
            "   FROM avanzamentoprogetto AP" +
            "       INNER JOIN statoavanzamento SAV1 ON AP.id_statorischi = SAV1.id" +
            "   WHERE AP.id = ?";
    
    
    /**
     * <p>Estrae lo stato risorse dell'avanzamento di un progetto, identificato tramite id, passato
     * come parametro.</p>
     */
    public static final String GET_STATORISORSE =  
            "SELECT "  +
            "       AP.id_statorisorse              AS \"id\"" +
            "   ,   SAV1.nome                       AS \"nome\"" +
            "   FROM avanzamentoprogetto AP" +
            "       INNER JOIN statoavanzamento SAV1 ON AP.id_statorisorse = SAV1.id" +
            "   WHERE AP.id = ?";
    
    
    /**
     * <p>Estrae lo stato scope dell'avanzamento di un progetto, identificato tramite id, passato
     * come parametro.</p>
     */
    public static final String GET_STATOSCOPE =  
            "SELECT "  +
            "       AP.id_statoscope                AS \"id\"" +
            "   ,   SAV1.nome                       AS \"nome\"" +
            "   FROM avanzamentoprogetto AP" +
            "       INNER JOIN statoavanzamento SAV1 ON AP.id_statoscope = SAV1.id" +
            "   WHERE AP.id = ?";
    
    
    /**
     * <p>Estrae lo stato risorse dell'avanzamento di un progetto, identificato tramite id, passato
     * come parametro.</p>
     */
    public static final String GET_STATOCOMUNICAZIONE =  
            "SELECT "  +
            "       AP.id_statocomunicazione              AS \"id\"" +
            "   ,   SAV1.nome                             AS \"nome\"" +
            "   FROM avanzamentoprogetto AP" +
            "       INNER JOIN statoavanzamento SAV1 ON AP.id_statocomunicazione = SAV1.id" +
            "   WHERE AP.id = ?";
    
    
    /**
     * <p>Estrae lo stato qualit&agrave; dell'avanzamento di un progetto, identificato tramite id, passato
     * come parametro.</p>
     */
    public static final String GET_STATOQUALITA =  
            "SELECT "  +
            "       AP.id_statoqualita              AS \"id\"" +
            "   ,   SAV1.nome                       AS \"nome\"" +
            "   FROM avanzamentoprogetto AP" +
            "       INNER JOIN statoavanzamento SAV1 ON AP.id_statoqualita = SAV1.id" +
            "   WHERE AP.id = ?";
    
    
    /**
     * <p>Estrae lo stato approvvigionamenti dell'avanzamento di un progetto, identificato tramite id, passato
     * come parametro.</p>
     */
    public static final String GET_STATOAPPROVVIGIONAMENTI =  
            "SELECT "  +
            "       AP.id_statoapprovvigionamenti              AS \"id\"" +
            "   ,   SAV1.nome                                  AS \"nome\"" +
            "   FROM avanzamentoprogetto AP" +
            "       INNER JOIN statoavanzamento SAV1 ON AP.id_statoapprovvigionamenti = SAV1.id" +
            "   WHERE AP.id = ?";
    
    
    /**
     * <p>Estrae lo stato stakeholder dell'avanzamento di un progetto, identificato tramite id, passato
     * come parametro.</p>
     */
    public static final String GET_STATOSTAKEHOLDER =  
            "SELECT "  +
            "       AP.id_statostakeholder              AS \"id\"" +
            "   ,   SAV1.nome                           AS \"nome\"" +
            "   FROM avanzamentoprogetto AP" +
            "       INNER JOIN statoavanzamento SAV1 ON AP.id_statostakeholder = SAV1.id" +
            "   WHERE AP.id = ?";
    
    
    /**
     * <p>Estrae lo stato di avanzamento selezionato di un progetto, identificato tramite id,
     * passato come parametro</p>
     */
    public static final String GET_PROJECT_STATUS = 
            "SELECT " +
            "       AP.id                           AS \"id\"" +
            "   ,   AP.datainizio                   AS \"dataInizio\"" + 
            "   ,   AP.datafine                     AS \"dataFine\"" +
            "   ,   AP.descrizioneavanzamento       AS \"descrizioneAvanzamento\"" +
            "   ,   AP.dataultimamodifica           AS \"dataUltimaModifica\"" +
            "   ,   AP.oraultimamodifica            AS \"oraUltimaModifica\"" +
            "   ,   AP.autoreultimamodifica         AS \"autoreUltimaModifica\"" +
            "   FROM avanzamentoprogetto AP" +
            "   WHERE AP.id_progetto = ? " +
            "       AND AP.datainizio = ?";
    
    /**
     * <p>Estrae lo stato di avanzamento di un progetto, identificato tramite id,
     * passato come parametro</p>
     */
    public static final String GET_STATUS = 
            "SELECT " +
            "       AP.id                           AS \"id\"" +
            "   ,   AP.datainizio                   AS \"dataInizio\"" + 
            "   ,   AP.datafine                     AS \"dataFine\"" +
            "   ,   AP.descrizioneavanzamento       AS \"descrizioneAvanzamento\"" +
            "   ,   AP.dataultimamodifica           AS \"dataUltimaModifica\"" +
            "   ,   AP.oraultimamodifica            AS \"oraUltimaModifica\"" +
            "   ,   AP.autoreultimamodifica         AS \"autoreUltimaModifica\"" +
            "   FROM avanzamentoprogetto AP" +
            "   WHERE AP.id = ?";
    
    /**
     * <p>Estrae tutti gli stati di avanzamento di un progetto, identificato tramite id,
     * passato come parametro</p>
     */
    public static final String GET_PROJECT_STATUS_LIST = 
            "SELECT " +
            "       AP.id                       AS \"id\"" +
            "   ,   AP.datainizio               AS \"dataInizio\"" + 
            "   ,   AP.datafine                 AS \"dataFine\"" +
            "   FROM avanzamentoprogetto AP " +
            "   WHERE AP.id_progetto = ?" +
            "   ORDER BY AP.datainizio";
    
    /**
     * <p>Estrae le attivit&agrave; presenti nel periodo di avanzamento progetto corrente.</p>
     */
    public static final String GET_CURRENT_ACTIVITIES = 
            "SELECT DISTINCT" +
            "       A.id                    AS  \"id\"" +
            "   ,   A.nome                  AS  \"nome\"" +
            "   ,   A.descrizione           AS  \"descrizione\"" +
            "   ,   A.datainizio            AS  \"dataInizio\"" +
            "   ,   A.datafine              AS  \"dataFine\"" +
            "   ,   A.datainizioattesa      AS  \"dataInizioAttesa\"" +
            "   ,   A.datafineattesa        AS  \"dataFineAttesa\"" +
            "   ,   A.datainizioeffettiva   AS  \"dataInizioEffettiva\"" +
            "   ,   A.datafineeffettiva     AS  \"dataFineEffettiva\"" +
            "   ,   A.guprevisti            AS  \"guPrevisti\"" +
            "   ,   A.gueffettivi           AS  \"guEffettivi\"" +
            "   ,   A.gurimanenti           AS  \"guRimanenti\"" +
            "   ,   A.noteavanzamento       AS  \"noteAvanzamento\"" +
            "   ,   A.milestone             AS  \"milestone\"" +
            "   ,   A.id_wbs                AS  \"idWbs\"" +
            "   ,   A.id_stato              AS  \"idStato\"" +
            "   ,   A.id_complessita        AS  \"idComplessita\"" +
            "   FROM attivita A " +
            "      INNER JOIN progetto P ON P.id = A.id_progetto " +
            "   WHERE P.id = ? " +
            "       AND ( " +
            "                   (A.datainizio <= ? AND A.datafine >= ?) " +
            "                OR ((A.datainizio BETWEEN ? AND ?))" +
            "           )";
    
    /**
     * <p>Estrae lo status di avanzamento di un progetto successivo, in ordine di datainizio, 
     * a quello con datainizio passata come parametro.</p>
     */
    public static final String GET_NEXT_STATUS = 
            "SELECT " +
            "       AP.id                           AS \"id\"" +
            "   ,   AP.datainizio                   AS \"dataInizio\"" + 
            "   ,   AP.datafine                     AS \"dataFine\"" +
            "   ,   AP.descrizioneavanzamento       AS \"descrizioneAvanzamento\"" +
            "   ,   AP.dataultimamodifica           AS \"dataUltimaModifica\"" +
            "   ,   AP.oraultimamodifica            AS \"oraUltimaModifica\"" +
            "   ,   AP.autoreultimamodifica         AS \"autoreUltimaModifica\"" +
            "   FROM    avanzamentoprogetto AP" +
            "   WHERE AP.datainizio = (" +
            "                           SELECT " +
            "                               MIN(AP1.datainizio) " +
            "                           FROM avanzamentoprogetto AP1 " +
            "                           WHERE AP1.id_progetto = ? " +
            "                               AND AP1.datainizio > ? " +
            "                         )";
    
    /**
     * <p>Estrae tutte le attivit&agrave; che avranno inizio nel periodo 
     * di avanzamento progetto successivo a quello attuale, 
     * identificato tramite data inizio, passata come parametro.</p>
     */
    public static final String GET_ACTIVITIES_OF_NEXT_STATUS = 
            "SELECT DISTINCT " +
            "       A.id                    AS  \"id\"" +
            "   ,   A.nome                  AS  \"nome\"" +
            "   ,   A.descrizione           AS  \"descrizione\"" +
            "   ,   A.datainizio            AS  \"dataInizio\"" +
            "   ,   A.datafine              AS  \"dataFine\"" +
            "   ,   A.datainizioattesa      AS  \"dataInizioAttesa\"" +
            "   ,   A.datafineattesa        AS  \"dataFineAttesa\"" +
            "   ,   A.datainizioeffettiva   AS  \"dataInizioEffettiva\"" +
            "   ,   A.datafineeffettiva     AS  \"dataFineEffettiva\"" +
            "   ,   A.guprevisti            AS  \"guPrevisti\"" +
            "   ,   A.gueffettivi           AS  \"guEffettivi\"" +
            "   ,   A.gurimanenti           AS  \"guRimanenti\"" +
            "   ,   A.noteavanzamento       AS  \"noteAvanzamento\"" +
            "   ,   A.milestone             AS  \"milestone\"" +
            "   ,   A.id_wbs                AS  \"idWbs\"" +
            "   ,   A.id_stato              AS  \"idStato\"" +
            "   ,   A.id_complessita        AS  \"idComplessita\"" +
            "   FROM attivita A " + 
            "      INNER JOIN progetto P ON P.id = A.id_progetto " +
            "   WHERE P.id = ? " +
            "      AND (A.datainizio BETWEEN ? AND ?)";
    
    /* ************************************************************************ *
     *                               QUERY DI POL                               *
     * ************************************************************************ *
     *                       2.  Query di aggiornamento                         *
     * ************************************************************************ */ 
    /**
     * <p>Modifica la tupla della tabella attivit&agrave; identificata dall'id, 
     * che &egrave; passato come parametro.</p>
     * <p>I ? servono per prelevare i dati modificati dalle form, 
     * e settarli quindi nel db 
     * (tranne nella clausola WHERE dove il question mark sta ad indicare 
     * l'id dell'attivit&agrave; da modificare).</p>
     * <p>Ho considerato che nella tabella <code>attivita</code> 
     * i seguenti attributi non possono essere modificati:
     * <ul>
     * <li>id</li>
     * <li>id_progetto</li>
     * </ul>
     * I seguenti campi devono essere modificati in altra relazione:
     * <ul>
     * <li>id_ruolo</li>
     * <li>id_persona</li>
     * </ul></p>
     */
    public static final String UPDATE_ACTIVITY = 
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
          //"   ,       id_progetto = ?" +
            "   ,       id_wbs = ?" +
            "   ,       id_complessita = ?" +
            "   ,       id_stato = ?" +
            "   ,       dataultimamodifica = ?" +
            "   ,       oraultimamodifica = ?" +
            "   ,       autoreultimamodifica = ?" +
            "   WHERE id = ?";

    /**
     * <p>Query per aggiornamento di persona su una attivit&agrave;.</p>
     */
    public static final String UPDATE_PERSON_ON_ACTIVITY = 
            "UPDATE attivitagestione" +
            "   SET     id_persona  = ?" +
            "   ,       id_competenza = ?" +
            "   WHERE id_attivita = ?";
    
    /**
     * <p>Modifica la tupla della tabella attivit&agrave; identificata dall'id, 
     * che &egrave; passato come parametro.</p>
     * <p>Modifica solo i campi che sono presenti nel Project Charter del progetto.</p>
     */
    public static final String UPDATE_ATTIVITA_FROM_PROGETTO =
            "UPDATE attivita" +
            "   SET     nome = ?" +
            "   ,       descrizione = ?" +
            "   ,       milestone = ?" + 
            "   WHERE   id = ? " +
            "       AND id_progetto = ?";
    
    /**
     * <p>Modifica i campi della vision di un progetto, identificato 
     * tramite l'id, passato come parametro.</p>
     */
    public static final String UPDATE_VISION = 
            "UPDATE progetto" + 
            "   SET     situazioneattuale = ?" +
            "   ,       descrizione = ?" +
            "   ,       obiettivimisurabili = ?" +
            "   ,       minacce = ? " + 
            "   WHERE id = ?";
    
    /**
     * <p>Modifica i campi degli stakeholder di un progetto, identificato 
     * tramite l'id, passato come parametro.</p>
     */
    public static final String UPDATE_STAKEHOLDER = 
            "UPDATE progetto" + 
            "   SET     stakeholdermarginali = ?" +
            "   ,       stakeholderoperativi = ? " +
            "   ,       stakeholderistituzionali = ?" +
            "   ,       stakeholderchiave = ?" + 
            "   WHERE id = ?";
    
    /**
     * <p>Modifica il campo della deliverable di un progetto, identificato 
     * tramite l'id, passato come parametro.</p>
     */
    public static final String UPDATE_DELIVERABLE = 
            "UPDATE progetto" + 
            "   SET     deliverable = ?" + 
            "   WHERE id = ?";
    
    /**
     * <p>Modifica i campi della competenza, identificata tramite id, di un progetto, identificato 
     * tramite l'id, passato come parametro.</p>
     */
    public static final String UPDATE_SKILL_FROM_PROJECT = 
            "UPDATE competenza" +
            "   SET     descrizione = ?" +
            "   ,       informativa = ?" +
            "   ,       presenza = ?" +
            "   WHERE id = ?" +
            "       AND id_progetto = ?";
    
    /**
     * <p>Modifica i campi delle risorse di un progetto, identificato 
     * tramite l'id, passato come parametro.</p>
     */
    public static final String UPDATE_RESOURCE = 
            "UPDATE progetto" +
            "   SET     fornitorichiaveesterni = ?" +
            "   ,       fornitorichiaveinterni = ?" +
            "   ,       serviziateneo = ?" +
            "   WHERE id = ?";
    
    /**
     * <p>Modifica i rischi di un progetto, identificato tramite l'id, 
     * passato come parametro.</p>
     */
    public static final String UPDATE_RISK = 
            "UPDATE rischio" +
            "   SET     descrizione = ?" + 
            "   ,       probabilita = ?" + 
            "   ,       impatto = ?" + 
            "   ,       livello = ?" + 
            "   ,       stato = ?" +
            "   WHERE id = ?" + 
            "       AND id_progetto = ?";
    
    /**
     * <p>Modifica il campo dei vincoli di un progetto, identificato 
     * tramite l'id, passato come parametro.</p>
     */
    public static final String UPDATE_CONSTRAINT = 
            "UPDATE progetto" + 
            "   SET vincoli = ?" +
            "   WHERE id = ?";
    
    /**
     * <p>Modifica i campi dello status del progetto identificato 
     * tramite l'id, passato come parametro.</p>
     */
    public static final String UPDATE_PROJECT_STATUS = 
            "UPDATE avanzamentoprogetto" + 
            "   SET     datainizio = ?" +
            "   ,       datafine = ?" +
            "   ,       descrizioneavanzamento = ?" +
            "   ,       id_statotempi = ?" + 
            "   ,       id_statocosti = ?" +
            "   ,       id_statorischi = ?" +
            "   ,       id_statorisorse = ?" +
            "   ,       id_statoscope = ?" +
            "   ,       id_statocomunicazione = ?" +
            "   ,       id_statoqualita = ?" +
            "   ,       id_statoapprovvigionamenti = ?" +
            "   ,       id_statostakeholder = ?" +
            "   ,       dataultimamodifica = ?" +
            "   ,       oraultimamodifica = ?" +
            "   ,       autoreultimamodifica = ?" +
            "   ,       id_progetto = ?" +
            "   WHERE id = ?";
    
    /**
     * <p>Modifica una WBS (o workpackage) selezionato,
     * identificato tramite l'id, passato come parametro.</p>
     */
    public static final String UPDATE_WBS = 
            "UPDATE wbs" +
            "   SET     nome = ?" +
            "   ,       descrizione = ?" +
            "   ,       workpackage = ?" +
            "   ,       id_wbs = ?" +
            "   ,       dataultimamodifica = ?" +
            "   ,       oraultimamodifica = ?" +
            "   ,       autoreultimamodifica = ?" +
            "   WHERE id = ?";

        
    /* ************************************************************************ *
     *                               QUERY DI POL                               *
     * ************************************************************************ *
     *                        3.  Query di inserimento                          *
     * ************************************************************************ */
    /**
     * <p>Query di selezione finalizzata all'inserimento</p>
     */
    public static final String SELECT_MAX_ID = 
            "SELECT " +
            "       MAX(id)                     AS \"max\"" +
            "   FROM ";
    
    
    /**
     * 
     */
    public static final String GET_PROJECT_PERIOD = 
            "SELECT " +
            "       PR.mesibase    AS \"mesiBase\"" +
            "   FROM periodoriferimento PR" +
            "       INNER JOIN progetto P ON PR.id = P.id_periodoriferimento" +
            "   WHERE P.id = ?";
    
    
    /**
     * <p>Query per inserimento di un nuovo status progetto.</p>
     */
    public static final String INSERT_STATUS =
            "INSERT INTO avanzamentoprogetto " +
            "   (   id" +
            "   ,   datainizio" +
            "   ,   datafine" +
            "   ,   dataultimamodifica" +
            "   ,   oraultimamodifica" +
            "   ,   autoreultimamodifica" +
            "   ,   id_progetto )" +
            "   VALUES (? " +          // id
            "   ,       ? " +          // datainizio
            "   ,       ? " +          // datafine
            "   ,       ? " +          // data ultima modifica
            "   ,       ? " +          // ora ultima modifica
            "   ,       ? " +          // autore ultima modifica
            "   ,       ?)";           // id_progetto
            

    /**
     * <p>Query per inserimento di una nuova attivit&agrave;.</p>
     */
    public static final String INSERT_ACTIVITY = 
            "INSERT INTO attivita" +
            "   (   id" +
            "   ,   nome" +
            "   ,   descrizione" +
            "   ,   datainizio" +
            "   ,   datafine" +
            "   ,   datainizioattesa" +
            "   ,   datafineattesa" +
            "   ,   datainizioeffettiva" +
            "   ,   datafineeffettiva" +
            "   ,   guprevisti" +
            "   ,   gueffettivi" +
            "   ,   gurimanenti" +
            "   ,   noteavanzamento" +
            "   ,   milestone" +
            "   ,   id_progetto" +
            "   ,   id_wbs" +
            "   ,   id_complessita" +
            "   ,   id_stato" +
            "   ,   dataultimamodifica" +
            "   ,   oraultimamodifica" +
            "   ,   autoreultimamodifica)" +
            "   VALUES (? " +          // id
            "   ,       ? " +          // nome
            "   ,       ? " +          // descrizione
            "   ,       ? " +          // data inizio attività
            "   ,       ? " +          // data fine attività
            "   ,       ? " +          // data inizio attesa
            "   ,       ? " +          // data fine attesa
            "   ,       ? " +          // data inizio effettiva
            "   ,       ? " +          // data fine effettiva
            "   ,       ? " +          // giorni/uomo previsti
            "   ,       ? " +          // giorni/uomo effettivi
            "   ,       ? " +          // giorni/uomo rimanenti
            "   ,       ? " +          // note di avanzamento
            "   ,       ? " +          // milestone
            "   ,       ? " +          // id progetto
            "   ,       ? " +          // id wbs
            "   ,       ? " +          // id complessità
            "   ,       ? " +          // id stato attività
            "   ,       ? " +          // data ultima modifica
            "   ,       ? " +          // ora ultima modifica
            "   ,       ?)" ;          // autore ultima modifica
    
    /**
     * <p>Query per inserimento di persona su una attivit&agrave;.</p>
     */
    public static final String INSERT_PERSON_ON_ACTIVITY = 
            "INSERT INTO attivitagestione" +
            "   (   id" +
            "   ,   id_attivita" +
            "   ,   id_persona" +
            "   ,   id_competenza)" +
            "   VALUES (? " +          // id
            "   ,       ? " +          // id attività
            "   ,       ? " +          // id persona
            "   ,       ?)" ;          // id competenza
    
    /**
     * <p>Query per inserimento di una nuova competenza.</p>
     */
    public static final String INSERT_SKILL = 
            "INSERT INTO competenza" +
            "   (   id" +
            "   ,   descrizione" +
            "   ,   informativa" +
            "   ,   presenza" +
            "   ,   id_progetto" +
            "   ,   id_persona )" +    
            "   VALUES (? " +          // id
            "   ,       ? " +          // descrizione
            "   ,       ? " +          // informativa
            "   ,       ? " +          // presenza
            "   ,       ? " +          // id progetto
            "   ,       ?)" ;          // id persona
    
    /**
     * <p>Query per inserimento di un nuovo rischio.</p>
     */
    public static final String INSERT_RISK = 
            "INSERT INTO rischio" +
            "   (   id" +
            "   ,   descrizione" +
            "   ,   probabilita" +
            "   ,   impatto" +
            "   ,   livello" +
            "   ,   stato" +
            "   ,   id_progetto )" +
            "   VALUES (? " +          // id
            "   ,       ? " +          // descrizione
            "   ,       ? " +          // probabilita�
            "   ,       ? " +          // impatto
            "   ,       ? " +          // livello
            "   ,       ? " +          // stato
            "   ,       ?)" ;          // id progetto
    
    /**
     * <p>Query di inserimento di una nuova WBS (o workpackage)</p>
     */
    public static final String INSERT_WBS = 
            "INSERT INTO wbs" +
            "   (   id" +
            "   ,   id_wbs" +
            "   ,   id_progetto" +
            "   ,   nome" +
            "   ,   descrizione" +
            "   ,   workpackage" + 
            "   ,   dataultimamodifica" + 
            "   ,   oraultimamodifica" + 
            "   ,   autoreultimamodifica)" +
            "   VALUES (? " +       // id
            "   ,       ? " +       // id_wbs
            "   ,       ? " +       // id_progetto
            "   ,       ? " +       // nome
            "   ,       ? " +       // descrizione
            "   ,       ? " +       // workpackage
            "   ,       ? " +       // dataultimamodifica
            "   ,       ? " +       // oraultimamodifica
            "   ,       ? )";       // autoreultimamodifica
    
    
    /* ************************************************************************ *
     *                               QUERY DI POL                               *
     * ************************************************************************ *
     *                       4.  Query di eliminazione                          *
     * ************************************************************************ */
    /**
     * <p>Esegue una eliminazione logica di una WBS, identificata tramite id, 
     * passato come parametro, dal progetto a cui appartiene.</p>
     */
    public String DELETE_WBS = 
            "UPDATE wbs" +
            "   SET id_progetto = ?" +
            "   WHERE id = ?";
    
    /**
     * <p>Estrae il primo progetto con id negativo, dato in input l'id del dipartimento.</p>
     */
    public String GET_PROJECT_FROM_ID_DIPART = 
            "SELECT " +
            "   P.id    AS \"id\"" +
            "   FROM progetto P" + 
            "   WHERE P.id_dipart = ?" +
            "       AND P.id < 0";
    
    
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