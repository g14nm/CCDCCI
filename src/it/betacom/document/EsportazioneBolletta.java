package it.betacom.document;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;

import it.betacom.model.Conto;
import it.betacom.model.ContoCorrente;
import it.betacom.model.ContoDeposito;

public class EsportazioneBolletta {
	
	private static final String DIRECTORY = "./Estratto conto/"; 
	
	private static final Logger logger = LogManager.getLogger(EsportazioneBolletta.class.getName());
	
	public static void creaBollettaInPdf(Conto conto, int anno) {
		String tipoConto = conto.getClass().equals(ContoCorrente.class) 
				? "corrente" 
						: (conto.getClass().equals(ContoDeposito.class)
								? "deposito"
										: "investimento");
		
		String nomeFile = DIRECTORY + "EC_" + anno + "_" + tipoConto + "_" + conto.getTitolare() + ".pdf";

		try {
			//Inizializzazione documento PDF
			PdfDocument pdf = new PdfDocument(new PdfWriter(nomeFile));

			//Inizializzazione PDF con font specifico
			Document document = new Document(pdf);
			PdfFont courier_new = PdfFontFactory.createFont("./font/22815_LCOUR.ttf");
			document.setFont(courier_new);

			//Definizione contenuti
			
			//intestazione principale
			Paragraph intestazione = new Paragraph(Layout.intestazione(conto, anno));
			
			//intestazione telefonate ed elenco telefonate
			Paragraph movimenti = new Paragraph()
					.add(new Text(Layout.intestazioneMovimenti()).setBold())
					.add(Layout.movimenti(conto.getMovimentiAnno(anno)));
			
			//statistiche e totale
			Paragraph fine = new Paragraph()
					.add(Layout.interessi(conto, anno))
					.add(new Text(Layout.totale(conto, anno)).setBold())
					.add(Layout.separatoreRighe());

			//Aggiunta contenuti al documento
			document.add(intestazione);
			document.add(movimenti);
			document.add(fine);

			//Chiusura documento
			document.close();
			
			logger.info("Generato pdf dell'estratto conto del " + anno + " per conto " + tipoConto);		
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	
	public static void stampaBollettaSuConsole(Conto conto, int anno) {
		System.out.println(
				Layout.intestazione(conto, anno)
				+ "\n"
				+ Layout.intestazioneMovimenti()
				+ Layout.movimenti(conto.getMovimentiAnno(anno))
				+ "\n"
				+ Layout.interessi(conto, anno)
				+ Layout.totale(conto, anno)
				+ Layout.separatoreRighe()
				);
	}
	
}
