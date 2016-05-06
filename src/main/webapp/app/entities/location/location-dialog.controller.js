(function() {
    'use strict';

    angular
        .module('eventmanagerApp')
        .controller('LocationDialogController', LocationDialogController);

    LocationDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Location', 'User'];

    function LocationDialogController ($scope, $stateParams, $uibModalInstance, entity, Location, User) {
        var vm = this;
        vm.location = entity;
        vm.users = User.query();
        vm.load = function(id) {
            Location.get({id : id}, function(result) {
                vm.location = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('eventmanagerApp:locationUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.location.id !== null) {
                Location.update(vm.location, onSaveSuccess, onSaveError);
            } else {
                Location.save(vm.location, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        vm.datePickerOpenStatus = {};
        vm.datePickerOpenStatus.date_created = false;
        vm.datePickerOpenStatus.last_updated = false;

        vm.openCalendar = function(date) {
            vm.datePickerOpenStatus[date] = true;
        };
    }
})();
