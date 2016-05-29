'use strict';

angular.module('eventmanagerApp')
    .controller('CalendarController', function ($scope, $filter, $state, $compile, $log, uiCalendarConfig, DateUtils, CalendarService) {

        $scope.eventSource = function getEvents(start, end, timezone, callback) {
            // start and end are for displayed calendar, so see if end is in current month before subtracting a month
            var date = end;
            var endOfMonth = moment(end).endOf('month');
            if (endOfMonth.diff(date, 'days') > 0) {
                date = end.subtract({months: 1});
            }
            date = date.format('YYYY-MM');
            $log.info("Fetching data for this date: " + date);
            $scope.events = [];

            CalendarService.query({month: date}, function (data) {

                data.forEach(function (invitation) {
                    console.log("invitation.event.date->"+invitation.event.date);
                    // invitation.event.date = $filter('date')(invitation.event.date, "dd-MM-yyyy hh:mm");
                    invitation.date = $filter('date')(invitation.date, "dd-MM-yyyy hh:mm");


                   // function  yyyymmdd (invitationStart) {
                   //      // 25-04-2016 06:28 -> "yyyy-mm-dd hh:mm"
                   //      var dateHour = invitationStart.split(" ");
                   //      var date = dateHour[0].split("-");
                   //
                   //      return date[2]+"-" + date[1] +"-"+ date[0]+ " "+dateHour[1]; // padding
                   //  };
                    console.log("invitation.event.date->"+invitation.event.date);
                    // console.log("invitation.event.date2->"+yyyymmdd(invitation.event.date));

                    $log.info(invitation.event.name + " " + invitation.event.location + " " + invitation.event.date + " "+invitation.event.duration + " " + invitation.date);

                    var msec = Date.parse(invitation.event.date);
                    var msecEnd = msec + invitation.event.duration * 60*60*1000;
                    var dateEnd = new Date(msecEnd);
                    $log.info ("dateEnd="+dateEnd);

                    $scope.events.push({
                        id: invitation.id,
                        title: invitation.event.name,
                        tooltipLocation: invitation.event.location.name,
                        tooltipDetail: invitation.event.details,
                        type: 'events',
                        start:invitation.event.date,
                        end: dateEnd,
                        allDay: false,
                        className: ['label label-success']
                    });
                });

                $log.info(JSON.stringify($scope.events));
                callback($scope.events);
            });
        };

        $scope.onEventClick = function (date, jsEvent, view) {
            $state.go('calendar-detail' ,{id: date.id});
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
            // var tooltip = (event.tooltip) ? event.tooltip : event.title;

            // element.attr({
            //     'tooltip': tooltip,
            //     'tooltip-append-to-body': true
            // });
            // $compile(element)($scope);

            element.qtip({
                content: {
                    title: event.tooltipLocation,
                    text: event.tooltipDetail
                },
                // position: {
                //     target: 'mouse', // Track the mouse as the positioning target
                //     adjust: { x: 5, y: 5 } // Offset it slightly from under the mouse
                // },
                show: {
                    effect: function() {
                        $(this).show('slide', 500);
                    }
                },
                hide: {
                    effect: function() {
                        $(this).hide('puff', 500);
                    }
                },


                style: { classes: 'qtip-default  qtip qtip-green qtip-rounded' }
            });


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
