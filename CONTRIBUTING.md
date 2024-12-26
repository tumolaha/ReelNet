# Hướng dẫn đóng góp

Cảm ơn bạn đã quan tâm đến việc đóng góp cho ReelNet! Dưới đây là một số hướng dẫn để giúp quá trình đóng góp của bạn hiệu quả hơn.

## Quy trình đóng góp

1. Fork dự án
2. Tạo branch mới (`git checkout -b feature/AmazingFeature`)
3. Commit các thay đổi (`git commit -m 'Add some AmazingFeature'`)
4. Push lên branch (`git push origin feature/AmazingFeature`)
5. Tạo Pull Request

## Tiêu chuẩn code

- Tuân thủ ESLint và Prettier đã được cấu hình trong dự án
- Viết test cho các tính năng mới
- Đảm bảo tất cả test đều pass
- Cập nhật documentation nếu cần thiết

## Quy ước commit

Chúng tôi sử dụng [Conventional Commits](https://www.conventionalcommits.org/):

- `feat:` Tính năng mới
- `fix:` Sửa lỗi
- `docs:` Thay đổi documentation
- `style:` Thay đổi không ảnh hưởng đến code (format, semicolons, etc)
- `refactor:` Refactor code
- `test:` Thêm hoặc sửa test
- `chore:` Thay đổi build process, công cụ, etc.

## Báo cáo lỗi

Khi báo cáo lỗi, vui lòng bao gồm:

- Mô tả ngắn gọn về lỗi
- Các bước để tái hiện lỗi
- Hành vi mong đợi
- Hành vi thực tế
- Screenshots (nếu có thể)
- Môi trường (browser, OS, etc.)

## Review code

- Mỗi pull request cần ít nhất một approval
- Reviewer sẽ kiểm tra:
  - Chất lượng code
  - Test coverage
  - Documentation
  - Conventional commits
  - Conflicts 