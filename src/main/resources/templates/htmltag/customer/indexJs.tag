<script>

    /*********************变量定义区 begin*************/
        //行索引计数器
        //如 let itemIndex = 0;
    let _customerGrid = $('#customerGrid');
    let currentSelectRowIndex;
    var dia;

    /*********************变量定义区 end***************/

    /******************************驱动执行区 begin***************************/
    $(function () {
        $(window).resize(function () {
            _customerGrid.bootstrapTable('resetView')
        });
        let size = ($(window).height() - $('#customerQueryForm').height() - 210) / 40;
        size = size > 10 ? size : 10;
        _customerGrid.bootstrapTable('refreshOptions', {url: '/customer/listPage.action', pageSize: parseInt(size)});
    });

    /******************************驱动执行区 end****************************/

    /*****************************************函数区 begin************************************/
    /**
    时间范围
    */
    lay('.laydatetime').each(function () {
        laydate.render({
            elem: this
            , trigger: 'click'
        });
    });

    /**
     * 打开新增窗口
     * @param organizationType 客户类型
    */
    function openInsertHandler(organizationType) {
        let url = "/customer/register.action?sourceSystem=CUSTOMER&sourceChannel=bg_create&organizationType=" + organizationType;
        dia = bs4pop.dialog({
            title: '新增客户',
            content: url,
            isIframe: true,
            closeBtn: true,
            backdrop: 'static',
            width: '650',
            height: '600',
            btns: []
        });
    }

    /**
     * 打开客户更新
     * @param organizationType 客户类型
     */
    function openUpdateHandler() {
        //获取选中行的数据
        let rows = _customerGrid.bootstrapTable('getSelections');
        if (null == rows || rows.length == 0) {
            bs4pop.alert('请选中一条数据');
            return;
        }
        //table选择模式是单选时可用
        let selectedRow = rows[0];
        let url = "/customer/update.html?customerId=" + selectedRow.id + "&marketId=" + selectedRow.$_marketId;
        dia = bs4pop.dialog({
            title: '更新客户',
            content: url,
            isIframe: true,
            closeBtn: true,
            backdrop: 'static',
            width: '95%',
            height: '95%',
            btns: []
        });
    }

    /**
     * 查看客户详情
     * @param id 客户id
     * @param marketId 所属市场
     */
    function openDetailHandler(id,marketId) {
        if (!id || !marketId){
            //获取选中行的数据
            let rows = _customerGrid.bootstrapTable('getSelections');
            if (null == rows || rows.length == 0) {
                bs4pop.alert('请选中一条数据');
                return;
            }
            //table选择模式是单选时可用
            let selectedRow = rows[0];
            id = selectedRow.id;
            marketId = selectedRow.$_marketId;
        }
        let url = '/customer/detail.action?id=' + id + "&marketId=" + marketId;
        dia = bs4pop.dialog({
            title: '客户详情',
            content: url,
            isIframe: true,
            closeBtn: true,
            backdrop: 'static',
            width: '95%',
            height: '95%',
            btns: []
        });
    }

    /**
     * 禁启用操作
     * @param enable 是否启用:true-启用
     */
    function doEnableHandler(enable) {
        //获取选中行的数据
        let rows = _customerGrid.bootstrapTable('getSelections');
        if (null == rows || rows.length == 0) {
            bs4pop.alert('请选中一条数据');
            return;
        }
        let selectedRow = rows[0];
        let msg = (enable || 'true' == enable) ? '确定要启用该客户吗？' : '确定要禁用该客户吗？';
        bs4pop.confirm(msg, undefined, function (sure) {
            if(sure){
                bui.loading.show('努力提交中，请稍候。。。');
                $.ajax({
                    type: "POST",
                    url: "${contextPath}/customer/doEnable.action",
                    data: {id: selectedRow.id, enable: enable},
                    processData:true,
                    dataType: "json",
                    async : true,
                    success : function(ret) {
                        bui.loading.hide();
                        if(ret.success){
                            queryCustomerDataHandler();
                        }else{
                            bs4pop.alert(ret.result, {type: 'error'});
                        }
                    },
                    error : function() {
                        bui.loading.hide();
                        bs4pop.alert('远程访问失败', {type: 'error'});
                    }
                });
            }
        });
    }


    /**
     * 查询处理
     */
    function queryCustomerDataHandler() {
        currentSelectRowIndex = undefined;
        $('#toolbar button').attr('disabled', false);
        _customerGrid.bootstrapTable('refresh');
    }

    /**
     * table参数组装
     * 可修改queryParams向服务器发送其余的参数
     * @param params
     */
    function queryParams(params) {
        let temp = {
            rows: params.limit,   //页面大小
            page: ((params.offset / params.limit) + 1) || 1, //页码
            sort: params.sort,
            order: params.order
        };
        return $.extend(temp, bui.util.bindGridMeta2Form('customerGrid', 'customerQueryForm'));
    }

    /**
     * table参数组装
     * 可修改queryParams向服务器发送其余的参数
     * @param params
     */
    function subQueryParams(params) {
        let temp = {
            rows: params.limit,   //页面大小
            page: ((params.offset / params.limit) + 1) || 1, //页码
            sort: params.sort,
            order: params.order
        };
        return $.extend(temp, bui.util.bindMetadata(this.id));
    }

    /*****************************************函数区 end**************************************/

    //选中行事件
    _customerGrid.on('uncheck.bs.table', function (e, row, $element) {
        currentSelectRowIndex = undefined;
    });


    //选中行事件 -- 可操作按钮控制
    _customerGrid.on('check.bs.table', function (e, row, $element) {
        let state = row.$_state;
        if (state === 1) {
            $('#toolbar button').attr('disabled', false);
            $('#btn_enabled').attr('disabled', true);
        } else if (state === 2) {
            $('#toolbar button').attr('disabled', false);
            $('#btn_disabled').attr('disabled', true);
        }
    });

    /**
     * 点击查询详情
     */
    function formatterView(value, row, index) {
        return '<a class="like" href="javascript:void(0)" onclick="openDetailHandler(' + row.id + ','+row.$_marketId+')" title="详情">' + value + '</a>'
    }

    /*****************************************自定义事件区 end**************************************/
    window.addEventListener('message', function (e) {
        // alert(e.data);
        dia.hide();
        queryCustomerDataHandler();
    }, false);

</script>