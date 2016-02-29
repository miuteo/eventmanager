'use strict';

angular.module('eventsmanagerApp')
    .controller('LocationDetailController', function ($scope, $rootScope, $stateParams, entity, Location, Event) {
        $scope.location = entity;
        $scope.load = function (id) {
            Location.get({id: id}, function(result) {
                $scope.location = result;
            });
        };
        var unsubscribe = $rootScope.$on('eventsmanagerApp:locationUpdate', function(event, result) {
            $scope.location = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
