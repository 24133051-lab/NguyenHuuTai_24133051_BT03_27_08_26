<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!doctype html>
<html lang="vi">
<head>
    <meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Sửa danh mục — Nova Store</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body>
<%@ include file="../WEB-INF/views/fragments/header.jspf" %>
<main class="admin-page">
    <div class="container">
        <div class="admin-topbar"><div><h1>Sửa danh mục</h1><p>Cập nhật nội dung và hình ảnh của danh mục #${cate.categoryid}.</p></div></div>
        <nav class="admin-nav"><a href="${pageContext.request.contextPath}/admin/products">Sản phẩm</a><a class="active" href="${pageContext.request.contextPath}/admin/categories">Danh mục</a></nav>
        <div class="form-card">
            <div class="form-card-header"><h2>Thông tin danh mục</h2><p>Chọn ảnh mới nếu muốn thay ảnh hiện tại trên Cloudinary.</p></div>
            <c:if test="${not empty error}"><div class="alert alert-error"><span>!</span><c:out value="${error}"/></div><br></c:if>
            <form action="${pageContext.request.contextPath}/admin/category/update" method="post" enctype="multipart/form-data">
                <input type="hidden" name="categoryid" value="${cate.categoryid}">
                <div class="admin-form-layout">
                    <div>
                        <div class="form-group"><label for="categoryname">Tên danh mục *</label><input class="form-control" id="categoryname" type="text" name="categoryname" value="<c:out value='${cate.categoryname}'/>" maxlength="255" required autofocus></div>
                        <div class="form-group"><label for="imageUrl">Đường dẫn ảnh</label><input class="form-control" id="imageUrl" type="url" name="imageUrl" value="<c:out value='${cate.images}'/>" placeholder="https://..."><span class="help-text">Để nguyên nếu muốn giữ ảnh hiện tại.</span></div>
                        <div class="form-group"><label for="status">Trạng thái</label><select class="form-control" id="status" name="status"><option value="1" ${cate.status == 1 ? 'selected' : ''}>Hoạt động</option><option value="0" ${cate.status == 0 ? 'selected' : ''}>Đang khóa</option></select></div>
                    </div>
                    <aside>
                        <div class="form-group"><label>Ảnh danh mục</label><div class="image-preview">
                            <c:choose>
                                <c:when test="${not empty cate.images and fn:startsWith(cate.images, 'http')}"><img id="categoryPreview" src="<c:out value='${cate.images}'/>" alt="Ảnh danh mục"></c:when>
                                <c:when test="${not empty cate.images}"><c:url var="legacyImageUrl" value="/image"><c:param name="fname" value="${cate.images}"/></c:url><img id="categoryPreview" src="${legacyImageUrl}" alt="Ảnh danh mục"></c:when>
                                <c:otherwise><img id="categoryPreview" hidden alt="Xem trước"><div class="image-placeholder">N</div></c:otherwise>
                            </c:choose>
                        </div></div>
                        <div class="upload-box"><input type="file" name="imageFile" accept="image/*" data-image-input="#categoryPreview"><span class="help-text">JPG, PNG, WEBP — tối đa 5 MB.</span></div>
                    </aside>
                </div>
                <div class="form-actions"><a class="btn btn-secondary" href="${pageContext.request.contextPath}/admin/categories">Hủy</a><button class="btn btn-primary" type="submit">Lưu thay đổi</button></div>
            </form>
        </div>
    </div>
</main>
<%@ include file="../WEB-INF/views/fragments/footer.jspf" %>
</body>
</html>
