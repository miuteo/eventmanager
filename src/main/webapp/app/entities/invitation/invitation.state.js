(function() {
    'use strict';

    angular
        .module('eventmanagerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('invitation', {
            parent: 'entity',
            url: '/invitation',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'eventmanagerApp.invitation.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/invitation/invitations.html',
                    controller: 'InvitationController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('invitation');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('invitation-detail', {
            parent: 'entity',
            url: '/invitation/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'eventmanagerApp.invitation.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/invitation/invitation-detail.html',
                    controller: 'InvitationDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('invitation');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Invitation', function($stateParams, Invitation) {
                    return Invitation.get({id : $stateParams.id});
                }]
            }
        })
        .state('invitation.new', {
            parent: 'invitation',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/invitation/invitation-dialog.html',
                    controller: 'InvitationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                date: null,
                                accept: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('invitation', null, { reload: true });
                }, function() {
                    $state.go('invitation');
                });
            }]
        })
        .state('invitation.edit', {
            parent: 'invitation',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/invitation/invitation-dialog.html',
                    controller: 'InvitationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Invitation', function(Invitation) {
                            return Invitation.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('invitation', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('invitation.delete', {
            parent: 'invitation',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/invitation/invitation-delete-dialog.html',
                    controller: 'InvitationDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Invitation', function(Invitation) {
                            return Invitation.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('invitation', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
