<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}/" />
<c:set var="proPath" value="${empty pageContext.request.contextPath ? '' : contextPath}" />
<!DOCTYPE html>
<html>
<head>

    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>vindisk</title>
    <link rel="shortcut icon" href="images/logo_small.png">
    <link rel="stylesheet" href="css/reset.css">
    <link rel="stylesheet" href="css/bootstrap.css">
    <script src="js/jquery.min.js"></script>
    <script src="js/bootstrap.js"></script>
    <script src="//ajax.googleapis.com/ajax/libs/jqueryui/1.10.3/jquery-ui.min.js"></script>


    <link rel="stylesheet" href="css/jquery.lineProgressbar.min.css"
          type="text/css"></link>
    <script type="text/javascript" src="js/jquery.lineProgressbar.js"></script>
    <script type="text/javascript">
        jQuery(document).ready(function ($) {
            //获取使用量
            var space = ${sessionScope.percentSpace};
            //render使用量条
            $('#progressbar1').LineProgressbar({
                percentage: space,
                fillBackgroundColor: '#f1c40f',
                height: '20px',
                radius: '50px',
            });

        });

        $(function () {
            //下载之后跳转，为了更新下载数目
            $("tbody tr td[jq='true']")
                .find("a:last")
                .click(
                    function () {

                        setTimeout(
                            function () {

                                location.href = "<c:url value='CatalogServlet?method=myCatalog&cid=${catalog.cId}' />";
                            }, 1000);

                    });
            //点击删除进入的逻辑
            $("tbody tr td[jq='true']").find("a:first").click(function () {

                if (!confirm('are you sure?')) {
                    return false;
                }

                var iids = $(this).attr("iid");

                $.ajax({
                    url: "${proPath}mydisks/FileServlet",
                    data: {
                        method: "deleteFile",
                        fid: iids
                    },
                    type: "POST",
                    dataType: "json",
                    async: false,
                    cache: false,
                    success: function (result) {
                        if (result) {
                            alert("删除成功！");
                            $("." + iids).remove();
                            if ($(".abc").length == 0) {
                                $("#msg").text("没有了");
                            }

                        } else {
                            alert("删除失败");
                        }
                    }

                });

            });

        })
    </script>
<body>
<div class="top" id="all">
    <!--顶部导航-->
    <div class="top_nav">
        <nav class="navbar navbar-dark bg-inverse">
            <div class="nav_width">
                <a class="navbar-brand" href="#"> <img src="images/logo1.png"/>
                    <p>vindisk</p></a>
                <ul class="nav navbar-nav pull-right">
                    <!-- <li class="nav-item active"><a class="nav-link" href="#">主页<span
                class="sr-only">(current)</span></a></li> -->
                    <li class="nav-item"><a class="nav-link"
                                            href="<c:url value='${proPath}CatalogServlet?method=myCatalog' />">My file<span
                            class="sr-only">(current)</span> </a>
                    </li>

                    <li class="nav-item"><a class="nav-link"
                                            href="<c:url value='${proPath}SharedServlet?method=myShared' />">My share</a>
                    </li>

                    <li class="nav-item"><a class="nav-link"
                                            href="<c:url value='${proPath}SharedServlet?method=myShared' />">public share</a>
                    </li>
                    <li class="nav-item"><a class="nav-link"
                                            href="<c:url value='${proPath}UserServlet?method=quit' />">Logout</a>
                    </li>
                </ul>
            </div>
        </nav>
    </div>
    <div class="leftnav">
        <div class="container-fluid">
            <div class="row">
                <div class="col-xs-2 col-sm-2 col-md-2 col-lg-2">
                    <div class="admin">
                        <img src="images/user.jpg">
                        <p>${sessionScope.user.userName}</p>
                        <table class="table">
                            <tr>
                                <td>share</td>
                                <td>subscribe</td>
                                <td>fans</td>
                            </tr>
                            <tr>
                                <td><a
                                        href="<c:url value='SharedServlet?method=myShared' />">${sessionScope.session_user.shareCount
                                        }</a>
                                </td>
                                <td><a
                                        href="<c:url value='FollowServlet?method=myFollow' />">${sessionScope.session_user.followcount
                                        }</a>
                                </td>
                                <td><a
                                        href="<c:url value='FollowServlet?method=myFans' />">${sessionScope.session_user.follower}</a>
                                </td>
                            </tr>
                        </table>
                    </div>
                    volume:
                    <div id="progressbar1"></div>
                </div>
                <div class="col-xs-10 col-sm-10 col-md-10 col-lg-10">
                    <!--右侧内容区域-->
                    <div class="rightarea">
                        <div class="righthead">
