package vop.groep7.vop7backend.Datasources;

/**
 *
 * @author Backend Team
 */
public interface DataSource {
     
    /**
     * Check the datasource for updates
     */
    public void checkDataSource();
    
    /**
     * Update appropriate events to inactive
     */
    public void updateInactive();
    
    /**
     * Delete appropriate inactive events
     */
    public void deleteInactive();
    
}
