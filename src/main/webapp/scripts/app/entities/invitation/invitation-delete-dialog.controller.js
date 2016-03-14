'use strict';

angular.module('eventsmanagerApp')
	.controller('InvitationDeleteController', function($scope, $uibModalInstance, entity, Invitation) {

        $scope.invitation = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Invitation.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
