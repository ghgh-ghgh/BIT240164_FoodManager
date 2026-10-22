# Kịch bản thuyết minh mã nguồn Video 2 – Môn Phát triển ứng dụng di động
**Đề tài:** Quản lý món ăn | **MSSV:** BIT240164 | **Tên Project:** BIT240164_FoodManager

---

## 1. Giới thiệu tổng quan cấu trúc Project
- **Kiến trúc:** MVVM (Model - View - ViewModel) kết hợp Jetpack Compose và Jetpack Navigation.
- **Cấu trúc thư mục (`com.example.bit240164_foodmanager`):**
  - `data/model`: Các data class (`Dish`, `DishCategory`, `DishImage`).
  - `data/remote`: Giao tiếp API (`ApiService`, `RetrofitClient`).
  - `data/utils`: Dữ liệu mẫu khởi tạo (`SeedData`).
  - `data/repository`: Tầng Repository (`FoodRepository`) xử lý logic nghiệp vụ và kết nối MockAPI.
  - `ui/list`: Màn hình danh sách, tìm kiếm, lọc, sắp xếp (`DishListScreen`, `DishListViewModel`).
  - `ui/detail`: Màn hình chi tiết món ăn (`DishDetailScreen`).
  - `ui/form`: Màn hình thêm và chỉnh sửa món ăn (`DishFormScreen`).
  - `ui/navigation`: Điều hướng ứng dụng (`AppNavigation`).
  - `utils`: Theo dõi mạng (`NetworkMonitor`) và quản lý đa ngôn ngữ (`LanguageManager`).

---

## 2. Thuyết minh tiền tố MSSV (`BIT240164_`) theo yêu cầu
- **Tên Project:** `BIT240164_FoodManager` (thể hiện rõ MSSV trong định danh ứng dụng).
- **Tên Endpoint / Tài nguyên trên MockAPI:** Sử dụng tiền tố `BIT240164_` cho các bảng/endpoint:
  - `BIT240164_DishCategory`
  - `BIT240164_Dish`
- **Tên các trường dữ liệu (Fields/Columns):** Tất cả các thuộc tính quan trọng đều gắn tiền tố `BIT240164_` để tuân thủ tuyệt đối quy định đặt tên:
  - `BIT240164_Name`, `BIT240164_Price`, `BIT240164_PreparationTime`, `BIT240164_IsAvailable`, `BIT240164_Description`, `BIT240164_DishCategoryId`, `BIT240164_DishImage`.

---

## 3. Mô hình dữ liệu & Quan hệ (Data Models & Relationships)
- **`DishCategory` ([DishCategory.kt](file:///C:/Users/Admin/AndroidStudioProjects/BIT240164_FoodManager/app/src/main/java/com/example/bit240164_foodmanager/data/model/DishCategory.kt)):**
  - Quản lý loại món ăn với `id`, `BIT240164_Name`, `BIT240164_Description`.
- **`DishImage` ([DishImage.kt](file:///C:/Users/Admin/AndroidStudioProjects/BIT240164_FoodManager/app/src/main/java/com/example/bit240164_foodmanager/data/model/DishImage.kt)):**
  - Quản lý danh sách ảnh của món ăn gồm `ImageUrl` và `IsThumbnail` (ảnh đại diện).
- **`Dish` ([Dish.kt](file:///C:/Users/Admin/AndroidStudioProjects/BIT240164_FoodManager/app/src/main/java/com/example/bit240164_foodmanager/data/model/Dish.kt)):**
  - Liên kết 1-nhiều với `DishCategory` thông qua trường `BIT240164_DishCategoryId`.
  - Chứa danh sách ảnh `BIT240164_DishImage` (mỗi món có tối đa 01 ảnh thumbnail `IsThumbnail = true`).

---

## 4. Công nghệ & Luồng dữ liệu (Technology & Data Flow)
- **Công nghệ lưu trữ & Giao tiếp:** Sử dụng **MockAPI** qua **Retrofit 2** (`RetrofitClient` + `ApiService`) để đồng bộ dữ liệu RESTful.
- **Tầng Repository ([FoodRepository.kt](file:///C:/Users/Admin/AndroidStudioProjects/BIT240164_FoodManager/app/src/main/java/com/example/bit240164_foodmanager/data/repository/FoodRepository.kt)):**
  - Cung cấp các coroutine suspend function (`getAllCategories`, `getAllDishes`, `createDish`, `updateDish`, `deleteDish`).
  - Xử lý linh hoạt việc bắt lỗi mạng và tự động fallback về `SeedData` khi MockAPI trống hoặc mất kết nối.
- **Quản lý trạng thái ([DishListViewModel.kt](file:///C:/Users/Admin/AndroidStudioProjects/BIT240164_FoodManager/app/src/main/java/com/example/bit240164_foodmanager/ui/list/DishListViewModel.kt)):**
  - Sử dụng `StateFlow` để reactive UI.
  - Xử lý logic tìm kiếm chuỗi con, lọc đa điều kiện (theo loại, trạng thái đang bán, khoảng giá min/max) và sắp xếp (theo giá tăng/giảm, thời gian chế biến).

---

## 5. Xử lý nghiệp vụ, Validation & Đa ngôn ngữ (i18n)
- **Validation trong Form ([DishFormScreen.kt](file:///C:/Users/Admin/AndroidStudioProjects/BIT240164_FoodManager/app/src/main/java/com/example/bit240164_foodmanager/ui/form/DishFormScreen.kt)):**
  - Kiểm tra tên không rỗng, giá `> 0`, thời gian chế biến `> 0`, bắt buộc chọn loại món.
  - **Ràng buộc nghiệp vụ:** Kiểm tra tên món không bị trùng lặp trong cùng một loại món ăn.
- **Đa ngôn ngữ ([LanguageManager.kt](file:///C:/Users/Admin/AndroidStudioProjects/BIT240164_FoodManager/app/src/main/java/com/example/bit240164_foodmanager/utils/LanguageManager.kt)):**
  - Hỗ trợ chuyển đổi ngôn ngữ **English ↔ Tiếng Việt** trực tiếp trên giao diện thông qua nút `EN | VI` ở TopAppBar.
  - Lưu cấu hình ngôn ngữ vào `SharedPreferences` và tự động cập nhật resource strings cho toàn bộ ứng dụng.
