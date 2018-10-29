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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.MissingResourceException;
import java.util.logging.Logger;

import it.alma.exception.CommandException;


/**
 * <p>Classe contenitore di metodi di utilit&agrave; e costanti
 * di uso comune</p>
 * 
 * @author  <a href="mailto:giovanroberto.torre@univr.it">Giovanroberto Torre</a>
 */
public class Utils {
    
    /**
     * <p>Logger della classe per scrivere i messaggi di errore. 
     * All logging goes through this logger.</p>
     * <p>Non &egrave; privata ma Default (friendly) per essere visibile 
     * negli oggetti ovverride implementati da questa classe.</p>
     */
    static Logger log = Logger.getLogger(Utils.class.getName());
    /**
     * <p>Nome di questa classe 
     * (viene utilizzato per contestualizzare i messaggi di errore).</p>
     * <p>Non &egrave; privata ma Default (friendly) per essere visibile 
     * negli oggetti ovverride implementati da questa classe.</p>
     */
    static final String FOR_NAME = "\n" + Logger.getLogger(new Throwable().getStackTrace()[0].getClassName()) + ": ";
    /**
     * <p>Costante da utilizzare quando serve un valore per inizializzazione
     * o da utilizzare come argomento.</p>
     * <p>Maggiormente visibile e chiara della stringa vuota 
     * (doppi apici aperti chiusi) utilizzata direttamente nel codice, 
     * e pi&uacute; ottimizzata rispetto al puntamento a una nuova stringa 
     * attraverso il richiamo del costruttore &nbsp;<code>new String("");</code><br />
     * (si veda, ad esempio: 
     * <a href="http://www.precisejava.com/javaperf/j2se/StringAndStringBuffer.htm">
     * questa discussione</a>).
     */
    public static final String VOID_STRING = "";
    /**
     * <p>Numero di anni da spostare per l'estrazione degli elementi
     * nella visualizzazione di default.
     * P.es. un valore 1 implica un'estrazione di un anno, tra la data corrente
     * e la data corrente dell'anno prossimo.</p>
     */
    public static final int YEAR_SHIFT = 1; 
    
    
    /**
     * Costruttore vuoto.
     * Svolge la stessa funzione del costruttore di default.
     */
    public Utils() {
        /*;*/   // It doesn't anything
    }

    
    /**
     * <p>Controlla se una stringa corrisponde a un valore intero.</p>
     * 
     * @param s la String da controllare
     * @return <code>true</code> se la String e' convertibile in intero, false altrimenti
     */
    private static boolean isInteger(String s) {
        try { 
            Integer.parseInt(s); 
        } catch (NumberFormatException nfe) { 
            return false; 
        } catch (NullPointerException npe) {
            return false;
        }
        // Only got here if we didn't return false
        return true;
    }
    
    /* ************************************************************************ *
     *   Metodi di utilita' per la definizione e la manipolazione delle date    *
     * ************************************************************************ */    
    
