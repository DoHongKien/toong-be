-- ==========================================================
-- FILE: 01_schema.sql
-- Tạo toàn bộ cấu trúc bảng cho dự án Toong Adventure
-- ==========================================================

CREATE DATABASE IF NOT EXISTS toong_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE toong_db;

-- =============================================
-- CORE: TOURS, DEPARTURES, BOOKINGS
-- =============================================

-- 1. Bảng TOURS
CREATE TABLE IF NOT EXISTS tours (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    slug VARCHAR(255) UNIQUE NOT NULL,
    hero_image VARCHAR(500),
    card_image VARCHAR(500),
    badge VARCHAR(50),
    region ENUM('nam', 'trung', 'taynguyen') NOT NULL,
    duration_days INT DEFAULT 0,
    duration_nights INT DEFAULT 0,
    difficulty VARCHAR(50),
    distance_km INT,
    min_age INT,
    max_age INT,
    summary TEXT,
    description TEXT,
    base_price DECIMAL(10,2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2. Bảng DEPARTURES (Lịch khởi hành)
CREATE TABLE IF NOT EXISTS departures (
    id INT AUTO_INCREMENT PRIMARY KEY,
    tour_id INT,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    deposit_deadline DATE,
    payment_deadline DATE,
    price DECIMAL(10,2) NOT NULL,
    total_slots INT NOT NULL,
    booked_slots INT DEFAULT 0,
    status ENUM('active', 'full', 'completed', 'cancelled') DEFAULT 'active',
    FOREIGN KEY (tour_id) REFERENCES tours(id) ON DELETE CASCADE
);

-- 3. Bảng BOOKINGS (Đặt chỗ)
CREATE TABLE IF NOT EXISTS bookings (
    id INT AUTO_INCREMENT PRIMARY KEY,
    booking_code VARCHAR(50) UNIQUE NOT NULL,
    departure_id INT,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    email VARCHAR(100),
    quantity INT NOT NULL,
    total_amount DECIMAL(10,2) NOT NULL,
    deposit_amount DECIMAL(10,2) NOT NULL,
    remaining_amount DECIMAL(10,2) NOT NULL,
    payment_method ENUM('vnpay', 'bank_transfer') NOT NULL,
    status ENUM('pending', 'deposited', 'fully_paid', 'cancelled') DEFAULT 'pending',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (departure_id) REFERENCES departures(id)
);

-- 4. Bảng ITINERARIES (Lịch trình theo ngày)
CREATE TABLE IF NOT EXISTS itineraries (
    id INT AUTO_INCREMENT PRIMARY KEY,
    tour_id INT,
    day_number INT NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    FOREIGN KEY (tour_id) REFERENCES tours(id) ON DELETE CASCADE
);

-- 5. Bảng ITINERARY_TIMELINES (Hoạt động theo giờ)
CREATE TABLE IF NOT EXISTS itinerary_timelines (
    id INT AUTO_INCREMENT PRIMARY KEY,
    itinerary_id INT,
    execution_time TIME,
    activity TEXT NOT NULL,
    FOREIGN KEY (itinerary_id) REFERENCES itineraries(id) ON DELETE CASCADE
);

-- 6. Bảng TOUR_COST_DETAILS (Chi phí bao gồm/không bao gồm)
CREATE TABLE IF NOT EXISTS tour_cost_details (
    id INT AUTO_INCREMENT PRIMARY KEY,
    tour_id INT,
    is_included BOOLEAN NOT NULL,
    content TEXT NOT NULL,
    FOREIGN KEY (tour_id) REFERENCES tours(id) ON DELETE CASCADE
);

-- 7. Bảng TOUR_LUGGAGES (Hành lý chuẩn bị)
CREATE TABLE IF NOT EXISTS tour_luggages (
    id INT AUTO_INCREMENT PRIMARY KEY,
    tour_id INT,
    name VARCHAR(255) NOT NULL,
    detail TEXT,
    FOREIGN KEY (tour_id) REFERENCES tours(id) ON DELETE CASCADE
);

-- 8. Bảng TOUR_FAQS (Câu hỏi thường gặp của tour)
CREATE TABLE IF NOT EXISTS tour_faqs (
    id INT AUTO_INCREMENT PRIMARY KEY,
    tour_id INT,
    question TEXT NOT NULL,
    answer TEXT NOT NULL,
    FOREIGN KEY (tour_id) REFERENCES tours(id) ON DELETE CASCADE
);

-- =============================================
-- ADVENTURE PASS
-- =============================================

-- 9. Bảng ADVENTURE_PASSES (Gói thẻ Pass)
CREATE TABLE IF NOT EXISTS adventure_passes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    subtitle VARCHAR(100),
    price DECIMAL(10,2) NOT NULL,
    validity_date DATE,
    is_signature BOOLEAN DEFAULT FALSE,
    color_theme VARCHAR(50)
);

-- 10. Bảng PASS_FEATURES (Quyền lợi theo thẻ pass)
CREATE TABLE IF NOT EXISTS pass_features (
    id INT AUTO_INCREMENT PRIMARY KEY,
    pass_id INT,
    content TEXT NOT NULL,
    is_bold BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (pass_id) REFERENCES adventure_passes(id) ON DELETE CASCADE
);

-- 11. Bảng PASS_ORDERS (Giao dịch mua pass)
CREATE TABLE IF NOT EXISTS pass_orders (
    id INT AUTO_INCREMENT PRIMARY KEY,
    order_code VARCHAR(50) UNIQUE NOT NULL,
    pass_id INT,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    email VARCHAR(100),
    payment_method ENUM('vnpay', 'bank_transfer') NOT NULL,
    status ENUM('pending', 'paid', 'cancelled') DEFAULT 'pending',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (pass_id) REFERENCES adventure_passes(id)
);

-- =============================================
-- RBAC: ROLES, PERMISSIONS, EMPLOYEES
-- =============================================

-- 12. Bảng ROLES (Vai trò nhân viên)
CREATE TABLE IF NOT EXISTS roles (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    code VARCHAR(50) UNIQUE NOT NULL
);

-- 13. Bảng PERMISSIONS (Quyền hạn)
CREATE TABLE IF NOT EXISTS permissions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    code VARCHAR(50) UNIQUE NOT NULL,
    module VARCHAR(50) NOT NULL
);

