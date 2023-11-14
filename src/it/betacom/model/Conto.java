package it.betacom.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import it.betacom.util.FormattatoreDouble;

public abstract class Conto {

	private String titolare;
	private LocalDate dataApertura;
	private LocalDate dataChiusura;
	private final double tassoInteresseGiornaliero;
	private double interessiTotali;
	private double saldo;
	private Map<Integer, List<Movimento>> movimenti;
	private Map<Integer, Double> interessiFineAnno;
	
	private static final int BONUS_APERTURA = 1000;
	private static final int TASSAZIONE = 26;
	
	private static final Logger logger = LogManager.getLogger(Conto.class.getName());
	
	public Conto(String titolare, LocalDate dataApertura, double tassoInteresseAnnuo) {
		this.titolare = titolare;
		this.dataApertura = dataApertura;
		this.tassoInteresseGiornaliero = tassoInteresseAnnuo / 365;
		this.interessiTotali = 0;
		this.movimenti = new HashMap<Integer, List<Movimento>>();
		this.interessiFineAnno = new HashMap<Integer, Double>();
		this.apri(dataApertura);
	}
	
	public String getTitolare() {
		return titolare;
	}
	
	public double getInteressiTotali() {
		return this.interessiTotali;
	}
	
	public LocalDate getDataChiusura() {
		return this.dataChiusura;
	}
	
	private void apri(LocalDate dataApertura) {
		this.saldo = BONUS_APERTURA;
		this.movimenti.put(dataApertura.getYear(), new ArrayList<Movimento>());
		this.movimenti.get(dataApertura.getYear()).add(new Movimento(TipoMovimento.APERTURA, dataApertura, BONUS_APERTURA, this.saldo));
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
		int annoMovimento = movimento.getData().getYear();
		//se nella mappa dei movimenti non è presente la lista corrispondente all'anno del movimento
		//viene aggiunto un movimento alla fine dell'anno precedente e viene istanziata la lista
		if(movimenti.get(annoMovimento) == null) {
			this.movimenti.get(annoMovimento - 1).add(new Movimento(TipoMovimento.FINE_ANNO, LocalDate.of(annoMovimento - 1, 12, 31), 0, this.saldo));
			this.movimenti.put(annoMovimento, new ArrayList<Movimento>());
		}
		this.movimenti.get(annoMovimento).add(movimento);
	}
	
	/** per ogni movimento viene calcolato l'interesse generato nel periodo tra
	la data del movimento precedente e la data del movimento corrente e l'interesse
	viene aggiunto al totale degli interessi **/
	private void generaInteressi() {
		int anno = this.dataApertura.getYear();
		while(this.movimenti.keySet().contains(anno)) {
			List<Movimento> movimentiAnno = this.movimenti.get(anno);
			//se l'anno è quello dell'apertura del conto il ciclo parte dall'indice 1
			for(int i = (anno == this.dataApertura.getYear()) ? 1 : 0; i < movimentiAnno.size(); i++) {
				//se l'indice è 0 considera come movimento precedente l'ultimo dell'anno prima
				Movimento movimentoPrecedente = (i == 0) 
						? this.movimenti.get(anno - 1).get(this.movimenti.get(anno - 1).size() - 1)
								: movimentiAnno.get(i - 1);
				double interesse = calcolaInteresseTraMovimenti(movimentiAnno.get(i), movimentoPrecedente);
				this.interessiTotali = FormattatoreDouble.formatta(this.interessiTotali + interesse);
				this.logCalcoloInteresse(movimentoPrecedente.getData(), movimentiAnno.get(i).getData(), movimentoPrecedente.getSaldoFinale(), interesse);
			}
			this.interessiFineAnno.put(anno, this.interessiTotali);
			anno++;
		};
	}
	
	private double calcolaInteresseTraMovimenti(Movimento corrente, Movimento precedente) {
		return FormattatoreDouble.formatta(
				(FormattatoreDouble.formatta(
						precedente.getSaldoFinale()
						* ((int)ChronoUnit.DAYS.between(precedente.getData(), corrente.getData()))
						* tassoInteresseGiornaliero)
						) / 100);
	}
	
	private void logCalcoloInteresse(LocalDate inizioPeriodo, LocalDate finePeriodo, double saldoPrecedente, double interesse) {
		logger.debug("interesse tra " + inizioPeriodo.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) 
		+ " e " + finePeriodo.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
		+ " : (" + saldoPrecedente
		+ " * " + (int)ChronoUnit.DAYS.between(inizioPeriodo, finePeriodo)
		+ " * " + tassoInteresseGiornaliero
		+ ") / 100 = " + interesse);
	}
	
	public List<Movimento> getMovimentiAnno(int anno) {
		return this.movimenti.get(anno);
	}
	
	public double getSaldoAnno(int anno) {
		if(anno == LocalDate.now().getYear()) return this.saldo;
		return this.movimenti.get(anno).get(this.movimenti.get(anno).size() - 1).getSaldoFinale();
	}
	
	public double getInteressiLordiAnno(int anno) {
		return this.interessiFineAnno.get(anno);
	}
	
	public double calcolaInteressiNettiAnno(int anno) {
		return FormattatoreDouble.formatta(getInteressiLordiAnno(anno) - FormattatoreDouble.formatta((FormattatoreDouble.formatta(this.interessiFineAnno.get(anno) * TASSAZIONE)) / 100));
	}
	
	public double calcolaSaldoFinaleAnno(int anno) {
		return FormattatoreDouble.formatta(getSaldoAnno(anno) + calcolaInteressiNettiAnno(anno));
	}
	
}
