/*
 *   uol: University on Line. Applicazione WEB per la visualizzazione
 *   di siti web di Facoltà
 *   Copyright (C) 2000,2001 Roberto Posenato, Mirko Manea
 *
 *   This program is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation; either version 2 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program; if not, write to the Free Software
 *   Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 *   Roberto Posenato <posenato@sci.univr.it>
 *   Dipartimento di Informatica
 *   Università degli Studi di Verona
 *   Strada le Grazie 15
 *   37134 Verona (Italy)
 *
 */
package it.alma.bean;

import it.alma.exception.AttributoNonValorizzatoException;
import java.util.Vector;

public class DegreeBean extends CodeBean {
    private int id;
    private String nome;
    private int durataAnni;
    private String informativa;
    private int idTipoCsMenu;
    private String tipoCsMenu;
    private String nomeTipoCsMenu;
    private String tipoCs;
    private String nomeTipoCs;
    private String codice;
    private int idStato;
    private String stato;
    //private int idClasse;
    //private String classe;
    private int idConsiglio;
    private String consiglio;
    private int idSegreteria;
    private String segreteria;
    private int idMenuWeb;
    private String menuWeb;
    private boolean vecchioOrdinamento;
    private String linguaNome;
    private String linguaInformativa;
    private String linguaStato;
    private String linguaClasse;
    private String linguaConsiglio;
    private String linguaSegreteria;
    private String linguaNomeTipoCs;
    private String linguaNomeTipoCsMenu;
    private Vector classiCorso;
    private String sede;
    private String obiettiviFormativi;
    private String sbocchiProfessionali;
    private int idScuolaDottorato;
    private int idAreaDisciplinare;
    private String nomeAreaDisciplinare;
    private int idAmbitoAreaDisc;
    private String nomeAmbitoAreaDisc;
    private Vector<CodeBean> dipartimenti; //dipartimenti che gestiscono il corso
    private int idUOgestDidattica;
    private int idUOgestStudenti;
    private String note;
    private Vector immagineCs;
    
    public DegreeBean() {
        id = durataAnni = idTipoCsMenu = idStato = idConsiglio = idSegreteria = idMenuWeb = idUOgestDidattica = idUOgestStudenti = -2;
        informativa = stato = nomeTipoCsMenu = nomeTipoCs = tipoCsMenu = consiglio = segreteria = menuWeb = null;
        codice = null;
        // Alcune pagine jsp fanno il test su titoloCs.getNome()
        // non va innalzata l'eccezione AttributoNonValorizzato
        // per tutti quei bean che sono "opzionali" nella jsp.
        // Se la command non popola il bean non e' possibile fare il test sul nome.
        nome = "";
        vecchioOrdinamento = false;
        linguaClasse = linguaStato = linguaInformativa = linguaNome = null;
        linguaNomeTipoCs = linguaSegreteria = linguaConsiglio = null;
        sede = null;
        classiCorso = null;
        idScuolaDottorato = -2;
        idAreaDisciplinare = idAmbitoAreaDisc = -2;
        nomeAreaDisciplinare = nomeAmbitoAreaDisc = null;
        dipartimenti = new Vector<CodeBean>();
        immagineCs = null;
    }
    
    /**
     * @return vecchioOrdinamento
     */
    public boolean isVecchioOrdinamento() {
        return vecchioOrdinamento;
    }
    
    /**
     * @return id
     */
    public int getId() throws AttributoNonValorizzatoException {
        if (id == -2) {
            throw new AttributoNonValorizzatoException("DegreeBean: attributo id non valorizzato!");
        } else {
            return this.id;
        }
    }
    
    /**
     * @return nome
     */
    public String getNome() throws AttributoNonValorizzatoException {
        if (nome == null) {
            throw new AttributoNonValorizzatoException("DegreeBean: attributo nome non valorizzato!");
        } else {
            return this.nome;
        }
    }
    
    /**
     * @return durataAnni
     */
    public int getDurataAnni() throws AttributoNonValorizzatoException {
        if (durataAnni == -2) {
            throw new AttributoNonValorizzatoException("DegreeBean: attributo durataAnni non valorizzato!");
        } else {
            return this.durataAnni;
        }
    }
    
    /**
     * @return informativa
     */
    public String getInformativa() throws AttributoNonValorizzatoException {
        if (informativa == null) {
            throw new AttributoNonValorizzatoException("DegreeBean: attributo informativa non valorizzato!");
        } else {
            return this.informativa;
        }
    }
    
