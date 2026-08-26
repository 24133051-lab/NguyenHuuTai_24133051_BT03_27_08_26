<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!doctype html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Tất cả sản phẩm — Nova Store</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body>
<%@ include file="../fragments/header.jspf" %>

<main>
    <section class="page-hero">
        <div class="container">
            <span class="eyebrow">Bộ sưu tập Nova</span>
            <h1>Tất cả sản phẩm</h1>
            <p>Khám phá trọn bộ sản phẩm, được sắp xếp từ mới nhất.</p>
        </div>
    </section>

    <section class="section">
        <div class="container">
            <div class="results-bar">
                <span>Tìm thấy <strong>${totalProducts}</strong> sản phẩm</span>
                <span>Trang ${currentPage}/${totalPages} · 6 sản phẩm/trang</span>
            </div>

            <div class="product-grid">
                <c:forEach items="${products}" var="product">
                    <a class="product-card" href="${pageContext.request.contextPath}/product/detail?id=${product.id}">
                        <div class="product-image-wrap">
                            <c:choose>
                                <c:when test="${not empty product.imageUrl}">
                                    <img class="product-image" src="<c:out value='${product.imageUrl}'/>" alt="<c:out value='${product.name}'/>" loading="lazy">
                                </c:when>
                                <c:otherwise><div class="image-placeholder">N</div></c:otherwise>
                            </c:choose>
                        </div>
                        <div class="product-info">
                            <span class="product-category"><c:out value="${product.category.categoryname}"/></span>
                            <h3><c:out value="${product.name}"/></h3>
                            <div class="product-meta">
                                <span class="price"><fmt:formatNumber value="${product.price}" maxFractionDigits="0"/> ₫</span>
                                <span class="stock">Còn ${product.quantity}</span>
                            </div>
                        </div>
                    </a>
                </c:forEach>

                <c:if test="${empty products}">
                    <div class="empty-state">
                        <div class="empty-icon">◇</div>
                        <h3>Danh sách đang trống</h3>
                        <p>Các sản phẩm mới sẽ sớm xuất hiện tại đây.</p>
                    </div>
                </c:if>
            </div>

            <c:if test="${totalPages > 1}">
                <nav class="pagination" aria-label="Phân trang sản phẩm">
                    <a class="page-link ${currentPage == 1 ? 'disabled' : ''}" href="${pageContext.request.contextPath}/product?page=${currentPage - 1}">‹</a>
                    <c:forEach begin="1" end="${totalPages}" var="pageNumber">
                        <a class="page-link ${currentPage == pageNumber ? 'active' : ''}" href="${pageContext.request.contextPath}/product?page=${pageNumber}">${pageNumber}</a>
                    </c:forEach>
                    <a class="page-link ${currentPage == totalPages ? 'disabled' : ''}" href="${pageContext.request.contextPath}/product?page=${currentPage + 1}">›</a>
                </nav>
            </c:if>
        </div>
    </section>
</main>

<%@ include file="../fragments/footer.jspf" %>
</body>
</html>
