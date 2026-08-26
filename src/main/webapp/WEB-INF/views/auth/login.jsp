<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!doctype html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đăng nhập — Nova Store</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body>
<%@ include file="../fragments/header.jspf" %>
<main class="auth-page">
    <div class="auth-shell">
        <aside class="auth-aside">
            <a class="brand" href="${pageContext.request.contextPath}/"><span class="brand-mark">N</span><span>NOVA</span></a>
            <div class="auth-aside-copy">
                <h2>Chào mừng bạn quay lại.</h2>
                <p>Đăng nhập để quản lý sản phẩm và tiếp tục khám phá Nova Store.</p>
                <div class="auth-points">
                    <div class="auth-point"><span>✓</span>Tài khoản đã xác minh bằng OTP</div>
                    <div class="auth-point"><span>✓</span>Mật khẩu được mã hóa an toàn</div>
                    <div class="auth-point"><span>✓</span>Phiên đăng nhập bảo vệ khu vực quản trị</div>
                </div>
            </div>
        </aside>
        <section class="auth-form-panel">
            <div class="auth-heading">
                <span class="eyebrow">Tài khoản</span>
                <h1>Đăng nhập</h1>
                <p>Nhập email và mật khẩu của bạn để tiếp tục.</p>
            </div>

            <c:if test="${param.logout == '1'}"><div class="alert alert-success"><span>✓</span>Bạn đã đăng xuất an toàn.</div><br></c:if>
            <c:if test="${not empty error}"><div class="alert alert-error"><span>!</span><c:out value="${error}"/></div><br></c:if>

            <form action="${pageContext.request.contextPath}/login" method="post">
                <div class="form-group">
                    <label for="email">Địa chỉ email</label>
                    <input class="form-control" id="email" type="email" name="email" value="<c:out value='${email}'/>" placeholder="you@example.com" autocomplete="email" required autofocus>
                </div>
                <div class="form-group">
                    <label for="password">Mật khẩu</label>
                    <div class="password-wrap">
                        <input class="form-control" id="password" type="password" name="password" placeholder="Nhập mật khẩu" autocomplete="current-password" required>
                        <button class="password-toggle" type="button" data-password-toggle="password">Hiện</button>
                    </div>
                </div>
                <div class="form-row-between">
                    <span></span>
                    <a class="form-link" href="${pageContext.request.contextPath}/forgot-password">Quên mật khẩu?</a>
                </div>
                <button class="btn btn-primary btn-block" type="submit">Đăng nhập <span>→</span></button>
            </form>

            <c:if test="${not empty activationEmail}">
                <c:url var="activationUrl" value="/activate"><c:param name="email" value="${activationEmail}"/></c:url>
                <div class="auth-footer"><a href="${activationUrl}">Nhập OTP để kích hoạt tài khoản</a></div>
            </c:if>
            <div class="auth-footer">Chưa có tài khoản? <a href="${pageContext.request.contextPath}/register">Đăng ký miễn phí</a></div>
        </section>
    </div>
</main>
<%@ include file="../fragments/footer.jspf" %>
</body>
</html>