<%--                            <p>my file</p>--%>
                            <button onclick="upload()" type="button"
                                    class="btn btn-primary-outline btn-sm">
									<span><i
                                            class="glyphicon glyphicon glyphicon-circle-arrow-up"></i> </span>
                                upload file
                            </button>
                            <span id="filepath"> </span> </br>
                            <button onclick="prom()" type="button"
                                    class="btn btn-primary-outline btn-sm">
									<span><i
                                            class="glyphicon glyphicon glyphicon-circle-arrow-up"></i> </span>
                                new folder
                            </button>
                            <p></p>
                            <div class="myprogress clearfix" id="myprogress"
                                 style="display:none;">
                                <progress
                                        class="progress progress-striped progress-info progress-animated"
                                        value="0" max="100"></progress>
                                <p class="myprogress_text">0</p>
                            </div>
                        </div>
                        <a href="<c:url value='CatalogServlet?method=myCatalog' />">my file</a>>>
                        <!--面包屑指引栏  -->
                        <c:forEach items="${path}" var="p">
                            <a
                                    href="<c:url value='CatalogServlet?method=myCatalog&cid=${p.cId }' /> ">${p.cName}</a>>></c:forEach>

                        <table class="table fp">
                            <tr>
                                <td></td>
                                <td>name</td>
                                <td>modify date</td>
                                <td>size</td>
                                <td>type</td>
                                <td>download count</td>
                                <td>action</td>
                            </tr>
                            <c:choose>
                            <c:when
                                    test="${ empty catalog.children  and  empty catalog.myFile  }">
                                <tr>
                                    <td>sorry，no file</td>
                                </tr>
                            </c:when>

                            <c:otherwise>
                            <!--先展示所有文件夹  -->
                            <c:forEach items="${catalog.children }" var="c">
                                <tr>
                                    <td>
                                        <img width="50" height="30" src='<c:url value="images/1024.png"></c:url>'/>
                                    </td>
                                    <td>
                                        <a href='<c:url value="CatalogServlet?method=myCatalog&cid=${c.cId }"></c:url>'>${c.cName }
                                        </a>
                                    </td>
                                    <td>${c.cDate }</td>
                                    <td></td>
                                    <td>folder</td>
                                    <td></td>
                                    <td>
                                        <a href='<c:url value="CatalogServlet?method=deleteCatalog&cid=${c.cId }&pid=${c.parent.cId }&name=${c.cName }" ></c:url>'
                                           onclick="return confirm('are you sure?')">delete</a>
                                        <a href='javascript:void(0)'
                                           onclick="changeName('0','${c.cId}','${c.parent.cId }')">rename</a>
                                        <a name="move"
                                           href="${proPath}CatalogServlet?method=moveCatalog&cid=${c.cId}&do=get&type=folder">move</a>
                                    </td>
                                </tr>
                            </c:forEach>

                            <!--再展示文件  -->
                            <c:forEach items="${catalog.myFile }" var="f">
                            <tr class="${f.fId } abc">
                                <td><img width="40" height="20" src='<c:url value="images/02.png"></c:url>'/></td>
                                <td>${f.fName }</td>
                                <td>${f.fUploadtime }</td>
                                <td><fmt:formatNumber type="number" value="${f.fSize/1048576}"
                                                      maxFractionDigits="4"/>MB
                                </td>
                                <td>${f.fType }</td>
                                <td>${f.fDowncount }</td>
                                <td jq="true"><a href="#" iid=${f.fId }>delete</a>
                                    <a href='javascript:void(0)' onclick="changeName('1','${f.fId }')">rename</a>
                                    <a name="move"
                                       href="${proPath}CatalogServlet?method=moveCatalog&fid=${f.fId}&do=get&type=file">move</a>
                                    <a href='<c:url value="DownLoadServlet?fid=${f.fId }&cid=${c.cId }"></c:url>'>download</a>
                                </td>
                                </c:forEach>
                                </c:otherwise>
                                </c:choose>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>


<div id="modal"></div>
<script>

    // $("#modal").position({
    //     my: "center",
    //     at: "center",
    //     of: window
    // });

    var winW = $(window).width() - 1080;
    var winH = $(window).height() - 380;
    $(function () {
        $('a[name=move]').on('click', function (e) {
            e.preventDefault(); //stops from going to test2.html
           // $.fn.button.noConflict();
            $('#modal').load(this.href).dialog({
                width: winW,
                height: winH,
                modal: true

            });
        });
    });


</script>

<!--底部-->
<div class="footer">
    <p>Copyright &copy; 2018vincent</p>
</div>
<!-- Include all compiled plugins (below), or include individual files as needed -->

