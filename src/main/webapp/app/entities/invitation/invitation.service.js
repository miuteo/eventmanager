(function() {
    'use strict';
    angular
        .module('eventmanagerApp')
        .factory('Invitation', Invitation);

    Invitation.$inject = ['$resource', 'DateUtils'];

    function Invitation ($resource, DateUtils) {
        var resourceUrl =  'api/invitations/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.date = DateUtils.convertDateTimeFromServer(data.date);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
