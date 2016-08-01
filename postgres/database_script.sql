DROP DOMAIN IF EXISTS trans CASCADE;
CREATE DOMAIN trans AS varchar
CHECK(VALUE IN ('bike', 'car', 'bus', 'streetcar', 'train'));


DROP TABLE IF EXISTS recurring CASCADE;
CREATE TABLE recurring (
	id serial NOT NULL,
	monday boolean NOT NULL,
	tuesday boolean NOT NULL,
	wednesday boolean NOT NULL,
	thursday boolean NOT NULL,
	friday boolean NOT NULL,
	saturday boolean NOT NULL,
	sunday boolean NOT NULL,
	PRIMARY KEY(id)
);

DROP TABLE IF EXISTS person CASCADE;
CREATE TABLE person (
	id integer NOT NULL,
	first_name varchar(30),
	last_name varchar(50),
	email varchar(100) NOT NULL UNIQUE CHECK(email LIKE '%@%.%'),
	cell_number varchar(20),
	mute_notifications boolean DEFAULT (false),
	validated_email boolean DEFAULT (false),
	validated_cell_number boolean DEFAULT (false),
	PRIMARY KEY(id)
);

DROP TABLE IF EXISTS address CASCADE;
CREATE TABLE address (
	id serial NOT NULL,
	city varchar NOT NULL,
	postal_code integer NOT NULL,
	street varchar NOT NULL,
	housenumber varchar(10) NOT NULL,
	country varchar NOT NULL,
	lat real CHECK(lat>=-90.0 AND lat <= 90.0),
	lon real CHECK(lon>=-180.0 AND lon <= 180.0),
	PRIMARY KEY(id)
);

DROP TABLE IF EXISTS pointsOfInterest CASCADE;
CREATE TABLE pointsOfInterest (
	id serial NOT NULL,
	user_id integer NOT NULL,
	address_id integer NOT NULL,
	name varchar,
	radius integer,
	active boolean,
	email_notify boolean DEFAULT(false),
	cell_number_notify boolean DEFAULT (false),
	PRIMARY KEY (id),
	FOREIGN KEY(user_id) REFERENCES person ON DELETE CASCADE,
	FOREIGN KEY(address_id) REFERENCES address ON DELETE CASCADE
);

DROP TABLE IF EXISTS travel CASCADE;
CREATE TABLE travel (
	id serial NOT NULL,
	user_id integer NOT NULL,
	notify_start time NOT NULL,
	notify_stop time NOT NULL,
	name varchar NOT NULL,
	recurring_id integer,
	-- Integer key for address table
	startpoint integer NOT NULL,
	-- Integer key for address table
	endpoint integer NOT NULL,
	PRIMARY KEY(id),
	FOREIGN KEY (user_id) REFERENCES person ON DELETE CASCADE,
	FOREIGN KEY(recurring_id) REFERENCES recurring ON DELETE CASCADE,
	FOREIGN KEY(startpoint) REFERENCES address ON DELETE CASCADE,
	FOREIGN KEY(endpoint) REFERENCES address ON DELETE CASCADE
);

DROP TABLE IF EXISTS route CASCADE;
CREATE TABLE route (
	id serial NOT NULL,
	user_id integer,
	travel_id integer,
	transport trans NOT NULL,
	email_notify boolean DEFAULT(false),
	cell_number_notify boolean DEFAULT (false),
	active boolean DEFAULT (true),
	PRIMARY KEY(id),
	FOREIGN KEY(user_id) REFERENCES person ON DELETE CASCADE,
	FOREIGN KEY(travel_id) REFERENCES travel ON DELETE CASCADE
);


DROP TABLE IF EXISTS waypoint CASCADE;
CREATE TABLE waypoint(
	id serial NOT NULL,
	lat real NOT NULL CHECK(lat>=-90.0 AND lat <= 90.0),
	lon real NOT NULL CHECK(lon>=-180.0 AND lon <= 180.0),
	route_id integer NOT NULL,
	PRIMARY KEY(id),
	FOREIGN KEY(route_id) REFERENCES route ON DELETE CASCADE
);

DROP TABLE IF EXISTS eventtypeRoute CASCADE;
CREATE TABLE eventtypeRoute(
	route_id integer NOT NULL,
	event_type varchar NOT NULL,
	PRIMARY KEY(route_id, event_type),
	FOREIGN KEY(route_id) REFERENCES route ON DELETE CASCADE
);

DROP TABLE IF EXISTS eventtypePOI CASCADE;
CREATE TABLE eventtypePOI(
	poi_id integer NOT NULL,
	event_type varchar NOT NULL,
	PRIMARY KEY(poi_id, event_type),
	FOREIGN KEY(poi_id) REFERENCES pointsOfInterest ON DELETE CASCADE
);

DROP DOMAIN IF EXISTS authority CASCADE;
CREATE DOMAIN authority AS varchar
CHECK(VALUE IN ('ROLE_ADMIN', 'ROLE_OPERATOR', 'ROLE_USER'));

DROP TABLE IF EXISTS credentials CASCADE;
CREATE TABLE credentials(
	password bytea NOT NULL,
	user_id integer NOT NULL,
	role authority NOT NULL DEFAULT 'ROLE_USER',
	PRIMARY KEY(user_id),
	FOREIGN KEY(user_id) REFERENCES person ON DELETE CASCADE
);

DROP TABLE IF EXISTS matchedEvents CASCADE;
CREATE TABLE matchedEvents(
	user_id integer NOT NULL,
	event_id integer NOT NULL,
	PRIMARY KEY(user_id, event_id)
);

CREATE INDEX matched_events_index ON matchedEvents (event_id);

DROP TABLE IF EXISTS userPins CASCADE;
CREATE TABLE userPins(
	user_id	integer NOT NULL,
	email_pin varchar(20) DEFAULT NULL,
	password_reset_pin varchar(20) DEFAULT NULL,
	PRIMARY KEY(user_id),
	FOREIGN KEY(user_id) REFERENCES person ON DELETE CASCADE
);

DROP TABLE IF EXISTS interpolated_waypoints CASCADE;
CREATE TABLE interpolated_waypoints(
    id serial NOT NULL,
    lat real NOT NULL CHECK(lat>=-90.0 AND lat <= 90.0),
    lon real NOT NULL CHECK(lon>=-180.0 AND lon <= 180.0),
    route_id integer NOT NULL,
    PRIMARY KEY(id),
    FOREIGN KEY(route_id) REFERENCES route ON DELETE CASCADE
);

DROP TABLE IF EXISTS refreshToken CASCADE;
CREATE TABLE refreshToken(
	user_id integer NOT NULL,
	token varchar(100) NOT NULL,
	time_last_used timestamp NOT NULL,
	PRIMARY KEY(token)
);

-- Permissions
GRANT SELECT,UPDATE,INSERT,DELETE ON ALL TABLES IN SCHEMA public TO backend;
GRANT USAGE,SELECT ON ALL SEQUENCES IN SCHEMA public TO backend;
