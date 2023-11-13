package it.betacom.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ContoInvestimento extends Conto {
	
	private static final Logger logger = LogManager.getLogger(ContoInvestimento.class.getName());

	public ContoInvestimento(String titolare, LocalDate dataApertura, int interesse) {
		super(titolare, dataApertura, interesse);
		logger.info("Conto investimento aperto in data " + dataApertura.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
	}

}
