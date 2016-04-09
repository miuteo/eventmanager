(function() {
    'use strict';
    angular
        .module('eventmanagerApp')
        .factory('HomeInvitation', HomeInvitation);

    HomeInvitation.$inject = ['$resource', 'DateUtils'];

    function HomeInvitation ($resource, DateUtils) {
        var resourceUrl =  'api/homeinvitations';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.date = DateUtils.convertDateTimeFromServer(data.date);
                    debugger;
                    return data;
                }
            }
        });
    }
})();
