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

package it.alma.bean;

import it.alma.exception.AttributoNonValorizzatoException;

import java.io.Serializable;
import java.util.Date;
import java.util.Vector;


/**
 * <p>Classe per rappresentare occorrenze di insegnamento, anche dette,
 * in terminologia Reporting, <cite>&quot;Attivit&agrave; Didattiche&quot;</cite>.</p>
 * Le &quot;Attivit&agrave; Didattiche&quot; (o AD) possono presentarsi sotto
 * molteplici forme ed avere svariate tipologie, come per esempio:
 * <ul>
 *  <li>AD Semplici</li>
 *  <li>AD con Moduli</li>
     <ul>
 *    <li>Insegnamento Padre</li>
 *    <li>Modulo (Insegnamento Figlio)</li>
 *   </ul>
 *  <li>AD con UL (Unit&agrave; Logistiche)</li>
 *  <li>Mutuazioni</li>
 *   <ul>
 *    <li>Reale</li>
 *    <li>Fantasma</li>
 *   </ul>
 *  <li>Repliche</li> 
 * </ul>
 * Le Repliche e le Unit&agrave; Logistiche in Esse3 vanno sotto il nome di
 * <cite>Partizionamenti</cite>.
 * 
 * @author <a href="mailto:giovanroberto.torre@univr.it">Giovanroberto Torre</a>
 */
public class CourseBean implements Serializable {
    
