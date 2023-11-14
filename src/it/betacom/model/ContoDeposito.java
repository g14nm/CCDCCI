package it.betacom.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ContoDeposito extends Conto {
	
	private static final double TASSO_INTERESSE = 10;
	private static final int IMPORTO_MASSIMO_PRELIEVO = 1000;
	
	private static final Logger logger = LogManager.getLogger(ContoDeposito.class.getName());
	
	public ContoDeposito(String titolare, LocalDate dataApertura) {
		super(titolare, dataApertura, TASSO_INTERESSE);
		logger.info("Conto deposito aperto in data " + dataApertura.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
	}

	@Override
	public void preleva(LocalDate data, double importo) {
		if(importo <= IMPORTO_MASSIMO_PRELIEVO)
			super.preleva(data, importo);
	}

}
