import { zip } from 'zip-a-folder';
import fs from 'fs';
import path from 'path';

const distDir = path.join(process.cwd(), 'dist');
const zipPath = path.join(process.cwd(), `dist-${new Date().toISOString().slice(0, 10)}.zip`);

// 检查dist目录是否存在
if (!fs.existsSync(distDir)) {
  console.error('Error: dist directory not found. Please run build first.');
  process.exit(1);
}

console.log(`Creating zip file: ${zipPath}`);

// 执行压缩
zip(distDir, zipPath)
  .then(() => {
    console.log('✅ Zip file created successfully!');
    console.log(`Zip file path: ${zipPath}`);
  })
  .catch((error) => {
    console.error('❌ Error creating zip file:', error);
    process.exit(1);
  });
