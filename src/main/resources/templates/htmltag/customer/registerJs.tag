<script>

    /**
    客户类型
    */
    $('[name="customerType"]').change(function(){
        if ($(this).val() === '1') {
            $('[data-type="company"]').show();
        } else {
            $('[data-type="company"]').val('').hide();
        }
    });


    /**
    保存数据
    */
    $('#formSubmit').on('click', function () {
        if (!$('form').valid()) {
            return false;
        } else {
            bui.loading.show('努力提交中，请稍候。。。');
            let _formData = new FormData($('#addForm')[0]);
            $.ajax({
                type: "POST",
                url: "${contextPath}/customer/insert.action",
                data: _formData,
                processData: false,
                contentType: false,
                async: true,
                success: function (res) {
                    bui.loading.hide();
                    if (data.code == "200") {
                        bs4pop.alert('注册成功', {type: 'success '}, function () {
                            /* 应该要带条件刷新 */
                            window.location.reload();
                        });
                    } else {
                        bs4pop.alert(res.result, {type: 'error'});
                    }
                },
                error: function (error) {
                    bui.loading.hide();
                    bs4pop.alert(error.result, {type: 'error'});
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