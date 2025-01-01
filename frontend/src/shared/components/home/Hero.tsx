import { Button } from "flowbite-react";
import { HiPlay, HiSparkles } from "react-icons/hi";
import { useAuth } from "@/core/auth/hooks/useAuth";

export function Hero() {
  const { login } = useAuth();
  return (
    <div className="relative overflow-hidden bg-gradient-to-br from-purple-900 via-purple-800 to-indigo-900 text-white py-24">
      {/* Decorative circles */}
      <div className="absolute top-0 left-0 w-72 h-72 bg-purple-500 rounded-full mix-blend-multiply filter blur-xl opacity-70 animate-blob"></div>
      <div className="absolute top-0 right-0 w-72 h-72 bg-yellow-500 rounded-full mix-blend-multiply filter blur-xl opacity-70 animate-blob animation-delay-2000"></div>
      <div className="absolute -bottom-8 left-20 w-72 h-72 bg-pink-500 rounded-full mix-blend-multiply filter blur-xl opacity-70 animate-blob animation-delay-4000"></div>

      <div className="container mx-auto px-6 relative">
        <div className="flex flex-col lg:flex-row items-center gap-12">
          <div className="lg:w-1/2">
            <div className="inline-block px-3 py-1 mb-6 rounded-full bg-white/10 backdrop-blur-sm border border-white/20">
              <span className="flex items-center text-sm">
                <HiSparkles className="mr-2 text-yellow-400" />
                Công nghệ AI mới nhất
              </span>
            </div>
            <h1 className="text-4xl lg:text-6xl font-bold mb-6 leading-tight">
              Biên Tập Video <span className="gradient-text">Cộng Tác</span>{" "}
              Thời Gian Thực
            </h1>
            <p className="text-xl mb-8 text-gray-300 leading-relaxed">
              Tạo video chuyên nghiệp cùng nhau. Được hỗ trợ bởi AI, dễ dàng
              chia sẻ và cộng tác trong thời gian thực.
            </p>
            <div className="flex gap-4 flex-wrap">
              <Button
                size="xl"
                gradientDuoTone="purpleToPink"
                className="shadow-lg shadow-purple-500/30"
                onClick={login}
              >
                Dùng thử miễn phí
              </Button>
              <Button size="xl" color="gray" className="glass-effect" outline>
                <HiPlay className="mr-2 h-6 w-6" /> Xem Demo
              </Button>
            </div>
            <div className="mt-8 flex items-center gap-4 text-sm text-gray-300">
              <div className="flex -space-x-2">
                <img
                  className="w-8 h-8 rounded-full border-2 border-white"
                  src="https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?auto=format&fit=crop&q=80&w=100"
                  alt="User"
                />
                <img
                  className="w-8 h-8 rounded-full border-2 border-white"
                  src="https://images.unsplash.com/photo-1494790108377-be9c29b29330?auto=format&fit=crop&q=80&w=100"
                  alt="User"
                />
                <img
                  className="w-8 h-8 rounded-full border-2 border-white"
                  src="https://images.unsplash.com/photo-1599566150163-29194dcaad36?auto=format&fit=crop&q=80&w=100"
                  alt="User"
                />
              </div>
              <span>1000+ người dùng đã tham gia</span>
            </div>
          </div>
          <div className="lg:w-1/2">
            <div className="relative">
              <div className="absolute inset-0 bg-gradient-to-r from-purple-600 to-pink-500 rounded-xl blur-2xl opacity-30"></div>
              <img
                src="https://images.unsplash.com/photo-1626544827763-d516dce335e2?auto=format&fit=crop&q=80&w=800"
                alt="CollabVideo Interface"
                className="relative rounded-xl shadow-2xl animate-float"
              />
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
