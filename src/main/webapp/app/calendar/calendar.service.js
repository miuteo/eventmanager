(function() {
    'use strict';
    angular
        .module('eventmanagerApp')
        .factory('CalendarService', CalendarService);

    CalendarService.$inject = ['$resource', 'DateUtils'];

    function CalendarService ($resource, DateUtils) {
        var resourceUrl =  'api/acceptedinvitations/:month';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true },
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.date = DateUtils.convertLocaleDateFromServer(data.date);
                    return data;
                }
            }
        });
    }
})();

