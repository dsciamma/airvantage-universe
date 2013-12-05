var airvantage = require('cloud/airvantage.js');

airvantage.initialize('AairVantage_client_id', 'airvantage_client_secret', 'airvantage_user', 'airvantage_password');

Parse.Cloud.define("getThing", function(request, response) {
	if (!request.params.uid || !request.user) {
        throw 'Missing initialization parameter';
    }
    else {
    	var currentUser = Parse.User.current();

    	var Thing = Parse.Object.extend("Thing");
		var query = new Parse.Query(Thing);
		query.equalTo("owner", currentUser);
		query.first({
		  success: function(object) {
		  	if (!object) {
				response.error("The current user doesn't have access to the thing '" + request.params.uid + "'");
		  	}
		  	else {
		        airvantage.system({
		  			uid: request.params.uid
		  		},
				{
					success: function(system) {
						response.success(system);
					}
				});
		  	}
		  },
		  error: function(error) {
			response.error("Unable to retrieve things for the current user.");
		  }
		});
    }
});

Parse.Cloud.define("getData", function(request, response) {
	if (!request.params.uid || !request.user) {
        throw 'Missing initialization parameter';
    }
    else {
    	var currentUser = Parse.User.current();

    	var Thing = Parse.Object.extend("Thing");
		var query = new Parse.Query(Thing);
		query.equalTo("owner", currentUser);
		query.first({
		  success: function(object) {
		  	if (!object) {
				response.error("The current user doesn't have access to the thing '" + request.params.uid + "'");
		  	}
		  	else {
		        airvantage.data(request.params.uid, request.params.data,
				{
					success: function(data) {
						response.success(data);
					}
				});
		  	}
		  },
		  error: function(error) {
			response.error("Unable to retrieve things for the current user.");
		  }
		});
    }
});

Parse.Cloud.define("getMaxHourlyData", function(request, response) {
	if (!request.params.uid || !request.user) {
        throw 'Missing initialization parameter';
    }
    else {
    	var currentUser = Parse.User.current();

    	var Thing = Parse.Object.extend("Thing");
		var query = new Parse.Query(Thing);
		query.equalTo("owner", currentUser);
		query.first({
		  success: function(object) {
		  	if (!object) {
				response.error("The current user doesn't have access to the thing '" + request.params.uid + "'");
		  	}
		  	else {
		        airvantage.hourly(request.params.uid, request.params.data, "max", 24,
				{
					success: function(data) {
						response.success(data);
					}
				});
		  	}
		  },
		  error: function(error) {
			response.error("Unable to retrieve things for the current user.");
		  }
		});
    }
});

Parse.Cloud.define("addThing", function(request, response) {
	if (!request.params.serial || !request.user) {
        throw 'Missing initialization parameter';
    }
    else {
    	// Get System from AirVantage
        airvantage.system({
  			"gateway": "serialNumber:" + request.params.serial
  		},
		{
			success: function(system) {		
				if (!system) {
					response.error("No Serial '" + request.params.serial + "' available");
				}
				else {
					// Search if system is not already registered
					var Thing = Parse.Object.extend("Thing");
					var query = new Parse.Query(Thing);
					query.equalTo("uid", system.uid);
					query.first({
					  success: function(object) {
					  	if (!object) {
					  		// Thing doesn't exist, we can create it for the current user					  		
					    	var currentUser = Parse.User.current();

					    	var name = request.params.name;
					    	if (!name) {
					    		name.system.name;
					    	}

							var Thing = Parse.Object.extend("Thing");
							var newThing = new Thing();
							 
							newThing.set("uid", system.uid);
							newThing.set("name", name);
							newThing.set("owner", currentUser);
							 
							newThing.save(null, {
							  success: function(thing) {
								response.success("Thing '" + system.uid + "' added to the current user");
							  },
							  error: function(thing, error) {
								response.error("Failed to add Thing '" + system.uid + "' to the current user");
							  }
							});
					  	}
					  	else {
							response.error("The thing '" + system.uid + "' is already registered.");
					  	}
					  },
					  error: function(error) {
						response.error("Unable to retrieve things.");
					  }
					});
				}
			}
		});

    }
});
