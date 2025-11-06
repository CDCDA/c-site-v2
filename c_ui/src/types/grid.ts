import i18n from '@/locales/i18n';
// types/grid.ts
export interface GridCell {
  id: string; // 唯一标识 (如 "r1-c2")
  row: number; // 起始行
  col: number; // 起始列
  rowspan: number; // 合并行数
  colspan: number; // 合并列数
  merged?: boolean; // 是否被合并
  component?: string; // 后续填充的组件类型
}

export type GridLayout = GridCell[][];
