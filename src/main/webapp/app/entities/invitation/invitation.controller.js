(function() {
    'use strict';

    angular
        .module('eventmanagerApp')
        .controller('InvitationController', InvitationController);

    InvitationController.$inject = ['$scope', '$state', 'Invitation'];

    function InvitationController ($scope, $state, Invitation) {
        var vm = this;
        vm.invitations = [];
        vm.loadAll = function() {
            Invitation.query(function(result) {
                vm.invitations = result;
            });
        };

        vm.loadAll();
        
    }
})();
