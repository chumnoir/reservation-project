package dto;

import java.io.Serializable;

public class ReservationHistory implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long book_id;
    private String course_name;
    private String workshop_date;
    private String start_time;
    private String guest_name;
    private int num_people;
    private int total_price;

    public ReservationHistory() {
    }

    public ReservationHistory(Long book_id, String course_name, String workshop_date,
                                 String start_time, String guest_name, int num_people, int total_price) {
        this.book_id = book_id;
        this.course_name = course_name;
        this.workshop_date = workshop_date;
        this.start_time = start_time;
        this.guest_name = guest_name;
        this.num_people = num_people;
        this.total_price = total_price;
    }

    public Long getBook_id() {
        return book_id;
    }

    public void setBook_id(Long book_id) {
        this.book_id = book_id;
    }

    public String getCourse_name() {
        return course_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public String getWorkshop_date() {
        return workshop_date;
    }

    public void setWorkshop_date(String workshop_date) {
        this.workshop_date = workshop_date;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getGuest_name() {
        return guest_name;
    }

    public void setGuest_name(String guest_name) {
        this.guest_name = guest_name;
    }

    public int getNum_people() {
        return num_people;
    }

    public void setNum_people(int num_people) {
        this.num_people = num_people;
    }

    public int getTotal_price() {
        return total_price;
    }

    public void setTotal_price(int total_price) {
        this.total_price = total_price;
    }
}
