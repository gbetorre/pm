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
    public static final boolean GET_ONLY_WRITABLE_PROJECTS = false;
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
     * <p>Costante per il parametro identificante la pagina dello Status di un progetto.</p>
     */
    public static final String PART_PROJECT                     = "prj";
    /**
     * <p>Costante per il parametro identificante la pagina di inserimento di un'Attivit&agrave; di un progetto.</p>
     */
    public static final String ADD_ACTIVITY_TO_PROJECT          = "addAct";
    /**
     * <p>Costante per il parametro identificante la pagina di inserimento di un rischio di un progetto.</p>
     */
    public static final String ADD_RISK_TO_PROJECT              = "addRisk";
    /**
     * <p>Costante per il parametro identificante la pagina di inserimento di una competenza di un progetto.</p>
     */
    public static final String ADD_SKILL_TO_PROJECT             = "addSkill";
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
    
    /* ************************************************************************ *
     *                   Query comuni a tutte le applicazioni                   *
     * ************************************************************************ */
    /**
     * <p>Estrae le classi Command previste per l'applicazione.</p>
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
     *                               Query di POL                               *
     * ************************************************************************ *
     *                          1. Query di selezione                           *
     * ************************************************************************ */    
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
    		"	WHERE 	P.id = ?";
    
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
     * <p>Estrae le wbs relative ad un progetto, identificato tramite id, passato come parametro</p>
     */
    public static final String GET_WBS_OF_PROJECT =
            "SELECT " +
            "       W.id            AS \"id\"" + 
            "   ,   W.nome          AS \"nome\"" + 
            "   ,   W.descrizione   AS \"descrizione\"" + 
            "   ,   W.workpackage   AS \"workPackage\"" + 
            "   FROM wbs W" +
            "   WHERE id_progetto = ?";
    
    /**
     * <p>Estrae tutte le persone che hanno un ruolo in un determinato progetto,
     * identificato tramite id, passato come parametro.</p>
     */
    public static final String GET_PEOPLE_ON_PROJECT = 
            "SELECT " + 
            "       P.id            AS \"id\"" +
            "   ,   P.nome          AS \"nome\"" +
            "   ,   P.cognome       AS \"cognome\"" +
            "   FROM ruologestione RG" +
            "       INNER JOIN persona P ON RG.id_persona = P.id" +
            "   WHERE RG.id_progetto = ?";
    
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
            "   WHERE id_progetto = ?";
    
    /**
     * <p>Estrae l'attivit&agrave; specificata tramite id relativa ad un progetto, identificato 
     * tramite l'id, passato come parametro.</p>
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
            "   FROM attivita A" + 
            "   WHERE id_progetto = ?" +
            "       AND id = ? ";
    
    /**
     * <p>Estrae le persone che sono collegate ad una attivit&agrave;</p>
     */
    public static final String GET_PEOPLE_ON_ACTIVITY = 
            "SELECT " +
            "       P.id        AS \"id\"" +
            "   ,   P.nome      AS \"nome\"" +
            "   ,   P.cognome   AS \"cognome\"" +
            "   FROM attivitagestione AG" +
            "       INNER JOIN persona P ON AG.id_persona = P.id" +
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
            "   ,   C.id_persona    AS \"idPersona\"" +
            "   FROM competenza C" +
            "   WHERE id_progetto = ?";
    
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
            "   WHERE id_progetto = ?";
    
    /**
     * <p>Estrae lo stato di avanzamento selezionato di un progetto, identificato tramite id,
     * passato come parametro</p>
     */
    public static final String GET_PROJECT_STATUS = 
            "SELECT " +
            "       AP.id                       AS \"id\"" +
            "   ,   AP.datainizio               AS \"dataInizio\"" + 
            "   ,   AP.datafine                 AS \"dataFine\"" +
            "   ,   AP.descrizioneavanzamento   AS \"descrizioneAvanzamento\"" +
            "   ,   AP.statotempi               AS \"statoTempi\"" + 
            "   ,   AP.statocosti               AS \"statoCosti\"" + 
            "   ,   AP.statorischi              AS \"statoRischi\"" + 
            "   ,   AP.statorisorse             AS \"statoRisorse\"" + 
            "   ,   AP.statoscope               AS \"statoScope\"" + 
            "   ,   AP.statocomunicazione       AS \"statoComunicazione\"" + 
            "   ,   AP.statoqualita             AS \"statoQualita\"" + 
            "   ,   AP.statoapprovvigionamenti  AS \"statoApprovvigionamenti\"" + 
            "   ,   AP.statostakeholder         AS \"statoStakeholder\"" +
            "   ,   AP.dataultimamodifica       AS \"dataUltimaModifica\"" +
            "   ,   AP.oraultimamodifica        AS \"oraUltimaModifica\"" +
            "   ,   AP.autoreultimamodifica     AS \"autoreUltimaModifica\"" +
            "   FROM avanzamentoprogetto AP " +
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
            "   WHERE AP.id_progetto = ?";

    
    /* ************************************************************************ *
     *                               Query di POL                               *
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
     * i seguenti attributi non possono essere modificati: <br />
     *      - id<br />
     *      - id_progetto<br />
     *      - id_wbs<br />
     *      - id_complessita<br />
     *      - id_stato<br />
     *      - id_ruolo<br />
     *      - id_persona<br />
     * Se potessero essere modificati, baster&agrave; aggiungere 
     * la stringa da settare.</p>
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
            "   ,       statotempi = ?" + 
            "   ,       statocosti = ?" +
            "   ,       statorischi = ?" +
            "   ,       statorisorse = ?" +
            "   ,       statoscope = ?" +
            "   ,       statocomunicazione = ?" +
            "   ,       statoqualita = ?" +
            "   ,       statoapprovvigionamenti = ?" +
            "   ,       statostakeholder = ?" +
            "   ,       dataultimamodifica = ?" +
            "   ,       oraultimamodifica = ?" +
            "   ,       autoreultimamodifica = ?" +
            "   WHERE id = ?";

        
    /* ************************************************************************ *
     *                               Query di POL                               *
     * ************************************************************************ *
     *                        2.  Query di inserimento                          *
     * ************************************************************************ */
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
            "   ,   id_ruolo" +
            "   ,   id_persona )" +
            "   VALUES (? " +          //id
            "   ,       ? " +          //nome
            "   ,       ? " +          //descrizione
            "   ,       ? " +          //data inizio attività
            "   ,       ? " +          //data fine attività
            "   ,       ? " +          //data inizio attesa
            "   ,       ? " +          //data fine attesa
            "   ,       ? " +          //data inizio effettiva
            "   ,       ? " +          //data fine effettiva
            "   ,       ? " +          //giorni/uomo previsti
            "   ,       ? " +          //giorni/uomo effettivi
            "   ,       ? " +          //giorni/uomo rimanenti
            "   ,       ? " +          //note di avanzamento
            "   ,       ? " +          //milestone
            "   ,       ? " +          //id progetto
            "   ,       ? " +          //id wbs
            "   ,       ? " +          //id complessità
            "   ,       ? " +          //id stato attività
            "   ,       ? " +          //id ruolo
            "   ,       ? )" ;         //id persona
    
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
            "   VALUES (? " +          //id
            "   ,       ? " +          //descrizione
            "   ,       ? " +          //informativa
            "   ,       ? " +          //presenza
            "   ,       ? " +          //id progetto
            "   ,       ? )" ;         //id persona
    
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
            "   ,   id_progetto" +
            "   VALUES (? " +          //id
            "   ,       ? " +          //descrizione
            "   ,       ? " +          //probabilità
            "   ,       ? " +          //impatto
            "   ,       ? " +          //livello
            "   ,       ? " +          //stato
            "   ,       ? )" ;         //id progetto
    
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