# Cirquo — Feature & Folder Plan

## 1. Quy ước kiến trúc

Cirquo dùng **feature-first** ở cấp cao nhất. Mỗi feature tự chứa các layer kỹ thuật của chính nó.

```text
feature/
├── controller/     REST endpoints
├── dto/
│   ├── request/
│   └── response/
├── entity/         JPA entities thuộc feature
├── enums/          enum nghiệp vụ
├── mapper/         MapStruct mappers
├── repository/     Spring Data repositories
├── service/
│   └── impl/
├── exception/      exception riêng của feature
└── client/         gọi dịch vụ ngoài, chỉ tạo khi cần
```

Quy tắc sử dụng:

- Không tạo một `entity/`, `service/`, `controller/` chung cho toàn project.
- Không tạo feature theo từng bảng; một feature có thể sở hữu nhiều bảng liên quan.
- Không tạo folder rỗng. Chỉ tạo folder con khi bắt đầu có file tương ứng.
- Một bảng nối chỉ có khóa ngoại không nhất thiết cần entity riêng. Có thể map bằng `@ManyToMany`/`@JoinTable`; chỉ tạo entity khi bảng nối có dữ liệu nghiệp vụ riêng.
- Flyway migration là nguồn sự thật của database schema; JPA annotation phải khớp migration.

## 2. Shared foundation

```text
com.huysg136.cirquo_server/
├── common/
│   ├── ApiResponse.java
│   └── BaseController.java
├── config/
│   └── SecurityConfig.java
├── exception/
│   ├── ErrorCode.java
│   └── GlobalExceptionHandler.java
└── annotation/
    ├── PhoneNumber.java
    └── PhoneNumberValidator.java
```

`common` không chứa nghiệp vụ User, Product, Order hoặc Payment.

## 3. User feature

Sở hữu tài khoản người dùng, hồ sơ, địa chỉ, role và permission.

Schema ownership:

```text
users
user_addresses
roles
permissions
role_permissions
user_permissions
```

Target structure:

```text
user/
├── controller/
│   ├── UserController.java
│   └── UserAddressController.java
├── dto/
│   ├── request/
│   │   ├── CreateUserRequest.java
│   │   ├── UpdateUserRequest.java
│   │   ├── CreateUserAddressRequest.java
│   │   └── UpdateUserAddressRequest.java
│   └── response/
│       ├── UserResponse.java
│       └── UserAddressResponse.java
├── entity/
│   ├── User.java
│   ├── UserAddress.java
│   ├── Role.java
│   └── Permission.java
├── enums/
│   ├── UserStatus.java
│   └── RoleName.java
├── mapper/
│   ├── UserMapper.java
│   └── UserAddressMapper.java
├── repository/
│   ├── UserRepository.java
│   ├── UserAddressRepository.java
│   ├── RoleRepository.java
│   └── PermissionRepository.java
├── service/
│   ├── UserService.java
│   ├── UserAddressService.java
│   └── impl/
│       ├── UserServiceImpl.java
│       └── UserAddressServiceImpl.java
└── exception/
    ├── UserNotFoundException.java
    └── EmailAlreadyExistsException.java
```

Mapping decision:

- `role_permissions`: map quan hệ nhiều-nhiều giữa `Role` và `Permission`; chưa cần `RolePermission.java` vì bảng không có field bổ sung.
- `user_permissions`: map permission bổ sung của `User`; chưa cần `UserPermission.java` vì bảng không có field bổ sung.
- `UserAddressController` chỉ được tạo khi bắt đầu API quản lý địa chỉ.

Current tasks:

- Đồng bộ `User` với schema: `role_id`, `password_hash`, độ dài và nullable.
- Chuẩn hóa response của toàn bộ UserController.
- Kiểm tra email trùng trước khi tạo/cập nhật.
- Hoàn thiện exception `USER_NOT_FOUND` và `EMAIL_ALREADY_EXISTS`.
- Sau CRUD User mới triển khai UserAddress.

## 4. Auth feature

Sở hữu đăng ký, đăng nhập, JWT/refresh token và kiểm tra credential. Auth dùng entity/repository thông qua User feature, không tạo `AuthUser`.

```text
auth/
├── controller/
│   └── AuthController.java
├── dto/
│   ├── request/
│   │   ├── RegisterRequest.java
│   │   ├── LoginRequest.java
│   │   └── RefreshTokenRequest.java
│   └── response/
│       └── AuthResponse.java
├── service/
│   ├── AuthService.java
│   ├── JwtService.java
│   └── impl/
└── exception/
```

Tasks:

- Seed role `CUSTOMER`, `STAFF`, `ADMIN` và permission bằng Flyway.
- Register luôn gán role `CUSTOMER`.
- Login chỉ thực hiện sau khi register và password hashing hoạt động đúng.

## 5. Catalog feature

Schema ownership:

```text
categories
products
product_variants
product_images
```

```text
catalog/
├── controller/
├── dto/
│   ├── request/
│   └── response/
├── entity/
│   ├── Category.java
│   ├── Product.java
│   ├── ProductVariant.java
│   └── ProductImage.java
├── enums/
│   └── CatalogStatus.java
├── mapper/
├── repository/
└── service/
    └── impl/
```

Quyết định MVP:

