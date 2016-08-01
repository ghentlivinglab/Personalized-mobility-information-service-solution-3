SET DATABASE SQL SYNTAX PGS TRUE;
SET DATABASE SQL SIZE FALSE;

-- Table recurring --
INSERT INTO recurring(monday,tuesday,wednesday,thursday,friday,saturday,sunday) VALUES ( true,true,true,true,true,true,true);
INSERT INTO recurring(monday,tuesday,wednesday,thursday,friday,saturday,sunday) VALUES ( true,true,true,true,true,true,true);

-- Table person --
INSERT INTO person ( id, first_name, last_name, email, cell_number, mute_notifications, validated_email, validated_cell_number ) VALUES (4242, 'Liam','Vermeir','Liam.Vermeir1@hotmail.com','+32498047339',True, True, True);
INSERT INTO person ( id, first_name, last_name, email, cell_number, mute_notifications, validated_email, validated_cell_number ) VALUES (9127, 'Katja','Tolens','Katja.Tolens@skynet.be','+32478951761',False, True, True);

-- Table address -- 
INSERT INTO address ( city, postal_code, street, housenumber, country, lat, lon ) VALUES ( 'Brussel','2805','Babbelaarstraat','298','Belgie',50.4460734672,3.51132552398);
INSERT INTO address ( city, postal_code, street, housenumber, country, lat, lon ) VALUES ( 'Lokeren','9646','Beekstraat','747','Belgie',51.9356536715,4.27300638697);
INSERT INTO address ( city, postal_code, street, housenumber, country, lat, lon ) VALUES ( 'Antwerpen','5948','Bergekouter','548','Belgie',51.7000044444,3.52444296567);
INSERT INTO address ( city, postal_code, street, housenumber, country, lat, lon ) VALUES ( 'Aarschot','9788','Abrahamsweg','70','Belgie',49.3071734326,3.86378276825); 
INSERT INTO address ( city, postal_code, street, housenumber, country, lat, lon ) VALUES ( 'Roeselare','1501','Baron Romain Moyersoen Park','639','Belgie',51.8039984503,4.44875563993); 
INSERT INTO address ( city, postal_code, street, housenumber, country, lat, lon ) VALUES ( 'Kortrijk','7583','Beekstraat','766','Belgie',50.6855778022,4.74503707699);

-- Table poi --
INSERT INTO pointsOfInterest ( user_id, address_id, name, radius, active ) VALUES(4242,1,'LiamVermeir1',0,False);
INSERT INTO pointsOfInterest ( user_id, address_id, name, radius, active ) VALUES(9127,4,'KatjaTolens1',5,True);

-- Table travel --
INSERT INTO travel ( user_id, name, notify_start, notify_stop, recurring_id, startpoint, endpoint ) VALUES ( 4242,'LiamVermeir1','12:00:00','13:00:00',1,2,3);
INSERT INTO travel ( user_id, name, notify_start, notify_stop, recurring_id, startpoint, endpoint ) VALUES ( 9127,'KatjaTolens1','12:00:00','13:00:00',2,5,6);

-- Table route --
INSERT INTO route ( user_id, travel_id, transport, email_notify, cell_number_notify, active ) VALUES ( 4242, 1, 'CAR', False, False, True);
INSERT INTO route ( user_id, travel_id, transport, email_notify, cell_number_notify, active ) VALUES ( 9127, 2, 'BIKE', False, False, True);

-- Table waypoint --
INSERT INTO waypoint ( lat, lon, route_id ) VALUES ( 49.1370093293,3.05829755662,1);
INSERT INTO waypoint ( lat, lon, route_id ) VALUES ( 51.1479552842,3.86465416465,2);

-- Table eventtypeRoute --
INSERT INTO eventtypeRoute ( route_id, event_type ) VALUES (1, 'JAM_MODERATE_TRAFFIC');
INSERT INTO eventtypeRoute ( route_id, event_type ) VALUES (2, 'HAZARD_ON_ROAD_CONSTRUCTION');

-- Table eventtypePOI -- 
INSERT INTO eventtypePOI ( poi_id, event_type ) VALUES (1, 'ROAD_CLOSED');
INSERT INTO eventtypePOI ( poi_id, event_type ) VALUES (2, 'JAM_HEAVY_TRAFFIC');

-- Table credentials --
INSERT INTO credentials ( password, user_id, role ) VALUES ('747d010078da', 4242, 'ROLE_OPERATOR');
INSERT INTO credentials ( password, user_id, role ) VALUES ('877d015378da', 9127, 'ROLE_ADMIN');

-- Table userPins --
INSERT INTO userPins ( user_id, email_pin, password_reset_pin ) VALUES (4242, null, null);
INSERT INTO userPins ( user_id, email_pin, password_reset_pin ) VALUES (9127, null, null);

-- Table matchedEvents --
INSERT INTO matchedEvents ( user_id, event_id ) VALUES (4242, 1);
INSERT INTO matchedEvents ( user_id, event_id ) VALUES (9127, 1);
INSERT INTO matchedEvents ( user_id, event_id ) VALUES (4242, 2);

-- Table refreshToken --
INSERT INTO refreshToken ( user_id, token, time_last_used ) VALUES (4242, 123456789, '2001-09-11 15:00:00');

--Table interpolated_waypoints
INSERT INTO interpolated_waypoints ( lat, lon, route_id ) VALUES (51.8039984503, 4.44875563993, 2)

