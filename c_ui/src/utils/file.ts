/*
 * @Description:文件相关方法
 */

// 遍历文件下的文件并返回[{fileName,src}]数组集合
export const traverseFolder = (folderPath: String, filePattern: String, fileSuffix: String) => {
  // 使用import.meta.glob方法获取文件集合
  const files = (import.meta as any).glob([`${folderPath}/${filePattern}`], { eager: true });
  // 定义一个空数组用于存储文件信息
  const utilFuns = [] as any;
  // 遍历文件集合
  Object.keys(files).forEach(name => {
    // 获取文件名
    const fileName = name.replace(`/\.\/|\.${fileSuffix}/g`, '');
    // 将文件名和文件内容添加到utilFuns数组中
    utilFuns.push({ fileName, src: files[name].default });
  });
  // 返回utilFuns数组
  return utilFuns;
};
