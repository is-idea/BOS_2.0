var signupApp = angular.module("signupApp",[]);
signupApp.controller("signupController",["$scope","$http",function ($scope,$http) {
    $scope.btnMsg = "获取验证码";
    var active = true;
    // 倒计时60秒
    var second = 60;
    var secondInterval;
    // 失去焦点事件
    $scope.change = function (telephone) {
        if($scope.codeVal != "" || $scope.codeVal != null){
            $http({
                method:'GET',
                url:'async.action',
                params:{
                    telephone:telephone,
                    code:$scope.codeVal
                }
            }).error(function () {
                alert("验证码错误")
                return;
            });
        }
    }
    $scope.getCode = function (telephone) {

        if(active == false){
            return;
        }

        // 向服务器发送请求
        var regxt = /^1(3|5|7|8)\d{9}$/;
        if(regxt.test(telephone)){
            // 效验通过
            $http({
                method: 'GET',
                url: 'Customer_SendSms.action',
                params:{
                    telephone : telephone
                }
            }).error(function(data,status,headers,config) {
                alert("发送信息出错");
                return;
            });
        }else {
            // 效验不通过
            alert("手机号格式不正确");
            return;
        }

        // 每一秒执行一次,60秒后可重新点击发送
        active = false;
        secondInterval = setInterval(function() {
            if(second < 0) {
                // 倒计时结束，允许重新发送
                $scope.btnMsg = "重发验证码";
                // 强制更新视图
                $scope.$digest();
                active = true;
                second = 60;
                // 关闭定时器
                clearInterval(secondInterval);
                secondInterval = undefined;
            } else {
                // 继续计时
                $scope.btnMsg = second + "秒后重发";
                // 强制更新视图
                $scope.$digest();
                second--;
            }
        }, 1000);
    }
}]);