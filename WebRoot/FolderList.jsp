<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:set var="proPath" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>File/Folder Move</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <link href="${proPath}/css/general.css" rel="stylesheet" type="text/css"/>
    <link href="${proPath}/css/main.css" rel="stylesheet" type="text/css"/>
</head>
<body>
<form method="post" action="" name="listForm">
    <!-- start ad position list -->
    <div class="list-div" id="listDiv">
        <table width="100%" cellspacing="1" cellpadding="2" id="list-table">
            <tbody>
            <tr>
                <th>Folder</th>
                <th><a href="javascript:window.location.reload();">X</a></th>
            </tr>
            <c:forEach items="${catalogList}" var="list">
                <tr align="center" class="${list.cLevel}"
                    id="${list.cLevel }_${list.cId }">
                    <td align="left" class="first-cell">
                        <c:set var="cLevel" value="${list.cLevel}" scope="request"></c:set>
                        <%
                            Integer cLevel = (Integer) pageContext.getRequest()
                                    .getAttribute("cLevel");
                            for (int i = 0; i < cLevel; i++) {
                                out.print("&nbsp&nbsp&nbsp");
                            }
                        %>
                        <img src="${proPath}/images/menu_minus.gif"
                             id="icon_${list.cLevel }_${list.cId }" width="9" height="9"
                             border="0" style="margin-left:0em" onclick="rowClicked(this)">
                        <span>
                            <c:choose>
                                <c:when test="${fileType == true}">
                                    <c:if test="${!list.highlight}">
                                        <a href="${proPath}/CatalogServlet?method=moveCatalog&do=post&mcid=${list.cId }&fid=${id}&type=file">
                                                ${list.cName }
                                        </a>
                                    </c:if>
                                    <c:if test="${list.highlight}">
                                        <a href="#" style="color:blue">${list.cName }</a></c:if>
                                </c:when>
                                <c:otherwise>
                                    <c:if test="${!list.highlight}">
                                        <a href="${proPath}/CatalogServlet?method=moveCatalog&do=post&mcid=${list.cId }&cid=${id}&type=folder">
                                                ${list.cName }
                                        </a>
                                    </c:if>
                                    <c:if test="${list.highlight}">
                                        <a href="#" style="color:blue">${list.cName }</a></c:if>
                                </c:otherwise>
                            </c:choose>
                        </span>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</form>
<div id="footer">CCopyRight &copy; vincent</div>
</div>
<script>
    /**
     * 折叠分类列表
     */
    var imgPlus = new Image();
    imgPlus.src = "${proPath}/images/menu_plus.gif";

    function rowClicked(obj) {
        // 当前图像
        img = obj;
        // 取得上二级tr>td>img对象，也就是当前节点的爷节点
        obj = obj.parentNode.parentNode;
        // 获取整个分类列表表格，目的是之后遍历它，获取每一行 tr
        var tbl = document.getElementById("list-table");
        // 当前分类级别
        var lvl = parseInt(obj.className);
        // 是否找到元素，当前用户点击的节点是否为之后遍历相同父类的节点，目的是修改跟住这个tr的后面tr的展示属性
        var fnd = false;
        //当用户点击折叠的图片时候意味着其子节点需要隐藏，所以将其属性设定为none
        var sub_display = img.src.indexOf('menu_minus.gif') > 0 ? 'none'
            : 'table-row';
        // 遍历所有的分类,rows 集合返回表格中所有行
        for (i = 0; i < tbl.rows.length; i++) {
            //获取当前行
            var row = tbl.rows[i];
            //如果当前行等于刚刚获取到的tr，那么就代表当前行是当前传入的obj，也就是用户点击的展开符号
            if (row == obj) {
                // 找到当前行
                fnd = true;
                //document.getElementById('result').innerHTML += 'Find row at ' + i +"<br/>";
            }
            //如果用户点击展开符号，后面还有其他子类就进入这个语块体
            else {
                //判断用户点击的展开图标是否为true，是的话才进入，否则会出现展开其他非本节点的子节点。
                if (fnd == true) {
                    //获取tr的class名字，该名字的含意是该节点的深度，如顶类则是1，顶类的子节点则是2
                    var cur = parseInt(row.className);
                    //获取节点深度和ID，如1_5这样子，然后拼接为icon_1_5
                    var icon = 'icon_' + row.id;
                    //如果当前深度大于该tr的深度，则代表是子节点
                    if (cur > lvl) {
                        //根据父节点的图标状态将其设定是否展示的属性
                        row.style.display = sub_display;
                        if (sub_display != 'none') {
                            //获取当前用户点击的图片
                            var iconimg = document.getElementById(icon);
                            //将图片更改为被用户点击之后的折叠状态，因为用户从加号图标点击，然后节点展开，那么就要更改相应的图片
                            iconimg.src = iconimg.src.replace('plus.gif',
                                'minus.gif');
                        }
                    }
                    //当找到不是当前节点深度不是大于用户点击的节点深度，意味着跟住下面的tr都不是该节点的子节点
                    else {
                        fnd = false;
                        break;
                    }
                }
            }
        }

        //做多一重逻辑检验。确保剪嘴图标到被修改为正确的图标
        for (i = 0; i < obj.cells[0].childNodes.length; i++) {
            var imgObj = obj.cells[0].childNodes[i];
            if (imgObj.tagName == "IMG" && imgObj.src != '${proPath}/images/menu_arrow.gif') {
                imgObj.src = (imgObj.src == imgPlus.src) ? '${proPath}/images/menu_minus.gif'
                    : imgPlus.src;
            }
        }
    }
</script>

<SCRIPT LANGUAGE="JavaScript">

/*
    var trs = document.getElementById('list-table').getElementsByTagName(
        'tr');
    window.onload = function () {
        for (var i = 0; i < trs.length; i++) {
            trs[i].onmousedown = function () {
                tronmousedown(this);
            }
        }
    }

    function tronmousedown(obj) {
        for (var o = 0; o < trs.length; o++) {
            if (trs[o] == obj) {
                trs[o].style.backgroundColor = '#DFEBF2';
            } else {
                trs[o].style.backgroundColor = '';
            }
        }
    }
*/


</SCRIPT>
</body>
</html>