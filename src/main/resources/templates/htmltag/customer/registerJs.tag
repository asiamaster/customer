<script>

    /**
     * 客户类型改变时，
    */
    $('[name="organizationType"]').change(function(){
        let organizationType = $(this).val();
        //如果选择的是企业，则显示企业需要的相关信息
        if (organizationType === 'enterprise') {
            $('[data-type="company"]').show();
        } else {
            $('[data-type="company"]').val('').hide();
        }
        getCertificateType();
    });

    
    function organizationTypeLoadSuccess() {
        <%if (has(organizationType)){%>
            let organizationType = '${organizationType}';
            $('#organizationType').val(organizationType);
            $('#organizationTypeDiv').hide();
        <%}%>
        getCertificateType();
    }

    /**
     *  客户类型改变时，的操作事件
     */
    function getCertificateType(){
        let organizationType = $('#organizationType').val();
        $("#certificateType").empty();
        if (organizationType){
            //根据类型，加载不同的证件类型
            $.ajax({
                type: "POST",
                url: "${contextPath}/customer/getCertificateType.action",
                data: {organizationType:organizationType},
                processData:true,
                dataType: "json",
                async : false,
                success: function (ret) {
                    if(ret.success){
                        //获取 ret.data
                        let data = [];
                        ret.data.forEach(function(el, index){
                            data.push("<option value='"+el.code+"'>"+el.name+"</option>");
                        });
                        $('#certificateType').html(data.join(""));
                    }else{
                        bs4pop.alert(ret.result, {width: 300,type: 'error'});
                    }
                },
                error: function(){
                    bs4pop.alert('远程访问失败', {width: 300,type: 'error'});
                }
            });
        }
    }


    /**
     * 保存数据
    */
    $('#formSubmit').on('click', function () {
        if (!$('form').valid()) {
            return false;
        } else {
            bui.loading.show('努力提交中，请稍候。。。');
            let _formData = new FormData($('#addForm')[0]);
            var url = "${contextPath}/customer/registerIndividual.action";
            let organizationType = $('#organizationType').val();
            //如果选择的是企业，则显示企业需要的相关信息
            if (organizationType === 'enterprise') {
                url = "${contextPath}/customer/registerEnterprise.action";
            }
            $.ajax({
                type: "POST",
                url: url,
                data: _formData,
                processData: false,
                contentType: false,
                async: true,
                success: function (ret) {
                    bui.loading.hide();
                    if (ret.success) {
                        bs4pop.alert('注册成功', {type: 'success',width: 400}, function () {
                            /* 应该要带条件刷新 */
                            // parent.window.location.reload();
                            var tmpJson = {};
                            tmpJson.certificateNumber = $('#certificateNumber').val();
                            tmpJson.name = $('#name').val();
                            tmpJson.contactsPhone = $('#contactsPhone').val();
                            window.parent.postMessage(JSON.stringify(tmpJson),'/');

                        });
                    } else {
                        bs4pop.alert(ret.result, {width: 400,type: 'error'},function () {});
                    }
                },
                error: function (error) {
                    bui.loading.hide();
                    bs4pop.alert(error.result, {width: 400,type: 'error'},function () {

                    });
                }
            });
        }
    });

    /**
     * 取消保存
    */
    $('#formCancel').on('click', function () {
        parent.dia.hide()
    });

    /**
     * 证件号码输入完成后回车时间
     */
    $('#certificateNumber').keydown(function (e) {
        if (e.keyCode == 13) {
            queryCustomerByNumber();
        }
    });

    /**
     * 读卡操作
     */
    $('#idCardRead').click(function(){
        setTimeout(function(){
            var userObj = reader();
            if(!userObj){
                return;
            }
            $('#certificateNumber').val(userObj.IDCardNo);
            queryCustomerByNumber();
        },50);
    });

    /**
     * 读取身份证
     */
    function reader() {
        if (!window.callbackObj) {
            bs4pop.alert("该模式不支持读卡", {width: 400, type: 'warning'});
            return;
        }
        var card = callbackObj.readIDCard();
        var a = eval('(' + card + ')');
        return a;
    }

    /**
     * 根据证件号查询客户在当前市场是否已存在
     */
    function queryCustomerByNumber() {
        let certificateNumber = $('#certificateNumber').val();
        if (certificateNumber){
            if (certificateNumber.length < 15) {
                bs4pop.alert("证件号至少输入15位", {width: 400, type: 'error'});
                return;
            }
            $.ajax({
                type: "POST",
                url: "${contextPath}/customer/queryCustomerByNumber.action",
                data: {certificateNumber: certificateNumber},
                processData: true,
                dataType: "json",
                async: false,
                success: function (ret) {
                    if (ret.success) {
                        //返回true，表示用户不存在，或者用户已存在，但在该市场不存在
                        let data = ret.data;
                        if (data){
                            if (data.organizationType != $('#organizationType').val()) {
                                var msg = '该证件号已在[' + $("#organizationType").find("option:selected").text() + ']客户里存在';
                                bs4pop.alert(msg, {width: 400, type: 'error'});
                                return;
                            }
                            bs4pop.confirm('已存在客户基本信息，本次将沿用客户已有信息?', {type: 'warning'}, function (flag) {
                                if (flag) {
                                    $(".form-control").attr("readonly",true);
                                    $("#certificateNumber").attr("readonly",false);
                                    $('#id').val(data.id);
                                    $('#certificateType').val(data.certificateType);
                                    $('#name').val(data.name);
                                    $('#corporationName').val(data.corporationName);
                                    $('#contactsName').val(data.contactsName);
                                    $('#contactsPhone').val(data.contactsPhone);
                                } else {
                                    parent.window.location.reload();
                                }
                            });
                        }else{
                            $(".form-control").attr("readonly",false);
                            $(".form-control").val('');
                            $('#id').val('');
                            $('#certificateNumber').val(certificateNumber);
                        }
                    } else {
                        $(".form-control").attr("readonly",false);
                        $(".form-control").val('');
                        $('#id').val('');
                        $('#certificateNumber').val(certificateNumber);
                        bs4pop.alert(ret.result, {width: 350,type: 'error'});
                    }
                },
                error: function () {
                    bs4pop.alert('远程访问失败', {width: 350,type: 'error'});
                }
            });
        }
    }

</script>