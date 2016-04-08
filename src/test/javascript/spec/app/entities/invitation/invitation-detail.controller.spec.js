'use strict';

describe('Controller Tests', function() {

    describe('Invitation Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockInvitation, MockEvent, MockUser;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockInvitation = jasmine.createSpy('MockInvitation');
            MockEvent = jasmine.createSpy('MockEvent');
            MockUser = jasmine.createSpy('MockUser');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Invitation': MockInvitation,
                'Event': MockEvent,
                'User': MockUser
            };
            createController = function() {
                $injector.get('$controller')("InvitationDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'eventmanagerApp:invitationUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