    /**
     * <p>Restituisce una data di default da utilizzare nell'estrazione
     * di elementi soggetti a una scansione temporale.</p>
     * <p>Ad esempio, i "bolli", anche quelli di dol3 ("superbolli"),
     * hanno una data di inizio e di fine pubblicazione.<br />
     * Lo stesso vale per le iniziative, e in genere per tutti gli elementi
     * che vengono mostrati a vario titolo, sotto forma di claim, di elenco,
     * di lista, di menu, etc., nelle pagine intermedie 
     * del sito di dipartimento.</p>
     * <p>La data di default restituita in questione &egrave; quella che 
     * la command utilizza come data di inizio ricerca di tutti gli elementi
     * da mostrare nelle pagine intermedie, in particolare i "superbolli"
     * (contenuti) di dol3 <em>e corrisponde, sostanzialmente,
     * alla data corrente</em>.</p>
     * <p>Questo valore &egrave; molto utile nel caso in cui si debbano
     * effettuare nell'intervallo di default estrazioni di elementi storicizzati 
     * dall'esterno del metodo in cui tali date determinanti l'intervallo
     * di default sono calcolate, evitando quindi
     * di dover riprodurre tutto il codice del calcolo di tali date
     * (p.es., dal metodo pubblico che restituisce il numero di iniziative 
     * attive nell'intervallo di default).</p>
     * <p>
     * Il valore restituito &egrave; un 
     * <a href="http://docs.oracle.com/javase/6/docs/api/java/util/GregorianCalendar.html">
     * GregorianCalendar</a> piuttosto che una
     * <a href="http://docs.oracle.com/javase/6/docs/api/java/util/Date.html">
     * java.util.Date</a>.<br />
     * Il motivo per cui &egrave; preferito un GregorianCalendar a una Date
     * &egrave, che la 
     * <a href="http://docs.oracle.com/javase/6/docs/api/java/util/Date.html">
     * java.util.Date</a> 
     * <cite id="stackoverflow">ha molti metodi di trasformazione deprecati,
     * mentre il Calendar ha una localizzazione pi&uacute; agevole 
     * <a href="http://stackoverflow.com/questions/1404210/java-date-vs-calendar">
     * (v.)</a></cite>
     * Siccome per motivi di retrocompatibilit&agrave; spesso non va bene
     * usare un Calendar ma ci vuole una Date (la WebStorage usa le Date),
     * basta applicare una semplice trasformazione al valore restituito,
     * quale la seguente:
     * <pre>
     * java.util.Date startdate = getDefaultStartDate().getTime();
     * </pre>
     * (o, se si preferisce, per ancor maggiore comodit&agrave;, utilizzare
     * il metodo di utilit&agrave; <code>convert()</code>,
     * definito pi&uacute; avanti).</p>
     * 
     * @return <code>GregorianCalendar</code> - la data corrispondente al limite inferiore dell'intervallo di default di ricerca di elementi storicizzati
     */
    public static GregorianCalendar getCurrentDate() {
        /* Variabile privata di tipo GregorianCalendar 
         * necessaria per fornire un default in mancanza di 
         * date di inzio e di fine ricerca
         * personalizzate dall'utente.<br /> 
         * Viene definita a livello di istanza e non a livello locale
         * perch&eacute; utile a diversi metodi di istanza
         * (metodi di utilit&agrave;). */
        GregorianCalendar calendar = new GregorianCalendar();
        /* Oggi (giorno corrente) sotto forma numerica.
         * Indica il giorno del mese (p.es. '22' del 22/04/1970*)
         * <p><small>* First <em>Earth Day</em></small></p> */
        int day = calendar.get(Calendar.DATE);
        /* Questo mese (mese corrente) sotto forma numerica.
         * Indica il mese dell'anno (p.es. '3' del 22/04/1970*)
         * <p><small>* Calenda.MONTH parte da zero</small></p> */
        int month = calendar.get(Calendar.MONTH);
        /* Quest'anno (mese corrente) sotto forma numerica.
         * <p>Indica l'anno corrente (p.es. '1970' del 22/04/1970)</p> */
        int year = calendar.get(Calendar.YEAR);
        // Costruisce la data di default
        GregorianCalendar dateConverted = new GregorianCalendar(year, month, day);
        // Restituisce al chiamante la data di default
        return dateConverted;
    }
    
    
    /**
     * <p>Restituisce una data di default, <em>pari alla data corrente traslata
     * di un anno</em> (la stessa data di oggi, ma fra un anno), 
     * da utilizzare nell'estrazione di elementi 
     * soggetti a una scansione temporale.</p>
     * <p>Questo valore &egrave; molto utile nel caso in cui si debbano
     * effettuare nell'intervallo di default estrazioni 
     * di contenuti storicizzati, dall'esterno
     * del metodo in cui tali date determinanti l'intervallo
     * di default sono calcolate, evitando quindi
     * di dover riprodurre tutto il codice del calcolo di tali date
     * (p.es., dal metodo pubblico che restituisce il numero di iniziative 
     * attive nell'intervallo di default).</p>
     * <p>
     * Il valore restituito &egrave; un 
     * <a href="http://docs.oracle.com/javase/6/docs/api/java/util/GregorianCalendar.html">
     * GregorianCalendar</a> piuttosto che una
     * <a href="http://docs.oracle.com/javase/6/docs/api/java/util/Date.html">
     * java.util.Date</a>.<br />
     * Il motivo per cui &egrave; preferito un GregorianCalendar a una Date
     * &egrave, che la 
     * <a href="http://docs.oracle.com/javase/6/docs/api/java/util/Date.html">
     * java.util.Date</a> 
     * <cite id="stackoverflow">ha molti metodi di trasformazione deprecati,
     * mentre il Calendar ha una localizzazione pi&uacute; agevole 
     * <a href="http://stackoverflow.com/questions/1404210/java-date-vs-calendar">
     * (v.)</a></cite>
     * Siccome per motivi di retrocompatibilit&agrave; spesso non va bene
     * usare un Calendar ma ci vuole una Date (la WebStorage usa le Date),
     * basta applicare una semplice trasformazione al valore restituito,
     * quale la seguente:
     * <pre>
     * java.util.Date enddate = getDefaultEndDate().getTime();
     * </pre>
     * o, ancor pi&uacute; comodamente, utilizzare
     * il metodo di utilit&agrave; <code>convert()</code>.</p>
     * 
     * @return <code>GregorianCalendar</code> - la data corrispondente al limite superiore dell'intervallo di default di ricerca di elementi storicizzati
     */
    public static GregorianCalendar getCurrentDateNextYear() {
        GregorianCalendar calendar = new GregorianCalendar();
        int day = calendar.get(Calendar.DATE);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        GregorianCalendar dateConverted = new GregorianCalendar(year + YEAR_SHIFT, month, day);
        return dateConverted;
    }
    
    
    /**
     * <p>Restituisce una data di default (vale a dire una data da usare
     * nel caso in cui non esista una data costruita a tempo di esecuzione 
     * sulla base di parametri inseriti dall'utente) che la command utilizza 
     * come data di fine ricerca di elementi soggetti a una scansione
     * temporale, pi&uacute; uno shift a piacere passato come argomento
     * (in pratica, equivale alla data odierna tra <em>'tot'</em> anni, 
     * con <em>'tot'</em> passato come argomento).</p>
     * <p>Questo valore &egrave; molto utile nel caso in cui si debbano
     * effettuare estrazioni di contenuti nell'intervallo di default dall'esterno
     * del metodo in cui tali date sono calcolate, evitando quindi
     * di dover riprodurre tutto il codice del calcolo di tali date
     * (p.es., dal metodo pubblico che restituisce il numero di contenuti 
     * attivi nell'intervallo di default).<br />
     * Il motivo per il quale viene utilizzato uno shift &egrave;
     * che non &egrave; sempre detto che l'anno di fine ricerca
     * sia pari all'anno di inizio ricerca incrementato di 1. Potrebbe 
     * essere desiderabile effettuare una ricerca in un intervallo pi&uacute;
     * lungo, p.es. se si volesse mostrare il numero totale di
     * iniziative attive nei prossimi <em>tot</em> anni, con <em>tot</em>
     * passato come argomento.</p>
     * <p>
     * Il valore restituito &egrave; un 
     * <a href="http://docs.oracle.com/javase/6/docs/api/java/util/GregorianCalendar.html">
     * GregorianCalendar</a> piuttosto che una
     * <a href="http://docs.oracle.com/javase/6/docs/api/java/util/Date.html">
     * java.util.Date</a>.<br />
     * Il motivo per cui &egrave; preferito un GregorianCalendar a una Date
     * &egrave, che la 
     * <a href="http://docs.oracle.com/javase/6/docs/api/java/util/Date.html">
     * java.util.Date</a> 
     * <cite id="stackoverflow">ha molti metodi di trasformazione deprecati,
     * mentre il Calendar ha una localizzazione pi&uacute; agevole 
     * <a href="http://stackoverflow.com/questions/1404210/java-date-vs-calendar">
     * (v.)</a></cite>
     * Siccome per motivi di retrocompatibilit&agrave; spesso non va bene
     * usare un Calendar ma ci vuole una Date (la WebStorage usa le Date),
     * basta applicare una semplice trasformazione al valore restituito,
     * quale la seguente:
     * <pre>
     * java.util.Date enddate = getDefaultEndDate(10).getTime();
     * </pre>
     * dove 10 &egrave, in questo caso, ovviamente, un valore arbitrario
     * (sta per un intervallo di ricerca di 10 anni!).</p>
     * 
     * @param shift intero corrispondente al numero di anni da sommare all'anno di inizio ricerca per ottenere l'anno di fine ricerca
     * @return <code>GregorianCalendar</code> - la data corrispondente al limite superiore dell'intervallo di default di ricerca di elementi storicizzati, con l'intervallo determinato dallo shift
     */
    public static GregorianCalendar getCurrentDateInAFewYears(int shift) {
        GregorianCalendar calendar = new GregorianCalendar();
        int day = calendar.get(Calendar.DATE);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        GregorianCalendar dateConverted = new GregorianCalendar(year + shift, month, day);
        return dateConverted;
    }
    
    
    /**
     * <p>Restituisce la data corrente,
     * addizionata o sottratta di:
     * <em><ul>
     * <li>tot giorni</li>
     * <li>tot mesi</li>
     * <li>tot anni</li>
     * </ul></em>
     * con giorni, mesi e anni passati come argomenti.</p>
     * <p><ol>
     * <li>Se si vuol <em>sottrarre</em> anzich&eacute; <em>aggiungere</em> 
     * basta passare valori negativi dei parametri.</li>
     * <li>Se si vuol sottrarre o aggiungere solo uno o solo un paio di 
     * parametri, ma non tutti e tre, basta passare zero <code>(0)</code> 
     * come valore dei parametri non interessanti.</li>
     * </ol></p>
     * <p>
     * Questo metodo risulta utile quando bisogna passare ad una query
     * una data come limite di periodo di estrazione.</p>
     * <p>
     * Notare che per ottenere qualsiasi data sarebbe sufficiente
     * aggiungere o sottrarre il numero giusto di soli giorni.
     * Tuttavia in alcuni casi pu&ograve; essere pi&uacute;
     * comodo passare lo shift giusto di mesi e anni piuttosto che 
     * ricavare la data voluta attraverso il numero di giorni in pi&uacute;
     * o in meno, e per questo motivo il metodo ha 3 possibili parametri
     * (anche se nulla vieta di utilizzare solo il primo per ottenere
     * tutte le date volute).</p>
     * 
     * @param days   numero di giorni da aggiungere o togliere alla data corrente per ottenere una data desiderata
     * @param months numero di mesi   da aggiungere o togliere alla data corrente per ottenere una data desiderata
     * @param years  numero di anni   da aggiungere o togliere alla data corrente per ottenere una data desiderata
     * @return <code>GregorianCalendar</code> - la data desiderata, pari alla data corrente aggiunta o sottratta di giorni e/o mesi e/o anni specificati dai parametri
     */
    public static GregorianCalendar getDate(int days, int months, int years) {
        GregorianCalendar date = getCurrentDate();
        if (days != 0) {
            // Aggiunge, o toglie, 'days'
            date.add(Calendar.DATE, days);
        }
        if (months != 0) {
            // Aggiunge, o toglie, 'months'
            date.add(Calendar.MONTH, months);
        }
        if (years != 0) {
            // Aggiunge, o toglie, 'years'
            date.add(Calendar.YEAR, years);
        }
        return date;
    }
    
    
    /**
     * Formatta una data di tipo <code>java.util.GregorianCalendar</code> 
     * secondo il formato standard italiano, ovvero in base alla maschera:
     * <pre>"gg/mm/aaaa"</pre>
     * e la restituisce sotto forma di oggetto String.
     * 
     * @param date un java.util.GregorianCalendar da formattare e convertire
     * @return <code>String</code> - una rappresentazione String della data originale
     */
    public static String format(GregorianCalendar date) {
        SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");
        String dateFormatted = fmt.format(date.getTime());
        return dateFormatted;
    }
    
    
    /**
     * Formatta una data di tipo <code>java.util.Date</code> 
     * secondo il formato standard italiano, ovvero in base alla maschera:
     * <pre>"gg/mm/aaaa"</pre>
     * e la restituisce sotto forma di oggetto String.
     * 
     * @param date una java.util.Date da formattare e convertire
     * @return <code>String</code> - una rappresentazione String della data originale
     */
    public static String format(java.util.Date date) {
        SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");
        String dateFormatted = fmt.format(date.getTime());
        return dateFormatted;
    }
    
    
    /**
     * <p>Formatta una data di tipo <code>java.util.GregorianCalendar</code> 
     * secondo un filtro in base a maschera, ovvero in base a un pattern
     * passato come argomento
     * e la restituisce sotto forma di oggetto String.</p>
     * <p>
     * Per un elenco dei valori di pattern ammessi, v. la classe
     * {@link SimpleDateFormat} 
     * (<a href="http://docs.oracle.com/javase/6/docs/api/java/text/SimpleDateFormat.html">
     * javadoc</a>)</p>
     * 
     * @param date un java.util.GregorianCalendar da formattare e convertire
     * @param mask la maschera in base a cui formattare la data
     * @return <code>String</code> - una rappresentazione String della data originale
     * @throws CommandException se il pattern
     */
    public static String format(GregorianCalendar date, 
                                String mask)
                         throws CommandException {
        try {
            SimpleDateFormat fmt = new SimpleDateFormat(mask);
            String dateFormatted = fmt.format(date.getTime());
            return dateFormatted;
        } catch (NullPointerException npe) {
            StackTraceElement[] stackTrace = npe.getStackTrace();
            StringBuffer trace = new StringBuffer("\n");
            for (StackTraceElement stack : stackTrace) 
                trace.append(stack.toString()).append("\n");
            String msg = FOR_NAME + "Si e\' verificato un problema di puntamento a: " + npe.getMessage() + trace.toString();            
            log.warning(msg + "Problema probabilmente legato al costruttore di SimpleDateFormat, che restituisce una NullPointerException se la maschera in base a cui l\'oggetto viene generato e\' null.\n");
            throw new CommandException(msg, npe);
        } catch (IllegalArgumentException iae) {
            String msg = FOR_NAME + "Si e\' verificato un problema nella generazione di un oggetto per la formattazione delle date.\n" + iae.getMessage();
            log.warning(msg + "Problema legato con ogni probabilita\' al costruttore di SimpleDateFormat, che restituisce una IllegalArgumentException se il pattern di formattazione fornito non e\' valido.\n");
            throw new CommandException(msg, iae);
        } catch (MissingResourceException mre) {
            String msg = FOR_NAME + "Problema nel recupero di un valore.\n" + mre.getMessage();
            log.warning(msg + "Si e\' verificato un problema nel metodo di formattazione della data.\n");
            throw new CommandException(msg, mre);
        } catch (Exception e) {
            String msg = FOR_NAME + "Si e\' verificato un problema. Impossibile visualizzare i risultati.\n" + e.getLocalizedMessage();
            log.warning(msg + "Attenzione: si e\' verificato un problema nel metodo di formattazione della data.\n");
            throw new CommandException(msg, e);
        }
    }

    
    /**
     * <p>Converte un'istanza di <code>java.util.GregorianCalendar</code>
     * in un'istanza di <code>java.util.Date</code></p>
     * <p>
     * <a href="http://docs.oracle.com/javase/6/docs/api/java/util/GregorianCalendar.html">
     * java.util.GregorianCalendar</a> rispetto a
     * <a href="http://docs.oracle.com/javase/6/docs/api/java/util/Date.html">
     * java.util.Date</a> &egrave; un oggetto preferibile per
     * la gestione del tempo, perch&eacute; la {@link java.util.Date} 
     * <cite id="stackoverflow">ha molti metodi di trasformazione deprecati,
     * mentre il Calendar ha una localizzazione pi&uacute; agevole 
     * <a href="http://stackoverflow.com/questions/1404210/java-date-vs-calendar">
     * (v.)</a></cite></p>
     * Siccome per motivi di retrocompatibilit&agrave; spesso non va bene
     * usare un Calendar ma ci vuole una Date (la {@link DBWrapper} usa le Date),
     * viene messo a disposizione questo metodo, che implementa la semplice
     * trasformazione da {@link GregorianCalendar} a {@link Date}. 
     * 
     * @param date un java.util.GregorianCalendar da trasformare in una java.util.Date
     * @return <code>java.util.Date</code> - una java.util.Date in cui il GregorianCalendar passato come argomento e' stato trasformato
     */
    public static java.util.Date convert(GregorianCalendar date) {
        return date.getTime();
    }
    
    
    /**
     * <p>Converte un'istanza di <code>java.util.Date</code>
     * in un'istanza di <code>java.sql.Date</code></p>
     * <p>
     * Per rappresentare le date
     * nelle interrogazioni SQL standard non va bene 
     * un'istanza di {@link java.util.Date}
     * ma ci vuole piuttosto un'istanza di {@link java.sql.Date}.</p> 
     * 
     * @param date una java.util.Date da trasformare in una java.sql.Date
     * @return <code>java.sql.Date</code> - una java.sql.Date in cui la java.util.Date passata come argomento e' stato trasformata
     */
    public static java.sql.Date convert(java.util.Date date) {
        return new java.sql.Date(date.getTime());
    }
    
    
    /**
     * Restituisce l'anno corrente.<br />
     * Questo metodo &egrave; generalmente utilizzato ad esempio nei footer, 
     * per l'indicazione del copyright (&copy;).
     * 
     * @return <code>String</code> - l'anno corrente sotto forma di oggetto String
     */
    public static String getCurrentYear() {
        int yearPosition = Calendar.YEAR;
        Calendar nowHere = Calendar.getInstance();
        int year = nowHere.get(yearPosition);
        Integer yearWrapper = new Integer(year);
        return yearWrapper.toString();
    }
    
    
    /**
     * Restituisce l'anno corrente.<br />
     * Questo metodo &egrave; generalmente utilizzato ad esempio nei footer, 
     * per l'indicazione del copyright (&copy;).
     * 
     * @return <code>String</code> - l'anno corrente sotto forma di oggetto String
     */
    public static int getCurrentIntYear() {
        int yearPosition = Calendar.YEAR;
        Calendar nowHere = Calendar.getInstance();
        int year = nowHere.get(yearPosition);
        return year;
    }
    
}