<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!doctype html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quản lý danh mục — Nova Store</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body>
<%@ include file="../WEB-INF/views/fragments/header.jspf" %>
<main class="admin-page">
    <div class="container">
        <div class="admin-topbar">
            <div><h1>Quản lý danh mục</h1><p>Tổ chức sản phẩm theo từng nhóm và quản lý trạng thái hiển thị.</p></div>
            <a class="btn btn-primary" href="${pageContext.request.contextPath}/admin/category/add">+ Thêm danh mục</a>
        </div>
        <nav class="admin-nav">
            <a href="${pageContext.request.contextPath}/admin/products">Sản phẩm</a>
            <a class="active" href="${pageContext.request.contextPath}/admin/categories">Danh mục</a>
            <a href="${pageContext.request.contextPath}/">Xem cửa hàng</a>
        </nav>

        <div class="table-card">
            <table class="data-table">
                <thead><tr><th>#</th><th>Hình ảnh</th><th>Tên danh mục</th><th>Trạng thái</th><th>Thao tác</th></tr></thead>
                <tbody>
                <c:forEach items="${listcate}" var="cate" varStatus="status">
                    <tr>
                        <td>${status.index + 1}</td>
                        <td>
                            <div class="table-image">
                                <c:choose>
                                    <c:when test="${not empty cate.images and fn:startsWith(cate.images, 'http')}"><img src="<c:out value='${cate.images}'/>" alt="<c:out value='${cate.categoryname}'/>"></c:when>
                                    <c:when test="${not empty cate.images}">
                                        <c:url var="legacyImageUrl" value="/image"><c:param name="fname" value="${cate.images}"/></c:url>
                                        <img src="${legacyImageUrl}" alt="<c:out value='${cate.categoryname}'/>">
                                    </c:when>
                                    <c:otherwise><div class="image-placeholder">N</div></c:otherwise>
                                </c:choose>
                            </div>
                        </td>
                        <td><strong><c:out value="${cate.categoryname}"/></strong></td>
                        <td><span class="status-badge ${cate.status == 1 ? 'status-active' : 'status-inactive'}">${cate.status == 1 ? 'Hoạt động' : 'Đang khóa'}</span></td>
                        <td>
                            <div class="table-actions">
                                <a class="action-link" href="${pageContext.request.contextPath}/admin/category/edit?id=${cate.categoryid}">Sửa</a>
                                <form class="inline-form" action="${pageContext.request.contextPath}/admin/category/delete" method="post" data-confirm="Bạn có chắc muốn xóa danh mục này?">
                                    <input type="hidden" name="id" value="${cate.categoryid}">
                                    <button class="action-link danger" type="submit">Xóa</button>
                                </form>
                            </div>
                        </td>
                    </tr>
                </c:forEach>
                <c:if test="${empty listcate}"><tr><td colspan="5"><div class="empty-state"><h3>Chưa có danh mục</h3><p>Hãy tạo danh mục trước khi thêm sản phẩm.</p></div></td></tr></c:if>
                </tbody>
            </table>
        </div>
    </div>
</main>
<%@ include file="../WEB-INF/views/fragments/footer.jspf" %>
</body>
</html>
