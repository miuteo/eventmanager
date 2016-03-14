'use strict';

angular.module('eventsmanagerApp')
    .factory('ResourceSearch', function ($resource) {
        return $resource('api/_search/resources/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
