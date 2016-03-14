'use strict';

angular.module('eventsmanagerApp')
    .controller('EventDetailController', function ($scope, $rootScope, $stateParams, entity, Event, User) {
        $scope.event = entity;
        $scope.load = function (id) {
            Event.get({id: id}, function(result) {
                $scope.event = result;
            });
        };
        var unsubscribe = $rootScope.$on('eventsmanagerApp:eventUpdate', function(event, result) {
            $scope.event = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
