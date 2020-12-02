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
     * per questo motivo pu&ograve; essere utilizzata in inizializzazioni di 
     * variabili e in test che controllano che specifici parametri
     * abbiano un valore significativo.</p> 
     */
    public static final byte NOTHING = 0;
    /**
     * <p>Costante parlante per valore da passare sul secondo argomento
     * in query strutturate in modo da considerare il primo se il secondo
     * vale NOTHING oppure il secondo se il primo vale NOTHING e il secondo
     * vale GET_ALL_BY_CLAUSE.</p>
     */
    public static final int GET_ALL_BY_CLAUSE = -1;
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
     * <p>Costante per il parametro identificante la gestione dei ruoli dell'utente.</p>
     */
    public static final String PART_PERMISSION                  = "per";
    /**
     * <p>Costante per il parametro identificante la gestione delle competenze.</p>
     */
    public static final String PART_RESOURCES                   = "ski";
    /*
     * <p>Costante per il parametro identificante la gestione delle appartenenze.</p>
     */
    //public static final String PART_BELONGS                     = "blg";
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
     * <p>Costante per il parametro identificante la navigazione degli indicatori.</p>
     */
    public static final String PART_INDICATOR                   = "ind";
    /**
     * <p>Costante per il parametro identificante la pagina del Report di un progetto.</p>
     */
    public static final String PART_REPORT                      = "rep";
    /**
     * <p>Costante per il parametro identificante la pagina del Report di un progetto.</p>
     */
    public static final String PART_TIMELINES                   = "tml";
    /**
     * <p>Costante per il parametro identificante la pagina del grafico di WBS di un progetto.</p>
     */
    public static final String PART_GRAPHIC                     = "gra";
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
     * <p>Costante per il parametro identificante la pagina di aggiornamento di una entit&agrave;.</p>
     */
    public static final String UPDATE_PART                      = "upd";
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
     * <p>Costante per il parametro identificante la parte inserimento ulteriori informazioni.</p>
     */
    public static final String EXTRAINFO_PART                   = "ext";
    /**
     * <p>Costante per il parametro identificante la sezione dove vengono mostrate occorrenze marcate come eliminate.</p>
     */
    public static final String TRASH_PART                       = "bin";    
    /**
     * <p>Costante per il parametro identificante il token dei monitoraggio.</p>
     */
    public static final String MONITOR_PART                     = "mon";
    /**
     * <p>Costante per il parametro identificante la sezione identificante il monitoraggio ateneo.</p>
     */
    public static final String MONITOR_ATE                      = "ate";
    /**
     * <p>Costante per il parametro identificante la pagina dei monitoraggio.</p>
     */
    public static final String PROJECT_PART                     = "pol";
    /**
     * <p>Costante per il parametro identificante la pagina deil'ordinamento.</p>
     */
    public static final String ORDER_BY_PART                    = "srt";
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
    public static final int WBS_GET_ALL     = 1;
    /**
     * <p>Costante identificante la query che estrae tutte le WBS esclusi i Workpackage</p>
     */
    public static final int WBS_BUT_WP      = 2;
    /**
     * <p>Costante identificante la query che estrae tutte le WBS che sono Workpackage</p>
     */
    public static final int WBS_WP_ONLY     = 3;
    /**
     * <p>Costante identificante la query che estrae la WBS padre della WBS data</p>
     */
    public static final int WBS_PARENT_ONLY = 4;
    /**
     * <p>Costante identificante la query che estrae le WBS figlie della WBS data</p>
     */
    public static final int WBS_CHILDREN_ONLY           = 5;
    /**
     * <p>Costante identificante la query che estrae le attivit&agrave; correnti nel periodo corrente di avanzamento progetto.</p>
     */
    public static final int ACT_GET_CURRENT_STATUS      = 6;
    /**
     * <p>Costante identificante la query che estrae le attivit&agrave; correnti nel periodo prossimo di avanzamento progetto.</p>
     */
    public static final int ACT_GET_NEXT_STATUS         = 7;
    /**
     * <p>Costante identificante la query che estrae le attivit&agrave; future rispetto al periodo corrente di avanzamento progetto.</p>
     */
    public static final int ACT_GET_FUTURE_ACTIVITIES   = 8;
    /* ************************************************************************ *
     *              Costanti parlanti per identificativi di stato               *
     * ************************************************************************ */
    /**
     * Un'attivit&agrave; APERTA &egrave; un'attivit&agrave; avviata 
     * ma non ancora iniziata, cio&egrave; non vi &egrave; stato imputato lavoro
     */
    public static final int APERTA = 1;
    /**
     * Un'attivit&agrave; IN PROGRESS &egrave; un'attivit&agrave; avviata 
     * su cui &egrave; stato imputato lavoro (giorni uomo o data inizio effettiva)
     */
    public static final int IN_PROGRESS = 2;
    /**
     * Un'attivit&agrave; CHIUSA &egrave; un'attivit&agrave; conclusa, 
     * su cui non &egrave; pi&ugrave; possibile allocare GU o risorse
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
     * <p>Un'attivit&agrave; IN PROGRESS regolare &egrave; un'attivit&agrave; 
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
     * Un'attivit&agrave; CHIUSA &egrave; un'attivit&agrave; conclusa, 
     * su cui non &egrave; pi&ugrave; possibile allocare GU o risorse
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
    public static final String APERTA_IN_RITARDO_APERTURA_HELP = APERTA_HELP + "Quelle <cite>IN RITARDO RISPETTO ALL\\'APERTURA</cite> hanno:<dl><dt>&ndash; DATA INIZIO PREVISTA</dt> <dd> nel passato</dd></dl>";
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
    public static final String ELIMINATA_HELP = "Le Attivit&agrave; <cite>ELIMINATE</cite> sono quelle che non vengono pi&uacute; sviluppate.<br /> In caso di necessit&agrave; pu&ograve; esserne chiesto il recupero scrivendo al PMO di Ateneo.";
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
     * Estrae un tipo indicatore, dato il suo id, passato come parametro 
     * &ndash; <strong>oppure</strong> tutti i tipi indicatori, 
     * nel caso in cui la seconda clausola risulti <code>true</code>.
     */
   public static final String GET_INDICATOR_TYPES = 
           "SELECT " +
           "       T.id        AS \"id\"" +
           "   ,   T.nome      AS \"nome\"" +
           "   ,   T.valore    AS \"informativa\"" +
           "   FROM tipoindicatore T" + 
           "   WHERE  (T.id = ? OR -1 = ?)" +
           "   ORDER BY T.id";
    
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
            "   FROM usr U" +
            "       INNER JOIN identita I ON U.id = I.id0_usr" + 
            "       INNER JOIN persona P ON P.id = I.id1_persona" + 
            "   WHERE   login = ?" +
            "       AND (( passwd IS NULL OR passwd = ? ) " +
            "           AND ( passwdform IS NULL OR passwdform = ? ))";
    
    /**
     * <p>Estrae la lista di utenti appartenenti ad un certo dipartimento oppure la
     * lista di tutti gli utenti del sistema.</p>
     */
    public static final String GET_BELONGS_USR = 
            "SELECT DISTINCT" +
            "   U.id            AS \"id\"" +
            "   ,   P.nome      AS \"nome\"" +
            "   ,   P.cognome   AS \"cognome\"" +
            "   FROM usr U "  + 
            "       INNER JOIN identita I ON U.id = I.id0_usr" + 
            "       INNER JOIN persona P ON P.id = I.id1_persona" +
            "       INNER JOIN belongs B ON B.id0_usr = U.id" +
            "       INNER JOIN grp G ON G.id = B.id1_grp" +
            "   WHERE G.name ILIKE ? OR -1 = ?" +
            "   ORDER BY P.cognome, P.nome";
    
    /**
     * <p>Estrae la lista delle persone appartenenti ad un certo dipartimento 
     * oppure la lista di tutti gli utenti del sistema.</p>
     */
    public static final String GET_BELONGS_PERSON = 
            "SELECT DISTINCT" +
            "   P.id            AS \"id\"" +
            "   ,   P.nome      AS \"nome\"" +
            "   ,   P.cognome   AS \"cognome\"" +
            "   FROM usr U "  + 
            "       INNER JOIN identita I ON U.id = I.id0_usr" + 
            "       INNER JOIN persona P ON P.id = I.id1_persona" +
            "       INNER JOIN belongs B ON B.id0_usr = U.id" +
            "       INNER JOIN grp G ON G.id = B.id1_grp" +
            "   WHERE G.name ILIKE ? OR -1 = ?" +
            "   ORDER BY P.cognome, P.nome";
    
    /**
     * <p>Restituisce il valore boolean 'true' (in realt&agrave; mostra 't')
     * se l'id dell'utente passato come parametro gi&agrave; esce dalla 
     * query che va a estrarre tutti gli appartenenti (belongs) al gruppo
     * del dipartimento che aggrega il progetto di dato id.</p>
     * <p>Nota che in postgres boolean &egrave; un tipo polimorfo:
     * <pre>
     * Boolean constants can be represented in SQL queries by the SQL key words 
     * TRUE, FALSE, and NULL.
     * The datatype input function for type boolean accepts 
     * these string representations for the "true" state:<ul><li>true</li><li>yes</li><li>on</li><li>1</li></ul>
     * and these representations for the "false" state:<ul><li>false</li><li>no</li><li>off</li><li>0</li></pre>
     * </p>
     * <p>In realt&agrave; non &egrave; importante che il valore restituito
     * sia effettivamente di tipo boolean, perch&eacute; il valore di ritorno 
     * non &egrave; veramente sfruttato:
     * basta che la query torni qualcosa, qualsiasi cosa, perch&eacute; si 
     * capisca che l'utente era gi&agrave; inserito, quindi si poteva anche 
     * restituire 1 o qualunque altro valore a piacere.</p>
     */
    public static final String IS_BELONGS_PERSON = 
            "SELECT " +
            "   true" +
            "   WHERE ? IN" +
            "      (SELECT B.id0_usr FROM belongs B" +
            "           INNER JOIN dipartimento D ON B.id1_grp = D.id_share_grp " +
            "           INNER JOIN progetto PJ ON PJ.id_dipart = D.id " +
            "       WHERE PJ.id = ?)";
    
    /**
     * <p>Estrae il gruppo di appartenenza dell'utente che ha eseguito la login.</p>
     */
    public static final String GET_GROUP = 
            "SELECT DISTINCT" +
            "       G.id        AS \"id\"" +
            "   ,   G.name      AS \"nome\"" +
            "   FROM grp G " +
            "       INNER JOIN belongs B ON B.id1_grp = G.id" +
            "       INNER JOIN usr U ON U.id = B.id0_usr" +
            "       INNER JOIN identita I ON U.id = I.id0_usr" + 
            "       INNER JOIN persona P ON P.id = I.id1_persona" +
            "       INNER JOIN ruologestione RG ON RG.id_persona = P.id" + 
            "   WHERE P.id = ? " +
            "       AND RG.id_ruolo <= 2";
            
    /**
     * <p>Estrae l'username dell'utente tramite l'id, passato come parametro.</p>
     */
    public static final String GET_USERNAME = 
            "SELECT " +
            "       U.login      AS \"login\"" +
            "   FROM usr U" + 
            "   WHERE U.id = ?";
    
    /**
     * <p>Estrae la password criptata e il seme dell'utente, identificato tramite username, passato come parametro.</p>
     */
    public static final String GET_ENCRYPTEDPASSWORD =
            "SELECT " +
            "       U.passwdform    AS \"nome\"" +
            "   ,   U.salt          AS \"informativa\"" +
            "   FROM usr U" + 
            "   WHERE U.login = ?";
    
    /**
     * <p>Estrae i ruoli di una persona  
     * avente identificativo passato come parametro.</p>
     * <p>Scarta dai ruoli quello di 'NONE' perch&egrave; questo serve 
     * solo come segnaposto per i ruoli cancellati dal PMODIP, ma non 
     * &egrave; per nulla utile all'utente nella gestione delle informazioni
     * (anzi, potrebbe dare errore nelle assunzioni fatte a livello di cicli
     * nelle pagine JSP dal momento che l'id progetto associato al ruolo NONE
     * che ha id = 6, ha id negativo).</p>
     */
    public static final String GET_RUOLIPERSONA = 
            "SELECT " +
            "       RG.id_progetto  AS \"id\"" +
            "   ,   RG.id_ruolo     AS \"ordinale\"" +
            "   ,   R.nome          AS \"nome\"" +
            "   FROM ruologestione RG " +
            "       INNER JOIN ruolo R on RG.id_ruolo = R.id " +
            "   WHERE RG.id_persona = ?" +
            "       AND RG.id_ruolo <> 6";
        
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
            "       AND PJ.id > 0" + 
    		"   ORDER BY PJ.titolo ASC";
    
    /**
     * <p>Estrae i progetti dell'utente 
     * avente identificativo passato come parametro
     * appartenenti al dipartimento avente identificativo 
     * passato come parametro, 
     * e che non si trovano in stato 'ELIMINATO'.</p>
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
            "   ,   PJ.sottotipo                AS \"tag\"" +
            "   ,   PJ.tipo                     AS \"tipo\"" +
            "   ,   RG.id_ruolo                 AS \"descrizioneStatoCorrente\"" +
            "   FROM progetto PJ" + 
            "       INNER JOIN ruologestione RG ON PJ.id = RG.id_progetto" + 
            "       INNER JOIN persona P ON RG.id_persona = P.id" + 
            "       INNER JOIN identita I ON P.id = I.id1_persona" + 
            "       INNER JOIN usr U ON I.id0_usr = U.id" + 
            "   WHERE   P.id = ?" + 
            "       AND PJ.id_dipart = ?" +
            "       AND PJ.id > 0" + 
            "       AND PJ.id_statoprogetto < 5" +
            "   ORDER BY PJ.titolo ASC";
    
    /**
     * <p>Estrae i progetti dell'utente 
     * avente identificativo passato come parametro
     * appartenenti al dipartimento avente identificativo 
     * passato come parametro, 
     * e che non si trovano in stato 'ELIMINATO'.
     * Oltre a questi criteri, ne sono stati aggiunti a fini di 
     * storicizzazione e per avere una vista dei soli obiettivi strategici
     * di un desiderato piano integrato delle performance:
     * <ul>
     * <li>la data di fine deve essere maggiore o uguale al 31/12 dell'anno passato
     * come parametro</li>
     * <li>e deve essere minore o uguale al 31/12 dell'anno passato + 2 anni</li>
     * <li>a meno che il mese riferimento sia inferiore, allora vale quello ai
     * fini della scadenza</li>
     * <li>e a meno che lo stato progetto sia IN PROGRESS (2); in tal caso
     * il progetto non scade mai.</li></ul>
     * </p>
     */
    public static final String GET_PROJECTS_BY_DEPART_AND_YEAR = 
            "SELECT " +
            "       PJ.id           " + 
            "   ,   PJ.id_dipart                AS \"idDipart\"" + 
            "   ,   PJ.titolo                   AS \"titolo\"" + 
            "   ,   PJ.descrizione              AS \"descrizione\"" + 
            "   ,   PJ.datainizio               AS \"dataInizio\"" + 
            "   ,   PJ.datafine                 AS \"dataFine\"" + 
            "   ,   PJ.id_statoprogetto         AS \"idStatoProgetto\"" + 
            "   ,   PJ.sottotipo                AS \"tag\"" +
            "   ,   PJ.tipo                     AS \"tipo\"" +
            "   ,   RG.id_ruolo                 AS \"descrizioneStatoCorrente\"" +
            "   FROM progetto PJ" + 
            "       INNER JOIN ruologestione RG ON PJ.id = RG.id_progetto" + 
            "       INNER JOIN persona P ON RG.id_persona = P.id" + 
            "       INNER JOIN identita I ON P.id = I.id1_persona" + 
            "       INNER JOIN usr U ON I.id0_usr = U.id" + 
            "   WHERE   P.id = ?" +                 // clausola utente
            "       AND PJ.id_dipart = ?" +         // clausola dipartimento
            "       AND PJ.id > 0" +                // clausola no phantom
            "       AND PJ.id_statoprogetto < 5" +  // clausola no eliminati
            "       AND ( ( " +
            "               (PJ.datafine >= ? AND PJ.datafine <= ?)" +
            "               AND PJ.meseriferimento >= ?" +
            "              )" +
            "             OR PJ.id_statoprogetto = ?" +
            "           )" +
            "   ORDER BY PJ.titolo ASC";
    
    /**
     * <p>Estrae il primo progetto con id negativo, 
     * dato in input l'id del dipartimento.</p>
     */
    public String GET_PHANTOM_PROJECT_BY_DEPART = 
            "SELECT " +
            "   PJ.id    AS \"id\"" +
            "   FROM progetto PJ" + 
            "   WHERE PJ.id_dipart = ?" +
            "       AND PJ.id < 0";
    
    /**
     * <p>Estrae i progetti, con il loro dipartimento, partendo dal ruolo
     * che la persona possiede su quel progetto.</p>
     */
    public static final String GET_PROJECTS_BY_ROLE = 
            "SELECT " + 
            "       PJ.id" +
            "   ,   PJ.titolo       AS \"informativa\"" +
            "   ,   D.id            AS \"livello\"" +
            "   ,   D.nome          AS \"nome\"" +
            "   ,   R.nome          AS \"extraInfo\"" +
            "   FROM progetto PJ" +
            "       INNER JOIN dipartimento D on D.id = PJ.id_dipart" +
            "       INNER JOIN ruologestione RG on RG.id_progetto = PJ.id" +
            "       INNER JOIN ruolo R on R.id = RG.id_ruolo" +
            "   WHERE RG.id_persona = ?" +
            "       AND PJ.id > 0" + 
            "   ORDER BY (D.nome, PJ.titolo) ASC";
    
    /**
     * <p>Estrae i progetti, con le relative competenze, partendo 
     * dall'identificativo della persona.</p>
     */
    public static final String GET_PROJECTS_BY_SKILLS = 
            "SELECT " + 
            "       PJ.id" +
            "   ,   PJ.titolo       AS \"informativa\"" +
            "   ,   C.id            AS \"livello\"" +
            "   ,   C.descrizione   AS \"nome\"" +
            //"   ,   R.nome          AS \"extraInfo\"" +
            "   FROM progetto PJ" +
            "       INNER JOIN competenza C on C.id_progetto = PJ.id" +
            "       INNER JOIN competenzagestione CG on CG.id_competenza = C.id" +
            //"       INNER JOIN persona R on R.id = RG.id_ruolo" +
            "   WHERE CG.id_persona = ?" +
            "       AND PJ.id > 0" + 
            "   ORDER BY (C.descrizione, PJ.titolo) ASC";
    
    /**
     * <p>Restituisce il valore boolean 'true' se e solo se
     * esiste gi&agrave; una riga nella tabella <code>ruologestione</code>
     * avente id_progetto e id_persona passati come parametri.</p>
     */
    public String IS_PROJECT_BY_ROLE = 
            "SELECT " +
            "   true" +
            "   WHERE EXISTS" +
            "       (SELECT 1 FROM ruologestione RG" +
            "           WHERE RG.id_persona = ?" +
            "               AND RG.id_progetto = ?)";
    
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
     * <p>Estrae i progetti dell'utente corrispondente alla persona
     * avente <code>id</code> passato come parametro 
     * e nel contesto dei quali lo stesso risulta essere 
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
            "   WHERE   P.id = ?" + 
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
     * <p>Estrae un progetto di dato id, passato come parametro
     * su cui, a qualche titolo, &egrave; autorizzata una persona,
     * il cui identificativo &egrave; a sua volta passato come parametro.</p>
     */
    public static final String GET_PROJECT = 
            "SELECT " +
            "       PJ.id" + 
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
            "   ,   PJ.tipo                     AS \"tipo\"" +
            "   FROM progetto PJ" + 
            "       INNER JOIN ruologestione RG ON PJ.id = RG.id_progetto" + 
            "       INNER JOIN persona P ON RG.id_persona = P.id" + 
            "   WHERE   PJ.id = ?" +
            "       AND P.id = ?";
    
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
            "   ,   W.dataultimamodifica    AS \"dataUltimaModifica\"" +
            "   ,   W.oraultimamodifica     AS \"oraUltimaModifica\"" +
            "   ,   W.autoreultimamodifica  AS \"autoreUltimaModifica\"" +
            "   FROM wbs W" + 
            "   WHERE W.id = (SELECT " +
            "                        WF.id_wbs" + 
            "                 FROM wbs WF" + 
            "                 WHERE WF.id = ?)" + 
            "   ORDER BY W.dataultimamodifica ASC";
    
    /**
     * <p>Estrae lo stato di una wbs dato il suo identificativo, 
     * passato come parametro.</p>
     */
    public static final String GET_WBS_STATE = 
            "SELECT " +
            "       W.id_stato              AS \"id\"" +
            "   ,   W.nome                  AS \"nome\"" +
            "   ,   WS.note                 AS \"descrizione\"" +
            "   ,   WS.datasospensione      AS \"dataMisurazione\"" + 
            "   ,   WS.dataultimamodifica   AS \"dataUltimaModifica\"" +
            "   ,   WS.oraultimamodifica    AS \"oraUltimaModifica\"" +
            "   ,   WS.autoreultimamodifica AS \"autoreUltimaModifica\"" +
            "   FROM wbs W" + 
            "       INNER JOIN wbsgestione WS ON WS.id_wbs = W.id" +
            "   WHERE WS.id_progetto = ?" +
            "       AND WS.id_wbs = ?";
    
    /**
     * <p>Estrae le wbs figlie data una wbs padre, 
     * identificata tramite l'id, passato come parametro.</p>
     */
    public static final String GET_WBS_FIGLIE = 
            "SELECT " +
            "       W.id                    AS \"id\"" +
            "   ,   W.nome                  AS \"nome\"" +
            "   ,   W.ordinale              AS \"ordinale\"" +
            "   ,   W.descrizione           AS \"descrizione\"" +
            "   ,   W.workpackage           AS \"workPackage\"" + 
            "   ,   W.noteavanzamento       AS \"noteAvanzamento\"" +
            "   ,   W.risultatiraggiunti    AS \"risultatiRaggiunti\"" +
            "   FROM wbs W" + 
            "   WHERE W.id_progetto = ?" +
            "       AND W.id_wbs = ?" + 
            "   ORDER BY W.ordinale, W.nome, W.dataultimamodifica DESC"; 
    
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
            "   ,   W.dataultimamodifica    AS \"dataUltimaModifica\"" +
            "   ,   W.oraultimamodifica     AS \"oraUltimaModifica\"" +
            "   ,   W.autoreultimamodifica  AS \"autoreUltimaModifica\"" +
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
            "   ,   W.dataultimamodifica    AS \"dataUltimaModifica\"" +
            "   ,   W.oraultimamodifica     AS \"oraUltimaModifica\"" +
            "   ,   W.autoreultimamodifica  AS \"autoreUltimaModifica\"" +
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
            "   ,   W.ordinale              AS \"ordinale\"" +
            "   ,   W.descrizione           AS \"descrizione\"" + 
            "   ,   W.workpackage           AS \"workPackage\"" + 
            "   ,   W.noteavanzamento       AS \"noteAvanzamento\"" +
            "   ,   W.risultatiraggiunti    AS \"risultatiRaggiunti\"" +
            "   ,   W.dataultimamodifica    AS \"dataUltimaModifica\"" +
            "   ,   W.oraultimamodifica     AS \"oraUltimaModifica\"" +
            "   ,   W.autoreultimamodifica  AS \"autoreUltimaModifica\"" +
            "   FROM wbs W" +
            "   WHERE W.id_progetto = ?" + 
            "       AND W.id_wbs IS NULL" +
            "   ORDER BY W.ordinale, W.nome, W.dataultimamodifica ASC";
    
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
            "   ORDER BY W.nome, W.dataultimamodifica ASC";
    
    /**
     * <p>Estrae tutte le persone che hanno un (qualsivoglia) ruolo 
     * in un determinato progetto, identificato tramite id, 
     * passato come parametro.</p>
     * <p>In pratica, se la query restituisce almeno una riga vuol dire che sul
     * progetto, il cui identificativo viene passato come parametro,
     * c'&egrave; almeno una persona.</p>
     */
    public static final String GET_PEOPLE_BY_PROJECT = 
            "SELECT " + 
            "       P.id            AS \"id\"" +
            "   ,   P.nome          AS \"nome\"" +
            "   ,   P.cognome       AS \"nomeReale\"" +
            "   ,   R.nome          AS \"nomeClasse\"" +
            "   FROM ruologestione  RG" +
            "       INNER JOIN persona P ON RG.id_persona = P.id" +
            "       INNER JOIN ruolo R ON RG.id_ruolo = R.id" +
            "   WHERE RG.id_progetto = ?" + 
            "   ORDER BY R.id, P.cognome";

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
     * identificato tramite id, passato come parametro,
     * e che abbiano almeno una competenza nel progetto stesso.</p>
     */
    public static final String GET_PEOPLE_BY_DEPARTMENT = 
            "SELECT DISTINCT" + 
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
            "       INNER JOIN competenzagestione CG ON CG.id_persona = P.id" +
            "       INNER JOIN competenza C ON CG.id_competenza = C.id" +
            "   WHERE PJ.id = ?" + 
            "       AND C.id_progetto = PJ.id" +
            "   ORDER BY P.cognome, P.nome";
    
    /**
     * <p>Estrae una persona, identificata 
     * tramite il suo id, passato come parametro.</p>
     */
    public static final String GET_PERSON_BY_ID = 
            "SELECT " +
            "       P.id            AS \"id\"" +
            "   ,   P.nome          AS \"nome\"" +
            "   ,   P.cognome       AS \"cognome\"" +
            "   ,   P.sesso         AS \"sesso\"" +
            "   ,   P.datanascita   AS \"dataNascita\"" +
            "   FROM persona P    " +
            "   WHERE P.id = ?";
    
    /**
     * <p>Estrae una persona, identificata 
     * tramite il suo id utente, passato come parametro.</p>
     * <p>OSSERVAZIONE: avendo una relazione di appoggio tra utente e persona,
     * l'applicazione consente in potenza pi&uacute; utenze per 
     * una sola persona; si accorda con questi gradi di libert&agrave;
     * la mancanza di vincoli sulla relazione <code>identita</code>. 
     * Tuttavia, se &egrave; vero che a una data persona possono corrispondere 
     * potenzialmente pi&acute; utenze, a una sola utenza dovrebbe corrispondere 
     * una e una sola persona; di qui il senso della query, che risale 
     * la relazione dall'utenza alla persona.</p>
     */
    public static final String GET_PERSON_BY_USER_ID = 
            "SELECT " +
            "       P.id            AS \"id\"" +
            "   ,   P.nome          AS \"nome\"" +
            "   ,   P.cognome       AS \"cognome\"" +
            "   ,   P.sesso         AS \"sesso\"" +
            "   ,   P.datanascita   AS \"dataNascita\"" +
            "   FROM usr U " +
            "       INNER JOIN identita I ON U.id = I.id0_usr " +
            "       INNER JOIN persona P ON I.id1_persona = P.id" + 
            "   WHERE U.id = ?";
    
    /**
     * <p>Estrae una persona, identificata 
     * tramite il suo id, passato come parametro.</p>
     */
    public static final String GET_PERSON_BY_PERSON_ID = 
            "SELECT " +
            "       P.id            AS \"id\"" +
            "   ,   P.nome          AS \"nome\"" +
            "   ,   P.cognome       AS \"cognome\"" +
            "   ,   P.sesso         AS \"sesso\"" +
            "   ,   P.datanascita   AS \"dataNascita\"" +
            "   FROM persona P " +
            "   WHERE P.id = ?";
    
    /**
     * <p>Estrae un utente a partire dall'identificativo della persona 
     * ad esso associato, passato come parametro.</p>
     */
    public static final String GET_USER_BY_PERSON_ID = 
            "SELECT " +
            "       U.id            AS \"id\"" +
            "   ,   U.realname      AS \"nome\"" +
            "   ,   (SELECT U1.realname FROM usr U1 WHERE U1.id = P.id) AS \"informativa\"" +
            "   ,   P.id            AS \"ordinale\"" +
            "   FROM usr U " +
            "       INNER JOIN identita I ON U.id = I.id0_usr " +
            "       INNER JOIN persona P ON I.id1_persona = P.id" + 
            "   WHERE P.id = ?";
    
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
     * &ndash; <strong>oppure</strong> tutti i dipartimenti, 
     * nel caso in cui la seconda clausola risulti <code>true</code>.
     */
    public static final String GET_DIPART = 
            "SELECT " +
            "       D.id              AS \"id\"" + 
            "   ,   D.nome            AS \"nome\"" + 
            "   ,   D.prefisso        AS \"prefisso\"" + 
            "   ,   D.acronimo        AS \"acronimo\"" +             
            "   ,   D.indirizzosede   AS \"indirizzoSede\"" +
            "   FROM dipartimento D" + 
            "   WHERE  (D.id = ? OR -1 = ?)" +
            "       AND D.acronimo IS NOT NULL" +
            "   ORDER BY D.acronimo";
    
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
     * <p>Estrae tutti i campi necessari al calcolo dello stato 
     * per tutte le attivit&agrave; relative ad un progetto, individuato 
     * tramite l'id, passato come parametro
     * &ndash; <strong>oppure</strong> tutte le attivi&agrave; 
     * indipendentemente dal progetto di appartenenza, 
     * nel caso in cui la seconda clausola risulti <code>true</code>..</p>
     */
    public static final String  GET_ACTIVITIES_WITH_DATES = 
            "SELECT " +
            "       A.id                    AS  \"id\"" +
            "   ,   A.datainizio            AS  \"dataInizio\"" +
            "   ,   A.datafine              AS  \"dataFine\"" +
            "   ,   A.datainizioeffettiva   AS  \"dataInizioEffettiva\"" +
            "   ,   A.datafineeffettiva     AS  \"dataFineEffettiva\"" +
            "   ,   A.id_stato              AS  \"idStato\"" +
            "   FROM attivita A" + 
            "   WHERE A.id_progetto = ? OR -1 = ?";
    
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
     * valore dei parametri passati, la query pu&ograve; estrarre le sole
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
            "   ORDER BY A.nome, A.dataultimamodifica";
    
    /**
     * <p>Estrae il numero di tuple presenti nella tabella attivit&agrave;
     * con l'id della wbs selezionata dall'utente, identificata tramite id.</p>
     */
    public static final String GET_ACTIVITIES_COUNT_BY_WBS =
            "SELECT count(*)" +
            "   FROM attivita A" +
            "   WHERE id_progetto = ?" +
            "     AND id_wbs = ?" +
            "     AND A.id_stato <> " + ELIMINATA;
    
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
            "   ,   A.id_dipart             AS  \"idStrutturaGregaria\"" +
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
     * <p>Estrae la struttura che &egrave; collegata ad una data attivit&agrave;</p>
     */
    public static final String GET_DEPART_ON_ACTIVITY = 
            "SELECT " +
            "       D.id                AS \"id\"" +
            "   ,   D.nome              AS \"nome\"" +
            "   ,   D.prefisso          AS \"prefisso\"" +
            "   ,   D.acronimo          AS \"acronimo\"" +
            "   FROM attivita A" +
            "       INNER JOIN dipartimento D ON A.id_dipart = D.id" + 
            "   WHERE A.id = ?";
    
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
            "   WHERE C.id_progetto = ?" + 
            "     AND CG.id_persona = ?";
    
    /**
     * <p>Estrae i rischi relativi ad un progetto, identificato 
     * tramite l'id, passato come parametro.</p>
     */
    public static final String GET_RISKS = 
            "SELECT " +
            "       R.id            AS \"id\"" +
            "   ,   R.descrizione   AS \"nome\"" +
            "   ,   R.tipologia     AS \"informativa\"" + 
            "   ,   R.urgenza       AS \"urgenza\"" + 
            "   ,   R.impatto       AS \"impatto\"" + 
            "   ,   R.livello       AS \"livello\"" +
            "   ,   R.stato         AS \"stato\"" +
            "   FROM rischio R" +
            "   WHERE id_progetto = ?" +
            "   ORDER BY R.descrizione ASC";
    
    /**
     * <p>Estrae gli identificativi di tutti gli indicatori 
     * relativi ad un progetto, individuato 
     * tramite l'id, passato come parametro.</p>
     */
    public static final String  GET_INDICATORS = 
            "SELECT " +
            "       I.id                    AS  \"id\"" +
            "   FROM indicatore I" + 
            "   WHERE I.id_progetto = ?";
    
    /**
     * <p>Estrae gli indicatori relativi ad un progetto, identificato 
     * tramite l'id, passato come parametro.</p>
     * <p>Il nome della query fa riferimento al fatto che l'estrazione &egrave;
     * parametrizzata per:
     * <ul>
     * <li>DATE - data a partire dalla quale vengono estratti 
     * gli indicatori; per estrarli tutti basta passare una data 
     * &quot;antidiluviana&quot;</li>
     * <li>TYPE - questo riferimento indica il fatto che, in funzione del
     * valore dei parametri passati, la query pu&ograve; estrarre i soli
     * indicatori di un dato tipo, il cui identificativo viene passato
     * come parametro, oppure tutti gli indicatori
     * indipendentemente dal loro &quot;tipo&quot;</li>
     * </ul></p>  
     */
    public static final String  GET_INDICATORS_BY_DATE_AND_TYPE = 
            "SELECT " +
            "       I.id                    AS  \"id\"" +
            "   ,   I.nome                  AS  \"nome\"" +
            "   ,   I.descrizione           AS  \"descrizione\"" +
            "   ,   I.baseline              AS  \"baseline\"" +
            "   ,   I.databaseline          AS  \"dataBaseline\"" +
            "   ,   I.target                AS  \"target\"" +
            "   ,   I.dataTarget            AS  \"dataTarget\"" +
            "   ,   I.id_tipoindicatore     AS  \"idTipo\"" +
            "   ,   I.id_stato              AS  \"idStato\"" +
            "   ,   I.dataultimamodifica    AS \"dataUltimaModifica\"" +
            "   ,   I.oraultimamodifica     AS \"oraUltimaModifica\"" +
            "   ,   I.autoreultimamodifica  AS \"autoreUltimaModifica\"" +
            "   , (SELECT count(*) " +
            "      FROM indicatoregestione IG" +
            "      WHERE IG.id_indicatore = I.id) AS  \"totMisurazioni\"" +
            "   FROM indicatore I" +
            "   WHERE I.id_progetto = ?" +
            "       AND (I.databaseline >= ? OR I.databaseline IS NULL)" +
            "       AND (I.id_tipoindicatore = ? OR -1 = ?)" +
            "   ORDER BY I.nome";
    
    /**
     * <p>Estrae gli indicatori relativi ad una wbs, identificata 
     * tramite l'id, passato come parametro, oppure gli indicatori di
     * tutte le wbs del progetto identificato tramite id, passato come
     * parametro.</p>
     * <p>In particolare: 
     * <ul>
     * <li>per ottenere solo gli indicatori della wbs
     * specificata, passare l'id della wbs sul primo e un valore qualunque
     * (anche lo stesso id) sul secondo parametro</li>
     * <li>per ottenere gli indicatori di tutte le wbs, passare un valore
     * qualunque sul primo parametro e -1 sul secondo parametro.</li>
     * </ul></p>  
     */
    public static final String  GET_INDICATORS_BY_WBS = 
            "SELECT " +
            "       I.id                    AS  \"id\"" +
            "   ,   I.nome                  AS  \"nome\"" +
            "   ,   I.descrizione           AS  \"descrizione\"" +
            "   ,   I.baseline              AS  \"baseline\"" +
            "   ,   I.databaseline          AS  \"dataBaseline\"" +
            "   ,   I.target                AS  \"target\"" +
            "   ,   I.dataTarget            AS  \"dataTarget\"" +
            "   ,   I.id_tipoindicatore     AS  \"idTipo\"" +
            "   ,   I.id_stato              AS  \"idStato\"" +
            "   ,   I.dataultimamodifica    AS \"dataUltimaModifica\"" +
            "   ,   I.oraultimamodifica     AS \"oraUltimaModifica\"" +
            "   ,   I.autoreultimamodifica  AS \"autoreUltimaModifica\"" +
            "   , (SELECT count(*) " +
            "      FROM indicatoregestione IG" +
            "      WHERE IG.id_indicatore = I.id) AS  \"totMisurazioni\"" +
            "   FROM indicatore I" +
            "   WHERE I.id_progetto = ?" +
            "       AND (I.id_wbs = ? OR -1 = ?)" +
            "   ORDER BY I.nome";
    
    /**
     * <p>Estrae l'indicatore specificato tramite id 
     * passato come parametro, 
     * relativo ad un progetto, identificato tramite id, 
     * passato come parametro.</p>
     */
    public static final String GET_INDICATOR = 
            "SELECT " +
            "       I.id                    AS  \"id\"" +
            "   ,   I.nome                  AS  \"nome\"" +
            "   ,   I.descrizione           AS  \"descrizione\"" +
            "   ,   I.baseline              AS  \"baseline\"" +
            "   ,   I.databaseline          AS  \"dataBaseline\"" +
            "   ,   I.annobaseline          AS  \"annoBaseline\"" +
            "   ,   I.target                AS  \"target\"" +
            "   ,   I.datatarget            AS  \"dataTarget\"" +
            "   ,   I.annotarget            AS  \"annoTarget\"" +
            "   ,   I.id_tipoindicatore     AS  \"idTipo\"" +
            "   ,   I.id_stato              AS  \"idStato\"" +
            "   ,   I.dataultimamodifica    AS \"dataUltimaModifica\"" +
            "   ,   I.oraultimamodifica     AS \"oraUltimaModifica\"" +
            "   ,   I.autoreultimamodifica  AS \"autoreUltimaModifica\"" +
            "   , (SELECT count(*) " +
            "      FROM indicatoregestione IG" +
            "      WHERE IG.id_indicatore = I.id) AS  \"totMisurazioni\"" +
            "   FROM indicatore I" + 
            "   WHERE I.id_progetto = ?" +
            "     AND I.id = ?";
    
    /**
     * <p>Estrae l'azione collegata ad un dato indicatore</p>
     */
    public static final String GET_WBS_ON_INDICATOR = 
            "SELECT " +
            "       W.id                AS \"id\"" +
            "   ,   W.nome              AS \"nome\"" +
            "   ,   W.ordinale          AS \"ordinale\"" +
            "   FROM wbs W" +
            "       INNER JOIN indicatore I ON I.id_wbs = W.id" +
            "   WHERE I.id = ?";
    
    /**
     * <p>Estrae il tipo di un dato indicatore</p>
     */
    public static final String GET_TYPE_ON_INDICATOR = 
            "SELECT " +
            "       T.id                AS \"id\"" +
            "   ,   T.nome              AS \"nome\"" +
            "   ,   T.valore            AS \"informativa\"" +
            "   FROM tipoindicatore T" +
            "   WHERE T.id = ?";
    
    /**
     * <p>Estrae le misurazioni collegata ad un dato indicatore</p>
     */
    public static final String GET_MEASURES_ON_INDICATOR = 
            "SELECT " +
            "       M.id                    AS \"id\"" +
            "   ,   M.valore                AS \"descrizione\"" +
            "   ,   M.note                  AS \"informativa\"" +
            "   ,   M.ultimo                AS \"ultimo\"" +
            "   ,   M.data                  AS \"dataMisurazione\"" +
            "   ,   M.dataultimamodifica    AS \"dataUltimaModifica\"" +
            "   ,   M.oraultimamodifica     AS \"oraUltimaModifica\"" +
            "   ,   M.autoreultimamodifica  AS \"autoreUltimaModifica\"" +
            "   FROM indicatoregestione M" +
            "       INNER JOIN indicatore I ON M.id_indicatore = I.id" +
            "   WHERE I.id = ?" +
            "       AND M.id_progetto = ? OR -1 = ?";
    
    /**
     * <p>Estrae le misurazioni collegata ad un dato 
     * progetto/obiettivo strategico</p>
     */
    public static final String GET_MEASURES = 
            "SELECT " +
            "       M.id                    AS \"id\"" +
            "   ,   M.valore                AS \"descrizione\"" +
            "   ,   M.note                  AS \"informativa\"" +
            "   ,   M.ultimo                AS \"ultimo\"" +
            "   ,   M.data                  AS \"dataMisurazione\"" +
            "   ,   M.dataultimamodifica    AS \"dataUltimaModifica\"" +
            "   ,   M.oraultimamodifica     AS \"oraUltimaModifica\"" +
            "   ,   M.autoreultimamodifica  AS \"autoreUltimaModifica\"" +
            "   ,   M.id_indicatore         AS \"ordinale\"" +
            "   FROM indicatoregestione M" +
            "       INNER JOIN indicatore I ON M.id_indicatore = I.id" +
            "   WHERE (I.id = ? OR -1 = ?)" +
            "       AND (M.id_progetto = ? OR -1 = ?)" +
            "       AND (M.data >= ?)" +
            "   ORDER BY M.data DESC";
    
    /**
     * <p>Estrae la misurazione di id passato come parametro collegata ad un dato 
     * progetto/obiettivo strategico il cui id viene passato come parametro
     * (pleonastico).</p>
     */
    public static final String GET_MEASURE = 
            "SELECT " +
            "       M.id                    AS \"id\"" +
            "   ,   M.valore                AS \"descrizione\"" +
            "   ,   M.note                  AS \"informativa\"" +
            "   ,   M.ultimo                AS \"ultimo\"" +
            "   ,   M.data                  AS \"dataMisurazione\"" +
            "   ,   M.dataultimamodifica    AS \"dataUltimaModifica\"" +
            "   ,   M.oraultimamodifica     AS \"oraUltimaModifica\"" +
            "   ,   M.autoreultimamodifica  AS \"autoreUltimaModifica\"" +
            "   ,   M.id_indicatore         AS \"ordinale\"" +
            "   FROM indicatoregestione M" +
            "       INNER JOIN indicatore I ON M.id_indicatore = I.id" +
            "   WHERE   M.id = ?" +
            "       AND M.id_progetto = ?" +
            "   ORDER BY M.data DESC";
    
    /**
     * <p>Estrae tutti i target revisionati di un dato indicatore</p>
     */
    public static final String GET_UPDATES_ON_INDICATOR = 
            "SELECT " +
            "       U.id                    AS \"id\"" +
            "   ,   U.target                AS \"nome\"" +
            "   ,   U.motivazione           AS \"descrizione\"" +
            "   ,   U.dataaggiornamento     AS \"dataMisurazione\"" +
            "   ,   U.autoreultimamodifica  AS \"autoreUltimaModifica\"" +
            "   FROM indicatoreaggiornamento U" +
            "       INNER JOIN indicatore I ON U.id_indicatore = I.id" +
            "   WHERE U.id_indicatore = ?" +
            "   ORDER BY U.dataaggiornamento DESC";
    
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
            "       AND A.id_stato <> 12" +
            "   ORDER BY A.nome";
    
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
            "   ,   M.aperto                AS  \"open\"" +
            "   ,   M.d4_reclutamento       AS  \"quadroD4\"" +
            "   ,   M.d5_infrastrutture     AS  \"quadroD5\"" +
            "   ,   M.d6_premialita         AS  \"quadroD6\"" +
            "   ,   M.d7_didattica          AS  \"quadroD7\"" +
            "   ,   M.d8_modalitafasi       AS  \"quadroD8\"" +
            "   FROM monitoraggio M " + 
            "   WHERE M.anno = ? " +
            "      AND M.id_dipart = ?";
    
    /**
     * <p>Estrae tutti i monitoraggi di dato anno e dato dipartimento
     * il cui identificativo viene passato come parametro.</p>
     */
    public static final String GET_MONITOR_ATENEO = 
            "SELECT DISTINCT " +
            "       MA.id                   AS  \"id\"" +
            "   ,   MA.anno                 AS  \"anno\"" +  
            "   ,   MA.descrizione          AS  \"descrizione\"" +
            "   ,   MA.informativa          AS  \"informativa\"" +
            "   ,   MA.aperto               AS  \"open\"" +
            "   FROM monitoraggioate MA " + 
            "   WHERE MA.anno = ?";
    
    /**
     * <p>Seleziona un valore convenzionale dalla tabella degli utenti</p>
     */
    public static final String GET_DB_NAME = 
            "SELECT " +
            "       U.email      AS \"db\"" +
            "   FROM usr U" + 
            "   WHERE U.id = 500";
    
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
            "   ORDER BY A.dataultimoaccesso DESC, A.oraultimoaccesso DESC";
    
    
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
            "   SET     passwdform = ?" +
            "   ,       salt = ?" +
            "   ,       passwd = ?" +
            "   ,       autoreultimamodifica = ?" +
            "   ,       dataultimamodifica = ?" +
            "   ,       oraultimamodifica = ?" +
            "   WHERE id = ?";
    
    /**
     * <p>Query per modifica della password dell'utente loggato</p>
     * <p>I parametri da passare sono: 
     * <ul>
     * <li>passwd - password inserita dall'utente</li>
     * <li>passwdform - password criptata</li>
     * <li>id - id della persona da cui ricavare l'utente sul quale cambiare la password</li>
     * </ul>
     * </p>
     */
    public static final String UPDATE_PWD_BY_PERSON =
            "UPDATE usr" +
            "   SET     passwdform = ?" +
            "   ,       salt = ?" +
            "   ,       passwd = ?" +
            "   ,       autoreultimamodifica = ?" +
            "   ,       dataultimamodifica = ?" +
            "   ,       oraultimamodifica = ?" +
            "   WHERE id = " +
            "               (SELECT U.id " +
            "                   FROM usr U " +
            "                       INNER JOIN identita I ON U.id = I.id0_usr " +
            "                       WHERE I.id1_persona = ?)";
    
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
            "   ,       id_wbs = ?" +
            "   ,       id_dipart = ?" +
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
            "   ,       dataultimamodifica = ?" +
            "   ,       oraultimamodifica = ?" +
            "   ,       autoreultimamodifica = ?" +
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
            "   ,       dataultimamodifica = ?" +
            "   ,       oraultimamodifica = ?" +
            "   ,       autoreultimamodifica = ?" +
            "   WHERE id = ?";
    
    /**
     * <p>Modifica il campo della deliverable di un progetto, identificato 
     * tramite l'id, passato come parametro.</p>
     */
    public static final String UPDATE_DELIVERABLE = 
            "UPDATE progetto" + 
            "   SET     deliverable = ?" + 
            "   ,       dataultimamodifica = ?" +
            "   ,       oraultimamodifica = ?" +
            "   ,       autoreultimamodifica = ?" +
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
            "   ,       dataultimamodifica = ?" +
            "   ,       oraultimamodifica = ?" +
            "   ,       autoreultimamodifica = ?" +
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
            "   SET     vincoli = ?" +
            "   ,       dataultimamodifica = ?" +
            "   ,       oraultimamodifica = ?" +
            "   ,       autoreultimamodifica = ?" +
            "   WHERE id = ?";
    
    /**
     * <p>Modifica la tupla della tabella indicatore identificata dall'id, 
     * che &egrave; passato come parametro.</p>
     * <p>I ? servono per prelevare i dati modificati dalle form, 
     * e settarli quindi nel db 
     * (tranne nella clausola WHERE dove il question mark sta ad indicare 
     * l'id dell'indicatore da modificare).</p>
     * <p>Ho stabilito che nella tabella <code>indicatore</code> 
     * i seguenti attributi non possono essere modificati:
     * <ul>
     * <li>id</li>
     * <li>id_progetto</li>
     * </ul>
     * I seguenti campi potrebbero dover essere modificati anche in altra 
     * relazione:<ul>
     * <li>id_wbs</li>
     * </ul></p>
     */
    public static final String UPDATE_INDICATOR = 
            "UPDATE indicatore" +
            "   SET     nome = ?" +
            "   ,       descrizione = ?" + 
            "   ,       baseline = ?" + 
            "   ,       databaseline = ?" + 
            "   ,       annobaseline = ?" + 
            "   ,       target = ?" + 
            "   ,       datatarget = ?" + 
            "   ,       annotarget = ?" + 
            "   ,       id_tipoindicatore = ?" +
            "   ,       id_wbs = ?" +
            "   ,       dataultimamodifica = ?" +
            "   ,       oraultimamodifica = ?" +
            "   ,       autoreultimamodifica = ?" +
            "   WHERE id = ?";
    
    /**
     * <p>Query per aggiornamento di dati misurazione a seguito
     * aggiornamento dei dati dell'indicatore misurato.</p>
     */
    public static final String UPDATE_INDICATOR_MEASUREMENT = 
            "UPDATE indicatoregestione" +
            "   SET     id_wbs  = ?" +
            "   WHERE id_indicatore = ?";
    
    /**
     * <p>Aggiorna lo stato di un indicatore dato.</p>
     */
    public static final String UPDATE_INDICATOR_BY_TOGGLE = 
            "UPDATE indicatore " +
            "   SET    id_stato = (SELECT id FROM statoprogetto WHERE nome = ?)" +
            "   WHERE  id = ? " +
            "      AND id_progetto = ?";

    /**
     * <p>Modifica la tupla della tabella indicatoreaggiornamento 
     * identificata dall'id dell'indicatore, passato come parametro,
     * e dall'id del progetto, passato come parametro.</p>
     * <p>Siccome le modifiche del target possono essere molteplici, anche
     * se vale solo l'ultima, nell'aggiornamento bisogna sincerarsi di 
     * aggiornare solo l'ultima modifica esistente nel set di risultati
     * individuati dalla coppia: id_indicatore, id_progetto. Infatti,
     * nell'aggiornamento non viene identificato un id indicatoreaggiornamento,
     * e quindi bisogna inferire la tupla su cui intervenire tramite
     * queste altre informazioni, che incrociate tra loro rendono comunque
     * l'identificazione altrettanto univoca.</p> 
     * <p>I ? servono per prelevare i dati modificati dalle form, 
     * e settarli quindi nel db 
     * (tranne nella clausola WHERE dove il question mark sta ad indicare 
     * le coordinate necessarie a identificare l'aggiornamento da modificare).</p>
     */
    public static final String UPDATE_INDICATOR_EXTRAINFO = 
        "UPDATE     indicatoreaggiornamento" +
        "   SET     motivazione = ?" +
        "   ,       target = ?" + 
        "   ,       datatarget = ?" + 
        "   ,       annotarget = ?" + 
        "   ,       dataaggiornamento = ?" + 
        "   ,       dataultimamodifica = ?" +
        "   ,       oraultimamodifica = ?" +
        "   ,       autoreultimamodifica = ?" +
        "   WHERE   id_indicatore = ?" +
        "       AND id_progetto = ?" +
        "       AND dataultimamodifica = (SELECT max(dataultimamodifica) FROM indicatoreaggiornamento WHERE id_indicatore = ? AND id_progetto = ?)" +
        "       AND dataaggiornamento =  (SELECT max(dataaggiornamento) FROM indicatoreaggiornamento WHERE id_indicatore = ? AND id_progetto = ?)";   
    
    /**
     * <p>Modifica solo target e motivazione nella tupla della tabella 
     * indicatoreaggiornamento identificata dall'id dell'indicatore, 
     * passato come parametro, e dall'id del progetto, passato come parametro.</p>
     * <p>Siccome le modifiche del target possono essere molteplici, anche
     * se vale solo l'ultima, nell'aggiornamento bisogna sincerarsi di 
     * aggiornare solo l'ultima modifica esistente nel set di risultati
     * individuati dalla coppia: id_indicatore, id_progetto. Infatti,
     * nell'aggiornamento non viene identificato un id indicatoreaggiornamento,
     * e quindi bisogna inferire la tupla su cui intervenire tramite
     * queste altre informazioni, che incrociate tra loro rendono comunque
     * l'identificazione altrettanto univoca.</p> 
     */
    public static final String UPDATE_INDICATOR_EXTRAINFO_LITE = 
        "UPDATE     indicatoreaggiornamento" +
        "   SET     motivazione = ?" +
        "   ,       target = ?" +
        "   WHERE   id_indicatore = ?" +
        "       AND id_progetto = ?" +
        "       AND dataultimamodifica = (SELECT max(dataultimamodifica) FROM indicatoreaggiornamento WHERE id_indicatore = ? AND id_progetto = ?)" +
        "       AND dataaggiornamento =  (SELECT max(dataaggiornamento) FROM indicatoreaggiornamento WHERE id_indicatore = ? AND id_progetto = ?)";  
    
    /**
     * <p>Query di aggiornamento di una misurazione esistente 
     * il cui identificativo viene passato come parametro.</p>
     * <p>Possono essere aggiornati solo i campi descrittivi della misurazione
     * (valore, note) e il campo "ultima misurazione", e solo da parte di 
     * utenti con elevati privilegi che intervengono per effettuare correzioni
     * richieste dagli utenti. Per questo motivo, i campi automatici
     * non vengono aggiornati, altrimenti si perderebbe traccia dell'utente
     * che originariamente ha effettuato la misurazione, dato di rilievo.</p>
     */
    public static final String UPDATE_MEASUREMENT = 
        "UPDATE     indicatoregestione" +
        "   SET     valore = ?" +
        "   ,       note = ?" +
        "   ,       ultimo = ?" +
        "   WHERE   id = ?";
    
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
     * <p>Modifica il padre di una WBS (o workpackage) selezionato,
     * identificato tramite l'id, passato come parametro.</p>
     */
    public static final String UPDATE_WBS_FATHER = 
            "UPDATE wbs" +
            "   SET     id_wbs = ?" +
            "   ,       dataultimamodifica = ?" +
            "   ,       oraultimamodifica = ?" +
            "   ,       autoreultimamodifica = ?" +
            "   WHERE id = ?";
    
    /**
     * <p>Modifica il numero d'ordine di una WBS (o workpackage) selezionato,
     * identificato tramite l'id, passato come parametro.</p>
     */
    public static final String UPDATE_WBS_ORDER = 
            "UPDATE wbs" +
            "   SET     ordinale = ?" +
            "   ,       dataultimamodifica = ?" +
            "   ,       oraultimamodifica = ?" +
            "   ,       autoreultimamodifica = ?" +
            "   WHERE id = ?";
    
    /**
     * <p>Aggiorna lo stato di una WBS, 
     * identificata tramite id, passato come parametro.</p> 
     * <p>Utile per impostare la WBS come sospesa o eliminata, perch&eacute;
     * questi non sono stati calcolati ma dipendono dall'input dell'utente,
     * dato in una fase successiva a quella dell'inserimento.</p> 
     */
    public String UPDATE_WBS_STATE = 
            "UPDATE wbs" +
            "   SET id_stato = ?" +
            "   ,   dataultimamodifica = ?" +
            "   ,   oraultimamodifica = ?" +
            "   ,   autoreultimamodifica = ?" +
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
     * <p>Aggiorna un monitoraggio dipartimentale di un dato anno.</p>
     */
    public static final String UPDATE_MONITOR_BY_TOGGLE = 
            "UPDATE monitoraggio " +
            "   SET     aperto = ?" +
            "   WHERE  anno = ? " +
            "      AND id_dipart = ?";
    
    /**
     * <p>Aggiorna i permessi dell'utente dipartimentale di un dato anno.</p>
     */
    public static final String UPDATE_ROLES_BY_USER = 
            "UPDATE ruologestione " +
            "   SET     id_ruolo = (SELECT R.id " +
            "                       FROM ruolo R " +
            "                       WHERE R.nome = ?)" +
            "   ,       dataultimamodifica = ?" +
            "   ,       oraultimamodifica = ?" +
            "   ,       autoreultimamodifica = ?" +
            "   WHERE  id_progetto = ? " +
            "      AND id_persona = ?";
    
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
     * <p>Query di selezione finalizzata all'inserimento</p>
     */
    public static final String SELECT_MIN_ID = 
            "SELECT " +
            "       MIN(id)                     AS \"min\"" +
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
            "   ,   id_dipart" +
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
            "   ,       ? " +          // id dipart
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
     * <p>Query per inserimento di una nuova associazione tra competenza
     * e persona (entry di competenzagestione).</p>
     */
    public static final String INSERT_COMPETENZA_GESTIONE = 
            "INSERT INTO competenzagestione" +
            "   (   id" +
            "   ,   id_competenza" +
            "   ,   id_persona )" +    
            "   VALUES (? " +          // id
            "   ,       ? " +          // id competenza
            "   ,       ?)" ;          // id persona
    
    /**
     * <p>Query per inserimento di una nuova appartenenza di un utente 
     * ad un gruppo (entry di belongs).</p>
     */
    public static final String INSERT_BELONGS_TO = 
            "INSERT INTO belongs" +
            "   (   id" +
            "   ,   id0_usr" +
            "   ,   id1_grp )" +    
            "   VALUES (? " +          // id
            "   ,       ? " +          // id utente
            "   ,       (SELECT D.id_share_grp FROM dipartimento D INNER JOIN progetto PJ ON PJ.id_dipart = D.id WHERE PJ.id = ?)" +
            "           )"; // id gruppo"
    
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
     * <p>Inserisce le informazioni di contesto all'aggiornamento dello stato 
     * di una WBS, identificata tramite id, passato come parametro.</p> 
     * <p>Le informazioni di contesto devono essere inserite nella stessa
     * transazione in cui viene cambiato lo stato della WBS, se si tratta
     * di una sospensione, perc&eacute; vi sono alcuni campi obbligatori 
     * che devono essere specificati dall'utente per motivare la sospensione.</p> 
     */
    public String INSERT_WBS_STATE_EXTRAINFO = 
            "INSERT INTO wbsgestione" +
            "   (   id" +
            "   ,   id_wbs" +
            "   ,   id_progetto" +
            "   ,   datasospensione" +
            "   ,   note" +
            "   ,   dataultimamodifica" +
            "   ,   oraultimamodifica" +
            "   ,   autoreultimamodifica" +
            "   ) " +
            "   VALUES (? " +       // id
            "   ,       ? " +       // id_wbs
            "   ,       ? " +       // id_progetto
            "   ,       ? " +       // datasospensione
            "   ,       ? " +       // note
            "   ,       ? " +       // dataultimamodifica
            "   ,       ? " +       // oraultimamodifica
            "   ,       ?)";        // autoreultimamodifica
    
    /**
     * <p>Query di inserimento di un nuovo indicatore</p>
     */
    public static final String INSERT_INDICATOR = 
            "INSERT INTO indicatore" +
            "   (   id" +
            "   ,   nome" +
            "   ,   descrizione" +
            "   ,   baseline" +
            "   ,   dataBaseline" +
            "   ,   annoBaseline" +
            "   ,   target" +
            "   ,   dataTarget" +
            "   ,   annoTarget" +
            "   ,   dataultimamodifica" + 
            "   ,   oraultimamodifica" + 
            "   ,   autoreultimamodifica" +
            "   ,   id_tipoindicatore" +
            "   ,   id_wbs" +
            "   ,   id_progetto" +
            "   ) " +
            "   VALUES (? " +       // id
            "   ,       ? " +       // nome
            "   ,       ? " +       // descrizione
            "   ,       ? " +       // baseline
            "   ,       ? " +       // databaseline
            "   ,       ? " +       // annobaseline
            "   ,       ? " +       // target
            "   ,       ? " +       // datatarget
            "   ,       ? " +       // annotarget
            "   ,       ? " +       // dataultimamodifica
            "   ,       ? " +       // oraultimamodifica
            "   ,       ? " +       // autoreultimamodifica
            "   ,       ? " +       // id_tipoindicatore
            "   ,       ? " +       // id_wbs
            "   ,       ?)";        // id_progetto
    
    /**
     * <p>Query per inserimento di associazione tra indicatore e azione.</p>
     */
    public static final String INSERT_INDICATOR_ON_ACTION = 
            "INSERT INTO indicatoregestione" +
            "   (   id" +
            "   ,   id_indicatore" +
            "   ,   id_wbs" +
            "   ,   id_progetto" +
            "   ,   dataultimamodifica" + 
            "   ,   oraultimamodifica" + 
            "   ,   autoreultimamodifica)" +
            "   VALUES (? " +          // id
            "   ,       ? " +          // id indicatore
            "   ,       ? " +          // id wbs
            "   ,       ? " +          // id progetto
            "   ,       ? " +          // dataultimamodifica
            "   ,       ? " +          // oraultimamodifica
            "   ,       ?)" ;          // autoreultimamodifica
    
    /**
     * <p>Query di inserimento di una nuova misurazione di indicatore</p>
     */
    public static final String INSERT_MEASUREMENT = 
            "INSERT INTO indicatoregestione" +
            "   (   id" +
            "   ,   valore" +
            "   ,   note" +
            "   ,   data" +
            "   ,   ultimo" +
            "   ,   dataultimamodifica" + 
            "   ,   oraultimamodifica" + 
            "   ,   autoreultimamodifica" +
            "   ,   id_indicatore" +
            "   ,   id_wbs" +
            "   ,   id_progetto" +
            "   ) " +
            "   VALUES (? " +       // id
            "   ,       ? " +       // valore
            "   ,       ? " +       // note
            "   ,       ? " +       // data
            "   ,       ? " +       // ultimo
            "   ,       ? " +       // dataultimamodifica
            "   ,       ? " +       // oraultimamodifica
            "   ,       ? " +       // autoreultimamodifica
            "   ,       ? " +       // id_indicatore
            "   ,       (SELECT I.id_wbs FROM indicatore I WHERE I.id = ?) " + // id_wbs
            "   ,       ?)";        // id_progetto
    
    /**
     * <p>Inserisce informazioni di aggiornamento al campo target di un indicatore 
     * identificato tramite id passato come parametro.</p> 
     * <p>L'aggiornamento di un target di un indicatore effettuato in questo
     * modo permette di cambiare il valore del target in modo non distruttivo
     * perch&eacute; la nuova informazione si aggiunge alla precedente, non
     * la sostituisce; inoltre, vi sono ulteriori informaizioni che devono 
     * essere specificate dall'utente per motivare l'aggiornamento, e che
     * richiedono quindi la valorizzazione in una tabella a parte rispetto
     * a quella dell'indicatore stesso.</p> 
     */
    public String INSERT_INDICATOR_EXTRAINFO = 
            "INSERT INTO indicatoreaggiornamento" +
            "   (   id" +
            "   ,   id_indicatore" +
            "   ,   id_progetto" +
            "   ,   motivazione" +           
            "   ,   target" +            
            "   ,   datatarget" +
            "   ,   annotarget" +
            "   ,   dataaggiornamento" +
            "   ,   dataultimamodifica" +
            "   ,   oraultimamodifica" +
            "   ,   autoreultimamodifica" +
            "   ) " +
            "   VALUES (? " +       // id
            "   ,       ? " +       // id_indicatore
            "   ,       ? " +       // id_progetto
            "   ,       ? " +       // motivazione
            "   ,       ? " +       // target
            "   ,       ? " +       // datatarget
            "   ,       ? " +       // annotarget
            "   ,       ? " +       // dataaggiornamento
            "   ,       ? " +       // dataultimamodifica
            "   ,       ? " +       // oraultimamodifica
            "   ,       ?)";        // autoreultimamodifica
    
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
    
    /**
     * <p>Query per inserimento di un nuovo progetto phantom.</p>
     */
    public static final String INSERT_PHANTOM_PROJECT = 
            "INSERT INTO progetto" +
            "   (   id" +
            "   ,   titolo" +
            "   ,   datainizio" +
            "   ,   datafine" +
            "   ,   meseriferimento" +
            "   ,   dataultimamodifica" +
            "   ,   oraultimamodifica" +
            "   ,   autoreultimamodifica" +
            "   ,   id_dipart" +
            "   ,   id_statoprogetto" +
            "   ,   id_periodoriferimento )" +
            "   VALUES (? " +          // id
            "   ,       'Phantom ' || (SELECT acronimo FROM dipartimento WHERE id = ?) " +          // titolo
            "   ,       ? " +          // datainizio
            "   ,       ? " +          // datafine
            "   ,       ? " +          // meseriferimento
            "   ,       ? " +          // dataultimamodifica
            "   ,       ? " +          // oraltimamodifica
            "   ,       ? " +          // autoreultimamodifica
            "   ,       ? " +          // id_dipart
            "   ,       ? " +          // id_statoprogetto
            "   ,       ?)" ;          // id_periodoriferimento
    
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
            "   ,   dataultimamodifica = ?" +
            "   ,   oraultimamodifica = ?" +
            "   ,   autoreultimamodifica = ?" +
            "   WHERE id = ?";    
    
    /**
     * <p>Dereferenzia il ruolo di una persona rispetto a un progetto cui
     * era referenziato, collegandolo ad un altro progetto, che nella logica
     * applicativo corrisponder&agrave; al progetto fantasma di dipartimento.</p>
     */
    public static final String DELETE_ROLE = 
            "UPDATE ruologestione " +
            "   SET     id_ruolo = (SELECT R.id " +
            "                       FROM ruolo R " +
            "                       WHERE R.nome ILIKE ?)" +
            "   ,       id_progetto = ?" +
            "   ,       dataultimamodifica = ?" +
            "   ,       oraultimamodifica = ?" +
            "   ,       autoreultimamodifica = ?" +
            "   WHERE  id_progetto = ? " +
            "      AND id_persona = ?";
    
    
}