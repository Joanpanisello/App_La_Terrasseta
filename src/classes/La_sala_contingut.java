package classes;

import com.google.gson.annotations.SerializedName;

public class La_sala_contingut {
	
	@SerializedName("title_plain")
	private String titol;
	
	@SerializedName("content")
	private String contingut;

	
	public La_sala_contingut(String titol, String contingut) {
		super();
		this.titol = titol;
		this.contingut = contingut;
	}
	
	public String getTitol() {
		return titol;
	}

	public void setTitol(String titol) {
		this.titol = titol;
	}

	public String getContingut() {
		return contingut;
	}

	public void setContingut(String contingut) {
		this.contingut = contingut;
	}
}
