select t.number as task, MAX(s.module) as module from tasks t
inner join tasks_to_subjects ts on ts.task_id = t.id
inner join subjects s on ts.subject_id = s.id group by t.number;