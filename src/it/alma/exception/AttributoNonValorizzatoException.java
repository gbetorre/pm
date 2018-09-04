/*
 *   uol: University on Line. Applicazione WEB per la visualizzazione
 *   Copyright (C) 2000-2004 Dipartimento di Informatica
 *   Università degli Studi di Verona
 *   Strada le Grazie 15
 *   37134 Verona (Italy)
 *
 */
package it.alma.exception;

/**
 * Questa eccezione viene ritornata quando si tenta di accedere
 * ad un attributo di un Bean che non è stato inizializzato
 * (attributo del Bean non letto dal Database)
 */
public class AttributoNonValorizzatoException extends Exception {
    /**
	 * Necessario in quanto si espande Exception.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public AttributoNonValorizzatoException() {
        super();
    }

    /**
     * @param message
     */
    public AttributoNonValorizzatoException(String message) {
        super(message);
    }

    /**
     * @param message
     * @param cause
     */
    public AttributoNonValorizzatoException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @param cause
     */
    public AttributoNonValorizzatoException(Throwable cause) {
        super(cause);
    }
}