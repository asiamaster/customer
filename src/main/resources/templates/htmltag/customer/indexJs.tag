<script>

    /*********************变量定义区 begin*************/
        //行索引计数器
        //如 let itemIndex = 0;
    let _customerGrid = $('#customerGrid');
    let _form = $('#_form');
    let currentSelectRowIndex;
    var dia;

    /*********************变量定义区 end***************/

    /******************************驱动执行区 begin***************************/
    $(function () {
        $(window).resize(function () {
            _customerGrid.bootstrapTable('resetView')
        });
        queryCustomerDataHandler();
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
            width: '600',
            height: '700',
            btns: []
        });
    }

    /**
     * 打开客户更新
     * @param organizationType 客户类型
     */
    function openUpdateHandler(organizationType) {
        let url = "/customer/" + organizationType + "/enterprise/update.html";
        dia = bs4pop.dialog({
            title: '更新客户',
            content: url,
            isIframe: true,
            closeBtn: true,
            backdrop: 'static',
            width: '100%',
            height: '100%',
            btns: []
        });
    }

    /**
     * 查看客户详情
     */
    function openDetailHandler() {
        //获取选中行的数据
        let rows = _customerGrid.bootstrapTable('getSelections');
        if (null == rows || rows.length == 0) {
            bs4pop.alert('请选中一条数据', {type: "warning"});
            return;
        }
        //table选择模式是单选时可用
        let selectedRow = rows[0];
        let url = '/customer/detail.action?id='+selectedRow.id;
        dia = bs4pop.dialog({
            title: '客户详情',
            content: url,
            isIframe: true,
            closeBtn: true,
            backdrop: 'static',
            width: '100%',
            height: '100%',
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
        //table选择模式是单选时可用
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
                    success : function(data) {
                        bui.loading.hide();
                        if(data.success){
                            _customerGrid.bootstrapTable('refresh');
                            _modal.modal('hide');
                        }else{
                            bs4pop.alert(data.result, {type: 'error'});
                        }
                    },
                    error : function() {
                        bui.loading.hide();
                        bs4pop.alert('远程访问失败', {type: 'error'});
                    }
                });
            }
        })
    }


    /**
     * 查询处理
     */
    function queryCustomerDataHandler() {
        currentSelectRowIndex = undefined;
        $('#toolbar button').attr('disabled', false);
        _customerGrid.bootstrapTable('refreshOptions', {url: '/customer/listPage.action'});
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

    //选中行事件
     /**
    还没做

        */
    /*****************************************自定义事件区 end**************************************/
    window.addEventListener('message', function (e) {
        queryCustomerDataHandler();
    }, false)

</script>