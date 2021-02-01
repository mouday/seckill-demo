// 主要交互逻辑
// javascript模块化
var seckill = {
    // ajax相关的url
    URL: {
        nowTime: function () {
            return '/seckill_war/seckill/now/time'
        },

        exposer: function (seckillId) {
            return `/seckill_war/seckill/${seckillId}/exposer`
        },

        execution: function (seckillId, md5) {
            return `/seckill_war/seckill/${seckillId}/${md5}/execution`;
        }
    },

    // 处理秒杀
    handleSeckill: function (seckillId, node) {
        //    获取秒杀地址，控制秒杀显示逻辑，执行秒杀
        node.hide().html('<button class="btn btn-primary" id="killBtn">开始秒杀</button>').show();

        $.post(seckill.URL.exposer(seckillId), {}, function (result) {
            if (result && result['success']) {
                var exposer = result['data'];
                if (exposer['exposed']) {
                    //    开启秒杀

                    // 获取秒杀地址
                    var md5 = exposer['md5'];
                    var seckillUrl = seckill.URL.execution(seckillId, md5);
                    console.log('seckillUrl', seckillUrl)

                    //只绑定一次点击事件
                    $('#killBtn').one('click', function () {
                        //    执行秒杀，
                        //     1、禁用按钮
                        $(this).addClass('disabled');
                        //2、执行秒杀
                        $.post(seckillUrl, {}, function (result) {
                            if (result && result['success']) {
                                var killResult = result['data'];
                                var state = killResult['state'];
                                var stateInfo = killResult['stateInfo'];
                                //3、显示秒杀结果
                                node.html('<span class="label label-success">'+ stateInfo + '</span> ')
                            } else{
                                node.html('<span class="label label-danger">'+ '秒杀失败' + '</span> ')
                            }
                        })
                    })
                } else {
                    //    未开启秒杀, 每个人的机器不一样，可能存在倒计时偏差

                    var now = exposer['now'];
                    var start = exposer['start'];
                    var end = exposer['end'];

                    seckill.countDown(seckillId, now, start, end);
                }
            }
        })
    },

    // 验证手机号
    validatePhone: function (phone) {
        if (phone && phone.length === 11 && !isNaN(phone)) {
            return true;
        }
    },

    //倒计时
    countDown: function (seckillId, nowTime, startTime, endTime) {
        var seckillBox = $('#seckill-box');

        console.log('startTime', startTime);
        console.log('endTime', endTime);
        console.log('nowTime', nowTime);

        //时间判断
        if (nowTime > endTime) {
            //秒杀结束
            console.log('秒杀结束');

            seckillBox.html('秒杀结束');
        } else if (nowTime < startTime) {
            console.log('秒杀未开始');
            //    秒杀未开始
            var killTime = new Date(startTime + 1000);

            seckillBox.countdown(killTime, function (event) {
                var format = event.strftime("秒杀计时：%D天 %H小时 %M分钟 %S秒")
                seckillBox.html(format);
                // 时间完成后回调事件
            }).on('finish.countdown', function () {
                // 秒杀开始
                seckill.handleSeckill(seckillId, seckillBox)
            })
        } else {
            console.log('秒杀开始');
            //    秒杀开始
            seckill.handleSeckill(seckillId, seckillBox)

        }
    },

    // 秒杀详情的逻辑
    detail: {
        // 初始化
        init: function (params) {
            // 手机登录和验证，计时交互

            // 从cookie中获取手机号
            var phone = $.cookie('phone');

            // 验证手机号
            if (!seckill.validatePhone(phone)) {
                var seckillModal = $('#seckill-modal');

                // 显示弹出层
                seckillModal.modal({
                    show: true, // 显示
                    backdrop: 'static', // 禁止位置关闭
                    keyboard: false // 关闭键盘事件
                });

                // 绑定事件
                $('#submit').click(function () {
                    var inputPhone = $('#inputPhone').val();
                    if (seckill.validatePhone(inputPhone)) {
                        // 写入cookie
                        $.cookie('phone', inputPhone,
                            {
                                'expires': 7, // 7天
                                'path': '/seckill_war/seckill' // 生效路径
                            });
                        // 刷新页面
                        window.location.reload();
                    } else {
                        $('#message').hide().html('<label class="label label-danger">手机号错误</label>').show(300);
                    }
                })
            }

            var seckillId = params['seckillId'];
            var startTime = params['startTime'];
            var endTime = params['endTime'];

            // 已经登录的交互逻辑
            $.get(seckill.URL.nowTime(), {}, function (result) {
                if (result && result['success']) {
                    var nowTime = result['data'];
                    // 计时交互
                    seckill.countDown(seckillId, nowTime, startTime, endTime);
                }
            })
        }
    }
};