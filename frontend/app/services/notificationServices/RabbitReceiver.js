var vopRabbit = angular.module('vopRabbit');

vopRabbit.factory('RabbitReceiver', ['$q', 'RABBITMQ', '$injector', function($q, RABBITMQ, $injector){
	return {
		client:null,
		active:1,
		nmsg: 0,

		startReceiving: function(){
		    var Session = $injector.get('Session');
			if(this.active && !this.client && (Session.isOpen() || Session.isLoggingIn())){
				this.client = true;
				this.nmsg = parseInt(Session.getUnseenEvents()) || 0;
				if(this.nmsg){
					alertUserForEvents(this.nmsg);
				}
				var deferred = $q.defer();
				var ressource = RABBITMQ.RESSOURCE_URL + Session.getUserId();
				var ws = new WebSocket(RABBITMQ.BROKER_URL);

                this.client = Stomp.over(ws);

                this.client.debug = undefined;
                var extra_output;
                var receiver = this;

                var on_connect = function () {
                    deferred.resolve();
                    subscription = receiver.client.subscribe(ressource, function (message) {
                        if (extra_output) {
                            var xtra_msg = "" + receiver.nmsg + ": Event " + (event.id || '[no_id]') + " occured on " + JSON.stringify(event.coordinates || '[no_coordinates]') + ":\n" + (event.description || '[no_description]');
                            extra_output(xtra_msg);
                        }
                        receiver.nmsg = receiver.nmsg + 1;
                        alertUserForEvents(receiver.nmsg);
                        Session.setUnseenEvents(receiver.nmsg);
                    });
                };

                var on_error = function () {
                    alert('Kon niet verbinden met de server');
                    deferred.reject();
                };

                this.client.connect(RABBITMQ.USERNAME, RABBITMQ.PWD, on_connect, on_error, RABBITMQ.VHOST);

                this.client.heartbeat.outgoing = 5000;
                this.client.heartbeat.incoming = 0;

                return deferred.promise;
            }
            return $q.resolve(this.client);
        },

        hasSeenEvents: function () {
            this.nmsg = 0;
            var Session = $injector.get('Session');
            Session.setUnseenEvents(this.nmsg);
            clearAlertForEvents();
            return $q.resolve();
        },

		stopReceiving: function(){
			if(this.client){
				var rabbit = this;
				this.client.disconnect(function() {
					rabbit.client = null;
				});
			}
		}
	}
}]);
