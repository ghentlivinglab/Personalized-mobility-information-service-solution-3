package vop.groep7.vop7backend.factories;

import vop.groep7.vop7backend.Models.APIModels.APITransport;
import vop.groep7.vop7backend.Models.Domain.Transport;

/**
 *
 * @author Backend Team
 */
public class TransportFactory {
    
    /**
     * Create a new Transport object
     * 
     * @param type The type of transport
     * @return A new Transport object
     */
    public static Transport create(String type) {
        Transport transport = new Transport(type);
        return transport;
    }

    /**
     * Build a valid Transport object
     * 
     * @param type The type of transport
     * @return A valid Transport object
     */
    public static Transport build(String type) {
        Transport transport = new Transport(type);
        return transport;
    }

    /**
     * Create a Transport object starting from an APITransport
     * 
     * @param transport An APITransport object
     * @return A Transport object
     */
    public static Transport toDomain(APITransport transport) {
        String type = null;
        if(transport.getType()!=null){
            type=transport.getType();
        }
        Transport result = new Transport(type);
        return result;
    }

    /**
     * Create an APITransport object starting from a Transport
     * 
     * @param transport A Transport object
     * @return A APITransport object
     */
    public static APITransport toAPIModel(Transport transport) {
        String type = null;
        if(transport.getTransport()!=null){
            type=transport.getTransport();
        }
        APITransport result = APITransport.getTransport(type);
        return result;
    }
}
