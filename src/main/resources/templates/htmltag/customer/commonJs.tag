<script>

    /**
     * 根据组织类型获取证件类型
     * @param organizationType 组织类型
     * @param targetId 目标显示控件ID
     * @param currentValue 当前值
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
                            if (el.code == currentValue) {
                                data.push("<option value='" + el.code + "' selected>" + el.name + "</option>");
                            } else {
                                data.push("<option value='" + el.code + "'>" + el.name + "</option>");
                            }
                        });
                        $('#' + targetId).html(data.join(""));
                    } else {
                        bs4pop.alert(ret.message, {width: 300, type: 'error'});
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
     * @param currentValue 当前值
     * @param nullValue 空值显示的名称
     */
    function listDepartmentByMarketId(marketId, targetId, currentValue,nullValue) {
        $("#" + targetId).empty();
        let data = [];
        if (nullValue) {
            data.push("<option value=''>" + nullValue + "</option>");
        }
        if (marketId) {
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
                            if (el.id == currentValue) {
                                data.push("<option value='" + el.id + "' selected>" + el.name + "</option>");
                            } else {
                                data.push("<option value='" + el.id + "'>" + el.name + "</option>");
                            }
                        });
                        $('#' + targetId).html(data.join(""));
                    } else {
                        bs4pop.alert(ret.message, {width: 300, type: 'error'});
                    }
                },
                error: function () {
                    bs4pop.alert('远程访问失败', {width: 300, type: 'error'});
                }
            });
        }
    }

    /**
     * 获取民族主数据信息
     * @param targetId 目标显示控件ID
     * @param currentValue 当前值
     * @param nullValue 空值显示的名称
     */
    function listNationality(targetId, currentValue, nullValue) {
        $("#" + targetId).empty();
        let data = [];
        if (nullValue) {
            data.push("<option value=''>" + nullValue + "</option>");
        }
        //根据类型，加载不同的证件类型
        $.ajax({
            type: "POST",
            url: "${contextPath}/customer/listNationality.action",
            processData: true,
            dataType: "json",
            async: false,
            success: function (ret) {
                if (ret.success) {
                    //获取 ret.data
                    ret.data.forEach(function (el, index) {
                        if (el.code === currentValue) {
                            data.push("<option value='" + el.code + "' selected>" + el.value + "</option>");
                        } else {
                            data.push("<option value='" + el.code + "'>" + el.value + "</option>");
                        }
                    });
                    $('#' + targetId).html(data.join(""));
                } else {
                    bs4pop.alert(ret.message, {width: 300, type: 'error'});
                }
            },
            error: function () {
                bs4pop.alert('远程访问失败', {width: 300, type: 'error'});
            }
        });
    }

    /**
     * 读取身份证,获取读取信息
     */
    function reader() {
        if (!window.callbackObj) {
            bs4pop.alert("该模式不支持读取证件", {width: 400, type: 'warning'});
            return;
        }
        var card = callbackObj.readIDCard();
        var a = eval('(' + card + ')');
        return a;
    }


</script>