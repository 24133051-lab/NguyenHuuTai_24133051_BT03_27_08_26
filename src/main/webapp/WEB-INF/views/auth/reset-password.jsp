<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!doctype html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đặt lại mật khẩu — Nova Store</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body>
<%@ include file="../fragments/header.jspf" %>
<main class="auth-page">
    <div class="auth-shell">
        <aside class="auth-aside">
            <a class="brand" href="${pageContext.request.contextPath}/"><span class="brand-mark">N</span><span>NOVA</span></a>
            <div class="auth-aside-copy">
                <h2>Tạo một mật khẩu mới.</h2>
                <p>Dùng OTP vừa nhận và chọn mật khẩu khác với mật khẩu cũ để bảo vệ tài khoản.</p>
                <div class="auth-points">
                    <div class="auth-point"><span>✓</span>Ít nhất 8 ký tự</div>
                    <div class="auth-point"><span>✓</span>Bao gồm cả chữ và số</div>
                    <div class="auth-point"><span>✓</span>OTP chỉ dùng một lần</div>
                </div>
            </div>
        </aside>
        <section class="auth-form-panel">
            <div class="auth-heading">
                <span class="eyebrow">Bảo mật tài khoản</span>
                <h1>Đặt lại mật khẩu</h1>
                <p>Hoàn tất thông tin dưới đây để tạo mật khẩu mới.</p>
            </div>
            <c:if test="${not empty error}"><div class="alert alert-error"><span>!</span><c:out value="${error}"/></div><br></c:if>

            <form action="${pageContext.request.contextPath}/reset-password" method="post">
                <div class="form-group">
                    <label for="email">Địa chỉ email</label>
                    <input class="form-control" id="email" type="email" name="email" value="<c:out value='${email}'/>" required>
                </div>
                <div class="form-group">
                    <label for="otp">Mã OTP 6 chữ số</label>
                    <input class="form-control otp-input" id="otp" type="text" name="otp" inputmode="numeric" pattern="[0-9]{6}" maxlength="6" placeholder="000000" autocomplete="one-time-code" required autofocus>
                </div>
                <div class="form-grid">
                    <div class="form-group">
                        <label for="password">Mật khẩu mới</label>
                        <div class="password-wrap">
                            <input class="form-control" id="password" type="password" name="password" placeholder="Ít nhất 8 ký tự" autocomplete="new-password" required>
                            <button class="password-toggle" type="button" data-password-toggle="password">Hiện</button>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="confirmPassword">Nhập lại mật khẩu</label>
                        <div class="password-wrap">
                            <input class="form-control" id="confirmPassword" type="password" name="confirmPassword" placeholder="Xác nhận mật khẩu" autocomplete="new-password" required>
                            <button class="password-toggle" type="button" data-password-toggle="confirmPassword">Hiện</button>
                        </div>
                    </div>
                </div>
                <button class="btn btn-primary btn-block" type="submit">Đổi mật khẩu</button>
            </form>
            <div class="auth-footer"><a href="${pageContext.request.contextPath}/forgot-password">Gửi lại OTP</a> · <a href="${pageContext.request.contextPath}/login">Đăng nhập</a></div>
        </section>
    </div>
</main>
<%@ include file="../fragments/footer.jspf" %>
</body>
</html>
