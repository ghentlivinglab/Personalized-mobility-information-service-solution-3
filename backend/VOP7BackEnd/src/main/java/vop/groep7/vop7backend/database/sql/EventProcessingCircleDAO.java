/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vop.groep7.vop7backend.database.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Time;
import vop.groep7.vop7backend.Models.Domain.Coordinate;

/**
 *
 * @author Backend Team
 */
public class EventProcessingCircleDAO extends EventProcessingDAO {

    private final int deltaSquare;

    /**
     * Create an event processingDAO that works based on circles
     * 
     * @param connection The connection to the database
     */
    public EventProcessingCircleDAO(Connection connection) {
        super(connection);
        deltaSquare = DELTA * DELTA;
    }

    @Override
    protected PreparedStatement getPreparedStatement(int day, Time time, String eventType, Coordinate c) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(begin + "AND ((((w.lat-?)*?)*((w.lat-?)*?))+(((w.lon-?)*?)*((w.lon-?)*?)) < ?)" + middle + days.get(day) + end);
        ps.setString(1, eventType);
        ps.setDouble(2, c.getLat());
        ps.setDouble(3, meterDegreeLatitude);
        ps.setDouble(4, c.getLat());
        ps.setDouble(5, meterDegreeLatitude);

        ps.setDouble(6, c.getLon());
        ps.setDouble(7, meterDegreeLongitude);
        ps.setDouble(8, c.getLon());
        ps.setDouble(9, meterDegreeLongitude);

        ps.setInt(10, deltaSquare);
        ps.setTime(11, time);
        ps.setTime(12, time);

        return ps;
    }

}
