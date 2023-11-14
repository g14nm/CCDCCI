package it.betacom.main;

import java.time.LocalDate;
import java.util.Random;

import it.betacom.document.EsportazioneBolletta;
import it.betacom.model.ContoCorrente;
import it.betacom.model.ContoDeposito;
import it.betacom.model.ContoInvestimento;

public class Main {
	
	public static void main(String[] args) {
		
		ContoCorrente corrente = new ContoCorrente("Mario Rossi", LocalDate.of(2021, 1, 15));
		ContoDeposito deposito = new ContoDeposito("Mario Rossi", LocalDate.of(2021, 3, 4));
		ContoInvestimento investimento = new ContoInvestimento("Mario Rossi", LocalDate.of(2021, 4, 20), new Random().nextInt(200) + 1 - 100);
		
		//simulazione di operazioni nei vari conti
		Simulatore.simula(corrente, deposito, investimento);
		
		EsportazioneBolletta.creaBollettaInPdf(corrente, 2021);
		EsportazioneBolletta.creaBollettaInPdf(corrente, 2022);
		EsportazioneBolletta.creaBollettaInPdf(corrente, 2023);
		
		EsportazioneBolletta.creaBollettaInPdf(deposito, 2021);
		EsportazioneBolletta.creaBollettaInPdf(deposito, 2022);
		EsportazioneBolletta.creaBollettaInPdf(deposito, 2023);
		
		EsportazioneBolletta.creaBollettaInPdf(investimento, 2021);
		EsportazioneBolletta.creaBollettaInPdf(investimento, 2022);
		EsportazioneBolletta.creaBollettaInPdf(investimento, 2023);
		
	}
	
}
