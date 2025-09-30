<!DOCTYPE html>
<html>
<head>
    <title>My Own Gantt Chart</title>
    <script src="assets/codebase/dhtmlxgantt.js" type="text/javascript" charset="utf-8"></script>
    <script src="assets/codebase/ext/dhtmlxgantt_grouping.js"type="text/javascript" charset="utf-8"></script>  
    <link href="assets/codebase/dhtmlxgantt.css" rel="stylesheet">
    
    <style type="text/css">
        html, body{ height:100%; padding:0px; margin:0px; overflow: hidden;}
    </style>
</head>
<body>
    <input type="button" id="default" onclick="showGroups()" value="Tree">
    <input type="button" id="priority" onclick="showGroups('priority')" value="Group by priority">
    <div class="gantt_wrapper panel" id="my_gantt" style="width:100%; height:700px;"></div>
   <script src="assets/plugins/jQuery/jQuery-2.1.4.min.js"></script>
    <script>
		gantt.config.xml_date = "%Y-%m-%d %H:%i";
		gantt.init("my_gantt");
		gantt.config.columns = [
			{name:"text",       label:"Task name",  width:"*", tree:true },
			{name:"start_date", label:"Start time", align: "center" ,  width:"*"},
			{name:"duration",   label:"Duration",   align: "center" ,  width:46},
//			{name:"priority",   label:"Priority",   align: "center",  width:55, template:function(obj){
//				if (obj.priority == "High"){ return "High"}
//				if (obj.priority == "Medium"){ return "Medium"}
//				return "Low"
//			} },
                        {name:"priority",width:80, align: "center", template: function(item){ return byId(gantt.serverList('priority'), item.priority) }},
			{name:"add",        label:"",           width:44 }
		];
		gantt.templates.task_class  = function(start, end, task){
		switch (task.priority){
			case "High":
				return "high";
				break;
			case "Medium":
				return "medium";
				break;
			case "Low":
				return "low";
				break;
		}
                };
                
                gantt.config.order_branch = true;
		gantt.config.grid_resize = true;
                
                gantt.locale.labels.column_priority =
                gantt.locale.labels.section_priority = "Priority";
        
                function byId(list, id){
                    for(var i = 0; i < list.length; i++){
                            if(list[i].key == id)
                                    return list[i].label || "";
                    }
                    return "";
		}
                
                gantt.templates.grid_row_class =
                gantt.templates.task_row_class = function(start, end, task){
                        if(task.$virtual)
                                return "summary-row"
		};
		gantt.templates.task_class=function(start, end, task){
			if(task.$virtual)
				return "summary-bar";
		};
                   
                 gantt.config.lightbox.sections = [
                    {name: "description", height: 38, map_to: "text", type: "textarea", focus: true},
                    {name: "employee", map_to: "Employee_IdEmployee", type: "select", options: gantt.serverList("employee")},
                    {name: "tasktype", map_to: "TaskType_Id", type: "select", options: gantt.serverList("tasktype")},
                    {name: "proyek", map_to: "Proyek_IdProyek", type: "select", options: gantt.serverList("proyek")},
                    {name: "time", height: 72, type: "duration", map_to: "auto"}
                ];
                
                gantt.locale.labels.section_employee = "Employee Name";
                gantt.locale.labels.section_tasktype = "Task Type Name";
                gantt.locale.labels.section_proyek = "Proyek Name";

//                gantt.attachEvent("onLightboxSave", function(id, task, is_new){
//                    var dateToStr = gantt.date.date_to_str("%d-%m-%Y");
//
//
//                 $.post("Control",{
//                       id:task.id,
//                       duration:task.duration,
//                       text:task.text,
//                       start_date:dateToStr(task.start_date),
//                       Employee_IdEmployee:task.employee
//                    },function(data){
//                       alert(data);
//                    }) ;
//                    return true;
//                 });
                
                function showGroups(listname){
                    if(listname){
                            gantt.groupBy({
                                    groups: gantt.serverList(listname),
                                    relation_property: listname,
                                    group_id: "key",
                                    group_text: "label"
                            });
                            gantt.sort("start_date");
                    }else{
                            gantt.groupBy(false);

                    }
		}
                
		gantt.load('data.php');
		var dp=new dataProcessor("data.php");   
		dp.init(gantt);
                
                
                
    </script>
</body>
</html>