- Cirquo chỉ bán sản phẩm Apple, nên không có `brands` và `Product` không có `brand_id`.
- Tồn kho thuộc trực tiếp `ProductVariant` qua `stock_quantity`; không tạo `inventories` hoặc `stock_movements`.
- Giá cũng thuộc `ProductVariant`, không thuộc `Product`.
- Số lượng khả dụng chính là `stock_quantity`. Khi đơn được xác nhận/thanh toán, Order giảm trực tiếp giá trị này trong transaction.

Thứ tự triển khai Catalog:

```text
1. Category CRUD và seed nhóm sản phẩm.
2. Product CRUD: category, tên, slug, mô tả, specs, trạng thái.
3. ProductVariant CRUD: SKU, giá, thuộc tính, stockQuantity, trạng thái.
4. ProductImage CRUD: ảnh chung/ảnh variant, ảnh chính, thứ tự hiển thị.
5. Public API: danh sách phân trang, lọc category, tìm kiếm, chi tiết theo slug.
```

## 6. Shopping feature

Schema ownership:

```text
carts
cart_items
wishlists
```

```text
shopping/
├── controller/
│   ├── CartController.java
│   └── WishlistController.java
├── dto/
├── entity/
│   ├── Cart.java
│   ├── CartItem.java
│   └── Wishlist.java
├── mapper/
├── repository/
└── service/
    └── impl/
```

## 7. Promotion feature

Schema ownership:

```text
coupons
coupon_usages
```

```text
promotion/
├── controller/
├── dto/
├── entity/
│   ├── Coupon.java
│   └── CouponUsage.java
├── enums/
│   ├── DiscountType.java
│   └── CouponStatus.java
├── repository/
└── service/
    └── impl/
```

## 8. Order feature

Sở hữu checkout orchestration và snapshot của đơn hàng.

Schema ownership:

```text
orders
order_items
order_addresses
order_status_history
```

```text
order/
├── controller/
├── dto/
│   ├── request/
│   └── response/
├── entity/
│   ├── Order.java
│   ├── OrderItem.java
│   ├── OrderAddress.java
│   └── OrderStatusHistory.java
├── enums/
│   └── OrderStatus.java
├── mapper/
├── repository/
└── service/
    └── impl/
```

Checkout flow:

```text
Cart
→ Kiểm tra stock_quantity của từng variant
→ Coupon validation
→ Order + Item + Address snapshots
→ Payment creation
→ Shipment creation
```

Khi Order được xác nhận/thanh toán, giảm `product_variants.stock_quantity` trong cùng transaction. Không có bước reserve/release ở MVP.

## 9. Payment feature

Schema ownership:

```text
payments
refunds
```

```text
payment/
├── controller/
├── dto/
├── entity/
│   ├── Payment.java
│   └── Refund.java
├── enums/
│   ├── PaymentMethod.java
│   ├── PaymentStatus.java
│   └── RefundStatus.java
├── repository/
├── service/
│   └── impl/
└── client/
    ├── VnPayClient.java
    └── MomoClient.java
```

## 10. Shipping feature

Schema ownership: `shipments`.

```text
shipping/
├── controller/
├── dto/
├── entity/
│   └── Shipment.java
├── enums/
│   └── ShipmentStatus.java
├── repository/
├── service/
└── client/
```

## 11. Review feature

Schema ownership: `reviews`.

```text
review/
├── controller/
├── dto/
├── entity/
│   └── Review.java
├── enums/
│   └── ReviewStatus.java
├── repository/
└── service/
```

## 12. Loyalty feature

Schema ownership: `customer_loyalty`.

```text
loyalty/
├── controller/
├── dto/
├── entity/
│   └── CustomerLoyalty.java
├── enums/
│   └── MembershipTier.java
├── repository/
└── service/
```

## 13. Recommendation feature

Schema ownership:

```text
user_events
product_relations
```

```text
recommendation/
├── controller/
├── dto/
├── entity/
│   ├── UserEvent.java
│   └── ProductRelation.java
├── enums/
│   ├── UserEventType.java
│   └── ProductRelationType.java
├── repository/
├── service/
│   ├── EventTrackingService.java
│   ├── RecommendationService.java
│   └── impl/
└── client/
    └── RecommendationEngineClient.java
```

Recommendation data flow:

```text
API/user actions
→ user_events
→ worker/ETL
→ training dataset or feature store
→ ML model
→ C++ recommendation engine
→ RecommendationEngineClient
→ RecommendationController
```

Không để controller gọi C++ engine trực tiếp và không để C++ engine query toàn bộ `user_events` cho mỗi request.

## 14. Thứ tự triển khai

```text
1. Shared response + exception foundation
2. User core
3. Auth
4. User Address
5. Catalog
6. Shopping
7. Promotion
8. Order
9. Payment
10. Shipping
11. Review
12. Loyalty
13. Recommendation event tracking
14. ML/C++ recommendation integration
```

## 15. Bước tiếp theo hiện tại

Không tạo toàn bộ folder trên ngay bây giờ. User, Auth và User Address đã xong, nên bắt đầu Catalog theo phạm vi MVP:

1. Tạo Flyway migration cho `categories`, `products`, `product_variants`, `product_images` và index liên quan.
2. Tạo Category trước, seed các nhóm: iPhone, Mac, iPad, Watch, Tai nghe/loa, Phụ kiện.
3. Tạo Product CRUD cho ADMIN/STAFF và public API đọc Product đang ACTIVE.
4. Thêm ProductVariant với `stockQuantity`; không tạo feature Inventory.
5. Thêm ProductImage sau khi Variant ổn định.
