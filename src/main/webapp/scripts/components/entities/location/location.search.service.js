'use strict';

angular.module('eventsmanagerApp')
    .factory('LocationSearch', function ($resource) {
        return $resource('api/_search/locations/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
