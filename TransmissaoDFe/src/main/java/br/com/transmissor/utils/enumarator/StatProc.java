package br.com.transmissor.utils.enumarator;

public enum StatProc {

	AUTORIZADA("100|150"),
	DENEGADA("110|301|302|303"),
	CANCELADA("101|151"),
	INUTILIZADA("102"),
	EVENTO("135|136");
	
	private String stats;
	
	private StatProc(String stats){
		setStats(stats);
	}

	public String getStats() {
		return stats;
	}
	public void setStats(String stats) {
		this.stats = stats;
	}
	
	public static boolean isAutorizada(String stat){
		return AUTORIZADA.getStats().contains(stat);
	}
	
	public static boolean isDenegada(String stat){
		return DENEGADA.getStats().contains(stat);
	}
	
	public static boolean isCancelada(String stat){
		return CANCELADA.getStats().contains(stat);
	}
	
	public static boolean isInutilizada(String stat){
		return INUTILIZADA.getStats().contains(stat);
	}
	
	public static boolean isEventoAut(String stat){
		return EVENTO.getStats().contains(stat);
	}
}