'use strict';

angular.module('eventsmanagerApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('invitation', {
                parent: 'entity',
                url: '/invitations',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'eventsmanagerApp.invitation.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/invitation/invitations.html',
                        controller: 'InvitationController'
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
            .state('invitation.detail', {
                parent: 'entity',
                url: '/invitation/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'eventsmanagerApp.invitation.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/invitation/invitation-detail.html',
                        controller: 'InvitationDetailController'
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
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/invitation/invitation-dialog.html',
                        controller: 'InvitationDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    answer: null,
                                    message: null,
                                    dateCreated: null,
                                    lastUpdated: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('invitation', null, { reload: true });
                    }, function() {
                        $state.go('invitation');
                    })
                }]
            })
            .state('invitation.edit', {
                parent: 'invitation',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/invitation/invitation-dialog.html',
                        controller: 'InvitationDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Invitation', function(Invitation) {
                                return Invitation.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('invitation', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('invitation.delete', {
                parent: 'invitation',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/invitation/invitation-delete-dialog.html',
                        controller: 'InvitationDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Invitation', function(Invitation) {
                                return Invitation.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('invitation', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
