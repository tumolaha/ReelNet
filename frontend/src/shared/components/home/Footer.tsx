export function Footer() {
  return (
    <footer className="bg-gray-900 text-white py-12">
      <div className="container mx-auto px-6">
        <div className="grid md:grid-cols-4 gap-8">
          <div>
            <h4 className="text-xl font-bold mb-4">CollabVideo</h4>
            <p className="text-gray-400">
              Nền tảng biên tập video cộng tác thời gian thực hàng đầu Việt Nam
            </p>
          </div>
          <div>
            <h4 className="text-xl font-bold mb-4">Sản Phẩm</h4>
            <ul className="space-y-2 text-gray-400">
              <li>Tính Năng</li>
              <li>Bảng Giá</li>
              <li>Hướng Dẫn</li>
              <li>Blog</li>
            </ul>
          </div>
          <div>
            <h4 className="text-xl font-bold mb-4">Công Ty</h4>
            <ul className="space-y-2 text-gray-400">
              <li>Về Chúng Tôi</li>
              <li>Tuyển Dụng</li>
              <li>Liên Hệ</li>
              <li>Đối Tác</li>
            </ul>
          </div>
          <div>
            <h4 className="text-xl font-bold mb-4">Theo Dõi</h4>
            <ul className="space-y-2 text-gray-400">
              <li>Facebook</li>
              <li>Twitter</li>
              <li>Instagram</li>
              <li>LinkedIn</li>
            </ul>
          </div>
        </div>
        <div className="border-t border-gray-800 mt-8 pt-8 text-center text-gray-400">
          <p>© 2024 CollabVideo. Tất cả quyền được bảo lưu.</p>
        </div>
      </div>
    </footer>
  );
}