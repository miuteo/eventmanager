'use strict';

angular.module('eventmanagerApp')
    .controller('CalendarController', function ($scope, $state, $compile, $log, uiCalendarConfig,HomeInvitation) {

        /* event source that calls a function on every view switch */
        $scope.invitationsHome = [];



        function getInvitationsToAccept() {


            HomeInvitation.query(function(result) {
                $scope.invitationsHome = result;
                console.log(result);

            });

        }
        getInvitationsToAccept();
        console.log($scope.invitationsHome);
        







        $scope.eventSource = function getEvents(start, end, timezone, callback) {
            // start and end are for displayed calendar, so see if end is in current month before subtracting a month
            var date = end;
            var endOfMonth = moment(end).endOf('month');
            if (endOfMonth.diff(date, 'days') > 0) {
                date = end.subtract({months: 1});
            }
            date = date.format('YYYY-MM');
            $log.info("Fetching data forasfasf: " + date);
            $scope.events = [];


            var i;

            console.log($scope.invitationsHome);

            // for(i=0; i<$scope.invitationsHome.length;i++)
            //     $scope.events.push({
            //         id: $scope.invitationsHome[i].id,
            //         title: $scope.invitationsHome[i].event.name,
            //         tooltip: 'Exercise: ',
            //         type: 'points',
            //         start: '2016-04-04',
            //         allDay: true,
            //         className: ['label label-primary']
            //
            //     });



                    $scope.events.push({
                        id: '4',
                        title: ' Points',
                        tooltip: 'Exercise: ',
                        type: 'points',
                        start: '2016-04-04',
                        allDay: true,
                        className: ['label label-primary']

                    });



            callback($scope.events);




        };

        $scope.onEventClick = function (date, jsEvent, view) {
            $state.go('history.' + date.type, {id: date.id});
        };

        /* Change View */
        $scope.changeView = function (view, calendar) {
            uiCalendarConfig.calendars[calendar].fullCalendar('changeView', view);
        };

        /* Change View */
        $scope.renderCalender = function (calendar) {
            if (uiCalendarConfig.calendars[calendar]) {
                uiCalendarConfig.calendars[calendar].fullCalendar('render');
            }
        };

        /* Render Tooltip */
        $scope.eventRender = function (event, element, view) {
            var tooltip = (event.tooltip) ? event.tooltip : event.title;
            element.attr({
                'tooltip': tooltip,
                'tooltip-append-to-body': true
            });
            $compile(element)($scope);
        };

        /* config object */
        $scope.uiConfig = {
            calendar: {
                editable: false,
                header: {
                    left: 'month agendaWeek agendaDay',
                    center: 'title',
                    right: 'today prev,next'
                },
                eventClick: $scope.onEventClick,
                eventRender: $scope.eventRender
            }
        };

        $scope.eventSources = [$scope.eventSource];
    });
