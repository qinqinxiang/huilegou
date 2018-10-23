bosfore_app.controller("ctrlRead", ['$scope', '$http', function ($scope, $http) {
    $scope.currentPage = 1;//当前页码
    $scope.pageSize = 4;//每页显示的数据
    $scope.totalCount = 0;//总记录数
    $scope.totalPages = 0;//总页数

    $scope.prev = function () {
        if ($scope.currentPage > 1) {
            $scope.selectPage($scope.currentPage - 1);
        }
    }

    $scope.next = function () {
        if ($scope.currentPage < $scope.totalPages) {
            $scope.selectPage($scope.currentPage + 1);
        }
    }

    $scope.selectPage = function (page) {
        // 如果页码超出范围
        if ($scope.totalPages != 0) {
            if (page < 1 || page > $scope.totalPages) return;
        }

        $http({
            method: 'GET',
            url: 'promotionPage.action',
            params: {
                "page": page,//将当前页和每页显示的数据传到后台，分页必须条件
                "pageSize": $scope.pageSize
            }
        }).success(function (data, status, headers, config) {
            // 显示表格数据 
            $scope.pageItems = data.pageData;
            // 计算总页数
            $scope.totalCount = data.totalCount;
            $scope.totalPages = Math.ceil($scope.totalCount / $scope.pageSize);

            // 当前显示页，设为当前页
            $scope.currentPage = page;

            // 固定显示10页 (前5后4)
            var begin;
            var end;

            begin = page - 5;
            if (begin < 0) {
                begin = 1;
            }

            end = begin + 9;
            if (end > $scope.totalPages) {
                end = $scope.totalPages;
            }

            begin = end - 9;
            if (begin < 1) {
                begin = 1;
            }

            $scope.pageList = new Array();
            for (var i = begin; i <= end; i++) {
                $scope.pageList.push(i);
            }

        }).error(function (data, status, headers, config) {
            // 当响应以错误状态返回时调用
            alert("出错，请联系管理员 ");
        });
    }

    $scope.isActivePage = function (page) {
        return page == $scope.currentPage;
    }

    // 发起请求 显示第一页数据 
    $scope.selectPage($scope.currentPage);

}]);