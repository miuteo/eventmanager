(function() {
    'use strict';

    angular
        .module('eventmanagerApp')
        .controller('EventDialogController', EventDialogController);

    EventDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Event', 'Invitation', 'User'];

    function EventDialogController ($scope, $stateParams, $uibModalInstance, $q, entity, Event, Invitation, User) {
        var vm = this;
        vm.event = entity;
        vm.invitations = Invitation.query();
        vm.users = User.query();
        vm.load = function(id) {
            Event.get({id : id}, function(result) {
                vm.event = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('eventmanagerApp:eventUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.event.id !== null) {
                Event.update(vm.event, onSaveSuccess, onSaveError);
            } else {
                Event.save(vm.event, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        var d = new Date();
        vm.event.date=d;
        vm.datePickerOpenStatus = {};
        vm.datePickerOpenStatus.date = false;

        vm.openCalendar = function(date) {

            vm.datePickerOpenStatus[date] = true;
        };
    }
})();
