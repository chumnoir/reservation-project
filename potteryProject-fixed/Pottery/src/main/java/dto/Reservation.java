package dto;
import java.io.Serializable;

public class Reservation implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long book_id;
	private int workshop_id;
	private int user_id;
	private String guest_id;
	private int num_people;
	private int sum_price;
	
	
	public Reservation(Long book_id, int workshop_id, int user_id, String guest_id, int num_people, int sum_price) {
		super();
		this.book_id = book_id;
		this.workshop_id = workshop_id;
		this.user_id = user_id;
		this.guest_id = guest_id;
		this.num_people = num_people;
		this.sum_price = sum_price;
	}


	public Long getBook_id() {
		return book_id;
	}


	public void setBook_id(Long book_id) {
		this.book_id = book_id;
	}


	public int getWorkshop_id() {
		return workshop_id;
	}


	public void setWorkshop_id(int workshop_id) {
		this.workshop_id = workshop_id;
	}


	public int getUser_id() {
		return user_id;
	}


	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}


	public String getGuest_id() {
		return guest_id;
	}


	public void setGuest_id(String guest_id) {
		this.guest_id = guest_id;
	}


	public int getNum_people() {
		return num_people;
	}


	public void setNum_people(int num_people) {
		this.num_people = num_people;
	}


	public int getSum_price() {
		return sum_price;
	}


	public void setSum_price(int sum_price) {
		this.sum_price = sum_price;
	}
	
	

}


