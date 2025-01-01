import { FiDownload, FiPlay, FiStar, FiClock } from "react-icons/fi";
import FilterTemplate from "./template/FillterTemplate";
import VideoTemplate, { TemplateType } from "./template/VideoTemplate";
// Data mẫu

const recentProjects = [
  {
    id: 1,
    title: "Video quảng cáo sản phẩm",
    thumbnail: "/path-to-thumbnail.jpg",
    lastModified: "2 giờ trước",
  },
  // Thêm các dự án khác...
];

const templates = [
  {
    id: 1,
    title: "Template kinh doanh",
    thumbnail: "/path-to-thumbnail.jpg",
  },
  // Thêm các template khác...
];
const ContainerTemplates = () => {
  return (
    <div className="container mx-auto px-6 py-8">
      {/* Recent projects */}
      <div className="mb-14 space-y-4">
        <div className="flex items-center justify-between mb-8">
          <h3 className="text-lg font-semibold text-gray-900 dark:text-white">
            Bắt đầu với mẫu
          </h3>
          <button className="text-blue-600 hover:text-blue-700 font-medium flex items-center gap-2">
            Xem tất cả
            <FiPlay className="text-sm" />
          </button>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-3 lg:grid-cols-4 gap-6">
          {recentProjects.map((project) => (
            <div
              key={project.id}
              className="group bg-white dark:bg-gray-800 rounded-xl overflow-hidden hover:shadow-xl transition-all duration-300 hover:-translate-y-1"
            >
              <div className="relative aspect-video bg-gray-200 dark:bg-gray-700">
                <img
                  src={project.thumbnail}
                  alt={project.title}
                  className="w-full h-full object-cover"
                />
                <div className="absolute inset-0 bg-gradient-to-t from-black/70 via-black/20 to-transparent opacity-0 group-hover:opacity-100 transition-opacity">
                  <div className="absolute bottom-0 left-0 right-0 p-4 flex items-center justify-between">
                    <button className="p-2.5 bg-white/20 rounded-full hover:bg-white/30 backdrop-blur-sm">
                      <FiPlay className="text-white text-lg" />
                    </button>
                    <div className="flex gap-2">
                      <button className="p-2.5 bg-white/20 rounded-full hover:bg-white/30 backdrop-blur-sm">
                        <FiStar className="text-white text-lg" />
                      </button>
                      <button className="p-2.5 bg-white/20 rounded-full hover:bg-white/30 backdrop-blur-sm">
                        <FiDownload className="text-white text-lg" />
                      </button>
                    </div>
                  </div>
                </div>
              </div>
              <div className="p-5">
                <h4 className="font-semibold text-gray-900 dark:text-white mb-2">
                  {project.title}
                </h4>
                <div className="flex items-center text-sm text-gray-500 dark:text-gray-400">
                  <FiClock className="mr-2" />
                  {project.lastModified}
                </div>
              </div>
            </div>
          ))}
        </div>
      </div>

      {/* Templates section */}
      <div className="space-y-4">
        <div className="flex items-center justify-between ">
          <h3 className="text-lg font-semibold text-gray-900 dark:text-white">
            Bắt đầu với mẫu
          </h3>
          <div></div>
        </div>
        <FilterTemplate />
        <div className="grid grid-cols-2 md:grid-cols-4 lg:grid-cols-6 gap-5">
          {templates.map((template) => (
            <VideoTemplate template={template as unknown as TemplateType} key={template.id} />
          ))}
        </div>
      </div>
    </div>
  );
};

export default ContainerTemplates;
