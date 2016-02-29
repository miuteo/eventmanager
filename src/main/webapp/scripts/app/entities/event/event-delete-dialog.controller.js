'use strict';

angular.module('eventsmanagerApp')
	.controller('EventDeleteController', function($scope, $uibModalInstance, entity, Event) {

        $scope.event = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Event.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