<script type="text/javascript">
    window.onload = function () {

        var obj2 = document.getElementById("uploadform").children;
        obj2[0].value = "";
    };

    function shared(id, name) {
        var s = confirm("are you sure make " + name + " being public?");
        if (s) {
            location.href = "<c:url value='SharedServlet?method=addShared' />&id="
                + id + "&isPublic=true";
        } else {
            location.href = "<c:url value='SharedServlet?method=addShared' />&id="
                + id + "&isPublic=false";
        }
    }

    //对文件夹进行改名，已经废弃
    function rename(id, name, pid) {
        //获取改变后的名字
        var name = prompt("the " + name + "rename to :", name);
        if (name === "") {
            alert("folder name can not be empty");
        }
        //访问服务器进行改名
        if (name) {
            location.href = "<c:url value='CatalogServlet?method=rename' />&id="
                + id + "&name=" + name + "&pid=" + pid;
        }
    }

    function download(fid, name) {
        location.href = "<c:url value='DownloadServlet' />?filename="
            + name + "&fid=" + fid;
    }

    //新增文件夹
    function prom() {
        var name = prompt("folder name", "new folder");

        if (name === "") {
            alert("file name must be fill in");
        }

        if (name)//如果返回的有内容
        {
            location.href = "<c:url value='CatalogServlet?method=createCatalog&cid=${catalog.cId}' />&name="
                + encodeURI(encodeURI(name));
        }

    }

    //对文件进行改名
    function changeName(flag, cId, pId) {
        var name = prompt("the name after modifying", "the name after modifying");
        if (name === "") {
            alert("file name can not be empty");
            return;
        }
        if (name) {
            if (flag == 0) {
                location.href = "${proPath}CatalogServlet?method=changeName&cid="
                    + cId
                    + "&name="
                    + encodeURI(encodeURI(name) + "&pid=" + pId);
            } else if (flag == 1) {
                $.ajax({
                    url: "mydisks/FileServlet",
                    data: {
                        method: "changeName",
                        fid: cId,
                        name: name
                    },
                    type: "POST",
                    dataType: "json",
                    async: false,
                    cache: false,
                    success: function (result) {
                        if (result) {
                            alert("update succeeful！");
                            if ($(".abc").length == 0) {
                                $("#msg").text("没有了");
                            }
                            top.document.location.reload();
                        } else {
                            alert("update faild");
                        }
                    }

                });
            }
        }
    }

    //删除文件，已废弃
    function deleteFile(name, id, pid) {
        var r = confirm("delete" + name);
        if (r == true) {
            location.href = "<c:url value='CatalogServlet?method=removeCatalogById' />&id="
                + id + "&pid=" + pid;
        }
    }

    //上传文件逻辑，当有没有文件的点击上传就要求输入文件，没有文件的话就可以上传
    function upload() {
        var obj = document.getElementById("uploadform").children;

        if (obj[0].value === "") {
            obj[0].click();

        } else {
            obj[3].click();
            $('#myprogress').show();
            window.setTimeout("getProgressBar()", 100);
        }
    }

    //向用户展示将要上传的文件的名字
    function fileName() {
        var obj1 = document.getElementById("uploadform").children;
        var inf = document.getElementById('filepath');
        var Name = obj1[0].value;
        inf.innerHTML = "the upload file is :" + Name;
    }

    //上传进度条，被upload函数调用
    function getProgressBar() {
        var timestamp = (new Date()).valueOf();
        //上传的时候不停调用后台的progress servlet来获取上传进度
        $.getJSON("<c:url value='ProgressServlet' />", function (json) {
            //获取html中的id为myprogress的div之其子元素的class含有的progress的子元素
            var myprogress = $('#myprogress').children(".progress");
            //获取进度条的文字输入位置
            var myprogress_text = $('#myprogress').children(
                ".myprogress_text");
            //后台传来的pBytesRead（上传了多少)以及pContentLength上传总长度获取上传进度
            myprogress.attr('value', function () {
                var value = json.pBytesRead / json.pContentLength * 100;
                //使用文字表达
                myprogress_text.text((value + "").substring(0, 4) + "%");
                return value;
            });
            //如果总长度等于已经上传的长度,就进行刷新
            if (json.pBytesRead == json.pContentLength) {

                //	location.href = "<c:url value='CatalogServlet?method=myCatalog&cid=${catalog.cId}' />";
            } else {
                //否则继续调用本方法，进而继续向服务器获取上传进度信息
                window.setTimeout("getProgressBar()", 100);
            }
        });
    }

    //对文件进行格式化
    $(function () {
        var msg = "${msg}";
        if (!msg == "")
            alert(msg);
        //获取不同类型文件
        var fileicontype = $("td>p.icontype");
        var foldericontype = $("a>p.icontype");

        foldericontype.each(function (index, element) {
            element.className = "folder";
        });
        fileicontype.each(function (index, element) {
            element.className = checktype(element.innerText);
        });
        //获取类型
        function checktype(filename) {
            filename = filename.toLocaleLowerCase();
            var b = filename.split('.');
            if (b.length < 2) {
                return "others";
            }
            var lastword = b[b.length - 1];
            switch (lastword) {
                case "exe":
                    return "exe";
                    break;
                case "bmp":
                case "gif":
                case "jpeg":
                case "png":
                case "jpg":
                    return "jpg";
                    break;
                case "apk":
                    return "apk";
                    break;
                case "wav":
                case "mp3":
                case "wma":
                    return "mp3";
                    break;
                case "zip":
                case "rar":
                case "7z":
                    return "zip";
                    break;
                case "mp4":
                case "avi":
                case "wmv":
                case "rmvb":
                    return "mp4";
                    break;
                default:
                    return "others";
                    break;
            }
        }
    });
</script>
<!-- 上传文件 -->
<form id="uploadform" action="<c:url value='UploadServlet' />"
      method="post" enctype="multipart/form-data" style="display:none;">
    <input type="file" name="filename" multiple="multiple"
           onChange="fileName()"/><br/> <input type="hidden" name="cid"
                                               value="${catalog.cId }"/> <input type="submit" value="上传">
</form>
</body>