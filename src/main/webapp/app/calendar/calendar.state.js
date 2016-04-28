(function() {
    'use strict';

    angular
        .module('eventmanagerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('calendar', {
            parent: 'app',
            url: '/calendar',
            data: {
                roles: ['ROLE_USER']
            },
            views: {
                'content@': {
                    templateUrl: 'app/calendar/calendar.html',
                    controller: 'CalendarController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                mainTranslatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate,$translatePartialLoader) {
                    $translatePartialLoader.addPart('home');
                    return $translate.refresh();
                }]
            }
        })
            .state('calendar-detail', {
                parent: 'entity',
                url: '/calendar/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'eventmanagerApp.invitation.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/calendar/calendar-detail.html',
                        controller: 'CalendarDetailController',
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

    }
})();
