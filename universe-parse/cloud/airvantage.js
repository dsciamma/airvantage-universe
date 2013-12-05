

(function() {

	var baseUrl = 'https://na.airvantage.net/api';
	var clientId = '';
	var clientSecret = '';
	var user = '';
	var password = '';

	authenticate = function(options) {
		return Parse.Cloud.httpRequest({
	        url: baseUrl + '/oauth/token',
	  		params: {
	    		grant_type: 'password',
	    		username: user,
	    		password: password,
	    		client_id: clientId,
	    		client_secret: clientSecret
	  		},
      	}).then(function(httpResponse) {
	        if (options && options.success) {
		    	console.log('token: ' + httpResponse.data.access_token);
	          	options.success(httpResponse.data.access_token);
	        }
      	}, function(httpResponse) {
      		console.error(httpResponse);
	        if (options && options.error) {
	          options.error('Unable to get token');
	        }
      	});
    };

  module.exports = {
    /**
     * Get the version of the module.
     * @return {String}
     */
    version: '1.0.0',

    initialize: function(id, secret, u, p) {
		//Required client config
	    if (!id || !secret || !u || !p) {
	        throw 'Missing initialization parameter';
	    }
	    else {
	        //if auth param passed in manually, trim spaces
	        clientId = id.replace(/ /g,'');
	        clientSecret = secret.replace(/ /g,'');
	        user = u.replace(/ /g,'');
	        password = p.replace(/ /g,'');
	    }
	    return this;
	},

    system: function(params, options) {
    	return authenticate(
		{
			success: function(token) {
				params.fields = 'uid,name,gateway,subscriptions';
				params.access_token = token;
		      	Parse.Cloud.httpRequest({
		        	url: baseUrl + '/v1/systems',
			  		params: params,
		      	}).then(function(httpResponse) {
		        	if (options && options.success) {
		          		options.success(httpResponse.data.items[0]);
		        	}
		      	}, function(httpResponse) {
		      		console.error(httpResponse);
		        	if (options && options.error) {
		          		options.error(httpResponse);
		        	}
		      	});
			}
		});
    },

    data: function(uid, data, options) {
    	return authenticate(
		{
			success: function(token) {
		      	Parse.Cloud.httpRequest({
		        	url: baseUrl + '/v1/systems/' + uid +'/data',
			  		params: {
			  			ids: data,
			  			access_token: token
			  		},
		      	}).then(function(httpResponse) {
		        	if (options && options.success) {
		        		ids = data.split(',');
		        		values = httpResponse.data;
		        		result = {};
		        		for (var i = 0; i < ids.length; i++) { 
					    	result[ids[i]] = values[ids[i]][0].value;
					    }
		          		options.success(result);
		        	}
		      	}, function(httpResponse) {
		      		console.error(httpResponse);
		        	if (options && options.error) {
		          		options.error(httpResponse);
		        	}
		      	});
			}
		});
    },

    hourly: function(uid, data, fn, size, options) {
    	return authenticate(
		{
			success: function(token) {
		      	Parse.Cloud.httpRequest({
		        	url: baseUrl + '/v1/systems/' + uid +'/data/' + data + '/aggregated',
			  		params: {
			  			access_token: token,
			  			fn: fn,
			  			size: size
			  		},
		      	}).then(function(httpResponse) {
		        	if (options && options.success) {
		          		options.success(httpResponse.data);
		        	}
		      	}, function(httpResponse) {
		      		console.error(httpResponse);
		        	if (options && options.error) {
		          		options.error(httpResponse);
		        	}
		      	});
			}
		});
    }

  }
}());
