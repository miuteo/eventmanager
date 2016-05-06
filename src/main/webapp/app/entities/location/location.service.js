(function() {
    'use strict';
    angular
        .module('eventmanagerApp')
        .factory('Location', Location);

    Location.$inject = ['$resource', 'DateUtils'];

    function Location ($resource, DateUtils) {
        var resourceUrl =  'api/locations/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.date_created = DateUtils.convertDateTimeFromServer(data.date_created);
                    data.last_updated = DateUtils.convertDateTimeFromServer(data.last_updated);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
