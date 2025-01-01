import { motion } from 'framer-motion';
import { FeatureCard } from './features/FeatureCard';
import { SectionTitle } from './features/SectionTitle';
import { features } from './features/featuresData';

export function Features() {
  return (
    <section className="py-24 bg-gradient-to-b from-gray-50 to-white">
      <div className="container mx-auto px-6">
        <SectionTitle 
          title="Tính Năng"
          highlight="Nổi Bật"
          description="Trải nghiệm những công nghệ tiên tiến nhất trong lĩnh vực biên tập video"
        />
        
        <motion.div 
          className="grid md:grid-cols-2 lg:grid-cols-4 gap-8"
          initial={{ opacity: 0 }}
          animate={{ opacity: 1 }}
          transition={{ duration: 0.5, staggerChildren: 0.1 }}
        >
          {features.map((feature, index) => (
            <FeatureCard 
              key={index}
              {...feature}
              index={index}
            />
          ))}
        </motion.div>
      </div>
    </section>
  );
}