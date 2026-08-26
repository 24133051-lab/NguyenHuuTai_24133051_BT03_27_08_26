<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!doctype html>
<html lang="vi">
<head>
    <meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Thêm sản phẩm — Nova Store</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body>
<%@ include file="../WEB-INF/views/fragments/header.jspf" %>
<main class="admin-page">
    <div class="container">
        <div class="admin-topbar"><div><h1>Thêm sản phẩm</h1><p>Tạo sản phẩm mới và đưa lên cửa hàng.</p></div></div>
        <nav class="admin-nav"><a class="active" href="${pageContext.request.contextPath}/admin/products">Sản phẩm</a><a href="${pageContext.request.contextPath}/admin/categories">Danh mục</a></nav>
        <div class="form-card">
            <div class="form-card-header"><h2>Thông tin sản phẩm</h2><p>Các trường có dấu * là bắt buộc. Ảnh sẽ được lưu trên Cloudinary.</p></div>
            <c:if test="${not empty error}"><div class="alert alert-error"><span>!</span><c:out value="${error}"/></div><br></c:if>
            <c:if test="${empty categories}"><div class="alert alert-error"><span>!</span>Bạn cần <a href="${pageContext.request.contextPath}/admin/category/add">tạo danh mục</a> trước khi thêm sản phẩm.</div><br></c:if>
            <form action="${pageContext.request.contextPath}/admin/product/insert" method="post" enctype="multipart/form-data">
                <div class="admin-form-layout">
                    <div>
                        <div class="form-group"><label for="name">Tên sản phẩm *</label><input class="form-control" id="name" type="text" name="name" value="<c:out value='${product.name}'/>" maxlength="255" required autofocus></div>
                        <div class="form-group"><label for="description">Mô tả</label><textarea class="form-control" id="description" name="description" placeholder="Mô tả nổi bật, thông tin và đặc điểm sản phẩm..."><c:out value="${product.description}"/></textarea></div>
                        <div class="form-grid">
                            <div class="form-group"><label for="price">Giá bán (VNĐ) *</label><input class="form-control" id="price" type="number" name="price" value="<c:out value='${product.price}'/>" min="0" max="1000000000" step="1000" required><span class="help-text">Giá tối đa 1.000.000.000 ₫.</span></div>
                            <div class="form-group"><label for="quantity">Số lượng *</label><input class="form-control" id="quantity" type="number" name="quantity" value="${empty product.quantity ? 0 : product.quantity}" min="0" required></div>
                            <div class="form-group"><label for="categoryId">Danh mục *</label><select class="form-control" id="categoryId" name="categoryId" required><option value="">-- Chọn danh mục --</option><c:forEach items="${categories}" var="category"><option value="${category.categoryid}" ${not empty product.category and product.category.categoryid == category.categoryid ? 'selected' : ''}><c:out value="${category.categoryname}"/></option></c:forEach></select></div>
                            <div class="form-group"><label for="active">Trạng thái</label><select class="form-control" id="active" name="active"><option value="1" ${empty product or product.active ? 'selected' : ''}>Đang bán</option><option value="0" ${not empty product and not product.active ? 'selected' : ''}>Tạm ẩn</option></select></div>
                        </div>
                        <div class="form-group"><label for="imageUrl">Đường dẫn ảnh dự phòng</label><input class="form-control" id="imageUrl" type="url" name="imageUrl" value="<c:out value='${product.imageUrl}'/>" placeholder="https://..."><span class="help-text">Nếu chọn tệp bên phải, ảnh Cloudinary sẽ được ưu tiên.</span></div>
                    </div>
                    <aside>
                        <div class="form-group"><label>Ảnh sản phẩm</label><div class="image-preview"><c:choose><c:when test="${not empty product.imageUrl}"><img id="productPreview" src="<c:out value='${product.imageUrl}'/>" alt="Xem trước"></c:when><c:otherwise><img id="productPreview" hidden alt="Xem trước"><div class="image-placeholder">N</div></c:otherwise></c:choose></div></div>
                        <div class="upload-box"><input type="file" name="imageFile" accept="image/*" data-image-input="#productPreview"><span class="help-text">JPG, PNG, WEBP — tối đa 5 MB.</span></div>
                    </aside>
                </div>
                <div class="form-actions"><a class="btn btn-secondary" href="${pageContext.request.contextPath}/admin/products">Hủy</a><button class="btn btn-primary" type="submit" ${empty categories ? 'disabled' : ''}>Tạo sản phẩm</button></div>
            </form>
        </div>
    </div>
</main>
<%@ include file="../WEB-INF/views/fragments/footer.jspf" %>
</body>
</html>
