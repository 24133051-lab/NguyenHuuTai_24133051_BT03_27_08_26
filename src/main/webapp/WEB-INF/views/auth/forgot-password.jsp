<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!doctype html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quên mật khẩu — Nova Store</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body>
<%@ include file="../fragments/header.jspf" %>
<main class="auth-page">
    <div class="auth-shell">
        <aside class="auth-aside">
            <a class="brand" href="${pageContext.request.contextPath}/"><span class="brand-mark">N</span><span>NOVA</span></a>
            <div class="auth-aside-copy">
                <h2>Đừng lo, Nova sẽ giúp.</h2>
                <p>Một mã xác nhận an toàn sẽ được gửi tới email đã đăng ký của bạn.</p>
                <div class="auth-points">
                    <div class="auth-point"><span>1</span>Nhập email tài khoản</div>
                    <div class="auth-point"><span>2</span>Nhận OTP trong hộp thư</div>
                    <div class="auth-point"><span>3</span>Tạo mật khẩu mới</div>
                </div>
            </div>
        </aside>
        <section class="auth-form-panel">
            <div class="auth-heading">
                <span class="eyebrow">Khôi phục tài khoản</span>
                <h1>Quên mật khẩu?</h1>
                <p>Nhập email đã đăng ký để nhận mã OTP đặt lại mật khẩu.</p>
            </div>
            <c:if test="${not empty error}"><div class="alert alert-error"><span>!</span><c:out value="${error}"/></div><br></c:if>

            <form action="${pageContext.request.contextPath}/forgot-password" method="post">
                <div class="form-group">
                    <label for="email">Địa chỉ email</label>
                    <input class="form-control" id="email" type="email" name="email" value="<c:out value='${email}'/>" placeholder="you@example.com" autocomplete="email" required autofocus>
                </div>
                <button class="btn btn-primary btn-block" type="submit">Gửi mã OTP <span>→</span></button>
            </form>
            <div class="auth-footer">Đã nhớ mật khẩu? <a href="${pageContext.request.contextPath}/login">Đăng nhập</a></div>
        </section>
    </div>
</main>
<%@ include file="../fragments/footer.jspf" %>
</body>
</html>
