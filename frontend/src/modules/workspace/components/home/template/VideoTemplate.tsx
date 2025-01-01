import { useState } from "react";
import DetailTemplate from "./DetailTemplate";

export interface TemplateType {
  id: string;
  title: string;
  thumbnail: string;
}

const VideoTemplate = ({ template }: { template: TemplateType }) => {
  const [isOpen, setIsOpen] = useState(false);
  return (
    <>
      <div
        onClick={() => setIsOpen(true)}
        key={template.id}
        className="relative aspect-[3/4] rounded-xl overflow-hidden group hover:shadow-xl transition-all duration-300 hover:-translate-y-1"
      >
        <img
          src={template.thumbnail}
          alt={template.title}
          className="w-full h-full object-cover"
        />
        <div className="absolute inset-0 bg-gradient-to-t from-black/80 via-black/30 to-transparent opacity-0 group-hover:opacity-100 transition-opacity">
          <div className="absolute bottom-0 left-0 right-0 p-4">
            <h4 className="text-white text-base font-semibold mb-2">
              {template.title}
            </h4>
            <button className="w-full py-2 bg-white/20 text-white rounded-lg hover:bg-white/30 transition-colors backdrop-blur-sm font-medium">
              Sử dụng mẫu
            </button>
          </div>
        </div>
      </div>
      <DetailTemplate
        isOpen={isOpen}
        onClose={() => setIsOpen(false)}
        template={template}
      />
    </>
  );
};

export default VideoTemplate;
