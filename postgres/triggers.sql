CREATE OR REPLACE FUNCTION deletingAddress() RETURNS trigger AS $$
	BEGIN
		DELETE FROM address WHERE id = OLD.address_id;
		RETURN OLD;
	END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION deletingTravel() RETURNS trigger AS $$
	BEGIN
		DELETE FROM travel WHERE id = OLD.travel_id;
		RETURN OLD;
	END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER deleteAddress
AFTER DELETE
ON pointsOfInterest
FOR EACH ROW
EXECUTE PROCEDURE deletingAddress();

-- CREATE TRIGGER deleteTravel
-- AFTER DELETE
-- ON route
-- FOR EACH ROW
-- EXECUTE PROCEDURE deletingTravel();
