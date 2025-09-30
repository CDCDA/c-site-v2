<template>
  <div style="width: 100%; height: 100%">
    <el-form :model="queryParams" :inline="true">
      <el-form-item label="分组查询">
        <el-select v-model="queryParams.group" clearable>
          <el-option value="x" label="x"></el-option>
          <el-option value="y" label="y"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button @click="ganttFilter()">查询</el-button>
        <el-button @click="handleImport()">导入</el-button>
        <el-button @click="handleExport()">导出</el-button>
        <el-button @click="handleGroup()">分组</el-button>
        <el-button @click="handleResetGroup()">还原分组</el-button>
        <el-button @click="sortByDuration()">按工时排序</el-button>
        <el-button @click="sortDefault()">默认排序</el-button>
        <!-- <el-button @click="sortByPlanQty()">按计划数量排序</el-button> -->
        <el-button @click="handleReadOnly()">{{
          this.readOnly == false ? '只读' : '编辑'
        }}</el-button>
        <el-button @click="submit()">保存</el-button>
      </el-form-item>
    </el-form>
    <div
      id="gantt"
      style="width: 100%; height: calc(100% - 42px); border-radius: 8px; overflow: hidden"
    ></div>
    <c-dialog :title="title" v-model="open" width="450" :modal="true">
      <el-upload :http-request="loadExcel" action=" " :limit="1">
        <el-button type="primary">选择文件</el-button>
        <template #tip>
          <div class="el-upload__tip">只能上传xlsx文件</div>
        </template>
      </el-upload>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="open = false">取消</el-button>
        </span>
      </template>
    </c-dialog>
  </div>
</template>

<script>
import { gantt } from 'dhtmlx-gantt';
import 'dhtmlx-gantt/codebase/dhtmlxgantt.css';

