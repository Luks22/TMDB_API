package br.usjt.usjt_ccp3_consumo_img_init.model.javabeans;

import br.usjt.usjt_ccp3_consumo_img_init.model.javabeans.Cast;
import br.usjt.usjt_ccp3_consumo_img_init.model.javabeans.Crew;

public class Creditos {

	private int id;
	private Cast[] cast;
	private Crew[] crew;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Cast[] getCast() {
		return cast;
	}
	public void setCast(Cast[] cast) {
		this.cast = cast;
	}
	public Crew[] getCrew() {
		return crew;
	}
	public void setCrew(Crew[] crew) {
		this.crew = crew;
	}
	
}
