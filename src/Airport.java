
public class Airport {
	String code;
	ACC controller;
	ATC responsible;
	public Airport(String code,ACC controller) {
		this.code = code;
		this.controller = controller;
	}
	public void setATC(ATC atc) {
		this.responsible = atc;
	}
}
