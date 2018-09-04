/**
 * BeanUtil Copyright (C) 2003 Roberto Posenato Roberto Posenato
 * &lt;posenato@sci.univr.it&gt; Dipartimento di Informatica Università degli
 * Studi di Verona Strada le Grazie 15 37134 Verona (Italy)
 */
package it.alma.bean;

import java.util.logging.Logger;
import java.util.logging.Level;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.DoubleConverter;
import org.apache.commons.beanutils.converters.FloatConverter;
import org.apache.commons.beanutils.converters.IntegerConverter;
import org.apache.commons.beanutils.converters.LongConverter;
import org.apache.commons.beanutils.converters.SqlDateConverter;
import org.apache.commons.beanutils.converters.SqlTimeConverter;
//import org.apache.commons.beanutils.converters.SqlTimestampConverter;

/**
 * BeanUtil è una classe di servizio. Permette di popolare un Java Data Bean a
 * partire da una riga di un ResultSet scegliendo, per gli attributi che hanno
 * anche le traduzioni, i valori secondo le preferenze espresse da un oggetto di
 * tipo Lingue. Inoltre inserisce nell'attributo 'lingua'<nomeAttributo> il
 * codice della lingua del valore inserito in <nomeAttributo>. Gli attributi del
 * Java Data Bean devono avere il medesimo nome delle colonne del ResultSet
 * Created: 25/06/2003
 * 
 * @author Roberto Posenato
 * @version 0.3
 */
public class BeanUtil {

	/**
	 * Rappresenta una coppia di valori stringa
	 */
	static class ValoreELingua {
		String valore, lingua;

		ValoreELingua(String v, String l) {
			valore = v;
			lingua = l;
		}
	}

	/*
	 * L'oggetto log può essere definito a livello di classe All logging goes
	 * through this logger
	 */
	private static Logger log = Logger.getLogger(BeanUtil.class.getName());

	/**
	 * static initializer block Inizializza tutti i parametri per le chiamate ai
	 * convertitori.
	 *
	static {
		// Attivo valori di default per le conversioni di valori
		// nulli secondo la specifica 08
		ConvertUtils.register(new IntegerConverter(new Integer(-1)), Integer.TYPE);
		ConvertUtils.register(new LongConverter(new Long(-1)), Long.TYPE);
		ConvertUtils.register(new DoubleConverter(new Double(-1.0)), Double.TYPE);
		ConvertUtils.register(new FloatConverter(new Float(-1.0)), Float.TYPE);

		// assicuro che gli attributi di tipo Date e Time siano convertiti senza
		// lanciare eccezioni
		ConvertUtils.register(new SqlDateConverter(null), java.sql.Date.class);
		ConvertUtils.register(new SqlTimeConverter(null), java.sql.Time.class);
		//Registro un converter specifico per java.util.Date in modo
		//da poter mappare attributi di tipo timestamp.
		ConvertUtils.register(new DateConverter(null), java.sql.Timestamp.class);
		ConvertUtils.register(new DateConverter(null), java.util.Date.class);
	}*/

	/**
	 * Popola gli attributi di bean che hanno nome uguale a nomi delle attributi
	 * del resultSet con i valori presenti nel resultSet. Per quei attributi che
	 * hanno anche valori in più lingue, la determinazione di quale valore
	 * utilizzare è data dall'ordine delle lingue fornito dall'oggetto lingue.
	 * <br>
	 * Il bean deve rispettare la specifica dei Java Data Bean circa la
	 * composizione dei nomi e dei metodi getter e setter degli attributi. <br>
	 * I nomi degli attributi soggetti a traduzione devono avere il formato
	 * L0-nome per il valore in l'italiano, L1-nome per l valore nella prima
	 * lingua, L2-nome per il valore nella seconda lingua. L'ordine degli
	 * attributi nel resultSet è obbligatoriamente: L0-nome, L1-nome, L2-nome.
	 * 
	 * @param bean
	 *        a JavaDataBean to fill.
	 * @param resultSet
	 *        the result set that contains data.
	 * @param lingue
	 *        the wanted languages.
	 */
	public static void populate(Object bean, ResultSet resultSet) throws SQLException {
		int cols = 0;
		ResultSetMetaData metaData = null;

		try {
			metaData = resultSet.getMetaData();
			cols = metaData.getColumnCount();
		} catch (SQLException emd) {
			throw new SQLException("Errore nell'acquisire MetaData per un ResultSet: " + emd.getMessage());
		}

		log.finest("Numero colonne 'cols': " + cols);

		if (resultSet != null) {
			String columnName = null;
			int columnType = java.sql.Types.NULL;
			Object columnValue = null;
			ValoreELingua valoreELingua = null;

			for (int i = 1; i <= cols; i++) {
				try {
					try {
						columnName = metaData.getColumnName(i);
						columnType = metaData.getColumnType(i);
					} catch (SQLException emd1) {
						throw new SQLException(
								"BeanUtil.populate: errore nell'acquisire nome colonna e suo tipo dal metadata: "
										+ emd1.getMessage());
					}

					// gestione di un attributo normale (cioè senza lingue)
					columnValue = resultSet.getObject(i);

					if (columnValue == null) {
						// Secondo la direttiva 08 il valore null di una
						// colonna di tipo VARCHAR deve essere inserito
						// nell'attributo del bean come stringa ""
						// Per i tipi primitivi, la gestione dei valori di
						// default è demandata al ConvertUtils
						if (columnType == java.sql.Types.VARCHAR)
							columnValue = "";
					}

					BeanUtils.copyProperty(bean, columnName, columnValue);

					if (log.getLevel() == Level.FINEST) {
						log.logp(Level.FINEST, BeanUtil.class.getName(), "populate()", "columnName: '" + columnName
								+ "'\nValore nel result set: '" + resultSet.getObject(i) + "'\nValore nel bean: '"
								+ BeanUtils.getProperty(bean, columnName) + "'");
					}
				} catch (java.lang.IllegalAccessException ie) {
					throw new SQLException("Non è possibile accedere al metodo associato a '" + columnName
							+ "' nel bean di tipo '" + bean.getClass() + "': " + ie.getMessage());
				} catch (java.lang.reflect.InvocationTargetException te) {
					if (te.getMessage() == null) {
						log.logp(Level.SEVERE, "it.univr.di.dol.bean.BeanUtils", "populate()",
								"Il messaggio associato all'eccezione è vuoto, in stderr viene stampato lo StackTrace");
						te.printStackTrace();
					}
					throw new SQLException("Problemi nel settare l'attributo '" + columnName + "' nel bean di tipo '"
							+ bean.getClass() + "': " + te.getMessage());
				} catch (SQLException emd1) {
					throw new SQLException("BeanUtil.populate: errore generico all'interno del ciclo for: "
							+ emd1.getMessage());
				} catch (NoSuchMethodException emd2) {
					// il result set è più ricco del bean... ignoriamo.
					log.logp(Level.INFO, BeanUtil.class.getName(), "populate()", "Il result set contiene la colonna '"
							+ columnName + "' che non è presente nel bean '" + bean.getClass() + "'");
				}
			}
		}
	}