    /**
     * La serializzazione necessita di dichiarare una costante di tipo long
     * identificativa della versione seriale. 
     * (Se questo dato non fosse inserito, verrebbe calcolato in maniera automatica
     * dalla JVM, e questo potrebbe portare a errori riguardo alla serializzazione). 
     */
    private static final long serialVersionUID = 1L;
    /**
     *  Nome di questa classe. 
     *  Viene utilizzato per contestualizzare i messaggi di errore.
     */
    private final String FOR_NAME = "\n" + this.getClass().getName() + ": "; //$NON-NLS-1$
    /* ************************************************************************ *  
     *               Attributi da tracciato record estrazione WI                *
     * ************************************************************************ */
    /** Contatore (row.names) */
 	private int id; 
 	/** Codice Corso di Studi di WebIntegrato (WI) */
 	private String codiceCdSWI;
 	/** Codice Corso di Studi di UGOV */
 	private String codiceCdSUGOV;
 	/** Nome Corso di Studi di WI */
 	private String nomeCdSWI;
 	/** Nome Corso di Studi di UGOV */
 	private String nomeCdSUGOV;
 	/** Nome struttura didattica associata */
 	private String DipartimentoScuola;
 	/** Sede di WebIntegrato */
 	private String SedePrimariaCDS;
 	/** Codice Attivit&agrave; Didattica di WebIntegrato */
 	private int codiceADWI;
 	/** Codice Attivit&agrave; Didattica di UGOV */
 	private String codiceADUGOV;
 	/** Nome Attività Didattica (AD) */
 	private String nomeInsegnamento;
    /** Settore Scientifico Disciplinare (SSD) */
    private String SSD;
    /** Docente Attivit&agrave; Didattica */
    private PersonBean docente;
 	/** Flag Modulo */
    private int modulo;
    /** Flag Unit&agrave; Logistica */
    private boolean ul;
    /** Eventuale discriminante modulo/partizionamento */
    private String discriminante;
    /** Crediti totali (CFU) */
    private float creditiTotali;
    /** Crediti di lezione (LEZ) */
    private float creditiLezione;
    /** Crediti di laboratorio (LAB) */
    private float creditiLaboratorio; 
    /** Crediti di altro tipo (ALT) */
    private float creditiAltro;
    /** Ore totali */
    private String ore;
    /** Frazione ore di lezione (LEZ) */
    private String oreLezione;
    /** Frazione ore di laboratorio (LAB) */
    private String oreLaboratorio;
    /** Frazione ore di altro (ALT) */
    private String oreAltro;
    /** Frazione ore di seminario (SEM) */
    private String oreSeminario;
    /** Frazione ore di esercitazione (ESE) */
    private String oreEsercitazione;
    /** Frazione ore di tirocinio (TIR) */
    private String oreTirocinio;
    /** Data inizio periodo didattico */
    private String inizioPerDid;
    /** Data fine periodo didattico */
    private String finePerDid;
    /* ************************************************************************ *  
     *                      Attributi utili da codice legacy                    *
     * ************************************************************************ */
    /** Anno Accademico in esame */
    private String annoAccademico;
    /** Flag identificante la modulazione dell'insegnamento cio&egrave;
     *  identificante il fatto che l'insegnamento corrente &egrave; padre */
    private boolean divisoInModuli;
	/** Nome modulo in caso l'AD corrente sia un Modulo di un Padre*/
	private String nomeModulo;
	/** Crediti del Modulo */
	private float crediti;
	/** Crediti del Padre */
	private float creditiPadre;
    /** Discriminante per crediti diversi */
    private String discrPerCreditiDiversi;
    /** Discriminante Modulo in caso di Modulo */
    private String discriminanteModulo;
    /** Flag identificante il fatto che l'AD &egrave; suddivisa in Unit&agrave; Logistiche */
    private boolean divisoInUnita;
    /** Nome unit&agrave; logistica in caso l'AD corrente si un'Unit&agrave; Logistica di un Padre */
    private String nomeUL;
    /** Identificativo del Padre nel caso in cui l'AD corrente si un'Unit&agrave; Logistica di un Padre */
    private int idPadreUL;
    /** Reale dell'insegnamento, in caso l'AD corrente sia un Fantasma */
    private CourseBean mutuante;
    /** ID del reale dell'insegnamento, in caso il corrente sia un fantasma */
    private int idMutuante;
    /** Flag identificante il fatto che l'AD corrente sia un Reale di un Fantasma */
    private boolean mutuato;
    /** Flag replica */
    private char replica;
    /** Eventuale URL a una pagina pubblica dell'insegnamento */
	private String url;
	/** Flag identificante il fatto che l'AD corrente ha ore di lezione (LEZ) */
    private boolean conLezioni;
	/** Elenco di periodi in cui l'insegnamento &egrave; erogato */
	private Vector<CodeBean> periodi;
	/** Inizio del periodo di lezioni */
	private Date inizioPeriodo;
	/** Fine del periodo di lezioni */
	private Date finePeriodo;
	/** Lingua in cui l'ìnsegnamento &egrave; erogato */
    private String linguaErog;
	/** Lista di docenti collegati all'Attivit&agrave; Didattica */
	private Vector<PersonBean> docenti;
	/** Misura in cui &egrave; espresso il peso dell'insegnamento (p.es. 'CFU' o 'Ore') */ 
	private String misura;
	/** Vettore di corsi di studio a cui l'insegnamento &egrave; collegato */
	private Vector<DegreeBean> corsiStudi;
	/** Sede didattica ove &egrave; erogato l'insegnamento */
	private String sede;
	/** Provincia sede didattica */
	private String provinciaSede;
	/** Obiettivi Formativi / Sbocchi professionali */
	private String obiettiviFormativi;
	/** Lingua in cui sono inserite le spiegazioni inerenti gli obiettivi formativi */
	private String linguaObiettiviFormativi;
	/** Programma */
	private String programma;
	/** Lingua in cui sono inserite le informazioni inerenti al programma d'esame */
	private String linguaProgramma;
	/** Modalit&agrave; di esame */
    private String modalitaEsame;
    /** Lingua in cui sono inserite le spiegazioni inerenti le modalit&agrave: di esame */
    private String linguaModalitaEsame;
    /** Flag identificante il Percorso Comune ai vari curricula */
	private boolean comuneIndirizzi;

	
    /**
     * Costruttore.
     * Inizializza i campi a valori convenzionali di default.
     */
	public CourseBean() {
		id = modulo = idMutuante = idPadreUL = -2;
		crediti = creditiPadre = -2;
		replica = ' ';
		annoAccademico = nomeInsegnamento = discriminante = url = null;
		misura = null;
		nomeModulo = discriminanteModulo = discrPerCreditiDiversi = nomeUL = null;
		periodi = null;
		docenti = null;
		corsiStudi = null;
		divisoInModuli = mutuato = conLezioni = comuneIndirizzi = divisoInUnita = false;
		mutuante = null;
		obiettiviFormativi = linguaObiettiviFormativi = null;
		programma = linguaProgramma = null;
        modalitaEsame = linguaModalitaEsame = null;
		inizioPeriodo = finePeriodo = new Date(0);
		sede = provinciaSede = null;
        linguaErog = null;
	}

	
	/**
	 * Restituisce il nome dell'insegnamento completo di discriminante, 
	 * nome modulo e discriminante modulo nel formato 
	 * "insegnamento [<em>discriminante</em>] - modulo [<em>discriminanteModulo</em>]". 
	 * Se un attributo &egrave; stringa vuota, allora non viene stampato. 
	 * Se &egrave; null, viene lanciata un'eccezione di attributo mancante. 
	 * La stringa risultante &egrave; una stringa HTML. 
	 * Questo metodo DEVE essere il metodo di riferimento 
	 * per stampare il nome completo in tutti gli ambiti dell'applicazione Alma.
	 * 
	 * @param insegnamento nome dell'insegnamento. Non può essere NULL. Esempio: 'Algoritmi'
	 * @param discriminante discriminante per il nome dell'insegnamento. Non può essere NULL. Esempio: 'A-L'
	 * @param modulo nome del modulo. Non può essere NULL. Esempio: 'Teoria'
	 * @param discriminanteModulo discriminante per il nome del modulo. Non pu&ograve; essere NULL. Esempio: 'matricole pari'
	 * @return il nome dell'insegnamento completo di discriminante, nome modulo e discriminante modulo in formato HTML.
	 * @throws AttributoNonValorizzatoException se uno dei parametri è NULL.
	 */
	static public String getNomeInsegnamentoCompleto(String insegnamento, 
	                                                 String discriminante, 
	                                                 String modulo,
	                                                 String discriminanteModulo) 
	                                          throws AttributoNonValorizzatoException {
		// Verifico che i parametri siano corretti!
		String attributiErrati = "";
		if (insegnamento == null)
			attributiErrati = "insegnamento";
		if (discriminante == null)
			attributiErrati += ", discriminante";
		if (modulo == null)
			attributiErrati += ", modulo";
		if (discriminanteModulo == null)
			attributiErrati += ", discriminanteModulo.";
		if (!attributiErrati.equals(""))
			throw new AttributoNonValorizzatoException("CourseBean: attributi '" + attributiErrati
					+ "' non sono valorizzati!");
		// Costruisco il nome!
		StringBuffer nome = new StringBuffer(insegnamento);
		if (discriminante.length() > 0) {
			nome.append(" [<em>" + discriminante + "</em>]");
		}
		if (modulo.length() > 0) {
			nome.append(" - " + modulo);
			if (discriminanteModulo.length() > 0) {
				nome.append(" [<em>" + discriminanteModulo + "</em>]");
			}
		}
		return nome.toString();
	}

	
	/**
	 * Restituisce il nome dell'insegnamento completo di: 
	 * discriminante, nome modulo e discriminante modulo e anno accademico nel formato 
	 * "insegnamento [<em>discriminante</em>] - modulo [<em>discriminanteModulo</em>] (anno accademico)".
	 * Vedi {@link #getNomeInsegnamentoCompleto(String, String, String, String)}.
	 * 
	 * @param insegnamento nome dell'insegnamento. Non può essere NULL. Esempio: 'Algoritmi'
	 * @param discriminante discriminante per il nome dell'insegnamento. Non può essere NULL. Esempio: 'A-L'
	 * @param modulo nome del modulo. Non può essere NULL. Esempio: 'Teoria'
	 * @param discriminanteModulo discriminante per il nome del modulo. Non può essere NULL. Esempio: 'matricole pari'
	 * @param aa anna accademico
	 * @return il nome dell'insegnamento completo di discriminante, nome modulo e discriminante modulo in formato HTML.
	 * @throws AttributoNonValorizzatoException se uno dei parametri è NULL.
	 * @see #getNomeInsegnamentoCompleto(String, String, String, String)
	 */
	static public String getNomeInsegnamentoCompleto(String insegnamento, 
	                                                 String discriminante, 
	                                                 String modulo,
	                                                 String discriminanteModulo, 
	                                                 String aa) 
	                                          throws AttributoNonValorizzatoException {
	    // Controllo sull'input
		if (aa == null)
			throw new AttributoNonValorizzatoException(
					"CourseBean.getNomeInsegnamentoCompleto: attributo anno accademico non sono valorizzato!");
		// Costruisco il nome!
		StringBuffer nome = new StringBuffer(CourseBean.getNomeInsegnamentoCompleto(insegnamento,
				discriminante, modulo, discriminanteModulo));
		nome.append(" (" + aa + ")");
		return nome.toString();
	}

	
	static public String getNomeInsegnamentoCompleto(String insegnamento, 
                                                     String discriminante, 
                                                     String modulo,
                                                     String discriminanteModulo, 
                                                     String aa, 
                                                     String nomeUL) 
                                              throws AttributoNonValorizzatoException {
       // Controllo sull'input
        if (aa == null)
            throw new AttributoNonValorizzatoException(
                    "CourseBean.getNomeInsegnamentoCompleto: attributo anno accademico non sono valorizzato!");
        // Costruisco il nome!
        StringBuffer nome = new StringBuffer(CourseBean.getNomeInsegnamentoCompleto(insegnamento,
                discriminante, modulo, discriminanteModulo));
        if (nomeUL != null && !nomeUL.equals("")) {
            nome.append(" - "+nomeUL);
        }
        if (aa != null && !aa.equals("")) {
            nome.append(" (" + aa + ")");
        }
        return nome.toString();
    }

   
	/**
	 * Restituisce il nome dell'insegnamento completo di: 
	 * discriminante, nome modulo e discriminante modulo.
	 * 
	 * @param oi Occorrenza insegnamento contenente l'insegnamento da pubblicare.
	 * @return il nome dell'insegnamento completo di discriminante, nome modulo e discriminante modulo in formato HTML.
	 * @throws AttributoNonValorizzatoException
	 * @see #getNomeInsegnamentoCompleto(String, String, String, String)
	 */
	static public String getNomeInsegnamentoCompleto(CourseBean oi)
			                                  throws AttributoNonValorizzatoException {
		return CourseBean.getNomeInsegnamentoCompleto(oi.getNomeInsegnamento(), oi.getDiscriminante(),
				oi.getNomeModulo(), oi.getDiscriminanteModulo());
	}

	
	/**
	 * Restituisce il nome dell'insegnamento completo di: 
	 * discriminante, nome modulo e discriminante modulo.
	 * 
	 * @return il nome dell'insegnamento completo di discriminante, nome modulo e discriminante modulo in formato HTML.
	 * @see #getNomeInsegnamentoCompleto(String, String, String, String)
	 */
	public String getNomeInsegnamentoCompleto() throws AttributoNonValorizzatoException {
		return CourseBean.getNomeInsegnamentoCompleto(this.getNomeInsegnamento(), this
				.getDiscriminante(), this.getNomeModulo(), this.getDiscriminanteModulo());
	}

