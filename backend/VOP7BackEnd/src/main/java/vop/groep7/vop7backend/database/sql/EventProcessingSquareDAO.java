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
public class EventProcessingSquareDAO extends EventProcessingDAO {

    private final double deltaRad;

    /**
     * Create an event processingDAO that works based on squares
     *
     * @param connection The connection to the database
     */
    public EventProcessingSquareDAO(Connection connection) {
        super(connection);
        deltaRad = 1.0 / 111249.0 * DELTA;
    }

    @Override
    protected PreparedStatement getPreparedStatement(int day, Time time, String eventType, Coordinate c) throws SQLException {
        Coordinate maxPoint, minPoint;
        minPoint = new Coordinate(c.getLat() - deltaRad, c.getLon() - deltaRad);
        maxPoint = new Coordinate(c.getLat() + deltaRad, c.getLon() + deltaRad);
        PreparedStatement ps = connection.prepareStatement(begin + "AND (? < w.lat) AND (w.lat < ?) AND (? < w.lon) AND (w.lon < ?)" + middle + days.get(day) + end);
        ps.setString(1, eventType);
        ps.setDouble(2, minPoint.getLat());
        ps.setDouble(3, maxPoint.getLat());

        ps.setDouble(4, minPoint.getLon());
        ps.setDouble(5, maxPoint.getLon());

        ps.setTime(6, time);
        ps.setTime(7, time);
        return ps;
    }

}
