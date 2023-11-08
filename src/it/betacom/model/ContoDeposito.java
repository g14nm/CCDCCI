package it.betacom.model;

import java.time.LocalDate;

public class ContoDeposito extends Conto {
	
	private static final double TASSO_INTERESSE = 10;
	private static final int MAX_PRELIEVO = 1000;
	
	private double importoPrelevato;
	
	public ContoDeposito(String titolare, LocalDate dataApertura) {
		super(titolare, dataApertura, TASSO_INTERESSE);
	}

	@Override
	public void preleva(LocalDate data, double importo) {
		if((importoPrelevato + importo) <= MAX_PRELIEVO) {
			super.preleva(data, importo);
			importoPrelevato += importo;
		}	
	}

}
