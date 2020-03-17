<script>

    window.domain = 'diligrp.com';

    // /**
    //  * 客户类型改变时，
    // */
    // $('[name="organizationType"]').change(function () {
    //
    // });

    function organizationTypeChange() {
        let organizationType = $('#organizationType').val();
        //如果选择的是企业，则显示企业需要的相关信息
        if (organizationType === 'enterprise') {
            $('[data-type="company"]').show();
        } else {
            $('[data-type="company"]').val('').hide();
        }
        getCertificateType(organizationType, 'certificateType');
    }
    
    function organizationTypeLoadSuccess() {
        <%if (has(organizationType)){%>
            let organizationType = '${organizationType}';
            $('#organizationType').val(organizationType);
            $('#organizationTypeDiv').hide();
        <%}%>
        organizationTypeChange();
        //getCertificateType($('#organizationType').val(),'certificateType');
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
                crossDomain:true,
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
                            debugger
                            // parent.window.location.reload();
                            let tmpJson = {};
                            let postData = {};
                            tmpJson.certificateNumber = $('#certificateNumber').val();
                            tmpJson.name = $('#name').val();
                            tmpJson.contactsPhone = $('#contactsPhone').val();
                            tmpJson.customerId = ret.data;
                            postData['topic'] = 'customerRegister';
                            postData['content'] = tmpJson;
                            postData['isClose'] = true;
                            parent.window.postMessage(JSON.stringify(postData) , '*');
                        });
                    } else {
                        bs4pop.alert(ret.result, {width: 400,type: 'error'});
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

    // 关闭注册框
    $('#formCancel').on('click', function(){
        let postData = {};
        postData['isClose'] = true;
        parent.postMessage( JSON.stringify(postData) ,'*');
    });




    /**
     * 证件号码输入完成后回车时间
     */
    $('#certificateNumber').keydown(function (e) {
        if (e.keyCode == 13) {
            queryCustomerByNumber();
        }
    });

    $('#certificateNumber').bind('input', function () {
        var old = $(this).data('selectVal');
        if (old != $(this).val()) {
            clearCustomerVal();
        }
        $(this).data('selectVal', $(this).val());
    });

    /**
     * 清空输入框
     */
    function clearCustomerVal(){
        $('#id').val('');
        // let certificateNumber = $('#certificateNumber').val();
        // let certificateType = $('#certificateType').val();
        // let organizationType = $('#organizationType').val();
        // $(".form-control").val('');
        // $('#certificateNumber').val(certificateNumber);
        // $('#certificateType').val(certificateType);
        // $('#organizationType').val(organizationType);
    }

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
            queryCustomerByNumber(userObj);
        },50);
    });

    /**
     * 根据证件号查询客户在当前市场是否已存在
     */
    function queryCustomerByNumber(userObj) {
        let certificateNumber = $('#certificateNumber').val();
        let organizationType = $('#organizationType').val();
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
                        //如果用户已存在，则用系统中的用户信息
                        if (data){
                            if (data.organizationType != organizationType) {
                                var msg = '该证件号已在[' + $("#organizationType").find("option:selected").text() + ']客户里存在';
                                bs4pop.alert(msg, {width: 400, type: 'error'},function () {
                                    parent.dia.hide();
                                });
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
                            //如果用户不存在，判断本次是否读卡操作
                            //如果userObj 有值，则表示是读卡读出的数据
                            if (userObj){
                                $("#id").val('');
                                var gender='2';
                                if(userObj.Sex =="男"){
                                    gender='1';
                                }else{
                                    gender='2';
                                }
                                var photo = "data:image/jpeg;base64,"+userObj.PhotoFileName;
                                let name = userObj.Name;
                                $('#name').val(name.trim());
                                $('#gender').val(gender);
                                $('#photo').val(photo);
                                var address = userObj.Address;
                                $('#certificateAddr').val(address.trim());
                                $('#birthdate').val(userObj.Born);
                                $('#certificateRange').val(userObj.UserLifeBegin+' ~ '+userObj.UserLifeEnd);
                                $(".form-control").attr("readonly",true);
                                $("#certificateNumber").attr("readonly",false);
                            }else{
                                $(".form-control").attr("readonly",false);
                                clearCustomerVal();
                            }
                        }
                    } else {
                        $(".form-control").attr("readonly",false);
                        clearCustomerVal();
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