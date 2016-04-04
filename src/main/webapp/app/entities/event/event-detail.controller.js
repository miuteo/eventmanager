(function() {
    'use strict';

    angular
        .module('eventmanagerApp')
        .controller('EventDetailController', EventDetailController);

    EventDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Event', 'Invitation', 'User'];

    function EventDetailController($scope, $rootScope, $stateParams, entity, Event, Invitation, User) {
        var vm = this;
        vm.event = entity;
        vm.load = function (id) {
            Event.get({id: id}, function(result) {
                vm.event = result;
            });
        };
        var unsubscribe = $rootScope.$on('eventmanagerApp:eventUpdate', function(event, result) {
            vm.event = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
