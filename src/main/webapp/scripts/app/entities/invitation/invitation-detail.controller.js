'use strict';

angular.module('eventsmanagerApp')
    .controller('InvitationDetailController', function ($scope, $rootScope, $stateParams, entity, Invitation, Event, User) {
        $scope.invitation = entity;
        $scope.load = function (id) {
            Invitation.get({id: id}, function(result) {
                $scope.invitation = result;
            });
        };
        var unsubscribe = $rootScope.$on('eventsmanagerApp:invitationUpdate', function(event, result) {
            $scope.invitation = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
