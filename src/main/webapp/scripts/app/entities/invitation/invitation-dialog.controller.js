'use strict';

angular.module('eventsmanagerApp').controller('InvitationDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Invitation', 'Event', 'User',
        function($scope, $stateParams, $uibModalInstance, entity, Invitation, Event, User) {

        $scope.invitation = entity;
        $scope.events = Event.query();
        $scope.users = User.query();
        $scope.load = function(id) {
            Invitation.get({id : id}, function(result) {
                $scope.invitation = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('eventsmanagerApp:invitationUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.invitation.id != null) {
                Invitation.update($scope.invitation, onSaveSuccess, onSaveError);
            } else {
                Invitation.save($scope.invitation, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
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
