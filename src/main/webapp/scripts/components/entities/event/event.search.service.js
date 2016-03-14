'use strict';

angular.module('eventsmanagerApp')
    .factory('EventSearch', function ($resource) {
        return $resource('api/_search/events/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
