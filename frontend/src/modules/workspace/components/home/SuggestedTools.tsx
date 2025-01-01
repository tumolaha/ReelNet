import React, { useRef, useState, useEffect } from "react";
import { FiArrowRight } from "react-icons/fi";
import {
  newVideoImage,
  oneClickImage,
  textToSpeechImage,
  videoShortsImage,
  voiceChangerImage,
} from "../../assets";
import { motion, useScroll, useSpring } from "framer-motion";
import SwiperScroll from "@/shared/components/SwiperScroll";

interface Tool {
  id: string;
  title: string;
  icon: string;
  isNew?: boolean;
  gradient?: string;
}

const tools: Tool[] = [
  {
    id: "new-video",
    title: "New video",
    icon: newVideoImage,
    gradient: "from-blue-400 to-blue-500",
  },
  {
    id: "text-to-speech",
    title: "Text to speech",
    icon: textToSpeechImage,
    isNew: true,
    gradient: "from-purple-400 to-purple-500",
  },
  {
    id: "voice-changer",
    title: "Voice changer",
    icon: voiceChangerImage,
    isNew: true,
    gradient: "from-indigo-400 to-indigo-500",
  },
  {
    id: "long-video-to-shorts",
    title: "Long video to shorts",
    icon: videoShortsImage,
    gradient: "from-orange-400 to-orange-500",
  },
  {
    id: "one-click-creation",
    title: "One-click video creation",
    icon: oneClickImage,
    gradient: "from-pink-400 to-pink-500",
  },
];

const SuggestedTools = () => {
  return (
    <div className="px-6">
      <div className="flex items-center justify-between mb-4">
        <h3 className="text-lg font-semibold text-gray-900 dark:text-white">
          Có thể bạn sẽ thích
        </h3>

        <div></div>
      </div>

      <div className="relative">
        <SwiperScroll>
          {tools.map((tool) => (
            <motion.div
              key={tool.id}
              className="flex-shrink-0 w-60 group cursor-pointer"
              whileHover={{ y: -4 }}
              transition={{ type: "spring", stiffness: 300 }}
            >
              <div
                className={`
                relative rounded-xl overflow-hidden bg-gradient-to-r ${tool.gradient}
                aspect-[4/2.5] p-4
              `}
              >
                {tool.isNew && (
                  <motion.span
                    className="absolute top-3 right-3 px-2 py-1 text-xs font-medium bg-red-500 text-white rounded-full"
                    initial={{ scale: 0 }}
                    animate={{ scale: 1 }}
                    transition={{ type: "spring", stiffness: 500 }}
                  >
                    New
                  </motion.span>
                )}

                <div className="absolute bottom-0 left-0 right-0 p-4">
                  <div className="flex items-center justify-between">
                    <h4 className="text-white font-medium">{tool.title}</h4>
                    <motion.div
                      className="w-8 h-8 flex items-center justify-center rounded-lg bg-white/20 backdrop-blur-sm"
                      whileHover={{ scale: 1.1 }}
                      whileTap={{ scale: 0.95 }}
                    >
                      <FiArrowRight className="text-white" />
                    </motion.div>
                  </div>
                </div>

                <motion.div
                  className="absolute top-4 left-4"
                  initial={{ opacity: 0, x: -20 }}
                  animate={{ opacity: 1, x: 0 }}
                  transition={{ duration: 0.3 }}
                >
                  <img
                    src={tool.icon}
                    alt={tool.title}
                    className="w-20 h-30 object-contain"
                  />
                </motion.div>
              </div>
            </motion.div>
          ))}
        </SwiperScroll>
      </div>
    </div>
  );
};

export default SuggestedTools;
