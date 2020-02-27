<script>

    /**
     * 根据组织类型获取证件类型
     * @param organizationType 组织类型
     * @param targetId 目标显示控件ID
     * @param currentValue 当前证件类型值
     * @param nullValue 空值显示的名称
     */
    function getCertificateType(organizationType, targetId, currentValue,nullValue) {
        $("#" + targetId).empty();
        let data = [];
        if (nullValue) {
            data.push("<option value=''>" + nullValue + "</option>");
        }
        if (organizationType) {
            //根据类型，加载不同的证件类型
            $.ajax({
                type: "POST",
                url: "${contextPath}/customer/getCertificateType.action",
                data: {organizationType: organizationType},
                processData: true,
                dataType: "json",
                async: false,
                success: function (ret) {
                    if (ret.success) {
                        //获取 ret.data
                        ret.data.forEach(function (el, index) {
                            if (el.code === currentValue) {
                                data.push("<option value='" + el.code + "' selected>" + el.name + "</option>");
                            } else {
                                data.push("<option value='" + el.code + "'>" + el.name + "</option>");
                            }
                        });
                        $('#' + targetId).html(data.join(""));
                    } else {
                        bs4pop.alert(ret.result, {width: 300, type: 'error'});
                    }
                },
                error: function () {
                    bs4pop.alert('远程访问失败', {width: 300, type: 'error'});
                }
            });
        }
    }

    /**
     * 根据市场id获取市场的部门信息
     * @param marketId 市场ID
     * @param targetId 目标显示控件ID
     * @param currentValue 当前证件类型值
     * @param nullValue 空值显示的名称
     */
    function listDepartmentByMarketId(marketId, targetId, currentValue,nullValue) {
        $("#" + targetId).empty();
        let data = [];
        if (nullValue) {
            data.push("<option value=''>" + nullValue + "</option>");
        }
        if (organizationType) {
            //根据类型，加载不同的证件类型
            $.ajax({
                type: "POST",
                url: "${contextPath}/department/listByMarketId.action",
                data: {marketId: marketId},
                processData: true,
                dataType: "json",
                async: false,
                success: function (ret) {
                    if (ret.success) {
                        //获取 ret.data
                        ret.data.forEach(function (el, index) {
                            if (el.code === currentValue) {
                                data.push("<option value='" + el.code + "' selected>" + el.name + "</option>");
                            } else {
                                data.push("<option value='" + el.code + "'>" + el.name + "</option>");
                            }
                        });
                        $('#' + targetId).html(data.join(""));
                    } else {
                        bs4pop.alert(ret.result, {width: 300, type: 'error'});
                    }
                },
                error: function () {
                    bs4pop.alert('远程访问失败', {width: 300, type: 'error'});
                }
            });
        }
    }

</script>