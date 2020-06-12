package com.trevormetcalf.trevortaskc195;

import com.trevormetcalf.model.Appointment;
import com.trevormetcalf.model.Customer;
import com.trevormetcalf.model.User;

/*
    This is a singleton class used to keep track of state.
    It can hold customer, appointment, and user information.
    Since it is static, the data may be accessed from anywhere within the program.
 */

public class State {

    private static final State instance = new State();
    private Customer selectedCustomer;
    private Appointment selectedAppointment;
    private User currentUser;


    private State(){
    }

    // Getters and setters
    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public Appointment getSelectedAppointment() {
        return selectedAppointment;
    }

    public void setSelectedAppointment(Appointment selectedAppointment) {
        this.selectedAppointment = selectedAppointment;
    }

    public void setSelectedCustomer(Customer selectedCustomer) {
        this.selectedCustomer = selectedCustomer;
    }

    public Customer getSelectedCustomer() {
        return selectedCustomer;
    }

    public static State getInstance(){
        return instance;
    }

}
