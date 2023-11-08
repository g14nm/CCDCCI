package it.betacom.document;

import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import it.betacom.model.Conto;
import it.betacom.model.ContoCorrente;
import it.betacom.model.ContoDeposito;
import it.betacom.model.Movimento;
import it.betacom.model.TipoMovimento;

public class Layout {
	
	private static final int LUNGHEZZA_SEPARATORE_RIGHE = 70,
			NUMERO_CAMPI = 4,
			LUNGHEZZA_DATA = 10,
			LUNGHEZZA_TIPO = 15,
			LUNGHEZZA_QUANTITA = 8,
			LUNGHEZZA_SALDO = 8;
	
	public static String intestazione(Conto conto, int anno) {
		return
				separatoreRighe()
				+ "\n"
				+ "| Data: " + 
				(anno == conto.getDataChiusura().getYear() ? conto.getDataChiusura().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) : anno)
				+ " | Titolare: "
				+ conto.getTitolare()
				+ " | Tipo: Conto "
				+ (conto.getClass().equals(ContoCorrente.class) 
						? "corrente" 
								: (conto.getClass().equals(ContoDeposito.class)
										? "deposito"
												: "investimento")
						)
				+ " |\n"
				+ separatoreRighe();
	}
	
	public static String intestazioneMovimenti() {
		return
				"Dettaglio movimenti"
				+ "\n\n"
				+ StringUtils.rightPad("Data", LUNGHEZZA_DATA) + " | "
				+ (StringUtils.rightPad("Tipo operazione", LUNGHEZZA_TIPO)) + " | "
				+ (StringUtils.rightPad("Quantità", LUNGHEZZA_QUANTITA)) + " | "
				+ (StringUtils.rightPad("Saldo", LUNGHEZZA_SALDO))
				+ "\n";
	}
	
	public static String movimenti(List<Movimento> movimenti) {
		//ordinamento lista telefonate per data
		movimenti.sort(Comparator.comparing(Movimento::getData));
		
		//elenco telefonate con dettagli di ciascuna movimento
		StringBuilder listaMovimenti = new StringBuilder("");
		for(Movimento movimento : movimenti) {
			if(!movimento.getTipoMovimento().equals(TipoMovimento.CHIUSURA) && !movimento.getTipoMovimento().equals(TipoMovimento.FINE_ANNO))
				listaMovimenti
				.append(separatoreRighe(LUNGHEZZA_DATA + LUNGHEZZA_TIPO + LUNGHEZZA_QUANTITA + LUNGHEZZA_SALDO + (NUMERO_CAMPI - 1) * 3) + "\n")
				.append(StringUtils.rightPad(movimento.getData().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")), LUNGHEZZA_DATA)).append(" | ")
				.append(StringUtils.rightPad(movimento.getTipoMovimento().toString(), LUNGHEZZA_TIPO)).append(" | ")
				.append(StringUtils.leftPad(String.format("%.2f", movimento.getImporto()), LUNGHEZZA_QUANTITA)).append(" | ")
				.append(StringUtils.leftPad(String.format("%.2f", movimento.getSaldoFinale()), LUNGHEZZA_SALDO))
				.append("\n");
		}
		return new String(listaMovimenti);
	}
	
	public static String interessi(Conto conto, int anno) {
		String giorno = "";
		if(anno == conto.getDataChiusura().getYear()) giorno = conto.getDataChiusura().format(DateTimeFormatter.ofPattern("dd/MM"));
		else giorno = "31/12";
		StringBuilder sb = new StringBuilder();
		int leftPad = LUNGHEZZA_DATA + LUNGHEZZA_TIPO + LUNGHEZZA_QUANTITA + LUNGHEZZA_SALDO + (NUMERO_CAMPI - 1) * 3;
		String inizioRiga = "Saldo al " + giorno +":";
		sb.append("\n" + inizioRiga + StringUtils.leftPad(String.format("%.2f", conto.getSaldoAnno(anno)), leftPad - inizioRiga.length()));
		inizioRiga = "Interessi lordi maturati al " + giorno + ":";
		sb.append("\n" + inizioRiga + StringUtils.leftPad(String.format("%.2f", conto.getInteressiLordiAnno(anno)), leftPad - inizioRiga.length()));
		inizioRiga = "Interessi netti maturati al " + giorno + ":";
		sb.append("\n" + inizioRiga + StringUtils.leftPad(String.format("%.2f", conto.calcolaInteressiNettiAnno(anno)), leftPad - inizioRiga.length()));
		return new String(sb);
	}
	
	public static String totale(Conto conto, int anno) {
		int leftPad = LUNGHEZZA_DATA + LUNGHEZZA_TIPO + LUNGHEZZA_QUANTITA + LUNGHEZZA_SALDO + (NUMERO_CAMPI - 1) * 3;
		String inizioRiga = "Saldo finale:";
		return "\n\n" + inizioRiga + StringUtils.leftPad(String.format("%.2f", conto.calcolaSaldoFinaleAnno(anno)), leftPad - inizioRiga.length()) + "\n";
	}
	
	//separatore di default
	public static String separatoreRighe() {
		return StringUtils.rightPad("", LUNGHEZZA_SEPARATORE_RIGHE, "-");
	}
	
	public static String separatoreRighe(int lunghezza) {
		return StringUtils.rightPad("", lunghezza, "-");
	}

}
