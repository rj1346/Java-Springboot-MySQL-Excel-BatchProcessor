package batch.model;

import lombok.Data;

@Data
public class User {
	
	private String Title;
	private Integer Release_year;
	private String Actor_name;
	private String Genre;
	private Float Rental_rate;
	
	public String getTitle() {
		return Title;
	}
	public void setTitle(String title) {
		Title = title;	
	}
	
	public Integer getRelease_year() {
		return Release_year;
	}
	public void setRelease_year(Integer release_year) {
		Release_year = release_year;
	}
	
	public String getActor_name() {
		return Actor_name;
	}
	public void setActor_name(String actor_name) {
		Actor_name = actor_name;	
	}
	
	public String getGenre() {
		return Genre;
	}
	public void setGenre(String genre) {
		Genre = genre;	
	}
	
	public Float getRental_rate() {
		return Rental_rate;
	}
	public void setRental_rate(Float rental_rate) {
		Rental_rate = rental_rate;	
	}
	
}

