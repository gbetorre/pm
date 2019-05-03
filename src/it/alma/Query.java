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
     * <p>Costante parlante per i test che controllano 
     * che interi abbiano un valore maggiore di zero.</p>
     * <p>Maggiormente visibile e chiara del valore che incapsula (0) 
     * per questo motivo può essere utilizzata in inizializzazioni di 
     * variabili e in test che controllano che specifici parametri
     * abbiano un valore significativo.</p> 
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
     * <p>Costante per il parametro identificante la pagina dell'utente.</p>
     */
    public static final String PART_USR                         = "usr";
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
     * <p>Costante per il parametro identificante la pagina dei rischi associati ad un progetto.</p>
     */
    public static final String PART_RISK                        = "ris";
    /**
     * <p>Costante per il parametro identificante la pagina del Report di un progetto.</p>
     */
    public static final String PART_REPORT                      = "rep";
    /**
     * <p>Costante per il parametro identificante la pagina del grafico di WBS di un progetto.</p>
     */
    public static final String PART_GRAPHIC                      = "gra";
    /**
     * <p>Costante per il parametro identificante la pagina di inserimento di uno status di un progetto.</p>
     */
    public static final String ADD_STATUS_TO_PROJECT            = "addSts";
    /**
     * <p>Costante per il parametro identificante la pagina di inserimento di una entit&agrave; nel progetto.</p>
     */
    public static final String ADD_TO_PROJECT                   = "add";
    /**
     * <p>Costante per il parametro identificante la pagina di modifica di una entit&agrave;.</p>
     */
    public static final String MODIFY_PART                      = "mod";
    /**
     * <p>Costante per il parametro identificante la parte di eliminazione di una entit&agrave;.</p>
     */
    public static final String DELETE_PART                      = "del";
    /**
     * <p>Costante per il parametro identificante la parte di sospensione di una attivit&agrave;.</p>
     */
    public static final String SUSPEND_PART                     = "sus";
    /**
     * <p>Costante per il parametro identificante la parte di ripresa di una attivit&agrave; precedentemente sospesa.</p>
     */
    public static final String RESUME_PART                      = "res";
    /**
     * <p>Costante per il parametro identificante la sezione dove vengono mostrate occorrenze marcate come eliminate.</p>
     */
    public static final String TRASH_PART                       = "bin";    
    /**
     * <p>Costante per il parametro identificante il token dei monitoraggio.</p>
     */
    public static final String MONITOR_PART                     = "mon";
    /**
     * <p>Costante per il parametro identificante la pagina dei monitoraggio.</p>
     */
    public static final String PROJECT_PART                     = "pol";
    /**
     * <p>Costante per il parametro identificante la pagina dei credits dell'applicazione.</p>
     */
    public static final String CREDITS                          = "credits";
    /* ************************************************************************ *
     *           Costanti utilizzate per i metodi di estrazione wbs             *
     * ************************************************************************ */
    /**
     * <p>Costante identificante la query che estrae tutte le WBS compresi i Workpackage</p>
     */
    public static final int WBS_GET_ALL = 1;
    /**
     * <p>Costante identificante la query che estrae tutte le WBS esclusi i Workpackage</p>
     */
    public static final int WBS_BUT_WP = 2;
    /**
     * <p>Costante identificante la query che estrae tutte le WBS che sono Workpackage</p>
     */
    public static final int WBS_WP_ONLY = 3;
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
     *              Costanti parlanti per identificativi di stato               *
     * ************************************************************************ */
    /**
     * Un'attivit&agrave; APERTA &egrave; un'attivit&agrave; avviata 
     * ma non ancora iniziata, cio&egrave; non vi &egrave; stato imputato lavoro
     */
    public static final int APERTA = 1;
    /**
     * Un'attività IN PROGRESS &egrave; un'attivit&agrave; avviata 
     * su cui &egrave; stato imputato lavoro (giorni uomo o data inizio effettiva)
     */
    public static final int IN_PROGRESS = 2;
    /**
     * Un'attivit&agrave; CHIUSA è un'attivit&agrave; conclusa, 
     * su cui non &egrave; più possibile allocare GU o risorse
     */
    public static final int CHIUSA = 3;
    /**
     * Identificativo di stato attivit&agrave; non ancora lavorata ma 
     * gi&agrave; in ritardo (quindi in ritardo rispetto al previsto,
     * non all'effettivo)
     */
    public static final int APERTA_IN_RITARDO = 4;
    /**
     * Identificativo di stato attivit&agrave; gi&agrave; in lavorazione
     * (data inizio effettiva <> NULL) ma con data inizio effettiva
     * precedente rispetto a quanto previsto (data inizio prevista)
     */
    public static final int IN_PROGRESS_IN_ANTICIPO = 5;
    /**
     * Identificativo di stato attivit&agrave; gi&agrave; in lavorazione
     * (data inizio effettiva <> NULL) ma che in base al raffronto con 
     * la data fine effettiva avrebbe dovuto essere chiusa 
     */
    public static final int IN_PROGRESS_IN_RITARDO = 6;
    /**
     * Identificativo di stato attivit&agrave; gi&agrave; in lavorazione
     * (data inizio effettiva <> NULL) ma con data inizio effettiva
     * precedente rispetto a quanto previsto
     */
    public static final int IN_PROGRESS_INIZIO_IN_RITARDO = 7;
    /**
     * Identificativo di stato attivit&agrave; gi&agrave; chiusa
     * (data fine effettiva <> NULL) ma con data fine effettiva
     * precedente rispetto a quanto previsto (data fine prevista)
     */
    public static final int CHIUSA_IN_ANTICIPO = 8;
    /**
     * Identificativo di stato attivit&agrave; gi&agrave; chiusa
     * (data inizio effettiva <> NULL) ma con data fine effettiva
     * successiva rispetto a quanto previsto (data fine prevista)
     */
    public static final int CHIUSA_IN_RITARDO = 9;
    /**
     * Identificativo di stato attivit&agrave; inconsistente, che 
     * pu&ograve; dipendere da varie incongruenze
     */
    public static final int STATO_INCONSISTENTE = 10;
    /**
     * Identificativo di stato attivit&agrave; messa esplicitamente in
     * stato 'sospeso' dall'utente che ha diritti di scrittura sul progetto.
     * A differenza delle attivit&agrave; eliminate, un' attivit&agrave;
     * in stato di sospensione viene visualizzata negli elenchi di  
     * attivit&agrave; ma non nei report.
     */
    public static final int SOSPESA = 11;
    /**
     * Identificativo di stato attivit&agrave; messa esplicitamente in
     * stato 'eliminato' dall'utente che ha diritti di scrittura sul progetto.
     * Mentre le attivit&agrave; sospese si vedono negli elenchi ma non
     * nei report, le  attivit&agrave; cancellate non escono in nessuna
     * selezione &ndash; salvo che in quella esplicita del &quot;Cestino&quot;.
     */
    public static final int ELIMINATA = 12;
    /**
     * Identificativo di stato attivit&agrave; chiusa nei periodi
     * di avanzamento progetto antecedenti all'ultimo terminato
     */
    public static final int CHIUSA_DA_TEMPO = 21;
    /**
     * Identificativo di stato attivit&agrave; chiusa nel periodo 
     * di avanzamento progetto immediatamente antecedente al corrente
     */
    public static final int CHIUSA_DA_POCO = 22;
    /**
     * Identificativo di stato attivit&agrave; in ritardo ma in 
     * progress
     */
    public static final int IN_RITARDO = 23;
    /**
     * Identificativo di stato attivit&agrave; in corso di sviluppo
     */
    public static final int IN_CORSO = 24;
    /**
     * Identificativo di stato attivit&agrave; che saranno svolte
     * nel periodo immediatamente successivo
     */
    public static final int PERIODO_FUTURO_PROSSIMO = 25;
    /**
     * Identificativo di stato attivit&agrave; che saranno svolte nei
     * periodi successivi al prossimo
     */
    public static final int PERIODO_FUTURO_VENTURO = 26;
    /* ************************************************************************ *
     *              Costanti per incapsulare informative di stato               *
     * ************************************************************************ */
    /**
     * <p>Un'attivit&agrave; APERTA &egrave; un'attivit&agrave; avviata 
     * ma non ancora iniziata, cio&egrave; non vi &egrave; stato imputato lavoro.</p>
     */
    public static final String APERTA_HELP = "Le Attivit&agrave; <cite>APERTE</cite> hanno <cite>SOLO DATE PREVISTE</cite>.<br />";
    /**
     * <p>Un'attivit&agrave; IN PROGRESS &egrave; un'attivit&agrave; 
     * gi&agrave; iniziata,ma non ancora terminata.</p>
     */
    public static final String IN_PROGRESS_HELP = "Le Attivit&agrave; <cite>IN PROGRESS</cite> hanno <cite>DATA INIZIO EFFETTIVA</cite> non vuota.<br />";
    /**
     * <p>Un'attivit&agrave; IN PROGRESS &egrave; un'attivit&agrave; 
     * gi&agrave; terminata.</p>
     */
    public static final String CHIUSA_HELP = "Le Attivit&agrave; <cite>CHIUSE</cite> hanno <cite>DATA FINE EFFETTIVA</cite> non vuota.<br />";
    /**
     * <p>Un'attivit&agrave; APERTA &egrave; un'attivit&agrave; avviata 
     * ma non ancora iniziata, cio&egrave; non vi &egrave; stato imputato lavoro.</p>
     * <p>Inoltre, un'attivit&agrave; regolare &egrave; un'attivit&agrave; 
     * che vive nel presente, cio&egrave; la data di fine, sia pure prevista, 
     * &egrave; nel futuro e la data di inizio prevista &egrave; nel passato.</p>
     */
    public static final String APERTA_REGOLARE_HELP = APERTA_HELP + "Quelle <cite>APERTE REGOLARMENTE</cite> hanno: <dl><dt>DATA FINE PREVISTA</dt><dd> futura o presente</dd></dl>";
    /**
     * <p>Un'attività IN PROGRESS regolare &egrave; un'attivit&agrave; 
     * avviata ed iniziata, ovvero su cui &egrave; stato imputato lavoro 
     * (data di inizio effettiva diversa da NULL), quindi:
     * <ul>
     * <li>Data di inizio effettiva <> NULL</li>
     * <li>Data di fine effettiva = NULL</li>
     * <li>Data di fine prevista >= today</li>
     * </ul></p>
     */
    public static final String IN_PROGRESS_REGOLARE_HELP = IN_PROGRESS_HELP + "Quelle<cite>IN PROGRESS REGOLARE</cite> hanno: <dl><dt>&ndash; DATA FINE PREVISTA</dt><dd> futura o presente</dd></dl>";
    /**
     * Un'attivit&agrave; CHIUSA è un'attivit&agrave; conclusa, 
     * su cui non &egrave; più possibile allocare GU o risorse
     */
    public static final String CHIUSA_REGOLARE_HELP = CHIUSA_HELP + "Su quelle <cite>CHIUSE REGOLARMENTE</cite> non &egrave; pi&ugrave; possibile allocare risorse.";
    /**
     * Un'attivit&agrave; APERTA REGOLARE PIANIFICATA &egrave; 
     * un'attivit&agrave; che deve ancora iniziare effettivamente,
     * ma attualmente &egrave; pianificata nel futuro
     */
    public static final String APERTA_REGOLARE_PIANIFICATA_HELP = APERTA_HELP + "Quelle <cite>PIANIFICATE REGOLARMENTE</cite> hanno: <dl><dt>&ndash; DATA INIZIO PREVISTA</dt><dd>futura o presente";
    /**
     * Identificativo di stato attivit&agrave; non ancora lavorata ma 
     * gi&agrave; in ritardo (quindi in ritardo rispetto al previsto,
     * non all'effettivo)
     */
    public static final String APERTA_IN_RITARDO_APERTURA_HELP = APERTA_HELP + "Quelle <cite>IN RITARDO RISPETTO L\\'APERTURA</cite> hanno:<dl><dt>&ndash; DATA INIZIO PREVISTA</dt> <dd> nel passato</dd></dl>";
    /**
     * Identificativo di stato attivit&agrave; non ancora lavorata ma 
     * gi&agrave; in ritardo (quindi in ritardo rispetto al previsto,
     * non all'effettivo)
     */
    public static final String APERTA_IN_RITARDO_CHIUSURA_HELP = APERTA_HELP + "Quelle <cite>IN RITARDO RISPETTO ALLA CHIUSURA</cite> hanno:<dl><dt>&ndash; DATA FINE PREVISTA</dt> <dd> nel passato</dd></dl>";
    /**
     * Identificativo di stato attivit&agrave; gi&agrave; in lavorazione
     * (data inizio effettiva <> NULL) ma con data inizio effettiva
     * precedente rispetto a quanto previsto (data inizio prevista)
     */
    public static final String IN_PROGRESS_IN_ANTICIPO_HELP = IN_PROGRESS_HELP + "Quelle <cite>IN PROGRESS IN ANTICIPO SULL\\'INIZIO</cite> hanno:<dl><dt>&ndash; DATA INIZIO PREVISTA</dt> <dd> successiva alla DATA INIZIO EFFETTIVA</dd></dl>";
    /**
     * Identificativo di stato attivit&agrave; gi&agrave; in lavorazione
     * (data inizio effettiva <> NULL) ma che in base al raffronto con 
     * la data fine effettiva avrebbe dovuto essere chiusa 
     */
    public static final String IN_PROGRESS_IN_RITARDO_HELP = IN_PROGRESS_HELP + "Quelle <cite>IN PROGRESS IN RITARDO</cite> sono in ritardo su tutta la linea (sia sull\\'inizio che sulla fine)";
    /**
     * Identificativo di stato attivit&agrave; ancora in progress
     * (data inizio effettiva <> NULL) ma con data fine effettiva 
     * ancora null e data fine prevista precedente a today
     */
    public static final String IN_PROGRESS_CHIUSURA_IN_RITARDO_HELP = IN_PROGRESS_HELP + "Quelle <cite>IN PROGRESS CON CHIUSURA IN RITARDO</cite> hanno:<dl><dt>&ndash; DATA FINE PREVISTA</dt> <dd> precedente alla data odierna</dd><dt>&ndash; DATA FINE EFFETTIVA</dt><dd> vuota</dd></dl>";
    /**
     * Identificativo di stato attivit&agrave; gi&agrave; in lavorazione
     * (data inizio effettiva <> NULL) ma con data inizio effettiva
     * successiva rispetto a quanto previsto
     */
    public static final String IN_PROGRESS_INIZIO_IN_RITARDO_HELP = IN_PROGRESS_HELP + "Quelle <cite>IN PROGRESS CON INIZIO IN RITARDO</cite> hanno:<dl><dt>&ndash; DATA INIZIO PREVISTA</dt> <dd> precedente alla data di inizio effettiva</dd></dl>";
    /**
     * Identificativo di stato attivit&agrave; gi&agrave; chiusa
     * (data fine effettiva <> NULL) ma con data fine effettiva
     * precedente rispetto a quanto previsto (data fine prevista)
     */
    public static final String CHIUSA_IN_ANTICIPO_HELP = CHIUSA_HELP + "Quelle <cite>CHIUSA IN ANTICIPO</cite> hanno:<dl><dt>&ndash; DATA FINE EFFETTIVA</dt> <dd> precedente alla data di fine prevista</dd></dl>";
    /**
     * Identificativo di stato attivit&agrave; gi&agrave; chiusa
     * (data inizio effettiva <> NULL) ma con data fine effettiva
     * successiva rispetto a quanto previsto (data fine prevista)
     */
    public static final String CHIUSA_IN_RITARDO_HELP = CHIUSA_HELP + "Quelle <cite>CHIUSA IN RITARDO</cite> hanno:<dl><dt>&ndash; DATA FINE EFFETTIVA</dt> <dd> successiva alla data di fine prevista</dd></dl>"; 
    /**
     * Identificativo di stato attivit&agrave; inconsistente, che 
     * pu&ograve; dipendere da varie incongruenze
     */
    public static final String STATO_INCONSISTENTE_HELP = "Le Attivit&agrave; <cite>IN STATO INCONSISTENTE</cite> sono quelle che possono avere varie incongruenze";
    /**
     * Identificativo di stato attivit&agrave; inconsistente, che 
     * pu&ograve; dipendere da varie incongruenze
     */
    public static final String SOSPESA_HELP = "Le Attivit&agrave; <cite>SOSPESE</cite> sono quelle sulle quali si &egrave; deciso di <cite>NON</cite> proseguire temporaneamente la lavorazione.<br />Le Attivit&agrave; <cite>SOSPESE</cite> non vengono visualizzate nei report.";
    /**
     * Identificativo di stato attivit&agrave; inconsistente, che 
     * pu&ograve; dipendere da varie incongruenze
     */
    public static final String ELIMINATA_HELP = "Le Attivit&agrave; <cite>ELIMINATE</cite> sono quelle che non vengono pi&uacute; sviluppate.";
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
     * <p>Estrae i progetti dell'utente 
     * avente identificativo passato come parametro
     * appartenenti al dipartimento avente identificativo passato come parametro.</p>
     */
    public static final String GET_PROJECTS_BY_DEPART = 
            "SELECT " +
            "       PJ.id           " + 
            "   ,   PJ.id_dipart                AS \"idDipart\"" + 
            "   ,   PJ.titolo                   AS \"titolo\"" + 
            "   ,   PJ.descrizione              AS \"descrizione\"" + 
            "   ,   PJ.datainizio               AS \"dataInizio\"" + 
            "   ,   PJ.datafine                 AS \"dataFine\"" + 
            "   ,   PJ.id_statoprogetto         AS \"idStatoProgetto\"" + 
            //"   ,   PJ.situazioneattuale        AS \"situazioneAttuale\"" + 
            //"   ,   PJ.situazionefinale         AS \"situazioneFinale\"" + 
            //"   ,   PJ.obiettivimisurabili      AS \"obiettiviMisurabili\"" + 
            //"   ,   PJ.minacce                  AS \"minacce\"" + 
            //"   ,   PJ.stakeholdermarginali     AS \"stakeholderMarginali\"" + 
            //"   ,   PJ.stakeholderoperativi     AS \"stakeholderOperativi\"" + 
            //"   ,   PJ.stakeholderistituzionali AS \"stakeholderIstituzionali\"" + 
            //"   ,   PJ.stakeholderchiave        AS \"stakeholderChiave\"" + 
            //"   ,   PJ.deliverable              AS \"deliverable\"" + 
            //"   ,   PJ.fornitorichiaveinterni   AS \"fornitoriChiaveInterni\"" + 
            //"   ,   PJ.fornitorichiaveesterni   AS \"fornitoriChiaveEsterni\"" + 
            //"   ,   PJ.serviziateneo            AS \"serviziAteneo\"" + 
            //"   ,   PJ.vincoli                  AS \"vincoli\"" + 
            "   ,   PJ.sottotipo                AS \"tag\"" +
            "   FROM progetto PJ" + 
            "       INNER JOIN ruologestione RG ON PJ.id = RG.id_progetto" + 
            "       INNER JOIN persona P ON RG.id_persona = P.id" + 
            "       INNER JOIN identita I ON P.id = I.id1_persona" + 
            "       INNER JOIN usr U ON I.id0_usr = U.id" + 
            "   WHERE   P.id = ?" +
            "       AND PJ.id_dipart = ?" + 
            "   ORDER BY PJ.titolo ASC";
    
    
    /**
     * <p>Estrae i progetti, con il loro dipartimento, partendo dal ruolo
     * che la persona possiede su quel progetto.</p>
     */
    public static final String GET_PROJECTS_BY_ROLE = 
            "SELECT " + 
            "       P.id" +
            "   ,   P.titolo        AS \"informativa\"" +
            "   ,   D.nome          AS \"nome\"" +
            "   ,   R.nome          AS \"extraInfo\"" +
            "   FROM progetto P" +
            "       INNER JOIN dipartimento D on D.id = P.id_dipart" +
            "       INNER JOIN ruologestione RG on RG.id_progetto = P.id" +
            "       INNER JOIN ruolo R on R.id = RG.id_ruolo" +
            "   WHERE RG.id_persona = ?" +
            "   ORDER BY (D.nome, P.titolo) ASC";
    
    
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
     * <p>Estrae i dipartimenti dell'utente avente <code>username</code> 
     * passato come parametro e nel contesto dei quali lo stesso risulta essere 
     * un utente che pu&ograve; modificare i dati dei relativi progetti
     * con elevato livello di privilegi.</p>
     */
    public static final String GET_WRITABLE_DEPARTMENTS_BY_USER_NAME = 
            "SELECT DISTINCT" +
            "       PJ.id_dipart                AS \"id\"" + 
            "   ,   D.nome                      AS \"nome\"" + 
            "   FROM progetto PJ" + 
            "       INNER JOIN dipartimento D ON PJ.id_dipart = D.id" +
            "       INNER JOIN ruologestione RG ON PJ.id = RG.id_progetto" + 
            "       INNER JOIN ruolo R ON RG.id_ruolo = R.id" +
            "       INNER JOIN persona P ON RG.id_persona = P.id" + 
            "       INNER JOIN identita I ON P.id = I.id1_persona" + 
            "       INNER JOIN usr U ON I.id0_usr = U.id" + 
            "   WHERE   U.login = ?" + 
            "       AND R.nome IN ('PM', 'PMOATE', 'PMODIP')";
    
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
            "       W.id                    AS \"id\"" + 
            "   ,   W.nome                  AS \"nome\"" + 
            "   ,   W.descrizione           AS \"descrizione\"" + 
            "   ,   W.workpackage           AS \"workPackage\"" + 
            "   ,   W.noteavanzamento       AS \"noteAvanzamento\"" +
            "   ,   W.risultatiraggiunti    AS \"risultatiRaggiunti\"" +
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
            "       W.id                    AS \"id\"" +
            "   ,   W.nome                  AS \"nome\"" +
            "   ,   W.descrizione           AS \"descrizione\"" +
            "   ,   W.workpackage           AS \"workPackage\"" + 
            "   ,   W.noteavanzamento       AS \"noteAvanzamento\"" +
            "   ,   W.risultatiraggiunti    AS \"risultatiRaggiunti\"" +
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
            "       W.id                    AS \"id\"" +
            "   ,   W.nome                  AS \"nome\"" +
            "   ,   W.descrizione           AS \"descrizione\"" +
            "   ,   W.workpackage           AS \"workPackage\"" + 
            "   ,   W.noteavanzamento       AS \"noteAvanzamento\"" +
            "   ,   W.risultatiraggiunti    AS \"risultatiRaggiunti\"" +
            "   FROM wbs W" + 
            "   WHERE W.id_progetto = ?" +
            "       AND W.id_wbs = ?" + 
            "   ORDER BY W.dataultimamodifica DESC"; 
    
    /**
     * <p>Estrae le wbs che non sono workpackage di un progetto,
     * identificato tramite l'id, passato come parametro</p>
     */
    public static final String GET_WBS_NOT_WORKPACKAGE =
            "SELECT " +
            "       W.id                    AS \"id\"" + 
            "   ,   W.nome                  AS \"nome\"" + 
            "   ,   W.descrizione           AS \"descrizione\"" + 
            "   ,   W.noteavanzamento       AS \"noteAvanzamento\"" +
            "   ,   W.risultatiraggiunti    AS \"risultatiRaggiunti\"" +
            "   FROM wbs W" +
            "   WHERE W.id_progetto = ?" +
            "     AND W.workpackage = false" + 
            "   ORDER BY W.dataultimamodifica ASC";
    
    /**
     * <p>Estrae le wbs relative ad un progetto, identificato tramite id, passato come parametro</p>
     */
    public static final String GET_WBS_BY_PROJECT =
            "SELECT " +
            "       W.id                    AS \"id\"" + 
            "   ,   W.nome                  AS \"nome\"" + 
            "   ,   W.descrizione           AS \"descrizione\"" + 
            "   ,   W.workpackage           AS \"workPackage\"" + 
            "   ,   W.noteavanzamento       AS \"noteAvanzamento\"" +
            "   ,   W.risultatiraggiunti    AS \"risultatiRaggiunti\"" +
            "   FROM wbs W" +
            "   WHERE W.id_progetto = ?" + 
            "   ORDER BY W.dataultimamodifica ASC";
    
    /**
     * <p>Estrae le wbs di primo livello (= che non hanno padri) 
     * relative ad un progetto, identificato tramite id, passato come parametro</p>
     */
    public static final String GET_TOP_WBS_BY_PROJECT =
            "SELECT " +
            "       W.id                    AS \"id\"" + 
            "   ,   W.nome                  AS \"nome\"" + 
            "   ,   W.descrizione           AS \"descrizione\"" + 
            "   ,   W.workpackage           AS \"workPackage\"" + 
            "   ,   W.noteavanzamento       AS \"noteAvanzamento\"" +
            "   ,   W.risultatiraggiunti    AS \"risultatiRaggiunti\"" +
            "   FROM wbs W" +
            "   WHERE W.id_progetto = ?" + 
            "       AND W.id_wbs IS NULL" +
            "   ORDER BY W.dataultimamodifica ASC";
    
    /**
     * <p>Estrae i workpackage relative ad un progetto, identificato tramite id, passato come parametro</p>
     */
    public static final String GET_WP_BY_PROJECT =
            "SELECT " +
            "       W.id                    AS \"id\"" + 
            "   ,   W.nome                  AS \"nome\"" + 
            "   ,   W.descrizione           AS \"descrizione\"" + 
            "   ,   W.workpackage           AS \"workPackage\"" + 
            "   ,   W.noteavanzamento       AS \"noteAvanzamento\"" +
            "   ,   W.risultatiraggiunti    AS \"risultatiRaggiunti\"" +
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
            "   WHERE   D.id = ? OR -1 = ?";
    
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
     * <p>Estrae gli identificativi di tutte le attivit&agrave; 
     * relative ad un progetto, individuato 
     * tramite l'id, passato come parametro.</p>
     */
    public static final String  GET_ACTIVITIES = 
            "SELECT " +
            "       A.id                    AS  \"id\"" +
            "   ,   A.id_stato              AS  \"idStato\"" +
            "   FROM attivita A" + 
            "   WHERE A.id_progetto = ?";
    
    /**
     * <p>Estrae le attivit&agrave; relative ad un progetto, identificato 
     * tramite l'id, passato come parametro che non sono state cancellate
     * logicamente.</p>
     * <p>Il nome della query fa riferimento al fatto che l'estrazione &egrave;
     * parametrizzata per:
     * <ul>
     * <li>DATE - data a partire dalla quale vengono estratte 
     * le attivit&agrave;; per estrarle tutte basta passare una data 
     * &quot;antidiluviana&quot;</li>
     * <li>TYPE - questo riferimento indica il fatto che, in funzione del
     * valore dei parametri passato, la query pu&ograve; estrarre le sole
     * attivit&agrave; di tipo milestone oppure tutte le attivit&agrave;
     * indipendentemente dal loro &quot;tipo&quot;</li>
     * </ul></p>  
     */
    public static final String  GET_ACTIVITIES_BY_DATE_AND_TYPE = 
            "SELECT " +
            "       A.id                    AS  \"id\"" +
            "   ,   A.nome                  AS  \"nome\"" +
            "   ,   A.descrizione           AS  \"descrizione\"" +
            "   ,   A.datainizio            AS  \"dataInizio\"" +
            "   ,   A.datafine              AS  \"dataFine\"" +
            "   ,   A.datainizioeffettiva   AS  \"dataInizioEffettiva\"" +
            "   ,   A.datafineeffettiva     AS  \"dataFineEffettiva\"" +
            "   ,   A.noteavanzamento       AS  \"noteAvanzamento\"" +
            "   ,   A.risultatiraggiunti    AS  \"risultatiRaggiunti\"" +
            "   ,   A.milestone             AS  \"milestone\"" +
            "   ,   A.id_wbs                AS  \"idWbs\"" +
            "   ,   A.id_stato              AS  \"idStato\"" +
            "   FROM attivita A" + 
            "   WHERE A.id_progetto = ?" +
            "       AND A.id_stato <> 12" +
            "       AND (A.datainizio >= ? OR A.datainizio IS NULL)" +
            "       AND (A.milestone = ? OR ?)" +
            "   ORDER BY A.nome";
    
    /**
     * <p>Estrae le attivit&agrave; relative ad un progetto, identificato 
     * tramite l'id, passato come parametro, e che si trovano in stato
     * di dato valore, il cui identificativo viene passato come parametro.</p>
     */
    public static final String  GET_ACTIVITIES_BY_STATE = 
            "SELECT " +
            "       A.id                    AS  \"id\"" +
            "   ,   A.nome                  AS  \"nome\"" +
            "   ,   A.descrizione           AS  \"descrizione\"" +
            "   ,   A.datainizio            AS  \"dataInizio\"" +
            "   ,   A.datafine              AS  \"dataFine\"" +
            "   ,   A.datainizioeffettiva   AS  \"dataInizioEffettiva\"" +
            "   ,   A.datafineeffettiva     AS  \"dataFineEffettiva\"" +
            "   ,   A.noteavanzamento       AS  \"noteAvanzamento\"" +
            "   ,   A.risultatiraggiunti    AS  \"risultatiRaggiunti\"" +
            "   ,   A.milestone             AS  \"milestone\"" +
            "   ,   A.id_wbs                AS  \"idWbs\"" +
            "   ,   A.id_stato              AS  \"idStato\"" +
            "   FROM attivita A" + 
            "   WHERE A.id_progetto = ?" +
            "       AND A.id_stato = ?" +
            "       AND (A.datainizio >= ? OR A.datainizio IS NULL)" +
            "   ORDER BY A.nome";
    
    /**
     * <p>Estrae le attivit&agrave; di una specifica WBS,
     * identificata tramite id, passato come parametro, relativa ad 
     * un progetto, identificato tramite id, passato come parametro.</p>
     */
    public static final String GET_ACTIVITIES_BY_WBS =
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
            "   ,   A.risultatiraggiunti    AS  \"risultatiRaggiunti\"" +
            "   ,   A.milestone             AS  \"milestone\"" + 
            "   ,   A.id_wbs                AS  \"idWbs\"" + 
            "   ,   A.id_stato              AS  \"idStato\"" + 
            "   ,   A.id_complessita        AS  \"idComplessita\"" + 
            "   FROM attivita A" + 
            "   WHERE A.id_progetto = ?" + 
            "     AND A.id_wbs = ?" +
            "     AND A.id_stato <> 12" +
            "   ORDER BY A.dataultimamodifica ASC";
    
    /**
     * <p>Estrae il numero di tuple presenti nella tabella attivit&agrave;
     * con l'id della wbs selezionata dall'utente, identificata tramite id.</p>
     */
    public static final String GET_ACTIVITIES_COUNT_BY_WBS =
            "SELECT count(*)" +
            "   FROM attivita" +
            "   WHERE id_progetto = ?" +
            "     AND id_wbs = ?";
    
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
            "   ,   A.risultatiraggiunti    AS  \"risultatiRaggiunti\"" +
            "   ,   A.milestone             AS  \"milestone\"" +
            "   ,   A.id_wbs                AS  \"idWbs\"" +
            "   ,   A.id_stato              AS  \"idStato\"" +
            "   ,   A.id_complessita        AS  \"idComplessita\"" +
            "   FROM attivita A" + 
            "   WHERE A.id_progetto = ?" +
            "     AND A.id = ?";
    
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
     * <p>Seleziona i valori di uno stato attivit&agrave; a partire dal
     * proprio identificativo passato come parametro.</p>
     */
    public static final String GET_STATO_ATTIVITA = 
            "SELECT " +
            "       SA.id        AS \"id\"" +
            "   ,   SA.nome      AS \"nome\"" +
            "   ,   SA.valore    AS \"informativa\"" +
            "   FROM statoattivita SA" +
            "   WHERE SA.id = ?";
    
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
            "   ,   A.risultatiraggiunti    AS  \"risultatiRaggiunti\"" +
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
            "           )" +
            "       AND A.id_stato <> 12";
    
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
            "   ,   A.risultatiraggiunti    AS  \"risultatiRaggiunti\"" +
            "   ,   A.milestone             AS  \"milestone\"" +
            "   ,   A.id_wbs                AS  \"idWbs\"" +
            "   ,   A.id_stato              AS  \"idStato\"" +
            "   ,   A.id_complessita        AS  \"idComplessita\"" +
            "   FROM attivita A " + 
            "      INNER JOIN progetto P ON P.id = A.id_progetto " +
            "   WHERE P.id = ? " +
            "      AND (A.datainizio BETWEEN ? AND ?)" +
            "       AND A.id_stato <> 12";
    
    /**
     * <p>Estrae tutti i monitoraggi di dato anno e dato dipartimento
     * il cui identificativo viene passato come parametro.</p>
     */
    public static final String GET_MONITOR_BY_DEPART = 
            "SELECT DISTINCT " +
            "       M.id                    AS  \"id\"" +
            "   ,   M.anno                  AS  \"anno\"" +        
            "   ,   M.d4_reclutamento       AS  \"quadroD4\"" +
            "   ,   M.d5_infrastrutture     AS  \"quadroD5\"" +
            "   ,   M.d6_premialita         AS  \"quadroD6\"" +
            "   ,   M.d7_didattica          AS  \"quadroD7\"" +
            "   ,   M.d8_modalitafasi       AS  \"quadroD8\"" +
            "   FROM monitoraggio M " + 
            "   WHERE M.anno = ? " +
            "      AND M.id_dipart = ?";    
    
    /**
     * <p>Estrae identificativo tupla ultimo accesso, se esiste 
     * per l'utente il cui username viene passato come parametro.</p>
     */
    public static final String GET_ACCESSLOG_BY_LOGIN = 
            "SELECT " +
            "       A.id                    AS  \"id\"" +
            "   FROM accesslog A " + 
            "   WHERE A.login = ? ";
    
    /**
     * <p>Estrae tuple ultimi accessi di tutti gli utenti 
     * da mostrare ad utenti con particolari privilegi amministrativi
     * (PMOATE = 1).</p>
     */
    public static final String GET_ACCESSLOG = 
            "SELECT " +
            "       A.id                        AS  \"id\"" +
            "   ,   P.cognome || ' ' || P.nome  AS  \"autoreUltimaModifica\"" +
            "   ,   A.dataultimoaccesso         AS  \"dataUltimaModifica\"" +
            "   ,   A.oraultimoaccesso          AS  \"oraUltimaModifica\"" +
            "   ,   A.ipv4                      AS  \"descrizioneAvanzamento\"" +
            "   FROM accesslog A " + 
            "       INNER JOIN usr ON A.login = usr.login" +
            "       INNER JOIN identita I ON I.id0_usr = usr.id" +
            "       INNER JOIN persona P ON I.id1_persona = P.id" +
            "   WHERE EXISTS " +
            "       (SELECT RG.id_ruolo "       + 
            "           FROM ruologestione RG "  +
            "           WHERE RG.id_persona = ? " +
            "               AND RG.id_ruolo = 1)"  +
            "   ORDER BY A.dataultimoaccesso DESC";
    
    
    /* ************************************************************************ *
     *                               QUERY DI POL                               *
     * ************************************************************************ *
     *                       2.  Query di aggiornamento                         *
     * ************************************************************************ */
    /**
     * <p>Query per modifica della password dell'utente loggato</p>
     * <p>I parametri da passare sono: 
     * <ul>
     * <li>passwd - password inserita dall'utente</li>
     * <li>passwdform - password criptata</li>
     * <li>id - id dell'utente sul quale cambiare la password</li>
     * </ul>
     * </p>
     */
    public static final String UPDATE_PWD =
            "UPDATE usr" +
            "   SET     passwd = ?" +
            "   ,       passwdform = ?" +
            "   WHERE id = ?";
    
    
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
            "   ,       risultatiraggiunti = ?" + 
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
     * <p>Aggiorna lo stato di una attivit&agrave;, 
     * identificata tramite id, passato come parametro.</p> 
     * <p>Utile per effettuare aggiornamenti <em>a posteriori</em> rispetto
     * allo stato calcolato all'atto dell'inserimento, tipicamente per 
     * impostare l'attivit&agrave; come sospesa o eliminata, perch&eacute;
     * questi non sono stati calcolabili ma dipendono dall'input dell'utente,
     * dato in una fase successiva a quella dell'inserimento.</p> 
     */
    public String UPDATE_ACTIVITY_STATE = 
            "UPDATE attivita" +
            "   SET id_stato = ?" +
            "   ,   dataultimamodifica = ?" +
            "   ,   oraultimamodifica = ?" +
            "   ,   autoreultimamodifica = ?" +
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
            "   ,       noteavanzamento = ?" +
            "   ,       risultatiraggiunti = ?" +
            "   ,       dataultimamodifica = ?" +
            "   ,       oraultimamodifica = ?" +
            "   ,       autoreultimamodifica = ?" +
            "   WHERE id = ?";

    /**
     * <p>Aggiorna un monitoraggio dipartimentale di un dato anno.</p>
     */
    public static final String UPDATE_MONITOR_BY_DEPART = 
            "UPDATE monitoraggio " +
            "   SET     d4_reclutamento = ?" +
            "   ,       d5_infrastrutture = ?" +
            "   ,       d6_premialita = ?" +
            "   ,       d7_didattica = ?" +
            "   ,       d8_modalitafasi = ?" +
            "   ,       dataultimamodifica = ?" +
            "   ,       oraultimamodifica = ?" +
            "   ,       autoreultimamodifica = ?" +
            "   WHERE  anno = ? " +
            "      AND id_dipart = ?";   
    
    /**
     * <p>Query per aggiornamento di ultimo accesso al sistema.</p>
     */
    public static final String UPDATE_ACCESSLOG_BY_USER = 
            "UPDATE accesslog" +
            "   SET login  = ?" +
            "   ,   ipv4   = ?" +
            "   ,   client = ?" +
            "   ,   server = ?" +
            "   ,   dataultimoaccesso = ?" + 
            "   ,   oraultimoaccesso = ?" +
            "   WHERE id = ? ";
    
        
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
            "   ,   risultatiraggiunti" +
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
            "   ,       ? " +          // risultati raggiunti
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
            "   ,       ? " +          // probabilità
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
            "   ,   autoreultimamodifica" +
            "   ,   noteavanzamento" +
            "   ,   risultatiraggiunti)" +
            "   VALUES (? " +       // id
            "   ,       ? " +       // id_wbs
            "   ,       ? " +       // id_progetto
            "   ,       ? " +       // nome
            "   ,       ? " +       // descrizione
            "   ,       ? " +       // workpackage
            "   ,       ? " +       // dataultimamodifica
            "   ,       ? " +       // oraultimamodifica
            "   ,       ? " +       // autoreultimamodifica
            "   ,       ? " +       // noteavanzamento
            "   ,       ?)";        // risultati raggiunti
    
    /**
     * <p>Query per inserimento di ultimo accesso al sistema.</p>
     */
    public static final String INSERT_ACCESSLOG_BY_USER = 
            "INSERT INTO accesslog" +
            "   (   id" +
            "   ,   login" +
            "   ,   ipv4" +
            "   ,   client" +
            "   ,   server" +
            "   ,   dataultimoaccesso" + 
            "   ,   oraultimoaccesso )" +
            "   VALUES (? " +          // id
            "   ,       ? " +          // login
            "   ,       ? " +          // IPv4
            "   ,       ? " +          // client
            "   ,       ? " +          // server
            "   ,       ? " +          // dataultimoaccesso
            "   ,       ?)" ;          // oraultimoaccesso
    
    
    /* ************************************************************************ *
     *                               QUERY DI POL                               *
     * ************************************************************************ *
     *                       4.  Query di eliminazione                          *
     * ************************************************************************ */
    /**
     * <p>Esegue una eliminazione logica di una attivit&agrave;, 
     * identificata tramite id, passato come parametro, 
     * attraverso la dereferenziazione della stessa dal progetto e dalla WBS 
     * a cui appartiene, e collegandola invece ad una WBS e ad un progetto
     * "fantasma" aventi rispettivamente identificativo uguale 
     * in valore assoluto a quelli del dipartimento a cui sono collegati,
     * ma segno opposto (numero opposto o inverso additivo).</p>
     */
    public String DELETE_ACTIVITY = 
            "UPDATE attivita" +
            "   SET id_progetto = ?" +
            "   ,   id_wbs   = ?" +
            "   ,   id_stato = 3" +
            "   ,   dataultimamodifica = ?" +
            "   ,   oraultimamodifica = ?" +
            "   ,   autoreultimamodifica = ?" +
            "   WHERE id = ?";
    
    /**
     * <p>Esegue una eliminazione logica di una WBS, identificata tramite id, 
     * passato come parametro, attraverso la de-referenziazione della stessa 
     * dal progetto a cui appartiene, collegandola invece ad un progetto
     * "fantasma" avente come identificativo l'inverso additivo 
     * dell'identificativo del dipartimento a cui era collegata.</p>
     */
    public String DELETE_WBS = 
            "UPDATE wbs" +
            "   SET id_progetto = ?" +
            "   WHERE id = ?";
    
    /**
     * <p>Estrae il primo progetto con id negativo, 
     * dato in input l'id del dipartimento.</p>
     */
    public String GET_PROJECT_BY_DIPART = 
            "SELECT " +
            "   P.id    AS \"id\"" +
            "   FROM progetto P" + 
            "   WHERE P.id_dipart = ?" +
            "       AND P.id < 0";
    
    
}