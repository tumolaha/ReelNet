// Hàm helper để loại bỏ các giá trị không thể serialize
export function makeSerializable(obj: any): any {
  if (obj === null || obj === undefined) {
    return obj;
  }

  // Xử lý đối tượng Date
  if (obj instanceof Date) {
    return obj.toISOString();
  }

  // Xử lý hàm
  if (typeof obj === "function") {
    return "[Function]";
  }

  if (typeof obj !== "object") {
    return obj;
  }

  if (Array.isArray(obj)) {
    return obj.map(makeSerializable);
  }

  // Xử lý đối tượng Error
  if (obj instanceof Error) {
    return {
      name: obj.name,
      message: obj.message,
      stack: obj.stack,
    };
  }

  return Object.fromEntries(
    Object.entries(obj).map(([k, v]) => [k, makeSerializable(v)])
  );
}
