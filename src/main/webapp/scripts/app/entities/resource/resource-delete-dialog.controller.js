'use strict';

angular.module('eventsmanagerApp')
	.controller('ResourceDeleteController', function($scope, $uibModalInstance, entity, Resource) {

        $scope.resource = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Resource.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
