(function() {
    'use strict';

    angular
        .module('eventmanagerApp')
        .controller('EventController', EventController);

    EventController.$inject = ['$scope', '$state', 'Event'];

    function EventController ($scope, $state, Event) {
        var vm = this;
        vm.events = [];
        vm.loadAll = function() {
            Event.query(function(result) {
                vm.events = result;
            });
        };

        vm.loadAll();
        
    }
})();
