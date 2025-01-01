import SwiperScroll from "@/shared/components/SwiperScroll";

const listFilter = [
  {
    id: 1,
    name: "Tất cả",
  },
  {
    id: 2,
    name: "Năm mới",
  },
  {
    id: 3,
    name: "Do biên tập lựa chọn",
  },
  {
    id: 4,
    name: "Kinh doanh",
  },
  {
    id: 5,
    name: "Tiktok",
  },
  {
    id: 6,
    name: "Lời bài hát",
  },
  {
    id: 7,
    name: "Phần kết thúc bài viết",
  },
];
const FilterTemplate = () => {
  //   const scrollRef = useRef<HTMLDivElement>(null);
  //   const [isScrollable, setIsScrollable] = useState(false);
  //   const { scrollXProgress } = useScroll({ container: scrollRef });
  //   const scaleX = useSpring(scrollXProgress, {
  //     stiffness: 100,
  //     damping: 30,
  //     restDelta: 0.001,
  //   });

  //   // Check if content is scrollable
  //   useEffect(() => {
  //     if (scrollRef.current) {
  //       const { scrollWidth, clientWidth } = scrollRef.current;
  //       setIsScrollable(scrollWidth > clientWidth);
  //     }
  //   }, []);
  return (
    <div className="w-full h-full  flex gap-2">
      <SwiperScroll>
        {listFilter.map((category) => (
          <button
            key={category.id}
            className="px-5 py-2 text-sm font-medium rounded-full bg-white dark:bg-gray-800 text-gray-700 dark:text-gray-300 hover:bg-gray-100 dark:hover:bg-gray-700 transition-colors shadow-sm"
          >
            {category.name}
          </button>
        ))}
      </SwiperScroll>
    </div>
  );
};

export default FilterTemplate;
