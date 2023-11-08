package it.betacom.model;

public enum TipoMovimento {

	APERTURA {
		@Override
	    public String toString() {
	        return "Bonus Apertura";
	    }
	},
	
	PRELIEVO {
		@Override
	    public String toString() {
	        return "Prelievo";
	    }
	},
	
	VERSAMENTO {
		@Override
	    public String toString() {
	        return "Versamento";
	    }
	},
	
	FINE_ANNO,
	
	CHIUSURA
	
}