    /**
     * Ritorna il nome dell'insegnamento completo di discriminante, nome modulo e discriminante modulo.
     * 
     * @return il nome dell'insegnamento completo di discriminante, nome modulo e discriminante modulo in formato HTML.
     * @see #getNomeInsegnamentoCompleto(String, String, String, String)
     */
    public String getNomeInsegnamentoCompletoPerAvvisi() throws AttributoNonValorizzatoException {
        StringBuffer nome = new StringBuffer(CourseBean.getNomeInsegnamentoCompleto(this.getNomeInsegnamento(), this
                .getDiscriminante(), this.getNomeModulo(), this.getDiscriminanteModulo()));
        if (this.getNomeUL() != null) {
            nome.append(" - "+this.getNomeUL());
        }
        nome.append(" (" + this.getAnnoAccademico() + ")");
        
        return nome.toString();
    }



	/**
	 * @return id
	 */
	public int getId() throws AttributoNonValorizzatoException {
		if (id == -2) {
			throw new AttributoNonValorizzatoException("CourseBean: attributo id non valorizzato!");
		} else {
			return this.id;
		}
	}

	/**
	 * @param id
	 */
	public void setId(int i) {
		this.id = i;
	}

	/**
	 * @return annoAccademico
	 */
	public String getAnnoAccademico() throws AttributoNonValorizzatoException {
		if (annoAccademico == null) {
			throw new AttributoNonValorizzatoException(
					"CourseBean: attributo annoAccademico non valorizzato!");
		} else {
			return this.annoAccademico;
		}
	}

