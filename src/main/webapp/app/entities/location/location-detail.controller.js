(function() {
    'use strict';

    angular
        .module('eventmanagerApp')
        .controller('LocationDetailController', LocationDetailController);

    LocationDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Location', 'User'];

    function LocationDetailController($scope, $rootScope, $stateParams, entity, Location, User) {
        var vm = this;
        vm.location = entity;
        vm.load = function (id) {
            Location.get({id: id}, function(result) {
                vm.location = result;
            });
        };
        var unsubscribe = $rootScope.$on('eventmanagerApp:locationUpdate', function(event, result) {
            vm.location = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
