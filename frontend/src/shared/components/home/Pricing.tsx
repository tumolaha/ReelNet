import { Button, Card } from 'flowbite-react';
import { HiCheck } from 'react-icons/hi';

const plans = [
  {
    name: "Starter",
    price: "0",
    features: [
      "1 dự án cùng lúc",
      "2GB lưu trữ",
      "Tính năng AI cơ bản",
      "Xuất video HD"
    ]
  },
  {
    name: "Pro",
    price: "199.000",
    features: [
      "Không giới hạn dự án",
      "50GB lưu trữ",
      "Tất cả tính năng AI",
      "Xuất video 4K",
      "Hỗ trợ ưu tiên"
    ],
    popular: true
  },
  {
    name: "Enterprise",
    price: "Liên hệ",
    features: [
      "Giải pháp tùy chỉnh",
      "Lưu trữ không giới hạn",
      "API tích hợp",
      "Quản lý đội nhóm",
      "Hỗ trợ 24/7"
    ]
  }
];

export function Pricing() {
  return (
    <section className="py-24 bg-gray-50">
      <div className="container mx-auto px-6">
        <div className="text-center max-w-2xl mx-auto mb-16">
          <h2 className="text-3xl font-bold mb-4">
            Bảng Giá <span className="gradient-text">Dịch Vụ</span>
          </h2>
          <p className="text-gray-600">
            Lựa chọn gói dịch vụ phù hợp với nhu cầu của bạn
          </p>
        </div>
        <div className="grid md:grid-cols-3 gap-8">
          {plans.map((plan, index) => (
            <Card 
              key={index} 
              className={`card-hover ${plan.popular ? 'border-2 border-purple-500 relative' : ''}`}
            >
              {plan.popular && (
                <div className="absolute top-0 left-1/2 transform -translate-x-1/2 -translate-y-1/2">
                  <span className="bg-gradient-to-r from-purple-600 to-pink-500 text-white px-4 py-1 rounded-full text-sm font-medium">
                    Phổ biến nhất
                  </span>
                </div>
              )}
              <div className="text-center">
                <h3 className="text-2xl font-bold mb-2">{plan.name}</h3>
                <div className="mb-6">
                  <span className="text-4xl font-bold">
                    {plan.price === "Liên hệ" ? plan.price : `${plan.price}đ`}
                  </span>
                  {plan.price !== "Liên hệ" && <span className="text-gray-500">/tháng</span>}
                </div>
                <ul className="mb-8 space-y-4">
                  {plan.features.map((feature, idx) => (
                    <li key={idx} className="flex items-center justify-center text-gray-600">
                      <HiCheck className="w-5 h-5 text-green-500 mr-2" />
                      {feature}
                    </li>
                  ))}
                </ul>
                <Button 
                  gradientDuoTone={plan.popular ? "purpleToPink" : undefined}
                  color={plan.popular ? undefined : "gray"}
                  className={plan.popular ? "shadow-lg shadow-purple-500/30" : ""}
                  fullSized
                >
                  {plan.price === "0" ? "Dùng Miễn Phí" : "Đăng Ký Ngay"}
                </Button>
              </div>
            </Card>
          ))}
        </div>
      </div>
    </section>
  );
}