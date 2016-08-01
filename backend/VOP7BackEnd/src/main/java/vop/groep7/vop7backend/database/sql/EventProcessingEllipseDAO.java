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
public class EventProcessingEllipseDAO extends EventProcessingDAO {

    private final double ellipseDelta;

    /**
     * Create an event processingDAO that works based on ellipses
     *
     * @param connection The connection to the database
     */
    public EventProcessingEllipseDAO(Connection connection) {
        super(connection);
        ellipseDelta = DELTA * DELTA / ((meterDegreeLatitude + meterDegreeLongitude) / 2.0);
    }

    @Override
    protected PreparedStatement getPreparedStatement(int day, Time time, String eventType, Coordinate c) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(begin + "AND ((((w.lat-?)*(w.lat-?))+((w.lon-?)*(w.lon-?))) < ?)" + middle + days.get(day) + end);
        ps.setString(1, eventType);
        ps.setDouble(2, c.getLat());
        ps.setDouble(3, c.getLat());

        ps.setDouble(4, c.getLon());
        ps.setDouble(5, c.getLon());

        ps.setDouble(6, ellipseDelta);
        ps.setTime(7, time);
        ps.setTime(8, time);
        return ps;
    }

}
