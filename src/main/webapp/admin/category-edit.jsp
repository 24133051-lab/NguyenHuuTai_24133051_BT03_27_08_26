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
        Edit Category
    </title>

</head>

<body>

<h1>SỬA CATEGORY</h1>

<form
    action="${pageContext.request.contextPath}/admin/category/update"
    method="post"
    enctype="multipart/form-data">

    <input
        type="hidden"
        name="categoryid"
        value="${cate.categoryid}">

    <label>
        Category name:
    </label>

    <br>

    <input
        type="text"
        name="categoryname"
        value="${cate.categoryname}"
        required>

    <br><br>

    <label>
        Link Images:
    </label>

    <br>

    <input
        type="text"
        name="images"
        value="${cate.images}">

    <br><br>

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

    <br><br>

    <label>
        Upload ảnh mới:
    </label>

    <br>

    <input
        type="file"
        name="images1">

    <br><br>

    <input
        type="radio"
        name="status"
        value="1"
        ${cate.status == 1 ? 'checked' : ''}>

    Hoạt động

    <br>

    <input
        type="radio"
        name="status"
        value="0"
        ${cate.status == 0 ? 'checked' : ''}>

    Khóa

    <br><br>

    <button type="submit">
        Update
    </button>

</form>

<br>

<a href="${pageContext.request.contextPath}/admin/categories">
    Quay lại
</a>

</body>

</html>