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

package it.alma.command;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import it.alma.DBWrapper;
import it.alma.Query;
import it.alma.bean.CourseBean;
import it.alma.bean.ItemBean;
import it.alma.exception.AttributoNonValorizzatoException;
import it.alma.exception.CommandException;
import it.alma.exception.NotFoundException;
import it.alma.exception.WebStorageException;


/** 
 * <p>ValuationCommand.java</p>
 * 
 * <p>Classe per l'implementazione delle regole di scelta delle AD
 * da mettere in valutazione ai fini dei questionari della didattica.
 * <br /> 
 * Consente di applicare su una lista di Attivit&agrave; Didattiche 
 * le regole stabilite dal presidio della qualit&agrave;.<br /></p>
 * <p>Questa classe possiede i metodi che le consentono di capire, 
 * partendo da una lista di "AD grezze", ovvero da un "semilavorato":
 * <ul>
 * <li>quali occorrenze sono realmente doppie;</li> 
 * <li>quali sono apparentemente doppie 
 * (cio&egrave; sono uguali in tutti i dati fondamentali
 * tranne che nel periodo).</li>
 * </ul>
 * Facciamo un esempio, molto semplificato: se avessimo il seguente insieme 
 * {3, 4a, 4a, 4b, 4b, 7, 7} come lista delle "AD semilavorate", dovremmo ottenere
 * come risultato "pulito" ("lavorato") il seguente:
 * {3, 4ab, 7}.
 * Questo risultato, secondo le logica implementata nei metodi della presente
 * classe, si otterrebbe nel seguente modo:
 * <pre>
 * {3, 4a, 4a, 4b, 4b, 7, 7} -> {3}, {4a, 4a, 4b, 4b, 7, 7};
 *    {4a, 4a, 4b, 4b, 7, 7} -> {4a, 4b, 7}
 *      {4a, 4b, 7} -> {4ab, 7}
 * {3}, {4ab, 7} -> {3, 4ab, 7}.</pre>
 * Una volta individuati i record "puliti" - risultato che si ottiene depurando
 * la lista "semilavorata" di tutti i doppi in blocco, raffinando tali doppi 
 * (prendendo solo una delle due (o pi&uacute;) occorrenze di un doppione, 
 * e unificando i periodi), e riunendo la lista di tali "occorrenze doppie 
 * lavorate" alla lista con i doppi tagliati - la classe &egrave; in grado 
 * di selezionare da questo elenco lavorato soltanto le AD 
 * che devono essere sottoposte a valutazione, attraverso l'applicazione, 
 * sulla lista "raffinata", di un algoritmo che implenenta 
 * i criteri stabiliti dal presidio.</p>
 * 
 * <p>Created on martedì 4 settembre 2018 10:28:08</p>
 * 
 * 
 * @author <a href="mailto:giovanroberto.torre@univr.it">Giovanroberto Torre</a>
 */
public class ValuationCommand extends ItemBean implements Command {
    
