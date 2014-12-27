define([
	'backbone'
], function(Backbone) {
	return function(method, model, options) {
        var methodMap = {
            'create': {
                method: 'POST',
                url: "/auth",
                success: function (resp) {
                    if (resp.status == 1) {
                        model.clear();
                        model.trigger('successAuth');
                    }
                    else if (resp.status == 2) {
                        model.trigger('failAuth', resp.message);
                    }
                },
                error: function () {
                    console.log('rett');
                    model.trigger('signup:error');
                }
            },
            'read': {
                method: 'GET',
                url: "/auth",
                success: function (resp) {
                    if (resp.status == 1) {
                    model.login=resp.user;
                        model.set({
                            login: resp.user,
                            'email': resp.email,
                            'isLoggedIn': true
                        });
                        model.trigger('main:known');
                    } else {
                        model.trigger('main:anonymous');
                    }
                }
            },
            'update': {
                method: 'POST',
                url: model.has('password') ? model.loginUrl : model.logoutUrl,
                success: function (resp) {
                    if (model.has('password')) {
                        if (resp.status === 200) {
                            model.set({
                                'login': resp.login,
                                'email': resp.email,
                                'isLogined': true
                            });
                            debugger;
                            model.unset('password');
                            model.trigger('login:ok');
                        }
                        else if (resp.status === 500) {
                            model.trigger('login:bad', resp.message);
                        }
                    }
                    else {
                        model.clear();
                        model.trigger('logout');
                    }
                },
                error: function () {
                    if (model.has('password')) {
                        model.trigger('login:error');
                    }
                    else {
                        model.trigger('logout:error');
                    }
                }
            }
        };
        var type = methodMap[method].method,
	            url = methodMap[method].url,
	            success = methodMap[method].success,
	            error = methodMap[method].error;

        var xhr = $.ajax({
            type: type,
            url: url,
            data: (model instanceof Backbone.Model) ? model.toJSON() : {},
            success: success,
            error: error,
            dataType: 'json'
        });
	};
});
	