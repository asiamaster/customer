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
        // addCustomerItem();
        laydateInt();
    });

    /**
     * 添加摊位
     * */
    function addCustomerItem(){
        $('#customerTable tbody').append(HTMLDecode(template('customerItem',{index: ++itemIndex})))
    }

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
        if (!$('#baseInfoForm').validate({ignore:''}).form()) {
            $('#nav-baseInfo-tab').tab('show');
            return false;
        }

        if (!$('#cardInfoForm').validate({ignore:''}).form()) {
            $('#nav-cardInfo-tab').tab('show');
            return false;
        }

        if (!$('#resourceInfoForm').validate({ignore:''}).form()) {
            $('#nav-resourceInfo-tab').tab('show');
            return false;
        }

       bui.loading.show('努力提交中，请稍候。。。');
       let _formDataObj = {};
       let cardData = {};
       let contactsData = []
       $.each($('#baseInfoForm').serializeArray(), function(index, value){
           _formDataObj[value.name] = value.value;
       });

       $.each($('#cardInfoForm').serializeArray(), function(index, value){
           cardData[value.name] = value.value;
       });
       $.each($('#resourceInfoForm tbody tr'), function(index, value){
           let itemObj = {};
           $(value).find('input, select').each(function (i, el) {
               itemObj[el.name.split('_')[0]] = el.value;
           });
           contactsData.push(itemObj);
       });

       _formDataObj['customerCertificate'] = cardData;
       _formDataObj['contactsList'] = contactsData;
        debugger

        console.log(JSON.stringify(_formDataObj))
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


    /*****************************************自定义事件区 end**************************************/
</script>