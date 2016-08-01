package vop.groep7.vop7backend.Models.Domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import vop.groep7.vop7backend.Models.DomainModel;

/**
 *
 * @author Backend Team
 */
public class User extends DomainModel implements Serializable {

    private final int userIdentifier;

    private final boolean mute;
    private final Name name;
    private String cellPhone;
    private boolean cellPhoneValidated;
    private final String email;
    private boolean emailValidated;

    private ArrayList<POI> pointsOfInterest;
    private ArrayList<Travel> travels;

    /**
     * The public constructor with all fields required to create a User object
     * 
     * @param userIdentifier the unique identifier for this user
     * @param email the email address of this user
     * @param firstName the first name of the user
     * @param lastName the last name of the user
     * @param mute boolean that indicates if the user wants to mute
     * notifications or not
     * @param cellPhone the cell phone number of the user
     * @param cellPhoneValidated boolean that indicates if the cell phone number
     * of the user is validated
     * @param emailValidated boolean that indicates if the email address of the
     * user is validated
     */
    public User(int userIdentifier, String email, String firstName, String lastName, String cellPhone, boolean mute, boolean cellPhoneValidated, boolean emailValidated) {
        this.userIdentifier = userIdentifier;
        this.email = email;
        this.emailValidated = emailValidated;
        this.name = new Name(firstName, lastName);
        this.cellPhone = cellPhone;
        this.mute = mute;
        this.cellPhoneValidated = cellPhoneValidated;
    }

    /**
     * Get the unique identifier of this user
     * 
     * @return the identifier
     */
    public int getUserIdentifier() {
        return userIdentifier;
    }

    /**
     * Get the email address of this user
     * 
     * @return the email address
     */
    public String getEmail() {
        return email;
    }
    
    /**
     * Check if the email of the user is validated
     * 
     * @return true if validated, else false
     */
    public boolean isEmailValidated() {
        return emailValidated;
    }

    /**
     * Set teh first name of the user
     * 
     * @param firstName the first name of the user
     */
    public void setFirstName(String firstName) {
        name.setFirstName(firstName);
    }

    /**
     * Set teh last name of the user
     * 
     * @param lastName the last name of the user
     */
    public void setLastName(String lastName) {
        name.setLastName(lastName);
    }

    /**
     * Get the cell phone number of the user
     * 
     * @param cellPhone the cell phone number of the user
     */
    public void setCellPhone(String cellPhone) {
        this.cellPhone = cellPhone;
    }

    /**
     * Set if the cell phone number of the user is validated
     * 
     * @param cellPhone boolean true if validated, else false
     */
    public void setCellPhoneValidation(boolean cellPhone) {
        this.cellPhoneValidated = cellPhone;
    }

    /**
     * Set if the email of the user is validated
     * 
     * @param email boolean true if validated, else false
     */
    public void setEmailValidation(boolean email) {
        this.emailValidated = email;
    }

    /**
     * Check if the user wants to mute notifiactions
     * 
     * @return boolean true if yes, else false
     */
    public boolean isMute() {
        return mute;
    }

    /**
     * Get the name of the user
     * 
     * @return the name
     * @see Name
     */
    public Name getName() {
        return name;
    }

    /**
     * Get the cell phone number of the user
     * 
     * @return the cell phone number
     */
    public String getCellPhone() {
        return cellPhone;
    }

    /**
     * Check if the cell phone number of the user is validated
     * 
     * @return boolean true if validated, else false
     */
    public boolean isCellPhoneValidated() {
        return cellPhoneValidated;
    }

    /**
     * Get the list of pois of this user
     * 
     * @return the list of pois
     * @see POI
     */
    public ArrayList<POI> getPointsOfInterest() {
        return (ArrayList<POI>) pointsOfInterest.clone();
    }

    /**
     * Get the list of travels of this user
     * 
     * @return the list of travels
     * @see Travel
     */
    public ArrayList<Travel> getTravels() {
        return (ArrayList<Travel>) travels.clone();
    }

    /**
     * Add a poi to the list of pois
     * 
     * @param poi the poi to be added
     * @see POI
     */
    public void addPOI(POI poi) {
        if (!isPointsOfInterestLoaded()) {
            pointsOfInterest = new ArrayList<>();
        }
        if (poi != null) {
            pointsOfInterest.add(poi);
        }
    }

    /**
     * Remove a poi of the list of pois
     * 
     * @param poiIdentifier identifier of the poi to be removed
     * @see POI
     */
    public void removePOI(int poiIdentifier) {
        if (isPointsOfInterestLoaded()) {
            for (int i = 0; i < pointsOfInterest.size(); i++) {
                try {
                    POI p = pointsOfInterest.get(i);
                    if (p.getPoiIdentifier() == poiIdentifier) {
                        pointsOfInterest.remove(p);
                        break;
                    }
                } catch (IllegalAccessException ex) {
                    Logger.getLogger(User.class.getName()).log(Level.SEVERE, "POI should have an Id", ex);
                }
            }
        }
    }

    /**
     * Add a travel to the list of travels
     * 
     * @param travel the ravel to be added
     * @see Travel
     */
    public void addTravel(Travel travel) {
        if (!isTravelsLoaded()) {
            travels = new ArrayList<>();
        }
        if (travel != null) {
            travels.add(travel);
        }
    }

    /**
     * Remove a travel of the list of travels
     * 
     * @param travelIdentifier identifier of the travel to be removed
     * @see Travel
     */
    public void removeTravel(int travelIdentifier) {
        if (isTravelsLoaded()) {
            for (int i = 0; i < travels.size(); i++) {
                try {
                    Travel t = travels.get(i);
                    if (t.getTravelIdentifier() == travelIdentifier) {
                        travels.remove(t);
                        break;
                    }
                } catch (IllegalAccessException ex) {
                    Logger.getLogger(User.class.getName()).log(Level.SEVERE, "Travel should have an Id", ex);
                }
            }
        }
    }

    /**
     * Check if the pois of this user are loaded
     * 
     * @return true if laoded, else false
     */
    public boolean isPointsOfInterestLoaded() {
        return pointsOfInterest != null;
    }

    /**
     * Check if the travels of this user are loaded
     * 
     * @return true if laoded, else false
     */
    public boolean isTravelsLoaded() {
        return travels != null;
    }
}
