import { useDictStore } from '@/store/modules/dict.ts';
// 根据字典类型查询字典数据信息
import { getDictDataByDictType } from '@/api/system/dict/dictData';
import { ref, toRefs } from 'vue';
/**
 * 获取字典数据
 */
export function useDict(...args: any) {
  const res = ref({}) as any;
  return (() => {
    args.forEach((dictType: any) => {
      res.value[dictType] = [];
      //dictType字典名称
      const dicts = useDictStore().getDict(dictType);
      //如果dicts有值，说明仓库中已经存储，直接使用
      if (dicts) {
        res.value[dictType] = dicts;
      } else {
        //如果无值，代表仓库中还未存储，调用获取字典标签的接口，参数dictType代表要查询的字典名称
        getDictDataByDictType(dictType).then((resp: any) => {
          res.value[dictType] = resp.data.map((p: any) => ({
            label: p.dictLabel,
            value: p.dictValue,
            listClass: p.listClass,
            elTagType: p.listClass,
            elTagClass: p.cssClass
          }));
          useDictStore().setDict(dictType, res.value[dictType]);
        });
      }
    });
    return res.value;
  })();
}
