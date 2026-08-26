<%@ page contentType="text/html;charset=UTF-8"
         language="java" %>

<!DOCTYPE html>

<html>

<head>

    <meta charset="UTF-8">

    <title>
        Add Category
    </title>

</head>

<body>

<h1>THÊM CATEGORY</h1>

<form
    action="${pageContext.request.contextPath}/admin/category/insert"
    method="post"
    enctype="multipart/form-data">

    <label>
        Category name:
    </label>

    <br>

    <input
        type="text"
        name="categoryname"
        required>

    <br><br>

    <label>
        Link Images:
    </label>

    <br>

    <input
        type="text"
        name="images">

    <br><br>

    <label>
        Upload Images:
    </label>

    <br>

    <input
        type="file"
        name="images1">

    <br><br>

    <label>
        Status:
    </label>

    <br>

    <input
        type="radio"
        name="status"
        value="1"
        checked>

    Hoạt động

    <br>

    <input
        type="radio"
        name="status"
        value="0">

    Khóa

    <br><br>

    <button type="submit">
        Insert
    </button>

</form>

<br>

<a href="${pageContext.request.contextPath}/admin/categories">
    Quay lại
</a>

</body>

</html>