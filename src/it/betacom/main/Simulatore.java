package it.betacom.main;

import java.time.LocalDate;
import java.util.Random;

import it.betacom.model.ContoCorrente;
import it.betacom.model.ContoDeposito;
import it.betacom.model.ContoInvestimento;

public class Simulatore {
	
	private static final int IMPORTO_MASSIMO = 5000;
	
	public static void simula(ContoCorrente corrente, ContoDeposito deposito, ContoInvestimento investimento) {
		Random random = new Random();
		
		//operazioni 2021
		corrente.versa(LocalDate.of(2021, 3, 6), random.nextInt(IMPORTO_MASSIMO) + 1);
		corrente.preleva(LocalDate.of(2021, 4, 10), random.nextInt(IMPORTO_MASSIMO) + 1);
		corrente.versa(LocalDate.of(2021, 5, 14), random.nextInt(IMPORTO_MASSIMO) + 1);
		deposito.versa(LocalDate.of(2021, 6, 18), random.nextInt(IMPORTO_MASSIMO) + 1);
		deposito.preleva(LocalDate.of(2021, 7, 23), random.nextInt(IMPORTO_MASSIMO) + 1);
		deposito.versa(LocalDate.of(2021, 8, 28), random.nextInt(IMPORTO_MASSIMO) + 1);
		investimento.preleva(LocalDate.of(2021, 9, 13), random.nextInt(IMPORTO_MASSIMO) + 1);
		investimento.versa(LocalDate.of(2021, 10, 22), random.nextInt(IMPORTO_MASSIMO) + 1);
		investimento.preleva(LocalDate.of(2021, 11, 3), random.nextInt(IMPORTO_MASSIMO) + 1);
		
		//operazioni 2022
		corrente.versa(LocalDate.of(2022, 3, 6), random.nextInt(IMPORTO_MASSIMO) + 1);
		corrente.preleva(LocalDate.of(2022, 4, 10), random.nextInt(IMPORTO_MASSIMO) + 1);
		corrente.versa(LocalDate.of(2022, 5, 14), random.nextInt(IMPORTO_MASSIMO) + 1);
		deposito.versa(LocalDate.of(2022, 6, 18), random.nextInt(IMPORTO_MASSIMO) + 1);
		deposito.preleva(LocalDate.of(2022, 7, 23), random.nextInt(IMPORTO_MASSIMO) + 1);
		deposito.versa(LocalDate.of(2022, 8, 28), random.nextInt(IMPORTO_MASSIMO) + 1);
		investimento.preleva(LocalDate.of(2022, 9, 13), random.nextInt(IMPORTO_MASSIMO) + 1);
		investimento.versa(LocalDate.of(2022, 10, 22), random.nextInt(IMPORTO_MASSIMO) + 1);
		investimento.preleva(LocalDate.of(2022, 11, 3), random.nextInt(IMPORTO_MASSIMO) + 1);
		
		//operazioni 2023
		corrente.versa(LocalDate.of(2023, 3, 6), random.nextInt(IMPORTO_MASSIMO) + 1);
		corrente.preleva(LocalDate.of(2023, 4, 10), random.nextInt(IMPORTO_MASSIMO) + 1);
		corrente.versa(LocalDate.of(2023, 5, 14), random.nextInt(IMPORTO_MASSIMO) + 1);
		deposito.versa(LocalDate.of(2023, 6, 18), random.nextInt(IMPORTO_MASSIMO) + 1);
		deposito.preleva(LocalDate.of(2023, 7, 23), random.nextInt(IMPORTO_MASSIMO) + 1);
		deposito.versa(LocalDate.of(2023, 8, 28), random.nextInt(IMPORTO_MASSIMO) + 1);
		investimento.preleva(LocalDate.of(2023, 9, 13), random.nextInt(IMPORTO_MASSIMO) + 1);
		investimento.versa(LocalDate.of(2023, 10, 22), random.nextInt(IMPORTO_MASSIMO) + 1);
		investimento.preleva(LocalDate.of(2023, 11, 3), random.nextInt(IMPORTO_MASSIMO) + 1);
		
		//chiusura conti nel giorno corrente
		corrente.chiudi(LocalDate.now());
		deposito.chiudi(LocalDate.now());
		investimento.chiudi(LocalDate.now());
	}
}
