package com.trevormetcalf.model;

import com.trevormetcalf.utility.Query;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/*
    This class is used to create appointment objects.
    All getter and setters are very typical except for getCustomerName();
 */
public class Appointment {

    private int appointmentId, customerId, userId;
    private String title, description, location, contact, type, customerName;
    private LocalDateTime start, end;


    public Appointment(int appointmentId, int customerId, int userId, String title, String description, String location, String contact, String type, LocalDateTime start, LocalDateTime end) {
        this.appointmentId = appointmentId;
        this.customerId = customerId;
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.location = location;
        this.contact = contact;
        this.type = type;
        this.start = start;
        this.end = end;
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDateTime getStart() {
        return start;
    }
    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    // This method uses a sql query to return the customer's name using the customerId.
    // It allows the appointment table view to have a Name column using property value factories.
    public String getCustomerName() {
        String sql = "SELECT customerName FROM customer where customerId = " + customerId;
        Query.executeQuery(sql);
        ResultSet result = Query.getResult();
        try {
            while(result.next()) {
                customerName = result.getString("customerName");
            }
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
        return customerName;
    }
}
