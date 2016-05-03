(function() {
    'use strict';

    angular
        .module('eventmanagerApp')
        .controller('HomeInvitationsController', HomeInvitationsController);

    HomeInvitationsController.$inject = ['$scope', 'Principal', 'LoginService', 'HomeInvitation', '$http', '$q','AlertService'];

    function HomeInvitationsController ($scope, Principal, LoginService, HomeInvitation, $http, $q,AlertService) {

        var vm = this;

        vm.account = null;
        vm.isAuthenticated = null;
        vm.login = LoginService.open;
        $scope.invitationsHome = [];

        $scope.$on('authenticationSuccess', function() {
            getInvitationsToAccept();
        });

        function getInvitationsToAccept() {
            Principal.identity().then(function(account) {
                vm.account = account;
                vm.isAuthenticated = Principal.isAuthenticated;
            });

            HomeInvitation.query(function(result) {
                $scope.invitationsHome = result;
            });

        }

        var updateInvitation = function(invitation) {
            var d = $q.defer();

            $http({
                method: 'PUT',
                data: invitation,
                url: '/api/acceptinvitation'
            }).success(function(data, status, headers) {
                d.resolve(data);
            }).error(function (data, status, headers) {
                d.reject(data);
            });

            return d.promise;
        };

        $scope.acceptOrRejectInvitation = function (invitation) {
            if (invitation.id !== null) {
                updateInvitation(invitation).then(function(result) {
                    console.log("Save OK " + JSON.stringify(result));
                    // reload invitations to accept
                    getInvitationsToAccept();
                }, function(err) {
                    console.log("Save Error " + + JSON.stringify(err));
                });
            }
        };

        // initial load for this page
        getInvitationsToAccept();
    }
})();