	/*
	 * Popola gli attributi di bean che hanno nome uguale a nomi delle attributi
	 * del resultSet con i valori presenti nel resultSet. <br>
	 * Il bean deve rispettare la specifica dei Java Data Bean circa la
	 * composizione dei nomi e dei metodi getter e setter degli attributi. <br>
	 * Questo metodo non gestisce i campi nelle query per le traduzioni (L0-*,
	 * L1-*, L2-*).
	 *
	public static void populate(Object bean, ResultSet resultSet) throws SQLException {
		BeanUtil.populate(bean, resultSet, null);
	}*/

	/*
	 * Dato un array di 3 stringhe 'traduzione', dove traduzione[0] è il valore
	 * in italiano di un attributo, traduzione[1] è il valore nella prima lingua
	 * e traduzione[2] è il valore nella seconda lingua, il metodo restituisce
	 * il valore dell'attributo nella PrimaLingua specificata dall'oggetto
	 * 'lingue' o, nel caso in cui questo valore sia nullo, nella SecondaLingua.
	 * Se anche il valore nella SecondaLingua non è presente, restituisce il
	 * valore in italiano (presente per gli attributi obbligatori, mentre può
	 * essere a 'null' negli altri). Il numero di lingue dell'oggetto 'lingue'
	 * viene modificato in base alla lingua utilizzata per estrarre
	 * l'informazione. Il valore di ritorno è un oggetto ValoreELingua in
	 * quanto, oltre il valore, restituisce anche il codice della lingua del
	 * valore scelto.
	 *
	static ValoreELingua getMultilingua(String[] traduzione, Lingue lingue) {
		String tmp = null, tmpL = null;
		String a1 = null, l1 = null;
		String a2 = null, l2 = null;
		int nLingue = 1;

//		 seleziono tra i 3 valori possibili per l'attributo
//		 le due traduzioni principali in base all'ordine delle lingue
//		 richieste
//		 seguendo le direttive della specifica 09.
//		 a1 = valore attributo nella prima lingua richiesta
//		 a2 = valore attributo nella seconda lingua richiesta
		if (lingue.getPrimaLingua().equals("it")) {
			a1 = traduzione[0];
			l1 = "it";
			// la prima lingua non è presente in quanto è l'italiano
			// si deve prendere la seconda
			a2 = traduzione[2];
			l2 = lingue.getSecondaLingua();
		} else {
			a1 = traduzione[1];
			l1 = lingue.getPrimaLingua();

			if (lingue.getSecondaLingua().equals("it")) {
				a2 = traduzione[0];
				l2 = "it";
			} else {
				a2 = traduzione[2];
				l2 = lingue.getSecondaLingua();
			}
		}

		// La prima e/o la seconda traduzione possono essere nulle...
		// Determino il valore da restituire in base a queste possibilità,
		// restituendo il valore in italiano se nessuna delle due traduzioni
		// è presente, come indicato nella specifica 09.
		tmp = a1;
		tmpL = l1;

		if (tmp == null) {
			nLingue = 2;
			tmp = a2;
			tmpL = l2;
			if (tmp == null) {
				nLingue = 3;
				tmp = traduzione[0];
				tmpL = "it";
			}
		}

		if (tmp != null) {
			// esiste un valore per l'attributo
			// modifico il numero Lingue usate
			lingue.setNLingue(nLingue);
		}
		return new ValoreELingua(tmp, tmpL);
	}*/
}