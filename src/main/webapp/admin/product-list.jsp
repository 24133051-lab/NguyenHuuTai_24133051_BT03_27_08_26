<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!doctype html>
<html lang="vi">
<head>
    <meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quản lý sản phẩm — Nova Store</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body>
<%@ include file="../WEB-INF/views/fragments/header.jspf" %>
<main class="admin-page">
    <div class="container">
        <div class="admin-topbar">
            <div><h1>Quản lý sản phẩm</h1><p>Thêm, sửa, xóa và kiểm soát sản phẩm hiển thị trên cửa hàng.</p></div>
            <a class="btn btn-primary" href="${pageContext.request.contextPath}/admin/product/add">+ Thêm sản phẩm</a>
        </div>
        <nav class="admin-nav">
            <a class="active" href="${pageContext.request.contextPath}/admin/products">Sản phẩm</a>
            <a href="${pageContext.request.contextPath}/admin/categories">Danh mục</a>
            <a href="${pageContext.request.contextPath}/">Xem cửa hàng</a>
        </nav>
        <div class="table-card">
            <table class="data-table">
                <thead><tr><th>#</th><th>Sản phẩm</th><th>Danh mục</th><th>Giá</th><th>Kho</th><th>Trạng thái</th><th>Thao tác</th></tr></thead>
                <tbody>
                <c:forEach items="${products}" var="product">
                    <tr>
                        <td>#${product.id}</td>
                        <td>
                            <div style="display:flex;align-items:center;gap:12px">
                                <div class="table-image"><c:choose><c:when test="${not empty product.imageUrl}"><img src="<c:out value='${product.imageUrl}'/>" alt=""></c:when><c:otherwise><div class="image-placeholder">N</div></c:otherwise></c:choose></div>
                                <strong><c:out value="${product.name}"/></strong>
                            </div>
                        </td>
                        <td><c:out value="${product.category.categoryname}"/></td>
                        <td><strong><fmt:formatNumber value="${product.price}" maxFractionDigits="0"/> ₫</strong></td>
                        <td>${product.quantity}</td>
                        <td><span class="status-badge ${product.active ? 'status-active' : 'status-inactive'}">${product.active ? 'Đang bán' : 'Đã ẩn'}</span></td>
                        <td>
                            <div class="table-actions">
                                <a class="action-link" href="${pageContext.request.contextPath}/product/detail?id=${product.id}" target="_blank">Xem</a>
                                <a class="action-link" href="${pageContext.request.contextPath}/admin/product/edit?id=${product.id}">Sửa</a>
                                <form class="inline-form" action="${pageContext.request.contextPath}/admin/product/delete" method="post" data-confirm="Bạn có chắc muốn xóa vĩnh viễn sản phẩm này?">
                                    <input type="hidden" name="id" value="${product.id}"><button class="action-link danger" type="submit">Xóa</button>
                                </form>
                            </div>
                        </td>
                    </tr>
                </c:forEach>
                <c:if test="${empty products}"><tr><td colspan="7"><div class="empty-state"><h3>Chưa có sản phẩm</h3><p>Hãy tạo sản phẩm đầu tiên để hiển thị trên cửa hàng.</p></div></td></tr></c:if>
                </tbody>
            </table>
        </div>
    </div>
</main>
<%@ include file="../WEB-INF/views/fragments/footer.jspf" %>
</body>
</html>