    /**
     * @return idTipoCsMenu
     */
    public int getIdTipoCsMenu() throws AttributoNonValorizzatoException {
        if (idTipoCsMenu == -2) {
            throw new AttributoNonValorizzatoException("DegreeBean: attributo idTipoCsMenu non valorizzato!");
        } else {
            return this.idTipoCsMenu;
        }
    }
    
    /**
     * @return tipoCsMenu
     */
    public String getTipoCsMenu() throws AttributoNonValorizzatoException {
        if (tipoCsMenu == null) {
            throw new AttributoNonValorizzatoException("DegreeBean: attributo tipoCsMenu non valorizzato!");
        } else {
            return this.tipoCsMenu;
        }
    }
    
    /**
     * @return nomeTipoCsMenu
     */
    public String getNomeTipoCsMenu() throws AttributoNonValorizzatoException {
        if (nomeTipoCsMenu == null) {
            throw new AttributoNonValorizzatoException("DegreeBean: attributo nomeTipoCsMenu non valorizzato!");
        } else {
            return this.nomeTipoCsMenu;
        }
    }
    
    /**
     * @return nomeTipoCs
     */
    public String getNomeTipoCs() throws AttributoNonValorizzatoException {
        if (nomeTipoCs == null) {
            throw new AttributoNonValorizzatoException("DegreeBean: attributo nomeTipoCs non valorizzato!");
        } else {
            return this.nomeTipoCs;
        }
    }
    
    /**
     * @return idStato
     */
    public int getIdStato() throws AttributoNonValorizzatoException {
        if (idStato == -2) {
            throw new AttributoNonValorizzatoException("DegreeBean: attributo idStato non valorizzato!");
        } else {
            return this.idStato;
        }
    }
    
    /**
     * @return stato
     */
    public String getStato() throws AttributoNonValorizzatoException {
        if (stato == null) {
            throw new AttributoNonValorizzatoException("DegreeBean: attributo stato non valorizzato!");
        } else {
            return this.stato;
        }
    }
    
    /**
     * @return idClasse
     *
    public int getIdClasse() throws AttributoNonValorizzatoException {
        if (idClasse == -2) {
            throw new AttributoNonValorizzatoException("DegreeBean: attributo idClasse non valorizzato!");
        } else {
            return this.idClasse;
        }
    }
	*/
    
    /**
     * @return classe
     *
    public String getClasse() throws AttributoNonValorizzatoException {
        if (classe == null) {
            throw new AttributoNonValorizzatoException("DegreeBean: attributo classe non valorizzato!");
        } else {
            return this.classe;
        }
    }
    */ 
    /**
     * @return idConsiglio
     */
    public int getIdConsiglio() throws AttributoNonValorizzatoException {
        if (idConsiglio == -2) {
            throw new AttributoNonValorizzatoException("DegreeBean: attributo idConsiglio non valorizzato!");
        } else {
            return this.idConsiglio;
        }
    }
    
    /**
     * @return consiglio
     */
    public String getConsiglio() throws AttributoNonValorizzatoException {
        if (consiglio == null) {
            throw new AttributoNonValorizzatoException("DegreeBean: attributo consiglio non valorizzato!");
        } else {
            return this.consiglio;
        }
    }
    
    /**
     * @return idSegreteria
     */
    public int getIdSegreteria() throws AttributoNonValorizzatoException {
        if (idSegreteria == -2) {
            throw new AttributoNonValorizzatoException("DegreeBean: attributo idSegreteria non valorizzato!");
        } else {
            return this.idSegreteria;
        }
    }
    
    /**
     * @return segreteria
     */
    public String getSegreteria() throws AttributoNonValorizzatoException {
        if (segreteria == null) {
            throw new AttributoNonValorizzatoException("DegreeBean: attributo segreteria non valorizzato!");
        } else {
            return this.segreteria;
        }
    }
    
    /**
     * @return idMenuWeb
     */
    public int getIdMenuWeb() throws AttributoNonValorizzatoException {
        if (idMenuWeb == -2) {
            throw new AttributoNonValorizzatoException("DegreeBean: attributo idMenuWeb non valorizzato!");
        } else {
            return this.idMenuWeb;
        }
    }
    
    /**
     * @return menuWeb
     */
    public String getMenuWeb() throws AttributoNonValorizzatoException {
        if (menuWeb == null) {
            throw new AttributoNonValorizzatoException("DegreeBean: attributo menuWeb non valorizzato!");
        } else {
            return this.menuWeb;
        }
    }
    
    /**
     * @param id
     */
    public void setId(int i) {
        id = i;
    }
    
    /**
     * @param nome
     */
    public void setNome(String string) {
        nome = string;
    }
    
