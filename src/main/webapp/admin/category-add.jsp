<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!doctype html>
<html lang="vi">
<head>
    <meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Thêm danh mục — Nova Store</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body>
<%@ include file="../WEB-INF/views/fragments/header.jspf" %>
<main class="admin-page">
    <div class="container">
        <div class="admin-topbar"><div><h1>Thêm danh mục</h1><p>Tạo một nhóm mới để phân loại sản phẩm.</p></div></div>
        <nav class="admin-nav"><a href="${pageContext.request.contextPath}/admin/products">Sản phẩm</a><a class="active" href="${pageContext.request.contextPath}/admin/categories">Danh mục</a></nav>
        <div class="form-card">
            <div class="form-card-header"><h2>Thông tin danh mục</h2><p>Ảnh tải lên sẽ được lưu trực tiếp trên Cloudinary.</p></div>
            <c:if test="${not empty error}"><div class="alert alert-error"><span>!</span><c:out value="${error}"/></div><br></c:if>
            <form action="${pageContext.request.contextPath}/admin/category/insert" method="post" enctype="multipart/form-data">
                <div class="admin-form-layout">
                    <div>
                        <div class="form-group"><label for="categoryname">Tên danh mục *</label><input class="form-control" id="categoryname" type="text" name="categoryname" value="<c:out value='${not empty formCategory ? formCategory.categoryname : param.categoryname}'/>" maxlength="255" required autofocus></div>
                        <div class="form-group"><label for="imageUrl">Đường dẫn ảnh dự phòng</label><input class="form-control" id="imageUrl" type="url" name="imageUrl" value="<c:out value='${not empty formCategory ? formCategory.images : param.imageUrl}'/>" placeholder="https://..."><span class="help-text">Nếu chọn tệp bên phải, ảnh Cloudinary sẽ được ưu tiên.</span></div>
                        <div class="form-group"><label for="status">Trạng thái</label><select class="form-control" id="status" name="status"><option value="1" selected>Hoạt động</option><option value="0">Đang khóa</option></select></div>
                    </div>
                    <aside>
                        <div class="form-group"><label>Ảnh danh mục</label><div class="image-preview"><img id="categoryPreview" hidden alt="Xem trước ảnh"><div class="image-placeholder">N</div></div></div>
                        <div class="upload-box"><input id="imageFile" type="file" name="imageFile" accept="image/*" data-image-input="#categoryPreview"><span class="help-text">JPG, PNG, WEBP — tối đa 5 MB.</span></div>
                    </aside>
                </div>
                <div class="form-actions"><a class="btn btn-secondary" href="${pageContext.request.contextPath}/admin/categories">Hủy</a><button class="btn btn-primary" type="submit">Lưu danh mục</button></div>
            </form>
        </div>
    </div>
</main>
<%@ include file="../WEB-INF/views/fragments/footer.jspf" %>
</body>
</html>
