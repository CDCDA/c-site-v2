<?php
include ('assets/codebase/connector/gantt_connector.php');

$res=mysql_connect("localhost","root","");
mysql_select_db("projects");

$gantt = new JSONGanttConnector($res,$dbtype);
$list = new JSONOptionsConnector($res,$dbtype);
$list->render_complex_sql("SELECT IdEmployee as `value`, FirstName as `label` FROM Employee","IdEmployee","value,label");
$gantt->set_options("employee", $list);

$list2 = new JSONOptionsConnector($res,$dbtype);
$list2->render_complex_sql("SELECT Id as `value`, Name as `label` FROM TaskType","Id","value,label");
$gantt->set_options("tasktype", $list2);

$list3 = new JSONOptionsConnector($res,$dbtype);
$list3->render_complex_sql("SELECT IdProyek as `value`, ProyekName as `label` FROM Proyek","Id","value,label");
$gantt->set_options("proyek", $list3);

$gantt->render_links("gantt_links","id","source,target,type");
$gantt->render_table(
    "task",
    "id",
    "start_date,duration,text,progress,sortorder,parent,priority,Employee_IdEmployee,TaskType_Id,Proyek_IdProyek"
);
?>