import { motion, AnimatePresence } from "framer-motion";
import { FiX, FiClock, FiImage } from "react-icons/fi";
import ContainerTemplates from "../ContainerTemplates";

interface DetailTemplateModalProps {
  isOpen: boolean;
  onClose: () => void;
  template: {
    id: string;
    title: string;
    thumbnail: string;
    clips?: number;
    duration?: string;
    usage?: string;
    aspectRatio?: string;
  };
}

const DetailTemplateModal = ({
  isOpen,
  onClose,
  template,
}: DetailTemplateModalProps) => {
  return (
    <AnimatePresence>
      {isOpen && (
        <motion.div
          initial={{ opacity: 0 }}
          animate={{ opacity: 1 }}
          exit={{ opacity: 0 }}
          className="fixed inset-0 z-50 flex items-center justify-center bg-black/60 backdrop-blur-sm"
          onClick={onClose}
        >
          <motion.div
            initial={{ scale: 0.95 }}
            animate={{ scale: 1 }}
            exit={{ scale: 0.95 }}
            onClick={(e) => e.stopPropagation()}
            className="relative w-full max-w-7xl max-h-[90vh]  bg-white dark:bg-gray-900 rounded-xl overflow-x-hidden overflow-y-auto p-2"
          >
            {/* Close button */}
            <button
              onClick={onClose}
              className="absolute top-4 right-4 p-2 rounded-full bg-black/20 hover:bg-black/30 text-white transition-colors z-10"
            >
              <FiX size={20} />
            </button>

            {/* Video/Image Preview */}
            <div className="flex items-center gap-2 w-full">
              <div className="relative aspect-video flex-1">
                <img
                  src={template.thumbnail}
                  alt={template.title}
                  className="w-full h-full object-cover"
                />
                <div className="absolute inset-0 bg-gradient-to-t from-black/60 via-transparent to-transparent" />
              </div>
              {/* Content */}
              <div className="p-6 flex-1">
                {/* Title */}
                <h2 className="text-2xl font-semibold text-gray-900 dark:text-white mb-4">
                  {template.title}
                </h2>
                {/* Stats */}
                <div className="flex items-center gap-6 mb-6">
                  <div className="flex items-center gap-2 text-gray-600 dark:text-gray-400">
                    <FiImage size={18} />
                    <span>{template.clips} clip</span>
                  </div>
                  <div className="flex items-center gap-2 text-gray-600 dark:text-gray-400">
                    <FiClock size={18} />
                    <span>{template.duration}</span>
                  </div>
                  <div className="text-gray-600 dark:text-gray-400">
                    {template.usage} lượt sử dụng
                  </div>
                </div>
                {/* Aspect Ratio */}
                <div className="mb-6">
                  <div className="text-sm text-gray-600 dark:text-gray-400">
                    Tỷ lệ khung hình gốc: {template.aspectRatio}
                  </div>
                </div>
                {/* Action Button */}
                <button className="w-full py-3 px-4 bg-blue-500 hover:bg-blue-600 text-white font-medium rounded-lg transition-colors">
                  Dùng mẫu này
                </button>
              </div>
            </div>
            <ContainerTemplates />
          </motion.div>
        </motion.div>
      )}
    </AnimatePresence>
  );
};

export default DetailTemplateModal;
