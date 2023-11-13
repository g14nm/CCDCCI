package it.betacom.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
	
	private static final Logger logger = LogManager.getLogger(Conto.class.getName());
	
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
	
	//per ogni movimento viene calcolato l'interesse generato nel periodo tra
	//la data del movimento precedente e la data del movimento corrente e l'interesse
	//viene aggiunto a interessiTotali
	private void generaInteressi() {
		this.movimenti.forEach(movimento -> {
			if(!movimento.getTipoMovimento().equals(TipoMovimento.APERTURA)) {
				Movimento movimentoPrecedente = movimenti.get(movimenti.indexOf(movimento) - 1);
				double interesse = calcolaInteresseTraMovimenti(movimento, movimentoPrecedente);
				this.interessiTotali = FormattatoreDouble.formatta(this.interessiTotali + interesse);
				this.interessiFineAnno.put(movimento.getData().getYear(), this.interessiTotali);
				logger.debug("interesse tra " + movimentoPrecedente.getData().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) 
						+ " e " + movimento.getData().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
						+ " : (" + movimentoPrecedente.getSaldoFinale()
						+ " * " + (int)ChronoUnit.DAYS.between(movimentoPrecedente.getData(), movimento.getData())
						+ " * " + tassoInteresseGiornaliero
						+ ") / 100 = " + interesse);
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
	
}
