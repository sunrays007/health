(function() {
    'use strict';

    angular
        .module('healthApp')
        .factory('BloodpressureSearch', BloodpressureSearch);

    BloodpressureSearch.$inject = ['$resource'];

    function BloodpressureSearch($resource) {
        var resourceUrl =  'api/_search/bloodpressures/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
