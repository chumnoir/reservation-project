package dto;
import java.io.Serializable;

public class Course implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long course_id;
	private String course_name;
	private int price;
	private int required_time;
	private String description;

	public Course(Long course_id, String course_name, int price, int required_time, String description) {
		super();
		this.course_id = course_id;
		this.course_name = course_name;
		this.price = price;
		this.required_time = required_time;
		this.description = description;
	}

	public Long getCourse_id() {
		return course_id;
	}

	public void setCourse_id(Long course_id) {
		this.course_id = course_id;
	}

	public String getCourse_name() {
		return course_name;
	}

	public void setCourse_name(String course_name) {
		this.course_name = course_name;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getRequired_time() {
		return required_time;
	}

	public void setRequired_time(int required_time) {
		this.required_time = required_time;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Course() {

	}

}