    /**
     * @param durataAnni
     */
    public void setDurataAnni(int i) {
        durataAnni = i;
    }
    
    /**
     * @param informativa
     */
    public void setInformativa(String string) {
        informativa = string;
    }
    
    /**
     * @param idTipoCsMenu
     */
    public void setIdTipoCsMenu(int i) {
        idTipoCsMenu = i;
    }
    
    /**
     * @param tipoCSMenu
     */
    public void setTipoCsMenu(String v) {
        this.tipoCsMenu = v;
        vecchioOrdinamento = (("V".equals(v)) || ("D".equals(v)) || ("J".equals(v)));
    }
    
    /**
     * @param nomeTipoCsMenu
     */
    public void setNomeTipoCsMenu(String string) {
        nomeTipoCsMenu = string;
    }
    
    /**
     * @param nomeTipoCs
     */
    public void setNomeTipoCs(String string) {
        nomeTipoCs = string;
    }
    
    /**
     * @param idStato
     */
    public void setIdStato(int i) {
        idStato = i;
    }
    
    /**
     * @param stato
     */
    public void setStato(String string) {
        stato = string;
    }
    
    /**
     * @param idClasse
     *
    public void setIdClasse(int i) {
        idClasse = i;
    }
    */
    /**
     * @param classe
     *
    public void setClasse(String string) {
        classe = string;
    }
    */    
    /**
     * @param idConsiglio
     */
    public void setIdConsiglio(int i) {
        idConsiglio = i;
    }
    
    /**
     * @param consiglio
     */
    public void setConsiglio(String string) {
        consiglio = string;
    }
    
    /**
     * @param idSegreteria
     */
    public void setIdSegreteria(int i) {
        idSegreteria = i;
    }
    
    /**
     * @param segreteria
     */
    public void setSegreteria(String string) {
        segreteria = string;
    }
    
    /**
     * @param idMenuWeb
     */
    public void setIdMenuWeb(int i) {
        idMenuWeb = i;
    }
    
    /**
     * @param menuWeb
     */
    public void setMenuWeb(String string) {
        menuWeb = string;
    }
    
    /**
     * @param vecchioOrdinamento
     */
    public void setVecchioOrdinamento(boolean b) {
        vecchioOrdinamento = b;
    }
    
    /**
     * Getter for property linguaNome.
     * @return Value of property linguaNome.
     */
    public String getLinguaNome() throws AttributoNonValorizzatoException {
        if (linguaNome == null) {
            throw new AttributoNonValorizzatoException("DegreeBean: attributo linguaNome non valorizzato!");
        } else {
            return this.linguaNome;
        }
    }
    
    /**
     * Setter for property linguaNome.
     * @param linguaNome New value of property linguaNome.
     */
    public void setLinguaNome(String linguaNome) {
        this.linguaNome = linguaNome;
    }
    
    /**
     * Getter for property linguaInformativa.
     * @return Value of property linguaInformativa.
     */
    public String getLinguaInformativa() throws AttributoNonValorizzatoException {
        if (linguaInformativa == null) {
            throw new AttributoNonValorizzatoException("DegreeBean: attributo linguaInformativa non valorizzato!");
        } else {
            return this.linguaInformativa;
        }
    }
    
    /**
     * Setter for property linguaInformativa.
     * @param linguaInformativa New value of property linguaInformativa.
     */
    public void setLinguaInformativa(String linguaInformativa) {
        this.linguaInformativa = linguaInformativa;
    }
    
    /**
     * Getter for property linguaStato.
     * @return Value of property linguaStato.
     */
    public String getLinguaStato() throws AttributoNonValorizzatoException {
        if (linguaStato == null) {
            throw new AttributoNonValorizzatoException("DegreeBean: attributo linguaStato non valorizzato!");
        } else {
            return this.linguaStato;
        }
    }
    
    /**
     * Setter for property linguaStato.
     * @param linguaStato New value of property linguaStato.
     */
    public void setLinguaStato(String linguaStato) {
        this.linguaStato = linguaStato;
    }
    
    /**
     * Getter for property linguaClasse.
     * @return Value of property linguaClasse.
     */
    public String getLinguaClasse() throws AttributoNonValorizzatoException {
        if (linguaClasse == null) {
            throw new AttributoNonValorizzatoException("DegreeBean: attributo linguaClasse non valorizzato!");
        } else {
            return this.linguaClasse;
        }
    }
    
    /**
     * Setter for property linguaClasse.
     * @param linguaClasse New value of property linguaClasse.
     */
    public void setLinguaClasse(String linguaClasse) {
        this.linguaClasse = linguaClasse;
    }
    