    /**
     *  Nome di questa classe 
     *  (utilizzato per contestualizzare i messaggi di errore)
     */
    static final String FOR_NAME = "\n" + Logger.getLogger(new Throwable().getStackTrace()[0].getClassName()) + ": ";
    /**
     * Pagina a cui la servlet reindirizza
     */
    private static final String nomeFileElenco = "/jsp/elenco.jsp";  
    /**
     * Lista Hash contenente tutti i codici dei corsi elettivi
     */
    private LinkedList<String> corsiElettivi;
    
    
    /** 
     * Crea una nuova istanza di ValuationCommand 
     */
    public ValuationCommand() {
        ;   // Vuota
    }
  
    
    /** 
     * <p>Raccoglie i valori dell'oggetto ItemBean
     * e li passa a questa classe command.</p>
	 *
	 * @param voceMenu la VoceMenuBean pari alla Command presente.
	 * @throws it.alma.exception.CommandException se l'attributo paginaJsp di questa command non e' stato valorizzato.
     */
    public void init(ItemBean voceMenu) throws CommandException {
        this.setId(voceMenu.getId());
        this.setNome(voceMenu.getNome());
        this.setLabelWeb(voceMenu.getLabelWeb());
        this.setNomeClasse(voceMenu.getNomeClasse());
        this.setPaginaJsp(voceMenu.getPaginaJsp());
        this.setInformativa(voceMenu.getInformativa());
        if (this.getPaginaJsp() == null) {
          String msg = FOR_NAME + "La voce menu' " + this.getNome() + " non ha il campo paginaJsp. Impossibile visualizzare i risultati.\n";
          throw new CommandException(msg);
        }
        // Trasforma l'array dei corsi elettivi in una LinkedList per comodità di utilizzo
        corsiElettivi = new LinkedList<String>();
        for (int i = 0; i < Query.CODICI_CORSI_ELETTIVI.length; i++) {
            // Codice corrente corso elettivo
            String codiceElettivo = Query.CODICI_CORSI_ELETTIVI[i];
            corsiElettivi.add(codiceElettivo);
        }
    }  
  
    
    /**
     * <p>Gestisce il flusso principale.</p>
     * <p>Prepara i bean.</p>
     * <p>Passa nella Request i valori che verranno utilizzati dall'applicazione.</p>
     */
    public void execute(HttpServletRequest req) 
                 throws CommandException {  
        DBWrapper db = null;               // Databound
        Vector<CourseBean> v = new Vector<CourseBean>();
        HashMap<String, CourseBean> sda = new HashMap<String, CourseBean>();
        List<CourseBean> duplicates = null;
        //Vector<CourseBean> purged = new Vector<CourseBean>();
        //HashMap<String, CourseBean> vHash = new HashMap<String, CourseBean>();
        // Instanzia nuova classe WebStorage per il recupero dei dati
        try {
            db = new DBWrapper();
        } catch (WebStorageException wse) {
            throw new CommandException(FOR_NAME + "Non e\' disponibile un collegamento al database\n." + wse.getMessage(), wse);
        }
        // Recupera Attività Didattiche Semplici
        try {
            v = db.getADSemplici();
        } catch (NotFoundException nfe) {
            throw new CommandException(FOR_NAME + ": Impossibile recuperare un attributo obbligatorio.\n" + nfe.getMessage(), nfe);
        } catch (WebStorageException wse) {
            StackTraceElement[] stackTrace = wse.getStackTrace();
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i<stackTrace.length; i++) {
                StackTraceElement ste = stackTrace[i];
                sb.append(ste.getClassName());
                sb.append(".");
                sb.append(ste.getMethodName());
                sb.append(" (");
                sb.append(ste.getClassName());
                sb.append(":");
                sb.append(ste.getLineNumber());
                sb.append(")");
                sb.append("\n\t");
            }
            throw new CommandException(FOR_NAME + "Si e\' verificato un problema nel recuperare i dati relativi all'elenco delle AD.\n" + wse.getMessage() + "\n stack: " + sb, wse);
        } catch (NullPointerException npe) {
            StackTraceElement[] stackTrace = npe.getStackTrace();
            throw new CommandException(FOR_NAME + "Si e\' verificato un puntamento a null in " + this.getClass().getName() + ": " + npe.getMessage() + "\n stack: " + stackTrace[0], npe);            
        } catch (Exception e) {
            StackTraceElement[] stackTrace = e.getStackTrace();
            throw new CommandException(FOR_NAME + "Eccezione generica da " + this.getClass().getName() + ": " + e.getMessage() + "\n stack: " + stackTrace[0], e);
        }
        // Ottiene i duplicati logici (righe con la stessa chiave e id diversi)
        duplicates = getDuplicates(v);
        // Ordina i duplicati logici per chiave
        sortByCode(duplicates);
        // Ottiene una lista di solo i campioni di duplicati, cioè in pratica di chiavi che hanno due o più occorrenze nella lista delle AD
        List<CourseBean> samplesDuplicates = splitDuplicates(duplicates);
        
        List<CourseBean> merged = mergeDates(samplesDuplicates);
        // Individua le Attività Didattiche Semplici da porre in valutazione
        //sortByCode(v);
        sda = removeDuplicates(v, new ArrayList<CourseBean>(duplicates));
        
