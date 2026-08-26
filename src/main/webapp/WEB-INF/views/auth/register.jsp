<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!doctype html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đăng ký — Nova Store</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body>
<%@ include file="../fragments/header.jspf" %>
<main class="auth-page">
    <div class="auth-shell">
        <aside class="auth-aside">
            <a class="brand" href="${pageContext.request.contextPath}/"><span class="brand-mark">N</span><span>NOVA</span></a>
            <div class="auth-aside-copy">
                <h2>Bắt đầu hành trình cùng Nova.</h2>
                <p>Tạo tài khoản chỉ trong một phút và xác minh an toàn qua email.</p>
                <div class="auth-points">
                    <div class="auth-point"><span>1</span>Điền thông tin đăng ký</div>
                    <div class="auth-point"><span>2</span>Nhận OTP qua email</div>
                    <div class="auth-point"><span>3</span>Kích hoạt và đăng nhập</div>
                </div>
            </div>
        </aside>
        <section class="auth-form-panel">
            <div class="auth-heading">
                <span class="eyebrow">Tham gia Nova</span>
                <h1>Tạo tài khoản</h1>
                <p>OTP kích hoạt sẽ được gửi tới địa chỉ email của bạn.</p>
            </div>
            <c:if test="${not empty error}"><div class="alert alert-error"><span>!</span><c:out value="${error}"/></div><br></c:if>

            <form action="${pageContext.request.contextPath}/register" method="post">
                <div class="form-group">
                    <label for="fullName">Họ và tên</label>
                    <input class="form-control" id="fullName" type="text" name="fullName" value="<c:out value='${fullName}'/>" placeholder="Nguyễn Văn An" maxlength="120" autocomplete="name" required autofocus>
                </div>
                <div class="form-group">
                    <label for="email">Địa chỉ email</label>
                    <input class="form-control" id="email" type="email" name="email" value="<c:out value='${email}'/>" placeholder="you@example.com" autocomplete="email" required>
                </div>
                <div class="form-grid">
                    <div class="form-group">
                        <label for="password">Mật khẩu</label>
                        <div class="password-wrap">
                            <input class="form-control" id="password" type="password" name="password" placeholder="Ít nhất 8 ký tự" autocomplete="new-password" required>
                            <button class="password-toggle" type="button" data-password-toggle="password">Hiện</button>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="confirmPassword">Xác nhận mật khẩu</label>
                        <div class="password-wrap">
                            <input class="form-control" id="confirmPassword" type="password" name="confirmPassword" placeholder="Nhập lại mật khẩu" autocomplete="new-password" required>
                            <button class="password-toggle" type="button" data-password-toggle="confirmPassword">Hiện</button>
                        </div>
                    </div>
                </div>
                <span class="help-text">Mật khẩu cần có ít nhất 8 ký tự, bao gồm chữ và số.</span><br>
                <button class="btn btn-primary btn-block" type="submit">Đăng ký &amp; nhận OTP <span>→</span></button>
            </form>
            <div class="auth-footer">Đã có tài khoản? <a href="${pageContext.request.contextPath}/login">Đăng nhập</a></div>
        </section>
    </div>
</main>
<%@ include file="../fragments/footer.jspf" %>
</body>
</html>
