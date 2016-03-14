'use strict';

angular.module('eventsmanagerApp')
    .factory('InvitationSearch', function ($resource) {
        return $resource('api/_search/invitations/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
