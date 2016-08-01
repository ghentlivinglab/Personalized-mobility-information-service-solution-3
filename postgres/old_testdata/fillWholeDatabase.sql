INSERT INTO recurring
                (
                monday,
                tuesday,
                wednesday,
                thursday,
                friday,
                saturday,
                sunday
                ) VALUES ( true,true,true,true,true,true,true);

                INSERT INTO person
                (
                id,
                first_name,
                last_name,
                email,
                cell_number,
                mute_notifications,
                validated_email,
                validated_cell_number,
                facebook,
                twitter,
                snapchat,
                whatsapp
                )
                VALUES (1,
'Liam',
'Vermeir',
'Liam.Vermeir1@hotmail.com',
'+32481650919',
False,
False,
True,
'www.facebook.com/046023204008260',
'www.twitter.com/636935231417598',
'www.snapchat.com/320870894842762',
'www.whatsapp.com/100447163546863');
 INSERT INTO address
                (
                city,
                postal_code,
                street,
                housenumber,
                country,
                lat,
                lon
                ) VALUES (
                'Gent',
'9320',
'Begijnhof',
'623',
'Belgie',
51.0588382,
3.7088733);
 INSERT INTO pointsOfInterest
                (
                user_id,
                address_id,
                name,
                radius,
                active,
                email_notify,
                cell_number_notify
                ) VALUES(1,
1,
'LiamVermeir1',
300,
True,
False,
True);
 INSERT INTO travel
                (
                user_id,
                notify_start,
                notify_stop,
                name,
                recurring_id,
                startpoint,
                endpoint
                ) VALUES (
                1,
'12:00',
'13:00',
'TravelTest',
1,
1,
1);
 INSERT INTO route
                (
                user_id,
                travel_id,
                transport,
                email_notify,
                cell_number_notify,
                active
                ) VALUES (
                1,
1,
'streetcar',
False,
False,
True);
 INSERT INTO waypoint
                (
                lat,
                lon,
                route_id
                ) VALUES (
                49.8693993336,
3.34326390705,
1);
 INSERT INTO eventtypeRoute
                (
                route_id,
                event_type
                ) VALUES (1,
'Jam');
 INSERT INTO eventtypePOI
                (
                poi_id,
                event_type
                ) VALUES (
                1,
'Jam');
 INSERT INTO credentials
                (
                password,
                user_id
                ) VALUES (
                '6SgpjTFam4',
1);
INSERT INTO recurring
                (
                monday,
                tuesday,
                wednesday,
                thursday,
                friday,
                saturday,
                sunday
                ) VALUES ( true,true,true,true,true,true,true);

                INSERT INTO person
                (
                id,
                first_name,
                last_name,
                email,
                cell_number,
                mute_notifications,
                validated_email,
                validated_cell_number,
                facebook,
                twitter,
                snapchat,
                whatsapp
                )
                VALUES (2,
'Liam',
'Vermeir',
'Liam.Vermeir2@skynet.be',
'+32472352253',
True,
False,
True,
'www.facebook.com/621825547740189',
'www.twitter.com/806199253180751',
'www.snapchat.com/863774856290362',
'www.whatsapp.com/875359195255857');
 INSERT INTO address
                (
                city,
                postal_code,
                street,
                housenumber,
                country,
                lat,
                lon
                ) VALUES (
                'Antwerpen',
'6689',
'Beekkant',
'359',
'Belgie',
51.0823564,
3.5744039);
 INSERT INTO pointsOfInterest
                (
                user_id,
                address_id,
                name,
                radius,
                active,
                email_notify,
                cell_number_notify
                ) VALUES(2,
2,
'LiamVermeir2',
500,
False,
True,
True);
 INSERT INTO travel
                (
                user_id,
                notify_start,
                notify_stop,
                name,
                recurring_id,
                startpoint,
                endpoint
                ) VALUES (
                2,
'12:00',
'13:00',
'TravelTest',
2,
2,
2);
 INSERT INTO route
                (
                user_id,
                travel_id,
                transport,
                email_notify,
                cell_number_notify,
                active
                ) VALUES (
                2,
2,
'train',
False,
True,
False);
 INSERT INTO waypoint
                (
                lat,
                lon,
                route_id
                ) VALUES (
                50.8806750995,
4.19368337218,
2);
 INSERT INTO eventtypeRoute
                (
                route_id,
                event_type
                ) VALUES (2,
'Jam');
 INSERT INTO eventtypePOI
                (
                poi_id,
                event_type
                ) VALUES (
                2,
'Jam');
 INSERT INTO credentials
                (
                password,
                user_id
                ) VALUES (
                'UdINva51bz',
2);
INSERT INTO recurring
                (
                monday,
                tuesday,
                wednesday,
                thursday,
                friday,
                saturday,
                sunday
                ) VALUES ( true,true,true,true,true,true,true);

                INSERT INTO person
                (
                id,
                first_name,
                last_name,
                email,
                cell_number,
                mute_notifications,
                validated_email,
                validated_cell_number,
                facebook,
                twitter,
                snapchat,
                whatsapp
                )
                VALUES (3,
'Liam',
'Vermeir',
'Liam.Vermeir3@ugent.be',
'+32476707618',
True,
True,
False,
'www.facebook.com/245554953683296',
'www.twitter.com/004814652960497',
'www.snapchat.com/726433106437629',
'www.whatsapp.com/905500863725908');
 INSERT INTO address
                (
                city,
                postal_code,
                street,
                housenumber,
                country,
                lat,
                lon
                ) VALUES (
                'Brussel',
'4498',
'Begijnhof',
'545',
'Belgie',
51.0823564,
3.5744039);
 INSERT INTO pointsOfInterest
                (
                user_id,
                address_id,
                name,
                radius,
                active,
                email_notify,
                cell_number_notify
                ) VALUES(3,
3,
'LiamVermeir3',
150,
False,
False,
True);
 INSERT INTO travel
                (
                user_id,
                notify_start,
                notify_stop,
                name,
                recurring_id,
                startpoint,
                endpoint
                ) VALUES (
                3,
'12:00',
'13:00',
'TravelTest',
3,
3,
3);
 INSERT INTO route
                (
                user_id,
                travel_id,
                transport,
                email_notify,
                cell_number_notify,
                active
                ) VALUES (
                3,
3,
'train',
False,
False,
True);
 INSERT INTO waypoint
                (
                lat,
                lon,
                route_id
                ) VALUES (
                51.5655472536,
4.53399677812,
3);
 INSERT INTO eventtypeRoute
                (
                route_id,
                event_type
                ) VALUES (3,
'Weatherhazard');
 INSERT INTO eventtypePOI
                (
                poi_id,
                event_type
                ) VALUES (
                3,
'Weatherhazard');
 INSERT INTO credentials
                (
                password,
                user_id
                ) VALUES (
                'PwiBzotySH',
3);
INSERT INTO recurring
                (
                monday,
                tuesday,
                wednesday,
                thursday,
                friday,
                saturday,
                sunday
                ) VALUES ( true,true,true,true,true,true,true);

                INSERT INTO person
                (
                id,
                first_name,
                last_name,
                email,
                cell_number,
                mute_notifications,
                validated_email,
                validated_cell_number,
                facebook,
                twitter,
                snapchat,
                whatsapp
                )
                VALUES (4,
'Liam',
'Vermeir',
'Liam.Vermeir4@telenet.be',
'+32489737294',
True,
False,
False,
'www.facebook.com/971973420484076',
'www.twitter.com/054012931398103',
'www.snapchat.com/137386972270838',
'www.whatsapp.com/203867682033793');
 INSERT INTO address
                (
                city,
                postal_code,
                street,
                housenumber,
                country,
                lat,
                lon
                ) VALUES (
                'Lokeren',
'9389',
'Baron de Meerstraat',
'101',
'Belgie',
51.0823564,
3.5744039);
 INSERT INTO pointsOfInterest
                (
                user_id,
                address_id,
                name,
                radius,
                active,
                email_notify,
                cell_number_notify
                ) VALUES(4,
4,
'LiamVermeir4',
270,
False,
True,
True);
 INSERT INTO travel
                (
                user_id,
                notify_start,
                notify_stop,
                name,
                recurring_id,
                startpoint,
                endpoint
                ) VALUES (
                4,
'12:00',
'13:00',
'TravelTest',
4,
4,
4);
 INSERT INTO route
                (
                user_id,
                travel_id,
                transport,
                email_notify,
                cell_number_notify,
                active
                ) VALUES (
                4,
4,
'bike',
False,
True,
False);
 INSERT INTO waypoint
                (
                lat,
                lon,
                route_id
                ) VALUES (
                50.6146315908,
3.3628637288,
4);
 INSERT INTO eventtypeRoute
                (
                route_id,
                event_type
                ) VALUES (4,
'Weatherhazard');
 INSERT INTO eventtypePOI
                (
                poi_id,
                event_type
                ) VALUES (
                4,
'Jam');
 INSERT INTO credentials
                (
                password,
                user_id
                ) VALUES (
                '6ucROEl855',
4);
INSERT INTO recurring
                (
                monday,
                tuesday,
                wednesday,
                thursday,
                friday,
                saturday,
                sunday
                ) VALUES ( true,true,true,true,true,true,true);

                INSERT INTO person
                (
                id,
                first_name,
                last_name,
                email,
                cell_number,
                mute_notifications,
                validated_email,
                validated_cell_number,
                facebook,
                twitter,
                snapchat,
                whatsapp
                )
                VALUES (5,
'Liam',
'Vermeir',
'Liam.Vermeir5@msn.be',
'+32498867167',
False,
True,
True,
'www.facebook.com/624051990202877',
'www.twitter.com/638608342670706',
'www.snapchat.com/419030357148989',
'www.whatsapp.com/438285342069197');
 INSERT INTO address
                (
                city,
                postal_code,
                street,
                housenumber,
                country,
                lat,
                lon
                ) VALUES (
                'Aalst',
'3550',
'Abbeelstraat',
'583',
'Belgie',
50.9447505,
4.0214221);
 INSERT INTO pointsOfInterest
                (
                user_id,
                address_id,
                name,
                radius,
                active,
                email_notify,
                cell_number_notify
                ) VALUES(5,
5,
'LiamVermeir5',
300,
False,
False,
False);
 INSERT INTO travel
                (
                user_id,
                notify_start,
                notify_stop,
                name,
                recurring_id,
                startpoint,
                endpoint
                ) VALUES (
                5,
'12:00',
'13:00',
'TravelTest',
5,
5,
5);
 INSERT INTO route
                (
                user_id,
                travel_id,
                transport,
                email_notify,
                cell_number_notify,
                active
                ) VALUES (
                5,
5,
'bike',
False,
False,
False);
 INSERT INTO waypoint
                (
                lat,
                lon,
                route_id
                ) VALUES (
                49.7541271914,
4.61474509199,
5);
 INSERT INTO eventtypeRoute
                (
                route_id,
                event_type
                ) VALUES (5,
'Weatherhazard');
 INSERT INTO eventtypePOI
                (
                poi_id,
                event_type
                ) VALUES (
                5,
'Jam');
 INSERT INTO credentials
                (
                password,
                user_id
                ) VALUES (
                'BSqeqnPcCc',
5);
INSERT INTO recurring
                (
                monday,
                tuesday,
                wednesday,
                thursday,
                friday,
                saturday,
                sunday
                ) VALUES ( true,true,true,true,true,true,true);

                INSERT INTO person
                (
                id,
                first_name,
                last_name,
                email,
                cell_number,
                mute_notifications,
                validated_email,
                validated_cell_number,
                facebook,
                twitter,
                snapchat,
                whatsapp
                )
                VALUES (6,
'Liam',
'Vermeir',
'Liam.Vermeir6@live.com',
'+32499328177',
True,
False,
True,
'www.facebook.com/829102134108016',
'www.twitter.com/436526674503723',
'www.snapchat.com/653605531477953',
'www.whatsapp.com/531854244091624');
 INSERT INTO address
                (
                city,
                postal_code,
                street,
                housenumber,
                country,
                lat,
                lon
                ) VALUES (
                'Roeselare',
'4156',
'Aartstraat',
'986',
'Belgie',
51.0823564,
3.5744039);
 INSERT INTO pointsOfInterest
                (
                user_id,
                address_id,
                name,
                radius,
                active,
                email_notify,
                cell_number_notify
                ) VALUES(6,
6,
'LiamVermeir6',
800,
True,
False,
True);
 INSERT INTO travel
                (
                user_id,
                notify_start,
                notify_stop,
                name,
                recurring_id,
                startpoint,
                endpoint
                ) VALUES (
                6,
'12:00',
'13:00',
'TravelTest',
6,
6,
6);
 INSERT INTO route
                (
                user_id,
                travel_id,
                transport,
                email_notify,
                cell_number_notify,
                active
                ) VALUES (
                6,
6,
'train',
False,
True,
True);
 INSERT INTO waypoint
                (
                lat,
                lon,
                route_id
                ) VALUES (
                50.5166684752,
4.89370875799,
6);
 INSERT INTO eventtypeRoute
                (
                route_id,
                event_type
                ) VALUES (6,
'Weatherhazard');
 INSERT INTO eventtypePOI
                (
                poi_id,
                event_type
                ) VALUES (
                6,
'Weatherhazard');
 INSERT INTO credentials
                (
                password,
                user_id
                ) VALUES (
                'NfxRZhHTnv',
6);
INSERT INTO recurring
                (
                monday,
                tuesday,
                wednesday,
                thursday,
                friday,
                saturday,
                sunday
                ) VALUES ( true,true,true,true,true,true,true);

                INSERT INTO person
                (
                id,
                first_name,
                last_name,
                email,
                cell_number,
                mute_notifications,
                validated_email,
                validated_cell_number,
                facebook,
                twitter,
                snapchat,
                whatsapp
                )
                VALUES (7,
'Liam',
'Vermeir',
'Liam.Vermeir7@outlook.com',
'+32497664249',
False,
False,
False,
'www.facebook.com/762490402054917',
'www.twitter.com/415168692956342',
'www.snapchat.com/113962320283291',
'www.whatsapp.com/141324736624539');
 INSERT INTO address
                (
                city,
                postal_code,
                street,
                housenumber,
                country,
                lat,
                lon
                ) VALUES (
                'Brugge',
'8907',
'Bedrijvigheidstraat',
'265',
'Belgie',
51.0823564,
3.5744039);
 INSERT INTO pointsOfInterest
                (
                user_id,
                address_id,
                name,
                radius,
                active,
                email_notify,
                cell_number_notify
                ) VALUES(7,
7,
'LiamVermeir7',
110,
True,
True,
False);
 INSERT INTO travel
                (
                user_id,
                notify_start,
                notify_stop,
                name,
                recurring_id,
                startpoint,
                endpoint
                ) VALUES (
                7,
'12:00',
'13:00',
'TravelTest',
7,
7,
7);
 INSERT INTO route
                (
                user_id,
                travel_id,
                transport,
                email_notify,
                cell_number_notify,
                active
                ) VALUES (
                7,
7,
'streetcar',
False,
True,
False);
 INSERT INTO waypoint
                (
                lat,
                lon,
                route_id
                ) VALUES (
                50.0878289733,
4.89071582499,
7);
 INSERT INTO eventtypeRoute
                (
                route_id,
                event_type
                ) VALUES (7,
'Jam');
 INSERT INTO eventtypePOI
                (
                poi_id,
                event_type
                ) VALUES (
                7,
'Jam');
 INSERT INTO credentials
                (
                password,
                user_id
                ) VALUES (
                'Rl1H2Q9Sy2',
7);
INSERT INTO recurring
                (
                monday,
                tuesday,
                wednesday,
                thursday,
                friday,
                saturday,
                sunday
                ) VALUES ( true,true,true,true,true,true,true);

                INSERT INTO person
                (
                id,
                first_name,
                last_name,
                email,
                cell_number,
                mute_notifications,
                validated_email,
                validated_cell_number,
                facebook,
                twitter,
                snapchat,
                whatsapp
                )
                VALUES (8,
'Liam',
'Peeters',
'Liam.Peeters8@hotmail.com',
'+32469337869',
True,
False,
True,
'www.facebook.com/970508715200301',
'www.twitter.com/687490880835736',
'www.snapchat.com/932611740936362',
'www.whatsapp.com/241850459999494');
 INSERT INTO address
                (
                city,
                postal_code,
                street,
                housenumber,
                country,
                lat,
                lon
                ) VALUES (
                'Dendermonde',
'4382',
'Beekpad',
'858',
'Belgie',
51.032106,
3.7459811);
 INSERT INTO pointsOfInterest
                (
                user_id,
                address_id,
                name,
                radius,
                active,
                email_notify,
                cell_number_notify
                ) VALUES(8,
8,
'LiamPeeters8',
570,
True,
False,
True);
 INSERT INTO travel
                (
                user_id,
                notify_start,
                notify_stop,
                name,
                recurring_id,
                startpoint,
                endpoint
                ) VALUES (
                8,
'12:00',
'13:00',
'TravelTest',
8,
8,
8);
 INSERT INTO route
                (
                user_id,
                travel_id,
                transport,
                email_notify,
                cell_number_notify,
                active
                ) VALUES (
                8,
8,
'streetcar',
False,
True,
False);
 INSERT INTO waypoint
                (
                lat,
                lon,
                route_id
                ) VALUES (
                51.1195379825,
4.94736622035,
8);
 INSERT INTO eventtypeRoute
                (
                route_id,
                event_type
                ) VALUES (8,
'Weatherhazard');
 INSERT INTO eventtypePOI
                (
                poi_id,
                event_type
                ) VALUES (
                8,
'Weatherhazard');
 INSERT INTO credentials
                (
                password,
                user_id
                ) VALUES (
                'AdlY9KWUch',
8);
INSERT INTO recurring
                (
                monday,
                tuesday,
                wednesday,
                thursday,
                friday,
                saturday,
                sunday
                ) VALUES ( true,true,true,true,true,true,true);

                INSERT INTO person
                (
                id,
                first_name,
                last_name,
                email,
                cell_number,
                mute_notifications,
                validated_email,
                validated_cell_number,
                facebook,
                twitter,
                snapchat,
                whatsapp
                )
                VALUES (9,
'Liam',
'Peeters',
'Liam.Peeters9@skynet.be',
'+32467068977',
False,
False,
False,
'www.facebook.com/229955620032430',
'www.twitter.com/011418966552824',
'www.snapchat.com/659815206100919',
'www.whatsapp.com/025325391792622');
 INSERT INTO address
                (
                city,
                postal_code,
                street,
                housenumber,
                country,
                lat,
                lon
                ) VALUES (
                'Kortrijk',
'8117',
'Baron de Meerstraat',
'274',
'Belgie',
51.0823564,
3.5744039);
 INSERT INTO pointsOfInterest
                (
                user_id,
                address_id,
                name,
                radius,
                active,
                email_notify,
                cell_number_notify
                ) VALUES(9,
9,
'LiamPeeters9',
170,
False,
False,
True);
 INSERT INTO travel
                (
                user_id,
                notify_start,
                notify_stop,
                name,
                recurring_id,
                startpoint,
                endpoint
                ) VALUES (
                9,
'12:00',
'13:00',
'TravelTest',
9,
9,
9);
 INSERT INTO route
                (
                user_id,
                travel_id,
                transport,
                email_notify,
                cell_number_notify,
                active
                ) VALUES (
                9,
9,
'bike',
False,
True,
False);
 INSERT INTO waypoint
                (
                lat,
                lon,
                route_id
                ) VALUES (
                50.9146897204,
3.15446561156,
9);
 INSERT INTO eventtypeRoute
                (
                route_id,
                event_type
                ) VALUES (9,
'Weatherhazard');
 INSERT INTO eventtypePOI
                (
                poi_id,
                event_type
                ) VALUES (
                9,
'Weatherhazard');
 INSERT INTO credentials
                (
                password,
                user_id
                ) VALUES (
                '8HZ8FyKy1m',
9);
