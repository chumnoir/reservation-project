package dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

public class WorkshopDisplay implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long workshop_id;
    private Long course_id;
    private String course_name;
    private LocalDate workshop_date;
    private LocalTime start_time;
    private int price;
    private int required_time;
    private String description;
    private int capacity;

    public WorkshopDisplay() {
    }

    public Long getWorkshop_id() {
        return workshop_id;
    }

    public void setWorkshop_id(Long workshop_id) {
        this.workshop_id = workshop_id;
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

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
}