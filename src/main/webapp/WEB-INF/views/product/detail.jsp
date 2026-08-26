<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!doctype html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><c:out value="${product.name}"/> — Nova Store</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body>
<%@ include file="../fragments/header.jspf" %>

<main class="detail-section">
    <div class="container">
        <div class="breadcrumb">
            <a href="${pageContext.request.contextPath}/">Trang chủ</a> / 
            <a href="${pageContext.request.contextPath}/product">Sản phẩm</a> / 
            <span><c:out value="${product.name}"/></span>
        </div>

        <div class="detail-grid">
            <div class="detail-media">
                <c:choose>
                    <c:when test="${not empty product.imageUrl}">
                        <img src="<c:out value='${product.imageUrl}'/>" alt="<c:out value='${product.name}'/>">
                    </c:when>
                    <c:otherwise><div class="image-placeholder">N</div></c:otherwise>
                </c:choose>
            </div>

            <div class="detail-info">
                <span class="eyebrow"><c:out value="${product.category.categoryname}"/></span>
                <h1><c:out value="${product.name}"/></h1>
                <div class="detail-price"><fmt:formatNumber value="${product.price}" maxFractionDigits="0"/> ₫</div>
                <p class="detail-description"><c:out value="${empty product.description ? 'Sản phẩm mới tại Nova Store. Thông tin chi tiết đang được cập nhật.' : product.description}"/></p>

                <div class="detail-facts">
                    <div class="detail-fact"><small>Tình trạng</small><strong>${product.quantity > 0 ? 'Còn hàng' : 'Tạm hết hàng'}</strong></div>
                    <div class="detail-fact"><small>Số lượng hiện có</small><strong>${product.quantity} sản phẩm</strong></div>
                    <div class="detail-fact"><small>Danh mục</small><strong><c:out value="${product.category.categoryname}"/></strong></div>
                    <div class="detail-fact"><small>Mã sản phẩm</small><strong>#${product.id}</strong></div>
                </div>

                <a class="btn btn-primary" href="${pageContext.request.contextPath}/product">← Quay lại danh sách</a>
            </div>
        </div>
    </div>
</main>

<%@ include file="../fragments/footer.jspf" %>
</body>
</html>
