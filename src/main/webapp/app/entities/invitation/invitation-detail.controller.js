(function() {
    'use strict';

    angular
        .module('eventmanagerApp')
        .controller('InvitationDetailController', InvitationDetailController);

    InvitationDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Invitation', 'Event', 'User'];

    function InvitationDetailController($scope, $rootScope, $stateParams, entity, Invitation, Event, User) {
        var vm = this;
        vm.invitation = entity;
        vm.load = function (id) {
            Invitation.get({id: id}, function(result) {
                vm.invitation = result;
            });
        };
        var unsubscribe = $rootScope.$on('eventmanagerApp:invitationUpdate', function(event, result) {
            vm.invitation = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
