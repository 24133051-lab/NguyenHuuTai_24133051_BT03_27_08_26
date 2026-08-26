<%@ page contentType="text/html;charset=UTF-8"
         language="java" %>

<%@ taglib prefix="c"
           uri="jakarta.tags.core" %>

<%@ taglib prefix="fn"
           uri="jakarta.tags.functions" %>

<!DOCTYPE html>

<html>

<head>

    <meta charset="UTF-8">

    <title>
        Quản lý Category
    </title>

</head>

<body>

<h1>QUẢN LÝ CATEGORY - JPA</h1>

<a href="${pageContext.request.contextPath}/admin/category/add">
    Thêm Category
</a>

<br><br>

<table border="1"
       cellpadding="8"
       cellspacing="0">

    <tr>

        <th>STT</th>
        <th>Images</th>
        <th>Category name</th>
        <th>Status</th>
        <th>Action</th>

    </tr>

    <c:forEach
        items="${listcate}"
        var="cate"
        varStatus="stt">

        <tr>

            <td>
                ${stt.index + 1}
            </td>

            <td>

                <c:choose>

                    <c:when test="${empty cate.images}">
                        Không có ảnh
                    </c:when>

                    <c:when test="${fn:startsWith(cate.images, 'http')}">

                        <img
                            src="${cate.images}"
                            width="120"
                            height="90">

                    </c:when>

                    <c:otherwise>

                        <c:url
                            value="/image"
                            var="imgUrl">

                            <c:param
                                name="fname"
                                value="${cate.images}"/>

                        </c:url>

                        <img
                            src="${imgUrl}"
                            width="120"
                            height="90">

                    </c:otherwise>

                </c:choose>

            </td>

            <td>
                ${cate.categoryname}
            </td>

            <td>

                <c:choose>

                    <c:when test="${cate.status == 1}">
                        Hoạt động
                    </c:when>

                    <c:otherwise>
                        Khóa
                    </c:otherwise>

                </c:choose>

            </td>

            <td>

                <a href="${pageContext.request.contextPath}/admin/category/edit?id=${cate.categoryid}">
                    Sửa
                </a>

                |

                <a
                    href="${pageContext.request.contextPath}/admin/category/delete?id=${cate.categoryid}"
                    onclick="return confirm('Bạn có chắc muốn xóa?')">

                    Xóa

                </a>

            </td>

        </tr>

    </c:forEach>

</table>

</body>

</html>