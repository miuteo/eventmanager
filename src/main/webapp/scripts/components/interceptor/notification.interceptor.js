 'use strict';

angular.module('eventsmanagerApp')
    .factory('notificationInterceptor', function ($q, AlertService) {
        return {
            response: function(response) {
                var alertKey = response.headers('X-eventsmanagerApp-alert');
                if (angular.isString(alertKey)) {
                    AlertService.success(alertKey, { param : response.headers('X-eventsmanagerApp-params')});
                }
                return response;
            }
        };
    });
