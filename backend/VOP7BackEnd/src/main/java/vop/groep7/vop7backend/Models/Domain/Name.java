package vop.groep7.vop7backend.Models.Domain;

import java.io.Serializable;
import vop.groep7.vop7backend.Models.Model;

/**
 *
 * @author Backend Team
 */
public class Name implements Serializable, Model {

    private String firstName;
    private String lastName;

    /**
     * The public constructor with all fields required to create a Name object
     * 
     * @param firstName the first name of the name
     * @param lastName the last name of the name
     */
    public Name(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }   
    
    /**
     * Get the first name
     * 
     * @return the first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Get the last name
     * 
     * @return the last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Set the first name
     * 
     * @param firstName the first name of the name
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Set the last name
     * 
     * @param lastName the last name of the name
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

}
