var paginationApp = angular.module("paginationApp",[]);
paginationApp.controller("paginationCtrl",["$scope","$http",function ($scope,$http) {
        // 分页组件
        $scope.currentPage=1;// 当前页
        $scope.pageSize=4; //每页记录数
        $scope.totalCount=0;// 总记录数
        $scope.totalPage=0;// 总页数(根据总记录数,每页记录数计算)

        // 要在分页工具条显示所有的页码
        $scope.pageList=new Array();

        // 加载上一页数据
        $scope.prev=function () {
            $scope.selectPage($scope.currentPage-1)
        }

        // 加载下一页数据
        $scope.next=function () {
            $scope.selectPage($scope.currentPage+1)
        }

        // 加载指定页数据
        $scope.selectPage=function (page) {
            $http({
                method:'GET',
                url:'promotion_pageQuery.action',
                params:{
                    page:page,// 页码
                    pageSize:$scope.pageSize // 每页记录数
                }
            }).success(function (data) {
                // 显示表格数据
                $scope.products=data.products;
                // 根据总记录数,计算总页数
                $scope.totalCount = data.totalCount;
                $scope.totalPages = Math.ceil($scope.totalCount / $scope.pageSize);
                // 更新当前显示页码
                $scope.currentPage = page;
                // 显示分页工具条页码
                var begin; // 显示第一个页码
                var end; // 显示最后一个页码

                // 理论上begin是当前页减5
                begin = $scope.currentPage -5;
                // 第一个页码不能小于一
                if(begin < 1){
                    begin = 1;
                }

                // 显示10个页码,理论上end是begin+9
                end = begin + 9;
                if(end > $scope.totalPages){
                    // 最后一个页码不能大于总页数
                    end = $scope.totalPages;
                }

                // 修正begin 的值,理论上时end-9
                begin = end-9;
                if(begin < 1){
                    begin = 1;
                }

                // 将页码加入List集合
                for(var i = begin;i<end;i++){
                    $scope.pageList.push(i);
                }

            }).error(function (data) {

            });

            $scope.isActivePage=function (page) {
                return page === $scope.currentPage;
            }
        }

        // 初始化选中第一页
        $scope.selectPage(1);
}]);