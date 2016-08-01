package vop.groep7.vop7backend.Models.Domain;

/**
 *
 * @author Backend Team
 */
public interface Recurring {
    
    /**
     * Check if the recurring is applicable at the moment of the method call
     * 
     * @return boolean, true if applicable 
     */
    public boolean isApplicableNow();
    
}
