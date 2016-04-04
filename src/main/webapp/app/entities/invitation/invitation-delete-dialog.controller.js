(function() {
    'use strict';

    angular
        .module('eventmanagerApp')
        .controller('InvitationDeleteController',InvitationDeleteController);

    InvitationDeleteController.$inject = ['$uibModalInstance', 'entity', 'Invitation'];

    function InvitationDeleteController($uibModalInstance, entity, Invitation) {
        var vm = this;
        vm.invitation = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Invitation.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
