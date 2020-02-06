<script>

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