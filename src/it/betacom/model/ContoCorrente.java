package it.betacom.model;

import java.time.LocalDate;

public class ContoCorrente extends Conto {
	
	private static final double TASSO_INTERESSE = 7;

	public ContoCorrente(String titolare, LocalDate dataApertura) {
		super(titolare, dataApertura, TASSO_INTERESSE);
	}

}
