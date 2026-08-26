<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!doctype html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Kích hoạt tài khoản — Nova Store</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body>
<%@ include file="../fragments/header.jspf" %>
<main class="auth-page">
    <div class="auth-shell">
        <aside class="auth-aside">
            <a class="brand" href="${pageContext.request.contextPath}/"><span class="brand-mark">N</span><span>NOVA</span></a>
            <div class="auth-aside-copy">
                <h2>Chỉ còn một bước cuối.</h2>
                <p>Kiểm tra hộp thư và dùng mã gồm 6 chữ số để bảo vệ tài khoản của bạn.</p>
                <div class="auth-points">
                    <div class="auth-point"><span>✉</span>OTP được gửi trực tiếp qua email</div>
                    <div class="auth-point"><span>◷</span>Mã có hiệu lực trong 5 phút</div>
                    <div class="auth-point"><span>✓</span>Mỗi mã chỉ sử dụng một lần</div>
                </div>
            </div>
        </aside>
        <section class="auth-form-panel">
            <div class="auth-heading">
                <span class="eyebrow">Xác minh email</span>
                <h1>Kích hoạt tài khoản</h1>
                <p>Nhập email và mã OTP bạn vừa nhận.</p>
            </div>
            <c:if test="${not empty error}"><div class="alert alert-error"><span>!</span><c:out value="${error}"/></div><br></c:if>

            <form action="${pageContext.request.contextPath}/activate" method="post">
                <div class="form-group">
                    <label for="email">Địa chỉ email</label>
                    <input class="form-control" id="email" type="email" name="email" value="<c:out value='${email}'/>" required>
                </div>
                <div class="form-group">
                    <label for="otp">Mã OTP 6 chữ số</label>
                    <input class="form-control otp-input" id="otp" type="text" name="otp" inputmode="numeric" pattern="[0-9]{6}" maxlength="6" placeholder="000000" autocomplete="one-time-code" required autofocus>
                </div>
                <button class="btn btn-primary btn-block" type="submit">Kích hoạt tài khoản</button>
            </form>

            <form class="resend-form" action="${pageContext.request.contextPath}/activate/resend" method="post">
                <input type="hidden" name="email" value="<c:out value='${email}'/>">
                <span class="help-text">Chưa nhận được mã? </span>
                <button class="link-button" type="submit">Gửi lại OTP</button>
            </form>
            <div class="auth-footer"><a href="${pageContext.request.contextPath}/login">← Quay lại đăng nhập</a></div>
        </section>
    </div>
</main>
<%@ include file="../fragments/footer.jspf" %>
</body>
</html>
