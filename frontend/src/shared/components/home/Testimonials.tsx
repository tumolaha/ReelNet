import { Card } from 'flowbite-react';
import { HiStar } from 'react-icons/hi';

const testimonials = [
  {
    name: "Nguyễn Văn A",
    role: "Video Creator",
    content: "CollabVideo đã thay đổi hoàn toàn cách làm việc của team chúng tôi. Giờ đây việc biên tập video trở nên dễ dàng và hiệu quả hơn rất nhiều.",
    avatar: "https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?auto=format&fit=crop&q=80&w=200",
    rating: 5,
    company: "Creative Studio",
    background: "https://images.unsplash.com/photo-1622737133809-d95047b9e673?auto=format&fit=crop&q=80&w=500"
  },
  {
    name: "Trần Thị B",
    role: "Content Manager",
    content: "Tính năng AI của CollabVideo giúp tiết kiệm rất nhiều thời gian. Việc tạo phụ đề tự động là một điểm cộng tuyệt vời.",
    avatar: "https://images.unsplash.com/photo-1438761681033-6461ffad8d80?auto=format&fit=crop&q=80&w=200",
    rating: 5,
    company: "Digital Agency",
    background: "https://images.unsplash.com/photo-1522071820081-009f0129c71c?auto=format&fit=crop&q=80&w=500"
  },
  {
    name: "Lê Văn C",
    role: "Youtuber",
    content: "Khả năng cộng tác thời gian thực giúp tôi làm việc hiệu quả với editor của mình, dù ở cách xa nhau.",
    avatar: "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?auto=format&fit=crop&q=80&w=200",
    rating: 5,
    company: "Tech Channel",
    background: "https://images.unsplash.com/photo-1515378960530-7c0da6231fb1?auto=format&fit=crop&q=80&w=500"
  }
];

export function Testimonials() {
  return (
    <section className="py-24 bg-white">
      <div className="container mx-auto px-6">
        <div className="text-center max-w-2xl mx-auto mb-16">
          <h2 className="text-3xl font-bold mb-4">
            Khách Hàng <span className="gradient-text">Nói Gì</span> Về Chúng Tôi
          </h2>
          <p className="text-gray-600">
            Khám phá trải nghiệm từ cộng đồng người dùng CollabVideo
          </p>
        </div>
        <div className="grid md:grid-cols-3 gap-8">
          {testimonials.map((testimonial, index) => (
            <Card key={index} className="card-hover border-0 overflow-hidden">
              <div className="relative h-48 mb-16">
                <img 
                  src={testimonial.background}
                  alt="Background"
                  className="w-full h-full object-cover"
                />
                <div className="absolute -bottom-12 left-1/2 -translate-x-1/2">
                  <div className="relative">
                    <div className="absolute inset-0 bg-gradient-to-r from-purple-600 to-pink-500 rounded-full blur-lg opacity-30"></div>
                    <img 
                      src={testimonial.avatar} 
                      alt={testimonial.name}
                      className="relative w-24 h-24 rounded-full border-4 border-white shadow-lg object-cover"
                    />
                  </div>
                </div>
              </div>
              <div className="text-center px-6">
                <div className="flex gap-1 justify-center mb-4">
                  {[...Array(testimonial.rating)].map((_, i) => (
                    <HiStar key={i} className="text-yellow-400 w-5 h-5" />
                  ))}
                </div>
                <p className="mb-4 text-gray-600 italic">"{testimonial.content}"</p>
                <h4 className="font-semibold text-lg">{testimonial.name}</h4>
                <p className="text-sm text-gray-500 mb-1">{testimonial.role}</p>
                <p className="text-sm font-medium text-purple-600">{testimonial.company}</p>
              </div>
            </Card>
          ))}
        </div>
      </div>
    </section>
  );
}