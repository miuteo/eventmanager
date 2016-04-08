(function() {
    'use strict';

    angular
        .module('eventmanagerApp')
        .controller('InvitationDialogController', InvitationDialogController);

    InvitationDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Invitation', 'Event', 'User'];

    function InvitationDialogController ($scope, $stateParams, $uibModalInstance, $q, entity, Invitation, Event, User) {
        var vm = this;
        vm.invitation = entity;
        vm.events = Event.query();
        vm.users = User.query();
        vm.load = function(id) {
            Invitation.get({id : id}, function(result) {
                vm.invitation = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('eventmanagerApp:invitationUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.invitation.id !== null) {
                Invitation.update(vm.invitation, onSaveSuccess, onSaveError);
            } else {
                Invitation.save(vm.invitation, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        vm.datePickerOpenStatus = {};
        vm.datePickerOpenStatus.date = false;

        vm.openCalendar = function(date) {
            vm.datePickerOpenStatus[date] = true;
        };
    }
})();
