package dto;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

public class WorkShop implements Serializable {
	private Long workshop_id;
	private int course_id;
	private LocalDate workshop_date;
	private LocalTime start_time;
	private int capacity;

	private static final long serialVersionUID = 1L;

	public WorkShop(Long workshop_id, int course_id, LocalDate workshop_date, LocalTime start_time, int capacity) {
		super();
		this.workshop_id = workshop_id;
		this.course_id = course_id;
		this.workshop_date = workshop_date;
		this.start_time = start_time;
		this.capacity = capacity;
	}

	public WorkShop() {

	}

	public Long getWorkshop_id() {
		return workshop_id;
	}

	public void setWorkshop_id(Long workshop_id) {
		this.workshop_id = workshop_id;
	}

	public int getCourse_id() {
		return course_id;
	}

	public void setCourse_id(int course_id) {
		this.course_id = course_id;
	}

	public LocalDate getWorkshop_date() {
		return workshop_date;
	}

	public void setWorkshop_date(LocalDate workshop_date) {
		this.workshop_date = workshop_date;
	}

	public LocalTime getStart_time() {
		return start_time;
	}

	public void setStart_time(LocalTime start_time) {
		this.start_time = start_time;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}