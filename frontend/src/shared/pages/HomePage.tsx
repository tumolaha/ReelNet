import { Features } from "../components/home/Features";
import { Hero } from "../components/home/Hero";
import { Pricing } from "../components/home/Pricing";
import { Testimonials } from "../components/home/Testimonials";
import { Footer } from "../components/home/Footer";

const HomePage = () => {

  return (
    <div className="min-h-screen">
      <Hero />
      <Features />
      <Testimonials />
      <Pricing />
      <Footer />
    </div>
  );
};

export default HomePage;
