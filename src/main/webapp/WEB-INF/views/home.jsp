<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!doctype html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Nova Store — Khám phá sản phẩm mới</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body>
<%@ include file="fragments/header.jspf" %>

<main>
    <section class="hero">
        <div class="container hero-grid">
            <div>
                <span class="eyebrow">Bộ sưu tập mới 2026</span>
                <h1>Chọn điều mới.<br><em>Sống chất riêng.</em></h1>
                <p class="hero-copy">Khám phá những sản phẩm mới nhất được tuyển chọn cho nhịp sống hiện đại — đẹp hơn, tiện hơn và đầy cảm hứng.</p>
                <div class="hero-actions">
                    <a class="btn btn-primary" href="${pageContext.request.contextPath}/product">Khám phá sản phẩm <span>→</span></a>
                    <c:if test="${empty sessionScope.currentUser}">
                        <a class="btn btn-secondary" href="${pageContext.request.contextPath}/register">Tạo tài khoản</a>
                    </c:if>
                </div>
                <div class="hero-stats">
                    <div class="hero-stat"><strong>100%</strong><span>Sản phẩm chọn lọc</span></div>
                    <div class="hero-stat"><strong>24/7</strong><span>Trải nghiệm trực tuyến</span></div>
                    <div class="hero-stat"><strong>Cloud</strong><span>Hình ảnh sắc nét</span></div>
                </div>
            </div>
            <div class="hero-visual" aria-hidden="true">
                <div class="visual-orb"></div>
                <c:choose>
                    <c:when test="${not empty latestProducts and not empty latestProducts[0].imageUrl}">
                        <img class="hero-product-image" src="<c:out value='${latestProducts[0].imageUrl}'/>" alt="">
                    </c:when>
                    <c:otherwise>
                        <div class="hero-product-image image-placeholder">N</div>
                    </c:otherwise>
                </c:choose>
                <div class="visual-card top"><span class="spark">★★★★★</span><strong>Trải nghiệm nổi bật</strong><span>Thiết kế tinh tế</span></div>
                <div class="visual-card bottom"><strong>Hàng mới mỗi ngày</strong><span>Luôn có điều để khám phá</span></div>
            </div>
        </div>
    </section>

    <section class="section">
        <div class="container">
            <div class="section-heading">
                <div>
                    <span class="eyebrow">Vừa cập nhật</span>
                    <h2>10 sản phẩm mới nhất</h2>
                    <p>Những lựa chọn vừa xuất hiện tại Nova Store.</p>
                </div>
                <a class="arrow-link" href="${pageContext.request.contextPath}/product">Xem tất cả →</a>
            </div>

            <div class="product-grid home-products">
                <c:forEach items="${latestProducts}" var="product" varStatus="status">
                    <a class="product-card" href="${pageContext.request.contextPath}/product/detail?id=${product.id}">
                        <div class="product-image-wrap">
                            <c:if test="${status.index < 3}"><span class="product-badge">Mới</span></c:if>
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

                <c:if test="${empty latestProducts}">
                    <div class="empty-state">
                        <div class="empty-icon">◇</div>
                        <h3>Chưa có sản phẩm</h3>
                        <p>Đăng nhập và thêm sản phẩm đầu tiên trong khu vực quản trị.</p>
                        <a class="btn btn-primary" href="${pageContext.request.contextPath}/admin/product/add">Thêm sản phẩm</a>
                    </div>
                </c:if>
            </div>
        </div>
    </section>

    <section class="section section-soft">
        <div class="container benefits">
            <article class="benefit"><div class="benefit-icon">✦</div><h3>Sản phẩm mới chọn lọc</h3><p>Danh sách được cập nhật theo thời gian tạo để bạn luôn nhìn thấy lựa chọn mới nhất.</p></article>
            <article class="benefit"><div class="benefit-icon">☁</div><h3>Hình ảnh Cloudinary</h3><p>Hình ảnh được lưu trữ và phân phối nhanh chóng qua hạ tầng đám mây.</p></article>
            <article class="benefit"><div class="benefit-icon">✓</div><h3>Tài khoản an toàn</h3><p>Kích hoạt và khôi phục tài khoản bằng mã OTP gửi trực tiếp qua email.</p></article>
        </div>
    </section>
</main>

<%@ include file="fragments/footer.jspf" %>
</body>
</html>
