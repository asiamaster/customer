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
                        bs4pop.alert('错误',ret.result, 'error');
                    }
                },
                error: function(){
                    bs4pop.alert('错误', '远程访问失败', 'error');
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
                            window.location.reload();
                        });
                    } else {
                        bs4pop.alert(ret.result, {width: 400,type: 'error'},function () {

                        });
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
    取消保存
    */
    $('#formCancel').on('click', function () {
        parent.editCustomerDia.hide()
    });

</script>