        // Imposta il testo del Titolo da visualizzare prima dell'elenco
        req.setAttribute("titoloE", "AD Semplici");
        // Salva nella request: Titolo (da mostrare nell'HTML)
        req.setAttribute("tP", req.getAttribute("titoloE"));         
        // Imposta la Pagina JSP di forwarding
        req.setAttribute("fileJsp", nomeFileElenco);
         // Salva nella request: elenco AD Semplici
        req.setAttribute("lista", sda);
        req.setAttribute("elenco", v);
        req.setAttribute("duplicati", duplicates);
        req.setAttribute("doppioni", samplesDuplicates);
        req.setAttribute("doppi", merged);
    }
    
    
    /**
     * <p>Restituisce una lista di AD che sono state individuate come doppie
     * in base alla chiave specificata nell'oggetto AD.</p>
     * <p>I doppioni si definiscono come <code>"AD aventi la stessa chiave"</code>
     * e non quindi come "tuple identiche".<br />
     * Si tratta quindi di duplicati logici, non di duplicati identici.<br /> 
     * Il presente metodo effettua un controllo per eliminare eventuali duplicati
     * identici e restituisce soltanto i duplicati logici.</p>
     * 
     * @param v un Vector di AD da setacciare alla ricerca di duplicati
     * @return <code>List&lt;CourseBean&gt;</code> - ArrayList di duplicati trovati  
     * @throws CommandException se si verifica un problema, generalmente nell'accesso a qualche valore o un puntamento fuori tabella
     */
    private List<CourseBean> getDuplicates(Vector<CourseBean> v)
                                    throws CommandException {
        List<CourseBean> genericDuplicates = null;
        List<CourseBean> logicalDuplicates = null;
        try {
            int vSize = v.size();
            genericDuplicates = new ArrayList<CourseBean>();
            // Cicla due volte il Vector delle AD per individuare AD con la stessa chiave
            for (int i = 0; i < vSize; i++) {
                for (int j = 0; j < vSize; j++) {
                    if (i != j) {   // Se i e j sono uguali sta puntando allo stesso elemento, ed è evidente che un elemento è uguale a se stesso...
                        CourseBean courseRaw = v.elementAt(i);
                        CourseBean courseOrd = v.elementAt(j);
                        if (courseRaw.getKey().equals(courseOrd.getKey()) && (courseRaw.getId() != courseOrd.getId())) {
                            genericDuplicates.add(courseOrd);
                        }
                    }
                }
            }
            // Ordina la lista ottenuta in base al criterio specificato nel metodo override compareTo del Bean
            Collections.sort(genericDuplicates);
            // Clona la lista dei duplicati
            logicalDuplicates = new ArrayList<CourseBean>(genericDuplicates);
            // Cicla la lista dei duplicati
            for (int i = 0; i < genericDuplicates.size(); i++) {
                // Costruisce l'indice del record successivo a quello recuperato
                int j = i + 1;
                // Controllo per evitare puntamento fuori tabella
                if (j < genericDuplicates.size()) {
                    // I doppioni veri hanno anche lo stesso id
                    if (genericDuplicates.get(i).getId() == genericDuplicates.get(j).getId()) {
                        // Elimina il doppione vero (vero = identico in tutto, anche nell'id)
                        logicalDuplicates.remove(j);
                    }
                }
            }
            return logicalDuplicates;
        } catch (ArrayIndexOutOfBoundsException aiobe) {
            StackTraceElement[] stackTrace = aiobe.getStackTrace();
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i<stackTrace.length; i++) {
                StackTraceElement ste = stackTrace[i];
                sb.append(ste.getClassName());
                sb.append(".");
                sb.append(ste.getMethodName());
                sb.append(" (");
                sb.append(ste.getClassName());
                sb.append(":");
                sb.append(ste.getLineNumber());
                sb.append(")");
                sb.append("\n\t");
            }
            throw new CommandException(FOR_NAME + "Puntamento fuori tabella!\n" + aiobe.getMessage() + "\n stack: " + sb, aiobe);
        } catch (NullPointerException npe) {
            StackTraceElement[] stackTrace = npe.getStackTrace();
            throw new CommandException(FOR_NAME + "Si e\' verificato un puntamento a null in " + this.getClass().getName() + ": " + npe.getMessage() + "\n stack: " + stackTrace[0], npe);            
        } catch (Exception e) {
            StackTraceElement[] stackTrace = e.getStackTrace();
            throw new CommandException(FOR_NAME + "Eccezione generica da " + this.getClass().getName() + ": " + e.getMessage() + "\n stack: " + stackTrace[0], e);
        }
    }
    
    
    /**
     * <p>Ottiene la lista dei "doppi singoli" ("doppioni puliti") 
     * ovvero di una singola copia delle righe doppie, non di entrambe
     * (e men che meno pi&uacute;, come avviene nel caso di alcuni laboratori 
     * o AD particolari come "Inglese Scientifico" che non hanno un solo
     * doppione ma quattro o cinque).</p>
     * <p>Prende in input la lista dei duplicati logici, passata come
     * argomento, e suddivide tale lista delle AD duplicate approssimativamente  
     * in due, individuando le chiavi duplicate - cio&egrave; in pratica 
     * chiavi che hanno due o più occorrenze nella lista delle AD.</p>
     * <p>Restituisce una lista di elementi che hanno un valore doppio.
     * I duplicati, per definizione, vanno a coppie (almeno), per cui, volendo
     * eliminarli dall'elenco delle tuple "grezze", non vanno eliminati a coppie, 
     * ma solo singolarmente.<br />
     * P.es., se avessi il seguente insieme &nbsp;<code>{3, 4, 4, 7}</code>
     * volendo eliminare i duplicati dovrei eliminare <strong>un solo</strong> 4, 
     * non tutti e due; ma, nel vettore dei duplicati, attualmente ci sono 
     * tutti i duplicati (tranne i duplicati identici, naturalmente, cio&egrave;
     * tuple identiche in tutto e per tutto, anche nell'id), quindi tutti i 
     * duplicati logici - ovvero le tuple aventi la stessa chiave ma diverso id -
     * e non solo l'elenco distinto degli elementi soggetti a duplicazione.
     * Per questo motivo, il presente metodo seleziona, secondo un criterio
     * ragionato, solo la met&agrave;, o poco meno, dei duplicati 
     * in modo da poterli togliere dall'elenco completo.</p> 
     * 
     * @param duplicates lista dei duplicati logici
     * @return <code>List&lt;CourseBean&gt;</code> - ArrayList di AD campione che hanno almeno un duplicato  
     * @throws CommandException se si verifica un problema in un puntamento fuori tabella o nell'accesso a qualche elemento
     */
    private static List<CourseBean> splitDuplicates(List<CourseBean> duplicates)
                                             throws CommandException {
        // Calcola la lunghezza della lista di duplicati logici
        int vSize = duplicates.size();
        // Dichiara la lista "pulita" di elementi che hanno almeno un duplicato logico
        List<CourseBean> samplesDuplicates = new ArrayList<CourseBean>();
        try {
            // Contatori
            int i = 0;
            int j = 0;
            // Il primo va aggiunto a mano, altrimenti, dato l'algoritmo, non entrerebbe mai nei duplicati puliti...
            samplesDuplicates.add(duplicates.get(i));
            // Cicla tutti i duplicati logici
            while (i < vSize) {
                // Indice del successivo
                j = i + 1;
                // Punta al record corrente
                CourseBean current = duplicates.get(i);
                // Recupera la chiave
                String key = current.getKey();
                // Controllo per evitare puntamento fuori tabella
                if (j < vSize) {
                    // Punta al record successivo e confronta la chiave col record successivo
                    // Finché la chiave non cambia...
                    while (j < vSize && key.equals(duplicates.get(j).getKey())) {
                        // ...incrementa l'indice
                        if (j < vSize) {
                            j++;
                        }
                    }
                    // Controllo per evitare puntamento fuori tabella
                    if (j < vSize) {
                        // Nel momento in cui la chiave cambia, tiene il record
                        String candidateKey = duplicates.get(j).getKey();
                        // Flag specificante se il record corrente è già stato salvato nella lista delle AD che hanno duplicazioni
                        boolean alreadyYet = false;
                        // A meno che non sia già presente nel Vector (could be...)
                        for (int k = 0; k < samplesDuplicates.size(); k++) {
                            CourseBean controllo = samplesDuplicates.get(k);
                            // Recupera la chiave
                            String controlKey = controllo.getKey();
                            // Se le due chiavi sono uguali... 
                            if (controlKey.equals(candidateKey)) {
                                // ...vuol dire che il record era già stato salvato nella lista delle AD campione che hanno duplicazioni 
                                alreadyYet = true;
                                // Esce dal ciclo perché non c'è bisogno di sapere altro
                                break;
                            }
                        }
                        // Se non è già presente nei record salvati...
                        if (!alreadyYet) {
                            // ...lo aggiunge alla lista delle AD campione che hanno duplicazioni
                            samplesDuplicates.add(duplicates.get(j));
                        }
                    }
                } 
                i++;
            }
            return samplesDuplicates;
        } catch (ClassCastException cce) {
            throw new CommandException(FOR_NAME + cce);
        } catch (ArrayIndexOutOfBoundsException aiobe) {
            throw new CommandException(FOR_NAME + "Si e\' verificato un problema nell'indice di un puntamento in una lista di AD duplicate.\n", aiobe);
        } catch (NullPointerException npe) {
            throw new CommandException(FOR_NAME + "Si e\' verificato un problema nel puntamento a un oggetto.\n", npe);
        } catch (Exception e) {
            throw new CommandException(FOR_NAME + "Si e\' verificato un problema.\n",e);
        }
    }
    
    
    /**
     * <p>Prende in input un lista di AD che, nell'estrazione grezza, presentano
     * uno o pi&uacute; duplicati logici tra cui vi sono alcune AD aventi
     * la stessa chiave-base ma diverso periodo.<br />
     * Tenta di unire i periodi dove possibile, ovvero dove il doppione 
     * &egrave; persistito nella lista dei "doppioni puliti", 
     * ottenendo cos&iacute; una lista di AD pulite 
     * e tutte distinte anche per chiave-base, che dovr&agrave;
     * essere poi unita (in gergo informatichese: joinata, mergiata...)
     * alla lista delle AD da cui sono stati tolti i duplicati, per ottenere
     * l'elenco pulito delle AD.</p>  
     * 
     * @param duplicates
     * @return
     * @throws CommandException
     */
    private static List<CourseBean> mergeDates(List<CourseBean> samplesDuplicates)
            throws CommandException {
        // Calcola la lunghezza della lista di campioni di duplicati logici
        int vSize = samplesDuplicates.size();
        // Dichiara la lista "grezza" di elementi che hanno la stessa chiave-base ma diverso periodo di erogazione
        List<CourseBean> baseDuplicates = new ArrayList<CourseBean>();
        /* Trasforma la lista in una HashMap per poter utilizzare i metodi di ricerca ottimizzati
        HashMap<String, CourseBean> samplesDuplicatesAsHashMap = new HashMap<String, CourseBean>();
        try {
            // Ciclo forEach (Java 1.5+)            
            for (CourseBean ad : samplesDuplicates) {
                //String key = ad.getKey();
                String smallKey = ad.getSmallKey();
                samplesDuplicatesAsHashMap.put(smallKey, ad);
            }
            // Unisce i periodi dove possibile, ovvero dove il doppione è persistito nella lista dei "doppioni puliti"
            int size = samplesDuplicatesAsHashMap.size();*/
        // Cicla
        try {
            // Contatori
            int i = 0;
            int j = 0;
            // Cicla tutti i duplicati logici
            while (i < vSize) {
                // Indice del successivo
                j = i + 1;
                // Punta al record corrente
                CourseBean current = samplesDuplicates.get(i);
                // Per definizione di questa List le chiavi complete non possono essere uguali, quindi recupera la chiave-base
                String smallKey = current.getSmallKey();
                /*
                // Controllo per evitare puntamento fuori tabella
                if (j < vSize) {
                    // Punta al record successivo e confronta la chiave col record successivo
                    // Finché la chiave-base è diversa...
                    while (j < vSize && !smallKey.equals(samplesDuplicates.get(j).getSmallKey())) {
                        // ...incrementa l'indice
                        if (j < vSize) {
                            j++;
                        }
                    }
                    // Controllo per evitare puntamento fuori tabella
                    if (j < vSize) {
                        // Nel momento in cui le chiavi-base sono uguali, tiene il record perché gli interessa
                        baseDuplicates.add(samplesDuplicates.get(j));
                    }
                }*/
                for (CourseBean ad : samplesDuplicates) {
                    // Controlla che le occorrenze abbiano la stessa chiave-base ma diverso id 
                    // (AD con la stessa chiave-base e lo stesso id sono la stessa AD!)
                    if (ad.getSmallKey().equals(current.getSmallKey()) && ad.getId() != current.getId()) {
                     // Nel momento in cui le chiavi-base sono uguali (e gli id diversi), tiene il record perché gli interessa
                        baseDuplicates.add(ad);
                    }
                }
                i++;
            }
        // confronta la smallkey
        // se le smallkey sono uguali, cerca la data minore, la data maggiore, e genera un unico periodo che è la fusione dei due
        // setta il nuovo periodo in un bean avente la stessa smallkey ma ovviamente a questo punto differente key
        // rimuove dal vector i 2 oggetti con i due periodi parziali e ci aggiunge il vector con il periodo risultante dai due
            
            // Toglie dalla lista grezza delle AD semplici (semilavorato) tutti i doppi indistintamente
            // Riaggiunge alla lista depurata di tutti i doppi la lista dei "doppi singoli" lavorati, ovvero con i periodi uniti
            // Questa è la lista finale pulita delle AD semplici, su cui si può implementare l'ulteriore algoritmo di scelta delle AD Semplici da valutare

        } catch (AttributoNonValorizzatoException anve) {
            throw new CommandException(anve);
        } catch (ArrayIndexOutOfBoundsException aiobe) {
            throw new CommandException(aiobe);
        } catch (NullPointerException npe) {
            throw new CommandException(npe);
        } catch (Exception e) {
            throw new CommandException(e);
        }
        return baseDuplicates;
    }

    
    /**
     * <p>Trasforma il Vector in una HashMap.</p>
     * <p>Rimuove dal set dei risultati (elenco completo) gli elementi
     * che sono duplicati. P.es., se l'insieme dei risultati fosse il seguente:
     * {3, 4, 4, 7} dovremmo ottenere: {3, 4, 7}.</p> 
     * 
     * @param v
     * @param duplicates
     * @return
     * @throws CommandException
     */
    private static HashMap<String, CourseBean> removeDuplicates(Vector<CourseBean> v, 
                                                                ArrayList<CourseBean> duplicates)
                                                         throws CommandException {
        HashMap<String, CourseBean> vH = new HashMap<String, CourseBean>();
        try {
            // Ciclo forEach (Java 1.5+)            
            for (CourseBean ad : v) {
                String key = ad.getKey();
                vH.put(key, ad);
            }
            // Cicla nuovamente il Vector, per individuare i doppioni
            for (CourseBean doublet : duplicates) {
                String key = doublet.getKey();
                if (vH.containsKey(key)) {
                    vH.remove(key);
                }
            }
            // 
            // Ottiene la lista dei "doppi singoli" ("doppioni puliti") ovvero di una singola copia delle righe doppie, non di entrambe
            // Unisce i periodi dove possibile, ovvero dove il doppione è persistito nella lista dei "doppioni puliti"
            // Toglie dalla lista grezza delle AD semplici (semilavorato) tutti i doppi indistintamente
            // Riaggiunge alla lista depurata di tutti i doppi la lista dei "doppi singoli" lavorati, ovvero con i periodi uniti
            // Questa è la lista finale pulita delle AD semplici, su cui si può implementare l'ulteriore algoritmo di scelta delle AD Semplici da valutare
        } catch (AttributoNonValorizzatoException anve) {
            throw new CommandException(anve);
        } catch (ArrayIndexOutOfBoundsException aiobe) {
            throw new CommandException(aiobe);
        } catch (NullPointerException npe) {
            throw new CommandException(npe);
        } catch (Exception e) {
            throw new CommandException(e);
        }
        return vH;
    }
    
    private List<CourseBean> purge(Vector<CourseBean> v) {
        List<CourseBean> a = new ArrayList<CourseBean>(v);
        return a;
    }
    
    private ArrayList<CourseBean> clean(Vector<CourseBean> v) {
        List<CourseBean> simpleDA = new ArrayList<CourseBean>(v);
        
        // Ripulisce la lista delle AD Semplici dai duplicati
        
        // Controlla se è un corso di area medica
        //if (ad.getCodiceCdSUGOV().startsWith("M")) {
            /* *******************************
             *    Ramo Corsi di area medica
             * *******************************/
            // Se NON è un corso elettivo
        //    if (!corsiElettivi.contains(ad.getCodiceADUGOV())) {
                // Se NON è un corso elettivo ha senso continuare...
                //
          //  }
        /*}
        
        else {
            /* *******************************
             *  Ramo Corsi di area non medica
             * *******************************/
            //
        //}
            // Se no, controlla se i CFU totali sono >= 4
                // Controlla che i CFU di tipo LEZ siano > 0
        
        // Se sì controlla che non sia un corso elettivo
            // Controlla che i CFU di tipo LEZ siano > 0
        
        // Sul risultato trovato cerca i doppioni
            // Se è un doppione cerca di accorpare i periodi
        /*
        // Controlla che i CFU totali siano pià di 3
        if (ad.getCreditiTotali() < 4) {
            // Se è un corso di area medica va considerato, altrimenti va scartato direttamente
            if (ad.getCodiceCdSUGOV())
        }
        // Controlla che ci siano ore di lezione
        //        if (ad.getOreLezione() > 0) {
            
        //        }
        }*/
        return null;
    }
    
    
    private void sortByKey(List<CourseBean> l) 
            throws CommandException {
        Collections.sort(l, new Sortbykey());
        //return null;
    }
    
    
    private void sortByCode(List<CourseBean> l) 
            throws CommandException {
        Collections.sort(l, new Sortbycode());
        //return null;
    }
    
    
    protected class Sortbykey  implements Comparator<CourseBean> 
    { 
        // Used for sorting in ascending order of course key 
        @Override
        public int compare(CourseBean o1, CourseBean o2) {
            String std = null;
            if (o1 == null || o2 == null)
                return 1;
            try {
                std = o1.getKey();
            } catch (AttributoNonValorizzatoException anve) {
                // Auto-generated method stub
                return 0;
            }
            try {
                return std.compareTo(o2.getKey());
            } catch (AttributoNonValorizzatoException anve) {
                return 1;
            }
        } 
    } 
    
    
    protected class Sortbycode  implements Comparator<CourseBean> 
    { 
        // Used for sorting in ascending order of course key 
        @Override
        public int compare(CourseBean o1, CourseBean o2) {
            String std = null;
            if (o1 == null || o2 == null)
                return 1;
            try {
                std = o1.getCodiceFiscaleDocente() + 
                      o1.getCodiceADUGOV() + 
                      o1.getCodiceCdSUGOV() + 
                      o1.getInizioPerDid() + 
                      o1.getFinePerDid();
            } catch (NullPointerException npe) {
                // Auto-generated method stub
                return 0;
            }
            try {
                return std.compareTo(o2.getCodiceFiscaleDocente() + o2.getCodiceADUGOV() + o2.getCodiceCdSUGOV() + o1.getInizioPerDid() + o1.getFinePerDid());
            } catch (NullPointerException npe) {
                return 1;
            }
        } 
    } 
    
}