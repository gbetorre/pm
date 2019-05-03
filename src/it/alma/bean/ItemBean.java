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
 *   Copyright (C) renewed 2019 Universita' degli Studi di Verona, 
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

package it.alma.bean;


/**
 * <p>Classe che serve a rappresentare oggetti generici (p.es. link in un menu,
 * righe di un log, etc.)</p>
 * 
 * @author <a href="mailto:giovanroberto.torre@univr.it">Giovanroberto Torre</a>
 */
public class ItemBean implements Comparable<ItemBean> {
    /** 
     * Suffisso per le Command                                     
     */
    static final String COMMAND_SUFFIX = "Command";
    /** 
     * Identificativo della voce                                   
     */
    private int id;
    /** 
     * Nome della voce                                             
     */
    private String nome;
    /** 
     * Nome reale della voce, in caso di corrispondenze logiche    
     */
    private String nomeReale;
    /** 
     * Etichetta da visualizzare per la voce                       
     */
    private String labelWeb;
    /** 
     * Attributo che pu&ograve; essere usato per memorizzare
     * il nome di una entit&agrave; associata alla voce
     * oppure altre informazioni attinenti 
     */
    private String nomeClasse;
    /**
     * Attributo che pu&ograve; essere usato per memorizzare
     * il valore della pagina associata ad una classe referenziata
     * dalla voce di menu, oppure altre informazioni attinenti
     */
    private String paginaJsp;
    /** 
     * Attributo per memorizzare il link sotteso all'etichetta     
     */
    private String url;
    /** 
     * Attributo che pu&ograve; contenere informazioni descrittive 
     */
    private String informativa;
    /**
     * Attributo per memorizzare il nome di un'immagine,
     * o un attributo di stile utile a far apparire un'immagine,
     * a corredo della voce di menu
     */
    private String icona;
    /**
     * Attributo per la definizione del livello di 
     * indentazione della voce nel menu o per contenere altre informazioni
     * di contesto
     */
    private int livello;
    /**
     * Attributo che serve a definire se l'url della voce di menu
     * punta a un'applicazione web istituzionale oppure a un sito esterno
     */
    private boolean urlInterno;
    /**
     * Attributo per definire altre informazioni contestuali, p.es. il fatto
     * che il link richiede un'autenticazione a monte del <cite>landing</cite>
     * o il fatto che l'elemento non &egrave; pubblico
     */
    private boolean privato;
    /**
     * Attributo che serve a memorizzare ulteriori informazioni,
     * utile e.g. quando questo oggetto viene usato per 
     * incapsulare valori che necessitano di molte informazioni aggiuntive
     */
    private String extraInfo;
    
    
    /**
     * <p>Override Costruttore di Default</p>
     * <p>
     * Inizializza le variabili di classe a valori convenzionali</p>
     */
    public ItemBean() {
        id = -2;
        nome = nomeReale = labelWeb = nomeClasse = paginaJsp = url = informativa = icona = null;
        livello = 0;
        urlInterno = true;
        privato = false;
        extraInfo = null;
    }
    
    
    /**
     * <p>Costruttore da ItemBean</p>
     * <p>
     * Inizializza le variabili di classe a valori presi da
     * un ItemBean passato come argomento</p>
     *  
     * @param old il ItemBean di cui si vogliono recuperare i valori
     */
    public ItemBean(final ItemBean old) {
        this.id = old.getId();
        this.nome = old.getNome();
        this.nomeReale = old.getNomeReale();
        this.labelWeb = old.getLabelWeb();
        this.nomeClasse = old.getNomeClasse();
        this.paginaJsp = old.getPaginaJsp();
        this.url = old.getUrl();
        this.informativa = old.getInformativa();
        this.icona = old.getIcona();
        this.livello = old.getLivello();
        this.urlInterno = true;
    }
    
    
    /**
     * <p>Costruttore parametrizzato</p>
     * <p>
     * ItemBean(String nome, String labelWeb, String url, int livello)</p>
     * 
     * @param nome      nome della voce da creare; puo' corrispondere al token
     * @param labelWeb  etichetta da mostrare per l'url della voce da creare
     * @param url       URL della voce da creare
     * @param livello   livello di indentazione della voce da creare
     */
    public ItemBean(String nome, String labelWeb, String url, int livello) {
        this.nome = nome;
        this.labelWeb = labelWeb;
        this.url = url;
        this.livello = livello;
        this.urlInterno = true;
    }
    
    
    /**
     * <p>Costruttore parametrizzato</p>
     * <p>
     * ItemBean(String nome, String labelWeb, String url, String informativa, int livello)
     * </p>
     * 
     * @param nome          nome della voce da creare; puo' corrispondere al token
     * @param labelWeb      etichetta da mostrare per l'url della voce da creare
     * @param url           URL della voce da creare
     * @param informativa   una descrizione della voce da creare
     * @param livello       livello di indentazione della voce da creare
     */
    public ItemBean(String nome, String labelWeb, String url, String informativa, int livello) {
        this.nome = nome;
        this.labelWeb = labelWeb;
        this.url = url;
        this.informativa = informativa;
        this.livello = livello;
        this.urlInterno = true;
    }
    
    
    /**
     * <p>Costruttore parametrizzato</p>
     * <p>
     * ItemBean(String nome, String labelWeb, String url, String informativa, String icona, int livello)
     * </p>
     * 
     * @param nome          nome della voce da creare; puo' corrispondere al token
     * @param labelWeb      etichetta da mostrare per l'url della voce da creare
     * @param url           URL della voce da creare
     * @param informativa   una descrizione della voce da creare
     * @param icona         immagine da mostrare a corredo della voce da creare
     * @param livello       livello di indentazione della voce da creare
     */
    public ItemBean(String nome, String labelWeb, String url, String informativa, String icona, int livello) {
        this.nome = nome;
        this.labelWeb = labelWeb;
        this.url = url;
        this.informativa = informativa;
        this.icona = icona;
        this.livello = livello;
        this.urlInterno = true;
    }
    
    
    /**
     * <p>Costruttore parametrizzato</p>
     * <p>
     * ItemBean(String nome, String labelWeb, String url, String informativa, String icona, int livello)
     * </p>
     * 
     * @param nome          nome della voce da creare; puo' corrispondere al token
     * @param nomeReale     nome che dovrebbe essere 'reale' nel senso che non e' il nome di un altro elemento ma proprio dell'attributo 'nome' nella tabella delle voci di menu 
     * @param labelWeb      etichetta da mostrare per l'url della voce da creare
     * @param url           URL della voce da creare
     * @param informativa   una descrizione della voce da creare
     * @param icona         immagine da mostrare a corredo della voce da creare
     * @param livello       livello di indentazione della voce da creare
     */
    public ItemBean(String nome, String nomeReale, String labelWeb, String url, String informativa, String icona, int livello) {
        this.nome = nome;
        this.nomeReale = nomeReale;
        this.labelWeb = labelWeb;
        this.url = url;
        this.informativa = informativa;
        this.icona = icona;
        this.livello = livello;
        this.urlInterno = true;
    }
    
    
    /**
     * <p>Compara due oggetti ItemBean basandosi sull'id</p>
     * <p>
     * Questo permette di effettuare comparazioni direttamente 
     * tra oggetti ItemBean, funzionali anche ad ordinamenti, 
     * come nell'esempio:
     * <pre>
     * // Dichiara le voci del tempo
     * List&lt;ItemBean&gt; theVoicesOfTime = null;
     * // Recupera le voci del tempo
     * theVoicesOfTime = db.getTheVoicesOfTime();
     * // Fa l'ordinamento per id voce, come specificato nel metodo compareTo()
     * Collections.sort(theVoicesOfTime);
     * </pre></p>
     */
    @Override
    public int compareTo(ItemBean o) {
        if (this.id == o.getId()) return 0;
        else if (this.id < o.getId()) return -1;
        else return 1;
    }
    
