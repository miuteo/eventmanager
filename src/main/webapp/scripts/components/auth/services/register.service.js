'use strict';

angular.module('eventsmanagerApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