	/**
	 * @param annoAccademico
	 */
	public void setAnnoAccademico(String string) {
		this.annoAccademico = string;
	}

	/**
	 * @return nomeInsegnamento
	 */
	public String getNomeInsegnamento() throws AttributoNonValorizzatoException {
		if (nomeInsegnamento == null) {
			throw new AttributoNonValorizzatoException(
					"CourseBean: attributo nomeInsegnamento non valorizzato!");
		} else {
			return this.nomeInsegnamento;
		}
	}

	/**
	 * @param nomeInsegnamento
	 */
	public void setNomeInsegnamento(String v) {
		this.nomeInsegnamento = v;
	}

	/**
	 * @return codiceInsegnamento
	 */
	public String getCodiceInsegnamento() throws AttributoNonValorizzatoException {
		if (codiceInsegnamento == null) {
			throw new AttributoNonValorizzatoException(
					"CourseBean: attributo codiceInsegnamento non valorizzato!");
		} else {
			return this.codiceInsegnamento;
		}
	}

	/**
	 * @param codiceInsegnamento
	 */
	public void setCodiceInsegnamento(String v) {
		this.codiceInsegnamento = v;
	}

	/**
	 * @return replica
	 */
	public char getReplica() {
		return replica;
	}

	/**
	 * @param replica
	 */
	public void setReplica(char v) {
		this.replica = v;
	}

	/**
	 * @return tipoErogazione (normale, corso elettivo, etc)
	 */
	public String getTipoErogazione() {
		return tipoErogazione;
	}

	/**
	 * @param tipoErogazione (normale, corso elettivo etc)
	 */
	public void setTipoErogazione(String v) {
		this.tipoErogazione = v;
	}

	/**
	 * @return nomeTipoErogazione (normale, corso elettivo, etc)
	 */
	public String getNomeTipoErogazione() {
		return nomeTipoErogazione;
	}

	/**
	 * @param nomeTipoErogazione (normale, corso elettivo etc)
	 */
	public void setNomeTipoErogazione(String v) {
		this.nomeTipoErogazione = v;
	}

	/**
	 * @return discriminante
	 */
	public String getDiscriminante() {
		return this.discriminante;
	}

	/**
	 * @param discriminante
	 */
	public void setDiscriminante(String v) {
		this.discriminante = v;
	}

	/**
	 * @return discrPerCreditiDiversi
	 * @throws AttributoNonValorizzatoException
	 */
	public String getDiscrPerCreditiDiversi() throws AttributoNonValorizzatoException {
		if (discrPerCreditiDiversi == null) {
			throw new AttributoNonValorizzatoException(
					"CourseBean: attributo discrPerCreditiDiversi non valorizzato!");
		} else {
			return this.discrPerCreditiDiversi;
		}
	}

	/**
	 * @param discrPerCreditiDiversi
	 */
	public void setDiscrPerCreditiDiversi(String v) {
		this.discrPerCreditiDiversi = v;
	}

	/**
	 * @return nomeModulo
	 */
	public String getNomeModulo() {
		return this.nomeModulo;
	}

	/**
	 * @param nomeModulo
	 */
	public void setNomeModulo(String v) {
		this.nomeModulo = v;
	}

