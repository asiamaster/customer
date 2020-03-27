<script>
    /**
     *
     * @Date 2019-11-06 17:30:00
     * @author jiangchengyong
     *
     ***/

    /*********************变量定义区 begin*************/
        //行索引计数器

    let itemIndex = $('#customerTable tbody tr').length ? $('#customerTable tbody tr').length-1 : 0;
    /*********************变量定义区 end***************/


    $(function () {
        laydateInt();
    });

    /**
     * 添加摊位
     * */
    function addCustomerItem(){
        $('#customerTable tbody').append(HTMLDecode(template('customerItem', {index: ++itemIndex})));
        let targetId = "nation_" + itemIndex;
        listNationality(targetId, '', '-- 请选择 --');
    }

    /**
     * 长期选择项，点击事件
     */
    $('#longTerm').on('click', function () {
        if ($(this).find('input:checked').length){
            $('.inputdate').val('长期').attr('disabled', false).show();
            $('.laydate').attr('disabled', true).hide();
            $('#certificateLongTerm').val('1');
        } else {
            $('.inputdate').attr('disabled', true).hide();
            $('.laydate').attr('disabled', false).val('').show();
            laydateInt();
            $('#certificateLongTerm').val('0');
        }
    });

    $('#addCustomer').on('click', function(){
        addCustomerItem();
        laydateInt();
    });

    //删除行事件 （删除摊位行）
    $(document).on('click', '.item-del', function () {
        if ($('#customerTable tbody tr').length > 1) {
            $(this).closest('tr').remove();
        }
    });

    // 提交保存
    $('#formSubmit').on('click', function (e) {
        let _formDataObj = {};
        let cardData = {};
        let contactsData = [];

        if (!$('#baseInfoForm').validate({ignore:''}).form()) {
            $('#nav-baseInfo-tab').tab('show');
            return false;
        }
        if (!$('#cardInfoForm').validate({ignore:''}).form()) {
            $('#nav-cardInfo-tab').tab('show');
            return false;
        }
        if ($('#resourceInfoForm').length) {
            if (!$('#resourceInfoForm').validate({ignore:''}).form()) {
                $('#nav-resourceInfo-tab').tab('show');
                return false;
            }
        }
       bui.loading.show('努力提交中，请稍候。。。');
       $.each($('#baseInfoForm').serializeArray(), function(index, value){
           _formDataObj[value.name] = value.value;
       });
       $.each($('#cardInfoForm').serializeArray(), function(index, value){
           cardData[value.name] = value.value;
       });

        if ($('#resourceInfoForm').length) {
            $.each($('#resourceInfoForm tbody tr'), function(index, value){
                let itemObj = {};
                $(value).find('input, select').each(function (i, el) {
                    itemObj[el.name.split('_')[0]] = el.value;
                });
                contactsData.push(itemObj);
            });
            _formDataObj['contactsList'] = contactsData;
        }
        _formDataObj['customerCertificate'] = cardData;
       $.ajax({
           type: "POST",
           url: "${contextPath}/customer/doUpdate.action",
            data: JSON.stringify(_formDataObj),
            processData: false,
            contentType: false,
            async: true,
            success: function (res) {
                bui.loading.hide();
                if (res.success) {
                    bs4pop.alert('更新成功', {type: 'success'}, function () {
                        sendUpdateLog();
                        window.parent.postMessage('操作成功','/');
                        parent.dia.hide();
                    });
                } else {
                    bui.loading.hide();
                    bs4pop.alert(res.result, {type: 'error'});
                }
            },
            error: function (error) {
                bui.loading.hide();
                bs4pop.alert(error.result, {type: 'error'});
            }
        });
    });


    /**
     * 修改时保存日志信息
     */
    function sendUpdateLog() {
        let logObj = {};
        logObj.marketId = $("#operatorMarket").val();
        logObj.systemCode = "CUSTOMER";
        logObj.businessType = "customer";
        logObj.businessId = $("#id").val();
        logObj.businessCode = $("#code").val();
        logObj.operationType = "edit";
        logObj.marketId = $("#operatorMarket").val();
        logObj.operatorId = $("#operatorId").val();
        logObj.operatorName = $("#operatorName").text()
        logObj.content = Log.buildUpdateContent();
        logObj.notes = "此记录客户市场数据为：" + $("#marketName").val() + " 所有";
        Log.operatorLog(logObj);
    }

    /**
     * 企业客户法人信息读卡操作
     */
    $('#idCardRead').click(function(){
        setTimeout(function(){
            var userObj = reader();
            if(!userObj){
                return;
            }
            $('#corporationCertificateNumber').val(userObj.IDCardNo);
            $('#corporationCertificateNumber').data('selectVal', userObj.IDCardNo);
            let name = userObj.Name;
            $('#corporationName').val(name.trim());
            $("#corporationName").attr("readonly",true);
        },50);
    });

    /**
     * 法人证件号输入框事件
     */
    $('#corporationCertificateNumber').bind('input', function () {
        var old = $(this).data('selectVal');
        if (old != $(this).val()) {
            $("#corporationName").attr("readonly",false);
        }
        $(this).data('selectVal', $(this).val());
    });

    /*****************************************自定义事件区 end**************************************/
</script>