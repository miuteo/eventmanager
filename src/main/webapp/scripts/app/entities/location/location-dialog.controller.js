'use strict';

angular.module('eventsmanagerApp').controller('LocationDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Location', 'Event',
        function($scope, $stateParams, $uibModalInstance, entity, Location, Event) {

        $scope.location = entity;
        $scope.events = Event.query();
        $scope.load = function(id) {
            Location.get({id : id}, function(result) {
                $scope.location = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('eventsmanagerApp:locationUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.location.id != null) {
                Location.update($scope.location, onSaveSuccess, onSaveError);
            } else {
                Location.save($scope.location, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