    /* ************************************************************************ *  
     *                          Accessori e Mutatori                            *
     * ************************************************************************ */
    
    /**
     * @return <code>id</code> - l'identificativo della voce di menu
     */
    public int getId() {
        return this.id;
    }
    
    
    /**
     * @param i un intero rappresentante l'identificativo della voce di menu da impostare
     */
    public void setId(int i) {
        id = i;
    }
    
    
    /**
     * @return <code>nome</code> - il token usato per individuare questa entità nella query string
     */
    public String getNome() {
        return this.nome;
    }
    
    
    /**
     * @param v un oggetto String usato per impostare nella voce di menu il valore di 'ent'
     */
    public void setNome(String v) {
        this.nome = v;
    }
    
    
    /**
     * @return <code>nomeReale</code> - un oggetto String usato per impostare il nome della voce di menu 
     */
    public String getNomeReale() {
        return this.nomeReale;
    }
    
    
    /**
     * @param v un oggetto String usato per impostare il nome della voce di menu 
     */
    public void setNomeReale(String v) {
        this.nomeReale = v;
    }
    
    
    /**
     * @return <code>labelWeb</code> - etichetta da mostrare nel menu, per rappresentare la voce
     */
    public String getLabelWeb() {
        return this.labelWeb;
    }
    
    
    /**
     * @param v un oggetto String contenente l'etichetta per la voce di menu
     */
    public void setLabelWeb(String v) {
        this.labelWeb = v;
    }
    
    
    /**
     * @return <code>nomeClasse</code> - un oggetto String contenente tradizionalmente il nome della Command associata alla voce di menu
     */
    public String getNomeClasse() {
        return this.nomeClasse + COMMAND_SUFFIX;
    }
    
    
    /**
     * @param v un oggetto String usato tradizionalmente per impostare il nome della Command associata alla voce di menu
     */
    public void setNomeClasse(String v) {
        this.nomeClasse = v;
    }
    
    
    /**
     * @return <code>paginaJsp</code> - oggetto String usato tradizionalmente per memorizzare il nome della pagina jsp associata alla it.univr.di.uol.command.Command {@link #getNomeClasse()}
     */
    public String getPaginaJsp() {
        return this.paginaJsp;
    }
    
    
    /**
     * @param v oggetto String usato tradizionalmente per impostare il nome della pagina jsp associata alla it.univr.di.uol.command.Command {@link #getNomeClasse()}
     */
    public void setPaginaJsp(String v) {
        this.paginaJsp = v;
    }
    
    
    /**
     * @return <code>url</code> - oggetto String contenente il link da sottendere alla voce di menu
     */
    public String getUrl() {
        return this.url;
    }
    
    
    /**
     * @param v oggetto String per memorizzare l'url della voce di menu
     */
    public void setUrl(String v) {
        this.url = v;
    }
    
    
    /**
     * @return <code>informativa</code> - un campo note descrittivo
     */
    public String getInformativa() {
        return this.informativa;
    }
    
    
    /**
     * @param string un oggetto String contenente informazioni da impostare
     */
    public void setInformativa(String string) {
        informativa = string;
    }
    
        
    /**
     * @return <code>icona</code> - il nome di un'immagine, o un attributo di stile utile a far apparire un'immagine, a corredo della voce di menu
     */
    public String getIcona() {
        return this.icona;
    }
    
    
    /**
     * @param string il nome di un'immagine, o un attributo di stile utile a far apparire un'immagine, a corredo della voce di menu da impostare
     */
    public void setIcona(String string) {
        icona = string;
    }
    
    
    /**
     * <p>Restituisce un intero rappresentante ad esempio 
     * il livello di indentazione della voce del menu:
     * <dl>
     * <dt>0</dt>
     * <dd>primo livello, ovvero voci presentate di default,
     * navigazione di default o alberatura piatta</dd>
     * <dt>1</dt>
     * <dd>voci ad un livello di indentazione sotto al 
     * livello zero</dd>
     * <dt>2</dt>
     * <dd>voci figlie delle voci di livello 1</dd>
     * <dt>...</dt>
     * <dd>la serie potrebbe continuare</dd>
     * </dl>
     * La serie di indentazioni potrebbe proseguire indefinitamente
     * (in teoria, fino alla massima capienza di un intero in Java,
     * che &egrave; pari a <pre>[2<sup>31</sup>-1]</pre>). 
     * Tuttavia, per ragioni di ordine pratico (di praticit&agrave;
     * di navigazione) nella maggioranza dei casi esistono solo 
     * voci di livello zero e 1, in taluni casi di livello 2.
     * </p> 
     * @return </code>livello</code> - un intero rappresentante il livello della voce di menu 
     */
    public int getLivello() {
        return this.livello;
    }
    
    
    /**
     * @param livello un intero per impostare il livello della voce
     */
    public void setLivello(int livello) {
        this.livello = livello;
    }
    
    
    /**
     * Metodo per sapere se l'url specificato &egrave; interno o meno.
     *
     * @return true se l'url &egrave; interno, false altrimenti.
     */
    public boolean isUrlInterno() {
        return urlInterno;
    }    
    
    
    /**
     * Metodo per impostare se l'url &egrave; interno o meno.
     *
     * @param v valore da assegnare alla variabile
     */
    public void setUrlInterno(boolean v) {
        urlInterno = v;
    }

    
    /**
     * Restituisce il valore dell'attributo specificante se l'item 
     * deve essere considerato 'privato'
     * 
     * @return boolean - true se l'elemento e' privato, false altrimenti
     */
    public boolean isPrivato() {
        return privato;
    }

    
    /**
     * Imposta il valore dell'attributo specificante se l'item
     * deve essere considerato 'privato'
     * 
     * @param privato flag boolean true se l'elemento e' privato, false altrimenti
     */
    public void setPrivato(boolean privato) {
        this.privato = privato;
    }
    

    /**
     * @return <code>String</code> extraInfo - un oggetto String contenente eventuali informazioni aggiuntive
     */
    public String getExtraInfo() {
        return extraInfo;
    }


    /**
     * @param extraInfo le eventuali informazioni extra da impostare
     */
    public void setExtraInfo(String extraInfo) {
        this.extraInfo = extraInfo;
    }
    
}