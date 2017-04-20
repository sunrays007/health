(function() {
    'use strict';

    angular
        .module('healthApp')
        .controller('BloodpressureDetailController', BloodpressureDetailController);

    BloodpressureDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Bloodpressure', 'User'];

    function BloodpressureDetailController($scope, $rootScope, $stateParams, previousState, entity, Bloodpressure, User) {
        var vm = this;

        vm.bloodpressure = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('healthApp:bloodpressureUpdate', function(event, result) {
            vm.bloodpressure = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