    /**
     * Getter for property linguaConsiglio.
     * @return Value of property linguaConsiglio.
     */
    public String getLinguaConsiglio() throws AttributoNonValorizzatoException {
        if (linguaConsiglio == null) {
            throw new AttributoNonValorizzatoException("DegreeBean: attributo linguaConsiglio non valorizzato!");
        } else {
            return this.linguaConsiglio;
        }
    }
    
    /**
     * Setter for property linguaConsiglio.
     * @param linguaConsiglio New value of property linguaConsiglio.
     */
    public void setLinguaConsiglio(String linguaConsiglio) {
        this.linguaConsiglio = linguaConsiglio;
    }
    
    /**
     * Getter for property linguaSegreteria.
     * @return Value of property linguaSegreteria.
     */
    public String getLinguaSegreteria() throws AttributoNonValorizzatoException {
        if (linguaSegreteria == null) {
            throw new AttributoNonValorizzatoException("DegreeBean: attributo linguaSegreteria non valorizzato!");
        } else {
            return this.linguaSegreteria;
        }
    }
    
    /**
     * Setter for property linguaSegreteria.
     * @param linguaSegreteria New value of property linguaSegreteria.
     */
    public void setLinguaSegreteria(String linguaSegreteria) {
        this.linguaSegreteria = linguaSegreteria;
    }
    
    /**
     * Getter for property linguaNomeTipoCSMenu.
     * @return Value of property linguaNomeTipoCSMenu.
     */
    public String getLinguaNomeTipoCsMenu() throws AttributoNonValorizzatoException {
        if (linguaNomeTipoCsMenu== null) {
            throw new AttributoNonValorizzatoException("DegreeBean: attributo linguaNomeTipoCsMenu non valorizzato!");
        } else {
            return this.linguaNomeTipoCsMenu;
        }
    }
    
    /**
     * Setter for property linguaNomeTipoCSMenu.
     * @param linguaNomeTipoCSMenu New value of property linguaNomeTipoCSMenu.
     */
    public void setLinguaNomeTipoCsMenu(String linguaNomeTipoCsMenu) {
        this.linguaNomeTipoCsMenu = linguaNomeTipoCsMenu;
    }
    
    
    /**
     * Ritorna un vector di ClassiCorsoStudioBean
     * @return Vector
     */
    public Vector getClassiCorso() {
        return this.classiCorso;
    }
    
    /**
     * Metodo per l'assegnamento di ClassiCorsoStudioBean
     * @param classe Vector
     */
    public void setClassiCorso(Vector classe) {
        this.classiCorso = classe;
    }
    
    /**
     * Ritorna la sede del corso di studi
     * @return sede
     */
    public String getSede() {
        return this.sede;
    }
    
    /**
     * Metodo per l'assegnamento della sede
     * @param s sede
     */
    public void setSede(String s) {
        this.sede = s;
    }
    
    /**
     * Ritorna il codice del corso di studi
     * @return codice
     */
    public String getCodice() {
        return this.codice;
    }
    
    /**
     * Metodo per l'assegnamento del codice
     * @param s codice
     */
    public void setCodice(String s) {
        this.codice = s;
    }

    /**
     * Getter for property linguaNomeTipoCS.
     * @return Value of property linguaNomeTipoCS.
     */
    public String getLinguaNomeTipoCs() throws AttributoNonValorizzatoException {
        if (linguaNomeTipoCs== null) {
            throw new AttributoNonValorizzatoException("DegreeBean: attributo linguaNomeTipoCs non valorizzato!");
        } else {
            return this.linguaNomeTipoCs;
        }
    }
    
    /**
     * Setter for property linguaNomeTipoCs.
     * @param linguaNomeTipoCs New value of property linguaNomeTipoCs.
     */
    public void setLinguaNomeTipoCs(String linguaNomeTipoCs) {
        this.linguaNomeTipoCs = linguaNomeTipoCs;
    }

    /**
     * Getter for property tipoCs.
     * @return Value of property tipoCs.
     */
    public String getTipoCs() throws AttributoNonValorizzatoException {
        if (tipoCs == null) {
            throw new AttributoNonValorizzatoException("DegreeBean: attributo tipoCs non valorizzato!");
        } else {
            return this.tipoCs;
        }
    }

    /**
     * Setter for property tipoCs.
     * @param tipoCs New value of property tipoCs.
     */
     public void setTipoCs(String tipoCs) {
        this.tipoCs = tipoCs;
     }