	/**
	 * @return crediti
	 */
	public float getCrediti() throws AttributoNonValorizzatoException {
		if (crediti == -2) {
			throw new AttributoNonValorizzatoException("CourseBean: attributo crediti non valorizzato!");
		} else {
			return this.crediti;
		}
	}

	/**
	 * @param crediti
	 */
	public void setCrediti(float crediti) {
		this.crediti = crediti;
	}

	/**
	 * @return creditiPadre
	 */
	public float getCreditiPadre() throws AttributoNonValorizzatoException {
		if (creditiPadre == -2) {
			throw new AttributoNonValorizzatoException(
					"CourseBean: attributo creditiPadre non valorizzato!");
		} else {
			return this.creditiPadre;
		}
	}

	/**
	 * @param creditiPadre
	 */
	public void setCreditiPadre(float creditiPadre) {
		this.creditiPadre = creditiPadre;
	}

	/**
	 * @return url
	 */
	public String getUrl() throws AttributoNonValorizzatoException {
		if (url == null) {
			throw new AttributoNonValorizzatoException("CourseBean: attributo url non valorizzato!");
		} else {
			return this.url;
		}
	}

	/**
	 * @param url
	 */
	public void setUrl(String v) {
		this.url = v;
	}

	/**
	 * @return codiceEsame
	 */
	public String getCodiceEsame() throws AttributoNonValorizzatoException {
		if (codiceEsame == null) {
			throw new AttributoNonValorizzatoException(
					"CourseBean: attributo codiceEsame non valorizzato!");
		} else {
			return this.codiceEsame;
		}
	}

	/**
	 * @param codiceEsame
	 */
	public void setCodiceEsame(String codiceEsame) {
		this.codiceEsame = codiceEsame;
	}

	/**
	 * @return mansione
	 */
	public String getMansione() throws AttributoNonValorizzatoException {
		if (mansione == null) {
			throw new AttributoNonValorizzatoException(
					"CourseBean: attributo mansione non valorizzato!");
		} else {
			return this.mansione;
		}
	}

	/**
	 * @param mansione
	 */
	public void setMansione(String v) {
		this.mansione = v;
	}

	/**
	 * @return Value of idFacolta
	 */
	public int getIdFacolta() throws AttributoNonValorizzatoException {
		if (idFacolta == -2) {
			throw new AttributoNonValorizzatoException(
					"CourseBean: attributo idFacolta non valorizzato!");
		} else {
			return this.idFacolta;
		}
	}

	/**
	 * @param idFacolta
	 */
	public void setIdFacolta(int v) {
		this.idFacolta = v;
	}

	/**
	 * @return facolta
	 */
	public String getFacolta() throws AttributoNonValorizzatoException {
		if (facolta == null) {
			throw new AttributoNonValorizzatoException("CourseBean: attributo facolta non valorizzato!");
		} else {
			return this.facolta;
		}
	}

	/**
	 * @param facolta
	 */
	public void setFacolta(String v) {
		this.facolta = v;
	}

	/**
	 * @return Value of urlFacolta.
	 */
	public String getUrlFacolta() throws AttributoNonValorizzatoException {
		if (urlFacolta == null) {
			throw new AttributoNonValorizzatoException(
					"CourseBean: attributo urlFacolta non valorizzato!");
		} else {
			return this.urlFacolta;
		}
	}

	/**
	 * @param urlFacolta
	 */
	public void setUrlFacolta(String v) {
		this.urlFacolta = v;
	}

	/**
	 * Getter for property linguaNomeInsegnamento.
	 * 
	 * @return Value of property linguaNomeInsegnamento.
	 */
	public String getLinguaNomeInsegnamento() throws AttributoNonValorizzatoException {
		if (linguaNomeInsegnamento == null) {
			throw new AttributoNonValorizzatoException(
					"CourseBean: attributo linguaNomeInsegnamento non valorizzato!");
		} else {
			return this.linguaNomeInsegnamento;
		}
	}

	/**
	 * Setter for property linguaNomeInsegnamento.
	 * 
	 * @param linguaNomeInsegnamento New value of property linguaNomeInsegnamento.
	 */
	public void setLinguaNomeInsegnamento(String linguaNomeInsegnamento) {
		this.linguaNomeInsegnamento = linguaNomeInsegnamento;
	}

	/**
	 * Getter for property linguaFacolta.
	 * 
	 * @return Value of property linguaFacolta.
	 */
	public String getLinguaFacolta() throws AttributoNonValorizzatoException {
		if (linguaFacolta == null) {
			throw new AttributoNonValorizzatoException(
					"CourseBean: attributo linguaFacolta non valorizzato!");
		} else {
			return this.linguaFacolta;
		}
	}

	/**
	 * Setter for property linguaFacolta.
	 * 
	 * @param linguaFacolta New value of property linguaFacolta.
	 */
	public void setLinguaFacolta(String linguaFacolta) {
		this.linguaFacolta = linguaFacolta;
	}



	public void setCodiceCs(String codice) {
		codiceCs = codice;
	}