var params = { group: null };
export default {
  name: 'gantt',
  props: {
    tasks: {
      type: Object,
      default() {
        return { data: [], links: [] };
      }
    }
  },
  data() {
    return {
      readOnly: false,
      open: false,
      queryParams: {
        group: null
      },
      selectIds: [],
      sourceTaskTop: null,
      sourceId: null,
      target: null,
      targetParent: null,
      columns: [
        { name: 'text', label: '名称', tree: true, width: '200', align: 'center', resize: true },
        {
          name: 'duration',
          label: '工时',
          align: 'center',
          template: function (obj) {
            return obj.duration + '小时';
          }
        },
        {
          name: 'progress',
          label: '进度',
          align: 'center',
          template: function (obj) {
            return (obj.progress * 100).toFixed(2) + '%';
          }
        },
        {
          name: 'planQty',
          label: '计划数量',
          align: 'center',
          template: function (obj) {
            return obj.planQty;
          }
        }
      ],
      lightBoxSections: [
        { name: 'description', height: 38, map_to: 'text', type: 'textarea', focus: true },
        {
          name: 'priority',
          height: 22,
          map_to: 'priority',
          type: 'select',
          options: [
            { key: 1, label: 'High' },
            { key: 2, label: 'Normal' },
            { key: 3, label: 'Low' }
          ]
        },
        { name: 'progress', type: 'textarea', map_to: 'progress', height: 20 },
        { name: 'time', type: 'duration', map_to: 'auto' },
        { name: 'planQty', type: 'textarea', map_to: 'planQty', height: 20 }
      ],
      ganttData: [
        {
          id: 11,
          text: '工段 A', //名称
          type: 'project', //类型
          progress: 0.6, //进度
          open: true, //是否展开
          start_date: '02-04-2023 0 0:00', //时间
          duration: 13, //工期
          planQty: 60, //计划数量
          group: 'y', //分组
          parent: 0 //父级id
        },
        // {
        //   id: 12,
        //   text: '产线 A-1',
        //   type: 'project',
        //   start_date: '02-04-2023 0 0:00',
        //   duration: 5,
        //   parent: '11',
        //   progress: 0.3,
        //   planQty: 30,
        //   group: 'y',
        //   open: true
        // },
        {
          id: 13,
          text: '产线 A-2',
          start_date: '02-04-2023 0 0:00',
          type: 'project',
          render: 'split',
          parent: '11',
          progress: 0.3,
          planQty: 40,
          group: 'y',
          open: true,
          duration: 5
        },
        {
          id: 17,
          text: '任务 A-2-1',
          start_date: '02-04-2023 0 0:00',
          duration: 1,
          group: 'y',
          parent: '13',
          progress: 0.2,
          planQty: 10,
          group: 'y',
          open: true
        },
        {
          id: 18,
          text: '任务 A-2-2',
          start_date: '02-04-2023 01:00',
          duration: 2,
          parent: '13',
          progress: 0.5,
          planQty: 10,
          group: 'y',
          open: true
        },
        {
          id: 19,
          text: '任务 A-2-3',
          start_date: '02-04-2023 03:00',
          duration: 1,
          parent: '13',
          progress: 0.9,
          planQty: 10,
          group: 'y',
          open: true
        },
        {
          id: 20,
          text: '任务 A-2-4',
          start_date: '02-04-2023 04:00',
          duration: 2,
          parent: '13',
          progress: 0,
          planQty: 10,
          group: 'y',
          open: true
        },
        {
          id: 14,
          text: '产线 A-3',
          type: 'project',
          start_date: '02-04-2023 03:00',
          duration: 6,
          parent: '11',
          progress: 0,
          planQty: 10,
          group: 'x',
          open: true
        },
        {
          id: 31,
          text: '任务 A-3-1',
          start_date: '02-04-2023 09:00',
          duration: 1,
          group: 'y',
          parent: '14',
          progress: 0.2,
          planQty: 10,
          group: 'y',
          open: true
        },
        {
          id: 15,
          text: '产线 A-4',
          type: 'project',
          render: 'split',
          parent: '11',
          progress: 0,
          planQty: 10,
          group: 'x',
          open: true,
          start_date: '02-04-2023 04:00',
          duration: 11
        },
        {
          id: 21,
          text: '任务 A-4-1',
          start_date: '02-04-2023 04:00',
          duration: 4,
          parent: '15',
          progress: 0,
          planQty: 10,
          group: 'x',
          open: true
        },
        {
          id: 22,
          text: '任务 A-4-2',
          start_date: '02-04-2023 05:00',
          duration: 3,
          parent: '15',
          progress: 0,
          planQty: 10,
          group: 'x',
          open: true
        },
        {
          id: 23,
          text: '任务 A-4-3',
          start_date: '02-04-2023 09:00',
          duration: 3,
          type: 'milestone',
          parent: '15',
          progress: 0,
          planQty: 10,
          group: 'x',
          open: true,
          duration: 2
        },
        {
          id: 16,
          text: '任务 A-4-4',
          start_date: '02-04-2023 0 0:00',
          duration: 1,
          type: 'milestone',
          parent: '15',
          progress: 0,
          planQty: 10,
          group: 'x',
          open: true,
          duration: 0
        },
        {
          id: 101,
          text: '工段 B',

          type: 'project',
          progress: 0,
          open: true,
          start_date: '02-04-2023 0 0:00',
          duration: 10,
          planQty: 10,
          group: 'x',
          parent: 0
        },
        {
          id: 102,
          text: '产线 B-1',

          type: 'project',
          progress: 0,
          open: true,
          start_date: '02-04-2023 0 0:00',
          duration: 6,
          planQty: 4,
          group: 'x',
          parent: 101
        },
        {
          id: 103,
          text: '任务 B-1-1',

          progress: 0,
          open: true,
          start_date: '02-04-2023 0 0:00',
          duration: 2,
          planQty: 4,
          group: 'x',
          parent: 102
        },
        {
          id: 104,
          text: '任务 B-1-2',

          progress: 0,
          open: true,
          start_date: '02-04-2023 0 0:00',
          duration: 1,
          planQty: 4,
          group: 'x',
          parent: 102
        }
      ],
      ganttLinks: [
        { id: '1', source: '17', target: '18', type: '0' },
        { id: '2', source: '18', target: '19', type: '0' },
        { id: '3', source: '19', target: '20', type: '0' },
        { id: '4', source: '21', target: '22', type: '0' },
        { id: '5', source: '22', target: '23', type: '0' }
      ],
      labels: {
        planQty: '计划数量',
        dhx_cal_today_button: '今天',
        day_tab: '日',
        week_tab: '周',
        month_tab: '月',
        new_event: '新建日程',
        icon_save: '保存',
        icon_cancel: '关闭',
        icon_details: '详细',
        icon_edit: '编辑',
        icon_delete: '删除',
        confirm_closing: '请确认是否撤销修改!', //Your changes will be lost, are your sure?
        confirm_deleting: '是否删除计划?',
        section_description: '描述:',
        section_planQty: '计划数量:',
        section_progress: '进度',
        section_time: '时间范围:',
        section_type: '类型:',
        section_text: '计划名称:',
        section_test: '测试:',
        section_projectClass: '项目类型:',
        taskProjectType_0: '项目任务',
        taskProjectType_1: '普通任务',
        section_head: '负责人:',
        section_priority: '优先级:',
        taskProgress: '任务状态',
        taskProgress_0: '未开始',
        taskProgress_1: '进行中',
        taskProgress_2: '已完成',
        taskProgress_3: '已延期',
        taskProgress_4: '搁置中',
        section_template: 'Details',
        /* grid columns */
        column_text: '计划名称',
        column_start_date: '开始时间',
        column_duration: '持续时间',
        column_add: '',
        column_priority: '难度',
        /* link confirmation */
        link: '关联',
        confirm_link_deleting: '将被删除',
        message_ok: '确定',
        message_cancel: '取消',
        link_start: ' (开始)',
        link_end: ' (结束)',

        type_task: '任务',
        type_project: '项目',
        type_milestone: '里程碑',
        minutes: '分钟',
        hours: '小时',
        days: '天',
        weeks: '周',
        months: '月',
        years: '年'
      },
      tempGanttData: []
    };
  },

  methods: {
    submit() {
      console.log('AAA', gantt.getLinks());
    },
    //还原分组
    handleResetGroup() {
      if (this.tempGanttData.length != 0) {
        gantt.clearAll();
        this.ganttData = [];
        this.tempGanttData.forEach(x => {
          x.start_date = new Date(x.start_date);
          x.end_date = new Date(x.end_date);
          x.parent = x.sourceParent;
          if (!x.isGroupSource) {
            this.ganttData.push(x);
          }
        });
        gantt.parse({ data: this.ganttData, links: this.ganttLinks });
      }
    },
    //随机id
    generateRandomId(length) {
      var characters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
      var id = '';
      for (var i = 0; i < length; i++) {
        id += characters.charAt(Math.floor(Math.random() * characters.length));
      }
      return id;
    },
    //分组
    handleGroup() {
      let groupSourceMap = new Map();
      this.tempGanttData = [];
      this.ganttData.forEach(x => {
        if (!groupSourceMap.get(x.group)) {
          groupSourceMap.set(x.group, {
            id: this.generateRandomId(8),
            text: x.group,
            type: 'project',

            progress: 0,
            open: true,
            start_date: x.start_date,
            duration: 2,
            parent: 0,
            isGroupSource: true
          });
          this.tempGanttData.push(groupSourceMap.get(x.group));
          x.sourceParent = x.parent;
          x.parent = groupSourceMap.get(x.group).id;
          this.tempGanttData.push(x);
        } else {
          x.sourceParent = x.parent;
          x.parent = groupSourceMap.get(x.group).id;
          this.tempGanttData.push(x);
        }
      });
      gantt.clearAll();
      gantt.parse({ data: this.tempGanttData, links: this.ganttLinks });
    },
    sortByDuration() {
      this.tempGanttData = JSON.parse(JSON.stringify(this.ganttData));
      gantt.sort('duration', true);
    },
    sortDefault() {
      gantt.clearAll();
      this.ganttData = [];
      this.tempGanttData.forEach(x => {
        x.start_date = new Date(x.start_date);
        x.end_date = new Date(x.end_date);
        this.ganttData.push(x);
        // x.parent = x.sourceParent;
        // if (!x.isGroupSource) {
        //   this.ganttData.push(x);
        // }
      });
      gantt.parse({ data: this.ganttData, links: this.ganttLinks });
    },
    sortByPlanQty() {
      gantt.sort('planQty', true);
    },
    //只读
    handleReadOnly() {
      this.readOnly = !this.readOnly;
      gantt.config.readonly = this.readOnly;
    },
    //导入excel
    loadExcel(data) {
      gantt.importFromExcel({
        data: data.file,
        callback: async function (project) {
          if (project) {
            await project.forEach(x => {
              let ymdParts = x.start_date.split(' ')[0].split('-');
              let hsParts = x.start_date.split(' ')[1].split(':');
              x.start_date =
                ymdParts[2] +
                '-' +
                ymdParts[1] +
                '-' +
                ymdParts[0] +
                ' ' +
                hsParts[0] +
                ':' +
                hsParts[1];
            });
            gantt.clearAll();
            gantt.parse({ data: project });
          }
        }
      });
    },
    //打开导出弹窗
    handleImport() {
      this.open = true;
    },
    //导出excel
    handleExport() {
      console.log(this.ganttData);
      gantt.exportToExcel({
        name: `gantt_${new Date().getTime()}.xlsx`,
        columns: [
          { id: 'id', header: 'id', width: 25, type: 'number' },
          { id: 'parent', header: 'parent', width: 25, type: 'number' },
          { id: 'text', header: 'text', width: 25 },
          { id: 'duration', header: 'duration', width: 25, type: 'number' },
          { id: 'planQty', header: 'planQty', width: 25, type: 'number' },
          { id: 'type', header: 'type', width: 25 },
          { id: 'start_date', header: 'start_date', width: 25, type: 'date' }
        ]
      });
    },
    // 过滤
    ganttFilter() {
      params = this.queryParams;
      gantt.refreshData();
    },
    //任务拆分
    onDragEnd(startPoint, endPoint, startDate, endDate, tasksBetweenDates, tasksInRow) {
      if (tasksInRow.length === 1) {
        var currentTask = tasksInRow[0];
        if (currentTask.type === 'project') {
          currentTask.render = 'split';
          gantt.addTask(
            {
              text: 'Subtask of ' + currentTask.text,
              start_date: gantt.roundDate(startDate),
              end_date: gantt.roundDate(endDate)
            },
            currentTask.id
          );
        } else {
          let sourceStartDate = currentTask.start_date;
          let sourceEndDate = currentTask.end_date;
          let newStartDate = gantt.roundDate(startDate);
          let newEndDate = gantt.roundDate(endDate);
          let duration = 0;
          if (sourceStartDate.getTime() <= newStartDate.getTime()) {
            duration = gantt.calculateDuration({
              start_date: sourceStartDate,
              end_date: newStartDate
            });
            if (duration > 0) {
              if (newEndDate.getTime() == sourceEndDate.getTime()) {
                var nextTask = gantt.addTask(
                  {
                    text: currentTask.text + '-拆分任务',
                    start_date: newStartDate,
                    end_date: newEndDate
                  },
                  currentTask.parent
                );
                gantt.moveTask(nextTask, currentTask.$local_index + 1, currentTask.parent);
                currentTask.end_date = newStartDate;
              } else {
                var preTask = gantt.addTask(
                  {
                    text: currentTask.text + '-拆分任务',
                    start_date: sourceStartDate,
                    end_date: newStartDate
                  },
                  currentTask.parent
                );
                gantt.moveTask(preTask, currentTask.$local_index, currentTask.parent);
                let endDuration = gantt.calculateDuration({
                  start_date: newEndDate,
                  end_date: sourceEndDate
                });
                if (endDuration > 0) {
                  var newTask = gantt.addTask(
                    {
                      text: currentTask.text + '-拆分任务',
                      start_date: newStartDate,
                      end_date: newEndDate
                    },
                    currentTask.parent
                  );
                  gantt.moveTask(newTask, currentTask.$local_index, currentTask.parent);
                  currentTask.start_date = gantt.roundDate(endDate);
                } else {
                  currentTask.start_date = newStartDate;
                }
              }
            } else if (duration == 0) {
              var newTask = gantt.addTask(
                {
                  text: currentTask.text + '-拆分任务',
                  start_date: newStartDate,
                  end_date: newEndDate
                },
                currentTask.parent
              );
              gantt.moveTask(newTask, currentTask.$local_index, currentTask.parent);
              currentTask.start_date = gantt.roundDate(endDate);
            }
          }
          gantt.updateTask(currentTask.id);
        }
      } else if (tasksInRow.length === 0) {
        gantt.createTask({
          text: 'New task',
          start_date: gantt.roundDate(startDate),
          end_date: gantt.roundDate(endDate)
        });
      }
    }
  },

  //将初始化方法移动到methods里也会报错
  mounted() {
    gantt.plugins({
      click_drag: true,
      multiselect: true,
      undo: true,
      export_api: true
    });
    gantt.config.order_branch = true; // 启用任务条垂直拖拽
    gantt.config.drag_project = true; //整体拖拽
    gantt.config.drag_move = true; // 允许任务条拖动
    gantt.config.lightbox.sections = this.lightBoxSections; // 编辑弹窗设置
    gantt.config.open_split_tasks = true; //通过单击 +/- 按钮启用展开/折叠拆分任务的可能性
    gantt.config.multiselect = true; //多选
    gantt.config.click_drag = {
      callback: this.onDragEnd
    };
    gantt.config.date_format = '%d-%m-%Y %H:%i';
    gantt.config.min_column_width = 20;
    gantt.config.duration_unit = 'minute';
    gantt.config.duration_step = 60;
    gantt.config.scale_height = 75;
    gantt.config.readonly = this.readOnly; //只读
    gantt.locale.date = {
      //时间显示修改
      month_full: [
        '1月',
        '2月',
        '3月',
        '4月',
        '5月',
        '6月',
        '7月',
        '8月',
        '9月',
        '10月',
        '11月',
        '12月'
      ],
      month_short: [
        '1月',
        '2月',
        '3月',
        '4月',
        '5月',
        '6月',
        '7月',
        '8月',
        '9月',
        '10月',
        '11月',
        '12月'
      ],
      day_full: ['星期日', '星期一', '星期二', '星期三', '星期四', '星期五', '星期六'],
      day_short: ['星期日', '星期一', '星期二', '星期三', '星期四', '星期五', '星期六']
    };
    // gantt.config.date_format = '%Y-%m-%d %H:%i';
    gantt.config.scales = [
      { unit: 'hour', step: 1, format: '%g %a' },
      { unit: 'day', step: 1, format: '%j %F, %l' },
      { unit: 'minute', step: 10, format: '%i' }
    ];
    gantt.locale.labels = this.labels;
    gantt.config.columns = this.columns;
    // gantt.config.duration_unit = 'hour';
    // gantt.config.duration_step = 1;
    // gantt.config.scale_unit = 'hour'; //单元格时间单位
    // gantt.config.step = 1; //单元格时间长度
    // gantt.config.date_scale = '%H:%i'; // 设置日期格式为小时:分钟
    // gantt.config.multiselect_one_level = true;
    gantt.templates.task_class =
      gantt.templates.grid_row_class =
      gantt.templates.task_row_class =
        function (start, end, task) {
          if (gantt.isSelectedTask(task.id)) return 'gantt_selected';
        };
    //拖拽事件
    gantt.attachEvent('onAfterTaskMove', async function (id, parent, tindex) {
      this.target = tindex;
      this.targetParent = parent;
      this.sourceId = id;
      // this.sourceTaskTop = gantt.getTaskTop(id);
      return true;
    });
    //树状图任务拖拽结束事件（将方法单独放到metheds里会报错）
    gantt.attachEvent('onRowDragEnd', function (id, parent, tindex) {
      // console.log("数据",this.ganttData)
      let selectedTaskIds = this.selectIds;
      function sortBroTask(sTask) {
        let parentTask = gantt.getTask(gantt.getParent(sTask.id));
        //排列同级兄弟的时间跨度
        let broIds = gantt.getSiblings(sTask.id);
        broIds.forEach((tId, i) => {
          let task = gantt.getTask(tId);
          if (i == 0) {
            task.start_date = parentTask.start_date;
          } else {
            let preTask = gantt.getTask(broIds[i - 1]);
            task.start_date = preTask.end_date;
          }
          task.end_date = gantt.calculateEndDate(task.start_date, task.duration);
        });
      }
      //单选不执行多选事件
      if (!selectedTaskIds || selectedTaskIds.length <= 1) {
        sortBroTask({ id });
        return true;
      }
      if (!selectedTaskIds.find(x => x == id)) {
        sortBroTask({ id });
        return true;
      }
      let sTask = gantt.getTask(selectedTaskIds[0]);
      //不是第三级的批量拖拽，不执行
      if (sTask.$level != 2) {
        sortBroTask({ id });
        return true;
      }
      //本次拖拽是否在同一个父级任务下进行
      let isSameParent = true;
      let isSameLevel = true;
      let level = null;
      gantt.eachSelectedTask(id => {
        let task = gantt.getTask(id);
        if (task.parent != this.targetParent) {
          isSameParent = false;
        }
        if (level == null) {
          level = task.$level;
        } else if (level != task.$level) {
          isSameLevel = false;
        }
      });
      //非同级多选不执行
      if (!isSameLevel) {
        sortBroTask({ id });
        return true;
      }
      // console.log('是否相同父级', isSameParent);
      if (isSameParent) {
        let sourceTask = gantt.getTask(id);
        let index = null;
        let afterLength = 0;
        selectedTaskIds.forEach((nid, i) => {
          if (nid == id) {
            index = i;
          }
          let task = gantt.getTask(nid);
          // console.log('任务', sourceTask.$local_index);
          //该任务在当前任务之前
          if (index == null || i < index) {
            if (task.$local_index < sourceTask.$local_index) {
              let targetIndex = sourceTask.$local_index - 1;
              // console.log('任务前', targetIndex);
              gantt.moveTask(nid, targetIndex, this.targetParent);
            } else {
              // console.log('任务前', sourceTask.$local_index);
              gantt.moveTask(nid, sourceTask.$local_index, this.targetParent);
            }
          } else if (i > index) {
            //该任务在当前任务之后

            if (task.$local_index < sourceTask.$local_index) {
              // console.log('任务前', sourceTask.$local_index + afterLength, afterLength);
              gantt.moveTask(nid, sourceTask.$local_index + afterLength, this.targetParent);
              afterLength++;
            } else {
              let targetIndex = sourceTask.$local_index + afterLength + 1;
              // console.log('任务前', targetIndex, afterLength);
              gantt.moveTask(nid, targetIndex, this.targetParent);
              afterLength++;
            }
          }
        });
      } else {
        //修改拖拽插入的任务顺序;
        sTask = JSON.parse(JSON.stringify(sTask));
        selectedTaskIds.forEach((nid, i) => {
          let task = gantt.getTask(nid);
          if (i !== 0) {
            task.$local_index = sTask.$local_index + i;
            gantt.moveTask(nid, task.$local_index, this.targetParent);
          }
        });
      }

      sortBroTask(sTask);
      // let finalTasks = [];
      // gantt.eachTask(function (task) {
      //   finalTasks.push(task);
      // }, this.targetParent);
      // console.log('最终任务排序', finalTasks);

      return true;
    });
    //弹窗保存事件
    gantt.attachEvent('onLightboxSave', function (id, task, is_new) {
      return true;
    });
    //多选选中事件
    gantt.attachEvent('onTaskMultiSelect', function (id, state, e) {
      if (!this.selectIds) {
        this.selectIds = [];
      }
      if (state) {
        this.selectIds.push(id);
      } else {
        this.selectIds.splice(this.selectIds.indexOf(id), 1);
      }
      return true;
    });
    //过滤事件
    function hasGroup(parent, group) {
      if (gantt.getTask(parent).group == group) return true;

      var child = gantt.getChildren(parent);
      for (var i = 0; i < child.length; i++) {
        if (hasGroup(child[i], group)) return true;
      }
      return false;
    }
    //任务展示前事件
    gantt.attachEvent('onBeforeTaskDisplay', function (id, task) {
      if (params && params.group) {
        if (hasGroup(id, params.group)) {
          return true;
        }
      } else {
        return true;
      }
      return false;
    });
    //计算父级任务进度，数量等
    function calculateTaskData(task) {
      if (task.type != gantt.config.types.project) return task.progress;
      var totalToDo = 0;
      var totalDone = 0;
      gantt.eachTask(function (child) {
        if (child.type != gantt.config.types.project) {
          totalToDo += child.duration;
          totalDone += (child.progress || 0) * child.duration;
        }
      }, task.id);
      if (!totalToDo) return 0;
      else return (totalDone / totalToDo).toFixed(2);
    }
    //刷新任务进度，数量等
    function refreshTaskData(id, submit) {
      if (!gantt.isTaskExists(id)) return;

      var task = gantt.getTask(id);
      var newProgress = calculateTaskData(task);

      if (newProgress !== task.progress) {
        task.progress = newProgress;

        if (!submit) {
          gantt.refreshTask(id);
        } else {
          gantt.updateTask(id);
        }
      }

      if (!submit && gantt.getParent(id) !== gantt.config.root_id) {
        refreshTaskData(gantt.getParent(id), submit);
      }
    }
    //数据加载单未渲染时的事件
    gantt.attachEvent('onParse', function () {
      gantt.eachTask(function (task) {
        task.progress = calculateTaskData(task);
      });
    });
    //任务更新后事件
    gantt.attachEvent('onAfterTaskUpdate', function (id) {
      refreshTaskData(gantt.getParent(id), true);
    });
    //任务拖拽后事件
    gantt.attachEvent('onTaskDrag', function (id) {
      refreshTaskData(gantt.getParent(id), false);
    });
    //新增任务后事件
    gantt.attachEvent('onAfterTaskAdd', function (id) {
      refreshTaskData(gantt.getParent(id), true);
    });
    (function () {
      var idParentBeforeDeleteTask = 0;
      gantt.attachEvent('onBeforeTaskDelete', function (id) {
        idParentBeforeDeleteTask = gantt.getParent(id);
      });
      gantt.attachEvent('onAfterTaskDelete', function () {
        refreshTaskData(idParentBeforeDeleteTask, true);
      });
    })();

    gantt.init('gantt');
    gantt.parse({
      data: this.ganttData,
      links: this.ganttLinks
    });
    (function () {
      function shiftTask(task_id, direction) {
        var task = gantt.getTask(task_id);
        task.start_date = gantt.date.add(task.start_date, direction, 'day');
        task.end_date = gantt.calculateEndDate(task.start_date, task.duration);
        gantt.updateTask(task.id);
      }

      var actions = {
        undo: function () {
          gantt.ext.undo.undo();
        },
        redo: function () {
          gantt.ext.undo.redo();
        },
        indent: function indent(task_id) {
          var prev_id = gantt.getPrevSibling(task_id);
          while (gantt.isSelectedTask(prev_id)) {
            var prev = gantt.getPrevSibling(prev_id);
            if (!prev) break;
            prev_id = prev;
          }
          if (prev_id) {
            var new_parent = gantt.getTask(prev_id);
            gantt.moveTask(task_id, gantt.getChildren(new_parent.id).length, new_parent.id);
            new_parent.type = gantt.config.types.project;
            new_parent.$open = true;
            gantt.updateTask(task_id);
            gantt.updateTask(new_parent.id);
            return task_id;
          }
          return null;
        },
        outdent: function outdent(task_id, initialIndexes, initialSiblings) {
          var cur_task = gantt.getTask(task_id);
          var old_parent = cur_task.parent;
          if (gantt.isTaskExists(old_parent) && old_parent != gantt.config.root_id) {
            var index = gantt.getTaskIndex(old_parent) + 1;
            var prevSibling = initialSiblings[task_id].first;

            if (gantt.isSelectedTask(prevSibling)) {
              index += initialIndexes[task_id] - initialIndexes[prevSibling];
            }
            gantt.moveTask(task_id, index, gantt.getParent(cur_task.parent));
            if (!gantt.hasChild(old_parent))
              gantt.getTask(old_parent).type = gantt.config.types.task;
            gantt.updateTask(task_id);
            gantt.updateTask(old_parent);
            return task_id;
          }
          return null;
        },
        del: function (task_id) {
          if (gantt.isTaskExists(task_id)) gantt.deleteTask(task_id);
          return task_id;
        },
        moveForward: function (task_id) {
          shiftTask(task_id, 1);
        },
        moveBackward: function (task_id) {
          shiftTask(task_id, -1);
        }
      };
      var cascadeAction = {
        indent: true,
        outdent: true,
        del: true
      };

      var singularAction = {
        undo: true,
        redo: true
      };

      gantt.performAction = function (actionName) {
        var action = actions[actionName];
        if (!action) return;

        if (singularAction[actionName]) {
          action();
          return;
        }

        gantt.batchUpdate(function () {
          // need to preserve order of items on indent/outdent,
          // remember order before changing anything:
          var indexes = {};
          var siblings = {};
          gantt.eachSelectedTask(function (task_id) {
            gantt.ext.undo.saveState(task_id, 'task');
            indexes[task_id] = gantt.getTaskIndex(task_id);
            siblings[task_id] = {
              first: null
            };

            var currentId = task_id;
            while (
              gantt.isTaskExists(gantt.getPrevSibling(currentId)) &&
              gantt.isSelectedTask(gantt.getPrevSibling(currentId))
            ) {
              currentId = gantt.getPrevSibling(currentId);
            }
            siblings[task_id].first = currentId;
          });

          var updated = {};
          gantt.eachSelectedTask(function (task_id) {
            if (cascadeAction[actionName]) {
              if (!updated[gantt.getParent(task_id)]) {
                var updated_id = action(task_id, indexes, siblings);

                updated[updated_id] = true;
              } else {
                updated[task_id] = true;
              }
            } else {
              action(task_id, indexes);
            }
          });
        });
      };
    })();

    var els = document.getElementsByClassName('action');
    for (var i = 0; i < els.length; i++) {
      els[i].onclick = function () {
        gantt.performAction(this.name);
      };
    }

    // gantt.templates.grid_row_class = gantt.templates.task_row_class = function (start, end, task) {
    //   if (task.isGroupSource) return 'summary-row';
    // };
    gantt.templates.task_class = function (start, end, task) {
      if (task.isGroupSource) return 'summary-bar';
    };
    //任务类名设置
    gantt.templates.task_class = function (start, end, task) {
      let className = '';
      if (task.type == gantt.config.types.project)
        className = className + 'hide_project_progress_drag ';
      if (task.isGroupSource) className = className + 'summary-bar ';
      if (task.progress == 0) {
        className = className + 'notStart';
      } else if (0.3 > task.progress > 0) {
        className = className + 'initial';
      } else if (0.8 >= task.progress >= 0.3) {
        className = className + 'inProgress';
      } else if (1 > task.progress > 0.8) {
        className = className + 'finalStage';
      } else if (task.progress == 1) {
        className = className + 'end';
      }
      return className;
    };
    // gantt.templates.progress_text = function (start, end, task) {
    //   return (
    //     "<span style='float:left;margin-left:10px;color:white;'>" +
    //     (task.progress * 100).toFixed(2) +
    //     '% </span>'
    //   );
    // };

    // gantt.templates.task_class = function (start, end, task) {
    //   if (task.type == gantt.config.types.project) return 'hide_project_progress_drag';
    // };
    //撤销
    document.addEventListener('keydown', function (event) {
      if (event.ctrlKey && event.key === 'z') {
        // 检测到用户按下 Ctrl + Z 组合键时，调用撤销函数
        gantt.undo();
      }
    });
    //重做
    document.addEventListener('keydown', function (event) {
      if (event.ctrlKey && event.key === 'y') {
        gantt.redo();
      }
    });
  }
};
</script>
<style lang="scss" scoped>
.summary-row,
.summary-row.odd {
  background-color: #eeeeee;
  font-weight: bold;
}

.gantt_grid div,
.gantt_data_area div {
  font-size: 12px;
}
</style>
<style lang="scss">
.container {
  height: 100% !important;
}

#gantt {
  .notStart .gantt_task_progress {
    background-color: #ebedf0;
  }
  .initial .gantt_task_progress {
    background-color: red;
  }
  .inProgress .gantt_task_progress {
    background-color: #3a8cd5;
  }
  .finalStage .gantt_task_progress {
    background-color: #d4673b;
  }
  .end .gantt_task_progress {
    background-color: #7ee048;
  }

  .summary-bar {
    // opacity: 0.4;
    background-color: #b7dcbb !important;
  }
}
</style>
