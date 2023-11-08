package it.betacom.model;

import java.time.LocalDate;

public class ContoInvestimento extends Conto {

	public ContoInvestimento(String titolare, LocalDate dataApertura, int interesse) {
		super(titolare, dataApertura, interesse);
	}

}