-- 14. Bảng ROLE_PERMISSIONS (Gán quyền cho vai trò)
CREATE TABLE IF NOT EXISTS role_permissions (
    role_id INT,
    permission_id INT,
    PRIMARY KEY (role_id, permission_id),
    FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE,
    FOREIGN KEY (permission_id) REFERENCES permissions(id) ON DELETE CASCADE
);

-- 15. Bảng EMPLOYEES (Nhân viên)
CREATE TABLE IF NOT EXISTS employees (
    id INT AUTO_INCREMENT PRIMARY KEY,
    role_id INT,
    username VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    status ENUM('active', 'locked') DEFAULT 'active',
    last_login TIMESTAMP NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (role_id) REFERENCES roles(id)
);

-- =============================================
-- CMS: BANNERS, BLOG_POSTS, FAQS, CONTACTS
-- =============================================

-- 16. Bảng BANNERS
CREATE TABLE IF NOT EXISTS banners (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255),
    image_url VARCHAR(500) NOT NULL,
    link_url VARCHAR(500),
    sort_order INT DEFAULT 0,
    is_active BOOLEAN DEFAULT TRUE
);

-- 17. Bảng BLOG_POSTS
CREATE TABLE IF NOT EXISTS blog_posts (
    id INT AUTO_INCREMENT PRIMARY KEY,
    author_id INT,
    title VARCHAR(255) NOT NULL,
    slug VARCHAR(255) UNIQUE NOT NULL,
    content TEXT NOT NULL,
    thumbnail VARCHAR(500),
    status ENUM('draft', 'published') DEFAULT 'draft',
    published_at DATETIME,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (author_id) REFERENCES employees(id)
);

-- 18. Bảng GENERAL_FAQS (FAQ chung của công ty)
CREATE TABLE IF NOT EXISTS general_faqs (
    id INT AUTO_INCREMENT PRIMARY KEY,
    question TEXT NOT NULL,
    answer TEXT NOT NULL,
    sort_order INT DEFAULT 0
);

-- 19. Bảng CONTACT_MESSAGES (Tin nhắn liên hệ)
CREATE TABLE IF NOT EXISTS contact_messages (
    id INT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    email VARCHAR(100),
    message TEXT NOT NULL,
    status ENUM('new', 'contacted', 'resolved') DEFAULT 'new',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
