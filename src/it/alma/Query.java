/**
 * 
 */
package it.alma;

/**
 * @author trrgnr59
 *
 */
public interface Query {

    /**
     * Estrae il profilo dei laureati dalla base dati Almalaurea
     * Access Style Query
     */
    public static final String NUM_LAUREATI = 
            "SELECT " 
          + "2016_A.CORSb            AS [Codice CdS]," 
          + "2016_A.CORSc            AS [Corso di Studio]," 
          + "Count(2016_A.MATRICOLA) AS [Numero di laureati 2016]," 
          + "Sum(2016_A.Abbin)       AS [Hanno compilato il questionario],"
          + "2016_A.CORSclasse"
          + "FROM" 
          + "[2016-A-GW] AS 2016_A"
          + "INNER JOIN [2016-GX-LT] AS 2016_B ON 2016_A.id_cv = 2016_B.id_cv" 
          + "WHERE ("
          + "("
          + "(2016_A.CORSb) = 'C75' OR (2016_A.CORSb) = 'MM10'"
          + ")" 
          + ")"
          + "GROUP BY 2016_A.CORSb, 2016_A.CORSc, 2016_A.CORSclasse"
          + "ORDER BY 2016_A.CORSb;";
    
    
    public static final String NUM_MATRICOLE_2016 = 
            "SELECT" 
//          + " count(*) "
          + " * "
          + " FROM " 
          + " \"70040-2016\" LIMIT 10";
    
    public static final String AD_2016 = 
            "SELECT " 
          + " \"MATRICOLA\"   AS \"misura\""
          + ",\"CODICIONE\"   AS \"codiceInsegnamento\""
          + ",\"CORSb\"       AS \"codiceCs\""
          + " FROM \"70040-2016\" A" 
          + " LIMIT 10";

    
}