	public String getCodiceCs() {
		return codiceCs;
	}

	/**
	 * Metodo per assegnare i docenti che tenfono l'insegnamento
	 **/
	public void setDocenti(Vector d) {
		docenti = d;
	}

	/**
	 * Metodo che restituisce i docenti dell'insegnamento
	 */
	public Vector getDocenti() {
		return docenti;
	}

	/**
	 * Metodo per assegnare la misura dell'insegnamento (crediti/unita didattiche..)
	 */
	public void setMisura(String m) {
		misura = m;
	}

	/**
	 * Metodo che restituisce la misura dell'insegnamento (crediti/unita didattiche..)
	 */
	public String getMisura() {
		return misura;
	}

	/**
	 * Metodo per assegnare un vector di CorsoStudi a cui l'occorrenza appartiene
	 */
	public void setCorsiStudi(Vector c) {
		corsiStudi = c;
	}

	/**
	 * Metodo che restituisce un vector di CorsoStudi a cui l'occorrenza apprtiene
	 */
	public Vector getCorsiStudi() {
		return corsiStudi;
	}

	/**
	 * Metodo per assegnare il mutuante
	 */
	public void setMutuante(CourseBean c) {
		mutuante = c;
	}

	/**
	 * Metodo che restituisce il mutuante
	 */
	public CourseBean getMutuante() {
		return mutuante;
	}

	/**
	 * Metodo che ritorna se l'occorrenza insegnamento ha moduli
	 */
	public boolean isDivisoInModuli() {
		return divisoInModuli;
	}

	/**
	 * Metodo per settare se l'occorrenza insegnamento ha moduli
	 */
	public void setDivisoInModuli(boolean b) {
		divisoInModuli = b;
	}

	/**
	 * Metodo per assegnare l'id del corso di studio a cui l'occorrenza appartiene
	 */
	public void setIdCorsoStudio(int c) {
		idCorsoStudio = c;
	}

	/**
	 * Metodo che restituisce l'id del corso di studio a cui l'occorrenza appratiene
	 */
	public int getIdCorsoStudio() {
		return idCorsoStudio;
	}

	/**
	 * @return discriminanteModulo
	 */
	public String getDiscriminanteModulo() {
		return this.discriminanteModulo;
	}

	/**
	 * @param discriminanteModulo
	 */
	public void setDiscriminanteModulo(String v) {
		this.discriminanteModulo = v;
	}

	/**
	 * Metodo per assegnare il modulo
	 */
	public void setModulo(int c) {
		modulo = c;
	}

	/**
	 * Metodo che restituisce il modulo
	 */
	public int getModulo() {
		return modulo;
	}

	/**
	 * Metodo per assegnare l'id del mutuante
	 */
	public void setIdMutuante(int c) {
		idMutuante = c;
	}

	/**
	 * Metodo che restituisce l'id del mutuante
	 */
	public int getIdMutuante() {
		return idMutuante;
	}

	/**
	 * @return inizio
	 */
	public Date getInizioPeriodo() throws AttributoNonValorizzatoException {
		if (new Date(0).equals(inizioPeriodo)) {
			throw new AttributoNonValorizzatoException("PeriodoDidatticoBean: attributo inizio non valorizzato!");
		} else {
			return this.inizioPeriodo;
		}
	}

	/**
	 * @param inizio
	 */
	public void setInizioPeriodo(Date date) {
		inizioPeriodo = date;
	}

	/**
	 * @return fine
	 */
	public Date getFinePeriodo() throws AttributoNonValorizzatoException {
		if (new Date(0).equals(finePeriodo)) {
			throw new AttributoNonValorizzatoException("PeriodoDidatticoBean: attributo fine non valorizzato!");
		} else {
			return this.finePeriodo;
		}
	}

	/**
	 * @param fine
	 */
	public void setFinePeriodo(Date date) {
		finePeriodo = date;
	}

	/**
	 * Metodo che ritorna se l'occorrenza insegnamento &egrave; mutuata
	 */
	public boolean isMutuato() {
		return mutuato;
	}

	/**
	 * Metodo per settare se l'occorrenza insegnamento &egrave; mutuata
	 */
	public void setMutuato(boolean b) {
		mutuato = b;
	}

	/**
	 * Metodo che ritorna se l'occorrenza insegnamento ha lezioni
	 */
	public boolean isConLezioni() {
		return conLezioni;
	}

	/**
	 * Metodo per settare se l'occorrenza insegnamento prevede lezioni
	 */
	public void setConLezioni(boolean b) {
		conLezioni = b;
	}

	/**
	 * Metodo che ritorna se l'occorrenza insegnamento prevede visualizazione accorpata dei moduli
	 */
	public boolean isVisualizzaModuliAccorpati() {
		return visualizzaModuliAccorpati;
	}

	/**
	 * Metodo per settare se l'occorrenza insegnamento prevede visualizzazione accorpata dei moduli
	 */
	public void setVisualizzaModuliAccorpati(boolean b) {
		visualizzaModuliAccorpati = b;
	}

