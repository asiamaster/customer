<script>

    //时间范围
    lay('.laydatetime').each(function () {
        laydate.render({
            elem: this
            , trigger: 'click'
            , range: true
        });
    });

    $(function () {
        $(window).resize(function () {
            $('#customerGrid').bootstrapTable('resetView')
        });
        queryDataHandler();
    });

    /**
     * 列表查询方法
     */
    function queryDataHandler() {
        $('#customerGrid').bootstrapTable('refreshOptions', {url: '/customer/listPage.action'});
    }




</script>