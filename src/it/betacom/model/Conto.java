package it.betacom.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import it.betacom.util.FormattatoreDouble;

public abstract class Conto {

	private String titolare;
	private LocalDate dataChiusura;
	private final double tassoInteresseGiornaliero;
	private double interessiTotali;
	private double saldo;
	private List<Movimento> movimenti;
	private Map<Integer, Double> interessiFineAnno;
	
	private static final int BONUS_APERTURA = 1000;
	private static final int TASSAZIONE = 26;
	
	public Conto(String titolare, LocalDate dataApertura, double tassoInteresseAnnuo) {
		this.titolare = titolare;
		this.tassoInteresseGiornaliero = tassoInteresseAnnuo / 365;
		this.interessiTotali = 0;
		this.movimenti = new ArrayList<Movimento>();
		this.interessiFineAnno = new HashMap<Integer, Double>();
		this.apri(dataApertura);
	}
	
	public String getTitolare() {
		return titolare;
	}
	
	public LocalDate getDataChiusura() {
		return dataChiusura;
	}
	
	public double getInteressiTotali() {
		return this.interessiTotali;
	}
	
	private void apri(LocalDate dataApertura) {
		this.saldo = BONUS_APERTURA;
		this.movimenti.add(new Movimento(TipoMovimento.APERTURA, dataApertura, BONUS_APERTURA, this.saldo));
	}
	
	public void preleva(LocalDate data, double importo) {
		this.aggiungiMovimento(new Movimento(TipoMovimento.PRELIEVO, data, importo, this.saldo - importo));
		this.saldo -= importo;
	}
	
	public void versa(LocalDate data, double importo) {
		this.aggiungiMovimento(new Movimento(TipoMovimento.VERSAMENTO, data, importo, this.saldo + importo));
		this.saldo += importo;
	}
	
	public void chiudi(LocalDate data) {
		this.aggiungiMovimento(new Movimento(TipoMovimento.CHIUSURA, data, 0, this.saldo));
		this.dataChiusura = data;
		this.generaInteressi();
	}
	
	private void aggiungiMovimento(Movimento movimento) {
		//controlla l'anno dell'ultimo movimento, se non è uguale al nuovo movimento
		//viene aggiunto un movimento alla fine dell'anno precedente con il saldo corrente
		//e poi aggiunto il nuovo movimento
		int annoMovimentoPrecedente = this.movimenti.get(this.movimenti.size() - 1).getData().getYear();
		if(!(annoMovimentoPrecedente == movimento.getData().getYear()))
			this.movimenti.add(new Movimento(TipoMovimento.FINE_ANNO, LocalDate.of(annoMovimentoPrecedente, 12, 31), 0, this.saldo));
		this.movimenti.add(movimento);
	}
	
	private void generaInteressi() {
		this.movimenti.forEach(movimento -> {
			if(!movimento.getTipoMovimento().equals(TipoMovimento.APERTURA)) {
				this.interessiTotali = FormattatoreDouble.formatta(this.interessiTotali + calcolaInteresseTraMovimenti(movimento, movimenti.get(movimenti.indexOf(movimento) - 1)));
				this.interessiFineAnno.put(movimento.getData().getYear(), this.interessiTotali);
			}
		});
	}
	
	private double calcolaInteresseTraMovimenti(Movimento corrente, Movimento precedente) {
		return FormattatoreDouble.formatta(
				(FormattatoreDouble.formatta(
						precedente.getSaldoFinale()
						* ((int)ChronoUnit.DAYS.between(precedente.getData(), corrente.getData()))
						* tassoInteresseGiornaliero)
						) / 100);
	}
	
	public List<Movimento> getMovimentiAnno(int anno){
		return this.movimenti.stream().filter(movimento -> movimento.getData().getYear() == anno).collect(Collectors.toList());
	}
	
	public double getSaldoAnno(int anno) {
		if(anno == LocalDate.now().getYear()) return this.saldo;
		return getMovimentiAnno(anno).get(getMovimentiAnno(anno).size() - 1).getSaldoFinale();
	}
	
	public double getInteressiLordiAnno(int anno) {
		if(anno == LocalDate.now().getYear()) return this.interessiTotali;
		return this.interessiFineAnno.get(anno);
	}
	
	public double calcolaInteressiNettiAnno(int anno) {
		return FormattatoreDouble.formatta(getInteressiLordiAnno(anno) - FormattatoreDouble.formatta((FormattatoreDouble.formatta(this.interessiFineAnno.get(anno) * TASSAZIONE)) / 100));
	}
	
	public double calcolaSaldoFinaleAnno(int anno) {
		return FormattatoreDouble.formatta(getSaldoAnno(anno) + calcolaInteressiNettiAnno(anno));
	}
	
//	private double calcolaInteresse(Movimento movimento) {
//	//esclude l'apertura del conto dal calcolo
//	if(movimenti.indexOf(movimento) != 0) {
//		Movimento movimentoPrecedente = movimenti.get(movimenti.indexOf(movimento) - 1);
//		return FormattatoreDouble.formatta(
//				(FormattatoreDouble.formatta(
//						movimentoPrecedente.getSaldoFinale()
//						* ((int)ChronoUnit.DAYS.between(movimentoPrecedente.getData(), movimento.getData()))
//						* tassoInteresseGiornaliero)
//						) / 100);
//	}
//	//alla data dell'apertura gli interessi accumulati sono pari a 0 
//	return 0;
//}
	
	//per ogni movimento viene calcolato l'interesse generato nel periodo tra
	//la data del movimento precedente e la data del movimento corrente e l'interesse
	//viene aggiunto a interessiTotali
//	public void generaInteressi() {
//		movimenti.forEach(movimento -> {
//			this.interessiTotali = 
//					FormattatoreDouble.formatta(this.interessiTotali + this.calcolaInteresse(movimento)
//					//viene aggiunto l'interesse generato
//					+ FormattatoreDouble.formatta(this.tassoInteresseGiornaliero * this.saldo));
//		});
//	}
	
}