	/**
	 * Metodo per assegnare i settori disciplinari
	 **/
	public void setSettoriDisciplinari(Vector d) {
		settoriDisciplinari = d;
	}

	/**
	 * Metodo che restituisce i settori dell'insegnamento
	 */
	public Vector getSettoriDisciplinari() {
		return settoriDisciplinari;
	}

	/**
	 * Getter for property modalitaEsame.
	 * 
	 * @return Value of property modalitaEsame.
	 */
	public String getModalitaEsame() throws AttributoNonValorizzatoException {
		if (modalitaEsame == null) {
			throw new AttributoNonValorizzatoException(
					"CourseBean: attributo modalità esame non valorizzato!");
		} else {

			return this.modalitaEsame;
		}
	}

	/**
	 * Setter for property modalitaEsame.
	 * 
	 * @param modalitaEsame New value of property modalitaEsame.
	 */
	public void setModalitaEsame(String modalitaEsame) {

		this.modalitaEsame = modalitaEsame;
	}

	/**
	 * Getter for property linguaModalitaEsame.
	 * 
	 * @return Value of property modalitaEsame.
	 */
	public String getLinguaModalitaEsame() throws AttributoNonValorizzatoException {
		if (linguaModalitaEsame == null) {
			throw new AttributoNonValorizzatoException(
					"CourseBean: attributo lingua modalità esame non valorizzato!");
		} else {

			return this.linguaModalitaEsame;
		}
	}

	/**
	 * Setter for property modalitaEsame.
	 * 
	 * @param modalitaEsame New value of property modalitaEsame.
	 */
	public void setLinguaModalitaEsame(String linguaModalitaEsame) {

		this.linguaModalitaEsame = linguaModalitaEsame;
	}

	/**
	 * Getter for property obiettiviFormativi.
	 * 
	 * @return Value of property obiettiviFormativi.
	 */
	public String getObiettiviFormativi() throws AttributoNonValorizzatoException {
		if (obiettiviFormativi == null) {
			throw new AttributoNonValorizzatoException(
					"CourseBean: attributo obiettiviFormativi non valorizzato!");
		} else {
			return this.obiettiviFormativi;
		}
	}

	/**
	 * Setter for property obiettiviFormativi.
	 * 
	 * @param obiettiviFormativi New value of property obiettiviFormativi.
	 */
	public void setObiettiviFormativi(String obiettiviFormativi) {

		this.obiettiviFormativi = obiettiviFormativi;
	}

	/**
	 * Getter for property LinguaObiettiviFormativi.
	 * 
	 * @return Value of property LinguaObiettiviFormativi.
	 */
	public String getLinguaObiettiviFormativi() throws AttributoNonValorizzatoException {
		if (linguaObiettiviFormativi == null) {
			throw new AttributoNonValorizzatoException(
					"CourseBean: attributo LinguaobiettiviFormativi non valorizzato!");
		} else {
			return this.linguaObiettiviFormativi;
		}
	}

	/**
	 * Setter for property LinguaobiettiviFormativi.
	 * 
	 * @param obiettiviFormativi New value of property obiettiviFormativi.
	 */
	public void setLinguaObiettiviFormativi(String linguaObiettiviFormativi) {

		this.linguaObiettiviFormativi = linguaObiettiviFormativi;
	}

	/**
	 * Getter for property programma
	 * 
	 * @return Value of property programma.
	 */
	public String getProgramma() throws AttributoNonValorizzatoException {
		if (programma == null) {
			throw new AttributoNonValorizzatoException(
					"CourseBean: attributo programma non valorizzato!");
		} else {
			return this.programma;
		}
	}

	/**
	 * Setter for property programma.
	 * 
	 * @param programma New value of property programma.
	 */
	public void setProgramma(String programma) {

		this.programma = programma;
	}

	/**
	 * Getter for property LinguaProgramma.
	 * 
	 * @return Value of property LinguaProgramma.
	 */
	public String getLinguaProgramma() throws AttributoNonValorizzatoException {
		if (linguaProgramma == null) {
			throw new AttributoNonValorizzatoException(
					"CourseBean: attributo LinguaProgramma non valorizzato!");
		} else {
			return this.linguaProgramma;
		}
	}

	/**
	 * Setter for property LinguaProgramma.
	 * 
	 * @param linguaProgramma New value of property linguaProgramma.
	 */
	public void setLinguaProgramma(String linguaProgramma) {

		this.linguaProgramma = linguaProgramma;
	}

	/**
	 * Getter for property Sede.
	 * 
	 * @return Value of property Sede.
	 */
	public String getSede() throws AttributoNonValorizzatoException {
		if (sede == null) {
			throw new AttributoNonValorizzatoException("CourseBean: attributo Sede non valorizzato!");
		} else {
			return this.sede;
		}
	}

	/**
	 * Setter for property Sede.
	 * 
	 * @param linguaProgramma New value of property Sede.
	 */
	public void setSede(String sede) {
		this.sede = sede;
	}

