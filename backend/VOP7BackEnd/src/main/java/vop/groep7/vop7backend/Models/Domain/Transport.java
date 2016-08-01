package vop.groep7.vop7backend.Models.Domain;

import java.util.Objects;
import vop.groep7.vop7backend.Models.DomainModel;

/**
 *
 * @author Backend Team
 */
public class Transport extends DomainModel {

    private final String transport;

    /**
     * Create a Transport object
     * 
     * @param transport the type of transportation as string
     */
    public Transport(String transport) {
        this.transport = transport;
    }

    /**
     * Get the transportation type as string
     * 
     * @return the transportation type as string
     */
    public String getTransport() {
        return transport;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 83 * hash + Objects.hashCode(this.transport);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Transport other = (Transport) obj;
        return Objects.equals(this.transport, other.transport);
    }
       
}
