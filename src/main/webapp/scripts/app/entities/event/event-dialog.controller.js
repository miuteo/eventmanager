'use strict';

angular.module('eventsmanagerApp').controller('EventDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Event', 'User',
        function($scope, $stateParams, $uibModalInstance, entity, Event, User) {

        $scope.event = entity;
        $scope.users = User.query();
        $scope.load = function(id) {
            Event.get({id : id}, function(result) {
                $scope.event = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('eventsmanagerApp:eventUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.event.id != null) {
                Event.update($scope.event, onSaveSuccess, onSaveError);
            } else {
                Event.save($scope.event, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.datePickerForDate = {};

        $scope.datePickerForDate.status = {
            opened: false
        };

        $scope.datePickerForDateOpen = function($event) {
            $scope.datePickerForDate.status.opened = true;
        };
        $scope.datePickerForDateCreated = {};

        $scope.datePickerForDateCreated.status = {
            opened: false
        };

        $scope.datePickerForDateCreatedOpen = function($event) {
            $scope.datePickerForDateCreated.status.opened = true;
        };
        $scope.datePickerForLastUpdated = {};

        $scope.datePickerForLastUpdated.status = {
            opened: false
        };

        $scope.datePickerForLastUpdatedOpen = function($event) {
            $scope.datePickerForLastUpdated.status.opened = true;
        };
}]);
