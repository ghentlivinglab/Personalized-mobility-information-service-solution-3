/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Domain;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import vop.groep7.vop7backend.Models.Domain.Coordinate;
import vop.groep7.vop7backend.Models.Domain.Transport;

/**
 *
 * @author Jonas
 */
public class TransportTest {
    
    public TransportTest() {
    }
    
    @Test
    public void equals_shouldBeTrue() {
        Transport t1 = new Transport("car");
        Transport t2 = new Transport("car");
        assertTrue(t1.equals(t2));
    }

    @Test
    public void equals_shouldBeFalse() {
        Transport t1 = new Transport("car");
        Transport t2 = new Transport("bike");
        assertFalse(t1.equals(t2));
    }

    @Test
    public void equals_shouldBeFalse2() {
        Transport t1 = new Transport("bus");
        Transport t2 = null;
        assertFalse(t1.equals(t2));
    }

    @Test
    public void hashCode_shouldBeEqual() {
        Transport t1 = new Transport("car");
        Transport t2 = new Transport("car");
        assertTrue(t1.hashCode() == t2.hashCode());

    }

    @Test
    public void hashCode_shouldBeDifferent() {
        Transport t1 = new Transport("train");
        Transport t2 = new Transport("streetcar");
        assertFalse(t1.hashCode() == t2.hashCode());
    }
}
