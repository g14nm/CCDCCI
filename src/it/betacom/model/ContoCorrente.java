package it.betacom.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ContoCorrente extends Conto {
	
	private static final double TASSO_INTERESSE = 7;
	
	private static final Logger logger = LogManager.getLogger(ContoCorrente.class.getName());

	public ContoCorrente(String titolare, LocalDate dataApertura) {
		super(titolare, dataApertura, TASSO_INTERESSE);
		logger.info("Conto corrente aperto in data " + dataApertura.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
	}

}
