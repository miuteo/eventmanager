'use strict';

angular.module('eventsmanagerApp')
    .factory('Resource', function ($resource, DateUtils) {
        return $resource('api/resources/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.dateCreated = DateUtils.convertDateTimeFromServer(data.dateCreated);
                    data.lastUpdated = DateUtils.convertDateTimeFromServer(data.lastUpdated);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
