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
		
		Simulatore.simula(corrente, deposito, investimento);
		
		corrente.chiudi(LocalDate.now());
		deposito.chiudi(LocalDate.now());
		investimento.chiudi(LocalDate.now());
		
		EsportazioneBolletta.stampaBollettaSuConsole(corrente, 2021);
		EsportazioneBolletta.stampaBollettaSuConsole(corrente, 2022);
		EsportazioneBolletta.stampaBollettaSuConsole(corrente, 2023);
		EsportazioneBolletta.stampaBollettaSuConsole(deposito, 2021);
		EsportazioneBolletta.stampaBollettaSuConsole(deposito, 2022);
		EsportazioneBolletta.stampaBollettaSuConsole(deposito, 2023);
		EsportazioneBolletta.creaBollettaInPdf(corrente, 2021);
		EsportazioneBolletta.creaBollettaInPdf(corrente, 2022);
		EsportazioneBolletta.creaBollettaInPdf(corrente, 2023);
	}
	
}