	/**
	 * Getter for property ProvinciaSede.
	 * 
	 * @return Value of property ProvinciaSede.
	 */
	public String getProvinciaSede() throws AttributoNonValorizzatoException {
		if (provinciaSede == null) {
			throw new AttributoNonValorizzatoException(
					"CourseBean: attributo ProvinciaSede non valorizzato!");
		} else {
			return this.provinciaSede;
		}
	}

	/**
	 * Setter for property ProvinciaSede.
	 * 
	 * @param linguaProgramma New value of property ProvinciaSede.
	 */
	public void setProvinciaSede(String provincia) {
		this.provinciaSede = provincia;
	}

	/**
	 * Getter for property urlElearning.
	 * 
	 * @return Value of property urlElearning.
	 */
	public String getUrlElearning() {
		if (urlElearning == null) {
			return new String();
		} else {
			return this.urlElearning;
		}
	}

	/**
	 * Setter for property urlElearning.
	 * 
	 * @param linguaProgramma New value of property urlElearning.
	 */
	public void setUrlElearning(String urlElearning) {
		this.urlElearning = urlElearning;
	}

	/**
	 * Getter for property codElearning.
	 * 
	 * @return Value of property codElearning.
	 */
	public String getCodElearning() throws AttributoNonValorizzatoException {
		if (codElearning == null) {
			throw new AttributoNonValorizzatoException(
					"CourseBean: attributo CodElearning non valorizzato!");
		} else {
			return this.codElearning;
		}
	}

	/**
	 * Setter for property codElearning.
	 * 
	 * @param codElearning New value of property codElearning.
	 */
	public void setCodElearning(String codElearning) {
		this.codElearning = codElearning;
	}

	/**
	 * Getter for property Periodi.
	 * 
	 * @return Value of property Periodi.
	 */
	public Vector getPeriodi() throws AttributoNonValorizzatoException {
		if (periodi == null) {
			throw new AttributoNonValorizzatoException("CourseBean: attributo Periodi non valorizzato!");
		} else {
			return this.periodi;
		}
	}

	/**
	 * Setter for property Periodi.
	 * 
	 * @param linguaProgramma New value of property Periodi.
	 */
	public void setPeriodi(Vector periodi) {
		this.periodi = periodi;
	}

	/**
	 * Metodo che ritorna se l'occorrenza insegnamento è comune agli indirizzi
	 */
	public boolean isComuneIndirizzi() {
		return comuneIndirizzi;
	}

	/**
	 * Metodo per settare se l'occorrenza insegnamento è comune agli indirizzi
	 */
	public void setComuneIndirizzi(boolean b) {
		comuneIndirizzi = b;
	}

	/**
	 * @return nomeUL
	 */
	public String getNomeUL() {
		return this.nomeUL;
	}

	/**
	 * @param nomeUL
	 */
	public void setNomeUL(String v) {
		this.nomeUL = v;
	}

	/**
	 * @return Value of idPadreUL
	 */
	public int getIdPadreUL() { 
		return this.idPadreUL;
	}

	/**
	 * @param idPadreUL
	 */
	public void setIdPadreUL(int v) {
		this.idPadreUL = v;
	}

	/**
	 * Metodo che ritorna se l'occorrenza insegnamento ha UL
	 */
	public boolean isDivisoInUnita() {
		return divisoInUnita;
	}

	/**
	 * Metodo per settare se l'occorrenza insegnamento ha UL
	 */
	public void setDivisoInUnita(boolean b) {
		divisoInUnita = b;
	}
        
        /**
	 * @return linguaErog
	 */
	public String getLinguaErog() {
		return this.linguaErog;
	}

	/**
	 * @param linguaErog
	 */
	public void setLinguaErog(String l) {
		this.linguaErog = l;
	}

	/**
	 * Getter for property codiceStudente.
	 * 
	 * @return Value of property codiceStudente.
	 */
	public String getCodiceStudente() throws AttributoNonValorizzatoException {
		if (codiceStudente == null) {
			throw new AttributoNonValorizzatoException(
					"CourseBean: attributo CodiceStudente non valorizzato!");
		} else {
			return this.codiceStudente;
		}
	}

	/**
	 * Setter for property codiceStudente.
	 * 
	 * @param codiceStudente New value of property codiceStudente.
	 */
	public void setCodiceStudente(String codiceStudente) {
		this.codiceStudente = codiceStudente;
	}	
	
	/**
	 * @return Value of idDipartimento
	 */
	public int getIdDipartimento() {
		return this.idDipartimento;
	}

	/**
	 * @param idDipartimento
	 */
	public void setIdDipartimento(int v) {
		this.idDipartimento = v;
	}

	/**
	 * @return dipartimento
	 */
	public String getDipartimento() {
		return this.dipartimento;
	}

	/**
	 * @param dipartimento
	 */
	public void setDipartimento(String v) {
		this.dipartimento = v;
	}

	/**
	 * @return Value of urlDipartimento.
	 */
	public String getUrlDipartimento() {
		return this.urlDipartimento;
	}

	/**
	 * @param urlDipartimento
	 */
	public void setUrlDipartimento(String v) {
		this.urlDipartimento = v;
	}

}
