function successmsg(title, content) {
    toastr.success(content,title, {
        "timeOut" : "1500",
        "closeButton" : true,
        "positionClass" : "toast-top-right"
    })
    return false;
}

function errormsg(title, content) {
    toastr.error(content, title, {
        "timeout" : "8000",
        "closeButton" : true,
        "positionClass" : "toast-top-right"
    })
    return false;
}

function nextPage(pageNum, pageSize) {
    if ($('#searchForm').length != 0) {
        var url = $('#searchForm').attr('action');
        var data = "?pageNum=" + pageNum + "&pageSize=" + pageSize + "&" + $('#searchForm').serialize();
        window.location.href = url + data;
    } else {
        var url = window.location.href;
        url = url.split('?')[0];
        var data = "?pageNum=" + pageNum + "&pageSize=" + pageSize;
        window.location.href = url + data;
    }
}

function  commonAjax(url, data) {
    var code = 0;
    // $.now() 返回当前时间戳
    if (data) {
        data = '_=' + $.now() + '&' + data;
    }else {
        data = '_=' + $.now();
    }

    var jsonObj;
    $.ajax({
        url: url,
        type: 'POST',
        data: data,
        cache: false,
        timeout: 60000
    })
    .done(function (ret) {

        })
    .fail(function (jqXHR, textStatus, errorThrown) {
        if(textStatus&&textStatus=='error') {
            errormsg("error!", 'System Error')
        };
        if(textStatus&&textStatus=='timeout') {
            errormsg("error!", 'Response Timeout')
        };
    })
}

