package it.betacom.model;

import java.time.LocalDate;

public class Movimento {

	private TipoMovimento tipoMovimento;
	private LocalDate data;
	private double importo, saldoFinale;
	
	public Movimento(TipoMovimento tipoMovimento, LocalDate data, double importo, double saldoFinale) {
		this.tipoMovimento = tipoMovimento;
		this.data = data;
		this.importo = importo;
		this.saldoFinale = saldoFinale;
	}
	
	public TipoMovimento getTipoMovimento() {
		return this.tipoMovimento;
	}
	
	public LocalDate getData() {
		return data;
	}

	public double getImporto() {
		return importo;
	}

	public double getSaldoFinale() {
		return saldoFinale;
	}
	
}