  /**
   * Getter for property obiettiviFormativi.
   * @return Value of property obiettiviFormativi.
   */
  public String getObiettiviFormativi() {
    return this.obiettiviFormativi;
  }

  /**
   * Setter for property obiettiviFormativi.
   * @param obiettiviFormativi New value of property obiettiviFormativi.
   */
  public void setObiettiviFormativi(String obiettiviFormativi) {
    this.obiettiviFormativi = obiettiviFormativi;
  }

  /**
   * Getter for property sbocchiProfessionali.
   * @return Value of property sbocchiProfessionali.
   */
  public String getSbocchiProfessionali() {
    return this.sbocchiProfessionali;
  }

  /**
   * Setter for property sbocchiProfessionali.
   * @param sbocchiProfessionali New value of property sbocchiProfessionali.
   */
  public void setSbocchiProfessionali(String sbocchiProfessionali) {
    this.sbocchiProfessionali = sbocchiProfessionali;
  }

  public void setIdScuolaDottorato(int id) {
      this.idScuolaDottorato = id;
  }

  public int getIdScuolaDottorato() throws AttributoNonValorizzatoException {
      if (this.idScuolaDottorato == -2) {
          throw new AttributoNonValorizzatoException("DegreeBean: attributo idScuolaDottorato non valorizzato!\n");
      } else {
          return this.idScuolaDottorato;
      }
  }

/**
 * @return the idAreaDisciplinare
 */
public int getIdAreaDisciplinare() {
	return idAreaDisciplinare;
}

/**
 * @param idAreaDisciplinare the idAreaDisciplinare to set
 */
public void setIdAreaDisciplinare(int idAreaDisciplinare) {
	this.idAreaDisciplinare = idAreaDisciplinare;
}

/**
 * @return the nomeAreaDisciplinare
 */
public String getNomeAreaDisciplinare() {
	return nomeAreaDisciplinare;
}

/**
 * @param nomeAreaDisciplinare the nomeAreaDisciplinare to set
 */
public void setNomeAreaDisciplinare(String nomeAreaDisciplinare) {
	this.nomeAreaDisciplinare = nomeAreaDisciplinare;
}

/**
 * @return the idAmbitoAreaDisc
 */
public int getIdAmbitoAreaDisc() {
	return idAmbitoAreaDisc;
}

/**
 * @param idAmbitoAreaDisc the idAmbitoAreaDisc to set
 */
public void setIdAmbitoAreaDisc(int idAmbitoAreaDisc) {
	this.idAmbitoAreaDisc = idAmbitoAreaDisc;
}

/**
 * @return the nomeAmbitoAreaDisc
 */
public String getNomeAmbitoAreaDisc() {
	return nomeAmbitoAreaDisc;
}

/**
 * @param nomeAmbitoAreaDisc the nomeAmbitoAreaDisc to set
 */
public void setNomeAmbitoAreaDisc(String nomeAmbitoAreaDisc) {
	this.nomeAmbitoAreaDisc = nomeAmbitoAreaDisc;
}

/**
 * @return the dipartimenti
 */
public Vector<CodeBean> getDipartimenti() {
	return dipartimenti;
}

/**
 * @param dipartimenti the dipartimenti to set
 */
public void setDipartimenti(Vector<CodeBean> dipartimenti) {
	this.dipartimenti = dipartimenti;
}


public void setIdUOgestDidattica(int idUOgestDidattica) {
    this.idUOgestDidattica = idUOgestDidattica;
}

public int getIdUOgestDidattica() throws AttributoNonValorizzatoException {
    if (this.idUOgestDidattica == -2) {
        throw new AttributoNonValorizzatoException("DegreeBean: attributo idUOgestDidattica non valorizzato!\n");
    } else {
        return this.idUOgestDidattica;
    }
}

public void setIdUOgestStudenti(int idUOgestStudenti) {
    this.idUOgestStudenti = idUOgestStudenti;
}

public int getIdUOgestStudenti() throws AttributoNonValorizzatoException {
    if (this.idUOgestStudenti == -2) {
        throw new AttributoNonValorizzatoException("DegreeBean: attributo idUOgestStudenti non valorizzato!\n");
    } else {
        return this.idUOgestStudenti;
    }
}

/**
 * @return the note
 */
public String getNote() {
	return note;
}

/**
 * @param note the note to set
 */
public void setNote(String note) {
	this.note = note;
}

public void setImmagineCs(Vector immagineCsFileDoc) {
    immagineCs = immagineCsFileDoc;
}

public Vector getImmagineCs() {
    return immagineCs;
}

}
